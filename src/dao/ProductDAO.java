package dao;

import entity.Product;
import util.DBUtil;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {

    private static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
    }

    public void addProduct(Product product) {
        String query = "INSERT INTO product(product_name, category_id, vendor_id, quantity, price, reorder_level) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, product.getProductName());
            pst.setInt(2, product.getCategoryId());
            pst.setInt(3, product.getVendorId());
            pst.setInt(4, product.getQuantity());
            pst.setDouble(5, product.getPrice());
            pst.setInt(6, product.getReorderLevel());
            pst.executeUpdate();

            System.out.println("Product Added Successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public void viewProducts() {
        String query =
            "SELECT p.product_id, p.product_name, c.category_name, v.vendor_name, p.quantity, p.price " +
            "FROM product p " +
            "JOIN category c ON p.category_id = c.category_id " +
            "JOIN vendor v ON p.vendor_id = v.vendor_id";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nID | Product Name | Category | Vendor | Quantity | Price");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-4d | %-20s | %-15s | %-20s | %-8d | %.2f%n",
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category_name"),
                    rs.getString("vendor_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"));
            }

        } catch (SQLException e) {
            System.out.println("Error viewing products: " + e.getMessage());
        }
    }

    public void lowStockAlert() {
        String query = "SELECT * FROM product WHERE quantity < reorder_level";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nLOW STOCK PRODUCTS:");
            System.out.println("-----------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("ID: %-4d | %-20s | Qty: %-5d | Reorder Level: %d%n",
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getInt("reorder_level"));
            }
            if (!found) System.out.println("All products are adequately stocked.");

        } catch (SQLException e) {
            System.out.println("Error checking low stock: " + e.getMessage());
        }
    }

    public void generateLowStockHTMLReport() {
        String query = "SELECT * FROM product WHERE quantity < reorder_level";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery();
             FileWriter writer = new FileWriter("reports/low_stock_alert.html")) {

            writer.write("<!DOCTYPE html><html><head><title>Low Stock Report</title></head><body>");
            writer.write("<h1>Low Stock Products</h1>");
            writer.write("<table border='1' cellpadding='6'>");
            writer.write("<tr><th>ID</th><th>Name</th><th>Quantity</th><th>Reorder Level</th></tr>");

            while (rs.next()) {
                writer.write("<tr>"
                    + "<td>" + rs.getInt("product_id") + "</td>"
                    + "<td>" + escapeHtml(rs.getString("product_name")) + "</td>"
                    + "<td>" + rs.getInt("quantity") + "</td>"
                    + "<td>" + rs.getInt("reorder_level") + "</td>"
                    + "</tr>");
            }

            writer.write("</table></body></html>");
            System.out.println("Low Stock HTML Report generated: reports/low_stock_alert.html");

        } catch (Exception e) {
            System.out.println("Error generating low stock report: " + e.getMessage());
        }
    }

    public void updateStock(int productId, int newQuantity) {
        if (newQuantity < 0) {
            System.out.println("Error: Quantity cannot be negative.");
            return;
        }
        String query = "UPDATE product SET quantity = ? WHERE product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, newQuantity);
            pst.setInt(2, productId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Stock Updated Successfully.");
            } else {
                System.out.println("Product Not Found (ID: " + productId + ").");
            }

        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }

    public void generateStockSummaryHTMLReport() {
        String query =
            "SELECT c.category_name, " +
            "SUM(p.quantity) AS total_qty, " +
            "SUM(p.quantity * p.price) AS stock_value " +
            "FROM product p " +
            "JOIN category c ON p.category_id = c.category_id " +
            "GROUP BY c.category_name";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery();
             FileWriter writer = new FileWriter("reports/stock_summary.html")) {

            writer.write("<!DOCTYPE html><html><head><title>Stock Summary Report</title></head><body>");
            writer.write("<h1>Category-Wise Stock Summary</h1>");
            writer.write("<table border='1' cellpadding='6'>");
            writer.write("<tr><th>Category</th><th>Total Quantity</th><th>Stock Value (Rs.)</th></tr>");

            while (rs.next()) {
                writer.write("<tr>"
                    + "<td>" + escapeHtml(rs.getString("category_name")) + "</td>"
                    + "<td>" + rs.getInt("total_qty") + "</td>"
                    + "<td>" + String.format("%.2f", rs.getDouble("stock_value")) + "</td>"
                    + "</tr>");
            }

            writer.write("</table></body></html>");
            System.out.println("Stock Summary HTML Report generated: reports/stock_summary.html");

        } catch (Exception e) {
            System.out.println("Error generating stock summary report: " + e.getMessage());
        }
    }

    public void categoryWiseStockReport() {
        String query =
            "SELECT c.category_name, SUM(p.quantity * p.price) AS stock_value " +
            "FROM product p " +
            "JOIN category c ON p.category_id = c.category_id " +
            "GROUP BY c.category_name";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nCATEGORY WISE STOCK REPORT:");
            System.out.println("-----------------------------------");

            while (rs.next()) {
                System.out.printf("%-20s | Stock Value: Rs. %.2f%n",
                    rs.getString("category_name"),
                    rs.getDouble("stock_value"));
            }

        } catch (SQLException e) {
            System.out.println("Error generating category report: " + e.getMessage());
        }
    }
}
