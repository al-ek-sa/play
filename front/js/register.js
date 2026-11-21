export function register() {
  const registerForm = document.getElementById("register");

  if (!registerForm) {
    return;
  }

  registerForm.addEventListener("submit", async event => {
    event.preventDefault();

    const formData = new FormData(registerForm);
    const data = Object.fromEntries(formData);

    const username = data.username.trim();
    const password = data.password;
    const confirmPassword = data.confirmPassword;

    if (!username || !password || !confirmPassword) {
      alert("Заполните все поля");
      return;
    }

    if (password !== confirmPassword) {
      alert("Пароли не совпадают");
      return;
    }

    try {
      const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username,
          password
        })
      });

      const result = await response.json();

      if (!response.ok) {
        alert(result.message || "Ошибка регистрации");
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
