package dev.omaremara.auctionsystem.model;

public class User {
  public int id;
  public String name;
  public String password;

  public User() {}

  public User(int id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }
}

// CREATE TABLE users (
//     id       INTEGER PRIMARY KEY,
//     name     VARCHAR(128) NOT NULL,
//     password VARCHAR(256) NOT NULL
// );
