package model;

public class ProductZones {
    
    private int ProductID;
    private int ZoneID;

    public ProductZones() {
    }

    public ProductZones(int ProductID, int ZoneID) {
        this.ProductID = ProductID;
        this.ZoneID = ZoneID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getZoneID() {
        return ZoneID;
    }

    public void setZoneID(int zoneID) {
        ZoneID = zoneID;
    }
}

