<%-- 
    Document   : ListDebtRecordDetail
    Created on : Feb 28, 2025, 1:17:30 PM
    Author     : ADMIN
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Công Nợ</title>
        <link rel="stylesheet" href="css/detail.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <%  
            DAOUser dao = new DAOUser();
            DAOCustomers dao1 = new DAOCustomers();
            Users u = (Users) request.getAttribute("user");
            DAOShops daoShop = new DAOShops();
            Shops shop = (Shops) session.getAttribute("shop");
            Customers customer = (Customers) request.getAttribute("customer");
            DebtRecords debtrecords = (DebtRecords) request.getAttribute("debtrecords");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
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
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-user list-item-icon"></i>Cửa Hàng</a></li>
                    </ul>
                </div>
                
                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Chi tiết công nợ</h3>
                    </div>
                    <div class="user-info-container">
                        <% 
                            if(debtrecords.getShopID() == u.getShopID()){
                        %>
                        <div class="user-info-item">
                            <span class="user-info-label">ID</span>
                            <span class="user-info-value"><%= debtrecords.getID() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Tên khách hàng</span>
                            <span class="user-info-value"><%= dao1.getCustomersByID(debtrecords.getCustomerID()).getName() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Số tiền</span>
                            <span class="user-info-value"><%= currencyFormat.format(debtrecords.getAmountOwed()) +" VND" %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Trạng thái</span>
                            <span class="user-info-value"><% if (debtrecords.getPaymentStatus() == 1) { %>
                                        Khách trả
                                        <% } if (debtrecords.getPaymentStatus() == -1) { %>
                                        Khách vay
                                        <% } %>
                                        <% if (debtrecords.getPaymentStatus() == 2) { %>
                                        Cửa hàng vay
                                        <% } %>
                                        <%if (debtrecords.getPaymentStatus() == -2) { %>
                                        Cửa hàng trả
                                        <% } %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Ngày tạo phiếu</span>
                            <span class="user-info-value"><%= debtrecords.getInvoiceDate() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Ngày tạo</span>
                            <span class="user-info-value"><%= debtrecords.getCreateAt() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Người tạo</span>
                            <span class="user-info-value"><%= dao.getUserByID(debtrecords.getCreateBy()).getFullName() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Hình ảnh</span>
                            <span class="user-info-value"><img src="<%= debtrecords.getImagePath() %>"
                                                                class="product-image"></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Ghi chú</span>
                            <span class="user-info-value"><%= debtrecords.getNote() %></span>
                        </div>      
                        <button class="action-button" onclick="window.location.href = 'listdebtrecords'">quay lại danh sách công nợ</button>
                        <%  
                                } 
                        %>
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

