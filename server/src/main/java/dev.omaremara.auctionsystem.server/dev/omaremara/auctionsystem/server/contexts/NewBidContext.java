package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.requests.NewBidRequest;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.DataBaseException;
import dev.omaremara.auctionsystem.server.exceptions.NewBidException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewBidContext implements HttpHandler {
  private void validateNewBid(NewBidRequest newBidRequest)
      throws NewBidException, DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "SELECT bid FROM lots WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, newBidRequest.lotID);
        try (ResultSet result = statement.executeQuery()) {
          if (!result.isBeforeFirst()) {
            throw new NewBidException("No lot exists with this lotID!");
          } else {
            result.next();
            if (newBidRequest.bid <= result.getInt("bid")) {
              throw new NewBidException(
                  "The new bid must be greater than the last bid!");
            }
          }
        } catch (SQLException exception) {
          throw new DataBaseException("Couldn't retrieve from database!");
        }
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't select from database!");
      }

      query = "SELECT * FROM users WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, newBidRequest.bidderID);
        try (ResultSet result = statement.executeQuery()) {
          if (!result.isBeforeFirst()) {
            throw new NewBidException("No user exists with this bidderID!");
          }
        } catch (SQLException exception) {
          throw new DataBaseException("Couldn't retrieve from database!");
        }
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't select from database!");
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Couldn't establish connection to database!");
    }
  }

  private void updateBide(NewBidRequest newBidRequest)
      throws DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "UPDATE lots SET bidder = ?, bid = ?  WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, newBidRequest.bidderID);
        statement.setInt(2, newBidRequest.bid);
        statement.setInt(3, newBidRequest.lotID);
        statement.execute();
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't Update the database!");
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Couldn't establish connection to database!");
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    InputStreamReader requestReader = new InputStreamReader(
        exchange.getRequestBody(), StandardCharsets.UTF_8);
    NewBidRequest newBidRequest =
        Main.gson.fromJson(requestReader, NewBidRequest.class);

    try {
      validateNewBid(newBidRequest);
      updateBide(newBidRequest);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
    } catch (NewBidException | DataBaseException exception) {
      ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
      String response = Main.gson.toJson(errorResponse, ErrorResponse.class);
      exchange.sendResponseHeaders(
          HttpURLConnection.HTTP_BAD_REQUEST,
          response.getBytes(StandardCharsets.UTF_8).length);
      OutputStreamWriter writer = new OutputStreamWriter(
          exchange.getResponseBody(), StandardCharsets.UTF_8);
      writer.write(response);
      writer.flush();
    }

    exchange.close();
  }
}
