/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.userservlet;

import dal.DAOCustomers;
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
import model.Customers;

/**
 *
 * @author ADMIN
 */
public class RegisterServlet extends HttpServlet {

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
        DAOCustomers dao = new DAOCustomers();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        request.setAttribute("name", "");
        request.setAttribute("password", "");
        request.setAttribute("password2", "");
        request.setAttribute("fullname", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String fullname = request.getParameter("fullname");
        if (!password.endsWith(password2) || password == null || "".equals(name)) {
            request.setAttribute("message", "Hãy xem lại tài khoản và mật khẩu của bạn!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        DAOUser dao = new DAOUser();
        Users user = new Users();
        HttpSession session = request.getSession();
        user = (Users) session.getAttribute("user");
        try {

            //Check if username already exists
            if (dao.checkUsernameExists(name)) {
                request.setAttribute("user", user);
                request.setAttribute("message", "Tên đã được dùng, hãy sử dụng tên khác!");
                request.setAttribute("name", name);
                request.setAttribute("password", password);
                request.setAttribute("password2", password2);
                request.setAttribute("fullname", "");
                RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if (user != null) {
                Users userRegister = new Users();

                userRegister.setUsername(name);
                userRegister.setPasswordHash(password);
                userRegister.setFullName(fullname);
                userRegister.setRoleid(user.getRoleid() + 1);
                if (user.getRoleid() + 1 == 3) {
                    userRegister.setShopID(user.getShopID());
                } else {
                    userRegister.setShopID(0);
                }
                dao.Register(userRegister, user.getID());
                response.sendRedirect("listusers");
            } else {
                request.setAttribute("message", "Invalid role selected.");
                request.setAttribute("name", name);
                request.setAttribute("password", password);
                request.setAttribute("password2", password2);
                request.setAttribute("fullname", "");
                RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("name", name);
            request.setAttribute("password", password);
            request.setAttribute("password2", password2);
            request.setAttribute("fullname", "");
            RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
