export function initNavigation(){
  const lv1 = document.getElementById('lv1');
  const history = document.getElementById('history--1lv');
  const exit = document.getElementById('exit--1lv');
  const lv1Success = document.getElementById('understand-true');
  const lv1Failure = document.getElementById('failure-lv1');
  const modalLv1= document.getElementById('false');
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
