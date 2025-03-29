<%-- 
    Document   : ListOrderDetail
    Created on : Mar 14, 2025, 2:37:10 PM
    Author     : DELL
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Users" %>
<%@ page import="model.Customers" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOOrders" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
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
    </head>
    <body>
        <% 
            DAOUser dao = new DAOUser();
            DAOCustomers dao1 = new DAOCustomers();
            Shops shop = (Shops) session.getAttribute("shop");
            Users u = (Users) request.getAttribute("user");
            Orders o = (Orders) request.getAttribute("order");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));  
        %>

        <div class="header">
            <div class="container">
                <a href="shopdetail">
                    <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                </a>
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"> <%= u.getFullName()%></span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id=<%= u.getID()%>" class="navbar__info--item">Tài khoản của tôi</a>
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
                        <li class="mainmenu__list-item"><a href="historyexport"><i class="fa-solid fa-history list-item-icon"></i>Lịch Sử Giá</a></li>                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin đơn hàng</h3>
                    </div>
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Mã khách hàng</th>
                                    <th class="table-header-item">Tên khách hàng</th>
                                    <th class="table-header-item">Số tiền</th>
                                    <th class="table-header-item">Tình trạng</th>
                                    <th class="table-header-item">Ngày tạo</th>
                                    <th class="table-header-item">Người tạo</th>
                                    <th class="table-header-item">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="table-row">
                                    <td class="table-cell"><%= o.getCustomerID()%></td>
                                    <td class="table-cell"><%= dao1.getCustomersByID(o.getCustomerID()).getName()%></td>
                                    <td class="table-cell"><%= currencyFormat.format(o.getTotalAmount()) +" VND"%></td>
                                    <td class="table-cell">
                                        <% if (o.getStatus() == 1) { %>
                                        Nhập hàng
                                        <% } else if (o.getStatus() == -1) { %>
                                        Bán hàng
                                        <% }%>
                                    </td>
                                    <td class="table-cell"><%= o.getCreateAt()%></td>
                                    <td class="table-cell"><%= dao.getUserByID(o.getCreateBy()).getFullName()%></td>
                                    <td class="table-cell">
                                        <button class="action-button" onclick="window.location.href = 'listorders'">Quay lại danh sách</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
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
