<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

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
        <title>Employee Support Messages</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                background: #f5f6fa;
                overflow-x: hidden;
            }

            /* Fix the main wrapper structure */
            .wrapper {
                display: flex;
                width: 100%;
                min-height: 100vh;
            }

            /* Admin sidebar styles - keep existing admin sidebar */
            #sidebar {
                width: 250px;
                background: #343a40;
                min-height: 100vh;
                flex-shrink: 0;
            }

            .main {
                flex: 1;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }

            .content {
                flex: 1;
                padding: 0;
                background: #f5f6fa;
            }

            /* Container for the chat interface - reset margins */
            .content .container {
                max-width: none;
                width: 100%;
                height: calc(100vh - 70px); /* Subtract navbar height */
                margin: 0;
                padding: 15px;
                display: flex;
                gap: 0;
            }

            /* Chat sidebar (customer list) */
            .sidebar {
                width: 320px;
                background: white;
                height: 100%;
                overflow-y: auto;
                border-right: 1px solid #e0e0e0;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                display: flex;
                flex-direction: column;
                border-radius: 8px 0 0 8px;
            }

            .sidebar-support {
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                padding: 20px;
                text-align: center;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                border-radius: 8px 0 0 0;
            }

            .sidebar-support h3 {
                margin: 0 0 5px 0;
                font-size: 18px;
                font-weight: 600;
            }

            .sidebar-support p {
                margin: 0;
                font-size: 13px;
                opacity: 0.9;
            }

            .search-container {
                padding: 15px;
                background: white;
                border-bottom: 1px solid #e9ecef;
            }

            .search-box {
                position: relative;
            }

            .search-input {
                width: 100%;
                padding: 10px 15px 10px 40px;
                border: 1px solid #ddd;
                border-radius: 20px;
                font-size: 14px;
                transition: all 0.3s ease;
                background: #f8f9fa;
            }

            .search-input:focus {
                outline: none;
                border-color: #667eea;
                background: white;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .search-icon {
                position: absolute;
                left: 12px;
                top: 50%;
                transform: translateY(-50%);
                color: #999;
                font-size: 14px;
            }

            .chat-list {
                flex: 1;
                list-style-type: none;
                padding: 0;
                margin: 0;
                overflow-y: auto;
            }

            .chat-list::-webkit-scrollbar {
                width: 4px;
            }

            .chat-list::-webkit-scrollbar-track {
                background: #f1f1f1;
            }

            .chat-list::-webkit-scrollbar-thumb {
                background: #ccc;
                border-radius: 4px;
            }

            .chat-item {
                padding: 16px 20px;
                border-bottom: 1px solid #f0f0f0;
                cursor: pointer;
                transition: all 0.3s ease;
                background: white;
                margin: 5px 10px;
                border-radius: 12px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            }

            .chat-item:hover {
                background: #f8f9fa;
                transform: translateY(-1px);
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }

            .chat-item.active {
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(102, 126, 234, 0.3);
            }

            .customer-info {
                font-weight: 600;
                font-size: 15px;
                margin-bottom: 6px;
                display: flex;
                align-items: center;
            }

            .customer-info i {
                margin-right: 8px;
                color: #667eea;
            }

            .chat-item.active .customer-info i {
                color: white;
            }

            .message-time {
                font-size: 12px;
                opacity: 0.7;
                display: flex;
                align-items: center;
                margin-bottom: 4px;
            }

            .message-time i {
                margin-right: 5px;
                font-size: 11px;
            }

            .processing-status {
                font-size: 11px;
                background: rgba(40, 167, 69, 0.1);
                color: #28a745;
                padding: 3px 8px;
                border-radius: 10px;
                margin-top: 5px;
                display: inline-block;
            }

            .chat-item.active .processing-status {
                background: rgba(255, 255, 255, 0.2);
                color: white;
            }

            .empty-state {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                padding: 40px 20px;
                text-align: center;
                color: #6c757d;
                height: 200px;
            }

            .empty-state i {
                font-size: 40px;
                margin-bottom: 15px;
                opacity: 0.5;
            }

            .empty-state h4 {
                margin-bottom: 8px;
                font-size: 16px;
                font-weight: 600;
            }

            .empty-state p {
                font-size: 13px;
                opacity: 0.8;
            }

            /* Main Content Styles - Chat Area */
            .main-content {
                flex: 1;
                display: flex;
                flex-direction: column;
                background: white;
                border-radius: 0 8px 8px 0;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
                overflow: hidden;
                height: 100%;
            }

            .chat-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px 25px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                box-shadow: 0 2px 10px rgba(102, 126, 234, 0.2);
                border-radius: 0 8px 0 0;
            }

            .chat-header h2 {
                margin: 0;
                font-size: 20px;
                font-weight: 600;
            }

            #complete-btn {
                background: linear-gradient(135deg, #28a745, #20c997);
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 20px;
                cursor: pointer;
                font-weight: 500;
                font-size: 14px;
                transition: all 0.3s ease;
                box-shadow: 0 4px 12px rgba(40, 167, 69, 0.3);
            }

            #complete-btn:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
            }

            #chat-messages {
                flex: 1;
                overflow-y: auto;
                padding: 25px;
                background: #f8f9fa;
                display: flex;
                flex-direction: column;
                gap: 15px;
            }

            #chat-messages::-webkit-scrollbar {
                width: 6px;
            }

            #chat-messages::-webkit-scrollbar-track {
                background: #f1f1f1;
                border-radius: 6px;
            }

            #chat-messages::-webkit-scrollbar-thumb {
                background: linear-gradient(135deg, #667eea, #764ba2);
                border-radius: 6px;
            }

            .message {
                max-width: 70%;
                padding: 15px 18px;
                border-radius: 18px;
                box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
                line-height: 1.5;
            }
            .message.new-message {
                animation: messageSlideIn 0.3s ease-out;
            }

            @keyframes messageSlideIn {
                from {
                    opacity: 0;
                    transform: translateY(15px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .customer {
                align-self: flex-start;
                background: white;
                color: #2c3e50;
                border: 1px solid #e9ecef;
                border-left: 4px solid #667eea;
            }

            .employee {
                align-self: flex-end;
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                box-shadow: 0 4px 15px rgba(102, 126, 234, 0.25);
            }

            .message-content {
                font-size: 14px;
                word-wrap: break-word;
                margin-bottom: 6px;
            }

            .timestamp {
                font-size: 11px;
                opacity: 0.7;
                text-align: right;
                font-weight: 400;
            }

            #chat-form {
                display: flex;
                padding: 20px 25px;
                background: white;
                border-top: 1px solid #e9ecef;
                align-items: flex-end;
                gap: 12px;
                border-radius: 0 0 8px 0;
            }

            #message-input {
                flex: 1;
                border: 2px solid #e9ecef;
                border-radius: 20px;
                padding: 12px 18px;
                font-size: 14px;
                resize: none;
                min-height: 45px;
                max-height: 100px;
                transition: all 0.3s ease;
                font-family: inherit;
                background: #f8f9fa;
            }

            #message-input:focus {
                outline: none;
                border-color: #667eea;
                background: white;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            #send-button {
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                border: none;
                border-radius: 50%;
                width: 45px;
                height: 45px;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 16px;
                box-shadow: 0 3px 12px rgba(102, 126, 234, 0.3);
            }

            #send-button:hover {
                transform: translateY(-2px) scale(1.05);
                box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
            }

            #send-button:active {
                transform: translateY(0) scale(0.95);
            }

            #no-chat-selected {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                height: 100%;
                color: #6c757d;
                text-align: center;
            }

            #no-chat-selected i {
                font-size: 48px;
                margin-bottom: 15px;
                opacity: 0.5;
            }

            #no-chat-selected h3 {
                font-size: 20px;
                margin-bottom: 8px;
                font-weight: 600;
            }

            #no-chat-selected p {
                font-size: 14px;
                opacity: 0.8;
            }

            /* Remove the problematic mt-4 rule */
            .mt-4 {
                margin-top: 1.5rem !important;
                margin-left: 0 !important;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .content .container {
                    flex-direction: column;
                    height: auto;
                    min-height: calc(100vh - 70px);
                }

                .sidebar {
                    width: 100%;
                    height: 300px;
                    border-radius: 8px 8px 0 0;
                    border-right: none;
                    border-bottom: 1px solid #e0e0e0;
                }

                .main-content {
                    border-radius: 0 0 8px 8px;
                    min-height: 400px;
                }

                .message {
                    max-width: 85%;
                }
            }

            /* Animation for online status */
            .online-indicator {
                width: 8px;
                height: 8px;
                background: #28a745;
                border-radius: 50%;
                display: inline-block;
                margin-left: 8px;
                animation: pulse 2s infinite;
            }

            @keyframes pulse {
                0% {
                    box-shadow: 0 0 0 0 rgba(40, 167, 69, 0.7);
                }
                70% {
                    box-shadow: 0 0 0 10px rgba(40, 167, 69, 0);
                }
                100% {
                    box-shadow: 0 0 0 0 rgba(40, 167, 69, 0);
                }
            }

            @keyframes pulse {
                0% {
                    box-shadow: 0 0 0 0 rgba(40, 167, 69, 0.7);
                }
                70% {
                    box-shadow: 0 0 0 10px rgba(40, 167, 69, 0);
                }
                100% {
                    box-shadow: 0 0 0 0 rgba(40, 167, 69, 0);
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Spring Web MVC project</title>
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
                        <li class="sidebar-item">
                            <a class="sidebar-link" href="accountVouchers.htm">
                                <i class="align-middle me-2" data-feather="percent"></i> 
                                <span class="align-middle">Specific Vouchers</span>
                            </a>
                        </li>
                        <li class="sidebar-item active">
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
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/employee/accountInformation.htm">
                                        <i class="fa fa-user"></i> Account Information
                                    </a>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/employee/changePassword.htm">
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
                    <div class="container mt-4">
                        <div class="sidebar">
                            <div class="sidebar-support">
                                <h3><i class="fas fa-headset" style="margin-right: 8px;"></i>Support Customer</h3>
                                <p>Message management and support</p>
                            </div>

                            <div class="search-container">
                                <div class="search-box">
                                    <input type="text" class="search-input" placeholder="Search Customer...">
                                    <i class="fas fa-search search-icon"></i>
                                </div>
                            </div>

                            <ul class="chat-list">
                                <c:choose>
                                    <c:when test="${empty customers}">
                                        <li>
                                            <div class="empty-state">
                                                <i class="fas fa-inbox"></i>
                                                <h4>No messages</h4>
                                                <p>There are no customer support messages yet.</p>
                                            </div>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="customer" items="${customers}">
                                            <li class="chat-item" data-customer-id="${customer.customerID}" data-employee-id="${customer.employeeID}">
                                                <div class="customer-info">
                                                    <i class="fas fa-user-circle"></i>
                                                    <span><c:out value="${customer.fullName}" /></span>
                                                </div>
                                                <div style="font-size: 12px; opacity: 0.8; margin-bottom: 4px;">
                                                    <i class="fas fa-phone" style="margin-right: 4px;"></i>
                                                    <c:out value="${customer.phoneNumber}" />
                                                </div>
                                                <div class="message-time">
                                                    <i class="fas fa-clock"></i>
                                                    <c:choose>
                                                        <c:when test="${customer.sendDateFormatted != null}">
                                                            ${customer.sendDateFormatted}
                                                        </c:when>
                                                        <c:otherwise>N/A</c:otherwise>
                                                    </c:choose>
                                                </div>

                                                <c:if test="${customer.employeeID != null}">
                                                    <div class="processing-status">
                                                        <i class="fas fa-user-check" style="margin-right: 3px;"></i>
                                                        Emp #<c:out value="${customer.employeeID}" />
                                                    </div>
                                                </c:if>
                                            </li>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </ul>
                        </div>

                        <div class="main-content">
                            <div class="chat-header">
                                <h2>Support Message</h2>
                                <button id="complete-btn" style="display: none;">
                                    Close the conversation
                                </button>
                            </div>

                            <div id="chat-messages">
                                <div id="no-chat-selected">
                                    <i class="fas fa-comment-dots"></i>
                                    <h3>Customer Support System</h3>
                                    <p>Select a customer to start a conversation</p>
                                </div>

                                <c:forEach var="message" items="${messages}">
                                    <div class="message ${message.employeeID == null ? 'customer' : 'employee'}">
                                        <div class="message-content"><c:out value="${message.message}" /></div>
                                        <div class="timestamp">
                                            <c:choose>
                                                <c:when test="${message.sendDate != null}">
                                                    ${message.sendDate}
                                                </c:when>
                                                <c:otherwise>N/A</c:otherwise>
                                            </c:choose>                                </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <div id="chat-form">
                                <textarea id="message-input" placeholder="Enter your message..." required></textarea>
                                <button id="send-button" type="button">
                                    <i class="fas fa-paper-plane"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.js"></script>
        <script>
            feather.replace();
        </script>
        <script src="../assets/admin/js/app.js"></script>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                let currentCustomerID = null;
                document.querySelectorAll('.chat-item').forEach(item => {
                    item.addEventListener('click', function () {
                        document.querySelectorAll('.chat-item').forEach(el => el.classList.remove('active'));
                        this.classList.add('active');

                        currentCustomerID = this.getAttribute('data-customer-id');
                        if (currentCustomerID) {
                            $('#chat-messages').show();
                            loadMessages(currentCustomerID);
                            const completeBtn = document.querySelector('#complete-btn');
                            if (completeBtn) {
                                completeBtn.style.display = 'block';
                            }

                            startPollingMessages(currentCustomerID);
                        }
                    });
                });

                function startPollingMessages(customerID) {
                    if (window.pollingInterval)
                        clearInterval(window.pollingInterval);
                    window.pollingInterval = setInterval(function () {
                        loadMessages(customerID);
                    }, 3000);
                }

                function loadMessages(customerID) {
                    if (!customerID) {
                        console.error("Customer ID is missing or invalid");
                        return;
                    }

                    $.ajax({
                        url: '../employee/getCustomerMessages.htm',
                        type: 'GET',
                        dataType: 'xml',
                        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                        data: {customerID: customerID},
                        success: function (data) {
                            console.log("Raw XML response:", new XMLSerializer().serializeToString(data));
                            var messages = $(data).find('message');
                            $('#chat-messages').empty();
                            console.log("Number of messages found:", messages.length);
                            messages.each(function () {
                                var parsedMessage = {
                                    supportID: $(this).find('supportID').text(),
                                    customerID: $(this).find('customerID').text(),
                                    employeeID: $(this).find('employeeID').text(),
                                    content: $(this).find('messageContent').text(),
                                    date: $(this).find('sendDate').text()
                                };
                                console.log("Parsed message:", parsedMessage);

                                if (parsedMessage.content) {
                                    addMessageToChat(
                                            parsedMessage.content,
                                            parsedMessage.employeeID !== 'null' && parsedMessage.employeeID !== '',
                                            parsedMessage.date
                                            );
                                } else {
                                    console.warn("Empty message content for supportID:", parsedMessage.supportID);
                                }
                            });

                            scrollChatToBottom();
                        },
                        error: function (xhr, status, error) {
                            console.error('Error fetching messages:', status, error);
                            console.error('Response Text:', xhr.responseText);
                        }
                    });
                }

                function scrollChatToBottom() {
                    const chatMessages = document.getElementById('chat-messages');
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                }

                function checkMessageVisibility() {
                    const messages = $('#chat-messages .message');
                    console.log("Total messages:", messages.length);
                    messages.each(function (index) {
                        const $this = $(this);
                        console.log(`Message ${index + 1}:`, {
                            visible: $this.is(':visible'),
                            display: $this.css('display'),
                            height: $this.height(),
                            text: $this.text()
                        });
                    });
                }

                function completeConversation() {
                    if (!currentCustomerID) {
                        console.log("No customer selected");
                        return;
                    }

                    if (confirm("Are you sure you want to complete and delete this conversation?")) {
                        console.log(`Attempting to complete conversation for customer ID: ${currentCustomerID}`);
                        fetch(`../employee/completeConversation.htm`, {
                            method: 'POST',
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                            body: new URLSearchParams({
                                customerID: currentCustomerID
                            })
                        })
                                .then(response => {
                                    console.log(`Server response status: ${response.status}`);
                                    console.log(`Server response status text: ${response.statusText}`);
                                    return response.text().then(text => {
                                        console.log(`Server response body: ${text}`);
                                        if (response.ok) {
                                            return text;
                                        } else {
                                            throw new Error(`Server responded with status: ${response.status}, body: ${text}`);
                                        }
                                    });
                                })
                                .then(result => {
                                    if (result === 'success') {
                                        alert("Conversation has been completed and deleted successfully.");

                                        const chatItem = document.querySelector(`.chat-item[data-customer-id="${currentCustomerID}"]`);
                                        if (chatItem) {
                                            chatItem.remove();
                                        }

                                        document.getElementById('chat-messages').innerHTML = `
        <div id="no-chat-selected">
            <i class="fas fa-comment-dots"></i>
            <h3>Customer Support System</h3>
            <p>Select a customer to start a conversation</p>
        </div>
    `;

                                        document.getElementById('complete-btn').style.display = 'none';
                                        currentCustomerID = null;

                                        if (window.pollingInterval) {
                                            clearInterval(window.pollingInterval);
                                            window.pollingInterval = null;
                                        }
                                    }
                                })
                                .catch(error => {
                                    console.error("Detailed error in completing conversation:", error);
                                    alert(`Cannot complete conversation. Error: ${error.message}`);
                                });
                    }
                }

                document.getElementById('complete-btn').addEventListener('click', completeConversation);

                function addMessageToChat(message, isFromEmployee = false, timestamp = null) {
                    console.log("Adding message to chat:", {message, isFromEmployee, timestamp});

                    const chatMessages = $('#chat-messages');
                    const messageElement = $('<div>').addClass(isFromEmployee ? 'message employee' : 'message customer');

                    try {
                        const messageContent = $('<div>').addClass('message-content').text(message);
                        console.log("Created message content element:", messageContent[0].outerHTML);
                        messageElement.append(messageContent);

                        if (timestamp) {
                            let date;
                            if (timestamp.includes('T')) {
                                date = new Date(timestamp);
                            } else {
                                date = new Date(timestamp.replace(' ', 'T'));
                            }

                            const formattedDate = date.toLocaleString('en-US', {
                                year: 'numeric',
                                month: '2-digit',
                                day: '2-digit',
                                hour: '2-digit',
                                minute: '2-digit'
                            });
                            const timeElement = $('<div>').addClass('timestamp').text(formattedDate);
                            console.log("Created time element:", timeElement[0].outerHTML);
                            messageElement.append(timeElement);
                        }

                        console.log("Final message element before appending:", messageElement[0].outerHTML);
                        chatMessages.append(messageElement);

                        const addedMessage = chatMessages.find(`.message:contains('${message}')`);
                        if (addedMessage.length === 0) {
                            console.error("Message was not successfully added to the DOM");
                        } else {
                            console.log("Message successfully added to the DOM");
                        }
                    } catch (error) {
                        console.error("Error in addMessageToChat:", error);
                    }

                    scrollChatToBottom();
                }

                function sendMessage() {
                    const messageInput = document.querySelector('#message-input');
                    const message = messageInput.value.trim();

                    if (!currentCustomerID) {
                        console.log("No customer selected");
                        return;
                    }

                    if (message) {
                        console.log("Sending message:", message);

                        fetch(`../employee/sendMessage.htm`, {
                            method: 'POST',
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                            body: new URLSearchParams({
                                customerID: currentCustomerID,
                                message: message
                            })
                        })
                                .then(response => {
                                    if (response.ok) {
                                        return response.text();
                                    } else {
                                        throw new Error("Error when sending message: " + response.statusText);
                                    }
                                })
                                .then(result => {
                                    if (result === 'success') {
                                        addMessageToChat(message, true, new Date().toISOString());
                                        scrollChatToBottom();
                                        messageInput.value = '';
                                        loadMessages(currentCustomerID);
                                    } else {
                                        throw new Error("Cannot send message");
                                    }
                                })
                                .catch(error => {
                                    console.error("Error sending message:", error);
                                    alert("Cannot send message. Please try again");
                                });
                    }
                }

                document.querySelector('#send-button').addEventListener('click', sendMessage);

                document.querySelector('#message-input').addEventListener('keypress', function (e) {
                    if (e.key === 'Enter' && !e.shiftKey) {
                        e.preventDefault();
                        sendMessage();
                    }
                });

                const messageInput = document.querySelector('#message-input');
                messageInput.addEventListener('input', function () {
                    this.style.height = 'auto';
                    this.style.height = Math.min(this.scrollHeight, 120) + 'px';
                });

                const searchInput = document.querySelector('.search-input');
                searchInput.addEventListener('input', function () {
                    const searchTerm = this.value.toLowerCase();
                    const chatItems = document.querySelectorAll('.chat-item');

                    chatItems.forEach(item => {
                        const customerInfo = item.querySelector('.customer-info');
                        if (customerInfo) {
                            const text = customerInfo.textContent.toLowerCase();
                            item.style.display = text.includes(searchTerm) ? 'block' : 'none';
                        }
                    });
                });

                let typingTimer;
                messageInput.addEventListener('input', function () {
                    clearTimeout(typingTimer);
                    typingTimer = setTimeout(() => {
                    }, 1000);
                });

                function pollForNewCustomers() {
                    $.ajax({
                        url: '../employee/getUpdatedCustomerList.htm',
                        type: 'GET',
                        dataType: 'xml',
                        success: function (xml) {
                            console.log("XML response:", xml);
                            updateCustomerList(xml);
                        },
                        error: function (xhr) {
                            console.error("Error:", xhr.responseText);
                        }
                    });
                }

                function updateCustomerList(xml) {
                    console.log("=== updateCustomerList Debug ===");
                    console.log("Raw XML:", new XMLSerializer().serializeToString(xml));

                    const chatList = document.querySelector('.chat-list');
                    const existingCustomerIds = Array.from(document.querySelectorAll('.chat-item'))
                            .map(item => parseInt(item.getAttribute('data-customer-id')));

                    const customers = xml.getElementsByTagName("customer");
                    console.log("Number of customers found:", customers.length);

                    const emptyState = chatList.querySelector('.empty-state');
                    if (emptyState && customers.length > 0) {
                        emptyState.parentElement.remove();
                    }

                    Array.from(customers).forEach(custNode => {
                        const customerID = parseInt(custNode.getElementsByTagName("customerID")[0].textContent);
                        const fullName = custNode.getElementsByTagName("fullName")[0].textContent;
                        const phoneNumber = custNode.getElementsByTagName("phoneNumber")[0]?.textContent || "";
                        const sendDateStr = custNode.getElementsByTagName("sendDate")[0]?.textContent;
                        const employeeID = custNode.getElementsByTagName("employeeID")[0]?.textContent;

                        console.log('Processing customer:', {customerID, fullName, phoneNumber, sendDateStr, employeeID});

                        if (!existingCustomerIds.includes(customerID)) {
                            console.log('Adding new customer:', fullName);

                            const sendDate = sendDateStr ? new Date(sendDateStr) : new Date();
                            const formattedDate = sendDate.toLocaleString('en-US');

                            const li = document.createElement('li');
                            li.className = 'chat-item';
                            li.setAttribute('data-customer-id', customerID);
                            li.setAttribute('data-employee-id', employeeID || '');

                            let htmlContent = `
            <div class="customer-info">
                <i class="fas fa-user-circle"></i>
                <span>\${fullName}</span>
            </div>
            <div style="font-size: 12px; opacity: 0.8; margin-bottom: 4px;">
                <i class="fas fa-phone" style="margin-right: 4px;"></i>
                \${phoneNumber}
            </div>
            <div class="message-time">
                <i class="fas fa-clock"></i>
                \${formattedDate}
            </div>
        `;

                            if (employeeID && employeeID !== 'null' && employeeID !== '') {
                                htmlContent += `
                <div class="processing-status">
                    <i class="fas fa-user-check" style="margin-right: 3px;"></i>
                    Emp #\${employeeID}
                </div>
            `;
                            }

                            htmlContent = htmlContent
                                    .replace('\\${fullName}', fullName)
                                    .replace('\\${phoneNumber}', phoneNumber)
                                    .replace('\\${formattedDate}', formattedDate);

                            if (employeeID && employeeID !== 'null' && employeeID !== '') {
                                htmlContent = htmlContent.replace('\\${employeeID}', employeeID);
                            }

                            li.innerHTML = htmlContent;

                            li.addEventListener('click', function () {
                                document.querySelectorAll('.chat-item').forEach(el => el.classList.remove('active'));
                                this.classList.add('active');

                                currentCustomerID = this.getAttribute('data-customer-id');
                                if (currentCustomerID) {
                                    $('#chat-messages').show();
                                    loadMessages(currentCustomerID);
                                    const completeBtn = document.querySelector('#complete-btn');
                                    if (completeBtn) {
                                        completeBtn.style.display = 'block';
                                    }

                                    startPollingMessages(currentCustomerID);
                                }
                            });

                            chatList.insertBefore(li, chatList.firstChild);

                            console.log('Successfully added customer:', li.querySelector('.customer-info span').textContent);
                        }
                    });
                }

                function startGlobalPolling() {
                    window.globalPollingInterval = setInterval(function () {
                        pollForNewCustomers();
                    }, 5000);
                }

                startGlobalPolling();
            });
        </script>
    </body>
</html>