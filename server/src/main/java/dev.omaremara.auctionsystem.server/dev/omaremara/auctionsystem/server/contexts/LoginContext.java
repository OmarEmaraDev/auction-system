package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.User;
import dev.omaremara.auctionsystem.model.requests.LoginRequest;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.LoginException;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;


public class LoginContext implements HttpHandler {
    private User getUser(LoginRequest loginRequest) throws LoginException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "SELECT * FROM users WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, loginRequest.name);
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.isBeforeFirst()) {
                        throw new LoginException("No user exists with this name.");
                    } else {
                        result.next();
                        String password = result.getString("password");
                        if(password.equals(loginRequest.password)){
                            return new User(Integer.parseInt(result.getString("id")), result.getString("name"), result.getString("password"));
                        }
                        else {
                            throw new LoginException("Wrong password");
                        }
                    }
                } catch (SQLException exception) {
                    throw new LoginException("Couldn't retrieve user from database!");
                }
            } catch (SQLException exception) {
                throw new LoginException("Couldn't query from database!");
            }
        } catch (SQLException exception) {
            throw new LoginException("Couldn't establish connection to database!");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStreamReader requestReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        LoginRequest loginRequest = Main.gson.fromJson(requestReader, LoginRequest.class);

        String response;
        try {
            User user = getUser(loginRequest);
            response = Main.gson.toJson(user, User.class);
            exchange.sendResponseHeaders(200, response.length());

        } catch (LoginException exception){
            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
            response = Main.gson.toJson(errorResponse, ErrorResponse.class);
            exchange.sendResponseHeaders(401, response.length());
        }
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8);
        writer.write(response);

        writer.flush();
        exchange.close();
    }
}
