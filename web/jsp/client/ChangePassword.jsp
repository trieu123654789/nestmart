<%-- 
    Document   : index
    Created on : Oct 5, 2024, 11:04:48 AM
    Author     : Win10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Nestmart - Change Password</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet"/>
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="../assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="../assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="../assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="../assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="../assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="../assets/client/css/style.css" />
        <link rel="stylesheet" href="../assets/client/css/main-color04.css" />
        <style>

            #main-content {
                width: 100%;
                display: flex;
                justify-content: center;
                padding-top: 50px;
            }

            .form-wrapper {
                display: flex;
                justify-content: center;
                width: 100%;
            }

            .form-container {
                background-color: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                max-width: 500px;
                width: 100%;
                box-sizing: border-box;
            }

            .txt-input {
                width: 100%;
                padding: 12px;
                border: 2px solid #ced4da;
                border-radius: 8px;
                font-size: 16px;
                box-sizing: border-box;
                transition: border-color 0.3s, box-shadow 0.3s;
            }

            .form-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
            }

            .form-row .form-group {
                flex: 1;
                margin-right: 10px;
            }

            .form-row .form-group:last-child {
                margin-right: 0;
            }

            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
                font-size: 16px;
                color: #333;
            }

            .error-message {
                color: #dc3545;
                font-size: 0.875em;
                margin-top: 5px;
                font-style: italic;
            }

            .alert-success, .alert-danger {
                border-radius: 8px;
                padding: 15px;
                margin-bottom: 15px;
                font-size: 16px;
                font-weight: bold;
            }

            .alert-success {
                color: #155724;
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
            }

            .alert-danger {
                color: #721c24;
                background-color: #f8d7da;
                border: 1px solid #f5c6cb;
            }

            .form-row.wrap-btn {
                display: flex;
                flex-direction: column;
                align-items: center;
                margin-top: 20px;
            }

            .form-row.wrap-btn > * {
                margin: 10px 0;
            }

            .btn-submit {
                background-color: #ff9702;
                color: #fff;
                border: none;
                padding: 12px 20px;
                font-size: 18px;
                border-radius: 8px;
                cursor: pointer;
                transition: background-color 0.3s, transform 0.3s;
                width: 100%;
                text-align: center;
            }

            .btn-submit:hover {
                background-color: #0056b3;
                transform: scale(1.05);
            }

            .btn-submit:active {
                background-color: #004085;
                transform: scale(0.98);
            }

            .link-to-help {
                color: #ff9702;
                text-decoration: none;
                font-size: 16px;
                text-align: center;
            }
            .fa {
                font-family: FontAwesome !important;
            }
            .link-to-help:hover {
                text-decoration: underline;
            }
            .button-group {
                display: flex;
                justify-content: center;
                gap: 10px;
            }

            .button-group button,
            .button-group a {
                flex: 1;
                text-align: center;
                background-color: #ff9702;
                color: #fff;
                border: none;
                padding: 12px 20px;
                font-size: 18px;
                border-radius: 8px;
                cursor: pointer;
                transition: background-color 0.3s, transform 0.3s;
                text-decoration: none;
                display: inline-block;
            }

            /* Hover effect cho từng button riêng lẻ */
            .button-group button:hover,
            .button-group a:hover {
                background-color: #e08900 !important;
                color: #ffffff !important;
                transform: scale(1.05);
            }

            .button-group button:active,
            .button-group a:active {
                background-color: #cc7700;
                transform: scale(0.98);
            }

            /* Style riêng cho nút Cancel */
            .btn-secondary {
                background-color: #6c757d !important;
            }

            .btn-secondary:hover {
                background-color: #e08900 !important;
                color: #ffffff !important;
            }

            /* Mobile button adjustments */
            @media (max-width: 480px) {
                .button-group {
                    flex-direction: column;
                    gap: 15px;
                }

                .button-group button,
                .button-group a {
                    flex: none;
                    width: 100%;
                }
            }

            /* Style the popup content */
            .popup-content {
                display: none;
                position: absolute;
                top: 50px;
                right: 0;
                background-color: #FF9702;
                min-width: 200px;
                box-shadow: 0px 8px 16px rgba(0,0,0,0.3);
                z-index: 9999;
                border-radius: 8px;
                padding: 10px 0;
            }

            .popup-content a {
                color: #FF9702;
                font-weight: 600;
                padding: 12px 16px;
                text-decoration: none;
                display: block;
                transition: background-color 0.3s ease, color 0.3s ease; /* Smooth transition on hover */
            }


            .popup-content a:hover {
                background-color: #FF9702;
                color: #ffffff;
                border-radius: 4px;
            }

            .dropdown {
                position: relative;
            }

        </style>
    </head>
    <body>
        <c:if test="${not empty message}">
            <div class="alert alert-success">
                ${message}
            </div>
        </c:if>
        <jsp:include page="/jsp/client/header.jsp" />

        <div class="hero-section hero-background">
            <h1 class="page-title">Organic Fruits</h1>
        </div>

        <div class="container">
            <nav class="nestmart-nav">

            </nav>
        </div>

        <div id="main-content" class="main-content">
            <div class="form-wrapper">
                <div class="form-container">
                    <h2 class="text-center mb-4">Change Password</h2>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <c:if test="${not empty message}">
                        <div class="alert alert-success">${message}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/client/changePassword.htm" method="post">
                        <input type="hidden" name="accountID" value="${account.accountID}" />

                        <div class="form-row">
                            <div class="form-group">
                                <label for="oldPassword"><strong>Current Password:</strong></label>
                                <input type="password" id="oldPassword" name="oldPassword" class="txt-input" required />
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="newPassword"><strong>New Password:</strong></label>
                                <input type="password" id="newPassword" name="newPassword" class="txt-input" required />
                            </div>

                            <div class="form-group">
                                <label for="confirmNewPassword"><strong>Confirm New Password:</strong></label>
                                <input type="password" id="confirmNewPassword" name="confirmNewPassword" class="txt-input" required />
                            </div>
                        </div>

                        <div class="text-center button-group">
                            <button type="submit" class="btn-submit">Change Password</button>
                            <a href="${pageContext.request.contextPath}/client/clientboard.htm" class="btn-submit btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/client/footer.jsp" />

        <a class="btn-scroll-top"><i class="nestmart-icon icon-left-arrow"></i></a>


        <script src="assets/client/js/jquery-3.4.1.min.js"></script>
        <script src="assets/client/js/bootstrap.min.js"></script>
        <script src="assets/client/js/jquery.countdown.min.js"></script>
        <script src="assets/client/js/jquery.nice-select.min.js"></script>
        <script src="assets/client/js/jquery.nicescroll.min.js"></script>
        <script src="assets/client/js/slick.min.js"></script>
        <script src="assets/client/js/nestmart.framework.js"></script>
        <script src="assets/client/js/functions.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.29.2/feather.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const usernameTab = document.getElementById('username-tab');
                const otpTab = document.getElementById('otp-tab');
                const usernameForm = document.getElementById('username-form');
                const otpForm = document.getElementById('otp-form');

                function showForm(formToShow) {
                    usernameForm.style.display = 'none';
                    otpForm.style.display = 'none';
                    usernameTab.classList.remove('active');
                    otpTab.classList.remove('active');

                    if (formToShow === 'username') {
                        usernameForm.style.display = 'block';
                        usernameTab.classList.add('active');
                    } else if (formToShow === 'otp') {
                        otpForm.style.display = 'block';
                        otpTab.classList.add('active');
                    }
                }

                usernameTab.addEventListener('click', function () {
                    showForm('username');
                });

                otpTab.addEventListener('click', function () {
                    showForm('otp');
                });

                showForm('username');
            });


        </script>
        <script>
            $(document).ready(function () {
                $("#otpForm").hide();

                $("#emailForm").submit(function (event) {
                    event.preventDefault();
                    $.post("${pageContext.request.contextPath}/sendOtp", {email: $("#email").val()}, function () {
                        $("#emailForm").hide();
                        $("#otpForm").show();
                    });
                });

                $("#backToEmail").click(function () {
                    $("#otpForm").hide();
                    $("#emailForm").show();
                });
            });
        </script>
        <jsp:include page="livechat.jsp" />
    </body>
</html>
