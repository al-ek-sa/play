package org.example.net;

import org.example.figure.Valid;

public final class CheckHandler implements Handler{
    @Override
    public Response handler(Request request) {
        if(!request.method().equals("GET")){
            return Response.notFoundMethod();
        }

        return switch (){
            case Valid.Success -> Response.ok("OK");
            case Valid.Failure -> Response.clientError("Error");
        };
    }
}
