<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
            .salary-summary {
                background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
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

            .salary-card {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                margin-bottom: 15px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                transition: box-shadow 0.3s ease;
            }

            .salary-card:hover {
                box-shadow: 0 4px 8px rgba(0,0,0,0.15);
            }

            .salary-header {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                display: flex;
                justify-content: between;
                align-items: center;
            }

            .salary-body {
                padding: 20px;
            }

            .week-period {
                font-weight: 600;
                font-size: 1.1rem;
                color: #495057;
            }

            .salary-amount {
                font-size: 1.3rem;
                margin-left: 8px;
                font-weight: bold;
                color: #28a745;
            }

            .salary-details {
                margin-top: 15px;
            }

            .detail-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 8px;
                padding: 5px 0;
                border-bottom: 1px solid #f1f3f4;
            }

            .detail-row:last-child {
                border-bottom: none;
                font-weight: 600;
                margin-top: 10px;
                padding-top: 10px;
                border-top: 2px solid #dee2e6;
            }

            .detail-label {
                color: #6c757d;
            }

            .detail-value {
                font-weight: 500;
            }
            .btn-primary{
                background-color: #28A745;
                transition: background-color 0.3s ease;

            }
            .btn-primary:hover {
                background-color: #00cc00;
                color: white;
                text-decoration: none;
            }
            .view-detail-btn {
                background-color: #28A745;
                color: white;
                padding: 6px 12px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 0.875rem;
                transition: background-color 0.3s ease;
            }

            .view-detail-btn:hover {
                background-color: #00cc00;
                color: white;
                text-decoration: none;
            }

            .filter-section {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 30px;
            }

            .employee-header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                border: 1px solid #dee2e6;
                margin-bottom: 30px;
            }

            .no-salary {
                text-align: center;
                padding: 40px 20px;
                color: #6c757d;
                background: white;
                border-radius: 8px;
                border: 1px solid #dee2e6;
            }

            .export-btn {
                background-color: #28a745;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 0.875rem;
                transition: background-color 0.3s ease;
            }

            .export-btn:hover {
                background-color: #218838;
                color: white;
                text-decoration: none;
            }

            .payment-status {
                display: inline-block;
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 0.75rem;
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

           
            .info-row-salary {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
            }

            .info-label-salary {
                font-weight: 600;
                color: #495057;
                min-width: 120px;
                margin-right: 15px;
            }

            .info-value-salary {
                color: #333;
                font-weight: 700;
                font-size: 1.1rem;
                background-color: #f8f9fa;
                padding: 8px 12px;
                border-radius: 6px;
                border-left: 3px solid #28a745;
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
                            <h1 class="h2">My Salary</h1>
                            <div class="btn-toolbar mb-2 mb-md-0">
                                <div class="btn-group me-2">
                                    <a href="scheduleEmp.htm" class="btn btn-outline-primary">
                                        <i data-feather="calendar" class="me-1"></i> View Schedule
                                    </a>
                                    <c:choose>
                                        <c:when test="${isFiltered}">
                                            <a href="exportSalaryCSV.htm?month=${selectedMonth}&year=${selectedYear}" class="btn btn-success export-btn">
                                                <i data-feather="download" class="me-1"></i> Export ${selectedMonthName} ${selectedYear}
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="exportSalaryCSV.htm" class="btn btn-success export-btn">
                                                <i data-feather="download" class="me-1"></i> Export All Data
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
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

                        <c:if test="${not empty employee}">
                            <div class="employee-header">
                                <h5><i data-feather="user" class="me-2"></i>Employee Information</h5>
                                <div class="mt-3">
                                    <div class="info-row-salary">
                                        <span class="info-label-salary">Name:</span>
                                        <span class="info-value-salary">${employee.fullName}</span>
                                    </div>
                                    <div class="info-row-salary">
                                        <span class="info-label-salary">Employee ID:</span>
                                        <span class="info-value-salary">${employeeID}</span>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <div class="salary-summary">
                            <div class="row">
                                <c:choose>
                                    <c:when test="${isFiltered}">
                                        <div class="col-12 mb-3">
                                            <h5 class="text-center mb-3">
                                                ${selectedMonthName} ${selectedYear} Summary
                                            </h5>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="summary-item">
                                                <span class="summary-value">$<fmt:formatNumber value="${periodTotal}" pattern="#,##0.00" /></span>
                                                <div class="summary-label">Total Earnings</div>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="summary-item">
                                                <span class="summary-value">${periodHours}</span>
                                                <div class="summary-label">Total Hours</div>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="summary-item">
                                                <span class="summary-value"><fmt:formatNumber value="${periodOvertime}" pattern="#,##0.0" /></span>
                                                <div class="summary-label">Overtime Hours</div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-12 mb-3">
                                            <h5 class="text-center mb-3">${currentMonth} ${currentYear} Summary</h5>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="summary-item">
                                                <span class="summary-value">$<fmt:formatNumber value="${currentMonthTotal}" pattern="#,##0.00" /></span>
                                                <div class="summary-label">This Month</div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="summary-item">
                                                <span class="summary-value">${currentMonthHours}</span>
                                                <div class="summary-label">Hours This Month</div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="summary-item">
                                                <span class="summary-value"><fmt:formatNumber value="${currentMonthOvertime}" pattern="#,##0.0" /></span>
                                                <div class="summary-label">Overtime This Month</div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="summary-item">
                                                <span class="summary-value">$<fmt:formatNumber value="${totalEarnings}" pattern="#,##0.00" /></span>
                                                <div class="summary-label">Total Earnings</div>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="filter-section">
                            <h5><i data-feather="filter" class="me-2"></i>Filter Salary by Month/Year</h5>
                            <form action="${pageContext.request.contextPath}/employee/salaryFilterMonthly.htm" method="GET" class="row g-3 mt-2">
                                <div class="col-md-4">
                                    <label for="month" class="form-label">Select Month</label>
                                    <select class="form-select" id="month" name="month" required>
                                        <option value="">Choose month...</option>
                                        <option value="1" ${selectedMonth == 1 ? 'selected' : ''}>January</option>
                                        <option value="2" ${selectedMonth == 2 ? 'selected' : ''}>February</option>
                                        <option value="3" ${selectedMonth == 3 ? 'selected' : ''}>March</option>
                                        <option value="4" ${selectedMonth == 4 ? 'selected' : ''}>April</option>
                                        <option value="5" ${selectedMonth == 5 ? 'selected' : ''}>May</option>
                                        <option value="6" ${selectedMonth == 6 ? 'selected' : ''}>June</option>
                                        <option value="7" ${selectedMonth == 7 ? 'selected' : ''}>July</option>
                                        <option value="8" ${selectedMonth == 8 ? 'selected' : ''}>August</option>
                                        <option value="9" ${selectedMonth == 9 ? 'selected' : ''}>September</option>
                                        <option value="10" ${selectedMonth == 10 ? 'selected' : ''}>October</option>
                                        <option value="11" ${selectedMonth == 11 ? 'selected' : ''}>November</option>
                                        <option value="12" ${selectedMonth == 12 ? 'selected' : ''}>December</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label for="year" class="form-label">Select Year</label>
                                    <select class="form-select" id="year" name="year" required>
                                        <option value="">Choose year...</option>
                                        <%-- Tạo danh sách các năm từ năm hiện tại trở về 5 năm trước --%>
                                        <c:set var="currentYearValue" value="<%= java.time.LocalDate.now().getYear()%>" />
                                        <c:forEach var="i" begin="0" end="5">
                                            <c:set var="yearValue" value="${currentYearValue - i}" />
                                            <option value="${yearValue}" ${selectedYear == yearValue ? 'selected' : ''}>
                                                ${yearValue}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">&nbsp;</label>
                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-primary">
                                            <i data-feather="search" class="me-1"></i> Filter
                                        </button>
                                        <c:if test="${isFiltered}">
                                            <a href="salary.htm" class="btn btn-outline-secondary">
                                                <i data-feather="x" class="me-1"></i> Clear Filter
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <h3><i data-feather="history" class="me-2"></i>
                            <c:choose>
                                <c:when test="${isFiltered}">Filtered Salary History</c:when>
                                <c:otherwise>Recent Salary History</c:otherwise>
                            </c:choose>
                        </h3>

                        <c:choose>
                            <c:when test="${not empty salaryHistory}">
                                <c:forEach var="salary" items="${salaryHistory}">
                                    <div class="salary-card">
                                        <div class="salary-header">
                                            <div class="week-period">
                                                <fmt:formatDate value="${salary.weekStartDate}" pattern="MMM dd, yyyy" /> - 
                                                <fmt:formatDate value="${salary.weekEndDate}" pattern="MMM dd, yyyy" />
                                            </div>
                                            <div class="d-flex gap-2 align-items-center">
                                                <span class="salary-amount">
                                                    $<fmt:formatNumber value="${salary.totalSalary}" pattern="#,##0.00" />
                                                </span>
                                                <a href="salaryDetailsEmp.htm?weekScheduleID=${salary.weekScheduleID}" 
                                                   class="view-detail-btn">
                                                    <i data-feather="eye" style="width: 14px; height: 14px;"></i>
                                                    Details
                                                </a>
                                            </div>
                                        </div>
                                        <div class="salary-body">
                                            <div class="salary-details">
                                                <div class="detail-row">
                                                    <span class="detail-label">Hours Worked:</span>
                                                    <span class="detail-value">${salary.totalHoursWorked} hours</span>
                                                </div>
                                                <div class="detail-row">
                                                    <span class="detail-label">Overtime Hours:</span>
                                                    <span class="detail-value">
                                                        <fmt:formatNumber value="${salary.totalOvertimeHours}" pattern="#,##0.0" /> hours
                                                    </span>
                                                </div>
                                                <div class="detail-row">
                                                    <span class="detail-label">Overtime Pay:</span>
                                                    <span class="detail-value">
                                                        $<fmt:formatNumber value="${salary.totalOvertimeSalary}" pattern="#,##0.00" />
                                                    </span>
                                                </div>

                                                <div class="detail-row">
                                                    <span class="detail-label"><strong>Total Salary:</strong></span>
                                                    <span class="detail-value salary-amount">
                                                        $<fmt:formatNumber value="${salary.totalSalary}" pattern="#,##0.00" />
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="no-salary">
                                    <i data-feather="dollar-sign" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                    <h5>No Salary Records</h5>
                                    <p>
                                        <c:choose>
                                            <c:when test="${isFiltered}">
                                                No salary records found for the selected date range.
                                            </c:when>
                                            <c:otherwise>
                                                You don't have any salary records to display.
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
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

                $('.view-detail-btn').on('click', function () {
                    $(this).html('<i data-feather="loader" class="me-1" style="width: 14px; height: 14px;"></i> Loading...');
                });

                $('form').on('submit', function (e) {
                    const startDate = new Date($('#startDate').val());
                    const endDate = new Date($('#endDate').val());

                    if (startDate > endDate) {
                        e.preventDefault();
                        alert('Start date cannot be after end date.');
                        return false;
                    }
                });
            });
        </script>
    </body>
</html>