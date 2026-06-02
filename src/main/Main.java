package main;

import dao.ProductDAO;
import entity.Product;

public class Main {

    public static void main(String[] args) {

        ProductDAO dao = new ProductDAO();

        // ADD PRODUCT
        Product p = new Product(
                0,
                "Marker",
                1,
                1,
                15,
                50,
                5
        );

        dao.addProduct(p);

        // VIEW PRODUCTS
        System.out.println("\nPRODUCT LIST:\n");

        dao.viewProducts();

        // LOW STOCK ALERT
        dao.lowStockAlert();
    }
}