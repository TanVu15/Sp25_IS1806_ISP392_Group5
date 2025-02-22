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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class UpdateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOUser dao = new DAOUser();

        request.setAttribute("message", "");
        request.setAttribute("username", "");
        request.setAttribute("password", "");
        request.setAttribute("password2", "");
        request.setAttribute("fullname", "");
        int userid = Integer.parseInt(request.getParameter("id"));

        try {
            Users user = dao.getUserByID(userid);
            request.setAttribute("user", user);
            request.getRequestDispatcher("UsersManager/UpdateUser.jsp").forward(request, response);
        } catch (Exception ex) {
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Lấy thông tin người dùng từ form
        int userid = Integer.parseInt(request.getParameter("id"));
        Users user;
        try {
            user = DAOUser.INSTANCE.getUserByID(userid);
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            

            String fullname = request.getParameter("fullname");
            request.setAttribute("user", user);
            // Cập nhật người dùng trong database

            user.setPasswordHash(password2);
            user.setFullName(fullname);

            if (!password.endsWith(password2) || password == null) {
                request.setAttribute("message", "Hãy kiểm tra lại mật khẩu!");
                request.setAttribute("username", username);
                request.setAttribute("password", password);
                request.setAttribute("password2", password2);
                request.setAttribute("fullname", fullname);
                request.setAttribute("user", user);
                RequestDispatcher dispatcher = request.getRequestDispatcher("UsersManager/UpdateUser.jsp");
                dispatcher.forward(request, response);
                return;
            }
            DAOUser.INSTANCE.updateUser(user);
            //nếu cập nhập user đang đăng nhập
            Users userSession = (Users) session.getAttribute("user");
            Users userSessionNew = DAOUser.INSTANCE.getUserByID(userSession.getID());
            session.removeAttribute("user");
            session.setAttribute("user", userSessionNew);
            // Chuyển hướng tới trang danh sách người dùng sau khi cập nhật thành công
            response.sendRedirect("listusers");
        } catch (Exception ex) {
            Logger.getLogger(UpdateUserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
