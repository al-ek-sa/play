package org.example;

public sealed interface Outcome<T> permits Outcome.Ok, Outcome.Error {
    record Ok<T> (T value) implements Outcome<T>{}
    record Error<T> (T  value) implements Outcome<T>{}
}