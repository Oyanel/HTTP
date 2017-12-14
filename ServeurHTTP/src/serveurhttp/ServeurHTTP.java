package serveurhttp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 *
 * @author Epulapp
 */
public class ServeurHTTP {

    private static final String FILE_DIRECTORY = "C:" + File.separator + "Users" + File.separator + "Epulapp" + File.separator + "Documents" + File.separator + "Cours" + File.separator + "2017" + File.separator + "ARSIR" + File.separator + "server";

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            OutputStream os = null;
            byte[] RESPONSE;

            try {
                System.out.println(t.getRequestURI());
                Path pagePath = Paths.get(FILE_DIRECTORY + t.getRequestURI());
                File file = new File(pagePath.toString());
                if (file.exists()) {
                    RESPONSE = Files.readAllBytes(pagePath);
                    if (pagePath.getFileName().toString().endsWith("png")) {
                        t.getResponseHeaders().add("Content-Type", "image/png");
                    } else if (pagePath.getFileName().toString().endsWith("html")) {
                        t.getResponseHeaders().add("Content-Type", "text/html");
                    }
                    t.sendResponseHeaders(200, RESPONSE.length);
                } else {
                    RESPONSE = "Error 404".getBytes();
                    t.sendResponseHeaders(404, RESPONSE.length);
                }
                os = t.getResponseBody();
                os.write(RESPONSE);
                os.flush();
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }
}
