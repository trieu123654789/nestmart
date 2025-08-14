<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <meta name="author" content="AdminKit">
        <meta name="keywords" content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link rel="shortcut icon" href="../assets/admin/static/img/icons/icon-48x48.png" />
        <link href="https://unpkg.com/feather-icons@latest/dist/feather.css" rel="stylesheet">

        <link rel="canonical" href="https://demo-basic.adminkit.io/pages-blank.html" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.28.0/feather.min.css">
        <title>NestMart - Feedback</title>

        <link rel="stylesheet" href="../assets/admin/css/app.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
        <style>
            .search-container {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
                width: 100%;
            }

            .search-form {
                display: flex;
                align-items: center;
                width: 100%;
            }

            .search-input {
                flex: 1;
                height: 45px;
                padding: 0 10px;
                border-radius: 20px 0 0 20px;
                border: 1px solid #ced4da;
                border-right: none;
                font-size: 16px;
            }

            .search-button {
                height: 45px;
                width: 65px;
                border-radius: 0 20px 20px 0;
                border: 1px solid #ced4da;
                border-left: none;
                background-color: #f1f1f1;
                color: #333;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                margin-right: 15px;
            }

            .search-button i {
                font-size: 20px;
            }

            .search-button:hover {
                background-color: #e0e0e0;
            }

            .icon-container {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-left: auto;
            }
            .icon-container .btn {
                background-color: #f1f1f1;
                border: none;
                color: #333;
                width: 50px;
                height: 50px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                font-size: 24px;
                transition: background-color 0.3s, box-shadow 0.3s;
            }
            .icon-container .btn:hover {
                background-color: #e0e0e0;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            .btn-container {
                display: flex;
                gap: 10px;
            }


            .btn-update {
                color: #007bff;
            }

            .btn-delete {
                color: #dc3545;
            }


            .btn-update:hover {
                color: #0056b3;
            }

            .btn-delete:hover {
                color: #c82333;
            }

            tr {
                position: relative;
            }
            .table td {
                vertical-align: middle;
            }
            .image-container {
                position: relative;
                display: inline-block;
            }

            .img-thumbnail {
                display: block;
            }

            .image-count {
                position: absolute;
                top: 5px;
                right: 5px;
                background-color: rgba(0, 0, 0, 0.6);
                color: white;
                padding: 2px 6px;
                border-radius: 50%;
                font-size: 14px;
                font-weight: bold;
            }

            /*chuyentrang*/
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                margin-top: 20px;
            }

            .pagination .page-item .page-link {
                color: #282c3c;
                background-color: #fff;
                border: 1px solid #282c3c;
                padding: 8px 16px;
                margin: 0 4px;
                border-radius: 20px;
                transition: all 0.3s ease;
            }

            .pagination .page-item.active .page-link {
                color: #fff;
                background-color: #282c3c;
                border-color: #282c3c;
            }

            .pagination .page-item .page-link:hover {
                color: #fff;
                background-color: #282c3c;
                border-color: #282c3c;
            }

            .pagination .page-item.disabled .page-link {
                color: #6c757d;
                background-color: #fff;
                border-color: #dee2e6;
                cursor: not-allowed;
            }



            .btn:hover::after, .cannot-delete-indicator:hover::after {
                opacity: 1;
                visibility: visible;
            }
            .add-images-btn {
                background: #ff9404;
                border: 2px solid #ff9404;
                color: #ffffff;
                border-radius: 8px;
                padding: 12px 20px;
                color: white;
                font-size: 16px;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                gap: 8px;
                margin: 20px 0;
            }

            .add-images-btn:hover {
                background: #444444;
                border: 2px solid #444444;
            }

            .add-images-btn i {
                font-size: 16px;
            }

            .image-selection-container {
                background: #f8f9fa;
                border-radius: 8px;
                padding: 20px;
                margin: 20px 0;
                border: 1px solid #dee2e6;
                display: none;
            }

            .image-count-selector {
                display: flex;
                gap: 10px;
                margin: 15px 0;
            }

            .count-option {
                width: 45px;
                height: 45px;
                border: 2px solid #dee2e6;
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                background: white;
            }

            .count-option:hover {
                border-color: #ff9900;
            }

            .count-option.active {
                background: #ff9900;
                color: white;
                border-color: #ff9900;
            }

            .image-input-group {
                background: white;
                border-radius: 8px;
                padding: 15px;
                margin: 10px 0;
                border: 1px solid #dee2e6;
            }

            .image-input-group label {
                font-weight: 600;
                color: #495057;
                margin-bottom: 8px;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .product-container {
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .product-image {
                width: 100px;
                height: 100px;
                object-fit: cover;
                border-radius: 8px;
                border: 1px solid #ddd;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            }

            .product-name span {
                font-size: 18px;
                font-weight: 600;
                color: #333;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .rating-section {
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .rating {
                display: flex;
                flex-direction: row-reverse;
                justify-content: flex-start;
                gap: 5px;
            }

            .rating input {
                display: none;
            }

            .rating label {
                font-size: 28px;
                color: #ccc;
                cursor: pointer;
                transition: color 0.2s, transform 0.2s;
            }

            .rating input:checked ~ label {
                color: #f8c74e;
            }


            .feedback-section textarea {
                border-radius: 10px;
                border: 1px solid #ddd;
                padding: 12px;
                font-size: 15px;
                width: 100%;
                resize: vertical;
                transition: all 0.2s ease-in-out;
            }
            .feedback-section textarea:focus {
                outline: none;
                border-color: #4a90e2;
                box-shadow: 0 0 5px rgba(74, 144, 226, 0.5);
            }
            .feedback-section label {
                font-weight: 600;
                margin-bottom: 8px;
                display: block;
                color: #333;
            }
            .col-md-12.text-center {
                margin-top: 20px;
            }

            .col-md-12.text-center button {
                padding: 10px 25px;
                font-size: 16px;
                font-weight: 500;
                border-radius: 8px;
                border: none;
                margin: 0 8px;
                transition: all 0.3s ease;
            }

            .col-md-12.text-center .btn-primary {
                background-color: #007bff;
                color: white;
            }

            .col-md-12.text-center .btn-primary:hover {
                background-color: #0056b3;
                transform: translateY(-2px);
                box-shadow: 0px 4px 10px rgba(0,0,0,0.2);
            }

            .col-md-12.text-center .btn-close-quickview {
                background-color: #f0f0f0;
                color: #333;
                border: 1px solid black;
            }

            .col-md-12.text-center .btn-close-quickview:hover {
                background-color: #d6d6d6;
                transform: translateY(-2px);
                box-shadow: 0px 4px 8px rgba(0,0,0,0.15);
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
        <div class="page-contain">
            <div id="main-content" class="main-content">
                <div class="hero-section hero-background">
                    <h1 class="page-title">Rate Product</h1>
                </div>

                <div class="container">
                    <nav class="nestmart-nav">

                    </nav>
                </div>

                <div class="page-contain category-page no-sidebar">
                    <div class="container">
                        <div class="row">

                            <div id="main-content" class="main-content col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <div class="container">
                                    <div class="row mb-4">
                                        <div class="col-md-4">
                                            <div class="product-container">
                                                <img src="${pageContext.request.contextPath}/assets/client/images/uploads/products/${product.images[0].images}" alt="alt" class="product-image"/>
                                                <div class="product-name">
                                                    <span>${product.productName}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-8">
                                        </div>
                                    </div>

                                    <form action="${pageContext.request.contextPath}/client/feedbacks.htm" method="post" enctype="multipart/form-data">
                                        <input type="hidden" name="productID" value="${product.productID}" />

                                        <!-- Rating -->
                                        <div class="row mb-4">
                                            <div class="col-md-12">
                                                <div class="rating-section">
                                                    <label class="rating-label">Rate this product:</label>
                                                    <div class="rating">
                                                        <input type="radio" id="star5" name="rating" value="5">
                                                        <label for="star5"><i class="fa fa-star"></i></label>
                                                        <input type="radio" id="star4" name="rating" value="4">
                                                        <label for="star4"><i class="fa fa-star"></i></label> 
                                                        <input type="radio" id="star3" name="rating" value="3">
                                                        <label for="star3"><i class="fa fa-star"></i></label>
                                                        <input type="radio" id="star2" name="rating" value="2">
                                                        <label for="star2"><i class="fa fa-star"></i></label>

                                                        <input type="radio" id="star1" name="rating" value="1">
                                                        <label for="star1"><i class="fa fa-star"></i></label>
                                                    </div>
                                                </div>

                                                <div class="feedback-section">
                                                    <label for="feedbackContent">Your Feedback:</label>
                                                    <textarea name="feedbackContent" class="form-control" rows="4" placeholder="Write your feedback here..."></textarea>
                                                </div>

                                            </div>
                                        </div>

                                        <div class="row mb-4">
                                            <div class="col-md-12">
                                                <button type="button" class="add-images-btn" onclick="toggleImageSelection()">
                                                    <i class="fa fa-camera"></i>
                                                    Add Images
                                                </button>

                                                <div class="image-selection-container" id="imageSelectionContainer">
                                                    <h6><i class="fa fa-images"></i> Choose number of images:</h6>

                                                    <div class="image-count-selector">
                                                        <div class="count-option" onclick="selectImageCount(1)">1</div>
                                                        <div class="count-option" onclick="selectImageCount(2)">2</div>
                                                        <div class="count-option" onclick="selectImageCount(3)">3</div>
                                                        <div class="count-option" onclick="selectImageCount(4)">4</div>
                                                        <div class="count-option" onclick="selectImageCount(5)">5</div>
                                                    </div>

                                                    <div id="imageInputs"></div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row mb-4">
                                            <div class="col-md-12 text-center">
                                                <button type="submit" class="btn btn-primary">Submit Feedback</button>
                                                <button type="button" onclick="window.history.back();" class="btn btn-close-quickview">Cancel</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <jsp:include page="/jsp/client/footer.jsp" />

        </div>
        <!-- Scroll Top Button -->
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
                                                    function generateImageInputs(count) {
                                                        var container = document.getElementById("imageInputs");
                                                        container.innerHTML = "";

                                                        for (var i = 1; i <= count; i++) {
                                                            var inputDiv = document.createElement("div");
                                                            inputDiv.className = "form-group";

                                                            var label = document.createElement("label");
                                                            label.setAttribute("for", "imageFile" + i);
                                                            label.textContent = "Image " + i + ":";

                                                            var input = document.createElement("input");
                                                            input.type = "file";
                                                            input.className = "form-control";
                                                            input.id = "imageFile" + i;
                                                            input.name = "imageFiles";
                                                            input.accept = "image/*";

                                                            inputDiv.appendChild(label);
                                                            inputDiv.appendChild(input);
                                                            container.appendChild(inputDiv);
                                                        }
                                                    }

                                                    window.onload = function () {
                                                        generateImageInputs(1);
                                                    };
                                                    function toggleImageSelection() {
                                                        const container = document.getElementById('imageSelectionContainer');
                                                        const btn = document.querySelector('.add-images-btn');

                                                        if (container.style.display === 'none' || container.style.display === '') {
                                                            container.style.display = 'block';
                                                            btn.innerHTML = '<i class="fa fa-minus"></i> Hide';
                                                        } else {
                                                            container.style.display = 'none';
                                                            btn.innerHTML = '<i class="fa fa-camera"></i> Add images';
                                                        }
                                                    }

                                                    function selectImageCount(count) {
                                                        // Update active state
                                                        document.querySelectorAll('.count-option').forEach(option => {
                                                            option.classList.remove('active');
                                                        });
                                                        event.target.classList.add('active');

                                                        generateImageInputs(count);
                                                    }

                                                    function generateImageInputs(count) {
                                                        var container = document.getElementById("imageInputs");
                                                        container.innerHTML = "";

                                                        for (var i = 1; i <= count; i++) {
                                                            var inputDiv = document.createElement("div");
                                                            inputDiv.className = "image-input-group";

                                                            var label = document.createElement("label");
                                                            label.setAttribute("for", "imageFile" + i);
                                                            label.innerHTML = '<i class="fa fa-image"></i> Image ' + i + ':';

                                                            var input = document.createElement("input");
                                                            input.type = "file";
                                                            input.className = "form-control";
                                                            input.id = "imageFile" + i;
                                                            input.name = "imageFiles";
                                                            input.accept = "image/*";

                                                            inputDiv.appendChild(label);
                                                            inputDiv.appendChild(input);
                                                            container.appendChild(inputDiv);
                                                        }
                                                    }
        </script>
        <jsp:include page="livechat.jsp" />

    </body>

</html>
