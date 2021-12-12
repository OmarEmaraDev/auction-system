package dev.omaremara.auctionsystem.model;

import dev.omaremara.auctionsystem.model.User;

public class Lot {
  public int id;
  public String title;
  public User seller;
  public User currentBidder;
  public int currentBid;

  public Lot() {}

  public Lot(int id, String title, User seller, User currentBidder,
             int currentBid) {
    this.id = id;
    this.title = title;
    this.seller = seller;
    this.currentBidder = currentBidder;
    this.currentBid = currentBid;
  }
}
