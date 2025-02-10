/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.DebtRecordsManager;

import DAL.DAOCustomers;
import DAL.DAODebtRecords;
import Model.Customers;
import Model.DebtRecords;
import Model.Users;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class AddDebtRecordsServlet extends HttpServlet {

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
        int customerID = Integer.parseInt(request.getParameter("customerid"));
        try {
            Customers customer = dao.getCustomersByID(customerID);
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            Logger.getLogger(AddDebtRecordsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp");
        dispatcher.forward(request, response);
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
        DAOCustomers daoCus = new DAOCustomers();
        response.setContentType("text/html;charset=UTF-8");
        int customerID = Integer.parseInt(request.getParameter("customerid"));
        try {
            Customers customer = daoCus.getCustomersByID(customerID);
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            Logger.getLogger(AddDebtRecordsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        int amountOwed = Integer.parseInt(request.getParameter("amountowed"));
        int paymentStatus = Integer.parseInt(request.getParameter("paymentstatus"));

        Users user = new Users();
        HttpSession session = request.getSession();
        user = (Users) session.getAttribute("user");

        DAODebtRecords dao = new DAODebtRecords();
        try {
            if (user != null) {
                DebtRecords debtRecord = new DebtRecords();
                debtRecord.setCustomerID(customerID);
                debtRecord.setAmountOwed(amountOwed);
                debtRecord.setPaymentStatus(paymentStatus);

                dao.AddDebtRecords(debtRecord, user.getID());
                response.sendRedirect("listcustomerdebtrecords?customerid=" + customerID);
            } else {
                request.setAttribute("errorMessage", "User not authenticated.");
                request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp").forward(request, response);
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
