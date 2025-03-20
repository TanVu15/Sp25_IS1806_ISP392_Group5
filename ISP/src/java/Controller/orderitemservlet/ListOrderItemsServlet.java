/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.orderitemservlet;

import Controller.userservlet.UserDetailServlet;
import dal.DAOOrderItem;
import dal.DAOOrders;
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
import model.OrderItems;
import model.Orders;
import model.Users;

/**
 *
 * @author ASUS
 */
public class ListOrderItemsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ListOrderItemsServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListOrderItemsServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        DAOOrderItem dao = new DAOOrderItem();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        int orderid = Integer.parseInt(request.getParameter("id"));
        ArrayList<OrderItems> orderitems = new ArrayList<>();
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        try {
            orderitems = dao.getAllOrderItemsByOrderID(orderid);
            request.setAttribute("orderitems", orderitems);
        } catch (Exception ex) {
            Logger.getLogger(UserDetailServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
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

        DAOOrderItem dao = new DAOOrderItem();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        ArrayList<OrderItems> orderitems;
        try {
            orderitems = dao.getOrderItemBySearch(information);
            if (orderitems == null || orderitems.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
                orderitems = dao.getAllOrderItem();
                request.setAttribute("orderitems", orderitems);
            } else {
                request.setAttribute("orderitems", orderitems);
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
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
