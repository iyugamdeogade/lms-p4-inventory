package entity;

public class Vendor {
    private int vendorId;
    private String vendorName;
    private String phone;
    private String address;

    public Vendor() {}

    public Vendor(int vendorId, String vendorName, String phone, String address) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.phone = phone;
        this.address = address;
    }

    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}