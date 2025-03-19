/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.debtrecordservlet;

import dal.DAOCustomers;
import dal.DAODebtRecords;
import model.Customers;
import model.DebtRecords;
import model.Users;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jakarta.servlet.annotation.MultipartConfig; // Thêm import này
import model.Shops;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
/**
 *
 * @author ADMIN
 */
public class AddDebtRecordsServlet extends HttpServlet {

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
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        ArrayList<Customers> customers = dao.getAllCustomers();
        request.setAttribute("customers", customers);
        
        

        int customerID = Integer.parseInt(request.getParameter("customerid"));
        try {
            
            Customers customer = dao.getCustomersByID(customerID);
            Shops shop = (Shops) session.getAttribute("shop");
            if(shop.getID() != customer.getShopID() || customer == null){
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
             request.getRequestDispatcher("logout").forward(request, response);
             return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp");
        dispatcher.forward(request, response);
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

        DAOCustomers daoCus = new DAOCustomers();
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Shops shop = (Shops) session.getAttribute("shop");

        // 🔹 Lấy customerID từ Part thay vì request.getParameter()
        Part customerIDPart = request.getPart("customerid");
        BufferedReader reader = new BufferedReader(new InputStreamReader(customerIDPart.getInputStream()));
        String customerIDStr = reader.readLine();
        int customerID = Integer.parseInt(customerIDStr.trim());

        try {
            Customers customer = daoCus.getCustomersByID(customerID);
            if(shop.getID() != customer.getShopID()){
                request.getRequestDispatcher("logout").forward(request, response);
                return;
            }
            request.setAttribute("customer", customer);
        } catch (Exception ex) {
            Logger.getLogger(AddDebtRecordsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 🔹 Các tham số khác vẫn lấy bằng request.getParameter() bình thường
        String amountOwedStr = request.getParameter("amountowed");
        if(amountOwedStr.endsWith("")){
            request.setAttribute("message", "Invalid format.");
            request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp").forward(request, response);
            return;
        }
        int amountOwed = Integer.parseInt(amountOwedStr);
        
        int paymentStatus = Integer.parseInt(request.getParameter("paymentstatus"));
        String note = request.getParameter("note");
        String invoiceDateStr = request.getParameter("invoicedate");

        // Chuyển đổi ngày lập phiếu sang java.sql.Date
        Date invoiceDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(invoiceDateStr);
            invoiceDate = new Date(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Invalid date format.");
            request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp").forward(request, response);
            return;
        }

        // 🔹 Xử lý upload file ảnh
        Part imagePart = request.getPart("image");
        String imageLink = "";

        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = imagePart.getSubmittedFileName();
            String imageDirectory = getServletContext().getRealPath("/Image/");
            File dir = new File(imageDirectory);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, fileName);
            imagePart.write(file.getAbsolutePath());
            imageLink = "Image/" + fileName; // Đường dẫn lưu trữ ảnh
        }

        // 🔹 Lưu vào database
        Users user = (Users) request.getSession().getAttribute("user");
        DAODebtRecords dao = new DAODebtRecords();

        try {
            if (user != null) {
                DebtRecords debtRecord = new DebtRecords();
                debtRecord.setCustomerID(customerID);
                debtRecord.setAmountOwed(amountOwed);
                debtRecord.setPaymentStatus(paymentStatus);
                debtRecord.setNote(note);
                debtRecord.setImagePath(imageLink);
                debtRecord.setInvoiceDate(invoiceDate);
                debtRecord.setShopID(user.getShopID());

                dao.AddDebtRecords(debtRecord, user.getID());
                response.sendRedirect("listcustomerdebtrecords?customerid=" + customerID);
            } else {
                request.setAttribute("message", "User not authenticated.");
                request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
            request.getRequestDispatcher("DebtRecordsManager/AddDebtRecord.jsp").forward(request, response);
        }
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
