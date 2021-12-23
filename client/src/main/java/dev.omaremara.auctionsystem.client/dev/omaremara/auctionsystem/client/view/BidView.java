package dev.omaremara.auctionsystem.client.view;

import dev.omaremara.auctionsystem.client.controller.BidController;
import dev.omaremara.auctionsystem.client.view.View;
import dev.omaremara.auctionsystem.model.Lot;
import java.lang.NumberFormatException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BidView implements View {
  private Lot lot;

  public BidView(Lot lot) { this.lot = lot; }

  public Parent getRoot() {
    BidController controller = new BidController();

    Button cancelButton = new Button("Cancel");
    cancelButton.setOnAction(e -> controller.cancel());
    cancelButton.setCancelButton(true);

    HBox titleHbox = new HBox();
    titleHbox.setAlignment(Pos.CENTER);
    Text title = new Text(lot.title);
    title.setStyle("-fx-font-size : 40px;");
    titleHbox.getChildren().addAll(title);

    HBox sellerHbox = new HBox();
    Label sellerLabel = new Label("Seller: " + lot.seller.name);
    sellerLabel.setStyle("-fx-font-size : 20px;");
    sellerHbox.getChildren().addAll(sellerLabel);

    HBox bidderHbox = new HBox();
    bidderHbox.setAlignment(Pos.BASELINE_RIGHT);
    Label bidderLabel = new Label();
    bidderLabel.setStyle("-fx-font-size : 15px;");
    if (lot.currentBidder != null) {
      bidderLabel.setText("Current Bidder: " + lot.currentBidder.name);
    } else {
      bidderLabel.setText("No Bidder on this item yet.");
    }
    bidderHbox.getChildren().addAll(bidderLabel);

    HBox bidHbox = new HBox();
    bidHbox.setAlignment(Pos.CENTER);
    Label bidLabel = new Label("Current bid: " + lot.currentBid);
    bidLabel.setStyle("-fx-font-size : 15px;");
    bidHbox.getChildren().addAll(bidLabel);

    HBox errorHbox = new HBox();
    Label errorLabel = new Label();
    errorLabel.setTextFill(Color.RED);
    errorHbox.setAlignment(Pos.CENTER);
    errorHbox.getChildren().addAll(errorLabel);

    HBox bidFieldHbox = new HBox();
    bidFieldHbox.setAlignment(Pos.CENTER);
    TextField bidField = new TextField();
    bidFieldHbox.getChildren().addAll(bidField);

    HBox bidButtonHbox = new HBox();
    bidButtonHbox.setAlignment(Pos.CENTER);
    Button bidButton = new Button("Bid now!");
    bidButton.setDefaultButton(true);
    bidButton.setOnAction(e -> {
      errorLabel.setText(null);
      try {
        controller.submit(lot, Integer.valueOf(bidField.getText()), errorLabel);
      } catch (NumberFormatException exception) {
        errorLabel.setText("Please enter integer value!");
      }
    });
    bidButtonHbox.getChildren().addAll(bidButton);

    VBox layout = new VBox(10);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.getChildren().addAll(cancelButton, titleHbox, sellerHbox, bidderHbox,
                                bidHbox, bidFieldHbox, bidButtonHbox,
                                errorHbox);
    return layout;
  }
}
