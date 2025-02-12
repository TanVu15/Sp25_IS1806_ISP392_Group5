package Controller.ProductsManager;

import DAL.DAOProducts;
import Model.Products;
import Model.Users;
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
        request.setAttribute("user", user);

        ArrayList<Products> products = dao.getAllProducts();
        request.setAttribute("products", products);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("HomePage/Home.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String information = request.getParameter("information");

        DAOProducts dao = DAOProducts.INSTANCE;
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
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

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("HomePage/Home.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý sản phẩm";
    }
}