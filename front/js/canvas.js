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
  ctx.moveTo(0, cy);
  ctx.lineTo(width, cy);
  ctx.moveTo(width - 6, cy - 6);
  ctx.lineTo(width, cy);
  ctx.lineTo(width - 6, cy + 6);
  ctx.moveTo(cx, 0);
  ctx.lineTo(cx, width);
  ctx.moveTo(cx - 6, 6);
  ctx.lineTo(cx, 0);
  ctx.lineTo(cx + 6, 6);
  ctx.stroke();

  ctx.font = "14px sans-serif";
  ctx.textAlign = "right";
  ctx.fillStyle = getComputedStyle(document.body).getPropertyValue("--text").trim();
  ctx.fillText("x", width - 4, cy + 19);
  ctx.fillText("y", cx - 10,  15);

  ctx.globalAlpha = 0.1;

  for(let i = 0; i < width; i+= (step)){
    ctx.beginPath();
    ctx.moveTo(0, i);
    ctx.lineTo(width, i);
    ctx.moveTo(i, 0);
    ctx.lineTo(i, width);
    ctx.stroke();
  }
  ctx.globalAlpha = 1;

  function area(r){
    const full = r * (step);
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

  function lv1(x, y, r, isCorrect) {
    sessionStorage.setItem("LAST_VERSION", JSON.stringify({x: x, y: y, r: r, ans: isCorrect}));
    ctx.strokeStyle = getComputedStyle(document.body).getPropertyValue("--text").trim();
    ctx.fillStyle = getComputedStyle(document.body).getPropertyValue("--text").trim();

    area(r);
    ctx.globalAlpha = 0.2;
    ctx.fill();
    ctx.globalAlpha = 1;
    ctx.stroke();

    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(cx - 6, cy - step * r);
    ctx.lineTo(cx + 6, cy - step * r);

    ctx.moveTo(cx - 6, cy + step * (r/2));
    ctx.lineTo(cx + 6, cy + step * (r/2));

    ctx.moveTo(cx - step * (r/2), cy - 6);
    ctx.lineTo(cx - step * (r/2), cy + 6);

    ctx.moveTo(cx + step * (r/2), cy - 6);
    ctx.lineTo(cx + step * (r/2), cy + 6);
    ctx.stroke();

    ctx.font = "14px sans-serif";
    ctx.textAlign = "center";
    ctx.fillText("R/2", cx + step * (r/2), cy  + 20);
    ctx.fillText("R/2", cx - step * (r/2), cy  + 20);
    ctx.fillText("R/2", cx - 15, cy  + step * (r/2) + 10);
    ctx.fillText("R", cx - 15, cy  - step * r + 10);

    if (x !== undefined && y !== undefined) {
      if(isCorrect) {
        ctx.fillStyle = getComputedStyle(document.body).getPropertyValue("--ok").trim();
      } else {
        ctx.fillStyle = getComputedStyle(document.body).getPropertyValue("--miss").trim();
      }

      ctx.beginPath();
      ctx.arc(cx + x * step, cy - y * step, 4, 0, Math.PI * 2);
      ctx.fill();
    }
  }


  return lv1;
}
