<%-- 
    Document   : ListUsers
    Created on : Feb 6, 2025, 2:22:21 PM
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
        <title>Quản lý Tài Khoản</title>
        <link rel="stylesheet" href="css/home2.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <%      DAOShops daoShop = new DAOShops();
                Shops shop = (Shops) session.getAttribute("shop");
                DAOUser dao = new DAOUser();
                Users u = (Users) request.getAttribute("user");
                ArrayList<Users> users = (ArrayList<Users>) request.getAttribute("users");
                
        %>

        <div class="header">
            <div class="container">
                <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
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
                        <li class="mainmenu__list-item"><a href="ListZones.jsp"><i class="fa-solid fa-box list-item-icon"></i>Kho</a></li>
                        <li class="mainmenu__list-item"><a href="listorders"><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-user list-item-icon"></i>Cửa Hàng</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin tài khoản</h3>
                        <div class="search-container">
                            <form action="listusers" method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm nguời dùng..." class="search-input">
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
                            <a href="register" class="add-product-button">Thêm tài khoản</a>
                        </div>
                    </div>
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">ID</th>
                                    <th class="table-header-item">Tài khoản</th>
                                    <th class="table-header-item">Mật khẩu</th>
                                    <th class="table-header-item">Vai trò</th>
                                    <th class="table-header-item">Tên</th>
                                    <th class="table-header-item">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (Users user : users) {
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><%= user.getID() %></td>
                                    <td class="table-cell"><%= user.getUsername() %></td>
                                    <td class="table-cell"><%= user.getPasswordHash() %></td>
                                    <td class="table-cell"><% 
                                                                if (user.getRoleid() == 1) {
                                                                    out.print("Admin");
                                                                } else if (user.getRoleid() == 2) {
                                                                    out.print("Owner");
                                                                } else {
                                                                    out.print("Staff");
                                                                }
                                        %></td>
                                    <td class="table-cell"><%= user.getFullName() %></td>
                                    <td class="table-cell">
                                        <button class="action-button" onclick="window.location.href = 'updateuser?id=<%= user.getID() %>'">Chỉnh sửa</button>
                                        <% if(u.getRoleid() < user.getRoleid()){ %>
                                        <button class="action-button" onclick="if (confirm('Bạn có chắc chắn muốn xóa?')) {
                                                    window.location.href = 'deleteuser?deleteid=<%= user.getID() %>&userid=<%= u.getID() %>';
                                                }">Xóa</button>  
                                        <button class="action-button" onclick="if (confirm('Bạn có muốn đặt lại mật khẩu thành : 12345678 ?')) {
                                                    window.location.href = 'resetpassword?resetid=<%= user.getID() %>';
                                                }">Đặt lại mật khẩu</button>        
                                        <%
                                            }
                                        %>
                                        <button class="action-button" onclick="window.location.href = 'userdetail?id=<%= user.getID() %>'">Thông tin chi tiết</button>
                                    </td>
                                </tr>
                                <% 
                                    }
                                %>
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
