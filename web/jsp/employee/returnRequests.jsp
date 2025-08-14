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

        <meta name="keywords" content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" href="../../admin/static/img/icons/icon-48x48.png" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <link rel="canonical" href="https://demo-basic.adminkit.io/pages-blank.html" />
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">

        <title>NestMart - Return Request</title>

        <link rel="stylesheet" href="../assets/admin/css/app.css" />
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
        <style>


            .content {
                padding: 2rem;
            }

            h1 {
                color: #34495e;
                font-size: 2.5rem;
                margin-bottom: 1.5rem;
                text-align: center;
                text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
            }

            table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0 10px;
                margin-top: 20px;
            }

            th {
                background-color: #3498db;
                color: white;
                padding: 15px;
                text-align: left;
                font-weight: bold;
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            td {
                background-color: white;
                padding: 15px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            tr:hover td {
                background-color: #f5f7fa;
                transition: background-color 0.3s ease;
            }

            .btn-view-details {
                background-color: #2ecc71;
                color: white;
                border: none;
                padding: 10px 15px;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .btn-view-details:hover {
                background-color: #27ae60;
            }

            /* Form elements */
            textarea, select {
                width: 100%;
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid #bdc3c7;
                border-radius: 5px;
            }

            select {
                appearance: none;
                background-image: url('data:image/svg+xml;utf8,<svg fill="black" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/><path d="M0 0h24v24H0z" fill="none"/></svg>');
                background-repeat: no-repeat;
                background-position-x: 98%;
                background-position-y: 50%;
            }

            button[type="submit"] {
                background-color: #3498db;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            button[type="submit"]:hover {
                background-color: #2980b9;
            }

            .order-details {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0,0,0,0.4);
            }

            .order-details-content {
                background-color: #fefefe;
                margin: 15% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 800px;
                border-radius: 8px;
            }

            .blue-header {
                background-color: #3498db;
                color: white;
                font-size: 0.75rem;
                font-weight: bold;
                padding: 0.5rem;
                text-transform: uppercase;
            }

            .close {
                color: #aaa;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }

            .close:hover,
            .close:focus {
                color: black;
                text-decoration: none;
                cursor: pointer;
            }

            .table-header {
                font-size: 0.875rem;
                font-weight: 600;
                text-transform: uppercase;
                color: #4a5568;
                background-color: #edf2f7;
                padding: 0.75rem 1rem;
            }

            .table-data {
                font-size: 0.875rem;
                color: #2d3748;
                padding: 0.75rem 1rem;
            }

            .status-badge {
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 0.8rem;
                font-weight: bold;
            }

            .status-approved {
                background-color: #d4edda;
                color: #155724;
            }

            .status-denied {
                background-color: #f8d7da;
                color: #721c24;
            }

            .btn-delete {
                background-color: #dc3545;
                color: white;
                border: none;
                padding: 6px 12px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 0.8rem;
            }

            .btn-delete:hover {
                background-color: #c82333;
            }
            .status-badge {
                padding: 6px 12px !important;
                border-radius: 4px !important;
                font-size: 0.85rem !important;
                font-weight: bold !important;
                display: inline-block !important;
                min-width: 70px !important;
                text-align: center !important;
                border: 1px solid #ccc; 
            }

            .status-approved {
                background-color: #d4edda !important;
                color: #155724 !important;
                border-color: #c3e6cb !important;
            }

            .status-denied {
                background-color: #f8d7da !important;
                color: #721c24 !important;
                border-color: #f5c6cb !important;
            }

            td:has(.status-badge) {
                background-color: #f9f9f9 !important;
                padding: 10px !important;
            }

            .status-cell {
                background-color: #f9f9f9 !important;
                padding: 10px !important;
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

                        <li class="sidebar-item active">
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
                        <h1>Return Requests</h1>
                        <h2>Return Requests Process</h2>
                        <c:if test="${empty returnRequests}">
                            <div class="alert alert-info" role="alert">
                                There are currently no return requests to process.
                            </div>
                        </c:if>
                        <c:if test="${not empty returnRequests}">
                            <table>
                                <tr>
                                    <th class="blue-header">Order ID</th>
                                    <th class="blue-header">Customer Name</th>
                                    <th class="blue-header">Return Request Date</th>
                                    <th class="blue-header">Action</th>
                                </tr>
                                <c:forEach var="order" items="${returnRequests}">
                                    <tr>
                                        <td>${order.orderID}</td>
                                        <td>${order.customerName}</td>
                                        <td id="returnRequestDate-${order.orderID}">${order.formattedReturnRequestDate}</td>
                                        <td>
                                            <button class="btn-view-details" onclick="showDetails('${order.orderID}')">
                                                View Details
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>

                            <c:forEach var="order" items="${returnRequests}">
                                <div id="orderDetails-${order.orderID}" class="order-details">
                                    <div class="order-details-content">
                                        <span class="close" onclick="closeDetails('${order.orderID}')">&times;</span>
                                        <h2>Order #${order.orderID}</h2>
                                        <table>
                                            <tr>
                                                <th>Customer Name</th>
                                                <td>${order.customerName}</td>
                                                <th>Customer Email</th>
                                                <td>${order.customerEmail}</td>
                                            </tr>
                                            <tr>
                                                <th>Order Date</th>
                                                <td id="orderDate-${order.orderID}">${order.formattedOrderDate}</td>
                                                <th>Return Request Date</th>
                                                <td id="returnRequestDate-detail-${order.orderID}">${order.formattedReturnRequestDate}</td>
                                            </tr>
                                            <tr>
                                                <th>Total Amount</th>
                                                <td><fmt:formatNumber value="${order.totalAmount}" type="currency"/></td>
                                                <th>Payment Method</th>
                                                <td>${order.paymentMethod}</td>
                                            </tr>
                                            <tr>
                                                <th>Shipping Address</th>
                                                <td colspan="3">${order.shippingAddress}</td>
                                            </tr>
                                            <tr>
                                                <th>Return Request Reason</th>
                                                <td colspan="3">${order.returnRequestReason}</td>
                                            </tr>
                                        </table>

                                        <div class="order-products">
                                            <h3>Order Details</h3>
                                            <table>
                                                <tr>
                                                    <th>Product ID</th>
                                                    <th>Product Name</th>
                                                    <th>Quantity</th>
                                                    <th>Unit Price</th>
                                                    <th>Total Price</th>
                                                </tr>
                                                <c:forEach var="detail" items="${order.orderDetails}">
                                                    <tr>
                                                        <td>${detail.product.productID}</td>
                                                        <td>${detail.product.productName}</td>
                                                        <td>${detail.quantity}</td>
                                                        <td><fmt:formatNumber value="${detail.unitPrice}" type="currency"/></td>
                                                        <td><fmt:formatNumber value="${detail.totalPrice}" type="currency"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </div>

                                        <form id="returnRequestForm-${order.orderID}" action="../employee/process.htm" method="POST">
                                            <input type="hidden" name="orderID" value="${order.orderID}">
                                            <textarea name="message" placeholder="Enter message..."></textarea>
                                            <select name="newOrderStatus" id="newOrderStatus-${order.orderID}"
                                                    onchange="updateReturnStatus('${order.orderID}')">
                                                <option value="Approved">Approve</option>
                                                <option value="Denied">Deny</option>
                                            </select>
                                            <input type="hidden" name="newReturnRequestStatus" id="newReturnRequestStatus-${order.orderID}">
                                            <button type="submit">Process Request</button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>


                        <!-- Completed Returns -->
                        <h2>Completed Returns</h2>

                        <c:if test="${not empty success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${empty completedReturns}">
                            <div class="alert alert-info" role="alert">
                                No completed returns found.
                            </div>
                        </c:if>
                        <c:if test="${not empty completedReturns}">
                            <table>
                                <tr>
                                    <th class="blue-header">Order ID</th>
                                    <th class="blue-header">Customer Name</th>
                                    <th class="blue-header">Return Completed Date</th>
                                    <th class="blue-header">Action</th>
                                </tr>
                                <c:forEach var="order" items="${completedReturns}">
                                    <tr id="completed-row-${order.orderID}">
                                        <td>${order.orderID}</td>
                                        <td>${order.customerName}</td>
                                        <td id="returnCompletedDate-${order.orderID}">${order.formattedReturnRequestDate}</td>
                                        <td>
                                            <form action="deleteCompletedReturn.htm" method="POST"
                                                  onsubmit="return confirmDelete('${order.orderID}')"
                                                  style="display: inline;">
                                                <input type="hidden" name="orderID" value="${order.orderID}">
                                                <button type="submit" class="btn-delete" id="delete-btn-${order.orderID}">
                                                    Delete
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:if>

                    </div>
                </main>


            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script>
        <script>feather.replace();</script>
        <script src="../assets/admin/js/app.js"></script>

        <script>
                                                      // FIXED: Updated JavaScript functions to handle quoted parameters correctly
                                                      function updateReturnStatus(orderID) {
                                                          var selectElement = document.getElementById('newOrderStatus-' + orderID);
                                                          if (selectElement) {
                                                              var returnRequestStatus = selectElement.value === 'Approved' ? 'Approved' : 'Denied';
                                                              var hiddenElement = document.getElementById('newReturnRequestStatus-' + orderID);
                                                              if (hiddenElement) {
                                                                  hiddenElement.value = returnRequestStatus;
                                                                  console.log('New Return Request Status for order ' + orderID + ':', returnRequestStatus);
                                                              }
                                                          }
                                                      }

                                                      function formatDate(dateString) {
                                                          if (!dateString || dateString.trim() === '')
                                                              return '';
                                                          try {
                                                              const date = new Date(dateString);
                                                              if (isNaN(date.getTime()))
                                                                  return dateString; // Return original if invalid date
                                                              return date.getFullYear() + '-' +
                                                                      String(date.getMonth() + 1).padStart(2, '0') + '-' +
                                                                      String(date.getDate()).padStart(2, '0') + ' ' +
                                                                      String(date.getHours()).padStart(2, '0') + ':' +
                                                                      String(date.getMinutes()).padStart(2, '0');
                                                          } catch (e) {
                                                              return dateString; // Return original if error
                                                          }
                                                      }

                                                      function showDetails(orderId) {
                                                          var modal = document.getElementById('orderDetails-' + orderId);
                                                          if (modal) {
                                                              modal.style.display = 'block';
                                                          }
                                                      }

                                                      function closeDetails(orderId) {
                                                          var modal = document.getElementById('orderDetails-' + orderId);
                                                          if (modal) {
                                                              modal.style.display = 'none';
                                                          }
                                                      }

                                                      // Close modal when clicking outside of it
                                                      window.onclick = function (event) {
                                                          var modals = document.querySelectorAll('.order-details');
                                                          modals.forEach(function (modal) {
                                                              if (event.target == modal) {
                                                                  modal.style.display = 'none';
                                                              }
                                                          });
                                                      }
// Enhanced delete confirmation function
                                                      function confirmDelete(orderID) {
                                                          console.log('Attempting to delete order:', orderID);

                                                          if (!orderID || orderID.trim() === '') {
                                                              alert('Invalid Order ID');
                                                              return false;
                                                          }

                                                          var confirmed = confirm('Are you sure you want to delete this completed return order #' + orderID + '?');

                                                          if (confirmed) {
                                                              console.log('Delete confirmed for order:', orderID);

                                                              // Optional: Disable the button to prevent double-clicks
                                                              var deleteBtn = document.getElementById('delete-btn-' + orderID);
                                                              if (deleteBtn) {
                                                                  deleteBtn.disabled = true;
                                                                  deleteBtn.innerHTML = 'Deleting...';
                                                              }

                                                              return true;
                                                          } else {
                                                              console.log('Delete cancelled for order:', orderID);
                                                              return false;
                                                          }
                                                      }
                                                      document.addEventListener('DOMContentLoaded', function () {
                                                          try {
                                                              // Format dates on page load - with null checks
                                                              document.querySelectorAll('td[id^="orderDate-"]').forEach(td => {
                                                                  if (td && td.textContent) {
                                                                      td.textContent = formatDate(td.textContent);
                                                                  }
                                                              });

                                                              document.querySelectorAll('td[id^="returnRequestDate-"]').forEach(td => {
                                                                  if (td && td.textContent) {
                                                                      td.textContent = formatDate(td.textContent);
                                                                  }
                                                              });

                                                              document.querySelectorAll('td[id^="returnCompletedDate-"]').forEach(td => {
                                                                  if (td && td.textContent) {
                                                                      td.textContent = formatDate(td.textContent);
                                                                  }
                                                              });

                                                              // Initialize return status for all forms - with existence checks
                                                              document.querySelectorAll('select[id^="newOrderStatus-"]').forEach(function (select) {
                                                                  if (select && select.id) {
                                                                      var orderID = select.id.replace('newOrderStatus-', '');
                                                                      updateReturnStatus(orderID);
                                                                  }
                                                              });
                                                          } catch (error) {
                                                              console.error('Error initializing page:', error);
                                                          }
                                                      });
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>