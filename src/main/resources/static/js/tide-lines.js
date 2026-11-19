(() => {
  const art = document.querySelector('.tide-art');
  const source = art?.querySelector('.tide-still')?.src ?? art?.dataset.tideSource;
  if (!source || !CSS.supports('width', '1cqw') || !Element.prototype.animate) return;

  const motion = matchMedia('(prefers-reduced-motion: reduce)');
  const svgNamespace = 'http://www.w3.org/2000/svg';
  const highlightColor = '#1582ad';
  const header = art.closest('header');

  function svgElement(name, attributes, parent) {
    const element = document.createElementNS(svgNamespace, name);
    for (const [key, value] of Object.entries(attributes)) element.setAttribute(key, value);
    parent.append(element);
    return element;
  }

  function projection(parent) {
    const view = document.createElement('div');
    const rig = document.createElement('div');
    view.className = 'tide-projection';
    rig.className = 'tide-rig';
    view.append(rig);
    parent.append(view);
    return rig;
  }

  function revealCurves(curves) {
    const delay = document.documentElement.classList.contains('nav-in') ? 340 : 0;
    return curves.map((curve, index) => {
      const id = 'tide-reveal-' + index;
      const svg = curve.ownerSVGElement;
      const defs = svg.querySelector('defs') ?? svgElement('defs', {}, svg);
      const mask = svgElement('mask', {
        id, 'mask-type': 'alpha', maskUnits: 'userSpaceOnUse', x: 0, y: 0, width: 1120, height: 560
      }, defs);
      const trace = svgElement('path', {
        d: curve.getAttribute('d'), pathLength: 1, fill: 'none', stroke: '#fff',
        'stroke-width': 6, 'stroke-dasharray': '1 1'
      }, mask);
      curve.setAttribute('mask', 'url(#' + id + ')');
      return trace.animate([{ strokeDashoffset: 1 }, { strokeDashoffset: 0 }], {
        delay: delay + index * 230, duration: index === 0 ? 1250 : 940, fill: 'backwards',
        easing: 'cubic-bezier(.28,.22,.28,1)'
      }).finished.then(() => {
        curve.removeAttribute('mask');
        mask.remove();
      });
    });
  }

  function bindPointer(scene) {
    const pivotY = parseFloat(getComputedStyle(scene).getPropertyValue('--tide-pivot-y')) / 100;
    let frame = 0;
    let touchTimeout;

    function clear() {
      cancelAnimationFrame(frame);
      clearTimeout(touchTimeout);
      scene.classList.remove('is-lit');
      scene.style.removeProperty('--tide-tilt-x');
      scene.style.removeProperty('--tide-tilt-y');
    }

    function show(event) {
      const { left, top, width, height } = scene.getBoundingClientRect();
      const x = (event.clientX - left) / width;
      const y = (event.clientY - top) / height;
      if (x < 0 || x > 1 || y < 0 || y > 1) {
        clear();
        return;
      }
      scene.style.setProperty('--tide-light-x', (x * 100) + '%');
      scene.style.setProperty('--tide-light-y', (y * 100) + '%');
      scene.style.setProperty('--tide-light-radius', Math.max(54, width * .145) + 'px');
      if (event.pointerType !== 'touch') {
        const verticalRange = y < pivotY ? pivotY : 1 - pivotY;
        scene.style.setProperty('--tide-tilt-x', (-8 * (y - pivotY) / verticalRange) + 'deg');
        scene.style.setProperty('--tide-tilt-y', ((x - .5) * 20) + 'deg');
      }
      scene.classList.add('is-lit');
    }

    header.addEventListener('pointermove', event => {
      if (motion.matches || event.pointerType === 'touch') return;
      cancelAnimationFrame(frame);
      frame = requestAnimationFrame(() => show(event));
    });
    header.addEventListener('pointerleave', event => {
      if (event.pointerType !== 'touch') clear();
    });
    header.addEventListener('pointerdown', event => {
      if (motion.matches || event.pointerType !== 'touch') return;
      clear();
      show(event);
      touchTimeout = setTimeout(clear, 1400);
    }, { passive: true });
    motion.addEventListener('change', clear);
  }

  async function enhance() {
    if (motion.matches) return;
    motion.removeEventListener('change', enhance);
    try {
      const response = await fetch(source, { mode: 'same-origin' });
      if (!response.ok) return;
      const graphic = new DOMParser().parseFromString(await response.text(), 'image/svg+xml').documentElement;
      if (graphic.namespaceURI !== svgNamespace || graphic.localName !== 'svg') return;

      const front = document.importNode(graphic, true);
      const axes = front.querySelector('#curves-and-axes');
      const samples = front.querySelector('#samples');
      const nodes = front.querySelector('#nodes-and-labels');
      // 线谱 SVG 的主线组以四条可描画的路径结尾，最后一条留在前景。
      const curves = Array.from(axes.children).slice(-4);
      const scene = document.createElement('div');
      scene.className = 'tide-scene';
      const rig = projection(scene);
      const viewBox = front.getAttribute('viewBox');
      const layer = (parent, className) => svgElement('svg', { viewBox, fill: 'none', class: className }, parent);
      layer(rig, 'tide-plane-back').append(samples);
      layer(rig, 'tide-plane-middle').append(...curves.slice(0, -1));
      rig.append(front);

      const light = document.createElement('div');
      light.className = 'tide-light';
      scene.append(light);
      const lightRig = projection(light);
      const middleLight = layer(lightRig, 'tide-plane-middle');
      const frontLight = layer(lightRig, 'tide-plane-front');
      curves.forEach((curve, index) => {
        const main = index === curves.length - 1;
        const accent = curve.cloneNode();
        accent.setAttribute('stroke', highlightColor);
        accent.setAttribute('stroke-width', main ? 3.4 : 2.4);
        accent.setAttribute('opacity', main ? .92 : .72);
        (main ? frontLight : middleLight).append(accent);
      });
      Array.from(nodes.querySelectorAll('circle')).slice(0, 9).forEach(node => {
        svgElement('circle', {
          cx: node.getAttribute('cx'), cy: node.getAttribute('cy'), r: 4.5, fill: highlightColor
        }, frontLight);
      });

      art.append(scene);
      art.classList.add('is-enhanced');
      if (!motion.matches) {
        const rotation = rig.animate([
          { transform: 'rotateX(25deg)' },
          { transform: 'rotateX(0deg)' }
        ], { duration: 1750, easing: 'cubic-bezier(.25,.55,.3,1)' });
        await Promise.all([rotation.finished, ...revealCurves([curves[3], curves[1], curves[2], curves[0]])]);
      }
      bindPointer(scene);
    } catch (error) {
      console.warn('线谱动效初始化失败', error);
      art.classList.remove('is-enhanced');
      art.querySelector('.tide-scene')?.remove();
    }
  }

  motion.addEventListener('change', enhance);
  enhance();
})();
