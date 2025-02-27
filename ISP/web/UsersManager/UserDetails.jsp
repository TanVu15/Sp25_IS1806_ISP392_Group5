<%-- 
    Document   : UserDetails
    Created on : Feb 26, 2025, 4:12:08 PM
    Author     : ADMIN
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông tin tài khoản</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        
        <% 
                DAOUser dao = new DAOUser();
                Users u = (Users) request.getAttribute("user");
                Users users = (Users) request.getAttribute("users");
                                
        %>
        
        <div class="header">
            <div class="container">
                <img src="Image/logo.png" alt="logo" class="home-logo">
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"> <%= u.getFullName() %></span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id=<%= u.getID() %>"class="navbar__info--item">Tài khoản của tôi</a>
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
                        <li class="mainmenu__list-item"><a href="sales.jsp"><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-user list-item-icon"></i>Cửa Hàng</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Tài khoản</th>
                                    <th class="table-header-item">Mật khẩu</th>
                                    <th class="table-header-item">Vai trò</th>
                                    <th class="table-header-item">Tên</th>
                                    <th class="table-header-item">Ngày tạo</th>
                                    <th class="table-header-item">Ngày cập nhật</th>
                                    <th class="table-header-item">Người tạo</th>
                                    <th class="table-header-item">Trạng thái</th>
                                    <th class="table-header-item">Ngày xóa</th>
                                    <th class="table-header-item">Người xóa</th>
                                    <th class="table-header-item">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><%= users.getUsername() %></td>
                                    <td class="table-cell"><%= users.getPasswordHash() %></td>
                                    <td class="table-cell"><% 
                                                                if (users.getRoleid() == 1) {
                                                                    out.print("Admin");
                                                                } else if (users.getRoleid() == 2) {
                                                                    out.print("Owner");
                                                                } else {
                                                                    out.print("Staff");
                                                                }
                                        %></td>
                                    <td class="table-cell"><%= users.getFullName() %></td>
                                    <td class="table-cell"><%= users.getCreateAt() %></td>
                                    <td class="table-cell"><%= users.getUpdateAt() %></td>
                                    <td class="table-cell"><%= dao.getUserByID(users.getCreateBy()).getFullName() %></td>

                                    <td class="table-cell"><%= (users.getIsDelete() == 0) ? "Hoạt Động" : "Xóa" %></td>
                                    <td class="table-cell"><%= users.getDeletedAt() %></td>
                                    <td class="table-cell"><%= (users.getIsDelete() == 0) ? "Null" : dao.getUserByID(users.getDeleteBy()).getFullName() %></td>
                                    <td class="table-cell">
                                        <button class="action-button" onclick="window.location.href = 'updateuser?id=<%= users.getID() %>'">Chỉnh sửa</button>
                                        
                                    </td>
                                    <% 
                                    
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
