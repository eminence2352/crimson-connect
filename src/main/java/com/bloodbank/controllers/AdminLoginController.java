package com.bloodbank.controllers;

import com.bloodbank.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class AdminLoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;


    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        setupEventHandlers();
        System.out.println("Admin login screen initialized.");
    }


    private void setupUI() {

        usernameField.setPromptText("Enter username");
        passwordField.setPromptText("Enter password");


        statusLabel.setText("");


        usernameField.requestFocus();


        loginButton.getStyleClass().add("primary-button");
        backButton.getStyleClass().add("secondary-button");
        statusLabel.getStyleClass().add("status-label");
    }


    private void setupEventHandlers() {

        loginButton.setOnAction(event -> handleLogin());


        backButton.setOnAction(event -> handleBack());


        passwordField.setOnKeyPressed(this::handleKeyPress);


        usernameField.setOnKeyPressed(this::handleKeyPress);
    }


    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }


    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();


        statusLabel.setText("");


        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }


        if (isValidCredentials(username, password)) {
            showSuccess("Login successful! Redirecting...");

            // Small delay for user to see success message
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(500); // 500ms delay
                    SceneManager.switchScene("admin-dashboard");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

        } else {
            showError("Invalid username or password.");
            passwordField.clear();
            usernameField.requestFocus();
        }
    }


    @FXML
    private void handleBack() {

            SceneManager.switchScene("welcome");
    }


    private boolean isValidCredentials(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");


        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }
}