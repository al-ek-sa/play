package org.example.net;

import lombok.extern.slf4j.Slf4j;
import org.example.figure.Area;
import org.example.figure.Coordinate;
import org.example.figure.FieldError;
import org.example.figure.PointValidator;
import org.example.figure.Valid;
import org.example.history.History;
import org.example.history.ResultValidation;

import java.time.Instant;
import java.util.List;

@Slf4j
public record CheckHandler(History history) implements Handler {

    @Override
    public Response handler(Request request) {
        long start = System.nanoTime();

        return switch (request.method()) {
            case "POST" -> post(request, start);
            case "DELETE" -> clear(start);
            default -> Response.notFoundMethod();
        };
    }

    private Response post(Request request, long start){
        if (request.data().isEmpty()) {
            log.debug("history requested");
            return historyOnly(start);
        }

        return switch (PointValidator.validate(request.data())) {
            case Valid.Success<Coordinate> success -> check(success.value(), start);
            case Valid.Failure<Coordinate> failure -> errors(failure.errors(), start);
        };
    }

    private Response check(Coordinate c, long start){
        boolean hit = Area.variant(c.r()).contains(c.point());
        Instant now = Instant.now();
        long executionMicros = micros(start);
        ResultValidation result = new ResultValidation(c.x(), c.y(), c.r(), hit, now, executionMicros);
        history.add(result);
        log.info("check x={} y={} r={} hit={} in {} µs", c.x(), c.y(), c.r(), hit, executionMicros);
        return Response.ok(Json.write(new Responses.Checked(result, history.reverse(), now, executionMicros)));
    }

    private Response errors(List<FieldError> errors, long start){
        log.info("validation failed: {}", errors);
        List<Responses.ErrorItem> items = errors.stream()
                .map(e -> new Responses.ErrorItem(e.field(), e.message().name(), e.message().getText()))
                .toList();
        return Response.clientError(Json.write(new Responses.Errors(items, Instant.now(), micros(start))));
    }

    private Response clear(long start){
        history.clear();
        log.info("history cleared");
        return historyOnly(start);
    }

    private Response historyOnly(long start){
        return Response.ok(Json.write(new Responses.HistoryOnly(history.reverse(), Instant.now(), micros(start))));
    }

    private static long micros(long start){
        return (System.nanoTime() - start) / 1_000;
    }
}