package dev.omaremara.auctionsystem.client.view;

import dev.omaremara.auctionsystem.client.controller.NewLotController;
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

public class NewLotView implements View {
  public Parent getRoot() {
    NewLotController controller = new NewLotController();

    Button cancelButton = new Button("Cancel");
    cancelButton.setOnAction(e -> controller.cancel());
    cancelButton.setCancelButton(true);

    HBox titleFieldHbox = new HBox(10);
    titleFieldHbox.setAlignment(Pos.CENTER);
    Label titleLabel = new Label("Lot Title:");
    TextField titleField = new TextField();
    titleFieldHbox.getChildren().addAll(titleLabel, titleField);

    HBox errorHbox = new HBox();
    Label errorLabel = new Label();
    errorLabel.setTextFill(Color.RED);
    errorHbox.setAlignment(Pos.CENTER);
    errorHbox.getChildren().addAll(errorLabel);

    HBox initialBidFieldHbox = new HBox(10);
    initialBidFieldHbox.setAlignment(Pos.CENTER);
    Label initialBidLabel = new Label("Initial Bid:");
    TextField initialBidField = new TextField();
    initialBidFieldHbox.getChildren().addAll(initialBidLabel, initialBidField);

    HBox newButtonHbox = new HBox();
    newButtonHbox.setAlignment(Pos.CENTER);
    Button newButton = new Button("New");
    newButton.setDefaultButton(true);
    newButton.setOnAction(e -> {
      errorLabel.setText(null);
      try {
        controller.submit(titleField.getText(),
                          Integer.valueOf(initialBidField.getText()),
                          errorLabel);
      } catch (NumberFormatException exception) {
        errorLabel.setText("Please enter integer value!");
      }
    });
    newButtonHbox.getChildren().addAll(newButton);

    VBox layout = new VBox(10);
    layout.setPadding(new Insets(10, 10, 10, 10));
    layout.getChildren().addAll(cancelButton, titleFieldHbox,
                                initialBidFieldHbox, newButtonHbox, errorHbox);
    return layout;
  }
}
