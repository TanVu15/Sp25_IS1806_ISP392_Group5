/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import static dal.DAOUser.today;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Shops;
import model.Users;

/**
 *
 * @author Admin
 */
public class DAOShops {
    public static final DAOShops INSTANCE = new DAOShops();
    protected Connection connect;

    public DAOShops() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);
    
    
    public void createShop(Shops shop, int userid) {
        String sql = "INSERT INTO Shops (OwnerID, ShopName, LogoShop, Location, Phone, BankAcc, Email, CreateAt, CreateBy, isDelete) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, userid);
            ps.setString(2, shop.getShopName());
            ps.setString(3, shop.getLogoShop());
            ps.setString(4, shop.getLocation());
            ps.setString(5, shop.getPhone());
            ps.setString(6, shop.getBankAcc());
            ps.setString(7, shop.getEmail());
            ps.setDate(8, today);
            ps.setInt(9, userid);
            ps.setInt(10, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        
        String sql2 = "UPDATE Users SET ShopID = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql2)) {
            ps.setInt(1, shop.getID());
            ps.setInt(2, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public Shops getShopByID(int ID) throws Exception {
        String query = "SELECT * FROM Shops WHERE ID = ? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Shops shop = new Shops();
            shop.setID(rs.getInt("ID"));
            shop.setOwnerID(rs.getInt("OwnerID"));
            shop.setShopName(rs.getString("ShopName"));
            shop.setLogoShop(rs.getString("LogoShop"));
            shop.setLocation(rs.getString("Location"));
            shop.setPhone(rs.getString("Phone"));
            shop.setBankAcc(rs.getString("BankAcc"));
            shop.setEmail(rs.getString("Email"));
            shop.setCreatedAt(rs.getDate("CreateAt"));
            shop.setCreatedBy(rs.getInt("CreateBy"));
            shop.setIsDelete(rs.getInt("isDelete"));
            shop.setDeleteBy(rs.getInt("DeleteBy"));
            shop.setDeletedAt(rs.getDate("DeletedAt"));

            return shop;
        }
        return null;
    }
    
    public Shops getShopByOwnerID(int OwnerID) throws Exception {
        String query = "SELECT * FROM Shops WHERE OwnerID = ? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, OwnerID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Shops shop = new Shops();
            shop.setID(rs.getInt("ID"));
            shop.setOwnerID(rs.getInt("OwnerID"));
            shop.setShopName(rs.getString("ShopName"));
            shop.setLogoShop(rs.getString("LogoShop"));
            shop.setLocation(rs.getString("Location"));
            shop.setPhone(rs.getString("Phone"));
            shop.setBankAcc(rs.getString("BankAcc"));
            shop.setEmail(rs.getString("Email"));
            shop.setCreatedAt(rs.getDate("CreateAt"));
            shop.setCreatedBy(rs.getInt("CreateBy"));
            shop.setIsDelete(rs.getInt("isDelete"));
            shop.setDeleteBy(rs.getInt("DeleteBy"));
            shop.setDeletedAt(rs.getDate("DeletedAt"));

            return shop;
        }
        return null;
    }
    
    public void updateShopbyOwnerid(Shops shop) {
        String sql = "UPDATE Shops SET ShopName = ?,LogoShop = ? ,Location = ?,Phone = ?,BankAcc = ?, Email = ? , UpdateAt = ? WHERE ownerid = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getLogoShop());
            ps.setString(3, shop.getLocation());
            ps.setString(4, shop.getPhone());
            ps.setString(5, shop.getBankAcc());
            ps.setString(6, shop.getEmail());
            ps.setDate(7, today);
            ps.setInt(8, shop.getOwnerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        DAOUser dao = new DAOUser();
        Shops shop = new Shops("a", "", "", "","","", 1);
        DAOShops.INSTANCE.updateShopbyOwnerid(shop);
    }
}
