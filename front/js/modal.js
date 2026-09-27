/**
 * Инициализирует открытие модальных окон с теорией для уровней 1–3.
 *
 * Ожидает в разметке элементы с id:
 * - `lv-one` — кнопка уровня 1 на карте
 * - `th-square` — модалка с теорией квадрата
 * - `lv-two` — кнопка уровня 2 на карте
 * - `th-circle` — модалка с теорией круга
 * - `lv-tree` — кнопка уровня 3 на карте
 * - `th-triangle` — модалка с теорией треугольника
 *
 * Если хотя бы одна пара кнопка + модалка не найдена — функция
 * прерывается без ошибок.
 *
 * По клику на кнопку открывает соответствующую модалку,
 * добавляя класс `active`.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
export  function initModal(){
  const button = document.getElementById('lv-one');
  const modal = document.getElementById('th-square');
  const buttonLv2 = document.getElementById('lv-two');
  const modalLv2 = document.getElementById('th-circle');
  const buttonLv3 = document.getElementById('lv-tree');
  const modalLv3 = document.getElementById('th-triangle')
  // const mod = document.querySelector('.modal');
  //
  // mod.addEventListener('click', event => {
  //   mod.classList.remove('active');
  // });


  if (!button || !modal) return;

  if (!buttonLv2 || !modalLv2) return;

  if (!buttonLv3 || !modalLv3) return;

  buttonLv3.addEventListener('click', event => {
    event.preventDefault();
    modalLv3.classList.add('active');
  });

  button.addEventListener('click', (event) => {
    event.preventDefault();
    modal.classList.add('active');
  });

  buttonLv2.addEventListener('click', event => {
    event.preventDefault();
    modalLv2.classList.add('active');
  });
}
