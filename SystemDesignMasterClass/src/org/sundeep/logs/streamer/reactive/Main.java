package org.sundeep.logs.streamer.reactive;

import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.NettyOutbound;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

class Config {
    public static final int SERVER_PORT = 8080;
    public static final String LOG_FILE_PATH = "target/logs.txt";
    public static final String HTML_FILE_PATH = "src/org/sundeep/logs/streamer/index.html";
}

class LogsWriter {
    public Disposable generateLogs() {
        return Flux.interval(Duration.ofMillis((int) (1000 * (Math.random() * 2 + 1))))
            .doOnNext(tick -> {
                try {
                    Files.writeString(
                        Path.of(Config.LOG_FILE_PATH),
                    "Log entry at " + System.currentTimeMillis() + "\n",
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                    );
                } catch (IOException e) {
                    System.out.println("Log generation stopped. Reason: " + e.getMessage());
                }
            })
            .subscribe();
    }
}

class ServerHandler {
    private final Path HTML_FILE_PATH = Path.of(Config.HTML_FILE_PATH);
    private final byte[] fileBytes;

    public ServerHandler() throws IOException {
        fileBytes = Files.readString(HTML_FILE_PATH).getBytes();
    }

    public Publisher<Void> getHomePage(HttpServerRequest req, HttpServerResponse res) {
        return res
                .header("Content-Type", "text/html; charset=UTF-8")
                .chunkedTransfer(false)
                .sendByteArray(Mono.just(fileBytes));
    }

    public Publisher<Void> handleLogStream(HttpServerRequest req, HttpServerResponse res) {
        String userAgent = req.requestHeaders().get("User-Agent");
        String clientRemoteAddr = req.remoteAddress().toString();
        System.out.println("Client connected: " + clientRemoteAddr + ", User Agent: " + userAgent);

        return res
            .header("Content-Type", "text/event-stream; charset=UTF-8")
            .header("Cache-Control", "no-cache")
            .header("Connection", "keep-alive")
            .sendString(LogsReader.getLogFlux())
            .then()
            .doOnTerminate(() -> System.out.println("Client disconnected: " + clientRemoteAddr + ", User Agent: " + userAgent));
    }
}

class LogsReader {
    /**
     * Creates a reactive stream that polls the file for new lines.
     */
    public static Flux<String> getLogFlux() {
        AtomicLong lastPos = new AtomicLong(0);

        return Flux.interval(Duration.ofSeconds(1)) // Non-blocking polling
                .flatMap(tick -> Mono.fromCallable(() -> {
                    Path path = Path.of(Config.LOG_FILE_PATH);
                    if (!Files.exists(path)) return "";

                    try (var reader = Files.newBufferedReader(path)) {
                        reader.skip(lastPos.get());
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append("data: ").append(line).append("\n\n");
                            lastPos.addAndGet(line.length() + 1);
                        }
                        return sb.toString();
                    }
                }))
                .filter(s -> !s.isEmpty());
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        LogsWriter writer = new LogsWriter();
        writer.generateLogs();

        System.out.println("Started log generation. Writing logs to " + Config.LOG_FILE_PATH);

        ServerHandler handler = new ServerHandler();

        HttpServer.create()
            .port(Config.SERVER_PORT)
            .route(routes -> routes
                .get("/", handler::getHomePage)
                .get("/logs/stream", handler::handleLogStream)
            )
            .bindNow()
            .onDispose()
            .block();
    }
}
