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
        <title>nestmart - Organic Food</title>
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
        .product-image-container {
            text-align: center;
        }

        .product-image {
            width: 60%;
            max-width: 300px;
            height: auto;
        }

        .product-thumbnails img {
            width: 80px;
            height: 80px;
            cursor: pointer;
            margin: 5px;
        }

        .quantity-control {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .quantity-control input {
            width: 50px;
            text-align: center;
        }

        .add-to-cart-btn {
            background-color: #ff0000;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            font-size: 16px;
            margin-top: 20px;
        }

        .product-thumbnails img {
            width: 80px;
            height: 80px;
            cursor: pointer;
            margin: 5px;
            border: 2px solid transparent;
            transition: border-color 0.3s ease;
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

        .product-thumbnails img.active-thumbnail {
            border-color: red;
        }

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

        .product-item:hover {
            transform: translateY(-6px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
            border-color: #ff9404;
        }

        .product-thumb1 img {
            transition: transform 0.4s ease;
        }

        .product-item:hover .product-thumb1 img {
            transform: scale(1.08);
        }

        .product-item:hover .product-actions {
            opacity: 1;
            transform: translate(-50%, -50%) scale(1);
        }

        .product-thumbnail1 {
            width: 180px !important;
            height: 180px !important;
            object-fit: cover;
            object-position: center;
            display: block;
            margin: 0 auto;
        }

        .product-title {
            font-size: 16px;
            margin: 25px 0 5px 0;
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

        .feedback__item--img img{
            width: 10%;
            height: auto;
        }

        html {
            scroll-behavior: smooth;
        }

        #feedback-pagination {
            width: 100%;
            display: flex;
            justify-content: center;
            margin: 20px 0;
        }

        #feedback-pagination nav {
            width: 100%;
            display: flex;
            justify-content: center;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0 auto;
            list-style: none;
            padding: 0;
        }

        .pagination .page-item .page-link {
            color: #282c3c;
            background-color: #fff;
            border: 1px solid #282c3c;
            padding: 8px 16px;
            margin: 0 4px;
            border-radius: 20px;
            transition: all 0.3s ease;
            text-decoration: none;
        }

        .pagination .page-item.active .page-link {
            color: #fff;
            background-color: #282c3c;
            border-color: #282c3c;
        }

        .pagination .page-item .page-link:hover {
            color: #fff;
            background-color: #282c3c;
            border-color: #282c3c;
        }

        .pagination .page-item.disabled .page-link {
            color: #6c757d;
            background-color: #fff;
            border-color: #dee2e6;
            cursor: not-allowed;
        }

        /* Mobile responsive */
        @media (max-width: 768px) {
            .pagination .page-item .page-link {
                padding: 6px 12px;
                margin: 0 2px;
                font-size: 14px;
            }
        }

        @media (max-width: 480px) {
            .pagination .page-item .page-link {
                padding: 4px 8px;
                margin: 0 1px;
                font-size: 13px;
            }


        }

        .products-list {
            display: grid;
            gap: 31px;
            padding: 40px;
        }

        @media (min-width: 1201px) {
            .products-list {
                grid-template-columns: repeat(5, 1fr);
                gap: 31px;
                padding: 40px;
            }
        }

        @media (min-width: 993px) and (max-width: 1200px) {
            .products-list {
                grid-template-columns: repeat(4, 1fr);
                gap: 25px;
                padding: 30px;
            }
        }

        @media (min-width: 769px) and (max-width: 992px) {
            .products-list {
                grid-template-columns: repeat(3, 1fr);
                gap: 20px;
                padding: 25px;
            }
        }

        @media (min-width: 577px) and (max-width: 768px) {
            .products-list {
                grid-template-columns: repeat(2, 1fr);
                gap: 15px;
                padding: 20px;
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
        #main-large-image {
            display: none;
            width: 100%;
            margin-top: 20px;
            clear: both;
            text-align: left;
        }

        #main-large-image img {
            width: auto;
            max-width: 400px;
            max-height: 300px;
            height: auto;
            border: 1px solid #ccc;
            border-radius: 8px;
            object-fit: contain;
        }

        @media (max-width: 768px) {
            #main-large-image {
                text-align: center;
            }

            #main-large-image img {
                max-width: 70vw;
                max-height: 50vh;
            }
        }
        .employee-response {
            position: relative;
            margin-top: 15px;
            padding: 15px;
            background-color: #f8f9fa;
            border-left: 4px solid #007bff;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .employee-response::before {
            content: '';
            position: absolute;
            top: 15px;
            left: -8px;
            width: 0;
            height: 0;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
            border-right: 8px solid #f8f9fa;
        }

        .employee-response .response-header {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .employee-response .fa-reply {
            color: #007bff;
            margin-right: 8px;
        }

        .employee-response strong {
            color: #007bff;
            font-weight: 600;
        }

        .employee-response p {
            margin: 0;
            color: #495057;
            line-height: 1.5;
        }

        .employee-response .response-date {
            font-size: 12px;
            color: #6c757d;
            margin-top: 8px;
        }

        @media (max-width: 768px) {
            .employee-response {
                margin-left: 0;
                padding: 12px;
            }

            .employee-response .response-header {
                flex-direction: column;
                align-items: flex-start;
            }

            .employee-response .fa-reply {
                margin-bottom: 5px;
            }

        }
        .fa {
            font-family: FontAwesome !important;
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
                        <form action="../client/addToCart.htm" method="post">
                            <div class="container product-details">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="product-image-container">
                                            <img id="mainImage" 
                                                 src="../assets/admin/images/uploads/products/${not empty product.images ? product.images[0].images : 'default.jpg'}" 
                                                 alt="${product.productName}" class="product-image img-fluid">
                                            <div class="product-thumbnails">
                                                <c:forEach var="image" items="${product.images}">
                                                    <img src="../assets/admin/images/uploads/products/${image.images}" 
                                                         alt="${product.productName}" class="thumbnail-image" 
                                                         onclick="changeImage(this.src)">
                                                </c:forEach>
                                            </div>
                                            <input type="hidden" name="productImage" value="${product.images[0].images}">
                                        </div>
                                    </div>


                                    <div class="col-md-6">
                                        <h1 style="font-weight: bold; color:black">${product.productName}</h1>
                                        <div class="product-info">
                                            <p>
                                                <a href="#feedback-section" class="text-decoration-none">
                                                    <span class="badge bg-success">
                                                        ${averageRating} 
                                                        <img src="../assets/client/images/star-16.png" alt="Feedback Star">
                                                    </span> 
                                                    (${feedbackCount} feedbacks)
                                                </a>
                                            </p>


                                            <div class="product-price-container">
                                                <c:choose>
                                                    <c:when test="${product.discount > 0}">
                                                        <p>
                                                            <span class="original-price">
                                                                <fmt:formatNumber value="${product.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                            </span>
                                                            <span class="discounted-price">
                                                                <fmt:formatNumber value="${product.discount}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                            </span>
                                                            <input type="hidden" name="productPrice" value="${product.discount}">
                                                        </p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p class="product-price">
                                                            <fmt:formatNumber value="${product.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                        </p>
                                                        <input type="hidden" name="productPrice" value="${product.unitPrice}">
                                                    </c:otherwise>
                                                </c:choose>

                                            </div>

                                            <p class="product-description">${product.productDescription}</p>
                                        </div>
                                        <input type="hidden" name="productId" value="${product.productID}">
                                        <input type="hidden" name="productName" value="${product.productName}">
                                        <div class="from-cart">
                                            <p class="quantity-text">Quantity:</p>
                                            <div class="qty-input">
                                                <button type="button" class="qty-btn btn-down" onclick="decreaseQuantity()" 
                                                        <c:if test="${product.quantity == 0}">disabled</c:if>>-</button>

                                                        <input type="text" id="quantity" name="quantity" class="form-control quantity-input"
                                                               value="1" readonly 
                                                               data-max="${product.quantity}"
                                                        <c:if test="${product.quantity == 0}">disabled</c:if> />

                                                        <button type="button" class="qty-btn btn-up" onclick="increaseQuantity()" 
                                                                id="btnUp"
                                                        <c:if test="${product.quantity == 0}">disabled</c:if>>+</button>
                                                </div>
                                            </div>

                                        <c:if test="${product.quantity <= 5 && product.quantity > 0}">
                                            <div class="alert alert-warning mt-3" style="margin-top: 20px" role="alert">
                                                Hurry up! Only ${product.quantity} items left in stock!
                                            </div>
                                        </c:if>

                                        <c:if test="${product.quantity == 0}">
                                            <div class="alert alert-danger mt-3" style="margin-top: 20px" role="alert">
                                                This product is sold out!
                                            </div>
                                        </c:if>


                                        <div class="mt-4">
                                            <button type="submit" class="btnAddcart"
                                                    <c:if test="${product.quantity == 0}">disabled style="opacity:0.5; cursor:not-allowed;"</c:if>>
                                                        Add To Cart
                                                    </button>
                                            </div>
                                        <c:if test="${not empty errorMessage}">
                                            <div class="alert alert-danger" style="margin-top: 30px" role="alert">
                                                ${errorMessage}
                                            </div>
                                        </c:if> 
                                        <c:if test="${not empty successMessage}">
                                            <div class="alert alert-success" style="margin-top: 30px" role="alert">
                                                Product added to cart successfully!
                                            </div>
                                        </c:if> 
                                    </div>
                                </div>
                            </div>
                            <div class="product-details-section mt-4">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th colspan="2" style="text-align: center">Product Details</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${categoryDetailsList}" var="detail">
                                            <tr>
                                                <td class="attribute-name">${detail.attributeName}</td>
                                                <td>${detail.attributeValue}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                        </form>
                        <div id="feedback-section" class="feedback-section">
                            <h1>Customer Feedbacks</h1>
                            <div class="filter-section">
                                <div class="star-group" role="group">
                                    <button type="button" class="btn btn-outline-primary" onclick="filterFeedbacks(0)">
                                        All Ratings
                                    </button>
                                    <button type="button" class="btn btn-outline-primary" onclick="filterFeedbacks(1)">
                                        1 <img src="../assets/client/images/star-16.png" alt="1 Star">
                                    </button>
                                    <button type="button" class="btn btn-outline-primary" onclick="filterFeedbacks(2)">
                                        2 <img src="../assets/client/images/star-16.png" alt="2 Stars">
                                    </button>
                                    <button type="button" class="btn btn-outline-primary" onclick="filterFeedbacks(3)">
                                        3 <img src="../assets/client/images/star-16.png" alt="3 Stars">
                                    </button>
                                    <button type="button" class="btn btn-outline-primary" onclick="filterFeedbacks(4)">
                                        4 <img src="../assets/client/images/star-16.png" alt="4 Stars">
                                    </button>
                                    <button type="button" class="btn btn-outline-primary" onclick="filterFeedbacks(5)">
                                        5 <img src="../assets/client/images/star-16.png" alt="5 Stars">
                                    </button>
                                </div>
                            </div>

                            <div id="feedback-list">
                                <c:choose>
                                    <c:when test="${not empty feedbacks}">
                                        <ul class="list-group">
                                            <c:forEach items="${feedbacks}" var="feedback">
                                                <li class="list-group-item">
                                                    <p><strong>${feedback.customerName}</strong> rated: 
                                                        <span class="badge bg-success">${feedback.rating} 
                                                            <img src="../assets/client/images/star-16.png" alt="Rating Star">
                                                        </span>
                                                    </p>
                                                    <p>${feedback.feedbackContent}</p>

                                                    <c:if test="${not empty feedback.images}">
                                                        <div class="feedback-images-container" style="margin: 10px 0;">
                                                            <c:forEach items="${feedback.images}" var="img">
                                                                <div class="feedback-image-wrapper" style="display:inline-block; text-align:center;">
                                                                    <img src="${pageContext.request.contextPath}/assets/client/images/uploads/feedbacks/${img.image}" 
                                                                         alt="Feedback Image" 
                                                                         class="feedback-image"
                                                                         style="max-width: 150px;
                                                                         max-height: 150px;
                                                                         margin-right: 10px;
                                                                         margin-bottom: 10px;
                                                                         border: 1px solid #ddd;
                                                                         border-radius: 5px;
                                                                         object-fit: cover;
                                                                         cursor: pointer;"
                                                                         onclick="showLargeFeedbackImage(this)" />
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                    </c:if>
                                                    <p class="text-muted"><small>${feedback.formattedFeedbackDate}</small></p>
                                                            <c:if test="${not empty feedback.employeeResponse}">
                                                        <div class="employee-response" style="margin-top: 15px; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #007bff; border-radius: 5px;">
                                                            <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                                                <i class="fa fa-reply" style="color: #007bff; margin-right: 8px;"></i>
                                                                <strong style="color: #007bff;">Store Response:</strong>
                                                            </div>
                                                            <p style="margin: 0; color: #495057;">${feedback.employeeResponse.responseContent}</p>
                                                            <small class="text-muted" style="font-size: 12px;">
                                                                Responded on: ${feedback.employeeResponse.formattedResponseDate}
                                                            </small>
                                                        </div>
                                                    </c:if>


                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:when>
                                    <c:otherwise>
                                        <p>No feedbacks available.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div id="feedback-pagination">
                                <c:if test="${totalPages > 1}">
                                    <nav aria-label="Feedback pagination" class="mt-3">
                                        <ul class="pagination">
                                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                                <a class="page-link" href="javascript:void(0)"
                                                   <c:if test="${currentPage != 1}">
                                                       onclick="loadFeedbackPage(${currentPage - 1}, ${starFilter != null ? starFilter : 0})"
                                                   </c:if>
                                                   >Previous</a>
                                            </li>

                                            <c:set var="maxDisplayPages" value="5"/>

                                            <c:choose>
                                                <c:when test="${totalPages <= maxDisplayPages}">
                                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                            <a class="page-link" href="javascript:void(0)"
                                                               onclick="loadFeedbackPage(${i}, ${starFilter != null ? starFilter : 0})">${i}</a>
                                                        </li>
                                                    </c:forEach>
                                                </c:when>

                                                <c:when test="${currentPage <= 3}">
                                                    <c:forEach begin="1" end="${maxDisplayPages}" var="i">
                                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                            <a class="page-link" href="javascript:void(0)"
                                                               onclick="loadFeedbackPage(${i}, ${starFilter != null ? starFilter : 0})">${i}</a>
                                                        </li>
                                                    </c:forEach>
                                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                                    <li class="page-item">
                                                        <a class="page-link" href="javascript:void(0)"
                                                           onclick="loadFeedbackPage(${totalPages}, ${starFilter != null ? starFilter : 0})">${totalPages}</a>
                                                    </li>
                                                </c:when>

                                                <c:when test="${currentPage >= totalPages - 2}">
                                                    <li class="page-item">
                                                        <a class="page-link" href="javascript:void(0)"
                                                           onclick="loadFeedbackPage(1, ${starFilter != null ? starFilter : 0})">1</a>
                                                    </li>
                                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                                        <c:forEach begin="${totalPages - (maxDisplayPages - 1)}" end="${totalPages}" var="i">
                                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                            <a class="page-link" href="javascript:void(0)"
                                                               onclick="loadFeedbackPage(${i}, ${starFilter != null ? starFilter : 0})">${i}</a>
                                                        </li>
                                                    </c:forEach>
                                                </c:when>

                                                <c:otherwise>
                                                    <li class="page-item">
                                                        <a class="page-link" href="javascript:void(0)"
                                                           onclick="loadFeedbackPage(1, ${starFilter != null ? starFilter : 0})">1</a>
                                                    </li>
                                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                                        <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                            <a class="page-link" href="javascript:void(0)"
                                                               onclick="loadFeedbackPage(${i}, ${starFilter != null ? starFilter : 0})">${i}</a>
                                                        </li>
                                                    </c:forEach>
                                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                                    <li class="page-item">
                                                        <a class="page-link" href="javascript:void(0)"
                                                           onclick="loadFeedbackPage(${totalPages}, ${starFilter != null ? starFilter : 0})">${totalPages}</a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>

                                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                <a class="page-link" href="javascript:void(0)"
                                                   <c:if test="${currentPage != totalPages}">
                                                       onclick="loadFeedbackPage(${currentPage + 1}, ${starFilter != null ? starFilter : 0})"
                                                   </c:if>
                                                   >Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </c:if>
                            </div>




                        </div>
                    </div>
                </div>
            </div>

            <div class="product-tab z-index-20 sm-margin-top-59px xs-margin-top-20px">
                <div class="container">
                    <div class="nestmart-title-box slim-item">
                        <h3 class="main-title">Another Products</h3>
                    </div>
                    <div id="tab01_1st" class="tab-contain active">
                        <c:if test="${listProducts1.isEmpty()}">
                            <p>No products found.</p>
                        </c:if>

                        <c:if test="${!listProducts1.isEmpty()}">
                            <ul class="products-list">
                                <c:forEach var="product" items="${listProducts1}">
                                    <li class="product-item">
                                        <div class="contain-product layout-default">
                                            <div class="product-thumb1">
                                                <c:choose>
                                                    <c:when test="${not empty product.images}">
                                                        <c:forEach var="image" items="${product.images}" varStatus="status">
                                                            <c:if test="${status.index == 0}">
                                                                <div class="image-container">
                                                                    <a href="../client/productDetails.htm?productID=${product.productID}" class="link-to-product">
                                                                        <img src="../assets/admin/images/uploads/products/${image.images}" 
                                                                             alt="${product.productName}" width="250" height="250" class="product-thumbnail1"/>
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
                                                                        <fmt:formatNumber value="${product.averageRating}" type="number" maxFractionDigits="1" />
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

                    </div>
                </div>
            </div>


        </div>

        <jsp:include page="footer.jsp" />

        <a class="btn-scroll-top"><i class="nestmart-icon icon-left-arrow"></i></a>

        <script src="../assets/client/js/jquery-3.4.1.min.js"></script>
        <script src="../assets/client/js/bootstrap.min.js"></script>
        <script src="../assets/client/js/jquery.countdown.min.js"></script>
        <script src="../assets/client/js/jquery.nice-select.min.js"></script>
        <script src="../assets/client/js/jquery.nicescroll.min.js"></script>
        <script src="../assets/client/js/slick.min.js"></script>
        <script src="../assets/client/js/nestmart.framework.js"></script>
        <script src="../assets/client/js/functions.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>



        <script>
                                                           function increaseQuantity() {
                                                               let input = document.getElementById("quantity");
                                                               let max = parseInt(input.getAttribute("data-max"));
                                                               let current = parseInt(input.value);

                                                               if (current < max) {
                                                                   input.value = current + 1;
                                                               }
                                                               if (current + 1 >= max) {
                                                                   document.getElementById("btnUp").disabled = true;
                                                               }
                                                           }

                                                           function decreaseQuantity() {
                                                               let input = document.getElementById("quantity");
                                                               let current = parseInt(input.value);

                                                               if (current > 1) {
                                                                   input.value = current - 1;
                                                                   document.getElementById("btnUp").disabled = false;
                                                               }
                                                           }


                                                           function changeImage(imageSrc) {
                                                               document.getElementById("mainImage").src = imageSrc;
                                                           }
                                                           fetch('/nestmartFinal/client/getFeedbacks?productID=fruit0002-4')
                                                                   .then(res => res.json())
                                                                   .then(data => {
                                                                       let container = document.getElementById("feedbackContainer");
                                                                       let dateStr = fb.createdAt; // ví dụ: "2025-08-20T14:33:25"
                                                                       let date = new Date(dateStr);
                                                                       let formattedDate = isNaN(date.getTime()) ? dateStr : date.toLocaleDateString();
                                                                       container.innerHTML = "";
                                                                       data.forEach(fb => {
                                                                           container.innerHTML += `
                                                            <div class="feedback">
                                                                <strong>${fb.customerName}</strong>
                                                                <small>${formattedDate}</small>
                                                                <p>${fb.content}</p>
                                                            </div>
                                                        `;
                                                                       });
                                                                   })
                                                                   .catch(err => console.error(err));
                                                           function showLargeFeedbackImage(imgElement) {
                                                               const feedbackItem = imgElement.closest('.list-group-item');
                                                               let largeContainer = feedbackItem.querySelector('.main-large-image');

                                                               if (!largeContainer) {
                                                                   largeContainer = document.createElement('div');
                                                                   largeContainer.className = 'main-large-image';
                                                                   largeContainer.style.cssText = 'display:none; text-align: left; margin-top:20px; width:100%; clear:both;';

                                                                   const largeImg = document.createElement('img');
                                                                   largeImg.className = 'large-image';
                                                                   largeImg.style.cssText = 'max-width: 400px; max-height: 300px; height:auto; border:1px solid #ccc; border-radius:8px; object-fit: contain;';

                                                                   largeContainer.appendChild(largeImg);
                                                                   feedbackItem.querySelector('.feedback-images-container').appendChild(largeContainer);
                                                               }

                                                               const largeImg = largeContainer.querySelector('.large-image');

                                                               if (largeImg.src === imgElement.src && largeContainer.style.display === "block") {
                                                                   largeContainer.style.display = "none";
                                                               } else {
                                                                   largeImg.src = imgElement.src;
                                                                   largeContainer.style.display = "block";
                                                               }
                                                           }
                                                           var productID = '${product.productID}';
                                                           var currentStarFilter = ${starFilter != null ? starFilter : 0};

                                                           function filterFeedbacks(starRating) {
                                                               const feedbackContainer = document.getElementById('feedback-list');
                                                               feedbackContainer.innerHTML = '<div class="text-center"><p>Loading feedbacks...</p></div>';

                                                               const url = '../client/getFeedbacksFiltered.htm?productID=' + productID +
                                                                       '&starFilter=' + (starRating || '') + '&page=1';

                                                               fetch(url)
                                                                       .then(response => {
                                                                           if (!response.ok) {
                                                                               throw new Error('Server responded with status: ' + response.status);
                                                                           }
                                                                           return response.text();
                                                                       })
                                                                       .then(responseText => {
                                                                           const data = JSON.parse(responseText);
                                                                           updateFeedbackDisplay(data);
                                                                           updateActiveButton(starRating);
                                                                       })
                                                                       .catch(error => {
                                                                           feedbackContainer.innerHTML =
                                                                                   '<div class="alert alert-danger">' +
                                                                                   '<h5>Error Loading Feedbacks</h5>' +
                                                                                   '<p>Error: ' + error.message + '</p>' +
                                                                                   '<button class="btn btn-primary btn-sm" onclick="location.reload()">Refresh Page</button>' +
                                                                                   '</div>';
                                                                       });
                                                           }

                                                           function loadFeedbackPage(page, starFilter) {
                                                               const feedbackContainer = document.getElementById('feedback-list');
                                                               feedbackContainer.innerHTML = '<div class="text-center"><p>Loading page ' + page + '...</p></div>';

                                                               const url = '../client/getFeedbacksFiltered.htm?productID=' + productID +
                                                                       '&starFilter=' + (starFilter || '') + '&page=' + page;

                                                               fetch(url)
                                                                       .then(response => {
                                                                           if (!response.ok) {
                                                                               throw new Error('HTTP ' + response.status);
                                                                           }
                                                                           return response.text();
                                                                       })
                                                                       .then(responseText => {
                                                                           const data = JSON.parse(responseText);
                                                                           updateFeedbackDisplay(data);

                                                                           document.getElementById('feedback-section').scrollIntoView({behavior: 'smooth'});
                                                                       })
                                                                       .catch(error => {
                                                                           feedbackContainer.innerHTML = '<div class="alert alert-danger">Error loading page: ' + error.message + '</div>';
                                                                       });
                                                           }


                                                           function updateFeedbackDisplay(data) {
                                                               const feedbackContainer = document.getElementById('feedback-list');
                                                               const paginationContainer = document.getElementById('feedback-pagination');

                                                               if (!data || data.success === false) {
                                                                   feedbackContainer.innerHTML = '<div class="alert alert-danger">Error: ' + (data && data.error ? data.error : 'Unknown error') + '</div>';
                                                                   paginationContainer.innerHTML = '';
                                                                   return;
                                                               }

                                                               if (data.feedbacks && data.feedbacks.length > 0) {
                                                                   let html = '<ul class="list-group">';

                                                                   data.feedbacks.forEach(function (feedback) {
                                                                       html += '<li class="list-group-item">';
                                                                       html += '<p><strong>' + (feedback.customerName || 'Anonymous') + '</strong> rated: ';
                                                                       html += '<span class="badge bg-success">' + (feedback.rating || 0) + ' ';
                                                                       html += '<img src="../assets/client/images/star-16.png" alt="Rating Star"></span></p>';
                                                                       html += '<p>' + (feedback.feedbackContent || 'No comment provided') + '</p>';

                                                                       if (feedback.feedbackImages && Array.isArray(feedback.feedbackImages) && feedback.feedbackImages.length > 0) {
                                                                           html += '<div class="feedback-images-container" style="margin: 10px 0;">';
                                                                           feedback.feedbackImages.forEach(function (imagePath, index) {
                                                                               html += '<div class="feedback-image-wrapper" style="display:inline-block; text-align:center;">';
                                                                               html += '<img src="../assets/client/images/uploads/feedbacks/' + imagePath + '" ';
                                                                               html += 'alt="Feedback Image" class="feedback-image" ';
                                                                               html += 'style="max-width: 150px; max-height: 150px; margin-right: 10px; margin-bottom: 10px; ';
                                                                               html += 'border: 1px solid #ddd; border-radius: 5px; object-fit: cover; cursor: pointer;" ';
                                                                               html += 'data-feedback-index="' + index + '" />';
                                                                               html += '</div>';
                                                                           });
                                                                           html += '</div>';
                                                                       }

                                                                       if (feedback.employeeResponse && feedback.employeeResponse.responseContent) {
                                                                           html += '<div class="employee-response" style="margin-top: 15px; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #007bff; border-radius: 5px;">';
                                                                           html += '<div style="display: flex; align-items: center; margin-bottom: 10px;">';
                                                                           html += '<i class="fa fa-reply" style="color: #007bff; margin-right: 8px;"></i>';
                                                                           html += '<strong style="color: #007bff;">Store Response:</strong>';
                                                                           html += '</div>';
                                                                           html += '<p style="margin: 0; color: #495057;">' + (feedback.employeeResponse.responseContent || '') + '</p>';
                                                                           if (feedback.employeeResponse.formattedResponseDate) {
                                                                               html += '<small class="text-muted" style="font-size: 12px;">Responded on: ' + feedback.employeeResponse.formattedResponseDate + '</small>';
                                                                           }
                                                                           html += '</div>';
                                                                       }

                                                                       html += '<p class="text-muted"><small>' + (feedback.formattedFeedbackDate || '') + '</small></p>';
                                                                       html += '</li>';
                                                                   });

                                                                   html += '</ul>';
                                                                   feedbackContainer.innerHTML = html;

                                                                   bindImageClickEvents();
                                                                   updatePagination(data.currentPage, data.totalPages, data.starFilter);
                                                               } else {
                                                                   feedbackContainer.innerHTML = '<p class="text-center text-muted">No feedbacks available for this rating.</p>';
                                                                   paginationContainer.innerHTML = '';
                                                               }
                                                           }

                                                           function bindImageClickEvents() {
                                                               const images = document.querySelectorAll('.feedback-image');
                                                               images.forEach(function (img) {
                                                                   img.addEventListener('click', function () {
                                                                       showLargeFeedbackImage(this);
                                                                   });
                                                               });
                                                           }

                                                           function updatePagination(currentPage, totalPages, starFilter) {
                                                               const paginationContainer = document.getElementById('feedback-pagination');

                                                               if (!totalPages || totalPages <= 1) {
                                                                   paginationContainer.innerHTML = '';
                                                                   return;
                                                               }

                                                               const maxDisplayPages = 5;
                                                               let html = '<nav aria-label="Feedback pagination" class="mt-3">';
                                                               html += '<ul class="pagination">';

                                                               const prevDisabled = currentPage == 1 ? 'disabled' : '';
                                                               html += '<li class="page-item ' + prevDisabled + '">';
                                                               html += '<a class="page-link" href="javascript:void(0)" ';
                                                               html += 'onclick="loadFeedbackPage(' + (currentPage - 1) + ', ' + (starFilter || 0) + ')">Previous</a>';
                                                               html += '</li>';

                                                               if (totalPages <= maxDisplayPages) {
                                                                   for (let i = 1; i <= totalPages; i++) {
                                                                       const activeClass = i === currentPage ? 'active' : '';
                                                                       html += '<li class="page-item ' + activeClass + '">';
                                                                       html += '<a class="page-link" href="javascript:void(0)" ';
                                                                       html += 'onclick="loadFeedbackPage(' + i + ', ' + (starFilter || 0) + ')">' + i + '</a>';
                                                                       html += '</li>';
                                                                   }
                                                               } else if (currentPage <= 3) {
                                                                   for (let i = 1; i <= maxDisplayPages; i++) {
                                                                       const activeClass = i === currentPage ? 'active' : '';
                                                                       html += '<li class="page-item ' + activeClass + '">';
                                                                       html += '<a class="page-link" href="javascript:void(0)" ';
                                                                       html += 'onclick="loadFeedbackPage(' + i + ', ' + (starFilter || 0) + ')">' + i + '</a>';
                                                                       html += '</li>';
                                                                   }
                                                                   html += '<li class="page-item disabled"><span class="page-link">...</span></li>';
                                                                   html += '<li class="page-item">';
                                                                   html += '<a class="page-link" href="javascript:void(0)" ';
                                                                   html += 'onclick="loadFeedbackPage(' + totalPages + ', ' + (starFilter || 0) + ')">' + totalPages + '</a>';
                                                                   html += '</li>';
                                                               } else if (currentPage >= totalPages - 2) {
                                                                   html += '<li class="page-item">';
                                                                   html += '<a class="page-link" href="javascript:void(0)" ';
                                                                   html += 'onclick="loadFeedbackPage(1, ' + (starFilter || 0) + ')">1</a>';
                                                                   html += '</li>';
                                                                   html += '<li class="page-item disabled"><span class="page-link">...</span></li>';

                                                                   for (let i = totalPages - (maxDisplayPages - 1); i <= totalPages; i++) {
                                                                       const activeClass = i === currentPage ? 'active' : '';
                                                                       html += '<li class="page-item ' + activeClass + '">';
                                                                       html += '<a class="page-link" href="javascript:void(0)" ';
                                                                       html += 'onclick="loadFeedbackPage(' + i + ', ' + (starFilter || 0) + ')">' + i + '</a>';
                                                                       html += '</li>';
                                                                   }
                                                               } else {
                                                                   html += '<li class="page-item">';
                                                                   html += '<a class="page-link" href="javascript:void(0)" ';
                                                                   html += 'onclick="loadFeedbackPage(1, ' + (starFilter || 0) + ')">1</a>';
                                                                   html += '</li>';
                                                                   html += '<li class="page-item disabled"><span class="page-link">...</span></li>';

                                                                   for (let i = currentPage - 1; i <= currentPage + 1; i++) {
                                                                       const activeClass = i === currentPage ? 'active' : '';
                                                                       html += '<li class="page-item ' + activeClass + '">';
                                                                       html += '<a class="page-link" href="javascript:void(0)" ';
                                                                       html += 'onclick="loadFeedbackPage(' + i + ', ' + (starFilter || 0) + ')">' + i + '</a>';
                                                                       html += '</li>';
                                                                   }

                                                                   html += '<li class="page-item disabled"><span class="page-link">...</span></li>';
                                                                   html += '<li class="page-item">';
                                                                   html += '<a class="page-link" href="javascript:void(0)" ';
                                                                   html += 'onclick="loadFeedbackPage(' + totalPages + ', ' + (starFilter || 0) + ')">' + totalPages + '</a>';
                                                                   html += '</li>';
                                                               }

                                                               const nextDisabled = currentPage == totalPages ? 'disabled' : '';
                                                               html += '<li class="page-item ' + nextDisabled + '">';
                                                               html += '<a class="page-link" href="javascript:void(0)" ';
                                                               html += 'onclick="loadFeedbackPage(' + (currentPage + 1) + ', ' + (starFilter || 0) + ')">Next</a>';
                                                               html += '</li>';

                                                               html += '</ul></nav>';
                                                               paginationContainer.innerHTML = html;
                                                           }

                                                           function updateActiveButton(starRating) {
                                                               const buttons = document.querySelectorAll('.star-group button');

                                                               buttons.forEach(function (btn) {
                                                                   btn.classList.remove('btn-primary');
                                                                   btn.classList.add('btn-outline-primary');
                                                               });

                                                               if (starRating >= 0 && starRating < buttons.length) {
                                                                   buttons[starRating].classList.remove('btn-outline-primary');
                                                                   buttons[starRating].classList.add('btn-primary');
                                                               }
                                                           }

                                                           function testConnection() {
                                                               const url = '../client/getFeedbacksFiltered.htm?productID=' + productID + '&starFilter=&page=1';
                                                               fetch(url)
                                                                       .then(response => response.text())
                                                                       .then(responseText => {
                                                                           try {
                                                                               JSON.parse(responseText);
                                                                           } catch (e) {
                                                                               console.error('Test failed - not valid JSON:', responseText);
                                                                           }
                                                                       })
                                                                       .catch(error => console.error('Test connection failed:', error));
                                                           }

                                                           document.addEventListener('DOMContentLoaded', function () {
                                                               if (!productID || productID === '') {
                                                                   return;
                                                               }
                                                               updateActiveButton(currentStarFilter);
                                                               setTimeout(testConnection, 1000);
                                                           });

                                                           function increaseQuantity() {
                                                               let input = document.getElementById("quantity");
                                                               let max = parseInt(input.getAttribute("data-max"));
                                                               let current = parseInt(input.value);

                                                               if (current < max) {
                                                                   input.value = current + 1;
                                                               }
                                                               if (current + 1 >= max) {
                                                                   document.getElementById("btnUp").disabled = true;
                                                               }
                                                           }

                                                           function decreaseQuantity() {
                                                               let input = document.getElementById("quantity");
                                                               let current = parseInt(input.value);

                                                               if (current > 1) {
                                                                   input.value = current - 1;
                                                                   document.getElementById("btnUp").disabled = false;
                                                               }
                                                           }

                                                           function changeImage(imageSrc) {
                                                               document.getElementById("mainImage").src = imageSrc;
                                                           }
        </script>

        <jsp:include page="livechat.jsp" />
    </body>

</html>
