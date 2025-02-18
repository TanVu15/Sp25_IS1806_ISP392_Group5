/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;

import model.Zones;
import model.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

public class DAOZones {

    public static final DAOZones INSTANCE = new DAOZones();
    protected Connection connect;

    public DAOZones() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<Zones> getAllZones() {
        ArrayList<Zones> zones = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Zones ";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Zones z = new Zones();
                z.setID(rs.getInt("ID"));
                z.setZoneName(rs.getString("ZoneName"));
                z.setCreateAt(rs.getDate("CreateAt"));
                z.setUpdateAt(rs.getDate("UpdateAt"));
                z.setCreateBy(rs.getInt("CreateBy"));
                z.setIsDelete(rs.getInt("isDelete"));
                z.setDeletedAt(rs.getDate("DeletedAt"));
                z.setDeleteBy(rs.getInt("DeleteBy"));
                zones.add(z);

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return zones;
    }

    public void deleteZones(int deleteid, int userid) {
        String sql = "UPDATE Zones SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE id = ?";
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

    public void updateZones(Zones zones) {
        String sql = "UPDATE Zones SET ZoneName = ?,UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, zones.getZoneName());
            ps.setDate(2, today);
            ps.setInt(3, zones.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Zones getZonesByID(int ID) throws Exception {
        String query = "SELECT * FROM Zones WHERE ID=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Zones z = new Zones();
            z.setID(rs.getInt("ID"));
            z.setZoneName(rs.getString("ZoneName"));
            z.setCreateAt(rs.getDate("CreateAt"));
            z.setUpdateAt(rs.getDate("UpdateAt"));
            z.setCreateBy(rs.getInt("CreateBy"));
            z.setIsDelete(rs.getInt("isDelete"));
            z.setDeleteBy(rs.getInt("DeleteBy"));
            z.setDeletedAt(rs.getDate("DeletedAt"));

            return z;
        }
        return null;
    }

    public void AddZone(Zones zone, int userid) {
        String sql = "INSERT INTO Zones (ZoneName,CreateAt, CreateBy, isDelete) VALUES ( ?, ?, ?, ?) ";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, zone.getZoneName());
            ps.setDate(2, today);
            ps.setInt(3, userid);
            ps.setInt(4, 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public ArrayList<Zones> getZonesBySearch(String information) throws Exception {
        information = information.toLowerCase();
        ArrayList<Zones> zones = new ArrayList<>();
        String sql = "SELECT * FROM Zones"; // Lấy toàn bộ danh sách Zones

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Zones z = new Zones();
                z.setID(rs.getInt("ID"));
                z.setZoneName(rs.getString("ZoneName"));
                z.setCreateAt(rs.getDate("CreateAt"));
                z.setUpdateAt(rs.getDate("UpdateAt"));
                z.setCreateBy(rs.getInt("CreateBy"));
                z.setIsDelete(rs.getInt("isDelete"));
                z.setDeletedAt(rs.getDate("DeletedAt"));
                z.setDeleteBy(rs.getInt("DeleteBy"));

                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(z.getCreateBy());

                // Tạo một chuỗi chứa toàn bộ thông tin của zone
                String zoneData = (z.getZoneName() + " "
                        + z.getCreateAt() + " "
                        + z.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + z.getIsDelete() + " ");

                //Lấy thông tin người xóa nếu có
                if (z.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(z.getDeleteBy());
                    zoneData += ("ban" + z.getIsDelete() + " "
                            + z.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                } else {
                    zoneData += "active";
                }

                // Kiểm tra nếu information xuất hiện trong dữ liệu sản phẩm
                if (zoneData.toLowerCase().contains(information.toLowerCase())) {
                    zones.add(z);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
    }


        public static void main(String[] args) throws Exception {
            //DAOZones dao = new DAOZones();
            //ArrayList<Zones> zonesList = new DAOZones().getAllZones();

            //for (Zones zone : zonesList) {
            //    System.out.println(zone);
            //}
            //dao.deleteZones(3, 1);
            //Zones zone = dao.getZonesByID(1);
        //if (zone != null) {
           // zone.setZoneName("Zone A Updated");
           // zone.setWarehouseID(2);
           // dao.updateZones(zone);
        //}
        //System.out.println(dao.getZonesByID(2));
        DAOZones daoZones = new DAOZones();
        String searchKeyword = "Zone B"; // Từ khóa tìm kiếm

        ArrayList<Zones> searchResults = daoZones.getZonesBySearch(searchKeyword);

        for (Zones z : searchResults) {
            System.out.println("ID: " + z.getID());
            System.out.println("Zone Name: " + z.getZoneName());
  
            System.out.println("Created At: " + z.getCreateAt());
            System.out.println("Updated At: " + z.getUpdateAt());
            System.out.println("Created By: " + z.getCreateBy());
            System.out.println("Is Deleted: " + z.getIsDelete());
            System.out.println("Deleted By: " + z.getDeleteBy());
            System.out.println("Deleted At: " + z.getDeletedAt());
            System.out.println("------------------------");
        }
    
        
        }

}

