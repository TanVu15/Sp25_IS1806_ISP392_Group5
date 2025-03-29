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
                        + o.getStatus() + " "
                        + o.getCreateAt() + " "
                        + o.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + o.getIsDelete());
                if(o.getStatus()==1){
                    orderData+="Nhập hàng ";
                }
                if(o.getStatus()== -1){
                    orderData+="Bán hàng ";
                }
                
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
    
    public int getTotalOrdersByShopId(int shopId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE ShopID = ?";
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

    public ArrayList<Orders> getOrdersByPage(int page, int ordersPerPage, int shopId) {
        ArrayList<Orders> order = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE ShopID = ? ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, (page - 1) * ordersPerPage);
            ps.setInt(3, ordersPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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
                order.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }
    
    public ArrayList<Orders> getOrdersBySearch(String information, Integer customerID) throws Exception {
    information = information.toLowerCase();
    ArrayList<Orders> orders = new ArrayList<>();
    String sql = "SELECT * FROM Orders";
    
    // Nếu có customerID, thêm điều kiện lọc vào SQL
    if (customerID != null) {
        sql += " WHERE CustomerID = ?";
    }

    try (PreparedStatement statement = connect.prepareStatement(sql)) {
        // Nếu có customerID, set giá trị cho câu lệnh SQL
        if (customerID != null) {
            statement.setInt(1, customerID);
        }

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Orders order = new Orders();
                order.setID(rs.getInt("ID"));
                order.setCustomerID(rs.getInt("CustomerID"));
                order.setTotalAmount(rs.getInt("TotalAmount"));
                order.setShopID(rs.getInt("ShopID"));
                order.setStatus(rs.getInt("Status"));
                order.setCreateAt(rs.getDate("CreateAt"));
                order.setUpdateAt(rs.getDate("UpdateAt"));
                order.setCreateBy(rs.getInt("CreateBy"));
                order.setIsDelete(rs.getInt("isDelete"));
                order.setDeletedAt(rs.getDate("deletedAt"));
                order.setDeleteBy(rs.getInt("deleteBy"));

                // Lấy thông tin người tạo
                Users userCreate = DAO.INSTANCE.getUserByID(order.getCreateBy());

                // Chuỗi dữ liệu đơn hàng để kiểm tra tìm kiếm
                String orderData = (order.getTotalAmount() + " "
                        + order.getStatus() + " "
                        + order.getCreateAt() + " "
                        + order.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + order.getIsDelete() + " ");

                // Nếu đơn hàng bị xóa, lấy thêm thông tin người xóa
                if (order.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(order.getDeleteBy());
                    orderData += ("xóa " + order.getDeletedAt() + " " + userDelete.getFullName().toLowerCase());
                } else {
                    orderData += "Hoạt Động";
                }

                // Kiểm tra nếu thông tin tìm kiếm xuất hiện trong dữ liệu đơn hàng
                if (orderData.toLowerCase().contains(information.toLowerCase())) {
                    orders.add(order);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return orders;
}


    public static void main(String[] args) throws Exception {
        DAOOrders dao = new DAOOrders();
         // Test 2: Tìm kiếm đơn hàng có chứa "2024" và thuộc về CustomerID = 5
            System.out.println("\nTest 2: Tìm kiếm đơn hàng có chứa '2024' của CustomerID = 5");
            ArrayList<Orders> result2 = dao.getOrdersBySearch("200", 1);
            for (Orders order : result2) {
                System.out.println(order);
            }
    }
}
