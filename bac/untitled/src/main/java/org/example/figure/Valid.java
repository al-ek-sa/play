package org.example.figure;

import java.util.List;

public sealed interface Valid<T> {
    record Success<T>(T value) implements Valid<T> {}
    record Failure<T>(List<FieldError> errors) implements Valid<T> {}
}