/**
 * Создает и инициализирует окружение для рисования координатной плоскости на <canvas>.
 *
 * Начало координат (0, 0) располагается в центре <canvas>, ось У направлена вверх, плоскость условно разбита на 40 делений по каждой оси.
 * @param {string} id - id элемента <canvas> на странице.
 * @returns {{canvas: HTMLElement, ctx: *, width: *, height: *, b: number, c: number, drawSquare: drawSquare, redraw: redraw, drawPoint: drawPoint, drawCircle: drawCircle, drawTriangle: drawTriangle}|null}
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
export function createCanvas(id) {
  const canvas = document.getElementById(id);
  if(!canvas) return null;

  const ctx = canvas.getContext('2d');
  const width = canvas.width;
  const height = canvas.height;
  const b = width / 40;
  const c = height / 40;

  /**
   * Очищает <canvas> и рисует координатную плоскость: оси Х и У с направляющими стрелками.
   * @private
   */
  function coordinatePlane() {
    ctx.clearRect(0, 0, width, height);
    ctx.save();
    ctx.strokeStyle = '#9A7951FF';

    const centerX = Math.floor(width / 2) + 0.5;
    const centerY = Math.floor(height / 2) + 0.5;

    ctx.translate(centerX, centerY);
    ctx.scale(1, -1);
    ctx.lineWidth = 2;

    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(0, -centerY);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(centerX, 0);
    ctx.lineTo(-centerX, 0);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(centerX - 2, 0);
    ctx.lineTo(centerX - 6, -4);
    ctx.moveTo(centerX - 2, 0);
    ctx.lineTo(centerX - 6, 4);
    ctx.moveTo(0, centerY);
    ctx.lineTo(-4, centerY - 4);
    ctx.moveTo(0, centerY);
    ctx.lineTo(4, centerY - 4);
    ctx.stroke();

    ctx.restore();
  }

  /**
   * Рисует сетку для системы координат размером 40*40.
   * @private
   */
  function grid() {
    ctx.save();
    ctx.strokeStyle = '#d9b68f';
    ctx.lineWidth = 0.5;

    for (let i = 0; i < width; i += b) {
      ctx.beginPath();
      ctx.moveTo(i, 0);
      ctx.lineTo(i, height);
      ctx.stroke();
    }

    for (let i = 0; i < height; i += c) {
      ctx.beginPath();
      ctx.moveTo(0, i);
      ctx.lineTo(width, i);
      ctx.stroke();
    }

    ctx.restore();
  }

  /**
   * Рисует квадрат с центром в точке (х, у).
   *
   * @param {number} a - половина длины стороны квадрата (в условных единицах).
   * @param {number} x - координата Х центра квадрата.
   * @param {number} y - координата У центра квадрата.
   */
  function drawSquare(a, x, y) {
    const centerX = Math.floor(width / 2) + 0.5;
    const centerY = Math.floor(height / 2) + 0.5;

    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.scale(1, -1);
    ctx.strokeStyle = '#9A7951FF';
    ctx.lineWidth = 3;

    const left   = (x - a) * b;
    const right  = (x + a) * b;
    const top    = (y + a) * c;
    const bottom = (y - a) * c;

    ctx.beginPath();
    ctx.moveTo(left, top);
    ctx.lineTo(right, top);
    ctx.lineTo(right, bottom);
    ctx.lineTo(left, bottom);
    ctx.closePath();
    ctx.stroke();
    ctx.restore();
  }

  /**
   * Рисует закрашенную точку в заданных координатах.
   * @param {number} x - координата Х точки (в условных единицах).
   * @param {number} y - координата У точки (в условных единицах).
   */
  function drawPoint(x, y) {
    const centerX = Math.floor(width / 2) + 0.5;
    const centerY = Math.floor(height / 2) + 0.5;

    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.scale(1, -1);
    ctx.strokeStyle = '#9A7951FF';
    ctx.fillStyle = '#9A7951FF';
    ctx.lineWidth = 0.5;

    ctx.beginPath();
    ctx.arc(x * b, y * c, 5, 0, 2 * Math.PI);
    ctx.fill();
    ctx.restore();
  }

  /**
   * Рисует окружность с центром в точке (x, y).
   *
   * @param {number} r - радиус окружности (в условных единицах).
   * @param {number} x - координата X центра окружности.
   * @param {number} y - координата Y центра окружности.
   */
  function drawCircle(r, x, y){
    const centerX = Math.floor(width / 2) + 0.5;
    const centerY = Math.floor(height / 2) + 0.5;

    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.scale(1, -1);
    ctx.strokeStyle = '#9A7951FF';
    ctx.lineWidth = 3;

    ctx.beginPath();
    ctx.arc(x * b, y * c, r * b, 0, 2 * Math.PI);
    ctx.stroke();
    ctx.restore();
  }

  /**
   * Рисует треугольник по трём вершинам.
   *
   * @param {[number, number]} p1 - координаты первой вершины.
   * @param {[number, number]} p2 - координаты второй вершины.
   * @param {[number, number]} p3 - координаты третьей вершины.
   */
  function drawTriangle(p1, p2, p3) {
    const centerX = Math.floor(width / 2) + 0.5;
    const centerY = Math.floor(height / 2) + 0.5;

    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.scale(1, -1);
    ctx.strokeStyle = '#9A7951FF';
    ctx.lineWidth = 3;

    ctx.beginPath();
    ctx.moveTo(p1[0] * b, p1[1] * c);
    ctx.lineTo(p2[0] * b, p2[1] * c);
    ctx.lineTo(p3[0] * b, p3[1] * c);
    ctx.closePath();
    ctx.stroke();
    ctx.restore();
  }

  /**
   * Полностью перерисовывает систему координат: очищает canvas
   * и рисует координатную плоскость с сеткой.
   */
  function redraw() {
    coordinatePlane();
    grid();
  }

  return { canvas, ctx, width, height, b, c, drawSquare, redraw, drawPoint,drawCircle, drawTriangle };
}
