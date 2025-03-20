/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.OrderItems;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class DAOOrderItem {

    public static final DAOOrderItem INSTANCE = new DAOOrderItem();
    protected Connection connect;

    public DAOOrderItem() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<OrderItems> getAllOrderItem() {
        ArrayList<OrderItems> orderItemses = new ArrayList<>();
        String sql = "SELECT * FROM OrderItems";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                OrderItems oi = new OrderItems();
                oi.setID(rs.getInt("ID"));
                oi.setOrderID(rs.getInt("OrderID"));
                oi.setProductName(rs.getString("ProductName"));
                oi.setDescription(rs.getString("Description"));
                oi.setPrice(rs.getInt("Price"));
                oi.setQuantity(rs.getInt("Quantity"));
                oi.setUnitPrice(rs.getInt("UnitPrice"));
                oi.setShopID(rs.getInt("ShopID"));
                oi.setCreateAt(rs.getDate("CreateAt"));
                oi.setUpdateAt(rs.getDate("UpdateAt"));
                oi.setCreateBy(rs.getInt("CreateBy"));
                oi.setIsDelete(rs.getInt("isDelete"));
                oi.setDeletedAt(rs.getDate("DeletedAt"));
                oi.setDeleteBy(rs.getInt("DeleteBy"));
                orderItemses.add(oi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItemses;
    }

    public ArrayList<OrderItems> getAllOrderItemsByOrderID(int ID) throws Exception {
        ArrayList<OrderItems> orderItemsList = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE OrderID=?";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            OrderItems oi = new OrderItems();
            oi.setID(rs.getInt("ID"));
            oi.setOrderID(rs.getInt("OrderID"));
            oi.setProductName(rs.getString("ProductName"));
            oi.setDescription(rs.getString("Description"));
            oi.setPrice(rs.getInt("Price"));
            oi.setQuantity(rs.getInt("Quantity"));
            oi.setUnitPrice(rs.getInt("UnitPrice"));
            oi.setShopID(rs.getInt("ShopID"));
            oi.setCreateAt(rs.getDate("CreateAt"));
            oi.setUpdateAt(rs.getDate("UpdateAt"));
            oi.setCreateBy(rs.getInt("CreateBy"));
            oi.setIsDelete(rs.getInt("isDelete"));
            oi.setDeletedAt(rs.getDate("DeletedAt"));
            oi.setDeleteBy(rs.getInt("DeleteBy"));

            orderItemsList.add(oi);
        }
        return orderItemsList;
    }

//    public void deleteProducts(int deleteid, int userid) {
//        String sql = "UPDATE Products SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE ID = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setInt(1, 1); // Đánh dấu sản phẩm là đã bị xóa
//            ps.setInt(2, userid); // Ghi lại ID của người xóa
//            ps.setDate(3, today); // Lưu thời gian xóa
//            ps.setInt(4, deleteid); // ID sản phẩm cần xóa
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    public void updateOrderItems(OrderItems orderitems) {
//        String sql = "UPDATE OrderItems SET ProductName = ?, Description = ?, Price = ?, Quantity = ?, UpdateAt = ?, ImageLink = ?, Location = ? WHERE ID = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setString(1, product.getProductName());
//            ps.setString(2, product.getDescription());
//            ps.setDouble(3, product.getPrice());
//            ps.setInt(4, product.getQuantity());
//            ps.setDate(5, today);
//            ps.setString(6, product.getImageLink()); // Sửa tên trường
//            ps.setString(7, product.getLocation());
//            ps.setInt(8, product.getID());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public void AddOrderItems(OrderItems orderitems, int userid) {
        String sql = "INSERT INTO OrderItems (OrderID, ProductName, Description, Price, Quantity, UnitPrice, shopID, CreateAt, CreateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, orderitems.getOrderID());
            ps.setString(2, orderitems.getProductName());
            ps.setString(3, orderitems.getDescription());
            ps.setInt(4, orderitems.getPrice());
            ps.setInt(5, orderitems.getQuantity());
            ps.setInt(6, orderitems.getUnitPrice());
            ps.setInt(7, orderitems.getShopID());
            ps.setDate(8, today);
            ps.setInt(9, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public OrderItems getOrderItemByID(int ID) throws Exception {
        String query = "SELECT * FROM OrderItems WHERE ID=?";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            OrderItems oi = new OrderItems();
            oi.setID(rs.getInt("ID"));
            oi.setOrderID(rs.getInt("OrderID"));
            oi.setProductName(rs.getString("ProductName"));
            oi.setDescription(rs.getString("Description"));
            oi.setPrice(rs.getInt("Price"));
            oi.setQuantity(rs.getInt("Quantity"));
            oi.setUnitPrice(rs.getInt("UnitPrice"));
            oi.setShopID(rs.getInt("ShopID"));
            oi.setCreateAt(rs.getDate("CreateAt"));
            oi.setUpdateAt(rs.getDate("UpdateAt"));
            oi.setCreateBy(rs.getInt("CreateBy"));
            oi.setIsDelete(rs.getInt("isDelete"));
            oi.setDeletedAt(rs.getDate("DeletedAt"));
            oi.setDeleteBy(rs.getInt("DeleteBy"));
            return oi;
        }
        return null;
    }

    public ArrayList<OrderItems> getOrderItemBySearch(String information) throws Exception {
        information = information.toLowerCase();
        ArrayList<OrderItems> orderItemses = new ArrayList<>();
        String sql = "SELECT * FROM OrderItems";

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                OrderItems oi = new OrderItems();
                oi.setID(rs.getInt("ID"));
                oi.setOrderID(rs.getInt("OrderID"));
                oi.setProductName(rs.getString("ProductName"));
                oi.setDescription(rs.getString("Description"));
                oi.setPrice(rs.getInt("Price"));
                oi.setQuantity(rs.getInt("Quantity"));
                oi.setUnitPrice(rs.getInt("UnitPrice"));
                oi.setShopID(rs.getInt("ShopID"));
                oi.setCreateAt(rs.getDate("CreateAt"));
                oi.setUpdateAt(rs.getDate("UpdateAt"));
                oi.setCreateBy(rs.getInt("CreateBy"));
                oi.setIsDelete(rs.getInt("isDelete"));
                oi.setDeletedAt(rs.getDate("DeletedAt"));
                oi.setDeleteBy(rs.getInt("DeleteBy"));

                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(oi.getCreateBy());

                // Tạo một chuỗi chứa toàn bộ thông tin của product
                String orderData = (oi.getProductName() + " "
                        + oi.getDescription().toLowerCase() + " "
                        + oi.getPrice() + " "
                        + oi.getQuantity() + " "
                        + oi.getUnitPrice() + " "
                        + oi.getShopID() + " "
                        + oi.getCreateAt() + " "
                        + oi.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + oi.getIsDelete() + " ");

                // Lấy thông tin người xóa nếu có
                if (oi.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(oi.getDeleteBy());
                    orderData += ("xóa" + oi.getIsDelete() + " "
                            + oi.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                } else {
                    orderData += "Hoạt Động";
                }

                // Kiểm tra nếu information xuất hiện trong dữ liệu sản phẩm
                if (orderData.toLowerCase().contains(information.toLowerCase())) {
                    orderItemses.add(oi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItemses;
    }
    
    public ArrayList<OrderItems> searchProductByOrderID(int orderId, String productName) {
        ArrayList<OrderItems> orderItemsList = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE OrderID = ? AND LOWER(ProductName) LIKE ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setString(2, "%" + productName.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItems oi = new OrderItems();
                oi.setID(rs.getInt("ID"));
                oi.setOrderID(rs.getInt("OrderID"));
                oi.setProductName(rs.getString("ProductName"));
                oi.setDescription(rs.getString("Description"));
                oi.setPrice(rs.getInt("Price"));
                oi.setQuantity(rs.getInt("Quantity"));
                oi.setUnitPrice(rs.getInt("UnitPrice"));
                oi.setShopID(rs.getInt("ShopID"));
                oi.setCreateAt(rs.getDate("CreateAt"));
                oi.setUpdateAt(rs.getDate("UpdateAt"));
                oi.setCreateBy(rs.getInt("CreateBy"));
                oi.setIsDelete(rs.getInt("isDelete"));
                oi.setDeletedAt(rs.getDate("DeletedAt"));
                oi.setDeleteBy(rs.getInt("DeleteBy"));

                orderItemsList.add(oi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItemsList;
    }


    public static void main(String[] args) throws Exception {
        DAOOrderItem dao = new DAOOrderItem();
        System.out.println(dao.getAllOrderItem());
        OrderItems information = new OrderItems(3, 2, "rice", "kg", 0, 0, 0, 0, today, today, 2, 0, today, 0);
        System.out.println(dao.getOrderItemBySearch("gao BC"));
        dao.getOrderItemByID(1);
        //dao.AddOrderItems(information, 2);
        System.out.println(dao.getAllOrderItemsByOrderID(2));
        dao.AddOrderItems(information, 2);
    }
}
