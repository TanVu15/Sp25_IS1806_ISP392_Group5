package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import model.Zones;

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
        String sql = "SELECT * FROM Zones WHERE isDelete = 0"; // Only retrieve active zones

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Zones z = new Zones();
                z.setID(rs.getInt("ID"));
                z.setZoneName(rs.getString("ZoneName"));
                z.setShopID(rs.getInt("shopid"));
                z.setProductID(rs.getInt("ProductID")); // Set ProductID
                z.setCreateAt(rs.getDate("CreateAt"));
                z.setUpdateAt(rs.getDate("UpdateAt"));
                z.setCreateBy(rs.getInt("CreateBy"));
                z.setIsDelete(rs.getInt("isDelete"));
                z.setDeletedAt(rs.getDate("DeletedAt"));
                z.setDeleteBy(rs.getInt("DeleteBy"));
                zones.add(z);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving zones: " + e.getMessage());
        }
        return zones;
    }

    public void deleteZones(int deleteid, int userid) {
        String sql = "UPDATE Zones SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ps.setInt(2, userid);
            ps.setDate(3, today);
            ps.setInt(4, deleteid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting zone: " + e.getMessage());
        }
    }

    public void updateZones(Zones zones) {
        String sql = "UPDATE Zones SET ZoneName = ?, ProductID = ?, UpdateAt = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, zones.getZoneName());
            ps.setInt(2, zones.getProductID()); // Update ProductID
            ps.setDate(3, today);
            ps.setInt(4, zones.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating zone: " + e.getMessage());
        }
    }

    public Zones getZonesByID(int ID) {
        Zones z = null;
        String query = "SELECT * FROM Zones WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                z = new Zones();
                z.setID(rs.getInt("ID"));
                z.setZoneName(rs.getString("ZoneName"));
                z.setShopID(rs.getInt("shopid"));
                z.setProductID(rs.getInt("ProductID")); // Set ProductID
                z.setCreateAt(rs.getDate("CreateAt"));
                z.setUpdateAt(rs.getDate("UpdateAt"));
                z.setCreateBy(rs.getInt("CreateBy"));
                z.setIsDelete(rs.getInt("isDelete"));
                z.setDeleteBy(rs.getInt("DeleteBy"));
                z.setDeletedAt(rs.getDate("DeletedAt"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving zone by ID: " + e.getMessage());
        }
        return z;
    }

    public void addZone(Zones zone, int userid) {
        String sql = "INSERT INTO Zones (ZoneName, shopid, CreateAt, CreateBy, isDelete) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, zone.getZoneName());
            ps.setInt(2, zone.getShopID());
            ps.setDate(3, today);
            ps.setInt(4, userid);
            ps.setInt(5, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding zone: " + e.getMessage());
        }
    }

    public ArrayList<Zones> getZonesBySearch(String information) {
        ArrayList<Zones> zones = new ArrayList<>();
        String sql = "SELECT * FROM Zones WHERE LOWER(ZoneName) LIKE ? AND isDelete = 0"; // Search by zone name
        information = "%" + information.toLowerCase() + "%"; // Prepare search pattern

        try (PreparedStatement statement = connect.prepareStatement(sql)) {
            statement.setString(1, information);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Zones z = new Zones();
                z.setID(rs.getInt("ID"));
                z.setZoneName(rs.getString("ZoneName"));
                z.setShopID(rs.getInt("ShopID"));
                z.setProductID(rs.getInt("ProductID")); // Set ProductID
                z.setCreateAt(rs.getDate("CreateAt"));
                z.setUpdateAt(rs.getDate("UpdateAt"));
                z.setCreateBy(rs.getInt("CreateBy"));
                z.setIsDelete(rs.getInt("isDelete"));
                z.setDeletedAt(rs.getDate("DeletedAt"));
                z.setDeleteBy(rs.getInt("DeleteBy"));
                zones.add(z);
            }
        } catch (SQLException e) {
            System.err.println("Error searching zones: " + e.getMessage());
        }
        return zones;
    }
    
        public void updateZoneImportOrder(String zoneName,int ProductID, int shopid) {
        String sql = "UPDATE Zones SET ProductID = ? WHERE zoneName = ? And shopID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, ProductID);
            ps.setString(2, zoneName);
            ps.setInt(3, shopid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Khởi tạo DAOZones
        DAOZones daoZones = new DAOZones();

        // Tạo một zone mới để thêm vào database
        Zones newZone = new Zones();
        newZone.setZoneName("Test Zone");
        newZone.setShopID(4); // Giả sử ShopID là 1
        newZone.setProductID(3); // Giả sử ProductID là 101

        int userId = 4; // Giả sử user ID của người tạo là 1

        // Gọi hàm addZone để thêm zone mới
        daoZones.addZone(newZone, userId);
        System.out.println("Zone mới đã được thêm!");

        // Kiểm tra lại bằng cách lấy danh sách zones từ database
        ArrayList<Zones> zonesList = daoZones.getAllZones();
        for (Zones z : zonesList) {
            System.out.println("ID: " + z.getID() + ", Zone Name: " + z.getZoneName() + ", Product ID: " + z.getProductID());
        }
    }
}
