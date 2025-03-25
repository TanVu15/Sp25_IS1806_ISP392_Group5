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
import java.util.Arrays;
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

        DAOOrders dao = new DAOOrders();
        DAOCustomers dao1 = new DAOCustomers();
        DAOProducts dao2 = new DAOProducts();
        DAOOrderItem dao3 = new DAOOrderItem();
        DAOZones dao4 = new DAOZones();

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
            request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
            return;
        }

        int totalCost = Integer.parseInt(totalCostRaw.replace(".", "").trim());
        int status = Integer.parseInt(orderTypeStr);

        int customerID = dao1.getCustomerIdByNameAndShop(customerName.trim(), shopID);
        if (customerID == -1) {
            request.setAttribute("message", "Không tìm thấy khách hàng với tên: " + customerName);
            request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
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
            String note = "Ghi nợ từ hóa đơn chủ cửa hàng nhập kho ";
            java.sql.Date invoiceDate = new java.sql.Date(System.currentTimeMillis());

            if ("partial".equals(paymentStatus)) {
                String partialPaymentStr = request.getParameter("partialPayment");
                int partialPayment = Integer.parseInt(partialPaymentStr);
                amountOwed = totalCost - partialPayment;
                note = "Chủ cửa hàng thanh toán một phần từ hóa đơn nhập kho ";
            } else {
                amountOwed = totalCost;
            }

            if (amountOwed > 0) {
                DebtRecords debtRecord = new DebtRecords();
                debtRecord.setCustomerID(customerID);
                debtRecord.setAmountOwed(amountOwed);
                debtRecord.setPaymentStatus(2);
                debtRecord.setNote(note);
                debtRecord.setInvoiceDate(invoiceDate);
                debtRecord.setShopID(shopID);
                debtRecord.setOrderID(id);

                try {
                    DAODebtRecords.INSTANCE.AddDebtRecords(debtRecord, user.getID());
                } catch (Exception ex) {
                    Logger.getLogger(AddImportOrderServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

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

        // vòng for here!
        for (int i = 0; i < productNames.length; i++) {
            // Kiểm tra từng phần tử không được null hoặc rỗng

            if (productNames[i].trim().isEmpty()
                    || quantities[i].trim().isEmpty()
                    || prices[i].trim().isEmpty()
                    || discounts[i].trim().isEmpty()
                    || spec[i].trim().isEmpty()) {

                out.println("<h3 style='color:red;'>Lỗi1: Thiếu thông tin sản phẩm thứ " + (i + 1) + ".</h3>");
                return;
            }

            // Chuyển đổi dữ liệu từ chuỗi sang số
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
            dao2.updateProductQuantity(productName, quantity, user.getShopID());

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

        }

        // Chuyển hướng về danh sách đơn hàng
        response.sendRedirect("listorders");

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
