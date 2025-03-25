/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.userservlet;

import dal.DAOUser;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class UserDetailServlet extends HttpServlet {

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
        DAOUser dao = new DAOUser();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        int userid = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("user", user);
        Users users = new Users();
        try {
            users = dao.getUserByID(userid);
            if (user.getRoleid() != 1) {
                int shopid = users.getShopID();
                int shopid2 = user.getShopID();
                if (users.getRoleid() > user.getRoleid()) {
                    request.getRequestDispatcher("logout").forward(request, response);
                    return;
                }

                if ((user.getRoleid() == 3) && users.getRoleid() != user.getRoleid()) {
                    request.getRequestDispatcher("logout").forward(request, response);
                    return;
                }

                if (shopid != shopid2 && user.getRoleid() != 1) {
                    request.getRequestDispatcher("logout").forward(request, response);
                    return;
                }
            }
            request.setAttribute("users", users);
        } catch (Exception ex) {
            Logger.getLogger(UserDetailServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("UsersManager/UserDetails.jsp");
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
