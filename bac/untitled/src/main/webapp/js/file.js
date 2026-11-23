export function batch() {
  const fileInput = document.getElementById("batchFile");
  const uploadButton = document.getElementById("uploadBatch");
  const fileName = document.getElementById("fileName");
  const status = document.getElementById("batchStatus");

  if (!fileInput || !uploadButton || !status) {
    console.error("Элементы пакетной загрузки не найдены");
    return;
  }

  const MAX_POINTS = 500;
  const MAX_FILE_SIZE = 1024 * 1024;

  let selectedFile = null;
  let isUploading = false;


  function showStatus(message, type = "info") {
    status.textContent = message;
    status.className = `batch-upload__status batch-upload__status--${type}`;
  }

  function clearStatus() {
    status.textContent = "";
    status.className = "batch-upload__status";
  }

  fileInput.addEventListener("change", () => {
    clearStatus();

    selectedFile = fileInput.files?.[0] ?? null;

    if (!selectedFile) {
      if (fileName) {
        fileName.textContent = "";
      }
      return;
    }

    if (fileName) {
      fileName.textContent = selectedFile.name;
    }

    if (!selectedFile.name.toLowerCase().endsWith(".json")) {
      showStatus("Выберите файл формата JSON", "error");
      selectedFile = null;
      fileInput.value = "";
      return;
    }

    if (selectedFile.size > MAX_FILE_SIZE) {
      showStatus("Размер файла не должен превышать 1 МБ", "error");
      selectedFile = null;
      fileInput.value = "";
      return;
    }

    showStatus("Файл выбран. Можно загружать.", "success");
  });

  async function readPoints(file) {
    const content = await file.text();

    let parsed;

    try {
      parsed = JSON.parse(content);
    } catch (error) {
      throw new Error("Файл содержит некорректный JSON");
    }

    if (!Array.isArray(parsed)) {
      throw new Error("JSON должен содержать массив точек");
    }

    if (parsed.length === 0) {
      throw new Error("Файл не содержит точек");
    }

    if (parsed.length > MAX_POINTS) {
      throw new Error(`Допускается не более ${MAX_POINTS} точек`);
    }

    for (let i = 0; i < parsed.length; i++) {
      const point = parsed[i];

      if (
        point === null ||
        typeof point !== "object" ||
        Array.isArray(point)
      ) {
        throw new Error(`Точка №${i + 1} имеет неправильный формат`);
      }
    }

    return parsed;
  }

  async function parseResponse(response) {
    const responseText = await response.text();

    if (!responseText.trim()) {
      return null;
    }

    try {
      return JSON.parse(responseText);
    } catch {
      return {
        rawResponse: responseText
      };
    }
  }

  function getErrorMessage(response, data) {
    if (data && typeof data === "object") {
      if (data.message) {
        return data.message;
      }

      if (data.error) {
        return data.error;
      }

      if (data.rawResponse) {
        console.error(
          "Сервер вернул HTML вместо JSON:",
          data.rawResponse
        );
      }
    }

    switch (response.status) {
      case 400:
        return "Некорректный запрос";

      case 401:
        return "Необходимо авторизоваться";

      case 403:
        return "Доступ запрещён";

      case 404:
        return "Endpoint пакетной загрузки не найден";

      case 405:
        return "Сервер не поддерживает данный HTTP-метод";

      case 413:
        return "Размер запроса слишком большой";

      case 415:
        return "Неподдерживаемый формат данных";

      case 500:
        return "Внутренняя ошибка сервера";

      case 502:
        return "Сервер временно недоступен";

      case 503:
        return "Сервис временно недоступен";

      default:
        return `Ошибка HTTP ${response.status}`;
    }
  }
  async function uploadPoints(points) {
    const idempotencyKey = crypto.randomUUID();

    const response = await fetch("/api/points/batch", {
      method: "POST",

      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        "Idempotency-Key": idempotencyKey
      },

      body: JSON.stringify(points)
    });

    const data = await parseResponse(response);

    if (!response.ok) {
      throw new Error(getErrorMessage(response, data));
    }

    return data;
  }

  uploadButton.addEventListener("click", async () => {
    if (isUploading) {
      return;
    }

    if (!selectedFile) {
      showStatus("Сначала выберите JSON-файл", "error");
      return;
    }

    isUploading = true;

    uploadButton.disabled = true;
    uploadButton.textContent = "Загрузка...";
    showStatus("Проверка файла...", "info");

    try {
      const points = await readPoints(selectedFile);

      showStatus(
        `Отправка ${points.length} точек на сервер...`,
        "info"
      );

      const result = await uploadPoints(points);

      console.log("Результат пакетной загрузки:", result);

      showStatus(
        `Пакет успешно обработан. Отправлено точек: ${points.length}`,
        "success"
      );

    } catch (error) {
      console.error("Ошибка пакетной загрузки:", error);

      showStatus(
        error.message || "Не удалось загрузить пакет",
        "error"
      );

    } finally {
      isUploading = false;

      uploadButton.disabled = false;
      uploadButton.textContent = "Загрузить пакет";
    }
  });
}
