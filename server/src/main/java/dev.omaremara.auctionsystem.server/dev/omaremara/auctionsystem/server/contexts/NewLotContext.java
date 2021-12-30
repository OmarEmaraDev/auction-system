package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.requests.NewLotRequest;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.DataBaseException;
import dev.omaremara.auctionsystem.server.exceptions.NewLotException;
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
import java.sql.Statement;

public class NewLotContext implements HttpHandler {
  private void validateNewLot(NewLotRequest newLotRequest)
      throws NewLotException, DataBaseException {
    if (newLotRequest.initialBid <= 0) {
      throw new NewLotException("Initial bid must be greater than zero!");
    }
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "SELECT * FROM users WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, newLotRequest.sellerID);
        try (ResultSet result = statement.executeQuery()) {
          if (!result.isBeforeFirst()) {
            throw new NewLotException("No user exists with this sellerID!");
          }
        } catch (SQLException exception) {
          throw new DataBaseException("Couldn't retrieve from database!");
        }
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't query from database!");
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Couldn't establish connection to database!");
    }
  }

  private int getLotsCount() throws DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      try (Statement statement = connection.createStatement()) {
        String query = "SELECT COUNT (*) AS count FROM lots";
        try (ResultSet result = statement.executeQuery(query)) {
          result.next();
          return result.getInt("count");
        } catch (SQLException exception) {
          throw new DataBaseException("Couldn't retrieve from database!");
        }
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't query from database!");
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Couldn't establish connection to database!");
    }
  }

  private void insertLot(NewLotRequest newLotRequest) throws DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "INSERT INTO lots VALUES(?, ?, ?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, getLotsCount());
        statement.setString(2, newLotRequest.title);
        statement.setInt(3, newLotRequest.sellerID);
        statement.setNull(4, java.sql.Types.INTEGER);
        statement.setInt(5, newLotRequest.initialBid);
        statement.execute();
      } catch (SQLException exception) {
        throw new DataBaseException("Couldn't insert into database!");
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Couldn't establish connection to database!");
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    InputStreamReader requestReader = new InputStreamReader(
        exchange.getRequestBody(), StandardCharsets.UTF_8);
    NewLotRequest newLotRequest =
        Main.gson.fromJson(requestReader, NewLotRequest.class);

    try {
      validateNewLot(newLotRequest);
      insertLot(newLotRequest);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
    } catch (NewLotException | DataBaseException exception) {
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
