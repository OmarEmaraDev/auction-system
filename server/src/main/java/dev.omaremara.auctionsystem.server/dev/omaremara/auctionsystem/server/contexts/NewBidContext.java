package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.requests.NewBidRequest;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.NewBidException;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class NewBidContext implements HttpHandler {

    private void ensureNewBid(NewBidRequest newBidRequest) throws NewBidException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "SELECT bid FROM lots WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)){
                statement.setInt(1, newBidRequest.lotID);
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.isBeforeFirst()) {
                        throw new NewBidException("There is no id in lot table with this lotID!");
                    } else {
                        result.next();
                        if(newBidRequest.bid <= result.getInt("bid") ){
                            throw new NewBidException("The new bid must be greater than the last bid!");
                        }
                    }
                } catch (SQLException exception) {
                    throw new NewBidException("Couldn't retrieve from database!");
                }
            } catch (SQLException exception){
                throw new NewBidException("Couldn't select from database!");
            }
        }  catch (SQLException exception) {
            throw new NewBidException("Couldn't establish connection to database!");
        }
    }

    private void updateBide(NewBidRequest newBidRequest) throws NewBidException {
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "UPDATE lots SET bidder = ?, bid = ?  WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, newBidRequest.bidderID);
                statement.setInt(2, newBidRequest.bid);
                statement.setInt(3, newBidRequest.lotID);
                statement.execute();
            } catch (SQLException exception){
                throw new NewBidException("Couldn't Update the database!");
            }
        }  catch (SQLException exception) {
            throw new NewBidException("Couldn't establish connection to database!");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStreamReader requestReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        NewBidRequest newBidRequest = Main.gson.fromJson(requestReader, NewBidRequest.class);

        try {
            ensureNewBid(newBidRequest);
            updateBide(newBidRequest);
            exchange.sendResponseHeaders(200, -1);
        } catch (NewBidException exception){
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
