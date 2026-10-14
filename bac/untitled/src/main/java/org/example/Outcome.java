package org.example;

public sealed interface Outcome<T> {
    record Ok<T>(T value) implements Outcome<T> {}
    record Error<T>(Message message) implements Outcome<T> {}

    static <T> Outcome<T> ok(T value) { return new Ok<>(value); }
    static <T> Outcome<T> error(Message message) { return new Error<>(message); }
}