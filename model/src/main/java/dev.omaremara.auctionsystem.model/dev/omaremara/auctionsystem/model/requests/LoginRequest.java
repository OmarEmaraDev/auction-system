package dev.omaremara.auctionsystem.model.requests;

public class LoginRequest {
  public String name;
  public String password;

  public LoginRequest() {}

  public LoginRequest(String name, String password) {
    this.name = name;
    this.password = password;
  }
}
