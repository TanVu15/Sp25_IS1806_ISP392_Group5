package dal;

import model.Products;
import model.Zones; // Nhập lớp Zones
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOProducts {

    public static final DAOProducts INSTANCE = new DAOProducts();
    protected Connection connect;

    public DAOProducts() {
        connect = new DBContext().connect; // Khởi tạo kết nối
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<Products> getAllProducts() {
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT p.ID, p.ProductName, p.Description, p.Price, p.Quantity, p.ImageLink, " +
                     "p.CreateAt, p.UpdateAt, p.CreateBy, p.isDelete " +
                     "FROM Products p WHERE p.isDelete = 0";

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setImageLink(rs.getString("ImageLink"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setPrice(rs.getInt("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setCreateAt(rs.getDate("CreateAt"));
                product.setUpdateAt(rs.getDate("UpdateAt"));
                product.setCreateBy(rs.getInt("CreateBy"));
                product.setIsDelete(rs.getInt("isDelete"));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Zones> getZonesByProductId(int productId) {
        ArrayList<Zones> zones = new ArrayList<>();
        String sql = "SELECT z.ID, z.ZoneName FROM Zones z " +
                     "JOIN ProductZones pz ON z.ID = pz.ZoneID " +
                     "WHERE pz.ProductID = ?";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Zones zone = new Zones();
                zone.setID(rs.getInt("ID"));
                zone.setZoneName(rs.getString("ZoneName"));
                zones.add(zone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
    }

    public void deleteProducts(int deleteid, int userid) {
        String sql = "UPDATE Products SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 1); // Đánh dấu sản phẩm là đã xóa
            ps.setInt(2, userid); // Ghi nhận ID người dùng đã xóa
            ps.setDate(3, today); // Lưu thời gian xóa
            ps.setInt(4, deleteid); // ID sản phẩm cần xóa
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProducts(Products product) {
        String sql = "UPDATE Products SET ProductName = ?, Description = ?, Price = ?, Quantity = ?, UpdateAt = ?, ImageLink = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setDate(5, today);
            ps.setString(6, product.getImageLink());
            ps.setInt(7, product.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addProducts(Products product, int userid) {
        String sql = "INSERT INTO Products (ImageLink, ProductName, Description, Price, Quantity, CreateAt, CreateBy, UpdateAt, isDelete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getImageLink());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getPrice());
            ps.setInt(5, product.getQuantity());
            ps.setDate(6, today);
            ps.setInt(7, userid);
            ps.setDate(8, today); // Cập nhật thời gian
            ps.setInt(9, 0); // Đánh dấu là chưa bị xóa
            ps.executeUpdate();

            // Lấy ID của sản phẩm vừa thêm
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newProductId = generatedKeys.getInt(1); // Lưu ID sản phẩm mới thêm
                
                // Thêm thông tin vào bảng ProductZones
                addProductZones(newProductId, product.getProductZone());

                return newProductId; // Trả về ID sản phẩm mới thêm
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu có lỗi
    }

    public void addProductZones(int productId, Zones zone) {
        if (zone != null) {
            String zoneSql = "INSERT INTO ProductZones (ProductID, ZoneID) VALUES (?, ?)";
            try (PreparedStatement zonePs = connect.prepareStatement(zoneSql)) {
                zonePs.setInt(1, productId);
                zonePs.setInt(2, zone.getID()); // Lưu ID khu vực
                zonePs.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

public void clearProductZones(int productId) {
    // Thực hiện xóa tất cả các khu vực liên kết với sản phẩm này
    String sql = "DELETE FROM ProductZones WHERE ProductID = ?"; // Sửa tên cột cho đúng
    try (PreparedStatement stmt = connect.prepareStatement(sql)) {
        stmt.setInt(1, productId);
        stmt.executeUpdate(); // Thực hiện câu lệnh xóa
    } catch (SQLException e) {
        e.printStackTrace(); // In ra lỗi nếu có
    }
}
    
    public Products getProductByID(int ID) throws Exception {
        String query = "SELECT * FROM Products WHERE ID=?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Products p = new Products();
                p.setID(rs.getInt("ID"));
                p.setProductName(rs.getString("ProductName"));
                p.setImageLink(rs.getString("ImageLink"));
                p.setDescription(rs.getString("Description"));
                p.setPrice(rs.getInt("Price"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setCreateAt(rs.getDate("CreateAt"));
                p.setUpdateAt(rs.getDate("UpdateAt"));
                p.setCreateBy(rs.getInt("CreateBy"));
                p.setIsDelete(rs.getInt("isDelete"));
                p.setDeleteBy(rs.getInt("DeleteBy"));
                p.setDeletedAt(rs.getDate("DeletedAt"));
                return p;
            }
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }
    
    public int getTotalProducts() {
    String sql = "SELECT COUNT(*) FROM Products WHERE isDelete = 0";
    try (PreparedStatement ps = connect.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}
    
    public ArrayList<Products> getProductsByPage(int page, int productsPerPage) {
    ArrayList<Products> products = new ArrayList<>();
    String sql = "SELECT * FROM Products WHERE isDelete = 0 ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    
    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setInt(1, (page - 1) * productsPerPage);
        ps.setInt(2, productsPerPage);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Products product = new Products();
            product.setID(rs.getInt("ID"));
            product.setProductName(rs.getString("ProductName"));
            product.setDescription(rs.getString("Description"));
            product.setPrice(rs.getInt("Price"));
            product.setQuantity(rs.getInt("Quantity"));
            product.setImageLink(rs.getString("ImageLink"));
            product.setCreateAt(rs.getDate("CreateAt"));
            product.setUpdateAt(rs.getDate("UpdateAt"));
            product.setCreateBy(rs.getInt("CreateBy"));
            product.setIsDelete(rs.getInt("isDelete"));
            products.add(product);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return products;
}

    public ArrayList<Products> getProductsBySearch(String information) throws Exception {
        information = information.toLowerCase();
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE isDelete = 0"; // Lấy sản phẩm chưa bị xóa

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setImageLink(rs.getString("ImageLink"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setPrice(rs.getInt("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setCreateAt(rs.getDate("CreateAt"));
                product.setUpdateAt(rs.getDate("UpdateAt"));
                product.setCreateBy(rs.getInt("CreateBy"));
                product.setIsDelete(rs.getInt("isDelete"));
                product.setDeletedAt(rs.getDate("DeletedAt"));
                product.setDeleteBy(rs.getInt("DeleteBy"));

                // Tạo chuỗi chứa tất cả thông tin sản phẩm
                String productData = (product.getImageLink() + product.getProductName() + " "
                        + product.getDescription() + " "
                        + product.getPrice() + " "
                        + product.getQuantity() + " "
                        + product.getCreateAt() + " "
                        + product.getUpdateAt() + " ");

                // Kiểm tra xem thông tin có nằm trong dữ liệu sản phẩm không
                if (productData.toLowerCase().contains(information)) {
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void main(String[] args) throws Exception {
        DAOProducts dao = DAOProducts.INSTANCE;
        ArrayList<Products> productList = dao.getAllProducts();
            
        int userId = 1; // Giả sử đây là ID của người dùng đang thêm sản phẩm

        int currentPage = 1; // Bạn có thể thay đổi giá trị này để kiểm tra các trang khác
    int productsPerPage = 5; // Số sản phẩm trên mỗi trang

    ArrayList<Products> products = dao.getProductsByPage(currentPage, productsPerPage);
    
    // Kiểm tra danh sách sản phẩm
    if (products != null && !products.isEmpty()) {
        System.out.println("Danh sách sản phẩm trên trang " + currentPage + ":");
        for (Products product : products) {
            System.out.println("ID: " + product.getID() + ", Tên: " + product.getProductName() + ", Giá: " 
                + product.getPrice() + " VND, Số lượng: " + product.getQuantity());
        }
    } else {
        System.out.println("Không có sản phẩm nào để hiển thị trên trang " + currentPage);
    }
        
    }
}