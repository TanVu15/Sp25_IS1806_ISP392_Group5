package Controller.productsservlet;

import dal.DAOProducts;
import model.Products;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import model.Users;
import model.Zones;

@MultipartConfig
public class UpdateProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOProducts dao = DAOProducts.INSTANCE;
        HttpSession session = request.getSession();
        int productId = Integer.parseInt(request.getParameter("id"));

        try {
            Products product = dao.getProductByID(productId);
            
            int shopid = product.getShopID();
            int shopid2 = ((Users)session.getAttribute("user")).getShopID();
            if(shopid != shopid2 && ((Users)session.getAttribute("user")).getRoleid() != 1){
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            
            request.setAttribute("product", product);
            request.getRequestDispatcher("ProductsManager/UpdateProduct.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Không tìm thấy sản phẩm.");
            request.getRequestDispatcher("ProductsManager/UpdateProduct.jsp").forward(request, response);
        }
    }

  @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    int productId = Integer.parseInt(request.getParameter("id"));
    String productName = request.getParameter("productName");
    String description = request.getParameter("description");
    Part filePart = request.getPart("image");
    int price = Integer.parseInt(request.getParameter("price"));
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    String[] zoneIDs = request.getParameterValues("zoneIDs"); // Lấy danh sách ID khu vực

    // Lấy đường dẫn thư mục ảnh
    String imageDirectory = getServletContext().getRealPath("/Image/");
    String imageLink = "";

    // Lưu ảnh vào thư mục nếu có
    if (filePart != null && filePart.getSize() > 0) {
        String fileName = filePart.getSubmittedFileName();
        File dir = new File(imageDirectory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, fileName);
        filePart.write(file.getAbsolutePath());
        imageLink = "Image/" + fileName; // Đường dẫn lưu trữ ảnh
    } else {
        imageLink = request.getParameter("currentImageLink"); // Giữ ảnh cũ
    }

    // Cập nhật sản phẩm trong database
    Products product = new Products();
    product.setID(productId);
    product.setProductName(productName);
    product.setDescription(description);
    product.setImageLink(imageLink);
    product.setPrice(price);
    product.setQuantity(quantity);

    try {
        // Cập nhật sản phẩm
        DAOProducts.INSTANCE.updateProducts(product);

        // Cập nhật khu vực
        if (zoneIDs != null) {
            // Xóa các khu vực cũ (nếu cần)
            DAOProducts.INSTANCE.clearProductZones(productId); // Phương thức để xóa các khu vực cũ

            // Thêm các khu vực mới
            for (String zoneID : zoneIDs) {
                int id = Integer.parseInt(zoneID);
                Zones zone = new Zones(id); // Tạo đối tượng Zones từ ID
                DAOProducts.INSTANCE.addProductZones(productId, zone); // Phương thức để thêm khu vực mới
            }
        }

        response.sendRedirect("listproducts");
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật sản phẩm.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("ProductsManager/UpdateProduct.jsp");
        dispatcher.forward(request, response);
    }
}

    @Override
    public String getServletInfo() {
        return "Servlet quản lý cập nhật sản phẩm";
    }
}