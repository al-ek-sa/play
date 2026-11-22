import { getHistory, filter } from "./historyFilter.js";

export function history() {
  const tbody = document.getElementById("history-body");
  const pageInfo = document.getElementById("history-page");
  const prevButton = document.getElementById("history-prev");
  const nextButton = document.getElementById("history-next");

  const LIMIT = 5;
  let currentPage = 1;
  let totalPages = 0;
  let loading = false;

  let filters = { result: "true" };

  function renderMessage(message) {
    if (!tbody) return;
    tbody.replaceChildren();
    const row = tbody.insertRow();
    row.className = "history__empty";
    const cell = row.insertCell();
    cell.colSpan = 5;
    cell.textContent = message;
  }

  function render(items) {
    if (!tbody) return;
    tbody.replaceChildren();

    if (!items?.length) {
      renderMessage("История пуста");
      return;
    }

    for (const item of items) {
      if (![item.x, item.y, item.r].every(Number.isFinite)) continue;

      const row = tbody.insertRow();
      row.dataset.x = String(item.x);
      row.dataset.y = String(item.y);
      row.dataset.r = String(item.r);
      row.dataset.ans = String(item.hit);
      row.dataset.version = String(item.version ?? "");
      row.dataset.offsetX = String(item.offsetX ?? 0);
      row.dataset.offsetY = String(item.offsetY ?? 0);
      row.dataset.rotation = String(item.rotation ?? 0);

      row.insertCell().textContent = item.x;
      row.insertCell().textContent = item.y;
      row.insertCell().textContent = item.r;

      const result = document.createElement("span");
      result.className = item.hit ? "result result--hit" : "result result--miss";
      result.textContent = item.hit ? "Попадание" : "Промах";
      row.insertCell().append(result);

      const timeCell = row.insertCell();
      timeCell.textContent = item.time
        ? new Date(item.time).toLocaleString("ru-RU")
        : "—";
    }

    if (!tbody.rows.length) renderMessage("История пуста");
  }

  async function load() {
    if (!tbody || loading) return;
    loading = true;
    renderMessage("Загрузка истории…");

    if (prevButton) prevButton.disabled = true;
    if (nextButton) nextButton.disabled = true;

    try {
      const response = await getHistory({
        page: currentPage,
        limit: LIMIT,
        filters
      });

      if (!response.ok) {
        renderMessage(response.message || `Ошибка загрузки истории (HTTP ${response.status})`);
        totalPages = 0;
        if (pageInfo) pageInfo.textContent = "0 / 0";
        return;
      }

      currentPage = response.page;
      totalPages = response.totalPages;
      render(response.items);

      if (pageInfo) {
        pageInfo.textContent = totalPages
          ? `${currentPage} / ${totalPages}`
          : "0 / 0";
      }
    } catch (error) {
      console.error("History loading error", error);
      renderMessage(`Ошибка загрузки истории: ${error.message}`);
      totalPages = 0;
      if (pageInfo) pageInfo.textContent = "0 / 0";
    } finally {
      loading = false;
      if (prevButton) prevButton.disabled = currentPage <= 1 || totalPages === 0;
      if (nextButton) nextButton.disabled = currentPage >= totalPages || totalPages === 0;
    }
  }

  async function next() {
    if (loading || currentPage >= totalPages) return;
    currentPage++;
    await load();
  }

  async function prev() {
    if (loading || currentPage <= 1) return;
    currentPage--;
    await load();
  }

  async function setFilters(newFilters) {
    filters = newFilters;
    currentPage = 1;
    await load();
  }

  async function resetFilters() {
    filters = { result: "true" };
    currentPage = 1;
    await load();
  }

  async function refresh() {
    await load();
  }

  const controller = { load, next, prev, setFilters, resetFilters, refresh };
  filter(controller);
  load();

  return controller;
}
