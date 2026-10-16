package org.example.net;

public record Response(int status, String body) {
    public static Response ok(String body){
        return new Response(200, body);
    }

    public static Response clientError(String body){
        return new Response(400, body);
    }

    public String reason(){
        return switch (status){
            case(200) -> "OK";
            case(400) -> "Bad Request";
            default -> "Internal Server Error";
        };
    }
}
