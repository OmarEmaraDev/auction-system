package dev.omaremara.auctionsystem.model.requests;

public class RegisterRequest {
  public String name;
  public String password;

  public RegisterRequest(String name, String password) {
    this.name = name;
    this.password = password;
  }
}
