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
    public static final DAO INSTANCE = new DAO();
    protected Connection connect;

    public DAOShops() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);
    
    public void createShop(Shops shop, int userid) {
        String sql = "INSERT INTO Shops (OwnerID, ShopName, LogoShop, Location, Email, CreateAt, CreateBy, isDelete) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, shop.getOwnerID());
            ps.setString(2, shop.getShopName());
            ps.setString(3, shop.getLogoShop());
            ps.setString(4, shop.getLocation());
            ps.setString(5, shop.getEmail());
            ps.setDate(6, today);
            ps.setInt(7, userid);
            ps.setInt(8, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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
            shop.setEmail(rs.getString("Email"));
            shop.setCreatedAt(rs.getDate("CreatedAt"));
            shop.setCreatedBy(rs.getInt("CreateBy"));
            shop.setIsDelete(rs.getInt("isDelete"));
            shop.setDeleteBy(rs.getInt("DeleteBy"));
            shop.setDeletedAt(rs.getDate("DeletedAt"));

            return shop;
        }
        return null;
    }
    
    public void updateShop(Shops shop) {
        String sql = "UPDATE Shops SET ShopName = ?,LogoShop = ? ,Location = ?, Email = ? , UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getLogoShop());
            ps.setString(3, shop.getLocation());
            ps.setString(4, shop.getEmail());
            ps.setDate(5, today);
            ps.setInt(6, shop.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
