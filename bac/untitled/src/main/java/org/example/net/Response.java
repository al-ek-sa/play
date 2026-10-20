package org.example.net;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Response(int status, String body) {
    public static Response ok(String body){
        return new Response(200, body);
    }

    public static Response clientError(String body){
        return new Response(400, body);
    }

    public static Response notFoundMethod(){
        return new Response(405, Json.write(new Responses.Failure("method not allowed")));
    }

    public static Response serverError(){
        return new Response(500, Json.write(new Responses.Failure("internal error")));
    }

    public String reason(){
        return switch (status){
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 405 -> "Method Not Allowed";
            case 500 -> "Server error";
            default -> "Internal Server Error";
        };
    }
}