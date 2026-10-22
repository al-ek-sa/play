package org.example.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.history.History;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckHandlerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private History history;
    private CheckHandler handler;

    @BeforeEach
    void cleanDatabase() {
        history = new History();
        history.clear();
        handler = new CheckHandler(history);
    }

    private static Request post(Map<String, String> data) {
        return new Request("/fcgi-bin/check", "POST", data);
    }

    private static Request request(String method) {
        return new Request("/fcgi-bin/check", method, Map.of());
    }

    private static JsonNode json(Response response) throws JsonProcessingException {
        return MAPPER.readTree(response.body());
    }

    @Test
    void hitReturnsOkAndResult() throws JsonProcessingException {
        Response response = handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        assertEquals(200, response.status());
        JsonNode body = json(response);
        assertTrue(body.get("result").get("hit").asBoolean());
        assertEquals("1", body.get("result").get("x").asText());
        assertEquals(1, body.get("history").size());
    }

    @Test
    void missReturnsHitFalse() throws JsonProcessingException {
        Response response = handler.handler(post(Map.of("x", "-1", "y", "1", "r", "4")));

        assertEquals(200, response.status());
        assertFalse(json(response).get("result").get("hit").asBoolean());
    }

    @Test
    void commaInNumberWorks() throws JsonProcessingException {
        Response response = handler.handler(post(Map.of("x", "1,5", "y", "1", "r", "4")));

        assertEquals(200, response.status());
        assertEquals("1.5", json(response).get("result").get("x").asText());
    }

    @Test
    void invalidReturnsBadRequest() throws JsonProcessingException {
        Response response = handler.handler(post(Map.of("x", "abc", "y", "1", "r", "4")));

        assertEquals(400, response.status());
        JsonNode error = json(response).get("errors").get(0);
        assertEquals("x", error.get("field").asText());
        assertEquals("NOT_NUMBER", error.get("code").asText());
    }

    @Test
    void invalidIsNotAddedToHistory() {
        handler.handler(post(Map.of("x", "abc", "y", "1", "r", "4")));

        assertTrue(history.reverse().isEmpty());
    }

    @Test
    void emptyPostReturnsHistoryOnly() throws JsonProcessingException {
        handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        Response response = handler.handler(post(Map.of()));

        assertEquals(200, response.status());
        JsonNode body = json(response);
        assertFalse(body.has("result"));
        assertEquals(1, body.get("history").size());
    }

    @Test
    void historyNewestFirst() throws JsonProcessingException {
        handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        Response response = handler.handler(post(Map.of("x", "2", "y", "1", "r", "4")));

        JsonNode list = json(response).get("history");
        assertEquals(2, list.size());
        assertEquals("2", list.get(0).get("x").asText());
        assertEquals("1", list.get(1).get("x").asText());
    }

    @Test
    void deleteClearsHistory() throws JsonProcessingException {
        handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        Response response = handler.handler(request("DELETE"));

        assertEquals(200, response.status());
        assertEquals(0, json(response).get("history").size());
        assertTrue(history.reverse().isEmpty());
    }

    @Test
    void getIsNotAllowed() {
        Response response = handler.handler(request("GET"));

        assertEquals(405, response.status());
    }

    @Test
    void responseHasTimeAndExecution() throws JsonProcessingException {
        JsonNode body = json(handler.handler(post(Map.of("x", "1", "y", "1", "r", "4"))));

        assertTrue(body.has("currentTime"));
        assertTrue(body.get("executionMicros").asLong() >= 0);
    }
}