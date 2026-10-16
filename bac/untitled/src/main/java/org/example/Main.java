package org.example;

import org.example.net.CheckHandler;
import org.example.net.FastCGUServer;

public class Main {
    static void main() {
        new FastCGUServer(new CheckHandler()).connect();
    }
}
