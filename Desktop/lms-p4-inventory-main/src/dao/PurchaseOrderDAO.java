package dao;

import entity.OrderItem;
import entity.PurchaseOrder;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PurchaseOrderDAO {

    public void createPurchaseOrder(PurchaseOrder po, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("Error: A purchase order must have at least one item.");
            return;
        }

        String poQuery   = "INSERT INTO purchase_order(vendor_id, order_date, total_amount, status) VALUES (?, ?, ?, ?)";
        String itemQuery = "INSERT INTO order_item(po_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            try {
                int poId;

                try (PreparedStatement pst = con.prepareStatement(poQuery, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, po.getVendorId());
                    pst.setDate(2, po.getOrderDate());
                    pst.setDouble(3, po.getTotalAmount());
                    pst.setString(4, po.getStatus());
                    pst.executeUpdate();

                    try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("Failed to retrieve generated PO ID.");
                        }
                        poId = generatedKeys.getInt(1);
                    }
                }

                // Reuse one PreparedStatement for all items
                try (PreparedStatement itemPst = con.prepareStatement(itemQuery)) {
                    for (OrderItem item : items) {
                        itemPst.setInt(1, poId);
                        itemPst.setInt(2, item.getProductId());
                        itemPst.setInt(3, item.getQuantity());
                        itemPst.setDouble(4, item.getPrice());
                        itemPst.executeUpdate();
                    }
                }

                con.commit();
                System.out.println("Purchase Order Created Successfully. PO ID: " + poId);

            } catch (SQLException e) {
                con.rollback();
                System.out.println("Purchase Order creation failed, transaction rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    // JOIN 1: purchase_order + vendor (summary list)
    public void viewPurchaseOrders() {
        String query =
            "SELECT po.po_id, v.vendor_name, po.order_date, po.total_amount, po.status " +
            "FROM purchase_order po " +
            "JOIN vendor v ON po.vendor_id = v.vendor_id " +
            "ORDER BY po.po_id DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\nPO ID | Vendor               | Date       | Total Amount | Status");
            System.out.println("--------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-6d | %-20s | %-10s | Rs. %-10.2f | %s%n",
                    rs.getInt("po_id"),
                    rs.getString("vendor_name"),
                    rs.getDate("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("status"));
            }
            if (!found) System.out.println("No purchase orders found.");

        } catch (SQLException e) {
            System.out.println("Error viewing purchase orders: " + e.getMessage());
        }
    }

    // JOIN 2: purchase_order + vendor + order_item + product (full PO detail)
    public void viewPurchaseOrderDetails(int poId) {
        String query =
            "SELECT po.po_id, v.vendor_name, po.order_date, po.status, " +
            "p.product_name, oi.quantity, oi.price, (oi.quantity * oi.price) AS line_total " +
            "FROM purchase_order po " +
            "JOIN vendor v ON po.vendor_id = v.vendor_id " +
            "JOIN order_item oi ON po.po_id = oi.po_id " +
            "JOIN product p ON oi.product_id = p.product_id " +
            "WHERE po.po_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, poId);

            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("\n=== Purchase Order Details: PO #" + poId + " ===");
                System.out.println("Product              | Qty  | Price     | Line Total");
                System.out.println("------------------------------------------------------");

                boolean found = false;
                while (rs.next()) {
                    if (!found) {
                        System.out.println("Vendor : " + rs.getString("vendor_name"));
                        System.out.println("Date   : " + rs.getDate("order_date"));
                        System.out.println("Status : " + rs.getString("status"));
                        System.out.println("------------------------------------------------------");
                        found = true;
                    }
                    System.out.printf("%-20s | %-4d | Rs. %-6.2f | Rs. %.2f%n",
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("line_total"));
                }
                if (!found) System.out.println("PO #" + poId + " not found or has no items.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing PO details: " + e.getMessage());
        }
    }
}
