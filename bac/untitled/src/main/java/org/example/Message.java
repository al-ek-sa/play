package org.example;

import lombok.Getter;

@Getter
public enum Message {
    EMPTY("Поле не может быть пустым", 0),
    NOT_NUMBER("Значение должно быть числом", 1),
    TOO_LONG("Слишком длинное значение", 2),
    X_OUT_OF_RANGE("X должен быть от -100 до 100", 3),
    Y_OUT_OF_RANGE("Y должен быть от -100 до 100", 3),
    R_OUT_OF_RANGE("R должен быть от 0 до 100", 3);


    private final String text;
    private final int priority;

    Message(String text, int priority) {
        this.text = text;
        this.priority = priority;
    }
}