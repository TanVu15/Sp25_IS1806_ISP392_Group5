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
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class AddCustomerServlet extends HttpServlet {

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
        ArrayList<Customers> customers = dao.getAllCustomers();
        request.setAttribute("customers", customers);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("CustomersManager/AddCustomer.jsp");
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

        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        DAOCustomers dao = new DAOCustomers();
        Customers cus = new Customers();
        Users user = new Users();
        HttpSession session = request.getSession();
        user = (Users) session.getAttribute("user");
        try {

            if (user != null) {
                Customers addcustomer = new Customers();

                addcustomer.setName(name);
                addcustomer.setPhone(phone);
                addcustomer.setAddress(address);

                dao.AddCustomer(addcustomer, user.getID());
                response.sendRedirect("listcustomers");

            } else {
                request.setAttribute("message", "Thông tin không phù hợp.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("CustomersManager/AddCustomer.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("CustomersManager/AddCustomer.jsp");
            dispatcher.forward(request, response);
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
//    public static long millis = System.currentTimeMillis();
//    public static Date today = new Date(millis);
//    public static void main(String[] args) {
//        DAOCustomers dao = new DAOCustomers();
//        Customers customer = new Customers(0, 0, "Dong", "12323213", "hai duong", today, today, 0, 0, today, 0);
//        dao.AddCustomer(customer, 1);
//    }

}
