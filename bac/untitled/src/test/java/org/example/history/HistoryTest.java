package org.example.history;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryTest {

    private static ResultValidation result(String x) {
        return new ResultValidation(new BigDecimal(x), BigDecimal.ONE, BigDecimal.TWO, true, Instant.EPOCH, 10);
    }

    @Test
    void newHistoryIsEmpty() {
        History history = new History();

        assertTrue(history.reverse().isEmpty());
    }

    @Test
    void addOne() {
        History history = new History();
        ResultValidation first = result("1");

        history.add(first);

        assertEquals(List.of(first), history.reverse());
    }

    @Test
    void newestComesFirst() {
        History history = new History();
        ResultValidation first = result("1");
        ResultValidation second = result("2");
        ResultValidation third = result("3");

        history.add(first);
        history.add(second);
        history.add(third);

        assertEquals(List.of(third, second, first), history.reverse());
    }

    @Test
    void clearRemovesEverything() {
        History history = new History();
        history.add(result("1"));
        history.add(result("2"));

        history.clear();

        assertTrue(history.reverse().isEmpty());
    }

    @Test
    void addAfterClear() {
        History history = new History();
        history.add(result("1"));
        history.clear();
        ResultValidation fresh = result("5");

        history.add(fresh);

        assertEquals(List.of(fresh), history.reverse());
    }

    @Test
    void returnedListCannotBeChanged() {
        History history = new History();
        history.add(result("1"));

        List<ResultValidation> list = history.reverse();

        assertThrows(UnsupportedOperationException.class, () -> list.add(result("2")));
    }

    @Test
    void returnedListDoesNotChangeAfterNewAdd() {
        History history = new History();
        ResultValidation first = result("1");
        history.add(first);

        List<ResultValidation> before = history.reverse();
        history.add(result("2"));

        assertEquals(List.of(first), before);
    }
}