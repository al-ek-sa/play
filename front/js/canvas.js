export function canvas(name) {
  const canvas = document.getElementById(`${name}`);
  const ctx = canvas.getContext("2d");
  const width = canvas.width;
  const step = width / 40;
  const cx = width / 2;
  const cy = width / 2;

  ctx.clearRect(0, 0, width, canvas.height);
  ctx.strokeStyle = getComputedStyle(document.body).getPropertyValue("--text").trim();

  ctx.beginPath();
  ctx.moveTo(0, width / 2);
  ctx.lineTo(width, width / 2);
  ctx.moveTo(width - 6, width / 2 - 6);
  ctx.lineTo(width, width / 2);
  ctx.lineTo(width - 6, width / 2 + 6);
  ctx.moveTo(width / 2, 0);
  ctx.lineTo(width / 2, width);
  ctx.moveTo(width / 2 - 6, 6);
  ctx.lineTo(width / 2, 0);
  ctx.lineTo(width / 2 + 6, 6);
  ctx.stroke();

  ctx.globalAlpha = 0.1;
  for(let i = 0; i < width; i+= (width/40)){
    ctx.beginPath();
    ctx.moveTo(0, i);
    ctx.lineTo(width, i);
    ctx.moveTo(i, 0);
    ctx.lineTo(i, width);
    ctx.stroke();
  }
  ctx.globalAlpha = 1;

  function area(r){
    const full = r * (width /40);
    const half = (r/2) * step;

    ctx.beginPath();
    ctx.rect(cx, cy - full, half, full);

    ctx.moveTo(cx, cy);
    ctx.lineTo(cx + half, cy);
    ctx.lineTo(cx, cy + half);
    ctx.closePath();

    ctx.moveTo(cx, cy);
    ctx.arc(cx, cy, half, Math.PI / 2, Math.PI);
    ctx.closePath();

  }

  function lv1(x, y, r) {
    ctx.strokeStyle = "green";
    ctx.fillStyle = "green";

    area(r);
    ctx.globalAlpha = 0.2;
    ctx.fill();
    ctx.globalAlpha = 1;
    ctx.stroke();

    if (x !== undefined && y !== undefined) {
      ctx.fillStyle = "blue";
      ctx.beginPath();
      ctx.arc(cx + x * step, cy - y * step, 4, 0, Math.PI * 2);
      ctx.fill();
    }

    ctx.strokeStyle = "blue";
  }


  return lv1;
}
