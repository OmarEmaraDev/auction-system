package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LotListView;
import dev.omaremara.auctionsystem.model.Lot;
import javafx.scene.control.Label;

public class BidController {
  public void submit(Lot lot, int bid, Label errorLabel) {
    // TODO: Executed when the user create a new bid.
  }

  public void cancel() { ViewUtil.setSceneRoot(new LotListView()); }
}
