<%-- 
    Document   : AddImportOrder
    Created on : Mar 9, 2025, 10:30:02 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Orders" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="model.Zones" %>
<%@ page import="model.Products" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOZones" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hóa Đơn Nhập Kho</title>
        <link rel="stylesheet" href="css/addorder.css">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        
        <script>
    function openNewInvoiceTab() {
        window.open('addimportorder', '_blank'); // Opens the add order page in a new tab
    }
</script>

    </head>
    <body>
        <% DAOUser dao = new DAOUser(); 
        List<Zones> zones = (List<Zones>) request.getAttribute("zones");
        ArrayList<Products> products = (ArrayList<Products>) request.getAttribute("products");
        ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
        Users u = (Users) request.getAttribute("user"); 
        %>
        
        <form action="addorder" method="post" id="invoiceForm">
            <div class="invoice-details">
                <h2>Chi Tiết Hóa Đơn</h2>
                <div class="input-container">
                    <label>Loại Hóa Đơn:</label>
                    <input type="hidden" name="orderType" value="1"> <!-- Gửi giá trị 1 -->
                    <span class="input-info">Nhập Hàng</span> <!-- Hiển thị cho người dùng -->
                </div>


                <div class="invoice-info">
                    <div class="input-container">
                        <label for="customerName">Tên Khách Hàng:</label>
                        <input class="input-info" type="text" id="customerName" name="customerName" list="customersList" placeholder="Tìm kiếm khách hàng..." required>
                        <datalist id="customersList">
                            <%
                           if (customers != null && !customers.isEmpty()) {
                           for (Customers customer1 : customers) { 
                                if(customer1.getShopID() == u.getShopID()){
                            %>
                            <option value="<%= customer1.getName() %>"></option>
                            <% } } }%>
                        </datalist>
                    </div>
                    <div class="input-container">
                        <label for="customerPhone">Số ĐT:</label>
                        <input class="input-info" type="text" id="customerPhone" name="customerPhone">
                    </div>

                    <div class="input-container">
                        <label for="creater">Người tạo:</label>
                        <input class="input-info" type="text" id="creater" value="<%= u.getFullName() %>" readonly>
                    </div>
                </div>

                <div class="product-list">
                    <table id="productTable">
                        <thead>
                            <tr>
                                <th>Tên Sản Phẩm</th>
                                <th>Số Lượng</th>
                                <th>Khu Vực</th>
                                <th>Quy Cách</th>
                                <th>Giá Gốc</th>
                                <th>Giảm Giá</th>
                                <th>Thành Tiền</th>
                                <th>Xóa</th>
                            </tr>
                        </thead>
                        <tbody id="productList">
                            <tr>
                                <td>
                                    <select class="area-select" name="productName" required style="width: 100%;">
                                         <% 
                                           for (Products product : products) { 
                                                if(product.getShopID() == u.getShopID()) {
                                        %>
                                        <option  value="<%= product.getID() %>"><%= product.getProductName() %></option>
                                        <% } }%>
                                    </select>
                                </td>
                                <td><input type="number" name="quantity" min="1" required onchange="calculateTotal()"></td>
                                <td>
                                    <select class="area-select" name="area" multiple="multiple" required style="width: 100%;">
                                        <% 
                                           for (Zones zone : zones) { 
                                                if(zone.getShopID() == u.getShopID()) { 
                                        %>
                                        <option value="<%= zone.getID() %>"><%= zone.getZoneName() %></option>
                                        <% } } %>
                                    </select>
                                </td>
                                <td><input type="text" name="spec" placeholder="Kg/bao"></td>
                                <td><input type="number" name="price" min="0" value="0" required onchange="calculateTotal()"></td>
                                <td><input type="number" name="discount" min="0" value="0" onchange="calculateTotal()"></td>
                                <td><input type="text" name="total" readonly></td>
                                <td><button type="button" onclick="deleteProductRow(this)">Xóa</button></td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" onclick="addProductRow()">Thêm Sản Phẩm</button>
                </div>
            <script>
            $(document).ready(function () {
                $('.area-select').select2({
                    placeholder: "Chọn...",
                    allowClear: true
                });
            });
            
            const productList = [
                <% if (products != null) { 
                    for (Products product : products) { 
                        if(product.getShopID() == u.getShopID()) { %>
                            { id: '<%= product.getID() %>', text: '<%= product.getProductName() %>' },
               <%    } 
                  } 
               } %>
            ];

            const zoneList = [
                <%  if (zones != null) {
                    for (Zones zone : zones) { 
                    if(zone.getShopID() == u.getShopID()) { %>
                        { id: '<%= zone.getID() %>', text: '<%= zone.getZoneName() %>' },
                <% } } } %>
            ];
            
                    function calculateTotal() {
    let grandTotal = 0;

    $('#productList tr').each(function () {
        // Get the product ID from the select
        const productId = $(this).find('select[name="productName"]').val();
        
        // Get other input values
        let quantityVal = $(this).find('input[name="quantity"]').val();
        let priceVal = $(this).find('input[name="price"]').val();
        let discountVal = $(this).find('input[name="discount"]').val();

        const quantity = quantityVal.trim() === "" ? 0 : parseInt(quantityVal);
        const price = priceVal.trim() === "" ? 0 : parseFloat(priceVal);
        const discount = discountVal.trim() === "" ? 0 : parseFloat(discountVal);

        // Calculate total price
        let totalPrice = (price * quantity) - discount;
        totalPrice = totalPrice < 0 ? 0 : totalPrice;

        // Update total input field
        $(this).find('input[name="total"]').val(totalPrice.toLocaleString('vi-VN'));

        // Add to grand total
        grandTotal += totalPrice;

        // Here you can also save the product ID and quantity to update later
        console.log(`Product ID: ${productId}, Quantity: ${quantity}`);
    });

    // Display grand total
    $('#totalCost').val(grandTotal.toLocaleString('vi-VN'));
}


            function addProductRow() {
                const newRow = `
                    <tr>
                        <td>
                            <select class="area-select product-select" name="productName" required style="width: 100%;">
                            </select>
                        </td>
                        <td><input type="number" name="quantity" min="1" required onchange="calculateTotal()"></td>
                        <td>
                            <select class="area-select zone-select" name="area" multiple="multiple" required style="width: 100%;">
                            </select>
                        </td>
                        <td><input type="text" name="spec" placeholder="Kg/bao"></td>
                        <td><input type="number" name="price" min="0" value="0" required onchange="calculateTotal()"></td>
                        <td><input type="number" name="discount" min="0" value="0" onchange="calculateTotal()"></td>
                        <td><input type="text" name="total" readonly></td>
                        <td><button type="button" onclick="deleteProductRow(this)">Xóa</button></td>
                    </tr>
                `;

                $('#productList').append(newRow);

                // Khởi tạo select2 với data cho dropdown vừa thêm
                const lastProductSelect = $('#productList').find('tr:last .product-select');
                const lastZoneSelect = $('#productList').find('tr:last .zone-select');

                lastProductSelect.select2({
                    data: productList,
                    placeholder: "Chọn...",
                    allowClear: true
                });

                lastZoneSelect.select2({
                    data: zoneList,
                    placeholder: "Chọn...",
                    allowClear: true
                });
            }

            function deleteProductRow(button) {
                $(button).closest('tr').remove();
                calculateTotal();
            }
        </script>
                <div class="input-container">
                    <label for="totalCost">Tổng chi phí:</label>
                    <input class="input-info" type="text" id="totalCost" name="totalCost" readonly>
                </div>

                <div class="action-buttons">
                    <button id="createButton" type="submit">Tạo</button>
                    <button id="createButton" onclick="window.location.href = 'listorders'">Hủy</button>
                    <button id="createButton" type="button" onclick="openNewInvoiceTab()">Thêm Hóa Đơn Mới</button>
                </div>
            </div>
        </form>
    </body>
</html>