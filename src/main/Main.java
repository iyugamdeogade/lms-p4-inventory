package main;

import dao.ProductDAO;
import entity.Product;

public class Main {

    public static void main(String[] args) {

        ProductDAO dao = new ProductDAO();

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

        System.out.println("\nProduct List:\n");

        dao.viewProducts();
    }
}