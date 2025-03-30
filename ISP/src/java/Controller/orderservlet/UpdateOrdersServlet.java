package Controller.orderservlet;

import dal.DAOOrders;
import model.Orders;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@MultipartConfig
public class UpdateOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOOrders dao = DAOOrders.INSTANCE;
        HttpSession session = request.getSession();
        

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Orders order = dao.getOrderByID(orderId);
            request.setAttribute("order", order);
            request.getRequestDispatcher("OrdersManager/UpdateOrders.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Không tìm thấy đơn hàng.");
            request.getRequestDispatcher("OrdersManager/UpdateOrders.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        int customerID = Integer.parseInt(request.getParameter("CustimerID"));
        int userID = Integer.parseInt(request.getParameter("UserrID"));
        int shopID = Integer.parseInt(request.getParameter("ShopID"));
        String totalamount = request.getParameter("amount");
        String statusString = request.getParameter("status");

        int status = 0;

        if (statusString != null) {
            try {
                status = Integer.parseInt(statusString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        int amount = 0;

        if (totalamount != null && !totalamount.isEmpty()) {
            amount = Integer.parseInt(totalamount);
            if (amount < 0) {
                request.setAttribute("errorMessage", "Giá trị không thể âm.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddOrders.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
        
        Orders order = new Orders();
        order.setID(orderId);
        order.setCustomerID(customerID);
        order.setShopID(shopID);
        order.setTotalAmount(amount);
        order.setStatus(status);
        DAOOrders.INSTANCE.updateOrders(order);
        
        response.sendRedirect("listorders");
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý cập nhật đơn hàng";
    }

}
