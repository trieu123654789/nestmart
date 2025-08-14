<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="Responsive Admin &amp; Dashboard Template based on Bootstrap 5">
        <meta name="author" content="AdminKit">

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" href="../assets/admin/images/favicon.ico" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <title>NestMart - Shipper Schedule & Salary</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">

        <style>
            /* Header Styles */
            .page-header {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
                padding: 30px;
                border-radius: 10px;
                margin-bottom: 30px;
            }

            .header-info {
                display: flex;
                justify-content: center;
                align-items: center;
                flex-wrap: wrap;
            }

            .shipper-info {
                text-align: center;
            }

            .shipper-info h3 {
                margin: 0;
                font-weight: 600;
            }

            .shipper-info p {
                margin: 5px 0 0 0;
                opacity: 0.9;
            }

            /* Tab Navigation */
            .custom-tabs {
                display: flex;
                align-items: center;
                border-bottom: 2px solid #dee2e6;
                margin-bottom: 30px;
            }

            .custom-tab {
                background: none;
                border: none;
                padding: 15px 25px;
                font-size: 1rem;
                font-weight: 600;
                color: #6c757d;
                cursor: pointer;
                border-bottom: 3px solid transparent;
                transition: all 0.3s ease;
            }

            .custom-tab:hover {
                color: #FF9702;
                background-color: #f8f9fa;
            }

            .custom-tab.active {
                color: #FF9702;
                border-bottom-color: #FF9702;
                background-color: #fff8f0;
            }

            .tab-content {
                display: none;
            }

            .tab-content.active {
                display: block;
                animation: fadeIn 0.3s ease-in;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            /* Filter Section */
            .filter-section {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 30px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            /* Schedule Styles */
            .schedule-card {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
            }

            .schedule-card:hover {
                box-shadow: 0 4px 8px rgba(0,0,0,0.15);
                transform: translateY(-2px);
            }

            .current-week {
                border-left: 4px solid #FF9702;
                background: linear-gradient(to right, #fff8f0, white);
            }

            .upcoming-week {
                border-left: 4px solid #007bff;
                background: linear-gradient(to right, #f8f9ff, white);
            }

            .past-week {
                border-left: 4px solid #6c757d;
                background: linear-gradient(to right, #f8f9fa, white);
            }

            .schedule-header {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-radius: 8px 8px 0 0;
            }

            .schedule-body {
                padding: 20px;
            }

            .week-dates {
                font-size: 1.1rem;
                font-weight: 600;
                color: #495057;
            }

            .status-badge {
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 600;
                text-transform: uppercase;
            }

            .status-current {
                background: #d4edda;
                color: #155724;
            }
            .status-upcoming {
                background: #d1ecf1;
                color: #0c5460;
            }
            .status-past {
                background: #e2e3e5;
                color: #383d41;
            }

            /* Salary Status Badges */
            .status-paid {
                background: #FF9702;
                color: white;
                padding: 0.3rem 0.7rem;
                border-radius: 12px;
                font-weight: 600;
                font-size: 0.75rem;
                text-transform: uppercase;
                letter-spacing: 0.3px;
                display: inline-flex;
                align-items: center;
                box-shadow: 0 1px 4px rgba(0,0,0,0.08);
            }

            .status-pending {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
                padding: 0.3rem 0.7rem;
                border-radius: 12px;
                font-weight: 600;
                font-size: 0.75rem;
                text-transform: uppercase;
                letter-spacing: 0.3px;
                display: inline-flex;
                align-items: center;
                box-shadow: 0 1px 4px rgba(0,0,0,0.08);
            }

            /* Salary Styles */
            .salary-card {
                background: white;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                margin-bottom: 15px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
            }

            .salary-card:hover {
                box-shadow: 0 4px 8px rgba(0,0,0,0.15);
                transform: translateY(-2px);
            }

            .salary-header {
                background-color: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-radius: 8px 8px 0 0;
            }

            .salary-body {
                padding: 20px;
            }

            .salary-amount {
                font-size: 1.3rem;
                font-weight: bold;
                color: #FF9702;
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
                color: #495057;
            }

            /* Button Styles */
            .btn-success-custom {
                background-color: #FF9702;
                border-color: #FF9702;
                color: white;
                transition: all 0.3s ease;
            }

            .btn-success-custom:hover {
                background-color: #e67e22;
                border-color: #d35400;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(255, 151, 2, 0.3);
            }


            .btn-primary-custom {
                background-color: #007bff;
                border-color: #007bff;
                color: white;
                transition: all 0.3s ease;
            }

            .btn-primary-custom:hover {
                background-color: #0056b3;
                border-color: #004085;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 123, 255, 0.3);
            }

            .view-detail-btn {
                background-color: #FF9702;
                color: white;
                padding: 6px 12px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 0.875rem;
                transition: all 0.3s ease;
                border: none;
            }

            .view-detail-btn:hover {
                background-color: #e67e22;
                color: white;
                text-decoration: none;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(255, 151, 2, 0.3);
            }

            /* Loading and Empty States */
            .no-data {
                text-align: center;
                padding: 60px 20px;
                color: #6c757d;
                background: white;
                border-radius: 8px;
                border: 1px solid #dee2e6;
            }

            .loading {
                text-align: center;
                padding: 40px;
                color: #6c757d;
            }

            /* Performance Modal Styles */
            .performance-modal .modal-content {
                border-radius: 10px;
                border: none;
                box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            }

            .performance-modal .modal-header {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
                border-radius: 10px 10px 0 0;
                border-bottom: none;
            }

            .performance-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
                margin: 20px 0;
            }

            .performance-item {
                text-align: center;
                padding: 20px;
                background: #f8f9fa;
                border-radius: 8px;
                border: 2px solid #e9ecef;
            }

            .performance-value {
                font-size: 1.8rem;
                font-weight: bold;
                color: #FF9702;
                display: block;
                margin-bottom: 8px;
            }

            .performance-label {
                color: #6c757d;
                font-weight: 500;
            }

            /* Date Selector Styling */
            .filter-section .form-select {
                font-size: 0.9rem;
            }

            .filter-section .form-label {
                font-weight: 600;
                color: #495057;
                margin-bottom: 6px;
            }

            .filter-section .btn-sm {
                padding: 6px 12px;
                font-size: 0.875rem;
            }

            /* Enhanced Responsive Design */
            @media (max-width: 992px) {
                .page-header {
                    padding: 20px 15px;
                    margin-bottom: 20px;
                }

                .filter-section {
                    padding: 15px;
                    margin-bottom: 20px;
                }

                .schedule-card, .salary-card {
                    margin-bottom: 15px;
                }
            }

            @media (max-width: 768px) {
                /* Header Adjustments */
                .page-header {
                    padding: 15px 10px;
                    margin-bottom: 15px;
                }

                .header-info {
                    text-align: center;
                }

                .shipper-info h3 {
                    font-size: 1.3rem;
                }

                .shipper-info p {
                    font-size: 0.9rem;
                }

                /* Tab Navigation */
                .custom-tabs {
                    flex-wrap: wrap;
                    border-bottom: 1px solid #dee2e6;
                    margin-bottom: 20px;
                }

                .custom-tab {
                    padding: 8px 12px;
                    font-size: 0.85rem;
                    min-width: 140px;
                    text-align: center;
                    border-radius: 6px 6px 0 0;
                }

                .custom-tab i {
                    display: block;
                    margin-bottom: 2px;
                }

                /* Filter Section */
                .filter-section {
                    padding: 12px;
                }

                .filter-section h5 {
                    font-size: 1rem;
                    margin-bottom: 15px;
                }

                .filter-section .row .col-md-2,
                .filter-section .row .col-md-4 {
                    margin-bottom: 12px;
                }

                .filter-section .row {
                    --bs-gutter-x: 0.5rem;
                }

                .filter-section .col-md-2 {
                    flex: 0 0 auto;
                    width: 50%;
                }

                .filter-section .col-md-4 {
                    width: 100%;
                }

                .filter-section .d-flex {
                    flex-direction: column;
                    gap: 8px;
                }

                .filter-section .btn {
                    width: 100%;
                    font-size: 0.85rem;
                    padding: 8px 12px;
                }

                /* Cards */
                .schedule-card, .salary-card {
                    margin-bottom: 12px;
                    border-radius: 6px;
                }

                .schedule-header, .salary-header {
                    padding: 12px 15px;
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 10px;
                }

                .schedule-body, .salary-body {
                    padding: 15px;
                }

                .week-dates {
                    font-size: 1rem;
                    margin-bottom: 8px;
                }

                /* Button Groups */
                .d-flex.gap-2 {
                    flex-wrap: wrap;
                    gap: 6px !important;
                }

                .view-detail-btn {
                    padding: 6px 10px;
                    font-size: 0.75rem;
                    border-radius: 4px;
                }

                .view-detail-btn i {
                    width: 12px !important;
                    height: 12px !important;
                }

                /* Status Badges */
                .status-badge {
                    font-size: 0.7rem;
                    padding: 3px 8px;
                }

                .status-paid, .status-pending {
                    font-size: 0.65rem;
                    padding: 0.25rem 0.5rem;
                }

                /* Salary Details */
                .salary-amount {
                    font-size: 1.1rem;
                }

                .detail-row {
                    flex-direction: column;
                    align-items: flex-start;
                    margin-bottom: 10px;
                    padding: 8px 0;
                }

                .detail-label {
                    font-size: 0.8rem;
                    margin-bottom: 2px;
                }

                .detail-value {
                    font-size: 0.9rem;
                }

                /* No Data States */
                .no-data {
                    padding: 40px 15px;
                }

                .no-data i {
                    width: 32px !important;
                    height: 32px !important;
                    margin-bottom: 12px !important;
                }

                .no-data h5 {
                    font-size: 1rem;
                }

                .no-data p {
                    font-size: 0.85rem;
                }

                /* Performance Modal */
                .performance-grid {
                    grid-template-columns: 1fr 1fr;
                    gap: 10px;
                }

                .performance-item {
                    padding: 12px;
                }

                .performance-value {
                    font-size: 1.4rem;
                }

                .performance-label {
                    font-size: 0.8rem;
                }
            }

            @media (max-width: 576px) {
                /* Extra Small Devices */
                .page-header {
                    padding: 12px 8px;
                    margin-bottom: 12px;
                }

                .shipper-info h3 {
                    font-size: 1.1rem;
                }

                .shipper-info p {
                    font-size: 0.8rem;
                }

                /* Filter Section - Stack Everything */
                .filter-section {
                    padding: 10px;
                }

                .filter-section .col-md-2 {
                    width: 100%;
                    margin-bottom: 10px;
                }

                .filter-section .form-select,
                .filter-section .form-label {
                    font-size: 0.85rem;
                }

                /* Tabs - Stack Vertically */
                .custom-tabs {
                    flex-direction: column;
                }

                .custom-tab {
                    width: 100%;
                    padding: 10px;
                    border-radius: 0;
                    border-bottom: 1px solid #dee2e6;
                    border-left: 3px solid transparent;
                }

                .custom-tab.active {
                    border-left-color: #FF9702;
                    border-bottom-color: #FF9702;
                    background-color: #fff8f0;
                }

                .custom-tab i {
                    display: inline;
                    margin-right: 6px;
                    margin-bottom: 0;
                }

                /* Cards - Full Width */
                .schedule-header, .salary-header {
                    padding: 10px 12px;
                }

                .schedule-body, .salary-body {
                    padding: 12px;
                }

                /* Button Groups - Stack */
                .d-flex.gap-2 {
                    flex-direction: column;
                    gap: 4px !important;
                }

                .view-detail-btn {
                    width: 100%;
                    text-align: center;
                    padding: 8px 12px;
                    font-size: 0.8rem;
                }

                /* Status Badges - Smaller */
                .status-badge {
                    font-size: 0.65rem;
                    padding: 2px 6px;
                }

                /* Performance Grid - Single Column */
                .performance-grid {
                    grid-template-columns: 1fr;
                    gap: 8px;
                }

                .performance-item {
                    padding: 10px;
                }

                .performance-value {
                    font-size: 1.2rem;
                }

                /* Modal Adjustments */
                .modal-dialog {
                    margin: 10px;
                }

                .modal-header {
                    padding: 10px 15px;
                }

                .modal-body {
                    padding: 15px;
                }

                .modal-title {
                    font-size: 1rem;
                }

                /* Content Spacing */
                .content {
                    padding: 0 10px;
                }

                .container-fluid {
                    padding: 0 5px;
                }

                /* Section Headers */
                h4 {
                    font-size: 1.1rem;
                    margin-bottom: 12px;
                }

                h4 i {
                    width: 16px !important;
                    height: 16px !important;
                }

                /* Utility Classes for Mobile */
                .mobile-stack {
                    flex-direction: column !important;
                }

                .mobile-full-width {
                    width: 100% !important;
                }

                .mobile-text-center {
                    text-align: center !important;
                }
            }

            @media (max-width: 400px) {
                /* Very Small Devices */
                .page-header {
                    padding: 8px 5px;
                }

                .filter-section {
                    padding: 8px;
                }

                .schedule-card, .salary-card {
                    margin: 0 -5px 10px -5px;
                    border-radius: 4px;
                }

                .schedule-header, .salary-header {
                    padding: 8px 10px;
                }

                .schedule-body, .salary-body {
                    padding: 10px;
                }

                .view-detail-btn {
                    padding: 6px 8px;
                    font-size: 0.75rem;
                }

                .salary-amount {
                    font-size: 1rem;
                }

                .detail-value {
                    font-size: 0.85rem;
                }
            }
        </style>
    </head>
    <body>
        <div class="wrapper">
            <!-- Sidebar -->
            <nav id="sidebar" class="sidebar js-sidebar">
                <div class="sidebar-content js-simplebar">
                    <div class="sidebar-brand">
                        <span class="align-middle">NestMart</span>
                    </div>

                    <ul class="sidebar-nav">
                        <li class="sidebar-header">Pages</li>
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="shippers.htm">
                                <i class="align-middle me-2" data-feather="truck"></i>
                                <span class="align-middle">Orders</span>
                            </a>
                        </li>
                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="scheduleAndSalary.htm">
                                <i class="align-middle me-2" data-feather="calendar"></i>
                                <span class="align-middle">Schedule & Salary</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <div class="main">
                <!-- Top Navigation -->
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
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/shipper/accountInformation.htm">
                                        <i class="fa fa-user"></i> Account Information
                                    </a>

                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/shipper/changePassword.htm">
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
                    <div class="container-fluid p-0">
                        <!-- Page Header -->
                        <div class="page-header">
                            <div class="header-info">
                                <div class="shipper-info">
                                    <h3>Shipper Dashboard</h3>
                                    <p>ID: ${shipperID} | View your schedule and salary information</p>
                                </div>
                            </div>
                        </div>

                        <!-- Error/Success Messages -->
                        <c:if test="${error != null}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i data-feather="alert-circle" class="me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>


                        <!-- Filter Section - Always Visible -->
                        <div class="filter-section">
                            <h5><i data-feather="filter" class="me-2"></i>Filter Salary Records</h5>
                            <form action="scheduleAndSalary.htm" method="GET" class="row g-3 mt-2" id="salaryFilterForm">
                                <input type="hidden" id="currentTab" name="currentTab" value="schedule" />
                                <div class="col-md-2">
                                    <label for="startMonth" class="form-label">Start Month</label>
                                    <select class="form-select" id="startMonth" name="startMonth">
                                        <option value="">All Months</option>
                                        <option value="1">January</option>
                                        <option value="2">February</option>
                                        <option value="3">March</option>
                                        <option value="4">April</option>
                                        <option value="5">May</option>
                                        <option value="6">June</option>
                                        <option value="7">July</option>
                                        <option value="8">August</option>
                                        <option value="9">September</option>
                                        <option value="10">October</option>
                                        <option value="11">November</option>
                                        <option value="12">December</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="startYear" class="form-label">Start Year</label>
                                    <select class="form-select" id="startYear" name="startYear">
                                        <option value="">All Years</option>
                                        <!-- Years will be populated by JavaScript -->
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="endMonth" class="form-label">End Month</label>
                                    <select class="form-select" id="endMonth" name="endMonth">
                                        <option value="">All Months</option>
                                        <option value="1">January</option>
                                        <option value="2">February</option>
                                        <option value="3">March</option>
                                        <option value="4">April</option>
                                        <option value="5">May</option>
                                        <option value="6">June</option>
                                        <option value="7">July</option>
                                        <option value="8">August</option>
                                        <option value="9">September</option>
                                        <option value="10">October</option>
                                        <option value="11">November</option>
                                        <option value="12">December</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="endYear" class="form-label">End Year</label>
                                    <select class="form-select" id="endYear" name="endYear">
                                        <option value="">All Years</option>
                                        <!-- Years will be populated by JavaScript -->
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">&nbsp;</label>
                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-success-custom">
                                            <i data-feather="search" class="me-1"></i> Filter Salary Records
                                        </button>
                                        <a href="scheduleAndSalary.htm" class="btn btn-outline-secondary">
                                            <i data-feather="x" class="me-1"></i> Clear Filters
                                        </a>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <!-- Tab Navigation -->
                        <div class="custom-tabs">
                            <button class="custom-tab active" onclick="switchTab('schedule')">
                                <i data-feather="calendar" class="me-2"></i>Work Schedule
                            </button>
                            <button class="custom-tab" onclick="switchTab('salary')">
                                <i data-feather="dollar-sign" class="me-2"></i>Salary History
                            </button>
                        </div>

                        <!-- Schedule Tab Content -->
                        <div id="schedule-content" class="tab-content active">
                            <!-- Current Week Section -->
                            <h4><i data-feather="calendar" class="me-2"></i>Current Week</h4>
                            <c:choose>
                                <c:when test="${currentSchedule != null}">
                                    <div class="schedule-card current-week">
                                        <div class="schedule-header">
                                            <div class="week-dates">
                                                <c:out value="${currentSchedule.weekStartDate}"/> - <c:out value="${currentSchedule.weekEndDate}"/>
                                            </div>
                                            <div class="d-flex gap-2 align-items-center">
                                                <span class="status-badge status-current">Current Week</span>
                                                <button class="view-detail-btn" onclick="viewWeekPerformance('<c:out value="${currentSchedule.weekScheduleID}"/>')">
                                                    <i data-feather="bar-chart" style="width: 14px; height: 14px;"></i>
                                                    Performance
                                                </button>
                                                <button class="view-detail-btn" onclick="viewWeekDetails('<c:out value="${currentSchedule.weekScheduleID}"/>')">
                                                    <i data-feather="eye" style="width: 14px; height: 14px;"></i>
                                                    Details
                                                </button>
                                                <button class="view-detail-btn export-csv-btn" onclick="exportWeekCSV('<c:out value="${currentSchedule.weekScheduleID}"/>')">
                                                    <i data-feather="file-text" style="width: 14px; height: 14px;"></i>
                                                    Export CSV
                                                </button>
                                            </div>
                                        </div>
                                        <div class="schedule-body">
                                            <p class="mb-1"><strong>Week Schedule ID:</strong> <c:out value="${currentSchedule.weekScheduleID}" default="N/A"/></p>
                                            <p class="mb-0 text-muted">This is your active work schedule for this week</p>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-data">
                                        <i data-feather="calendar-x" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                        <h5>No Current Schedule</h5>
                                        <p>You don't have a schedule assigned for this week.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <!-- Upcoming Schedules -->
                            <h4 class="mt-4"><i data-feather="arrow-right-circle" class="me-2"></i>Upcoming Schedules</h4>
                            <c:choose>
                                <c:when test="${not empty upcomingSchedules}">
                                    <c:forEach var="schedule" items="${upcomingSchedules}">
                                        <div class="schedule-card upcoming-week">
                                            <div class="schedule-header">
                                                <div class="week-dates">
                                                    <c:out value="${schedule.weekStartDate}"/> - <c:out value="${schedule.weekEndDate}"/>
                                                </div>
                                                <div class="d-flex gap-2 align-items-center">
                                                    <span class="status-badge status-upcoming">Upcoming</span>
                                                    <button class="view-detail-btn" onclick="viewWeekDetails('<c:out value="${schedule.weekScheduleID}"/>')">
                                                        <i data-feather="eye" style="width: 14px; height: 14px;"></i>
                                                        Details
                                                    </button>
                                                    <button class="view-detail-btn export-csv-btn" onclick="exportWeekCSV('<c:out value="${schedule.weekScheduleID}"/>')">
                                                        <i data-feather="file-text" style="width: 14px; height: 14px;"></i>
                                                        Export CSV
                                                    </button>
                                                </div>
                                            </div>
                                            <div class="schedule-body">
                                                <p class="mb-1"><strong>Week Schedule ID:</strong> <c:out value="${schedule.weekScheduleID}" default="N/A"/></p>
                                                <p class="mb-0 text-muted">Scheduled for future work</p>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-data">
                                        <i data-feather="calendar" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                        <h5>No Upcoming Schedules</h5>
                                        <p>You don't have any upcoming schedules assigned.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <!-- Recent Schedules -->
                            <!-- Recent Schedules - simplified version -->
                            <h4 class="mt-4"><i data-feather="clock" class="me-2"></i>Recent Schedules</h4>
                            <c:choose>
                                <c:when test="${not empty pastSchedules}">
                                    <c:forEach var="schedule" items="${pastSchedules}" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <div class="schedule-card past-week">
                                                <div class="schedule-header">
                                                    <div class="week-dates">
                                                        <c:out value="${schedule.weekStartDate}"/> - <c:out value="${schedule.weekEndDate}"/>
                                                    </div>
                                                    <div class="d-flex gap-2 align-items-center">
                                                        <span class="status-badge status-past">Completed</span>
                                                        <button class="view-detail-btn" onclick="viewWeekPerformance('<c:out value="${schedule.weekScheduleID}"/>')">
                                                            <i data-feather="bar-chart" style="width: 14px; height: 14px;"></i>
                                                            Performance
                                                        </button>
                                                        <button class="view-detail-btn" onclick="viewWeekDetails('<c:out value="${schedule.weekScheduleID}"/>')">
                                                            <i data-feather="eye" style="width: 14px; height: 14px;"></i>
                                                            Details
                                                        </button>
                                                        <button class="view-detail-btn export-csv-btn" onclick="exportWeekCSV('<c:out value="${schedule.weekScheduleID}"/>')">
                                                            <i data-feather="file-text" style="width: 14px; height: 14px;"></i>
                                                            Export CSV
                                                        </button>
                                                    </div>
                                                </div>
                                                <div class="schedule-body">
                                                    <p class="mb-1"><strong>Week Schedule ID:</strong> <c:out value="${schedule.weekScheduleID}" default="N/A"/></p>
                                                    <p class="mb-0 text-muted">Past work schedule</p>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-data">
                                        <i data-feather="archive" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                        <h5>No Recent Schedules</h5>
                                        <p>You don't have any recent work schedules to display.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Salary Tab Content -->
                        <div id="salary-content" class="tab-content">

                            <!-- Salary History Section -->
                            <h4><i data-feather="dollar-sign" class="me-2"></i>Salary History</h4>
                            <c:choose>
                                <c:when test="${not empty salaryHistory}">
                                    <c:forEach var="salary" items="${salaryHistory}">
                                        <div class="salary-card">
                                            <div class="salary-header">
                                                <div class="week-dates">
                                                    <c:out value="${salary.weekStartDate}"/> - <c:out value="${salary.weekEndDate}"/>
                                                </div>
                                                <div class="d-flex gap-2 align-items-center">
                                                    <c:choose>
                                                        <c:when test="${salary.status == 'PAID'}">
                                                            <span class="status-badge status-paid">✓ Paid</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge status-pending">⏳ Pending</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div class="salary-amount">
                                                        <fmt:formatNumber value="${salary.totalSalary}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                                    </div>
                                                    <button class="view-detail-btn" onclick="printWeekPayroll('<c:out value="${salary.weekScheduleID}"/>')" style="background-color: #007bff;">
                                                        <i data-feather="printer" style="width: 14px; height: 14px;"></i>
                                                        Print
                                                    </button>
                                                </div>
                                            </div>
                                            <div class="salary-body">
                                                <div class="salary-details">
                                                    <div class="detail-row">
                                                        <span class="detail-label">Working Hours:</span>
                                                        <span class="detail-value"><fmt:formatNumber value="${salary.totalHoursWorked}" type="number" maxFractionDigits="0" groupingUsed="true" /> hours</span>
                                                    </div>
                                                    <div class="detail-row">
                                                        <span class="detail-label">Overtime Hours:</span>
                                                        <span class="detail-value"><fmt:formatNumber value="${salary.totalOvertimeHours}" type="number" maxFractionDigits="0" groupingUsed="true" /> hours</span>
                                                    </div>
                                                    <div class="detail-row">
                                                        <span class="detail-label">Overtime Pay:</span>
                                                        <span class="detail-value">$<fmt:formatNumber value="${salary.totalOvertimeSalary}" type="number" maxFractionDigits="0" groupingUsed="true" /></span>
                                                    </div>
                                                    <div class="detail-row">
                                                        <span class="detail-label">Payment Date:</span>
                                                        <span class="detail-value">
                                                            <c:choose>
                                                                <c:when test="${salary.salaryPaymentDate != null}">
                                                                    <c:out value="${salary.salaryPaymentDate}"/>
                                                                </c:when>
                                                                <c:otherwise>Pending</c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </div>
                                                    <div class="detail-row">
                                                        <span class="detail-label">Payment Status:</span>
                                                        <span class="detail-value">
                                                            <c:choose>
                                                                <c:when test="${salary.status == 'PAID'}">
                                                                    <span class="status-badge status-paid">✓ Paid</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="status-badge status-pending">⏳ Pending</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </div>
                                                    <div class="detail-row">
                                                        <span class="detail-label">Total Salary:</span>
                                                        <span class="detail-value">$<fmt:formatNumber value="${salary.totalSalary}" type="number" maxFractionDigits="0" groupingUsed="true" /></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-data">
                                        <i data-feather="dollar-sign" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>
                                        <h5>No Salary Records</h5>
                                        <p>You don't have any salary records to display.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <!-- Performance Modal -->
        <div class="modal fade performance-modal" id="performanceModal" tabindex="-1" aria-labelledby="performanceModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="performanceModalLabel">
                            <i data-feather="bar-chart" class="me-2"></i>Week Performance
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="performanceContent">
                            <div class="loading">
                                <i data-feather="loader" class="me-2"></i>Loading performance data...
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Week Details Modal -->
        <div class="modal fade" id="detailsModal" tabindex="-1" aria-labelledby="detailsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="detailsModalLabel">
                            <i data-feather="calendar" class="me-2"></i>Week Schedule Details
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="detailsContent">
                            <div class="loading">
                                <i data-feather="loader" class="me-2"></i>Loading schedule details...
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>

        <script src="../assets/admin/js/app.js"></script>

        <script>
                                                        try {
                                                            console.log("Custom script loaded");

                                                            // This will be replaced with AJAX calls to get real server data
                                                            const performanceData = {};
                                                            const detailsData = {};

                                                            // Also add data for some common test IDs - using safe approach without EL in JS
                                                            // Performance and details data will be handled by static data for now

                                                            console.log('Performance data loaded:', performanceData);
                                                            console.log('Details data loaded:', detailsData);

                                                            // Debug: Check if functions are being defined
                                                            console.log('About to define functions...');

                                                            // Tab switching function - moved to global scope
                                                            function switchTab(tabName) {
                                                                console.log('Switching to tab:', tabName);
                                                                try {
                                                                    // Update tab buttons
                                                                    $('.custom-tab').removeClass('active');
                                                                    $('[onclick="switchTab(\'' + tabName + '\')"').addClass('active');

                                                                    // Update tab content
                                                                    $('.tab-content').removeClass('active');
                                                                    $('#' + tabName + '-content').addClass('active');

                                                                    // Update the current tab in the form
                                                                    $('#currentTab').val(tabName);

                                                                    // Update filter form appearance based on active tab
                                                                    updateFilterForCurrentTab(tabName);

                                                                    // No need for global button toggling anymore

                                                                    // Re-initialize feather icons for new content
                                                                    setTimeout(() => feather.replace(), 100);
                                                                } catch (error) {
                                                                    console.error('Error in switchTab:', error);
                                                                }
                                                            }

                                                            // Update filter form based on current tab
                                                            function updateFilterForCurrentTab(tabName) {
                                                                const filterTitle = $('.filter-section h5');
                                                                const filterButton = $('.filter-section button[type="submit"]');

                                                                if (tabName === 'salary') {
                                                                    filterTitle.html('<i data-feather="filter" class="me-2"></i>Filter Salary Records');
                                                                    filterButton.html('<i data-feather="search" class="me-1"></i> Filter Salary Records');
                                                                } else {
                                                                    filterTitle.html('<i data-feather="filter" class="me-2"></i>Filter Schedule Records');
                                                                    filterButton.html('<i data-feather="search" class="me-1"></i> Filter Schedule Records');
                                                                }

                                                                // Re-render feather icons
                                                                setTimeout(() => feather.replace(), 50);
                                                            }

                                                            // Make switchTab globally accessible
                                                            window.switchTab = switchTab;

                                                            // Debug: Check if function is available
                                                            console.log('switchTab function defined:', typeof switchTab);
                                                            console.log('window.switchTab available:', typeof window.switchTab);

                                                            // View week performance - load real data via AJAX
                                                            function viewWeekPerformance(weekScheduleID) {
                                                                console.log('viewWeekPerformance called with weekScheduleID:', weekScheduleID);
                                                                $('#performanceModal').modal('show');

                                                                // Show loading state
                                                                $('#performanceContent').html(
                                                                        '<div class="loading">' +
                                                                        '<i data-feather="loader" class="me-2"></i>Loading performance data...' +
                                                                        '</div>'
                                                                        );

                                                                // Load real performance data from server
                                                                $.ajax({
                                                                    url: 'performance/' + weekScheduleID + '.htm',
                                                                    method: 'GET',
                                                                    dataType: 'xml',
                                                                    success: function (xmlData) {
                                                                        var $xml = $(xmlData);
                                                                        var totalWorkingHours = $xml.find('totalWorkingHours').text();

                                                                        if (totalWorkingHours) {
                                                                            // Extract data from XML
                                                                            var totalOrdersDelivered = $xml.find('totalOrdersDelivered').text() || '0';
                                                                            var totalSalary = $xml.find('totalSalary').text() || '0';
                                                                            var totalOvertimeHours = $xml.find('totalOvertimeHours').text() || '0';
                                                                            var totalOvertimeSalary = $xml.find('totalOvertimeSalary').text() || '0';
                                                                            var error = $xml.find('error').text();

                                                                            const overtimeText = parseFloat(totalOvertimeHours) > 0 ?
                                                                                    '(including <strong>' + parseFloat(totalOvertimeHours).toFixed(1) + ' overtime hours</strong>)' : '';

                                                                            const hrsPerDelivery = parseFloat(totalOrdersDelivered) > 0 ?
                                                                                    (parseFloat(totalWorkingHours) / parseFloat(totalOrdersDelivered)).toFixed(1) : '0';

                                                                            $('#performanceContent').html(
                                                                                    '<div class="performance-grid">' +
                                                                                    '<div class="performance-item">' +
                                                                                    '<span class="performance-value" >' + totalWorkingHours + '</span>' +
                                                                                    '<div class="performance-label">Working Hours</div>' +
                                                                                    '</div>' +
                                                                                    '<div class="performance-item">' +
                                                                                    '<span class="performance-value">' + totalOrdersDelivered + '</span>' +
                                                                                    '<div class="performance-label">Orders Delivered</div>' +
                                                                                    '</div>' +
                                                                                    '<div class="performance-item">' +
                                                                                    '<span class="performance-value">$' + parseFloat(totalSalary).toLocaleString('en-US', {minimumFractionDigits: 0}) + '</span>' +
                                                                                    '<div class="performance-label">Total Salary</div>' +
                                                                                    '</div>' +
                                                                                    '<div class="performance-item">' +
                                                                                    '<span class="performance-value">' + parseFloat(totalOvertimeHours).toFixed(0) + '</span>' +
                                                                                    '<div class="performance-label">Overtime Hours</div>' +
                                                                                    '</div>' +
                                                                                    '<div class="performance-item">' +
                                                                                    '<span class="performance-value">$' + parseFloat(totalOvertimeSalary).toLocaleString('en-US', {minimumFractionDigits: 0}) + '</span>' +
                                                                                    '<div class="performance-label">Overtime Pay</div>' +
                                                                                    '</div>' +
                                                                                    '<div class="performance-item">' +
                                                                                    '<span class="performance-value">' + hrsPerDelivery + '</span>' +
                                                                                    '<div class="performance-label">Hrs per Delivery</div>' +
                                                                                    '</div>' +
                                                                                    '</div>' +
                                                                                    '<div class="mt-3">' +
                                                                                    '<h6>Performance Summary</h6>' +
                                                                                    '<p class="text-muted">' +
                                                                                    (error ? '<strong>Note: ' + error + '</strong><br/>' : '') +
                                                                                    'You worked <strong>' + totalWorkingHours + ' hours</strong> this week ' +
                                                                                    overtimeText +
                                                                                    ' and delivered <strong>' + totalOrdersDelivered + ' orders</strong>, ' +
                                                                                    'earning a total of <strong>$' + parseFloat(totalSalary).toLocaleString('en-US', {minimumFractionDigits: 2}) + '</strong>.' +
                                                                                    '</p>' +
                                                                                    '</div>'
                                                                                    );
                                                                        } else {
                                                                            $('#performanceContent').html(
                                                                                    '<div class="no-data">' +
                                                                                    '<i data-feather="alert-circle" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>' +
                                                                                    '<h5>No Performance Data</h5>' +
                                                                                    '<p>No performance data available for this week.</p>' +
                                                                                    '</div>'
                                                                                    );
                                                                        }
                                                                        feather.replace();
                                                                    },
                                                                    error: function () {
                                                                        $('#performanceContent').html(
                                                                                '<div class="no-data">' +
                                                                                '<i data-feather="alert-circle" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>' +
                                                                                '<h5>Error Loading Data</h5>' +
                                                                                '<p>Unable to load performance data. Please try again.</p>' +
                                                                                '</div>'
                                                                                );
                                                                        feather.replace();
                                                                    }
                                                                });
                                                            }

                                                            // View week details - load real data via AJAX
                                                            function viewWeekDetails(weekScheduleID) {
                                                                console.log('viewWeekDetails called with weekScheduleID:', weekScheduleID);
                                                                $('#detailsModal').modal('show');

                                                                // Show loading state
                                                                $('#detailsContent').html(
                                                                        '<div class="loading">' +
                                                                        '<i data-feather="loader" class="me-2"></i>Loading schedule details...' +
                                                                        '</div>'
                                                                        );

                                                                // Load real schedule details from server
                                                                $.ajax({
                                                                    url: 'week-details/' + weekScheduleID + '.htm',
                                                                    method: 'GET',
                                                                    dataType: 'xml',
                                                                    success: function (xmlData) {
                                                                        var $xml = $(xmlData);
                                                                        var details = $xml.find('detail');

                                                                        if (details.length > 0) {
                                                                            let detailsHTML =
                                                                                    '<div class="table-responsive">' +
                                                                                    '<table class="table table-striped">' +
                                                                                    '<thead>' +
                                                                                    '<tr>' +
                                                                                    '<th>Day</th>' +
                                                                                    '<th>Shift</th>' +
                                                                                    '<th>Hours</th>' +
                                                                                    '<th>Overtime</th>' +
                                                                                    '<th>Status</th>' +
                                                                                    '</tr>' +
                                                                                    '</thead>' +
                                                                                    '<tbody>';

                                                                            const dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

                                                                            details.each(function () {
                                                                                var $detail = $(this);
                                                                                var dayID = parseInt($detail.find('dayID').text()) || 1;
                                                                                var shiftID = $detail.find('shiftID').text() || 'N/A';
                                                                                var overtimeHours = parseFloat($detail.find('overtimeHours').text()) || 0;
                                                                                var status = $detail.find('status').text() || 'Scheduled';

                                                                                const dayName = dayNames[dayID - 1] || 'Unknown';

                                                                                detailsHTML +=
                                                                                        '<tr>' +
                                                                                        '<td><strong class="text-success">' + dayName + '</strong></td>' +
                                                                                        '<td>Shift ' + shiftID + '</td>' +
                                                                                        '<td>8 hours</td>' +
                                                                                        '<td>' + (overtimeHours > 0 ? overtimeHours.toFixed(1) + ' hours' : '-') + '</td>' +
                                                                                        '<td>' +
                                                                                        '<span class="badge ' + getStatusBadgeClass(status) + '">' + status + '</span>' +
                                                                                        '</td>' +
                                                                                        '</tr>';
                                                                            });

                                                                            detailsHTML +=
                                                                                    '</tbody>' +
                                                                                    '</table>' +
                                                                                    '</div>';

                                                                            $('#detailsContent').html(detailsHTML);
                                                                        } else {
                                                                            $('#detailsContent').html(
                                                                                    '<div class="no-data">' +
                                                                                    '<i data-feather="calendar-x" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>' +
                                                                                    '<h5>No Schedule Details</h5>' +
                                                                                    '<p>No detailed schedule information available for this week.</p>' +
                                                                                    '</div>'
                                                                                    );
                                                                        }
                                                                        feather.replace();
                                                                    },
                                                                    error: function () {
                                                                        $('#detailsContent').html(
                                                                                '<div class="no-data">' +
                                                                                '<i data-feather="alert-circle" style="width: 48px; height: 48px; margin-bottom: 16px;"></i>' +
                                                                                '<h5>Error Loading Data</h5>' +
                                                                                '<p>Unable to load schedule details. Please try again.</p>' +
                                                                                '</div>'
                                                                                );
                                                                        feather.replace();
                                                                    }
                                                                });
                                                            }

                                                            // Make functions globally available (just in case)
                                                            window.viewWeekPerformance = viewWeekPerformance;
                                                            window.viewWeekDetails = viewWeekDetails;
                                                            window.switchTab = switchTab;

                                                            console.log('Functions defined:', {
                                                                viewWeekPerformance: typeof window.viewWeekPerformance,
                                                                viewWeekDetails: typeof window.viewWeekDetails,
                                                                switchTab: typeof window.switchTab
                                                            });

                                                            // Helper function to get status badge CSS class
                                                            function getStatusBadgeClass(status) {
                                                                switch (status.toLowerCase()) {
                                                                    case 'completed':
                                                                        return 'bg-success';
                                                                    case 'scheduled':
                                                                        return 'bg-primary';
                                                                    case 'cancelled':
                                                                        return 'bg-danger';
                                                                    default:
                                                                        return 'bg-secondary';
                                                                }
                                                            }

                                                            $(document).ready(function () {

                                                                feather.replace();

                                                                // Initialize page
                                                                initializeDateSelectors();

                                                                // Auto-hide alerts
                                                                setTimeout(function () {
                                                                    $('.alert').fadeOut('slow');
                                                                }, 5000);

                                                                // Form validation for salary filter
                                                                $('#salaryFilterForm').on('submit', function (e) {
                                                                    console.log('Filter form submitted');
                                                                    const startMonth = parseInt($('#startMonth').val()) || 0;
                                                                    const startYear = parseInt($('#startYear').val()) || 0;
                                                                    const endMonth = parseInt($('#endMonth').val()) || 0;
                                                                    const endYear = parseInt($('#endYear').val()) || 0;

                                                                    console.log('Form data:', {startMonth, startYear, endMonth, endYear});
                                                                    console.log('Form action:', this.action);
                                                                    console.log('Form method:', this.method);

                                                                    // Only validate if both start and end are selected
                                                                    if (startMonth && startYear && endMonth && endYear) {
                                                                        const startDate = new Date(startYear, startMonth - 1, 1);
                                                                        const endDate = new Date(endYear, endMonth - 1, 1);

                                                                        if (startDate > endDate) {
                                                                            e.preventDefault();
                                                                            alert('Start month/year cannot be after end month/year.');
                                                                            return false;
                                                                        }
                                                                    }

                                                                    console.log('Form validation passed, submitting...');
                                                                    // Allow form to submit normally
                                                                    return true;
                                                                });
                                                            });

                                                            // Initialize date selectors with years and current values
                                                            function initializeDateSelectors() {
                                                                // Populate year dropdowns
                                                                const currentYear = new Date().getFullYear();
                                                                const startYear = currentYear - 5; // Show last 5 years
                                                                const endYear = currentYear + 1;   // Include next year

                                                                const startYearSelect = $('#startYear');
                                                                const endYearSelect = $('#endYear');

                                                                for (let year = startYear; year <= endYear; year++) {
                                                                    startYearSelect.append('<option value="' + year + '">' + year + '</option>');
                                                                    endYearSelect.append('<option value="' + year + '">' + year + '</option>');
                                                                }

                                                                // Set current values from server if available
                                                                const urlParams = new URLSearchParams(window.location.search);
                                                                const startMonth = urlParams.get('startMonth');
                                                                const startYearParam = urlParams.get('startYear');
                                                                const endMonth = urlParams.get('endMonth');
                                                                const endYearParam = urlParams.get('endYear');
                                                                const view = urlParams.get('view');

                                                                if (startMonth)
                                                                    $('#startMonth').val(startMonth);
                                                                if (startYearParam)
                                                                    $('#startYear').val(startYearParam);
                                                                if (endMonth)
                                                                    $('#endMonth').val(endMonth);
                                                                if (endYearParam)
                                                                    $('#endYear').val(endYearParam);

                                                                // Check if we should switch to salary tab based on form submission
                                                                const hasFilters = urlParams.get('startMonth') || urlParams.get('startYear') ||
                                                                        urlParams.get('endMonth') || urlParams.get('endYear');

                                                                // Check URL parameters to determine which tab should be active
                                                                const currentTab = urlParams.get('currentTab') || 'schedule';

                                                                // If there's a currentTab parameter, switch to that tab
                                                                if (currentTab) {
                                                                    switchTab(currentTab);
                                                                } else if (hasFilters) {
                                                                    // If no specific tab but filters exist, default to salary since that's commonly filtered
                                                                    switchTab('salary');
                                                                } else {
                                                                    // Initialize with default filter appearance for schedule tab
                                                                    updateFilterForCurrentTab('schedule');
                                                                }
                                                            }



                                                            // Export specific week schedule data as CSV - direct download
                                                            function exportWeekCSV(weekScheduleID) {
                                                                console.log('Exporting CSV for schedule ID:', weekScheduleID);

                                                                // Show brief loading indicator on the button
                                                                var btn = $('[onclick*="exportWeekCSV(' + "'" + weekScheduleID + "'" + ')"]');
                                                                var originalHtml = btn.html();
                                                                btn.prop('disabled', true).html('<i data-feather="clock" style="width: 14px; height: 14px;"></i> Exporting...');
                                                                feather.replace();

                                                                // Start download immediately
                                                                window.location.href = 'exportWeekCSV.htm?weekScheduleID=' + weekScheduleID;

                                                                // Reset button after short delay and show success
                                                                setTimeout(function () {
                                                                    btn.prop('disabled', false).html(originalHtml);
                                                                    feather.replace();
                                                                    showExportSuccessToast();
                                                                }, 1500);
                                                            }

                                                            // Show success toast notification
                                                            function showExportSuccessToast() {
                                                                // Create a toast notification
                                                                var toastHtml = '<div class="toast align-items-center text-white bg-success border-0 position-fixed" style="top: 20px; right: 20px; z-index: 9999;" role="alert" aria-live="assertive" aria-atomic="true">' +
                                                                        '<div class="d-flex">' +
                                                                        '<div class="toast-body">' +
                                                                        '<i data-feather="check-circle" class="me-2"></i>CSV file downloaded successfully!' +
                                                                        '</div>' +
                                                                        '<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>' +
                                                                        '</div>' +
                                                                        '</div>';

                                                                $('body').append(toastHtml);
                                                                var toast = new bootstrap.Toast($('.toast').last()[0], {delay: 4000});
                                                                toast.show();
                                                                feather.replace();

                                                                // Remove toast element after it's hidden
                                                                $('.toast').last().on('hidden.bs.toast', function () {
                                                                    $(this).remove();
                                                                });
                                                            }


                                                            // Print specific week payroll report
                                                            function printWeekPayroll(weekScheduleID) {
                                                                console.log('Printing week payroll for schedule ID:', weekScheduleID);
                                                                const printUrl = 'printWeekPayroll.htm?weekScheduleID=' + weekScheduleID;
                                                                window.open(printUrl, '_blank');
                                                            }

                                                            // Check schedule on specific date (utility function)
                                                            function checkScheduleOnDate(date) {
                                                                return $.get('schedule-check', {date: date});
                                                            }

                                                            // Mobile-specific enhancements
                                                            function initMobileEnhancements() {
                                                                // Add touch-friendly interactions
                                                                if ('ontouchstart' in window) {
                                                                    // Add mobile class to body
                                                                    $('body').addClass('mobile-device');

                                                                    // Improve button touch targets
                                                                    $('.view-detail-btn').css('min-height', '44px');
                                                                    $('.custom-tab').css('min-height', '44px');

                                                                    // Prevent double-tap zoom on buttons
                                                                    $('.view-detail-btn, .custom-tab, .btn').css('touch-action', 'manipulation');
                                                                }

                                                                // Responsive modal adjustments
                                                                function adjustModalForMobile() {
                                                                    const modalDialog = $('.modal-dialog');
                                                                    if ($(window).width() <= 576) {
                                                                        modalDialog.removeClass('modal-lg modal-xl');
                                                                    } else if ($(window).width() <= 768) {
                                                                        modalDialog.removeClass('modal-xl').addClass('modal-lg');
                                                                    }
                                                                }

                                                                // Apply modal adjustments
                                                                adjustModalForMobile();
                                                                $(window).resize(adjustModalForMobile);

                                                                // Smooth scrolling for mobile
                                                                if ($(window).width() <= 768) {
                                                                    $('html').css('scroll-behavior', 'smooth');
                                                                }

                                                                // Auto-hide alerts on mobile after shorter time
                                                                if ($(window).width() <= 576) {
                                                                    setTimeout(function () {
                                                                        $('.alert').fadeOut('slow');
                                                                    }, 3000);
                                                                }

                                                                // Add swipe gesture support for tab switching on mobile
                                                                if ($(window).width() <= 576) {
                                                                    let startX = 0;
                                                                    let endX = 0;

                                                                    $('.tab-content').on('touchstart', function (e) {
                                                                        startX = e.originalEvent.touches[0].clientX;
                                                                    });

                                                                    $('.tab-content').on('touchend', function (e) {
                                                                        endX = e.originalEvent.changedTouches[0].clientX;
                                                                        const difference = startX - endX;

                                                                        // Swipe left (next tab)
                                                                        if (difference > 50) {
                                                                            const currentTab = $('.custom-tab.active').index();
                                                                            const tabs = $('.custom-tab');
                                                                            if (currentTab < tabs.length - 1) {
                                                                                $(tabs[currentTab + 1]).click();
                                                                            }
                                                                        }
                                                                        // Swipe right (previous tab)
                                                                        else if (difference < -50) {
                                                                            const currentTab = $('.custom-tab.active').index();
                                                                            const tabs = $('.custom-tab');
                                                                            if (currentTab > 0) {
                                                                                $(tabs[currentTab - 1]).click();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            // Initialize mobile enhancements
                                                            initMobileEnhancements();

                                                            // Re-run on window resize
                                                            $(window).resize(function () {
                                                                setTimeout(function () {
                                                                    feather.replace();
                                                                    initMobileEnhancements();
                                                                }, 100);
                                                            });

                                                        } catch (error) {
                                                            console.error('JavaScript error in main script:', error);
                                                            // Define switchTab as a simple fallback function
                                                            window.switchTab = function (tabName) {
                                                                console.log('Fallback switchTab called for:', tabName);
                                                                $('.custom-tab').removeClass('active');
                                                                $('[onclick="switchTab(\'' + tabName + '\')"').addClass('active');
                                                                $('.tab-content').removeClass('active');
                                                                $('#' + tabName + '-content').addClass('active');
                                                                $('#currentTab').val(tabName);
                                                            };
                                                        }

        </script>
    </body>
</html>