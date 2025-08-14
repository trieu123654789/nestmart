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
        <title>Nestmart - About</title>
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
    </head> 
    <style>
        @font-face {
            font-family: 'Langdon';
            src: url('../assets/client/fonts/Langdon.otf') format('opentype');
        }
        body {
            font-family: 'Arial', sans-serif;
            line-height: 1.6;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }
        .about-section {
            padding: 80px 0;
            background-color: #f8f9fa;
        }
        h1, h2, h3 {
            font-family: 'Langdon', sans-serif;
            color: #ff9404;
        }
        h1 {
            font-size: 48px;
            margin-bottom: 40px;
            text-align: center;
        }
        .fa {
            font-family: FontAwesome !important;
        }
        .about-content {
            opacity: 0;
            transform: translateY(20px);
            transition: opacity 0.5s, transform 0.5s;
        }
        .about-content.visible {
            opacity: 1;
            transform: translateY(0);
        }
        .feature-list {
            list-style-type: none;
            padding: 0;
        }
        .feature-item {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            opacity: 0;
            transform: translateX(-20px);
            transition: opacity 0.5s, transform 0.5s;
        }
        .feature-item.visible {
            opacity: 1;
            transform: translateX(0);
        }
        .feature-icon {
            background-color: #ff9404;
            color: white;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            margin-right: 20px;
            font-size: 24px;
        }
        .about-us-section {
            font-family: 'Arial', sans-serif;
            text-align: center;
            padding: 50px;
        }

        #typing-text {
            font-size: 24px;
            white-space: nowrap;
            overflow: hidden;
            border-right: 2px solid;
            width: 100%;
            margin: 0 auto;
            animation: blink-caret 0.75s step-end infinite;
        }

        @keyframes blink-caret {
            from, to {
                border-color: transparent;
            }
            50% {
                border-color: black;
            }
        }

    </style>
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

        <div class="page-contain">

            <div id="main-content" class="main-content">
                <div class="about-section">
                    <div class="container">
                        <div class="about-us-section">
                            <h1>About Us</h1>
                            <p id="typing-text"></p>
                        </div>
                        <div class="about-content">
                            <p>Welcome to <strong>Nestmart</strong>, your trusted online supermarket! We are a pioneering platform dedicated to bringing you the convenience of shopping for all your daily essentials from the comfort of your home. Our mission is to redefine the grocery shopping experience, offering a vast selection of high-quality products at competitive prices, with fast and reliable delivery to your door.</p>
                            <p>At <strong>Nestmart</strong>, we believe in simplifying your life by providing a seamless, stress-free shopping experience. Whether you're looking for fresh produce, pantry staples, household items, or specialty products, our diverse range of categories ensures you'll find exactly what you need. We are constantly expanding our product offerings to cater to the evolving needs of our customers.</p>
                        </div>
                        <h2>Why Choose Nestmart?</h2>
                        <ul class="feature-list">
                            <li class="feature-item">
                                <div class="feature-icon">🛒</div>
                                <div>
                                    <h3>Wide Variety</h3>
                                    <p>From fresh fruits and vegetables to premium meats, snacks, and beverages, we have it all in one place.</p>
                                </div>
                            </li>
                            <li class="feature-item">
                                <div class="feature-icon">💰</div>
                                <div>
                                    <h3>Affordable Prices</h3>
                                    <p>We work directly with trusted suppliers to offer you the best prices without compromising on quality.</p>
                                </div>
                            </li>
                            <li class="feature-item">
                                <div class="feature-icon">🚚</div>
                                <div>
                                    <h3>Fast & Reliable Delivery</h3>
                                    <p>Our efficient delivery network ensures that your order arrives on time, every time.</p>
                                </div>
                            </li>
                            <li class="feature-item">
                                <div class="feature-icon">👥</div>
                                <div>
                                    <h3>Customer-Centric Service</h3>
                                    <p>Our dedicated support team is always ready to assist you, ensuring a smooth and pleasant shopping experience.</p>
                                </div>
                            </li>
                        </ul>
                        <div class="about-content">
                            <p>At <strong>Nestmart</strong>, we are committed to making your life easier with just a few clicks. Start your journey with us today and discover the future of online shopping!</p>
                        </div>
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
            document.addEventListener("DOMContentLoaded", function () {
                const text = "Discover the future of online shopping with Nestmart!";
                const typingText = document.getElementById('typing-text');
                let index = 0;

                function typeEffect() {
                    if (index < text.length) {
                        typingText.innerHTML += text.charAt(index);
                        index++;
                        setTimeout(typeEffect, 100);
                    }
                }

                typeEffect();
            });
            function isElementInViewport(el) {
                const rect = el.getBoundingClientRect();
                return (
                        rect.top >= 0 &&
                        rect.left >= 0 &&
                        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
                        rect.right <= (window.innerWidth || document.documentElement.clientWidth)
                        );
            }

            function handleScroll() {
                const elements = document.querySelectorAll('.about-content, .feature-item');
                elements.forEach(el => {
                    if (isElementInViewport(el)) {
                        el.classList.add('visible');
                    }
                });
            }

            window.addEventListener('load', handleScroll);
            window.addEventListener('scroll', handleScroll);
            window.addEventListener('resize', handleScroll);</script>
            <jsp:include page="livechat.jsp" />
    </body>

</html>
