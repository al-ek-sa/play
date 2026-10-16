package org.example.net;

@FunctionalInterface
public interface Handler {
    Response handler(Request request);
}
