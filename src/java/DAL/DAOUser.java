/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Model.Users;
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

    public void Register(Users user, int userid) {
        String sql = "INSERT INTO Users (Username, passwordhash, roleid, CreateAt, CreateBy, isDelete) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setInt(3, user.getRoleid());
            ps.setDate(4, today);
            ps.setInt(5, userid);
            ps.setInt(6, 0);
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
                u.setCreateAt(rs.getDate("CreateAt"));
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
            ps.setString(1, user.getPasswordHash());
            ps.setString(2, user.getFullName());
            ps.setDate(3, today);
            ps.setInt(4, user.getID());
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
            user.setCreateAt(rs.getDate("CreateAt"));
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
                u.setCreateAt(rs.getDate("CreateAt"));
                u.setCreateBy(rs.getInt("CreateBy"));
                u.setIsDelete(rs.getInt("isDelete"));
                u.setDeleteBy(rs.getInt("DeleteBy"));
                u.setDeletedAt(rs.getDate("DeletedAt"));
                
                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(u.getCreateBy()); 

                // Chuyển thông tin của user thành một chuỗi
                String userData = (u.getUsername() + " "
                        + u.getPasswordHash() + " "
                        + u.getRoleid() + " "
                        + u.getFullName() + " "
                        + u.getCreateAt() + " "
                        + userCreate.getFullName().toLowerCase()+ " "
                        + u.getIsDelete() + " "
                        + u.getDeleteBy() + " "
                        + u.getDeletedAt()).toLowerCase(); // Chuyển về chữ thường để tránh phân biệt hoa/thường
                
                //Lấy thông tin người xóa nếu có
                if (u.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(u.getDeleteBy());
                    userData += ("ban" + u.getIsDelete() + " "
                            + u.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                }else{
                    userData += "active";
                }

                // Kiểm tra nếu information xuất hiện trong bất kỳ trường nào của user
                if (userData.contains(information.toLowerCase())) {
                    users.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void main(String[] args) throws Exception {
        DAOUser dao = new DAOUser();
//        Users admin = new Users(0, "Admin2", "1234", 1, "Hoangvanviet2", today, today, 0, 0, today, 0);
//        dao.Register(admin, 0);
        System.out.println(dao.getUsersBySearch("viet"));
        //Users user = new Users(3, "Admin", "admin", 1, "Hoangvanviet123", today, today, 0, 0, today, 0);
//        dao.updateUser(user);
//        System.out.println(dao.getUserByName("Admin2"));
        //dao.updateUser(user);
        dao.deleteUser(4, 2);

    }
}
