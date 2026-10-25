let scale = 1;
let offsetX = 0;
let offsetY = 0;
const LIMIT = 40;

try {
  const saved = Number(sessionStorage.getItem("SCALE"));
  if (Number.isFinite(saved) && saved > 0) scale = saved;
  const offset = JSON.parse(sessionStorage.getItem("OFFSET"));
  if (offset && Number.isFinite(offset.x) && Number.isFinite(offset.y)) {
    offsetX = offset.x;
    offsetY = offset.y;
  }
} catch {
  scale = 1;
  offsetX = 0;
  offsetY = 0;
}

function color(name) {
  return getComputedStyle(document.body).getPropertyValue(name).trim();
}

function lastVersion() {
  try {
    return JSON.parse(sessionStorage.getItem("LAST_VERSION"));
  } catch {
    return null;
  }
}

export function canvas(name) {
  const canvas = document.getElementById(`${name}`);
  const ctx = canvas.getContext("2d");

  function redraw() {
    const last = lastVersion();
    if (last && Number.isFinite(last.r)) {
      lv1(last.x, last.y, last.r, last.ans);
    }
  }

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
      redraw();
    }, { passive: false });

    let dragging = false;
    let lastX = 0;
    let lastY = 0;
    canvas.style.cursor = "grab";
    canvas.style.touchAction = "none";

    canvas.addEventListener("pointerdown", event => {
      dragging = true;
      lastX = event.clientX;
      lastY = event.clientY;
      canvas.setPointerCapture(event.pointerId);
      canvas.style.cursor = "grabbing";
    });

    canvas.addEventListener("pointermove", event => {
      if (!dragging) return;
      const k = canvas.width / canvas.getBoundingClientRect().width;
      offsetX += (event.clientX - lastX) * k;
      offsetY += (event.clientY - lastY) * k;
      lastX = event.clientX;
      lastY = event.clientY;
      redraw();
    });

    function stop(){
      if(!dragging) return;
      dragging = false;
      canvas.style.cursor = "grab";
      try {
        sessionStorage.setItem("OFFSET", JSON.stringify({x: offsetX, y: offsetY}));
      } catch {
        console.warn("sessionStorage недоступен");
      }
    }

    canvas.addEventListener("pointerup", stop);
    canvas.addEventListener("pointercancel", stop);
  }

  function lv1(x, y, r, isCorrect) {
    const width = canvas.width;
    const step = width / 40 * scale;
    const max = LIMIT * step;
    offsetX = Math.min(max, Math.max(-max, offsetX));
    offsetY = Math.min(max, Math.max(-max, offsetY));
    const cx = width / 2 + offsetX;
    const cy = width / 2 + offsetY;

    try {
      sessionStorage.setItem("LAST_VERSION", JSON.stringify({x: x, y: y, r: r, ans: isCorrect}));
    } catch {
      console.warn("sessionStorage недоступен");
    }

    ctx.clearRect(0, 0, width, canvas.height);
    ctx.lineWidth = 1;
    ctx.setLineDash([]);
    ctx.globalAlpha = 1;
    ctx.strokeStyle = color("--text");
    ctx.fillStyle = color("--text");

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
    ctx.fillText("x", width - 4, cy + 19);
    ctx.fillText("y", cx - 10, 15);

    ctx.globalAlpha = 0.1;
    const startX = cx - Math.floor(cx / step) * step;
    const startY = cy - Math.floor(cy / step) * step;
    ctx.beginPath();
    for(let i = startX; i <= width; i += step){
      ctx.moveTo(i, 0);
      ctx.lineTo(i, width);
    }
    for(let i = startY; i <= width; i += step){
      ctx.moveTo(0, i);
      ctx.lineTo(width, i);
    }
    ctx.stroke();
    ctx.globalAlpha = 1;

    function lab1Area(){
      const full = r * step;
      const half = (r / 2) * step;

      ctx.beginPath();
      ctx.moveTo(cx, cy);
      ctx.lineTo(cx + half, cy);
      ctx.lineTo(cx, cy - full);
      ctx.closePath();

      ctx.rect(cx - half, cy, half, full);

      ctx.moveTo(cx, cy);
      ctx.arc(cx, cy, full, 0, Math.PI / 2);
      ctx.closePath();
    }

    function lab2Area(){
      const full = r * step;
      const half = (r / 2) * step;

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

    function fillArea(draw, alpha, dash){
      draw();
      ctx.globalAlpha = alpha;
      ctx.fill();
      ctx.globalAlpha = 1;
      ctx.setLineDash(dash);
      ctx.stroke();
      ctx.setLineDash([]);
    }

    function ticks(){
      const marks = [
        { value: r, text: "R" },
        { value: r / 2, text: "R/2" },
        { value: -r / 2, text: "-R/2" },
        { value: -r, text: "-R" }
      ];

      ctx.lineWidth = 2;
      ctx.beginPath();
      for (const mark of marks) {
        const shift = mark.value * step;
        ctx.moveTo(cx + shift, cy - 6);
        ctx.lineTo(cx + shift, cy + 6);
        ctx.moveTo(cx - 6, cy - shift);
        ctx.lineTo(cx + 6, cy - shift);
      }
      ctx.stroke();
      ctx.lineWidth = 1;

      ctx.font = "14px sans-serif";
      for (const mark of marks) {
        const shift = mark.value * step;
        ctx.textAlign = "center";
        ctx.fillText(mark.text, cx + shift, cy + 20);
        ctx.textAlign = "right";
        ctx.fillText(mark.text, cx - 10, cy - shift + 5);
      }
    }

    fillArea(lab1Area, 0.12, [5, 4]);
    fillArea(lab2Area, 0.2, []);
    ticks();

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
