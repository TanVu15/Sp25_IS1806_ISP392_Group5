<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Users" %>
<%@ page import="model.Shops" %>
<%@ page import="model.OrderItems" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Customers" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi Tiết Đơn Hàng</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <style>
    /* Form Sections */
    .form-section {
        margin-bottom: 30px;
    }

    .form-section h4 {
        font-size: 20px;
        color: #000000; /* Black */
        border-bottom: 2px solid #78ae39; /* Green */
        padding-bottom: 8px;
        margin-bottom: 20px;
    }

    /* Form Fields */
    .form-field {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
    }

    .form-field label {
        font-weight: bold;
        width: 180px;
        color: #555;
    }

    .form-field span {
        flex: 1;
        padding: 10px;
        background-color: #f9f9f9;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    /* Table Styling */
    .product-table {
        width: 100%;
        border-collapse: collapse;
        font-size: 16px;
        margin-top: 20px;
    }

    .product-table th, .product-table td {
        border: 1px solid #ddd;
        padding: 12px;
        text-align: left;
    }

    .product-table th {
        background-color: #78ae39; /* Green */
        color: white;
        font-weight: bold;
    }

    .product-table tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .product-table tr:hover {
        background-color: #e9ecef;
    }

    /* Total Amount */
    .total-amount {
        text-align: right;
        margin-top: 20px;
        font-size: 18px;
        font-weight: bold;
        color: #333;
    }

    .total-amount label {
        margin-right: 15px;
    }

    .total-amount span {
        color: #dc3545;
    }

    /* Buttons */
    .action-button {
        padding: 12px 24px;
        background-color: #78ae39; /* Green */
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .action-button:hover {
        background-color: #5d8c2e; /* Darker green for hover */
    }

    /* Error Message */
    .error {
        color: #dc3545;
        text-align: center;
        margin: 10px 0;
        font-size: 16px;
    }
</style>


    </head>
    <body>
        <% 
            Shops shop = (Shops) session.getAttribute("shop");
            Users u = (Users) request.getAttribute("user");
            Orders order = (Orders) request.getAttribute("order");
            Customers customer = (Customers) request.getAttribute("customer");
            ArrayList<OrderItems> allOrderItems = (ArrayList<OrderItems>) request.getAttribute("allOrderItems");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String message = (String) request.getAttribute("message");
            DAOUser daoUser = new DAOUser();
            String orderId = request.getAttribute("orderId") != null ? request.getAttribute("orderId").toString() : 
                            request.getParameter("id") != null ? request.getParameter("id") : "";
            String phoneNumber = (String) request.getAttribute("phoneNumber");
            String orderCreateAt = (String) request.getAttribute("orderCreateAt");

            String orderType = "Không xác định";
            if (order != null) {
                orderType = order.getStatus() == 1 ? "Nhập Hàng" : order.getStatus() == -1 ? "Xuất Hàng" : "Không xác định";
            }
        %>

        <div class="content-wrapper">
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
                            <a href="userdetail?id=<%= u.getID() %>" class="navbar__info--item">Tài khoản của tôi</a>
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
                            <li class="mainmenu__list-item"><a href="listzones"><i class="fa-solid fa-box list-item-icon"></i>Khu vực</a></li>
                            <li class="mainmenu__list-item"><a href="listorders"><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                            <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                            <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                            <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                            <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-shop list-item-icon"></i>Cửa Hàng</a></li>
                            <li class="mainmenu__list-item"><a href="analysis"><i class="fa-solid fa-chart-simple list-item-icon"></i>Báo Cáo</a></li>
                            <li class="mainmenu__list-item"><a href="historyexport"><i class="fa-solid fa-history list-item-icon"></i>Lịch sử giá</a></li>
                        </ul>
                    </div>

                    <div class="homepage-body">
                        <div class="body-head">
                            <h3 class="body__head-title">Chi Tiết Đơn Hàng</h3>
                            <button class="action-button" onclick="window.location.href = 'listorders'">Quay lại</button>
                        </div>

                        <% if (message != null) { %>
                        <p class="error"><%= message %></p>
                        <% } %>

                        <div class="order-details-form">
                            <!-- Thông tin chung -->
                            <div class="form-section">
                                <h4>Thông Tin Đơn Hàng</h4>
                                <div class="form-field">
                                    <label>Mã Đơn Hàng:</label>
                                    <span><%= order != null ? order.getID() : "Không xác định" %></span>
                                </div>
                                <div class="form-field">
                                    <label>Loại Hóa Đơn:</label>
                                    <span><%= orderType %></span>
                                </div>
                                <div class="form-field">
                                    <label>Tên Khách Hàng:</label>
                                    <span><%= customer != null ? customer.getName() : "Không xác định" %></span>
                                </div>
                                <div class="form-field">
                                    <label>Số Điện Thoại:</label>
                                    <span><%= phoneNumber %></span>
                                </div>
                                <div class="form-field">
                                    <label>Ngày Tạo:</label>
                                    <span><%= orderCreateAt %></span>
                                </div>
                                <div class="form-field">
                                    <label>Người Tạo Phiếu:</label>
                                    <span><%= order != null && order.getCreateBy() != 0 ? daoUser.getUserByID(order.getCreateBy()).getFullName() : "Không xác định" %></span>
                                </div>
                            </div>

                            <!-- Danh sách sản phẩm -->
                            <div class="form-section">
                                <h4>Danh Sách Sản Phẩm</h4>
                                <div class="table-container">
                                    <table class="product-table">
                                        <thead>
                                            <tr>
                                                <th>Tên Sản Phẩm</th>
                                                <th>Số Lượng</th>
                                                <th>Quy Cách</th>
                                                <th>Giá Gốc</th>
                                                <th>Giảm Giá</th>
                                                <th>Thành Tiền</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% 
                                                if (allOrderItems != null && !allOrderItems.isEmpty()) { 
                                                    for (OrderItems o : allOrderItems) { 
                                                        if (u != null && o.getShopID() == u.getShopID()) { 
                                                            double itemTotal = o.getPrice() * o.getQuantity() - o.getUnitPrice();
                                            %>
                                            <tr>
                                                <td><%= o.getProductName() %></td>
                                                <td><%= o.getQuantity() %></td>
                                                <td><%= o.getDescription() %> Kg/Bao</td>
                                                <td><%= currencyFormat.format(o.getPrice()) %> VND</td>
                                                <td><%= currencyFormat.format(o.getUnitPrice()) %> VND</td>
                                                <td><%= currencyFormat.format(itemTotal) %> VND</td>
                                            </tr>
                                            <%          }
                                                    }
                                                } else { %>
                                            <tr>
                                                <td colspan="6">Không có sản phẩm trong đơn hàng.</td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Tổng chi phí -->
                            <div class="total-amount">
                                <label>Tổng Chi Phí:</label>
                                <span><%= order != null ? currencyFormat.format(order.getTotalAmount()) : "Không xác định" %> VND</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="footer">
                <div class="container">
                    <p>© 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
                </div>
            </div>
    </body>
</html>