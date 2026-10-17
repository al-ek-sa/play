package org.example;

import org.example.history.History;
import org.example.net.CheckHandler;
import org.example.net.FastCGIServer;

public class Main {
    static void main() {
        new FastCGIServer(new CheckHandler(new History())).connect();
    }
}
