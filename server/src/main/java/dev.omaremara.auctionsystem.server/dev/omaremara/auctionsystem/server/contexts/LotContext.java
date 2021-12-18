package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import dev.omaremara.auctionsystem.model.Lot;
import dev.omaremara.auctionsystem.model.User;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.LotException;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;

public class LotContext implements HttpHandler {
    private void getLots(List<Lot> lots) throws LotException{
        String connectionURL = System.getProperty("auctionSystem.JDBC.connection.url");
        try (Connection connection = DriverManager.getConnection(connectionURL, "postgres", "0")) {
            String query = "SELECT " +
                    "lot.id, " +
                    "lot.title, " +
                    "lot.bid, " +
                    "seller.id AS sellerID " +
                    "seller.name AS sellerName, " +
                    "seller.password AS sellerPassword, " +
                    "bidder.id AS bidderID " +
                    "bidder.name AS bidderName, " +
                    "bidder.password AS bidderPassword, " +
                    "FROM lots AS lot " +
                    "JOIN users AS bidder " +
                    "ON bidder.id = lot.bidder " +
                    "JOIN users AS seller " +
                    "ON seller.id = lot.seller;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.isBeforeFirst()) {
                         throw new LotException("There is an error.");
                    } else {
                        while (result.next()) {
                            Lot lot = new Lot();
                            lot.id = result.getInt("id");
                            lot.title = result.getString("title");
                            lot.currentBid = result.getInt("bid");
                            User seller = new User(result.getInt("sellerID"), result.getString("sellerName"),
                                    result.getString("sellerPassword"));
                            lot.seller = seller;
                            User bidder = new User(result.getInt("bidderID"), result.getString("bidderName"),
                                    result.getString("bidderPassword"));
                            lot.currentBidder = bidder;
                            lots.add(lot);
                        }
                    }
                } catch (SQLException exception) {
                    throw new LotException("Couldn't retrieve lot from database!");
                }
            } catch (SQLException exception) {
                throw new LotException("Couldn't query from database!");
            }
        } catch (SQLException exception) {
            throw new LotException("Couldn't establish connection to database!");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        try {
            List<Lot> lots = new ArrayList<>();
            getLots(lots);
            response = Main.gson.toJson(lots);
            exchange.sendResponseHeaders(200, response.length());
        } catch (LotException exception){
            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
            response = Main.gson.toJson(errorResponse, ErrorResponse.class);
            exchange.sendResponseHeaders(401, response.length());
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8);
            writer.write(response);

            writer.flush();
        }

        exchange.close();
    }
}
