package org.example.net;

import java.util.Map;

public record Request(String url, String method, Map<String, String> data) {
}
