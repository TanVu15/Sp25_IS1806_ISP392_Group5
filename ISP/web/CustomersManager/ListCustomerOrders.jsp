<%@page import="model.Customers"%>
<%@page import="model.Shops"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Users" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách đơn hàng</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <%
            Shops shop = (Shops) session.getAttribute("shop");
            Users user = (Users) request.getAttribute("user");
            ArrayList<Orders> orders = (ArrayList<Orders>) request.getAttribute("orders");
            Customers customer = (Customers) request.getAttribute("customer");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String message = (String) request.getAttribute("message");


        %>
        <%       
            Integer currentPage = (Integer) request.getAttribute("currentPage");
            Integer totalPages = (Integer) request.getAttribute("totalPages");
            String sortBy = (String) request.getAttribute("sortBy");

            // Kiểm tra xem các biến có được nhận hay không
            if (currentPage == null || totalPages == null) {
                out.println("<script>alert('Không thể nhận được currentPage hoặc totalPages.');</script>");
            }
        %>

        <div class="header">
            <div class="container">
                <a href="shopdetail">
                    <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                </a>
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"> <%= user.getFullName()%></span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id=<%= user.getID()%>" class="navbar__info--item">Tài khoản của tôi</a>
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
                        <li class="mainmenu__list-item"><a href="listproducts"><i class="fa-solid fa-bowl-rice list-item-icon"></i>Sản phẩm</a></li>
                        <li class="mainmenu__list-item"><a href="listzones"><i class="fa-solid fa-box list-item-icon"></i>Kho</a></li>
                        <li class="mainmenu__list-item"><a href="listorders"><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài khoản</a></li>
                        <li class="mainmenu__list-item"><a href="shopdetail"><i class="fa-solid fa-shop list-item-icon"></i>Cửa hàng</a></li>
                        <li class="mainmenu__list-item"><a href="analysis"><i class="fa-solid fa-chart-simple list-item-icon"></i></i>Báo Cáo</a></li>
                        <li class="mainmenu__list-item"><a href="historyexport"><i class="fa-solid fa-history list-item-icon"></i>Lịch sử</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin khách hàng: <%=customer.getName() %> </h3>
                        <div class="search-container">
                            <form action='listcustomerorders?customerid=<%= customer.getID()%>' method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm đơn hàng..." class="search-input">
                                <button type="submit" class="search-button">Search</button>
                            </form>
                            <% if (message != null && !message.isEmpty()) {%>
                            <div id="toast-message" class="toast-message"><%= message%></div>
                            <% }%>

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
                            <form action='listcustomerorders' method="get">
                                <input type="hidden" name="customerid" value="<%= customer.getID()%>">
                                <!-- Dropdown sắp xếp -->
                                <select name="sortBy" class="sort-dropdown" onchange="this.form.submit()">
                                    <option class="dropdown-default" value="" disabled selected>Sắp xếp theo</option>
                                    <option class="dropdown-value" value="price_asc">Giá tăng dần</option>
                                    <option class="dropdown-value" value="price_desc">Giá giảm dần</option>
                                    <option class="dropdown-value" value="name_import">Nhập Hàng</option>
                                    <option class="dropdown-value" value="name_export">Xuất Hàng</option>
                                </select>
                            </form>
                        </div>
                    </div>

                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Mã đơn</th>
                                    <th class="table-header-item">Tổng tiền</th>
                                    <th class="table-header-item">Loại đơn hàng</th>
                                    <th class="table-header-item">Ngày tạo</th>
                                    <th class="table-header-item">Ngày cập nhật</th>
                                    <th class="table-header-item">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (orders != null && !orders.isEmpty()) {
                                        for (Orders order : orders) {
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><%= order.getID()%></td>
                                    <td class="table-cell"><%= currencyFormat.format(order.getTotalAmount()) + " VND"%></td>
                                    <td class="table-cell">
                                        <%
                                            int status = order.getStatus();
                                            String statusText = "";
                                            switch (status) {
                                                case -1:
                                                    statusText = "Xuất hàng";
                                                    break;
                                                case 1:
                                                    statusText = "Nhập hàng";
                                                    break;
                                                default:
                                                    statusText = "Trạng thái khác";
                                            }
                                        %>
                                        <%= statusText%>
                                    </td>
                                    <td class="table-cell"><%= order.getCreateAt()%></td>
                                    <td class="table-cell"><%= order.getUpdateAt()%></td>
                                    <td class="table-cell"><button class="action-button" onclick="window.location.href = 'listorderitems?id=<%= order.getID()%>'">Chi tiết mặt hàng</button></td>
                                </tr>
                                <% }
                            } else { %>
                                <tr>
                                    <td colspan="5" class="table-cell">Không có đơn hàng nào.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Phân trang -->
                    <!-- Phân trang -->
                    <!-- Phân trang -->
                    <div class="pagination">
                        <div class="pagination-controls">
                            <button 
    class="pagination-button" 
    <% if (currentPage <= 1) { %> disabled <% }%> 
    onclick="window.location.href = 'listcustomerorders?customerid=<%= customer.getID()%>&page=<%= currentPage - 1%>&sortBy=<%= sortBy != null ? sortBy : ""%>'">
    Trước
</button>

<span class="pagination-info">Trang <%= currentPage%> / <%= totalPages%></span>

<button 
    class="pagination-button" 
    <% if (currentPage >= totalPages) { %> disabled <% }%> 
    onclick="window.location.href = 'listcustomerorders?customerid=<%= customer.getID()%>&page=<%= currentPage + 1%>&sortBy=<%= sortBy != null ? sortBy : ""%>'">
    Sau
</button>

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
