package org.example.net;

import com.fastcgi.*;


public final class FastCGUServer {

    public void connect(){
        FCGIInterface fcgi = new FCGIInterface();

         while (fcgi.FCGIaccept() >= 0){
         }
    }
}
