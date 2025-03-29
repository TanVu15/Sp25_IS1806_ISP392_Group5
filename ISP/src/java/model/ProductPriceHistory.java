package model;

import java.sql.Timestamp;

public class ProductPriceHistory {
    private int historyID;
    private int productID;
    private String productName;
    private String image;
    private int price;
    private String priceType;
    private Timestamp changedAt;
    private String changedBy;
    private int supplierID;
    private String supplierName;

    public ProductPriceHistory() {
    }

    public ProductPriceHistory(int historyID, int productID, String productName, String image, int price, String priceType, Timestamp changedAt, String changedBy, int supplierID, String supplierName) {
        this.historyID = historyID;
        this.productID = productID;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.priceType = priceType;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
    }
    

    public ProductPriceHistory(int historyID, int productID, String productName, String image, int price, String priceType, Timestamp changedAt, String changedBy) {
        this.historyID = historyID;
        this.productID = productID;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.priceType = priceType;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
    }

    public ProductPriceHistory(int productID, String productName, int price, String priceType, Timestamp changedAt, String changedBy) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.priceType = priceType;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
    }

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    }
