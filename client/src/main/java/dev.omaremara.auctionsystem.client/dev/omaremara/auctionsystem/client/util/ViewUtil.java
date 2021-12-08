package dev.omaremara.auctionsystem.client.util;

import dev.omaremara.auctionsystem.client.Main;
import dev.omaremara.auctionsystem.client.view.View;
import javafx.stage.Stage;

public class ViewUtil {
  public static void setSceneRoot(View view) {
    Stage stage = Main.primaryStage;
    stage.getScene().setRoot(view.getRoot());
  }
}
