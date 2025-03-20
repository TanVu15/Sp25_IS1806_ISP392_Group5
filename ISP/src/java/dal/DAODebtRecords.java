/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.DebtRecords;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Statement;

/**
 *
 * @author ADMIN
 */
public class DAODebtRecords {

    public static final DAODebtRecords INSTANCE = new DAODebtRecords();

    public Connection connect;

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
                debt.setInvoiceDate(rs.getDate("InvoiceDate"));
                debt.setImagePath(rs.getString("ImagePath"));
                debt.setShopID(rs.getInt("ShopID"));
                debt.setCreateAt(rs.getDate("CreateAt"));
                debt.setUpdateAt(rs.getDate("UpdateAt"));
                debt.setCreateBy(rs.getInt("CreateBy"));
                debt.setIsDelete(rs.getInt("isDelete"));
                debt.setDeletedAt(rs.getDate("DeletedAt"));
                debt.setDeleteBy(rs.getInt("DeleteBy"));
                debt.setNote(rs.getString("Note"));
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
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DebtRecords debt = new DebtRecords();
                    debt.setID(rs.getInt("ID"));
                    debt.setCustomerID(rs.getInt("customerID"));
                    debt.setAmountOwed(rs.getInt("AmountOwed"));
                    debt.setPaymentStatus(rs.getInt("PaymentStatus"));
                    debt.setInvoiceDate(rs.getDate("InvoiceDate"));
                    debt.setImagePath(rs.getString("ImagePath"));
                    debt.setShopID(rs.getInt("ShopID"));
                    debt.setActive(rs.getInt("Active"));
                    debt.setCreateAt(rs.getDate("CreateAt"));
                    debt.setUpdateAt(rs.getDate("UpdateAt"));
                    debt.setCreateBy(rs.getInt("CreateBy"));
                    debt.setIsDelete(rs.getInt("isDelete"));
                    debt.setDeletedAt(rs.getDate("DeletedAt"));
                    debt.setDeleteBy(rs.getInt("DeleteBy"));
                    debt.setNote(rs.getString("Note"));
                    debtRecordses.add(debt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi SQL
        }
        return debtRecordses;
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

    public ArrayList<DebtRecords> getCustomerDebtRecordsSearch(String information, int customerID) throws Exception {
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
                    debt.setShopID(rs.getInt("ShopID"));
                    debt.setActive(rs.getInt("Active"));
                    debt.setUpdateAt(rs.getDate("UpdateAt"));
                    debt.setCreateBy(rs.getInt("CreateBy"));
                    debt.setIsDelete(rs.getInt("isDelete"));
                    debt.setDeletedAt(rs.getDate("DeletedAt"));
                    debt.setDeleteBy(rs.getInt("DeleteBy"));
                    debt.setNote(rs.getString("Note"));
                    debt.setInvoiceDate(rs.getDate("InvoiceDate"));

                    String name = DAOCustomers.INSTANCE.getCustomersByID(debt.getCustomerID()).getName();
                    String status = "";
                    if (debt.getPaymentStatus() == 1) {
                        status += "Trả Nợ";
                    } else {
                        status += "Vay Nợ";
                    }
                    String debtSeach = debt.getID() + " "
                            + name + " "
                            + debt.getAmountOwed() + " "
                            + status + " "
                            + debt.getCreateAt() + " "
                            + debt.getUpdateAt() + " "
                            + DAOUser.INSTANCE.getUserByID(debt.getCreateBy()).getFullName() + " "
                            + debt.getNote();
                    if (debtSeach.toLowerCase().contains(information.toLowerCase())) {
                        debtRecordses.add(debt);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi SQL
        }
        return debtRecordses;
    }

    public ArrayList<DebtRecords> getDebtRecordsSearch(String information) throws Exception {
        ArrayList<DebtRecords> debtRecordses = new ArrayList<>();
        String sql = "SELECT * FROM DebtRecords ";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) { // Thực thi sau khi thiết lập tham số
                while (rs.next()) {
                    DebtRecords debt = new DebtRecords();
                    debt.setID(rs.getInt("ID"));
                    debt.setCustomerID(rs.getInt("customerID"));
                    debt.setAmountOwed(rs.getInt("AmountOwed"));
                    debt.setPaymentStatus(rs.getInt("PaymentStatus"));
                    debt.setShopID(rs.getInt("ShopID"));
                    debt.setActive(rs.getInt("Active"));
                    debt.setCreateAt(rs.getDate("CreateAt"));
                    debt.setUpdateAt(rs.getDate("UpdateAt"));
                    debt.setCreateBy(rs.getInt("CreateBy"));
                    debt.setIsDelete(rs.getInt("isDelete"));
                    debt.setDeletedAt(rs.getDate("DeletedAt"));
                    debt.setDeleteBy(rs.getInt("DeleteBy"));
                    debt.setNote(rs.getString("Note"));
                    debt.setInvoiceDate(rs.getDate("InvoiceDate"));

                    String name = DAOCustomers.INSTANCE.getCustomersByID(debt.getCustomerID()).getName();
                    String status = "";
                    if (debt.getPaymentStatus() == 1) {
                        status += "Khách trả";
                    } if (debt.getPaymentStatus() == -1) {
                        status += "Khách vay";
                    }if (debt.getPaymentStatus() == 2) {
                        status += "Cửa hàng vay";
                    }if (debt.getPaymentStatus() == -2) {
                        status += "Cửa hàng trả";
                    }
                    
                    String debtSeach = debt.getID() + " "
                            + name + " "
                            + debt.getAmountOwed() + " "
                            + status + " "
                            + debt.getCreateAt() + " "
                            + debt.getUpdateAt() + " "
                            + DAOUser.INSTANCE.getUserByID(debt.getCreateBy()).getFullName() + " "
                            + debt.getNote();
                    if (debtSeach.toLowerCase().contains(information.toLowerCase())) {
                        debtRecordses.add(debt);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi SQL
        }
        return debtRecordses;
    }

    public DebtRecords getDebtRecordByID(int ID) throws Exception {
        String query = "SELECT * FROM DebtRecords WHERE ID=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            DebtRecords debt = new DebtRecords();
            debt.setID(rs.getInt("ID"));
            debt.setCustomerID(rs.getInt("customerid"));
            debt.setAmountOwed(rs.getInt("AmountOwed"));
            debt.setPaymentStatus(rs.getInt("PaymentStatus"));
            debt.setInvoiceDate(rs.getDate("InvoiceDate"));
            debt.setImagePath(rs.getString("ImagePath"));
            debt.setShopID(rs.getInt("ShopID"));
            debt.setCreateAt(rs.getDate("CreateAt"));
            debt.setUpdateAt(rs.getDate("UpdateAt"));
            debt.setCreateBy(rs.getInt("CreateBy"));
            debt.setIsDelete(rs.getInt("isDelete"));
            debt.setDeletedAt(rs.getDate("DeletedAt"));
            debt.setDeleteBy(rs.getInt("DeleteBy"));
            debt.setNote(rs.getString("Note"));
            return debt;
        }
        return null;
    }

    public void updateCustomerDebt(int customerId, int paymentStatus, int amount) throws SQLException {
        String operation = switch (paymentStatus) {
            case 1, 2 ->
                "+";
            case -1, -2 ->
                "-";
            default ->
                throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ.");
        };

        String updateSQL = "UPDATE customers SET wallet = wallet " + operation + " ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(updateSQL)) {
            ps.setInt(1, amount);
            ps.setInt(2, customerId);
            ps.executeUpdate();
//            ps.close();
        }
    }

    public ArrayList<DebtRecords> getDebtRecordsActive() {
        ArrayList<DebtRecords> debtRecordses = new ArrayList<>();
        String sql = "SELECT * FROM DebtRecords WHERE Active = ?";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 0);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DebtRecords debt = new DebtRecords();
                    debt.setID(rs.getInt("ID"));
                    debt.setCustomerID(rs.getInt("customerID"));
                    debt.setAmountOwed(rs.getInt("AmountOwed"));
                    debt.setPaymentStatus(rs.getInt("PaymentStatus"));
                    debt.setInvoiceDate(rs.getDate("InvoiceDate"));
                    debt.setImagePath(rs.getString("ImagePath"));
                    debt.setShopID(rs.getInt("ShopID"));
                    debt.setActive(rs.getInt("Active"));
                    debt.setCreateAt(rs.getDate("CreateAt"));
                    debt.setUpdateAt(rs.getDate("UpdateAt"));
                    debt.setCreateBy(rs.getInt("CreateBy"));
                    debt.setIsDelete(rs.getInt("isDelete"));
                    debt.setDeletedAt(rs.getDate("DeletedAt"));
                    debt.setDeleteBy(rs.getInt("DeleteBy"));
                    debt.setNote(rs.getString("Note"));
                    debtRecordses.add(debt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi SQL
        }
        return debtRecordses;
    }

    public void updateDebtRecordActive(DebtRecords debtRecords) {
        String sql = "UPDATE DebtRecords SET Active = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ps.setInt(2, debtRecords.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getTotalDebtRecordByShopId(int shopId) {
        String sql = "SELECT COUNT(*) FROM DebtRecords WHERE ShopID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<DebtRecords> getDebtrecordsByPage(int page, int debtsPerPage, int shopId) {
        ArrayList<DebtRecords> debtRecordses = new ArrayList<>();
        String sql = "SELECT * FROM DebtRecords WHERE ShopID = ? ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, (page - 1) * debtsPerPage);
            ps.setInt(3, debtsPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DebtRecords debt = new DebtRecords();
                    debt.setID(rs.getInt("ID"));
                    debt.setCustomerID(rs.getInt("customerID"));
                    debt.setAmountOwed(rs.getInt("AmountOwed"));
                    debt.setPaymentStatus(rs.getInt("PaymentStatus"));
                    debt.setInvoiceDate(rs.getDate("InvoiceDate"));
                    debt.setImagePath(rs.getString("ImagePath"));
                    debt.setShopID(rs.getInt("ShopID"));
                    debt.setActive(rs.getInt("Active"));
                    debt.setCreateAt(rs.getDate("CreateAt"));
                    debt.setUpdateAt(rs.getDate("UpdateAt"));
                    debt.setCreateBy(rs.getInt("CreateBy"));
                    debt.setIsDelete(rs.getInt("isDelete"));
                    debt.setDeletedAt(rs.getDate("DeletedAt"));
                    debt.setDeleteBy(rs.getInt("DeleteBy"));
                    debt.setNote(rs.getString("Note"));
                    debtRecordses.add(debt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return debtRecordses;
    }

    public int AddDebtRecords(DebtRecords debtrecords, int userid) throws Exception {
        int generatedDebtID = -1; // Giá trị mặc định nếu lỗi xảy ra

        String sql = "INSERT INTO DebtRecords (CustomerID, AmountOwed, PaymentStatus, InvoiceDate, CreateAt, CreateBy, isDelete, ImagePath, Note, shopid, Active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // 🔥 THÊM RETURN_GENERATED_KEYS
            ps.setInt(1, debtrecords.getCustomerID());
            ps.setInt(2, debtrecords.getAmountOwed());
            ps.setInt(3, debtrecords.getPaymentStatus());
            ps.setDate(4, debtrecords.getInvoiceDate());
            ps.setDate(5, today);
            ps.setInt(6, userid);
            ps.setInt(7, 0);
            ps.setString(8, debtrecords.getImagePath());
            ps.setString(9, debtrecords.getNote());
            ps.setInt(10, debtrecords.getShopID());
            ps.setInt(11, 0);

            int affectedRows = ps.executeUpdate(); // 🔥 PHẢI CHẠY EXECUTEUPDATE() TRƯỚC
            if (affectedRows == 0) {
                throw new SQLException("Creating debt record failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) { // 🔥 LẤY GIÁ TRỊ SAU KHI CHẠY EXECUTEUPDATE()
                if (rs.next()) {
                    generatedDebtID = rs.getInt(1); // Lấy debtID vừa được tạo
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return generatedDebtID;
    }

    public static void main(String[] args) throws Exception {
        DAODebtRecords dao = new DAODebtRecords();
        dao.getDebtRecordsActive();
        System.out.println(dao.getDebtRecords());
        //DebtRecords debtRecords = new DebtRecords(0, 4, 500, -1, today, today, 0, 0, today, 0);
        //dao.AddDebtRecords(debtRecords, 0);
        //dao.getCustomerDebtRecords(3);
        System.out.println(dao.getDebtRecordsSearch("1000"));
        DAOShops daoShop = new DAOShops();

    }

}
