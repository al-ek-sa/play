let scale = 1;
let panX = 0;
let panY = 0;

const LIMIT = 40;

try {
  const savedScale = Number(sessionStorage.getItem("SCALE"));

  if (Number.isFinite(savedScale) && savedScale > 0) {
    scale = savedScale;
  }

  const savedPan = JSON.parse(sessionStorage.getItem("OFFSET"));

  if (
    savedPan &&
    Number.isFinite(savedPan.x) &&
    Number.isFinite(savedPan.y)
  ) {
    panX = savedPan.x;
    panY = savedPan.y;
  }
} catch {
  scale = 1;
  panX = 0;
  panY = 0;
}

function color(name) {
  return getComputedStyle(document.body)
    .getPropertyValue(name)
    .trim();
}

function lastVersion() {
  try {
    return JSON.parse(sessionStorage.getItem("LAST_VERSION"));
  } catch {
    return null;
  }
}

export function canvas(name) {
  const canvasElement = document.getElementById(name);

  if (!canvasElement) {
    console.error(`Canvas "${name}" не найден`);
    return null;
  }

  const ctx = canvasElement.getContext("2d");

  if (!ctx) {
    console.error("Не удалось получить Canvas context");
    return null;
  }

  function redraw() {
    const last = lastVersion();

    if (last) {
      lv1(
        last.x,
        last.y,
        last.r,
        last.ans,
        last.offsetX,
        last.offsetY,
        last.rotation
      );
    } else {
      lv1(undefined, undefined, 5, false, 0, 0, 0);
    }
  }

  if (!canvasElement.dataset.zoom) {
    canvasElement.dataset.zoom = "true";

    canvasElement.addEventListener("wheel", event => {
      event.preventDefault();

      scale *= event.deltaY < 0 ? 1.1 : 1 / 1.1;

      scale = Math.max(0.25, Math.min(8, scale));

      try {
        sessionStorage.setItem("SCALE", String(scale));
      } catch {
        console.warn("Не удалось сохранить масштаб");
      }

      redraw();
    }, { passive: false });

    let dragging = false;

    let lastMouseX = 0;
    let lastMouseY = 0;

    canvasElement.style.cursor = "grab";
    canvasElement.style.touchAction = "none";

    canvasElement.addEventListener("pointerdown", event => {
      dragging = true;

      lastMouseX = event.clientX;
      lastMouseY = event.clientY;

      canvasElement.setPointerCapture(event.pointerId);

      canvasElement.style.cursor = "grabbing";
    });


    canvasElement.addEventListener("pointermove", event => {
      if (!dragging) {
        return;
      }

      const rect = canvasElement.getBoundingClientRect();

      const kx = canvasElement.width / rect.width;
      const ky = canvasElement.height / rect.height;

      panX += (event.clientX - lastMouseX) * kx;
      panY += (event.clientY - lastMouseY) * ky;

      lastMouseX = event.clientX;
      lastMouseY = event.clientY;

      redraw();
    });


    function stopDragging() {
      if (!dragging) {
        return;
      }

      dragging = false;

      canvasElement.style.cursor = "grab";

      try {
        sessionStorage.setItem(
          "OFFSET",
          JSON.stringify({
            x: panX,
            y: panY
          })
        );
      } catch {
        console.warn("sessionStorage недоступен");
      }
    }

    canvasElement.addEventListener("pointerup", stopDragging);
    canvasElement.addEventListener("pointercancel", stopDragging);
  }


  function lv1(
    x,
    y,
    r,
    isCorrect,
    areaOffsetX = 0,
    areaOffsetY = 0,
    rotation = 0
  ) {
    const width = canvasElement.width;
    const height = canvasElement.height;

    const step = (width / 40) * scale;

    const cx = width / 2 + panX;
    const cy = height / 2 + panY;

    const full = r * step;
    const half = (r / 2) * step;

    try {
      sessionStorage.setItem(
        "LAST_VERSION",
        JSON.stringify({
          x,
          y,
          r,
          ans: isCorrect,
          offsetX: areaOffsetX,
          offsetY: areaOffsetY,
          rotation
        })
      );
    } catch {
      console.warn("sessionStorage недоступен");
    }

    ctx.clearRect(0, 0, width, height);

    ctx.lineWidth = 1;
    ctx.globalAlpha = 1;
    ctx.setLineDash([]);

    ctx.strokeStyle = color("--text");
    ctx.fillStyle = color("--text");
    ctx.globalAlpha = 0.1;

    ctx.beginPath();

    const startX = cx - Math.floor(cx / step) * step;
    const startY = cy - Math.floor(cy / step) * step;

    for (let gx = startX; gx <= width; gx += step) {
      ctx.moveTo(gx, 0);
      ctx.lineTo(gx, height);
    }

    for (let gy = startY; gy <= height; gy += step) {
      ctx.moveTo(0, gy);
      ctx.lineTo(width, gy);
    }
    ctx.stroke();
    ctx.globalAlpha = 1;

    ctx.beginPath();

    ctx.moveTo(0, cy);
    ctx.lineTo(width, cy);

    ctx.moveTo(width - 8, cy - 5);
    ctx.lineTo(width, cy);
    ctx.lineTo(width - 8, cy + 5);

    ctx.moveTo(cx, height);
    ctx.lineTo(cx, 0);

    ctx.moveTo(cx - 5, 8);
    ctx.lineTo(cx, 0);
    ctx.lineTo(cx + 5, 8);

    ctx.stroke();

    ctx.font = "14px Arial";

    ctx.textAlign = "right";
    ctx.fillText("x", width - 5, cy + 20);

    ctx.textAlign = "left";
    ctx.fillText("y", cx + 10, 15);

    const radians = rotation * Math.PI / 180;

    const dx = areaOffsetX * step;
    const dy = -areaOffsetY * step;

    const areaCx = cx + dx;
    const areaCy = cy + dy;

    ctx.save();

    ctx.translate(areaCx, areaCy);
    ctx.rotate(-radians);

    function fillArea(draw) {
      draw();

      ctx.globalAlpha = 0.2;
      ctx.fill();

      ctx.globalAlpha = 1;
      ctx.setLineDash([]);

      ctx.stroke();
    }

    function rectangle() {
      ctx.beginPath();

      ctx.rect(
        0,
        -full,
        full,
        full
      );
    }

    function triangle() {
      ctx.beginPath();

      ctx.moveTo(0, 0);
      ctx.lineTo(half, 0);
      ctx.lineTo(0, half);

      ctx.closePath();
    }

    function quarterCircle() {
      ctx.beginPath();

      ctx.moveTo(0, 0);

      ctx.arc(
        0,
        0,
        full,
        Math.PI / 2,
        Math.PI,
        false
      );

      ctx.closePath();
    }


    fillArea(rectangle);
    fillArea(triangle);
    fillArea(quarterCircle);

    function ticks() {
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

        ctx.moveTo(shift, -6);
        ctx.lineTo(shift, 6);

        ctx.moveTo(-6, -shift);
        ctx.lineTo(6, -shift);
      }
      ctx.stroke();
      ctx.lineWidth = 1;

      ctx.font = "14px Arial";

      for (const mark of marks) {
        const shift = mark.value * step;
        ctx.textAlign = "center";

        ctx.fillText(
          mark.text,
          shift,
          20
        );

        ctx.textAlign = "right";

        ctx.fillText(
          mark.text,
          -10,
          -shift + 5
        );
      }
    }

    ticks();

    ctx.restore();
    if (
      x !== undefined &&
      y !== undefined &&
      Number.isFinite(x) &&
      Number.isFinite(y)
    ) {
      ctx.fillStyle = isCorrect
        ? color("--ok")
        : color("--miss");

      ctx.beginPath();

      ctx.arc(
        cx + x * step,
        cy - y * step,
        4,
        0,
        Math.PI * 2
      );

      ctx.fill();
    }

    ctx.globalAlpha = 1;
    ctx.setLineDash([]);
    ctx.lineWidth = 1;
  }

  return lv1;
}
