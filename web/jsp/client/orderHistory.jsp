<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>nestmart - Order History</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet" />
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="../assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="../assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="../assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="../assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="../assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="../assets/client/css/style.css" />
        <link rel="stylesheet" href="../assets/client/css/main-color.css" />
        <style>
            .status-item {
                display: flex;
                flex-direction: column;
                align-items: center;
                padding: 5px;
                border-radius: 8px;
                text-align: center;
                transition: background-color 0.3s ease;
                width: 100px;
                margin: 0 5px;
            }

            .status-item .icon {
                font-size: 20px;
                color: #555;
                margin-bottom: 3px;
            }

            .status-item:hover {
                background-color: #f8f8f8;
            }
            .product-item.active a {
                border: 2px solid #ff9900;
                border-radius: 10px;
                background-color: #f0fff4;
                font-weight: bold;
                color: #28a745 !important;
                box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.15);
            }

            .status-item .pr-name {
                color: #333;
                font-weight: bold;
                text-decoration: none;
            }

            .status-item .pr-name:hover {
                color: #ff9900;
            }

            .status-item .icon,
            .status-item .pr-name {
                display: block;
            }

            .order-card {
                border: 1px solid #e8e8e8;
                margin-bottom: 50px;
                background-color: #fff;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);
                overflow: hidden;
                max-width: 1000px;
                margin: 30px auto;
                padding: 20px;
                width: 80%;
                animation: fadeInUp 0.4s ease-out;
            }

            @keyframes fadeInUp {
                from {
                    opacity: 0;
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .order-header {
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
                padding: 16px 24px;
                border-bottom: 1px solid #e8e8e8;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .order-id {
                font-weight: 600;
                color: #495057;
                font-size: 14px;
            }

            .order-date {
                font-size: 14px;
                color: #6c757d;
            }

            .order-status {
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .order-status-line {
                text-align: right;
                font-size: 14px;
                font-weight: bold;
                color: #28a745;
                margin-bottom: 15px;
            }

            .status-completed {
                background-color: #d4edda;
                color: #155724;
            }

            .status-pending {
                background-color: #fff3cd;
                color: #856404;
            }

            .status-confirmed {
                background-color: #cce5ff;
                color: #0056b3;
            }

            .status-delivering {
                background-color: #e7f3ff;
                color: #0066cc;
            }

            .status-cancelled {
                background-color: #f8d7da;
                color: #721c24;
            }

            .order-products {
                padding: 0;
            }

            .order-product {
                display: flex;
                align-items: center;
                padding: 18px 0;
                border-bottom: 1px solid #f0f0f0;
                transition: background-color 0.2s ease;
                position: relative;
            }

            .order-product:hover {
                background-color: #f8f9fa;
            }

            .order-product:last-child {
                border-bottom: none;
            }

            /* ===== PRODUCT ELEMENTS ===== */
            .product-thumb {
                flex-shrink: 0;
                margin-right: 16px;
            }

            .product-thumb img {
                width: 120px;
                height: 120px;
                object-fit: cover;
                border-radius: 8px;
                border: 1px solid #e8e8e8;
            }

            .product-info {
                flex: 1;
                min-width: 0;
                margin-left: 20px;
            }

            .product-name {
                font-size: 25px;
                font-weight: 600;
                margin: 6px 0;
                line-height: 1.4;
            }

            .product-name a {
                color: #343a40;
                text-decoration: none;
                transition: color 0.2s ease;
            }

            .product-name a:hover {
                color: #007bff;
            }

            .product-meta {
                display: flex;
                gap: 16px;
                align-items: center;
                font-size: 14px;
                color: #6c757d;
            }

            .product-quantity {
                background: #f8f9fa;
                padding: 2px 8px;
                border-radius: 4px;
                font-weight: 500;
                font-size: 15px;
                margin: 3px 0;
            }

            .product-price1 {
                color: black;
                font-weight: bold;
                font-size: 17px;
                margin: 3px 0;
            }

            .product-actions {
                position: absolute;
                right: 0;
                top: 50%;
                transform: translateY(-50%);
                display: flex;
                flex-direction: row;
                gap: 8px;
                align-items: center;
            }

            .order-footer {
                background: #f8f9fa;
                padding: 20px 24px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-top: 1px solid #e8e8e8;
            }

            .order-total {
                font-size: 25px;
                font-weight: 700;
                color: #dc3545;
            }
            .order-total1 {
                font-size: 25px;
                font-weight: 700;
                color: #black;
            }


            .order-actions {
                display: flex;
                gap: 12px;
            }


            .product-actions a,
            .order-actions a {
                display: inline-block;
                min-width: 100px;
                padding: 8px 12px;
                text-align: center;
                border-radius: 6px;
                font-size: 13px;
                font-weight: 500;
                text-decoration: none;
                transition: all 0.3s ease;
                white-space: nowrap;
            }

            .orange-outline-btn {
                border: 1px solid #ff9900;
                color: #ff9900;
                background: #fff;
            }
            .orange-outline-btn:hover {
                background-color: #ff9900;
                color: #fff;
            }

            .add-to-cart-btn {
                background-color: #ff4d4f;
                color: #fff;
                border: none;
            }


            .red-outline-btn {
                border: 2px solid #e74c3c;
                color: #e74c3c;
            }
            .red-outline-btn:hover {
                background-color: #e74c3c;
                color: #fff;
            }


            @media (max-width: 768px) {
                .order-card {
                    margin: 16px;
                    border-radius: 8px;
                    width: auto;
                }

                .order-header {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 8px;
                    padding: 16px;
                }

                .order-product {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 12px;
                    padding: 16px;
                }

                .product-thumb {
                    align-self: center;
                    margin-right: 0;
                }

                .product-info {
                    margin-left: 0;
                }

                .product-actions {
                    flex-direction: column;
                    justify-content: center;
                    width: 100%;
                    margin-left: 0;
                    position: static;
                    transform: none;
                }

                .product-actions a {
                    min-width: 120px;
                    padding: 10px 0;
                    font-size: 14px;
                }


                .order-footer {
                    display: flex;
                    justify-content: space-between;
                    padding-top: 10px;
                    font-size: 26px;
                    font-weight: bold;
                    color: #d0011b;
                    flex-direction: row;
                    align-items: center;
                }

                .order-actions {
                    width: 100%;
                    justify-content: center;
                }
            }
            .search-container {
                max-width: 600px;
                margin: 20px auto;
                padding: 0 20px;
            }

            .search-form {
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .search-input-group {
                position: relative;
                display: flex;
                width: 100%;
                max-width: 500px;
                background: #fff;
                border: 2px solid #e0e0e0;
                border-radius: 25px;
                overflow: hidden;
                transition: border-color 0.3s ease;
            }

            .search-input-group:focus-within {
                border-color: #ff9900;
                box-shadow: 0 0 0 3px rgba(255, 153, 0, 0.1);
            }

            .search-input {
                flex: 1;
                padding: 12px 20px;
                border: none;
                outline: none;
                font-size: 16px;
                background: transparent;
            }

            .search-input::placeholder {
                color: #999;
            }

            .search-btn {
                padding: 12px 20px;
                background: linear-gradient(135deg, #ff9900, #ff7700);
                border: none;
                color: white;
                cursor: pointer;
                transition: background 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                min-width: 50px;
            }

            .search-btn:hover {
                background: linear-gradient(135deg, #ff7700, #ff5500);
            }

            .search-btn i {
                font-size: 16px;
            }

            @media (max-width: 768px) {
                .search-container {
                    margin: 15px auto;
                    padding: 0 15px;
                }

                .search-input {
                    padding: 10px 15px;
                    font-size: 14px;
                }

                .search-btn {
                    padding: 10px 15px;
                    min-width: 45px;
                }
            }

            @media (max-width: 480px) {
                .search-input {
                    padding: 8px 12px;
                    font-size: 14px;
                }

                .search-btn {
                    padding: 8px 12px;
                    min-width: 40px;
                }

                .search-input::placeholder {
                    font-size: 14px;
                }
            }
            .no-orders-message {
                margin: 20px;
                padding: 15px;
                background-color: #f8f8f8;
                border: 1px solid #ddd;
                color: #555;
                text-align: center;
            }
            .order-status-line-red {
                text-align: right;
                font-size: 14px;
                font-weight: bold;
                color: red;
                margin-bottom: 15px;
            }
            .fa {
                font-family: FontAwesome !important;
            }
        </style>
    </head>
    <body class="nestmart-body">
        <jsp:include page="/jsp/client/header.jsp" />
        <div class="page-contain">
            <div id="main-content" class="main-content">
                <div class="hero-section hero-background">
                    <h1 class="page-title">My Purchase</h1>
                </div>
                <div class="container">
                    <nav class="nestmart-nav">
                        <div class="search-container">
                            <form action="../client/orderHistory.htm" method="GET" class="search-form">
                                <div class="search-input-group">
                                    <input type="text" 
                                           name="search" 
                                           placeholder="Search by Order ID or Product Name..." 
                                           value="${param.search}"
                                           class="search-input">
                                    <input type="hidden" name="status" value="${param.status}">
                                    <button type="submit" class="search-btn">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </nav>

                </div>

                <div class="page-contain category-page no-sidebar">
                    <div class="container">
                        <div class="row">

                            <div id="main-content" class="main-content col-lg-12 col-md-12 col-sm-12 col-xs-12">

                                <div class="block-item md-margin-bottom-30">
                                    <ul class="products-list nestmart-carousel nav-center-02 nav-none-on-mobile"
                                        data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":0,"slidesToShow":10,
                                        "responsive":[{"breakpoint":1200,"settings":{"slidesToShow":3}},{"breakpoint":992,"settings":{"slidesToShow":3,"slidesMargin":10}},{"breakpoint":768,"settings":{"slidesToShow":2,"slidesMargin":10}}]}'>

                                        <li class="product-item ${empty currentStatus ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-list-alt"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">All</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Pending' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Pending" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-clock-o"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Pending</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Confirmed' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Confirmed" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-truck"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Confirmed</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'On Delivering' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=On%20Delivering" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-car"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">On Delivering</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Completed' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Completed" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-check-circle"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Completed</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Cancelled' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Cancelled" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-times-circle"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Cancelled</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Return Requested' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Return%20Requested" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-undo"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Returned</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Approved' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Approved" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-check-circle"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Return Request Approved</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Denied' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Denied" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-times-circle"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Return Request Denied</h4>
                                                </div>
                                            </a>
                                        </li>

                                        <li class="product-item ${currentStatus eq 'Return Completed' ? 'active' : ''}">
                                            <a href="../client/orderHistory.htm?status=Return%20Completed" class="contain-product status-item">
                                                <div class="icon">
                                                    <i class="fa fa-check-circle"></i>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title pr-name">Return Completed</h4>
                                                </div>
                                            </a>
                                        </li>

                                    </ul>
                                </div>
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success" role="alert">
                                        ${successMessage}
                                    </div>
                                </c:if>
                                <c:if test="${not empty cancelMessage}">
                                    <div class="alert alert-success" role="alert">
                                        ${cancelMessage}
                                    </div>
                                </c:if>
                                <c:if test="${noOrdersFound}">
                                    <div class="no-orders-message">
                                        <p>No orders found.</p>
                                    </div>
                                </c:if>

                                <c:if test="${not empty ordersList}">
                                </c:if>

                                <div class="product-category list-style">
                                    <div class="products-list">
                                        <div class="row">

                                            <c:forEach var="order" items="${ordersList}">
                                                <div class="order-card">
                                                    <div class="order-status-line">
                                                        <c:choose>
                                                            <c:when test="${order.orderStatus eq 'Pending'}">Waiting for seller confirmation</c:when>
                                                            <c:when test="${order.orderStatus eq 'Confirmed'}">Order confirmed by seller</c:when>
                                                            <c:when test="${order.orderStatus eq 'On Delivering'}">Your package is on the way</c:when>
                                                            <c:when test="${order.orderStatus eq 'Completed'}">Order has been delivered successfully</c:when>
                                                            <c:when test="${order.orderStatus eq 'Return Requested'}">Return request submitted</c:when>
                                                            <c:when test="${order.orderStatus eq 'Approved'}">Return request approved</c:when>
                                                            <c:when test="${order.orderStatus eq 'Return Completed'}">Your return has been completed</c:when>
                                                        </c:choose>
                                                    </div>
                                                    <div class="order-status-line-red">
                                                        <c:choose>
                                                            <c:when test="${order.orderStatus eq 'Cancelled'}">Order has been cancelled</c:when>
                                                            <c:when test="${order.orderStatus eq 'Denied'}">Return request denied</c:when>
                                                        </c:choose>
                                                    </div>

                                                    <c:forEach var="orderDetail" items="${order.orderDetails}">
                                                        <div class="order-product">
                                                            <div class="product-thumb">
                                                                <c:choose>
                                                                    <c:when test="${not empty orderDetail.images}">
                                                                        <img src="../assets/client/images/uploads/products/${orderDetail.images[0].images}" 
                                                                             alt="${orderDetail.product.productName}" />
                                                                    </c:when>
                                                                    <c:otherwise>No image</c:otherwise>
                                                                </c:choose>
                                                            </div>

                                                            <div class="product-info">
                                                                <h4 class="product-name">
                                                                    <a href="../client/productDetails.htm?productID=${orderDetail.product.productID}">
                                                                        ${orderDetail.product.productName}
                                                                    </a>
                                                                </h4>
                                                                <p class="product-quantity">x${orderDetail.quantity}</p>
                                                                <p class="product-price1">
                                                                    <fmt:formatNumber value="${orderDetail.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                                </p>
                                                            </div>

                                                            <div class="product-actions">
                                                                <c:if test="${order.orderStatus eq 'Completed'}">
                                                                    <c:if test="${orderDetail.hasFeedback == false}">
                                                                        <a href="../client/productDetails.htm?productID=${orderDetail.product.productID}" 
                                                                           class="orange-outline-btn">Reorder</a>
                                                                        <a href="../client/feedback.htm?productID=${orderDetail.product.productID}" 
                                                                           class="btn add-to-cart-btn"> Rating</a>
                                                                    </c:if>

                                                                    <c:if test="${orderDetail.hasFeedback == true}">
                                                                        <a href="../client/productDetails.htm?productID=${orderDetail.product.productID}" 
                                                                           class="orange-outline-btn">Reorder</a>
                                                                        <a href="../client/viewFeedback.htm?productID=${orderDetail.product.productID}" 
                                                                           class="orange-outline-btn">View Rating</a>
                                                                    </c:if>
                                                                </c:if>


                                                            </div>

                                                        </div>
                                                    </c:forEach>

                                                    <div class="order-footer">
                                                        <span class="order-total1">Total: <span class="order-total"><fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />$</span></span>

                                                        <div class="order-actions">
                                                            <a href="../client/orderinfo.htm?order=${order.orderID}" 
                                                               class="red-outline-btn">Order info</a>
                                                        </div>
                                                    </div>
                                                </div>


                                            </c:forEach>

                                        </div>
                                    </div>

                                </div>

                            </div>

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
            function confirmCancelOrder(orderId) {
                Swal.fire({
                    title: 'Cancel Order?',
                    text: "Are you sure you want to cancel this order?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Yes',
                    cancelButtonText: 'No'
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById("cancelOrderID").value = orderId;
                        document.getElementById("cancelOrderForm").submit();
                    }
                });
            }
            $(document).ready(function () {
                $('.nestmart-carousel').slick();
            });
        </script>
        <jsp:include page="livechat.jsp" />
    </body>

</html>