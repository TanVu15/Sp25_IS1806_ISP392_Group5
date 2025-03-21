/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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

/**
 *
 * @author ASUS
 */
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
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int itemsPerPage = 5;

        try {
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new NumberFormatException("orderId không hợp lệ");
            }
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Mã đơn hàng không hợp lệ.");
            request.setAttribute("orderitems", new ArrayList<OrderItems>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 0);
            RequestDispatcher dispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
            dispatcher.forward(request, response);
            return;
        }

        fetchOrderDetails(request, response, orderId, null, currentPage, itemsPerPage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        String orderIdStr = request.getParameter("orderid");
        String information = request.getParameter("information");
        int orderId;
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int itemsPerPage = 5;

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
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 0);
            RequestDispatcher dispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
            dispatcher.forward(request, response);
            return;
        }

        fetchOrderDetails(request, response, orderId, information, currentPage, itemsPerPage);
    }

    private void fetchOrderDetails(HttpServletRequest request, HttpServletResponse response, int orderId, String information, int currentPage, int itemsPerPage)
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
                request.setAttribute("currentPage", 1);
                request.setAttribute("totalPages", 0);
            } else {
                request.setAttribute("order", order);
                Customers customer = daoCustomers.getCustomersByID(order.getCustomerID());
                request.setAttribute("customer", customer);

                // Lấy toàn bộ OrderItems cho pop-up (không lọc tìm kiếm)
                ArrayList<OrderItems> allOrderItems = daoOrderItem.getAllOrderItemsByOrderID(orderId);
                request.setAttribute("allOrderItems", allOrderItems);

                // Lấy OrderItems cho phân trang (có thể lọc theo tìm kiếm)
                ArrayList<OrderItems> filteredOrderItems = information == null || information.trim().isEmpty()
                        ? allOrderItems
                        : daoOrderItem.searchProductByOrderID(orderId, information);

                if (filteredOrderItems == null || filteredOrderItems.isEmpty()) {
                    request.setAttribute("message", information == null ? "" : "Không tìm thấy sản phẩm nào khớp với: " + information);
                    request.setAttribute("orderitems", new ArrayList<OrderItems>());
                    request.setAttribute("currentPage", 1);
                    request.setAttribute("totalPages", 0);
                } else {
                    int totalItems = filteredOrderItems.size();
                    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

                    int startIndex = (currentPage - 1) * itemsPerPage;
                    int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

                    ArrayList<OrderItems> pagedOrderItems = new ArrayList<>();
                    for (int i = startIndex; i < endIndex; i++) {
                        pagedOrderItems.add(filteredOrderItems.get(i));
                    }

                    request.setAttribute("orderitems", pagedOrderItems);
                    request.setAttribute("currentPage", currentPage);
                    request.setAttribute("totalPages", totalPages);

                    if (information != null && !information.trim().isEmpty()) {
                        request.getSession().setAttribute("searchTerm", information);
                        request.setAttribute("message", "Kết quả tìm kiếm cho: " + information);
                    } else {
                        request.getSession().removeAttribute("searchTerm");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Đã xảy ra lỗi: " + ex.getMessage());
            request.setAttribute("orderitems", new ArrayList<OrderItems>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 0);
            ex.printStackTrace();
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrderItemsManager/ListOrderItems.jsp");
        requestDispatcher.forward(request, response);
    }
}