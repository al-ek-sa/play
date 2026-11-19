package org.example;

import org.example.history.History;

public class Main {
    static void main() {
        new FastCGIServer(new CheckHandler(new History())).connect();
    }
}
