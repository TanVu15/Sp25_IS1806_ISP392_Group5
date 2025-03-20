/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import static dal.DAO.today;
import model.Users;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOUser {

    public static final DAOUser INSTANCE = new DAOUser();
    protected Connection connect;

    public DAOUser() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public boolean authenticateUser(String username, String password) throws Exception {
        Users user = getUserByName(username);
        if (user != null) {
            return DAO.PasswordUtils.hashPassword(password).equals(user.getPasswordHash()); // So sánh mật khẩu nhập vào với mật khẩu đã lưu
        }
        return false;
    }

    public void Register(Users user, int userid) {
        String sql = "INSERT INTO Users (Username, passwordhash, roleid, CreateAt, CreateBy, isDelete, shopid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, DAO.PasswordUtils.hashPassword(user.getPasswordHash()));
            ps.setInt(3, user.getRoleid());
            ps.setDate(4, today);
            ps.setInt(5, userid);
            ps.setInt(6, 0);
            ps.setInt(7, user.getShopID());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ArrayList<Users> getUsers() {
        ArrayList<Users> users = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Users ";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Users u = new Users();
                u.setID(rs.getInt("ID"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("passwordhash"));
                u.setRoleid(rs.getInt("roleid"));
                u.setFullName(rs.getString("FullName"));
                u.setShopID(rs.getInt("ShopID"));
                u.setCreateAt(rs.getDate("CreateAt"));
                u.setUpdateAt(rs.getDate("UpdateAt"));
                u.setCreateBy(rs.getInt("CreateBy"));
                u.setIsDelete(rs.getInt("isDelete"));
                u.setDeleteBy(rs.getInt("DeleteBy"));
                u.setDeletedAt(rs.getDate("DeletedAt"));
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return users;
    }

    public void deleteUser(int deleteid, int userid) {
        String sql = "UPDATE Users SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE id = ?";
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

    public void updateUser(Users user) {
        String sql = "UPDATE Users SET passwordhash = ?,FullName = ? , UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, DAO.PasswordUtils.hashPassword(user.getPasswordHash()));
            ps.setString(2, user.getFullName());
            ps.setDate(3, today);
            ps.setInt(4, user.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserShopId(Users user) {
        String sql = "UPDATE Users SET ShopId = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, user.getShopID());
            ps.setInt(2, user.getID());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Users getUserByName(String name) throws Exception {
        String query = "SELECT * FROM Users WHERE username=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Users user = new Users();
            user.setID(rs.getInt("ID"));
            user.setUsername(rs.getString("Username"));
            user.setPasswordHash(rs.getString("PasswordHash"));
            user.setRoleid(rs.getInt("Roleid"));
            user.setFullName(rs.getString("FullName"));
            user.setShopID(rs.getInt("ShopID"));
            user.setCreateAt(rs.getDate("CreateAt"));
            user.setCreateBy(rs.getInt("CreateBy"));
            user.setIsDelete(rs.getInt("isDelete"));
            user.setDeleteBy(rs.getInt("DeleteBy"));
            user.setDeletedAt(rs.getDate("DeletedAt"));

            return user;
        }
        return null;
    }

    //check insert user
    public boolean checkUsernameExists(String username) {
        try {
            String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Users getUserByID(int ID) throws Exception {
        String query = "SELECT * FROM Users WHERE ID=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Users user = new Users();
            user.setID(rs.getInt("ID"));
            user.setUsername(rs.getString("Username"));
            user.setPasswordHash(rs.getString("PasswordHash"));
            user.setRoleid(rs.getInt("Roleid"));
            user.setFullName(rs.getString("FullName"));
            user.setShopID(rs.getInt("ShopID"));
            user.setCreateAt(rs.getDate("CreateAt"));
            user.setUpdateAt(rs.getDate("UpdateAt"));
            user.setCreateBy(rs.getInt("CreateBy"));
            user.setIsDelete(rs.getInt("isDelete"));
            user.setDeleteBy(rs.getInt("DeleteBy"));
            user.setDeletedAt(rs.getDate("DeletedAt"));

            return user;
        }
        return null;
    }

    public ArrayList<Users> getUsersBySearch(String information) throws Exception {
        ArrayList<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users"; // Lấy toàn bộ dữ liệu từ bảng Users

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Users u = new Users();
                u.setID(rs.getInt("ID"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("passwordhash"));
                u.setRoleid(rs.getInt("roleid"));
                u.setFullName(rs.getString("FullName"));
                u.setShopID(rs.getInt("ShopID"));
                u.setCreateAt(rs.getDate("CreateAt"));
                u.setUpdateAt(rs.getDate("UpdateAt"));
                u.setCreateBy(rs.getInt("CreateBy"));
                u.setIsDelete(rs.getInt("isDelete"));
                u.setDeleteBy(rs.getInt("DeleteBy"));
                u.setDeletedAt(rs.getDate("DeletedAt"));

                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(u.getCreateBy());

                // Chuyển thông tin của user thành một chuỗi
                String userData = (u.getUsername() + " "
                        + u.getPasswordHash() + " "
                        + u.getFullName() + " "
                        + u.getCreateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " ");

                //Lấy role 
                if (u.getRoleid() == 1) {
                    userData += "admin ";
                }
                if (u.getRoleid() == 2) {
                    userData += "owner ";
                }
                if (u.getRoleid() == 3) {
                    userData += "staff ";
                }

                //Lấy thông tin người xóa nếu có
                if (u.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(u.getDeleteBy());
                    userData += ("xóa" + u.getIsDelete() + " "
                            + u.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                } else {
                    userData += "Hoạt Động";
                }

                // Kiểm tra nếu information xuất hiện trong bất kỳ trường nào của user
                if (userData.toLowerCase().contains(information.toLowerCase())) {
                    users.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void resetPasswordUser(int userid) {
        String sql = "UPDATE Users SET passwordhash = ?, UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, DAO.PasswordUtils.hashPassword("12345678"));
            ps.setDate(2, today);
            ps.setInt(3, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getTotalUsersById() {
        String sql = "SELECT COUNT(*) FROM Users WHERE isDelete = 0 ";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public ArrayList<Users> getUsersByPageID(int page, int usersPerPage) {
        ArrayList<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE isDelete = 0 ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, (page - 1) * usersPerPage);
            ps.setInt(2, usersPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Users u = new Users();
                u.setID(rs.getInt("ID"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("passwordhash"));
                u.setRoleid(rs.getInt("roleid"));
                u.setFullName(rs.getString("FullName"));
                u.setShopID(rs.getInt("ShopID"));
                u.setCreateAt(rs.getDate("CreateAt"));
                u.setUpdateAt(rs.getDate("UpdateAt"));
                u.setCreateBy(rs.getInt("CreateBy"));
                u.setIsDelete(rs.getInt("isDelete"));
                u.setDeleteBy(rs.getInt("DeleteBy"));
                u.setDeletedAt(rs.getDate("DeletedAt"));

                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public int getTotalUsersByShopId(int shopId) {
        String sql = "SELECT COUNT(*) FROM Users WHERE isDelete = 0 AND ShopID = ?";
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

    public ArrayList<Users> getUsersByPage(int page, int usersPerPage, int shopId) {
        ArrayList<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE isDelete = 0 AND ShopID = ? ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, (page - 1) * usersPerPage);
            ps.setInt(3, usersPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Users u = new Users();
                u.setID(rs.getInt("ID"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("passwordhash"));
                u.setRoleid(rs.getInt("roleid"));
                u.setFullName(rs.getString("FullName"));
                u.setShopID(rs.getInt("ShopID"));
                u.setCreateAt(rs.getDate("CreateAt"));
                u.setUpdateAt(rs.getDate("UpdateAt"));
                u.setCreateBy(rs.getInt("CreateBy"));
                u.setIsDelete(rs.getInt("isDelete"));
                u.setDeleteBy(rs.getInt("DeleteBy"));
                u.setDeletedAt(rs.getDate("DeletedAt"));

                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    
    public ArrayList<Users> sortUserByNewTime(ArrayList<Users> listOld) {
        int n = listOld.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // So sánh ngày tạo (mới hơn đứng trước)
                if (listOld.get(j).getCreateAt().before(listOld.get(j + 1).getCreateAt())) {
                    // Hoán đổi vị trí
                    Users temp = listOld.get(j);
                    listOld.set(j, listOld.get(j + 1));
                    listOld.set(j + 1, temp);
                }
            }
        }
        return listOld;
    }

    public static void main(String[] args) throws Exception {
        DAOUser dao = new DAOUser();
//        Users admin = new Users(0, "Admin2", "1234", 1, "Hoangvanviet2", today, today, 0, 0, today, 0);
//        dao.Register(admin, 0);
        //System.out.println(dao.getUsersBySearch("viet"));
        //Users user = new Users(3, "Admin", "admin", 1, "Hoangvanviet123", today, today, 0, 0, today, 0);
//        dao.updateUser(user);
//        System.out.println(dao.getUserByName("Admin2"));
        //dao.updateUser(user);
//        dao.deleteUser(4, 2);
//        Users newU = new Users();
//        newU.setID(4);
//        newU.setShopID(4);
//        dao.updateUserShopId(newU);
    }
}
