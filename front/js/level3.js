import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';

/**
 * Инициализирует интерактивный режим "Уровень 3": форма для ручного ввода
 * координат точки (x, y) с валидацией и отрисовкой на координатной
 * плоскости вместе с фиксированным треугольником.
 *
 * Ожидает в разметке элементы с id:
 * - `canvas-lv-three` — canvas для рисования
 * - `form-lv-three` — форма с полями `x`, `y`
 * - `button-lv-three` — кнопка отправки формы
 * - `fail--x--lv3` — элемент для текста ошибки поля `x`
 * - `fail--y--lv3` — элемент для текста ошибки поля `y`
 *
 * Если canvas, форма или кнопка не найдены на странице — функция
 * прерывается без ошибок.
 *
 * Валидация: оба поля обязательны, должны быть числами;
 * `x` и `y` — в диапазоне [-10, 10].
 * При успешной валидации рисует треугольник с вершинами
 * A(-3, 0), B(3, 0), C(0, 4) и точку с введёнными координатами.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
export function initLevel3() {
  const plane = createCanvas('canvas-lv-three');
  if (!plane) return;

  const form = document.getElementById('form-lv-three');
  const but = document.getElementById('button-lv-three');
  const failX = document.getElementById('fail--x--lv3');
  const failY = document.getElementById('fail--y--lv3');

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

    if(isInvalid) {
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

    failX.textContent = '';
    failY.textContent = '';

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

/**
 * Инициализирует режим проверки знаний "Уровень 3": пользователь вводит
 * число-ответ на заранее заданную задачу (треугольник с вершинами
 * A(-3, 0), B(3, 0), C(0, 4), правильный ответ x = -1.5), результат
 * сохраняется в историю и отображается модальное окно с результатом.
 *
 * Ожидает в разметке элементы с id:
 * - `canvas-lv-three` — canvas для рисования
 * - `form-lv3` — форма с полем `answer`
 * - `answer--lv3` — кнопка отправки ответа
 * - `answer-lv3` — элемент для текста ошибки поля `answer`
 * - `true-lv3` — модальное окно "верный ответ"
 * - `false-lv3` — модальное окно "неверный ответ"
 *
 * Использует модуль истории `createHistory('lv3')` для сохранения
 * каждой попытки ответа (значение + признак правильности).
 *
 * Если canvas не найден — функция прерывается без ошибок.
 */
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
