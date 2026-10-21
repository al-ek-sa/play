package org.example.validation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class Rules {
    public final Pattern DECIMAL = Pattern.compile("-?\\d+([.,]\\d+)?");

    public final Rule<String> REQUIRED = Rule.of(s -> s != null && !s.isBlank(), Message.EMPTY);

    public final Rule<String> DECIMAL_FORMAT = Rule.of(s -> DECIMAL.matcher(s.trim()).matches(), Message.NOT_NUMBER);

    public Rule<String> maxLength(int max){
        return Rule.of(s -> s.length() <= max, Message.TOO_LONG);
    }

    public Rule<String> between(BigDecimal min, BigDecimal max, Message message){
        return Rule.of(s -> toDecimal(s).compareTo(max) <= 0 && toDecimal(s).compareTo(min) >= 0, message);
    }

    public BigDecimal toDecimal(String s){
        return new BigDecimal(s.trim().replace(",", "."));
    }
}