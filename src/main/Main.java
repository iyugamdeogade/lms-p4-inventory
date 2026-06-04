package main;

import dao.ProductDAO;
import dao.VendorDAO;
import dao.PurchaseOrderDAO;
import entity.Product;
import entity.Vendor;
import entity.PurchaseOrder;
import entity.OrderItem;
import report.StockReport;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // Read an integer from stdin, retrying on bad input
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a whole number.");
            }
        }
    }

    // Read a double from stdin, retrying on bad input
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a valid number.");
            }
        }
    }

    // Read a non-empty string from stdin
    private static String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("  Input cannot be empty.");
        }
    }

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAO();
        VendorDAO vendorDAO = new VendorDAO();
        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();

        StockReport sr = new StockReport("Inventory Report");
        sr.showTitle();
        sr.generateReport();
        sr.printDocument();

        while (true) {

            System.out.println("\n===== INVENTORY TRACKER =====");
            System.out.println(" 1. Add Product");
            System.out.println(" 2. View Products");
            System.out.println(" 3. Low Stock Alert");
            System.out.println(" 4. Generate Low Stock HTML Report");
            System.out.println(" 5. Update Stock");
            System.out.println(" 6. Category Wise Stock Report");
            System.out.println(" 7. Add Vendor");
            System.out.println(" 8. View Vendors");
            System.out.println(" 9. Create Purchase Order");
            System.out.println("10. View Purchase Orders");
            System.out.println("11. View Purchase Order Details");
            System.out.println("12. Generate Stock Summary HTML Report");
            System.out.println("13. Exit");

            int choice = readInt("Enter Choice: ");

            switch (choice) {

                case 1:
                    String name        = readLine("Enter Product Name: ");
                    int categoryId     = readInt("Enter Category ID: ");
                    int vendorId       = readInt("Enter Vendor ID: ");
                    int quantity       = readInt("Enter Quantity: ");
                    double price       = readDouble("Enter Price: ");
                    int reorderLevel   = readInt("Enter Reorder Level: ");

                    productDAO.addProduct(new Product(0, name, categoryId, vendorId, quantity, price, reorderLevel));
                    break;

                case 2:
                    productDAO.viewProducts();
                    break;

                case 3:
                    productDAO.lowStockAlert();
                    break;

                case 4:
                    productDAO.generateLowStockHTMLReport();
                    break;

                case 5:
                    int pid = readInt("Enter Product ID: ");
                    int qty = readInt("Enter New Quantity: ");
                    productDAO.updateStock(pid, qty);
                    break;

                case 6:
                    productDAO.categoryWiseStockReport();
                    break;

                case 7:
                    String vName    = readLine("Enter Vendor Name: ");
                    String vPhone   = readLine("Enter Phone: ");
                    String vAddress = readLine("Enter Address: ");
                    vendorDAO.addVendor(new Vendor(0, vName, vPhone, vAddress));
                    break;

                case 8:
                    vendorDAO.viewVendors();
                    break;

                case 9:
                    int poVendorId = readInt("Enter Vendor ID: ");
                    int itemCount  = readInt("How many items in this order? ");

                    if (itemCount <= 0) {
                        System.out.println("Order must have at least one item.");
                        break;
                    }

                    List<OrderItem> items = new ArrayList<>();
                    double totalAmount = 0;

                    for (int i = 1; i <= itemCount; i++) {
                        System.out.println("-- Item " + i + " --");
                        int    itemProductId = readInt("  Enter Product ID: ");
                        int    itemQty       = readInt("  Enter Quantity: ");
                        double itemPrice     = readDouble("  Enter Price: ");

                        items.add(new OrderItem(0, 0, itemProductId, itemQty, itemPrice));
                        totalAmount += (double) itemQty * itemPrice;
                    }

                    PurchaseOrder po = new PurchaseOrder(
                        0, poVendorId, Date.valueOf(LocalDate.now()), totalAmount, "Pending"
                    );
                    // OOP demo: PurchaseOrder extends Document implements Reportable
                    po.showTitle();
                    po.generateReport();
                    po.printDocument();
                    poDAO.createPurchaseOrder(po, items);
                    break;

                case 10:
                    poDAO.viewPurchaseOrders();
                    break;

                case 11:
                    int detailPoId = readInt("Enter PO ID to view details: ");
                    poDAO.viewPurchaseOrderDetails(detailPoId);
                    break;

                case 12:
                    productDAO.generateStockSummaryHTMLReport();
                    break;

                case 13:
                    System.out.println("Exiting Program...");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 13.");
                    break;
            }
        }
    }
}
