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
import static java.lang.System.out;
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
public class AddImportOrderServlet extends HttpServlet {

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        DAOOrders dao = new DAOOrders();
        DAOCustomers dao1 = new DAOCustomers();
        DAOProducts dao2 = new DAOProducts();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
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

        RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp");
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

        DAOOrders dao = new DAOOrders();
        DAOCustomers dao1 = new DAOCustomers();
        DAOProducts dao2 = new DAOProducts();
        DAOZones zoneDAO = new DAOZones();

        Users user = new Users();
        HttpSession session = request.getSession();
        user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        try {

            int shopID = user.getShopID();

            String customerName = request.getParameter("customerName");
            String total = request.getParameter("totalCost").replace(".", "");
            int totalCostStr = Integer.parseInt(total);
            String orderTypeStr = request.getParameter("orderType");

            int status = Integer.parseInt(orderTypeStr); // 1: Nhập, -1: Xuất

            DAOCustomers daoCustomers = new DAOCustomers();
            int customerID = daoCustomers.getCustomerIdByNameAndShop(customerName, user.getShopID());

            // Tạo đối tượng Order mới
            Orders order = new Orders();
            order.setCustomerID(customerID);
            order.setTotalAmount(totalCostStr);
            order.setShopID(shopID);
            order.setStatus(status);

            // Thêm Order vào CSDL
            dao.addOrders(order, user.getID());
            

            response.sendRedirect("listorders");
        } catch (Exception e) {
            // In ra giao diện HTML thông báo lỗi
            e.printStackTrace(); // In ra console
            // Hoặc muốn in ra trình duyệt:
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            e.printStackTrace(out); // In ra trình duyệt
//            request.setAttribute("message", "Có lỗi xảy ra khi thêm hóa đơn: " + e.getMessage());
//            request.getRequestDispatcher("addorder").forward(request, response);
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
