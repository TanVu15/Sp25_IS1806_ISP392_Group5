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

            // Lấy dữ liệu từ form
            String customerName = request.getParameter("customerName");
            String totalCostRaw = request.getParameter("totalCost");
            String orderTypeStr = request.getParameter("orderType");
            String paymentStatus = request.getParameter("paymentStatus");

            if (customerName == null || customerName.trim().isEmpty()
                    || totalCostRaw == null || totalCostRaw.trim().isEmpty()
                    || orderTypeStr == null || orderTypeStr.trim().isEmpty()) {
                request.setAttribute("message", "Vui lòng nhập đầy đủ thông tin hóa đơn.");
                request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp").forward(request, response);
                return;
            }

            int totalCost = Integer.parseInt(totalCostRaw.replace(".", "").trim());
            int status = Integer.parseInt(orderTypeStr);

            int customerID = dao1.getCustomerIdByNameAndShop(customerName.trim(), shopID);
            if (customerID == -1) {
                request.setAttribute("message", "Không tìm thấy khách hàng với tên: " + customerName);
                request.getRequestDispatcher("OrdersManager/AddExportOrder.jsp").forward(request, response);
                return;
            }

            // Tạo order mới
            Orders order = new Orders();
            order.setCustomerID(customerID);
            order.setTotalAmount(totalCost);
            order.setShopID(shopID);
            order.setStatus(status);

            // Lưu order vào DB và lấy ID
            int id = dao.addOrdersreturnID(order, user.getID());

            // Xử lý thanh toán nếu có ghi nợ
            if ("partial".equals(paymentStatus) || "none".equals(paymentStatus)) {
                int amountOwed;
                String note = "Ghi nợ hóa đơn khách mua hàng ";
                java.sql.Date invoiceDate = new java.sql.Date(System.currentTimeMillis());

                if ("partial".equals(paymentStatus)) {
                    String partialPaymentStr = request.getParameter("partialPayment");
                    int partialPayment = Integer.parseInt(partialPaymentStr);
                    amountOwed = totalCost - partialPayment;
                    note = "Khách thanh toán một phần từ hóa đơn ";
                } else {
                    amountOwed = totalCost;
                }

                if (amountOwed > 0) {
                    DebtRecords debtRecord = new DebtRecords();
                    debtRecord.setCustomerID(customerID);
                    debtRecord.setAmountOwed(amountOwed);
                    debtRecord.setPaymentStatus(-1);
                    debtRecord.setNote(note);
                    debtRecord.setInvoiceDate(invoiceDate);
                    debtRecord.setShopID(shopID);

                    DAODebtRecords.INSTANCE.AddDebtRecords(debtRecord, user.getID());
                }
            }

            try {
                // Lấy danh sách sản phẩm từ form
                String[] productNames = request.getParameterValues("productName");
                String[] quantities = request.getParameterValues("quantity");
                String[] prices = request.getParameterValues("price");
                String[] spec = request.getParameterValues("spec");
                String[] discounts = request.getParameterValues("discount");
                String[] zoneNames = request.getParameterValues("area");
                String[] zoneCounts = request.getParameterValues("zoneCount"); // Lấy số lượng khu vực
                int zoneIndex = 0;

                // Kiểm tra dữ liệu đầu vào
                if (productNames == null || quantities == null || prices == null || discounts == null || spec == null) {
                    out.println("<h3 style='color:red;'>Lỗi: Dữ liệu đầu vào bị thiếu.</h3>");
                    return;
                }

                for (int i = 0; i <= productNames.length; i++) {
                    // Kiểm tra từng phần tử không được null hoặc rỗng
                    if (productNames[i].trim().isEmpty()
                            || quantities[i].trim().isEmpty() || prices[i].trim().isEmpty() || discounts[i].trim().isEmpty() || spec[i].trim().isEmpty()) {

                        out.println("<h3 style='color:red;'>Lỗi: Thiếu thông tin sản phẩm thứ " + (i + 1) + ".</h3>");
                        return;
                    }

                    // Chuyển đổi dữ liệu từ chuỗi sang số
                    try {
                        String productName = productNames[i].trim();
                        int quantity = Integer.parseInt(quantities[i].trim());
                        int price = Integer.parseInt(prices[i].trim());
                        String decription = spec[i].trim();
                        int discount = Integer.parseInt(discounts[i].trim());
                        int pId = dao2.getProductIdByNameAndShop(productName, user.getShopID());

                        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                        // Tạo đối tượng OrderItems
                        OrderItems orderItem = new OrderItems();
                        orderItem.setOrderID(id);
                        orderItem.setProductName(productName);
                        orderItem.setQuantity(quantity);
                        orderItem.setPrice(price);
                        orderItem.setUnitPrice(discount); // Đảm bảo đơn giá hợp lệ
                        orderItem.setDescription(decription);
                        orderItem.setShopID(shopID);
                        orderItem.setCreateAt(today);
                        orderItem.setCreateBy(user.getID());

                        // Thêm vào database
                        dao3.AddOrderItems(orderItem, user.getID());

                        // Cập nhật số lượng sản phẩm trong kho
                        dao2.updateProductQuantitydecre(productName, quantity, user.getShopID());

                        // 🔹 **Xử lý nhiều khu vực**
                        int zoneCount = Integer.parseInt(zoneCounts[i].trim());

                        // Cập nhật zoneCount vào sản phẩm nếu cần
                        if (zoneNames != null && zoneCount > 0) {
                            for (int j = 0; j < zoneCount; j++) {
                                if (zoneIndex >= zoneNames.length) {
                                    break; // Đảm bảo không vượt quá mảng
                                }
                                String zoneName = zoneNames[zoneIndex].trim();
                                dao4.updateZoneImportOrder(zoneName, pId, shopID);
                                zoneIndex++; // Chuyển sang khu vực tiếp theo
                            }
                        }

                    } catch (NumberFormatException e) {
                        out.println("<h3 style='color:red;'>Lỗi định dạng số ở sản phẩm thứ " + (i + 1) + ": " + e.getMessage() + "</h3>");
                        return;
                    }
                }

                // Nếu thành công, thông báo và chuyển hướng
                out.println("<h3 style='color:green;'>Thêm đơn hàng thành công!</h3>");
//            response.setHeader("Refresh", "2; URL=OrdersManager/ListOrder.jsp");

            } catch (Exception e) {
                out.println("<h3 style='color:red;'>Lỗi hệ thống: " + e.getMessage() + "</h3>");
                e.printStackTrace(out); // In chi tiết lỗi lên trình duyệt để debug
            }

            // Chuyển hướng về danh sách đơn hàng
            response.sendRedirect("listorders");
        } catch (NumberFormatException e) {
            // request.setAttribute("message", "3 ");
            //request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
            // return;
            out.println("<h3 style='color:red;'>Lỗi:2 Dữ liệu đầu vào bị thiếu.</h3>");
            return;

        } catch (Exception e) {
            //request.setAttribute("message", "4" );
            //request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
            //return;
            out.println("<h3 style='color:red;'>Lỗi:3 Dữ liệu đầu vào bị thiếu.</h3>");
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
