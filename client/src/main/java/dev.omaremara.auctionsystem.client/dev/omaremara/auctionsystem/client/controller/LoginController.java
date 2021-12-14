package dev.omaremara.auctionsystem.client.controller;

import java.io.IOException;
import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.RegisterView;
import javafx.scene.control.Label;
import dev.omaremara.auctionsystem.client.Main;
import dev.omaremara.auctionsystem.model.requests.LoginRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;

public class LoginController {
  public void login(String email, String password, Label errorLabel) {
    // TODO: Executed when the user logins.
    LoginRequest login =new LoginRequest(email,password);
    String s= Main.gson.toJson(login,LoginRequest.class);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://localhost:8000/api/login/"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(s))
        .build();
        
        try{
          HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        }catch(Exception e){
           throw new RuntimeException(e) ;       }
  }

  public void registerView() { ViewUtil.setSceneRoot(new RegisterView()); }
}
