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
        DAOUser dao = new DAOUser();
        Users user = new Users();
        HttpSession session = request.getSession();
        user = (Users) session.getAttribute("user");

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
        if (!password.endsWith(password2)) {
            request.setAttribute("errorMessage", "Please check again your password. Please enter a different name.");
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
                request.setAttribute("errorMessage", "Username already exists. Please enter a different name.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if (user != null) {
                Users userRegister = new Users();

                userRegister.setUsername(name);
                userRegister.setPasswordHash(password);
                userRegister.setRoleid(user.getRoleid() + 1);

                dao.Register(userRegister, user.getID());
                response.sendRedirect("listusers");
            } else {
                request.setAttribute("errorMessage", "Invalid role selected.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/register.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
