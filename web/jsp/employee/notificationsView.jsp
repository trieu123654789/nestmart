<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">

        <title>NestMart - Notifications</title>

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

            .table-actions .btn {
                background-color: transparent;
                border: none;
                cursor: pointer;
                display: flex;

                font-size: 20.5px;
                padding: 5px;
                transition: background-color 0.3s, box-shadow 0.3s;
            }
            .table-actions .btn-update {
                color: #007bff;
            }
            .table-actions .btn-delete {
                color: #dc3545;
            }
            .table-actions .btn:hover {
                background-color: #f0f0f0;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
            }
            .table-actions .btn-update:hover {
                color: #0056b3;
            }
            .table-actions .btn-delete:hover {
                color: #c82333;
            }
            tr {
                position: relative;
            }
            .table td {
                vertical-align: middle;
            }

            .bulk-actions {
                display: none;
                margin-bottom: 15px;
                padding: 10px;
                background-color: #f8f9fa;
                border-radius: 5px;
                border: 1px solid #dee2e6;
            }
            .bulk-actions.show {
                display: block;
            }
            .bulk-actions .btn {
                margin-right: 10px;
            }
            .select-all-container {
                margin-bottom: 15px;
            }
            .notification-checkbox {
                cursor: pointer;
            }
            .selected-count {
                font-weight: bold;
                color: #007bff;
            }

            .drag-selection {
                position: absolute;
                border: 2px dashed #007bff;
                background: rgba(0, 123, 255, 0.1);
                pointer-events: none;
                z-index: 1000;
                display: none;
            }

            /* Row selection styles */
            .table tbody tr.selecting {
                background-color: rgba(0, 123, 255, 0.1) !important;
            }

            .table tbody tr.drag-selected {
                background-color: rgba(0, 123, 255, 0.2) !important;
            }

            .drag-mode {
                user-select: none;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
            }

            .table tbody tr {
                cursor: pointer;
                transition: background-color 0.2s ease;
            }

            .table tbody tr:hover {
                background-color: rgba(0, 0, 0, 0.05);
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

                        <li class="sidebar-item active">
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
                          <li class="sidebar-item">
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
                      <div class="search-container">
                            <form action="${pageContext.request.contextPath}/employee/viewNotifications.htm" method="get" class="search-form">
                                <input type="text" name="keyword" class="form-control search-input"
                                       placeholder="Search by Notification ID, Notification Type, Title,.." value="${keyword}">
                                <button type="submit" class="btn search-button">
                                    <i class="align-middle" data-feather="search"></i>
                                </button>
                            </form>

                            <div class="icon-container">
                                <button class="btn btn-add"
                                        onclick="window.location.href = '${pageContext.request.contextPath}/employee/addNotificationForm.htm'">
                                    <i data-feather="plus"></i>
                                </button>
                            </div>
                        </div>

                        <c:if test="${not empty keyword}">
                            <c:choose>
                                <c:when test="${not empty notifications}">
                                    <p>Found <b>${totalNotifications}</b> results.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>No result for "<b>${keyword}</b>".</p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <div class="select-all-container">
                            <label>
                                <input type="checkbox" id="selectAll" class="notification-checkbox"> 
                                <strong>Select All</strong>
                            </label>
                        </div>

                        <div id="bulkActions" class="bulk-actions">
                            <span class="selected-count" id="selectedCount">0 selected</span>
                            <button type="button" class="btn btn-danger btn-sm" onclick="bulkDeleteNotifications()">
                                <i data-feather="trash-2"></i> Delete Selected
                            </button>
                            <button type="button" class="btn btn-secondary btn-sm" onclick="clearSelection()">
                                Clear Selection
                            </button>
                        </div>

                        <!-- Notifications Table -->
                        <form id="bulkDeleteForm" method="post" action="${pageContext.request.contextPath}/employee/bulkDeleteNotifications.htm">
                            <table class="table table-hover my-0" id="accountTable">
                                <thead>
                                    <tr>
                                        <th width="50px">Select</th>
                                        <th>Notification ID</th>
                                        <th>Customer Name</th>
                                        <th>Notification Type</th>
                                        <th>Title</th>
                                        <th>Message</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="notification" items="${notifications}">
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="selectedNotifications" 
                                                       value="${notification.notificationID}" 
                                                       class="notification-checkbox row-checkbox">
                                            </td>
                                            <td><c:out value="${notification.notificationID}" /></td>
                                            <td><c:out value="${notification.customerName}" /></td>
                                            <td><c:out value="${notification.notificationType}" /></td>
                                            <td><c:out value="${notification.title}" /></td>
                                            <td><c:out value="${notification.message}" /> <br/> <small><c:out value="${notification.sendDate}" /></small></td>
                                            <td><c:out value="${notification.status}" /></td>
                                            <td class="table-actions">
                                                                                            <div class="btn-container">

                                                <a href="${pageContext.request.contextPath}/employee/updateNotification.htm?notificationID=${notification.notificationID}" class="btn btn-update" title="Edit">
                                                    <i data-feather="edit"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/employee/empDeleteNotification.htm?notificationID=${notification.notificationID}" class="btn btn-delete" title="Delete" onclick="return confirm('Are you sure you want to delete this notification?');">
                                                    <i data-feather="trash-2"></i>
                                                </a>
                                                                                            </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty notifications}">
                                        <tr>
                                            <td colspan="8">No notification found.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </main>
               
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script>
        <script>
                                                    feather.replace();

                                                    let isDragging = false;
                                                    let startX, startY;
                                                    let selectionBox;
                                                    let selectedRows = new Set();

                                                    document.addEventListener('DOMContentLoaded', function () {
                                                        const selectAllCheckbox = document.getElementById('selectAll');
                                                        const rowCheckboxes = document.querySelectorAll('.row-checkbox');
                                                        const bulkActionsPanel = document.getElementById('bulkActions');
                                                        const selectedCountSpan = document.getElementById('selectedCount');
                                                        const tableBody = document.querySelector('#accountTable tbody');

                                                        initDragSelection();

                                                        selectAllCheckbox.addEventListener('change', function () {
                                                            rowCheckboxes.forEach(checkbox => {
                                                                checkbox.checked = this.checked;
                                                                const row = checkbox.closest('tr');
                                                                if (this.checked) {
                                                                    selectedRows.add(row);
                                                                    row.classList.add('drag-selected');
                                                                } else {
                                                                    selectedRows.delete(row);
                                                                    row.classList.remove('drag-selected');
                                                                }
                                                            });
                                                            updateBulkActionsPanel();
                                                        });

                                                        rowCheckboxes.forEach(checkbox => {
                                                            checkbox.addEventListener('change', function () {
                                                                const row = this.closest('tr');
                                                                if (this.checked) {
                                                                    selectedRows.add(row);
                                                                    row.classList.add('drag-selected');
                                                                } else {
                                                                    selectedRows.delete(row);
                                                                    row.classList.remove('drag-selected');
                                                                }
                                                                updateSelectAllState();
                                                                updateBulkActionsPanel();
                                                            });
                                                        });

                                                        function updateSelectAllState() {
                                                            const checkedBoxes = document.querySelectorAll('.row-checkbox:checked');
                                                            const totalBoxes = rowCheckboxes.length;

                                                            selectAllCheckbox.indeterminate = checkedBoxes.length > 0 && checkedBoxes.length < totalBoxes;
                                                            selectAllCheckbox.checked = checkedBoxes.length === totalBoxes && totalBoxes > 0;
                                                        }

                                                        function updateBulkActionsPanel() {
                                                            const checkedBoxes = document.querySelectorAll('.row-checkbox:checked');
                                                            const count = checkedBoxes.length;

                                                            if (count > 0) {
                                                                bulkActionsPanel.classList.add('show');
                                                                selectedCountSpan.textContent = count + ' selected';
                                                            } else {
                                                                bulkActionsPanel.classList.remove('show');
                                                            }
                                                        }

                                                        function initDragSelection() {
                                                            selectionBox = document.createElement('div');
                                                            selectionBox.className = 'drag-selection';
                                                            document.body.appendChild(selectionBox);

                                                            tableBody.addEventListener('mousedown', function (e) {
                                                                if (e.target.type === 'checkbox' || e.target.closest('.table-actions') || e.target.tagName === 'A') {
                                                                    return;
                                                                }

                                                                e.preventDefault();
                                                                isDragging = true;

                                                                const rect = tableBody.getBoundingClientRect();
                                                                startX = e.clientX;
                                                                startY = e.clientY;

                                                                document.body.classList.add('drag-mode');

                                                                if (!e.ctrlKey && !e.metaKey) {
                                                                    clearAllSelections();
                                                                }

                                                                selectionBox.style.display = 'block';
                                                                selectionBox.style.left = startX + 'px';
                                                                selectionBox.style.top = startY + 'px';
                                                                selectionBox.style.width = '0px';
                                                                selectionBox.style.height = '0px';
                                                            });

                                                            document.addEventListener('mousemove', function (e) {
                                                                if (!isDragging)
                                                                    return;

                                                                e.preventDefault();

                                                                const currentX = e.clientX;
                                                                const currentY = e.clientY;

                                                                const left = Math.min(startX, currentX);
                                                                const top = Math.min(startY, currentY);
                                                                const width = Math.abs(currentX - startX);
                                                                const height = Math.abs(currentY - startY);

                                                                selectionBox.style.left = left + 'px';
                                                                selectionBox.style.top = top + 'px';
                                                                selectionBox.style.width = width + 'px';
                                                                selectionBox.style.height = height + 'px';

                                                                updateRowSelections(left, top, width, height);
                                                            });

                                                            document.addEventListener('mouseup', function (e) {
                                                                if (!isDragging)
                                                                    return;

                                                                isDragging = false;
                                                                selectionBox.style.display = 'none';
                                                                document.body.classList.remove('drag-mode');

                                                                document.querySelectorAll('.selecting').forEach(row => {
                                                                    row.classList.remove('selecting');
                                                                });

                                                                const clickedRow = e.target.closest('tr');
                                                                if (selectionBox.style.width === '0px' && selectionBox.style.height === '0px' && clickedRow) {
                                                                    const checkbox = clickedRow.querySelector('.row-checkbox');
                                                                    if (checkbox) {
                                                                        if (selectedRows.has(clickedRow)) {
                                                                            selectedRows.delete(clickedRow);
                                                                            checkbox.checked = false;
                                                                            clickedRow.classList.remove('drag-selected');
                                                                        } else {
                                                                            selectedRows.add(clickedRow);
                                                                            checkbox.checked = true;
                                                                            clickedRow.classList.add('drag-selected');
                                                                        }
                                                                    }
                                                                }

                                                                updateCheckboxesFromSelection();
                                                                updateSelectAllState();
                                                                updateBulkActionsPanel();
                                                            });
                                                        }

                                                        function updateRowSelections(left, top, width, height) {
                                                            const rows = tableBody.querySelectorAll('tr');

                                                            rows.forEach(row => {
                                                                if (row.children.length === 1 && row.children[0].colSpan)
                                                                    return;

                                                                const rect = row.getBoundingClientRect();
                                                                const rowLeft = rect.left;
                                                                const rowTop = rect.top;
                                                                const rowRight = rect.right;
                                                                const rowBottom = rect.bottom;

                                                                const intersects = !(left > rowRight ||
                                                                        left + width < rowLeft ||
                                                                        top > rowBottom ||
                                                                        top + height < rowTop);

                                                                if (intersects) {
                                                                    row.classList.add('selecting');
                                                                    selectedRows.add(row);
                                                                } else {
                                                                    row.classList.remove('selecting');
                                                                }
                                                            });
                                                        }

                                                        function updateCheckboxesFromSelection() {
                                                            rowCheckboxes.forEach(checkbox => {
                                                                const row = checkbox.closest('tr');
                                                                const shouldBeChecked = selectedRows.has(row);

                                                                // Only update if different to avoid triggering change event
                                                                if (checkbox.checked !== shouldBeChecked) {
                                                                    checkbox.checked = shouldBeChecked;
                                                                }

                                                                if (shouldBeChecked) {
                                                                    row.classList.add('drag-selected');
                                                                } else {
                                                                    row.classList.remove('drag-selected');
                                                                }
                                                            });
                                                        }

                                                        function clearAllSelections() {
                                                            selectedRows.clear();
                                                            rowCheckboxes.forEach(checkbox => {
                                                                checkbox.checked = false;
                                                                const row = checkbox.closest('tr');
                                                                if (row) {
                                                                    row.classList.remove('drag-selected', 'selecting');
                                                                }
                                                            });
                                                            selectAllCheckbox.checked = false;
                                                            selectAllCheckbox.indeterminate = false;
                                                        }
                                                    });

                                                    function bulkDeleteNotifications() {
                                                        const checkedBoxes = document.querySelectorAll('.row-checkbox:checked');

                                                        if (checkedBoxes.length === 0) {
                                                            alert('Please select at least one notification to delete.');
                                                            return;
                                                        }

                                                        const confirmMessage = 'Are you sure you want to delete ' + checkedBoxes.length + ' notification(s)?';

                                                        if (confirm(confirmMessage)) {
                                                            document.getElementById('bulkDeleteForm').submit();
                                                        }
                                                    }

                                                    function clearSelection() {
                                                        selectedRows.clear();

                                                        document.querySelectorAll('.notification-checkbox').forEach(checkbox => {
                                                            checkbox.checked = false;
                                                        });

                                                        document.querySelectorAll('tr').forEach(row => {
                                                            row.classList.remove('drag-selected', 'selecting');
                                                        });

                                                        const selectAllCheckbox = document.getElementById('selectAll');
                                                        selectAllCheckbox.checked = false;
                                                        selectAllCheckbox.indeterminate = false;

                                                        const bulkActionsPanel = document.getElementById('bulkActions');
                                                        bulkActionsPanel.classList.remove('show');
                                                    }
        </script>
        <script src="../assets/admin/js/app.js"></script>
    </body>
</html>