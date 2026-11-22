import { canvas } from './canvas.js';
import { history } from './history.js';
import { getData, clearHistory } from './net.js';

const ENDPOINT = "/api/points/check";

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

  const lastVersion = sessionStorage.getItem("LAST_VERSION");

  if (lastVersion) {
    try {
      const last = JSON.parse(lastVersion);

      lv1(
        last.x,
        last.y,
        last.r,
        last.ans,
        last.offsetX ?? 0,
        last.offsetY ?? 0,
        last.rotation ?? 0
      );
    } catch {
      lv1(undefined, undefined, 15, true);
    }
  } else {
    lv1(undefined, undefined, 15, true);
  }

  function showInfo(data){
    if (!info || !data) return;

    const currentTime = data.currentTime
      ? new Date(data.currentTime).toLocaleString()
      : "";

    const executionTime = data.executionTime
      ? `${data.executionTime} ms`
      : "";

    info.textContent = [currentTime, executionTime]
      .filter(Boolean)
      .join(" | ");
  }

  function showError(message) {
    if (info) {
      info.textContent = message;
    }
  }

  function showFieldError(field, error) {
    const label = labels[field];

    if (label) {
      label.textContent = error;
    }

    showError("Неверно введенные данные");
  }

  function resetFieldErrors() {
    if (labels.r) labels.r.textContent = "R";
    if (labels.x) labels.x.textContent = "X";
    if (labels.y) labels.y.textContent = "Y";
  }

  function validateNumber(raw, min, max, minExclusive = false) {
    if (raw === undefined || raw === null || raw.trim() === "") {
      return "Поле обязательно";
    }

    const number = Number(raw.replace(",", "."));

    if (!Number.isFinite(number)) {
      return "Введите число";
    }

    if (number > max) {
      return `Значение должно быть не больше ${max}`;
    }

    if (minExclusive && number <= min) {
      return `Значение должно быть больше ${min}`;
    }

    if (number < min) {
      return `Значение должно быть не меньше ${min}`;
    }

    return "";
  }

  async function send(request) {
    try {
      return await request();
    } catch (error) {
      showError(`Сервер недоступен: ${error.message}`);
      return null;
    }
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    resetFieldErrors();

    const formData = Object.fromEntries(new FormData(form));

    const errors = {
      r: validateNumber(formData.r, 0, 100, true),
      x: validateNumber(formData.x, -100, 100),
      y: validateNumber(formData.y, -100, 100)
    };

    if (errors.r || errors.x || errors.y) {
      for (const [field, error] of Object.entries(errors)) {
        if (error) {
          showFieldError(field, error);
        }
      }

      return;
    }

    const point = {
      x: Number(formData.x.replace(",", ".")),
      y: Number(formData.y.replace(",", ".")),
      r: Number(formData.r.replace(",", "."))
    };

    const response = await send(() => getData(ENDPOINT, point));

    if (!response) {
      return;
    }

    if (!response.ok) {
      if (response.data?.errors) {
        for (const error of response.data.errors) {
          showFieldError(error.field, error.message);
        }
      } else {
        showError(
          response.data?.message ??
          response.data?.error ??
          `Ошибка сервера: ${response.status}`
        );
      }

      return;
    }

    const data = response.data;
    const result = data.result ?? data;

    if (
      result.x === undefined ||
      result.y === undefined ||
      result.r === undefined
    ) {
      showError("Сервер вернул некорректный результат");
      return;
    }

    lv1(
      Number(result.x),
      Number(result.y),
      Number(result.r),
      result.hit,
      Number(result.offsetX ?? 0),
      Number(result.offsetY ?? 0),
      Number(result.rotation ?? 0)
    );

    await hist.refresh();
    showInfo(data);
  });

  if (clearButton) {
    clearButton.addEventListener("click", async () => {
      showError("Очистка истории не поддерживается текущим backend");
    });
  }

  if (nextPage) {
    nextPage.addEventListener("click", () => {
      hist.next();
    });
  }

  if (prevPage) {
    prevPage.addEventListener("click", () => {
      hist.prev();
    });
  }
}

export function historyLv() {
  const table = document.getElementById("history");

  if (!table || table.dataset.listener === "true") {
    return;
  }

  table.dataset.listener = "true";

  table.addEventListener("click", (event) => {
    const row = event.target.closest("tr");

    if (!row || !table.contains(row)) {
      return;
    }

    const x = row.dataset.x;
    const y = row.dataset.y;
    const r = row.dataset.r;
    const hit = row.dataset.ans;

    if (x === undefined || y === undefined || r === undefined) {
      return;
    }

    let saved = {};
    try {
      saved = JSON.parse(sessionStorage.getItem("LAST_VERSION") || "{}");
    } catch {
      saved = {};
    }

    const lv1 = canvas("canvas");

    lv1(
      Number(x),
      Number(y),
      Number(r),
      hit === "true",
      Number(saved.offsetX ?? 0),
      Number(saved.offsetY ?? 0),
      Number(saved.rotation ?? 0)
    );
  });
}
