export  function initModal(){
  const button = document.getElementById('lv-one');
  const modal = document.getElementById('th-square');
  const buttonLv2 = document.getElementById('lv-two');
  const modalLv2 = document.getElementById('th-circle');
  const buttonLv3 = document.getElementById('lv-tree');
  const modalLv3 = document.getElementById('th-triangle')


  if (!button || !modal) return;

  if (!buttonLv2 || !modalLv2) return;

  if (!buttonLv3 || !modalLv3) return;

  buttonLv3.addEventListener('click', event => {
    event.preventDefault();
    modalLv3.classList.add('active');
  });

  button.addEventListener('click', (event) => {
    event.preventDefault();
    modal.classList.add('active');
  });

  buttonLv2.addEventListener('click', event => {
    event.preventDefault();
    modalLv2.classList.add('active');
  });
}
