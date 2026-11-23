package org.example.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Named
@ViewScoped
@Getter
@Setter
@NoArgsConstructor
public class RegisterBean implements Serializable {

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 32, message = "Логин должен содержать от 3 до 32 символов")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 128, message = "Пароль должен содержать от 8 до 128 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    private String confirmPassword;

    public void validateConfirmPassword() {
        if (password == null || !password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(
                    "registerForm:confirmPassword",
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Пароли не совпадают",
                            null
                    )
            );
            FacesContext.getCurrentInstance().validationFailed();
        }
    }
}