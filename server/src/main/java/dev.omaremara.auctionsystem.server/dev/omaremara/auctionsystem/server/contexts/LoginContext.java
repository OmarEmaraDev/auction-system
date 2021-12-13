package dev.omaremara.auctionsystem.server.dev.omaremara.auctionsystem.server.contexts;

import java.io.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import dev.omaremara.auctionsystem.model.requests.LoginRequest;
// import org.json.simple.JSONObject;

public class LoginContext implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        int length = Integer.parseInt(t.getRequestHeaders().getFirst("Content-Length"));
        InputStream is = t.getRequestBody();
        byte[] bytes = is.readNBytes(length);
        String str = new String(bytes, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        LoginRequest login = gson.fromJson(str, LoginRequest.class);
        System.out.println(login.name);
        System.out.println(login.password);
        String response = "This is the response";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
