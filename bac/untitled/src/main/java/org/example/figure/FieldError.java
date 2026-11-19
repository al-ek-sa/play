package org.example.figure;

import org.example.validation.Message;

public record FieldError(String field, Message message) {
}
