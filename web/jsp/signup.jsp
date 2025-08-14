
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Nestmart - Signup Page</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet"/>
        <link rel="shortcut icon" type="image/x-icon" href="assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="assets/client/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="assets/client/css/animate.min.css"/>
        <link rel="stylesheet" href="assets/client/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/client/css/nice-select.css"/>
        <link rel="stylesheet" href="assets/css/slick.min.css"/>
        <link rel="stylesheet" href="assets/client/css/style.css"/>
        <link rel="stylesheet" href="assets/client/css/main-color.css"/>
        <style>
            .form-container {
                background-color: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                max-width: 800px;
                margin: 0 auto;
            }

            .form-row {
                margin-bottom: 15px;
            }

            .form-row.double-column {
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
            }

            .form-row.double-column .form-group {
                flex: 1;
                min-width: calc(50% - 20px);
            }

            .txt-input {
                width: 100%;
                padding: 10px;
                border: 1px solid #ced4da;
                border-radius: 4px;
                font-size: 16px;
                box-sizing: border-box;
            }

            .form-row label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
                font-size: 14px;
                color: #000;
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

            .txt-input:focus {
                border-color: #007bff;
                box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
                outline: none;
            }

            .form-row label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
                font-size: 16px;
                color: #333; /* Màu chữ tối hơn */
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

            .btn-submit {
                background-color: #ff9702;
                color: #fff;
                border: none;
                padding: 12px 20px;
                font-size: 18px;
                border-radius: 8px;
                cursor: pointer;
                transition: background-color 0.3s, transform 0.3s;
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
                margin-top: 15px;
                display: block;
                text-align: center;
            }

            .link-to-help:hover {
                text-decoration: underline;
            }

            .form-container {
                background-color: #f9f9f9;
                border-radius: 12px;
                padding: 30px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .form-row.wrap-btn {
                margin-top: 20px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <!-- Preloader -->
        <div id="biof-loading">
            <div class="biof-loading-center">
                <div class="biof-loading-center-absolute">
                    <div class="dot dot-one"></div>
                    <div class="dot dot-two"></div>
                    <div class="dot dot-three"></div>
                </div>
            </div>
        </div>
        <header id="header" class="header-area style-01 layout-03">
            <div class="header-top bg-main hidden-xs">
                <div class="container">
                    <!-- Left top bar -->
                    <div class="top-bar left">
                        <ul class="horizontal-menu">
                            <li>
                                <a href="#">
                                    <i class="fa fa-envelope" aria-hidden="true"></i> Nestmart@gmail.com
                                </a>
                            </li>
                            <li>
                                <a href="#">Quality is not just our promise, it’s what makes our brand stand out.</a>
                            </li>
                        </ul>
                    </div>
                    <div class="top-bar right">
                        <ul class="horizontal-menu">
                            <c:choose>
                                <c:when test="${not empty sessionScope.email}">
                                    <li class="dropdown">
                                        <a href="javascript:void(0);" class="dropbtn" onclick="togglePopup()">
                                            <i class="nestmart-icon icon-user"></i>Hello, ${sessionScope.email}
                                        </a>
                                        <div id="popupMenu" class="popup-content">
                                            <a href="client/accountInformation.htm">Account Information</a>
                                            <a href="client/changePassword.htm">Change Password</a>
                                            <a href="logout.htm">Logout</a>
                                        </div>
                                    </li>
                                </c:when>

                                <c:otherwise>
                                    <li>
                                        <a href="login.htm" class="login-link">
                                            <i class="nestmart-icon icon-login"></i>Login
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="header-middle nestmart-sticky-object">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-3 col-md-2 col-sm-6 col-xs-6">
                            <a href="client/clientboard.htm" class="nestmart-logo">
                                <img src="assets/client/images/NestMart_logo.png" alt="nestmart logo" width="220" height="36"/>
                            </a>
                        </div>
                        <div class="col-lg-6 col-md-7 hidden-sm hidden-xs">
                            <div class="primary-menu">
                                <ul class="menu nestmart-menu clone-main-menu clone-primary-menu" id="primary-menu" data-menuname="main menu">
                                    <li class="menu-item"><a href="client/clientboard.htm">Home</a>

                                    </li>
                                    <li class="menu-item">

                                        <a href="client/product.htm" class="menu-name" data-title="Product">Product</a>

                                    </li>
                                    <li class="menu-item">

                                        <a href="client/discount.htm" class="menu-name" data-title="Discount">Discount</a>

                                    </li>
                                    <li class="menu-item">
                                        <a href="client/about.htm" class="menu-name" data-title="About">About</a>
                                    </li>
                                    <li class="menu-item">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.email}">
                                                <a href="client/viewNotifications.htm" class="menu-name" data-title="Order History">Notifications</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="login.htm" class="menu-name" data-title="Order History">Notifications</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </li>

                                    <li class="menu-item">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.email}">
                                                <a href="client/orderHistory.htm" class="menu-name" data-title="Order History">My Purchase</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="login.htm" class="menu-name" data-title="Order History">My Purchase</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-3 col-md-6 col-xs-6">
                            <div class="nestmart-cart-info">
                                <div class="mobile-search">
                                    <a href="javascript:void(0)" class="open-searchbox"><i class="nestmart-icon icon-search"></i></a>
                                    <div class="mobile-search-content">
                                        <form action="client/product.htm" class="form-search" name="mobile-search" method="get">
                                            <a href="#" class="btn-close"><span class="nestmart-icon icon-close-menu"></span></a>
                                            <input type="text" name="keyword" class="input-text" value="" placeholder="Search here..."/>
                                            <button type="submit" class="btn-submit">Go</button>
                                        </form>
                                    </div>
                                </div>
                                <div class="minicart-block">
                                    <div class="minicart-contain">
                                        <a href="client/cart.htm" class="link-to">
                                            <span class="icon-qty-combine">
                                                <img src="assets/client/images/icons8-cart-30.png" style="margin-top: -10px">
                                            </span>
                                            <span class="title">My Cart</span>
                                        </a>
                                    </div>  
                                </div>
                                <div class="mobile-menu-toggle">
                                    <a class="btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                                        <span></span>
                                        <span></span>
                                        <span></span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="header-bottom hidden-sm hidden-xs">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-3 col-md-4">
                            <div class="vertical-menu vertical-category-block">

                            </div>
                        </div>
                        <div class="col-lg-9 col-md-8 padding-top-2px">
                            <div class="header-search-bar layout-01">
                                <form action="client/product.htm" class="form-search" name="desktop-search" method="get">
                                    <input type="text" name="keyword" class="input-text" placeholder="Search product..." value="${keyword}">

                                    <button type="submit" class="btn-submit">
                                        <i class="nestmart-icon icon-search"></i>
                                    </button>
                                </form>
                            </div>

                            <div class="live-info">
                                <p class="telephone">
                                    <i class="fa fa-phone" aria-hidden="true"></i>
                                    <b class="phone-number">(+84) 123 456 789</b>
                                </p>
                                <p class="working-time">Mon-Sun: 8:00am-10:00pm</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <div class="hero-section hero-background">
            <h1 class="page-title">Organic Fruits</h1>
        </div>

        <div class="container">
            <nav class="nestmart-nav">

            </nav>
        </div>
        <div id="main-content" class="main-content">
            <div class="container register-on-checkout">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="form-container">
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">
                                    ${error}
                                </div>
                            </c:if>
                            <c:if test="${not empty message}">
                                <div class="alert alert-success">
                                    ${message}
                                </div>
                            </c:if>
                            <h2 style="text-align: center">Sign up</h2>

                            <form action="${pageContext.request.contextPath}/signup.htm" method="post">
                                <div class="form-row double-column">
                                    <div class="form-group">
                                        <label for="fid-fullname">Full Name:</label>
                                        <input type="text" id="fid-fullname" name="fullName" placeholder="Enter your full name" class="txt-input" value="${fullName}" />
                                        <c:if test="${not empty errors.fullName}">
                                            <div class="error-message">${errors.fullName}</div>
                                        </c:if>
                                    </div>
                                    <div class="form-group">
                                        <label for="fid-email">Email:</label>
                                        <input type="email" id="fid-email" name="email" placeholder="Enter your email" class="txt-input" value="${email}" />
                                        <c:if test="${not empty errors.email}">
                                            <div class="error-message">${errors.email}</div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-row double-column">
                                    <div class="form-group">
                                        <label for="fid-password">Password:</label>
                                        <input type="password" id="fid-password" name="password" placeholder="Enter your password" class="txt-input" />
                                        <c:if test="${not empty errors.password}">
                                            <div class="error-message">${errors.password}</div>
                                        </c:if>
                                    </div>
                                    <div class="form-group">
                                        <label for="fid-confirm-password">Confirm Password:</label>
                                        <input type="password" id="fid-confirm-password" name="confirmPassword" placeholder="Confirm your password" class="txt-input" />
                                        <c:if test="${not empty errors.confirmPassword}">
                                            <div class="error-message">${errors.confirmPassword}</div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-row double-column">
                                    <div class="form-group">
                                        <label for="fid-phone">Phone Number:</label>
                                        <input type="text" id="fid-phone" name="phoneNumber" placeholder="Enter your phone number" class="txt-input" value="${phoneNumber}" />
                                        <c:if test="${not empty errors.phoneNumber}">
                                            <div class="error-message">${errors.phoneNumber}</div>
                                        </c:if>
                                    </div>
                                    <div class="form-group">
                                        <label for="fid-address">Address:</label>
                                        <input type="text" id="fid-address" name="address" placeholder="Enter your address" class="txt-input" value="${address}" />
                                        <c:if test="${not empty errors.address}">
                                            <div class="error-message">${errors.address}</div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-row double-column">
                                    <div class="form-group">
                                        <label for="fid-gender">Gender:</label>
                                        <select id="fid-gender" name="gender" class="txt-input">
                                            <option value="Male" ${gender == 'Male' ? 'selected' : ''}>Male</option>
                                            <option value="Female" ${gender == 'Female' ? 'selected' : ''}>Female</option>
                                        </select>
                                        <c:if test="${not empty errors.gender}">
                                            <div class="error-message">${errors.gender}</div>
                                        </c:if>
                                    </div>
                                    <div class="form-group">
                                        <label for="fid-birthday">Birthday:</label>
                                        <input type="date" id="fid-birthday" name="birthday" class="txt-input" value="${birthday}" />
                                        <c:if test="${not empty errors.birthday}">
                                            <div class="error-message">${errors.birthday}</div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-row wrap-btn">
                                    <button class="btn-submit" type="submit">Register</button>
                                    <a href="${pageContext.request.contextPath}/login.htm" class="link-to-help">Have an account? Login</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <footer id="footer" class="footer layout-03">
            <div class="footer-content background-footer-03">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-4 col-md-4 col-sm-9">
                            <section class="footer-item">
                                <a href="#" class="logo footer-logo"><img src="assets/client/images/NestMart_logo.png" alt="nestmart logo" width="220" height="36"/></a>
                                <div class="footer-phone-info">
                                    <i class="nestmart-icon icon-head-phone"></i>
                                    <p class="r-info">
                                        <span>Got Questions ?</span>
                                        <span> (+84) 123 456 789</span>
                                    </p>
                                </div>
                                <div class="newsletter-block layout-01">

                            </section>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                            <section class="footer-item">
                                <div class="row">
                                    <div class="col-lg-6 col-sm-6 col-xs-6">
                                        <h3 class="section-title">Useful Links</h3>
                                        <div class="wrap-custom-menu vertical-menu-2">
                                            <ul class="menu">
                                                <li><a href="client/clientboard.htm">Home</a></li>
                                                <li><a href="client/product.htm">Product</a></li>
                                                <li><a href="client/discount.htm">Discount</a></li>
                                                <li><a href="client/about.htm">About</a></li>
                                                <li><a href="client/viewNotifications.htm">Notifications</a></li>
                                                <li><a href="client/orderHistory.htm">My Purchase</a></li>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="col-lg-6 col-sm-6 col-xs-6">
                                        <h3 class="section-title">Client Services</h3>
                                        <div class="wrap-custom-menu vertical-menu-2">
                                            <ul class="menu">
                                                <li><a href="client/refundpolicy.htm">Refund Policy</a></li>
                                                <li><a href="client/shippingpolicy.htm">Shipping Policy</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </div>

                        <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                            <section class="footer-item">
                                <h3 class="section-title">Transport Offices</h3>
                                <div class="contact-info-block footer-layout xs-padding-top-10px">
                                    <ul class="contact-lines">
                                        <li>
                                            <p class="info-item">
                                                <i class="nestmart-icon icon-location"></i>
                                                <b class="desc">7563 St. Vicent Place, Glasgow, Greater Newyork NH7689, UK </b>
                                            </p>
                                        </li>
                                        <li>
                                            <p class="info-item">
                                                <i class="nestmart-icon icon-phone"></i>
                                                <b class="desc">Phone: (+84) 123 456 789</b>
                                            </p>
                                        </li>
                                        <li>
                                            <p class="info-item">
                                                <i class="nestmart-icon icon-letter"></i>
                                                <b class="desc">Email:  nestmart@company.com</b>
                                            </p>
                                        </li>
                                        <li>
                                            <p class="info-item">
                                                <i class="nestmart-icon icon-clock"></i>
                                                <b class="desc">Mon-Sun: 8:00am-10:00pm</b>
                                            </p>
                                        </li>
                                    </ul>
                                </div>
                                <div class="nestmart-social inline">
                                    <ul class="socials">
                                        <li><a href="#" title="facebook" class="socail-btn"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#" title="youtube" class="socail-btn"><i class="fa fa-youtube" aria-hidden="true"></i></a></li>
                                        <li><a href="#" title="instagram" class="socail-btn"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </section>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="separator sm-margin-top-62px xs-margin-top-40px"></div>
                        </div>

                    </div>
                </div>
            </div>
        </footer>
        <!-- Scroll Top Button -->
        <a class="btn-scroll-top"><i class="nestmart-icon icon-left-arrow"></i></a>

        <script src="assets/js/jquery-3.4.1.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script src="assets/js/jquery.countdown.min.js"></script>
        <script src="assets/js/jquery.nice-select.min.js"></script>
        <script src="assets/js/jquery.nicescroll.min.js"></script>
        <script src="assets/js/slick.min.js"></script>
        <script src="assets/js/nestmart.framework.js"></script>
        <script src="assets/js/functions.js"></script>
        <script>
                                            document.querySelector('.dropdown-toggle').addEventListener('click', function () {
                                                var menu = document.querySelector('.dropdown-menu');
                                                menu.classList.toggle('show');
                                            });

                                            document.addEventListener('click', function (event) {
                                                var isClickInside = document.querySelector('.dropdown').contains(event.target);
                                                if (!isClickInside) {
                                                    document.querySelector('.dropdown-menu').classList.remove('show');
                                                }
                                            });

                                            function togglePopup() {
                                                var popupMenu = document.getElementById("popupMenu");
                                                if (popupMenu.style.display === "block") {
                                                    popupMenu.style.display = "none";
                                                } else {
                                                    popupMenu.style.display = "block";
                                                }
                                            }

                                            window.onclick = function (event) {
                                                if (!event.target.matches('.dropbtn')) {
                                                    var popupMenu = document.getElementById("popupMenu");
                                                    if (popupMenu.style.display === "block") {
                                                        popupMenu.style.display = "none";
                                                    }
                                                }
                                            }
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
    </body>
</html>
