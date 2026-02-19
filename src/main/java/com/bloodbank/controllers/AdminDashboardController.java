package com.bloodbank.controllers;

import com.bloodbank.db.DBHelper;
import com.bloodbank.models.Donor;
import com.bloodbank.utils.SceneManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {


    @FXML
    private TableView<Donor> donorTable;

    @FXML
    private TableColumn<Donor, String> nameColumn;

    @FXML
    private TableColumn<Donor, String> bloodGroupColumn;

    @FXML
    private TableColumn<Donor, String> locationColumn;

    @FXML
    private TableColumn<Donor, String> phoneColumn;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> bloodGroupCombo;

    @FXML
    private ComboBox<String> locationCombo;

    @FXML
    private TextField phoneField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label statusLabel;


    private ObservableList<Donor> donorList;

    private final String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private final String[] locations = {
            "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Barisal", "Sylhet", "Rangpur", "Mymensingh"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupComboBoxes();
        setupEventHandlers();
        loadDonorData();
        setupUI();

        System.out.println("Admin dashboard initialized successfully.");
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        nameColumn.setPrefWidth(150);
        bloodGroupColumn.setPrefWidth(100);
        locationColumn.setPrefWidth(120);
        phoneColumn.setPrefWidth(120);

        donorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    private void setupComboBoxes() {

        bloodGroupCombo.getItems().addAll(bloodGroups);
        bloodGroupCombo.setValue(bloodGroups[0]); // Default selection


        locationCombo.getItems().addAll(locations);
        locationCombo.setValue(locations[0]); // Default selection
    }


    private void setupEventHandlers() {
        addButton.setOnAction(event -> handleAddDonor());
        deleteButton.setOnAction(event -> handleDeleteDonor());
        refreshButton.setOnAction(event -> handleRefresh());
        logoutButton.setOnAction(event -> handleLogout());


        donorTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleViewDonor();
            }
        });
    }

    private void setupUI() {

        nameField.setPromptText("Enter donor name");
        phoneField.setPromptText("Enter phone number");


        addButton.getStyleClass().add("primary-button");
        deleteButton.getStyleClass().add("danger-button");
        refreshButton.getStyleClass().add("secondary-button");
        logoutButton.getStyleClass().add("secondary-button");


        statusLabel.setText("");
    }


    private void loadDonorData() {

            donorList = DBHelper.getAllDonors();
            donorTable.setItems(donorList);

            updateStatusLabel("Loaded " + donorList.size() + " donors.");
    }

    @FXML
    private void handleAddDonor() {
        String name = nameField.getText().trim();
        String bloodGroup = bloodGroupCombo.getValue();
        String location = locationCombo.getValue();
        String phone = phoneField.getText().trim();


        if (name.isEmpty()) {
            showError("Please enter donor name.");
            nameField.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            showError("Please enter phone number.");
            phoneField.requestFocus();
            return;
        }


        if (!isValidPhone(phone)) {
            showError("Please enter a valid phone number.");
            phoneField.requestFocus();
            return;
        }


            boolean success = DBHelper.insertDonor(name, bloodGroup, location, phone);

            if (success) {
                showSuccess("Donor added successfully!");
                clearInputFields();
                loadDonorData();
            } else {
                showError("Failed to add donor. Please try again.");
            }


    }


    @FXML
    private void handleDeleteDonor() {
        Donor selectedDonor = donorTable.getSelectionModel().getSelectedItem();

        if (selectedDonor == null) {
            showError("Please select a donor to delete.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Donor");
        confirmDialog.setContentText("Are you sure you want to delete " + selectedDonor.getName() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

                boolean success = DBHelper.deleteDonorById(selectedDonor.getId());

                if (success) {
                    showSuccess("Donor deleted successfully!");
                    loadDonorData();
                } else {
                    showError("Failed to delete donor.");
                }


        }
    }

    @FXML
    private void handleRefresh() {
        loadDonorData();
    }


    @FXML
    private void handleLogout() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Logout");
        confirmDialog.setHeaderText("Logout");
        confirmDialog.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            SceneManager.switchScene("welcome");

        }
    }


    private void handleViewDonor() {
        Donor selectedDonor = donorTable.getSelectionModel().getSelectedItem();

        if (selectedDonor != null) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Donor Details");
            infoDialog.setHeaderText("Donor Information");
            infoDialog.setContentText(
                            "Name: " + selectedDonor.getName() + "\n" +
                            "Blood Group: " + selectedDonor.getBloodGroup() + "\n" +
                            "Location: " + selectedDonor.getLocation() + "\n" +
                            "Phone: " + selectedDonor.getPhone()
            );
            infoDialog.showAndWait();
        }
    }

    private void clearInputFields() {
        nameField.clear();
        phoneField.clear();
        bloodGroupCombo.setValue(bloodGroups[0]);
        locationCombo.setValue(locations[0]);
        nameField.requestFocus();
    }

    private boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)\\+]", "");
        return cleanPhone.matches("\\d{10,15}"); // 10-15 digits
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: black;");
    }
}