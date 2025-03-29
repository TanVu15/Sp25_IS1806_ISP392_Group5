<%-- 
    Document   : ListZones
    Created on : Feb 18, 2025, 5:12:56 PM
    Author     : ASUS
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Zones" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOZones" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">


    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ Quản lý</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <% 
            DAOUser dao = new DAOUser();
            DAOShops daoShop = new DAOShops();
            Shops shop = (Shops) session.getAttribute("shop");
            Users u = (Users) request.getAttribute("user");
            ArrayList<Zones> zones = (ArrayList<Zones>) request.getAttribute("zones");
        %>
        <%
           Integer currentPage = (Integer) request.getAttribute("currentPage");
           Integer totalPages = (Integer) request.getAttribute("totalPages");

           // Kiểm tra xem các biến có được nhận hay không
           if (currentPage == null || totalPages == null) {
               out.println("<script>alert('Không thể nhận được currentPage hoặc totalPages.');</script>");
           }
        %>
        <div class="header">
            <div class="container">
                <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo" >
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
                        <h3 class="body__head-title">Thông tin khu vực</h3>
                        <div class="search-container">
                            <form action="listzones" method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm khu vực..." class="search-input">
                                <input type="hidden" name="page" value="1">
                                <button type="submit" class="search-button">Tìm kiếm</button>
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
                            <a href="addzone" class="add-product-button">Thêm kho</a>
                        </div>
                    </div>
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">

                                    <th class="table-header-item">Khu vực</th>
                                    <th class="table-header-item">Ngày tạo</th>
                                    <th class="table-header-item">Ngày cập nhật</th>
                                    <th class="table-header-item">Người tạo</th>
                                    <th class="table-header-item">Ngày xóa</th>
                                    <th class="table-header-item">Người xóa</th>
                                    <th class="table-header-item">Hành động</th>
                                    <th class="table-header-item">Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (zones != null && !zones.isEmpty()) { 
                                    for (Zones cus : zones) {
                                    if(cus.getShopID() == u.getShopID()){
                                %>
                                <tr class="table-row">

                                    <td class="table-cell"><%= cus.getZoneName() %></td>
                                    <td class="table-cell"><%= cus.getCreateAt() %></td>
                                    <td class="table-cell"><%= cus.getUpdateAt() %></td>
                                    <td class="table-cell"><%= dao.getUserByID(cus.getCreateBy()).getFullName() %></td>

                                    <td class="table-cell"><%= cus.getDeletedAt() %></td>
                                    <td class="table-cell"><%= (cus.getIsDelete() == 0) ? "Null" : dao.getUserByID(cus.getDeleteBy()).getFullName() %></td>

                                    <td class="table-cell">
                                        <button class="action-button" onclick="window.location.href = 'updatezone?id=<%= cus.getID() %>'">Sửa</button>

                                        <button class="action-button" onclick="if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
                                                    window.location.href = 'deletezone?deleteid=<%= cus.getID() %>&userid=<%= u.getID() %>';
                                                }">Xóa</button>
                                    </td>
                                    <td class="table-cell"><%= cus.getIsDelete() == 0 ? "Hoạt động" : "Khóa"%></td>

                                </tr>
                                <% } 
                                } }
                                %>
                            </tbody>
                        </table>
                    </div>
                    <!-- Pagination -->

                    <div class="pagination">
                        <div class="pagination-controls">
                            <% 
                                String searchTerm = (String) session.getAttribute("searchTerm");
                                String pageUrl = searchTerm != null ? 
                                    "listzones?information=" + searchTerm + "&page=" : 
                                    "listzones?page=";
                            %>
                            <button 
                                class="pagination-button" 
                                <% if (currentPage <= 1) { %> disabled <% }%> 
                                onclick="window.location.href = '<%= pageUrl %><%= currentPage - 1%>'">Trước</button>

                            <span class="pagination-info">Trang <%= currentPage%> / <%= totalPages%></span>

                            <button 
                                class="pagination-button" 
                                <% if (currentPage >= totalPages) { %> disabled <% }%> 
                                onclick="window.location.href = '<%= pageUrl %><%= currentPage + 1%>'">Sau</button>
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
    </body>

</html>