package Controller.analysis;

import dal.DAO;
import dal.DAOAnalysis;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import model.Users;
import org.apache.catalina.Session;

/**
 *
 * @author admin
 */
public class AnalysisServlet extends HttpServlet {

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
            out.println("<title>Servlet AnalysisServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AnalysisServlet at " + request.getContextPath() + "</h1>");
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
    try {
        DAOAnalysis dao = new DAOAnalysis();
        HttpSession session = request.getSession();

        // Kiểm tra người dùng đăng nhập
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp"); // Chuyển hướng đến trang đăng nhập
            return;
        }
        request.setAttribute("user", user);

        // Lấy ngày hiện tại làm mặc định
        LocalDate today = LocalDate.now();
        String startDate = today.withDayOfMonth(1).toString(); // Ngày đầu tháng
        String endDate = today.toString(); // Ngày hiện tại
        String chartType = "daily"; // Mặc định biểu đồ theo ngày

        String filter = request.getParameter("filter");
        String startParam = request.getParameter("startDate");
        String endParam = request.getParameter("endDate");

        if (startParam != null && !startParam.isEmpty() && endParam != null && !endParam.isEmpty()) {
            startDate = startParam;
            endDate = endParam;
        }

        // Xử lý lọc theo loại
        if ("today".equals(filter)) {
            startDate = today.toString();
            endDate = today.toString();
            chartType = "daily";
        } else if ("week".equals(filter)) {
            startDate = today.minusDays(today.getDayOfWeek().getValue() - 1).toString();
            endDate = today.toString();
            chartType = "weekly";
        } else if ("month".equals(filter)) {
            startDate = today.withDayOfMonth(1).toString();
            endDate = today.toString();
            chartType = "daily";
        } else if ("year".equals(filter)) {
            startDate = today.withDayOfYear(1).toString();
            endDate = today.toString();
            chartType = "monthly";
        } else if (filter != null) {
            throw new IllegalArgumentException("Lọc không hợp lệ: " + filter);
        }

        // Lấy dữ liệu từ DAO
        double totalRevenue = dao.getRevenueByDateRange(startDate, endDate);
        int totalOrders = dao.getTotalOrdersByDateRange(startDate, endDate);
        List<Integer> monthlyRevenue = dao.getMonthlyRevenue(today.getYear());
        List<Integer> weeklyRevenue = dao.getWeeklyRevenue(today.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        List<Integer> dailyRevenue = dao.getFilteredMonthlyRevenue(startDate, endDate);
        List<Map<String, Object>> topProducts = dao.getTop3SellingProductsByDateRange(startDate, endDate);

        // Gửi dữ liệu đến JSP
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("weeklyRevenue", weeklyRevenue);
        request.setAttribute("dailyRevenue", dailyRevenue);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("chartType", chartType);

        request.getRequestDispatcher("AnalysisManager/AnalysisDashboard.jsp").forward(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        request.getRequestDispatcher("logout").forward(request, response);
            return;    }
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
}