export function canvas() {
  const canvas = document.getElementById("canvas");
  const ctx = canvas.getContext("2d");
  const width = canvas.width;

  ctx.clearRect(0, 0, width, canvas.height);
  ctx.strokeStyle = getComputedStyle(document.body).getPropertyValue("--text").trim();

  ctx.beginPath();
  ctx.moveTo(0, width / 2);
  ctx.lineTo(width, width / 2);
  ctx.moveTo(width / 2, 0);
  ctx.lineTo(width / 2, width);
  ctx.stroke();
}
