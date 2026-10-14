package org.example;

import java.util.function.Function;

public sealed interface Outcome<T> {
    record Ok<T>(T value) implements Outcome<T> {}
    record Error<T>(Message message) implements Outcome<T> {}

    static <T> Outcome<T> ok(T value) { return new Ok<>(value); }
    static <T> Outcome<T> error(Message message) { return new Error<>(message); }

    default <R> Outcome<R> flatMap(Function<T, Outcome<R>> next){
        return switch (this){
            case Ok<T> ok -> next.apply(ok.value);
            case Error<T> error -> new Error<>(error.message);
        };
    }
}