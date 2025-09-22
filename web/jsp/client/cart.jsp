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
        <title>nestmart - Cart</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet" />
        <link rel="shortcut icon" type="image/x-icon" href="assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="assets/client/css/style.css" />
        <link rel="stylesheet" href="assets/client/css/main-color04.css" />

        <style>
            .sold-out .remove-btn {
                pointer-events: auto;
                opacity: 1;
            }
            .sold-out {
                position: relative;
            }
            .sold-out .product-info,
            .sold-out .cart-image,
            .sold-out .product-name,
            .sold-out .unit-price,
            .sold-out .quantity-cell,
            .sold-out .item-total {
                opacity: 0.5;
            }
            .sold-out .quantity-controls button,
            .sold-out .quantity-controls input {
                pointer-events: none;
            }
            .sold-out::after {
                content: "SOLD OUT";
                color: red;
                font-weight: bold;
                font-size: 18px;
                position: absolute;
                top: 40%;
                left: 40%;
                background: rgba(255,255,255,0.8);
                padding: 5px 10px;
                border-radius: 5px;
            }
            .quantity-controls {
                display: flex;
                align-items: center;
                gap: 8px;
                justify-content: center;
            }
            .quantity-btn {
                width: 35px;
                height: 35px;
                border: 1px solid #ddd;
                background: #f8f9fa;
                border-radius: 6px;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                transition: all 0.2s;
                font-size: 18px;
                color: #666;
            }
            .quantity-btn:hover:not(:disabled) {
                background: #e9ecef;
                border-color: #adb5bd;
            }
            .quantity-btn:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }
            .quantity-input {
                width: 70px;
                text-align: center;
                border: 1px solid #ddd;
                border-radius: 6px;
                padding: 8px;
            }
            .stock-warning {
                font-size: 12px;
                color: #ff6b35;
                font-weight: bold;
                margin-top: 4px;
            }
            .fa { font-family: FontAwesome !important; }

            @media (max-width: 768px) {
                table thead {
                    display: none;
                }
                table tbody tr {
                    display: block;
                    margin-bottom: 15px;
                    border: 1px solid #ddd;
                    border-radius: 10px;
                    padding: 10px;
                    background: #fff;
                }
                table tbody td {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding: 8px;
                    border: none !important;
                }
                table tbody td:before {
                    content: attr(data-label);
                    font-weight: bold;
                    color: #333;
                }
                .quantity-controls {
                    justify-content: flex-end;
                }
                .item-total {
                    font-weight: bold;
                    color: #222;
                }
                tfoot tr {
                    display: block;
                    text-align: right;
                }
                tfoot td {
                    display: block;
                    border: none !important;
                }
                .btn-lg {
                    width: 100%;
                    margin-top: 10px;
                }
            }
        </style>
    </head>

    <body class="nestmart-body">
         <div id="biof-loading">
            <div class="biof-loading-center">
                <div class="biof-loading-center-absolute">
                    <div class="dot dot-one"></div>
                    <div class="dot dot-two"></div>
                    <div class="dot dot-three"></div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/client/header.jsp" />
        <div class="page-contain">
            <div id="main-content" class="main-content">
                <div class="product-tab z-index-20 sm-margin-top-59px xs-margin-top-20px">
                    <div class="container">
                        <div class="cart-shopping">
                            <h1>Shopping Cart</h1>

                            <form id="cart-form" action="${pageContext.request.contextPath}/client/updateCart.htm" method="post">
                                <c:choose>
                                    <c:when test="${not empty cart}">
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>Product Image</th>
                                                    <th>Product Name</th>
                                                    <th>Unit Price</th>
                                                    <th>Quantity</th>
                                                    <th>Total Price</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody id="cart-items">
                                                <c:forEach var="entry" items="${cart}">
                                                    <c:set var="item" value="${entry.value}" />
                                                    <tr class="cart-row ${item.outOfStock ? 'sold-out' : ''}" 
                                                        data-product-id="${item.productId}"
                                                        data-available-stock="${item.availableQuantity}">
                                                        
                                                        <td data-label="Product Image">
                                                            <img src="../assets/admin/images/uploads/products/${item.productImage}"
                                                                 alt="${item.productName}"
                                                                 class="cart-image" style="width: 80px;">
                                                        </td>
                                                        <td data-label="Product Name" class="product-name">${item.productName}</td>
                                                        <td data-label="Unit Price" class="unit-price" data-price="${item.productPrice}">
                                                            <fmt:formatNumber value="${item.productPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                        </td>
                                                        <td data-label="Quantity" class="quantity-cell">
                                                            <div class="quantity-controls">
                                                                <button class="quantity-btn minus-btn" type="button" 
                                                                        onclick="changeQuantity('${item.productId}', -1)">−</button>
                                                                <input type="number" 
                                                                       name="quantities" 
                                                                       class="form-control quantity-input" 
                                                                       value="${item.quantity}" 
                                                                       min="1" 
                                                                       max="${item.availableQuantity}" 
                                                                       id="qty-${item.productId}"
                                                                       readonly />
                                                                <button class="quantity-btn plus-btn" type="button" 
                                                                        onclick="changeQuantity('${item.productId}', 1)">+</button>
                                                                <input type="hidden" name="productIds" value="${item.productId}" />
                                                            </div>
                                                        </td>
                                                        <td data-label="Total" class="item-total" id="total-${item.productId}">
                                                            <fmt:formatNumber value="${item.productPrice * item.quantity}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                        </td>
                                                        <td data-label="Action">
                                                            <button type="submit" 
                                                                    formaction="${pageContext.request.contextPath}/client/removeFromCart.htm?productId=${item.productId}"
                                                                    class="btn btn-danger btn-sm remove-btn">
                                                                Remove
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="4" class="text-end"><strong>Total:</strong></td>
                                                    <td colspan="2" id="cart-total">
                                                        <strong><fmt:formatNumber value="${grandTotal}" type="number" maxFractionDigits="0" groupingUsed="true" />$</strong>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="6" class="text-end">
                                                        <button type="button" 
                                                                class="btn btn-success btn-lg" 
                                                                onclick="proceedToCheckout()">
                                                            Proceed to Checkout
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-info">
                                            Your cart is empty. 
                                            <a href="${pageContext.request.contextPath}/client/product.htm">Shop now</a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/client/footer.jsp" />

        <a class="btn-scroll-top"><i class="nestmart-icon icon-left-arrow"></i></a>


        <script src="../assets/client/js/jquery-3.4.1.min.js"></script>
            <script src="../assets/client/js/bootstrap.min.js"></script>
            <script src="../assets/client/js/jquery.countdown.min.js"></script>
            <script src="../assets/client/js/jquery.nice-select.min.js"></script>
            <script src="../assets/client/js/jquery.nicescroll.min.js"></script>
            <script src="../assets/client/js/slick.min.js"></script>
            <script src="../assets/client/js/nestmart.framework.js"></script>
            <script src="../assets/client/js/functions.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
                                                                    function formatCurrency(amount) {
                                                                        return new Intl.NumberFormat('en-US').format(Math.round(amount)) + '$';
                                                                    }

                                                                    function changeQuantity(productId, change) {
                                                                        console.log('changeQuantity called:', productId, change);

                                                                        const input = document.getElementById('qty-' + productId);
                                                                        if (!input) {
                                                                            console.error('Input not found:', 'qty-' + productId);
                                                                            return;
                                                                        }

                                                                        const currentVal = parseInt(input.value) || 1;
                                                                        const maxVal = parseInt(input.getAttribute('max')) || 999;
                                                                        const minVal = parseInt(input.getAttribute('min')) || 1;

                                                                        let newVal = currentVal + change;
                                                                        console.log('Current:', currentVal, 'Change:', change, 'New:', newVal, 'Max:', maxVal);

                                                                        if (newVal < minVal) {
                                                                            newVal = minVal;
                                                                        } else if (newVal > maxVal) {
                                                                            alert('Only ' + maxVal + ' items available in stock!');
                                                                            return;
                                                                        }

                                                                        input.value = newVal;
                                                                        console.log('Updated input value:', input.value);

                                                                        updateItemTotal(productId);
                                                                    }

                                                                    function updateItemTotal(productId) {
                                                                        console.log('updateItemTotal called:', productId);

                                                                        const row = document.querySelector('tr[data-product-id="' + productId + '"]');
                                                                        if (!row) {
                                                                            console.error('Row not found for productId:', productId);
                                                                            return;
                                                                        }

                                                                        const priceEl = row.querySelector('.unit-price');
                                                                        const quantityEl = document.getElementById('qty-' + productId);

                                                                        if (!priceEl || !quantityEl) {
                                                                            console.error('Price or quantity element not found');
                                                                            return;
                                                                        }

                                                                        const price = parseFloat(priceEl.getAttribute('data-price')) || 0;
                                                                        const quantity = parseInt(quantityEl.value) || 1;

                                                                        console.log('Price:', price, 'Quantity:', quantity);

                                                                        const itemTotal = price * quantity;
                                                                        const itemTotalEl = document.getElementById('total-' + productId);

                                                                        if (itemTotalEl) {
                                                                            itemTotalEl.textContent = formatCurrency(itemTotal);
                                                                            console.log('Updated item total:', itemTotal);
                                                                        }

                                                                        updateCartTotal();
                                                                    }

                                                                    function updateCartTotal() {
                                                                        console.log('updateCartTotal called');

                                                                        let total = 0;

                                                                        const cartRows = document.querySelectorAll('.cart-row:not(.sold-out)');
                                                                        console.log('Found cart rows:', cartRows.length);

                                                                        cartRows.forEach(function (row, index) {
                                                                            const productId = row.getAttribute('data-product-id');
                                                                            const priceEl = row.querySelector('.unit-price');
                                                                            const quantityEl = document.getElementById('qty-' + productId);

                                                                            console.log('Row ' + index + ':', productId);

                                                                            if (priceEl && quantityEl && productId) {
                                                                                const price = parseFloat(priceEl.getAttribute('data-price')) || 0;
                                                                                const quantity = parseInt(quantityEl.value) || 0;
                                                                                const itemTotal = price * quantity;
                                                                                total += itemTotal;

                                                                                console.log('  Price:', price, 'Quantity:', quantity, 'Item Total:', itemTotal);
                                                                            } else {
                                                                                console.log('  Missing elements for product:', productId);
                                                                            }
                                                                        });

                                                                        console.log('Final cart total:', total);

                                                                        const cartTotalEl = document.getElementById('cart-total');
                                                                        if (cartTotalEl) {
                                                                            cartTotalEl.innerHTML = '<strong>' + formatCurrency(total) + '</strong>';
                                                                            console.log('Updated cart total display');
                                                                        } else {
                                                                            console.error('Cart total element not found');
                                                                        }
                                                                    }
                                                                    function proceedToCheckout() {
                                                                        const form = document.createElement('form');
                                                                        form.method = 'POST';
                                                                        form.action = '${pageContext.request.contextPath}/client/checkStockBeforeCheckout.htm';

                                                                        document.querySelectorAll('.cart-row:not(.sold-out)').forEach(row => {
                                                                            const productId = row.dataset.productId;
                                                                            const quantityEl = document.getElementById('qty-' + productId);

                                                                            if (productId && quantityEl) {
                                                                                const idInput = document.createElement('input');
                                                                                idInput.type = 'hidden';
                                                                                idInput.name = 'productIds';
                                                                                idInput.value = productId;
                                                                                form.appendChild(idInput);

                                                                                const qtyInput = document.createElement('input');
                                                                                qtyInput.type = 'hidden';
                                                                                qtyInput.name = 'quantities';
                                                                                qtyInput.value = quantityEl.value;
                                                                                form.appendChild(qtyInput);
                                                                            }
                                                                        });

                                                                        document.body.appendChild(form);
                                                                        form.submit();
                                                                    }

                                                                    document.addEventListener('DOMContentLoaded', function () {
                                                                        console.log('Page loaded, updating cart total...');
                                                                        updateCartTotal();

                                                                        const cartRows = document.querySelectorAll('.cart-row:not(.sold-out)');
                                                                        console.log('Found cart rows:', cartRows.length);

                                                                        cartRows.forEach(row => {
                                                                            const productId = row.dataset.productId;
                                                                            const priceEl = row.querySelector('.unit-price');
                                                                            const quantityEl = document.getElementById('qty-' + productId);
                                                                            console.log('Product:', productId, 'Price:', priceEl?.dataset.price, 'Quantity:', quantityEl?.value);
                                                                        });
                                                                    });
        </script>

        <jsp:include page="livechat.jsp" />
    </body>

</html>