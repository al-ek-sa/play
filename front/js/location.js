export function lab(){
  document.getElementById("lab1").addEventListener("click", event => {
    window.location.href = "../html/lab1.html";
    document.body.classList = localStorage.getItem("LD");
  });
}
