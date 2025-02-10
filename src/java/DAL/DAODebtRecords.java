/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Model.Customers;
import Model.DebtRecords;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class DAODebtRecords {

    public static final DAODebtRecords INSTANCE = new DAODebtRecords();
    protected Connection connect;

    public DAODebtRecords() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<DebtRecords> getDebtRecords() {
        ArrayList<DebtRecords> debtRecordses = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM DebtRecords ";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                DebtRecords debt = new DebtRecords();
                debt.setID(rs.getInt("ID"));
                debt.setCustomerID(rs.getInt("customerid"));
                debt.setAmountOwed(rs.getInt("AmountOwed"));
                debt.setPaymentStatus(rs.getInt("PaymentStatus"));
                debt.setCreateAt(rs.getDate("CreateAt"));
                debt.setUpdateAt(rs.getDate("UpdateAt"));
                debt.setCreateBy(rs.getInt("CreateBy"));
                debt.setIsDelete(rs.getInt("isDelete"));
                debt.setDeletedAt(rs.getDate("DeletedAt"));
                debt.setDeleteBy(rs.getInt("DeleteBy"));
                debtRecordses.add(debt);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return debtRecordses;
    }

   public ArrayList<DebtRecords> getCustomerDebtRecords(int customerID) {
    ArrayList<DebtRecords> debtRecordses = new ArrayList<>();
    String sql = "SELECT * FROM DebtRecords WHERE customerID = ?";
    
    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setInt(1, customerID); // Đặt giá trị customerID vào câu SQL trước khi chạy
        try (ResultSet rs = ps.executeQuery()) { // Thực thi sau khi thiết lập tham số
            while (rs.next()) {
                DebtRecords debt = new DebtRecords();
                debt.setID(rs.getInt("ID"));
                debt.setCustomerID(rs.getInt("customerID"));
                debt.setAmountOwed(rs.getInt("AmountOwed"));
                debt.setPaymentStatus(rs.getInt("PaymentStatus"));
                debt.setCreateAt(rs.getDate("CreateAt"));
                debt.setUpdateAt(rs.getDate("UpdateAt"));
                debt.setCreateBy(rs.getInt("CreateBy"));
                debt.setIsDelete(rs.getInt("isDelete"));
                debt.setDeletedAt(rs.getDate("DeletedAt"));
                debt.setDeleteBy(rs.getInt("DeleteBy"));
                debtRecordses.add(debt);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Xử lý lỗi SQL
    }
    return debtRecordses;
}


    public void AddDebtRecords(DebtRecords debtrecords, int userid) throws Exception {
        String sql = "INSERT INTO DebtRecords (CustomerID, AmountOwed, PaymentStatus, CreateAt, CreateBy, isDelete) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, debtrecords.getCustomerID());
            ps.setInt(2, debtrecords.getAmountOwed());
            ps.setInt(3, debtrecords.getPaymentStatus());
            ps.setDate(4, today);
            ps.setInt(5, userid);
            ps.setInt(6, 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        //Lấy wallet để update cus
        Customers customer = DAOCustomers.INSTANCE.getCustomersByID(debtrecords.getCustomerID());
        
        int wallet = customer.getWallet();
        if(debtrecords.getPaymentStatus()==1){
            wallet += debtrecords.getAmountOwed();
        }else{
            wallet -= debtrecords.getAmountOwed();
        }
        customer.setWallet(wallet);
        DAOCustomers.INSTANCE.setCustomerWallet(customer);
    }

    public void deleteDebtRecords(int deleteid, int userid) {
        String sql = "UPDATE DebtRecords SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ps.setInt(2, userid);
            ps.setDate(3, today);
            ps.setInt(4, deleteid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDebtRecord(DebtRecords debtRecords) {
        String sql = "UPDATE DebtRecords SET amountowed = ? , Paymentstatus = ?, UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, debtRecords.getAmountOwed());
            ps.setInt(2, debtRecords.getPaymentStatus());
            ps.setDate(3, today);
            ps.setInt(4, debtRecords.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        DAODebtRecords dao = new DAODebtRecords();
        dao.getDebtRecords();
        System.out.println(dao.getDebtRecords());
        //DebtRecords debtRecords = new DebtRecords(0, 4, 500, -1, today, today, 0, 0, today, 0);
        //dao.AddDebtRecords(debtRecords, 0);
        dao.getCustomerDebtRecords(3);

    }

}
