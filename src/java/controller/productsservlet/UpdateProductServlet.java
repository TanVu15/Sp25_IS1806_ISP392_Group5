package controller.productsservlet;

import dal.DAOProducts;
import model.Products;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UpdateProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOProducts dao = DAOProducts.INSTANCE;
        int productId = Integer.parseInt(request.getParameter("id"));

        try {
            Products product = dao.getProductByID(productId);
            request.setAttribute("product", product);
            request.getRequestDispatcher("HomePage/UpdateProduct.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Không tìm thấy sản phẩm.");
            request.getRequestDispatcher("HomePage/UpdateProduct.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("id"));
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        int price = Integer.parseInt(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String location = request.getParameter("location");

        // Cập nhật sản phẩm trong database
        Products product = new Products();
        product.setID(productId);
        product.setProductName(productName);
        product.setDescription(description);
        product.setImage(image);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setLocation(location);

        try {
            DAOProducts.INSTANCE.updateProducts(product);
            response.sendRedirect("listproducts"); // Chuyển hướng tới trang danh sách sản phẩm sau khi cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật sản phẩm.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("HomePage/UpdateProduct.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý cập nhật sản phẩm";
    }
}