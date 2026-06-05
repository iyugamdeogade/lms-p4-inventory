package entity;

import report.Document;
import report.Reportable;

import java.sql.Date;

public class PurchaseOrder extends Document implements Reportable {

    private int poId;
    private int vendorId;
    private Date orderDate;
    private double totalAmount;
    private String status;

    public PurchaseOrder() {
        super("Purchase Order");
    }

    public PurchaseOrder(int poId, int vendorId, Date orderDate, double totalAmount, String status) {
        super("Purchase Order" + (poId > 0 ? " #" + poId : ""));
        this.poId = poId;
        this.vendorId = vendorId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Reportable: print a summary of this PO to console
    @Override
    public void generateReport() {
        System.out.println("Generating Purchase Order Report...");
        System.out.println("Vendor ID : " + vendorId);
        System.out.println("Date      : " + orderDate);
        System.out.printf ("Total     : Rs. %.2f%n", totalAmount);
        System.out.println("Status    : " + status);
    }

    // Document: print the full document header + body
    @Override
    public void printDocument() {
        System.out.println("=== " + title + " ===");
        generateReport();
    }

    public int getPoId() { return poId; }
    public void setPoId(int poId) {
        this.poId = poId;
        this.title = "Purchase Order #" + poId;
    }

    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
