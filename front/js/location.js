export function lab(){
  document.getElementById("lab1").addEventListener("click", event => {
    event.preventDefault();
    window.location.href = "./user.xhtml";
    document.body.classList = localStorage.getItem("LD");
  });
}
