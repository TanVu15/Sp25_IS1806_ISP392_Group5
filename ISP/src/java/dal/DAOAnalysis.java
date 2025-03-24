/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author admin
 */
public class DAOAnalysis {
    public static final DAO INSTANCE = new DAO();
    protected Connection connect;

    public DAOAnalysis() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);
    //    For reporting:
    public int getTotalRevenue() {
        int totalRevenue = 0;
        String query = "SELECT COALESCE(SUM(TotalAmount), 0) AS DoanhThu FROM Orders WHERE isDelete = 0";
        try (PreparedStatement stmt = connect.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalRevenue = rs.getInt("DoanhThu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }
    
    public int getTotalOrders() {
        int totalOrders = 0;
        String query = "SELECT COUNT(*) AS SoDonHang FROM Orders WHERE isDelete = 0 and Status = -1";
        try (PreparedStatement stmt = connect.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalOrders = rs.getInt("SoDonHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalOrders;
    }
    
    public int getTotalStock() {
        int totalStock = 0;
        String query = "SELECT SUM(Quantity) AS TongSoLuong FROM Products WHERE IsDelete = 0";
        try (PreparedStatement stmt = connect.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalStock = rs.getInt("TongSoLuong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalStock;
    }
    public static void main(String[] args) {
        DAOAnalysis dao = new DAOAnalysis();
        System.out.println("Revenue: " + dao.getTotalRevenue());
        System.out.println("Orders: " + dao.getTotalOrders());
    }
    
}
