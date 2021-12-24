package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.client.util.ViewUtil;
import dev.omaremara.auctionsystem.client.view.LoginView;
import javafx.scene.control.Label;

public class RegisterController {
  public static void register(String name, String password, Label errorLabel) {
    // TODO: Executed when the user registers.

    RegisterRequest register =new RegisterRequest(email,password);
    String s= Main.gson.toJson(register,RegisterRequest.class);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://localhost:8000/api/register/"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(s))
        .build();
        
        try{
          HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        }catch(Exception e){
           throw new RuntimeException(e) ;       }
  }

  public void loginView() { ViewUtil.setSceneRoot(new LoginView()); }
}
