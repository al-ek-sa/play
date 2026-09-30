/**
 * todo написать доку
 * @param raw
 * @param min
 * @param max
 * @returns {string}
 */
export function validateNumber(raw, min, max) {
  const text = String(raw ?? '');
  if (text.trim() === '') return 'Поле обязательно к заполнению';
  const n = Number(text);
  if (!Number.isFinite(n)) return 'Поле должно быть числовым';
  if (n > max) return `Поле должно быть не больше ${max}`;
  if (n < min) return `Поле должно быть не меньше ${min}`;
  return '';
}
