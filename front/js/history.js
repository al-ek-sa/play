export function history(){
  const tb = document.getElementById("history-body");
  const CAPACITY = 7;
  let currentPage = 1;
  let page = 1;
  function before(){
    const history = load();
    const start = (currentPage - 1) * CAPACITY;
    const pageItems = history.slice(start, start + CAPACITY);

    for(let answer in pageItems){
      const newRow = tb.insertRow(0);
      newRow.insertCell(0).textContent = answer['x'];
      newRow.insertCell(1).textContent = answer['y'];
      newRow.insertCell(2).textContent = answer['r'];
      newRow.insertCell(3).textContent = answer['ans'] ? "попала" : "мимо";
      newRow.insertCell(4).textContent = new Date(answer['time']).toLocaleTimeString("ru-RU");
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
    table(answer);
  }

  function table(answer){
    if (!tb) return;

    const newRow = tb.insertRow(0);
    newRow.insertCell(0).textContent = answer['x'];
    newRow.insertCell(1).textContent = answer['y'];
    newRow.insertCell(2).textContent = answer['r'];
    newRow.insertCell(3).textContent = answer['ans'] ? "попала" : "мимо";
    newRow.insertCell(4).textContent = new Date(answer['time']).toLocaleTimeString("ru-RU");
  }

  function clear(){
    localStorage.setItem("HISTORY", JSON.stringify([]));
    if (!tb) return;
    currentPage = 1;
    tb.innerHTML = '<tr class="history__empty"><td colspan="5">Пока нет проверок</td></tr>';
  }

  load().forEach(table);

  function next(){
    if(currentPage < page) {
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

  return { add, clear, next, prev };
}
