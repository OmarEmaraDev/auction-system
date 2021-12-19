package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.requests.RegisterRequest;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.DataBaseException;
import dev.omaremara.auctionsystem.server.exceptions.RegisterException;
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

public class RegisterContext implements HttpHandler {
  private void validateNewUser(RegisterRequest registerRequest)
      throws RegisterException, DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "SELECT * FROM users WHERE name = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, registerRequest.name);
        try (ResultSet result = statement.executeQuery()) {
          if (result.isBeforeFirst()) {
            throw new RegisterException(
                "A user with the same name already exists!");
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

  private int getUsersCount() throws DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      try (Statement statement = connection.createStatement()) {
        String query = "SELECT COUNT (*) AS count FROM users";
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

  private void insertUser(RegisterRequest registerRequest)
      throws DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "INSERT INTO users VALUES(?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, getUsersCount());
        statement.setString(2, registerRequest.name);
        statement.setString(3, registerRequest.password);
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
    RegisterRequest registerRequest =
        Main.gson.fromJson(requestReader, RegisterRequest.class);

    try {
      validateNewUser(registerRequest);
      insertUser(registerRequest);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
    } catch (RegisterException | DataBaseException exception) {
      ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
      String response = Main.gson.toJson(errorResponse, ErrorResponse.class);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,
                                   response.getBytes().length);
      OutputStreamWriter writer = new OutputStreamWriter(
          exchange.getResponseBody(), StandardCharsets.UTF_8);
      writer.write(response);
      writer.flush();
    }

    exchange.close();
  }
}
