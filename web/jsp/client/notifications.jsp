<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>NestMart - Notification</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet" />
        <link rel="shortcut icon" type="image/x-icon" href="../assets/client/images/NestMart_icon.png" />
        <link rel="stylesheet" href="../assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="../assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="../assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="../assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="../assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="../assets/client/css/style.css" />
        <link rel="stylesheet" href="../assets/client/css/main-color.css" />
        <style>
            .feedback-container {
                margin: 20px;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 5px;
            }
            .feedback-item {
                margin-bottom: 15px;
                padding: 10px;
                border-bottom: 1px solid #eee;
            }
            .feedback-item:last-child {
                border-bottom: none;
            }
            .product-image {
                max-width: 150px;
                max-height: 150px;
            }
            .feedback-content {
                margin: 10px 0;
            }
            .feedback-date {
                color: #888;
            }
            .rating {
                color: #f5c518;
            }
            .close-btn {
                cursor: pointer;
                padding: 5px 10px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 5px;
                margin-top: 10px;
            }
            .notification-container {
                border: 1px solid #ddd;
                padding: 10px;
                margin-bottom: 30px;
                /*background-color: #f9f9f9;*/
            }
            .row {
                margin-left: -500px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                padding: 8px 12px;
                border: 1px solid #ddd;
            }
            th {
                background-color: #f4f4f4;
            }
            .unread {
                display: flex;
                align-items: center;
                font-weight: bold;
                color: #000;
                background-color: #f9f9f9;
                border-left: 4px solid #007BFF;
                padding: 10px;
                margin-bottom: 5px;
                border-radius: 3px;
                transition: background-color 0.3s, border-left-color 0.3s;
                cursor: pointer;
            }
            .unread .icon {
                margin-right: 8px;
            }
            .read {
                font-weight: normal;
                color: #666;
                background-color: #fff;
                border-left: 4px solid #ccc;
            }
            .fa {
                font-family: FontAwesome !important;
            }
        </style>
    </head>
    <body class="nestmart-body">

        <div id="biof-loading">
            <div class="biof-loading-center">
                <div class="biof-loading-center-absolute">
                    <div class="dot dot-one"></div>
                    <div class="dot dot-two"></div>
                    <div class="dot dot-three"></div>
                </div>
            </div>
        </div>

        <jsp:include page="/jsp/client/header.jsp" />

        <div class="hero-section hero-background style-02">
            <h1 class="page-title">Organic Fruits</h1>
            <nav class="biolife-nav">
                <ul>
                    <li class="nav-item"><a href="index-2.html" class="permal-link">Home</a></li>
                    <li class="nav-item"><span class="current-page">Our Blog</span></li>
                </ul>
            </nav>
        </div>

        <div class="page-contain blog-page">
            <div class="container">
                <div class="row" >
                    <!-- Main content -->
                    <div id="main-content" class="main-content col-lg-12 col-md-8 col-sm-12 col-xs-12">
                        <ul class="posts-list main-post-list">
                            <c:forEach var="notification" items="${notifications}">
                                <li class="post-elem notification-container ${notification.status == 'Unread' ? 'unread' : 'read'}">
                                    <div class="post-item style-wide">
                                        <div class="post-content">
                                            <span class="icon">🔔</span>
                                            <h4 class="post-name"><a href="viewNotificationDetails.htm?notificationID=${notification.notificationID}" class="linktopost">${notification.title}</a></h4>


                                            <b class="post-cat">${notification.notificationType}</b>
                                            <span class="post-date"> / ${notification.sendDate}</span>
                                            <span class="author">Sent By: NestMart</span>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>

                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/client/footer.jsp" />

        <a class="btn-scroll-top"><i class="nestmart-icon icon-left-arrow"></i></a>

        <script src="../assets/client/js/jquery-3.4.1.min.js"></script>
        <script src="../assets/client/js/bootstrap.min.js"></script>
        <script src="../assets/client/js/jquery.countdown.min.js"></script>
        <script src="../assets/client/js/jquery.nice-select.min.js"></script>
        <script src="../assets/client/js/jquery.nicescroll.min.js"></script>
        <script src="../assets/client/js/slick.min.js"></script>
        <script src="../assets/client/js/nestmart.framework.js"></script>
        <script src="../assets/client/js/functions.js"></script>

        <script>
            $(document).ready(function () {
                $('.nestmart-carousel').slick();
            });
        </script>
        <jsp:include page="livechat.jsp" />
    </body>

</html>