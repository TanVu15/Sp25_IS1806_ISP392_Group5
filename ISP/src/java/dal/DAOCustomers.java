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
import java.util.List;
import model.Orders;

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
                cs.setShopID(rs.getInt("ShopID"));
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
            cus.setShopID(rs.getInt("ShopID"));
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

    public void AddCustomer(Customers customer, int userid) throws Exception {
        String sql = "INSERT INTO Customers (Name, Phone, Address, CreateAt, CreateBy, isDelete, Wallet, shopid) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getAddress());
            ps.setDate(4, today);
            ps.setInt(5, userid);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setInt(8, customer.getShopID());
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
                cs.setShopID(rs.getInt("ShopID"));
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
                        + cs.getAddress()+ " "
                        + cs.getCreateAt() + " "
                        + cs.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + cs.getIsDelete() + " ");

                //Lấy thông tin người xóa nếu có
                if (cs.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(cs.getDeleteBy());
                    customerData += ("xóa" + cs.getIsDelete() + " "
                            + cs.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                } else {
                    customerData += "Hoạt Động";
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

    public int getTotalCustomersByShopId(int shopId) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE ShopID = ?";
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

    public ArrayList<Customers> getCustomersByPage(int page, int productsPerPage, int shopId) {
        ArrayList<Customers> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers WHERE ShopID = ? ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, (page - 1) * productsPerPage);
            ps.setInt(3, productsPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customers cs = new Customers();
                cs.setID(rs.getInt("ID"));
                cs.setWallet(rs.getInt("Wallet"));
                cs.setName(rs.getString("Name"));
                cs.setPhone(rs.getString("Phone"));
                cs.setAddress(rs.getString("Address"));
                cs.setShopID(rs.getInt("ShopID"));
                cs.setCreateAt(rs.getDate("CreateAt"));
                cs.setUpdateAt(rs.getDate("UpdateAt"));
                cs.setCreateBy(rs.getInt("CreateBy"));
                cs.setIsDelete(rs.getInt("isDelete"));
                cs.setDeletedAt(rs.getDate("DeletedAt"));
                cs.setDeleteBy(rs.getInt("DeleteBy"));
                customers.add(cs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    public int getCustomerIdByNameAndShop(String name, int shopId) {
    String sql = "SELECT ID FROM Customers WHERE Name = ? AND ShopID = ? AND isDelete = 0";
    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setInt(2, shopId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("ID"); // Trả về ID
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Không tìm thấy thì trả về -1
}

// Order of customer
     public List<Orders> getOrdersByCustomerId(int customerId) {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE CustomerID = ?";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
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
                    ordersList.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersList;
     }
     
     public List<Orders> getOrdersByCustomerId(int customerId, int currentPage, int ordersPerPage) {
    List<Orders> ordersList = new ArrayList<>();
    String sql = "SELECT * FROM Orders WHERE CustomerID = ? ORDER BY CreateAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setInt(1, customerId);
        ps.setInt(2, (currentPage - 1) * ordersPerPage);
        ps.setInt(3, ordersPerPage);

        try (ResultSet rs = ps.executeQuery()) {
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
                ordersList.add(order);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return ordersList;
}

     public int getTotalOrdersByCustomerId(int customerId) {
    String sql = "SELECT COUNT(*) FROM Orders WHERE CustomerID = ?";
    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

    public static void main(String[] args) throws Exception {
         DAOCustomers dao = new DAOCustomers();
    int customerId = 1; // Thay bằng ID thực tế từ cơ sở dữ liệu của bạn
    int currentPage = 2;
    int ordersPerPage = 5; // Số đơn hàng mỗi trang

    // Kiểm tra tổng số đơn hàng của khách hàng
    int totalOrders = dao.getTotalOrdersByCustomerId(customerId);
    System.out.println("Tổng số đơn hàng của khách hàng có ID " + customerId + ": " + totalOrders);

    // Kiểm tra danh sách đơn hàng theo phân trang
    List<Orders> paginatedOrders = dao.getOrdersByCustomerId(customerId, currentPage, ordersPerPage);

    // Kiểm tra và in ra kết quả
    if (paginatedOrders.isEmpty()) {
        System.out.println("Không tìm thấy đơn hàng nào cho khách hàng có ID = " + customerId);
    } else {
        System.out.println("Danh sách đơn hàng cho khách hàng ID = " + customerId + " (Trang " + currentPage + "):");
        for (Orders order : paginatedOrders) {
            System.out.println("Order ID: " + order.getID());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Created At: " + order.getCreateAt());
            System.out.println("===================================");
        }
    }
    }
}
