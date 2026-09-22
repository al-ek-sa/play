import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';

export function initLevel3() {
  const plane = createCanvas('canvas-lv-three');
  if (!plane) return;

  const form = document.getElementById('form-lv-three');
  const but = document.getElementById('button-lv-three');

  if (!form || !but) return;

  plane.redraw();

  but.addEventListener('click', (event) => {
    event.preventDefault();

    plane.redraw();

    const formData = new FormData(form);
    const object = Object.fromEntries(formData);

    const x = Number(object['x']);
    const y = Number(object['y']);

    const isEmpty = (v) => v.trim() === '';
    const isInvalid =
      isEmpty(object['x']) || isEmpty(object['y']) ||
      !Number.isFinite(x) || !Number.isFinite(y) ||
      x > 10 || y > 10 ||
      x < -10 || y < -10;

    if (isInvalid) return;

    plane.drawTriangle([-3, 0], [3, 0], [0, 4]);
    plane.drawPoint(x, y);
  });

  const canvas = document.getElementById('canvas-lv-three');
  if (canvas) {
    canvas.addEventListener('click', () => {
      alert('координатная плоскость по хорошему тут должна менять свой масштаб, но пока опустим этот момент');
    });
  }
}

export function level3() {
  const plane = createCanvas('canvas-lv-three');
  if (!plane) return;

  const form = document.getElementById('form-lv3');
  const answerLv = document.getElementById('answer--lv3');
  const answer = document.getElementById('answer-lv3');
  const modalTrue = document.getElementById('true-lv3');
  const modalFalse = document.getElementById('false-lv3');
  const history = createHistory('lv3');

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

      plane.drawTriangle([-3, 0], [3, 0], [0, 4]);
      plane.drawPoint(x, 2);

      const isCorrect = (x === -1.5);

      history.add(value, isCorrect);

      if (isCorrect) {
        modalTrue.classList.add('active');
        return;
      }
      modalFalse.classList.add('active');
    });
  }
}
