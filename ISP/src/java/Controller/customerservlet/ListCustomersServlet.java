/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.customerservlet;

import dal.DAOCustomers;
import model.Customers;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Products;

/**
 *
 * @author ADMIN
 */
public class ListCustomersServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOCustomers dao = new DAOCustomers();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        
        // Lấy trang hiện tại từ tham số URL, mặc định là 1
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int customersPerPage = 10; // Số sản phẩm trên mỗi trang

       // Lấy tổng số sản phẩm cho shop hiện tại
        int totalCustomer = dao.getTotalCustomersByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalCustomer / customersPerPage);
        
        // Lấy danh sách sản phẩm cho trang hiện tại
        ArrayList<Customers> customers = dao.getCustomersByPage(currentPage, customersPerPage, user.getShopID());
        
        // Thiết lập các thuộc tính cho JSP
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
//        ArrayList<Customers> customers = dao.getAllCustomers();
//        request.setAttribute("customers", customers);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("CustomersManager/ListCustomers.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String information = request.getParameter("information");

        DAOCustomers dao = new DAOCustomers();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        ArrayList<Customers> customers;
        try {
            customers = dao.getCustomersBySearch(information);
            if (customers == null || customers.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
//                
                
//                customers = dao.getAllCustomers();
//                request.setAttribute("customers", customers);
            } else {
                request.setAttribute("customers", customers);
            }
            // Cập nhật currentPage và totalPages
            int totalProducts = customers.size(); // Tổng sản phẩm tìm được
            int totalPages = (int) Math.ceil(totalProducts / 10); // Cập nhật với số sản phẩm mỗi trang
            
            // Thiết lập các thuộc tính cho JSP
            request.setAttribute("customers", customers);
            request.setAttribute("currentPage", 1); // Đặt lại về trang đầu tiên
            request.setAttribute("totalPages", totalPages); // Cập nhật tổng trang
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("CustomersManager/ListCustomers.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ListCustomersServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
