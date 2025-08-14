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
            .schedule-summary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 30px;
                border-radius: 10px;
                margin-bottom: 30px;
            }

            .summary-item {
                text-align: center;
                padding: 20px;
            }

            .summary-value {
                font-size: 2rem;
                font-weight: bold;
                display: block;
            }

            .summary-label {
                font-size: 0.9rem;
                opacity: 0.9;
                margin-top: 5px;
            }

            .day-schedule {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .day-header {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                font-weight: 600;
                font-size: 1.1rem;
                color: #495057;
            }

            .shift-item {
                padding: 15px 20px;
                border-bottom: 1px solid #f1f3f4;
                display: flex;
                justify-content: between;
                align-items: center;
            }

            .shift-item:last-child {
                border-bottom: none;
            }

            .shift-time {
                font-weight: 600;
                color: #007bff;
                font-size: 1.1rem;
            }

            .shift-details {
                flex-grow: 1;
                margin-left: 20px;
            }

            .shift-name {
                font-weight: 500;
                margin-bottom: 5px;
            }

            .shift-overtime {
                font-size: 0.9rem;
                color: #dc3545;
            }

            .no-shifts {
                padding: 30px;
                text-align: center;
                color: #6c757d;
            }

            .back-btn {
                margin-bottom: 20px;
            }

            .week-info {
                background: white;
                padding: 20px;
                border-radius: 8px;
                border: 1px solid #dee2e6;
                margin-bottom: 30px;
            }

            .employee-header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                border: 1px solid #dee2e6;
                margin-bottom: 30px;
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
                        <div class="back-btn">
                            <a href="scheduleEmp.htm" class="btn btn-outline-primary">
                                <i data-feather="arrow-left" class="me-1"></i> Back to Schedule
                            </a>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i data-feather="alert-circle" class="me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i data-feather="check-circle" class="me-2"></i>
                                ${successMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty employee}">
                            <div class="employee-header">
                                <h4><i data-feather="user" class="me-2"></i>Employee Information</h4>
                                <div class="mt-3">
                                    <div class="info-row">
                                        <span class="info-label-detail">Name:</span>
                                        <span class="info-value-detail">${employee.fullName}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label-detail">Employee ID:</span>
                                        <span class="info-value-detail">${employeeID}</span>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty weekSchedule}">
                            <div class="week-info">
                                <h4><i data-feather="calendar" class="me-2"></i>Week Schedule Details</h4>
                                <div class="mt-3">
                                    <div class="info-row">
                                        <span class="info-label-detail">Week Period:</span>
                                        <span class="info-value-detail">
                                            <fmt:formatDate value="${weekSchedule.weekStartDate}" pattern="MMM dd, yyyy" /> - 
                                            <fmt:formatDate value="${weekSchedule.weekEndDate}" pattern="MMM dd, yyyy" />
                                        </span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label-detail">Week Schedule ID:</span>
                                        <span class="info-value-detail">${weekSchedule.weekScheduleID}</span>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <div class="schedule-summary">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="summary-item">
                                        <span class="summary-value">${totalHours}</span>
                                        <div class="summary-label">Total Hours</div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="summary-item">
                                        <span class="summary-value">${regularHours}</span>
                                        <div class="summary-label">Regular Hours</div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="summary-item">
                                        <span class="summary-value">${totalOvertime}</span>
                                        <div class="summary-label">Overtime Hours</div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="summary-item">
                                        <span class="summary-value">${totalShifts}</span>
                                        <div class="summary-label">Total Shifts</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <h3><i data-feather="clock" class="me-2"></i>Daily Schedule</h3>

                        <c:choose>
                            <c:when test="${not empty scheduleByDay}">
                                <c:forEach var="dayEntry" items="${scheduleByDay}">
                                    <div class="day-schedule">
                                        <div class="day-header">
                                            ${dayEntry.key}
                                        </div>
                                        <c:forEach var="weekDetail" items="${dayEntry.value}">
                                            <div class="shift-item">
                                                <div class="shift-time">
                                                    <c:set var="shift" value="${shiftMap[weekDetail.shiftID]}" />
                                                    <c:if test="${not empty shift}">
                                                        <fmt:formatDate value="${shift.startTime}" pattern="HH:mm" /> - 
                                                        <fmt:formatDate value="${shift.endTime}" pattern="HH:mm" />
                                                    </c:if>
                                                </div>
                                                <div class="shift-details">
                                                    <div class="shift-name">
                                                        <c:if test="${not empty shift}">
                                                            ${shift.shiftName}
                                                        </c:if>
                                                    </div>
                                                    <c:if test="${weekDetail.overtimeHours != null && weekDetail.overtimeHours > 0}">
                                                        <div class="shift-overtime">
                                                            <i data-feather="clock" style="width: 14px; height: 14px;"></i>
                                                            Overtime: ${weekDetail.overtimeHours} hours
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${not empty weekDetail.status}">
                                                        <div class="shift-status">
                                                            Status: <span class="badge bg-secondary">${weekDetail.status}</span>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="day-schedule">
                                    <div class="no-shifts">
                                        <i data-feather="calendar-x" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                        <h5>No Schedule Data</h5>
                                        <p>No schedule details found for this week.</p>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>


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
            });
        </script>
    </body>
</html>