<%--
Document   : AddExportOrder
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
    <title>Hóa Đơn xuất kho</title>
    <link rel="stylesheet" href="css/exportorder.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        function openNewInvoiceTab() {
            window.open('addexportorder', '_blank'); // Opens the add order page in a new tab
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

<script>
    //tim kiem khach hang
    $(document).ready(function () {
        $("#searchCustomerInput").on("input", function () {
            let query = $(this).val();
            if (query.length > 0) {
                $.ajax({
                    url: "searchorder",
                    type: "POST",
                    data: { phone: query },
                    success: function (data) {
                        if (data.trim() !== "") {
                            $("#suggestionsCustomer").html(data).show();
                        } else {
                            $("#suggestionsCustomer").hide();
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error("Lỗi AJAX: " + status + " - " + error);
                        $("#suggestionsCustomer").html("<p>Có lỗi xảy ra trong quá trình tìm kiếm.</p>").show();
                    }
                });
            } else {
                $("#suggestionsCustomer").hide();
            }
        });
        // Ẩn danh sách khi click ra ngoài
        $(document).on("click", function (event) {
            if (!$(event.target).closest("#searchCustomerInput, #suggestionsCustomer").length) {
                $("#suggestionsCustomer").hide();
            }
        });
        // Hiển thị lại danh sách khi focus vào ô tìm kiếm (nếu có nội dung)
        $("#searchCustomerInput").on("focus", function () {
            if ($(this).val().length > 0) {
                $("#suggestionsCustomer").show();
            }
        });
    });
    // Hàm chọn khách hàng từ danh sách (giữ nguyên)
    function selectCustomer(id, name, phone) {
        $("#customerId").val(id);
        // Hiển thị thông tin khách hàng đã chọn
        $("#customerName").text("Khách hàng: " + name);
        $("#customerPhone").text("SĐT: " + phone);
        $("#customerInfo").show();

        // Reset lại thanh tìm kiếm
        $("#searchCustomerInput").val("");
        $("#suggestionsCustomer").hide();

    }

    //tim kiem san pham
    $(document).ready(function () {
        $("#search").on("input", function () {
            let query = $(this).val();
            if (query.length > 0) {
                $.ajax({
                    url: "searchorder",
                    type: "GET",
                    data: {
                        searchProduct: query,
                        orderType: -1
                    },
                    success: function (data) {
                        if (data.trim() !== "") {
                            $("#suggestions").html(data).show();
                        } else {
                            $("#suggestions").hide();
                        }
                    }
                });
            } else {
                $("#suggestions").hide();
            }
        });
        // Ẩn danh sách khi click ra ngoài
        $(document).on("click", function (event) {
            if (!$(event.target).closest("#search, #suggestions").length) {
                $("#suggestions").hide();
            }
        });
        // Hiển thị lại danh sách khi focus vào ô tìm kiếm (nếu có nội dung)
        $("#search").on("focus", function () {
            if ($(this).val().length > 0) {
                $("#suggestions").show();
            }
        });
    });

    // Hàm gọi khi sản phẩm được thêm vào đơn hàng
    function afterAddProduct() {
        // Reset lại thanh tìm kiếm sản phẩm
        $("#search").val("");
        $("#suggestions").hide();
    }

</script>

<div class="container">
    <h1>Hóa Đơn Bán Hàng</h1>

    <form action="addexportorder" method="post" id="invoiceForm" class="grid">
        <!-- Left Panel - Customer & Product Search -->
        <div class="space-y-6">
            <!-- Customer Search -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">
                        <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M20 21C20 16.5817 16.4183 13 12 13C7.58172 13 4 16.5817 4 21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        Tìm Kiếm Khách Hàng
                    </h2>
                </div>
                <div class="card-content">
                    <div class="search-container">
                        <div class="search-input-wrapper">
                            <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                            <input type="text" class="search-input" id="searchCustomerInput" placeholder="Tìm khách hàng theo SĐT...">
                        </div>

                        <div class="search-results" id="suggestionsCustomer" style="display: none;">
                            <!-- Customer search results will be populated here by AJAX -->
                        </div>
                    </div>

                    <div class="selected-customer" id="customerInfo" style="display: none;">
                        <h3 class="selected-customer-name" id="customerName"></h3>
                        <p class="selected-customer-phone" id="customerPhone"></p>
                        <input type="hidden" id="customerId" name="customerId">
                    </div>

                    <div class="form-group">
                        <input type="hidden" name="orderType" value="-1">
                        <div class="readonly-input">Loại Hóa Đơn: Bán Hàng</div>
                    </div>

                    <div class="form-group">
                        <input type="hidden" name="creater" value="<%= u.getFullName()%>">
                        <div class="readonly-input">Người tạo phiếu: <%= u.getFullName()%></div>
                    </div>
                </div>
            </div>

            <!-- Product Search -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">
                        <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M9 6L9 7C9 8.65685 10.3431 10 12 10C13.6569 10 15 8.65685 15 7V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M15 10C16.6569 10 18 8.65685 18 7V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M9 6C7.34315 6 6 7.34315 6 9V11.7C5.4022 12.0906 5.00003 12.7342 5.00003 13.5C5.00003 14.0906 5.20586 14.6272 5.54578 15.0511L5.0368 18.7812C5.01438 18.9031 5.00003 19.0294 5.00003 19.1572C5.00003 20.1784 5.82163 21 6.84277 21H17.1573C18.1784 21 19 20.1784 19 19.1572C19 19.0294 18.9857 18.9031 18.9632 18.7812L18.4542 15.0511C18.7942 14.6272 19 14.0906 19 13.5C19 12.7342 18.5978 12.0906 18 11.7V9C18 7.34315 16.6569 6 15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        Tìm Kiếm Sản Phẩm
                    </h2>
                </div>
                <div class="card-content">
                    <div class="search-container">
                        <div class="search-input-wrapper">
                            <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                            <input type="text" class="search-input" id="search" placeholder="Nhập tên sản phẩm...">
                        </div>

                        <div class="search-results" id="suggestions" style="display: none;">
                            <!-- Product search results will be populated here by AJAX -->
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Right Panel - Selected Products & Payment -->
        <div class="space-y-6">
            <!-- Selected Products -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Danh Sách Sản Phẩm</h2>
                </div>
                <div class="card-content">
                    <div class="table-container">
                        <table id="productTable">
                            <thead>
                            <tr>
                                <th>Tên Sản Phẩm</th>
                                <th class="text-center">Số lượng (Bao)</th>
                                <th class="text-center">Quy Cách (Kg/Bao)</th>
                                <th class="text-center">Giá Gốc</th>
                                <th class="text-center">Giảm Giá</th>
                                <th class="text-center">Thành Tiền</th>
                                <th class="text-center">Xóa</th>
                            </tr>
                            </thead>
                            <tbody id="productList">
                            <!-- Product items will be added here dynamically -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Payment Options -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">
                        <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <rect x="2" y="5" width="20" height="14" rx="2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M2 10H22" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M7 15H9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M15 15H17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        Thanh Toán
                    </h2>
                </div>
                <div class="card-content">
                    <div class="payment-options">
                        <h3 class="payment-options-title">Hình thức thanh toán:</h3>
                        <div class="radio-group">
                            <div class="radio-option">
                                <input type="radio" id="payment-full" name="paymentStatus" value="full" checked onchange="handlePaymentChange()" class="radio-input">
                                <label for="payment-full" class="radio-label">Trả đủ</label>
                            </div>
                            <div class="radio-option">
                                <input type="radio" id="payment-partial" name="paymentStatus" value="partial" onchange="handlePaymentChange()" class="radio-input">
                                <label for="payment-partial" class="radio-label">Trả một phần</label>
                            </div>
                            <div class="radio-option">
                                <input type="radio" id="payment-none" name="paymentStatus" value="none" onchange="handlePaymentChange()" class="radio-input">
                                <label for="payment-none" class="radio-label">Ghi nợ</label>
                            </div>
                        </div>
                    </div>

                    <div class="partial-payment-grid" id="partialPaymentContainer" style="display: none;">
                        <div>
                            <label for="partialPayment" class="input-label">Số tiền trả:</label>
                            <input type="number" id="partialPayment" name="partialPayment" min="0" value="0" onchange="calculateRemainingAmount()" class="payment-input">
                        </div>
                    </div>

                    <div class="partial-payment-grid" id="remainingAmountContainer" style="display: none;">
                        <div>
                            <label for="remainingAmount" class="input-label">Số tiền còn lại:</label>
                            <input type="text" id="remainingAmount" name="remainingAmount" class="payment-input readonly-input" readonly>
                        </div>
                    </div>

                    <input type="hidden" id="totalCostHidden" name="totalCostHidden">

                    <div class="total-cost-section">
                        <div class="total-cost-row">
                            <span>Tổng chi phí:</span>
                            <span class="total-cost-value">
                                <span id="totalCostDisplay"></span>
                            </span>
                        </div>
                    </div>

                    <div class="action-buttons">
                        <button type="submit" class="submit-button">
                            Tạo Hóa Đơn
                        </button>
                        <button type="button" class="cancel-button" onclick="window.location.href = 'listorders'">
                            Hủy
                        </button>
                        <button type="button" class="new-invoice-button" onclick="openNewInvoiceTab()">
                            <svg class="button-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 5V19M5 12H19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                            Thêm Hóa Đơn Mới
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <script>
        function addProductToOrder(productID, productName, price, availableQuantity, spec, unitPrice) {
            // Kiểm tra nếu sản phẩm hết hàng
            if (availableQuantity <= 0) {
                alert("Sản phẩm đã hết hàng! Không thể thêm vào đơn hàng.");
                return; // Dừng hàm nếu sản phẩm không còn hàng
            }

            var table = document.getElementById("productTable").getElementsByTagName('tbody')[0];
            // Nếu sản phẩm chưa có, thêm dòng mới

            var newRow = table.insertRow(table.rows.length);
            var cell1 = newRow.insertCell(0); // Tên sản phẩm
            var cell2 = newRow.insertCell(1); // Số lượng
            var cell3 = newRow.insertCell(2); // Quy cách (unit type)
            var cell4 = newRow.insertCell(3); // giá
            var cell5 = newRow.insertCell(4); // Giảm giá
            var cell6 = newRow.insertCell(5); // Thành tiền
            var cell7 = newRow.insertCell(6); // Nút xóa

            // Hiển thị tên sản phẩm
            cell1.innerHTML =
                '<input type="hidden" name="productID" value="' + productID + '">' +
                '<input type="hidden" name="productName" value="' + productName + '">' +
                productName;

            // Số lượng
            cell2.className = "text-center";
            cell2.innerHTML = '<input type="number" name="quantity" class="quantity-input" required value="1" min="1" max="' + availableQuantity + '">';

            // Quy cách
            cell3.className = "text-center";
            cell3.innerHTML = '<input type="number" name="spec" class="spec-input" required value="1" min="1" max="' + spec + '">';

            // Giá gốc
            cell4.className = "text-center";
            cell4.innerHTML = '<span class="price">' + price + '</span>' +
                                '<input type="hidden" name="price" value="' + price + '">';

            // Giảm giá
            cell5.className = "text-center";
            cell5.innerHTML = '<input type="number" name="unitPrice" class="discount-input" value="0" min="0">';

            // Thành tiền
            cell6.className = "text-center total-price";
            cell6.innerHTML =
                            '<span class="totalPriceDisplay"></span>' +
                            '<input type="hidden" name="totalPriceHidden" class="totalPriceHidden">';

            // Nút xóa
            cell7.className = "text-center";
            cell7.innerHTML = '<button type="button" onclick="deleteRow(this)" class="delete-button">' +
                '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">' +
                '<path d="M3 6H5H21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>' +
                '<path d="M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6H19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>' +
                '</svg>' +
                '</button>';

            var quantityInput = cell2.querySelector('.quantity-input');
            // Không cho nhập số âm
            quantityInput.addEventListener("input", function () {
                if (this.value < 0) {
                    this.value = 1; // Đặt giá trị tối thiểu là 1
                }
                recalculateRow(newRow, price, availableQuantity);
            });

            var specInput = cell3.querySelector('.spec-input');
            specInput.addEventListener("input", function () {
                if (this.value < 0) {
                    this.value = 1; // Đặt giá trị tối thiểu là 1
                }
                recalculateRow(newRow, price, availableQuantity);
            });

            var discountInput = cell5.querySelector('.discount-input');
            // Không cho nhập số âm
            discountInput.addEventListener("input", function () {
                if (this.value < 0) {
                    this.value = 0; // Đặt giá trị tối thiểu là 0
                }
                recalculateRow(newRow, price, availableQuantity);
            });

            recalculateRow(newRow, price, availableQuantity);

            // Gọi hàm reset sau khi thêm sản phẩm
            afterAddProduct();
        }

        function formatNumberVND(number) {
            return number.toLocaleString('vi-VN');
        }

        function recalculateRow(row, price, availableQuantity) {
            var quantityInput = row.querySelector(".quantity-input");
            var specInput = row.querySelector(".spec-input");
            var discountInput = row.querySelector(".discount-input");
            var totalPriceInput = row.querySelector(".totalPrice");
            var totalPriceHidden = row.querySelector(".totalPriceHidden");

            var unitMultiplier = parseInt(specInput.value);
            var quantity = parseInt(quantityInput.value);
            var discount = parseInt(discountInput.value) || 0;

            // Validate quantity against available stock
            if (quantity*unitMultiplier > availableQuantity) {
                alert("Số lượng vượt quá số lượng tồn kho.");
                quantityInput.value = availableQuantity; // Reset to max available
                quantity = availableQuantity; // Update the quantity variable
            }

            // Validate discount against price
            if (discount > price) {
                alert("Giảm giá không vượt quá giá sản phẩm.");
                discountInput.value = price;
                discount = price;
            }

            var totalPrice = (price-discount) * quantity * unitMultiplier;
            if (totalPrice < 0) totalPrice = 0;

            totalPriceHidden.value = totalPrice;
            row.querySelector(".totalPriceDisplay").innerText = formatNumberVND(totalPrice); // Hiển thị formatted number
            updateTotalOrderPrice();
        }

        function deleteRow(button) {
            var row = button.parentNode.parentNode;
            row.parentNode.removeChild(row);
            updateTotalOrderPrice();
        }

        function updateTotalOrderPrice() {
            var total = 0;
            var totalPriceInputs = document.querySelectorAll(".totalPriceHidden");

            totalPriceInputs.forEach(input => {
                total += parseFloat(input.value) || 0;
            });

            //document.getElementById("totalCost").value = formatNumberVND(total); // Display formatted number
            document.getElementById("totalCostDisplay").innerText = formatNumberVND(total);
            document.getElementById("totalCostHidden").value = total; // Store the actual value
            // Update remaining amount if partial payment is selected
            if (document.querySelector('input[name="paymentStatus"]:checked').value === 'partial') {
                calculateRemainingAmount();
            }
        }

        // Hiển thị/ẩn các ô khi chọn hình thức thanh toán
        function handlePaymentChange() {
            const paymentStatus = document.querySelector('input[name="paymentStatus"]:checked').value;
            const partialPaymentContainer = document.getElementById('partialPaymentContainer');
            const remainingAmountContainer = document.getElementById('remainingAmountContainer');
            if (paymentStatus === 'partial') {
                partialPaymentContainer.style.display = 'grid';
                remainingAmountContainer.style.display = 'grid';
                calculateRemainingAmount(); // Tính toán lại số tiền còn lại khi hiện form
            } else {
                partialPaymentContainer.style.display = 'none';
                remainingAmountContainer.style.display = 'none';
                document.getElementById('partialPayment').value = 0;
                document.getElementById('remainingAmount').value = '';
            }
        }

        // Tính số tiền còn lại khi nhập số tiền trả
        function calculateRemainingAmount() {
            const totalCostStr = document.getElementById('totalCostHidden').value;
            const totalCost = parseFloat(totalCostStr) || 0;
            const partialPayment = parseFloat(document.getElementById('partialPayment').value) || 0;
            let remainingAmount = totalCost - partialPayment;
            remainingAmount = remainingAmount < 0 ? 0 : remainingAmount;
            document.getElementById('remainingAmount').value = formatNumberVND(remainingAmount);
        }

       
        // Khởi tạo khi load trang
        $(document).ready(function () {
            // Sự kiện khi nhập số tiền trả
            $('#partialPayment').on('input', calculateRemainingAmount);
            // Khởi tạo ẩn/hiện các phần
            handlePaymentChange();
        });
    </script>
</div>
</body>
</html>