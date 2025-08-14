<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.TR/html4/loose.dtd">

<html>
    <head>
        <meta charset="utf-8">
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

        <title>NestMart - Categories</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
        <style>

            .schedule-card {
                border: 1px solid #dee2e6;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                transition: box-shadow 0.3s ease;
            }

            .schedule-card:hover {
                box-shadow: 0 4px 8px rgba(0,0,0,0.15);
            }

            .current-week {
                border-left: 4px solid #28a745;
                background-color: #f8fff9;
            }

            .upcoming-week {
                border-left: 4px solid #007bff;
                background-color: #f8f9ff;
            }

            .past-week {
                border-left: 4px solid #6c757d;
                background-color: #f8f9fa;
            }

            .schedule-header {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                font-weight: 600;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .schedule-body {
                padding: 20px;
            }

            .status-badge {
                font-size: 0.875rem;
                padding: 4px 8px;
                border-radius: 4px;
            }

            .status-current {
                background-color: #d4edda;
                color: #155724;
            }

            .status-upcoming {
                background-color: #d1ecf1;
                color: #0c5460;
            }

            .status-past {
                background-color: #e2e3e5;
                color: #383d41;
            }

            .week-info {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 15px;
            }

            .week-dates {
                font-size: 1.1rem;
                font-weight: 500;
                color: #495057;
            }

            .view-details-btn {
                background-color: #3B7DDD;
                color: white;
                padding: 8px 16px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 0.9rem;
                transition: background-color 0.3s ease;
            }

            .view-details-btn:hover {
                background-color: #0056b3;
                color: white;
                text-decoration: none;
            }

            .no-schedule {
                text-align: center;
                padding: 40px 20px;
                color: #6c757d;
            }

            .section-title {
                font-size: 1.5rem;
                font-weight: 600;
                margin-bottom: 20px;
                color: #343a40;
                border-bottom: 2px solid #dee2e6;
                padding-bottom: 10px;
            }

            .employee-info {
                background-color: #fff;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 30px;
            }

            .info-item {
                display: flex;
                justify-content: space-between;
                margin-bottom: 10px;
            }

            .info-label {
                font-weight: 600;
                color: #495057;
                min-width: 120px;
                margin-right: 15px;
            }

            .info-value {
                font-weight: 700;
                font-size: 1.1rem;
                background-color: #f8f9fa;
                border-radius: 6px;
                border-left: 3px solid #3B7DDD;
                padding: 8px 12px;
                color: #6c757d;
                flex: 1;
            }

            .quick-actions {
                margin-bottom: 30px;
            }

            .action-btn {
                margin-right: 10px;
                margin-bottom: 10px;
            }
            .action-btn.salary-btn {
                font-weight: bold;
                background-color: #3B7DDD;
                color: #fff;
            }

            .info-row {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
            }

            .info-label-detail {
                font-weight: 600;
                color: #495057;
                min-width: 140px;
                margin-right: 15px;
            }

            .info-value-detail {
                color: #333;
                font-weight: 700;
                font-size: 1.1rem;
                background-color: #f8f9fa;
                padding: 8px 12px;
                border-radius: 6px;
                border-left: 3px solid #3B7DDD;
                flex: 1;
            }
        </style>
    </head>
    <body>
        <div class="wrapper">
            <nav id="sidebar" class="sidebar js-sidebar">
                <div class="sidebar-content js-simplebar">
                    <div class="sidebar-brand">
                        <span class="align-middle">NestMart</span>
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
                         <li class="sidebar-item active">
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
                    <div class="container-fluid p-0">
                        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                            <h1 class="h2">My Work Schedule</h1>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i data-feather="alert-circle" class="me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i data-feather="check-circle" class="me-2"></i>
                                ${success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <div class="employee-info">
                            <h5 class="mb-3"><i data-feather="user" class="me-2"></i>Employee Information</h5>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="info-item">
                                        <span class="info-label">Name:</span>
                                        <span class="info-value">${employeeName}</span>
                                    </div>

                                </div>
                                <div class="col-md-6">
                                    <div class="info-item">
                                        <span class="info-label">Employee ID:</span>
                                        <span class="info-value">${employeeID}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="quick-actions">
                            <a href="../employee/salary.htm" class="btn action-btn salary-btn">
                                <i data-feather="dollar-sign" class="me-1"></i>View Salary
                            </a>
                            <c:if test="${hasCurrentWeek}">

                                <a href="schedule.htm?weekScheduleID=${currentWeek.weekScheduleID}" class="btn action-btn salary-btn">
                                    <i data-feather="eye" class="me-1"></i>View Current Week Details
                                </a>
                            </c:if>
                        </div>

                        <div class="row">
                            <div class="col-12">
                                <h3 class="section-title">
                                    <i data-feather="calendar" class="me-2"></i>Current Week
                                </h3>

                                <c:choose>
                                    <c:when test="${currentWeek != null}">
                                        <div class="schedule-card current-week">
                                            <div class="schedule-header">
                                                <div class="week-dates">
                                                    <fmt:formatDate value="${currentWeek.weekStartDate}" pattern="MMM dd, yyyy" /> - 
                                                    <fmt:formatDate value="${currentWeek.weekEndDate}" pattern="MMM dd, yyyy" />
                                                </div>
                                                <span class="status-badge status-current">Current Week</span>
                                            </div>
                                            <div class="schedule-body">
                                                <div class="week-info">
                                                    <div>
                                                        <p class="mb-1"><strong>Week Schedule ID:</strong> ${currentWeek.weekScheduleID}</p>
                                                        <p class="mb-0 text-muted">This is your active work schedule</p>
                                                    </div>
                                                    <a href="schedule.htm?weekScheduleID=${currentWeek.weekScheduleID}" 
                                                       class="view-details-btn">
                                                        <i data-feather="eye" class="me-1" style="width: 14px; height: 14px;"></i>
                                                        View Details
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="schedule-card">
                                            <div class="no-schedule">
                                                <i data-feather="calendar-x" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                                <h5>No Current Schedule</h5>
                                                <p>You don't have a schedule assigned for this week.</p>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-12">
                                <h3 class="section-title">
                                    <i data-feather="arrow-right-circle" class="me-2"></i>Upcoming Schedules
                                </h3>

                                <c:choose>
                                    <c:when test="${not empty upcomingSchedules}">
                                        <c:forEach var="schedule" items="${upcomingSchedules}">
                                            <div class="schedule-card upcoming-week">
                                                <div class="schedule-header">
                                                    <div class="week-dates">
                                                        <fmt:formatDate value="${schedule.weekStartDate}" pattern="MMM dd, yyyy" /> - 
                                                        <fmt:formatDate value="${schedule.weekEndDate}" pattern="MMM dd, yyyy" />
                                                    </div>
                                                    <span class="status-badge status-upcoming">Upcoming</span>
                                                </div>
                                                <div class="schedule-body">
                                                    <div class="week-info">
                                                        <div>
                                                            <p class="mb-1"><strong>Week Schedule ID:</strong> ${schedule.weekScheduleID}</p>
                                                            <p class="mb-0 text-muted">Scheduled for future work</p>
                                                        </div>
                                                        <a href="schedule.htm?weekScheduleID=${schedule.weekScheduleID}" 
                                                           class="view-details-btn">
                                                            <i data-feather="eye" class="me-1" style="width: 14px; height: 14px;"></i>
                                                            View Details
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="schedule-card">
                                            <div class="no-schedule">
                                                <i data-feather="calendar" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                                <h5>No Upcoming Schedules</h5>
                                                <p>You don't have any upcoming schedules assigned.</p>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-12">
                                <h3 class="section-title">
                                    <i data-feather="clock" class="me-2"></i>Recent Schedules
                                </h3>

                                <c:choose>
                                    <c:when test="${not empty recentSchedules}">
                                        <c:forEach var="schedule" items="${recentSchedules}">
                                            <div class="schedule-card past-week">
                                                <div class="schedule-header">
                                                    <div class="week-dates">
                                                        <fmt:formatDate value="${schedule.weekStartDate}" pattern="MMM dd, yyyy" /> - 
                                                        <fmt:formatDate value="${schedule.weekEndDate}" pattern="MMM dd, yyyy" />
                                                    </div>
                                                    <span class="status-badge status-past">Completed</span>
                                                </div>
                                                <div class="schedule-body">
                                                    <div class="week-info">
                                                        <div>
                                                            <p class="mb-1"><strong>Week Schedule ID:</strong> ${schedule.weekScheduleID}</p>
                                                            <p class="mb-0 text-muted">Past work schedule</p>
                                                        </div>
                                                        <a href="schedule.htm?weekScheduleID=${schedule.weekScheduleID}" 
                                                           class="view-details-btn">
                                                            <i data-feather="eye" class="me-1" style="width: 14px; height: 14px;"></i>
                                                            View Details
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="schedule-card">
                                            <div class="no-schedule">
                                                <i data-feather="archive" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                                <h5>No Recent Schedules</h5>
                                                <p>You don't have any recent work schedules to display.</p>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.29.2/feather.min.js"></script>
        <script src="../assets/admin/js/app.js"></script>

        <script>
            $(document).ready(function () {
                feather.replace();

                setTimeout(function () {
                    $('.alert').fadeOut('slow');
                }, 5000);

                $('.view-details-btn').on('click', function () {
                    $(this).html('<i data-feather="loader" class="me-1" style="width: 14px; height: 14px;"></i> Loading...');
                });
            });
        </script>
    </body>
</html>