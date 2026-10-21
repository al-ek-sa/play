package org.example.figure;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.validation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            case Outcome.Ok<String> ok -> Stream.empty();
            case Outcome.Error<String> error -> Stream.of(new FieldError(field, error.message()));
        };
    }

    private List<FieldError> topPriority(List<FieldError> errors) {
        int top = errors.stream()
                .mapToInt(e -> e.message().getPriority())
                .min()
                .orElseThrow();
        return errors.stream()
                .filter(e -> e.message().getPriority() == top)
                .toList();
    }

    private String valueOf(Map<String, String> params, String field) {
        return Objects.requireNonNullElse(params.get(field), "");
    }

    public Valid<Coordinate> validate(Map<String, String> params) {
        Map<String, String> safe = params == null ? Map.of() : params;

        List<FieldError> errors = FIELDS.stream()
                .flatMap(field -> errorOf(field.getKey(), field.getValue().apply(valueOf(safe, field.getKey()))))
                .toList();

        if(!errors.isEmpty()){
            return new Valid.Failure<>(topPriority(errors));
        }

        return new Valid.Success<>(new Coordinate(
                Rules.toDecimal(valueOf(safe, "x")),
                Rules.toDecimal(valueOf(safe, "y")),
                Rules.toDecimal(valueOf(safe, "r"))
        ));
    }
}