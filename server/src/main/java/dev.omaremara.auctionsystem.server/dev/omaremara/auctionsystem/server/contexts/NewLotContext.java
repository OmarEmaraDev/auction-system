package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.model.requests.NewLotRequest;
import dev.omaremara.auctionsystem.server.exceptions.NewLotException;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class NewLotContext implements HttpHandler {
    private void insertLot(NewLotRequest newLotRequest) throws NewLotException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "INSERT INTO lots VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, getLotsCount());
                statement.setString(2, newLotRequest.title);
                statement.setInt(3,  newLotRequest.sellerID);
                statement.setNull(4, Types.INTEGER);
                statement.setInt(5, newLotRequest.initialBid);
                statement.execute();
            } catch (SQLException exception) {
                throw new NewLotException("Couldn't insert into database!");
            }
        } catch (SQLException exception) {
            throw new NewLotException("Couldn't establish connection to database!");
        }
    }

    private int getLotsCount() throws NewLotException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT COUNT (*) AS count FROM lots";
                try (ResultSet result = statement.executeQuery(query)) {
                    result.next();
                    return result.getInt("count");
                } catch (SQLException exception) {
                    throw new NewLotException("Could not retrieve from database!");
                }
            } catch (SQLException exception) {
                throw new NewLotException("Could not query from database!");
            }
        } catch (SQLException exception) {
            throw new NewLotException("Could not establish connection to database!");
        }
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStreamReader requestReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        NewLotRequest newLotRequest = Main.gson.fromJson(requestReader, NewLotRequest.class);
        try {
            insertLot(newLotRequest);
            exchange.sendResponseHeaders(200, -1);
        } catch (NewLotException exception){
            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
            String response = Main.gson.toJson(errorResponse, ErrorResponse.class);
            exchange.sendResponseHeaders(1064, response.length());
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8);
            writer.write(response);
            writer.flush();
        }

        exchange.close();
    }
}
