---
name: blog-design
description: cz-blog（陈震的客栈）的视觉规范：终末地工业编辑风。纸墨为底、单一信号黄、全直角、超大字号、3D 等高线地形。含完整色板与实测对比度、字阶、几何、动效清单、组件配方、无障碍红线，以及 Thymeleaf 落地约定。改这个博客的样式、加页面、写组件、调色、改字号时读它。
---

# cz-blog 视觉规范 · 终末地工业编辑风

> 适用：`cz-blog`（Spring Boot + Thymeleaf 的中文个人博客）。
> 改样式、加页面、写组件、调色、改字号前先读本文件。
> 不是在描述"一种好看的风格"，是本项目**已经定稿并逐项验证过**的规范。偏离需要理由。

---

## 0. 这套风格的来源

参考《明日方舟：终末地》官网与游戏主界面。它的本质**不是"深色科技感"**，而是**工业编辑风**：

> 一张有质感的纸，压在上面的墨黑字，以及一个用来做信号的高饱和色。

三条铁律贯穿全篇：

1. **纸与墨是主体，强调色是信号。** 强调色只用于"需要被看见的那一处"（按钮、悬停行、焦点环、状态块），绝不大面积铺色。目标：信号色像素占比 **< 8%**。
2. **全直角。** 圆角会把界面推向"柔和产品"观感，直角推向"技术文档 / 工程图纸"。只有本来就圆的形状（头像、状态点、加载圈）保留圆形。
3. **数字等宽。** 全局 `font-variant-numeric: tabular-nums`，让日期、耗时、百分比跳动时不产生横向抖动。

另外三条是**本项目实践得出的、原版规范里没有的**，踩过坑，必须遵守：

4. **背景要浅、文字要黑，这是两件独立的事。** 把"整体太暗"误判为"文字太浅"，会得到一页读不清字的浅灰网站。见 §2.3。
5. **正文字号下限 17px**，技术标签层放宽到 15px。见 §3。
6. **`--font-mono` 的字体栈里必须垫中文字体。** 不垫就是一个 CSS 级 bug，全站中文回退到宋体。见 §3.3。

---

## 1. 何时用 / 何时不用

**用**：博客的首页、文章页、归档、标签、留言板、友链、关于页；以及这套风格下的任何新增页面或组件。

**不用**：后台管理页（`templates/admin/`）走功能优先，不套这套视觉。邮件模板（`mail*.html`）受邮件客户端 CSS 支持限制，只用色彩与字阶，不用 clip-path / 3D / GSAP。

---

## 2. 色板

### 2.1 完整令牌

```css
:root {
  /* ---- 纸与墨 ---- */
  --bg:            #ffffff;
  --bg-sunken:     #fafaf9;   /* 信息条 / 页脚 */
  --panel:         #fbfbfa;   /* 抽屉 / 悬停面 */
  --line:          #e8e8e8;   /* 细线 l1 */
  --line-strong:   #d4d4d4;   /* 重线 l2 */
  --ink:           #111111;   /* 正文      白底 19.1:1 */
  --ink-2:         #4a4a4a;   /* 次要文字  白底  8.9:1 */
  --ink-on-signal: #111111;   /* 信号色底上的墨字 */

  /* ---- 信号黄：按"要不要被读到"分三档 ---- */
  --signal:        #6b5d00;   /* 文字 / 焦点环      白底 6.58:1 */
  --signal-fill:   #ffe600;   /* 实心块，块内墨字   15.0:1 */
  --signal-fill-h: #ffd000;   /* 实心块 hover / press */
  --signal-edge:   #edc800;   /* 细线与描边         白底 1.63:1 */
  --signal-rgb:    255, 230, 0;   /* 半透明色块 rgba(var(--signal-rgb), α) */

  /* ---- 几何与字阶 ---- */
  --cut:      16px;   /* 斜切楔形尺寸 */
  --fs-label: 15px;   /* 技术标签层字号，见 §3 */
}
```

**纯 `#ffffff` 是刻意选的。** 早期版本用暖灰纸 `#e8e8e2`，在多数显示器上读起来发闷，已弃用。

### 2.2 为什么黄必须拆三档（本规范最重要的一条）

```
#fff500 亮黄  对  #ffffff = 1.00:1   ← 完全隐形
#fff500 亮黄  对  #e8e8e2 = 0.93:1   ← 也隐形
```

**任何亮到能被称为"亮黄"的颜色，对白底对比度都是 1:1。** 这是明度决定的物理限制，换 `#ffe600`、`#ffd400`、`#fffa00` 都一样。

所以黄不能当"一个颜色"用，必须按落点拆角色：

| 令牌 | 值 | 用在哪 | 判据 |
|---|---|---|---|
| `--signal` | `#6b5d00` 深金 | 悬停文字、焦点环、任何**要读**的黄 | 对白底 ≥ 4.5:1 |
| `--signal-fill` | `#ffe600` 亮黄 | 主按钮、索引行悬停、照片标牌、载入擦屏 | 块内墨字 ≥ 4.5:1 |
| `--signal-edge` | `#edc800` | 1px 下划线、状态块、**以及所有亮黄块的描边** | 只求看得见（装饰带 1.06-1.6:1） |

**`--signal-edge` 的存在理由**：亮黄块对白底是 1:1，块的边界会消失，只剩里面的黑字浮着。加一圈 `#edc800` 描边把形状定出来。顺带这个"亮黄块 + 硬边"的处理非常符合工业标识的语言。

**推论**：任何新增的亮黄实心块，都必须同时给它 `border: 1px solid var(--signal-edge)`。漏了就是隐形方块。

### 2.3 背景浅 ≠ 文字浅

这是本项目走过一次弯路的地方。用户说"太暗"，错误解法是把**背景和文字一起提亮**，结果得到一页读不清字的浅灰站。

正确解法：

| 症状 | 该动的地方 | 不该动的地方 |
|---|---|---|
| "整体发闷 / 太暗" | 背景、墨的**覆盖率**（底纹浓度、照片亮度、噪点） | 文字颜色 |
| "字看不清 / 太浅" | 文字颜色（`--ink` / `--ink-2`） | 背景 |

**背景保持纯白、墨覆盖率保持低的同时，文字可以且应该是 19:1 的黑。** 这两件事互不冲突。

### 2.4 对比度红线（新增颜色前必查）

| 组合 | 实测 | 门槛 |
|---|---|---|
| `--ink` / `--bg` | 19.1:1 | AA 4.5 |
| `--ink-2` / `--bg` | 8.9:1 | AA 4.5 |
| `--ink-2` / `--bg-sunken` | 8.4:1 | AA 4.5 |
| `--ink-on-signal` / `--signal-fill` | 15.0:1 | AA 4.5 |
| `--signal` / `--bg` | 6.58:1 | AA 4.5 |
| `--signal-edge` / `--bg` | 1.63:1 | 装饰带，不承载文字 |

**装饰与前景走两套标准**：前景文字守 AA 4.5:1；纯装饰底纹守"看得见但不抢戏"的 1.06-1.6:1 区间。低于 1.06:1 等于不存在。

**新颜色一律先算对比度再用。** 别凭感觉。

---

## 3. 字阶

### 3.1 字体栈

```css
--font-cjk:  "Noto Sans SC", "PingFang SC", "Microsoft YaHei", sans-serif;
--font-sans: "Space Grotesk", var(--font-cjk);          /* 正文 / UI */
--font-disp: "Archivo", "Space Grotesk", var(--font-cjk); /* 雷霆大字 / 数字 */
--font-mono: "JetBrains Mono", var(--font-cjk), ui-monospace, monospace; /* 技术标签 */
```

Google Fonts 请求：

```html
<link href="https://fonts.googleapis.com/css2?family=Archivo:wdth,wght@62..125,100..900&family=Space+Grotesk:wght@300..700&family=JetBrains+Mono:wght@300..700&family=Noto+Sans+SC:wght@300..900&display=swap" rel="stylesheet">
```

`Archivo` 是可变字体，有宽度轴。大字用 `font-variation-settings: 'wdth' 105, 'wght' 800`，数字用 `'wdth' 100, 'wght' 800`。

### 3.2 字号：两层，一个硬底

```css
body { font-size: 17px; line-height: 1.75; }
--fs-label: 15px;   /* 唯一允许小于 17px 的层 */
```

**正文下限 17px，没有例外。** 唯一放宽的是"技术标签层"：时间戳、页码、图片角标、栏目码、侧轨编号。它们的共同点是**只做注释，不承载正文信息**。这个划分用 `--fs-label` 令牌写明，是一个有名字的概念，不是散落的魔法数字。

| 层 | 字号 | 包含 |
|---|---|---|
| 雷霆大字 | `clamp(2.8rem, 9.4vw, 6.6rem)` | 首页主标题 |
| 章节标题 | `clamp(1.9rem, 4vw, 2.9rem)` | 「文章索引」等 |
| 大数字 | 34px / 30px / 48px | 数据条 / 索引行号 / 分类数字 |
| 标题 | 24px / 22px / 20px | 索引标题 / 分类名 / 关于页导语 |
| **正文** | **17px** | 段落、摘要、导航、抽屉、**所有按钮**、信息条标题 |
| **标签** | **`--fs-label` 15px** | `.mono` 系列、图片角标、照片标牌、侧轨编号 |

**按钮永远是 17px**，不进标签层，因为可点击元素不降级，要保证点击热区与可读性。

**字距要反向补偿**：字号降回 15px 后，`.mono` 的字距要从 `.1em` 放回 `.14em`。大写等宽字在小字号下需要更多字距才不糊成一团。

### 3.3 `--font-mono` 的中文陷阱（必读）

```css
/* ✗ 错误：JetBrains Mono 没有中文字形，栈里也没有中文字体 */
--font-mono: "JetBrains Mono", ui-monospace, monospace;

/* ✓ 正确 */
--font-mono: "JetBrains Mono", var(--font-cjk), ui-monospace, monospace;
```

**不垫中文字体的后果**：凡是 `class="mono"` 元素里的中文，浏览器只能一路回退到系统默认等宽，中文 Windows 上通常是**宋体**，和全站字体完全不是一套。用户会直接指出"这个字不对"。

**为什么垫上就对了**：CSS 字体回退是**按字形逐个走**的。拉丁字母与数字仍走 JetBrains Mono（保住等宽感和 tabular 数字），只有它缺的中文才落到 Noto Sans SC。

**推论**：**纯中文的标签不要给 `mono` 处理。** 没有拉丁字符需要等宽对齐时，mono 只有副作用（多余字距、对中文无效的 `uppercase`）。例子：

```css
/* "篇文章 / 开始写作 / 个常写话题" 是纯中文，整个不给 mono */
.hero-stats .u {
  font-family: var(--font-sans); font-size: var(--fs-label);
  letter-spacing: normal; text-transform: none;
}
```

中英混排的（`Personal Archive / 个人归档`）可以保留 mono，靠 §3.3 的字体栈兜底。

---

## 4. 几何

### 4.1 全直角

```css
*, *::before, *::after { box-sizing: border-box; border-radius: 0 !important; }

/* 唯一保留圆形的元素 */
.avatar, .dot, .spinner { border-radius: 50% !important; }
```

用 `!important` 是有意的，防止任何第三方组件带进圆角。

### 4.2 斜切楔形（标志性形状）

```css
--cut: 16px;
.cut-tr { clip-path: polygon(0 0, calc(100% - var(--cut)) 0, 100% var(--cut), 100% 100%, 0 100%); }
.cut-bl { clip-path: polygon(0 0, 100% 0, 100% 100%, var(--cut) 100%, 0 calc(100% - var(--cut))); }
.cut-br { clip-path: polygon(0 0, 100% 0, 100% calc(100% - var(--cut)), calc(100% - var(--cut)) 100%, 0 100%); }
```

切角是"工程图纸"语言的核心，图片、面板、需要"被裁切"观感的地方都用它。

**⚠️ 与 3D 的冲突**：`clip-path` 是 CSS 的 grouping 属性，**会拍平子元素的 3D 变换**。如果一块图既要斜切又要停在不同纵深，**把 clip-path 和 `translateZ` 放在同一个元素上**，它自身的 3D 定位仍然生效，只是子元素被拍平：

```html
<!-- ✗ 错：clip 在 img 上，translateZ 在父层，子元素的 3D 被拍平 -->
<div class="tile t1"><img class="cut-tr" src="..."></div>

<!-- ✓ 对：两者都在 tile 上 -->
<div class="tile cut-tr t1"><img src="..."></div>
```

### 4.3 栅格与间距

- 容器 `max-width: 1400px; margin: 0 auto`，内边距 `24px`，`≥1024px` 时 `48px`
- 章节垂直间距 `104px`，`≥1024px` 时 `136px`
- 分隔优先用 1px 细线 + 留白，**不要用卡片**。卡片只在高程真的表达层级时用

---

## 5. 动效

### 5.1 清单与动机

**每个动画必须能一句话说清它传达什么**（层级 / 叙事 / 反馈 / 状态转换）。说不出就别加。

| 动效 | 实现 | 动机 |
|---|---|---|
| 载入擦除 | 满屏 `--signal-fill` 用 `clip-path: inset(0 0 0 100%)` 定向抽走 | 叙事：开幕 |
| 翻页擦除 | `#pagewipe` 信号黄板带过站内跳转，方向与载入擦除一致：离开时从左扫入盖满（0.34s，`.nav-out`，板中央亮出目的地页名），落点页停一拍后向右收走（0.55s，`.nav-in`，animGate 读 sessionStorage 标记）；首页以 `#wipe` 开幕兼任到达半程（`data-selfwipe` 豁免） | 状态转换：页面之间有前后关系 |
| 雷霆大字入場 | 逐字 `z:-820px, rotationX:-78°` 飞入回正，父级 `perspective:1000px` | 叙事：标题宣告自己 |
| 定向裁切揭示 | `clip-path: inset(0 100% 0 0)` → `inset(0)` | 叙事：印刷式送出，比淡入更符合语言 |
| 索引行悬停 | `::before` 用 `clip-path` 从左填满 `--signal-fill`，文字翻转为墨色 | 反馈：终末地官网的标志交互 |
| 图像场分层 | 三块停在不同 `translateZ`，容器随指针 `rotateX/rotateY` | 层级：产生真实视差分层，不是整体平移 |
| 呼吸状态块 | 6px 方块 3.2s 透明度呼吸，全站仅一处 | 反馈：活着的系统状态 |
| 指针物理（磁吸 + 倾斜） | `chrome :: scripts` 的共享 rAF 循环，按需启停：`.magnet` 位移吸附（横向 0.28 / 纵向 0.42 / 跟随 0.14），`[data-tilt]` 指针追踪 `perspective(700px) rotateX/rotateY` ±4° | 反馈：物理手感 |
| 3D 等高线地形 | Three.js，见 `references/terrain-3d.md`；工厂在 `/js/terrain.js`（`CzTerrain.initGL` / `drawFlat`），首页重力感应、归档页头低速漂移标本（浓度 0.09，无指针响应） | 层级 + 叙事：还原游戏主界面的重力感应地形；归档用其标本喻「时间的沉积地层」 |
| 章节浮出 | 标题/图片从 `translateZ(-260px)` 浮出 | 层级：页面有前后关系 |
| 滚动时间轴（归档） | `.arc` 左缘墨轨 + `.arc-progress` 信号黄段 `scaleY` 随滚动 scrub 生长；年份刻度方块随视口当前年转黄（几何重算，不用边缘 IO） | 叙事：归档是一条时间轴 |
| 阅读进度（文章） | 视口顶 3px 信号条 `scaleX` = 文章滚动进度（ScrollTrigger scrub） | 状态转换：读到哪里了 |
| 正文标题揭示（文章） | `.md-body h2` 用 IntersectionObserver + CSS `clip-path` transition 逐节擦入；不建 ScrollTrigger 实例（文章几十个节点，防实例爆炸） | 叙事：章节依次送出 |
| 磁吸标签云（标签） | `.tag-chip` 挂 `.magnet`；选中态是 `--signal-fill` 的 `clip-path` 定向擦入，提交延迟 280ms 让擦入播完 | 反馈：标签是可被吸引的物件 |
| 名片墙倾斜（友链） | `.fl-card` 挂 `data-tilt`（共享循环驱动）；hover 时头像从灰度恢复彩色 | 反馈：名片被指尖捻起 |
| 对话生长（留言板） | `.cm` 纯 CSS keyframes（translateY+opacity，前 10 条 nth-child 错开 delay）；AJAX 整块替换列表后新节点自动重播，零 JS 介入 | 叙事：留言一条条长出来 |
| 逐字入场小号版（关于） | page-title 逐字 `z:-620, rotationX:-72°` 飞入（thunder 的收缩参数）；脚本拆字后才给标题开 `perspective`（`.chars-on`），reduce / 无 GSAP 时不拆字 | 叙事：人物档案的签名 |

**缓动**：入场统一 `cubic-bezier(.16, 1, .3, 1)` 或 GSAP `power3.out` / `power4.out`。禁用 linear（除走马灯）。

### 5.2 硬性约束

- **禁止 `window.addEventListener('scroll', ...)`。** 用 GSAP ScrollTrigger / IntersectionObserver / CSS scroll-driven animations。
- **禁止用 state 追踪连续值**（鼠标位置、滚动进度）。用单一 rAF 循环 + 变量插值。
- 只动 `transform` 和 `opacity`，不动 `top/left/width/height`。
- **一个属性只能有一个动画系统在写。** GSAP 在补间 `uOpacity` 时，rAF 循环里就不能再插值同一个 uniform，两边争夺会导致抖动或卡住。
- **`perspective` 必须写在父级**，子元素的 `translateZ` 才看得出纵深。
- 每个 `useEffect` / 事件监听都要有清理；rAF 循环在元素离开视口和标签页隐藏时停止。
- **走马灯全站最多一个**。多了就是填充料。

### 5.3 降级（必须实现）

```css
@media (prefers-reduced-motion: reduce) {
  *, *::before, *::after { transition-duration: .01ms !important; }
  .ticker-track, .status .dot { animation: none; }
  .wipe-in { clip-path: none; }
  .rise, .depth-in { opacity: 1; transform: none; }
}
```

- `prefers-reduced-motion` → 所有动画降为静态，内容直接可见
- **WebGL 不可用 → 退回 2D canvas 等高线**（代码保留在原型里）
- 滚动/标签页隐藏 → 停止渲染循环

---

## 6. 组件配方

### 顶栏
高度 60px（上限 80px），`rgba(255,255,255,.82)` + `backdrop-filter: blur(14px)`，底部 1px `--line`。导航**桌面端必须单行**；字号变大后断点从 900px 上移到 **1080px**，低于此收进汉堡抽屉。

### 导航下划线
`::after` 1px `--signal-edge`，`transform: scaleX(0)` → `scaleX(1)`，`transform-origin` 从 `right` 切到 `left`，`.4s`。**不要用 opacity 淡入**。

### 文章索引行
不用卡片。`border-bottom: 1px --line` 的行式布局，栅格 `104px / 1fr`（行号 / 标题+摘要+元信息）。

封面（`t_blog.first_picture`）有的话占**最右**一列 216px，栅格变成 `104px / 1fr / 216px`。
但它**不是固定第三栏**：已发布的 46 篇里有 13 篇没填封面，写死三栏就会在那些行右边留一条
216px 的空槽（最新的 12 篇里 10 篇是空的）。所以由模板给有封面的行加 `.has-thumb`，
栅格跟着切换。窄屏（<1024px）封面缩到 96×54 挪到**最左**列，行变两栏。

元信息（`.row-meta`）跟在摘要下面、左对齐，**没有** `justify-content: flex-end` ——
那是封面进来之前、右列空着时的写法，现在最右列是封面，甩过去会压到图上。

```css
.idx-row { position: relative; isolation: isolate; }
.idx-row.has-thumb { grid-template-columns: 96px minmax(0, 1fr); }   /* 窄屏两栏 */
.idx-row.has-thumb .idx-thumb { grid-column: 1; grid-row: 1 / span 2; }
@media (min-width: 1024px) {
  .idx-row { grid-template-columns: 104px minmax(0, 1fr); }
  .idx-row.has-thumb { grid-template-columns: 104px minmax(0, 1fr) 216px; }
  .idx-row.has-thumb .idx-thumb { grid-column: auto; grid-row: auto; align-self: center; }
}
.idx-row::before {
  content: ""; position: absolute; inset: 0; z-index: -1;
  background: var(--signal-fill);
  clip-path: inset(0 100% 0 0);
  transition: clip-path .5s cubic-bezier(.16, 1, .3, 1);
}
.idx-row:hover::before { clip-path: inset(0 0 0 0); }
.idx-row:hover .idx-title, .idx-row:hover .idx-desc,
.idx-row:hover .idx-num, .idx-row:hover .row-meta { color: var(--ink-on-signal); }
```

`isolation: isolate` 是必需的，让 `z-index: -1` 的填充层不跑到页面背景后面。
封面轨道的 `.idx-thumb` 基础尺寸规则必须排在 `@media` 里那条**之前**：两条特异性同为
`(0,1,0)`，媒体查询不加特异性，胜负纯看源序，放反了 216×122 会被压回 96×54。

封面是外链（有道笔记 / CSDN），平均 1MB 一张、最大 4MB，`loading="lazy"` 不能省。
归档页与标签页的 `.post-thumb` 是同一个组件、同一尺寸，宽屏 216×122，窄屏整张不显示。

### 按钮
方形，无圆角。主按钮 `--signal-fill` 底 + `--signal-edge` 描边 + 墨色字；次按钮透明 + `--line-strong` 描边，悬停时描边与文字转 `--signal`。字号 **17px**，字距 `.06em`。`:active` 用 `translateY(1px)` 模拟物理按压。

### 图片
**分两类，不要一刀切。**

- **版面装置**（Hero 的三块 3D 图像场、关于页站长照）：`filter: grayscale(1) contrast(.94) brightness(1.24)`，
  高调平灰的印刷品，**悬停恢复彩色**（`grayscale(0)`）。
- **列表封面**（首页索引的 `.idx-thumb`，归档与标签页的 `.post-thumb`，宽屏 216×122）：
  **不加任何滤镜，一律原色**。灰度是给装饰性大图当版面装置用的；列表封面是内容的一部分，
  读者要靠它认出这篇讲什么，压成灰就白放了。

照片上不叠标签贴片，角标放在角落作为「标签页」。

### 幽灵数字
章节背景里的超大数字，`font-variation-settings: 'wdth' 125, 'wght' 800`，颜色 `rgba(17,17,17,.045)`。**这是版面装置，不是眉标签**，不要退化成 `001 ·` 这种小号编号形式。

### 页眉眉标签（eyebrow）
长这样：`<span class="bar"></span><span class="mono">Personal Archive / 个人归档</span>`。
**克制使用：每 3 个章节最多 1 个，首页 Hero 算 1 个。** 通篇每个章节标题上方都挂一个眉标签是最典型的模板感来源。

---

## 7. 反面教材

- ❌ 到处警示条纹、伪军事警告语
- ❌ 亮黄大面积铺底 + 上面压长文本
- ❌ 亮黄实心块不加 `--signal-edge` 描边（隐形方块）
- ❌ 亮黄当文字色（对白底 1:1）
- ❌ 把"整体太暗"误修成"文字提亮"（见 §2.3）
- ❌ 任何字号小于 17px，除非它属于 `--fs-label` 标签层
- ❌ `--font-mono` 栈里不垫中文字体（见 §3.3）
- ❌ 三张等宽卡片并排的"特性行"
- ❌ 每个章节都挂眉标签 / 章节编号眉标签（`00 / INDEX`、`06 · how it works`）
- ❌ 圆角按钮混在直角布局里
- ❌ 弹性 `translateZ` 写在 `clip-path` 元素的子级上（会被拍平）
- ❌ 两个动画系统写同一个属性
- ❌ 同一个页面出现两个走马灯
- ❌ 破折号 `—`：全站禁用。用句号、逗号或括号拆句

---

## 8. Thymeleaf 落地约定

- **本规范落在 `static/css/site.css` 一个文件里**，前台不再依赖任何 CSS 框架（Bootstrap 与 jQuery 已从前台完全移除）。
  **绝不要写进 `me.css`**：那份样式后台 12 个模板还在用，本规范的全局规则（`* { border-radius: 0 !important }`、`body` 字号与背景、`--font-*` 覆盖）一进去就会直接打到后台。`me.css` 现已冻结，后台用 Element UI 重做时整个文件删除，只需把 `.admin-body` 一条规则搬走。
- **三方库一律自托管在 `static/lib/`，不走 CDN**：GSAP、ScrollTrigger、Three.js、Prism、Tocbot 都是本地文件。字体同理，全站零外部请求。
- **现有资源路径与 Thymeleaf 语法不动**：`th:href="@{/}"`、`th:text="${author}"`、`th:style="'background-image: url(' + @{/images/index.jpg} + ')'"` 原样保留。改版只动结构与 class，不动后端契约。
- **SEO 不回归**：`<title>`、`meta description/keywords`、页面 slug、导航文案保持稳定。改版最大的风险是 SEO 迁移，不是视觉。
- **导航文案与顺序**（首页 / 标签 / 归档 / 留言板 / 友链 / 🚇开往 / 关于我，共 7 项）是既有信息架构，不因视觉改版而改。注意「开往」不是站内路由，它指向外部 webring `https://www.travellings.cn/go.html`，因此不参与 `aria-current` 高亮，也是唯一没有 `active` 参数的导航项。
- 前端动效脚本放在页面底部，**在 DOM 就绪后初始化**；用 `IntersectionObserver` 判断 hero 是否在视口内以开关渲染。
- 首屏 LCP 元素（大标题）不要依赖外部字体加载完成才可见，配 `font-display: swap`。

---

## 9. 交付前自检

- [ ] 全站没有小于 17px 的字号，除了 `--fs-label` 标签层
- [ ] `--font-mono` 栈里垫了 `var(--font-cjk)`
- [ ] 纯中文的标签没有套 `mono`
- [ ] 每个亮黄实心块都有 `--signal-edge` 描边
- [ ] 亮黄没有当文字色用
- [ ] 所有新增配色都算过对比度，正文 ≥ 4.5:1
- [ ] 是"背景浅 + 文字黑"，不是"背景浅 + 文字也浅"
- [ ] 全站 `border-radius: 0`，只有头像/状态点/圆
- [ ] 桌面端导航单行，高度 ≤ 80px
- [ ] 眉标签数量 ≤ ceil(章节数 / 3)
- [ ] 走马灯 ≤ 1 个
- [ ] 全站零个破折号 `—`
- [ ] `prefers-reduced-motion` 下全部动画静默、内容可见
- [ ] WebGL 不可用时退回 2D 等高线
- [ ] rAF 循环在离屏 / 标签隐藏时停止
- [ ] 没有 `window.addEventListener('scroll')`
- [ ] 移动端每个多列布局都有明确折叠规则
- [ ] 页面 slug / 导航文案 / 后端 model 属性未被改动

---

## 参考

- `references/terrain-3d.md`：3D 等高线地形的着色器实现与踩坑
- 原型全量代码：`prototypes/g-endfield-terrain.html`
