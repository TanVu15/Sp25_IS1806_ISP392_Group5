package Controller.productsservlet;


import dal.DAOProducts;
import dal.DAOShops;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ProductPriceHistory;
import model.Shops;
import model.Users;

import java.io.IOException;
import java.util.List;

public class HistoryExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOProducts dao = new DAOProducts();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        DAOShops daoShop = new DAOShops();
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }

        // Ph�n trang
        int recordsPerPage = 5;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // T�m ki?m v� l?c
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null || (!sortOrder.equals("asc") && !sortOrder.equals("desc"))) {
            sortOrder = "asc";
        }

        // L?y t?ng s? b?n ghi v� danh s�ch
        int totalRecords = dao.getTotalHistoryRecords(keyword, startDate, endDate, user.getID());
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        if (currentPage > totalPages && totalPages > 0) currentPage = totalPages; // ?i?u ch?nh n?u trang v??t qu�
        List<ProductPriceHistory> historyList = dao.getExportPriceHistory(keyword, startDate, endDate, currentPage, recordsPerPage, user.getID(), sortOrder);

        // Set attributes
        request.setAttribute("HistoryList", historyList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("sortOrder", sortOrder);
        String message = "";
        if (!keyword.isEmpty()) message += "T�m ki?m: " + keyword;
        if (startDate != null && !startDate.isEmpty()) message += (message.isEmpty() ? "" : ", ") + "T? " + startDate;
        if (endDate != null && !endDate.isEmpty()) message += (message.isEmpty() ? "" : " ") + "??n " + endDate;
        request.setAttribute("message", message.isEmpty() ? "" : message);

        System.out.println("Total Records: " + totalRecords + ", Total Pages: " + totalPages + ", Current Page: " + currentPage + ", HistoryList Size: " + historyList.size());
        request.getRequestDispatcher("ProductsManager/HistoryExport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet hi?n th? l?ch s? gi� b�n";
    }
}