package dal;

import jakarta.servlet.http.HttpServletRequest;
import model.Products;
import model.Zones; // Nhập lớp Zones
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ProductPriceHistory;
import model.Users;

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
        String sql = "SELECT * "
                + "FROM Products p WHERE p.isDelete = 0";

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setImageLink(rs.getString("ImageLink"));
                product.setShopID(rs.getInt("ShopID"));
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
        String sql = "SELECT z.ID, z.ZoneName FROM Zones z WHERE z.ProductID = ?";

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
        String sql = "INSERT INTO Products (ImageLink, ProductName, Description, Price, Quantity, CreateAt, CreateBy, UpdateAt, isDelete, ShopID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getImageLink());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getPrice());
            ps.setInt(5, product.getQuantity());
            ps.setDate(6, today);
            ps.setInt(7, userid);
            ps.setDate(8, today);
            ps.setInt(9, 0);
            ps.setInt(10, product.getShopID());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return new product ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if error occurs
    }

    public void updateZoneWithProduct(int zoneId, int productId) {
        String sql = "UPDATE Zones SET ProductID = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, zoneId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductZones(int productId, String[] zoneIDs) {
        // Here you can implement logic to update the zones directly associated with the product
        // For example, you can set the ProductID in the Zones table for the selected zone IDs

        for (String zoneID : zoneIDs) {
            int zoneIdInt = Integer.parseInt(zoneID);
            // Update the zone with the new product ID
            String sql = "UPDATE Zones SET ProductID = ? WHERE ID = ?";
            try (PreparedStatement ps = connect.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ps.setInt(2, zoneIdInt);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getProductIdByNameAndShop(String productName, int shopId) {
        String sql = "SELECT ID FROM Products WHERE productName = ? AND ShopID = ? ";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, productName);
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
                p.setShopID(rs.getInt("ShopID"));
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

    public int getTotalProductsByShopId(int shopId) {
        String sql = "SELECT COUNT(*) FROM Products WHERE isDelete = 0 AND ShopID = ?";
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

    public ArrayList<Products> getProductsByPage(int page, int productsPerPage, int shopId) {
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE isDelete = 0 AND ShopID = ? ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, (page - 1) * productsPerPage);
            ps.setInt(3, productsPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setPrice(rs.getInt("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setImageLink(rs.getString("ImageLink"));
                product.setShopID(rs.getInt("ShopID"));
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
                product.setShopID(rs.getInt("ShopID"));
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

    // Method to get products by shop ID
    public ArrayList<Products> getProductsByShopId(int shopID) throws SQLException {
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE ShopID = ?";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, shopID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setProductName(rs.getString("ProductName"));
                product.setShopID(rs.getInt("ShopID"));
                product.setQuantity(rs.getInt("Quantity"));
                // Set other product properties as needed

                products.add(product);
            }
        }
        return products;
    }

//    search product by name for order
    public ArrayList<Products> searchProductsByName(String productName) {
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE isDelete = 0 AND ProductName LIKE ?";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, "%" + productName + "%"); // Use LIKE for partial matching
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setPrice(rs.getInt("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setImageLink(rs.getString("ImageLink"));
                product.setShopID(rs.getInt("ShopID"));
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

    public ArrayList<Products> searchProductsByNameAndShop(String name, int shopId) {
        ArrayList<Products> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Products WHERE ProductName LIKE ? AND ShopID = ?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ps.setInt(2, shopId);
            ResultSet rs = ps.executeQuery();
            rs = ps.executeQuery();

            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setProductName(rs.getString("ProductName"));

                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public void updateProductQuantity(String productName, int quantityToAdd, int shopid) {
        String sql = "UPDATE Products SET Quantity = Quantity + ? WHERE productName = ? And shopID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, quantityToAdd);
            ps.setString(2, productName);
            ps.setInt(3, shopid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductQuantitydecre(String productName, int quantityToAdd, int shopid) {
        String sql = "UPDATE Products SET Quantity = Quantity - ? WHERE productName = ? And shopID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, quantityToAdd);
            ps.setString(2, productName);
            ps.setInt(3, shopid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Products> getProductListFromRequest(HttpServletRequest request) {
        ArrayList<Products> productList = new ArrayList<>();

        // Lấy danh sách productId và quantity gửi từ form
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        // Kiểm tra nếu có dữ liệu
        if (productIds != null && quantities != null && productIds.length == quantities.length) {
            for (int i = 0; i < productIds.length; i++) {
                try {
                    int productId = Integer.parseInt(productIds[i]);
                    int quantity = Integer.parseInt(quantities[i]);

                    // Tạo đối tượng sản phẩm và thêm vào danh sách
                    Products product = new Products();
                    product.setID(productId);
                    product.setQuantity(quantity);
                    productList.add(product);
                } catch (NumberFormatException e) {
                    // Có thể ghi log hoặc xử lý lỗi nếu dữ liệu không hợp lệ
                    e.printStackTrace();
                }
            }
        }

        return productList;
    }

    public Integer getStoreIdByUserId(int userId) {
        String sql = "SELECT ShopID FROM Users WHERE ID = ?";
        Integer storeId = null;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                storeId = rs.getInt("ShopID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeId;
    }

    public boolean logPriceChange(int productId, int newPrice, String priceType, int userId) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            System.err.println("Không tìm thấy ShopID cho userId: " + userId);
            return false;
        }

        String sql = "INSERT INTO ProductPriceHistory (productID, price, priceType, changedBy, ShopID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, newPrice);
            stmt.setString(3, priceType);
            stmt.setInt(4, userId);
            stmt.setInt(5, storeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProductPriceHistory> getAllExportPriceHistory(int userId) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            return new ArrayList<>();
        }

        List<ProductPriceHistory> historyList = new ArrayList<>();
        String sql = """
            SELECT pph.historyID, pph.productID, p.productName, p.ImageLink AS image, 
                   pph.price, pph.priceType, pph.changedAt, u.FullName AS changedBy
            FROM ProductPriceHistory pph
            JOIN Products p ON pph.productID = p.ID
            JOIN Users u ON pph.changedBy = u.ID
            WHERE p.ShopID = ? AND pph.priceType = 'export'
            ORDER BY pph.changedAt DESC
        """;

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductPriceHistory history = new ProductPriceHistory(
                            rs.getInt("historyID"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getInt("price"),
                            rs.getString("priceType"),
                            rs.getTimestamp("changedAt"), // Sửa thành Date
                            rs.getString("changedBy")
                    );
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public int getTotalHistoryRecords(String keyword, String startDate, String endDate, int userId) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            return 0;
        }

        String sql = """
        SELECT COUNT(*) AS total
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.productID = p.ID
        WHERE p.ShopID = ? AND pph.priceType = 'export'
              AND p.productName COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?
    """;
        if (startDate != null && !startDate.isEmpty()) {
            sql += " AND pph.changedAt >= ?";
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += " AND pph.changedAt < DATEADD(day, 1, CAST(? AS DATE))";
        }

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, storeId);
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (startDate != null && !startDate.isEmpty()) {
                ps.setString(paramIndex++, startDate + " 00:00:00");
            }
            if (endDate != null && !endDate.isEmpty()) {
                ps.setString(paramIndex++, endDate); // Không cần thêm giờ, dùng DATEADD
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ProductPriceHistory> getExportPriceHistory(String keyword, String startDate, String endDate, int page, int recordsPerPage, int userId, String sortOrder) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            return new ArrayList<>();
        }

        List<ProductPriceHistory> historyList = new ArrayList<>();
        int offset = (page - 1) * recordsPerPage;
        String sql = """
        SELECT pph.historyID, pph.productID, p.productName, p.ImageLink AS image, 
               pph.price, pph.priceType, pph.changedAt, u.FullName AS changedBy
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.productID = p.ID
        JOIN Users u ON pph.changedBy = u.ID
        WHERE p.ShopID = ? AND pph.priceType = 'export'
              AND p.productName COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?
    """;
        if (startDate != null && !startDate.isEmpty()) {
            sql += " AND pph.changedAt >= ?";
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += " AND pph.changedAt < DATEADD(day, 1, CAST(? AS DATE))";
        }
        sql += " ORDER BY pph.changedAt %s OFFSET ? ROWS FETCH NEXT ? ROWS ONLY".formatted(sortOrder);

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, storeId);
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (startDate != null && !startDate.isEmpty()) {
                ps.setString(paramIndex++, startDate + " 00:00:00");
            }
            if (endDate != null && !endDate.isEmpty()) {
                ps.setString(paramIndex++, endDate); // Không cần thêm giờ, dùng DATEADD
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, recordsPerPage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductPriceHistory history = new ProductPriceHistory(
                            rs.getInt("historyID"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getInt("price"),
                            rs.getString("priceType"),
                            rs.getTimestamp("changedAt"),
                            rs.getString("changedBy")
                    );
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    // Cập nhật giá nhập (priceimport) trong bảng Products
    public void updateImportPrice(int productId, int newPrice) {
        String sql = "UPDATE Products SET priceimport = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, newPrice);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy giá nhập hiện tại từ Products
    public int getImportPrice(int productId) {
        String sql = "SELECT priceimport FROM Products WHERE ID = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("priceimport") != 0 ? rs.getInt("priceimport") : -1; // Trả về -1 nếu null
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    // Lấy lịch sử giá nhập
    public List<ProductPriceHistory> getImportPriceHistory(String keyword, String startDate, String endDate, int page, int recordsPerPage, int userId, String sortOrder) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            return new ArrayList<>();
        }

        List<ProductPriceHistory> historyList = new ArrayList<>();
        int offset = (page - 1) * recordsPerPage;
        String sql = """
            SELECT pph.historyID, pph.productID, p.productName, p.ImageLink AS image, 
                   pph.price, pph.priceType, pph.changedAt, u.FullName AS changedBy
            FROM ProductPriceHistory pph
            JOIN Products p ON pph.productID = p.ID
            JOIN Users u ON pph.changedBy = u.ID
            WHERE p.ShopID = ? AND pph.priceType = 'import'
                  AND p.productName COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?
        """;
        if (startDate != null && !startDate.isEmpty()) {
            sql += " AND pph.changedAt >= ?";
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += " AND pph.changedAt < DATEADD(day, 1, CAST(? AS DATE))";
        }
        sql += " ORDER BY pph.changedAt %s OFFSET ? ROWS FETCH NEXT ? ROWS ONLY".formatted(sortOrder);

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, storeId);
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (startDate != null && !startDate.isEmpty()) {
                ps.setString(paramIndex++, startDate + " 00:00:00");
            }
            if (endDate != null && !endDate.isEmpty()) {
                ps.setString(paramIndex++, endDate);
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, recordsPerPage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductPriceHistory history = new ProductPriceHistory(
                            rs.getInt("historyID"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getInt("price"),
                            rs.getString("priceType"),
                            rs.getTimestamp("changedAt"),
                            rs.getString("changedBy")
                    );
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    // Đếm tổng số bản ghi lịch sử giá nhập
    public int getTotalImportHistoryRecords(String keyword, String startDate, String endDate, int userId) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            return 0;
        }

        String sql = """
            SELECT COUNT(*) AS total
            FROM ProductPriceHistory pph
            JOIN Products p ON pph.productID = p.ID
            WHERE p.ShopID = ? AND pph.priceType = 'import'
                  AND p.productName COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?
        """;
        if (startDate != null && !startDate.isEmpty()) {
            sql += " AND pph.changedAt >= ?";
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += " AND pph.changedAt < DATEADD(day, 1, CAST(? AS DATE))";
        }

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, storeId);
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (startDate != null && !startDate.isEmpty()) {
                ps.setString(paramIndex++, startDate + " 00:00:00");
            }
            if (endDate != null && !endDate.isEmpty()) {
                ps.setString(paramIndex++, endDate);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ProductPriceHistory> getAllImportPriceHistory(int userId) {
        Integer storeId = getStoreIdByUserId(userId);
        if (storeId == null) {
            return new ArrayList<>();
        }

        List<ProductPriceHistory> historyList = new ArrayList<>();
        String sql = """
            SELECT pph.historyID, pph.productID, p.productName, p.ImageLink AS image, 
                   pph.price, pph.priceType, pph.changedAt, u.FullName AS changedBy
            FROM ProductPriceHistory pph
            JOIN Products p ON pph.productID = p.ID
            JOIN Users u ON pph.changedBy = u.ID
            WHERE p.ShopID = ? AND pph.priceType = 'import'
            ORDER BY pph.changedAt DESC
        """;

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductPriceHistory history = new ProductPriceHistory(
                            rs.getInt("historyID"),
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getString("image"),
                            rs.getInt("price"),
                            rs.getString("priceType"),
                            rs.getTimestamp("changedAt"), // Sửa thành Date
                            rs.getString("changedBy")
                    );
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public List<ProductPriceHistory> getAllExportPriceHistory1(int userId) {
    Integer storeId = getStoreIdByUserId(userId);
    if (storeId == null) return new ArrayList<>();

    List<ProductPriceHistory> historyList = new ArrayList<>();
    String sql = """
        SELECT pph.historyID, pph.productID, p.productName, p.ImageLink AS image, 
               pph.price, pph.priceType, pph.changedAt, u.Username AS changedBy
        FROM ProductPriceHistory pph
        JOIN Products p ON pph.productID = p.ID
        JOIN Users u ON pph.changedBy = u.ID
        WHERE p.ShopID = ? AND pph.priceType = 'export'
    """;

    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setInt(1, storeId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductPriceHistory history = new ProductPriceHistory(
                        rs.getInt("historyID"),
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getString("image"),
                        rs.getInt("price"),
                        rs.getString("priceType"),
                        rs.getTimestamp("changedAt"),
                        rs.getString("changedBy")
                );
                historyList.add(history);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    System.out.println("Số bản ghi từ DB: " + historyList.size()); // Thêm log
    return historyList;
}
    public static void main(String[] args) throws Exception {
        DAOProducts dao = DAOProducts.INSTANCE;

        // Test update product with ID = 3
        int productIdToUpdate = 3; // ID của sản phẩm cần cập nhật
        Products productToUpdate = dao.getProductByID(productIdToUpdate);
        int quantity = 400;
        System.out.println(dao.getProductIdByNameAndShop("Gạo", 2));

    }

}
