package org.example.net;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class QueryParser {

    public Map<String, String> parse(String query){
        if (query == null || query.isEmpty()) {
            return Map.of();
        }
        return Arrays.stream(query.split("&"))
                .filter(pair -> !pair.isEmpty())
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.toMap(
                        p -> decode(p[0]),
                        p -> p.length > 1 ? decode(p[1]) : "",
                        (first, _) -> first
                ));
    }

    private String decode(String s){
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}