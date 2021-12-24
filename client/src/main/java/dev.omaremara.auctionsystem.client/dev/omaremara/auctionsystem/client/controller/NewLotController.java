package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LotListView;
import javafx.scene.control.Label;

public class NewLotController {
  public void submit(String title, int initialBid, Label errorLabel) {
    // TODO: Executed when the user create a new lot.
    LotRequest lot =new LotRequest(title,initialBid);
    String s= Main.gson.toJson(bid,BidRequest.class);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://localhost:8000/api/lot/"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(s))
        .build();
        
        try{
          HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        }catch(Exception e){
           throw new RuntimeException(e) ;       }
  }

  public void cancel() { ViewUtil.setSceneRoot(new LotListView()); }
}
