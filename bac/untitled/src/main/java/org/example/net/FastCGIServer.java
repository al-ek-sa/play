package org.example.net;

import com.fastcgi.FCGIInterface;
import com.fastcgi.FCGIRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
public record FastCGIServer(Handler handler) {
    private static final int MAX_BODY = 1024;

    public void connect() {
        FCGIInterface fcgii = new FCGIInterface();

        while (fcgii.FCGIaccept() >= 0) {
            FCGIRequest request = FCGIInterface.request;
            if (request == null) {
                continue;
            }

            write(request, safeHandler(request));
        }
    }

    private Response safeHandler(FCGIRequest raw){
        try {
            return handler.handler(toRequest(raw));
        } catch (RuntimeException | IOException e) {
            return Response.serverError();
        }
    }

    private static Request toRequest(FCGIRequest raw) throws IOException {
        Properties params = raw.params;
        return new Request(
                params.getProperty("REQUEST_URI", ""),
                params.getProperty("REQUEST_METHOD", "GET"),
                QueryParser.parse(body(raw))
        );
    }

    private static String body(FCGIRequest raw) throws IOException {
        int length = contentLength(raw.params);
        if (length <= 0) {
            return "";
        }
        byte[] bytes = raw.inStream.readNBytes(Math.min(length, MAX_BODY));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static int contentLength(Properties params){
        try {
            return Integer.parseInt(params.getProperty("CONTENT_LENGTH", "0").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
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