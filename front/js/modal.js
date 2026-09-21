export  function initModal(){
  const button = document.getElementById('lv-one');
  const modal = document.getElementById('th-square');


  if (!button || !modal) return;

  button.addEventListener('click', (event) => {
    event.preventDefault();
    modal.classList.add('active');
  });

}
