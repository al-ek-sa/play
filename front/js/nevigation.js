export function initNavigation(){
  const lv1 = document.getElementById('lv1');
  const history = document.getElementById('history--1lv');
  const exit = document.getElementById('exit--1lv');
  const lv1Success = document.getElementById('success--exit--lv1');
  const lv1Failure = document.getElementById('failure-lv1');
  const modalLv1= document.getElementById('false');
  const tableLv1 = document.getElementById('but-table-1lv');
  const lv1toLv2 = document.getElementById('success--next--lv1');

  if(lv1toLv2) {
    lv1toLv2.addEventListener('click', event => {
      event.preventDefault();
      window.location.href = 'lv2.html';
    });
  }

  if(tableLv1){
    tableLv1.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'lv1.html';
    });
  }

  if(lv1){
    lv1.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'lv1.html';
    });
  }

  if(lv1Failure) {
    lv1Failure.addEventListener('click', event => {
      event.preventDefault();
      modalLv1.classList.remove('active');
    })
  }

  if(lv1Success){
    lv1Success.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    })
  }

  if(exit) {
    exit.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if(history){
    history.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = '1lv--history.html';
    });
  }
}
