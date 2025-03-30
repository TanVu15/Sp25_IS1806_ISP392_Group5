package Controller.orderservlet;



import dal.DAOCustomers;
import dal.DAOOrderItem;
import dal.DAOOrders;
import dal.DAOProducts;
import dal.DAOZones;
import dal.DAODebtRecords;
import dal.DAOShops;
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
import jdk.jfr.Description;
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
        DAOShops dao3 = new DAOShops();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        
        int shopId = user.getShopID();
        // Lấy trang hiện tại từ tham số URL, mặc định là 1
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int productsPerPage = 5; // Số sản phẩm trên mỗi trang

        ArrayList<Customers> customers = dao1.getCustomersByPage(currentPage, productsPerPage, user.getShopID());
        request.setAttribute("customers", customers);

        // Lấy tổng số sản phẩm cho shop hiện tại
        int totalProducts = dao2.getTotalProductsByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalProducts / productsPerPage);

        // Lấy danh sách sản phẩm cho trang hiện tại
        ArrayList<Products> products = dao2.getProductsByShopId(shopId);
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

    // Kiểm tra các trường bắt buộc
    if (customerName == null || customerName.trim().isEmpty()
            || totalCostRaw == null || totalCostRaw.trim().isEmpty()
            || orderTypeStr == null || orderTypeStr.trim().isEmpty()) {
        request.setAttribute("message", "Vui lòng nhập đầy đủ thông tin hóa đơn.");
        response.sendRedirect("addimportorder");
        return;
    }

    // Chuyển đổi dữ liệu
    int totalCost;
    int status;
    try {
        totalCost = Integer.parseInt(totalCostRaw.replace(".", "").trim());
        status = Integer.parseInt(orderTypeStr);
    } catch (NumberFormatException e) {
        request.setAttribute("message", "Số tiền hoặc trạng thái đơn hàng không hợp lệ.");
        request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
        return;
    }

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
        String note = "Ghi nợ từ hóa đơn nhập kho ";
        java.sql.Date invoiceDate = new java.sql.Date(System.currentTimeMillis());

        try {
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

                DAODebtRecords.INSTANCE.AddDebtRecords(debtRecord, user.getID());
            }
        } catch (Exception e) {
            request.setAttribute("message", "Lỗi khi xử lý thanh toán: " + e.getMessage());
            request.getRequestDispatcher("OrdersManager/AddImportOrder.jsp").forward(request, response);
            return;
        }
    }

    // Lấy danh sách sản phẩm từ form
    String[] productNames = request.getParameterValues("productName");
    String[] quantities = request.getParameterValues("quantity");
    String[] prices = request.getParameterValues("price");
    String[] spec = request.getParameterValues("spec");
    String[] discounts = request.getParameterValues("discount");
    String[] zoneNames = request.getParameterValues("area");
    String[] zoneCounts = request.getParameterValues("zoneCount");

    if (productNames == null || quantities == null || prices == null || discounts == null || spec == null) {
        request.setAttribute("message", "Vui lòng nhập đầy đủ thông tin sản phẩm.");
       response.sendRedirect("addimportorder");
        return;
    }

    int zoneIndex = 0;

    for (int i = 0; i < productNames.length; i++) {
        try {
            if (productNames[i].trim().isEmpty()
                    || quantities[i].trim().isEmpty()
                    || prices[i].trim().isEmpty()
                    || discounts[i].trim().isEmpty()
                    || spec[i].trim().isEmpty()) {
                throw new IllegalArgumentException("Thiếu thông tin sản phẩm thứ " + (i + 1));
            }

            String productName = productNames[i].trim();
            int quantity = Integer.parseInt(quantities[i].trim());
            int price = Integer.parseInt(prices[i].trim());
            int discount = Integer.parseInt(discounts[i].trim());
            int spec1 = Integer.parseInt(spec[i].trim());
            int pId = dao2.getProductIdByNameAndShop(productName, user.getShopID());

            int oldImportPrice = dao2.getImportPrice(pId);
            dao2.updateImportPrice(pId, price);

            if (oldImportPrice == -1 || oldImportPrice != price) {
                dao2.logPriceChange1(pId, price, "import", user.getID(), customerID);
            }

            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            OrderItems orderItem = new OrderItems();
            orderItem.setOrderID(id);
            orderItem.setProductName(productName);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price);
            orderItem.setUnitPrice(discount);
            orderItem.setDescription(spec[i].trim());
            orderItem.setShopID(shopID);
            orderItem.setCreateAt(today);
            orderItem.setCreateBy(user.getID());

            dao3.AddOrderItems(orderItem, user.getID());
            dao2.updateProductQuantity(productName, quantity * spec1, user.getShopID());

            int zoneCount = Integer.parseInt(zoneCounts[i].trim());

            if (zoneNames != null && zoneCount > 0) {
                for (int j = 0; j < zoneCount; j++) {
                    if (zoneIndex >= zoneNames.length) {
                        break;
                    }
                    String zoneName = zoneNames[zoneIndex].trim();
                    dao4.updateZoneImportOrder(zoneName, pId, shopID);
                    zoneIndex++;
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Lỗi định dạng dữ liệu sản phẩm: " + e.getMessage());
            response.sendRedirect("addimportorder");
            return;
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            response.sendRedirect("addimportorder");
            return;
        }
    }

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
