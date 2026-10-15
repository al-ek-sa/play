package org.example;

public enum Message {
    EMPTY("Поле не может быть пустым"),
    NOT_NUMBER("Значение должно быть числом"),
    TOO_LONG("Слишком длинное значение"),
    X_OUT_OF_RANGE("X должен быть от -100 до 100"),
    Y_OUT_OF_RANGE("Y должен быть от -100 до 100"),
    R_OUT_OF_RANGE("R должен быть от 0 до 100");


    private final String text;

    Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}