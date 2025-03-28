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
import java.util.Comparator;
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
    
    if (user != null) {
        request.setAttribute("user", user);
        
        // Lấy trang hiện tại từ tham số URL, mặc định là 1
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int productsPerPage = 5; // Số sản phẩm trên mỗi trang
        String sortBy = request.getParameter("sortBy");

       // Lấy tổng số sản phẩm cho shop hiện tại
        int totalProducts = dao.getTotalProductsByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalProducts / productsPerPage);
        
        // Lấy danh sách sản phẩm cho trang hiện tại
        if(user.getRoleid() == 2){
        ArrayList<Products> products = dao.getProductsByPage(currentPage, productsPerPage, user.getShopID());
        // Xử lý sắp xếp
            if (sortBy != null) {
                switch (sortBy) {
                    case "price_asc":
                        products.sort(Comparator.comparingDouble(Products::getPrice));
                        break;
                    case "price_desc":
                        products.sort(Comparator.comparingDouble(Products::getPrice).reversed());
                        break;
                    case "quantity_asc":
                        products.sort(Comparator.comparingInt(Products::getQuantity));
                        break;
                    case "quantity_desc":
                        products.sort(Comparator.comparingInt(Products::getQuantity).reversed());
                        break;
                    case "name_asc":
                        products.sort(Comparator.comparing(Products::getProductName));
                        break;
                    case "name_desc":
                        products.sort(Comparator.comparing(Products::getProductName).reversed());
                        break;
                }
            }
                request.setAttribute("products", products);
        } 
        else{
           ArrayList<Products> products = dao.getProductsByPage2(currentPage, productsPerPage, user.getShopID());
           // Xử lý sắp xếp
            if (sortBy != null) {
                switch (sortBy) {
                    case "price_asc":
                        products.sort(Comparator.comparingDouble(Products::getPrice));
                        break;
                    case "price_desc":
                        products.sort(Comparator.comparingDouble(Products::getPrice).reversed());
                        break;
                    case "quantity_asc":
                        products.sort(Comparator.comparingInt(Products::getQuantity));
                        break;
                    case "quantity_desc":
                        products.sort(Comparator.comparingInt(Products::getQuantity).reversed());
                        break;
                    case "name_asc":
                        products.sort(Comparator.comparing(Products::getProductName));
                        break;
                    case "name_desc":
                        products.sort(Comparator.comparing(Products::getProductName).reversed());
                        break;
                }
            }
               request.setAttribute("products", products);
        }
        
                    
        // Thiết lập các thuộc tính cho JSP
        
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sortBy", sortBy);

        // Chuyển đến trang dựa trên vai trò người dùng
        if (user.getRoleid() == 1) {
            request.getRequestDispatcher("listusers").forward(request, response);
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
            } else {
                request.setAttribute("message", "Kết quả tìm kiếm cho: " + information);
            }

           // Cập nhật currentPage và totalPages
            int totalProducts = products.size(); // Tổng sản phẩm tìm được
            int totalPages = (int) Math.ceil(totalProducts / 5.0); // Cập nhật với số sản phẩm mỗi trang
            
            // Thiết lập các thuộc tính cho JSP
            request.setAttribute("products", products);
            request.setAttribute("currentPage", 1); // Đặt lại về trang đầu tiên
            request.setAttribute("totalPages", totalPages); // Cập nhật tổng trang

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