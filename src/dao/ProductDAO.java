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

            String query =
                    "INSERT INTO product(product_name, category_id, vendor_id, quantity, price, reorder_level) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst =
                    con.prepareStatement(query);

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

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

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
}