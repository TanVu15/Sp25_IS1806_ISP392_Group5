<%-- 
    Document   : ListOrderItems
    Created on : Mar 19, 2025, 11:27:00 PM
    Author     : ADMIN
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Users" %>
<%@ page import="model.Shops" %>
<%@ page import="model.OrderItems" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Customers" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin đơn hàng</title>
    <link rel="stylesheet" href="css/product.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
       
        
        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
            z-index: 1000;
            width: 80%;
            max-width: 800px;
            max-height: 80vh;
            overflow-y: auto;
        }
        .popup-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
        .popup h2 { margin-bottom: 20px; }
        .popup .input-container { margin-bottom: 15px; }
        .popup .input-container label { font-weight: bold; margin-right: 10px; }
        .popup .input-container span { display: inline-block; }
        .popup table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .popup th, .popup td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        .popup th { background-color: #f2f2f2; }
        .close-button { position: absolute; top: 10px; right: 10px; font-size: 20px; cursor: pointer; }
        .content-wrapper { flex: 1 0 auto; }
        .footer { flex-shrink: 0; width: 100%; background-color: #f8f9fa; padding: 10px 0; text-align: center; }
    </style>
</head>
<body>
    <% 
        Shops shop = (Shops) session.getAttribute("shop");
        Users u = (Users) request.getAttribute("user");
        Orders order = (Orders) request.getAttribute("order");
        Customers customer = (Customers) request.getAttribute("customer");
        ArrayList<OrderItems> orderitems = (ArrayList<OrderItems>) request.getAttribute("orderitems");
        ArrayList<OrderItems> allOrderItems = (ArrayList<OrderItems>) request.getAttribute("allOrderItems");
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String message = (String) request.getAttribute("message");
        DAOUser daoUser = new DAOUser();
        Integer currentPage = (Integer) request.getAttribute("currentPage");
        Integer totalPages = (Integer) request.getAttribute("totalPages");
        String orderId = request.getAttribute("orderId") != null ? request.getAttribute("orderId").toString() : 
                        request.getParameter("id") != null ? request.getParameter("id") : "";

        String orderType = "Không xác định";
        if (order != null) {
            orderType = order.getStatus() == 1 ? "Nhập Hàng" : order.getStatus() == -1 ? "Xuất Hàng" : "Không xác định";
        }
    %>

    <div class="content-wrapper">
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
                    </ul>
                </div>

                <div class="homepage-body">
                    <div class="body-head">
                        <h3 class="body__head-title">Thông tin đơn hàng</h3>
                        <form action="listorderitems" method="post">
                            <input type="hidden" name="orderid" value="<%= orderId %>">
                            <input type="hidden" name="page" value="1">
                            <input type="text" id="information" name="information" placeholder="Nhập tên sản phẩm..." class="search-input">
                            <button type="submit" class="search-button">Tìm kiếm</button>
                        </form>
                    <button class="action-button" onclick="showPopup()">Xem hóa đơn</button>
                    <button class="action-button" onclick="window.location.href = 'listorders'">Quay lại</button>
                    </div>
                    
                    <% if (message != null) { %>
                        <p class="error"><%= message %></p>
                    <% } %>

                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Tên sản phẩm</th>
                                    <th class="table-header-item">Số lượng</th>
                                    <th class="table-header-item">Quy cách</th>
                                    <th class="table-header-item">Giá sản phẩm</th>
                                    <th class="table-header-item">Giảm giá</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (orderitems != null && !orderitems.isEmpty()) { 
                                    for (OrderItems o : orderitems) { 
                                        if (u != null && o.getShopID() == u.getShopID()) { %>
                                    <tr class="table-row">
                                        <td class="table-cell"><%= o.getProductName() %></td>
                                        <td class="table-cell"><%= o.getQuantity() %></td>
                                        <td class="table-cell"><%= o.getDescription() %> Kg/Bao</td>
                                        <td class="table-cell"><%= currencyFormat.format(o.getPrice()) %> VND</td>
                                        <td class="table-cell"><%= currencyFormat.format(o.getUnitPrice()) %> VND</td>
                                    </tr>
                                <%      }
                                    }
                                } else { %>
                                    <tr><td colspan="5">Không có sản phẩm nào trong đơn hàng.</td></tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <div class="pagination">
                        <div class="pagination-controls">
                            <% 
                                String searchTerm = (String) session.getAttribute("searchTerm");
                                String pageUrl = searchTerm != null ? 
                                    "listorderitems?information=" + searchTerm + "&id=" + orderId + "&page=" : 
                                    "listorderitems?id=" + orderId + "&page=";
                            %>
                            <button 
                                class="pagination-button" 
                                <% if (currentPage == null || currentPage <= 1) { %> disabled <% } %> 
                                onclick="window.location.href = '<%= pageUrl %><%= currentPage != null ? currentPage - 1 : 1 %>'">Trước</button>

                            <span class="pagination-info">Trang <%= currentPage != null ? currentPage : 1 %> / <%= totalPages != null ? totalPages : 1 %></span>

                            <button 
                                class="pagination-button" 
                                <% if (currentPage == null || totalPages == null || currentPage >= totalPages) { %> disabled <% } %> 
                                onclick="window.location.href = '<%= pageUrl %><%= currentPage != null ? currentPage + 1 : 1 %>'">Sau</button>
                        </div>
                    </div>

                    
                </div>
            </div>
        </div>

        <!-- Pop-up Overlay -->
        <div class="popup-overlay" onclick="hidePopup()"></div>
        <!-- Pop-up Hóa Đơn -->
        <div class="popup" id="invoicePopup">
            <span class="close-button" onclick="hidePopup()">×</span>
            <h2>Chi Tiết Hóa Đơn</h2>
            <div class="input-container">
                <label>Mã Đơn Hàng:</label>
                <span><%= order != null ? order.getID() : "Không xác định" %></span>
            </div>
            <div class="input-container">
                <label>Loại Hóa Đơn:</label>
                <span><%= orderType %></span>
            </div>
            <div class="input-container">
                <label>Tên Khách Hàng:</label>
                <span><%= customer != null ? customer.getName() : "Không xác định" %></span>
            </div>
            <div class="input-container">
                <label>Người Tạo Phiếu:</label>
                <span><%= order != null && order.getCreateBy() != 0 ? daoUser.getUserByID(order.getCreateBy()).getFullName() : "Không xác định" %></span>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Tên Sản Phẩm</th>
                        <th>Số Lượng</th>
                        <th>Quy Cách</th>
                        <th>Giá Gốc</th>
                        <th>Giảm Giá</th>
                        <th>Thành Tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        if (allOrderItems != null && !allOrderItems.isEmpty()) { 
                            for (OrderItems o : allOrderItems) { 
                                if (u != null && o.getShopID() == u.getShopID()) { 
                                    double itemTotal = o.getPrice() * o.getQuantity() - o.getUnitPrice();
                    %>
                    <tr>
                        <td><%= o.getProductName() %></td>
                        <td><%= o.getQuantity() %></td>
                        <td><%= o.getDescription() %> Kg/Bao</td>
                        <td><%= currencyFormat.format(o.getPrice()) %> VND</td>
                        <td><%= currencyFormat.format(o.getUnitPrice()) %> VND</td>
                        <td><%= currencyFormat.format(itemTotal) %> VND</td>
                    </tr>
                    <%          }
                            }
                        } else { %>
                    <tr>
                        <td colspan="6">Không có sản phẩm trong đơn hàng.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <div class="input-container">
                <label>Tổng Chi Phí:</label>
                <span><%= order != null ? currencyFormat.format(order.getTotalAmount()) : "Không xác định" %> VND</span>
            </div>
        </div>
    </div>

    <div class="footer">
        <div class="container">
            <p>© 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
        </div>
    </div>

    <script>
        function showPopup() {
            const popup = document.getElementById("invoicePopup");
            const overlay = document.querySelector(".popup-overlay");
            if (popup && overlay) {
                popup.style.display = "block";
                overlay.style.display = "block";
            }
        }
        function hidePopup() {
            const popup = document.getElementById("invoicePopup");
            const overlay = document.querySelector(".popup-overlay");
            if (popup && overlay) {
                popup.style.display = "none";
                overlay.style.display = "none";
            }
        }
    </script>
</body>
</html>