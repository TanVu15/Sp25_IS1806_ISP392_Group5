/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.userservlet;

import dal.DAOUser;
import model.Users;
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

/**
 *
 * @author ADMIN
 */
public class ListUsersServlet extends HttpServlet {

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
            out.println("<title>Servlet ListUsersServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListUsersServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOUser dao = new DAOUser();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        ArrayList<Users> users = dao.getUsers();
        request.setAttribute("users", users);
        if (session.getAttribute("user") != null) {
            if (user.getRoleid() == 1) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("UsersManager/ListUsers.jsp");
                requestDispatcher.forward(request, response);
            } else if (user.getRoleid() == 2) {
                if (user.getShopID() == 0) {
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("ShopsManager/CreateShop.jsp");
                    requestDispatcher.forward(request, response);
                    return;
                } else {
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("UsersManager/ListUsersForOwner.jsp");
                    requestDispatcher.forward(request, response);
                }
            } 
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
        response.setContentType("text/html;charset=UTF-8");
        String information = request.getParameter("information");

        DAOUser dao = new DAOUser();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        ArrayList<Users> users;
        try {
            users = dao.getUsersBySearch(information);
            if (users == null || users.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy kết quả nào.");
                users = dao.getUsers();
                request.setAttribute("users", users);
            } else {
                request.setAttribute("users", users);
            }
        } catch (Exception ex) {
            Logger.getLogger(ListUsersServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("UsersManager/ListUsers.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static void main(String[] args) throws Exception {
        System.out.println(DAOUser.INSTANCE.getUserByID(2).getShopID());

    }
}
