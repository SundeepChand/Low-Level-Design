package org.sundeep.two.phase.commit.simple.delivery;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.sundeep.two.phase.commit.simple.delivery.configs.ServerConfig;
import org.sundeep.two.phase.commit.simple.delivery.service.AgentService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

class ServerHandler {
    private final AgentService service;

    public ServerHandler() {
        this.service = new AgentService();
    }

    public void handlePrepareAgentAssignment(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Read orderId from query parameters (e.g. /agent/prepare?orderId=123)
        String orderId = getQueryParam(exchange, "orderId");
        if (orderId == null || orderId.isEmpty()) {
            exchange.sendResponseHeaders(400, -1); // Bad Request
            return;
        }

        try {
            int agentId = service.reserveAgentForOrder(orderId);
            byte[] responseBody = Integer.toString(agentId).getBytes();

            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, responseBody.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBody);
            }
        } catch (IOException e) {
            byte[] responseBody = e.getMessage().getBytes();
            exchange.sendResponseHeaders(500, responseBody.length);
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBody);
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private String getQueryParam(HttpExchange exchange, String name) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) return null;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                if (key.equals(name)) return value;
            }
        }
        return null;
    }

    public void handleCommitAgentAssignment(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Read orderId from query parameters (e.g. /agent/assign?orderId=123&agentId=456)
        String orderId = getQueryParam(exchange, "orderId");
        String agentId = getQueryParam(exchange, "agentId");

        if (orderId == null || orderId.isEmpty() || agentId == null || agentId.isEmpty()) {
            exchange.sendResponseHeaders(400, -1); // Bad Request
            return;
        }

        try {
            service.assignAgentToOrder(orderId, agentId);

            byte[] responseBody = "Success".getBytes();
            exchange.sendResponseHeaders(200, responseBody.length);
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBody);
            }

        } catch (IOException e) {
            byte[] responseBody = e.getMessage().getBytes();
            exchange.sendResponseHeaders(500, responseBody.length);
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBody);
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        // Create two HTTP endpoints
        // 1. To prepare an agent for assignment
        // 2. To commit the order assignment to the agent
        ServerHandler serverHandler = new ServerHandler();

        HttpServer server = HttpServer.create(new InetSocketAddress(ServerConfig.SERVER_PORT), 0);

        server.createContext("/agent/prepare", serverHandler::handlePrepareAgentAssignment);

        server.createContext("/agent/assign", serverHandler::handleCommitAgentAssignment);

        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
        System.out.println("HTTP server started on http://localhost:" + ServerConfig.SERVER_PORT);
    }
}
