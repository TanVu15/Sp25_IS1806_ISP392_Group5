/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.orderservlet;

import dal.DAOOrders;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Orders;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class ListOrdersServlet extends HttpServlet {

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
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }

        // Lấy trang hiện tại, mặc định là 1
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int ordersPerPage = 10;

        // Lấy status từ request
        String statusParam = request.getParameter("status");
        Integer status = null;
        if (statusParam != null && !statusParam.isEmpty()) {
            status = Integer.parseInt(statusParam);
        }

        // Lấy searchInfo từ request hoặc session
        String searchInfo = request.getParameter("information");
        String sessionSearchInfo = (String) session.getAttribute("searchInfo");
        if (searchInfo == null) {
            searchInfo = sessionSearchInfo;
        }

        ArrayList<Orders> orders;
        int totalOrders;
        int totalPages;

        if (searchInfo != null && !searchInfo.trim().isEmpty()) {
            try {
                orders = dao.getOrdersBySearch(searchInfo);
                totalOrders = orders.size();
                totalPages = (int) Math.ceil((double) totalOrders / ordersPerPage);

                // Áp dụng phân trang
                int start = (currentPage - 1) * ordersPerPage;
                int end = Math.min(start + ordersPerPage, totalOrders);

                if (start < totalOrders) {
                    orders = new ArrayList<>(orders.subList(start, end));
                } else {
                    orders = new ArrayList<>();
                    currentPage = 1;
                }
                session.setAttribute("searchInfo", searchInfo); // Duy trì trạng thái tìm kiếm
            } catch (Exception e) {
                e.printStackTrace();
                orders = new ArrayList<>();
                totalOrders = 0;
                totalPages = 1;
            }
        } else {
            session.removeAttribute("searchInfo"); // Xóa khi không tìm kiếm
            if (status != null) {
                totalOrders = dao.getTotalOrdersByShopIdAndStatus(user.getShopID(), status);
                orders = dao.getOrdersByPageAndStatus(currentPage, ordersPerPage, user.getShopID(), status);
            } else {
                totalOrders = dao.getTotalOrdersByShopId(user.getShopID());
                orders = dao.getOrdersByPage(currentPage, ordersPerPage, user.getShopID());
            }
            totalPages = (int) Math.ceil((double) totalOrders / ordersPerPage);
        }

        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("selectedStatus", status);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("OrdersManager/ListOrders.jsp");
        requestDispatcher.forward(request, response);
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
        String information = request.getParameter("information"); // Lấy giá trị từ form tìm kiếm
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        // Lưu thông tin tìm kiếm vào session
        if (information != null && !information.trim().isEmpty()) {
            session.setAttribute("searchInfo", information);
            // Kiểm tra kết quả tìm kiếm để đặt thông báo
            DAOOrders dao = new DAOOrders();
            try {
                ArrayList<Orders> orders = dao.getOrdersBySearch(information);
                if (orders == null || orders.isEmpty()) {
                    session.setAttribute("message", "Không tìm thấy kết quả nào.");
                } else {
                    session.setAttribute("message", "Kết quả tìm kiếm cho: " + information);
                }
            } catch (Exception e) {
                session.setAttribute("message", "Có lỗi xảy ra khi tìm kiếm.");
            }
        } else {
            session.removeAttribute("searchInfo");
            session.removeAttribute("message"); // Xóa thông báo nếu không tìm kiếm
        }

        // Chuyển hướng về doGet
        response.sendRedirect("listorders?page=1"
                + (information != null && !information.trim().isEmpty() ? "&information=" + java.net.URLEncoder.encode(information, "UTF-8") : ""));
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

    public static void main(String[] args) throws Exception {
        DAOOrders dao = new DAOOrders();
        System.out.println(dao.getOrderByID(4));
    }
}
