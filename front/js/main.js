import {color, ld} from './background.js';
import {lab} from './location.js';

document.addEventListener("DOMContentLoaded", () => {
  if(document.getElementById("color")) {
    ld();
  }

  if(document.getElementById("palette-toggle") || document.getElementById("palette-panel")) {
    color();
  }

  if(document.getElementById("lab1")){
    lab();
  }
});
