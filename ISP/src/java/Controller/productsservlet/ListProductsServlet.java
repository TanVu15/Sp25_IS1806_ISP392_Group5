package Controller.productsservlet;

import dal.DAOProducts;
import model.Products;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOProducts dao = DAOProducts.INSTANCE;
        HttpSession session = request.getSession();
        request.setAttribute("message", "");

        Users user = (Users) session.getAttribute("user");
        
        // Kiểm tra quyền truy cập dựa trên vai trò
        if (user != null) {
            request.setAttribute("user", user);
            ArrayList<Products> products = dao.getAllProducts();
            request.setAttribute("products", products);

            if (user.getRoleid() == 1) {
                request.getRequestDispatcher("ProductsManager/ListProductForAdmin.jsp").forward(request, response);
            } else if (user.getRoleid() == 2) {
                request.getRequestDispatcher("ProductsManager/ListProductForOwner.jsp").forward(request, response);
            } else if (user.getRoleid() == 3) {
                request.getRequestDispatcher("ProductsManager/ListProductForStaff.jsp").forward(request, response);
            } else {
                // Nếu vai trò không xác định, có thể chuyển đến trang mặc định hoặc thông báo lỗi
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Không có quyền truy cập.");
            }
        } else {
            // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập hoặc thông báo lỗi
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String information = request.getParameter("information");

        DAOProducts dao = DAOProducts.INSTANCE;
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user != null) {
            request.setAttribute("user", user);

            ArrayList<Products> products;
            try {
                products = dao.getProductsBySearch(information);
                if (products == null || products.isEmpty()) {
                    request.setAttribute("message", "Không tìm thấy sản phẩm nào.");
                    products = dao.getAllProducts(); // Lấy lại tất cả nếu không có kết quả
                }
                request.setAttribute("products", products);
            } catch (Exception ex) {
                Logger.getLogger(ListProductsServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("message", "Đã xảy ra lỗi khi tìm kiếm.");
            }

            // Chuyển hướng đến trang dựa trên vai trò người dùng
            if (user.getRoleid() == 1) {
                request.getRequestDispatcher("ProductsManager/ListProductForAdmin.jsp").forward(request, response);
            } else if (user.getRoleid() == 2) {
                request.getRequestDispatcher("ProductsManager/ListProductForOwner.jsp").forward(request, response);
            } else if (user.getRoleid() == 3) {
                request.getRequestDispatcher("ProductsManager/ListProductForStaff.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Không có quyền truy cập.");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý sản phẩm";
    }
}