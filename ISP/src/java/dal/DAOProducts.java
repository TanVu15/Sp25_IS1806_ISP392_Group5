package dal;

import model.Products;
import model.Users;
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
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public ArrayList<Products> getAllProducts() {
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Products cs = new Products();
                cs.setID(rs.getInt("ID"));
                cs.setImageLink(rs.getString("ImageLink")); // Sửa tên trường
                cs.setProductName(rs.getString("ProductName"));
                cs.setDescription(rs.getString("Description"));
                cs.setPrice(rs.getInt("Price"));
                cs.setQuantity(rs.getInt("Quantity"));
                cs.setLocation(rs.getString("Location"));
                cs.setCreateAt(rs.getDate("CreateAt"));
                cs.setUpdateAt(rs.getDate("UpdateAt"));
                cs.setCreateBy(rs.getInt("CreateBy"));
                cs.setIsDelete(rs.getInt("isDelete"));
                cs.setDeletedAt(rs.getDate("DeletedAt"));
                cs.setDeleteBy(rs.getInt("DeleteBy"));
                products.add(cs);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return products;
    }

    public void deleteProducts(int deleteid, int userid) {
        String sql = "UPDATE Products SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 1); // Đánh dấu sản phẩm là đã bị xóa
            ps.setInt(2, userid); // Ghi lại ID của người xóa
            ps.setDate(3, today); // Lưu thời gian xóa
            ps.setInt(4, deleteid); // ID sản phẩm cần xóa
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProducts(Products product) {
        String sql = "UPDATE Products SET ProductName = ?, Description = ?, Price = ?, Quantity = ?, UpdateAt = ?, ImageLink = ?, Location = ? WHERE ID = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setDate(5, today);
            ps.setString(6, product.getImageLink()); // Sửa tên trường
            ps.setString(7, product.getLocation());
            ps.setInt(8, product.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddProducts(Products product, int userid) {
        String sql = "INSERT INTO Products (ImageLink, ProductName, Description, Price, Quantity, Location, CreateAt, CreateBy, UpdateAt, isDelete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, product.getImageLink()); // Sửa tên trường
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getPrice());
            ps.setInt(5, product.getQuantity());
            ps.setString(6, product.getLocation());
            ps.setDate(7, today);
            ps.setInt(8, userid);
            ps.setDate(9, today); // Set UpdateAt to the current date
            ps.setInt(10, 0); // Set isDelete to 0 (not deleted)
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Products getProductByID(int ID) throws Exception {
        String query = "SELECT * FROM Products WHERE ID=?";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Products p = new Products();
            p.setID(rs.getInt("ID"));
            p.setProductName(rs.getString("ProductName"));
            p.setLocation(rs.getString("Location"));
            p.setImageLink(rs.getString("ImageLink")); // Sửa tên trường
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
        return null;
    }

    public ArrayList<Products> getProductsBySearch(String information) throws Exception {
        information = information.toLowerCase();
        ArrayList<Products> products = new ArrayList<>();
        String sql = "SELECT * FROM Products"; // Lấy toàn bộ danh sách Products

        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Products product = new Products();
                product.setID(rs.getInt("ID"));
                product.setImageLink(rs.getString("ImageLink")); // Sửa tên trường
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setPrice(rs.getInt("Price"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setLocation(rs.getString("Location"));
                product.setCreateAt(rs.getDate("CreateAt"));
                product.setUpdateAt(rs.getDate("UpdateAt"));
                product.setCreateBy(rs.getInt("CreateBy"));
                product.setIsDelete(rs.getInt("isDelete"));
                product.setDeletedAt(rs.getDate("DeletedAt"));
                product.setDeleteBy(rs.getInt("DeleteBy"));

                // Lấy thông tin người tạo 
                Users userCreate = DAO.INSTANCE.getUserByID(product.getCreateBy());

                // Tạo một chuỗi chứa toàn bộ thông tin của product
                String productData = (product.getImageLink() + product.getProductName() + " "
                        + product.getDescription() + " "
                        + product.getPrice() + " "
                        + product.getQuantity() + " "
                        + product.getLocation() + " "
                        + product.getCreateAt() + " "
                        + product.getUpdateAt() + " "
                        + userCreate.getFullName().toLowerCase() + " "
                        + product.getIsDelete() + " ");

                // Lấy thông tin người xóa nếu có
                if (product.getIsDelete() != 0) {
                    Users userDelete = DAO.INSTANCE.getUserByID(product.getDeleteBy());
                    productData += ("xóa" + product.getIsDelete() + " "
                            + product.getDeletedAt() + " "
                            + userDelete.getFullName().toLowerCase());
                } else {
                    productData += "Hoạt Động";
                }

                // Kiểm tra nếu information xuất hiện trong dữ liệu sản phẩm
                if (productData.toLowerCase().contains(information.toLowerCase())) {
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

        // Kiểm tra thêm sản phẩm
        Products newProduct = new Products();
        newProduct.setImageLink("path/to/image.jpg"); // Sửa tên trường
        newProduct.setProductName("New Product");
        newProduct.setDescription("This is a new product description.");
        newProduct.setPrice(1500);
        newProduct.setQuantity(20);
        newProduct.setLocation("Warehouse A");

        int userId = 1;

        // Thêm sản phẩm mới
//        dao.AddProducts(newProduct, userId);
//        System.out.println("Product added successfully!");

        // Kiểm tra danh sách sản phẩm
        for (Products product : productList) {
            System.out.println(product);
        }

        // Kiểm tra xóa
        int productIdToDelete = 10;
        dao.deleteProducts(productIdToDelete, userId);
        System.out.println("Product with ID " + productIdToDelete + " marked as deleted.");

        // Lấy và in danh sách sản phẩm đã cập nhật
        ArrayList<Products> updatedProductList = dao.getAllProducts();
        System.out.println("Updated product list after deletion:");
        for (Products product : updatedProductList) {
            System.out.println(product);
        }
        
        // Kiểm tra tính năng tìm kiếm
        // String searchKeyword = "A"; // Thay đổi từ khóa tìm kiếm theo ý muốn
        // ArrayList<Products> searchResults = dao.getProductsBySearch(searchKeyword);
        // if (searchResults.isEmpty()) {
        //     System.out.println("Không tìm thấy sản phẩm nào với từ khóa: " + searchKeyword);
        // } else {
        //     System.out.println("Kết quả tìm kiếm cho từ khóa: " + searchKeyword);
        //     for (Products product : searchResults) {
        //         System.out.println(product);
        //     }
        // }
    }
}