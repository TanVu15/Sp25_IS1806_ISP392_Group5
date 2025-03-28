/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.shopservlet;

import dal.DAOProducts;
import dal.DAOShops;
import dal.DAOUser;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.Date;
import model.Products;
import model.Shops;
import model.Users;
import jakarta.servlet.annotation.MultipartConfig; // Thêm import này
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)

/**
 *
 * @author Admin
 */
public class CreateShopServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateShopServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateShopServlet at " + request.getContextPath() + "</h1>");
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
        DAOShops daoShop = new DAOShops();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        request.setAttribute("message", "");
        if (user.getShopID() != 0 || user.getRoleid() == 3) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("logout");
            requestDispatcher.forward(request, response);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("ShopsManager/CreateShop.jsp");
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
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        String shopname = request.getParameter("shopname");
        Part filePart = request.getPart("logo");
        String email = request.getParameter("email");
        String location = request.getParameter("location");
        String phone = request.getParameter("phone");
        String bankacc = request.getParameter("bankacc");

        if ("".equals(shopname) || "".equals(email) || "".equals(location)) {
            request.setAttribute("message", "Hãy kiểm tra lại!");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("ShopsManager/CreateShop.jsp");
            requestDispatcher.forward(request, response);
            return;
        }

        String imageLink = "";
        String imageDirectory = getServletContext().getRealPath("/Image/");

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            File dir = new File(imageDirectory);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, fileName);
            filePart.write(file.getAbsolutePath());
            imageLink = "Image/" + fileName;
        } else {
            imageLink = "Image/logo.png";
        }

        DAOShops daoShops = new DAOShops();
        Shops shop = new Shops(shopname, imageLink, email, location,phone,bankacc, user.getID());
        try {
            daoShops.createShop(shop, user.getID());
        } catch (Exception ex) {
            request.setAttribute("message", "Hãy kiểm tra lại email do đã tồn tại email này!");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("ShopsManager/CreateShop.jsp");
            requestDispatcher.forward(request, response);
        }

        try {
            shop = daoShops.getShopByOwnerID(user.getID());
            session.setAttribute("shop", shop);
        } catch (Exception ex) {
            Logger.getLogger(CreateShopServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int shopid = daoShops.getShopByOwnerID(user.getID()).getID();
            user.setShopID(shopid);
            DAOUser.INSTANCE.updateUserShopId(user);
            session.removeAttribute("user");
            session.setAttribute("user", DAOUser.INSTANCE.getUserByID(user.getID()));
        } catch (Exception ex) {
        }

        response.sendRedirect("shopdetail");
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
