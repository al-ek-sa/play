export function login() {
  const loginForm = document.getElementById("login");

  if (!loginForm) {
    return;
  }

  loginForm.addEventListener("submit", async event => {
    event.preventDefault();

    const formData = new FormData(loginForm);
    const data = Object.fromEntries(formData);

    const username = data.login.trim();
    const password = data.password;

    if (!username || !password) {
      alert("Заполните все поля");
      return;
    }

    try {
      const response = await fetch("/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          login: username,
          password: password
        })
      });

      const result = await response.json();

      if (!response.ok) {
        alert(result.message || "Неверный логин или пароль");
        return;
      }

      if (result.role === "ADMIN") {
        window.location.href = "/admin.html";
      } else if (result.role === "USER") {
        window.location.href = "/main.html";
      }

    } catch (error) {
      console.error(error);
      alert("Ошибка соединения с сервером");
    }
  });
}
