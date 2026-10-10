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
  if(!form){
    console.warn("форма не найдена");
    return;
  }

  form.addEventListener("submit", event => {
    event.preventDefault();
    const formData = new FormData(form);
    const object = Object.fromEntries(formData);

    const errR = validateNumber(object['r'], 0, 100);
    const errX = validateNumber(object['x'], -100, 100);
    const errY = validateNumber(object['y'], -100, 100);

    const errors = [];
    if (errR) errors.push(`R: ${errR}`);
    if (errX) errors.push(`X: ${errX}`);
    if (errY) errors.push(`Y: ${errY}`);

    if (errors.length) {
      alert(errors.join("\n"));
      return;
    }

    const r = toNumber(object['r']);
    const x = toNumber(object['x']);
    const y = toNumber(object['y']);

    console.log("lab1:", { r, x, y });

  });
}
