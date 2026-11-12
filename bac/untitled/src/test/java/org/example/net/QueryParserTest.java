package org.example.net;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryParserTest {
    @ParameterizedTest
    @CsvSource({
            "x=1,           x,  1",
            "x=-2.5,        x,  -2.5",
            "x=1%2C5,       x,  '1,5'",
            "x=1+2,         x,  1 2",
            "x=,            x,  ''",
            "x,             x,  ''",
            "x=a=b,         x,  a=b",
            "x=1&x=999,     x,  1",
            "&&x=1&,        x,  1",
            "=5,            '', 5",
            "x=%20%201%20,  x,  '  1 '"
    })
    void parse(String query, String key, String expected){
        assertEquals(expected, QueryParser.parse(query).get(key));
    }
}
