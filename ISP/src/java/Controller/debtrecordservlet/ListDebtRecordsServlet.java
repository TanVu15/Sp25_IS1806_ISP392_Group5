/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.debtrecordservlet;

import Controller.userservlet.UserDetailServlet;
import dal.DAOCustomers;
import dal.DAODebtRecords;
import model.Customers;
import model.DebtRecords;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class ListDebtRecordsServlet extends HttpServlet {

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
        DAODebtRecords dao = new DAODebtRecords();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        if(user.getShopID()==0&&user.getRoleid()==2){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        
        // Lấy trang hiện tại từ tham số URL, mặc định là 1
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int debtsPerPage = 10; // Số sản phẩm trên mỗi trang

       // Lấy tổng số sản phẩm cho shop hiện tại
        int totalCustomer = dao.getTotalDebtRecordByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalCustomer / debtsPerPage);
        
        // Lấy danh sách sản phẩm cho trang hiện tại
        ArrayList<DebtRecords> debtrecords = dao.getDebtrecordsByPage(currentPage, debtsPerPage, user.getShopID());
        
        // Thiết lập các thuộc tính cho JSP
        request.setAttribute("debtrecords", debtrecords);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        //ArrayList<DebtRecords> debtrecords = dao.getDebtRecords();
        //request.setAttribute("debtrecords", debtrecords);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("DebtRecordsManager/ListDebtRecords.jsp");
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
        String information = request.getParameter("information");
        DAODebtRecords dao = new DAODebtRecords();
        DAOCustomers dao1 = new DAOCustomers();
        HttpSession session = request.getSession();
        ArrayList<DebtRecords> debtrecords;
        Customers customer;

        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        try {
            debtrecords = dao.getDebtRecordsSearch(information);
            if (debtrecords == null || debtrecords.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
                
            } else {
                request.setAttribute("message", "Kết quả tìm kiếm cho: " + information);
            }
            
            // Cập nhật currentPage và totalPages
            int totalProducts = debtrecords.size(); // Tổng sản phẩm tìm được
            int totalPages = (int) Math.ceil(totalProducts / 10.0); // Cập nhật với số sản phẩm mỗi trang
            
            // Thiết lập các thuộc tính cho JSP
            request.setAttribute("debtrecords", debtrecords);
            request.setAttribute("currentPage", 1); // Đặt lại về trang đầu tiên
            request.setAttribute("totalPages", totalPages); // Cập nhật tổng trang

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("DebtRecordsManager/ListDebtRecords.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception ex) {
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
