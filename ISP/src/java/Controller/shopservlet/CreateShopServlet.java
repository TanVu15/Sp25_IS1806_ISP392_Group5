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
        String shopname = request.getParameter("shopname");
        Part filePart = request.getPart("logo"); // Nhận tệp hình ảnh
        String email = request.getParameter("email");
        String location = request.getParameter("location");

        String imageLink = "";

        // Lấy đường dẫn thư mục ảnh
        String imageDirectory = getServletContext().getRealPath("/Image/");

        // Lưu ảnh vào thư mục
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            File dir = new File(imageDirectory);
            // Tạo thư mục nếu nó không tồn tại
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, fileName);
            filePart.write(file.getAbsolutePath());
            imageLink = "Image/" + fileName; // Đường dẫn lưu trữ ảnh
        }

        DAOShops dao = new DAOShops();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        Shops shop = new Shops();
        shop.setShopName(shopname);
        shop.setLogoShop(imageLink);
        shop.setEmail(email);
        shop.setLocation(location);
        shop.setCreatedBy(user.getID());
        dao.createShop(shop, user.getID());
        
        //
        
        //
        Users userSession = (Users) session.getAttribute("user");
        Users userSessionNew;
        int shopid;
        try {
            shopid = dao.getShopByOwnerID(userSession.getID()).getID();
            userSession.setShopID(shopid);
            DAOUser.INSTANCE.updateUser(userSession);
        } catch (Exception ex) {
            Logger.getLogger(CreateShopServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            userSessionNew = DAOUser.INSTANCE.getUserByID(userSession.getID());
            session.removeAttribute("user");
            session.setAttribute("user", userSessionNew);
            
        } catch (Exception ex) {
            Logger.getLogger(CreateShopServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Chuyển hướng tới trang danh sách người dùng sau khi cập nhật thành công
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("shopdetail");
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
