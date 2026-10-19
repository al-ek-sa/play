export function history(){
  const tb = document.getElementById("history-body");
  const pageInfo = document.getElementById("history-page");
  const prevButton = document.getElementById("history-prev");
  const nextButton = document.getElementById("history-next");
  const CAPACITY = 5;
  let items = [];
  let currentPage = 1;

  function pages(){
    return Math.max(1, Math.ceil(items.length / CAPACITY));
  }

  function render(){
    if (!tb) return;
    if (currentPage > pages()) currentPage = pages();
    const start = (currentPage - 1) * CAPACITY;
    const pageItems = items.slice(start, start + CAPACITY);

    if (pageInfo) pageInfo.textContent = `${currentPage} / ${pages()}`;
    if (prevButton) prevButton.disabled = currentPage === 1;
    if (nextButton) nextButton.disabled = currentPage === pages();

    tb.replaceChildren();

    if (pageItems.length === 0) {
      tb.innerHTML = '<tr class="history__empty"><td colspan="5">Пока нет проверок</td></tr>';
      return;
    }

    for (const item of pageItems) {
      row(item);
    }
  }

  function row(item){
    const newRow = tb.insertRow();
    newRow.dataset.x = item.x;
    newRow.dataset.y = item.y;
    newRow.dataset.r = item.r;
    newRow.dataset.ans = item.hit;
    newRow.insertCell(0).textContent = item.x;
    newRow.insertCell(1).textContent = item.y;
    newRow.insertCell(2).textContent = item.r;

    const badge = document.createElement("span");
    badge.className = item.hit ? "result result--hit" : "result result--miss";
    badge.textContent = item.hit ? "попала" : "мимо";
    newRow.insertCell(3).append(badge);

    const timeCell = newRow.insertCell(4);
    timeCell.textContent = new Date(item.time).toLocaleTimeString("ru-RU");
    timeCell.title = `выполнено за ${item.executionMicros} мкс`;
  }

  function set(list){
    items = Array.isArray(list) ? list : [];
    currentPage = 1;
    render();
  }

  function next(){
    if (currentPage < pages()) {
      currentPage++;
      render();
    }
  }

  function prev() {
    if (currentPage > 1) {
      currentPage--;
      render();
    }
  }

  render();

  return { set, next, prev };
}
