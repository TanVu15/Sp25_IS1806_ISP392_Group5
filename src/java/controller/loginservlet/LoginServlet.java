/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.loginservlet;

import dal.DAO;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ADMIN
 */
public class LoginServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Login/login.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        try {
            if (name != null && password != null) {
                DAO userDAO = new DAO();
                Users user = userDAO.getUserByName(name);

                if (user != null && user.getPasswordHash().equals(password)) {
                    if (user.getIsDelete()!= 0) {  
                        request.setAttribute("errorMessage", "User is not exist.");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("Login/login.jsp");
                        dispatcher.forward(request, response);
                    } else {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);

                        if (user.getRoleid() == 1 ) {
                            response.sendRedirect("Admin/Admin.jsp");
                        } else if (user.getRoleid() == 2) {
                            response.sendRedirect("HomePage/ProductForOwner.jsp");
                        } else if (user.getRoleid() == 3) {
                            response.sendRedirect("Staff/Staff.jsp");
                        }
                    }
                } else {
                    request.setAttribute("errorMessage", "Invalid username or password.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("Login/login.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
                response.sendRedirect("Login/login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Something went wrong. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("Login/login.jsp");
            dispatcher.forward(request, response);
        }
        
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
