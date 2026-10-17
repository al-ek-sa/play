package org.example.net;

import org.example.figure.Coordinate;
import org.example.figure.PointValidator;
import org.example.figure.Valid;
import org.example.history.History;

public record CheckHandler(History history) implements Handler{
    @Override
    public Response handler(Request request) {
        long start = System.nanoTime();

        if(!request.method().equals("GET")){
            return Response.notFoundMethod();
        }

        return switch (PointValidator.validate(request.data())){
            case Valid.Success<Coordinate> success -> Response.ok("OK");
            case Valid.Failure<Coordinate> failure -> Response.clientError("Error");
        };
    }
}
