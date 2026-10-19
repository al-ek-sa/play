package org.example.figure;

import lombok.experimental.UtilityClass;
import org.example.validation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
//todo не пропускает null
@UtilityClass
public class PointValidator {

    private Stream<FieldError> errorOf(String field, Outcome<String> outcome){
        return switch (outcome){
            case Outcome.Ok<String> ok -> Stream.empty();
            case Outcome.Error<String> error -> Stream.of(new FieldError(field, error.message()));
        };
    }

    private List<FieldError> topProperty(List<FieldError> errors) {
        int top = errors.stream()
                .mapToInt(e -> e.message().getPriority())
                .min()
                .orElseThrow();
        return errors.stream()
                .filter(e -> e.message().getPriority() == top)
                .toList();
    }

    private final List<Map.Entry<String, Rule<String>>> FIELDS = List.of(
            Map.entry("x", PointerRules.X),
            Map.entry("y", PointerRules.Y),
            Map.entry("r", PointerRules.R)
    );

    public Valid<Coordinate> validate(Map<String, String> params){
        List<FieldError> errors = FIELDS.stream()
                .flatMap(field -> errorOf(field.getKey(), field.getValue().apply(params.get(field.getKey())))).toList();

        if(!errors.isEmpty()){
            return new Valid.Failure<>(topProperty(errors));
        }

        return new Valid.Success<>(new Coordinate(
                Rules.toDecimal(params.get("x")),
                Rules.toDecimal(params.get("y")),
                Rules.toDecimal(params.get("r"))
        ));
    }
}
