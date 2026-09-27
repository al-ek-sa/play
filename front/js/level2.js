import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';

/**
 * Инициализирует интерактивный режим "Уровень 2": форма для ручного ввода
 * параметров круга (r, x, y) с валидацией и отрисовкой на координатной
 * плоскости.
 *
 * Ожидает в разметке элементы с id:
 * - `canvas-lv-two` — canvas для рисования
 * - `form-lv-two` — форма с полями `r`, `x`, `y`
 * - `button-lv-two` — кнопка отправки формы
 * - `fail--r--lv2` — элемент для текста ошибки поля `r`
 * - `fail--x--lv2` — элемент для текста ошибки поля `x`
 * - `fail--y--lv2` — элемент для текста ошибки поля `y`
 *
 * Если canvas, форма или кнопка не найдены на странице — функция
 * прерывается без ошибок.
 *
 * Валидация: все три поля обязательны, должны быть числами;
 * `r` — в диапазоне [0, 10]; `x` и `y` — в диапазоне [-10, 10].
 * При успешной валидации рисует круг с данными параметрами.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
export function initLevel2() {
  const plane = createCanvas('canvas-lv-two');
  if (!plane) return;

  const form = document.getElementById('form-lv-two');
  const but = document.getElementById('button-lv-two');
  const failR = document.getElementById('fail--r--lv2');
  const failX = document.getElementById('fail--x--lv2');
  const failY = document.getElementById('fail--y--lv2');

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

    if(isInvalid) {
      if (!Number.isFinite(r)){
        failR.textContent = 'Поле должно быть числовым';
      } else if(isEmpty(object['r'])) {
        failR.textContent = 'Поле обязательно к заполнению';
      } else if (r > 10) {
        failR.textContent = 'Поле должно быть не больше 10';
      } else if (r < 0) {
        failR.textContent = 'Поле должно быть не меньше 0';
      } else {
        failR.textContent = '';
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
    failR.textContent = '';
    failX.textContent = '';
    failY.textContent = '';

    plane.drawCircle(r, x, y);
  });

  const canvas = document.getElementById('canvas-lv-two');
  if (canvas) {
    canvas.addEventListener('click', () => {
      alert('координатная плоскость по хорошему тут должна менять свой масштаб, но пока опустим этот момент');
    });
  }
}

/**
 * Инициализирует режим проверки знаний "Уровень 2": пользователь вводит
 * число-ответ на заранее заданную задачу (круг с центром (2, 3),
 * радиусом 4, правильный ответ x = 5), результат сохраняется в историю
 * и отображается модальное окно с результатом.
 *
 * Ожидает в разметке элементы с id:
 * - `canvas-lv-two` — canvas для рисования
 * - `form-lv2` — форма с полем `answer`
 * - `answer--lv2` — кнопка отправки ответа
 * - `answer-lv2` — элемент для текста ошибки поля `answer`
 * - `true-lv2` — модальное окно "верный ответ"
 * - `false-lv2` — модальное окно "неверный ответ"
 *
 * Использует модуль истории `createHistory('lv2')` для сохранения
 * каждой попытки ответа (значение + признак правильности).
 *
 * Если canvas не найден — функция прерывается без ошибок.
 */
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
