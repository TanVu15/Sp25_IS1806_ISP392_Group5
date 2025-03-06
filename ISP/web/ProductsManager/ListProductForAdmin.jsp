<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Products" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOUser" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ Quản lý</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <%
            DAOUser dao = new DAOUser();
            Users u = (Users) request.getAttribute("user");
            ArrayList<Products> products = (ArrayList<Products>) request.getAttribute("products");
        %>
        <!-- Homepage Header -->
        <div class="header">
            <div class="container">
                <img src="Image/logo.png" alt="logo" class="home-logo">
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name">
                    <%= u.getFullName()%>
                </span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail" class="navbar__info--item">Tài khoản của tôi</a>
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
                        <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                    </ul>
                </div>
                <!-- HomePage Body -->
                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin sản phẩm</h3>
                        <div class="search-container">
                            <form action="listproducts" method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm sản phẩm..." class="search-input">
                                <input class="search-button" type="submit" value="Search">
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
                            <a href="addproduct" class="add-product-button">Thêm sản phẩm</a>
                        </div>
                    </div>
                    <!-- Product List -->
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Hình ảnh</th>
                                    <th class="table-header-item">Mã sản phẩm</th>
                                    <th class="table-header-item">Tên sản phẩm</th>
                                    <th class="table-header-item">Giá tiền / KG</th>
                                    <th class="table-header-item">Số lượng</th>
                                    <th class="table-header-item">Vị trí</th>
                                    <th class="table-header-item">Mô tả</th>
                                    <th class="table-header-item">Hành động</th>
                                    <th class="table-header-item">Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (Products product : products) {
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><img src="<%= product.getImageLink()%>" alt="<%= product.getProductName()%>"
                                                                class="product-image"></td> <!-- Sửa tên phương thức -->
                                    <td class="table-cell"><%= product.getID()%></td>
                                    <td class="table-cell"><%= product.getProductName()%></td>
                                    <td class="table-cell"><%= product.getPrice()%>đ</td>
                                    <td class="table-cell"><%= product.getQuantity()%></td>
                                   
                                    <td class="table-cell description"><%= product.getDescription()%></td>
                                    <td class="table-cell">
                                        <a href="updateproduct?id=<%= product.getID()%>" class="action-button">Sửa</a>
                                        <%
                                            if (product.getIsDelete() == 0) {
                                        %>
                                        <button class="action-button" onclick="if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
                                                    window.location.href = 'deleteproduct?deleteid=<%= product.getID()%>&userid=<%= u.getID()%>';
                                                }">Xóa</button>  
                                        <%
                                            }
                                        %>
                                    </td>
                                    <td class="table-cell">
                                        <%= product.getIsDelete() == 0 ? "Hoạt động" : "Đã xóa"%>
                                    </td>
                                </tr>
                                <% }%>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->

        <!-- Footer -->
        <div class="footer">
            <div class="container">
                <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>

    </body>
</html>