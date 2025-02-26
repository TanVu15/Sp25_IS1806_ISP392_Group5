/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.shopservlet;

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
public class UpdateShopServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet UpdateShopServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateShopServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
        
        if(user.getShopID()==0&&user.getRoleid()==2){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        request.setAttribute("user", user);
        Shops shop = new Shops();
        
        try {
            shop = daoShop.getShopByID(user.getShopID());
            request.setAttribute("shop", shop);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("ShopsManager/UpdateShop.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); // Log lỗi ra console
            response.getWriter().println("Error: " + ex.getMessage()); // Hiển thị lỗi trên trang
        }

    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
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
        }

        DAOShops daoShops = new DAOShops();
        Shops shop = new Shops(shopname, imageLink, email, location, user.getID());
        shop.setOwnerID(user.getID());
        daoShops.updateShopbyOwnerid(shop);

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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
