package org.example.net;

import com.fastcgi.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public record FastCGUServer(Handler handler) {

    public void connect() {
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            FCGIRequest request = FCGIInterface.request;
            if (request == null) {
                continue;
            }

            write(fcgi, safeHandler(request));
        }
    }

    private Response safeHandler(Request request){
        try{
            return handler.handler(request);
        } catch (RuntimeException e) {
            //todo
            return new Response(500, "***");
        }
    }

    private static void write(FCGIRequest fsgir, Response response){
        String http = "Status: " + response.reason() + "\n" +
        "Content-Type: " + "\n" + response.body();

        try {
            fsgir.outStream.write(http.getBytes(StandardCharsets.UTF_8));
            fsgir.outStream.flush();
        } catch (IOException e) {
            //todo
        }
    }
}
