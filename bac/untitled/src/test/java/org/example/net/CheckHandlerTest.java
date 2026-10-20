package org.example.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.history.History;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckHandlerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
        CheckHandler handler = new CheckHandler(new History());

        Response response = handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        assertEquals(200, response.status());
        JsonNode body = json(response);
        assertTrue(body.get("result").get("hit").asBoolean());
        assertEquals("1", body.get("result").get("x").asText());
        assertEquals(1, body.get("history").size());
    }

    @Test
    void missReturnsHitFalse() throws JsonProcessingException {
        CheckHandler handler = new CheckHandler(new History());

        Response response = handler.handler(post(Map.of("x", "-1", "y", "1", "r", "4")));

        assertEquals(200, response.status());
        assertFalse(json(response).get("result").get("hit").asBoolean());
    }

    @Test
    void commaInNumberWorks() throws JsonProcessingException {
        CheckHandler handler = new CheckHandler(new History());

        Response response = handler.handler(post(Map.of("x", "1,5", "y", "1", "r", "4")));

        assertEquals(200, response.status());
        assertEquals("1.5", json(response).get("result").get("x").asText());
    }

    @Test
    void invalidReturnsBadRequest() throws JsonProcessingException {
        CheckHandler handler = new CheckHandler(new History());

        Response response = handler.handler(post(Map.of("x", "abc", "y", "1", "r", "4")));

        assertEquals(400, response.status());
        JsonNode error = json(response).get("errors").get(0);
        assertEquals("x", error.get("field").asText());
        assertEquals("NOT_NUMBER", error.get("code").asText());
    }

    @Test
    void invalidIsNotAddedToHistory() throws JsonProcessingException {
        History history = new History();
        CheckHandler handler = new CheckHandler(history);

        handler.handler(post(Map.of("x", "abc", "y", "1", "r", "4")));

        assertTrue(history.reverse().isEmpty());
    }

    @Test
    void emptyPostReturnsHistoryOnly() throws JsonProcessingException {
        CheckHandler handler = new CheckHandler(new History());
        handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        Response response = handler.handler(post(Map.of()));

        assertEquals(200, response.status());
        JsonNode body = json(response);
        assertFalse(body.has("result"));
        assertEquals(1, body.get("history").size());
    }

    @Test
    void historyNewestFirst() throws JsonProcessingException {
        CheckHandler handler = new CheckHandler(new History());
        handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        Response response = handler.handler(post(Map.of("x", "2", "y", "1", "r", "4")));

        JsonNode history = json(response).get("history");
        assertEquals(2, history.size());
        assertEquals("2", history.get(0).get("x").asText());
        assertEquals("1", history.get(1).get("x").asText());
    }

    @Test
    void deleteClearsHistory() throws JsonProcessingException {
        History history = new History();
        CheckHandler handler = new CheckHandler(history);
        handler.handler(post(Map.of("x", "1", "y", "1", "r", "4")));

        Response response = handler.handler(request("DELETE"));

        assertEquals(200, response.status());
        assertEquals(0, json(response).get("history").size());
        assertTrue(history.reverse().isEmpty());
    }

    @Test
    void getIsNotAllowed() {
        CheckHandler handler = new CheckHandler(new History());

        Response response = handler.handler(request("GET"));

        assertEquals(405, response.status());
    }

    @Test
    void responseHasTimeAndExecution() throws JsonProcessingException {
        CheckHandler handler = new CheckHandler(new History());

        JsonNode body = json(handler.handler(post(Map.of("x", "1", "y", "1", "r", "4"))));

        assertTrue(body.has("currentTime"));
        assertTrue(body.get("executionMicros").asLong() >= 0);
    }
}