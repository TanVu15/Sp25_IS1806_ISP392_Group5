package Controller.orderservlet;

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

public class AddExportOrderServlet extends HttpServlet {

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

        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int productsPerPage = 5;

        ArrayList<Customers> customers = dao1.getCustomersByPage(currentPage, productsPerPage, user.getShopID());
        request.setAttribute("customers", customers);

        int totalProducts = dao2.getTotalProductsByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalProducts / productsPerPage);

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

        try {
            int shopID = user.getShopID();
            int customerID = Integer.parseInt(request.getParameter("customerId"));
            String totalCostRaw = request.getParameter("totalCostHidden");
            String orderTypeStr = request.getParameter("orderType");
            String paymentStatus = request.getParameter("paymentStatus");

            String[] productIDs = request.getParameterValues("productID");
            String[] productNames = request.getParameterValues("productName");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");
            String[] specs = request.getParameterValues("spec");
            String[] unitPrices = request.getParameterValues("unitPrice");

            // **Kiểm tra dữ liệu đầu vào trước khi xử lý**
            if (totalCostRaw == null || totalCostRaw.trim().isEmpty() ||
                orderTypeStr == null || orderTypeStr.trim().isEmpty() ||
                productIDs == null || productIDs.length == 0 ||
                productNames == null || productNames.length == 0 ||
                quantities == null || quantities.length == 0 ||
                prices == null || prices.length == 0 ||
                specs == null || specs.length == 0 ||
                unitPrices == null || unitPrices.length == 0) {

                // **Nếu có bất kỳ trường nào bị thiếu, load lại trang tạo hóa đơn với thông báo lỗi**
                request.setAttribute("message", "Vui lòng nhập đầy đủ thông tin hóa đơn.");
                doGet(request, response); // Sử dụng doGet để load lại dữ liệu cần thiết
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
                    orderItem.setQuantity(quantity*spec);
                    orderItem.setPrice(price);
                    orderItem.setUnitPrice(unitPrice);
                    orderItem.setDescription(String.valueOf(spec));
                    orderItem.setShopID(shopID);
                    orderItem.setCreateAt(today);
                    orderItem.setCreateBy(user.getID());

                    dao3.AddOrderItems(orderItem, user.getID());
                    dao2.updateProductQuantitydecre(productName, quantity*spec, user.getShopID());

                } catch (NumberFormatException e) {
                    request.setAttribute("message", "Lỗi định dạng số ở sản phẩm thứ " + (i + 1) + ": " + e.getMessage());
                    doGet(request, response); // Load lại trang với thông báo lỗi
                    return;
                }
            }

            response.sendRedirect("listorders");

        } catch (NumberFormatException e) {
           request.setAttribute("message", "Vui lòng nhập đúng định dạng số.");
            doGet(request, response);
            return;
        }
         catch (Exception e) {
            request.setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
            doGet(request, response); // Load lại trang với thông báo lỗi
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}