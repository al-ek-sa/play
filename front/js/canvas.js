let scale = 1;

try {
  const saved = Number(sessionStorage.getItem("SCALE"));
  if (Number.isFinite(saved) && saved > 0) scale = saved;
} catch {
  scale = 1;
}


function redraw(name){
  const lv1 = canvas(name);

  let last = null;
  try {
    last = JSON.parse(sessionStorage.getItem("LAST_VERSION"));
  } catch {
    last = null;
    lv1(0, 0, 15, true);
  }
  if (last && Number.isFinite(last.r)) {
    lv1(last.x, last.y, last.r, last.ans);
  }
}

export function canvas(name) {
  const canvas = document.getElementById(`${name}`);
  const ctx = canvas.getContext("2d");
  const width = canvas.width;
  const step = width / 40 * scale;
  const cx = width / 2;
  const cy = width / 2;

  if (!canvas.dataset.zoom) {
    canvas.dataset.zoom = "on";
    canvas.addEventListener("wheel", (event) => {
      event.preventDefault();
      scale = event.deltaY < 0 ? scale * 1.1 : scale / 1.1;
      scale = Math.min(8, Math.max(0.25, scale));
      try {
        sessionStorage.setItem("SCALE", String(scale));
      } catch {
        console.warn("sessionStorage недоступен");
      }
      redraw(name);
    });
  }


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

  for(let i = step; i <= cx; i+= step){
    ctx.beginPath();
    ctx.moveTo(0, cy - i);
    ctx.lineTo(width, cy - i);
    ctx.moveTo(0, cy + i);
    ctx.lineTo(width, cy + i);
    ctx.moveTo(cx - i, 0);
    ctx.lineTo(cx - i, width);
    ctx.moveTo(cx + i, 0);
    ctx.lineTo(cx + i, width);
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
