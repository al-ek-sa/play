import { createCanvas } from './canvas.js';
import { createHistory } from './history.js';
import { validateNumber } from './validation.js';

/**
 * Инициализирует интерактивный режим "Уровень 1": форма для ручного ввода
 * параметров квадрата (a, x, y) с валидацией и отрисовкой на координатной
 * плоскости.
 *
 * Ожидает в разметке элементы с id:
 * - `canvas-lv-one` — canvas для рисования
 * - `form-lv-one` — форма с полями `a`, `x`, `y`
 * - `button-lv-one` — кнопка отправки формы
 * - `fail--a--lv1` — элемент для текста ошибки поля `a`
 * - `fail--x--lv1` — элемент для текста ошибки поля `x`
 * - `fail--y--lv1` — элемент для текста ошибки поля `y`
 *
 * Если canvas, форма или кнопка не найдены на странице — функция
 * прерывается без ошибок.
 *
 * Валидация: диапазон изменен после нового функционала(условие в html нужно переписать)
 * При успешной валидации рисует квадрат с данными параметрами.
 */
export function initLevel1() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;

  const form = document.getElementById('form-lv-one');
  const but = document.getElementById('button-lv-one');
  const failA = document.getElementById('fail--a--lv1');
  const failX = document.getElementById('fail--x--lv1');
  const failY = document.getElementById('fail--y--lv1');

  if (!form || !but) return;

  form.addEventListener('submit', (event) => {
    event.preventDefault();
  });

  plane.redraw();

  but.addEventListener('click', (event) => {
    event.preventDefault();
    event.stopPropagation();

    const formData = new FormData(form);
    const object = Object.fromEntries(formData);
    const limit = plane.maxRange;

    const errA = validateNumber(object['a'], 0, limit);
    const errX = validateNumber(object['x'], -limit, limit);
    const errY = validateNumber(object['y'], -limit, limit);

    failA.textContent = errA;
    failX.textContent = errX;
    failY.textContent = errY;

    if (errA || errX || errY) return;

    const a = Number(object['a']);
    const x = Number(object['x']);
    const y = Number(object['y']);

    plane.clearShapes();
    plane.drawSquare(a / 2, x, y);
    plane.focusOn(x, y, a / 2);
  });
}

/*todo на лабораторной кнопки были рабочие поэтому пока не убирала код(в html кнопки убраны), потом вынести на канвас в виде + и -*/
export function initScaleButtons() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;

  const lv1max = document.getElementById('lv1-1');
  const lv1min = document.getElementById('lv1-2');

  if (lv1max) {
    lv1max.addEventListener('click', (event) => {
      event.preventDefault();
      event.stopPropagation();
      plane.setScale(plane.getScale() * 1.1);
    });
  }

  if (lv1min) {
    lv1min.addEventListener('click', (event) => {
      event.preventDefault();
      event.stopPropagation();
      plane.setScale(plane.getScale() * 0.9);
    });
  }
}

/**
 * Инициализирует режим проверки знаний "Уровень 1": пользователь вводит
 * число-ответ на заранее заданную задачу (квадрат с a=3, центром (2, 3),
 * правильные ответы x=5 или x=-1), результат сохраняется в историю
 * и отображается модальное окно с результатом.
 *
 * Ожидает в разметке элементы с id:
 * - `canvas-lv-one` — canvas для рисования
 * - `form-lv1` — форма с полем `answer`
 * - `answer--lv1` — кнопка отправки ответа
 * - `true` — модальное окно "верный ответ"
 * - `false` — модальное окно "неверный ответ"
 * - `fail--answer--lv1` — элемент для текста ошибки поля `answer`
 *
 * Использует модуль истории `createHistory('lv1')` для сохранения
 * каждой попытки ответа (значение + признак правильности).
 *
 * Если canvas не найден — функция прерывается без ошибок
 * @author Lishyk Aliaksandra
 * @version 1.1.
 */
export function level1() {
  const plane = createCanvas('canvas-lv-one');
  if (!plane) return;
  const form = document.getElementById('form-lv1');
  const answerLv = document.getElementById('answer--lv1');
  const modalTrue = document.getElementById('true');
  const modalFalse = document.getElementById('false');
  const answerFail = document.getElementById('fail--answer--lv1');
  const history = createHistory('lv1');

  if (!form) return;
  form.addEventListener('submit', (event) => {
    event.preventDefault();
  });

  plane.redraw();

  if (answerLv) {
    answerLv.addEventListener('click', (event) => {
      event.preventDefault();
      event.stopPropagation();

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

      plane.clearShapes();
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
