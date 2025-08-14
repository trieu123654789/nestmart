<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <link rel="shortcut icon" type="image/x-icon" href="assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="assets/client/css/style.css" />
        <link rel="stylesheet" href="assets/client/css/main-color04.css" />
    </head>
    <body class="nestmart-body">

        <jsp:include page="/jsp/client/header.jsp" />
        <div class="page-contain">
            <div id="main-content" class="main-content">
                <div class="product-tab z-index-20 sm-margin-top-59px xs-margin-top-20px">
                    <div class="container">
                        <c:choose>
                            <c:when test="${not empty orders}">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Name</th>
                                            <th>Phone</th>
                                            <th>Shipping Address</th>
                                            <th>Payment Method</th>
                                            <th>Notes</th>
                                            <th>Total Amount</th>
                                            <th>Order Date</th>
                                            <th>Order Status</th>
                                            <th>Shipper ID</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td>${order.orderID}</td>
                                                <td>${order.name}</td>
                                                <td>${order.phone}</td>
                                                <td>${order.shippingAddress}</td>
                                                <td>${order.paymentMethod}</td>
                                                <td>${order.notes}</td>
                                                <td>${order.totalAmount}</td>
                                                <td>${order.orderDate}</td>
                                                <td>${order.orderStatus}</td>
                                                <td>${order.shipperID}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <p>You have no orders.</p>
                            </c:otherwise>
                        </c:choose>

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
                let total = 0;
                const rows = document.querySelectorAll('tbody tr');
                rows.forEach(function (row) {
                    const price = parseFloat(row.querySelector('.unit-price').getAttribute('data-price'));
                    const quantityElement = row.querySelector('.quantity-input');
                    const quantity = quantityElement ? parseInt(quantityElement.value) : parseInt(row.querySelector('input[name="quantity"]').value);
                    const itemTotal = price * quantity;
                    row.querySelector('.item-total').textContent = '£' + itemTotal.toFixed(2);
                    total += itemTotal;
                });

                document.getElementById('cart-total').textContent = total.toFixed(2);

                document.getElementById('totalAmountInput').value = total.toFixed(2);
            }

            updateCartTotal();

            document.querySelectorAll('.quantity-input').forEach(function (input) {
                input.addEventListener('change', updateCartTotal);
            });
        });


    </script>

    <jsp:include page="livechat.jsp" />
</html>


