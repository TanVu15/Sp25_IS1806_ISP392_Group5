<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%
    // Lấy dữ liệu từ Servlet
    int revenue = (int) request.getAttribute("totalRevenue");
    int totalOrders = (int) request.getAttribute("totalOrders");
    int stock = (int) request.getAttribute("totalStock");
    
    List<Integer> monthlyRevenue = (List<Integer>) request.getAttribute("monthlyRevenue");
    List<String> productLabels = (List<String>) request.getAttribute("productLabels");
    List<Integer> productSales = (List<Integer>) request.getAttribute("productSales");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Báo cáo thông số</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
        }
        .sidebar {
            width: 250px;
            background: #2c3e50;
            color: white;
            padding: 20px;
            height: 100vh;
        }
        .sidebar h2 {
            text-align: center;
        }
        .sidebar ul {
            list-style: none;
            padding: 0;
        }
        .sidebar ul li {
            padding: 10px;
            cursor: pointer;
        }
        .sidebar ul li:hover {
            background: #34495e;
        }
        .main-content {
            flex: 1;
            padding: 20px;
            background: #ecf0f1;
        }
        .dashboard-header {
            display: flex;
            justify-content: space-around;
            background: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .dashboard-header .stat-box {
            text-align: center;
        }
        .charts {
            display: flex;
            margin-top: 20px;
        }
        .chart-container {
            flex: 1;
            margin: 10px;
            background: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>Filters</h2>
        <ul>
            <li>
                <a href="listproducts">Trang chủ</a>
            </li>
            <li>Theo ngày</li>
            <li>Theo tháng</li>
            <li>Theo sản phẩm</li>
            <li>Theo khách hàng</li>
        </ul>
    </div>
    <div class="main-content">
        <div class="dashboard-header">
            <div class="stat-box">
                <h3>Doanh thu</h3>
                <p><%= revenue %> VND</p>
            </div>
            <div class="stat-box">
                <h3>Đơn hàng</h3>
                <p><%= totalOrders %></p>
            </div>
            <div class="stat-box">
                <h3>Tồn kho</h3>
                <p><%= stock %> kg</p>
            </div>
        </div>
        <div class="charts">
            <div class="chart-container">
                <canvas id="revenueChart"></canvas>
            </div>
            <div class="chart-container">
                <canvas id="salesChart"></canvas>
            </div>
        </div>
    </div>

    <script>
        const revenueData = <%= monthlyRevenue %>;
        const productLabels = <%= productLabels %>;
        const productSales = <%= productSales %>;

        const ctx1 = document.getElementById('revenueChart').getContext('2d');
        new Chart(ctx1, {
            type: 'bar',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                    label: 'Doanh thu',
                    data: revenueData,
                    backgroundColor: 'rgba(52, 152, 219, 0.6)'
                }]
            }
        });

        const ctx2 = document.getElementById('salesChart').getContext('2d');
        new Chart(ctx2, {
            type: 'pie',
            data: {
                labels: productLabels,
                datasets: [{
                    data: productSales,
                    backgroundColor: ['#e74c3c', '#f1c40f', '#2ecc71', '#3498db']
                }]
            }
        });
    </script>
</body>
</html>
