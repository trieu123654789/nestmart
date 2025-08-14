<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>NestMart - Shipping Policy</title>
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
        <link rel="stylesheet" href="../assets/client/css/main-color04.css" />
        <style>
            body {
                font-family: 'Arial', sans-serif;
                line-height: 1.6;
                color: #333;
            }
            .policy-section {
                padding: 60px 0;
                background-color: #f8f9fa;
            }
            .policy-section h1 {
                font-size: 42px;
                color: #ff9404;
                text-align: center;
                margin-bottom: 40px;
                font-family: 'Poppins', sans-serif;
            }
            .policy-section h2 {
                font-size: 28px;
                color: #ff9404;
                margin-top: 30px;
                margin-bottom: 15px;
            }
            .policy-section p,
            .policy-section li {
                font-size: 16px;
                margin-bottom: 10px;
            }
            .policy-section ul {
                padding-left: 20px;
            }
            .highlight-box {
                background: #fff;
                border-left: 5px solid #ff9404;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0px 2px 8px rgba(0,0,0,0.05);
            }
            .fa {
                font-family: FontAwesome !important;
            }
        </style>
    </head>
    <body class="nestmart-body">

        <!-- HEADER -->
        <jsp:include page="/jsp/client/header.jsp" />

        <!-- Page Content -->
        <div class="page-contain">
            <div id="main-content" class="main-content">
                <div class="policy-section">
                    <div class="container">
                        <h1>Shipping & Delivery Policy</h1>

                        <div class="highlight-box">
                            <p>At <strong>NestMart</strong>, we strive to deliver your orders safely, quickly, and at a fair cost. Please read our shipping and delivery policy below.</p>
                        </div>

                        <h2>1. Delivery Scope</h2>
                        <ul>
                            <li>Currently, delivery is available <strong>within Vietnam only</strong>.</li>
                            <li>Delivery time is calculated from order confirmation to the moment the package reaches you, excluding weekends and public holidays.</li>
                            <li><em>Note:</em> Delivery may be delayed in case of natural disasters, epidemics, or other force majeure events.</li>
                        </ul>

                        <h2>2. Shipping Fee</h2>
                        <p>A flat shipping fee of <strong>5 USD</strong> applies to all orders nationwide.</p>
                        <ul>
                            <li>Free shipping promotions may be announced on our website from time to time.</li>
                            <li>The shipping fee (if any) will be included in your bill at checkout.</li>
                        </ul>

                        <h2>3. Order Inspection (Open & Check Policy)</h2>
                        <ul>
                            <li>Customers are encouraged to check their parcels upon delivery.</li>
                            <li>You may verify product type, quantity, color, and packaging condition (intact, no signs of damage).</li>
                            <li>Seal-breaking or product usage is <strong>not allowed</strong> during inspection.</li>
                            <li>If problems are found, refuse the parcel immediately and notify the delivery staff.</li>
                        </ul>

                        <h2>4. Failed Delivery</h2>
                        <ul>
                            <li>Orders that cannot be delivered after <strong>3 attempts</strong> will be returned to our warehouse.</li>
                            <li>Prepaid orders will be refunded within <strong>7–10 business days</strong> after the returned parcel is confirmed.</li>
                        </ul>

                        <h2>5. Undeliverable Orders</h2>
                        <ul>
                            <li>If delivery is not possible due to force majeure (natural disasters, epidemics, restricted areas), the order will be returned automatically.</li>
                            <li>Refunds for prepaid orders will be processed within <strong>7–10 business days</strong> after confirmation.</li>
                        </ul>

                        <div class="highlight-box">
                            <p>This policy ensures transparency and fairness for all customers of <strong>NestMart</strong>, while guaranteeing the safety and integrity of delivered goods.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- FOOTER -->
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
        <jsp:include page="livechat.jsp" />
    </body>
</html>
