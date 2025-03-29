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
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý đơn hàng</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer"/>
    </head>
    <body>
        <%
            DAOUser dao = new DAOUser();
            DAOCustomers dao1 = new DAOCustomers();
            Shops shop = (Shops) session.getAttribute("shop");
            Users u = (Users) request.getAttribute("user");
            ArrayList<Orders> orders = (ArrayList<Orders>) request.getAttribute("orders");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

            // Kiểm tra null để tránh lỗi
            if (u == null) {
                u = (Users) session.getAttribute("user"); // Fallback nếu không có trong request
            }
            if (shop == null || u == null) {
                out.println("<script>alert('Vui lòng đăng nhập lại để tiếp tục.'); window.location.href='login';</script>");
                return;
            }
        %>

        <%
            Integer currentPage = (Integer) request.getAttribute("currentPage");
            Integer totalPages = (Integer) request.getAttribute("totalPages");

            // Đặt giá trị mặc định nếu currentPage hoặc totalPages bị null
            if (currentPage == null) currentPage = 1;
            if (totalPages == null) totalPages = 1;
        %>

        <%
            String statusParam = request.getAttribute("selectedStatus") != null ? "&status=" + request.getAttribute("selectedStatus") : "";
            String searchInfo = (String) session.getAttribute("searchInfo");
            String searchParam = searchInfo != null && !searchInfo.trim().isEmpty() ? "&information=" + java.net.URLEncoder.encode(searchInfo, "UTF-8") : "";
        %>

        <div class="header">
            <div class="container">
                <a href="shopdetail">
                    <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                </a>
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"><%= u.getFullName() %></span>
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
                        <div class="search-container">
                            <form action="listorders" method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm đơn hàng..."
                                       class="search-input" value="">
                                <button type="submit" class="search-button">Search</button>
                            </form>
                            <div class="status-filter-container">

                            </div>
                            <% String message = (String) request.getAttribute("message"); %>
                            <% if (message != null && !message.isEmpty()) { %>
                            <div id="toast-message" class="toast-message"><%= message %></div>
                            <% } %>

                            <script>
                                window.onload = function () {
                                    var toast = document.getElementById("toast-message");
                                    if (toast) {
                                        toast.style.display = "block";
                                        setTimeout(function () {
                                            toast.style.opacity = "0";
                                            setTimeout(() => toast.style.display = "none", 500);
                                        }, 3000);
                                    }
                                };
                            </script>
                            <form action="listorders" method="get">
                                <select name="status" class="sort-dropdown" onchange="this.form.submit()">
                                    <option class="dropdown-default" value="" <%= request.getAttribute("selectedStatus") == null ? "selected" : "" %>>Tất cả đơn hàng</option>
                                    <option lass="dropdown-value" value="1" <%= request.getAttribute("selectedStatus") != null && ((Integer) request.getAttribute("selectedStatus")) == 1 ? "selected" : "" %>>Nhập hàng</option>
                                    <option lass="dropdown-value" value="-1" <%= request.getAttribute("selectedStatus") != null && ((Integer) request.getAttribute("selectedStatus")) == -1 ? "selected" : "" %>>Bán hàng</option>
                                </select>
                            </form>
                            <div class="addorder__btn">
                                <a href="addimportorder" class="add-product-button">Nhập đơn hàng</a>
                                <a href="addexportorder" class="add-product-button">Xuất đơn hàng</a>
                            </div>
                        </div>
                    </div>

                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Tên khách hàng</th>
                                    <th class="table-header-item">Số tiền</th>
                                    <th class="table-header-item">Trạng thái</th>
                                    <th class="table-header-item">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (orders != null && !orders.isEmpty()) {
                                    for (Orders order : orders) {
                                        // Bỏ điều kiện order.getShopID() == u.getShopID() vì servlet đã lọc rồi
                                        Customers customer = dao1.getCustomersByID(order.getCustomerID());
                                        String customerName = (customer != null) ? customer.getName() : "Khách không xác định";
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><%= customerName %></td>
                                    <td class="table-cell"><%= currencyFormat.format(order.getTotalAmount()) %> VND</td>
                                    <td class="table-cell">
                                        <% if (order.getStatus() == 1) { %>
                                        Nhập hàng
                                        <% } else if (order.getStatus() == -1) { %>
                                        Bán hàng
                                        <% } else { %>
                                        Không xác định
                                        <% } %>
                                    </td>
                                    <td class="table-cell">
                                        <button class="action-button"
                                                onclick="window.location.href = 'listorderitems?id=<%= order.getID() %>'">Chi tiết
                                        </button>
                                    </td>
                                </tr>
                                <% }
                    } else { %>
                                <tr>
                                    <td colspan="4" class="table-cell">Không có đơn hàng nào để hiển thị.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <div class="pagination">
                        <div class="pagination-controls">
                            <button class="pagination-button" <%= currentPage <= 1 ? "disabled" : "" %>
                                    onclick="window.location.href = 'listorders?page=<%= currentPage - 1 %>&status=<%= request.getAttribute("selectedStatus") != null ? request.getAttribute("selectedStatus") : "" %>'">
                                Trước
                            </button>
                            <span class="pagination-info">Trang <%= currentPage %> / <%= totalPages %></span>
                            <button class="pagination-button" <%= currentPage >= totalPages ? "disabled" : "" %>
                                    onclick="window.location.href = 'listorders?page=<%= currentPage + 1 %>&status=<%= request.getAttribute("selectedStatus") != null ? request.getAttribute("selectedStatus") : "" %>'">
                                Sau
                            </button>


                        </div>
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