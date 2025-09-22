<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>nestmart - Purchase</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet" />
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="assets/client/css/style.css" />
        <link rel="stylesheet" href="assets/client/css/main-color04.css" />
        <style>
            .order-container {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 40px;
                padding: 40px 0;
                max-width: 1700px;
                margin: auto;
            }

            .order-left, .order-right {
                background-color: #fff;
                padding: 30px 25px;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }

            .order-left h2, .order-right h2 {
                font-size: 24px;
                margin-bottom: 25px;
                color: #333;
            }

            .form-control, textarea.form-control {
                width: 100%;
                padding: 10px 12px;
                border-radius: 8px;
                border: 1px solid #ddd;
                margin-bottom: 15px;
                font-size: 14px;
            }

            .form-control:focus {
                border-color: #007bff;
                box-shadow: 0 0 5px rgba(0,123,255,0.2);
                outline: none;
            }

            .payment-options label {
                display: flex;
                align-items: center;
                gap: 6px;
                margin-bottom: 6px;
                cursor: pointer;
            }

            .table.cart-table {
                width: 100%;
                border-collapse: collapse;
            }

            .table.cart-table th, .table.cart-table td {
                text-align: center;
                padding: 10px 8px;
                border-bottom: 1px solid #eee;
                vertical-align: middle;
            }

            .table.cart-table th {
                background-color: #f0f0f0;
                font-weight: 600;
            }

            .cart-image {
                max-width: 80px;
                height: auto;
                border-radius: 8px;
            }

            .btnAddcart {
                background-color: #007bff;
                color: #fff;
                font-weight: 500;
                padding: 12px 25px;
                border-radius: 8px;
                border: none;
                cursor: pointer;
                transition: all 0.3s;
            }

            .btnAddcart:hover {
                background-color: #0056b3;
                transform: translateY(-2px);
            }

            .bank-info {
                margin-top: 15px;
                padding: 15px;
                border: 1px solid #ddd;
                border-radius: 8px;
                background-color: #f9f9f9;
            }

            .bank-info img.bank-qr {
                max-width: 150px;
                display: block;
                margin-bottom: 10px;
            }

            .bank-details p {
                margin: 4px 0;
                font-size: 14px;
            }

            .bank-note {
                margin-top: 10px;
                font-size: 17px;
                color: blue;
            }

            .summary-row {
                font-weight: 600;
                border-top: 2px solid #333 !important;
            }

            .summary-row.total-row {
                background-color: #f8f9fa;
                font-weight: bold;
                color: #007bff;
            }

            .fa {
                font-family: FontAwesome !important;
            }

            /* Responsive */
            @media (max-width: 992px) {
                .order-container {
                    grid-template-columns: 1fr;
                }
            }

            @media (max-width: 576px) {
                .table.cart-table thead {
                    display: none;
                }
                .table.cart-table tr {
                    display: block;
                    margin-bottom: 15px;
                    border-bottom: 2px solid #ddd;
                    padding-bottom: 10px;
                }
                .table.cart-table td {
                    display: block;
                    text-align: left;
                    border: none;
                    padding: 6px 0;
                }
                .table.cart-table tbody td::before {
                    content: attr(data-label);
                    font-weight: bold;
                    color: #333;
                    display: inline-block;
                    min-width: 100px;
                }

                .cart-image {
                    max-width: 60px;
                }
            }
            .voucher-section {
                margin: 20px 0;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 8px;
                background-color: #f8f9f9;
            }

            .voucher-section h4 {
                margin-bottom: 15px;
                color: #333;
            }

            .voucher-input-group {
                display: flex;
                gap: 10px;
                margin-bottom: 15px;
            }

            .voucher-input {
                flex: 1;
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }

            .voucher-apply-btn {
                background-color: #28a745;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 4px;
                cursor: pointer;
            }

            .voucher-apply-btn:hover {
                background-color: #218838;
            }

            .voucher-list {
                max-height: 250px;
                overflow-y: auto;
            }

            .voucher-item {
                background-color: white;
                border: 1px solid #ddd;
                border-radius: 6px;
                margin-bottom: 10px;
                padding: 12px;
                cursor: pointer;
                transition: all 0.2s;
            }

            .voucher-item.available {
                border-left: 4px solid #28a745;
            }

            .voucher-item.unavailable {
                border-left: 4px solid #dc3545;
                opacity: 0.6;
            }

            .voucher-item.applied {
                border-left: 4px solid #007bff;
                background-color: #e3f2fd;
            }

            .voucher-item:hover.available {
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                transform: translateY(-1px);
            }

            .voucher-code {
                font-weight: bold;
                color: #007bff;
                font-size: 14px;
            }

            .voucher-desc {
                font-size: 12px;
                color: #666;
                margin: 4px 0;
            }

            .voucher-reason {
                font-size: 11px;
                color: #dc3545;
                font-style: italic;
            }

            .voucher-discount {
                font-size: 12px;
                color: #28a745;
                font-weight: bold;
            }

            .applied-voucher-info {
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 10px;
            }

            .remove-voucher-btn {
                background-color: #dc3545;
                color: white;
                border: none;
                padding: 4px 8px;
                border-radius: 3px;
                font-size: 11px;
                cursor: pointer;
            }

            .discount-row {
                color: #28a745;
                font-weight: 600;
            }
        </style>

    </head>

    <body class="nestmart-body">

        <jsp:include page="/jsp/client/header.jsp" />
        <div class="page-contain">

            <div id="main-content" class="main-content">
                <div class="product-tab z-index-20 sm-margin-top-59px xs-margin-top-20px">
                    <div class="container">
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger">
                                ${errorMessage}
                            </div>
                        </c:if>
                        <h2 class="mb-4"  style="text-align: center">Confirm Order</h2>
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success" role="alert">
                                ${successMessage}
                            </div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/client/createOrder.htm" method="post">
                            <div class="order-container">
                                <div class="order-left">
                                    <h2 style="text-align: center">Customer Information</h2>

                                    <input type="hidden" name="customerID" value="${sessionScope.accountId}">

                                    <div class="form-group">
                                        <label for="name">Name:</label>
                                        <input type="text" class="form-control" id="name" name="name" value="${sessionScope.fullName}" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="phone">Phone:</label>
                                        <input type="text" class="form-control" id="phone" name="phone" value="${sessionScope.phoneNumber}" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="shippingAddress">Shipping Address:</label>
                                        <textarea class="form-control" id="shippingAddress" name="shippingAddress" rows="3" required>${sessionScope.address}</textarea>
                                    </div>

                                    <div class="form-group">
                                        <label for="note">Note:</label>
                                        <textarea class="form-control" id="note" name="note" rows="4"></textarea>
                                    </div>


                                    <div class="form-group">
                                        <label>Payment Method:</label>
                                        <div class="payment-options">
                                            <label>
                                                <input type="radio" name="paymentMethod" value="Cash on Delivery" checked> Cash on Delivery
                                            </label>

                                            <label>
                                                <input type="radio" name="paymentMethod" value="Bank transfer"> Bank transfer
                                            </label>
                                        </div>

                                        <div id="bank-info" class="bank-info" style="display: none;">
                                            <img src="../assets/client/images/qr-code.jpg" alt="QR Code" class="bank-qr">
                                            <div class="bank-details">
                                                <p><strong>Account Name:</strong> TO NGUYEN GIA THOAI</p>
                                                <p><strong>Account Number:</strong> 123456789</p>
                                                <p><strong>Bank:</strong> MB Bank</p>
                                            </div>
                                            <%
                                                String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                                                StringBuilder orderId = new StringBuilder();
                                                orderId.append("ORD");
                                                java.util.Random rnd = new java.util.Random();
                                                for (int i = 0; i < 8; i++) {
                                                    orderId.append(chars.charAt(rnd.nextInt(chars.length())));
                                                }
                                            %>

                                            <p class="bank-note">
                                                Please transfer the payment with the content: 
                                                "<strong>Order ID: <span id="order-id-text"><%= orderId.toString()%></span></strong>".<br>
                                                After completing the transfer, click the "Order" button. 
                                                Our team will contact you shortly to confirm your payment.
                                            </p>
                                        </div>
                                    </div>


                                </div>

                                <div class="order-right">
                                    <h2 style="text-align: center">Order Information</h2>
                                    <div class="voucher-section">
                                        <h4><i class="fa fa-ticket"></i> Apply Voucher</h4>

                                        <div class="voucher-input-group">
                                            <input type="text" class="voucher-input" id="voucherCodeInput" placeholder="Enter voucher code">
                                            <button type="button" class="voucher-apply-btn" onclick="applyVoucherByCode()">Apply</button>
                                        </div>

                                        <div id="appliedVoucherDisplay" style="display: none;">
                                            <div class="applied-voucher-info">
                                                <span id="appliedVoucherText"></span>
                                                <button type="button" class="remove-voucher-btn" onclick="removeAppliedVoucher()">Remove</button>
                                            </div>
                                        </div>

                                        <c:if test="${not empty availableVouchers}">
                                            <div class="voucher-list">
                                                <c:forEach items="${availableVouchers}" var="voucher">
                                                    <div class="voucher-item available" onclick="applyVoucher('${voucher.code}')">
                                                        <div class="voucher-code">${voucher.code}</div>
                                                        <div class="voucher-desc">
                                                            <c:choose>
                                                                <c:when test="${voucher.discountType == 'Percentage'}">
                                                                    Get ${voucher.discountValue}% off
                                                                    <c:if test="${voucher.maxDiscount != null}">
                                                                        (max $<fmt:formatNumber value="${voucher.maxDiscount}" type="number" maxFractionDigits="0" groupingUsed="true" />)
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Get $<fmt:formatNumber value="${voucher.discountValue}" type="number" maxFractionDigits="0" groupingUsed="true" /> off
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <c:if test="${voucher.minOrderValue != null}">
                                                            <div class="voucher-desc">Min order: $<fmt:formatNumber value="${voucher.minOrderValue}" type="number" maxFractionDigits="0" groupingUsed="true" /></div>
                                                        </c:if>
                                                        <div class="voucher-discount">
                                                            Saves: $<fmt:formatNumber value="${voucher.calculateDiscount(grandTotal)}" type="number" maxFractionDigits="2" groupingUsed="true" />
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                                <c:forEach items="${unavailableVouchers}" var="voucher">
                                                    <div class="voucher-item unavailable">
                                                        <div class="voucher-code">${voucher.code}</div>
                                                        <div class="voucher-desc">
                                                            <c:choose>
                                                                <c:when test="${voucher.discountType == 'Percentage'}">
                                                                    Get ${voucher.discountValue}% off
                                                                    <c:if test="${voucher.maxDiscount != null}">
                                                                        (max $<fmt:formatNumber value="${voucher.maxDiscount}" type="number" maxFractionDigits="0" groupingUsed="true" />)
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Get $<fmt:formatNumber value="${voucher.discountValue}" type="number" maxFractionDigits="0" groupingUsed="true" /> off
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <div class="voucher-reason">${voucher.reasonCannotUse}</div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </c:if>


                                    </div>
                                    <p style="font-weight: bold; font-size: 16px; margin-top: 10px;">
                                        Order ID: <span><%= orderId.toString()%></span>
                                    </p>
                                    <input type="hidden" name="orderId" value="<%= orderId%>" />
                                    <c:choose>
                                        <c:when test="${not empty cart}">
                                            <div class="table-responsive">
                                                <table class="table cart-table">
                                                    <thead>
                                                        <tr>
                                                            <th>Image</th>
                                                            <th>Name</th>
                                                            <th>Price</th>
                                                            <th>Quantity</th>
                                                            <th>Total</th>
                                                        </tr>
                                                    </thead>
                                                    <c:forEach items="${cart}" var="entry" varStatus="status">
                                                        <c:set var="item" value="${entry.value}" />
                                                        <input type="hidden" name="productId" value="${item.productId}" />
                                                        <input type="hidden" name="quantity" value="${item.quantity}" />
                                                        <input type="hidden" name="unitPrice" value="${item.productPrice}" />
                                                    </c:forEach>

                                                    <tbody>
                                                        <c:forEach items="${cart}" var="entry">
                                                            <c:set var="item" value="${entry.value}" />
                                                            <tr>
                                                                <td>
                                                                    <img src="../assets/admin/images/uploads/products/${item.productImage}" 
                                                                         alt="${item.productName}" class="cart-image">
                                                                </td>
                                                                <td data-label="Name">${item.productName}</td>
                                                                <td data-label="Price">
                                                                    <fmt:formatNumber value="${item.productPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                                </td>
                                                                <td data-label="Quantity">${item.quantity}</td>
                                                                <td data-label="Total">
                                                                    <fmt:formatNumber value="${item.productPrice * item.quantity}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                    <tfoot>
                                                        <tr>
                                                            <td colspan="4">Subtotal</td>
                                                            <td>
                                                                <c:set var="subtotal" value="0" scope="page"/>
                                                                <c:forEach items="${cart}" var="entry">
                                                                    <c:set var="item" value="${entry.value}" />
                                                                    <c:set var="subtotal" value="${subtotal + (item.productPrice * item.quantity)}" scope="page"/>
                                                                </c:forEach>
                                                                <span id="cart-subtotal"><fmt:formatNumber value="${subtotal}" type="number" maxFractionDigits="0" groupingUsed="true" />$</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="4">Shipping Fee</td>
                                                            <td>
                                                                <span id="shipping-fee">5$</span>
                                                                <input type="hidden" name="shippingFee" value="5" />
                                                            </td>
                                                        </tr>
                                                        <tr id="discountRow" style="display: none;" class="discount-row">
                                                            <td colspan="4">Voucher Discount</td>
                                                            <td>
                                                                -<span id="discount-amount">0$</span>
                                                            </td>
                                                        </tr>
                                                        <tr class="summary-row total-row">
                                                            <td colspan="4"><strong>Total</strong></td>
                                                            <td>
                                                                <c:set var="grandTotal" value="${subtotal + 5}" scope="page"/>
                                                                <span id="cart-total"><strong><fmt:formatNumber value="${grandTotal}" type="number" maxFractionDigits="0" groupingUsed="true" />$</strong></span>
                                                            </td>
                                                        </tr>
                                                    </tfoot>

                                                </table>

                                            </div>
                                            <input type="hidden" name="appliedVoucherCode" id="appliedVoucherCode" value="" />
                                            <input type="hidden" name="discountAmount" id="discountAmountInput" value="0" />
                                            <input type="hidden" name="totalAmount" id="totalAmountInput" value="${grandTotal}" />

                                            <div class="text-right mt-3">
                                                <button type="submit" class="btnAddcart">Order</button>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <p>Your cart is empty.</p>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/client/footer.jsp" />

        <a class="btn-scroll-top"><i class="nestmart-icon icon-left-arrow"></i></a>

        <script src="assets/client/js/jquery-3.4.1.min.js"></script>
        <script src="assets/client/js/bootstrap.min.js"></script>
        <script src="assets/client/js/jquery.countdown.min.js"></script>
        <script src="assets/client/js/jquery.nice-select.min.js"></script>
        <script src="assets/client/js/jquery.nicescroll.min.js"></script>
        <script src="assets/client/js/slick.min.js"></script>
        <script src="assets/client/js/nestmart.framework.js"></script>
        <script src="assets/client/js/functions.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
    <script>
                    document.addEventListener('DOMContentLoaded', function () {
                        function updateCartTotal() {
                            let subtotal = 0;
                            const shippingFee = 5;
                            const rows = document.querySelectorAll('tbody tr');

                            rows.forEach(function (row) {
                                const price = parseFloat(row.querySelector('.unit-price').getAttribute('data-price'));
                                const quantity = parseInt(row.querySelector('input[name="quantity"]').value);
                                const itemTotal = price * quantity;
                                row.querySelector('.item-total').textContent = '$' + itemTotal.toFixed(2);
                                subtotal += itemTotal;
                            });

                            const total = subtotal + shippingFee;

                            document.getElementById('cart-subtotal').textContent = subtotal.toFixed(2);
                            document.getElementById('cart-total').innerHTML = '<strong>$' + total.toFixed(2) + '</strong>';
                            document.getElementById('totalAmountInput').value = total.toFixed(2);
                        }

                        if (document.querySelectorAll('.quantity-input').length > 0) {
                            updateCartTotal();

                            document.querySelectorAll('.quantity-input').forEach(function (input) {
                                input.addEventListener('change', updateCartTotal);
                            });
                        }
                    });

                    const paymentRadios = document.querySelectorAll('input[name="paymentMethod"]');
                    const bankInfo = document.getElementById('bank-info');

                    paymentRadios.forEach(radio => {
                        radio.addEventListener('change', function () {
                            if (this.value === 'Bank transfer') {
                                bankInfo.style.display = 'block';
                            } else {
                                bankInfo.style.display = 'none';
                            }
                        });
                    });

                    let appliedVoucherData = null;

                    function getContextPath() {
                        return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
                    }

                    function applyVoucherByCode() {
                        const voucherInput = document.getElementById('voucherCodeInput');
                        const code = voucherInput.value.trim();

                        if (!code) {
                            alert('Please enter a voucher code');
                            return;
                        }

                        applyVoucherRequest(code);
                    }

                    function applyVoucher(code) {
                        document.querySelectorAll('.voucher-item.applied').forEach(item => {
                            item.classList.remove('applied');
                            if (item.classList.contains('available')) {
                                const voucherCode = item.querySelector('.voucher-code').textContent;
                                item.onclick = function () {
                                    applyVoucher(voucherCode);
                                };
                            }
                        });

                        applyVoucherRequest(code);
                    }

                    function parseXMLResponse(xmlString) {
                        const parser = new DOMParser();
                        const xmlDoc = parser.parseFromString(xmlString, "application/xml");

                        const response = {};
                        response.success = xmlDoc.querySelector('success').textContent === 'true';

                        const message = xmlDoc.querySelector('message');
                        if (message) {
                            response.message = message.textContent;
                        }

                        if (response.success) {
                            const voucherCode = xmlDoc.querySelector('voucherCode');
                            const discountAmount = xmlDoc.querySelector('discountAmount');
                            const newTotal = xmlDoc.querySelector('newTotal');
                            const discountType = xmlDoc.querySelector('discountType');
                            const discountValue = xmlDoc.querySelector('discountValue');

                            if (voucherCode)
                                response.voucherCode = voucherCode.textContent;
                            if (discountAmount)
                                response.discountAmount = discountAmount.textContent;
                            if (newTotal)
                                response.newTotal = newTotal.textContent;
                            if (discountType)
                                response.discountType = discountType.textContent;
                            if (discountValue)
                                response.discountValue = discountValue.textContent;
                        }

                        return response;
                    }

                    function applyVoucherRequest(code) {
                        const applyBtn = document.querySelector('.voucher-apply-btn');
                        const originalText = applyBtn.textContent;
                        applyBtn.textContent = 'Applying...';
                        applyBtn.disabled = true;

                        fetch(getContextPath() + '/client/applyVoucher.htm', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: 'voucherCode=' + encodeURIComponent(code)
                        })
                                .then(response => response.text()) // Changed from response.json() to response.text()
                                .then(xmlString => {
                                    const data = parseXMLResponse(xmlString); // Parse XML response

                                    if (data.success) {
                                        appliedVoucherData = data;
                                        showAppliedVoucher(data);
                                        updateOrderTotal(data.discountAmount, data.newTotal);

                                        document.getElementById('voucherCodeInput').value = '';

                                        const voucherItems = document.querySelectorAll('.voucher-item.available');
                                        voucherItems.forEach(item => {
                                            const voucherCode = item.querySelector('.voucher-code').textContent;
                                            if (voucherCode === code) {
                                                item.classList.add('applied');
                                                item.onclick = null; // Remove click handler
                                            }
                                        });

                                    } else {
                                        alert(data.message || 'Failed to apply voucher');
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    alert('Error applying voucher. Please try again.');
                                })
                                .finally(() => {
                                    applyBtn.textContent = originalText;
                                    applyBtn.disabled = false;
                                });
                    }

                    function showAppliedVoucher(data) {
                        const appliedDisplay = document.getElementById('appliedVoucherDisplay');
                        const appliedText = document.getElementById('appliedVoucherText');

                        let discountText = '';
                        if (data.discountType === 'Percentage') {
                            discountText = data.discountValue + '% off';
                        } else {
                            discountText = '$' + parseFloat(data.discountValue).toFixed(0) + ' off';
                        }

                        appliedText.innerHTML =
                                '<strong>' + data.voucherCode + '</strong> - ' + discountText +
                                ' (Saved: $' + parseFloat(data.discountAmount).toFixed(2) + ')';

                        appliedDisplay.style.display = 'block';

                        document.getElementById('appliedVoucherCode').value = data.voucherCode;
                        document.getElementById('discountAmountInput').value = data.discountAmount;
                    }

                    function removeAppliedVoucher() {
                        fetch(getContextPath() + '/client/removeVoucher.htm', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            }
                        })
                                .then(response => response.text())
                                .then(xmlString => {
                                    const data = parseXMLResponse(xmlString);

                                    if (data.success) {
                                        document.getElementById('appliedVoucherDisplay').style.display = 'none';

                                        const originalTotal = xmlString.match(/<originalTotal>(.*?)<\/originalTotal>/)?.[1];
                                        if (originalTotal) {
                                            resetOrderTotal(originalTotal);
                                        }

                                        appliedVoucherData = null;

                                        document.getElementById('appliedVoucherCode').value = '';
                                        document.getElementById('discountAmountInput').value = '0';

                                        document.querySelectorAll('.voucher-item.applied').forEach(item => {
                                            item.classList.remove('applied');
                                            if (item.classList.contains('available')) {
                                                const code = item.querySelector('.voucher-code').textContent;
                                                item.onclick = function () {
                                                    applyVoucher(code);
                                                };
                                            }
                                        });

                                        document.getElementById('discountRow').style.display = 'none';

                                    } else {
                                        alert(data.message || 'Failed to remove voucher');
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    alert('Error removing voucher. Please try again.');
                                });
                    }

                    function updateOrderTotal(discountAmount, newTotal) {
                        const discountRow = document.getElementById('discountRow');
                        const discountAmountElement = document.getElementById('discount-amount');
                        const cartTotalElement = document.getElementById('cart-total');
                        const totalAmountInput = document.getElementById('totalAmountInput');

                        if (discountRow) {
                            discountRow.style.display = 'table-row';
                            if (discountAmountElement) {
                                discountAmountElement.textContent = parseFloat(discountAmount).toFixed(2) + '$';
                            }
                        }

                        if (cartTotalElement) {
                            cartTotalElement.innerHTML = '<strong>' + parseFloat(newTotal).toFixed(0) + '$</strong>';
                        }

                        if (totalAmountInput) {
                            totalAmountInput.value = parseFloat(newTotal).toFixed(2);
                        }
                    }

                    function resetOrderTotal(originalTotal) {
                        const cartTotalElement = document.getElementById('cart-total');
                        const totalAmountInput = document.getElementById('totalAmountInput');

                        if (cartTotalElement) {
                            cartTotalElement.innerHTML = '<strong>' + parseFloat(originalTotal).toFixed(0) + '$</strong>';
                        }

                        if (totalAmountInput) {
                            totalAmountInput.value = parseFloat(originalTotal).toFixed(2);
                        }
                    }

                    function formatCurrency(amount) {
                        return parseFloat(amount).toLocaleString('en-US', {
                            minimumFractionDigits: 0,
                            maximumFractionDigits: 2
                        });
                    }
    </script>
    <jsp:include page="livechat.jsp" />
</html>