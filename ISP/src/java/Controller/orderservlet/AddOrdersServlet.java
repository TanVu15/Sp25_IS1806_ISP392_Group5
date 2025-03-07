package Controller.orderservlet;

import dal.DAOOrders;
import model.Orders;
import model.Users;
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
import java.io.PrintWriter;
import java.sql.Date;

public class AddOrdersServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddOrders.jsp");
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

        DAOOrders dao = DAOOrders.INSTANCE;
        Orders order = new Orders();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        try {
            if (user != null) {
                order.setCustomerID(customerID);;
                order.setUserID(userID);
                order.setTotalAmount(amount);
                order.setShopID(shopID);
                order.setStatus(status);
                order.setCreateAt(new Date(System.currentTimeMillis()));
                order.setUserID(user.getID());

                dao.addOrders(order, user.getID());
                response.sendRedirect("listorders");
            } else {
                request.setAttribute("errorMessage", "Thông tin không phù hợp.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddOrders.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("OrdersManager/AddOrders.jsp");
            dispatcher.forward(request, response);
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
