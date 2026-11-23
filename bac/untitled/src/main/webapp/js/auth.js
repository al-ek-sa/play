import { canvas as drawCanvas } from './canvas.js';

export function auth() {
  const form = document.getElementById("areaForm");

  if (!form) {
    return;
  }

  const draw = drawCanvas("areaCanvas");

  if (!draw) {
    return;
  }

  draw(undefined, undefined, 5, false, 0, 0, 0);

  const offsetYInput = document.getElementById("offsetY");
  const offsetXInput = document.getElementById("offsetX");
  const rotationInput = document.getElementById("rotation");

  function updateCanvas() {
    const offsetY = Number(offsetYInput.value) || 0;
    const offsetX = Number(offsetXInput.value) || 0;
    const rotation = Number(rotationInput.value) || 0;

    draw(
      undefined,
      undefined,
      5,
      false,
      offsetX,
      offsetY,
      rotation
    );
  }

  offsetYInput.addEventListener("input", updateCanvas);
  offsetXInput.addEventListener("input", updateCanvas);
  rotationInput.addEventListener("input", updateCanvas);

  form.addEventListener("submit", async event => {
    event.preventDefault();

    const offsetY = Number(offsetYInput.value);
    const offsetX = Number(offsetXInput.value);
    const rotation = Number(rotationInput.value);

    const response = await fetch("/api/admin/area", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        offsetX,
        offsetY,
        rotation
      })
    });

    const result = await response.json();

    if (!response.ok) {
      alert(result.error);
      return;
    }
  });
}
