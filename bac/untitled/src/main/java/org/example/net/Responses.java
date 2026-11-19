package org.example.net;

import org.example.history.ResultValidation;

import java.time.Instant;
import java.util.List;

public sealed interface Responses {

    record Checked(ResultValidation result, List<ResultValidation> history, Instant currentTime, long executionMicros)
            implements Responses {
    }

    record HistoryOnly(List<ResultValidation> history, Instant currentTime, long executionMicros)
            implements Responses {
    }

    record Errors(List<ErrorItem> errors, Instant currentTime, long executionMicros)
            implements Responses {
    }

    record Failure(String error) implements Responses {
    }

    record ErrorItem(String field, String code, String message) {
    }
}