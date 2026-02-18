package org.sundeep.sse.demo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

class HealthCheckServer {
    public static void handleHealthCheck(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Cache-Control", "no-cache");
        exchange.getResponseHeaders().add("Connection", "keep-alive");

        String response = "{\"status\": \"ok\"}";

        sendResponse(exchange, 200, response);
    }

    private static void sendResponse(HttpExchange exchange, int status, String body)
            throws IOException {

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

class SseServer {
    public static void handleSse(HttpExchange exchange) throws IOException {
        String clientIp = exchange.getRemoteAddress().toString();
        String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
        System.out.println("New client connected: " + clientIp + ", User agent: " + userAgent);

        exchange.getResponseHeaders().add("Content-Type", "text/event-stream");
        exchange.getResponseHeaders().add("Cache-Control", "no-cache");
        exchange.getResponseHeaders().add("Connection", "keep-alive");

        exchange.sendResponseHeaders(200, 0);

        OutputStream os = exchange.getResponseBody();

        try {
            int counter = 0;
            while (true) {
                String data = "data: message " + counter++ + "\n\n";
                os.write(data.getBytes(StandardCharsets.UTF_8));
                os.flush();

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error sending SSE data: " + e.getMessage());
        } finally {
            os.close();
            System.out.println("Connection closed: " + clientIp + ", User agent: " + userAgent);
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/health", HealthCheckServer::handleHealthCheck);
        server.createContext("/events", SseServer::handleSse);

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("SSE Server started on port 8080");
    }
}
