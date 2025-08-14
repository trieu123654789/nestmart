<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

        <title>NestMart - Product Create</title>

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
                    <div class="container mt-4">
                        <form action="../admin/productCreate.htm" method="post" enctype="multipart/form-data">
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger">${errorMessage}</div>
                            </c:if>

                            <table class="table table-hover my-0">
                                <tbody>
                                    <tr>
                                        <td><label for="productID">Product ID:</label></td>
                                        <td><input type="text" class="form-control" id="productID" name="productID" value="${product.productID}" /></td>
                                    </tr>
                                    <tr>
                                        <td><label for="categoryID">Category:</label></td>
                                        <td>
                                            <select class="form-control" id="categoryID" name="categoryID" required>
                                                <c:forEach var="category" items="${listCategories}">
                                                    <option value="${category.categoryID}" ${category.categoryID == product.categoryID ? 'selected' : ''}>${category.categoryName}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="brandID">Brand:</label></td>
                                        <td>
                                            <select class="form-control" id="brandID" name="brandID" required>
                                                <c:forEach var="brand" items="${listBrands}">
                                                    <option value="${brand.brandID}" ${brand.brandID == product.brandID ? 'selected' : ''}>${brand.brandName}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="productName">Product Name:</label></td>
                                        <td><input type="text" class="form-control" id="productName" name="productName" value="${product.productName}" /></td>
                                    </tr>
                                    <tr>
                                        <td><label for="productDescription">Description:</label></td>
                                        <td><textarea class="form-control" id="productDescription" name="productDescription" rows="4">${product.productDescription}</textarea></td>
                                    </tr>
                                    <tr>
                                        <td><label for="unitPrice">Unit Price:</label></td>
                                        <td><input type="text" class="form-control" id="unitPrice" name="unitPrice" value="${product.unitPrice}" required></td>
                                    </tr>

                                    <tr>
                                        <td><label for="imageCount">Number of Images:</label></td>
                                        <td>
                                            <select class="form-control" id="imageCount" name="imageCount" onchange="generateImageInputs(this.value)" required>
                                                <option value="1">1</option>
                                                <option value="2">2</option>
                                                <option value="3">3</option>
                                                <option value="4">4</option>
                                                <option value="5">5</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr id="imageInputsContainer">
                                        <td colspan="2">
                                            <div id="imageInputs">
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="discount">Discount:</label></td>
                                        <td><input type="number" step="0.01" class="form-control" id="discount" name="discount" value="${product.discount}" /></td>
                                    </tr>

                                    <tr>
                                        <td colspan="2" class="text-right">
                                            <button type="submit" class="btn btn-primary">Create Product</button>
                                            <a href="../admin/products.htm" class="btn btn-secondary">Cancel</a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </main>

            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script> <!-- Feather Icons JS -->
        <script>
                                                feather.replace();
        </script>
        <script>
            function formatNumber(value) {
                value = value.replace(/[^\d.]/g, '');

                let parts = value.split('.');
                let integerPart = parts[0];
                let decimalPart = parts.length > 1 ? '.' + parts[1].slice(0, 3) : '';

                integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                return integerPart + decimalPart;
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

                if (unitPriceInput.value) {
                    unitPriceInput.value = formatNumber(unitPriceInput.value);
                }
            });
            function generateImageInputs(count) {
                var container = document.getElementById("imageInputs");
                container.innerHTML = ""; 

                for (var i = 1; i <= count; i++) {
                    var inputDiv = document.createElement("div");
                    inputDiv.className = "form-group";

                    var label = document.createElement("label");
                    label.setAttribute("for", "imageFile" + i);
                    label.textContent = "Image " + i + ":";

                    var input = document.createElement("input");
                    input.type = "file";
                    input.className = "form-control";
                    input.id = "imageFile" + i;
                    input.name = "imageFiles"; // Ensure this is the same for all files
                    input.accept = "image/*";

                    inputDiv.appendChild(label);
                    inputDiv.appendChild(input);
                    container.appendChild(inputDiv);
                }
            }

            window.onload = function () {
                generateImageInputs(1);
            };
        </script>
        <script src="../assets/admin/js/app.js"></script>
    </body>
</html>
