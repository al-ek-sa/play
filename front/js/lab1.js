import {canvas} from './canvas.js';
import {history} from './history.js';
import {getData, clearHistory} from './net.js';

const ENDPOINT = "/fcgi-bin/check";

function validateNumber(raw, min, max, minExclusive = false) {
  const text = String(raw ?? '');
  if (text.trim() === '') return 'Поле обязательно к заполнению';
  const n = Number(text.replace(",", "."));
  if (!Number.isFinite(n)) return 'Поле должно быть числовым';
  if (n > max) return `Поле должно быть не больше ${max}`;
  if (minExclusive && n <= min) return `Поле должно быть больше ${min}`;
  if (n < min) return `Поле должно быть не меньше ${min}`;
  return '';
}

export function lab1(){
  const form = document.getElementById("form-lab1");
  const hist = history();
  const clearButton = document.getElementById("history-clear");
  const nextPage = document.getElementById("history-next");
  const prevPage = document.getElementById("history-prev");
  const info = document.getElementById("server-info");
  const labels = {
    r: document.getElementById("label-r"),
    x: document.getElementById("label-x"),
    y: document.getElementById("label-y")
  };

  if(!form){
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

  function showInfo(data){
    if (!info || !data) return;
    const time = new Date(data.currentTime).toLocaleTimeString("ru-RU");
    info.textContent = `Время сервера: ${time} · выполнение: ${data.executionMicros} мкс`;
  }

  function showError(message){
    if (info) info.textContent = message;
    else console.error(message);
  }

  function showFieldError(field, error){
    const label = labels[field];
    if (!label) return;
    label.textContent = error ? `${field.toUpperCase()}: ${error}` : field.toUpperCase();
    info.textContent = `Неверно введенные данные`
  }

  async function send(action){
    try {
      return await action();
    } catch (e) {
      showError(`Сервер недоступен: ${e.message}`);
      return null;
    }
  }

  send(() => getData(ENDPOINT)).then(res => {
    if (!res) return;
    hist.set(res.data.history);
    showInfo(res.data);
  });

  if (clearButton) clearButton.addEventListener("click", async () => {
    const data = await send(() => clearHistory(ENDPOINT));
    if (!data) return;
    hist.set(data.history);
    showInfo(data);
  });
  if (nextPage) nextPage.addEventListener("click", () => hist.next());
  if (prevPage) prevPage.addEventListener("click", () => hist.prev());

  historyLv();

  form.addEventListener("submit", async event => {
    event.preventDefault();
    const formData = new FormData(form);
    const object = Object.fromEntries(formData);

    const errors = {
      r: validateNumber(object['r'], 0, 100, true),
      x: validateNumber(object['x'], -100, 100),
      y: validateNumber(object['y'], -100, 100)
    };

    for (const field of ['r', 'x', 'y']) {
      showFieldError(field, errors[field]);
    }

    if (errors.r || errors.x || errors.y) return;

    const res = await send(() => getData(ENDPOINT, { x: object['x'], y: object['y'], r: object['r'] }));
    if (!res) return;

    showInfo(res.data);

    if (!res.ok) {
      for (const error of res.data.errors ?? []) {
        showFieldError(error.field, error.message);
      }
      return;
    }

    const result = res.data.result;
    lv1(Number(result.x), Number(result.y), Number(result.r), result.hit);
    hist.set(res.data.history);
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
