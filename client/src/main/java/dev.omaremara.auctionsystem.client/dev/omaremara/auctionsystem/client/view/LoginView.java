package dev.omaremara.auctionsystem.client.view;

import dev.omaremara.auctionsystem.client.controller.LoginController;
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

public class LoginView implements View {
  public Parent getRoot() {
    LoginController controller = new LoginController();

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

    Label headerLabel = new Label("Login");
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

    Button loginButton = new Button("Login");
    loginButton.setPrefHeight(40);
    loginButton.setPrefWidth(100);
    loginButton.setDefaultButton(true);
    grid.add(loginButton, 0, 4, 2, 1);
    GridPane.setHalignment(loginButton, HPos.CENTER);
    loginButton.setOnAction(e -> {
      controller.login(nameField.getText(), passwordField.getText(),
                       errorLabel);
    });

    Hyperlink registerLink = new Hyperlink("Register");
    grid.add(registerLink, 1, 5);
    registerLink.setOnAction(e -> controller.registerView());

    return grid;
  }
}
