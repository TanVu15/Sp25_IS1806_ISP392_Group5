/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.debtrecordservlet;

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
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Shops;

/**
 *
 * @author Admin
 */
public class ListCustomerDebtRecordsServlet extends HttpServlet {

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
        DAOCustomers dao1 = new DAOCustomers();
        HttpSession session = request.getSession();
        String sortBy = request.getParameter("sortBy");
        request.setAttribute("message", "");
        Customers customer;
        try {
            int customerID2 = Integer.parseInt(request.getParameter("customerid"));
            // lay customer đang cần
            customer = dao1.getCustomersByID(customerID2);
            Shops shop = (Shops) session.getAttribute("shop");
            if (shop.getID() != customer.getShopID() || customer == null) {
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            request.getRequestDispatcher("logout").forward(request, response);
            return;
        }
        //lay user người đang đăng nhập
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        // Lấy trang hiện tại từ tham số URL, mặc định là 1
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int debtsPerPage = 10; // Số sản phẩm trên mỗi trang

        // Lấy tổng số sản phẩm cho shop hiện tại
        int totalCustomer = dao.getTotalDebtRecordByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalCustomer / debtsPerPage);

        // lay líst debt của customer
        int customerID = Integer.parseInt(request.getParameter("customerid"));

        ArrayList<DebtRecords> debtrecords = dao.getCustomerDebtRecords(customerID);
        // Xử lý sắp xếp
        if (sortBy != null) {
            switch (sortBy) {
                case "price_asc":
                    debtrecords.sort(Comparator.comparingDouble(DebtRecords::getAmountOwed));
                    break;
                case "price_desc":
                    debtrecords.sort(Comparator.comparingDouble(DebtRecords::getAmountOwed).reversed());
                    break;
            }
        }

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("debtrecords", debtrecords);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("DebtRecordsManager/ListCustomerDebtRecords.jsp");
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
        int customerID = Integer.parseInt(request.getParameter("customerid"));
        Customers customer;
        try {
            // lay customer đang cần
            customer = dao1.getCustomersByID(customerID);
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            Logger.getLogger(ListCustomerDebtRecordsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        //lay user người đang đăng nhập
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        // lay líst debt của customer

        try {
            debtrecords = dao.getCustomerDebtRecordsSearch(information, customerID);
            if (debtrecords == null || debtrecords.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
                debtrecords = dao.getCustomerDebtRecords(customerID);
                request.setAttribute("debtrecords", debtrecords);
            } else {
                request.setAttribute("message", "Kết quả tìm kiếm cho: " + information);

                // Cập nhật currentPage và totalPages
                int debtsPerPage = 10;
                int totalProducts = debtrecords.size(); // Tổng sản phẩm tìm được
                int totalPages = (int) Math.ceil(totalProducts / debtsPerPage); // Cập nhật với số sản phẩm mỗi trang

                // Thiết lập các thuộc tính cho JSP
                request.setAttribute("debtrecords", debtrecords);
                request.setAttribute("currentPage", 1); // Đặt lại về trang đầu tiên
                request.setAttribute("totalPages", totalPages);
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("DebtRecordsManager/ListCustomerDebtRecords.jsp");
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
