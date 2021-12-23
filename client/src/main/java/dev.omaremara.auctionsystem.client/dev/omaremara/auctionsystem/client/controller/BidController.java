package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.Main;
import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LotListView;
import dev.omaremara.auctionsystem.model.Lot;
import dev.omaremara.auctionsystem.model.requests.NewBidRequest;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import javafx.scene.control.Label;

public class BidController {
  public void submit(Lot lot, int bid, Label errorLabel) {
    NewBidRequest newBidRequest = new NewBidRequest(lot.id, Main.user.id, bid);
    String requestBody = Main.gson.toJson(newBidRequest, NewBidRequest.class);
    HttpClient client = HttpClient.newHttpClient();
    String uri = System.getProperty("auctionSystem.server.URI");
    try {
      HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI(uri + "/api/new/bid/"))
                                .header("Content-Type", "application/json")
                                .POST(BodyPublishers.ofString(requestBody))
                                .build();
      HttpResponse<String> response =
          client.send(request, BodyHandlers.ofString());
      if (response.statusCode() != HttpURLConnection.HTTP_OK) {
        ErrorResponse errorResponse =
            Main.gson.fromJson(response.body(), ErrorResponse.class);
        errorLabel.setText(errorResponse.error);
      } else {
        ViewUtil.setSceneRoot(new LotListView());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void cancel() { ViewUtil.setSceneRoot(new LotListView()); }
}
