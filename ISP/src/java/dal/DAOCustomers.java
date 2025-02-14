/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author ADMIN
 */
import model.Customers;
import model.Users;
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
public class DAOCustomers {

    public static final DAOCustomers INSTANCE = new DAOCustomers();
    protected Connection connect;

    public DAOCustomers() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<Customers> getAllCustomers() {
        ArrayList<Customers> customers = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Customers ";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Customers cs = new Customers();
                cs.setID(rs.getInt("ID"));
                cs.setWallet(rs.getInt("Wallet"));
                cs.setName(rs.getString("Name"));
                cs.setPhone(rs.getString("Phone"));
                cs.setAddress(rs.getString("Address"));
                cs.setCreateAt(rs.getDate("CreateAt"));
                cs.setUpdateAt(rs.getDate("UpdateAt"));
                cs.setCreateBy(rs.getInt("CreateBy"));
                cs.setIsDelete(rs.getInt("isDelete"));
                cs.setDeletedAt(rs.getDate("DeletedAt"));
                cs.setDeleteBy(rs.getInt("DeleteBy"));
                customers.add(cs);
                
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return customers;
    }

    public void deleteCustomers(int deleteid, int userid) {
        String sql = "UPDATE Customers SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE id = ?";
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

    public void updateCustomers(Customers customers) {
        String sql = "UPDATE Customers SET Name = ?,Phone = ? , Address = ?, UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, customers.getName());
            ps.setString(2, customers.getPhone());
            ps.setString(3, customers.getAddress());
            ps.setDate(4, today);
            ps.setInt(5, customers.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Customers getCustomersByID(int ID) throws Exception {
        String query = "SELECT * FROM Customers WHERE ID=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Customers cus = new Customers();
            cus.setID(rs.getInt("ID"));
            cus.setWallet(rs.getInt("Wallet"));
            cus.setName(rs.getString("Name"));
            cus.setPhone(rs.getString("Phone"));
            cus.setAddress(rs.getString("Address"));
            cus.setCreateAt(rs.getDate("CreateAt"));
            cus.setUpdateAt(rs.getDate("UpdateAt"));
            cus.setCreateBy(rs.getInt("CreateBy"));
            cus.setIsDelete(rs.getInt("isDelete"));
            cus.setDeleteBy(rs.getInt("DeleteBy"));
            cus.setDeletedAt(rs.getDate("DeletedAt"));

            return cus;
        }
        return null;
    }
    
    public void AddCustomer(Customers customer, int userid) {
        String sql = "INSERT INTO Customers (Name, Phone, Address, CreateAt, CreateBy, isDelete, Wallet) VALUES ( ?, ?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getAddress());
            ps.setDate(4, today);
            ps.setInt(5, userid);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ArrayList<Customers> getCustomersBySearch(String information) throws Exception {
        information = information.toLowerCase();
        ArrayList<Customers> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers"; // Lấy toàn bộ danh sách Customers

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Customers cs = new Customers();
                cs.setID(rs.getInt("ID"));
                cs.setWallet(rs.getInt("Wallet"));
                cs.setName(rs.getString("Name"));
                cs.setPhone(rs.getString("Phone"));
                cs.setAddress(rs.getString("Address"));
                cs.setCreateAt(rs.getDate("CreateAt"));
                cs.setUpdateAt(rs.getDate("UpdateAt"));
                cs.setCreateBy(rs.getInt("CreateBy"));
                cs.setIsDelete(rs.getInt("isDelete"));
                cs.setDeletedAt(rs.getDate("DeletedAt"));
                cs.setDeleteBy(rs.getInt("DeleteBy"));

                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(cs.getCreateBy());

                // Tạo một chuỗi chứa toàn bộ thông tin của customer
                String customerData = (cs.getWallet() + " "
                        + cs.getName() + " "
                        + cs.getPhone() + " "
                        + cs.getCreateAt() + " "
                        + cs.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + cs.getIsDelete() + " ");

                //Lấy thông tin người xóa nếu có
                if (cs.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(cs.getDeleteBy());
                    customerData += ("ban" + cs.getIsDelete() + " "
                            + cs.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                }else{
                    customerData += "active";
                }

                // Kiểm tra nếu information xuất hiện trong dữ liệu khách hàng
                if (customerData.toLowerCase().contains(information.toLowerCase())) {
                    customers.add(cs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public void setCustomerWallet(Customers customers){
        String sql = "UPDATE Customers SET Wallet = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, customers.getWallet());
            ps.setInt(2, customers.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        DAOCustomers dao = new DAOCustomers();
        dao.getAllCustomers();
        System.out.println(dao.getCustomersByID(3));
        System.out.println(dao.getAllCustomers());
        //dao.deleteCustomers(4, 1);
        //Customers cu = new Customers(3, 500, "Viet", 388258116, "vn", today, today, 0, 0, today, 0);
        //dao.updateCustomers(cu);

    }
}
