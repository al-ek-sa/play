export function register() {
  const registerForm = document.getElementById("register");

  if (!registerForm) {
    return;
  }

  registerForm.addEventListener("submit", async event => {
    event.preventDefault();

    const formData = new FormData(registerForm);
    const data = Object.fromEntries(formData);

    const username = data.login.trim();
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
      const response = await fetch("/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username: username,
          password: password
        })
      });

      const result = await response.json();

      if (!response.ok) {
        alert(result.message || "Ошибка регистрации");
        return;
      }

      alert("Регистрация успешна!");
      window.location.href = "/login.html";

    } catch (error) {
      console.error(error);
      alert("Ошибка соединения с сервером");
    }
  });
}
