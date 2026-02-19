package org.sundeep.logs.streamer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;

class Config {
    public static final int SERVER_PORT = 8080;
    public static final String LOG_FILE_PATH = "target/logs.txt";
    public static final String HTML_FILE_PATH = "src/org/sundeep/logs/streamer/index.html";
}

class LogsWriter {
    public void generateLogs() {
        BufferedOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new BufferedOutputStream(new FileOutputStream(Config.LOG_FILE_PATH, true));
            System.out.println("Started log generation. Writing logs to " + Config.LOG_FILE_PATH);
            while (true) {
                String logEntry = "Log entry at " + System.currentTimeMillis() + "\n";
                fileOutputStream.write(logEntry.getBytes());
                fileOutputStream.flush();
                try {
                    Thread.sleep((int) (1000 * (Math.random() * 2 + 1))); // Simulate log generation every second
                } catch (Exception e) {
                    System.out.println("Log generation stopped. Reason: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerHandler {
    private final Path HTML_FILE_PATH = Path.of(Config.HTML_FILE_PATH);
    private final byte[] fileBytes;

    public ServerHandler() throws IOException {
        fileBytes = Files.readString(HTML_FILE_PATH, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8);
    }

    public void handleHomePage(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, fileBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileBytes);
        }
    }

    public void streamLogs(HttpExchange exchange) throws IOException {
        String clientIp = exchange.getRemoteAddress().toString();
        String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
        System.out.println("New client connected: " + clientIp + ", User agent: " + userAgent);

        exchange.getResponseHeaders().add("Content-Type", "text/event-stream");
        exchange.getResponseHeaders().add("Cache-Control", "no-cache");
        exchange.getResponseHeaders().add("Connection", "keep-alive");

        try (BufferedReader reader = new BufferedReader(new FileReader(Config.LOG_FILE_PATH))) {
            exchange.sendResponseHeaders(200, 0); // Use chunked encoding
            OutputStream os = exchange.getResponseBody();

            try {
                String line;
                while (true) {
                    while ((line = reader.readLine()) != null) {
                        String data = "data: " + line + "\n\n";
                        os.write(data.getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    }

                    try {
                        Thread.sleep(1000); // Check for new logs every second
                    } catch (InterruptedException e) {
                        System.out.println("Log streaming stopped. Reason: " + e.getMessage());
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error sending SSE data: " + e.getMessage());
            } finally {
                os.close();
                System.out.println("Connection closed: " + clientIp + ", User agent: " + userAgent);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        LogsWriter logsWriter = new LogsWriter();
        Thread logGenerationThread = new Thread(logsWriter::generateLogs);
        logGenerationThread.start();

        ServerHandler serverHandler = new ServerHandler();

        HttpServer server = HttpServer.create(new InetSocketAddress(Config.SERVER_PORT), 0);
        server.createContext("/", serverHandler::handleHomePage);

        server.createContext("/logs/stream", serverHandler::streamLogs);

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("HTTP server started on http://localhost:" + Config.SERVER_PORT);
    }
}
