package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.RegisterView;
import javafx.scene.control.Label;

public class LoginController {
  public void login(String email, String password, Label errorLabel) {
    // TODO: Executed when the user logins.
  }

  public void registerView() { ViewUtil.setSceneRoot(new RegisterView()); }
}
