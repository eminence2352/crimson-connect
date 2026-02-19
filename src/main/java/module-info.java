/**
 * Module definition for Blood Bank Management System
 *
 * This file defines:
 * - Which modules this application depends on
 * - Which packages are exported to other modules
 * - Which packages are opened for reflection (required for FXML)
 *
 * Location: src/main/java/module-info.java
 */
module com.bloodbank {

    // ========== REQUIRED MODULES ==========

    // JavaFX Core Modules
    requires javafx.controls;      // UI controls (Button, TextField, TableView, etc.)
    requires javafx.fxml;          // FXML loading and controller binding
    requires javafx.graphics;      // Graphics, Scene, Stage (automatically included with controls)

    requires org.json;

    // Java Standard Modules
    requires java.sql;             // JDBC database operations
    requires java.desktop;         // Desktop integration (optional, for system tray, etc.)

    // SQLite JDBC Driver
    requires java.naming;          // Required by some JDBC drivers

    // ========== EXPORTED PACKAGES ==========
    // These packages can be accessed by other modules

    exports com.bloodbank;                    // Main application package
    exports com.bloodbank.models;            // Data models (Donor class)
    exports com.bloodbank.utils;             // Utility classes (SceneManager)

    // Note: Usually don't export controllers, db, as they're internal implementation

    // ========== OPENED PACKAGES ==========
    // These packages are opened for reflection access (required for FXML)

    opens com.bloodbank to javafx.fxml;                        // Main package
    opens com.bloodbank.controllers to javafx.fxml;            // Controllers for FXML binding
    opens com.bloodbank.models to javafx.base;                 // Models for property binding

    // Optional: Open models to additional modules if needed for serialization
//    opens com.bloodbank.models to java.base;                   // For reflection access
}