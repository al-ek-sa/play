package org.example;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class Rules {
    public static final Pattern DECIMAL = Pattern.compile("-?\\d+([.,]\\d+)?");

    public static final Rule<String> REQUIRED = Rule.of(s -> !s.isBlank(), Message.EMPTY);

    public static final Rule<String> DECIMAL_FORMAT = Rule.of(s -> DECIMAL.matcher(s).matches(), Message.NOT_NUMBER);

    private Rules(){}

    public static Rule<String> maxLength(int max){
        return Rule.of(s -> s.length() <= max, Message.TOO_LONG);
    }

    //todo избавиться от повторного парсинга в between — парсить один раз, правила на Rule<BigDecimal>
    public static Rule<String> between(String min, String max, Message message){
        return Rule.of(s -> toDecimal(s).compareTo(toDecimal(max)) <= 0 && toDecimal(s).compareTo(toDecimal(min)) >= 0, message);
    }

    private static BigDecimal toDecimal(String s){
        return new BigDecimal(s.replace(",", "."));
    }
}