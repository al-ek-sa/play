package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.history.History;
import org.example.net.CheckHandler;
import org.example.net.FastCGIServer;

@Slf4j
public class Main {
    static void main() {
        new FastCGIServer(new CheckHandler(new History())).connect();
    }
}
