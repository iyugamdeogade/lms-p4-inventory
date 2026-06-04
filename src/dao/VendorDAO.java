package dao;

import entity.Vendor;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendorDAO {

    public void addVendor(Vendor vendor) {
        String query = "INSERT INTO vendor(vendor_name, phone, address) VALUES (?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, vendor.getVendorName());
            pst.setString(2, vendor.getPhone());
            pst.setString(3, vendor.getAddress());
            pst.executeUpdate();

            System.out.println("Vendor Added Successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding vendor: " + e.getMessage());
        }
    }

    public void viewVendors() {
        String query = "SELECT * FROM vendor";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nID | Vendor Name | Phone | Address");
            System.out.println("----------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-4d | %-20s | %-15s | %s%n",
                    rs.getInt("vendor_id"),
                    rs.getString("vendor_name"),
                    rs.getString("phone"),
                    rs.getString("address"));
            }
            if (!found) System.out.println("No vendors found.");

        } catch (SQLException e) {
            System.out.println("Error viewing vendors: " + e.getMessage());
        }
    }
}
