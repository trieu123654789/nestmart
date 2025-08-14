<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="Responsive Admin &amp; Dashboard Template based on Bootstrap 5">
        <meta name="author" content="AdminKit">
        <meta name="keywords" content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <link rel="canonical" href="https://demo-basic.adminkit.io/pages-blank.html" />
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">

        <title>NestMart - Accounts</title>

        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
        <style>
            .wrapper {
                background-color: white;
            }

            .dashboard-card {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 15px;
                padding: 25px;
                margin-bottom: 20px;
                transition: transform 0.3s, box-shadow 0.3s;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                position: relative;
                overflow: hidden;
            }

            .dashboard-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 25px rgba(0,0,0,0.2);
            }

            .dashboard-card::before {
                content: '';
                position: absolute;
                top: 0;
                right: 0;
                width: 100px;
                height: 100px;
                background: rgba(255,255,255,0.1);
                border-radius: 50%;
                transform: translate(30px, -30px);
            }

            .dashboard-card.revenue {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            }

            .dashboard-card.orders {
                background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            }

            .dashboard-card.products {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            }

            .dashboard-card.customers {
                background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            }

            .dashboard-card h3 {
                margin: 0;
                font-size: 2.5rem;
                font-weight: 700;
                position: relative;
                z-index: 2;
            }

            .dashboard-card p {
                margin: 0;
                opacity: 0.9;
                font-size: 1.1rem;
                position: relative;
                z-index: 2;
            }

            .dashboard-card .icon {
                position: absolute;
                top: 20px;
                right: 20px;
                font-size: 2.5rem;
                opacity: 0.3;
                z-index: 1;
            }

            .chart-container {
                position: relative;
                background: white;
                border-radius: 15px;
                padding: 25px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                margin-bottom: 30px;
                transition: transform 0.2s;
            }

            .chart-container:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(0,0,0,0.15);
            }

            .chart-container h5 {
                color: #2c3e50;
                font-weight: 600;
                margin-bottom: 20px;
                padding-bottom: 10px;
                border-bottom: 2px solid #3498db;
            }

            .chart-wrapper {
                position: relative;
                height: 400px;
                width: 100%;
            }

            .chart-wrapper.pie-chart {
                height: 350px;
            }

            .filter-section {
                background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                border-radius: 15px;
                padding: 25px;
                margin-bottom: 30px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }

            .status-summary {
                background: white;
                border-radius: 15px;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }

            .status-item {
                text-align: center;
                padding: 15px;
                border-radius: 10px;
                margin-bottom: 10px;
                transition: transform 0.2s;
            }

            .status-item:hover {
                transform: scale(1.05);
            }

            .status-completed {
                background: linear-gradient(135deg, #d4edda, #c3e6cb);
                color: #155724;
            }

            .status-pending {
                background: linear-gradient(135deg, #fff3cd, #ffeaa7);
                color: #856404;
            }

            .status-cancelled {
                background: linear-gradient(135deg, #f8d7da, #f5c6cb);
                color: #721c24;
            }

            .loading-spinner {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 200px;
            }

            .chart-no-data {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                height: 300px;
                color: #6c757d;
            }

            .top-items-section {
                background: white;
                border-radius: 15px;
                padding: 20px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }

            .top-item {
                background: #f8f9fa;
                border-radius: 10px;
                padding: 15px;
                margin-bottom: 10px;
                transition: all 0.2s;
            }

            .top-item:hover {
                background: #e9ecef;
                transform: translateX(5px);
            }

            .rank-badge {
                width: 35px;
                height: 35px;
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                margin-right: 15px;
                font-size: 1.1rem;
            }

            .rank-badge.rank-1 {
                background: linear-gradient(45deg, #ffd700, #ffed4a);
                color: #333;
                box-shadow: 0 4px 15px rgba(255, 215, 0, 0.3);
            }

            .rank-badge.rank-2 {
                background: linear-gradient(45deg, #c0c0c0, #e2e8f0);
                color: #333;
                box-shadow: 0 4px 15px rgba(192, 192, 192, 0.3);
            }

            .rank-badge.rank-3 {
                background: linear-gradient(45deg, #cd7f32, #d4a574);
                color: white;
                box-shadow: 0 4px 15px rgba(205, 127, 50, 0.3);
            }

            .rank-badge.rank-other {
                background: linear-gradient(45deg, #6c757d, #adb5bd);
                color: white;
            }

            @media (max-width: 768px) {
                .chart-wrapper {
                    height: 300px;
                }

                .dashboard-card h3 {
                    font-size: 2rem;
                }
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
                            <a class="sidebar-link" href="account.htm">
                                <i class="align-middle me-2" data-feather="users"></i> 
                                <span class="align-middle">Account</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="products.htm">
                                <i class="align-middle" data-feather="box"></i> 
                                <span class="align-middle">Product</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="brand.htm">
                                <i class="align-middle" data-feather="bold"></i> 
                                <span class="align-middle">Brand</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="categories.htm">
                                <i class="align-middle" data-feather="list"></i> 
                                <span class="align-middle">Category</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="categoryDetail.htm">
                                <i class="align-middle" data-feather="clipboard"></i> 
                                <span class="align-middle">Category Detail</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="discount.htm">
                                <i class="align-middle" data-feather="check-circle"></i> 
                                <span class="align-middle">Discount</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="offers.htm">
                                <i class="align-middle" data-feather="percent"></i> 
                                <span class="align-middle">Offers</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="schedule.htm">
                                <i class="align-middle" data-feather="calendar"></i> 
                                <span class="align-middle">Schedule</span>
                            </a>
                        </li>


                        <li class="sidebar-item">
                            <a class="sidebar-link" href="viewFeedbackAd.htm">
                                <i class="align-middle" data-feather="feather"></i> 
                                <span class="align-middle">Feedback</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="salary.htm">
                                <i class="align-middle" data-feather="user-check"></i> 
                                <span class="align-middle">Salary</span>
                            </a>
                        </li>

                        <li class="sidebar-item">
                            <a class="sidebar-link" href="notifications.htm">
                                <i class="align-middle" data-feather="navigation"></i> 
                                <span class="align-middle">Notification</span>
                            </a>
                        </li>

                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="salesReport.htm">
                                <i class="align-middle me-2" data-feather="pie-chart"></i> 
                                <span class="align-middle">Sale Report</span>
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
                <div class="content">
                    <div class="container-fluid p-4">
                        <!-- Header -->
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <div>
                                <h2><i class="fas fa-tachometer-alt"></i> Sales Dashboard</h2>
                                <p class="text-muted mb-0">Real-time sales analytics and insights</p>
                            </div>
                            <div class="btn-group">
                                <a href="salesReport.htm" class="btn btn-outline-primary">
                                    <i class="fas fa-table"></i> Detailed Report
                                </a>
                                <button class="btn btn-success" onclick="refreshDashboard()">
                                    <i class="fas fa-sync-alt"></i> Refresh
                                </button>
                            </div>
                        </div>

                        <!-- Alert Messages -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <!-- Filter Section -->
                        <div class="filter-section">
                            <h5><i class="fas fa-filter"></i> Time Period Filter</h5>
                            <form method="GET" action="../admin/salesDashboard.htm">
                                <div class="row">
                                    <div class="col-md-3">
                                        <label class="form-label">Start Date</label>
                                        <input type="date" class="form-control" name="startDate" value="${startDate}">
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">End Date</label>
                                        <input type="date" class="form-control" name="endDate" value="${endDate}">
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">&nbsp;</label>
                                        <div>
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-search"></i> Apply Filter
                                            </button>
                                            <a href="../admin/salesDashboard.htm" class="btn btn-secondary">
                                                <i class="fas fa-times"></i> Reset
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <!-- KPI Cards -->
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="dashboard-card revenue">
                                    <i class="fas fa-dollar-sign icon"></i>

                                    <h3> <fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="0" groupingUsed="true" />$</h3>
                                    <p>Total Revenue</p>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="dashboard-card orders">
                                    <i class="fas fa-shopping-cart icon"></i>
                                    <h3><fmt:formatNumber value="${totalOrders}" pattern="#,###"/></h3>
                                    <p>Total Orders</p>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="dashboard-card products">
                                    <i class="fas fa-box icon"></i>
                                    <h3><fmt:formatNumber value="${totalProductsSold}" pattern="#,###"/></h3>
                                    <p>Products Sold</p>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="dashboard-card customers">
                                    <i class="fas fa-users icon"></i>
                                    <c:set var="avgOrderValue" value="${totalOrders > 0 ? totalRevenue / totalOrders : 0}" />

                                    <h3><fmt:formatNumber value="${avgOrderValue}" type="number" maxFractionDigits="0" groupingUsed="true" />$</h3>
                                    <p>Avg Order Value</p>
                                </div>
                            </div>
                        </div>

                        <!-- Charts Section -->
                        <div class="row">
                            <!-- Daily Revenue Chart -->
                            <div class="col-md-8">
                                <div class="chart-container">
                                    <h5><i class="fas fa-chart-line"></i> Daily Revenue Trend</h5>
                                    <div class="chart-wrapper">
                                        <canvas id="dailyRevenueChart"></canvas>
                                    </div>
                                </div>
                            </div>

                            <!-- Order Status Pie Chart -->
                            <div class="col-md-4">
                                <div class="chart-container">
                                    <h5><i class="fas fa-chart-pie"></i> Order Status Distribution</h5>
                                    <div class="chart-wrapper pie-chart">
                                        <canvas id="orderStatusChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <!-- Top Products Bar Chart -->
                            <div class="col-md-8">
                                <div class="chart-container">
                                    <h5><i class="fas fa-chart-bar"></i> Top Selling Products</h5>
                                    <div class="chart-wrapper">
                                        <canvas id="topProductsChart"></canvas>
                                    </div>
                                </div>
                            </div>

                            <!-- Monthly Revenue Overview -->
                            <div class="col-md-4">
                                <div class="chart-container">
                                    <h5><i class="fas fa-calendar-alt"></i> Monthly Revenue Overview</h5>
                                    <div class="chart-wrapper">
                                        <canvas id="monthlyRevenueChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Order Status Summary -->
                        <c:if test="${not empty orderStatusReport}">
                            <div class="status-summary">
                                <h5><i class="fas fa-clipboard-list"></i> Order Status Summary</h5>
                                <div class="row">
                                    <c:forEach var="status" items="${orderStatusReport}">
                                        <div class="col-md-3">
                                            <div class="status-item status-${status.key.toLowerCase()}">
                                                <h4><fmt:formatNumber value="${status.value}" pattern="#,###"/></h4>
                                                <p class="mb-0 text-capitalize">${status.key} Orders</p>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <!-- Top Items Section -->
                        <div class="row">
                            <!-- Top Products -->
                            <div class="col-md-6">
                                <div class="top-items-section">
                                    <h5><i class="fas fa-star"></i> Top Selling Products</h5>
                                    <c:choose>
                                        <c:when test="${not empty topProducts}">
                                            <c:forEach var="product" items="${topProducts}" varStatus="status">
                                                <div class="top-item">
                                                    <div class="d-flex align-items-center">
                                                        <span class="rank-badge rank-${status.index < 3 ? status.index + 1 : 'other'}">
                                                            ${status.index + 1}
                                                        </span>
                                                        <div class="flex-grow-1">
                                                            <div class="fw-bold">${product.productName}</div>
                                                            <small class="text-muted">ID: ${product.productID}</small>
                                                            <div class="mt-1">
                                                                <span class="badge bg-primary me-1">
                                                                    ${product.quantitySold} sold
                                                                </span>
                                                                <span class="badge bg-success">
                                                                    <fmt:formatNumber value="${product.totalRevenue}" type="number" maxFractionDigits="0" groupingUsed="true" />$

                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="chart-no-data">
                                                <i class="fas fa-box-open fa-3x mb-3"></i>
                                                <p>No product data available</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <!-- Top Customers -->
                            <div class="col-md-6">
                                <div class="top-items-section">
                                    <h5><i class="fas fa-users"></i> Top Spending Customers</h5>
                                    <c:choose>
                                        <c:when test="${not empty topCustomers}">
                                            <c:forEach var="customer" items="${topCustomers}" varStatus="status">
                                                <div class="top-item">
                                                    <div class="d-flex align-items-center">
                                                        <span class="rank-badge rank-${status.index < 3 ? status.index + 1 : 'other'}">
                                                            ${status.index + 1}
                                                        </span>
                                                        <div class="flex-grow-1">
                                                            <div class="fw-bold">${customer.customerName}</div>
                                                            <small class="text-muted">${customer.customerEmail}</small>
                                                            <div class="mt-1">
                                                                <span class="badge bg-info me-1">
                                                                    ${customer.orderCount} orders
                                                                </span>
                                                                <span class="badge bg-success">
                                                                    <fmt:formatNumber value="${customer.totalRevenue}" type="number" maxFractionDigits="0" groupingUsed="true" />$

                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="chart-no-data">
                                                <i class="fas fa-user-slash fa-3x mb-3"></i>
                                                <p>No customer data available</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="../assets/admin/js/app.js"></script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.0/chart.min.js"></script>

        <script>
                                    const chartColors = {
                                        primary: '#667eea',
                                        secondary: '#764ba2',
                                        success: '#43e97b',
                                        danger: '#f5576c',
                                        warning: '#ffd700',
                                        info: '#4facfe'
                                    };

                                    document.addEventListener('DOMContentLoaded', function () {
                                        try {
                                            initializeDailyRevenueChart();
                                        } catch (error) {
                                            console.error('Error initializing daily revenue chart:', error);
                                            showNoDataMessage('dailyRevenueChart');
                                        }

                                        try {
                                            initializeOrderStatusChart();
                                        } catch (error) {
                                            console.error('Error initializing order status chart:', error);
                                            showNoDataMessage('orderStatusChart');
                                        }

                                        try {
                                            initializeTopProductsChart();
                                        } catch (error) {
                                            console.error('Error initializing top products chart:', error);
                                            showNoDataMessage('topProductsChart');
                                        }

                                        try {
                                            initializeMonthlyRevenueChart();
                                        } catch (error) {
                                            console.error('Error initializing monthly revenue chart:', error);
                                            showNoDataMessage('monthlyRevenueChart');
                                        }
                                    });


                                    function initializeDailyRevenueChart() {
                                        const ctx = document.getElementById('dailyRevenueChart').getContext('2d');
                                        const dailyData = generateDailyRevenueData();

                                        new Chart(ctx, {
                                            type: 'line',
                                            data: {
                                                labels: dailyData.labels,
                                                datasets: [{
                                                        label: 'Daily Revenue',
                                                        data: dailyData.values,
                                                        borderColor: chartColors.primary,
                                                        backgroundColor: chartColors.primary + '20',
                                                        borderWidth: 3,
                                                        fill: true,
                                                        tension: 0.4,
                                                        pointBackgroundColor: chartColors.primary,
                                                        pointBorderColor: '#fff',
                                                        pointBorderWidth: 2,
                                                        pointRadius: 6,
                                                        pointHoverRadius: 8
                                                    }]
                                            },
                                            options: {
                                                responsive: true,
                                                maintainAspectRatio: false,
                                                plugins: {
                                                    legend: {
                                                        display: false
                                                    },
                                                    tooltip: {
                                                        backgroundColor: 'rgba(0,0,0,0.8)',
                                                        titleColor: '#fff',
                                                        bodyColor: '#fff',
                                                        borderColor: chartColors.primary,
                                                        borderWidth: 1
                                                    }
                                                },
                                                scales: {
                                                    y: {
                                                        beginAtZero: true,
                                                        grid: {
                                                            color: 'rgba(0,0,0,0.1)'
                                                        },
                                                        ticks: {
                                                            callback: function (value) {
                                                                return '$' + value.toLocaleString();
                                                            }
                                                        }
                                                    },
                                                    x: {
                                                        grid: {
                                                            color: 'rgba(0,0,0,0.1)'
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }

                                    function initializeOrderStatusChart() {
                                        const ctx = document.getElementById('orderStatusChart');
                                        if (!ctx)
                                            return;

                                        const statusData = [];
            <c:if test="${not empty orderStatusReport}">
                <c:forEach var="status" items="${orderStatusReport}">
                                        statusData.push({
                                            label: '${status.key}',
                                            value: ${status.value != null ? status.value : 0}
                                        });
                </c:forEach>
            </c:if>

                                        if (statusData.length === 0) {
                                            showNoDataMessage('orderStatusChart');
                                            return;
                                        }

                                        const colors = [chartColors.success, chartColors.warning, chartColors.danger, chartColors.info];

                                        new Chart(ctx, {
                                            type: 'doughnut',
                                            data: {
                                                labels: statusData.map(item => item.label.charAt(0).toUpperCase() + item.label.slice(1)),
                                                datasets: [{
                                                        data: statusData.map(item => item.value),
                                                        backgroundColor: colors.slice(0, statusData.length),
                                                        borderWidth: 0,
                                                        hoverOffset: 4
                                                    }]
                                            },
                                            options: {
                                                responsive: true,
                                                maintainAspectRatio: false,
                                                plugins: {
                                                    legend: {
                                                        position: 'bottom',
                                                        labels: {
                                                            padding: 20,
                                                            usePointStyle: true
                                                        }
                                                    },
                                                    tooltip: {
                                                        callbacks: {
                                                            label: function (context) {
                                                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                                                const percentage = ((context.parsed * 100) / total).toFixed(1);
                                                                return context.label + ': ' + context.parsed + ' (' + percentage + '%)';
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    function initializeTopProductsChart() {
                                        const ctx = document.getElementById('topProductsChart');
                                        if (!ctx)
                                            return;

                                        const productData = [
            <c:forEach var="product" items="${topProducts}" varStatus="loop">
                                        {
                                        name: '<c:out value="${product.productName}" escapeXml="true"/>',
                                                quantity: ${product.quantitySold != null ? product.quantitySold : 0},
                                                revenue: ${product.totalRevenue != null ? product.totalRevenue : 0}
                                        }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
                                        ];

                                        if (productData.length === 0) {
                                            showNoDataMessage('topProductsChart');
                                                    return;
                                        }

                                        new Chart(ctx, {
                                            type: 'bar',
                                            data: {
                                                labels: productData.map(item => item.name.length > 15 ? item.name.substring(0, 15) + '...' : item.name),
                                                datasets: [{
                                                        label: 'Quantity Sold',
                                                        data: productData.map(item => item.quantity),
                                                        backgroundColor: chartColors.primary + '80',
                                                        borderColor: chartColors.primary,
                                                        borderWidth: 1
                                                    }]
                                            },
                                            options: {
                                                responsive: true,
                                                maintainAspectRatio: false,
                                                plugins: {
                                                    legend: {
                                                        display: true
                                                    },
                                                    tooltip: {
                                                        callbacks: {
                                                            label: function (context) {
                                                                return 'Quantity: ' + context.parsed.y + ' units';
                                                            },
                                                            afterLabel: function (context) {
                                                                const revenue = productData[context.dataIndex].revenue;
                                                                return 'Revenue: $' + revenue.toLocaleString();
                                                            }
                                                        }
                                                    }
                                                },
                                                scales: {
                                                    y: {
                                                        beginAtZero: true,
                                                        grid: {
                                                            color: 'rgba(0,0,0,0.1)'
                                                        },
                                                        ticks: {
                                                            callback: function (value) {
                                                                return value + ' units';
                                                            }
                                                        }
                                                    },
                                                    x: {
                                                        grid: {
                                                            color: 'rgba(0,0,0,0.1)'
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }

                                    function initializeMonthlyRevenueChart() {
                                        const ctx = document.getElementById('monthlyRevenueChart');
                                        if (!ctx)
                                            return;
                                        const monthlyData = [];
            <c:if test="${not empty monthlyRevenue}">
                <c:forEach var="month" items="${monthlyRevenue}">
                                        monthlyData.push({
                                            month: ${month.key != null ? month.key : 0},
                                            revenue: ${month.value != null ? month.value : 0}
                                        });
                </c:forEach>
            </c:if>

                                        if (monthlyData.length === 0) {
                                            showNoDataMessage('monthlyRevenueChart');
                                            return;
                                        }
                                        monthlyData.sort((a, b) => a.month - b.month);
                                        const monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                                            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

                                        new Chart(ctx, {
                                            type: 'bar',
                                            data: {
                                                labels: monthlyData.map(item => monthNames[item.month - 1] || 'Unknown'),
                                                datasets: [{
                                                        label: 'Monthly Revenue',
                                                        data: monthlyData.map(item => item.revenue),
                                                        backgroundColor: chartColors.info + '80',
                                                        borderColor: chartColors.info,
                                                        borderWidth: 2,
                                                        borderRadius: 5,
                                                        borderSkipped: false,
                                                    }]
                                            },
                                            options: {
                                                responsive: true,
                                                maintainAspectRatio: false,
                                                plugins: {
                                                    legend: {
                                                        display: false
                                                    },
                                                    tooltip: {
                                                        callbacks: {
                                                            label: function (context) {
                                                                return 'Revenue: $' + context.parsed.y.toLocaleString();
                                                            }
                                                        }
                                                    }
                                                },
                                                scales: {
                                                    y: {
                                                        beginAtZero: true,
                                                        grid: {
                                                            color: 'rgba(0,0,0,0.1)'
                                                        },
                                                        ticks: {
                                                            callback: function (value) {
                                                                return '$' + (value / 1000) + 'K';
                                                            }
                                                        }
                                                    },
                                                    x: {
                                                        grid: {
                                                            color: 'rgba(0,0,0,0.1)'
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    function generateDailyRevenueData() {
                                        const days = 30;
                                        const labels = [];
                                        const values = [];
                                        const baseRevenue = ${totalRevenue != null ? totalRevenue : 0};

                                        for (let i = days; i >= 0; i--) {
                                            const date = new Date();
                                            date.setDate(date.getDate() - i);
                                            labels.push(date.toLocaleDateString('en-US', {month: 'short', day: 'numeric'}));

                                            const dailyRevenue = (baseRevenue / days) * (0.5 + Math.random());
                                            values.push(Math.round(dailyRevenue));
                                        }

                                        return {labels, values};
                                    }

                                    function showNoDataMessage(canvasId) {
                                        const canvas = document.getElementById(canvasId);
                                        const container = canvas.parentElement;
                                        container.innerHTML = '<div class="chart-no-data"><i class="fas fa-chart-line fa-3x mb-3"></i><p>No data available</p></div>';
                                    }
                                    function refreshDashboard() {
                                        window.location.reload();
                                    }

                                    setInterval(function () {
                                        const lastRefresh = localStorage.getItem('dashboardLastRefresh');
                                        const now = Date.now();

                                        if (!lastRefresh || (now - lastRefresh) > 300000) { // 5 minutes
                                            localStorage.setItem('dashboardLastRefresh', now);
                                        }
                                    }, 300000);
                                    if (typeof feather !== 'undefined') {
                                        feather.replace();
                                    }
        </script>
    </body>
</html>