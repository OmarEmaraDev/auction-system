package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LotListView;
import dev.omaremara.auctionsystem.model.Lot;
import javafx.scene.control.Label;

public class BidController {
  public void submit(Lot lot, int bid, Label errorLabel) {
    // TODO: Executed when the user create a new bid.
    BidRequest bid =new BidRequest(lot,bid);
    String s= Main.gson.toJson(bid,BidRequest.class);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://localhost:8000/api/bid/"))
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
