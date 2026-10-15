package org.example;

import java.util.function.Predicate;

@FunctionalInterface
public interface Rule<T> {
    Outcome<T> apply(T value);

    static <T> Rule<T> of(Predicate<T> test, Message message){
        return value -> test.test(value) ? Outcome.ok(value) : Outcome.error(message);
    }
}