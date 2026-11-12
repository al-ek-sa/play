//"https://cdn.jsdelivr.net/npm/superagent@8.1.2/+esm";

export async function getData(url, form = {}) {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), 3000);
  try {
    const response = await fetch(url, {
      signal: controller.signal,
      method: "POST",
      cache: "no-cache",
      referrerPolicy: "no-referrer",
      mode: "same-origin",
      keepalive: false,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "Authorization": "Bearer JWT"
      },
      body: new URLSearchParams(form)
    });
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;
    return { ok: response.ok, status: response.status, data };
  } catch (err) {
    return { ok: false, status: 0, data: null, reason: err.name === "AbortError" ? "timeout" : "error" };
  } finally {
    clearTimeout(timer);
  }
}

export async function clearHistory(url) {
  const response = await fetch(url, { method: "DELETE", cache: "no-cache" });
  return response.json();
}
