package com.bloodbank.controllers;

import com.bloodbank.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class WelcomeController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("Welcome screen initialized successfully.");
    }

    @FXML
    private void handleAdminLogin() {
            SceneManager.switchScene("admin-login");
    }

    @FXML
    private void handleUserSearch() {

            SceneManager.switchScene("user-search");
    }
}