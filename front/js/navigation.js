export function initNavigation() {
  const lv1 = document.getElementById('lv1');
  const lv2 = document.getElementById('lv2');
  const lv3 = document.getElementById('lv3');

  if (lv1) {
    lv1.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'lv1.html';
    });
  }

  if (lv2) {
    lv2.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'lv2.html';
    });
  }

  if (lv3) {
    lv3.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'lv3.html';
    });
  }

  const history1 = document.getElementById('history--1lv');
  const exit1 = document.getElementById('exit--1lv');
  const success1Next = document.getElementById('success--next--lv1');
  const success1Exit = document.getElementById('success--exit--lv1');
  const failure1 = document.getElementById('failure-lv1');
  const modal1False = document.getElementById('false');

  if (history1) {
    history1.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = '1lv--history.html';
    });
  }

  if (exit1) {
    exit1.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if (success1Next) {
    success1Next.addEventListener('click', (event) => {
      event.preventDefault();
      alert('Тут будет генерация заданий по теме «Квадрат»');
    });
  }

  if (success1Exit) {
    success1Exit.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if (failure1) {
    failure1.addEventListener('click', (event) => {
      event.preventDefault();
      if (modal1False) modal1False.classList.remove('active');
    });
  }

  const history2 = document.getElementById('history--2lv');
  const exit2 = document.getElementById('exit--2lv');
  const success2Next = document.getElementById('success--next--lv2');
  const success2Exit = document.getElementById('success--exit--lv2');
  const failure2 = document.getElementById('failure-lv2');
  const modal2False = document.getElementById('false-lv2');

  if (history2) {
    history2.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = '2lv--history.html';
    });
  }

  if (exit2) {
    exit2.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if (success2Next) {
    success2Next.addEventListener('click', (event) => {
      event.preventDefault();
      alert('Тут будет генерация заданий по теме «Круг»');
    });
  }

  if (success2Exit) {
    success2Exit.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if (failure2) {
    failure2.addEventListener('click', (event) => {
      event.preventDefault();
      if (modal2False) modal2False.classList.remove('active');
    });
  }

  const history3 = document.getElementById('history--3lv');
  const exit3 = document.getElementById('exit--3lv');
  const success3Next = document.getElementById('success--next--lv3');
  const success3Exit = document.getElementById('success--exit--lv3');
  const failure3 = document.getElementById('failure-lv3');
  const modal3False = document.getElementById('false-lv3');

  if (history3) {
    history3.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = '3lv--history.html';
    });
  }

  if (exit3) {
    exit3.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if (success3Next) {
    success3Next.addEventListener('click', (event) => {
      event.preventDefault();
      alert('Тут будет генерация заданий по теме «Треугольник»');
    });
  }

  if (success3Exit) {
    success3Exit.addEventListener('click', (event) => {
      event.preventDefault();
      window.location.href = 'index.html';
    });
  }

  if (failure3) {
    failure3.addEventListener('click', (event) => {
      event.preventDefault();
      if (modal3False) modal3False.classList.remove('active');
    });
  }

  const back1 = document.getElementById('history-back-lv1');
  const back2 = document.getElementById('history-back-lv2');
  const back3 = document.getElementById('history-back-lv3');

  if (back1) {
    back1.addEventListener('click', () => window.location.href = 'lv1.html');
  }
  if (back2) {
    back2.addEventListener('click', () => window.location.href = 'lv2.html');
  }
  if (back3) {
    back3.addEventListener('click', () => window.location.href = 'lv3.html');
  }
}
