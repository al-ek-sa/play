const PAGE_SIZE = 10;

export function createHistory(id){
  const storageKey = `history:${id}`;
  let currentPage = 1;

  function add(answer, isCorrect) {
    const entry = {
      answer,
      isCorrect,
      date: new Date().toISOString(),
    };

    const data = loadData();
    data.push(entry);
    saveData(data);

    const totalPage = Math.ceil(data.length / PAGE_SIZE);
    currentPage = Math.max(1, totalPage);

    render();
  }

  function render() {
    const tbody = document.getElementById(`history-body-${id}`);
    if(!tbody) return;

    const data = loadData();
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

  function clear(){
    localStorage.removeItem(storageKey);
    currentPage = 1;
    render();
  }

  function initPagination() {
    const prevBut = document.getElementById(`history-prev-${id}`);
    const nextBut = document.getElementById(`history-next-${id}`);
    if(prevBut){
      prevBut.addEventListener('click', () => {
        currentPage--;
        render();
      });
    }
    if(nextBut){
      nextBut.addEventListener('click', ()=>{
        currentPage++;
        render();
      });
    }
  }

  function initClear() {
    const clearBtn = document.getElementById(`history-clear-${id}`);
    if(!clearBtn) return;

    clearBtn.addEventListener('click', () => {
      if (confirm('Очистить всю историю?')) clear();
    })
  }

  function loadData() {
    try {
      return JSON.parse(localStorage.getItem(storageKey) || '[]');
    } catch {
      return [];
    }
  }

  function saveData(data) {
    localStorage.setItem(storageKey, JSON.stringify(data));
  }

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
    clear,
    initPagination,
    initClear,
  };
}
