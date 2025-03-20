/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;

import model.Orders;
import model.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import model.Customers;
import java.sql.Statement;

public class DAOOrders {

    public static final DAOOrders INSTANCE = new DAOOrders();
    protected Connection connect;

    public DAOOrders() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<Orders> getAllOrders() {
        ArrayList<Orders> orders = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Orders ";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Orders o = new Orders();
                o.setID(rs.getInt("ID"));
                o.setCustomerID(rs.getInt("CustomerID"));
                o.setTotalAmount(rs.getFloat("TotalAmount"));
                o.setShopID(rs.getInt("ShopID"));
                o.setStatus(rs.getInt("status"));
                o.setCreateAt(rs.getDate("CreateAt"));
                o.setUpdateAt(rs.getDate("UpdateAt"));
                o.setCreateBy(rs.getInt("CreateBy"));
                o.setIsDelete(rs.getInt("isDelete"));
                o.setDeletedAt(rs.getDate("DeletedAt"));
                o.setDeleteBy(rs.getInt("DeleteBy"));
                orders.add(o);

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return orders;
    }

    public void addOrders(Orders orders, int userid) {
        String sql = "INSERT INTO Orders (CustomerID, TotalAmount, ShopID, Status, CreateAt, CreateBy) VALUES (?, ?, ?, ?, ?, ? ) ";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, orders.getCustomerID());
            ps.setFloat(2, orders.getTotalAmount());
            ps.setInt(3, orders.getShopID());
            ps.setInt(4, orders.getStatus());
            ps.setDate(5, today);
            ps.setInt(6, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int addOrdersreturnID(Orders orders, int userid) {
        String sql = "INSERT INTO Orders (CustomerID, TotalAmount, ShopID, Status, CreateAt, CreateBy) VALUES (?, ?, ?, ?, ?, ?)";
        int orderID = -1;

        try (PreparedStatement ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orders.getCustomerID());
            ps.setFloat(2, orders.getTotalAmount());
            ps.setInt(3, orders.getShopID());
            ps.setInt(4, orders.getStatus());
            ps.setDate(5, today);
            ps.setInt(6, userid);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    orderID = rs.getInt(1);
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderID;
    }

    

    public void updateOrders(Orders orders) {
        String sql = "UPDATE Orders SET CustomerID = ?, UserID = ?, OrderItemID = ?, TotalAmount = ?, ShopID = ?, Status = ?, UpdateAt = ?, ImageLink = ?, Location = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, orders.getCustomerID());
            ps.setDouble(4, orders.getTotalAmount());
            ps.setInt(5, orders.getShopID());
            ps.setInt(6, orders.getStatus());
            ps.setDate(7, today);
            ps.setInt(8, orders.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrders(int deleteid, int userid) {
        String sql = "UPDATE Orders SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE ID = ?";
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

    public Orders getOrderByID(int ID) throws Exception {
        String query = "SELECT * FROM Orders WHERE ID=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Orders o = new Orders();
            o.setID(rs.getInt("ID"));
            o.setCustomerID(rs.getInt("CustomerID"));
            o.setTotalAmount(rs.getInt("TotalAmount"));
            o.setShopID(rs.getInt("ShopID"));
            o.setStatus(rs.getInt("status"));
            o.setCreateAt(rs.getDate("CreateAt"));
            o.setUpdateAt(rs.getDate("UpdateAt"));
            o.setCreateBy(rs.getInt("CreateBy"));
            o.setIsDelete(rs.getInt("isDelete"));
            o.setDeleteBy(rs.getInt("DeleteBy"));
            o.setDeletedAt(rs.getDate("DeletedAt"));

            return o;
        }
        return null;
    }

    public ArrayList<Orders> getOrdersBySearch(String information) throws Exception {
        ArrayList<Orders> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders"; // Lấy toàn bộ dữ liệu từ bảng Orders

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Orders o = new Orders();
                o.setID(rs.getInt("ID"));
                o.setCustomerID(rs.getInt("CustomerID"));
                o.setTotalAmount(rs.getFloat("TotalAmount"));
                o.setShopID(rs.getInt("ShopID"));
                o.setStatus(rs.getInt("status"));
                o.setCreateAt(rs.getDate("CreateAt"));
                o.setUpdateAt(rs.getDate("UpdateAt"));
                o.setCreateBy(rs.getInt("CreateBy"));
                o.setIsDelete(rs.getInt("isDelete"));
                o.setDeleteBy(rs.getInt("DeleteBy"));
                o.setDeletedAt(rs.getDate("DeletedAt"));

                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(o.getCreateBy());

                Customers customerOr = DAOCustomers.INSTANCE.getCustomersByID(o.getCustomerID());
                String customerName = (customerOr != null) ? customerOr.getName() : "Unknown Customer";

                // Tạo một chuỗi chứa toàn bộ thông tin của order
                String orderData = (o.getCustomerID() + " "
                        + customerName + " "
                        + o.getShopID() + " "
                        + o.getCreateAt() + " "
                        + o.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + o.getIsDelete());

                // Lấy thông tin người xóa nếu có
                if (o.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(o.getDeleteBy());
                    orderData += ("xóa" + o.getIsDelete() + " "
                            + o.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                } else {
                    orderData += "Hoạt Động";
                }

                // Kiểm tra nếu information xuất hiện trong bất kỳ trường nào của order
                if (orderData.toLowerCase().contains(information.toLowerCase())) {
                    orders.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void main(String[] args) throws Exception {
        DAOOrders dao = new DAOOrders();
        System.out.println(dao.getAllOrders());
        System.out.println("================");
        //dao.addOrders(orders, 0);
        DAOCustomers cus = new DAOCustomers();

        int id = cus.getCustomerIdByNameAndShop("Nguyen Van D", 2);
        Orders orders = new Orders(2, id, 1000, 2, today, today, 3, 0, today, 0, 1);
        //dao.addOrders(orders, 3);
        System.out.println(dao.getOrderByID(1));
        Customers c = new Customers();
        System.out.println(cus.getCustomersByID(2).getName());
    }
}
