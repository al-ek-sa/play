const button = document.getElementById('lv-one');
const modal = document.getElementById('th-square');
const canvas = document.getElementById('canvas-lv-one');
const ctx = canvas.getContext("2d");
const form = document.getElementById('form-lv-one');
const but = document.getElementById('button-lv-one');
const answer_one_lv = document.getElementById('answer-lv1');
const lv1 = document.getElementById('lv1');
const height = canvas.height;
const width = canvas.width;
const c = height/40;
const b = width/40;
const history_1lv = document.getElementById('history--1lv');
const exit_1lv = document.getElementById('exit--1lv');

// exit_1lv.addEventListener('click', event =>{
//   event.preventDefault();
//   window.location.href='index.html';
// });
//
// history_1lv.addEventListener('click', (event) =>{
//   event.preventDefault();
//   window.location.href='1lv--history.html';
// });



coordinate_plane(ctx, height, width);

button.addEventListener('click', (event) => {
  event.preventDefault();
  modal.classList.add('active');
});
lv1.addEventListener('click', (event) => {
  event.preventDefault();
  window.location.href='lv1.html';
});

but.addEventListener('click', (event) => {
  event.preventDefault();
  coordinate_plane(ctx, height, width);
  a1();
  const  formData = new FormData(form);
  const object = Object.fromEntries(formData);
  const centerX = Math.floor(width/2) + 0.5;
  const centerY = Math.floor(height/2) + 0.5;
  ctx.strokeStyle = '#9A7951FF';
  ctx.save();
  ctx.translate(centerX, centerY);
  ctx.scale(1, -1);
  const a = Number(object['a']);
  const x = Number(object['x']);
  const y = Number(object['y']);
  console.log(a, x, y);
//todo нужно чтобы размер мог свапаться, то есть не было ограничений по рисунку просто размер координатной плоскости сделать интерактивным
  if(object['a'].trim() === '' || object['x'].trim() === '' || object['y'].trim() === '' ||
      !Number.isFinite(a) || !Number.isFinite(x) || !Number.isFinite(y) ||
      x > 10 || a > 10 || y > 10 || x < -10 || y < -10 || a < 0){

    return;
  }
  const left   = (x - a) * b;
  const right  = (x + a) * b;
  const top    = (y + a) * c;
  const bottom = (y - a) * c;
  ctx.lineWidth = 3;

  ctx.beginPath();
  ctx.moveTo(left,  top);
  ctx.lineTo(right, top);
  ctx.lineTo(right, bottom);
  ctx.lineTo(left,  bottom);
  ctx.closePath();
  ctx.stroke();
  ctx.restore();
});

function coordinate_plane(ctx, height, width){
  ctx.clearRect(0,0, width, height);
  ctx.save();
  ctx.strokeStyle = '#9A7951FF';
  const centerX = Math.floor(width/2) + 0.5;
  const centerY = Math.floor(height/2) + 0.5;
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
//todo перерисовать можно в 6 строк нарисовать в теории
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

function a1(){
  ctx.strokeStyle = "#d9b68f";
  ctx.translate(0, 0);
  ctx.lineWidth = 0.5;

  for(let i = 0; i < width; i += b){
    ctx.beginPath();
    ctx.moveTo(i, 0);
    ctx.lineTo(i, height);
    ctx.stroke();
  }

  for(let i = 0; i < width; i += c){
    ctx.beginPath();
    ctx.moveTo(0, i);
    ctx.lineTo(width, i);
    ctx.stroke();
  }
  ctx.restore();
}
 a1();

canvas.addEventListener('click', (event) => {
  alert('координатная плоскость по хорошему тут должна менять свой масштаб, но пока опустим этот момент');
});

function drawCoordinatePlane(event) {
  event.preventDefault();
}

answer_one_lv.addEventListener('submit', (event) => {
  event.preventDefault();

})
