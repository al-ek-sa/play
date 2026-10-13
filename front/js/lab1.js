import {canvas} from './canvas.js';
import {history} from './history.js';

function validateNumber(raw, min, max) {
  const text = String(raw ?? '');
  if (text.trim() === '') return 'Поле обязательно к заполнению';
  const n = Number(text.replace(",", "."));
  if (!Number.isFinite(n)) return 'Поле должно быть числовым';
  if (n > max) return `Поле должно быть не больше ${max}`;
  if (n < min) return `Поле должно быть не меньше ${min}`;
  return '';
}

export function lab1(){
  const form = document.getElementById("form-lab1");
  const but = document.getElementById("lv1");
  const hist = history();
  const clearButton = document.getElementById("history-clear");
  const nextPage = document.getElementById("history-next");
  const prevPage = document.getElementById("history-prev");
  const labelR = document.getElementById("label-r");
  const labelX = document.getElementById("label-x");
  const labelY = document.getElementById("label-y");

  if(!form){
    console.warn("форма не найдена");
    return;
  }

  const lv1 = canvas("canvas");

  let last = null;
  try {
    last = JSON.parse(sessionStorage.getItem("LAST_VERSION"));
  } catch {
    last = null;
  }
  if (last && Number.isFinite(last.r)) {
    lv1(last.x, last.y, last.r, last.ans);
  } else {
    lv1(undefined, undefined, 15, true);
  }

  if (clearButton) clearButton.addEventListener("click", () => hist.clear());
  if (nextPage) nextPage.addEventListener("click", () => hist.next());
  if (prevPage) prevPage.addEventListener("click", () => hist.prev());

  historyLv();

  form.addEventListener("submit", event => {
    event.preventDefault();
    const formData = new FormData(form);
    const object = Object.fromEntries(formData);

    const errR = validateNumber(object['r'], 0, 100);
    const errX = validateNumber(object['x'], -100, 100);
    const errY = validateNumber(object['y'], -100, 100);

    if (errR) labelR.textContent = `R: ${errR}`;
    if (errX) labelX.textContent = `X: ${errX}`;
    if (errY) labelY.textContent = `Y: ${errY}`;

    if (errR || errX || errY) return;

    const r = Number(object['r'].replace(",", "."));
    const x = Number(object['x'].replace(",", "."));
    const y = Number(object['y'].replace(",", "."));


    const lv1 = canvas("canvas");

    let isCorrect = false;

    if(x >= 0 && y >= 0){
      if(y <= r && x <= r / 2){
        isCorrect = true;
      }
    }

    if(x >= 0 && y <= 0){
      if(y >= x - r / 2){
        isCorrect = true;
      }
    }

    if(x <= 0 && y <= 0){
      if(x * x + y * y <= (r / 2) * (r / 2)){
        isCorrect = true;
      }
    }

    lv1(x, y, r, isCorrect);

    hist.add({ x, y, r, ans: isCorrect, time: new Date().toISOString() });
  });
}

export function historyLv(){
  const table = document.getElementById("history-body");
  if (!table) return;

  table.addEventListener("click", event =>{
    const row = event.target.closest("tr");
    if (!row || !row.dataset.x) return;

    const data = {
      x: Number(row.dataset.x),
      y: Number(row.dataset.y),
      r: Number(row.dataset.r),
      ans: row.dataset.ans === "true"
    };

    const lv1 = canvas("canvas");
    lv1(data.x, data.y, data.r, data.ans);
  });
}
