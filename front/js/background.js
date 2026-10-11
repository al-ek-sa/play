export function ld(){
  document.body.classList = localStorage.getItem("LD");
  document.getElementById('color').addEventListener('click', event => {
    const isDark= document.body.classList.toggle("dark");
    localStorage.setItem("LD", isDark ? "dark" : "light");
  });
}

export function color(){
  const but = document.getElementById("palette-toggle");
  const pl = document.getElementById("palette-panel");
  pl.hidden = localStorage.getItem("PL");
  document.body.dataset.theme = localStorage.getItem("COLOR");

  but.addEventListener('click', event => {
    pl.hidden = !pl.hidden;
    localStorage.setItem("PL", pl.hidden);
  });

  pl.addEventListener('change', ev => {
    document.body.dataset.theme = ev.target.value;
    localStorage.setItem("COLOR", ev.target.value);
  });
}
