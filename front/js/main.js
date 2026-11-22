import { color, ld } from './background.js';
import { lab } from './location.js';
import { lab1, historyLv } from './lab1.js';
import { auth } from './auth.js';
import { login } from './login.js';
import { register } from './register.js';
import { filter } from './historyFilter.js';

document.addEventListener("DOMContentLoaded", () => {
  restore();

  initIfExists("login", login);
  initIfExists("register", register);
  initIfExists("confirmPassword", register);
  initIfExists("lab1", lab);
  initIfExists("areaForm", auth);
  initIfExists("color", ld);
  initIfExists("palette-toggle", color);
  initIfExists("form-lab1", lab1);
  initIfExists("history", historyLv);
  initIfExists("history-filter", filter);
});

function initIfExists(id, callback) {
  if (document.getElementById(id)) {
    callback();
  }
}

function restore(){
  document.body.classList = localStorage.getItem("LD");
  document.body.dataset.theme = localStorage.getItem("COLOR");
}
