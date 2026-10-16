package org.example.validation;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@UtilityClass
public class Rules {
    public final Pattern DECIMAL = Pattern.compile("-?\\d+([.,]\\d+)?");

    public final Rule<String> REQUIRED = Rule.of(s -> !s.trim().isBlank(), Message.EMPTY);

    public final Rule<String> DECIMAL_FORMAT = Rule.of(s -> DECIMAL.matcher(s.trim()).matches(), Message.NOT_NUMBER);

    public Rule<String> maxLength(int max){
        return Rule.of(s -> s.length() <= max, Message.TOO_LONG);
    }

    //todo избавиться от повторного парсинга в between — парсить один раз, правила на Rule<BigDecimal>
    public Rule<String> between(BigDecimal min, BigDecimal max, Message message){
        return Rule.of(s -> toDecimal(s).compareTo(max) <= 0 && toDecimal(s).compareTo(min) >= 0, message);
    }

    private BigDecimal toDecimal(String s){
        return new BigDecimal(s.trim().replace(",", "."));
    }
}