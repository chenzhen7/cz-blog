/* ==========================================================================
   3D 等高线地形 · 共享实现

   首页 hero 的「重力感应地形」与归档页页头的「时间地层」是同一片地形的两个标本：
   GLSL 着色器与 2D 兜底算法只在这里写一次，两页各自决定怎么驱动它
   （首页：指针 + 滚动 + 入场时间轴；归档：纯自动漂移）。

   原理与参数推导见 .claude/skills/blog-design/references/terrain-3d.md，
   改 shader 前先读它，里面的坑（fwidth 要开 derivatives、smoothstep 边界不能反、
   fbm 要拉动态范围）都是实测踩出来的。

   依赖：initGL 需要 window.THREE（three.js r128 UMD）；drawFlat 无依赖。
   用法：
     var gl = CzTerrain.initGL(glCanvas, container);   // 失败返回 null
     if (!gl) CzTerrain.drawFlat(flatCanvas, container); // 2D 静态兜底
   ========================================================================== */
window.CzTerrain = (function () {
    'use strict';

    var NOISE = [
        'float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123); }',
        'float vnoise(vec2 p) {',
        '  vec2 i = floor(p), f = fract(p);',
        '  vec2 u = f * f * (3.0 - 2.0 * f);',
        '  return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),',
        '             mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);',
        '}',
        'float fbm(vec2 p) {',
        '  float s = 0.0, a = 0.5;',
        '  for (int i = 0; i < 5; i++) { s += a * vnoise(p); p *= 2.03; a *= 0.5; }',
        '  return s;',
        '}',
        'float terrainH(vec2 p, float t) {',
        '  vec2 q = p * 0.00085;',
        // t 参与进来让噪声场随时间缓慢平移，等高线像活地图一样在爬，
        // 这是「活着的系统」这个观感的来源
        '  float n = fbm(q + vec2(t * 0.012, t * 0.008));',
        // (n - 0.5) * 2.6：fbm 的输出挤在 0.5 附近，不拉开动态范围的话
        // 等值线只会集中在地形中部几圈，撑不满整场
        '  return (n - 0.5) * 2.6;',
        '}'
    ].join('\n');

    var VERT = NOISE + '\n' + [
        'uniform float uTime;',
        'varying vec2  vXY;',
        'varying float vDepth;',
        'void main() {',
        '  vec3 p = position;',
        '  vXY = p.xy;',                                  // 未位移的平面坐标，传给片元
        '  float h = terrainH(p.xy, uTime);',
        '  p.z += h * 92.0;',                              // 沿平面法线抬升
        '  vec4 mv = modelViewMatrix * vec4(p, 1.0);',
        '  vDepth = -mv.z;',                               // 视深，用于远处淡出
        '  gl_Position = projectionMatrix * mv;',
        '}'
    ].join('\n');

    var FRAG = NOISE + '\n' + [
        'uniform float uTime;',
        'uniform vec3  uInk;',
        'uniform float uOpacity;',
        'varying vec2  vXY;',
        'varying float vDepth;',
        // 抗锯齿等高线：取到最近等值线的距离，用 fwidth 归一化
        'float contour(float h, float freq, float w) {',
        '  float v = h * freq;',
        '  float f = fract(v);',
        '  float d = min(f, 1.0 - f);',
        '  float aaw = fwidth(v) * w;',
        '  return 1.0 - smoothstep(0.0, aaw, d);',
        '}',
        'void main() {',
        '  float h = terrainH(vXY, uTime);',
        '  float main1 = contour(h, 26.0, 1.5);',          // 主等高线
        '  float minor = contour(h, 82.0, 1.0) * 0.30;',    // 次级等高线，更密更淡
        '  float line = max(main1, minor);',
        '  if (line <= 0.002) discard;',
        // 边界反转的 smoothstep（边0 > 边1）在部分 GPU 上属未定义行为，
        // 一律写成 1.0 - smoothstep(边0, 边1, x) 且保证 边0 < 边1
        '  float fog = 1.0 - smoothstep(620.0, 2400.0, vDepth);',        // 远处沉入纸面
        '  float rad = 1.0 - smoothstep(520.0, 1650.0, length(vXY));',   // 边缘淡出
        // 两重淡出缺一不可：只有一重的话地形边缘会留下生硬的一道截断
        '  gl_FragColor = vec4(uInk, line * fog * rad * uOpacity);',
        '}'
    ].join('\n');

    /* 初始化 WebGL 地形。成功返回 { terrain, renderer, scene, camera, resize }，失败返回 null。
       返回 null 不是「降级掩盖错误」：调用方据此切到 drawFlat 的 2D 等高线，
       那是设计内的第二条路径，地形照样在，只是没有纵深。 */
    function initGL(glCanvas, container) {
        if (!window.THREE || !glCanvas || !container) return null;
        var renderer, scene, camera, terrain;
        try {
            renderer = new THREE.WebGLRenderer({
                canvas: glCanvas, alpha: true, antialias: false, powerPreference: 'high-performance'
            });
            renderer.setClearColor(0x000000, 0);   // 画布透明，地形直接叠在纸面上
            // 像素比封顶 1.5：地形是柔和的，不需要满视网膜分辨率，
            // 而每个片元都要算一次 fbm，这是全页最贵的一项
            renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 1.5));

            scene = new THREE.Scene();
            camera = new THREE.PerspectiveCamera(40, 1, 1, 6000);
            camera.position.set(0, 0, 640);

            var mat = new THREE.ShaderMaterial({
                transparent: true,
                depthWrite: false,                  // 否则半透明的线互相遮挡会压出黑边
                extensions: { derivatives: true },  // WebGL1 下 fwidth 要靠这个开关
                uniforms: {
                    uTime:    { value: 0 },
                    uInk:     { value: new THREE.Color('#111111') },
                    uOpacity: { value: 0 }          // 从 0 起，浓度由调用方决定怎么拉起来
                },
                vertexShader: VERT,
                fragmentShader: FRAG
            });

            terrain = new THREE.Mesh(new THREE.PlaneGeometry(2800, 2800, 176, 176), mat);
            terrain.rotation.x = -1.16;             // 向后倾倒，得到俯视地形的视角
            terrain.position.set(0, -150, -300);
            scene.add(terrain);
        } catch (err) {
            // WebGL 拿不到（无 GPU、被策略禁用、上下文分配失败）
            return null;
        }

        function resize() {
            var w = container.clientWidth, h = container.clientHeight;
            // 第三个参数 false：只改画布的位图尺寸，不写 style。
            // 宽高由 CSS 的 100% 控制，写 style 反而会和版式打架
            renderer.setSize(w, h, false);
            camera.aspect = w / h;
            camera.updateProjectionMatrix();
        }

        resize();
        renderer.render(scene, camera);
        return { terrain: terrain, renderer: renderer, scene: scene, camera: camera, resize: resize };
    }

    /* 2D 兜底：拿不到 WebGL 或减少动效时，画一帧静态平面等高线（Marching Squares 网格插值） */
    function drawFlat(flatCanvas, container) {
        if (!flatCanvas || !container) return;
        flatCanvas.style.display = 'block';
        var ctx = flatCanvas.getContext('2d');
        var w = container.clientWidth, h = container.clientHeight;
        var dpr = Math.min(window.devicePixelRatio || 1, 2);
        flatCanvas.width = Math.floor(w * dpr);
        flatCanvas.height = Math.floor(h * dpr);
        ctx.setTransform(dpr, 0, 0, dpr, 0, 0);   // 按设备像素比缩放，线条才不会发虚
        ctx.clearRect(0, 0, w, h);

        // 与着色器里同名同形的噪声，保证两条路径画出来是同一片地形
        function hash(x, y) { var n = Math.sin(x * 127.1 + y * 311.7) * 43758.5453123; return n - Math.floor(n); }
        function sm(t) { return t * t * (3 - 2 * t); }
        function vn(x, y) {
            var xi = Math.floor(x), yi = Math.floor(y), xf = x - xi, yf = y - yi;
            var u = sm(xf), v = sm(yf);
            var a = hash(xi, yi), b = hash(xi + 1, yi), c = hash(xi, yi + 1), d = hash(xi + 1, yi + 1);
            return a * (1 - u) * (1 - v) + b * u * (1 - v) + c * (1 - u) * v + d * u * v;
        }
        function fbm(x, y) {
            var s = 0, amp = .5, f = 1;
            for (var i = 0; i < 4; i++) { s += amp * vn(x * f, y * f); f *= 2.03; amp *= .5; }
            return s;
        }

        var step = 13, cols = Math.ceil(w / step) + 1, rows = Math.ceil(h / step) + 1;
        var field = new Float32Array(cols * rows);
        var i, j, x, y;
        for (j = 0; j < rows; j++) {
            for (i = 0; i < cols; i++) field[j * cols + i] = fbm(i * step * .0026, j * step * .0026);
        }

        // ctx 与线段工具提到循环外：写在双重循环里的话，每个格子都要新建一个闭包
        function seg(p, q) { ctx.moveTo(p[0], p[1]); ctx.lineTo(q[0], q[1]); }

        ctx.strokeStyle = 'rgba(17,17,17,0.035)';
        ctx.lineWidth = 1;
        ctx.beginPath();
        for (var L = 0; L < 9; L++) {
            var iso = .26 + L * .052;    // 9 条等值线，从低到高等距排开
            for (j = 0; j < rows - 1; j++) {
                for (i = 0; i < cols - 1; i++) {
                    var tl = field[j * cols + i], tr = field[j * cols + i + 1];
                    var bl = field[(j + 1) * cols + i], br = field[(j + 1) * cols + i + 1];
                    // 四个角落在等值线的哪一侧，编码成一个 0..15 的格子类型
                    var idx = 0;
                    if (tl > iso) idx |= 8;
                    if (tr > iso) idx |= 4;
                    if (br > iso) idx |= 2;
                    if (bl > iso) idx |= 1;
                    // 0 与 15 是四角同侧，这一格没有线段穿过。其余 14 种在下面的
                    // switch 里逐一列出，不留 default —— 漏了哪种一眼能看出来
                    if (idx === 0 || idx === 15) continue;
                    x = i * step; y = j * step;
                    // 四条边与等值线的交点，按两端高度线性插值
                    var top = [x + step * ((iso - tl) / (tr - tl)), y];
                    var right = [x + step, y + step * ((iso - tr) / (br - tr))];
                    var bottom = [x + step * ((iso - bl) / (br - bl)), y + step];
                    var left = [x, y + step * ((iso - tl) / (bl - tl))];
                    switch (idx) {
                        case 1: case 14: seg(left, bottom); break;
                        case 2: case 13: seg(bottom, right); break;
                        case 3: case 12: seg(left, right); break;
                        case 4: case 11: seg(top, right); break;
                        case 5: seg(left, top); seg(bottom, right); break;
                        case 6: case 9: seg(top, bottom); break;
                        case 7: case 8: seg(left, top); break;
                        case 10: seg(top, right); seg(left, bottom); break;
                    }
                }
            }
        }
        ctx.stroke();
    }

    return { initGL: initGL, drawFlat: drawFlat };
})();
