import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';

export function initLevel1() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;

  const form = document.getElementById('form-lv-one');
  const but = document.getElementById('button-lv-one');

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

    if (isInvalid) return;

    plane.drawSquare(a, x, y);
  });

  const canvas = document.getElementById('canvas-lv-one');
  if (canvas) {
    canvas.addEventListener('click', () => {
      alert('координатная плоскость по хорошему тут должна менять свой масштаб, но пока опустим этот момент');
    });
  }
}

export function level1() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;
  const form = document.getElementById('form-lv1');
  const answerLv = document.getElementById('answer--lv1');
  const answer = document.getElementById('answer');
  const modalTrue = document.getElementById('true');
  const modalFalse = document.getElementById('false');
  const history = createHistory('lv1');

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
      if(!Number.isFinite(x)) {
        answer.textContent = 'ответ должен быть числом';
        return;
      }

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
