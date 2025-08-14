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
        <title>NestMart - Feedback</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet" />
        <link rel="shortcut icon" href="../assets/admin/static/img/icons/icon-48x48.png" />
        <link rel="stylesheet" href="../assets/client/css/bootstrap.min.css" />
        <link rel="stylesheet" href="../assets/client/css/animate.min.css" />
        <link rel="stylesheet" href="../assets/client/css/font-awesome.min.css" />
        <link rel="stylesheet" href="../assets/client/css/nice-select.css" />
        <link rel="stylesheet" href="../assets/client/css/slick.min.css" />
        <link rel="stylesheet" href="../assets/client/css/style.css" />
        <link rel="stylesheet" href="../assets/client/css/main-color.css" />
        <style>
            .feedback-container {
                max-width: 900px;
                margin: 30px auto;
                padding: 25px 30px;
                background-color: #fff;
                border-radius: 12px;
                box-shadow: 0 6px 15px rgba(0,0,0,0.1);
                font-family: 'Arial', sans-serif;
            }

            .feedback-container label {
                font-weight: 600;
                color: #333;
                display: inline-block;
                width: 150px;
            }

            .feedback-container p {
                margin: 12px 0;
                color: #555;
                font-size: 16px;
                line-height: 1.5;
            }

            .product-container {
                display: flex;
                align-items: center;
                gap: 20px;
                margin-bottom: 15px;
            }

            .product-image {
                width: 120px;
                height: 120px;
                object-fit: cover;
                border-radius: 10px;
                border: 1px solid #ddd;
                box-shadow: 0 3px 8px rgba(0,0,0,0.1);
            }

            .product-name span {
                font-size: 18px;
                font-weight: 600;
                color: #333;
            }

            .rating {
                color: #f5a623;
                font-size: 18px;
            }

            .feedback-images {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                margin-top: 10px;
            }

            .feedback-image {
                width: 120px;
                height: 120px;
                object-fit: cover;
                border-radius: 10px;
                border: 1px solid #ddd;
            }

            .employee-response {
                margin-top: 20px;
                padding: 15px 20px;
                background-color: #fdf6e3;
                border-left: 4px solid #f5a623;
                border-radius: 8px;
            }

            .employee-response p {
                margin: 5px 0 0 0;
            }

            button {
                display: inline-block;
                justify-content: center;
                text-align: center;
                margin-top: 25px;
                border: 2px solid #ff9404;
                padding: 12px 30px;
                background: #ff9404;
                color: #fff !important;
                border-radius: 8px;
                cursor: pointer;
                font-size: 16px;
                transition: all 0.3s ease;
                text-decoration: none;
            }

            button:hover {
                background: #444444;
                color: #fff !important;
                border: 2px solid #444444;
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
        <div class="hero-section hero-background">
            <h1 class="page-title">Organic Fruits</h1>
        </div>

        <div class="container" style="text-align: center">
            <nav class="nestmart-nav"  >
                <ul>
                    <li class="nav-item"><span class="current-page" style="font-size: 35px">FEEDBACK INFORMATION</span></li>
                </ul>
            </nav>
        </div>

        <div class="page-contain about-us">

            <div id="main-content" class="main-content">

                <div class="container">
                    <c:if test="${not empty feedback}">
                        <div class="feedback-container">
                            <div class="product-container">
                                <c:if test="${not empty feedback.product.images}">
                                    <img src="${pageContext.request.contextPath}/assets/admin/images/uploads/products/${feedback.product.images[0].images}" 
                                         alt="Product Image" class="product-image" width="200"/>
                                </c:if>
                                <div class="product-name">
                                    <span>${feedback.product.productName}</span>
                                </div>
                            </div>

                            <p>
                                <label>By:</label> ${feedback.customerName}
                            </p>

                            <p>
                                <label>Rating:</label> 
                                <c:forEach var="i" begin="1" end="${feedback.rating}">&#9733;</c:forEach>
                                <c:forEach var="i" begin="${feedback.rating + 1}" end="5">&#9734;</c:forEach>
                                </p>
                            <c:if test="${not empty feedback.feedbackContent}">
                                <p>
                                    <label>Feedback Content:</label> ${feedback.feedbackContent}
                                </p>
                            </c:if>


                            <p>
                                <label>Date Feedback:</label> ${feedback.formattedFeedbackDate}
                            </p>

                            <c:if test="${not empty feedback.images}">
                                <div class="feedback-images">
                                    <label>Feedback Images:</label>
                                    <c:forEach var="img" items="${feedback.images}">
                                        <img src="${pageContext.request.contextPath}/assets/client/images/uploads/feedbacks/${img.image}" 
                                             alt="Feedback Image" class="feedback-image" width="200"/>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <c:if test="${not empty employeeResponse}">
                                <div class="employee-response">
                                    <label>NestMart's Response:</label>
                                    <p>${employeeResponse.responseContent}</p>
                                </div>
                            </c:if>

                            <button onclick="window.history.back();">OK</button>
                        </div>
                    </c:if>



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