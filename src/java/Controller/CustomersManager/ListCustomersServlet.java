/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.CustomersManager;

import DAL.DAOCustomers;
import Model.Customers;
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

/**
 *
 * @author ADMIN
 */
public class ListCustomersServlet extends HttpServlet {
   
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
        Customers customer = (Customers) session.getAttribute("customers");
        request.setAttribute("customer", customer);
        ArrayList<Customers> customers = dao.getAllCustomers();
        request.setAttribute("customers", customers);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("CustomersManager/ListCustomers.jsp");
        requestDispatcher.forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
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
        Customers customer = (Customers) session.getAttribute("customer");
        request.setAttribute("customer", customer);
        
        ArrayList<Customers> customers;
        try {
            customers = dao.getCustomersBySearch(information);
            if (customers == null || customers.isEmpty()) {
            request.setAttribute("message", "Không tìm thấy kết quả nào.");
            customers = dao.getAllCustomers();
            request.setAttribute("customers", customers);
        } else {
            request.setAttribute("customers", customers);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("CustomersManager/ListCustomers.jsp");
        requestDispatcher.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ListCustomersServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
