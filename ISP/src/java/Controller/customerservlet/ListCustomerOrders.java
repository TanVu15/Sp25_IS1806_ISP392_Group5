package Controller.customerservlet;

import Controller.debtrecordservlet.ListCustomerDebtRecordsServlet;
import dal.DAOCustomers;
import dal.DAOOrders;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Customers;
import model.Orders;
import model.Products;
import model.Shops;
import model.Users;

/**
 * Servlet xử lý danh sách đơn hàng của từng khách hàng với phân trang.
 */
public class ListCustomerOrders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOOrders dao = new DAOOrders();
        DAOCustomers dao1 = new DAOCustomers();
        HttpSession session = request.getSession();
        String sortBy = request.getParameter("sortBy");
        request.setAttribute("message", "");
        
                
        Customers customer;

        
          try {
              int customerID2 = Integer.parseInt(request.getParameter("customerid"));
            // lay customer đang cần
            customer = dao1.getCustomersByID(customerID2);
            Shops shop = (Shops) session.getAttribute("shop");
            if (shop.getID() != customer.getShopID() || customer == null) {
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            request.getRequestDispatcher("logout").forward(request, response);
            return;
        }
          //lay user người đang đăng nhập
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        // Ensure currentPage is always set, even if the parameter is null
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");

        int ordersPerPage = 10;

        // Lấy tổng số đơn hàng của khách hàng
        int customerID = Integer.parseInt(request.getParameter("customerid"));
        int totalOrders = dao1.getTotalOrdersByCustomerId(customerID);
        int totalPages = (int) Math.ceil((double) totalOrders / ordersPerPage);


        // Lấy danh sách đơn hàng của khách hàng theo phân trang
        ArrayList<Orders> orders = (ArrayList<Orders>) dao1.getOrdersByCustomerId(customerID, currentPage, ordersPerPage);
        // Xử lý sắp xếp
        if (sortBy != null) {
    switch (sortBy) {
        case "price_asc":
            orders.sort(Comparator.comparingDouble(Orders::getTotalAmount));
            break;
        case "price_desc":
            orders.sort(Comparator.comparingDouble(Orders::getTotalAmount).reversed());
            break;
        case "name_import":
            // Lọc đơn hàng có status = 1 (Nhập hàng)
            orders = new ArrayList<>(orders.stream()
                    .filter(order -> order.getStatus() == 1)
                    .collect(Collectors.toList()));
            break;
        case "name_export":
            // Lọc đơn hàng có status = -1 (Xuất hàng)
            orders = new ArrayList<>(orders.stream()
                    .filter(order -> order.getStatus() == -1)
                    .collect(Collectors.toList()));
            break;
    }
}



        // Thiết lập các thuộc tính cho JSP
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("customerId", customerID);
        request.setAttribute("sortBy", sortBy);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("CustomersManager/ListCustomerOrders.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String information = request.getParameter("information");
        
        DAOOrders dao = new DAOOrders();
        DAOCustomers dao1 = new DAOCustomers();
        HttpSession session = request.getSession();
        ArrayList<Orders> orders;
        int customerID = Integer.parseInt(request.getParameter("customerid"));
        
        Customers customer;
        try {
            // lay customer đang cần
            customer = dao1.getCustomersByID(customerID);
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            Logger.getLogger(ListCustomerOrders.class.getName()).log(Level.SEVERE, null, ex);
        }

        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        // Lấy `customerId` từ tham số request


        try {
            orders = dao.getOrdersBySearch(information, customerID); // Mặc định lấy trang đầu tiên
            if (orders == null || orders.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy đơn hàng nào.");
            }

            int currentPage = 1;
            int ordersPerPage = 10;

            // Cập nhật currentPage và totalPages
            int totalOrders = orders.size();
            int totalPages = (int) Math.ceil((double) totalOrders / ordersPerPage);

            // Thiết lập các thuộc tính cho JSP
            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("CustomersManager/ListCustomerOrders.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet hiển thị danh sách đơn hàng của từng khách hàng với phân trang";
    }
}
