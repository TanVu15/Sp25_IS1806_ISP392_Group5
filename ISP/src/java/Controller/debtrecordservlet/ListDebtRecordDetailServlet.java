/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.debtrecordservlet;

import dal.DAODebtRecords;
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
import model.DebtRecords;
import model.Shops;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class ListDebtRecordDetailServlet extends HttpServlet {
   
    

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
        DAODebtRecords dao = new DAODebtRecords();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        if(user.getShopID()==0 && user.getRoleid()==2){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        int debtid = Integer.parseInt(request.getParameter("debtid"));
        DebtRecords debtrecords;
        try {
            debtrecords = dao.getDebtRecordByID(debtid);
            Shops shop = (Shops) session.getAttribute("shop");
            if (shop.getID() != debtrecords.getShopID() || debtrecords == null) {
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("debtrecords", debtrecords);
        } catch (Exception ex) {
            request.getRequestDispatcher("logout").forward(request, response);
                return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("DebtRecordsManager/ListDebtRecordDetail.jsp");
        requestDispatcher.forward(request, response);
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
