<%-- 
    Document   : index
    Created on : Aug 27, 2024, 2:41:18 PM
    Author     : Win10
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="Responsive Admin &amp; Dashboard Template based on Bootstrap 5">
        <meta name="author" content="AdminKit">
        <meta name="keywords" content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" href="../../admin/static/img/icons/icon-48x48.png" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <link rel="canonical" href="https://demo-basic.adminkit.io/pages-blank.html" />
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">

        <title>NestMart - Employee Page</title>

        <link rel="stylesheet" href="../assets/admin/css/app.css" />
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
                margin-top: -22px;
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
                vertical-align: middle;
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
        </style>
    </head>
    <body>
        <c:if test="${param.sessionExpired}">
            <div style="color: red;">
                Your session has expired. Please log in again.
            </div>
        </c:if>
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

                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="index.htm">
                                <i class="align-middle me-2" data-feather="users"></i> 
                                <span class="align-middle">Account</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="order.htm">
                                <i class="align-middle me-2" data-feather="clipboard"></i> 
                                <span class="align-middle">Orders</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="returnRequests.htm">
                                <i class="align-middle me-2" data-feather="corner-down-left"></i> 
                                <span class="align-middle">Return Request</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="viewFeedbackEmp.htm">
                                <i class="align-middle me-2" data-feather="file-text"></i> 
                                <span class="align-middle">Feedback</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="viewNotifications.htm">
                                <i class="align-middle me-2" data-feather="bell"></i> 
                                <span class="align-middle">Notification</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="vouchers.htm">
                                <i class="align-middle me-2" data-feather="percent"></i> 
                                <span class="align-middle">Vouchers</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="accountVouchers.htm">
                                <i class="align-middle me-2" data-feather="percent"></i> 
                                <span class="align-middle">Specific Vouchers</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="supportmessage.htm">
                                <i class="align-middle me-2" data-feather="message-square"></i> 
                                <span class="align-middle">Support Message</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="scheduleEmp.htm">
                                <i class="align-middle me-2" data-feather="calendar"></i> 
                                <span class="align-middle">Schedule</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="salary.htm">
                                <i class="align-middle me-2" data-feather="dollar-sign"></i> 
                                <span class="align-middle">Salary</span>
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
                                    <a class="dropdown-item" href="../employee/accountInformation.htm">
                                        <i class="fa fa-user"></i> Account Information
                                    </a>
                                    <a class="dropdown-item" href="../employee/changePassword.htm">
                                        <i class="fa fa-user"></i> Change Password
                                    </a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="../logout.htm">
                                        <i class="align-middle me-1" data-feather="log-out"></i> Log out
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
                <main class="content">
                    <div class="container mt-4">
                        <!-- Search and Buttons -->
                        <div class="search-container">
                            <form action="index.htm" method="get" class="d-flex align-items-center w-100">
                                <div class="search-container flex-grow-1">
                                    <input type="text" name="keyword" class="form-control search-input" placeholder="Search..." value="${keyword != null ? keyword : ''}">
                                    <button class="btn search-button" type="submit">
                                        <i class="align-middle" data-feather="search"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                        <c:if test="${not empty keyword}">
                            <c:choose>
                                <c:when test="${not empty listAccount}">

                                    <p>Found <b>${totalAccounts}</b> results.</p>

                                </c:when>

                                <c:otherwise>
                                    <p>No result for "<b>${keyword}</b>".</p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <table class="table table-hover my-0" id="accountTable">
                            <thead>
                                <tr>
                                    <th>Account ID</th>
                                    <th>Full Name</th>
                                    <th>Phone Number</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Gender</th>
                                    <th>Birthday</th>
                                    <th>Address</th>
                                    <th>Hourly Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="account" items="${listAccount}">
                                    <tr>
                                        <td><c:out value="${account.accountID}" /></td>
                                        <td><c:out value="${account.fullName}" /></td>
                                        <td><c:out value="${account.phoneNumber}" /></td>
                                        <td><c:out value="${account.email}" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${account.role == 1}">Admin</c:when>
                                                <c:when test="${account.role == 2}">Employee</c:when>
                                                <c:when test="${account.role == 3}">Shipper</c:when>
                                                <c:when test="${account.role == 4}">Customer</c:when>
                                                <c:otherwise>Unknown</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><c:out value="${account.gender}" /></td>
                                        <td><fmt:formatDate value="${account.birthday}" pattern="yyyy/MM/dd" /></td>
                                        <td><c:out value="${account.address}" /></td>
                                        <td><c:out value="${account.hourlyRate}" /></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty listAccount}">
                                    <tr>
                                        <td colspan="10">No account found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                        <div class="pagination-container">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:set var="maxDisplayPages" value="5" />
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage - 1}">Previous</a>
                                    </li>
                                    <c:choose>
                                        <c:when test="${totalPages <= maxDisplayPages}">
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="?page=${i}">${i}</a>
                                                </li>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="startPage" value="${currentPage - (maxDisplayPages / 2)}" />
                                            <c:set var="endPage" value="${currentPage + (maxDisplayPages / 2)}" />
                                            <c:if test="${startPage < 1}">
                                                <c:set var="endPage" value="${endPage + (1 - startPage)}" />
                                                <c:set var="startPage" value="1" />
                                            </c:if>
                                            <c:if test="${endPage > totalPages}">
                                                <c:set var="startPage" value="${startPage - (endPage - totalPages)}" />
                                                <c:set var="endPage" value="${totalPages}" />
                                            </c:if>
                                            <c:if test="${startPage > 1}">
                                                <li class="page-item">
                                                    <a class="page-link" href="?page=1">1</a>
                                                </li>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                </c:if>
                                                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="?page=${i}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <c:if test="${endPage < totalPages}">
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                <li class="page-item">
                                                    <a class="page-link" href="?page=${totalPages}">${totalPages}</a>
                                                </li>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage + 1}">Next</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>

                    </div>

                </main>


            </div>
        </div>

        <script src="../assets/admin/js/app.js"></script>
        <script>
            feather.replace();
        </script>
        <script>document.getElementById('accountInfoModal').addEventListener('show.bs.modal', function (event) {
                fetch('/admin/information')
                        .then(response => response.json())
                        .then(data => {
                            document.getElementById('email').value = data.email;
                            document.getElementById('fullName').value = data.fullName;
                            document.getElementById('phoneNumber').value = data.phoneNumber;
                            document.getElementById('address').value = data.address;
                        });
            });
        </script>
    </body>
</html>
