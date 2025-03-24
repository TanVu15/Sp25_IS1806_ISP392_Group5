/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.orderservlet;

import Controller.Queue.AddImportOrderQueue;
import Controller.Queue.ImportOrderTask;
import dal.DAOCustomers;
import dal.DAOOrderItem;
import dal.DAOOrders;
import dal.DAOProducts;
import dal.DAOZones;
import dal.DAODebtRecords;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.sql.Date;
import model.Customers;
import model.DebtRecords;
import model.OrderItems;
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddOrders</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddOrders at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    request.setCharacterEncoding("UTF-8");
    HttpSession session = request.getSession();
    Users user = (Users) session.getAttribute("user");

    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    try {
        int shopID = user.getShopID();
        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String totalCostRaw = request.getParameter("totalCost");

        if (customerName == null || customerName.trim().isEmpty()
                || totalCostRaw == null || totalCostRaw.trim().isEmpty()) {
            request.setAttribute("message", "Vui lòng nhập đầy đủ thông tin hóa đơn.");
            request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
            return;
        }

        double totalCost = Double.parseDouble(totalCostRaw.replace(".", "").trim());

        // Lấy danh sách sản phẩm từ request
        String[] productNames = request.getParameterValues("productName");
        String[] quantities = request.getParameterValues("quantity");
        String[] prices = request.getParameterValues("price");

        if (productNames == null || quantities == null || prices == null) {
            out.println("<h3 style='color:red;'>Lỗi: Dữ liệu sản phẩm bị thiếu.</h3>");
            return;
        }

        ArrayList<Products> productList = new ArrayList<>();
        for (int i = 0; i < productNames.length; i++) {
            try {
                String productName = productNames[i].trim();
                int quantity = Integer.parseInt(quantities[i].trim());
                int price = Integer.parseInt(prices[i].trim());

                Products product = new Products();
                product.setProductName(productName);
                product.setQuantity(quantity);
                product.setPrice(price);
                productList.add(product);
            } catch (NumberFormatException e) {
                out.println("<h3 style='color:red;'>Lỗi định dạng số ở sản phẩm thứ " + (i + 1) + ".</h3>");
                return;
            }
        }

        // Tạo và đưa đơn hàng vào queue để xử lý bất đồng bộ
        ImportOrderTask importTask = new ImportOrderTask(customerName, customerPhone, productList, totalCost, session);
        AddImportOrderQueue.getInstance().addOrder(importTask); // Đưa vào hàng đợi

        response.sendRedirect("listorders");
    } catch (Exception e) {
        out.println("<h3 style='color:red;'>Lỗi hệ thống: " + e.getMessage() + "</h3>");
        e.printStackTrace(out);
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

//    public static void main(String[] args) {
//        DAOOrderItem dao = DAOOrderItem.INSTANCE;
//        OrderItems or1 = new OrderItems(0, 0, 0, LEGACY_DO_HEAD, LEGACY_DO_HEAD, 0, 0, 0, 0, CreateAt, UpdateAt, 0, 0, deletedAt, 0)
//        dao.AddOrderItems(orderitems, 0);
//    }
}
