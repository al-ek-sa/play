import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';

export function initLevel1() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;

  const form = document.getElementById('form-lv-one');
  const but = document.getElementById('button-lv-one');
  const failA = document.getElementById('fail--a--lv1');
  const failX = document.getElementById('fail--x--lv1');
  const failY = document.getElementById('fail--y--lv1');

  if (!form || !but) return;

  plane.redraw();

  but.addEventListener('click', (event) => {
    event.preventDefault();

    plane.redraw();

    const formData = new FormData(form);
    const object = Object.fromEntries(formData);

    const a = Number(object['a']);
    const x = Number(object['x']);
    const y = Number(object['y']);

    const isEmpty = (v) => v.trim() === '';
    const isInvalid =
      isEmpty(object['a']) || isEmpty(object['x']) || isEmpty(object['y']) ||
      !Number.isFinite(a) || !Number.isFinite(x) || !Number.isFinite(y) ||
      x > 10 || a > 10 || y > 10 ||
      x < -10 || y < -10 || a < 0;

    if(isInvalid) {
      if (!Number.isFinite(a)){
        failA.textContent = 'Поле должно быть числовым';
      } else if(isEmpty(object['a'])) {
        failA.textContent = 'Поле обязательно к заполнению';
      } else if (a > 10) {
        failA.textContent = 'Поле должно быть не больше 10';
      } else if (a < 0) {
        failA.textContent = 'Поле должно быть не меньше 0';
      } else {
        failA.textContent = '';
      }

      if (!Number.isFinite(x)){
        failX.textContent = 'Поле должно быть числовым';
      } else if(isEmpty(object['x'])) {
        failX.textContent = 'Поле обязательно к заполнению';
      } else if (x > 10) {
        failX.textContent = 'Поле должно быть не больше 10';
      } else if (x < -10) {
        failX.textContent = 'Поле должно быть не меньше -10';
      } else {
        failX.textContent = '';
      }

      if (!Number.isFinite(y)){
        failY.textContent = 'Поле должно быть числовым';
      } else if(isEmpty(object['y'])) {
        failY.textContent = 'Поле обязательно к заполнению';
      } else if (y > 10) {
        failY.textContent = 'Поле должно быть не больше 10';
      } else if (y < -10) {
        failY.textContent = 'Поле должно быть не меньше -10';
      } else {
        failY.textContent = '';
      }

      return;
    }
    failA.textContent = '';
    failX.textContent = '';
    failY.textContent = '';

    plane.drawSquare(a, x, y);
  });

  const canvas = document.getElementById('canvas-lv-one');
  if (canvas) {
    canvas.addEventListener('click', (event) => {
      // const rect = canvas.getBoundingClientRect();
      // const x = event.clientX - rect.left;
      // const y = event.clientY - rect.top;
      // plane.drawPoint(x/40, y/40);
       alert('масштабирование нудно добавить');
    });
  }
}

export function level1() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;
  const form = document.getElementById('form-lv1');
  const answerLv = document.getElementById('answer--lv1');
  const modalTrue = document.getElementById('true');
  const modalFalse = document.getElementById('false');
  const answerFail = document.getElementById('fail--answer--lv1');
  const history = createHistory('lv1');

  plane.redraw();

  if (answerLv) {
    answerLv.addEventListener('click', (event) => {
      event.preventDefault();
      plane.redraw();

      const formData = new FormData(form);
      const object = Object.fromEntries(formData);

      const value = object['answer'];

      const isEmpty = (v) => v.trim() === '';
      const x = Number(value);
      if(isEmpty(object['answer'])) {
        answerFail.textContent = 'Поле должно быть заполнено';
        return;
      } else if(!Number.isFinite(x)) {
        answerFail.textContent = 'Поле должно быть заполнено численным значением';
        return;
      }

      answerFail.textContent = '';

      plane.drawSquare(3, 2, 3);
      plane.drawPoint(x, 4);

      const isCorrect = (x === 5 || x === -1);

      history.add(value, isCorrect);

      if (isCorrect) {
        modalTrue.classList.add('active');
        return;
      }
      modalFalse.classList.add('active');
    });
  }
}
