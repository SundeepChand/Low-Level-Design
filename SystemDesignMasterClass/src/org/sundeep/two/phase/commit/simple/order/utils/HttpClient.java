package org.sundeep.two.phase.commit.simple.order.utils;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpClient {
    private static final java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

    public static HttpResponse<String> get(String url) throws RuntimeException {
        try {
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(new java.net.URI(url))
                    .GET()
                    .build();

            return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("[HttpClient.get][ERROR]: Error in calling URL " + url + ": " + e.getMessage());
            throw new RuntimeException("Error in calling URL " + url, e);
        }
    }

    public static CompletableFuture<HttpResponse<String>> getAsync(String url) throws RuntimeException {
        try {
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new java.net.URI(url))
                .GET()
                .build();

            return client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("[HttpClient.get][ERROR]: Error in calling URL " + url + ": " + e.getMessage());
            throw new RuntimeException("Error in calling URL " + url, e);
        }
    }
}
