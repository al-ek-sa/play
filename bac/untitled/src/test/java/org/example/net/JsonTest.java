package org.example.net;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {

    public record Number(BigDecimal value) {}

    public record Time(Instant time) {}

    public record Flag(boolean hit) {}

    public record Text(String text) {}

    public record Full(BigDecimal x, boolean hit, Instant time) {}

    @Test
    void decimalIsWrittenAsNumber() {
        assertEquals("{\"value\":1.5}", Json.write(new Number(new BigDecimal("1.5"))));
    }

    @Test
    void decimalKeepsScale() {
        assertEquals("{\"value\":1.50}", Json.write(new Number(new BigDecimal("1.50"))));
    }

    @Test
    void negativeDecimal() {
        assertEquals("{\"value\":-100}", Json.write(new Number(new BigDecimal("-100"))));
    }

    @Test
    void instantIsWrittenAsIsoString() {
        Time value = new Time(Instant.parse("2026-10-19T12:00:00Z"));

        assertEquals("{\"time\":\"2026-10-19T12:00:00Z\"}", Json.write(value));
    }

    @Test
    void instantKeepsMilliseconds() {
        Time value = new Time(Instant.parse("2026-10-19T12:00:00.123Z"));

        assertEquals("{\"time\":\"2026-10-19T12:00:00.123Z\"}", Json.write(value));
    }

    @Test
    void booleanIsWrittenAsBoolean() {
        assertEquals("{\"hit\":true}", Json.write(new Flag(true)));
    }

    @Test
    void quotesAreEscaped() {
        assertEquals("{\"text\":\"a\\\"b\"}", Json.write(new Text("a\"b")));
    }

    @Test
    void russianTextStaysReadable() {
        assertEquals("{\"text\":\"Поле не может быть пустым\"}", Json.write(new Text("Поле не может быть пустым")));
    }

    @Test
    void nullFieldIsWrittenAsNull() {
        assertEquals("{\"text\":null}", Json.write(new Text(null)));
    }

    @Test
    void listIsWrittenAsArray() {
        assertEquals("[1,2,3]", Json.write(List.of(1, 2, 3)));
    }

    @Test
    void emptyListIsEmptyArray() {
        assertEquals("[]", Json.write(List.of()));
    }

    @Test
    void mapIsWrittenAsObject() {
        assertEquals("{\"errors\":[]}", Json.write(Map.of("errors", List.of())));
    }

    @Test
    void recordWithAllFields() {
        Full value = new Full(new BigDecimal("2"), false, Instant.parse("2026-01-01T00:00:00Z"));

        String json = Json.write(value);

        assertTrue(json.contains("\"x\":2"));
        assertTrue(json.contains("\"hit\":false"));
        assertTrue(json.contains("\"time\":\"2026-01-01T00:00:00Z\""));
    }

    @Test
    void objectWithoutFieldsFails() {
        assertThrows(IllegalStateException.class, () -> Json.write(new Object()));
    }
}