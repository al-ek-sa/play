package org.example.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {
    @Test
    void ok(){
        String body = "{\"a\":1}";
        int status = 200;
        Response response = Response.ok(body);

        assertEquals(status, response.status());
        assertEquals(body, response.body());
    }

    @Test
    void clientError(){
        String body = "{\"a\":1}";
        int status = 400;
        Response response = Response.clientError(body);
        assertEquals(status, response.status());
        assertEquals(body, response.body());
    }

    @Test
    void serverError(){
        int status = 500;
        Response response = Response.serverError();
        assertEquals(status, response.status());
    }

    @Test
    void notFoundMethod(){
        int status = 405;
        Response response = Response.notFoundMethod();
        assertEquals(status, response.status());
    }

    @Test
    void reason200(){
        Response response = Response.ok("200");
        assertEquals("OK", response.reason());
    }

    @Test
    void reason405(){
        Response response = Response.notFoundMethod();
        assertEquals("Method Not Allowed", response.reason());
    }

    @Test
    void reason500(){
        Response response = Response.clientError("400");
        assertEquals("Bad Request", response.reason());
    }
}
