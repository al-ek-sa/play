package org.example;

import org.example.history.History;
import org.example.net.CheckHandler;
import org.example.net.FastCGUServer;

public class Main {
    static void main() {
        new FastCGUServer(new CheckHandler(new History())).connect();
    }
}
