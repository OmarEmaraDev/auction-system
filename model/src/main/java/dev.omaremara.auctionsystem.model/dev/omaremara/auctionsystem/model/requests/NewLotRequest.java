package dev.omaremara.auctionsystem.model.requests;

public class NewLotRequest {
  public String title;
  public int sellerID;
  public int initialBid;

  public NewLotRequest() {}

  public NewLotRequest(String title, int sellerID, int initialBid) {
    this.title = title;
    this.sellerID = sellerID;
    this.initialBid = initialBid;
  }
}
