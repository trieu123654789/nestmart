<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">

        <title>NestMart - Feedbacks</title>

        <link href="../assets/admin/css/app.css" rel="stylesheet"> 
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
                margin-left: auto;
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
                gap: 10px;
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
                vertical-align: middle;
            }

            /* Feedback image styles - THAY THẾ CSS CŨ */
            .feedback-images {
                display: flex;
                align-items: center;
                gap: 5px;
                max-width: 200px;
            }

            .main-image-container {
                position: relative;
                display: inline-block;
                cursor: pointer;
            }

            .main-image {
                width: 60px;
                height: 60px;
                object-fit: cover;
                border-radius: 4px;
                border: 2px solid #ddd;
                transition: transform 0.2s;
            }

            .main-image:hover {
                transform: scale(1.05);
                border-color: #007bff;
            }

            .image-count-badge {
                position: absolute;
                top: -5px;
                right: -5px;
                background-color: #007bff;
                color: white;
                padding: 2px 6px;
                border-radius: 10px;
                font-size: 10px;
                font-weight: bold;
                min-width: 18px;
                text-align: center;
            }

            .no-images {
                color: #6c757d;
                font-style: italic;
                font-size: 12px;
            }

            /* Small Modal styles */
            .image-modal {
                display: none;
                position: fixed;
                z-index: 9999;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.7);
            }

            .modal-content {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background: white;
                border-radius: 10px;
                width: 450px;
                max-width: 90vw;
                box-shadow: 0 10px 25px rgba(0,0,0,0.3);
                animation: modalSlideIn 0.3s ease-out;
            }

            @keyframes modalSlideIn {
                from {
                    opacity: 0;
                    transform: translate(-50%, -60%);
                }
                to {
                    opacity: 1;
                    transform: translate(-50%, -50%);
                }
            }

            .modal-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 15px 20px;
                border-bottom: 1px solid #eee;
                border-radius: 10px 10px 0 0;
                background: #f8f9fa;
            }

            .modal-title {
                font-size: 16px;
                font-weight: 600;
                color: #333;
            }

            .close {
                color: #666;
                font-size: 24px;
                font-weight: bold;
                cursor: pointer;
                line-height: 1;
                padding: 0;
                background: none;
                border: none;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                transition: all 0.2s;
            }

            .close:hover {
                background-color: #e9ecef;
                color: #333;
            }

            .modal-body {
                padding: 20px;
            }

            .image-container {
                position: relative;
                text-align: center;
                margin-bottom: 15px;
            }

            .modal-image {
                max-width: 100%;
                max-height: 300px;
                object-fit: contain;
                border-radius: 6px;
                border: 1px solid #ddd;
            }

            .nav-button {
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                background-color: rgba(255, 255, 255, 0.9);
                border: 1px solid #ddd;
                border-radius: 50%;
                width: 35px;
                height: 35px;
                cursor: pointer;
                font-size: 16px;
                display: flex;
                align-items: center;
                justify-content: center;
                transition: all 0.2s;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .nav-button:hover {
                background-color: white;
                box-shadow: 0 4px 8px rgba(0,0,0,0.15);
            }

            .prev-btn {
                left: 10px;
            }

            .next-btn {
                right: 10px;
            }



            .thumbnail-container {
                display: flex;
                justify-content: center;
                gap: 8px;
                flex-wrap: wrap;
                max-height: 80px;
                overflow-y: auto;
            }

            .thumbnail {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 4px;
                cursor: pointer;
                border: 2px solid transparent;
                transition: border-color 0.2s;
            }

            .thumbnail:hover {
                border-color: #007bff;
            }

            .thumbnail.active {
                border-color: #007bff;
            }

            /* Responsive cho mobile */
            @media (max-width: 768px) {
                .modal-content {
                    width: 350px;
                    margin: 20px;
                }

                .modal-image {
                    max-height: 250px;
                }

                .nav-button {
                    width: 30px;
                    height: 30px;
                    font-size: 14px;
                }

                .thumbnail {
                    width: 40px;
                    height: 40px;
                }

                .modal-header {
                    padding: 12px 15px;
                }

                .modal-body {
                    padding: 15px;
                }
            }

            @media (max-width: 480px) {
                .modal-content {
                    width: calc(100vw - 40px);
                }
            }
            .filter-container {
                display: flex;
                gap: 15px;
                margin-bottom: 20px;
                align-items: center;
            }

            .filter-btn {
                padding: 8px 16px;
                border: 1px solid #ddd;
                background: white;
                border-radius: 20px;
                cursor: pointer;
                transition: all 0.3s;
                color: #333;
                font-size: 14px;
            }

            .filter-btn:hover {
                background: #f8f9fa;
            }

            .filter-btn.active {
                background: #007bff;
                color: white;
                border-color: #007bff;
            }
            .btn-reply {
                background-color: #007bff;
                color: white;
                border: none;
                padding: 6px 12px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 12px;
                display: flex;
                align-items: center;
                gap: 5px;
            }

            .btn-reply:hover {
                background-color: white !important;
            }

            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
                padding: 5px 10px;
                margin-right: 5px;
                color: white;
            }

            .btn-primary:hover {
                background-color: #0056b3 !important;
                border-color: #0056b3 !important;
                color: white !important;
            }

            .btn-secondary {
                background-color: #6c757d;
                border-color: #6c757d;
                padding: 5px 10px;
            }

            .response-form-container {
                max-height: 0;
                overflow: hidden;
                opacity: 0;
                transition: all 0.4s ease-in-out;
                transform: translateY(-10px);
            }

            .response-form-container.show {
                max-height: 200px;
                opacity: 1;
                transform: translateY(0);
                margin-top: 10px;
            }

            .btn-reply {
                transition: all 0.3s ease;
            }

            .btn-reply.hide {
                opacity: 0;
                transform: scale(0.9);
            }

            /* Button container - CHÍNH SỬA */
            .button-container {
                display: flex;
                gap: 8px; /* Khoảng cách giữa các nút */
                align-items: center;
            }

            /* Button styling - CHÍNH SỬA */
            .btn-primary, .btn-secondary {
                padding: 6px 12px; /* Giảm padding để nút nhỏ hơn */
                font-size: 12px; /* Giảm font size */
                border-radius: 4px;
                cursor: pointer;
                border: none;
                transition: all 0.3s ease;
                min-width: 60px; /* Đảm bảo nút có độ rộng tối thiểu */
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
        <div class="wrapper">
            <nav id="sidebar" class="sidebar js-sidebar">
                <div class="sidebar-content js-simplebar">
                    <div class="sidebar-brand">
                        <span class="align-middle">Nestmart</span>
                    </div>

                    <ul class="sidebar-nav">
                        <li class="sidebar-header">Pages</li>

                        <li class="sidebar-item">
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

                        <li class="sidebar-item active">
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
                        <li class="sidebar-item ">
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
                            <form action="${pageContext.request.contextPath}/employee/viewFeedbackEmp.htm" method="get" class="search-form">
                                <input type="hidden" name="pageSize" value="${pageSize}">
                                <input type="text" name="keyword" class="form-control search-input" placeholder="Search by name, ID, or category..." value="${keyword}">
                                <button type="submit" class="btn search-button">
                                    <i class="align-middle" data-feather="search"></i>
                                </button>
                            </form>

                        </div>
                        <c:if test="${not empty keyword}">
                            <c:choose>
                                <c:when test="${not empty feedbacks}">
                                    <p>Found <b>${totalFeedbacks}</b> results.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>No result for "<b>${keyword}</b>".</p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>   

                        <div class="filter-container">
                            <button class="filter-btn active" onclick="filterFeedbacks('all', this)">All Feedback</button>
                            <button class="filter-btn" onclick="filterFeedbacks('no-response', this)">Without Response</button>
                            <button class="filter-btn" onclick="filterFeedbacks('with-response', this)">With Response</button>
                        </div>

                        <h3 id="pageTitle">All Feedback</h3>
                        <table class="table table-hover my-0" id="accountTable">
                            <thead>
                                <tr>
                                    <th>FeedbackID</th>
                                    <th>ProductID</th>
                                    <th>Product Name</th>
                                    <th>Customer Name</th>
                                    <th>Rating</th>
                                    <th>Feedback Content</th>
                                    <th>Feedback Images</th>
                                    <th>Response</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="feedback" items="${feedbacks}">
                                    <tr>
                                        <td><c:out value="${feedback.feedbackID}" /></td>
                                        <td><c:out value="${feedback.productID}" /></td>
                                        <td><c:out value="${feedback.productName}" /></td>
                                        <td><c:out value="${feedback.customerName}" /></td>
                                        <td width="8%">
                                            <div style="color: #f5c518;
                                                 font-size: 13px;">
                                                <c:forEach var="i" begin="1" end="${feedback.rating}">
                                                    &#9733; 
                                                </c:forEach>
                                                <c:forEach var="i" begin="${feedback.rating + 1}" end="5">
                                                    &#9734; 
                                                </c:forEach>
                                            </div>
                                        </td>
                                        <td><c:out value="${feedback.feedbackContent}" /> <br/> <small><c:out value="${feedback.formattedFeedbackDate}" /></small></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty feedback.images}">
                                                    <div class="feedback-images">
                                                        <div class="main-image-container" onclick="openImageModal(${feedback.feedbackID})">
                                                            <img src="${pageContext.request.contextPath}/assets/client/images/uploads/feedbacks/${feedback.images[0].image}"
                                                                 alt="Feedback Image"
                                                                 class="main-image"
                                                                 loading="lazy">
                                                            <c:if test="${fn:length(feedback.images) > 1}">
                                                                <span class="image-count-badge">+${fn:length(feedback.images) - 1}</span>
                                                            </c:if>
                                                        </div>
                                                    </div>

                                                    <!-- Hidden data for modal -->
                                                    <script type="application/json" id="images-${feedback.feedbackID}">
                                                        [
                                                        <c:forEach var="image" items="${feedback.images}" varStatus="status">
                                                            {
                                                            "src": "${pageContext.request.contextPath}/assets/client/images/uploads/feedbacks/${image.image}",
                                                            "alt": "Feedback Image ${status.index + 1}"
                                                            }<c:if test="${!status.last}">,</c:if>
                                                        </c:forEach>
                                                        ]
                                                    </script>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="no-images">No images</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <!-- Response Column -->
                                        <td>
                                            <c:if test="${feedbackResponses[feedback.feedbackID] != null}">
                                                <p>${feedbackResponses[feedback.feedbackID].responseContent}</p>
                                                <small>${feedbackResponses[feedback.feedbackID].formattedResponseDate}</small>
                                            </c:if>
                                            <c:if test="${feedbackResponses[feedback.feedbackID] == null}">
                                                <!-- Reply button -->
                                                <button type="button" class="btn btn-reply" onclick="showResponseForm(${feedback.feedbackID})" id="replyBtn-${feedback.feedbackID}">
                                                    <i data-feather="message-circle"></i> Reply
                                                </button>

                                                <!-- Hidden response form -->
                                                <div id="responseForm-${feedback.feedbackID}" class="response-form-container">
                                                    <form action="saveFeedbackResponse.htm" method="post">
                                                        <input type="hidden" name="feedbackID" value="${feedback.feedbackID}"/>
                                                        <input type="hidden" name="employeeID" value="2"/>
                                                        <input type="hidden" name="currentPage" value="${currentPage}"/>
                                                        <input type="hidden" name="pageSize" value="${pageSize}"/>
                                                        <c:if test="${not empty keyword}">
                                                            <input type="hidden" name="keyword" value="${keyword}"/>
                                                        </c:if>
                                                        <textarea name="responseContent" placeholder="Write your response here" required rows="3" style="width: 100%;
                                                                  margin-bottom: 10px;
                                                                  padding: 8px;
                                                                  border: 1px solid #ddd;
                                                                  border-radius: 4px;"></textarea>
                                                        <div>
                                                            <button type="submit" class="btn btn-primary btn-sm">Send</button>
                                                            <button type="button" class="btn btn-secondary btn-sm" onclick="hideResponseForm(${feedback.feedbackID})">Cancel</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </c:if>
                                        </td>

                                    </tr>
                                </c:forEach>
                                <c:if test="${empty feedbacks}">
                                    <tr>
                                        <td colspan="8">No feedback found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                        <!-- Replace the pagination section in your JSP with this fixed version -->
                        <div class="pagination-container">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:set var="maxDisplayPages" value="5" />

                                    <!-- Build base URL with parameters -->
                                    <c:set var="baseUrl" value="?page=" />
                                    <c:if test="${not empty keyword}">
                                        <c:set var="baseUrl" value="?keyword=${keyword}&page=" />
                                    </c:if>

                                    <!-- Previous button -->
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="${baseUrl}${currentPage - 1}">Previous</a>
                                    </li>

                                    <c:choose>
                                        <c:when test="${totalPages <= maxDisplayPages}">
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="${baseUrl}${i}">${i}</a>
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
                                                    <a class="page-link" href="${baseUrl}1">1</a>
                                                </li>
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                </c:if>

                                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="${baseUrl}${i}">${i}</a>
                                                </li>
                                            </c:forEach>

                                            <c:if test="${endPage < totalPages}">
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                <li class="page-item">
                                                    <a class="page-link" href="${baseUrl}${totalPages}">${totalPages}</a>
                                                </li>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>

                                    <!-- Next button -->
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link" href="${baseUrl}${currentPage + 1}">Next</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>

                </main>
                <div id="imageModal" class="image-modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="modal-title">Feedback Images</div>
                            <span class="close" onclick="closeImageModal()">&times;</span>
                        </div>

                        <div class="image-container">
                            <img id="modalImage" class="modal-image" src="" alt="">
                            <button class="nav-button prev-btn" onclick="changeImage(-1)">&#8249;</button>
                            <button class="nav-button next-btn" onclick="changeImage(1)">&#8250;</button>
                        </div>



                        <div class="thumbnail-container" id="thumbnailContainer">
                            <!-- Thumbnails will be generated here -->
                        </div>
                    </div>
                </div>






            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script>
        <script>
                                let currentImageIndex = 0;
                                let currentImages = [];

                                function openImageModal(feedbackId) {
                                    const imageData = document.getElementById('images-' + feedbackId);
                                    if (!imageData)
                                        return;

                                    try {
                                        currentImages = JSON.parse(imageData.textContent);
                                        currentImageIndex = 0;

                                        displayImage(0);
                                        generateThumbnails();

                                        document.getElementById('imageModal').style.display = 'block';
                                        document.body.style.overflow = 'hidden'; // Prevent background scrolling
                                    } catch (e) {
                                        console.error('Error parsing image data:', e);
                                    }
                                }

                                function closeImageModal() {
                                    document.getElementById('imageModal').style.display = 'none';
                                    document.body.style.overflow = 'auto'; // Restore scrolling
                                }

                                function displayImage(index) {
                                    if (!currentImages || currentImages.length === 0)
                                        return;

                                    const modalImage = document.getElementById('modalImage');

                                    modalImage.src = currentImages[index].src;
                                    modalImage.alt = currentImages[index].alt;

                                    // Update active thumbnail
                                    const thumbnails = document.querySelectorAll('.thumbnail');
                                    thumbnails.forEach((thumb, i) => {
                                        thumb.classList.toggle('active', i === index);
                                    });

                                    // Show/hide navigation buttons
                                    const prevBtn = document.querySelector('.prev-btn');
                                    const nextBtn = document.querySelector('.next-btn');

                                    if (currentImages.length <= 1) {
                                        prevBtn.style.display = 'none';
                                        nextBtn.style.display = 'none';
                                    } else {
                                        prevBtn.style.display = 'flex';
                                        nextBtn.style.display = 'flex';
                                    }
                                }

                                function changeImage(direction) {
                                    if (!currentImages || currentImages.length <= 1)
                                        return;

                                    currentImageIndex += direction;

                                    if (currentImageIndex < 0) {
                                        currentImageIndex = currentImages.length - 1;
                                    } else if (currentImageIndex >= currentImages.length) {
                                        currentImageIndex = 0;
                                    }

                                    displayImage(currentImageIndex);
                                }

                                function generateThumbnails() {
                                    const container = document.getElementById('thumbnailContainer');
                                    container.innerHTML = '';

                                    if (currentImages.length <= 1)
                                        return;

                                    currentImages.forEach((image, index) => {
                                        const thumbnail = document.createElement('img');
                                        thumbnail.src = image.src;
                                        thumbnail.alt = image.alt;
                                        thumbnail.className = 'thumbnail';
                                        thumbnail.onclick = () => {
                                            currentImageIndex = index;
                                            displayImage(index);
                                        };
                                        container.appendChild(thumbnail);
                                    });
                                }

                                // Close modal when clicking outside the image
                                document.getElementById('imageModal').onclick = function (event) {
                                    if (event.target === this) {
                                        closeImageModal();
                                    }
                                };

                                // Keyboard navigation
                                document.addEventListener('keydown', function (event) {
                                    const modal = document.getElementById('imageModal');
                                    if (modal.style.display === 'block') {
                                        switch (event.key) {
                                            case 'Escape':
                                                closeImageModal();
                                                break;
                                            case 'ArrowLeft':
                                                changeImage(-1);
                                                break;
                                            case 'ArrowRight':
                                                changeImage(1);
                                                break;
                                        }
                                    }
                                });
                                // Filter functionality
                                function filterFeedbacks(type, button) {
                                    const rows = document.querySelectorAll('#accountTable tbody tr');
                                    const title = document.getElementById('pageTitle');

                                    // Remove active class from all buttons
                                    document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
                                    // Add active class to clicked button
                                    button.classList.add('active');

                                    rows.forEach(row => {
                                        if (row.children.length === 1 && row.children[0].getAttribute('colspan')) {
                                            // Skip the "No feedback found" row
                                            return;
                                        }

                                        const responseCell = row.cells[7]; // Response column
                                        const hasResponse = responseCell.querySelector('p') !== null;

                                        if (type === 'all') {
                                            row.style.display = '';
                                            title.textContent = 'All Feedback';
                                        } else if (type === 'with-response') {
                                            row.style.display = hasResponse ? '' : 'none';
                                            title.textContent = 'Feedback With Response';
                                        } else if (type === 'no-response') {
                                            row.style.display = hasResponse ? 'none' : '';
                                            title.textContent = 'Feedback Without Response';
                                        }
                                    });
                                }
                                function showResponseForm(feedbackID) {
                                    const replyBtn = document.getElementById('replyBtn-' + feedbackID);
                                    const responseForm = document.getElementById('responseForm-' + feedbackID);

                                    // Add hide class to button for smooth transition
                                    replyBtn.classList.add('hide');

                                    // After button animation, show form
                                    setTimeout(() => {
                                        replyBtn.style.display = 'none';
                                        responseForm.classList.add('show');

                                        // Focus on textarea after animation
                                        setTimeout(() => {
                                            const textarea = responseForm.querySelector('textarea');
                                            if (textarea)
                                                textarea.focus();
                                        }, 200);
                                    }, 300);
                                }

                                function hideResponseForm(feedbackID) {
                                    const replyBtn = document.getElementById('replyBtn-' + feedbackID);
                                    const responseForm = document.getElementById('responseForm-' + feedbackID);

                                    // Hide form with animation
                                    responseForm.classList.remove('show');

                                    // After form animation, show button
                                    setTimeout(() => {
                                        replyBtn.style.display = 'flex';
                                        replyBtn.classList.remove('hide');
                                    }, 400);
                                }
                                function changePageSize(newPageSize) {
                                    const urlParams = new URLSearchParams(window.location.search);
                                    urlParams.set('pageSize', newPageSize);
                                    urlParams.set('page', '1'); // Reset về trang đầu khi thay đổi page size
                                    window.location.search = urlParams.toString();
                                }

                                window.onload = function () {
                                    const urlParams = new URLSearchParams(window.location.search);
                                    if (urlParams.get('success') === '1') {
                                        // You can add a toast notification here
                                        console.log('Response saved successfully');
                                    }
                                    if (urlParams.get('error') === '1') {
                                        // You can add an error notification here
                                        console.log('Error saving response');
                                    }
                                };
        </script>
        <script>
            feather.replace();
        </script>
        <script src="../assets/admin/js/app.js"></script>
    </body>
</html>