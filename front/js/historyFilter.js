const HISTORY_ENDPOINT = "/api/history";

function parseCsv(csv) {
  const rows = [];
  let row = [];
  let cell = "";
  let quoted = false;

  for (let i = 0; i < csv.length; i++) {
    const ch = csv[i];

    if (quoted) {
      if (ch === '"' && csv[i + 1] === '"') {
        cell += '"';
        i++;
      } else if (ch === '"') {
        quoted = false;
      } else {
        cell += ch;
      }
      continue;
    }

    if (ch === '"') {
      quoted = true;
    } else if (ch === ",") {
      row.push(cell);
      cell = "";
    } else if (ch === "\n") {
      row.push(cell.replace(/\r$/, ""));

      if (row.some(value => value.trim() !== "")) {
        rows.push(row);
      }

      row = [];
      cell = "";
    } else {
      cell += ch;
    }
  }

  if (cell.length || row.length) {
    row.push(cell.replace(/\r$/, ""));

    if (row.some(value => value.trim() !== "")) {
      rows.push(row);
    }
  }

  if (!rows.length) {
    return [];
  }

  const headers = rows.shift().map(header => header.trim().toLowerCase());

  return rows.map(values => {
    const raw = {};

    headers.forEach((header, index) => {
      raw[header] = values[index] ?? "";
    });

    const get = (...names) => {
      for (const name of names) {
        if (raw[name] !== undefined && raw[name] !== "") {
          return raw[name];
        }
      }

      return undefined;
    };

    const rawHit = String(
      get("result", "hit", "is hit", "ishit") ?? ""
    ).trim().toLowerCase();

    const hit =
      rawHit === "true" ||
      rawHit === "1" ||
      rawHit === "hit" ||
      rawHit === "попадание" ||
      rawHit === "попала";

    return {
      x: Number(get("x")),
      y: Number(get("y")),
      r: Number(get("r")),
      hit,
      time: get("created at", "created_at", "time", "timestamp", "date") ?? "",
      version: get("area version", "area_version", "version") ?? "",
      offsetX: 0,
      offsetY: 0,
      rotation: 0
    };
  }).filter(item =>
    [item.x, item.y, item.r].every(Number.isFinite)
  );
}

export async function getHistory({page = 1, limit = 5, filters = { result: "true" }} = {}) {
  let route;

  const params = new URLSearchParams({
    page: String(page),
    limit: String(limit)
  });

  if (filters.version !== undefined) {
    route = "/version";
    params.set("version", String(filters.version));
  } else if (filters.result !== undefined) {
    route = "/result";
    params.set("result", String(filters.result));
  } else if (
    filters.from !== undefined &&
    filters.to !== undefined
  ) {
    route = "/time";
    params.set("from", filters.from);
    params.set("to", filters.to);
  } else {
    return {
      ok: false,
      status: 400,
      items: [],
      page,
      totalPages: 0,
      message: "Выберите фильтр"
    };
  }

  try {
    const response = await fetch(
      `${HISTORY_ENDPOINT}${route}?${params.toString()}`,
      {
        method: "POST",
        cache: "no-cache",
        referrerPolicy: "no-referrer",
        mode: "same-origin",
        headers: {
          Accept: "text/csv, application/json"
        }
      }
    );

    const text = await response.text();

    if (!response.ok) {
      let message = `HTTP ${response.status}`;

      try {
        const error = JSON.parse(text);
        message = error.message || message;
      } catch {
        if (text.trim()) {
          message = text;
        }
      }

      return {
        ok: false,
        status: response.status,
        items: [],
        page,
        totalPages: 0,
        message
      };
    }

    const contentType =
      response.headers.get("content-type") || "";

    if (
      contentType.includes("text/html") ||
      /^\s*<(?:!doctype|html)/i.test(text)
    ) {
      return {
        ok: false,
        status: response.status,
        items: [],
        page,
        totalPages: 0,
        message: "Сервер вернул HTML вместо CSV"
      };
    }

    return {
      ok: true,
      status: response.status,
      items: parseCsv(text),
      total: Number(
        response.headers.get("X-Total-Count") ?? 0
      ),
      page: Number(
        response.headers.get("X-Page") ?? page
      ),
      limit: Number(
        response.headers.get("X-Limit") ?? limit
      ),
      totalPages: Number(
        response.headers.get("X-Total-Pages") ?? 0
      )
    };
  } catch (error) {
    return {
      ok: false,
      status: 0,
      items: [],
      page,
      totalPages: 0,
      message: error.message
    };
  }
}

export function filter(historyController) {
  const radios = document.querySelectorAll(
    'input[name="history-filter"]'
  );

  const versionSection =
    document.getElementById("version-filter");

  const resultSection =
    document.getElementById("result-filter");

  const timeSection =
    document.getElementById("time-filter");

  const versionInput =
    document.getElementById("area-version");

  const resultInput =
    document.getElementById("hit-result");

  const dateFrom =
    document.getElementById("date-from");

  const dateTo =
    document.getElementById("date-to");

  const applyButton =
    document.getElementById("apply-history-filter");

  const resetButton =
    document.getElementById("reset-history-filter");

  let selectedFilter = "result";

  const resultRadio = document.querySelector(
    'input[name="history-filter"][value="result"]'
  );

  if (resultRadio) {
    resultRadio.checked = true;
  }

  if (resultInput) {
    resultInput.value = "true";
  }

  function showFilter(selected) {
    const sections = {
      version: versionSection,
      result: resultSection,
      time: timeSection
    };

    for (const [name, element] of Object.entries(sections)) {
      if (!element) {
        continue;
      }

      const visible = name === selected;

      element.hidden = !visible;
      element.classList.toggle("hidden", !visible);
    }
  }

  function normalizeDateTime(value) {
    if (!value) {
      return "";
    }

    if (value.length === 16) {
      return `${value}:00`;
    }

    if (value.length === 19) {
      return value;
    }

    return value;
  }

  function collectFilters() {
    if (selectedFilter === "version") {
      const raw = versionInput?.value.trim() ?? "";
      const value = Number(raw);

      if (
        !raw ||
        !Number.isInteger(value) ||
        value < 1
      ) {
        versionInput?.focus();
        return null;
      }

      return {
        version: value
      };
    }


    if (selectedFilter === "result") {
      return {
        result: resultInput?.value ?? "true"
      };
    }


    if (selectedFilter === "time") {
      const from = dateFrom?.value ?? "";
      const to = dateTo?.value ?? "";

      if (!from || !to || from > to) {
        (!from ? dateFrom : dateTo)?.focus();
        return null;
      }

      return {
        from: normalizeDateTime(from),
        to: normalizeDateTime(to)
      };
    }

    return null;
  }

  radios.forEach(radio => {
    radio.addEventListener("change", event => {
      selectedFilter = event.target.value;
      showFilter(selectedFilter);
    });
  });


  applyButton?.addEventListener("click", async () => {
    const newFilters = collectFilters();

    if (newFilters) {
      await historyController.setFilters(newFilters);
    }
  });

  resetButton?.addEventListener("click", async () => {
    if (versionInput) {
      versionInput.value = "";
    }

    if (resultInput) {
      resultInput.value = "true";
    }

    if (dateFrom) {
      dateFrom.value = "";
    }

    if (dateTo) {
      dateTo.value = "";
    }

    selectedFilter = "result";

    if (resultRadio) {
      resultRadio.checked = true;
    }

    showFilter("result");

    await historyController.resetFilters();
  });


  showFilter(selectedFilter);
}
