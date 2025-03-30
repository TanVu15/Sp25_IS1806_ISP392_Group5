<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="dal.DAOShops"%>
<%@page import="model.Shops"%>
<%@page import="model.Zones"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Products" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Báo cáo thống kê</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="css/analysis.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">


        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />

    </head>


    <body>


        <%
            

            // Định dạng tiền tệ Việt Nam
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

            String startDate = request.getAttribute("startDate") != null ? (String) request.getAttribute("startDate") : "";
            String endDate = request.getAttribute("endDate") != null ? (String) request.getAttribute("endDate") : "";


            List<String> productLabels = (List<String>) request.getAttribute("productLabels");
            if (productLabels == null) {
                productLabels = new ArrayList<>();
            }

            List<Integer> productSales = (List<Integer>) request.getAttribute("productSales");
            if (productSales == null) {
                productSales = new ArrayList<>();
            }

            // Lấy thông tin cửa hàng từ session
             DAOShops daoShop = new DAOShops();
            Shops shop = (Shops) session.getAttribute("shop");

            // Lấy thông tin người dùng từ Servlet
                     DAOUser dao = new DAOUser();
            Users u = (Users) request.getAttribute("user");

            // Lấy danh sách sản phẩm từ Servlet
            ArrayList<Products> products = (ArrayList<Products>) request.getAttribute("products");
            if (products == null) {
                products = new ArrayList<>();
            }
            

    // Lấy dữ liệu từ request
    Double totalRevenue = (Double) request.getAttribute("totalRevenue");
    Integer totalOrders = (Integer) request.getAttribute("totalOrders");
    List<Integer> monthlyRevenue = (List<Integer>) request.getAttribute("monthlyRevenue");
    List<Integer> weeklyRevenue = (List<Integer>) request.getAttribute("weeklyRevenue");
    List<Integer> dailyRevenue = (List<Integer>) request.getAttribute("dailyRevenue");
    List<Map<String, Object>> topProducts = (List<Map<String, Object>>) request.getAttribute("topProducts");


        %>


        <!-- Homepage Header -->
        <div class="header">
            <div class="container">
                <a href="shopdetail">
                    <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                </a>
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name">
                           <%= u.getFullName()%>
                </span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id="class="navbar__info--item">Tài khoản của tôi</a>
                    </div>
                    <div class="navbar__info--wrapper">
                        <a href="logout" class="navbar__info--item">Đăng xuất</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="body">
            <div class="body-container">
                <div class="mainmenu">
                    <ul class="mainmenu-list row no-gutters">
                        <li class="mainmenu__list-item"><a href="listproducts"><i class="fa-solid fa-bowl-rice list-item-icon"></i>Sản Phẩm</a></li>
                        <li class="mainmenu__list-item"><a href="listzones"><i class="fa-solid fa-box list-item-icon"></i>Khu Vực</a></li>
                        <li class="mainmenu__list-item"><a href="listorders"><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-shop list-item-icon"></i>Cửa Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="analysis"><i class="fa-solid fa-chart-simple list-item-icon"></i></i>Báo Cáo</a></li>
                        <li class="mainmenu__list-item"><a href="historyexport"><i class="fa-solid fa-history list-item-icon"></i>Lịch Sử Giá</a></li>
                    </ul>
                </div>

                <!-- HomePage Body -->
                <div class="homepage-body">
                    <div class="body-head">
                        <h5 class="body__head-title dashboard-title">Thông tin doanh thu</h5>

                    </div>
                    <!-- Product List -->
                    <div class="main-content">
                        <div class="slicer-container">
                            <div class="time-button">
                                <button class="filter-btn" onclick="filterData('today')">Hôm nay</button>
                                <button class="filter-btn" onclick="filterData('week')">Tuần này</button>
                                <button class="filter-btn" onclick="filterData('month')">Tháng này</button>
                                <button class="filter-btn" onclick="filterData('year')">Năm nay</button>
                            </div>
                           
                        </div>
                        <div class="Chart-box">
                            <div class="chart-container">
                                <canvas class="revenue-chart" id="revenueChart"></canvas>
                            </div>
                            <div class="dashboard-box">
                                <span class="dash-header">Tổng Quan</span>
                                <div class="dashboard-header">
                                    <div class="stat-box">
                                        <h3>Doanh thu</h3>
                                        <p><%= NumberFormat.getInstance(new Locale("vi", "VN")).format(request.getAttribute("totalRevenue")) %> VND</p>
                                    </div>
                                    <div class="stat-box">
                                        <h3>Đơn hàng</h3>
                                        <p><%= request.getAttribute("totalOrders") %> đơn</p>
                                    </div>
                            
                                </div>
                                <span class="dash-header">Top sản phẩm bán chạy</span>
                                <div class="topsale-product">
                                     <% 
            List<Map<String, Object>> topProducts2 = (List<Map<String, Object>>) request.getAttribute("topProducts");
            if (topProducts2 != null && !topProducts2.isEmpty()) {
                for (Map<String, Object> product : topProducts2) {
        %>
                                    <div class="stat-boxe">
                    <h3><%= product.get("ProductName") %></h3>
                        <p>Doanh thu: <%= NumberFormat.getInstance(new Locale("vi", "VN")).format(product.get("TotalRevenue")) %> VND</p>
                        <p>Số lượng: <%= product.get("TotalQuantity") %>kg</p>
                </div>
    <%
         }
            } else {
    %>
            <p>Chưa có sản phẩm nào được bán!</p>
        <% } %>

                            </div>

                        </div>
                    </div>

                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <div class="container">
                <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>

    </body>
    <script>
        
        function filterData(filterType) {
    let url = "analysis?filter=" + filterType;
    window.location.href = url;
}
     var chartType = "<%= request.getAttribute("chartType") %>";

var dailyRevenueData = <%= new Gson().toJson(request.getAttribute("dailyRevenue")) %>;
var weeklyRevenueData = <%= new Gson().toJson(request.getAttribute("weeklyRevenue")) %>;
var monthlyRevenueData = <%= new Gson().toJson(request.getAttribute("monthlyRevenue")) %>;

var labels, revenueData;

if (chartType === "daily") {
    labels = [...Array(dailyRevenueData.length).keys()].map(i => "Ngày " + (i + 1));
    revenueData = dailyRevenueData;
} else if (chartType === "weekly") {
     labels = [...Array(weeklyRevenueData.length).keys()].map(i => "Thứ " + (i + 2));
    labels[5] = "Thứ 7"; // Điều chỉnh để hiển thị đúng
    labels[6] = "Chủ Nhật"; // Thay Thứ 8 thành Chủ Nhật
    revenueData = weeklyRevenueData;
} else if (chartType === "monthly") {
    labels = [...Array(monthlyRevenueData.length).keys()].map(i => "Tháng " + (i + 1));
    revenueData = monthlyRevenueData;
}

var ctx = document.getElementById('revenueChart').getContext('2d');
var revenueChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: [{
            label: 'Doanh thu',
            data: revenueData,
            borderColor: 'green',
            borderWidth: 2,
            fill: false
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false
    }
});

    </script>
</html>