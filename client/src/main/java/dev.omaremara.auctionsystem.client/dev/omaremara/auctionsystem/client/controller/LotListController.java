package dev.omaremara.auctionsystem.client.controller;

import com.google.gson.reflect.TypeToken;
import dev.omaremara.auctionsystem.client.Main;
import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.BidView;
import dev.omaremara.auctionsystem.client.view.LoginView;
import dev.omaremara.auctionsystem.client.view.NewLotView;
import dev.omaremara.auctionsystem.model.Lot;
import dev.omaremara.auctionsystem.model.User;
import dev.omaremara.auctionsystem.model.responses.ErrorResponse;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

public class LotListController {
  public static List<Lot> getAllLots(Label errorLabel) {
    HttpClient client = HttpClient.newHttpClient();
    String uri = System.getProperty("auctionSystem.server.URI");
    try {
      HttpRequest request =
          HttpRequest.newBuilder().uri(new URI(uri + "/api/lots/")).build();
      HttpResponse<String> response =
          client.send(request, BodyHandlers.ofString());
      if (response.statusCode() != HttpURLConnection.HTTP_OK) {
        ErrorResponse errorResponse =
            Main.gson.fromJson(response.body(), ErrorResponse.class);
        errorLabel.setText(errorResponse.error);
        return new ArrayList<Lot>();
      } else {
        Type listType = new TypeToken<ArrayList<Lot>>() {}.getType();
        return Main.gson.fromJson(response.body(), listType);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void newLot() { ViewUtil.setSceneRoot(new NewLotView()); }

  public static void bid(Lot lot) { ViewUtil.setSceneRoot(new BidView(lot)); }

  public static void logOut() { ViewUtil.setSceneRoot(new LoginView()); }
}
