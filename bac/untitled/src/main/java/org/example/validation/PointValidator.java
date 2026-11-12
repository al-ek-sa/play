package org.example.validation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.figure.Coordinate;
import org.example.figure.FieldError;
import org.example.figure.Valid;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
@Slf4j
public class PointValidator {

    private final List<Map.Entry<String, Rule<String>>> FIELDS = List.of(
            Map.entry("x", PointerRules.X),
            Map.entry("y", PointerRules.Y),
            Map.entry("r", PointerRules.R)
    );

    private Stream<FieldError> errorOf(String field, Outcome<String> outcome) {
        return switch (outcome) {
            case Outcome.Ok<String> _ -> Stream.empty();
            case Outcome.Error<String> error -> Stream.of(new FieldError(field, error.message()));
        };
    }

    private List<FieldError> topPriority(List<FieldError> errors) {
        return errors.stream()
                .collect(Collectors.groupingBy(FieldError::field, LinkedHashMap::new,
                        Collectors.minBy(Comparator.comparingInt(e -> e.message().getPriority()))))
                .values().stream()
                .flatMap(Optional::stream)
                .toList();
    }

    private String valueOf(Map<String, String> params, String field) {
        return Objects.requireNonNullElse(params.get(field), "");
    }

    public Valid<Coordinate> validate(Map<String, String> params) {
        if (params == null) {
            log.warn("validate called with null params");
        }
        Map<String, String> safe = params == null ? Map.of() : params;
        log.trace("validate params {}", safe);

        List<FieldError> errors = FIELDS.stream()
                .flatMap(field -> errorOf(field.getKey(), field.getValue().apply(valueOf(safe, field.getKey()))))
                .toList();

        if (!errors.isEmpty()) {
            List<FieldError> top = topPriority(errors);
            log.debug("validation failed: all {}, returned {}", errors, top);
            return new Valid.Failure<>(top);
        }

        Coordinate coordinate = new Coordinate(
                Rules.toDecimal(valueOf(safe, "x")),
                Rules.toDecimal(valueOf(safe, "y")),
                Rules.toDecimal(valueOf(safe, "r"))
        );
        log.debug("validation passed: {}", coordinate);
        return new Valid.Success<>(coordinate);
    }
}