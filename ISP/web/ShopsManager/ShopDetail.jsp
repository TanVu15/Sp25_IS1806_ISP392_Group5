<%-- 
    Document   : ShopDetail
    Created on : Feb 21, 2025, 4:48:48 PM
    Author     : Admin
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Products" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý cửa hàng</title>
        <link rel="stylesheet" href="css/shop.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <% 
            DAOUser dao = new DAOUser();
            Users u = (Users) request.getAttribute("user");
            
            Shops shop = (Shops) request.getAttribute("shop");
            Users user = (Users) request.getAttribute("user");
            if (shop == null) {
        %>
        <h3 style="color: red;">Lỗi: Không có dữ liệu cửa hàng!</h3>
        <%
            } else {
        %>
        <div class="header">
            <div class="container">
                <a href="shopdetail">
                    <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                </a>
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"> <%= u.getFullName() %></span>
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
                        <li class="mainmenu__list-item"><a href="listzones"><i class="fa-solid fa-box list-item-icon"></i>Kho</a></li>
                        <li class="mainmenu__list-item"><a href="listorders"><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-shop list-item-icon"></i>Cửa Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="analysis"><i class="fa-solid fa-chart-simple list-item-icon"></i></i>Báo Cáo</a></li>
                        <li class="mainmenu__list-item"><a href="historyexport"><i class="fa-solid fa-history list-item-icon"></i>Lịch sử</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="shop-detail-card">
                        <div class="shop-header">
                            <h2>Thông tin cửa hàng</h2>
                        </div>

                        <div class="shop-main-content">
                            <!-- Phần thông tin bên trái -->
                            <div class="shop-info-section">
                                <div class="shop-info">
                                    <div class="info-group">
                                        <div class="info-label">Tên cửa hàng:</div>
                                        <div class="info-value"><%= shop.getShopName() %></div>
                                    </div>
                                    
                                    <div class="info-group">
                                        <div class="info-label">Số điện thoại:</div>
                                        <div class="info-value"><%= shop.getPhone() %></div>
                                    </div>
                                    
                                    <div class="info-group">
                                        <div class="info-label">Tài khoản ngân hàng:</div>
                                        <div class="info-value"><%= shop.getBankAcc() %></div>
                                    </div>

                                    <div class="info-group">
                                        <div class="info-label">Địa chỉ:</div>
                                        <div class="info-value"><%= shop.getLocation() %></div>
                                    </div>

                                    <div class="info-group">
                                        <div class="info-label">Email:</div>
                                        <div class="info-value"><%= shop.getEmail() %></div>
                                    </div>
                                </div>
                            </div>

                            <!-- Phần logo bên phải -->
                            <div class="shop-logo-section">
                                <img src="<%= shop.getLogoShop() %>" alt="Logo cửa hàng" class="shop-logo">
                            </div>
                        </div>

                        <div class="shop-actions">
                            <% if(user.getRoleid() != 3) { %>
                                <button class="action-button" onclick="window.location.href = 'updateshop'">
                                    <i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa cửa hàng
                                </button>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="footer">
            <div class="container">
                <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
        <% } %>
    </body>
</html>