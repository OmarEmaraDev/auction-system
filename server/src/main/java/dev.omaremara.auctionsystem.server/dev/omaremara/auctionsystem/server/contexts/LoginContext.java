package dev.omaremara.auctionsystem.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.omaremara.auctionsystem.model.User;
import dev.omaremara.auctionsystem.model.requests.LoginRequest;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import dev.omaremara.auctionsystem.server.Main;
import dev.omaremara.auctionsystem.server.exceptions.DataBaseException;
import dev.omaremara.auctionsystem.server.exceptions.LoginException;
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

public class LoginContext implements HttpHandler {
  private User getUser(LoginRequest loginRequest)
      throws LoginException, DataBaseException {
    String connectionURL =
        System.getProperty("auctionSystem.JDBC.connection.url");
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String query = "SELECT * FROM users WHERE name = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, loginRequest.name);
        try (ResultSet result = statement.executeQuery()) {
          if (!result.isBeforeFirst()) {
            throw new LoginException("Wrong name or password!");
          } else {
            result.next();
            String password = result.getString("password");
            if (password.equals(loginRequest.password)) {
              return new User(result.getInt("id"), result.getString("name"),
                              result.getString("password"));
            } else {
              throw new LoginException("Wrong name or password!");
            }
          }
        } catch (SQLException exception) {
          throw new DataBaseException("Couldn't retrieve user from database!");
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
    InputStreamReader requestReader = new InputStreamReader(
        exchange.getRequestBody(), StandardCharsets.UTF_8);
    LoginRequest loginRequest =
        Main.gson.fromJson(requestReader, LoginRequest.class);

    String response;
    try {
      User user = getUser(loginRequest);
      response = Main.gson.toJson(user, User.class);
      exchange.sendResponseHeaders(
          HttpURLConnection.HTTP_OK,
          response.getBytes(StandardCharsets.UTF_8).length);
    } catch (LoginException | DataBaseException exception) {
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
