export async function getData(url, form = {}) {
  const fullURL = new URL(url);
  fullURL.search = new URLSearchParams(form).toString();

  const response = await fetch(fullURL, {
    method: "GET",
    mode: "cors",
    cache: "no-cache",
    credentials: "same-origin",
    redirect: "follow",
    referrerPolicy: "no-referrer"
  });

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
  }

  return response.json();
}
