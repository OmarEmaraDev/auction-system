package dev.omaremara.auctionsystem.client.view;

import dev.omaremara.auctionsystem.client.controller.BidController;
import dev.omaremara.auctionsystem.client.view.View;
import dev.omaremara.auctionsystem.model.Lot;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

public class BidView implements View {
  private Lot lot;

  public BidView(Lot lot) { this.lot = lot; }

  public Parent getRoot() {
    // TODO: Title and current bid. Submit button.
    return new Region();
  }
}
