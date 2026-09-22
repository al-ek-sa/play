import { initModal } from './modal.js';
import { initNavigation } from './nevigation.js';
import { initLevel1, level1 } from './level1.js';
import { createHistory } from './history.js';

document.addEventListener('DOMContentLoaded', () => {
  initModal();
  initNavigation();
  if (document.getElementById('form-lv-one')) {
    initLevel1();
  }

  if (document.getElementById('form-lv1')) {
    level1();
  }

  if (document.getElementById('history-body-lv1')) {
    const history = createHistory('lv1');
    history.render();
    history.initPagination();
    history.initClear();

    const backBtn = document.getElementById('history-back-lv1');
    if (backBtn) {
      backBtn.addEventListener('click', () => {
        window.location.href = 'lv1.html';
      });
    }
  }
});
