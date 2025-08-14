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
        <title>NestMart - Refund Policy</title>
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
            .fa {
                font-family: FontAwesome !important;
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
                        <h1>Refund & Return Policy</h1>

                        <div class="highlight-box">
                            <p>At <strong>NestMart</strong>, we are committed to delivering fresh, high-quality, and authentic products. Every order is carefully packed to ensure it reaches you in the best condition. Below is our refund and return policy.</p>
                        </div>

                        <h2>1. Return Conditions</h2>
                        <ul>
                            <li><strong>Immediate refusal:</strong> If the package is wet, torn, deformed, unsealed, or incorrect, you may refuse the order upon delivery.</li>
                            <li><strong>After receiving:</strong> Returns are accepted if products are damaged, missing, incorrect, or not co-checked upon delivery.</li>
                            <li><strong>Manufacturer defects:</strong> Expired, mislabeled, or faulty products.</li>
                            <li><em>Note:</em> Other cases are not eligible due to the nature of perishable goods.</li>
                        </ul>

                        <h2>2. Refund Method</h2>
                        <p>- Full-order returns: Refund via bank transfer.<br>
                            - Partial returns: Replacement (if available) or refund via bank transfer.</p>

                        <h2>3. Return Process</h2>
                        <ul>
                            <li>Notify within 24 hours of delivery (excluding weekends and holidays).</li>
                            <li>Contact us via website or hotline.</li>
                            <li>Confirm return method within 48 hours if request is valid.</li>
                            <li>Send back items within 7 days of confirmation.</li>
                            <li>Inspection and result notification within 3 working days.</li>
                        </ul>

                        <h2>4. Valid & Invalid Returns</h2>
                        <p><strong>Valid:</strong></p>
                        <ul>
                            <li>Products remain in original condition.</li>
                            <li>Order includes all items and gifts.</li>
                            <li>One return request per order only.</li>
                        </ul>
                        <p><strong>Invalid:</strong></p>
                        <ul>
                            <li>Incorrect/incomplete return form.</li>
                            <li>Items not returned within timeframe.</li>
                            <li>Missing products or gifts.</li>
                            <li>Products fail NestMart inspection.</li>
                        </ul>

                        <h2>5. Refund Timeline</h2>
                        <p>Refunds will be issued within <strong>5–10 working days</strong> (excluding weekends and holidays) if return is valid. Invalid returns are not eligible for refunds but customers may contact our Customer Service for further assistance.</p>

                        <div class="highlight-box">
                            <p>This policy ensures fairness, transparency, and protects our customers while maintaining food quality and safety at <strong>NestMart</strong>.</p>
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
