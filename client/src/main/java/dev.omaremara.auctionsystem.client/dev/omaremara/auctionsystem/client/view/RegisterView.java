package dev.omaremara.auctionsystem.client.view;

import dev.omaremara.auctionsystem.client.controller.RegisterController;
import dev.omaremara.auctionsystem.client.view.View;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class RegisterView implements View {
  public Parent getRoot() {
    RegisterController controller = new RegisterController();

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(40, 40, 40, 40));
    grid.setHgap(10);
    grid.setVgap(10);
    ColumnConstraints labelsConstraints = new ColumnConstraints();
    labelsConstraints.setHalignment(HPos.RIGHT);
    ColumnConstraints fieldsConstraints = new ColumnConstraints();
    fieldsConstraints.setHgrow(Priority.ALWAYS);
    grid.getColumnConstraints().addAll(labelsConstraints, fieldsConstraints);

    Label headerLabel = new Label("Register");
    headerLabel.setStyle("-fx-font-size : 24px;");
    grid.add(headerLabel, 0, 0, 2, 1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);

    grid.add(new Label("Name:"), 0, 1);

    TextField nameField = new TextField();
    nameField.setPrefHeight(40);
    grid.add(nameField, 1, 1);

    grid.add(new Label("Password:"), 0, 2);

    PasswordField passwordField = new PasswordField();
    passwordField.setPrefHeight(40);
    grid.add(passwordField, 1, 2);

    Label errorLabel = new Label();
    errorLabel.setTextFill(Color.RED);
    GridPane.setHalignment(errorLabel, HPos.CENTER);
    grid.add(errorLabel, 0, 3, 2, 1);

    Button registerButton = new Button("Register");
    registerButton.setPrefHeight(40);
    registerButton.setPrefWidth(100);
    registerButton.setDefaultButton(true);
    grid.add(registerButton, 0, 4, 2, 1);
    GridPane.setHalignment(registerButton, HPos.CENTER);
    registerButton.setOnAction(e -> {
      controller.register(nameField.getText(), passwordField.getText(),
                          errorLabel);
    });

    Hyperlink loginLink = new Hyperlink("Login");
    grid.add(loginLink, 1, 5);
    loginLink.setOnAction(e -> controller.loginView());

    return grid;
  }
}
