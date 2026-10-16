package org.example.net;

import com.fastcgi.*;

public record FastCGUServer(Handler handler) {

    public void connect() {
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            FCGIRequest request = FCGIInterface.request;
            if (request == null) {
                continue;
            }

            
        }
    }
}
