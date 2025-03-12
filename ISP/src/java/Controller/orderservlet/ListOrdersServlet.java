/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.orderservlet;

import dal.DAOOrders;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Orders;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class ListOrdersServlet extends HttpServlet {
   
    

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
        DAOOrders dao = new DAOOrders();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        if(user.getShopID()==0&&user.getRoleid()==2){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        ArrayList<Orders> orders = dao.getAllOrders();
        request.setAttribute("orders", orders);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrdersManager/ListOrders.jsp");
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

        DAOOrders dao = new DAOOrders();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        ArrayList<Orders> orders;
        try {
            orders = dao.getOrdersBySearch(information);
            if (orders == null || orders.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
                orders = dao.getAllOrders();
                request.setAttribute("orders", orders);
            } else {
                request.setAttribute("orders", orders);
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrdersManager/ListOrders.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception ex) {
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
    public static void main(String[] args) throws Exception {
        DAOOrders dao = new DAOOrders();
        System.out.println(dao.getOrderByID(4));
    }
}