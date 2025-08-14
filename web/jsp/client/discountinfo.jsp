<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Nestmart - Discount</title>
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
        <link rel="stylesheet" href="../assets/client/css/main-color04.css" />
    </head>
    <style>
        .discount-section {
            margin-bottom: 40px;
        }
        .discount-banner {
            display: block;
            margin: 0 auto 20px;
        }

        .discount-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .products-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
        }
        .discount-name {
            text-align: center;
            font-size: 36px;
            font-weight: bold;
            color: #e44d26;
            text-transform: uppercase;
            letter-spacing: 3px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4);
            margin-bottom: 15px;
        }

        .discount-description {
            text-align: center;
            font-size: 20px;
            font-weight: normal;
            color: #555;
            font-style: italic;
            margin-bottom: 20px;

        }

        .product-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
        }
        .discount-dates {
            text-align: center;
            font-size: 18px;
            color: #777;
            font-style: italic;
            margin-bottom: 15px;
        }

        .product-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 4px;
        }
        .product-name {
            font-size: 18px;
            margin: 10px 0;
        }
        .product-price {
            color: #e44d26;
            font-weight: bold;
        }
        .product-original-price {
            text-decoration: line-through;
            color: #999;
            margin-right: 10px;
        }
        .pagination-container {
            display: flex;
            justify-content: center;
            margin: 50px 0;
        }

        .pagination {
            display: flex;
            padding: 0;
            list-style: none;
        }

        .pagination .page-item .page-link {
            color: #282c3c;
            background-color: #fff;
            border: 1px solid #282c3c;
            padding: 8px 16px;
            margin: 0 4px;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .pagination .page-item.active .page-link {
            color: white;
            background-color: #ff9404;
            border-color: #282c3c;
        }

        .pagination .page-item .page-link:hover {
            color: white;
            background-color: #ff9404;
            border-color: #282c3c;
        }

        .pagination .page-item.disabled .page-link {
            color: #6c757d;
            background-color: #fff;
            border-color: #dee2e6;
            cursor: not-allowed;
        }
        .discount-section {
            margin-bottom: 40px;
        }

        .discount-banner {
            width: 100%;
            height: auto;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .products-list {
            display: grid;
            grid-template-columns: repeat(5, 1fr);
            gap: 31px;
            padding: 40px;
        }


        @media (max-width: 1200px) {
            .products-list {
                grid-template-columns: repeat(4, 1fr);
            }
        }

        @media (max-width: 768px) {
            .products-list {
                grid-template-columns: repeat(3, 1fr);
                gap: 15px;
            }
        }

        @media (max-width: 576px) {
            .products-list {
                grid-template-columns: repeat(2, 1fr);
                gap: 10px;
            }
        }

        .product-item {
            list-style: none;
            padding: 8px;
            background-color: #fff;
            border: 1px solid #eee;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .product-item:hover {
            transform: translateY(-6px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
            border-color: #ff9404;
        }

        .product-thumb img {
            transition: transform 0.4s ease;
        }
        .product-item:hover .product-thumb img {
            transform: scale(1.08);
        }


        .sold-out-badge {
            display: inline-block;
            background-color: #ff4d4d;
            color: white;
            font-weight: bold;
            font-size: 20px;
            padding: 8px 15px;
            text-align: center;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
            margin-top: 10px;
        }

        .product-item:hover .product-actions {
            opacity: 1;
            transform: translate(-50%, -50%) scale(1);
        }
        .fa {
            font-family: FontAwesome !important;
        }
        
        @media (max-width: 1200px) {
            .products-list {
                grid-template-columns: repeat(4, 1fr);
            }
        }

        @media (max-width: 992px) {
            .products-list {
                grid-template-columns: repeat(3, 1fr);
                gap: 20px;
            }
        }

        @media (max-width: 768px) {
            .container {
                padding-left: 15px;
                padding-right: 15px;
            }

            .product-details-section table {
                font-size: 14px;
            }

            .product-details-section table th,
            .product-details-section table td {
                padding: 8px;
            }

            /* Quantity controls */
            .qty-input {
                justify-content: center;
            }

            .qty-btn {
                padding: 8px 12px;
                font-size: 16px;
            }

            .quantity-input {
                width: 60px;
                text-align: center;
                font-size: 16px;
            }
        }
        @media (max-width: 480px) {
            .products-list {
                grid-template-columns: 1fr 1fr !important;
                gap: 6px !important;
                padding: 8px !important;
            }

            .product-item {
                padding: 3px !important;
            }

            .product-thumbnail1 {
                height: 90px !important;
            }

            .product-title {
                font-size: 12px !important;
                margin: 8px 0 2px 0 !important;
                line-height: 1.1;
            }

            .original-price1, .original-price2, .discounted-price1 {
                font-size: 10px !important;
            }

            h1 {
                font-size: 24px;
            }

            h2 {
                font-size: 20px;
            }

            h3 {
                font-size: 18px;
            }

            .pagination .page-item .page-link {
                padding: 3px 6px !important;
                font-size: 11px !important;
                min-width: 28px;
            }
        }
        @media (max-width: 576px) {
            .products-list {
                grid-template-columns: repeat(2, 1fr);
                gap: 10px;
                padding: 15px;
            }

            .product-item {
                padding: 6px;
                margin-bottom: 10px;
            }

            .product-thumbnail1 {
                width: 100% !important;
                height: 120px !important;
                object-fit: cover;
                object-position: center;
                display: block;
                margin: 0 auto;
            }

            .product-title {
                font-size: 14px;
                margin: 15px 0 3px 0;
                line-height: 1.2;
            }

            .original-price1, .original-price2, .discounted-price1 {
                font-size: 12px;
            }

            .product-item .badge {
                font-size: 9px;
                padding: 2px 4px;
            }

            .product-item div[style*="display: flex"] {
                flex-direction: row !important;
                align-items: center !important;
                justify-content: center !important;
                padding: 5px !important;
                gap: 15px;
            }
            .product-item {
                list-style: none;
                padding: 8px;
                background-color: #fff;
                border: 1px solid #eee;
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
                margin-bottom: 0 !important;
                padding-bottom: 10px !important;
            }
            .product-item div[style*="width: 1px"] {
                display: none !important;
            }

            .product-item .badge {
                margin: 2px 0 !important;
            }

            .product-details .row {
                margin: 0;
            }

            .product-details .col-md-6 {
                padding: 10px;
            }

            .product-image {
                width: 80%;
                max-width: 250px;
            }

            .product-thumbnails img {
                width: 60px;
                height: 60px;
                margin: 3px;
            }

            .feedback-section h1 {
                font-size: 24px;
                text-align: center;
            }

            .star-group {
                display: flex;
                flex-wrap: wrap;
                justify-content: center;
                gap: 5px;
            }

            .star-group button {
                font-size: 12px;
                padding: 6px 10px;
                margin: 2px;
            }

            .pagination {
                flex-wrap: wrap;
                justify-content: center;
            }

            .pagination .page-item .page-link {
                padding: 6px 12px;
                margin: 2px;
                font-size: 14px;
            }

            .feedback-image {
                max-width: 100px !important;
                max-height: 100px !important;
                margin: 5px !important;
            }
        }
    </style>
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
                        <div class="nestmart-title-box slim-item">
                            <h2 class="main-title">Newest Discount Here!</h2>
                        </div>
                        <div class="nestmart-tab nestmart-tab-contain sm-margin-top-23px">

                            <div class="container">
                                <div class="discount-section">
                                    <h2 class="discount-name">Discount Information</h2>
                                    <h3 class="discount-description">Products on discount:</h3>

                                    <ul class="products-list">
                                        <c:forEach var="product" items="${listProducts}">
                                            <li class="product-item">
                                                <div class="contain-product layout-default">
                                                    <div class="product-thumb">
                                                        <c:choose>
                                                            <c:when test="${not empty product.images}">
                                                                <c:forEach var="image" items="${product.images}" varStatus="status">
                                                                    <c:if test="${status.index == 0}">
                                                                        <a href="${pageContext.request.contextPath}/client/productDetails.htm?productID=${product.productID}" class="link-to-product">
                                                                            <img src="${pageContext.request.contextPath}/assets/client/images/uploads/products/${image.images}" 
                                                                                 alt="${product.productName}" width="250" height="250" class="product-thumbnail"/>
                                                                        </a>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p>No image available</p>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>

                                                    <div class="info">
                                                        <h4 class="product-title">
                                                            <a href="../client/productDetails.htm?productID=${product.productID}" class="pr-name">${product.productName}</a>
                                                        </h4>
                                                        <div class="price">
                                                            <div class="product-price-container">
                                                                <c:if test="${product.quantity > 0}">
                                                                    <c:choose>
                                                                        <c:when test="${product.discount > 0}">
                                                                            <p>
                                                                                <span class="original-price1">
                                                                                    <fmt:formatNumber value="${product.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                                                </span>
                                                                                <span class="discounted-price1">
                                                                                    <fmt:formatNumber value="${product.discount}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                                                </span>
                                                                            </p>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <p class="original-price2">
                                                                                <fmt:formatNumber value="${product.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                                            </p>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <div style="display: flex; align-items: center; justify-content: center;
                                                                         background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
                                                                        <span class="badge bg-success" style="margin-right: 10px;">
                                                                            <c:choose>
                                                                                <c:when test="${product.averageRating > 0}">
                                                                                    <fmt:formatNumber value="${product.averageRating}" type="number" maxFractionDigits="1" />
                                                                                    <img src="../assets/client/images/star-16.png" alt="Feedback Star" style="vertical-align: middle;">
                                                                                </c:when>
                                                                                <c:otherwise>No feedback</c:otherwise>
                                                                            </c:choose>
                                                                        </span>
                                                                        <div style="width: 1px; height: 30px; background-color: #ccc; margin: 0 10px;"></div>
                                                                        <span class="badge bg-light">Sold: ${product.totalQuantitySold}</span>
                                                                    </div>
                                                                </c:if>

                                                                <c:if test="${product.quantity == 0}">
                                                                    <div style="margin-top: 65px">
                                                                        <c:if test="${product.quantity == 0}">
                                                                            <span class="sold-out-badge">SOLD OUT</span>
                                                                        </c:if>



                                                                    </div>

                                                                </c:if>
                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>

                                <div class="pagination-container">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                                <a class="page-link" href="?discountID=${discountID}&page=${currentPage - 1}&pageSize=${pageSize}" tabindex="-1">Previous</a>
                                            </li>

                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="?discountID=${discountID}&page=${i}&pageSize=${pageSize}">${i}</a>
                                                </li>
                                            </c:forEach>

                                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                <a class="page-link" href="?discountID=${discountID}&page=${currentPage + 1}&pageSize=${pageSize}">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
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
            <jsp:include page="livechat.jsp" />
    </body>

</html>
