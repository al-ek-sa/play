const button = document.getElementById('lv-one');
const modal = document.getElementById('teor');
const canvas = document.getElementById('canvas-lv-one');
const ctx = canvas.getContext("2d");
const form = document.getElementById('form-lv-one');
const but = document.getElementById('button-lv-one');
const height = canvas.height;
const width = canvas.width;

coordinate_plane(ctx, height, width);
button.addEventListener('click', (event) => {
  modal.classList.add('active');
});

but.addEventListener('click', (event) => {
  event.preventDefault();
  coordinate_plane(ctx, height, width);
  const  formData = new FormData(form);
  const object = Object.fromEntries(formData);
  const centerX = Math.floor(width/2) + 0.5;
  const centerY = Math.floor(height/2) + 0.5;
  const a = Number(object['a']);
  const x = Number(object['x']);
  const y = Number(object['y']);
  console.log(a, x, y)
  // ctx.beginPath();
  // ctx.moveTo();

});

function coordinate_plane(ctx, height, width){
  ctx.clearRect(0,0, width, height);
  ctx.save();
  ctx.strokeStyle = '#9A7951FF';
  const centerX = Math.floor(width/2) + 0.5;
  const centerY = Math.floor(height/2) + 0.5;
  ctx.translate(centerX, centerY);
  ctx.scale(1, -1);
  ctx.lineWidth = 1;

  ctx.beginPath();
  ctx.moveTo(0, centerY);
  ctx.lineTo(0, -centerY);
  ctx.stroke();

  ctx.beginPath();
  ctx.moveTo(centerX, 0);
  ctx.lineTo(-centerX, 0);
  ctx.stroke();

  ctx.beginPath();
  ctx.moveTo(centerX-2, 0);
  ctx.lineTo(centerX-6, -4);
  ctx.moveTo(centerX-2, 0);
  ctx.lineTo(centerX-6, 4);
  ctx.moveTo(0, centerY);
  ctx.lineTo(-4, centerY-4);
  ctx.moveTo(0, centerY);
  ctx.lineTo(4, centerY-4);
  ctx.stroke();
  ctx.restore();
}
