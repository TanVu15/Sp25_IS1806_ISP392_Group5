<%-- 
    Document   : HistoryExport
    Created on : Mar 24, 2025
    Author     : [Your Name]
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.ProductPriceHistory" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch sử giá bán</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <% 
            DAOUser dao = new DAOUser();
            DAOShops daoShop = new DAOShops();
            Shops shop = (Shops) session.getAttribute("shop");
            Users u = (Users) request.getAttribute("user");
            ArrayList<ProductPriceHistory> historyList = (ArrayList<ProductPriceHistory>) request.getAttribute("HistoryList");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Integer currentPage = (Integer) request.getAttribute("currentPage");
            Integer totalPages = (Integer) request.getAttribute("totalPages");
            String keyword = (String) request.getAttribute("keyword");
            String startDate = (String) request.getAttribute("startDate");
            String endDate = (String) request.getAttribute("endDate");
            String sortOrder = (String) request.getAttribute("sortOrder");
          
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

            if (currentPage == null || totalPages == null) {
                out.println("<script>alert('Không thể nhận được currentPage hoặc totalPages.');</script>");
            }
        %>
        <div class="header">
            <div class="container">
                <img src="<%= shop != null ? shop.getLogoShop() : "" %>" alt="logo" class="home-logo">
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"><%= u != null ? u.getFullName() : "Khách" %></span>
                <div class="navbar__user--info">
                    <div class="navbar__info--wrapper">
                        <a href="userdetail?id=<%= u != null ? u.getID() : 0 %>" class="navbar__info--item">Tài khoản của tôi</a>
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
                        <div class="search-container">
                            <form action="historyexport" method="get">
                                <input style="margin-right: 40px;" type="text" id="information" name="keyword" placeholder="Tìm kiếm sản phẩm..." class="search-input" value="<%= keyword != null ? keyword : "" %>">
                                <label for="sortOrder">Sắp xếp:</label>
                                <select style="margin-left: 0; margin-right: 30px;" id="sortOrder" class="sort-dropdown" name="sortOrder" onchange="this.form.submit();">
                                    <option class="dropdown-value" value="asc" <%= "asc".equals(sortOrder) ? "selected" : "" %>>Cũ nhất → Mới nhất</option>
                                    <option class="dropdown-value" value="desc" <%= "desc".equals(sortOrder) ? "selected" : "" %>>Mới nhất → Cũ nhất</option>
                                </select>
                                <label for="startDate">Từ ngày:</label>
                                <input type="date" id="startDate" name="startDate" placeholder="Từ ngày..." class="date-input" value="<%= startDate != null ? startDate : "" %>">
                                <label for="startDate">Đến ngày:</label>
                                <input type="date" id="endDate" name="endDate" placeholder="Đến ngày..." class="date-input" value="<%= endDate != null ? endDate : "" %>">
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
                                        toast.style.display = "block";
                                        setTimeout(function () {
                                            toast.style.opacity = "0";
                                            setTimeout(() => toast.style.display = "none", 500);
                                        }, 3000);
                                    }
                                };
                            </script>
                           
                        </div>
                    </div>
                             <div class="heading-title">
                        <h3 class="body__head-title">Lịch sử giá bán</h3>
                        <div class="button-filter">
                        <div>
                           <a href="historyimport" class="add-product-button">Lịch sử giá nhập</a>
                        </div>
                             <a href="historyexport?page=1&sortOrder=asc" class="add-product-button">Xóa bộ lọc</a>
                            </div>
                    </div>
                    <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>    
                            
                    <div class="table-container">
                        <table class="product-table">
                            <thead>
                                <tr class="table-header">
                                    <th class="table-header-item">Hình ảnh</th>
                                    <th class="table-header-item">Tên sản phẩm</th>
                                    <th class="table-header-item">Giá bán</th>
                                    <th class="table-header-item">Ngày thay đổi</th>
                                    <th class="table-header-item">Người thay đổi</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (historyList != null && !historyList.isEmpty()) { 
                                    for (ProductPriceHistory history : historyList) {
                                %>
                                <tr class="table-row">
                                    <td class="table-cell"><img src="<%= history.getImage() %>" alt="Ảnh sản phẩm" width="50" height="50"></td>
                                    <td class="table-cell"><%= history.getProductName() %></td>
                                    <td class="table-cell"><%= currencyFormat.format(history.getPrice()) + " VND"%></td>
                                    <td class="table-cell"><%= sdf.format(history.getChangedAt()) %></td>
                                    <td class="table-cell"><%= history.getChangedBy() %></td>
                                </tr>
                                <% } } else { %>
                                <tr class="table-row">
                                    <td colspan="5" class="table-cell" style="text-align: center;">Không có lịch sử giá bán nào.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <!-- Pagination -->
                    <div class="pagination">
                        <div class="pagination-controls">
                            <% 
                                String searchTerm = (String) (keyword != null ? keyword : "");
                                String pageUrl = "historyexport?";
                                if (!searchTerm.isEmpty()) pageUrl += "keyword=" + searchTerm + "&";
                                if (startDate != null && !startDate.isEmpty()) pageUrl += "startDate=" + startDate + "&";
                                if (endDate != null && !endDate.isEmpty()) pageUrl += "endDate=" + endDate + "&";
                                pageUrl += "sortOrder=" + (sortOrder != null ? sortOrder : "asc") + "&page=";
                            %>
                            <button 
                                class="pagination-button" 
                                <% if (currentPage <= 1) { %> disabled <% } %> 
                                onclick="window.location.href = '<%= pageUrl %><%= currentPage - 1 %>'">Trước</button>
                            <span class="pagination-info">Trang <%= currentPage %> / <%= totalPages %></span>
                            <button 
                                class="pagination-button" 
                                <% if (currentPage >= totalPages) { %> disabled <% } %> 
                                onclick="window.location.href = '<%= pageUrl %><%= currentPage + 1 %>'">Sau</button>
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
                        <script>
            
            async function exportToExcel() {
                try {
                    let response = await fetch("excelexport");
                    let data = await response.json();

                    let worksheet = XLSX.utils.json_to_sheet(data.map(item => ({
                            "Tên sản phẩm": item.productName,
                            "Giá": item.price +" " + "VND",
                            "Loại": item.priceType === "export" ? "Bán hàng" : item.priceType,
                            "Ngày thay đổi": item.changedAt,
                            "Người thay đổi": item.changedBy // Giờ đây là userName thay vì ID
                        })));

                    let workbook = XLSX.utils.book_new();
                    XLSX.utils.book_append_sheet(workbook, worksheet, "LichSuGia");

                    XLSX.writeFile(workbook, "LichSuGia.xlsx");
                } catch (error) {
                    console.error("Lỗi khi xuất Excel:", error);
                    alert("Có lỗi xảy ra khi xuất Excel!");
                }
            }
        </script>
    </body>
</html>