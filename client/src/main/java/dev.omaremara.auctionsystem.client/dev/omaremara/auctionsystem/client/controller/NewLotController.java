package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LotListView;
import javafx.scene.control.Label;

public class NewLotController {
  public void submit(String title, String description, Label errorLabel) {
    // TODO: Executed when the user create a new lot.
  }

  public void cancel() { ViewUtil.setSceneRoot(new LotListView()); }
}
