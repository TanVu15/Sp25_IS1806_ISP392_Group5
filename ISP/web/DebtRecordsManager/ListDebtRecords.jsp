<%-- 
    Document   : ListDebtRecords
    Created on : Feb 9, 2025, 10:43:08 PM
    Author     : ADMIN
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Công Nợ</title>
        <link rel="stylesheet" href="css/home2.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <%  
            DAOUser dao = new DAOUser();
            DAOCustomers dao1 = new DAOCustomers();
            Users u = (Users) request.getAttribute("user");
            Customers customer = (Customers) request.getAttribute("customer");
            ArrayList<DebtRecords> debtrecords = (ArrayList<DebtRecords>) request.getAttribute("debtrecords");
            // Định dạng số tiền theo chuẩn VN (có dấu phân tách hàng nghìn)
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        %>

        <div class="header">
            <div class="container">
                <img src="Image/logo.png" alt="logo" class="home-logo">
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"> <%= u.getFullName() %></span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="" class="navbar__info--item">Tài khoản của tôi</a>
                    </div>
                    <div class="navbar__info--wrapper">
                        <a href="" class="navbar__info--item">Đăng xuất</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="body">
            <div class="body-container">
                <div class="mainmenu">
                    <ul class="mainmenu-list row no-gutters">
                        <li class="mainmenu__list-item"><a href="listproducts"><i class="fa-solid fa-bowl-rice list-item-icon"></i>Sản Phẩm</a></li>
                        <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-box list-item-icon"></i>Kho</a></li>
                        <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-user list-item-icon"></i>Cửa Hàng</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Công Nợ</h3>
                        <div class="search-container">
                            <form action='listdebtrecords' method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm khách hàng..." class="search-input">
                                <button type="submit" class="search-button">Search</button>
                            </form>
                            <% String message = (String) request.getAttribute("message"); %>
                            <% if (message != null && !message.isEmpty()) { %>
                            <div id="toast-message" class="toast-message"><%= message %></div>
                            <% } %>

                            <script>
                                window.onload = function () {
                                    var toast = document.getElementById("toast-message");
                                    if (toast) {
                                        toast.style.display = "block"; // Hiển thị thông báo
                                        setTimeout(function () {
                                            toast.style.opacity = "0";
                                            setTimeout(() => toast.style.display = "none", 500);
                                        }, 3000);
                                    }
                                };
                            </script>
                        </div>

                    </div>
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    
                                    <th class="table-header-item">ID</th>
                                    <th class="table-header-item">Tên Khách Hàng</th>
                                    <th class="table-header-item">Số tiền</th>
                                    <th class="table-header-item">Trạng Thái</th>
                                    <th class="table-header-item">Ngày Tạo Phiếu</th>
                                    <th class="table-header-item">Ngày tạo</th>
                                    <th class="table-header-item">Người tạo</th>
                                    <th class="table-header-item">Hình ảnh</th>
                                    <th class="table-header-item">Ghi Chú</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                for (DebtRecords debt : debtrecords) {
                                    if(debt.getShopID() == u.getShopID()){
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><%= debt.getID() %></td>
                                    <td class="table-cell"><%= dao1.getCustomersByID(debt.getCustomerID()).getName() %></td>
                                    <td class="table-cell"><%= currencyFormat.format(debt.getAmountOwed()) +" VND" %></td>
                                    <td class="table-cell"><% if (debt.getPaymentStatus() == 1) { %>
                                        Trả Nợ
                                        <% } if (debt.getPaymentStatus() == -1) { %>
                                        Vay Nợ
                                        <% } %>
                                        <% if (debt.getPaymentStatus() == 2) { %>
                                        Đi Vay
                                        <% } %>
                                        <%if (debt.getPaymentStatus() == -2) { %>
                                        Đi Trả
                                    <% } %></td>
                                    <td class="table-cell"><%= debt.getInvoiceDate() %></td>
                                    <td class="table-cell"><%= debt.getCreateAt() %></td>
                                    <td class="table-cell"><%= dao.getUserByID(debt.getCreateBy()).getFullName() %></td>
                                    <td class="table-cell"><img src="<%= debt.getImagePath() %>"
                                                                class="product-image"></td>
                                    <td class="table-cell"><%= debt.getNote() %></td>
                                </tr>
                                <% }} %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>


        <div class="footer">
            <div class="container">
                <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
    </body>
</html>
