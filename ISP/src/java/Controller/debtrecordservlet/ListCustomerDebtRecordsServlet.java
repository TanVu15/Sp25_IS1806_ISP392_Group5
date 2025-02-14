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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //truyen mes de mes ko null
        request.setAttribute("message", "");
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
        ArrayList<DebtRecords> debtrecords = dao.getCustomerDebtRecords(customerID);
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
            debtrecords = dao.getCustomerDebtRecordsSeach(information, customerID);
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
