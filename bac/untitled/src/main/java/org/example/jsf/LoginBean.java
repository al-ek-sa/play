package org.example.jsf;

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
public class LoginBean implements Serializable {

    @NotBlank(message = "Логин обязателен")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    @Size(max = 128, message = "Пароль слишком длинный")
    private String password;
}