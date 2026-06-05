package entity;

public class Product {
    private int productId;
    private String productName;
    private int categoryId;
    private int vendorId;
    private int quantity;
    private double price;
    private int reorderLevel;

    public Product() {}

    public Product(int productId, String productName, int categoryId, int vendorId,
                   int quantity, double price, int reorderLevel) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.vendorId = vendorId;
        this.quantity = quantity;
        this.price = price;
        this.reorderLevel = reorderLevel;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
}