import { initModal } from './modal.js';
import { initNavigation } from './navigation.js';
import { initLevel1, level1 } from './level1.js';
import { initLevel2, level2 } from './level2.js';
import { initLevel3, level3 } from './level3.js';
import { createHistory } from './history.js';

document.addEventListener('DOMContentLoaded', () => {
  initModal();
  initNavigation();

  if (document.getElementById('form-lv-one')) {
    initLevel1();
  }
  if (document.getElementById('form-lv-two')) {
    initLevel2();
  }
  if (document.getElementById('form-lv-three')) {
    initLevel3();
  }

  if (document.getElementById('form-lv1')) {
    level1();
  }
  if (document.getElementById('form-lv2')) {
    level2();
  }
  if (document.getElementById('form-lv3')) {
    level3();
  }

  initHistoryPage('lv1');
  initHistoryPage('lv2');
  initHistoryPage('lv3');
});

function initHistoryPage(id) {
  const tbody = document.getElementById(`history-body-${id}`);
  if (!tbody) return;

  const history = createHistory(id);
  history.render();
  history.initPagination();
  history.initClear();

  const backBtn = document.getElementById(`history-back-${id}`);
  if (backBtn) {
    backBtn.addEventListener('click', () => {
      window.location.href = `${id}.html`;
    });
  }
}
