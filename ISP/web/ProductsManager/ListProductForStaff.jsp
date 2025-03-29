<%@page import="dal.DAOShops"%>
<%@page import="model.Shops"%>
<%@page import="model.Zones"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Products" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
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
            DAOShops daoShop = new DAOShops();
            Shops shop = (Shops) session.getAttribute("shop");
            DAOUser dao = new DAOUser();
            Users u = (Users) request.getAttribute("user");
            ArrayList<Products> products = (ArrayList<Products>) request.getAttribute("products");
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        %>
        <%
            Integer currentPage = (Integer) request.getAttribute("currentPage");
            Integer totalPages = (Integer) request.getAttribute("totalPages");

            // Kiểm tra xem các biến có được nhận hay không
            if (currentPage == null || totalPages == null) {
                out.println("<script>alert('Không thể nhận được currentPage hoặc totalPages.');</script>");
            }
        %>
        <!-- Homepage Header -->
        <div class="header">
            <div class="container">
                <a href="shopdetail">
                    <img src="<%=shop.getLogoShop()%>" alt="logo" class="home-logo">
                </a>
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name">
                    <%= u.getFullName()%>
                </span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id=<%= u.getID()%>"class="navbar__info--item">Tài khoản của tôi</a>
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
                        <li class="mainmenu__list-item"><a href="historyexport"><i class="fa-solid fa-history list-item-icon"></i>Lịch Sử Giá</a></li>
                    </ul>
                </div>

                <!-- HomePage Body -->
                <div class="homepage-body">
                    <div class="body-head">
                        <h5 class="body__head-title">Thông tin sản phẩm</h5>
                        <div class="search-container">
                            <form action="listproducts" method="post">
                                <input type="text" id="information" name="information" placeholder="Tìm kiếm sản phẩm..." class="search-input">
                                <input type="submit" class="search-button" value="Tìm Kiếm">
                            </form>
                            <% String message = (String) request.getAttribute("message"); %>
                            <% if (message != null && !message.isEmpty()) {%>
                            <div id="toast-message" class="toast-message"><%= message%></div>
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
                            
                            <form action="listproducts" method="get">
                                <!-- Dropdown sắp xếp -->
                                <select name="sortBy" class="sort-dropdown" onchange="this.form.submit()">
                                    <option class="dropdown-default" value="" disabled selected>Sắp xếp theo</option>
                                    <option class="dropdown-value" value="price_asc">Giá tăng dần</option>
                                    <option class="dropdown-value" value="price_desc">Giá giảm dần</option>
                                    <option class="dropdown-value" value="quantity_asc">Số lượng ít → nhiều</option>
                                    <option class="dropdown-value" value="quantity_desc">Số lượng nhiều → ít</option>
                                    <option class="dropdown-value" value="name_asc">Tên A → Z</option>
                                    <option class="dropdown-value" value="name_desc">Tên Z → A</option>
                                </select>
                            </form>

                          
                        </div>
                    </div>
                    <!-- Product List -->
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Hình ảnh</th>
                                    <th class="table-header-item" style="max-width: 24px">Mã sản phẩm</th>
                                    <th class="table-header-item">Tên sản phẩm</th>
                                    <th class="table-header-item">Giá tiền / KG</th>
                                    <th class="table-header-item" style="max-width: 30px">Số lượng(kg)</th>
                                    <th class="table-header-item">Vị trí</th>
                                    <th class="table-header-item" style="width: 180px">Mô tả</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    DAOProducts dao1 = new DAOProducts();

                                    for (Products product : products) {
                                        if (shop.getID() == product.getShopID()) {
                                           
                                                // Lấy danh sách khu vực cho sản phẩm
                                                String zoneDisplay = "Trống"; // Giá trị mặc định
                                                ArrayList<Zones> zonesList = dao1.getZonesByProductId(product.getID());
                                                if (zonesList != null && !zonesList.isEmpty()) {
                                                    StringBuilder zonesNames = new StringBuilder();
                                                    for (Zones zone : zonesList) {
                                                        zonesNames.append(zone.getZoneName()).append(", ");
                                                    }
                                                    // Xóa dấu phẩy cuối
                                                    zoneDisplay = zonesNames.length() > 0 ? zonesNames.substring(0, zonesNames.length() - 2) : zoneDisplay;
                                                }
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><img src="<%= product.getImageLink()%>" alt="<%= product.getProductName()%>"
                                                                class="product-image"></td>
                                    <td class="table-cell"><%= product.getID()%></td>
                                    <td class="table-cell"><%= product.getProductName()%></td>
                                    <td class="table-cell"><%= currencyFormat.format(product.getPrice()) + " VND"%></td>
                                    <td class="table-cell"><%= product.getQuantity()%></td>
                                    <td class="table-cell"><%= zoneDisplay%></td>                                    
                                    <td class="table-cell description" style="height: 80px"><%= product.getDescription()%></td>

                                </tr>
                                <%
                                            
                                        }

                                    }
                                %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->

                    <div class="pagination">
                        <div class="pagination-controls">
                            <button 
                                class="pagination-button" 
                                <% if (currentPage <= 1) { %> disabled <% }%> 
                                onclick="window.location.href = 'listproducts?page=<%= currentPage - 1%>'">Trước</button>

                            <span class="pagination-info">Trang <%= currentPage%> / <%= totalPages%></span>

                            <button 
                                class="pagination-button" 
                                <% if (currentPage >= totalPages) { %> disabled <% }%> 
                                onclick="window.location.href = 'listproducts?page=<%= currentPage + 1%>'">Sau</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <div class="container">
                <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>

    </body>
</html>