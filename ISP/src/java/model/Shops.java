/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class Shops {

    private int ID;
    private int OwnerID;
    private String ShopName;
    private String LogoShop;
    private String Phone;
    private String BankAcc;
    private String Location;
    private String Email;
    private Date CreatedAt;
    private Date UpdatedAt;
    private int CreatedBy;
    private int IsDelete;
    private Date DeletedAt;
    private int DeleteBy;

    public Shops() {
    }

    public Shops(int ID, int OwnerID, String ShopName, String LogoShop, String Location, String Email, Date CreatedAt, Date UpdatedAt, int CreatedBy, int IsDelete, Date DeletedAt, int DeleteBy) {
        this.ID = ID;
        this.OwnerID = OwnerID;
        this.ShopName = ShopName;
        this.LogoShop = LogoShop;
        this.Location = Location;
        this.Email = Email;
        this.CreatedAt = CreatedAt;
        this.UpdatedAt = UpdatedAt;
        this.CreatedBy = CreatedBy;
        this.IsDelete = IsDelete;
        this.DeletedAt = DeletedAt;
        this.DeleteBy = DeleteBy;
    }

    public Shops(String shopname, String imageLink, String email, String location,String Phone,String BankAcc, int OwnerID) {
        this.OwnerID = OwnerID;
        this.ShopName = shopname;
        this.LogoShop = imageLink;
        this.Location = location;
        this.Phone = Phone;
        this.BankAcc = BankAcc;
        this.Email = email;
    }

    public Shops(String shopname, String email, String location, int OwnerID) {
        this.OwnerID = OwnerID;
        this.ShopName = shopname;
        this.Location = location;
        this.Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getBankAcc() {
        return BankAcc;
    }

    public void setBankAcc(String BankAcc) {
        this.BankAcc = BankAcc;
    }

    public Shops(int ID, int OwnerID, String ShopName, String LogoShop, String Phone, String BankAcc, String Location, String Email, Date CreatedAt, Date UpdatedAt, int CreatedBy, int IsDelete, Date DeletedAt, int DeleteBy) {
        this.ID = ID;
        this.OwnerID = OwnerID;
        this.ShopName = ShopName;
        this.LogoShop = LogoShop;
        this.Phone = Phone;
        this.BankAcc = BankAcc;
        this.Location = Location;
        this.Email = Email;
        this.CreatedAt = CreatedAt;
        this.UpdatedAt = UpdatedAt;
        this.CreatedBy = CreatedBy;
        this.IsDelete = IsDelete;
        this.DeletedAt = DeletedAt;
        this.DeleteBy = DeleteBy;
    }
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(int OwnerID) {
        this.OwnerID = OwnerID;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public String getLogoShop() {
        return LogoShop;
    }

    public void setLogoShop(String LogoShop) {
        this.LogoShop = LogoShop;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date UpdatedAt) {
        this.UpdatedAt = UpdatedAt;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public int getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(int IsDelete) {
        this.IsDelete = IsDelete;
    }

    public Date getDeletedAt() {
        return DeletedAt;
    }

    public void setDeletedAt(Date DeletedAt) {
        this.DeletedAt = DeletedAt;
    }

    public int getDeleteBy() {
        return DeleteBy;
    }

    public void setDeleteBy(int DeleteBy) {
        this.DeleteBy = DeleteBy;
    }

}
