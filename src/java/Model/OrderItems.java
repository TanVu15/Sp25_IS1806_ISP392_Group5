/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ADMIN
 */
public class OrderItems {

    private int ID;
    private int OrderID;
    private int ProductID;
    private String ProductName;
    private String Description;
    private int Price;
    private int Quantity;
    private int UnitPrice;

    public OrderItems() {
    }

    public OrderItems(int ID, int OrderID, int ProductID, String ProductName, String Description, int Price, int Quantity, int UnitPrice) {
        this.ID = ID;
        this.OrderID = OrderID;
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.Description = Description;
        this.Price = Price;
        this.Quantity = Quantity;
        this.UnitPrice = UnitPrice;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(int UnitPrice) {
        this.UnitPrice = UnitPrice;
    }
    
    
}
