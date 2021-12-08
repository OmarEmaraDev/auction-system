package dev.omaremara.auctionsystem.client.controller;

import dev.omaremara.auctionsystem.model.Lot;
import dev.omaremara.auctionsystem.model.User;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

public class LotListController {
  public static List<Lot> getAllLots(Label errorLabel) {
    // TODO: Get a list of all lots.

    // Seeded data for testing.
    return List.of(new Lot(0, "Car", new User(0, "Alice", ""), null, 10),
                   new Lot(1, "Cheese", new User(1, "Bob", ""),
                           new User(2, "Bidder", ""), 30),
                   new Lot(2, "Ball", new User(3, "Mark", ""), null, 30));
  }
}
