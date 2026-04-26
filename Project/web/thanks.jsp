<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Đặt Hàng Thành Công - V-SNKR" />
        <title>Đặt Hàng Thành Công | V-SNKR</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        
        <style>
            body {
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: var(--text-main);
            }

            /* Success Header */
            .success-header {
                text-align: center;
                margin-bottom: 40px;
                animation: fadeInDown 0.8s ease;
            }

            .success-icon {
                font-size: 5rem;
                color: var(--success);
                margin-bottom: 20px;
                filter: drop-shadow(0 0 15px rgba(34, 197, 94, 0.4));
            }

            .success-title {
                font-weight: 800;
                text-transform: uppercase;
                letter-spacing: 1px;
                color: var(--text-main);
            }

            /* Receipt Card Glassmorphism */
            .receipt-card {
                background: var(--card-bg);
                backdrop-filter: none;
                border: 1px solid var(--border);
                border-top: 5px solid var(--success);
                border-radius: 20px;
                box-shadow: 0 15px 40px rgba(0,0,0,0.1);
                padding: 40px;
                margin-bottom: 50px;
            }

            .receipt-title {
                font-weight: 700;
                text-transform: uppercase;
                color: var(--text-main);
                border-bottom: 1px solid var(--border);
                padding-bottom: 15px;
                margin-bottom: 25px;
                letter-spacing: 0.5px;
            }

            /* Table Styles */
            .table {
                color: var(--text-main);
            }

            .table thead.bg-light-custom {
                background-color: var(--bg);
            }

            .table th {
                border: none;
                text-transform: uppercase;
                font-size: 0.85rem;
                letter-spacing: 1px;
                color: var(--text-muted);
                padding: 15px;
            }

            .table td {
                vertical-align: middle;
                border-bottom: 1px solid var(--border);
                padding: 15px;
            }

            .product-img {
                width: 70px;
                height: 70px;
                object-fit: cover;
                background: var(--bg);
                border-radius: 12px;
                padding: 5px;
                border: 1px solid var(--border);
            }

            .product-name {
                font-weight: 600;
                color: var(--text-main);
                margin: 0;
            }

            /* Total Box */
            .total-box {
                background-color: var(--bg);
                border-radius: 12px;
                padding: 25px 30px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-top: 30px;
                border: 1px dashed var(--border);
            }

            .total-label {
                font-weight: 700;
                text-transform: uppercase;
                color: var(--text-muted);
                margin: 0;
                letter-spacing: 1px;
            }

            .total-amount {
                font-weight: 800;
                color: var(--primary);
                font-size: 1.8rem;
                margin: 0;
            }

            /* Button */
            .btn-continue {
                background-color: var(--primary);
                color: white;
                border: none;
                border-radius: 12px;
                font-weight: 700;
                text-transform: uppercase;
                letter-spacing: 1px;
                padding: 15px 40px;
                transition: all 0.3s ease;
                display: inline-flex;
                align-items: center;
                text-decoration: none;
            }

            .btn-continue:hover {
                background-color: var(--primary-dark);
                color: white;
                transform: translateY(-3px);
                box-shadow: 0 10px 20px rgba(234, 88, 12, 0.4);
                text-decoration: none;
            }

            @keyframes fadeInDown {
                from { opacity: 0; transform: translateY(-20px); }
                to { opacity: 1; transform: translateY(0); }
            }
            
            .text-muted-custom {
                color: var(--text-muted) !important;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <%@include file="components/navBarComponent.jsp" %>

        <section class="py-5" style="min-height: 85vh; display: flex; align-items: center;">
            <div class="container">
                
                <div class="success-header">
                    <i class="fa-solid fa-circle-check success-icon"></i>
                    <h1 class="success-title">Đặt Hàng Thành Công!</h1>
                    <p class="text-muted-custom lead mt-2">Cảm ơn bạn đã tin tưởng V-SNKR. Đơn hàng của bạn đang được chuẩn bị để giao đi.</p>
                </div>

                <div class="row justify-content-center">
                    <div class="col-lg-10">
                        <div class="receipt-card">
                            <h4 class="receipt-title"><i class="fa-solid fa-receipt mr-2 text-warning"></i> Chi tiết hóa đơn</h4>
                            
                            <div class="table-responsive">
                                <table class="table table-borderless">
                                    <thead class="bg-light-custom">
                                        <tr>
                                            <th scope="col" style="width: 50%;">Sản phẩm</th>
                                            <th scope="col" class="text-center">Đơn giá</th>
                                            <th scope="col" class="text-center">Số lượng</th>
                                            <th scope="col" class="text-right" style="text-align: right;">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cartss}" var="C">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <img src="${C.value.product.imageUrl}" class="product-img mr-3" alt="${C.value.product.name}" onerror="this.src='https://via.placeholder.com/70?text=No+Image'"/>
                                                        <div>
                                                            <p class="product-name">${C.value.product.name}</p>
                                                            <small class="text-muted-custom">Mã SP: #${C.value.product.id}</small>
                                                            <input type="hidden" name="productId" value="${C.value.product.id}"/>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="text-center align-middle text-muted"> <fmt:formatNumber value="${C.value.product.price}" pattern="#,### đ"/></td>
                                                <td class="text-center align-middle font-weight-bold text-main">x${C.value.quantity}</td>
                                                <td class="text-right align-middle font-weight-bold text-primary" style="text-align: right;">
                                                    <fmt:formatNumber value="${C.value.product.price * C.value.quantity}" pattern="#,### đ"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="total-box-wrapper">
                                <div class="px-3">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted-custom">Tạm tính:</span>
                                        <span class="text-main"><fmt:formatNumber value="${originalTotalPrice}" pattern="#,### đ"/></span>
                                    </div>
                                    <c:if test="${totalDiscount > 0}">
                                        <div class="d-flex justify-content-between mb-2">
                                            <span class="text-muted-custom">Giảm giá:</span>
                                            <span class="text-success">-<fmt:formatNumber value="${totalDiscount}" pattern="#,### đ"/></span>
                                        </div>
                                    </c:if>
                                    <div class="d-flex justify-content-between mb-3">
                                        <span class="text-muted-custom">VAT (10%):</span>
                                        <span class="text-main"><fmt:formatNumber value="${totalVat}" pattern="#,### đ"/></span>
                                    </div>
                                </div>
                                <div class="total-box">
                                    <h5 class="total-label">Tổng thanh toán</h5>
                                    <h3 class="total-amount"> <fmt:formatNumber value="${totalPrice}" pattern="#,### đ"/></h3>
                                </div>
                            </div>

                            <div class="text-center mt-5">
                                <a href="home" class="btn-continue">
                                    <i class="fa-solid fa-bag-shopping mr-2"></i> Tiếp Tục Mua Sắm
                                </a>
                            </div>
                            
                        </div>
                    </div>
                </div>
                
            </div>
        </section>

        <%@include file="components/footerComponent.jsp" %>
        
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>



