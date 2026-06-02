package dao;

import entity.Product;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDAO {

    // INSERT PRODUCT
    public void addProduct(Product product) {
        try {
            Connection con = DBUtil.getConnection();

            String query = "INSERT INTO product(product_name, category_id, vendor_id, quantity, price, reorder_level) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, product.getProductName());
            pst.setInt(2, product.getCategoryId());
            pst.setInt(3, product.getVendorId());
            pst.setInt(4, product.getQuantity());
            pst.setDouble(5, product.getPrice());
            pst.setInt(6, product.getReorderLevel());

            pst.executeUpdate();

            System.out.println("Product Added Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW PRODUCTS
    public void viewProducts() {
        try {
            Connection con = DBUtil.getConnection();

            String query = "SELECT * FROM product";

            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            System.out.println("\nID | Product Name | Quantity | Price");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("product_id") + " | " +
                        rs.getString("product_name") + " | " +
                        rs.getInt("quantity") + " | " +
                        rs.getDouble("price")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOW STOCK ALERT
    public void lowStockAlert() {
        try {
            Connection con = DBUtil.getConnection();

            String query = "SELECT * FROM product WHERE quantity < reorder_level";

            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            System.out.println("\nLOW STOCK PRODUCTS:\n");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("product_id") + " | " +
                        rs.getString("product_name") + " | Qty: " +
                        rs.getInt("quantity") + " | Reorder Level: " +
                        rs.getInt("reorder_level")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GENERATE LOW STOCK HTML REPORT
    public void generateLowStockHTMLReport() {
        try {
            Connection con = DBUtil.getConnection();

            String query = "SELECT * FROM product WHERE quantity < reorder_level";

            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            java.io.FileWriter writer = new java.io.FileWriter("reports/low_stock_alert.html");

            writer.write("<html><head><title>Low Stock Report</title></head><body>");
            writer.write("<h1>Low Stock Products</h1>");
            writer.write("<table border='1'>");
            writer.write("<tr><th>ID</th><th>Name</th><th>Quantity</th><th>Reorder Level</th></tr>");

            while (rs.next()) {
                writer.write(
                        "<tr>"
                                + "<td>" + rs.getInt("product_id") + "</td>"
                                + "<td>" + rs.getString("product_name") + "</td>"
                                + "<td>" + rs.getInt("quantity") + "</td>"
                                + "<td>" + rs.getInt("reorder_level") + "</td>"
                                + "</tr>"
                );
            }

            writer.write("</table>");
            writer.write("</body></html>");

            writer.close();

            System.out.println("HTML Report Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE STOCK
    public void updateStock(int productId, int newQuantity) {
        try {
            Connection con = DBUtil.getConnection();

            String query = "UPDATE product SET quantity = ? WHERE product_id = ?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, newQuantity);
            pst.setInt(2, productId);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("Stock Updated Successfully");
            } else {
                System.out.println("Product Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // CATEGORY WISE STOCK REPORT
public void categoryWiseStockReport() {

    try {

        Connection con = DBUtil.getConnection();

        String query =
                "SELECT c.category_name, " +
                "SUM(p.quantity * p.price) AS stock_value " +
                "FROM product p " +
                "JOIN category c " +
                "ON p.category_id = c.category_id " +
                "GROUP BY c.category_name";

        PreparedStatement pst =
                con.prepareStatement(query);

        ResultSet rs = pst.executeQuery();

        System.out.println("\nCATEGORY WISE STOCK REPORT:\n");

        while (rs.next()) {

            System.out.println(
                    rs.getString("category_name")
                    + " | Stock Value: "
                    + rs.getDouble("stock_value")
            );
        }

    } catch (Exception e) {

        e.printStackTrace();

    }
}
}