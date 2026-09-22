export function createCanvas(id) {
  const canvas = document.getElementById(id);
  if(!canvas) return null;

  const ctx = canvas.getContext('2d');
  const width = canvas.width;
  const height = canvas.height;
  const b = width / 40;
  const c = height / 40;

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

  function redraw() {
    coordinatePlane();
    grid();
  }

  return { canvas, ctx, width, height, b, c, coordinatePlane, grid, drawSquare, redraw, drawPoint,drawCircle, drawTriangle };
}
