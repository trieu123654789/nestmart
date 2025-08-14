


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <link rel="canonical" href="https://demo-basic.adminkit.io/pages-blank.html" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">

        <title>NestMart - Product Update</title>

        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
        <style>
            .search-container {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
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
            .table-actions {
                position: relative;
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 5px;
            }
            .table-actions {
                display: flex;
                gap: 5px;
                justify-content: center;
                align-items: center;
            }
            .table-actions .btn {
                background-color: transparent;
                border: none;
                cursor: pointer;
                font-size: 20.5px;
                padding: 5px;
                transition: background-color 0.3s, box-shadow 0.3s;
            }
            .table-actions .btn-update {
                color: #007bff;
            }
            .table-actions .btn-delete {
                color: #dc3545;
            }
            .table-actions .btn:hover {
                background-color: #f0f0f0;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
            }
            .table-actions .btn-update:hover {
                color: #0056b3;
            }
            .table-actions .btn-delete:hover {
                color: #c82333;
            }
            tr {
                position: relative;
            }
            .table td {
                vertical-align: middle; /* Center-align text vertically in table cells */
            }
            .image-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
                gap: 15px;
                padding: 10px;
            }

            .image-item {
                display: flex;
                flex-direction: column;
                align-items: center;
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 8px;
                background-color: #f9f9f9;
                transition: box-shadow 0.3s ease;
            }

            .image-item:hover {
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }

            .image-item img {
                width: 100%;
                height: 100px;
                object-fit: cover;
                border-radius: 4px;
                margin-bottom: 8px;
            }

            .delete-checkbox {
                display: flex;
                align-items: center;
                margin-top: 4px;
            }

            /* Ẩn checkbox mặc định */
            .delete-checkbox input[type="checkbox"] {
                display: none;
            }

            /* Tạo checkbox tùy chỉnh */
            .delete-checkbox label {
                display: inline-flex;
                align-items: center;
                position: relative;
                padding-left: 25px;
                cursor: pointer;
                font-size: 12px;
                color: #333;
                user-select: none;
            }

            .delete-checkbox label::before {
                content: '';
                position: absolute;
                left: 0;
                top: 50%;
                transform: translateY(-50%);
                width: 16px;
                height: 16px;
                border: 2px solid #007bff;
                border-radius: 3px;
                transition: all 0.3s ease;
            }

            /* Tạo dấu check */
            .delete-checkbox label::after {
                content: '✓';
                position: absolute;
                left: 4px;
                top: 50%;
                transform: translateY(-50%);
                font-size: 12px;
                color: #fff;
                opacity: 0;
                transition: opacity 0.2s ease;
            }

            /* Khi checkbox được chọn */
            .delete-checkbox input[type="checkbox"]:checked + label::before {
                background-color: #007bff;
            }

            .delete-checkbox input[type="checkbox"]:checked + label::after {
                opacity: 1;
            }

            /* Hiệu ứng hover */
            .delete-checkbox label:hover::before {
                border-color: #0056b3;
            }

            /* Responsive adjustments */
            @media (max-width: 768px) {
                .image-grid {
                    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
                    gap: 10px;
                }

                .image-item img {
                    height: 80px;
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Spring Web MVC project</title>
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
                <nav class="navbar navbar-expand navbar-light navbar-bg" style="padding: 14px 22px">
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
                    <div class="container mt-5">
                        <h2>Update Product</h2>
                        <form action="../admin/productUpdate.htm" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="productID" value="${product.productID}" />

                            <table class="table table-bordered">
                                <tr>
                                    <th>Field</th>
                                    <th>Value</th>
                                </tr>
                                <tr>
                                    <td>Product Name</td>
                                    <td>
                                        <input type="text" class="form-control" id="productName" name="productName" value="${product.productName}" required>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Category</td>
                                    <td>
                                        <select class="form-control" id="categoryID" name="categoryID" required>
                                            <c:forEach var="category" items="${listCategories}">
                                                <option value="${category.categoryID}" ${category.categoryID == product.categoryID ? 'selected' : ''}>
                                                    ${category.categoryName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Brand</td>
                                    <td>
                                        <select class="form-control" id="brandID" name="brandID" required>
                                            <c:forEach var="brand" items="${listBrands}">
                                                <option value="${brand.brandID}" ${brand.brandID == product.brandID ? 'selected' : ''}>
                                                    ${brand.brandName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Description</td>
                                    <td>
                                        <textarea class="form-control" id="productDescription" name="productDescription" rows="4" required>${product.productDescription}</textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Unit Price</td>
                                    <td>
                                        <input type="text" class="form-control" id="unitPrice" name="unitPrice"
                                               value="<fmt:formatNumber value="${product.unitPrice}" pattern="#,##0"/>" required>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Discount</td>
                                    <td>
                                        <input type="number" class="form-control" id="discount" name="discount" value="${product.discount}" step="0.01">
                                    </td>
                                </tr>

                                <tr>
                                    <td>Current Images</td>
                                    <td>
                                        <div class="image-grid">
                                            <c:forEach var="image" items="${product.images}" varStatus="status">
                                                <div class="image-item">
                                                    <img src="../assets/admin/images/uploads/products/${image.images}" 
                                                         alt="Product Image">
                                                    <div class="delete-checkbox">
                                                        <input type="checkbox" name="imagesToDelete" 
                                                               value="${image.images}" id="deleteImage${status.index}">
                                                        <label for="deleteImage${status.index}">Delete</label>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Number of New Images</td>
                                    <td>
                                        <select id="imageCount" class="form-control" onchange="generateImageInputs()">
                                            <c:forEach var="i" begin="1" end="10">
                                                <option value="${i}">${i}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr id="imageUploadRow">
                                    <td>New Images</td>
                                    <td id="imageUploadContainer">
                                        <input type="file" class="form-control" name="imageFiles" >
                                    </td>
                                </tr>
                                <tr>
                                    <td>Date Added</td>
                                    <td>
                                        <input type="datetime-local" class="form-control" id="dateAdded" name="dateAdded"
                                               value="${product.formattedDateAdded}" 
                                               required>

                                    </td>
                                </tr>
                            </table>

                            <button type="submit" class="btn btn-primary">Update Product</button>
                            <a href="../admin/products.htm" class="btn btn-secondary">Cancel</a>
                        </form>

                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success mt-3">
                                <c:out value="${successMessage}" />
                            </div>
                        </c:if>

                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger mt-3">
                                <c:out value="${errorMessage}" />
                            </div>
                        </c:if>

                    </div>
                </main>

            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script> <!-- Feather Icons JS -->

        <script type="text/javascript">
                                            function generateImageInputs() {
                                                var imageCount = document.getElementById('imageCount').value;
                                                var container = document.getElementById('imageUploadContainer');
                                                container.innerHTML = '';

                                                for (var i = 0; i < imageCount; i++) {
                                                    var input = document.createElement('input');
                                                    input.type = 'file';
                                                    input.name = 'imageFiles';
                                                    input.className = 'form-control mt-2';
                                                    container.appendChild(input);
                                                }
                                            }
        </script>
        <script>
            feather.replace();
        </script>
        <script src="../assets/admin/js/app.js"></script>
    </body>
</html>

