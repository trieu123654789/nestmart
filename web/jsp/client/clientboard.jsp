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
        <title>Nestmart - Home</title>
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
        h1, h2, h3, h4, h5, h6 {
            font-family: 'Roboto', sans-serif;
            color: #333;
            margin-bottom: 15px;
        }

        h1 {
            font-size: 32px;
        }

        h2 {
            font-size: 28px;
        }

        h3 {
            font-size: 24px;
        }

        button {
            background-color: #ff9404;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #e08900;
        }


        input[type="submit"]:hover {
            background-color: #e08900;
        }
        .fa {
            font-family: FontAwesome !important;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 16px;
            text-align: left;
        }

        table th, table td {
            padding: 12px;
            border: 1px solid #ddd;
        }

        table th {
            background-color: #f0f0f0;
        }

        table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        a {
            color: #ff9404;
            text-decoration: none;
        }

        a:hover {
            color: #e08900;
        }

        footer a {
            color: #ff9404;
            text-decoration: none;
        }

        footer a:hover {
            color: #e08900;
        }
        .products-list {
            display: grid;
            grid-template-columns: repeat(5, 1fr);
            gap: 20px;
            padding: 20px;
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
            border-color: #ff9404;
        }


        .product-title {
            font-size: 14px;
            margin: 8px 0 5px 0;
            line-height: 1.3;
        }

        .product-title a {
            color: #333;
            text-decoration: none;
        }

        .price {
            margin-top: 5px;
        }

        .original-price1, .original-price2 {
            font-size: 14px;
            font-weight: bold;
        }

        .discounted-price1 {
            font-size: 14px;
            color: #e74c3c;
            font-weight: bold;
        }

        .badge {
            font-size: 11px;
            padding: 3px 6px;
        }

        .image-container {
            position: relative;
            text-align: center;
        }

        .image-count {
            position: absolute;
            bottom: 5px;
            right: 5px;
            background: rgba(0,0,0,0.7);
            color: white;
            font-size: 11px;
            padding: 2px 5px;
            border-radius: 3px;
        }
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
        .btnSeeOffers {
            background-color: #ff9404;
            display: inline-block;
            font-size: 20px;
            line-height: 1;
            color: #ffffff;
            min-width: 100px;
            text-align: center;
            border-radius: 999px;
            font-weight: 700;
            text-transform: uppercase;
            padding: 12px 20px;
            text-decoration: none;
            transition: background-color 0.3s ease;
            margin-top: -30px;
            margin-bottom: 150px;
        }

        .btnSeeOffers:hover {
            background-color: #444444;
            color: white;
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
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">
                ${successMessage}
            </div>
        </c:if>
        <div class="page-contain">

            <div id="main-content" class="main-content">

                <div class="container">
                    <div class="row">
                        <div class="main-slide block-slider nav-change hover-main-color type02">
                            <ul class="nestmart-carousel" data-slick='{"arrows": true, "dots": false, "slidesMargin": 0, "slidesToShow": 1, "infinite": true, "speed": 800}' >
                                <li>
                                    <div class="slide-contain slider-opt04__layout01 light-version first-slide">
                                        <div class="media"></div>
                                        <div class="text-content">
                                            <i class="first-line">Fresh Fruits & Vegetables</i>
                                            <h3 class="second-line">Nature's Best at Your Fingertips</h3>
                                            <p class="third-line">Explore our wide selection of organic fruits and vegetables, sourced directly from local farms to ensure maximum freshness and quality.</p>
                                        </div>

                                    </div>
                                </li>
                                <li>
                                    <div class="slide-contain slider-opt04__layout01 light-version second-slide">
                                        <div class="media"></div>
                                        <div class="text-content">
                                            <i class="first-line">Convenient Grocery Delivery</i>
                                            <h3 class="second-line">Shop from Home, Enjoy Freshness</h3>
                                            <p class="third-line">With our grocery delivery service, you can shop for your favorite products from the comfort of your home and have them delivered right to your doorstep.</p>
                                        </div>

                                </li>
                                <li>
                                    <div class="slide-contain slider-opt04__layout01 light-version third-slide">
                                        <div class="media"></div>
                                        <div class="text-content">
                                            <i class="first-line">Healthy Eating Made Easy</i>
                                            <h3 class="second-line">Choose Wellness with Every Bite</h3>
                                            <p class="third-line">Our supermarket offers a variety of healthy options, including gluten-free, vegan, and organic products, making it easier for you to maintain a balanced diet.</p>
                                        </div>

                                    </div>
                                </li>
                            </ul>
                        </div>

                    </div>

                </div>

                <div class="product-tab z-index-20 sm-margin-top-100px xs-margin-top-50px">
                    <div class="container">
                        <div class="nestmart-title-box slim-item">
                            <span class="subtitle">All the best item for You</span>
                            <h3 class="main-title">Our Products</h3>
                        </div>
                        <div id="tab01_1st" class="tab-contain active">
                            <c:if test="${listProducts.isEmpty()}">
                                <p>No products found.</p>
                            </c:if>

                            <c:if test="${!listProducts.isEmpty()}">
                                <ul class="products-list">
                                    <c:forEach var="product" items="${listProducts}">
                                        <li class="product-item">
                                            <div class="contain-product layout-default">
                                                <div class="product-thumb">
                                                    <c:choose>
                                                        <c:when test="${not empty product.images}">
                                                            <c:forEach var="image" items="${product.images}" varStatus="status">
                                                                <c:if test="${status.index == 0}">
                                                                    <div class="image-container">
                                                                        <a href="../client/productDetails.htm?productID=${product.productID}" class="link-to-product">
                                                                            <img src="../assets/admin/images/uploads/products/${image.images}" 
                                                                                 alt="${product.productName}" width="250" height="250" class="product-thumbnail"/>
                                                                        </a>
                                                                    </div>
                                                                </c:if>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            No image available
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="info">
                                                    <h4 class="product-title">
                                                        <a href="../client/productDetails.htm?productID=${product.productID}" class="pr-name">${product.productName}</a>
                                                    </h4>
                                                    <div class="price">
                                                        <div class="product-price-container">
                                                            <c:choose>
                                                                <c:when test="${product.discount > 0}">
                                                                    <p>
                                                                        <span class="original-price1">
                                                                            <fmt:formatNumber value="${product.unitPrice}" 
                                                                                              type="number" 
                                                                                              minFractionDigits="2" 
                                                                                              maxFractionDigits="2" 
                                                                                              groupingUsed="true" />$
                                                                        </span>
                                                                        <span class="discounted-price1">
                                                                            <fmt:formatNumber value="${product.discount}" 
                                                                                              type="number" 
                                                                                              minFractionDigits="2" 
                                                                                              maxFractionDigits="2" 
                                                                                              groupingUsed="true" />$
                                                                        </span>
                                                                    </p>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <p class="original-price2">
                                                                        <fmt:formatNumber value="${product.unitPrice}" 
                                                                                          type="number" 
                                                                                          minFractionDigits="2" 
                                                                                          maxFractionDigits="2" 
                                                                                          groupingUsed="true" />$
                                                                    </p>
                                                                </c:otherwise>
                                                            </c:choose>

                                                            <p>
                                                            <div style="display: flex;
                                                                 align-items: center;
                                                                 justify-content: center;
                                                                 background-color: #f8f9fa;
                                                                 padding: 10px;
                                                                 border-radius: 5px;">
                                                                <span class="badge bg-success" style="margin-right: 10px;">
                                                                    <c:choose>
                                                                        <c:when test="${product.averageRating > 0}">
                                                                            <fmt:formatNumber value="${product.averageRating}" 
                                                                                              type="number" 
                                                                                              minFractionDigits="1" 
                                                                                              maxFractionDigits="1" />
                                                                            <img src="../assets/client/images/star-16.png" alt="Feedback Star" style="vertical-align: middle;">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            No feedback
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </span>
                                                                <div style="width: 1px;
                                                                     height: 30px;
                                                                     background-color: #ccc;
                                                                     margin: 0 10px;"></div>
                                                                <span class="badge bg-light">Sold: ${product.totalQuantitySold}</span>
                                                            </div>
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>

                                </ul>
                            </c:if>
                            <div class="text-center" style="margin-top: 20px;">
                                <a href="product.htm" class="btnSeeOffers">
                                    SEE MORE
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="product-tab z-index-20 sm-margin-top-59px xs-margin-top-20px">
                    <div class="container">

                        <c:choose>
                            <c:when test="${not empty discount}">
                                <div class="nestmart-title-box slim-item">
                                    <h2 class="main-title">Newest Discount Here!</h2>
                                </div>

                                <div class="discount-section">
                                    <h2 class="discount-name">${discount.discountName}</h2>
                                    <h3 class="discount-description">${discount.description}</h3>
                                    <div class="discount-dates">
                                        From <b>${discount.startDate}</b> to <b>${discount.endDate}</b>
                                    </div>
                                    <img src="${pageContext.request.contextPath}/assets/admin/images/uploads/discount/${discount.image}" 
                                         alt="${discount.discountName}" class="discount-banner">

                                    <ul class="products-list">
                                        <c:forEach items="${listProducts1}" var="product">
                                            <li class="product-item">
                                                <div class="contain-product layout-default">
                                                    <div class="product-thumb">
                                                        <c:choose>
                                                            <c:when test="${not empty product.images}">
                                                                <c:forEach var="image" items="${product.images}" varStatus="status">
                                                                    <c:if test="${status.index == 0}">
                                                                        <div class="image-container">
                                                                            <a href="${pageContext.request.contextPath}/client/productDetails.htm?productID=${product.productID}" class="link-to-product">
                                                                                <img src="${pageContext.request.contextPath}/assets/client/images/uploads/products/${image.images}" 
                                                                                     alt="${product.productName}" width="250" height="250" class="product-thumnail"/>
                                                                            </a>
                                                                            <c:if test="${product.images.size() > 1}">
                                                                                <span class="image-count">+${product.images.size() - 1}</span>
                                                                            </c:if>
                                                                        </div>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                No image available
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>

                                                    <div class="info">
                                                        <h4 class="product-title">
                                                            <a href="${pageContext.request.contextPath}/client/productDetails.htm?productID=${product.productID}" class="pr-name">${product.productName}</a>
                                                        </h4>
                                                        <div class="price">
                                                            <div class="product-price-container">
                                                                <c:if test="${product.quantity > 0}">
                                                                    <c:choose>
                                                                        <c:when test="${product.discount > 0}">
                                                                            <p>
                                                                                <span class="original-price1">
                                                                                    <fmt:formatNumber value="${product.unitPrice}" 
                                                                                                      type="number" 
                                                                                                      minFractionDigits="2" 
                                                                                                      maxFractionDigits="2" 
                                                                                                      groupingUsed="true" />$
                                                                                </span>
                                                                                <span class="discounted-price1">
                                                                                    <fmt:formatNumber value="${product.discount}" 
                                                                                                      type="number" 
                                                                                                      minFractionDigits="2" 
                                                                                                      maxFractionDigits="2" 
                                                                                                      groupingUsed="true" />$
                                                                                </span>
                                                                            </p>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <p class="original-price2">
                                                                                <fmt:formatNumber value="${product.unitPrice}" 
                                                                                                  type="number" 
                                                                                                  minFractionDigits="2" 
                                                                                                  maxFractionDigits="2" 
                                                                                                  groupingUsed="true" />$
                                                                            </p>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <div style="display: flex; align-items: center; justify-content: center;
                                                                         background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
                                                                        <span class="badge bg-success" style="margin-right: 10px;">
                                                                            <c:choose>
                                                                                <c:when test="${product.averageRating > 0}">
                                                                                    <fmt:formatNumber value="${product.averageRating}" 
                                                                                                      type="number" 
                                                                                                      minFractionDigits="1" 
                                                                                                      maxFractionDigits="1" />
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
                                                                        <span class="sold-out-badge">SOLD OUT</span>
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

                                <div class="text-center" style="margin-top: 20px;">
                                    <a href="../client/discount.htm" class="btnSeeOffers">
                                        SEE MORE DISCOUNT
                                    </a>
                                </div>
                            </c:when>


                            <c:otherwise>

                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>


            </div>
        </div>
        <div class="banner-block sm-margin-bottom-57px xs-margin-top-80px sm-margin-top-30px">
            <div class="container">
                <ul class="nestmart-carousel nav-center-bold nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":30,"slidesToShow":3, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 3}},{"breakpoint":992, "settings":{ "slidesToShow": 2}},{"breakpoint":768, "settings":{ "slidesToShow": 2}}, {"breakpoint":500, "settings":{ "slidesToShow": 1}}]}'>
                    <li>
                        <div class="nestmart-banner nestmart-banner__style-08">
                            <div class="banner-contain">
                                <div class="media">
                                    <a href="#" class="bn-link"><img src="../assets/client/images/home-04/bn_style08.png" width="193" height="185" alt=""/></a>
                                </div>
                                <div class="text-content">
                                    <span class="text1">Sumer Fruit</span>
                                    <b class="text2">100% Pure Natural Fruit Juice</b>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="nestmart-banner nestmart-banner__style-09">
                            <div class="banner-contain">
                                <div class="media">
                                    <a href="#" class="bn-link"><img src="../assets/client/images/home-04/bn_style09.png" width="191" height="185" alt=""/></a>
                                </div>
                                <div class="text-content">
                                    <span class="text1">California</span>
                                    <b class="text2">Fresh Fruit</b>
                                    <span class="text3">Association</span>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="nestmart-banner nestmart-banner__style-10">
                            <div class="banner-contain">
                                <div class="media">
                                    <a href="#" class="bn-link"><img src="../assets/client/images/home-04/bn_style10.png" width="185" height="185" alt=""/></a>
                                </div>
                                <div class="text-content">
                                    <span class="text1">Naturally fresh taste</span>
                                    <p class="text2">With <span>25% Off</span> All Teas</p>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>





        <div class="who-we-are xs-margin-top-50px sm-margin-top-40px">
            <div class="container">
                <div class="nestmart-title-box">
                    <h3 class="main-title">WHO WE ARE</h3>
                    <span class="subtitle">Discover the Essence of Our Supermarket</span>
                </div>
                <p style="margin-top: 20px">
                    Welcome to NestMart! Your ultimate supermarket destination, offering a wide range of quality products from fresh produce and organic items to household essentials and gourmet goods. We source from trusted local suppliers and renowned brands to ensure the best selection at competitive prices.

                    At NestMart, we support local communities and sustainability by partnering with farmers and providing eco-friendly products. Our friendly and knowledgeable staff are always ready to assist you, making your shopping experience convenient and enjoyable.

                    We also host events, promotions, and seasonal discounts to engage with customers and create a vibrant community atmosphere. Thank you for choosing NestMart – where quality, affordability, and exceptional service come together for a delightful shopping experience!
                </p>
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