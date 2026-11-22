export function login() {
  const form = document.getElementById("login");

  if (!form) {
    return;
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const username = form.elements.username.value.trim();
    const password = form.elements.password.value;

    const button = form.querySelector('button[type="submit"]');

    if (!username || !password) {
      return;
    }

    if (button) {
      button.disabled = true;
    }

    try {
      const response = await fetch("/api/auth/login", {
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
        throw new Error(result.message || "Ошибка авторизации");
      }

      if (result.role === "USER") {
        window.location.href = "/html/lab1.html";
      } else if (result.role === "ADMIN") {
        window.location.href = "/html/auth.html";
      }

    } catch (error) {
      alert(error.message);
    } finally {
      if (button) {
        button.disabled = false;
      }
    }
  });
}
