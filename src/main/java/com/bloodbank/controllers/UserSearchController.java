package com.bloodbank.controllers;

import com.bloodbank.db.DBHelper;
import com.bloodbank.models.Donor;
import com.bloodbank.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class UserSearchController implements Initializable {

    @FXML
    private ComboBox<String> bloodGroupCombo;

    @FXML
    private ComboBox<String> locationCombo;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button backButton;

    @FXML
    private TextArea resultsArea;

    @FXML
    private Label statusLabel;

    @FXML
    private Label instructionLabel;


    private final String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private final String[] locations = {
            "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupUI();
        setupEventHandlers();

        System.out.println("User search screen initialized successfully.");
    }


    private void setupComboBoxes() {

        bloodGroupCombo.getItems().addAll(bloodGroups);
        bloodGroupCombo.setValue(bloodGroups[0]); // Default to A+


        locationCombo.getItems().addAll(locations);
        locationCombo.setValue(locations[0]); // Default to Dhaka


        bloodGroupCombo.setPromptText("Select Blood Group");
        locationCombo.setPromptText("Select Location");
    }

    private void setupUI() {

        instructionLabel.setText("Select blood group and location to find donors");


        resultsArea.setEditable(false);
        resultsArea.setWrapText(true);
        resultsArea.setPromptText("Search results will appear here...");



        statusLabel.setText("");

        searchButton.getStyleClass().add("primary-button");
        clearButton.getStyleClass().add("secondary-button");
        backButton.getStyleClass().add("secondary-button");
        instructionLabel.getStyleClass().add("instruction-label");
    }


    private void setupEventHandlers() {
        searchButton.setOnAction(event -> handleSearch());
        clearButton.setOnAction(event -> handleClear());
        backButton.setOnAction(event -> handleBack());

        bloodGroupCombo.setOnAction(event -> {
            if (event.getSource() == bloodGroupCombo) {
                locationCombo.requestFocus();
            }
        });

        locationCombo.setOnAction(event -> {
            if (event.getSource() == locationCombo) {
                handleSearch();
            }
        });
    }
    @FXML
    private void handleSearch() {
        String selectedBloodGroup = bloodGroupCombo.getValue();
        String selectedLocation = locationCombo.getValue();

        if (selectedBloodGroup == null || selectedLocation == null) {
            showError("Please select both blood group and location.");
            return;
        }

        try {
            statusLabel.setText("Searching...");
            statusLabel.setStyle("-fx-text-fill: blue;");


            List<Donor> donors = DBHelper.getDonorsByBloodGroupAndLocation(selectedBloodGroup, selectedLocation);


            displaySearchResults(donors, selectedBloodGroup, selectedLocation);

        } catch (Exception e) {
            showError("Error performing search: " + e.getMessage());
        }
    }

    private void displaySearchResults(List<Donor> donors, String bloodGroup, String location) {
        StringBuilder results = new StringBuilder();


        results.append("~~RESULTS~~\n\n");
        results.append("Search Criteria:\n");
        results.append("Blood Group: ").append(bloodGroup).append("\n");
        results.append("Location: ").append(location).append("\n");
        results.append("Search Date: ").append(java.time.LocalDate.now()).append("\n\n");

        if (donors == null || donors.isEmpty()) {
            results.append(" NO DONORS FOUND\n\n");
            statusLabel.setText("No donors found for " + bloodGroup + " in " + location);
            statusLabel.setStyle("-fx-text-fill: red;");

        } else {

            results.append("FOUND ").append(donors.size()).append(" DONOR(S)\n\n");

            int count = 1;
            for (Donor donor : donors) {
                results.append("--- Donor #").append(count).append(" ---\n");
                results.append("Name: ").append(donor.getName()).append("\n");
                results.append("Blood Group: ").append(donor.getBloodGroup()).append("\n");
                results.append("Location: ").append(donor.getLocation()).append("\n");
                results.append("Phone: ").append(donor.getPhone()).append("\n");
                results.append("\n");
                count++;
            }

            statusLabel.setText("Found " + donors.size() + " donor(s) successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        }
        resultsArea.setText(results.toString());
        resultsArea.setScrollTop(0);
    }


    @FXML
    private void handleClear() {

        bloodGroupCombo.setValue(bloodGroups[0]);
        locationCombo.setValue(locations[0]);


        resultsArea.setText("Search cleared. Ready for new search.\n\n" +
                "Select blood group and location, then click Search.");

        statusLabel.setText("");

        bloodGroupCombo.requestFocus();
    }


    @FXML
    private void handleBack() {

            SceneManager.switchScene("welcome");

    }
    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        statusLabel.setStyle("-fx-text-fill: red;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Search Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}