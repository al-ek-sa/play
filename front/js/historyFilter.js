export function filter() {
  const filters = document.querySelectorAll(
    'input[name="history-filter"]'
  );

  const version = document.getElementById("version-filter");
  const result = document.getElementById("result-filter");
  const time = document.getElementById("time-filter");

  filters.forEach(radio => {
    radio.addEventListener("change", event => {
      const selected = event.target.value;

      version.hidden = selected !== "version";
      result.hidden = selected !== "result";
      time.hidden = selected !== "time";

      version.classList.toggle("hidden", selected !== "version");
      result.classList.toggle("hidden", selected !== "result");
      time.classList.toggle("hidden", selected !== "time");
    });
  });
}
