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

        <title>NestMart - Brand</title>

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
            .table-actions {
                white-space: nowrap;
            }
            .table-actions > * {
                margin-right: 5px;
            }
            .btn-update {
                color: #ffc107;
            }
            .btn-delete {
                color: #dc3545;
            }
            .badge {
                padding: 0.5em 0.75em;
                display: inline-flex;
                align-items: center;
                gap: 5px;
            }
            .badge i {
                width: 16px;
                height: 16px;
            }
             .table td, .table th {
    padding: 15px 12px; /* Increase padding from default */
    vertical-align: middle;
}

/* Ensure table takes full height and distributes space evenly */
.table {
    margin-bottom: 30px; /* Add more space after table */
    min-height: 600px; /* Set minimum height for consistent layout */
}

/* Improve container spacing */
.container.mt-4 {
    min-height: calc(100vh - 200px); /* Take up more vertical space */
    display: flex;
    flex-direction: column;
}

/* Make table container flexible to fill space */
.table-responsive {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
}

/* Better spacing for action buttons - spread them out more */
.btn-container {
    display: flex;
    gap: 20px; /* Increase gap between buttons */
    align-items: center;
    justify-content: flex-start;
    width: 100%; /* Take full width of cell */
}

/* Make action buttons larger */
.btn-update, .btn-delete {
    padding: 8px 12px;
    border-radius: 6px;
    border: 1px solid transparent;
    transition: all 0.2s;
}

.btn-update:hover, .btn-delete:hover {
    transform: scale(1.1);
}

/* Improve badge spacing */
.badge {
    padding: 8px 12px; /* Increase badge padding */
    font-size: 0.85em;
    display: inline-flex;
    align-items: center;
    gap: 6px;
}

/* Better pagination positioning */
.pagination-container {
    margin-top: auto; /* Push pagination to bottom */
    padding-top: 30px;
    border-top: 1px solid #e9ecef; /* Add subtle separator */
}

/* Improve search container spacing */
.search-container {
    margin-bottom: 30px; /* Increase space below search */
}

/* Add more space between rows */
.table tbody tr {
    height: 60px; /* Set consistent row height */
}

/* Empty state styling */
.table tbody tr td[colspan] {
    text-align: center;
    padding: 60px 20px;
    color: #6c757d;
    font-style: italic;
    height: 200px; /* Give empty state more height */
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

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="products.htm">
                                <i class="align-middle" data-feather="box"></i> <span class="align-middle">Product</span>
                            </a>
                        </li>

                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="brand.htm">
                                <i class="align-middle" data-feather="bold"></i> <span class="align-middle">Brand</span>
                            </a>
                        </li>

                        <li class="sidebar-item  ">
                            <a class="sidebar-link" href="categories.htm">
                                <i class="align-middle" data-feather="list"></i> <span class="align-middle">Category</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="categoryDetail.htm">
                                <i class="align-middle" data-feather="clipboard"></i> <span class="align-middle">Category Detail</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
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

                        <div class="search-container">
                            <form action="brand.htm" method="get" class="search-form">
                                <input type="text" name="keyword" class="form-control search-input" placeholder="Search by name" value="${keyword}">
                                <button type="submit" class="btn search-button">
                                    <i class="align-middle" data-feather="search"></i>
                                </button>
                            </form>

                            <div class="icon-container">
                                <button class="btn btn-add" onclick="window.location.href = 'brandCreate.htm'">
                                    <i data-feather="plus"></i>
                                </button>
                            </div>
                        </div>
                        <c:set var="suggestion" value="${closestMatch != null && !closestMatch.equalsIgnoreCase(keyword) ? closestMatch : null}" />

                        <c:if test="${not empty suggestion}">
                            <p>Did you mean <a href="brand.htm?keyword=${suggestion}">${suggestion}</a> ?</p>
                        </c:if>

                        <c:if test="${not empty keyword}">
                            <c:choose>
                                <c:when test="${not empty listBrands}">
                                    <p>Found <b>${totalBrands}</b> results.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>No result for "<b>${keyword}</b>".</p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>

<div class="table-responsive">

                        <table class="table table-hover my-0" id="accountTable">
                            <thead>
                                <tr>
                                    <th>Brand ID</th>
                                    <th>Brand Name</th>
                                    <th>Description</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="brand" items="${listBrands}">
                                    <tr>
                                        <td><c:out value="${brand.brandID}" /></td>
                                        <td><c:out value="${brand.brandName}" /></td>
                                        <td><c:out value="${brand.description}" /></td>
                                        <td class="table-actions">
                                                                                        <div class="btn-container">
                                            <a href="brandUpdate.htm?brandID=${brand.brandID}" class="btn btn-update" title="Edit">
                                                <i data-feather="edit"></i>
                                            </a>
                                            <c:choose>
                                                <c:when test="${brand.hasProducts}">
                                                    <span class="badge bg-warning text-dark" title="This brand has associated products">
                                                        <i data-feather="alert-circle"></i>
                                                        Brand in use
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="brandDelete.htm?brandID=${brand.brandID}" 
                                                       class="btn btn-delete" 
                                                       title="Delete" 
                                                       onclick="return confirm('Are you sure you want to delete this brand?');">
                                                        <i data-feather="trash-2"></i>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                                                                        </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty listBrands}">
                                    <tr>
                                        <td colspan="4">No brands found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
</div>
                        <div class="pagination-container">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:set var="maxDisplayPages" value="5" />

                                    <!-- Nút Previous -->
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage - 1}&pageSize=${pageSize}&keyword=${keyword}">Previous</a>
                                    </li>

                                    <!-- Nếu tổng trang ít -->
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
        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script> <!-- Feather Icons JS -->
        <script>
                                                           document.addEventListener("DOMContentLoaded", function () {
                                                               feather.replace();
                                                           });
        </script>
        <script src="../assets/admin/js/app.js"></script>
    </body>
</html>
