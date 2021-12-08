package dev.omaremara.auctionsystem.model.requests;

public class NewBidRequest {
  public int lotID;
  public int bidderID;
  public int bid;

  public NewBidRequest(int lotID, int bidderID, int bid) {
    this.lotID = lotID;
    this.bidderID = bidderID;
    this.bid = bid;
  }
}
