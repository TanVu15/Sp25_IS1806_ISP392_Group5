/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.debtrecordservlet;

import dal.DAOCustomers;
import dal.DAODebtRecords;
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
import model.Customers;
import model.DebtRecords;
import model.Shops;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class DebtRecordDetailServlet extends HttpServlet {

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

        request.setAttribute("message", "");

        
        DebtRecords debtrecords;
        try {
            int debtid = Integer.parseInt(request.getParameter("debtid"));
            // lay customer đang cần
            debtrecords = dao.getDebtRecordByID(debtid);
            Shops shop = (Shops) session.getAttribute("shop");
            if (shop.getID() != debtrecords.getShopID() || debtrecords == null) {
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("debtrecords", debtrecords);
        } catch (Exception ex) {
            request.getRequestDispatcher("logout").forward(request, response);
            return;
        }
        //lay user người đang đăng nhập
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        // lay líst debt của customer
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("DebtRecordsManager/DebtRecordDetail.jsp");
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
                request.setAttribute("debtrecords", debtrecords);
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
