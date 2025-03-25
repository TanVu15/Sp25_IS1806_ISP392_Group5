package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;
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
    String sql = "UPDATE Zones SET IsDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE ID = ?";
    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setInt(1, 1); // Đánh dấu là đã xóa
        ps.setInt(2, userid); // Lưu thông tin người xóa
        ps.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Thời gian xóa
        ps.setInt(4, deleteid);
        ps.executeUpdate();
        System.out.println("Zone " + deleteid + " has been marked as deleted.");
    } catch (SQLException e) {
        System.err.println("Error deleting zone: " + e.getMessage());
    }
}


    public void updateZones(Zones zones) {
        String sql = "UPDATE Zones SET ZoneName = ?, UpdateAt = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, zones.getZoneName());
            ps.setDate(2, today);
            ps.setInt(3, zones.getID());
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

    public ArrayList<Zones> getZonesBySearch1(String information) {
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
        
        public ArrayList<Zones> getZonesBySearch(String information) throws Exception {
        // Chuẩn hóa từ khóa tìm kiếm (bỏ dấu, chuyển thành chữ thường, xóa khoảng trắng thừa)
        String normalizedSearch = Normalizer.normalize(information, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("đ", "d").replaceAll("Đ", "D")
                .toLowerCase().replaceAll("\\s+", " ").trim();

        // Tạo Regex: "khu 6" -> "khu.*6"
        String regex = normalizedSearch.replaceAll(" ", ".*");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        ArrayList<Zones> zones = new ArrayList<>();
        String sql = "SELECT * FROM Zones WHERE isDelete = 0";

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Zones z = new Zones();
                z.setID(rs.getInt("ID"));
                z.setShopID(rs.getInt("ShopID"));
                z.setZoneName(rs.getString("ZoneName"));
                z.setProductID(rs.getInt("ProductID"));
                z.setCreateAt(rs.getDate("CreateAt"));
                z.setUpdateAt(rs.getDate("UpdateAt"));
                z.setCreateBy(rs.getInt("CreateBy"));
                z.setIsDelete(rs.getInt("isDelete"));
                z.setDeletedAt(rs.getDate("DeletedAt"));
                z.setDeleteBy(rs.getInt("DeleteBy"));

                // Chuẩn hóa dữ liệu khu vực để tìm kiếm
                String zoneData = Normalizer.normalize(z.getZoneName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("đ", "d").replaceAll("Đ", "D")
                        .toLowerCase().replaceAll("\\s+", " ").trim();

                // Kiểm tra nếu dữ liệu khớp với regex
                if (pattern.matcher(zoneData).find()) {
                    zones.add(z);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
    }

    public int getTotalZonesByShopId(int shopId) {
        String sql = "SELECT COUNT(*) FROM Zones WHERE isDelete = 0 AND ShopID = ?";
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

    public ArrayList<Zones> getZonesByPage(int page, int zonesPerPage, int shopId) {
        ArrayList<Zones> zones = new ArrayList<>();
        String sql = "SELECT * FROM Zones WHERE isDelete = 0 AND ShopID = ? ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, (page - 1) * zonesPerPage);
            ps.setInt(3, zonesPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Zones zone = new Zones();
                zone.setID(rs.getInt("ID"));
                zone.setZoneName(rs.getString("ZoneName"));
                zone.setProductID(rs.getInt("ProductID"));
                zone.setShopID(rs.getInt("ShopID"));
                zone.setCreateAt(rs.getDate("CreateAt"));
                zone.setUpdateAt(rs.getDate("UpdateAt"));
                zone.setCreateBy(rs.getInt("CreateBy"));
                zone.setIsDelete(rs.getInt("isDelete"));
                zones.add(zone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
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
