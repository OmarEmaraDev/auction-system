package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.model.requests.RegisterRequest;
import dev.omaremara.auctionsystem.server.exceptions.RegisterException;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class RegisterContext implements HttpHandler {

    private void ensureNewUser(RegisterRequest registerRequest) throws RegisterException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "SELECT * FROM users WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, registerRequest.name);
                try (ResultSet result = statement.executeQuery()) {
                    if (result.isBeforeFirst()) {
                        throw new RegisterException("User already exist.");
                    }
                } catch (SQLException exception) {
                    throw new RegisterException("Couldn't retrieve user from database!");
                }
            } catch (SQLException exception) {
                throw new RegisterException("Couldn't query from database!");
            }
        } catch (SQLException exception) {
            throw new RegisterException("Couldn't establish connection to database!");
        }
    }

    private void insertUser(RegisterRequest registerRequest) throws RegisterException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "INSERT INTO users VALUES(?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, getUsersCount());
                statement.setString(2, registerRequest.name);
                statement.setString(3, registerRequest.password);
                statement.execute();
            } catch (SQLException exception) {
                throw new RegisterException("Couldn't insert into database!");
            }
        } catch (SQLException exception) {
            throw new RegisterException("Couldn't establish connection to database!");
        }
    }

    private int getUsersCount() throws RegisterException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT COUNT (*) AS count FROM users";
                try (ResultSet result = statement.executeQuery(query)) {
                    result.next();
                    return result.getInt("count");
                } catch (SQLException exception) {
                    throw new RegisterException("Could not retrieve from database!");
                }
            } catch (SQLException exception) {
                throw new RegisterException("Could not query from database!");
            }
        } catch (SQLException exception) {
            throw new RegisterException("Could not establish connection to database!");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStreamReader requestReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        RegisterRequest registerRequest = Main.gson.fromJson(requestReader, RegisterRequest.class);

        try{
            ensureNewUser(registerRequest);
            insertUser(registerRequest);
            exchange.sendResponseHeaders(200, -1);
        } catch (RegisterException exception){
            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
            String response = Main.gson.toJson(errorResponse, ErrorResponse.class);
            exchange.sendResponseHeaders(403, response.length());
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8);
            writer.write(response);
            writer.flush();
        }

        exchange.close();
    }
}
