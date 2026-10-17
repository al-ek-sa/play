package org.example.net;

import org.example.history.ResultValidation;

import java.time.Instant;
import java.util.List;

public final class Responses {
    private Responses() {}

    public record Checked(ResultValidation result, List<ResultValidation> history, Instant currentTime, long executionMicros) {}

    public record HistoryOnly(List<ResultValidation> history, Instant currentTime, long executionMicros) {}

    public record Errors(List<ErrorItem> errors, Instant currentTime, long executionMicros) {}

    public record ErrorItem(String field, String code, String message) {}

    public record Failure(String error) {}
}