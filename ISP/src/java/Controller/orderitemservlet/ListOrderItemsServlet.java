package Controller.orderitemservlet;

import dal.DAOCustomers;
import dal.DAOOrderItem;
import dal.DAOOrders;
import dal.DAOUser;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customers;
import model.OrderItems;
import model.Orders;
import model.Users;

public class ListOrderItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        String orderIdStr = request.getParameter("id");
        int orderId;

        try {
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new NumberFormatException("orderId không hợp lệ");
            }
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Mã đơn hàng không hợp lệ.");
            request.setAttribute("orderitems", new ArrayList<OrderItems>());
            RequestDispatcher dispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
            dispatcher.forward(request, response);
            return;
        }

        fetchOrderDetails(request, response, orderId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        String orderIdStr = request.getParameter("orderid");
        int orderId;

        try {
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                orderIdStr = request.getParameter("id");
            }
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new NumberFormatException("orderId không hợp lệ");
            }
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Mã đơn hàng không hợp lệ.");
            request.setAttribute("orderitems", new ArrayList<OrderItems>());
            RequestDispatcher dispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
            dispatcher.forward(request, response);
            return;
        }

        fetchOrderDetails(request, response, orderId);
    }

    private void fetchOrderDetails(HttpServletRequest request, HttpServletResponse response, int orderId)
            throws ServletException, IOException {
        DAOOrderItem daoOrderItem = new DAOOrderItem();
        DAOOrders daoOrders = new DAOOrders();
        DAOCustomers daoCustomers = new DAOCustomers();
        DAOUser daoUser = new DAOUser();

        request.setAttribute("orderId", orderId);

        try {
            Orders order = daoOrders.getOrderByID(orderId);
            if (order == null) {
                request.setAttribute("message", "Không tìm thấy đơn hàng với ID: " + orderId);
                request.setAttribute("orderitems", new ArrayList<OrderItems>());
            } else {
                request.setAttribute("order", order);
                Customers customer = daoCustomers.getCustomersByID(order.getCustomerID());
                request.setAttribute("customer", customer);

                // Lấy tất cả OrderItems
                ArrayList<OrderItems> allOrderItems = daoOrderItem.getAllOrderItemsByOrderID(orderId);
                request.setAttribute("allOrderItems", allOrderItems);

                // Lấy số điện thoại từ Customers
                request.setAttribute("phoneNumber", customer != null && customer.getPhone() != null ? customer.getPhone() : "Không xác định");

                // Lấy ngày tạo từ Orders (giả định Orders có trường CreateAt)
                request.setAttribute("orderCreateAt", order.getCreateAt() != null ? order.getCreateAt().toString() : "Không xác định");
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Đã xảy ra lỗi: " + ex.getMessage());
            request.setAttribute("orderitems", new ArrayList<OrderItems>());
            ex.printStackTrace();
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
        requestDispatcher.forward(request, response);
    }
}
