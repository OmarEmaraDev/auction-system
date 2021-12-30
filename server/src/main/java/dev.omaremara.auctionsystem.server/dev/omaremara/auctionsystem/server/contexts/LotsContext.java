package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.DataBaseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LotsContext implements HttpHandler {
  private String getLots() throws DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      try (Statement statement = connection.createStatement()) {
        String query = """
          SELECT COALESCE(json_agg(fullLots), '[]') AS result FROM (
            SELECT
              lot.id,
              lot.title,
              lot.bid AS "currentBid",
              (SELECT to_json(seller.*) AS seller FROM (
                SELECT * FROM users WHERE id = lot.seller
              ) AS seller),
              (SELECT to_json(bidder.*) AS "currentBidder" FROM (
                SELECT * FROM users WHERE id = lot.bidder
              ) AS bidder)
            FROM lots AS lot
          ) AS fullLots;
        """;
        try (ResultSet result = statement.executeQuery(query)) {
          result.next();
          return result.getString("result");
        } catch (SQLException exception) {
          throw new DataBaseException("Couldn't retrieve lots from database!");
        }
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't query from database!");
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Couldn't establish connection to database!");
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response;
    try {
      response = getLots();
      exchange.sendResponseHeaders(
          HttpURLConnection.HTTP_OK,
          response.getBytes(StandardCharsets.UTF_8).length);
    } catch (DataBaseException exception) {
      ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
      response = Main.gson.toJson(errorResponse, ErrorResponse.class);
      exchange.sendResponseHeaders(
          HttpURLConnection.HTTP_BAD_REQUEST,
          response.getBytes(StandardCharsets.UTF_8).length);
    }

    OutputStreamWriter writer = new OutputStreamWriter(
        exchange.getResponseBody(), StandardCharsets.UTF_8);
    writer.write(response);

    writer.flush();
    exchange.close();
  }
}
