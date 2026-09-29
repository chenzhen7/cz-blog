# 3D 等高线地形 · 实现与踩坑

首页 Hero 背景那张会随指针倾斜的等高线地形图。它是这套风格里最有记忆点的装置，对应《终末地》游戏主界面正中央那张"重力感应明显"的地形图。

**为什么值得上 Three.js**：早期版本把等高线做成了 2D 静态底纹，结果被评价为"少了灵魂"。压平这张图等于丢掉了整套风格的核心装置。

---

## 1. 核心思路

```
顶点着色器  →  按 fbm 噪声抬升网格，产生真实起伏
片元着色器  →  用同一个噪声按世界坐标独立算等高线
```

**关键点：等高线在片元里算，不在几何体上算。**

如果按顶点高度插值来画线，线宽会随网格密度变化、远处糊成一片。改成在片元里从 `vXY`（世界坐标）重新求值，等高线就与网格密度完全解耦，**永远锐利**，还能直接用 `fwidth` 做抗锯齿。

代价是每个片元都要算一次 fbm。因为描边像素稀疏但必须全屏求值，所以像素比要封顶（见 §4）。

---

## 2. 着色器源码

噪声函数三个着色器共用，拼在同一个字符串前面：

```glsl
float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123); }

float vnoise(vec2 p) {
  vec2 i = floor(p), f = fract(p);
  vec2 u = f * f * (3.0 - 2.0 * f);
  return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),
             mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);
}

float fbm(vec2 p) {
  float s = 0.0, a = 0.5;
  for (int i = 0; i < 5; i++) { s += a * vnoise(p); p *= 2.03; a *= 0.5; }
  return s;
}

float terrainH(vec2 p, float t) {
  vec2 q = p * 0.00085;
  float n = fbm(q + vec2(t * 0.012, t * 0.008));
  return (n - 0.5) * 2.6;      // 拉开动态范围，等高线才有足够密度
}
```

`terrainH` 的 `(n - 0.5) * 2.6` 不是随手写的：fbm 的输出集中在 0.5 附近，不拉开动态范围的话等值线只会集中在地形中部几圈。乘 2.6 后范围约 ±1.3，配合下面的频率才有遍布全场的等高线。

`t * 0.012` 让噪声场随时间缓慢平移，等高线像活地图一样在爬，这是"活着的系统"观感的来源。

### 顶点着色器

```glsl
uniform float uTime;
varying vec2  vXY;
varying float vDepth;

void main() {
  vec3 p = position;
  vXY = p.xy;                              // 未位移的平面坐标，传给片元
  float h = terrainH(p.xy, uTime);
  p.z += h * 92.0;                         // 沿平面法线抬升
  vec4 mv = modelViewMatrix * vec4(p, 1.0);
  vDepth = -mv.z;                          // 视深，用于远处淡出
  gl_Position = projectionMatrix * mv;
}
```

### 片元着色器

```glsl
uniform float uTime;
uniform vec3  uInk;
uniform float uOpacity;
varying vec2  vXY;
varying float vDepth;

// 抗锯齿等高线：取到最近等值线的距离，用 fwidth 归一化
float contour(float h, float freq, float w) {
  float v = h * freq;
  float f = fract(v);
  float d = min(f, 1.0 - f);
  float aaw = fwidth(v) * w;
  return 1.0 - smoothstep(0.0, aaw, d);
}

void main() {
  float h = terrainH(vXY, uTime);
  float main1 = contour(h, 26.0, 1.5);            // 主等高线
  float minor = contour(h, 82.0, 1.0) * 0.30;     // 次级等高线，更密更淡
  float line = max(main1, minor);
  if (line <= 0.002) discard;

  // 反转边界的 smoothstep 在 GLSL 里属未定义行为，写成 1.0 - smoothstep 更稳
  float fog = 1.0 - smoothstep(620.0, 2400.0, vDepth);       // 远处沉入纸面
  float rad = 1.0 - smoothstep(520.0, 1650.0, length(vXY));  // 边缘淡出

  gl_FragColor = vec4(uInk, line * fog * rad * uOpacity);
}
```

两级线宽（主 26 / 次 82）+ 两重淡出（视深 / 半径）是"像自然沉进纸里而不是被切断"的关键。只有一重淡出的话，地形边缘会出现一道生硬的截断。

---

## 3. 场景搭建

```js
renderer = new THREE.WebGLRenderer({
  canvas: glCanvas, alpha: true, antialias: false, powerPreference: 'high-performance'
});
renderer.setClearColor(0x000000, 0);
renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 1.5));

scene = new THREE.Scene();
camera = new THREE.PerspectiveCamera(40, 1, 1, 6000);
camera.position.set(0, 0, 640);

var geo = new THREE.PlaneGeometry(2800, 2800, 176, 176);
var mat = new THREE.ShaderMaterial({
  transparent: true,
  depthWrite: false,
  extensions: { derivatives: true },   // WebGL1 下启用 fwidth
  uniforms: {
    uTime:    { value: 0 },
    uInk:     { value: new THREE.Color('#111111') },
    uOpacity: { value: 0 }
  },
  vertexShader: VERT,
  fragmentShader: FRAG
});

terrain = new THREE.Mesh(geo, mat);
terrain.rotation.x = -1.16;            // 向后倾倒，形成俯视地形的视角
terrain.position.set(0, -150, -300);
scene.add(terrain);
```

`alpha: true` 让画布透明，地形直接叠在白色纸面上。`depthWrite: false` 避免半透明的线互相遮挡产生黑边。

CDN（r128 是 UMD 版，603KB，比 module 版轻一半）：

```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js"></script>
```

---

## 4. 指针响应与性能

指针只记录目标值，**插值在唯一的 rAF 循环里做**：

```js
ptr.x += (ptr.tx - ptr.x) * 0.055;   // 0.055 是跟随速度，越大越跟手
ptr.y += (ptr.ty - ptr.y) * 0.055;
clock += 0.016;

var tx = -1.16 + ptr.y * 0.085 + Math.sin(clock * 0.22) * 0.012;
var tz = ptr.x * 0.05 + Math.sin(clock * 0.13) * 0.018;
terrain.rotation.x += (tx - terrain.rotation.x) * 0.045;
terrain.rotation.z += (tz - terrain.rotation.z) * 0.045;
terrain.position.y += ((-150 - ptr.y * 26) - terrain.position.y) * 0.045;
terrain.material.uniforms.uTime.value = clock;
renderer.render(scene, camera);
```

两个 `Math.sin` 是**自动漂移**：没人操作时地形也在极缓慢地呼吸，不会像一张死图。

**性能要点**：

- 像素比封顶 **1.5**。地形是柔和的，不需要满视网膜分辨率。
- **`IntersectionObserver` 监听 hero**，滚出视口就停 rAF。
- **`visibilitychange`** 标签页隐藏时停。
- **`resize` 防抖 200ms** 后再 `setSize` + `updateProjectionMatrix`。

`uOpacity` 的浓度常量单独提取，方便调：

```js
/* 白底比纸底更显墨（#111 对白 19.1:1，对暖灰纸只有 15.5:1），
   所以浓度要压下来，否则整页会重新变暗。调这一个数即可。 */
var TERRAIN_OPACITY = 0.22;
```

**改背景色时必须同步复查这个值。** 背景越白，墨线越显眼。

---

## 5. 滚出视野时的退场

```js
ScrollTrigger.create({
  trigger: '.hero', start: 'top top', end: 'bottom top', scrub: true,
  onUpdate: function (self) {
    var p = self.progress;
    terrain.position.z = -300 - p * 620;
    terrain.material.uniforms.uOpacity.value = TERRAIN_OPACITY * (1 - p * 0.85);
  }
});
```

滚动时地形退向纵深并淡出，感受上是"从地表沉入档案层"。

**⚠️ 冲突警告**：这段在写 `uOpacity`，所以 rAF 循环里**绝对不能再插值同一个 uniform**。两个系统争抢同一个属性会导致抖动或卡死。入场淡入用 GSAP 单独做一次，之后交给 ScrollTrigger：

```js
gsap.to(terrain.material.uniforms.uOpacity, {
  value: TERRAIN_OPACITY, duration: 1.8, delay: 0.45, ease: 'power2.out'
});
```

---

## 6. 降级

三层兜底，缺一不可：

1. **`prefers-reduced-motion`** → 不初始化 Three.js，或只静态渲染一帧后停。
2. **WebGL 不可用 / Three 加载失败** → 退回 2D canvas 版等高线（Marching Squares 网格插值）。代码保留在原型文件里，`initGL()` 用 `try/catch` 返回布尔值决定走哪条路。
3. **运行中离屏 / 标签隐藏** → 停 rAF（见 §4）。

```js
var hasGL = false;
if (window.THREE && !reduce) {
  hasGL = initGL();
  if (hasGL) glCanvas.style.display = 'block';
}
if (!hasGL) {
  glCanvas.style.display = 'none';
  drawFlat();          // 2D 兜底
}
```

---

## 7. 踩过的坑

| 坑 | 现象 | 正确做法 |
|---|---|---|
| 等高线按顶点高度插值 | 线宽随网格变、远处糊 | 在片元里从世界坐标重算 |
| 反转边界的 `smoothstep(2400., 620., d)` | 在部分 GPU 上结果未定义 | 一律写 `1.0 - smoothstep(边0, 边1, x)`，边0 < 边1 |
| WebGL1 下用 `fwidth` | 编译报错 | `ShaderMaterial` 加 `extensions: { derivatives: true }` |
| `uOpacity` 被两处写 | 浓度抖动 / 卡在某一值 | 只留一个系统写它 |
| fbm 不拉动态范围 | 等高线只集中在中间几圈 | `(n - 0.5) * 2.6` |
| 换白底后不复查浓度 | 整页重新变暗 | 背景越白，`TERRAIN_OPACITY` 越要往下调 |
| `clip-path` 与 `translateZ` 分层写 | 3D 纵深失效 | 两者放同一个元素上 |
