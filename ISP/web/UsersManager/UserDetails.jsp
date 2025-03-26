<%-- 
    Document   : UserDetails
    Created on : Feb 26, 2025, 4:12:08 PM
    Author     : ADMIN
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông tin tài khoản</title>
        <link rel="stylesheet" href="css/detail.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <% 
            DAOShops daoShop = new DAOShops();
                Shops shop = (Shops) session.getAttribute("shop");
            DAOUser dao = new DAOUser();
            Users u = (Users) request.getAttribute("user");
            Users users = (Users) request.getAttribute("users");
            
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
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin tài khoản</h3>
                    </div>
                    <div class="user-info-container">
                        <div class="user-info-item">
                            <span class="user-info-label">Tài khoản:</span>
                            <span class="user-info-value"><%= users.getUsername() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Vai trò:</span>
                            <span class="user-info-value"><% 
                                if (users.getRoleid() == 1) {
                                    out.print("Admin");
                                } else if (users.getRoleid() == 2) {
                                    out.print("Owner");
                                } else {
                                    out.print("Staff");
                                }
                            %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Tên:</span>
                            <span class="user-info-value"><%= users.getFullName() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Ngày tạo:</span>
                            <span class="user-info-value"><%= users.getCreateAt() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Ngày cập nhật:</span>
                            <span class="user-info-value"><%= users.getUpdateAt() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Người tạo:</span>
                            <span class="user-info-value"><%= dao.getUserByID(users.getCreateBy()).getFullName() %></span>
                        </div>
                        <div class="user-info-item">
                            <span class="user-info-label">Trạng thái:</span>
                            <span class="user-info-value"><%= (users.getIsDelete() == 0) ? "Hoạt Động" : "Xóa" %></span>
                        </div>
                        <button class="action-button" onclick="window.location.href = 'updateuser?id=<%= users.getID() %>'">Chỉnh sửa</button>
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