<%-- 
    Document   : ListOrderItems
    Created on : Mar 19, 2025, 11:27:00 PM
    Author     : ADMIN
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Users" %>
<%@ page import="model.Shops" %>
<%@ page import="model.OrderItems" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông tin đơn hàng</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <style>
            .error {
                color: red;
            }
        </style>
    </head>
    <body>
        <% 
            Shops shop = (Shops) session.getAttribute("shop");
            Users u = (Users) request.getAttribute("user");
            ArrayList<OrderItems> orderitems = (ArrayList<OrderItems>) request.getAttribute("orderitems");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String message = (String) request.getAttribute("message");
        %>

        <div class="header">
            <div class="container">
                <% if (shop != null) { %>
                <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                <% } else { %>
                <p>Chưa có logo cửa hàng</p>
                <% } %>
            </div>
            <div class="header__navbar-item navbar__user">
                <% if (u != null) { %>
                <span class="navbar__user--name"><%= u.getFullName()%></span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id=<%= u.getID()%>" class="navbar__info--item">Tài khoản của tôi</a>
                    </div>
                    <div class="navbar__info--wrapper">
                        <a href="logout" class="navbar__info--item">Đăng xuất</a>
                    </div>
                </div>
                <% } else { %>
                <span class="navbar__user--name">Khách</span>
                <a href="login">Đăng nhập</a>
                <% } %>
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
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-user list-item-icon"></i>Cửa Hàng</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin đơn hàng</h3>
                        <form action="listorderitems" method="post">
                        <input type="hidden" name="orderid" value="<%= request.getAttribute("orderId") != null ? request.getAttribute("orderId") : request.getParameter("id") != null ? request.getParameter("id") : "" %>">
                        <input type="text" id="information" name="information" placeholder="Nhập tên sản phẩm..." class="search-input">
                        <button type="submit" class="search-button">Tìm kiếm</button>
                    </form>
                    </div>
                    
                    <% if (message != null) { %>
                    <p class="error"><%= message %></p>
                    <% } %>
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Tên sản phẩm</th>
                                    <th class="table-header-item">Số lượng</th>
                                    <th class="table-header-item">Quy cách</th>
                                    <th class="table-header-item">Giá sản phẩm</th>
                                    <th class="table-header-item">Giảm giá</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (orderitems != null && !orderitems.isEmpty()) { 
                                    for (OrderItems o : orderitems) { 
                                        if (u != null && o.getShopID() == u.getShopID()) { %>
                                <tr class="table-row">
                                    <td class="table-cell"><%= o.getProductName()%></td>
                                    <td class="table-cell"><%= o.getQuantity() %></td>
                                    <td class="table-cell"><%= o.getDescription() +"Kg/Bao" %></td>
                                    <td class="table-cell"><%= currencyFormat.format(o.getPrice()) +" VND"%></td>
                                    <td class="table-cell"><%= currencyFormat.format(o.getUnitPrice()) +" VND"%></td>
                                </tr>
                                <%      }
                                    }
                                } else { %>
                                <tr><td colspan="5">Không có sản phẩm nào trong đơn hàng.</td></tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <button class="action-button" onclick="window.location.href = 'listorders'">Quay lại</button>
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