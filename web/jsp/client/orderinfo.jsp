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
        <title>NestMart - Order History</title>
        <!-- Bootstrap CSS -->
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
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
                max-width: 900px;
                margin: 30px auto;
                padding: 25px 30px;
                background-color: #fff;
                border-radius: 12px;
                box-shadow: 0 6px 15px rgba(0,0,0,0.1);
                font-family: 'Arial', sans-serif;
            }
            .order-status-line {
                text-align: right;
                font-size: 14px;
                font-weight: bold;
                color: #28a745;
                margin-bottom: 15px;
            }
            .order-status-line-red {
                text-align: right;
                font-size: 14px;
                font-weight: bold;
                color: red;
                margin-bottom: 15px;
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

                    <c:choose>
                        <c:when test="${order.orderStatus eq 'Return Requested'||order.orderStatus eq 'Approved'||order.orderStatus eq 'Denied'||order.orderStatus eq 'Return Completed'}">
                            <li class="nav-item">
                                <span class="current-page" style="font-size: 35px">RETURN ORDER INFORMATION</span>
                            </li>
                        </c:when>

                        <c:otherwise>
                            <li class="nav-item">
                                <span class="current-page" style="font-size: 35px">ORDER INFORMATION</span>
                            </li> 
                        </c:otherwise>
                    </c:choose>
                </ul>  
            </nav>
        </div>

        <div class="page-contain about-us">
            <div id="main-content" class="main-content">
                <div class="container">
                    <c:if test="${not empty order}">
                        <div class="feedback-container">
                            <div class="order-status-line">
                                <c:choose>
                                    <c:when test="${order.orderStatus eq 'Pending'}">Waiting for seller confirmation</c:when>
                                    <c:when test="${order.orderStatus eq 'Confirmed'}">Order confirmed by seller</c:when>
                                    <c:when test="${order.orderStatus eq 'On Delivering'}">Your package is on the way</c:when>
                                    <c:when test="${order.orderStatus eq 'Completed'}">Order has been delivered successfully</c:when>
                                    <c:when test="${order.orderStatus eq 'Return Requested'}">Return request submitted</c:when>
                                    <c:when test="${order.orderStatus eq 'Approved'}">Return request approved</c:when>
                                    <c:when test="${order.orderStatus eq 'Return Completed'}">Your return has been completed</c:when>
                                </c:choose>
                            </div>
                            <div class="order-status-line-red">
                                <c:choose>
                                    <c:when test="${order.orderStatus eq 'Cancelled'}">Order has been cancelled</c:when>
                                    <c:when test="${order.orderStatus eq 'Denied'}">Return request denied</c:when>
                                </c:choose>
                            </div>
                            <p><label>Order ID:</label> ${order.orderID}</p>
                            <p><label>Order Date:</label> ${order.formattedOrderDate}</p>
                            <c:if test="${not empty order.deliveryDate}">
                                <p><label>Delivery Date:</label> ${order.deliveryDate}</p>
                            </c:if>
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
<div style="border-top: 1px solid #ddd; padding-top: 15px; margin-top: 20px;">
    <!-- Subtotal -->
    <p style="font-size: 16px; margin: 8px 0;">
        <label>Subtotal:</label> 
        <c:set var="subtotal" value="0" />
        <c:forEach var="detail" items="${order.orderDetails}">
            <c:set var="subtotal" value="${subtotal + detail.totalPrice}" />
        </c:forEach>
        <fmt:formatNumber value="${subtotal}" type="number" maxFractionDigits="0" groupingUsed="true" />$
    </p>
    
    <p style="font-size: 16px; margin: 8px 0;">
        <label>Shipping Fee:</label> 5$
    </p>
  <c:if test="${not empty usedVoucher}">
    <c:set var="totalBeforeDiscount" value="${subtotal + 5}" />
    
    <c:choose>
        <c:when test="${usedVoucher.DiscountType eq 'Percentage'}">
            <!-- Tính discount theo phần trăm -->
            <c:set var="calculatedDiscount" value="${subtotal * usedVoucher.DiscountValue / 100}" />
            
            <!-- Kiểm tra MaxDiscount, nếu có và nhỏ hơn thì lấy MaxDiscount -->
            <c:choose>
                <c:when test="${not empty usedVoucher.MaxDiscount and usedVoucher.MaxDiscount < calculatedDiscount}">
                    <c:set var="discountAmount" value="${usedVoucher.MaxDiscount}" />
                </c:when>
                <c:otherwise>
                    <c:set var="discountAmount" value="${calculatedDiscount}" />
                </c:otherwise>
            </c:choose>
        </c:when>
        
        <c:when test="${usedVoucher.DiscountType eq 'FixedAmount'}">
            <c:choose>
                <c:when test="${usedVoucher.DiscountValue > subtotal}">
                    <c:set var="discountAmount" value="${subtotal}" />
                </c:when>
                <c:otherwise>
                    <c:set var="discountAmount" value="${usedVoucher.DiscountValue}" />
                </c:otherwise>
            </c:choose>
        </c:when>
        
        <c:otherwise>
            <c:set var="discountAmount" value="0" />
        </c:otherwise>
    </c:choose>

    <p style="font-size: 16px; margin: 8px 0; color: #28a745;">
        <label>Voucher Discount:</label>
    
        
       
        
        - <fmt:formatNumber value="${discountAmount}" type="number" 
                            maxFractionDigits="0" groupingUsed="true" />$
    </p>
</c:if>



</div>

<p style="font-size:18px; font-weight:600; margin-top:15px; border-top: 2px solid #333; padding-top: 10px;">
    <label>Total Amount:</label> 
    <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="0" groupingUsed="true" />$
</p>
                            <c:if test="${order.orderStatus eq 'Return Requested' 
                                          || order.orderStatus eq 'Approved' 
                                          || order.orderStatus eq 'Denied'}">
                                  <div class="return-info" style="margin-top:15px; margin-bottom:15px;padding:10px; border:1px solid #ccc; border-radius:8px;">
                                      <h4>Return Request Details</h4>
                                      <p><label>Request Date:</label> ${order.returnRequestDate}</p>
                                      <p><label>Status:</label> ${order.returnRequestStatus}</p>
                                      <p><label>Reason:</label> ${order.returnRequestReason}</p>
                                  </div>
                            </c:if>

                            <c:if test="${order.orderStatus eq 'Approved' || order.orderStatus eq 'Denied'}">
                                <div class="employee-responses" style="margin-top:10px; margin-bottom:15px;padding:10px; border:1px solid #ccc; border-radius:8px;">
                                    <h4>Employee Responses</h4>
                                    <c:forEach var="resp" items="${responses}">
                                        <div style="margin-bottom:10px; padding:5px; border-bottom:1px dashed #aaa;">
                                            <p><label>Response Date:</label> ${resp.returnRequestResponseDate}</p>
                                            <p><label>Message:</label> ${resp.message}</p>
                                        </div>
                                    </c:forEach>

                                </div>
                            </c:if>


                            <button onclick="window.history.back();">OK</button>
                            <c:if test="${order.orderStatus eq 'Pending'}">
                                <a href="javascript:void(0);" 
                                   onclick="confirmCancelOrder('${order.orderID}')"
                                   class="red-outline-btn">Cancel Order</a>
                            </c:if>
                            <c:set var="today" value="<%= new java.util.Date()%>" />

                            <c:if test="${order.orderStatus eq 'Completed' 
                                          and (today.time - order.deliveryDate.time) / (1000*60*60*24) <= 4}">
                                  <a href="${pageContext.request.contextPath}/client/returnorder.htm?orderId=${order.orderID}" 
                                     class="red-outline-btn">Return Order</a>
                            </c:if>


                        </div>
                    </c:if>
                    <form id="cancelOrderForm" method="post" action="../client/cancelOrder.htm">
                        <input type="hidden" name="orderID" id="cancelOrderID" />
                    </form>

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
                                       function confirmCancelOrder(orderId) {
                                           Swal.fire({
                                               title: 'Cancel Order?',
                                               text: "Are you sure you want to cancel this order?",
                                               icon: 'warning',
                                               showCancelButton: true,
                                               confirmButtonColor: '#d33',
                                               cancelButtonColor: '#3085d6',
                                               confirmButtonText: 'Yes',
                                               cancelButtonText: 'No'
                                           }).then((result) => {
                                               if (result.isConfirmed) {
                                                   document.getElementById("cancelOrderID").value = orderId;
                                                   document.getElementById("cancelOrderForm").submit();
                                               }
                                           });
                                       }
                                       $(document).ready(function () {
                                           $('.nestmart-carousel').slick();
                                       });
        </script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <jsp:include page="livechat.jsp" />
    </body>

</html>