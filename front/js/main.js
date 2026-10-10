import {color, ld} from './background.js';
import {lab} from './location.js';
import {canvas} from './canvas.js'
import {lab1} from './lab1.js';

document.addEventListener("DOMContentLoaded", () => {
  restore();

  if(document.getElementById("lab1")){
    lab();
  }

  if(document.getElementById("color")) {
    ld();
  }

  if(document.getElementById("palette-toggle") || document.getElementById("palette-panel")) {
    color();
  }

  if(document.getElementById("form-lab1")){
    lab1();
  }

  if(document.getElementById("canvas")){
    canvas();
  }
});


function restore(){
  document.body.classList = localStorage.getItem("LD");
  document.body.dataset.theme = localStorage.getItem("COLOR");
}
