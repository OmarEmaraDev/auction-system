package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LoginView;
import javafx.scene.control.Label;

public class RegisterController {
  public static void register(String name, String password, Label errorLabel) {
    // TODO: Executed when the user registers.
  }

  public void loginView() { ViewUtil.setSceneRoot(new LoginView()); }
}
