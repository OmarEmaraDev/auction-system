package dev.omaremara.auctionsystem.model;

import dev.omaremara.auctionsystem.model.User;

public record Lot(int id, String title, User seller, User currentBidder,
                  int currentBid) {}
