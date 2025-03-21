/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.zonesservlet;

import dal.DAOShops;
import dal.DAOZones;
import model.Zones;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class ListZonesServlet extends HttpServlet {

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
            out.println("<title>Servlet ListZones</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListZones at " + request.getContextPath() + "</h1>");
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
        DAOZones dao = new DAOZones();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        DAOShops daoShop = new DAOShops();
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }

        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int zonesPerPage = 5; // Số sản phẩm trên mỗi trang

        // Lấy tổng số sản phẩm cho shop hiện tại
        int totalZones = dao.getTotalZonesByShopId(user.getShopID());
        int totalPages = (int) Math.ceil((double) totalZones / zonesPerPage);

        // Lấy danh sách sản phẩm cho trang hiện tại
        ArrayList<Zones> zones = dao.getZonesByPage(currentPage, zonesPerPage, user.getShopID());

        // Thiết lập các thuộc tính cho JSP
        request.setAttribute("zones", zones);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("ZoneManager/ListZones.jsp");
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
        String information = request.getParameter("information");
        int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int zonesPerPage = 5;

        DAOZones dao = new DAOZones();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        ArrayList<Zones> allMatchedZones;
        try {
            // Get all matching zones
            allMatchedZones = dao.getZonesBySearch(information);

            // Filter zones by shop ID
            ArrayList<Zones> shopZones = new ArrayList<>();
            for (Zones zone : allMatchedZones) {
                if (zone.getShopID() == user.getShopID()) {
                    shopZones.add(zone);
                }
            }

            if (shopZones.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy khu vực nào.");
                request.setAttribute("zones", new ArrayList<Zones>());
                request.setAttribute("currentPage", 1);
                request.setAttribute("totalPages", 0);
            } else {
                // Paginate the results
                int totalZones = shopZones.size();
                int totalPages = (int) Math.ceil((double) totalZones / zonesPerPage);

                // Calculate start and end indices for the current page
                int startIndex = (currentPage - 1) * zonesPerPage;
                int endIndex = Math.min(startIndex + zonesPerPage, totalZones);

                // Get the subset of zones for the current page
                ArrayList<Zones> pagedZones = new ArrayList<>();
                for (int i = startIndex; i < endIndex; i++) {
                    pagedZones.add(shopZones.get(i));
                }

                request.setAttribute("message", "Kết quả tìm kiếm cho: " + information);
                request.setAttribute("zones", pagedZones);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);

                // Store search term in session for pagination links
                session.setAttribute("searchTerm", information);
            }
        } catch (Exception ex) {
            Logger.getLogger(ListZonesServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("message", "Đã xảy ra lỗi khi tìm kiếm.");
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("ZoneManager/ListZones.jsp");
        requestDispatcher.forward(request, response);
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
