import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';

export function initLevel2() {
  const plane = createCanvas('canvas-lv-two');
  if (!plane) return;

  const form = document.getElementById('form-lv-two');
  const but = document.getElementById('button-lv-two');

  if (!form || !but) return;

  plane.redraw();

  but.addEventListener('click', (event) => {
    event.preventDefault();

    plane.redraw();

    const formData = new FormData(form);
    const object = Object.fromEntries(formData);

    const r = Number(object['r']);
    const x = Number(object['x']);
    const y = Number(object['y']);

    const isEmpty = (v) => v.trim() === '';
    const isInvalid =
      isEmpty(object['r']) || isEmpty(object['x']) || isEmpty(object['y']) ||
      !Number.isFinite(r) || !Number.isFinite(x) || !Number.isFinite(y) ||
      x > 10 || r > 10 || y > 10 ||
      x < -10 || y < -10 || r < 0;

    if (isInvalid) return;

    plane.drawCircle(r, x, y);
  });

  const canvas = document.getElementById('canvas-lv-two');
  if (canvas) {
    canvas.addEventListener('click', () => {
      alert('координатная плоскость по хорошему тут должна менять свой масштаб, но пока опустим этот момент');
    });
  }
}

export function level2() {
  const plane = createCanvas('canvas-lv-two');
  if (!plane) return;

  const form = document.getElementById('form-lv2');
  const answerLv = document.getElementById('answer--lv2');
  const answer = document.getElementById('answer-lv2');
  const modalTrue = document.getElementById('true-lv2');
  const modalFalse = document.getElementById('false-lv2');
  const history = createHistory('lv2');

  plane.redraw();

  if (answerLv) {
    answerLv.addEventListener('click', (event) => {
      event.preventDefault();
      plane.redraw();

      const formData = new FormData(form);
      const object = Object.fromEntries(formData);

      const value = object['answer'];

      if (!value || value.trim() === '') {
        answer.textContent = 'ответ должен быть заполнен';
        return;
      }

      const x = Number(value);
      if (!Number.isFinite(x)) {
        answer.textContent = 'ответ должен быть числом';
        return;
      }

      plane.drawCircle(4, 2, 3);
      plane.drawPoint(x, 4);

      const isCorrect = (x === 5); //todo посчитать решение

      history.add(value, isCorrect);

      if (isCorrect) {
        modalTrue.classList.add('active');
        return;
      }
      modalFalse.classList.add('active');
    });
  }
}
