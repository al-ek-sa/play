const PAGE_SIZE = 10;

/**
 * Создаёт модуль истории ответов с пагинацией и хранением в localStorage.
 *
 * Ожидает в разметке страницы элементы со следующими id (где {id} -
 * переданный параметр):
 * - `history-body-{id}` — <tbody>, куда рендерятся строки истории
 * - `history-page-{id}` — элемент для отображения "текущая / всего страниц"
 * - `history-total-{id}` — элемент для отображения общего числа записей
 * - `history-prev-{id}` — кнопка "предыдущая страница"
 * - `history-next-{id}` — кнопка "следующая страница"
 * - `history-clear-{id}` — кнопка очистки истории
 *
 * @param {string|number} id - уникальный идентификатор экземпляра истории
 * (используется как часть ключа localStorage и id DOM-элементов).
 * @returns {{add: add, render: render, initPagination: initPagination, initClear: initClear}}
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
export function createHistory(id){
  const storageKey = `history:${id}`;
  let currentPage = 1;

  /**
   * Добавляет новую запись в историю, сохраняет её в localStorage
   * и переключает отображение на последнюю страницу.
   *
   * @param {number} answer - ответ пользователя.
   * @param {boolean} isCorrect - признак правильности ответа.
   * @returns {Promise<void>}
   */
  async function add(answer, isCorrect) {
    const entry = {
      answer,
      isCorrect,
      date: new Date().toISOString(),
    };

    const data = await loadData();
    data.push(entry);
    await saveData(data);

    const totalPage = Math.ceil(data.length / PAGE_SIZE);
    currentPage = Math.max(1, totalPage);

    await render();
  }

  /**
   * Перерисовывает таблицу истории для текущей страницы, а также
   * обновляет индикатор страниц, счётчик записей и состояние кнопок
   * навигации.
   * @returns {Promise<void>}
   */
  async function render() {
    const tbody = document.getElementById(`history-body-${id}`);
    if(!tbody) return;

    const data = await loadData();
    const totalPage = Math.max(1, Math.ceil(data.length / PAGE_SIZE));

    if (currentPage < 1) currentPage = 1;
    if (currentPage > totalPage) currentPage = totalPage;

    const start = (currentPage - 1) * PAGE_SIZE;
    const pageItems = data.slice(start, start + PAGE_SIZE);

    tbody.innerHTML = '';
    pageItems.forEach(renderRow);

    const pageInfo = document.getElementById(`history-page-${id}`);
    if (pageInfo) pageInfo.textContent = `${currentPage} / ${totalPage}`;

    const totalInfo = document.getElementById(`history-total-${id}`);
    if (totalInfo) totalInfo.textContent = data.length;

    const prevBut = document.getElementById(`history-prev-${id}`);
    const nextBut = document.getElementById(`history-next-${id}`);
    if (prevBut) prevBut.disabled = (currentPage === 1);
    if (nextBut) nextBut.disabled = (currentPage === totalPage);

  }

  /**
   * Полностью очищает историю: удаляет данные из localStorage,
   * сбрасывает текущую страницу на первую и перерисовывает таблицу.
   *
   * @returns {Promise<void>}
   */
  async function clear(){
    localStorage.removeItem(storageKey);
    currentPage = 1;
    await render();
  }

  /**
   * Навешивает обработчики кликов на кнопки "предыдущая"/"следующая
   * страница". Должна вызываться один раз при инициализации модуля.
   *
   * @returns {void}
   */
  function initPagination() {
    const prevBut = document.getElementById(`history-prev-${id}`);
    const nextBut = document.getElementById(`history-next-${id}`);
    if(prevBut){
      prevBut.addEventListener('click', async () => {
        currentPage--;
        await render();
      });
    }
    if(nextBut){
      nextBut.addEventListener('click', async ()=>{
        currentPage++;
        await render();
      });
    }
  }

  /**
   * Навешивает обработчик на кнопку очистки истории с подтверждением
   * действия через стандартный confirm(). Должна вызываться один раз
   * при инициализации модуля.
   *
   * @returns {void}
   */
  function initClear() {
    const clearBtn = document.getElementById(`history-clear-${id}`);
    if(!clearBtn) return;

    clearBtn.addEventListener('click', async () => {
      if (confirm('Очистить всю историю?')) await clear();
    });
  }

  /**
   * Загружает массив записей истории из localStorage.
   * При отсутствии данных или ошибке парсинга возвращает пустой массив.
   *
   * @returns {Promise<Array<{answer: *, isCorrect: boolean, date: string}>>}
   * @private
   */
  async function loadData() {
    try {
      return JSON.parse(localStorage.getItem(storageKey) || '[]');
    } catch {
      return [];
    }
  }

  /**
   * Сохраняет массив записей истории в localStorage.
   *
   * @param {Array<{answer: *, isCorrect: boolean, date: string}>} data
   * @returns {Promise<void>}
   * @private
   */
  async function saveData(data) {
    localStorage.setItem(storageKey, JSON.stringify(data));
  }

  /**
   * Создаёт и добавляет в tbody одну строку таблицы истории
   * для одной записи (ответ, результат, дата).
   *
   * @param {{answer: number, isCorrect: boolean, date: string}} entry
   * @returns {void}
   * @private
   */
  function renderRow(entry){
    const tbody = document.getElementById(`history-body-${id}`);
    if(!tbody) return;

    const row = document.createElement('tr');

    const answerCell = document.createElement('td');
    answerCell.textContent = entry.answer;

    const resultCell = document.createElement('td');
    resultCell.textContent = entry.isCorrect ? 'верно' : 'неверно';
    resultCell.className = entry.isCorrect ? 'is-correct' : 'is-wrong';

    const dateCell = document.createElement('td');
    const date = new Date(entry.date);
    dateCell.textContent = date.toLocaleString('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });

    row.appendChild(answerCell);
    row.appendChild(resultCell);
    row.appendChild(dateCell);
    tbody.appendChild(row);
  }

  return {
    add,
    render,
    initPagination,
    initClear,
  };
}
