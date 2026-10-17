package org.example.net;

import com.fastcgi.FCGIInterface;
import com.fastcgi.FCGIRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public record FastCGIServer(Handler handler) {

    public void connect() {
        FCGIInterface fcgii = new FCGIInterface();

        while (fcgii.FCGIaccept() >= 0) {
            FCGIRequest request = FCGIInterface.request;
            if (request == null) {
                continue;
            }

            write(request, safeHandler(toRequest(request.params)));
        }
    }

    private Response safeHandler(Request request){
        try {
            return handler.handler(request);
        } catch (RuntimeException e) {
            //todo
            return Response.serverError();
        }
    }

    private static Request toRequest(Properties params){
        return new Request(
                params.getProperty("REQUEST_URI", ""),
                params.getProperty("REQUEST_METHOD", "GET"),
                QueryParser.parse(params.getProperty("QUERY_STRING"))
        );
    }

    private static void write(FCGIRequest request, Response response){
        String http = "Status: " + response.status() + " " + response.reason() + "\r\n"
                + "Content-Type: application/json; charset=UTF-8\r\n"
                + "\r\n"
                + response.body();

        try {
            request.outStream.write(http.getBytes(StandardCharsets.UTF_8));
            request.outStream.flush();
        } catch (IOException e) {
            //todo
        }
    }
}