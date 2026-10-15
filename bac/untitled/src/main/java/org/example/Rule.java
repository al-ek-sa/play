package org.example;

import java.util.Optional;

@FunctionalInterface
public interface Rule<T> {
    Outcome<T> apply(T value);
}