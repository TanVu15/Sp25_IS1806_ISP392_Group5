/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.customerservlet;

import dal.DAOCustomers;
import model.Customers;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class UpdateCustomerServlet extends HttpServlet {
    

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
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        ArrayList<Customers> customers = dao.getAllCustomers();
        request.setAttribute("customers", customers);
        int userid = Integer.parseInt(request.getParameter("id"));

        try {
            Customers cus = dao.getCustomersByID(userid);
            request.setAttribute("cus", cus);
            request.getRequestDispatcher("CustomersManager/UpdateCustomer.jsp").forward(request, response);
        } catch (Exception ex) {
        }
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
        
        int userid = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        // Cập nhật người dùng trong database
        Customers cus = new Customers();
        cus.setID(userid);
        cus.setName(name);
        cus.setPhone(phone);
        cus.setAddress(address);
        DAOCustomers.INSTANCE.updateCustomers(cus);
        

        // Chuyển hướng tới trang danh sách người dùng sau khi cập nhật thành công
        response.sendRedirect("listcustomers");
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
