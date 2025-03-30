

package dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DAOAnalysis {
    public static final DAO INSTANCE = new DAO();
    protected Connection connect;

    public DAOAnalysis() {
        connect = new DBContext().connect;
    }

    public static LocalDate today = LocalDate.now();
    
    public double getTotalRevenueByToday() {
        return getRevenueByDateRange(today.toString(), today.toString());
    }
    
    public double getTotalRevenueByWeek() {
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return getRevenueByDateRange(startOfWeek.toString(), endOfWeek.toString());
    }
    
    public double getTotalRevenueByMonth() {
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        return getRevenueByDateRange(startOfMonth.toString(), endOfMonth.toString());
    }
    
    public double getTotalRevenueByYear() {
        LocalDate startOfYear = today.withDayOfYear(1);
        LocalDate endOfYear = today.withDayOfYear(today.lengthOfYear());
        return getRevenueByDateRange(startOfYear.toString(), endOfYear.toString());
    }
    
    public double getRevenueByDateRange(String startDate, String endDate) {
        double revenue = 0;
        String sql = "SELECT SUM(COALESCE(TotalAmount, 0)) AS TotalAmountSum FROM Orders WHERE CreateAt BETWEEN ? AND ? AND Status = -1;";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    revenue = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }
    
    // Lấy số lượng đơn hàng xuất kho trong khoảng ngày đã chọn
public int getTotalOrdersByDateRange(String startDate, String endDate) {
    int totalOrders = 0;
    String sql = "SELECT COUNT(*) FROM Orders WHERE CreateAt BETWEEN ? AND ? AND Status = -1;";
    
    try (PreparedStatement stmt = connect.prepareStatement(sql)) {
        stmt.setString(1, startDate);
        stmt.setString(2, endDate);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            totalOrders = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return totalOrders;
}

    public List<Integer> getMonthlyRevenue(int year) {
        List<Integer> revenues = new ArrayList<>(Collections.nCopies(12, 0));
        
        String query = "SELECT MONTH(CreateAt) AS Thang, SUM(TotalAmount) AS DoanhThu "
                     + "FROM Orders WHERE Status = -1 AND YEAR(CreateAt) = ? "
                     + "GROUP BY MONTH(CreateAt) ORDER BY MONTH(CreateAt)";
        
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int month = rs.getInt("Thang") - 1;
                int revenue = rs.getInt("DoanhThu");
                revenues.set(month, revenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenues;
    }
   // Lấy doanh thu theo từng ngày trong tuần (Thứ Hai - Chủ Nhật)
public List<Integer> getWeeklyRevenue(int weekNumber) {
    List<Integer> revenueList = new ArrayList<>(Collections.nCopies(7, 0)); // Tạo danh sách 7 phần tử mặc định = 0
    String sql = "SELECT DATEPART(WEEKDAY, CreateAt) AS DayOfWeek, SUM(TotalAmount) AS TotalRevenue "
               + "FROM Orders "
               + "WHERE DATEPART(WEEK, CreateAt) = ? "
               + "AND YEAR(CreateAt) = YEAR(GETDATE()) AND Status = -1 "
               + "GROUP BY DATEPART(WEEKDAY, CreateAt)";
    
    try (PreparedStatement stmt = connect.prepareStatement(sql)) {
        stmt.setInt(1, weekNumber);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int dayIndex = rs.getInt("DayOfWeek") - 2; // Chuyển đổi SQL (Chủ Nhật = 1) về Java (Thứ Hai = 0)
            if (dayIndex < 0) dayIndex = 6; // Nếu Chủ Nhật thì đặt về index cuối cùng
            revenueList.set(dayIndex, rs.getInt("TotalRevenue"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return revenueList;
}
   // Lấy doanh thu theo từng ngày trong tháng với khoảng ngày tự do
public List<Integer> getFilteredMonthlyRevenue(String startDate, String endDate) {
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);
    int daysInMonth = end.getDayOfMonth();
    
    List<Integer> revenueList = new ArrayList<>(Collections.nCopies(daysInMonth, 0)); // Danh sách mặc định với 0
    
    String sql = "SELECT DAY(CreateAt) AS DayOfMonth, SUM(TotalAmount) AS TotalRevenue "
               + "FROM Orders "
               + "WHERE CreateAt BETWEEN ? AND ? AND Status = -1 "
               + "GROUP BY DAY(CreateAt) ORDER BY DAY(CreateAt)";
    
    try (PreparedStatement stmt = connect.prepareStatement(sql)) {
        stmt.setString(1, startDate);
        stmt.setString(2, endDate);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int dayIndex = rs.getInt("DayOfMonth") - 1; // Chuyển về index 0-based
            revenueList.set(dayIndex, rs.getInt("TotalRevenue"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return revenueList;
}
    
     // Lấy Top 5 sản phẩm bán chạy nhất
    public List<Map<String, Object>> getTop3SellingProducts() {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        String sql = "SELECT TOP 3 p.ProductName, SUM(oi.Quantity) AS TotalQuantity, SUM(oi.Quantity * oi.Price) AS TotalRevenue "
                   + "FROM OrderItems oi "
                   + "JOIN Products p ON oi.ProductName = p.ProductName "
                   + "JOIN Orders o ON oi.OrderID = o.ID "
                   + "WHERE YEAR(o.CreateAt) = YEAR(GETDATE()) AND o.Status = -1 "
                   + "GROUP BY p.ProductName "
                   + "ORDER BY TotalQuantity DESC";
        try (PreparedStatement stmt = connect.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("ProductName", rs.getString("ProductName"));
                product.put("TotalQuantity", rs.getInt("TotalQuantity"));
                product.put("TotalRevenue", rs.getDouble("TotalRevenue"));
                topProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topProducts;
    }
 
    
    public List<Map<String, Object>> getTop3SellingProductsByDateRange(String startDate, String endDate) {
    List<Map<String, Object>> topProducts = new ArrayList<>();
    String sql = "SELECT TOP 3 p.ProductName, SUM(oi.Quantity) AS TotalQuantity, SUM(oi.Quantity * oi.Price) AS TotalRevenue "
                   + "FROM OrderItems oi "
                   + "JOIN Products p ON oi.ProductName = p.ProductName "
                   + "JOIN Orders o ON oi.OrderID = o.ID "
                   + "WHERE o.CreateAt BETWEEN ? AND ? AND o.Status = -1 "
                   + "GROUP BY p.ProductName "
                   + "ORDER BY TotalQuantity DESC";

    try (PreparedStatement ps = connect.prepareStatement(sql)) {
        ps.setString(1, startDate);
        ps.setString(2, endDate);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("ProductName", rs.getString("ProductName"));
                product.put("TotalQuantity", rs.getInt("TotalQuantity"));
                product.put("TotalRevenue", rs.getDouble("TotalRevenue"));
            topProducts.add(product);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return topProducts;
}


    public static void main(String[] args) {
        DAOAnalysis dao = new DAOAnalysis();

        System.out.println("===== Thống kê theo HÔM NAY =====");
        double revenueToday = dao.getRevenueByDateRange("2025-03-28", "2025-03-28");
        System.out.println("Doanh thu hôm nay: " + revenueToday);

        System.out.println("\n===== Thống kê theo TUẦN NÀY =====");
        List<Integer> weeklyRevenue = dao.getWeeklyRevenue(13); // Giả sử tuần hiện tại là tuần thứ 13
        System.out.println("Doanh thu theo từng ngày trong tuần: " + weeklyRevenue);

        System.out.println("\n===== Thống kê theo THÁNG NÀY =====");
        List<Integer> dailyRevenue = dao.getFilteredMonthlyRevenue("2025-03-01", "2025-03-31");
        System.out.println("Doanh thu theo từng ngày trong tháng: " + dailyRevenue);

        System.out.println("\n===== Thống kê theo NĂM NÀY =====");
        List<Integer> monthlyRevenue = dao.getMonthlyRevenue(2025);
        System.out.println("Doanh thu theo từng tháng: " + monthlyRevenue);

        System.out.println("\n===== Kiểm tra Top 3 sản phẩm bán chạy =====");
        List<Map<String, Object>> topProducts = dao.getTop3SellingProductsByDateRange("2025-02-28", "2025-03-30");
        for (Map<String, Object> product : topProducts) {
            System.out.println("Sản phẩm: " + product.get("ProductName"));
            System.out.println("Số lượng bán: " + product.get("TotalQuantity"));
            System.out.println("Doanh thu: " + product.get("TotalRevenue") + " VNĐ");
            System.out.println("---------------------------------");
        }

    }
}

