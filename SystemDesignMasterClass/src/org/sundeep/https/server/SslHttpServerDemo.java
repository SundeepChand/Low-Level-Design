package org.sundeep.https.server;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

public class SslHttpServerDemo {
    public static void main(String[] args) {
        try {
            String password = "1234";
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream("src/org/sundeep/https/server/keystore.p12"), password.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            int PORT = 8080;

            HttpsServer server = HttpsServer.create(new InetSocketAddress(PORT), 0);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext));

            server.createContext("/", exchange -> {
                String response = "Hello, World!";
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });
            server.setExecutor(Executors.newCachedThreadPool());

            server.start();

            System.out.println("HTTPS server started on port " + PORT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
