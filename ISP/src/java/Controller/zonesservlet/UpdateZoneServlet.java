/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.zonesservlet;

import dal.DAOZones;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customers;
import model.Shops;
import model.Zones;

/**
 *
 * @author ASUS
 */
public class UpdateZoneServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateZoneServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateZoneServlet at " + request.getContextPath() + "</h1>");
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
        

        try {
            int zoneId = Integer.parseInt(request.getParameter("id"));
            Zones zone = dao.getZonesByID(zoneId);
            Shops shop = (Shops) session.getAttribute("shop");
            if (shop.getID() != zone.getShopID()) {
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("z", zone);
            request.getRequestDispatcher("ZoneManager/UpdateZones.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); // In lỗi ra console

            request.getRequestDispatcher("logout").forward(request, response);
                return;
        }
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
        HttpSession session = request.getSession();
        int zoneId = Integer.parseInt(request.getParameter("id"));
        String zoneName = request.getParameter("zone");
        String description = request.getParameter("description"); // Lấy giá trị từ form
        Shops shop1 = (Shops) session.getAttribute("shop");
        int shop = shop1.getID();

        if ("".equals(zoneName)) {
            request.setAttribute("message", "Hãy xem lại!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("ZoneManager/UpdateZones.jsp");
            dispatcher.forward(request, response);
            return;
        }
        Zones zone = new Zones();
        zone.setID(zoneId);
        zone.setDescription(description); // Sử dụng giá trị từ form thay vì chuỗi cứng "description"
        zone.setZoneName(zoneName);
        zone.setShopID(shop); // Thêm shopID để đảm bảo liên kết với shop đúng

        DAOZones.INSTANCE.updateZones(zone);

        // Chuyển hướng tới trang danh sách người dùng sau khi cập nhật thành công
        response.sendRedirect("listzones");
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
