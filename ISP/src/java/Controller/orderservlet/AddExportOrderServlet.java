/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.orderservlet;

import Controller.customerservlet.ListCustomersServlet;
import dal.DAOCustomers;
import dal.DAODebtRecords;
import dal.DAOOrderItem;
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
public class AddExportOrderServlet extends HttpServlet {

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
        //ArrayList<Products> products = dao2.getAllProductsByShopId(user.getShopID());
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

        DAOOrders dao = new DAOOrders();
        DAOCustomers dao1 = new DAOCustomers();
        DAOProducts dao2 = new DAOProducts();
        DAOOrderItem dao3 = new DAOOrderItem();
        DAOZones dao4 = new DAOZones();
        try {
            int shopID = user.getShopID();
            int customerID = Integer.parseInt(request.getParameter("customerId"));
            String totalCostRaw = request.getParameter("totalCostHidden");
            String orderTypeStr = request.getParameter("orderType");
            String paymentStatus = request.getParameter("paymentStatus");

            if (totalCostRaw == null || totalCostRaw.trim().isEmpty()
                    || orderTypeStr == null || orderTypeStr.trim().isEmpty()) {
                request.setAttribute("message", "Vui lòng nhập đầy đủ thông tin hóa đơn.");
                request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp").forward(request, response);
                return;
            }

            int totalCost = (int) Double.parseDouble(totalCostRaw.trim());
            int status = Integer.parseInt(orderTypeStr);

            Orders order = new Orders();
            order.setCustomerID(customerID);
            order.setTotalAmount(totalCost);
            order.setShopID(shopID);
            order.setStatus(status);

            int id = dao.addOrdersreturnID(order, user.getID());

            if ("partial".equals(paymentStatus) || "none".equals(paymentStatus)) {
                int amountOwed;
                String note;
                java.sql.Date invoiceDate = new java.sql.Date(System.currentTimeMillis());

                if ("partial".equals(paymentStatus)) {
                    String partialPaymentStr = request.getParameter("partialPayment");
                    int partialPayment = (int) Double.parseDouble(partialPaymentStr);
                    amountOwed = totalCost - partialPayment;
                    note = "Khách thanh toán " + partialPayment + " VND từ hóa đơn " + totalCost +"VND" + "Nợ " + amountOwed;
                } else {
                    amountOwed = totalCost;
                    note = "Ghi nợ " + amountOwed +" VND từ hóa đơn khách mua hàng ";
                }

                if (amountOwed > 0) {
                    DebtRecords debtRecord = new DebtRecords();
                    debtRecord.setCustomerID(customerID);
                    debtRecord.setAmountOwed(amountOwed);
                    debtRecord.setPaymentStatus(-1);
                    debtRecord.setNote(note);
                    debtRecord.setInvoiceDate(invoiceDate);
                    debtRecord.setShopID(shopID);
                    debtRecord.setOrderID(id);

                    DAODebtRecords.INSTANCE.AddDebtRecords(debtRecord, user.getID());
                }
            }
            String[] productIDs = request.getParameterValues("productID");
            String[] productNames = request.getParameterValues("productName");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");
            String[] specs = request.getParameterValues("spec");
            String[] unitPrices = request.getParameterValues("unitPrice");

            if (productNames == null || quantities == null || prices == null || specs == null || unitPrices == null || productIDs == null) {
                out.println("<h3 style='color:red;'>Lỗi: Dữ liệu sản phẩm không đầy đủ.</h3>");
                return;
            }
            for (int i = 0; i < productNames.length; i++) {
                try {
                    int productID = Integer.parseInt(productIDs[i]);
                    String productName = productNames[i].trim();
                    int quantity = Integer.parseInt(quantities[i].trim());
                    int price = Integer.parseInt(prices[i].trim());
                    int spec = Integer.parseInt(specs[i].trim());
                    int unitPrice = Integer.parseInt(unitPrices[i].trim());
                    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

                    OrderItems orderItem = new OrderItems();
                    orderItem.setOrderID(id);
                    orderItem.setProductName(productName);
                    orderItem.setQuantity(quantity);
                    orderItem.setPrice(price);
                    orderItem.setUnitPrice(unitPrice);
                    orderItem.setDescription(String.valueOf(spec));
                    orderItem.setShopID(shopID);
                    orderItem.setCreateAt(today);
                    orderItem.setCreateBy(user.getID());

                    dao3.AddOrderItems(orderItem, user.getID());
                    dao2.updateProductQuantitydecre(productName, quantity, user.getShopID());

                } catch (NumberFormatException e) {
                    out.println("<h3 style='color:red;'>Lỗi định dạng số ở sản phẩm thứ " + (i + 1) + ": " + e.getMessage() + "</h3>");
                    return;
                }
            }

            // Nếu thành công, thông báo và chuyển hướng
            out.println("<h3 style='color:green;'>Thêm đơn hàng thành công!</h3>");
            response.sendRedirect("listorders");

        } catch (NumberFormatException e) {
            out.println("<h3 style='color:red;'>Lỗi: Dữ liệu đầu vào bị thiếu." + e.getMessage() + "</h3>");
            return;

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Lỗi hệ thống: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
            return;
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
