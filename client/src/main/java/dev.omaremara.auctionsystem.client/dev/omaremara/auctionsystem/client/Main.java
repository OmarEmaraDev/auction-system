package dev.omaremara.auctionsystem.client;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LoginView;
import dev.omaremara.auctionsystem.model.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {
  public static Stage primaryStage;
  public static User user;

  @Override
  public void start(Stage stage) {
    Main.primaryStage = stage;
    stage.setScene(new Scene(new Region()));
    ViewUtil.setSceneRoot(new LoginView());
    stage.setMaximized(true);
    stage.show();
  }

  public static void main(String[] args) { launch(); }
}
