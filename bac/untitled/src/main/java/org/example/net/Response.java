package org.example.net;

public record Response(int status, String body) {
    public static Response ok(String body){
        return new Response(200, body);
    }

    public static Response clientError(String body){
        return new Response(400, body);
    }

    public static Response notFoundMethod(){
        return new Response(405, "don't know");
    }

    public static Response serverError(){
        return new Response(500, "{\"error\":\"internal error\"}");
    }

    public String reason(){
        return switch (status){
            case(200) -> "OK";
            case(400) -> "Bad Request";
            case(405) -> "Method Not Allowed";
            default -> "Internal Server Error";
        };
    }
}
