package main;

import dao.ProductDAO;
import entity.Product;
import report.StockReport;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ProductDAO dao = new ProductDAO();

        // ABSTRACT CLASS + INTERFACE TEST
       StockReport sr = new StockReport("Inventory Report");

        sr.showTitle();
        sr.generateReport();
        sr.printDocument();

        while (true) {

            System.out.println("\n===== INVENTORY TRACKER =====");

            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Low Stock Alert");
            System.out.println("4. Generate HTML Report");
            System.out.println("5. Update Stock");
            System.out.println("6. Category Wise Stock Report");
            System.out.println("7. Exit");

            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    sc.nextLine();

                    System.out.print("Enter Product Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Category ID: ");
                    int categoryId = sc.nextInt();

                    System.out.print("Enter Vendor ID: ");
                    int vendorId = sc.nextInt();

                    System.out.print("Enter Quantity: ");
                    int quantity = sc.nextInt();

                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();

                    System.out.print("Enter Reorder Level: ");
                    int reorderLevel = sc.nextInt();

                    Product p = new Product(
                            0,
                            name,
                            categoryId,
                            vendorId,
                            quantity,
                            price,
                            reorderLevel
                    );

                    dao.addProduct(p);

                    break;

                case 2:

                    dao.viewProducts();

                    break;

                case 3:

                    dao.lowStockAlert();

                    break;

                case 4:

                    dao.generateLowStockHTMLReport();

                    break;

                case 5:

                    System.out.print("Enter Product ID: ");
                    int pid = sc.nextInt();

                    System.out.print("Enter New Quantity: ");
                    int qty = sc.nextInt();

                    dao.updateStock(pid, qty);

                    break;

                case 6:

                    dao.categoryWiseStockReport();

                    break;

                case 7:

                    System.out.println("Exiting Program...");
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice");
            }
        }
    }
}