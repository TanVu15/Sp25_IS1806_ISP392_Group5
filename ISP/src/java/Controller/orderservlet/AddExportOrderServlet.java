/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.orderservlet;

import Controller.customerservlet.ListCustomersServlet;
import dal.DAOCustomers;
import dal.DAOOrders;
import dal.DAOProducts;
import dal.DAOZones;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customers;
import model.Orders;
import model.Products;
import model.Users;
import model.Zones;

/**
 *
 * @author ADMIN
 */
public class AddExportOrderServlet extends HttpServlet {
   
    

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
        DAOCustomers dao1 = new DAOCustomers();
        DAOProducts dao2 = new  DAOProducts();
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
        int productsPerPage = 5; // Số sản phẩm trên mỗi trang
        
        ArrayList<Customers> customers = dao1.getCustomersByPage(currentPage, productsPerPage, user.getShopID());
        request.setAttribute("customers", customers);
        
        // Lấy tổng số sản phẩm cho shop hiện tại
        int totalProducts = dao2.getTotalProductsByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalProducts / productsPerPage);
        
        // Lấy danh sách sản phẩm cho trang hiện tại
        ArrayList<Products> products = dao2.getProductsByPage(currentPage, productsPerPage, user.getShopID());
        request.setAttribute("products", products);
        
        DAOZones zoneDAO = new DAOZones();
        ArrayList<Zones> zones = zoneDAO.getAllZones();
        request.setAttribute("zones", zones);
        
        
        ArrayList<Orders> orders = dao.getAllOrders();
        request.setAttribute("orders", orders);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp");
        dispatcher.forward(request, response);
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

        DAOCustomers dao1 = new DAOCustomers();
        

        ArrayList<Customers> customers;
        try {
            customers = dao1.getCustomersBySearch(information);
            if (customers == null || customers.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
                customers = dao1.getAllCustomers();
                request.setAttribute("customers", customers);
            } else {
                request.setAttribute("customers", customers);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp");
                dispatcher.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ListCustomersServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int customerID = Integer.parseInt(request.getParameter("CustimerID"));
        int userID = Integer.parseInt(request.getParameter("UserrID"));
        int shopID = Integer.parseInt(request.getParameter("ShopID"));
        String totalamount = request.getParameter("amount");
        String statusString = request.getParameter("status");

        int status = 0;

        if (statusString != null) {
            try {
                status = Integer.parseInt(statusString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        int amount = 0;

        if (totalamount != null && !totalamount.isEmpty()) {
            amount = Integer.parseInt(totalamount);
            if (amount < 0) {
                request.setAttribute("errorMessage", "Giá trị không thể âm.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }

        DAOOrders dao = DAOOrders.INSTANCE;
        Orders order = new Orders();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        try {
            if (user != null) {
                order.setCustomerID(customerID);;
                order.setTotalAmount(amount);
                order.setShopID(shopID);
                order.setStatus(status);
                order.setCreateAt(new Date(System.currentTimeMillis()));

                dao.addOrders(order, user.getID());
                response.sendRedirect("listorders");
            } else {
                request.setAttribute("errorMessage", "Thông tin không phù hợp.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp");
                dispatcher.forward(request, response);
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
