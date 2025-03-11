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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <script>
                $(document).ready(function() {
                    $('#area').select2({
                        placeholder: "Chọn khu vực",
                        allowClear: true,
                        tags: true
                    });
                });
            
            function filterCustomers() {
                let input = document.getElementById("customers").value.toLowerCase();
                let select = document.getElementById("customerList");
                let options = select.getElementsByTagName("option");
                let hasMatch = false;

                for (let option of options) {
                    if (option.text.toLowerCase().includes(input)) {
                        option.style.display = "block";
                        hasMatch = true;
                    } else {
                        option.style.display = "none";
                    }
                }

                select.classList.toggle("hidden", !hasMatch);
            }

            function filterProducts() {
                let input = document.getElementById("productSearch").value.toLowerCase();
                let select = document.getElementById("productDropdown");
                let options = select.getElementsByTagName("option");
                let hasMatch = false;

                for (let option of options) {
                    if (option.text.toLowerCase().includes(input)) {
                        option.style.display = "block";
                        hasMatch = true;
                    } else {
                        option.style.display = "none";
                    }
                }

                select.classList.toggle("hidden", !hasMatch);
            }

            // Khi mất focus, ẩn danh sách và xóa giá trị nhập vào
            function clearCustomerSearch() {
                document.getElementById("customers").value = "";
                document.getElementById("customerList").classList.add("hidden");
            }

            function clearProductSearch() {
                document.getElementById("productSearch").value = "";
                document.getElementById("productDropdown").classList.add("hidden");
            }
        </script>
        
    </head>
    <body>
                            <%      DAOShops daoShop = new DAOShops();
                                Shops shop = (Shops) session.getAttribute("shop");
                                DAOUser dao = new DAOUser();
                                Users u = (Users) request.getAttribute("user");
                            %>
        <div class="search-container">
            <div class="customer-search">
                <h2 class="search-heading">Tìm Khách Hàng</h2>
                <input type="text" placeholder="Nhập tên khách hàng..." id="customers" 
                       oninput="filterCustomers()" onblur="clearCustomerSearch()">
                <select id="customerList" name="customers" multiple="multiple" required class="hidden">
                    <%
                        ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
                        if (customers != null) {
                            for (Customers customer : customers) {
                    %>
                    <option value="<%= customer.getID() %>"><%= customer.getName() %></option>
                    <%
                            }
                        }
                    %>
                </select>

            </div>

            <div class="product-search">
                <h2 class="search-heading">Tìm Sản Phẩm</h2>
                <input type="text" placeholder="Nhập tên sản phẩm..." id="productSearch" 
                       oninput="filterProducts()" onblur="clearProductSearch()">
                <select id="productDropdown" name="products" multiple="multiple" required class="hidden">
                    <%
                        ArrayList<Products> products = (ArrayList<Products>) request.getAttribute("products");
                        if (products != null) {
                            for (Products product : products) {
                    %>
                    <option value="<%= product.getID() %>"><%= product.getProductName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
        </div>

                <!-- <form action="addexportorder" method="POST"> -->
            <div class="invoice-details">
                <h2>Chi Tiết Hóa Đơn</h2>
                <h4>Loại Hóa Đơn: Xuất</h4>

                <div class="invoice-info">
                    <div class="input-container">
                        <label for="customerName">Tên Khách Hàng:</label>
                        <input class="input-info" type="text" id="customerName">
                    </div>
                    <div class="input-container">
                        <label for="customerPhone">Số ĐT:</label>
                        <input class="input-info" type="text" id="customerPhone">
                    </div>
                    <div class="input-container">
                        <label for="creater">Người tạo:</label>
                        <input class="input-info" type="text" id="creater" placeholder="" value="<%= u.getFullName() %>">
                    </div>
                </div>

                <div class="product-list">
                    <table>
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
                            <!-- Sản phẩm sẽ được thêm vào đây -->
                        </tbody>
                    </table>
                </div>

                <div class="input-container">
                    <label for="totalCost">Tổng chi phí:</label>
                    <input class="input-info" type="number" id="totalCost" readonly>
                </div>
                <div class="action-buttons">
                    <button id="createButton" type="submit">Tạo</button>
                    <button id="createButton" onclick="window.location.href = 'listorders'">Hủy</button>
                </div>
            </div>
<!--        </form>-->

        <script>
            const productSearch = document.getElementById('productSearch');
            const customerSearch = document.getElementById('customers');
            const customerName = document.getElementById('customerName');
            const productList = document.getElementById('productList');
            const totalCost = document.getElementById('totalCost');

            // thêm product mới cho bảng hóa đơn
                productSearch.addEventListener('keypress', function (event) {
                if (event.key === 'Enter' && productSearch.value) {
                    let selectedProduct = null;
                    let options = document.querySelectorAll('#productDropdown option');

                    // Tìm sản phẩm theo tên nhập vào
                    for (let option of options) {
                        if (option.text.toLowerCase() === productSearch.value.toLowerCase()) {
                            selectedProduct = {
                                id: option.value,
                                name: option.text
                            };
                            break;
                        }
                    }

                    if (!selectedProduct) {
                        alert('Sản phẩm không tồn tại! Vui lòng chọn từ danh sách.');
                        return;
                    }

                    const newRow = document.createElement('tr');
                    newRow.innerHTML = `
                        <td>${selectedProduct.ProductName()}</td>
                        <td><input type="number" value="1" min="1" style="width: 100px" class="qty"></td>
                        <td><input type="text" placeholder="Khu vực" class="area"> 
                        <select id="area" name="area" multiple="multiple" required>
                            <%
                                List<Zones> zones = (List<Zones>) request.getAttribute("zones"); // Lấy danh sách khu vực từ request
                                if (zones != null) {
                                    for (Zones zone : zones) {
                                    if(zone.getShopID() == shop.getID()){
                            %>
                                <option value="<%= zone.getID() %>"><%= zone.getZoneName() %></option>
                            <%
                                    }
                                    }
                                }
                            %>
                        </select>
    
                        </td>
                        <td><input type="text" placeholder="kg/bao" class="spec"></td>
                        <td><input type="number" placeholder="Giá gốc" class="price"></td>
                        <td><input type="number" placeholder="Giảm giá" class="discount" value="0"></td>
                        <td><input type="text" placeholder="Thành tiền" style="width: 180px" readonly class="total"></td>
                        <td><button class="delete-btn">Xóa</button></td>
                    `;

                    productList.appendChild(newRow);
                    productSearch.value = ''; // Xóa ô tìm kiếm sau khi thêm sản phẩm

                    const qtyInput = newRow.querySelector('.qty');
                    const priceInput = newRow.querySelector('.price');
                    const discountInput = newRow.querySelector('.discount');
                    const specInput = newRow.querySelector('.spec');
                    const totalInput = newRow.querySelector('.total');
                    const deleteBtn = newRow.querySelector('.delete-btn');

                    const calculateTotal = () => {
                    const qty = parseFloat(qtyInput.value) || 0;
                    const price = parseCurrency(priceInput.value);
                    const discount = parseCurrency(discountInput.value);
                    const total = (price - discount) * qty;

                    totalInput.value = formatCurrency(total);
                    updateGrandTotal(); // Cập nhật tổng chi phí
                };

                const parseCurrency = (value) => {
                    return parseFloat(value.replace(/[,.]/g, '')) || 0;
                };

                const formatCurrency = (value) => {
                    return value.toLocaleString('vi-VN'); // Định dạng chuẩn VNĐ
                };

                const updateGrandTotal = () => {
                    let grandTotal = 0;
                    document.querySelectorAll('.total').forEach(total => {
                        grandTotal += parseCurrency(total.value);
                    });
                    totalCost.value = formatCurrency(grandTotal);
                };

                // Thêm sự kiện cho các ô nhập liệu
                [qtyInput, priceInput, discountInput].forEach(input => {
                    input.addEventListener('input', calculateTotal);
                });

                // Xóa sản phẩm và cập nhật tổng chi phí
                deleteBtn.addEventListener('click', () => {
                    productList.removeChild(newRow);
                    updateGrandTotal(); 
                });
                }
            });

                customerSearch.addEventListener('keypress', function (event) {
                if (event.key === 'Enter' && customerSearch.value) {
                    let selectedCustomer = null;
                    let options = document.querySelectorAll('#customerList option'); // Lấy danh sách khách hàng

                    // Tìm khách hàng theo tên nhập vào
                    for (let option of options) {
                        if (option.text.toLowerCase() === customerSearch.value.toLowerCase()) {
                            selectedCustomer = {
                                id: option.value,
                                name: option.text
                            };
                            break;
                        }
                    }

                    if (!selectedCustomer) {
                        alert('Khách hàng không tồn tại! Vui lòng chọn từ danh sách.');
                        customerSearch.value = ''; // Xóa ô nhập nếu không tìm thấy
                        return;
                    }

                    customerName.value = selectedCustomer.name; // Điền tên khách hàng vào trường
                    customerSearch.value = ''; // Xóa ô tìm kiếm tên khách hàng
                    customerName.focus(); // Đưa con trỏ đến ô tên khách hàng
                }
            });


        </script>
        

    </body>
</html>