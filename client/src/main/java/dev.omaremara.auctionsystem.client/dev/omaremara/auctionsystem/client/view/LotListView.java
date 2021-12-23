package dev.omaremara.auctionsystem.client.view;

import dev.omaremara.auctionsystem.client.controller.LotListController;
import dev.omaremara.auctionsystem.client.view.View;
import dev.omaremara.auctionsystem.model.Lot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LotListView implements View {
  public Parent getRoot() {
    Label errorLabel = new Label();
    errorLabel.setTextFill(Color.RED);

    Label headerLabel = new Label("Current Lots:");
    headerLabel.setStyle("-fx-font-size : 40px;");

    ObservableList<Lot> items = FXCollections.observableArrayList(
        LotListController.getAllLots(errorLabel));
    ListView<Lot> list = new ListView<Lot>();
    list.setItems(items);
    list.setCellFactory(targetList -> new ListCell<Lot>() {
      public void updateItem(Lot lot, boolean empty) {
        super.updateItem(lot, empty);
        if (lot == null || empty) {
          return;
        }

        BorderPane layout = new BorderPane();
        Label titleLabel = new Label(lot.title);
        titleLabel.setStyle("-fx-font-size : 20px;");
        Button bidButton = new Button("Bid");
        bidButton.setOnAction(e -> LotListController.bid(lot));
        layout.setLeft(titleLabel);
        layout.setRight(bidButton);
        setGraphic(layout);
      }
    });

    Button newLotButton = new Button("New Lot");
    newLotButton.setOnAction(e -> LotListController.newLot());

    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20, 80, 0, 80));
    layout.getChildren().addAll(headerLabel, list, errorLabel, newLotButton);
    return layout;
  }
}
