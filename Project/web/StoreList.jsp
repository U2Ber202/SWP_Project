<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Hệ thống Cửa hàng | V-SNKR</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            :root {
                --primary: #ea580c;
                --bg: #0f172a;
                --card-bg: rgba(255, 255, 255, 0.05);
                --border: rgba(255, 255, 255, 0.1);
            }
            body {
                background: var(--bg);
                color: #f1f5f9;
                font-family: 'Be Vietnam Pro', sans-serif;
            }
            .store-hero {
                background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
                padding: 100px 0 60px;
                text-align: center;
                border-bottom: 1px solid var(--border);
                margin-bottom: 50px;
            }
            .store-card {
                background: var(--card-bg);
                backdrop-filter: blur(12px);
                border: 1px solid var(--border);
                border-radius: 24px;
                transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
                position: relative;
                overflow: hidden;
                margin-bottom: 30px;
                height: 100%;
            }
            .store-card:hover {
                transform: translateY(-10px);
                border-color: var(--primary);
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
            }
            .store-avatar {
                width: 80px;
                height: 80px;
                background: linear-gradient(135deg, var(--primary), #fb923c);
                border-radius: 20px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 2rem;
                color: white;
                margin-bottom: 20px;
                box-shadow: 0 10px 20px rgba(234, 88, 12, 0.2);
            }
            .store-info { padding: 30px; }
            .btn-visit {
                background: rgba(234, 88, 12, 0.1);
                color: var(--primary);
                border: 1px solid rgba(234, 88, 12, 0.2);
                border-radius: 12px;
                padding: 10px 20px;
                font-weight: 600;
                width: 100%;
                transition: all 0.3s ease;
            }
            .store-card:hover .btn-visit {
                background: var(--primary);
                color: white;
            }
            .store-stat {
                display: flex;
                gap: 15px;
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid var(--border);
            }
            .stat-item { font-size: 0.85rem; color: #94a3b8; }
            .stat-item i { color: var(--primary); margin-right: 5px; }
        </style>
    </head>
    <body>
        <%@ include file="components/navBarComponent.jsp" %>

        <div class="store-hero">
            <div class="container">
                <span class="badge badge-pill badge-warning mb-3 px-3 py-2" style="background: rgba(234, 88, 12, 0.2); color: #fb923c;">ĐỐI TÁC TIN CẬY</span>
                <h1 class="display-4 font-weight-bold">Khám phá các Cửa hàng</h1>
                <p class="text-muted lead">Nơi quy tụ những thương hiệu Sneaker hàng đầu Việt Nam</p>
            </div>
        </div>

        <div class="container pb-5">
            <div class="row">
                <c:forEach items="${listStores}" var="s">
                    <div class="col-md-6 col-lg-4">
                        <div class="store-card">
                            <div class="store-info">
                                <div class="store-avatar">
                                    <i class="fas fa-shop"></i>
                                </div>
                                <h3 class="h4 font-weight-bold mb-2">${s.name}</h3>
                                <p class="text-muted small mb-4">Cung cấp bộ sưu tập giày chính hãng từ các thương hiệu lớn Nike, Adidas, Jordan...</p>
                                
                                <div class="store-stat">
                                    <div class="stat-item"><i class="fas fa-box"></i> ${s.productCount} SP</div>
                                    <div class="stat-item">
                                        <i class="fas fa-star"></i> 
                                        <c:choose>
                                            <c:when test="${s.averageRating > 0}">
                                                <fmt:formatNumber value="${s.averageRating}" pattern="#.0"/> Đánh giá
                                            </c:when>
                                            <c:otherwise>
                                                Chưa có đánh giá
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                
                                <a href="home?storeId=${s.id}" class="btn btn-visit mt-4">
                                    Ghé thăm cửa hàng <i class="fas fa-arrow-right ml-2"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <%@ include file="components/footerComponent.jsp" %>
    </body>
</html>
