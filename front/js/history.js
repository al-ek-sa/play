export function history(){
  const tb = document.getElementById("history-body");
  const pageInfo = document.getElementById("history-page");
  const prevButton = document.getElementById("history-prev");
  const nextButton = document.getElementById("history-next");
  const CAPACITY = 5;
  let currentPage = 1;

  function before(){
    if (!tb) return;
    const history = load().reverse();
    if (currentPage > pages()) currentPage = pages();
    const start = (currentPage - 1) * CAPACITY;
    const pageItems = history.slice(start, start + CAPACITY);

    if (pageInfo) pageInfo.textContent = `${currentPage} / ${pages()}`;
    if (prevButton) prevButton.disabled = currentPage === 1;
    if (nextButton) nextButton.disabled = currentPage === pages();

    tb.replaceChildren();

    if (pageItems.length === 0) {
      tb.innerHTML = '<tr class="history__empty"><td colspan="5">Пока нет проверок</td></tr>';
      return;
    }

    for(const answer of pageItems){
      table(answer);
    }
  }

  function load(){
    try {
      return JSON.parse(localStorage.getItem("HISTORY")) ?? [];
    } catch {
      return [];
    }
  }

  async function add(answer){
    const historyOld = await load();
    historyOld.push(answer);
    localStorage.setItem("HISTORY", JSON.stringify(historyOld));
    currentPage = 1;
    before();
  }

  function table(answer){
    if (!tb) return;

    const newRow = tb.insertRow();
    newRow.dataset.x = answer['x'];
    newRow.dataset.y = answer['y'];
    newRow.dataset.r = answer['r'];
    newRow.dataset.ans = answer['ans'];
    newRow.insertCell(0).textContent = answer['x'];
    newRow.insertCell(1).textContent = answer['y'];
    newRow.insertCell(2).textContent = answer['r'];
    newRow.insertCell(3).textContent = answer['ans'] ? "попала" : "мимо";
    newRow.insertCell(4).textContent = new Date(answer['time']).toLocaleTimeString("ru-RU");
  }

  function clear(){
    localStorage.setItem("HISTORY", JSON.stringify([]));
    currentPage = 1;
    before();
  }

  function next(){
    if (currentPage < pages()) {
      currentPage++;
      before();
    }
  }

  function prev() {
    if (currentPage > 1) {
      currentPage--;
      before();
    }
  }

  function pages(){
    return Math.max(1, Math.ceil(load().length / CAPACITY));
  }

  before();

  return { add, clear, next, prev };
}
