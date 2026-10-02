/**
 * todo доку перепишу
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
export function createCanvas(id) {
  const canvas = document.getElementById(id);
  if(!canvas) return null;

  const ctx = canvas.getContext('2d');
  const width = canvas.width;
  const height = canvas.height;
  const unit = width / 40;
  const MAX_RANGE = 1000;
  const MAX_SCALE = 5;
  const MIN_SCALE = Math.max(width, height) / (2 * MAX_RANGE * unit);
  const GRID_LEVELS = [1, 10, 100];
  const COLOR = '#9A7951FF';

  const storageKey = `scale:${id}`;
  let scale = parseFloat(sessionStorage.getItem(storageKey)) || 1;
  scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
  let offsetX = 0;
  let offsetY = 0;
  let animationId = null;

  const shapes = [];

  function clamp(value, min, max) {
    return Math.max(min, Math.min(max, value));
  }

  function maxOffset(size, s = scale) {
    return Math.max(0, MAX_RANGE * unit * s - size / 2);
  }

  function clampOffsets() {
    offsetX = clamp(offsetX, -maxOffset(width), maxOffset(width));
    offsetY = clamp(offsetY, -maxOffset(height), maxOffset(height));
  }

  function origin() {
    return [Math.floor(width / 2) + offsetX, Math.floor(height / 2) + offsetY];
  }

  function line(x1, y1, x2, y2) {
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
  }

  /**
   * Метод для отрисовки осей координатной плоскости с учетом направления. Цвет линий вынесен в константу.
   * При изменении масштаба координатная плоскость все еще остается на весь холст canvas.
   */
  function coordinatePlane() {
    const [ox, oy] = origin();

    ctx.save();
    ctx.strokeStyle = COLOR;
    ctx.lineWidth = 2;

    line(0, oy, width, oy);
    line(ox, 0, ox, height);

    ctx.beginPath();
    ctx.moveTo(width - 10, oy - 5);
    ctx.lineTo(width, oy);
    ctx.lineTo(width - 10, oy + 5);
    ctx.moveTo(ox - 5, 10);
    ctx.lineTo(ox, 0);
    ctx.lineTo(ox + 5, 10);
    ctx.stroke();

    ctx.restore();
  }

  /**
   * Рисует сетку для системы координат( todo доку изменю).
   * @private
   */
  function grid() {
    const [ox, oy] = origin();
    const limit = MAX_RANGE * unit;

    ctx.save();
    ctx.strokeStyle = '#d9b68f';
    ctx.translate(ox, oy);
    ctx.scale(scale, scale);

    const x0 = clamp(-ox / scale, -limit, limit);
    const x1 = clamp((width - ox) / scale, -limit, limit);
    const y0 = clamp(-oy / scale, -limit, limit);
    const y1 = clamp((height - oy) / scale, -limit, limit);

    for (const k of GRID_LEVELS) {
      const step = unit * k;
      const alpha = clamp((step * scale - 3) / 3, 0, 1);
      if (alpha === 0) continue;

      ctx.globalAlpha = alpha;
      ctx.lineWidth = (k === 1 ? 0.5 : 1) / scale;

      for (let i = Math.floor(x0 / step) * step; i <= x1; i += step) {
        line(i, y0, i, y1);
      }
      for (let j = Math.floor(y0 / step) * step; j <= y1; j += step) {
        line(x0, j, x1, j);
      }
    }

    ctx.restore();
  }

  function renderShape(shape) {
    const [ox, oy] = origin();

    ctx.save();
    ctx.translate(ox, oy);
    ctx.scale(scale, -scale);
    ctx.strokeStyle = COLOR;
    ctx.fillStyle = COLOR;
    ctx.lineWidth = 3 / scale;
    ctx.beginPath();
    //todo убрать потом этот бесконечный кейс
    switch (shape.type) {
      case 'square': {
        const { a, x, y } = shape;
        ctx.rect((x - a) * unit, (y - a) * unit, 2 * a * unit, 2 * a * unit);
        ctx.stroke();
        break;
      }
      case 'point':
        ctx.arc(shape.x * unit, shape.y * unit, 5 / scale, 0, 2 * Math.PI);
        ctx.fill();
        break;
      case 'circle':
        ctx.arc(shape.x * unit, shape.y * unit, shape.r * unit, 0, 2 * Math.PI);
        ctx.stroke();
        break;
      case 'triangle':
        ctx.moveTo(shape.p1[0] * unit, shape.p1[1] * unit);
        ctx.lineTo(shape.p2[0] * unit, shape.p2[1] * unit);
        ctx.lineTo(shape.p3[0] * unit, shape.p3[1] * unit);
        ctx.closePath();
        ctx.stroke();
        break;
    }

    ctx.restore();
  }

  /**
   * Полностью перерисовывает систему координат: очищает canvas
   * и рисует координатную плоскость с сеткой.
   */
  function redraw() {
    ctx.clearRect(0, 0, width, height);
    grid();
    coordinatePlane();
    shapes.forEach(renderShape);
  }

  function addShape(shape) {
    shapes.push(shape);
    redraw();
  }

  /**
   * Квадрат( todo доку дополню после переписанного html)
   *
   * @param {number} a - половина длины стороны в условных единицах.
   * @param {number} x - координата X центра.
   * @param {number} y - координата Y центра.
   */
  function drawSquare(a, x, y) {
    addShape({ type: 'square', a, x, y });
  }

  /**
   * Точка( todo доку дополню после переписанного html)
   *
   * @param {number} x - координата X.
   * @param {number} y - координата Y.
   */
  function drawPoint(x, y) {
    addShape({ type: 'point', x, y });
  }

  /**
   * Окружность(todo доку дополню после переписанного html)
   *
   * @param {number} r - радиус в условных единицах.
   * @param {number} x - координата X центра.
   * @param {number} y - координата Y центра.
   */
  function drawCircle(r, x, y) {
    addShape({ type: 'circle', r, x, y });
  }

  /**
   * Треугольник( todo доку дополню после переписанного html)
   *
   * @param {number[]} p1 - первая вершина [x, y].
   * @param {number[]} p2 - вторая вершина [x, y].
   * @param {number[]} p3 - третья вершина [x, y].
   */
  function drawTriangle(p1, p2, p3) {
    addShape({ type: 'triangle', p1, p2, p3 });
  }

  function clearShapes() {
    shapes.length = 0;
    redraw();
  }

  function getScale() {
    return scale;
  }

  function setScale(sc) {
    stopAnimation();
    scale = clamp(sc, MIN_SCALE, MAX_SCALE);
    clampOffsets();
    sessionStorage.setItem(storageKey, scale);
    redraw();
  }

  function resetScale() {
    stopAnimation();
    scale = clamp(1, MIN_SCALE, MAX_SCALE);
    offsetX = 0;
    offsetY = 0;
    sessionStorage.removeItem(storageKey);
    redraw();
  }

  function resetOffset() {
    stopAnimation();
    offsetX = 0;
    offsetY = 0;
    redraw();
  }

  function stopAnimation() {
    if (animationId !== null) {
      cancelAnimationFrame(animationId);
      animationId = null;
    }
  }

  function flyTo(targetX, targetY, targetScale, duration = 700) {
    stopAnimation();

    const endScale = clamp(targetScale, MIN_SCALE, MAX_SCALE);
    const startScale = scale;
    const startX = -offsetX / (unit * scale);
    const startY = offsetY / (unit * scale);
    const logRatio = Math.log(endScale / startScale);
    const startTime = performance.now();

    function frame(now) {
      const t = clamp((now - startTime) / duration, 0, 1);
      const k = t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;

      scale = startScale * Math.exp(logRatio * k);
      const cx = startX + (targetX - startX) * k;
      const cy = startY + (targetY - startY) * k;

      offsetX = -cx * unit * scale;
      offsetY = cy * unit * scale;
      clampOffsets();
      redraw();

      if (t < 1) {
        animationId = requestAnimationFrame(frame);
      } else {
        animationId = null;
        sessionStorage.setItem(storageKey, scale);
      }
    }

    animationId = requestAnimationFrame(frame);
  }

  function focusOn(x, y, a, duration = 700) {
    flyTo(x, y, 40 / Math.max(2 * a * 1.8, 1), duration);
  }

  canvas.addEventListener('wheel', (event) => {
    event.preventDefault();
    setScale(scale * Math.exp(-clamp(event.deltaY, -100, 100) * 0.002));
  }, { passive: false });

  let isDragging = false;
  let lastX = 0;
  let lastY = 0;

  canvas.addEventListener('mousedown', (event) => {
    stopAnimation();
    isDragging = true;
    lastX = event.clientX;
    lastY = event.clientY;
    canvas.style.cursor = 'grabbing';
  });

  canvas.addEventListener('mousemove', (event) => {
    if (!isDragging) return;
    offsetX += event.clientX - lastX;
    offsetY += event.clientY - lastY;
    lastX = event.clientX;
    lastY = event.clientY;
    clampOffsets();
    redraw();
  });

  function stopDragging() {
    isDragging = false;
    canvas.style.cursor = 'grab';
  }

  canvas.addEventListener('mouseup', stopDragging);
  canvas.addEventListener('mouseleave', stopDragging);
  canvas.style.cursor = 'grab';
  redraw();

  return {
    canvas, ctx, width, height, b: unit, c: unit, maxRange: MAX_RANGE, drawSquare, drawCircle,
    drawTriangle, drawPoint, redraw, setScale, getScale, resetScale, resetOffset, clearShapes, flyTo, focusOn,
  };
}
