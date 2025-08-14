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
        <title>NestMart - Return Order</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet" />
        <link rel="shortcut icon" type="image/x-icon" href="assets/client/images/NestMart_icon.png" />
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
            .red-outline-btn {
                margin-left: 10px;
                display: inline-block;
                text-align: center;
                margin-top: 25px;
                border: 2px solid #e63946;
                padding: 12px 30px;
                background: #e63946;
                color: #fff;
                border-radius: 8px;
                cursor: pointer;
                font-size: 16px;
                font-weight: 500;
                transition: all 0.3s ease;
                text-decoration: none;
            }

            .red-outline-btn:hover {
                background: #a4161a;
                border-color: #a4161a;
                color: #fff;
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
            <nav class="nestmart-nav">
                <ul>
                    <li class="nav-item">
                        <span class="current-page" style="font-size: 35px">RETURN ORDER</span>
                    </li>
                </ul>
            </nav>
        </div>

        <div class="page-contain about-us">
            <div id="main-content" class="main-content">
                <div class="container">
                    <c:if test="${not empty order}">
                        <div class="feedback-container">

                            <p><label>Order ID:</label> ${order.orderID}</p>
                            <p><label>Order Date:</label> ${order.orderDate}</p>
                            <p><label>Payment Method:</label> ${order.paymentMethod}</p>
                            <c:if test="${not empty order.notes}">
                                <p><label>Notes:</label> ${order.notes}</p>
                            </c:if>
                            <p><label>Customer:</label> ${order.customerName}</p>
                            <p><label>Address:</label> ${order.shippingAddress}</p>

                            <h3 style="margin-top:20px;">Products in this order:</h3>
                            <c:forEach var="detail" items="${order.orderDetails}">
                                <div class="product-container">
                                    <c:if test="${not empty detail.images}">
                                        <img src="${pageContext.request.contextPath}/assets/admin/images/uploads/products/${detail.images[0].images}" 
                                             alt="Product Image" class="product-image"/>
                                    </c:if>
                                    <div>
                                        <p><label>Product:</label> ${detail.product.productName}</p>
                                        <p><label>Quantity:</label> ${detail.quantity}</p>
                                        <p><label>Unit Price:</label> 
                                            <fmt:formatNumber value="${detail.unitPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                        </p>
                                        <p><label>Total:</label>
                                            <fmt:formatNumber value="${detail.totalPrice}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>

                            <p style="font-size:18px; font-weight:600; margin-top:15px;">
                                <label>Total Amount:</label> 
                                <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />$
                            </p>

                            <fmt:parseDate value="${order.deliveryDate}" pattern="yyyy-MM-dd" var="deliveryDate" />
                            <jsp:useBean id="now" class="java.util.Date" />

                            <c:if test="${order.orderStatus eq 'Completed' 
                                          and (now.time - deliveryDate.time) le (4*24*60*60*1000)}">
                                  <form action="${pageContext.request.contextPath}/client/returnorder.htm" method="post">
                                      <input type="hidden" name="orderId" value="${order.orderID}" />
                                      <label for="reason">Reason for return:</label><br>
                                      <textarea name="reason" id="reason" rows="4" cols="50" required></textarea><br><br>
                                      <button type="submit" class="red-outline-btn">Submit Return Request</button>
                                      <button type="button" onclick="window.history.back();">Cancel</button>
                                  </form>
                            </c:if>

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