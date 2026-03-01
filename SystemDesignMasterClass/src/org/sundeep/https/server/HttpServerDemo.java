package org.sundeep.https.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerDemo {
    public static void main(String[] args) {
        try {
            int PORT = 8080;
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", exchange -> {
                String response = "Hello, World!";
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("HTTP server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
