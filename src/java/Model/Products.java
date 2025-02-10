/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class Products {
    
    private int ID;
    private String ProductName;
    private String Description;
    private int Price;
    private int Quantity;
    private Date CreateAt;
    private Date UpdateAt;
    private int CreateBy;
    private int isDelete;
    private Date deletedAt;
    private int deleteBy;

    public Products() {
    }

    public Products(int ID, String ProductName, String Description, int Price, int Quantity, Date CreateAt, Date UpdateAt, int CreateBy, int isDelete, Date deletedAt, int deleteBy) {
        this.ID = ID;
        this.ProductName = ProductName;
        this.Description = Description;
        this.Price = Price;
        this.Quantity = Quantity;
        this.CreateAt = CreateAt;
        this.UpdateAt = UpdateAt;
        this.CreateBy = CreateBy;
        this.isDelete = isDelete;
        this.deletedAt = deletedAt;
        this.deleteBy = deleteBy;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public Date getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(Date CreateAt) {
        this.CreateAt = CreateAt;
    }

    public Date getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(Date UpdateAt) {
        this.UpdateAt = UpdateAt;
    }

    public int getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(int CreateBy) {
        this.CreateBy = CreateBy;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(int deleteBy) {
        this.deleteBy = deleteBy;
    }
    
    
    
}
