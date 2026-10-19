export async function getData(url, form = {}) {
  const response = await fetch(url, {
    method: "POST",
    cache: "no-cache",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams(form)
  });

  const data = await response.json();
  return { ok: response.ok, status: response.status, data };
}

export async function clearHistory(url) {
  const response = await fetch(url, { method: "DELETE", cache: "no-cache" });
  return response.json();
}
