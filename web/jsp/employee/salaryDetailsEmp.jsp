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
            .salary-header-card {
                background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
                color: white;
                padding: 30px;
                border-radius: 10px;
                margin-bottom: 30px;
            }

            .salary-overview {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 25px;
                margin-bottom: 25px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .overview-item {
                text-align: center;
                padding: 15px;
                border-right: 1px solid #e9ecef;
            }

            .overview-item:last-child {
                border-right: none;
            }

            .overview-value {
                font-size: 1.8rem;
                font-weight: 700;
                color: white !important; 
                display: block;
                text-shadow: 0 1px 3px rgba(0,0,0,0.2);  
                margin-bottom: 8px;
            }

            .overview-label {
                font-size: 0.95rem;
                color: rgba(255,255,255,0.9) !important;  
                margin-top: 5px;
                font-weight: 500;
            }

            .details-card {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .details-header {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                font-weight: 600;
                color: #495057;
            }

            .details-body {
                padding: 20px;
            }

            .schedule-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }

            .schedule-table th,
            .schedule-table td {
                padding: 15px 12px;
                text-align: left;
                border-bottom: 1px solid #e9ecef;
                color: #495057 !important;  
                background: #ffffff;  
            }

            .schedule-table th {
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%) !important;
                font-weight: 600;
                color: #495057 !important; 
            }
            .schedule-table tbody tr:hover td {
                background-color: #f8f9fa !important;  
            }

            .day-name {
                color: #007bff !important;  
            }

            .shift-time {
                color: #6c757d !important;  
            }

            .overtime-badge {
                color: #212529 !important; 
            }


            .payment-status {
                display: inline-block;
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 600;
                text-transform: uppercase;
            }

            .status-paid {
                background-color: #d4edda;
                color: #155724;
            }

            .status-pending {
                background-color: #fff3cd;
                color: #856404;
            }

            .summary-row td {
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%) !important;
                color: #495057 !important; 
                font-weight: 600;
            }
            .back-btn {
                background-color: #6c757d;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 0.875rem;
                transition: background-color 0.3s ease;
            }

            .back-btn:hover {
                background-color: #5a6268;
                color: white;
                text-decoration: none;
            }

            .print-btn {
                background-color: #17a2b8;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 4px;
                font-size: 0.875rem;
                transition: background-color 0.3s ease;
            }
            
            .print-btn:hover {
                background-color: #00cc00;
            }

            .employee-info {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 25px;
            }

            .no-data {
                text-align: center;
                padding: 40px 20px;
                color: #6c757d;
                background: white;
                border-radius: 8px;
                border: 1px solid #dee2e6;
            }
            .text-end {
                color: #28a745 !important;  
            }
            @media print {
                .no-print {
                    display: none !important;
                }

                .salary-header-card {
                    background: #f8f9fa !important;
                    color: #212529 !important;
                    border: 1px solid #dee2e6 !important;
                }
            }
            .btn-info{
             background-color: #28A745;
                transition: background-color 0.3s ease;
            }
            .day-name{
                color: #00cc00 !important;
            }
        </style>
    </head>
    <body>
        <div class="wrapper">
            <nav id="sidebar" class="sidebar js-sidebar no-print">
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
                          <li class="sidebar-item">
                            <a class="sidebar-link" href="scheduleEmp.htm">
                                <i class="align-middle me-2" data-feather="calendar"></i> 
                                <span class="align-middle">Schedule</span>
                            </a>
                        </li>
                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="salary.htm">
                                <i class="align-middle me-2" data-feather="dollar-sign"></i> 
                                <span class="align-middle">Salary</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <div class="main">
                <nav class="navbar navbar-expand navbar-light navbar-bg no-print">
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
                        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom no-print">
                            <h1 class="h2">Salary Details</h1>
                            <div class="btn-toolbar mb-2 mb-md-0">
                                <div class="btn-group me-2">
                                    <a href="salary.htm" class="btn btn-secondary back-btn">
                                        <i data-feather="arrow-left" class="me-1"></i> Back to Salary
                                    </a>
                                    <button type="button" class="btn btn-info print-btn" onclick="window.print()">
                                        <i data-feather="printer" class="me-1"></i> Print
                                    </button>
                                </div>
                            </div>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show no-print" role="alert">
                                <i data-feather="alert-circle" class="me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty success}">
                            <div class="alert alert-success alert-dismissible fade show no-print" role="alert">
                                <i data-feather="check-circle" class="me-2"></i>
                                ${success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty employee}">
                            <div class="employee-info">
                                <h5><i data-feather="user" class="me-2"></i>Employee Information</h5>
                                <div class="row mt-3">
                                    <div class="col-md-4">
                                        <strong>Name:</strong> ${employee.fullName}
                                    </div>
                                    <div class="col-md-4">
                                        <strong>Employee ID:</strong> ${employee.accountID}
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:choose>
                            <c:when test="${not empty weekSalary}">
                                <div class="salary-header-card">
                                    <div class="row text-center">
                                        <div class="col-12 mb-3">
                                            <h3>Week Salary Details</h3>
                                            <h5>
                                                <fmt:formatDate value="${weekSalary.weekStartDate}" pattern="MMMM dd, yyyy" /> - 
                                                <fmt:formatDate value="${weekSalary.weekEndDate}" pattern="MMMM dd, yyyy" />
                                            </h5>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div class="overview-item text-center">
                                                <span class="overview-value">$<fmt:formatNumber value="${weekSalary.totalSalary}" pattern="#,##0.00" /></span>
                                                <div class="overview-label">Total Salary</div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="overview-item text-center">
                                                <span class="overview-value">${totalHoursWorked}</span>
                                                <div class="overview-label">Hours Worked</div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="overview-item text-center">
                                                <span class="overview-value"><fmt:formatNumber value="${weekSalary.totalOvertimeHours}" pattern="#,##0.0" /></span>
                                                <div class="overview-label">Overtime Hours</div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="overview-item text-center">
                                                <span class="overview-value">$<fmt:formatNumber value="${weekSalary.totalOvertimeSalary}" pattern="#,##0.00" /></span>
                                                <div class="overview-label">Overtime Pay</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>


                                <c:if test="${not empty weekDetailsList}">
                                    <div class="details-card">
                                        <div class="details-header">
                                            <i data-feather="calendar" class="me-2"></i>Weekly Schedule Breakdown
                                        </div>
                                        <div class="details-body">
                                            <div class="table-responsive">
                                                <table class="schedule-table">
                                                    <thead>
                                                        <tr>
                                                            <th>Day</th>
                                                            <th>Shift</th>
                                                            <th>Regular Hours</th>
                                                            <th>Overtime Hours</th>
                                                            <th>Status</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="detail" items="${weekDetailsList}">
                                                            <tr>
                                                                <td>
                                                                    <span class="day-name">${dayNameMap[detail.dayID]}</span>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${not empty shiftMap[detail.shiftID]}">
                                                                        <strong>${shiftMap[detail.shiftID].shiftName}</strong><br>
                                                                        <span class="shift-time">
                                                                            <fmt:formatDate value="${shiftMap[detail.shiftID].startTime}" pattern="HH:mm" /> - 
                                                                            <fmt:formatDate value="${shiftMap[detail.shiftID].endTime}" pattern="HH:mm" />
                                                                        </span>
                                                                    </c:if>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${not empty shiftMap[detail.shiftID]}">
                                                                        <c:set var="regularHours" value="${(shiftMap[detail.shiftID].endTime.time - shiftMap[detail.shiftID].startTime.time) / (1000 * 60 * 60)}" />
                                                                        <fmt:formatNumber value="${regularHours}" pattern="#,##0.0" /> hours
                                                                    </c:if>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${detail.overtimeHours != null && detail.overtimeHours > 0}">
                                                                            <span class="overtime-badge">
                                                                                <fmt:formatNumber value="${detail.overtimeHours}" pattern="#,##0.0" /> hours
                                                                            </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">-</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${detail.status == 'Completed'}">
                                                                            <span class="badge bg-success">Completed</span>
                                                                        </c:when>
                                                                        <c:when test="${detail.status == 'Scheduled'}">
                                                                            <span class="badge bg-primary">Scheduled</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-secondary">${detail.status}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="details-card">
                                    <div class="details-header">
                                        <i data-feather="calculator" class="me-2"></i>Salary Calculation
                                    </div>
                                    <div class="details-body">
                                        <div class="table-responsive">
                                            <table class="table">
                                                <tbody>
                                                    <tr>
                                                        <td><strong>Regular Hours:</strong></td>
                                                        <td>${weekSalary.totalHoursWorked} hours</td>
                                                        <td>@ Regular Rate</td>
                                                        <td class="text-end">
                                                            $<fmt:formatNumber value="${weekSalary.totalSalary - weekSalary.totalOvertimeSalary}" pattern="#,##0.00" />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><strong>Overtime Hours:</strong></td>
                                                        <td><fmt:formatNumber value="${weekSalary.totalOvertimeHours}" pattern="#,##0.0" /> hours</td>
                                                        <td>@ 1.5x Rate</td>
                                                        <td class="text-end">
                                                            $<fmt:formatNumber value="${weekSalary.totalOvertimeSalary}" pattern="#,##0.00" />
                                                        </td>
                                                    </tr>
                                                    <tr class="summary-row">
                                                        <td><strong>Total Earnings:</strong></td>
                                                        <td colspan="2"><strong>Week Total</strong></td>
                                                        <td class="text-end">
                                                            <strong>$<fmt:formatNumber value="${weekSalary.totalSalary}" pattern="#,##0.00" /></strong>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                            </c:when>
                            <c:otherwise>
                                <div class="no-data">
                                    <i data-feather="alert-circle" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                    <h5>No Salary Data Available</h5>
                                    <p>No salary information found for the requested week. Please contact your administrator if you believe this is an error.</p>
                                    <a href="salary.htm" class="btn btn-primary mt-3">
                                        <i data-feather="arrow-left" class="me-1"></i> Back to Salary Overview
                                    </a>
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

                                            $('.print-btn').on('click', function () {
                                                window.print();
                                            });

                                            $('.details-card').hide().fadeIn(800);

                                            $('.schedule-table tbody tr').hover(
                                                    function () {
                                                        $(this).addClass('table-active');
                                                    },
                                                    function () {
                                                        $(this).removeClass('table-active');
                                                    }
                                            );
                                        });

                                        window.addEventListener('beforeprint', function () {
                                            document.title = 'Salary Details - ${employee.fullName} - Week ${weekSalary.weekStartDate} to ${weekSalary.weekEndDate}';
                                        });
        </script>
    </body>
</html>