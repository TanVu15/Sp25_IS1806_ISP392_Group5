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
public class DebtRecords {
    
    
    private int ID;
    private int CustomerID;
    private int AmountOwed;
    private int PaymentStatus;
    private Date CreateAt;
    private Date UpdateAt;
    private int CreateBy;
    private int isDelete;
    private Date deletedAt;
    private int deleteBy;

    public DebtRecords() {
    }
    
    
    public DebtRecords(int ID, int CustomerID, int AmountOwed, int PaymentStatus, Date CreateAt, Date UpdateAt, int CreateBy, int isDelete, Date deletedAt, int deleteBy) {
        this.ID = ID;
        this.CustomerID = CustomerID;
        this.AmountOwed = AmountOwed;
        this.PaymentStatus = PaymentStatus;
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

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int getAmountOwed() {
        return AmountOwed;
    }

    public void setAmountOwed(int AmountOwed) {
        this.AmountOwed = AmountOwed;
    }

    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int PaymentStatus) {
        this.PaymentStatus = PaymentStatus;
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
