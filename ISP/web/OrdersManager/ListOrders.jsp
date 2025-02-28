<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOOrders" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý đơn hàng</title>
        <link rel="stylesheet" href="css/home2.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <% 
            DAOUser dao = new DAOUser();
            Users u = (Users) request.getAttribute("user");
            ArrayList<Orders> orders = (ArrayList<Orders>) request.getAttribute("orders");
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
                        <li class="mainmenu__list-item"><a href="ListCustomers.jsp"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="debts.jsp"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin đơn hàng</h3>
                        <div class="search-container">
                            <form action="listorders" method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm đơn..." class="search-input">
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
                            <a href="addorder" class="add-product-button">Thêm đơn</a>
                        </div>
                    </div>
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">

                                    <th class="table-header-item">ID</th>
                                    <th class="table-header-item">Khách hàng</th>
                                    <th class="table-header-item">Sản phẩm</th>
                                    <th class="table-header-item">Cửa hàng</th>
                                    <th class="table-header-item">Giá trị</th>
                                    <th class="table-header-item">Ngày tạo</th>
                                    <th class="table-header-item">Người tạo</th>
                                    <th class="table-header-item">Ngày cập nhật</th>
                                    <th class="table-header-item">Ngày xóa</th>
                                    <th class="table-header-item">Người xóa</th>
                                    <th class="table-header-item">Hành động</th>
                                    <th class="table-header-item">Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (orders != null && !orders.isEmpty()) { 
                                    for (Orders o : orders) {
                                    Users create1 = dao.getUserByID(o.getCreateBy());
                                    Users create2 = dao.getUserByID(create1.getCreateBy());
                                    if(u.getID() == create1.getID() || u.getID() == create2.getID() || u.getID() == o.getCreateBy()){
                                %>
                                <tr class="table-row">

                                    <td class="table-cell"><%= o.getID() %></td>
                                    <td class="table-cell"><%= o.getCustomerID() %></td>
                                    <td class="table-cell"><%= o.getOrderItemID() %></td>
                                    <td class="table-cell"><%= o.getShopID() %></td>
                                    <td class="table-cell"><%= currencyFormat.format(o.getTotalAmount()) + " VND"%></td>
                                    <td class="table-cell"><%= o.getCreateAt() %></td>
                                    <td class="table-cell"><%= dao.getUserByID(o.getCreateBy()).getFullName() %></td>
                                    <td class="table-cell"><%= o.getUpdateAt() %></td>
                                    <td class="table-cell"><%= o.getDeletedAt() %></td>
                                    <td class="table-cell"><%= (o.getIsDelete() == 0) ? "Null" : dao.getUserByID(o.getDeleteBy()).getFullName() %></td>

                                    <td class="table-cell">
                                        <button class="action-button" onclick="window.location.href = 'updateorder?id=<%= o.getID() %>'">Sửa</button>

                                        <button class="action-button" onclick="window.location.href = 'deleteorder?deleteid=<%= o.getID() %>&userid=<%= u.getID() %>'">Ban</button>
                                    </td>
                                    <td class="table-cell"><%= o.getIsDelete() == 0 ? "Hoạt động" : "Khóa"%></td>

                                </tr>
                                <%      } 
                                    } 
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
