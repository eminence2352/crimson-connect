package com.bloodbank.db;

import com.bloodbank.models.Donor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {


    static {
        createDonorsTable();
    }

    public static void createDonorsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS donors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                blood_group TEXT NOT NULL,
                location TEXT NOT NULL,
                phone TEXT NOT NULL
            )""";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DBConnector.getConnection();

            stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Donors table created/verified successfully.");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean insertDonor(String name, String bloodGroup, String location, String phone) {
        String sql = "INSERT INTO donors (name, blood_group, location, phone) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnector.getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name.trim());
            pstmt.setString(2, bloodGroup);
            pstmt.setString(3, location);
            pstmt.setString(4, phone.trim());

            int rowsAffected = pstmt.executeUpdate();
            boolean success = rowsAffected > 0;
            return success;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static ObservableList<Donor> getAllDonors() {
        ObservableList<Donor> donors = FXCollections.observableArrayList();
        String sql = "SELECT * FROM donors ORDER BY name";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Donor donor = new Donor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("blood_group"),
                        rs.getString("location"),
                        rs.getString("phone")
                );
                donors.add(donor);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());


        }

        return donors;
    }

    public static boolean deleteDonorById(int id) {
        String sql = "DELETE FROM donors WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnector.getConnection();
            if (conn == null) {
                System.err.println("Could not establish database connection for delete");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;

            if (success) {
                System.out.println("Donor deleted successfully: ID " + id);
            }

            return success;

        } catch (SQLException e) {
            System.err.println("Error deleting donor: " + e.getMessage());
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing prepared statement: " + e.getMessage());
                }
            }
        }
    }

    public static List<Donor> getDonorsByBloodGroupAndLocation(String bloodGroup, String location) {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM donors WHERE blood_group = ? AND location = ? ORDER BY name";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bloodGroup);
            pstmt.setString(2, location);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Donor donor = new Donor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("blood_group"),
                        rs.getString("location"),
                        rs.getString("phone")
                );
                donors.add(donor);
            }

            System.out.println("Found " + donors.size() + " donors for " + bloodGroup + " in " + location);

        } catch (SQLException e) {
            System.err.println("Error searching donors: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing result set: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing prepared statement: " + e.getMessage());
                }
            }
        }

        return donors;
    }

}