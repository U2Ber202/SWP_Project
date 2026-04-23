<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Chi Tiết Đơn Hàng #${orderId} | V-SNKR Admin</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            /* Đồng bộ biến màu sắc với toàn hệ thống */
            :root {
                --primary: #ea580c;
                --primary-dark: #c2410c;
                --bg: #0f172a;
                --card-bg: #1e293b;
                --glass: #0f172a;
                --border: rgba(255, 255, 255, 0.1);
            }

            body {
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: #f1f5f9;
                padding-bottom: 40px;
            }

            /* Main Card Wrapper - Glassmorphism */
            .admin-wrapper {
                background: var(--card-bg);
                backdrop-filter: none;
                border: 1px solid var(--border);
                border-radius: 20px;
                box-shadow: 0 10px 40px #0f172a;
                overflow: hidden;
            }

            /* Header */
            .admin-header {
                background: #0f172a;
                color: #fff;
                padding: 25px 30px;
                border-bottom: 1px solid var(--border);
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .admin-header h2 {
                margin: 0;
                font-size: 1.4rem;
                font-weight: 700;
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            .admin-header h2 i {
                color: var(--primary);
                margin-right: 10px;
            }

            .highlight-id {
                color: var(--primary);
                font-weight: 800;
            }

            /* Table Styles */
            .table-container {
                padding: 30px;
            }

            .custom-table {
                margin-bottom: 0;
                color: #f1f5f9;
            }

            .custom-table thead th {
                border-bottom: 1px solid var(--border);
                border-top: none;
                color: #94a3b8;
                font-weight: 600;
                padding: 15px 20px;
                background-color: #0f172a;
                text-transform: uppercase;
                font-size: 0.85rem;
                letter-spacing: 0.5px;
            }

            .custom-table tbody td {
                padding: 15px 20px;
                vertical-align: middle;
                border-color: var(--border);
                border-bottom: 1px solid var(--border);
                font-weight: 500;
            }

            .custom-table tbody tr {
                transition: all 0.2s ease;
            }

            .custom-table tbody tr:hover {
                background-color: #1e293b;
            }

            /* Product Image Thumbnail */
            .product-img {
                width: 70px;
                height: 70px;
                object-fit: contain;
                background-color: rgba(0,0,0,0.3);
                border-radius: 12px;
                border: 1px solid var(--border);
                padding: 5px;
            }

            .product-name {
                font-weight: 600;
                color: #e2e8f0;
                margin: 0;
            }

            /* Buttons */
            .btn-custom-secondary {
                background-color: transparent;
                color: #94a3b8;
                border: 1px solid var(--border);
                border-radius: 12px;
                font-weight: 600;
                padding: 10px 25px;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 1px;
                font-size: 0.9rem;
                display: inline-flex;
                align-items: center;
                text-decoration: none;
            }

            .btn-custom-secondary:hover {
                background-color: var(--glass);
                color: white;
                border-color: var(--border);
            }
            
            .footer-actions {
                background-color: rgba(0, 0, 0, 0.15);
                padding: 20px 30px;
                border-top: 1px solid var(--border);
                text-align: right;
            }
            
            /* Custom text colors for dark mode */
            .text-price {
                color: #94a3b8;
            }
            
            .text-quantity {
                color: #e2e8f0;
            }
            
            .text-total-money {
                color: #fbbf24;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>

        <div class="container">
            <div class="admin-wrapper">
                
                <div class="admin-header">
                    <h2><i class="fa-solid fa-file-invoice"></i> Chi Tiết Đơn Hàng <span class="highlight-id">#${orderId}</span></h2>
                </div>

                <div class="table-container table-responsive">
                    <table class="table custom-table table-hover align-middle">
                        <thead>
                            <tr>
                                <th class="text-center" style="width: 5%;">#</th>
                                <th style="width: 15%;">Hình Ảnh</th>
                                <th style="width: 35%;">Tên Sản Phẩm</th>
                                <th class="text-center" style="width: 15%;">Đơn Giá</th>
                                <th class="text-center" style="width: 10%;">Số Lượng</th>
                                <th class="text-right" style="width: 20%; text-align: right;">Thành Tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderDetails}" var="detail" varStatus="loop">
                                <tr>
                                    <td class="text-center" style="color: #64748b;">${detail.id}</td>
                                    
                                    <td>
                                        <img src="${detail.productImage}" class="product-img" alt="${detail.productName}" onerror="this.src='https://via.placeholder.com/70?text=No+Image'" />
                                    </td>
                                    
                                    <td>
                                        <p class="product-name">${detail.productName}</p>
                                    </td>
                                    
                                    <td class="text-center fw-bold text-price"><fmt:formatNumber value="${detail.productPrice}" pattern="#,### đ"/></td>
                                    
                                    <td class="text-center fw-bold text-quantity">x${detail.quantity}</td>
                                    
                                    <td class="fw-bold text-total-money" style="text-align: right;">
                                        <fmt:formatNumber value="${detail.productPrice * detail.quantity}" pattern="#,### đ"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="footer-actions">
                    <c:choose>
                        <c:when test="${sessionScope.acc.role == 'customer'}">
                            <a href="purchaseHistory" class="btn btn-custom-secondary">
                                <i class="fa-solid fa-arrow-left me-2"></i>Quay lại đơn mua
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="orders" class="btn btn-custom-secondary">
                                <i class="fa-solid fa-arrow-left me-2"></i>Quay Lại Danh Sách
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



