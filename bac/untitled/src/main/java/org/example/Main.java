package org.example;

public class Main {
    static void main() {
        new FastCGIServer(new CheckHandler(new History())).connect();
    }
}
