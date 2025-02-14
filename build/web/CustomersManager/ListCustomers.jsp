<%-- 
    Document   : ListCustomers
    Created on : Feb 7, 2025, 5:09:42 PM
    Author     : ADMIN
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Customers" %>
<%@ page import="Model.Users" %>
<%@ page import="DAL.DAOCustomers" %>
<%@ page import="DAL.DAOUser" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ Quản lý</title>
    <link rel="stylesheet" href="css/home2.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body>
    <% 
        DAOUser dao = new DAOUser();
        Users u = (Users) request.getAttribute("user");
        ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
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
                    <a href="" class="navbar__info--item">Đăng xuất</a>
                </div>
            </div>
        </div>
    </div>

    <div class="body">
        <div class="body-container">
            <div class="mainmenu">
                <ul class="mainmenu-list row no-gutters">
                    <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-bowl-rice list-item-icon"></i>Sản Phẩm</a></li>
                    <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-box list-item-icon"></i>Kho</a></li>
                    <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                    <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                    <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                    <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                </ul>
            </div>

            <div class="homepage-body">
                <div class="body-head">
                    <h3 class="body__head-title">Thông tin khách hàng</h3>
                    <div class="search-container">
                        <form action="listcustomers" method="post">
                            <input type="text" id="information" name="information" placeholder="Tìm kiếm khách hàng..." class="search-input">
                            <input type="submit" value="Search">
                        </form>
                        <% String message = (String) request.getAttribute("message"); if (message != null && !message.isEmpty()) { %>
                        <p style="color: red;"><%= message %></p>
                        <% } %>
                    </div>
                </div>
                <div class="table-container">
                    <table class="product-table">
                        <thead>
                            <tr class="table-header">
                                <th class="table-header-item">ID</th>
                                <th class="table-header-item">Tên</th>
                                <th class="table-header-item">Ví</th>
                                <th class="table-header-item">Điện thoại</th>
                                <th class="table-header-item">Địa chỉ</th>
                                <th class="table-header-item">Ngày tạo</th>
                                <th class="table-header-item">Ngày cập nhật</th>
                                <th class="table-header-item">Người tạo</th>
                                <th class="table-header-item">Xóa</th>
                                <th class="table-header-item">Ngày xóa</th>
                                <th class="table-header-item">Người xóa</th>
                                <th class="table-header-item">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (customers != null && !customers.isEmpty()) { 
                                for (Customers cus : customers) { %>
                            <tr class="table-row">
                                <td class="table-cell"><%= cus.getID() %></td>
                                <td class="table-cell"><%= cus.getName() %></td>
                                <td class="table-cell"><%= cus.getWallet() %></td>
                                <td class="table-cell"><%= cus.getPhone() %></td>
                                <td class="table-cell"><%= cus.getAddress() %></td>
                                <td class="table-cell"><%= cus.getCreateAt() %></td>
                                <td class="table-cell"><%= cus.getUpdateAt() %></td>
                                <td class="table-cell"><%= dao.getUserByID(cus.getCreateBy()).getFullName() %></td>
                                <td class="table-cell"><%= (cus.getIsDelete() == 0) ? "Active" : "Ban" %></td>
                                <td class="table-cell"><%= cus.getDeletedAt() %></td>
                                <td class="table-cell"><%= (cus.getIsDelete() == 0) ? "Null" : dao.getUserByID(cus.getDeleteBy()).getFullName() %></td>
                                <td class="table-cell">
                                    <button class="action-button" onclick="window.location.href = 'updatecustomer?id=<%= cus.getID() %>'">Edit</button>
                                    <button class="action-button" onclick="window.location.href = 'deletecustomer?deleteid=<%= cus.getID() %>&userid=<%= u.getID() %>'">Ban</button>
                                    <button class="action-button" onclick="window.location.href = 'listcustomerdebtrecords?customerid=<%= cus.getID() %>'">Công nợ</button>
                                </td>
                            </tr>
                            <% } 
                            } else { %>
                            <tr><td colspan="12">Không có khách hàng nào.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <div class="pagination">
                    <button class="pagination-button" id="prev-button" onclick="prevPage()">Trước</button>
                    <span class="pagination-info">Trang <span class="current-page" id="current-page">1</span> / <span class="total-pages" id="total-pages">5</span></span>
                    <button class="pagination-button" id="next-button" onclick="nextPage()">Sau</button>
                </div>
            </div>
        </div>
    </div>

    <div id="product-detail-modal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <h2>Chi tiết khách hàng</h2>
            <div class="modal-body">
                <p><strong>ID:</strong> <span id="modal-product-code"></span></p>
                <p><strong>Tên:</strong> <span id="modal-product-name"></span></p>
                <p><strong>Ví:</strong> <span id="modal-product-wallet"></span></p>
                <p><strong>Điện thoại:</strong> <span id="modal-product-phone"></span></p>
                <p><strong>Địa chỉ:</strong> <span id="modal-product-address"></span></p>
                <p><strong>Ngày tạo:</strong> <span id="modal-product-create-at"></span></p>
                <p><strong>Ngày cập nhật:</strong> <span id="modal-product-update-at"></span></p>
                <p><strong>Người tạo:</strong> <span id="modal-product-create-by"></span></p>
                <p><strong>Xóa:</strong> <span id="modal-product-is-delete"></span></p>
                <p><strong>Ngày xóa:</strong> <span id="modal-product-delete-at"></span></p>
                <p><strong>Người xóa:</strong> <span id="modal-product-delete-by"></span></p>
                <div class="modal-actions">
                    <button class="action-button">Sửa</button>
                    <button class="action-button" id="delete-button">Xóa</button>
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