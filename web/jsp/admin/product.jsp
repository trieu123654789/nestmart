<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="Responsive Admin &amp; Dashboard Template based on Bootstrap 5">
        <meta name="author" content="AdminKit">
        <meta name="keywords" content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <link rel="canonical" href="https://demo-basic.adminkit.io/pages-blank.html" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">
        <title>NestMart - Product</title>

        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">

        <style>
            .search-container {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
                width: 100%;
            }

            .search-form {
                display: flex;
                align-items: center;
                width: 100%;
            }

            .search-input {
                flex: 1;
                height: 45px;
                padding: 0 10px;
                border-radius: 20px 0 0 20px;
                border: 1px solid #ced4da;
                border-right: none;
                font-size: 16px;
            }

            .search-button {
                height: 45px;
                width: 65px;
                border-radius: 0 20px 20px 0;
                border: 1px solid #ced4da;
                border-left: none;
                background-color: #f1f1f1;
                color: #333;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                margin-right: 15px;
            }

            .search-button i {
                font-size: 20px;
            }

            .search-button:hover {
                background-color: #e0e0e0;
            }

            .icon-container {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-left: auto; /* Đẩy các nút về bên phải */
            }
            .icon-container .btn {
                background-color: #f1f1f1;
                border: none;
                color: #333;
                width: 50px;
                height: 50px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                font-size: 24px;
                transition: background-color 0.3s, box-shadow 0.3s;
            }
            .icon-container .btn:hover {
                background-color: #e0e0e0;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            .btn-container {
                display: flex;
                gap: 10px; /* Khoảng cách giữa các nút */
            }

            .btn {
                background-color: transparent;
                border: none;
                cursor: pointer;
                font-size: 20.5px;
                padding: 5px;
                transition: background-color 0.3s, box-shadow 0.3s;
            }

            .btn-update {
                color: #007bff;
            }

            .btn-delete {
                color: #dc3545;
            }

            .btn:hover {
                background-color: #f0f0f0;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
            }

            .btn-update:hover {
                color: #0056b3;
            }

            .btn-delete:hover {
                color: #c82333;
            }

            tr {
                position: relative;
            }
            .table td {
                vertical-align: middle; /* Center-align text vertically in table cells */
            }
            .image-container {
                position: relative;
                display: inline-block;
            }

            .img-thumbnail {
                display: block;
            }

            .image-count {
                position: absolute;
                top: 5px;
                right: 5px;
                background-color: rgba(0, 0, 0, 0.6);
                color: white;
                padding: 2px 6px;
                border-radius: 50%;
                font-size: 14px;
                font-weight: bold;
            }

            /*chuyentrang*/
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                margin-top: 20px;
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
            /* Chỉ báo không thể xóa */
            .cannot-delete-indicator {
                background-color: #ffcccb; /* Màu nền đỏ nhạt */
                color: #d32f2f; /* Màu chữ đỏ đậm */
                padding: 8px 2px;
                border-radius: 4px;
                font-size: 10px;
                cursor: not-allowed;
                display: inline-flex;
                align-items: center;
                border: 1px solid #d32f2f; /* Viền đỏ */
            }

            /* Icon styles */
            .btn i, .cannot-delete-indicator i {
                margin-right: 5px;
            }

            /* Tooltip styles */
            .btn, .cannot-delete-indicator {
                position: relative;
            }

            .btn::after, .cannot-delete-indicator::after {
                content: attr(title);
                position: absolute;
                bottom: 100%;
                left: 50%;
                transform: translateX(-50%);
                background-color: #333;
                color: white;
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 12px;
                white-space: nowrap;
                opacity: 0;
                visibility: hidden;
                transition: opacity 0.3s, visibility 0.3s;
            }

            .btn:hover::after, .cannot-delete-indicator:hover::after {
                opacity: 1;
                visibility: visible;
            }
        </style>
    </head>

    <body>
        <div class="wrapper">
            <nav id="sidebar" class="sidebar js-sidebar">
                <div class="sidebar-content js-simplebar">
                    <div class="sidebar-brand">
                        <span class="align-middle">Nestmart</span>
                    </div>

                    <ul class="sidebar-nav">
                        <li class="sidebar-header">
                            Pages
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="account.htm" >
                                <i class="align-middle me-2" data-feather="users"></i> <span class="align-middle">Account</span>

                            </a>
                        </li>

                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="products.htm">
                                <i class="align-middle" data-feather="box"></i> <span class="align-middle">Product</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="brand.htm">
                                <i class="align-middle" data-feather="bold"></i> <span class="align-middle">Brand</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="categories.htm">
                                <i class="align-middle" data-feather="list"></i> <span class="align-middle">Category</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="categoryDetail.htm">
                                <i class="align-middle" data-feather="clipboard"></i> <span class="align-middle">Category Detail</span>
                            </a>
                        </li>

                        <li class="sidebar-item ">
                            <a class="sidebar-link" href="discount.htm">
                                <i class="align-middle" data-feather="check-circle"></i> <span class="align-middle">Discount</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="offers.htm">
                                <i class="align-middle" data-feather="percent"></i> <span class="align-middle">Offers</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="schedule.htm">
                                <i class="align-middle" data-feather="calendar"></i> <span class="align-middle">Schedule</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="viewFeedbackAd.htm">
                                <i class="align-middle" data-feather="feather"></i> <span class="align-middle">Feedback</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="salary.htm">
                                <i class="align-middle" data-feather="user-check"></i> <span class="align-middle">Salary</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="notifications.htm">
                                <i class="align-middle" data-feather="navigation"></i> <span class="align-middle">Notification</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="salesReport.htm" >
                                <i class="align-middle me-2" data-feather="pie-chart"></i> <span class="align-middle">Sale Report</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <div class="main">
                <nav class="navbar navbar-expand navbar-light navbar-bg">
                    <a class="sidebar-toggle js-sidebar-toggle">
                        <i class="hamburger align-self-center"></i>
                    </a>

                    <div class="navbar-collapse collapse">
                        <ul class="navbar-nav navbar-align">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-bs-toggle="dropdown">
                                    <span class="text-dark">${sessionScope.email}</span>
                                </a>
                                <div class="dropdown-menu dropdown-menu-end">
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/accountInformation.htm">
                                        <i class="fa fa-user"></i> Account Information
                                    </a>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/changePassword.htm">
                                        <i class="fa fa-user"></i> Change Password
                                    </a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/logout.htm">
                                        <i class="align-middle me-1" data-feather="log-out"></i> Log out
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>

                <main class="content">
                    <div class="container mt-4">
                        <div class="search-container">
                            <form action="../admin/products.htm" method="get" class="search-form">
                                <input type="text" name="keyword" class="form-control search-input" placeholder="Search product id, product name, category or brand" value="${keyword}">
                                <button type="submit" class="btn search-button">
                                    <i class="align-middle" data-feather="search"></i>
                                </button>
                            </form>

                                <div class="icon-container">
                                    <button class="btn btn-add" onclick="window.location.href = 'productCreate.htm'">
                                        <i data-feather="plus"></i>
                                    </button>
                                </div>
                        </div>
                        <c:if test="${not empty keyword}">
                            <c:choose>
                                <c:when test="${not empty listProducts}">
                                    <p>Found <b>${totalProducts}</b> results.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>No result for "<b>${keyword}</b>".</p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <table class="table table-hover my-0" id="productTable">
                            <thead>
                                <tr>
                                    <th>Product ID</th>
                                    <th>Category</th>
                                    <th>Brand</th>
                                    <th>Product Name</th>
                                    <th>Description</th>
                                    <th>Unit Price</th>
                                    <th>Image</th>
                                    <th>Date Added</th>
                                    <th>Discount</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${listProducts}">
                                    <tr>
                                        <td><c:out value="${product.productID}" /></td>
                                        <td><c:out value="${categoryNames[product.categoryID]}" /></td>
                                        <td><c:out value="${brandNames[product.brandID]}" /></td>
                                        <td><c:out value="${product.productName}" /></td>
                                        <td><c:out value="${product.productDescription}" /></td>
                                        <td><fmt:formatNumber value="${product.unitPrice}" type="number" groupingUsed="true" minFractionDigits="0" maxFractionDigits="2" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty product.images}">
                                                    <c:set var="imageCount" value="${fn:length(product.images)}" />
                                                    <c:forEach var="image" items="${product.images}" varStatus="status">
                                                        <c:if test="${status.index == 0}">
                                                            <div class="image-container">
                                                                <img src="../assets/admin/images/uploads/products/${image.images}"
                                                                     alt="Product Image"
                                                                     class="img-thumbnail"
                                                                     loading="lazy">
                                                                <c:if test="${imageCount > 1}">
                                                                    <span class="image-count">+${imageCount - 1}</span>
                                                                </c:if>
                                                            </div>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    No image available
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>${product.formattedDateAdded}</td>
                                        <td><fmt:formatNumber value="${product.discount}" type="number" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td>
                                            <div class="btn-container">
                                                <a href="../admin/productUpdate.htm?productID=${product.productID}" class="btn btn-update" title="Edit">
                                                    <i data-feather="edit-2"></i>
                                                </a>
                                                <c:choose>
                                                    <c:when test="${canDeleteMap[product.productID]}">
                                                        <a href="../admin/productDelete.htm?productID=${product.productID}" 
                                                           class="btn btn-delete" 
                                                           title="Delete" 
                                                           onclick="return confirm('Are you sure you want to delete this product?');">
                                                            <i data-feather="trash-2"></i>
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="cannot-delete-indicator" title="Product has been sold and cannot be deleted">Product was sold</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="pagination-container">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:set var="maxDisplayPages" value="5" />

                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage - 1}&pageSize=${pageSize}&keyword=${keyword}">Previous</a>
                                    </li>

                                    <c:if test="${totalPages <= maxDisplayPages + 2}">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?page=${i}&pageSize=${pageSize}&keyword=${keyword}">${i}</a>
                                            </li>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${totalPages > maxDisplayPages + 2}">
                                        <c:choose>
                                            <c:when test="${currentPage <= maxDisplayPages - 2}">
                                                <c:forEach begin="1" end="${maxDisplayPages}" var="i">
                                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                        <a class="page-link" href="?page=${i}&pageSize=${pageSize}&keyword=${keyword}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                <li class="page-item"><a class="page-link" href="?page=${totalPages - 1}&pageSize=${pageSize}&keyword=${keyword}">${totalPages - 1}</a></li>
                                                <li class="page-item"><a class="page-link" href="?page=${totalPages}&pageSize=${pageSize}&keyword=${keyword}">${totalPages}</a></li>
                                                </c:when>
                                                <c:when test="${currentPage >= totalPages - (maxDisplayPages - 3)}">
                                                <li class="page-item"><a class="page-link" href="?page=1&pageSize=${pageSize}&keyword=${keyword}">1</a></li>
                                                <li class="page-item"><a class="page-link" href="?page=2&pageSize=${pageSize}&keyword=${keyword}">2</a></li>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                    <c:forEach begin="${totalPages - (maxDisplayPages - 1)}" end="${totalPages}" var="i">
                                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                        <a class="page-link" href="?page=${i}&pageSize=${pageSize}&keyword=${keyword}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="page-item"><a class="page-link" href="?page=1&pageSize=${pageSize}&keyword=${keyword}">1</a></li>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                    <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                        <a class="page-link" href="?page=${i}&pageSize=${pageSize}&keyword=${keyword}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                <li class="page-item"><a class="page-link" href="?page=${totalPages}&pageSize=${pageSize}&keyword=${keyword}">${totalPages}</a></li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>

                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage + 1}&pageSize=${pageSize}&keyword=${keyword}">Next</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </main>

            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.29.2/feather.min.js"></script>
        <script>
                                                               feather.replace();
        </script>
        <script>
            function formatNumber(value) {
                let parts = value.split('.');
                let integerPart = parts[0].replace(/\D/g, '');
                let decimalPart = parts[1] || '';

                integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

                decimalPart = decimalPart.slice(0, 3);

                return decimalPart ? integerPart + '.' + decimalPart : integerPart;
            }

            function onInputChange(event) {
                let input = event.target;
                let cursorPosition = input.selectionStart;
                let oldValue = input.value;
                let newValue = formatNumber(input.value);

                input.value = newValue;

                let cursorOffset = newValue.length - oldValue.length;
                input.setSelectionRange(cursorPosition + cursorOffset, cursorPosition + cursorOffset);
            }

            document.addEventListener("DOMContentLoaded", function () {
                let unitPriceInput = document.getElementById("unitPrice");
                unitPriceInput.addEventListener("input", onInputChange);
            });
        </script>

        <script src="../assets/admin/js/app.js"></script>
    </body>
</html>
