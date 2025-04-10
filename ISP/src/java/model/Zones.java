/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class Zones {
    
    private int ID;
    private String ZoneName;
    private int ShopID;
    private String Description;
    private int ProductID;
    private Date CreateAt;
    private Date UpdateAt;
    private int CreateBy;
    private int isDelete;
    private Date deletedAt;
    private int deleteBy;

    public Zones() {
    }

    public Zones(int ID) {
        this.ID = ID;
    }

    public Zones(int ID, String ZoneName, int ShopID, String Description, int ProductID, Date CreateAt, Date UpdateAt, int CreateBy, int isDelete, Date deletedAt, int deleteBy) {
        this.ID = ID;
        this.ZoneName = ZoneName;
        this.ShopID = ShopID;
        this.Description = Description;
        this.ProductID = ProductID;
        this.CreateAt = CreateAt;
        this.UpdateAt = UpdateAt;
        this.CreateBy = CreateBy;
        this.isDelete = isDelete;
        this.deletedAt = deletedAt;
        this.deleteBy = deleteBy;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
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

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int ShopID) {
        this.ShopID = ShopID;
    }
    
    
}
