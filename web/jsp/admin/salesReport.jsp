<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
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

            .stats-card {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 15px;
                padding: 25px;
                margin-bottom: 20px;
                transition: transform 0.2s;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .stats-card:hover {
                transform: translateY(-5px);
            }
            .stats-card h3 {
                margin: 0;
                font-size: 2.2rem;
                font-weight: 700;
            }
            .stats-card p {
                margin: 0;
                opacity: 0.9;
                font-size: 1.1rem;
            }
            .filter-section {
                background-color: #f8f9fa;
                padding: 20px;
                border-radius: 10px;
                margin-bottom: 20px;
            }
            .table-responsive {
                background: white;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .status-badge {
                padding: 5px 10px;
                border-radius: 15px;
                font-size: 0.8rem;
                font-weight: bold;
            }
            .status-completed {
                background-color: #d4edda;
                color: #155724;
            }
            .status-pending {
                background-color: #fff3cd;
                color: #856404;
            }
            .status-cancelled {
                background-color: #f8d7da;
                color: #721c24;
            }

            .enhanced-summary {
                background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                border-radius: 15px;
                padding: 25px;
                margin-bottom: 30px;
                box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            }

            .top-item {
                background: white;
                border-radius: 10px;
                padding: 15px;
                margin-bottom: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.05);
                transition: transform 0.2s;
            }

            .top-item:hover {
                transform: scale(1.02);
            }

            .rank-badge {
                background: linear-gradient(45deg, #ff6b6b, #ee5a52);
                color: white;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                margin-right: 15px;
            }

            .rank-badge.rank-1 {
                background: linear-gradient(45deg, #ffd700, #ffed4a);
                color: #333;
            }
            .rank-badge.rank-2 {
                background: linear-gradient(45deg, #c0c0c0, #e2e8f0);
                color: #333;
            }
            .rank-badge.rank-3 {
                background: linear-gradient(45deg, #cd7f32, #d4a574);
                color: white;
            }

            .aov-card {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 15px;
                padding: 20px;
                text-align: center;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }

            .summary-section {
                margin-bottom: 25px;
            }

            .summary-title {
                color: #2c3e50;
                font-weight: 600;
                margin-bottom: 15px;
                border-bottom: 2px solid #3498db;
                padding-bottom: 8px;
            }
            .wrapper{
                background-color: white;
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

                        <li class="sidebar-item active">
                            <a class="sidebar-link" href="salesReport.htm" >
                                <i class="align-middle me-2" data-feather="pie-chart"></i> <span class="align-middle">Sale Report</span>

                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
            <!-- Main Content -->
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
                <div class="p-4">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2><i class="fas fa-chart-line"></i> Sales Report</h2>
                        <div class="btn-group">
                            <a href="../admin/salesDashboard.htm" class="btn btn-primary">
                                <i class="fas fa-dashboard"></i> Dashboard
                            </a>
                            <button class="btn btn-success" onclick="exportToCSV()" id="exportBtn">
                                <i class="fas fa-download"></i> Export CSV
                            </button>
                        </div>
                    </div>


                    <!-- Alert Messages -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show">
                            <i class="fas fa-check-circle"></i> ${successMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Statistics Cards -->
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="stats-card">
                                <i class="fas fa-dollar-sign fa-2x float-end"></i>

                                <h3><fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="0" groupingUsed="true" />$</h3>
                                <p>Total Revenue</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stats-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                                <i class="fas fa-shopping-cart fa-2x float-end"></i>
                                <h3><fmt:formatNumber value="${totalOrders}" pattern="#,###"/></h3>
                                <p>Total Orders</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stats-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                                <i class="fas fa-box fa-2x float-end"></i>
                                <h3><fmt:formatNumber value="${totalProductsSold}" pattern="#,###"/></h3>
                                <p>Products Sold</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stats-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                                <i class="fas fa-file-alt fa-2x float-end"></i>
                                <h3><fmt:formatNumber value="${totalReports}" pattern="#,###"/></h3>
                                <p>Total Records</p>
                            </div>
                        </div>
                    </div>

                    <!-- Enhanced Summary Section -->
                    <div class="enhanced-summary">
                        <div class="row">
                            <!-- Average Order Value -->
                            <div class="col-md-4">
                                <div class="summary-section">
                                    <h5 class="summary-title">
                                        <i class="fas fa-calculator"></i> Average Order Value
                                    </h5>
                                    <div class="aov-card">
                                        <h3><fmt:formatNumber value="${averageOrderValue}" type="number" maxFractionDigits="0" groupingUsed="true" />$</h3>
                                        <p class="mb-0">AOV = Total Revenue ÷ Total Orders</p>
                                    </div>
                                </div>
                            </div>

                            <!-- Top Products -->
                            <div class="col-md-4">
                                <div class="summary-section">
                                    <h5 class="summary-title">
                                        <i class="fas fa-star"></i> Top Selling Products
                                    </h5>
                                    <c:choose>
                                        <c:when test="${not empty topProducts}">
                                            <c:forEach var="product" items="${topProducts}" varStatus="status">
                                                <div class="top-item">
                                                    <div class="d-flex align-items-center">
                                                        <span class="rank-badge rank-${status.index + 1}">${status.index + 1}</span>
                                                        <div class="flex-grow-1">
                                                            <div class="fw-bold">${product.productName}</div>
                                                            <small class="text-muted">ID: ${product.productID}</small>
                                                            <div class="mt-1">
                                                                <span class="badge bg-primary">${product.quantitySold} sold</span>
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
                                            <div class="text-center text-muted py-3">
                                                <i class="fas fa-box-open fa-2x"></i>
                                                <p class="mt-2">No product data available</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <!-- Top Customers -->
                            <div class="col-md-4">
                                <div class="summary-section">
                                    <h5 class="summary-title">
                                        <i class="fas fa-users"></i> Top Spending Customers
                                    </h5>
                                    <c:choose>
                                        <c:when test="${not empty topCustomers}">
                                            <c:forEach var="customer" items="${topCustomers}" varStatus="status">
                                                <div class="top-item">
                                                    <div class="d-flex align-items-center">
                                                        <span class="rank-badge rank-${status.index + 1}">${status.index + 1}</span>
                                                        <div class="flex-grow-1">
                                                            <div class="fw-bold">${customer.customerName}</div>
                                                            <small class="text-muted">${customer.customerEmail}</small>
                                                            <div class="mt-1">
                                                                <span class="badge bg-info">${customer.orderCount} orders</span>
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
                                            <div class="text-center text-muted py-3">
                                                <i class="fas fa-user-slash fa-2x"></i>
                                                <p class="mt-2">No customer data available</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Order Status Summary -->
                    <c:if test="${not empty orderStatusReport}">
                        <div class="row mb-4">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-header">
                                        <h5><i class="fas fa-chart-pie"></i> Order Status Summary</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <c:forEach var="status" items="${orderStatusReport}">
                                                <div class="col-md-3 text-center">
                                                    <div class="status-item status-${status.key.toLowerCase()}">
                                                        <h4>${status.value}</h4>
                                                        <p class="text-capitalize">${status.key}</p>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <div class="filter-section">
                        <h5><i class="fas fa-filter"></i> Filter Options</h5>
                        <div class="alert alert-info mb-3">
                            <i class="fas fa-info-circle"></i> 
                            <strong>Note:</strong> Leave date fields empty to show total revenue for all time periods.
                            Use one or both date fields to filter by specific time ranges.
                        </div>
                        <form method="GET" action="../admin/salesReport.htm">
                            <div class="row">
                                <div class="col-md-2">
                                    <label class="form-label">Start Date</label>
                                    <input type="date" class="form-control" name="startDate" value="${startDate}" 
                                           title="Leave empty to include all dates from the beginning">
                                </div>
                                <div class="col-md-2">
                                    <label class="form-label">End Date</label>
                                    <input type="date" class="form-control" name="endDate" value="${endDate}"
                                           title="Leave empty to include all dates up to now">
                                </div>
                                <div class="col-md-2">
                                    <label class="form-label">Product ID</label>
                                    <input type="text" class="form-control" name="productID" value="${productID}" placeholder="Enter Product ID">
                                </div>
                                <div class="col-md-2">
                                    <label class="form-label">Customer ID</label>
                                    <input type="text" class="form-control" name="customerID" value="${customerID}" placeholder="Enter Customer ID">
                                </div>
                                <div class="col-md-2">
                                    <label class="form-label">Page Size</label>
                                    <select class="form-control" name="pageSize">
                                        <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                                        <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                                        <option value="50" ${pageSize == 50 ? 'selected' : ''}>50</option>
                                        <option value="100" ${pageSize == 100 ? 'selected' : ''}>100</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label class="form-label">&nbsp;</label>
                                    <div>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-search"></i> Filter
                                        </button>
                                        <a href="../admin/salesReport.htm" class="btn btn-secondary">
                                            <i class="fas fa-times"></i> Clear
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="row mt-2">
                                <div class="col-12">
                                    <small class="text-muted">
                                        <i class="fas fa-lightbulb"></i> 
                                        Current filter: 
                                        <c:choose>
                                            <c:when test="${empty startDate and empty endDate}">
                                                <span class="badge bg-info">All Time</span>
                                            </c:when>
                                            <c:when test="${not empty startDate and not empty endDate}">
                                                <span class="badge bg-primary">From ${startDate} to ${endDate}</span>
                                            </c:when>
                                            <c:when test="${not empty startDate}">
                                                <span class="badge bg-primary">From ${startDate} onwards</span>
                                            </c:when>
                                            <c:when test="${not empty endDate}">
                                                <span class="badge bg-primary">Up to ${endDate}</span>
                                            </c:when>
                                        </c:choose>

                                        <c:if test="${not empty productID}">
                                            <span class="badge bg-success">Product: ${productID}</span>
                                        </c:if>

                                        <c:if test="${not empty customerID}">
                                            <span class="badge bg-warning">Customer: ${customerID}</span>
                                        </c:if>
                                    </small>
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- Sales Report Table -->
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>Order Date</th>
                                    <th>Product</th>
                                    <th>Customer</th>
                                    <th>Quantity</th>
                                    <th>Unit Price</th>
                                    <th>Revenue</th>
                                    <th>Status</th>
                                    <th>Contact</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty salesReports}">
                                        <tr>
                                            <td colspan="8" class="text-center py-4">
                                                <i class="fas fa-inbox fa-3x text-muted"></i>
                                                <p class="mt-2">No sales records found</p>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="report" items="${salesReports}">
                                            <tr>
                                                <td>
                                                    <fmt:formatDate value="${report.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </td>
                                                <td>
                                                    <div>
                                                        <strong>${report.productName}</strong>
                                                        <br><small class="text-muted">ID: ${report.productID}</small>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div>
                                                        <strong>${report.customerName}</strong>
                                                        <br><small class="text-muted">${report.customerEmail}</small>
                                                    </div>
                                                </td>
                                                <td class="text-center">
                                                    <span class="badge bg-info">${report.quantitySold}</span>
                                                </td>
                                                <td class="text-end">
                                                    <fmt:formatNumber value="${report.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$

                                                </td>
                                                <td class="text-end">

                                                    <strong><fmt:formatNumber value="${report.totalRevenue}" type="number" maxFractionDigits="0" groupingUsed="true" />$</strong>
                                                </td>
                                                <td>
                                                    <span class="status-badge status-${report.orderStatus.toLowerCase()}">
                                                        ${report.orderStatus}
                                                    </span>
                                                </td>
                                                <td>
                                                    <small>
                                                        <i class="fas fa-phone"></i> ${report.customerPhone}
                                                    </small>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Sales report pagination">
                            <ul class="pagination justify-content-center">
                                <!-- Previous -->
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link" href="?page=${currentPage - 1}&pageSize=${pageSize}&startDate=${startDate}&endDate=${endDate}&productID=${productID}&customerID=${customerID}">
                                            <i class="fas fa-chevron-left"></i>
                                        </a>
                                    </li>
                                </c:if>

                                <!-- Page numbers -->
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <c:if test="${i <= 3 || i > totalPages - 3 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&pageSize=${pageSize}&startDate=${startDate}&endDate=${endDate}&productID=${productID}&customerID=${customerID}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:if>
                                    <c:if test="${i == 3 && currentPage > 5}">
                                        <li class="page-item disabled">
                                            <span class="page-link">...</span>
                                        </li>
                                    </c:if>
                                    <c:if test="${i == currentPage + 2 && currentPage < totalPages - 4}">
                                        <li class="page-item disabled">
                                            <span class="page-link">...</span>
                                        </li>
                                    </c:if>
                                </c:forEach>

                                <!-- Next -->
                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link" href="?page=${currentPage + 1}&pageSize=${pageSize}&startDate=${startDate}&endDate=${endDate}&productID=${productID}&customerID=${customerID}">
                                            <i class="fas fa-chevron-right"></i>
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </c:if>
                </div>
            </div>
        </div>
        </div>
        </div>
        <script src="../assets/admin/js/app.js"></script>

        <script>
                                           feather.replace();
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                    function exportToCSV() {
                                        const exportBtn = document.getElementById('exportBtn');
                                        const originalContent = exportBtn.innerHTML;
                                        exportBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Exporting...';
                                        exportBtn.disabled = true;

                                        const params = new URLSearchParams(window.location.search);

                                        window.location.href = '../admin/exportSalesReport.htm?' + params.toString();

                                        setTimeout(function () {
                                            exportBtn.innerHTML = originalContent;
                                            exportBtn.disabled = false;
                                        }, 3000);
                                    }

                                    document.querySelector('select[name="pageSize"]').addEventListener('change', function () {
                                        this.form.submit();
                                    });

                                    function showExportSuccess() {
                                        const toast = document.createElement('div');
                                        toast.className = 'alert alert-success position-fixed';
                                        toast.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
                                        toast.innerHTML = '<i class="fas fa-check-circle"></i> Export completed successfully!';

                                        document.body.appendChild(toast);

                                        setTimeout(function () {
                                            document.body.removeChild(toast);
                                        }, 3000);
                                    }

                                    const urlParams = new URLSearchParams(window.location.search);
                                    if (urlParams.has('exported')) {
                                        setTimeout(showExportSuccess, 500);
                                    }
        </script>
    </body>
</html>