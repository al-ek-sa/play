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
        log.info("FastCGI server started on port {}", System.getProperty("FCGI_PORT"));

        while (fcgii.FCGIaccept() >= 0) {
            FCGIRequest request = FCGIInterface.request;
            if (request == null) {
                log.warn("FCGIaccept returned without request");
                continue;
            }

            write(request, safeHandler(request));
        }
        log.info("FastCGI server stopped");
    }

    private Response safeHandler(FCGIRequest raw){
        String method = raw.params.getProperty("REQUEST_METHOD");
        String uri = raw.params.getProperty("REQUEST_URI");
        try {
            Response response = handler.handler(toRequest(raw));
            log.debug("{} {} -> {}", method, uri, response.status());
            return response;
        } catch (RuntimeException | IOException e) {
            log.error("{} {} failed", method, uri, e);
            return Response.serverError();
        }
    }

    private static Request toRequest(FCGIRequest raw) throws IOException {
        Properties params = raw.params;
        return new Request(
                params.getProperty("REQUEST_URI", ""),
                params.getProperty("REQUEST_METHOD", "POST"),
                QueryParser.parse(body(raw))
        );
    }

    private static String body(FCGIRequest raw) throws IOException {
        int length = contentLength(raw.params);
        System.err.println("12345678901234567890");
        if (length <= 0) {
            return "";
        }
        byte[] bytes = raw.inStream.readNBytes(Math.min(length, MAX_BODY));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static int contentLength(Properties params){
        String value = params.getProperty("CONTENT_LENGTH", "0").trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("invalid CONTENT_LENGTH: '{}'", value);
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
            log.error("failed to write response {}", response.status(), e);
        }
    }
}