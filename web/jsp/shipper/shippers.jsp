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
            /* Base Styles */
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #fef9e7 0%, #f6f3f0 100%);
                min-height: 100vh;
                color: #333;
            }

            .content {
                padding: 1rem 0;
            }

            /* Header Styles */
            .page-header {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
                padding: 1.5rem 0;
                border-radius: 12px;
                margin-bottom: 1.5rem;
                box-shadow: 0 4px 15px rgba(255, 148, 4, 0.2);
            }

            .page-header h1 {
                font-size: 1.8rem;
                font-weight: 600;
                margin: 0;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3);
            }

            .page-header .subtitle {
                font-size: 0.95rem;
                opacity: 0.9;
                margin-top: 0.3rem;
            }

            /* Filter Section */
            .filter-section {
                background: white;
                padding: 1.2rem;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.06);
                margin-bottom: 1.5rem;
                border: 1px solid #e9ecef;
            }

            .filter-title {
                display: flex;
                align-items: center;
                margin-bottom: 1rem;
                color: #495057;
                font-weight: 600;
                font-size: 1rem;
            }

            .filter-title i {
                margin-right: 0.5rem;
                color: #FF9702;
            }

            .form-control {
                border: 1px solid #e9ecef;
                border-radius: 6px;
                padding: 0.5rem 0.75rem;
                font-size: 0.9rem;
                transition: all 0.3s ease;
                background: #f8f9fa;
            }

            .form-control:focus {
                border-color: #FF9702;
                box-shadow: 0 0 0 0.15rem rgba(255, 148, 4, 0.1);
                background: white;
            }

            .btn-filter {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                border: none;
                color: white;
                padding: 0.5rem 1.5rem;
                border-radius: 6px;
                font-weight: 600;
                transition: all 0.3s ease;
                box-shadow: 0 2px 8px rgba(255, 148, 4, 0.25);
            }

            .btn-filter:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(255, 148, 4, 0.35);
                color: white;
            }

            /* Order Cards */
            .order-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
                gap: 1rem;
                margin-top: 1.5rem;
            }

            .order-card {
                background: white;
                border-radius: 12px;
                padding: 1.2rem;
                box-shadow: 0 3px 12px rgba(0,0,0,0.06);
                border: 1px solid #e9ecef;
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
            }

            .order-card::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 3px;
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
            }

            .order-card:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(0,0,0,0.1);
            }

            .order-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 0.8rem;
                padding-bottom: 0.8rem;
                border-bottom: 1px solid #f8f9fa;
            }

            .order-title {
                color: #2d3748;
                font-size: 1.1rem;
                font-weight: 600;
                margin: 0;
                display: flex;
                align-items: center;
            }

            .order-title i {
                margin-right: 0.4rem;
                color: #FF9702;
                font-size: 0.9rem;
            }

            .order-total {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
                padding: 0.4rem 0.8rem;
                border-radius: 15px;
                font-weight: 600;
                font-size: 0.95rem;
                box-shadow: 0 2px 6px rgba(255, 148, 4, 0.2);
            }

            .order-details {
                display: grid;
                gap: 0.5rem;
                margin-bottom: 1rem;
            }

            .order-detail-row {
                display: flex;
                align-items: center;
                padding: 0.3rem 0;
            }

            .detail-icon {
                width: 32px;
                height: 32px;
                background: rgba(255, 148, 4, 0.1);
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 0.75rem;
                color: #FF9702;
                font-size: 0.8rem;
            }

            .detail-content {
                flex: 1;
            }

            .detail-label {
                color: #718096;
                font-size: 0.75rem;
                font-weight: 500;
                text-transform: uppercase;
                letter-spacing: 0.3px;
                margin-bottom: 0.1rem;
            }

            .detail-value {
                color: #2d3748;
                font-weight: 600;
                font-size: 0.85rem;
            }

            /* Status Badges */
            .status-container {
                display: flex;
                gap: 0.5rem;
                margin: 0.8rem 0;
                flex-wrap: wrap;
            }

            .status-badge {
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

            .status-badge i {
                margin-right: 0.3rem;
                font-size: 0.7rem;
            }

            .badge-pending {
                background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%);
                color: #856404;
                border: 1px solid #f5c842;
            }

            .badge-confirmed {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
            }

            .badge-delivering {
                background: linear-gradient(135deg, #e67e22 0%, #d35400 100%);
                color: white;
            }

            .badge-completed {
                background: linear-gradient(135deg, #27ae60 0%, #229954 100%);
                color: white;
            }

            .badge-success {
                background: linear-gradient(135deg, #27ae60 0%, #229954 100%);
                color: white;
            }

            .badge-info {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                color: white;
            }

            /* Action Buttons */
            .order-actions {
                margin-top: 1rem;
                padding-top: 0.8rem;
                border-top: 1px solid #f8f9fa;
            }

            .btn-action {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                border: none;
                color: white;
                padding: 0.6rem 1.2rem;
                border-radius: 18px;
                font-weight: 600;
                transition: all 0.3s ease;
                box-shadow: 0 2px 8px rgba(255, 148, 4, 0.25);
                width: 100%;
                font-size: 0.85rem;
            }

            .btn-action:hover {
                transform: translateY(-1px);
                box-shadow: 0 4px 12px rgba(255, 148, 4, 0.35);
                color: white;
            }

            .btn-confirm {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                box-shadow: 0 2px 8px rgba(255, 148, 4, 0.25);
            }

            .btn-confirm:hover {
                box-shadow: 0 4px 12px rgba(255, 148, 4, 0.35);
            }

            .btn-deliver {
                background: linear-gradient(135deg, #e67e22 0%, #d35400 100%);
                box-shadow: 0 2px 8px rgba(230, 126, 34, 0.25);
            }

            .btn-deliver:hover {
                box-shadow: 0 4px 12px rgba(230, 126, 34, 0.35);
            }

            .btn-complete {
                background: linear-gradient(135deg, #27ae60 0%, #229954 100%);
                box-shadow: 0 2px 8px rgba(39, 174, 96, 0.25);
            }

            .btn-complete:hover {
                box-shadow: 0 4px 12px rgba(39, 174, 96, 0.35);
            }

            .btn-warning {
                background: linear-gradient(135deg, #FF9702 0%, #e67e22 100%);
                box-shadow: 0 2px 8px rgba(255, 148, 4, 0.25);
            }

            .order-completed {
                text-align: center;
                padding: 0.6rem;
                background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
                border-radius: 8px;
                color: #27ae60;
                font-weight: 600;
                font-size: 0.85rem;
            }

            .order-completed i {
                margin-right: 0.3rem;
                font-size: 1rem;
            }

            /* Empty State */
            .empty-state {
                text-align: center;
                padding: 2.5rem 1.5rem;
                background: white;
                border-radius: 12px;
                box-shadow: 0 3px 12px rgba(0,0,0,0.06);
            }

            .empty-state i {
                font-size: 3rem;
                color: #cbd5e0;
                margin-bottom: 0.8rem;
            }

            .empty-state h3 {
                color: #4a5568;
                margin-bottom: 0.4rem;
                font-size: 1.2rem;
            }

            .empty-state p {
                color: #718096;
                font-size: 0.9rem;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .order-grid {
                    grid-template-columns: 1fr;
                    gap: 0.8rem;
                }

                .page-header h1 {
                    font-size: 1.5rem;
                }

                .filter-section {
                    padding: 1rem;
                }

                .order-card {
                    padding: 1rem;
                }

                .order-header {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 0.6rem;
                }
            }

            @media (max-width: 576px) {
                .content {
                    padding: 0.8rem 0;
                }

                .page-header {
                    padding: 1.2rem 1rem;
                    margin: 0 0.5rem 1rem 0.5rem;
                }

                .filter-section {
                    margin: 0 0.5rem 1rem 0.5rem;
                    padding: 0.8rem;
                }

                .order-card {
                    margin: 0 0.5rem 0.8rem 0.5rem;
                    padding: 0.8rem;
                }

                .order-title {
                    font-size: 1rem;
                }

                .order-grid {
                    grid-template-columns: 1fr;
                    gap: 0.6rem;
                }
            }

            .timer {
                font-weight: bold;
                color: #00b894;
                background: #d1f2eb;
                padding: 0.3rem 0.8rem;
                border-radius: 15px;
                font-size: 0.9rem;
            }

            /* Loading Animation */
            @keyframes pulse {
                0%, 100% { opacity: 1; }
                50% { opacity: 0.5; }
            }

            .loading {
                animation: pulse 1.5s ease-in-out infinite;
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
                        <span class="align-middle">NestMart</span>
                    </div>

                    <ul class="sidebar-nav">
                        <li class="sidebar-header" active>Pages</li>
                        <li class="sidebar-item active">
                            <a class="sidebar-link " href="shippers.htm">
                                <i class="align-middle me-2" data-feather="truck"></i>
                                <span class="align-middle">Orders</span>
                            </a>
                        </li>
                        <li class="sidebar-item ">
                            <a class="sidebar-link" href="scheduleAndSalary.htm">
                                <i class="align-middle me-2" data-feather="calendar"></i>
                                <span class="align-middle">Schedule & Salary</span>
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
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/shipper/accountInformation.htm">
                                        <i class="fa fa-user"></i> Account Information
                                    </a>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/shipper/scheduleAndSalary.htm">
                                        <i class="align-middle me-1" data-feather="calendar"></i> Schedule And Salary
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
                    <div class="container">
                        <!-- Page Header -->
                        <div class="page-header text-center">
                            <h1><i class="fas fa-shipping-fast"></i> Order Management</h1>
                            <p class="subtitle">Manage and track your delivery orders</p>
                        </div>

                        <!-- Filter Section -->
                        <div class="filter-section">
                            <h5 class="filter-title">
                                <i class="fas fa-filter"></i>
                                Filter Orders
                            </h5>
                            <form method="get" action="${pageContext.request.contextPath}/shipper/shippers.htm">
                                <input type="hidden" name="shipperId" value="${shipperId}" />
                                <div class="row g-3">
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-0">
                                                <i class="fas fa-hashtag text-muted"></i>
                                            </span>
                                            <input type="text" name="orderID" class="form-control" placeholder="Search by Order ID..." value="${param.orderID}" />
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-0">
                                                <i class="fas fa-user text-muted"></i>
                                            </span>
                                            <input type="text" name="search" class="form-control" placeholder="Search by Customer Name" value="${param.customerName}" />
                                        </div>
                                    </div>
                                    <div class="col-md-2">
                                        <select name="status" class="form-control" id="statusFilter">
                                            <option value="">All Status</option>
                                            <option value="Confirmed" ${param.status == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                            <option value="On Delivering" ${param.status == 'On Delivering' ? 'selected' : ''}>Delivering</option>
                                            <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Completed</option>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <button type="submit" class="btn btn-filter w-100">
                                            <i class="fas fa-search me-2"></i>Filter
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <!-- Orders Grid -->
                        <c:choose>
                            <c:when test="${empty orders}">
                                <div class="empty-state">
                                    <i class="fas fa-inbox"></i>
                                    <h3>No Orders Found</h3>
                                    <p>There are no orders matching your criteria at the moment.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="order-grid">
                                    <c:forEach var="order" items="${orders}">
                                        <div class="order-card" id="order-${order.orderID}">
                                            <!-- Order Header -->
                                            <div class="order-header">
                                                <h3 class="order-title">
                                                    <i class="fas fa-receipt"></i>
                                                    Order #${order.orderID}
                                                </h3>
                                                <div class="order-total">
                                                    $${order.totalAmount}
                                                </div>
                                            </div>

                                            <!-- Order Details -->
                                            <div class="order-details">
                                                <div class="order-detail-row">
                                                    <div class="detail-icon">
                                                        <i class="fas fa-user"></i>
                                                    </div>
                                                    <div class="detail-content">
                                                        <div class="detail-label">Customer</div>
                                                        <div class="detail-value">${order.customerName}</div>
                                                    </div>
                                                </div>

                                                <div class="order-detail-row">
                                                    <div class="detail-icon">
                                                        <i class="fas fa-phone"></i>
                                                    </div>
                                                    <div class="detail-content">
                                                        <div class="detail-label">Phone</div>
                                                        <div class="detail-value">${order.customerPhone}</div>
                                                    </div>
                                                </div>

                                                <div class="order-detail-row">
                                                    <div class="detail-icon">
                                                        <i class="fas fa-map-marker-alt"></i>
                                                    </div>
                                                    <div class="detail-content">
                                                        <div class="detail-label">Address</div>
                                                        <div class="detail-value">${order.shippingAddress}</div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-6">
                                                        <div class="order-detail-row">
                                                            <div class="detail-icon">
                                                                <i class="fas fa-calendar"></i>
                                                            </div>
                                                            <div class="detail-content">
                                                                <div class="detail-label">Date</div>
                                                                <div class="detail-value">${order.formattedOrderDate.split(' ')[0]}</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-6">
                                                        <div class="order-detail-row">
                                                            <div class="detail-icon">
                                                                <i class="fas fa-clock"></i>
                                                            </div>
                                                            <div class="detail-content">
                                                                <div class="detail-label">Time</div>
                                                                <div class="detail-value">${order.formattedOrderDate.split(' ')[1]}</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="order-detail-row">
                                                    <div class="detail-icon">
                                                        <i class="fas fa-credit-card"></i>
                                                    </div>
                                                    <div class="detail-content">
                                                        <div class="detail-label">Payment Method</div>
                                                        <div class="detail-value">${order.paymentMethod}</div>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Status Badges -->
                                            <div class="status-container">
                                                <c:choose>
                                                    <c:when test="${order.orderStatus == 'Pending Confirmation'}">
                                                        <span class="status-badge badge-pending">
                                                            <i class="fas fa-hourglass-half"></i>
                                                            Pending
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Confirmed'}">
                                                        <span class="status-badge badge-confirmed">
                                                            <i class="fas fa-check-circle"></i>
                                                            Confirmed
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'On Delivering'}">
                                                        <span class="status-badge badge-delivering">
                                                            <i class="fas fa-truck"></i>
                                                            Delivering
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Completed'}">
                                                        <span class="status-badge badge-completed">
                                                            <i class="fas fa-check-double"></i>
                                                            Completed
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge badge-info order-status">
                                                            <i class="fas fa-info-circle"></i>
                                                            ${order.orderStatus}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>

                                                <c:choose>
                                                    <c:when test="${order.transactionStatus == 'Successful' || order.transactionStatus == 'Completed'}">
                                                        <span class="status-badge badge-success transaction-status">
                                                            <i class="fas fa-dollar-sign"></i>
                                                            Paid
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge badge-pending transaction-status">
                                                            <i class="fas fa-clock"></i>
                                                            ${order.transactionStatus}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <!-- Action Buttons -->
                                            <div class="order-actions">
                                                <c:choose>
                                                    <c:when test="${order.orderStatus == 'Pending Confirmation'}">
                                                        <button onclick="updateOrderStatus('${order.orderID}', 'Confirmed', '${order.paymentMethod}')" class="btn-action btn-confirm">
                                                            <i class="fas fa-check me-2"></i>Confirm Order
                                                        </button>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Confirmed'}">
                                                        <button onclick="updateOrderStatus('${order.orderID}', 'On Delivering', '${order.paymentMethod}')" class="btn-action btn-deliver">
                                                            <i class="fas fa-truck me-2"></i>Start Delivery
                                                        </button>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'On Delivering'}">
                                                        <button onclick="updateOrderStatus('${order.orderID}', 'Completed', '${order.paymentMethod}')" class="btn-action btn-complete">
                                                            <i class="fas fa-check-double me-2"></i>Complete Order
                                                        </button>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Approved'}">
                                                        <button onclick="confirmReturnPickup('${order.orderID}')" class="btn-action btn-warning">
                                                            <i class="fas fa-undo me-2"></i>Confirm Return Pickup
                                                        </button>
                                                    </c:when>
                                                    <c:when test="${order.orderStatus == 'Completed' || order.orderStatus == 'Return Completed'}">
                                                        <div class="order-completed">
                                                            <i class="fas fa-check-circle"></i>
                                                            Order ${order.orderStatus}
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="text-muted text-center py-2">
                                                            <i class="fas fa-info-circle me-2"></i>
                                                            No action available
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </main>

            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.29.2/feather.min.js"></script>
        <script src="../assets/admin/js/app.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>


        <script>
                                                    function approveReturn(orderID) {
                                                        $.ajax({
                                                            url: '../shipper/approveReturn.htm',
                                                            type: 'POST',
                                                            data: {
                                                                orderID: orderID
                                                            },
                                                            success: function (response) {
                                                                if (response === 'success') {
                                                                    alert('Return request approved successfully.');
                                                                    location.reload();
                                                                } else {
                                                                    alert('Error approving return request.');
                                                                }
                                                            },
                                                            error: function (xhr, status, error) {
                                                                console.error('Error approving return request:', error);
                                                                alert('Error approving return request: ' + error);
                                                            }
                                                        });
                                                    }

                                                    function confirmReturnPickup(orderID) {
                                                        $.ajax({
                                                            url: '../shipper/confirmReturnPickup.htm',
                                                            type: 'POST',
                                                            data: {
                                                                orderID: orderID
                                                            },
                                                            success: function (response) {
                                                                if (response === 'success') {
                                                                    alert('Return pickup confirmed successfully.');
                                                                    location.reload();
                                                                } else {
                                                                    alert('Error confirming return pickup.');
                                                                }
                                                            },
                                                            error: function (xhr, status, error) {
                                                                console.error('Error confirming return pickup:', error);
                                                                alert('Error confirming return pickup: ' + error);
                                                            }
                                                        });
                                                    }

                                                    $(document).ready(function () {
                                                        function filterOrders(status) {
                                                            if (status === "") {
                                                                $(".order-item").show();
                                                            } else {
                                                                $(".order-item").hide();
                                                                $(".order-item[data-status='" + status + "']").show();
                                                            }
                                                        }

                                                        filterOrders($("#statusFilter").val());

                                                        $("#statusFilter").change(function () {
                                                            filterOrders($(this).val());
                                                        });

                                                        function updateOrderStatus(orderID, status, paymentMethod) {
                                                            $.ajax({
                                                                url: '${pageContext.request.contextPath}/shipper/updateOrderStatus.htm',
                                                                type: 'POST',
                                                                data: {
                                                                    orderID: orderID,
                                                                    status: status,
                                                                    paymentMethod: paymentMethod
                                                                },
                                                                success: function (response) {
                                                                    console.log('Server response:', response);
                                                                    if (response === 'success') {
                                                                        var $orderCard = $('#order-' + orderID);
                                                                        $orderCard.find('.order-status').text(status);
                                                                        $orderCard.closest('.order-item').attr('data-status', status);
                                                                        updateButtonStatus(orderID, status, paymentMethod);
                                                                        updateTransactionStatus(orderID, status, paymentMethod);

                                                                        if (status === 'On Delivering') {
                                                                            var startTime = new Date().getTime();
                                                                            localStorage.setItem('startTime-' + orderID, startTime);
                                                                            startTimer(orderID);
                                                                        } else if (status === 'Completed') {
                                                                            finalizeTimer(orderID);
                                                                        }

                                                                        // Update the status filter dropdown and trigger filtering
                                                                        $("#statusFilter").val(status).trigger('change');
                                                                    } else {
                                                                        console.error('Server returned non-success response:', response);
                                                                        alert('Error updating order status.');
                                                                    }
                                                                },
                                                                error: function (xhr, status, error) {
                                                                    console.error('Error updating order status:', xhr.responseText);
                                                                    alert('Error sending request: ' + error);
                                                                }
                                                            });
                                                        }

                                                        function updateButtonStatus(orderID, status, paymentMethod) {
                                                            var orderCard = $('#order-' + orderID);
                                                            var button = orderCard.find('button');

                                                            switch (status) {
                                                                case 'Confirmed':
                                                                    button.text('Start delivery');
                                                                    button.removeClass().addClass('btn btn-info btn-update');
                                                                    button.off('click').on('click', function () {
                                                                        updateOrderStatus(orderID, 'On Delivering', paymentMethod);
                                                                    });
                                                                    break;
                                                                case 'On Delivering':
                                                                    button.text('Complete Order');
                                                                    button.removeClass().addClass('btn btn-success btn-update');
                                                                    button.off('click').on('click', function () {
                                                                        updateOrderStatus(orderID, 'Completed', paymentMethod);
                                                                    });
                                                                    break;
                                                                case 'Completed':
                                                                    button.remove();
                                                                    // Check if completion message doesn't already exist
                                                                    if (orderCard.find('.order-completed-message').length === 0) {
                                                                        orderCard.find('.order-actions').append('<div class="order-completed order-completed-message"><i class="fas fa-check-circle"></i>Order Completed</div>');
                                                                    }
                                                                    break;
                                                                default:
                                                                    console.warn('Unexpected order status:', status);
                                                            }
                                                        }


                                                        function updateTransactionStatus(orderID, status, paymentMethod) {
                                                            var transactionStatusElement = $('#order-' + orderID + ' .transaction-status');

                                                            if (paymentMethod === 'Bank Transfer') {
                                                                if (['Confirmed', 'On Delivering', 'Completed'].includes(status)) {
                                                                    transactionStatusElement.text('Completed');
                                                                }
                                                            } else if (paymentMethod === 'Cash') {
                                                                if (status === 'Completed') {
                                                                    transactionStatusElement.text('Completed');
                                                                } else if (['Confirmed', 'On Delivering'].includes(status)) {
                                                                    transactionStatusElement.text('Pending');
                                                                }
                                                            } else {
                                                                console.warn('Unexpected payment method or status:', paymentMethod, status);
                                                            }
                                                        }

                                                        function startTimer(orderID) {
                                                            var startTime = parseInt(localStorage.getItem('startTime-' + orderID));
                                                            var timerElement = $('#timer-' + orderID);

                                                            function updateTimer() {
                                                                var currentTime = new Date().getTime();
                                                                var secondsElapsed = Math.floor((currentTime - startTime) / 1000);
                                                                timerElement.text(secondsElapsed);

                                                                if (localStorage.getItem('completedTime-' + orderID)) {
                                                                    clearTimeout(timeoutId);
                                                                    displayCompletedTime(orderID);
                                                                } else {
                                                                    timeoutId = setTimeout(updateTimer, 1000);
                                                                }
                                                            }

                                                            var timeoutId = setTimeout(updateTimer, 0);
                                                            localStorage.setItem('timeoutId-' + orderID, timeoutId);
                                                        }

                                                        function finalizeTimer(orderID) {
                                                            var startTime = localStorage.getItem('startTime-' + orderID);
                                                            if (startTime) {
                                                                var endTime = new Date().getTime();
                                                                var totalSeconds = Math.floor((endTime - parseInt(startTime)) / 1000);
                                                                localStorage.setItem('completedTime-' + orderID, totalSeconds);
                                                                localStorage.removeItem('startTime-' + orderID);
                                                                localStorage.removeItem('timeoutId-' + orderID);
                                                                displayCompletedTime(orderID);
                                                            }
                                                        }

                                                        function displayCompletedTime(orderID) {
                                                            var completedTime = localStorage.getItem('completedTime-' + orderID);
                                                            if (completedTime) {
                                                                $('#timer-' + orderID).text(completedTime + ' (Hoàn thành)');
                                                            }
                                                        }

                                                        // Initialize timers for existing orders
                                                        $('.order-card').each(function () {
                                                            var orderID = $(this).attr('id').split('-')[1];
                                                            var startTime = localStorage.getItem('startTime-' + orderID);
                                                            var status = $(this).find('.order-status').text();
                                                            var completedTime = localStorage.getItem('completedTime-' + orderID);

                                                            if (completedTime) {
                                                                displayCompletedTime(orderID);
                                                            } else if (startTime && status === 'On Delivering') {
                                                                startTimer(orderID);
                                                            }
                                                        });

                                                        // Make updateOrderStatus globally accessible
                                                        window.updateOrderStatus = updateOrderStatus;
                                                        
                                                        // Initialize Feather icons
                                                        if (typeof feather !== 'undefined') {
                                                            feather.replace();
                                                        }
                                                    });
        </script>
    </body>
</html>
