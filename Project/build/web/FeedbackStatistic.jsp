<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thống kê đánh giá | V-SNKR</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <script src="js/theme.js"></script>
    <link rel="stylesheet" href="css/theme.css">
    <style>
        .stat-card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 30px;
            text-align: center;
            transition: all 0.3s ease;
        }
        .progress {
            height: 12px;
            border-radius: 6px;
            background: rgba(255,255,255,0.05);
        }
        .rating-row {
            margin-bottom: 15px;
            align-items: center;
        }
    </style>
</head>
<body class="bg-theme">
    <%@ include file="components/navBarComponent.jsp" %>
    <div class="container mt-5">
        <div class="mb-5">
            <a href="feedbacks" class="text-muted text-decoration-none small">
                <i class="fas fa-arrow-left mr-2"></i>Quay lại quản lý đánh giá
            </a>
            <h1 class="h2 font-weight-bold mt-3">Phân tích mức độ hài lòng</h1>
            <p class="text-muted">Biểu đồ phân bổ mức sao từ khách hàng.</p>
        </div>

        <div class="row">
            <div class="col-lg-4 mb-4">
                <div class="stat-card h-100 d-flex flex-column justify-content-center">
                    <h5 class="text-muted mb-4">Tổng số đánh giá</h5>
                    <c:set var="total" value="0" />
                    <c:forEach items="${stats}" var="entry">
                        <c:set var="total" value="${total + entry.value}" />
                    </c:forEach>
                    <div class="display-3 font-weight-bold text-primary">${total}</div>
                    <div class="mt-3">
                        <c:set var="sum" value="0" />
                        <c:forEach items="${stats}" var="entry">
                            <c:set var="sum" value="${sum + (entry.key * entry.value)}" />
                        </c:forEach>
                        <c:set var="avg" value="${total > 0 ? sum / total : 0}" />
                        <div class="text-warning h4">
                            <fmt:formatNumber value="${avg}" maxFractionDigits="1" /> 
                            <i class="fas fa-star ml-1"></i>
                        </div>
                        <p class="small text-muted">Trung bình cộng</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-8 mb-4">
                <div class="stat-card">
                    <h5 class="text-left mb-4 font-weight-bold">Chi tiết phân bổ</h5>
                    <c:forEach begin="0" end="4" var="idx">
                        <c:set var="star" value="${5 - idx}" />
                        <c:set var="starKey" value="${star.toString()}" />
                        <c:set var="count" value="${stats[starKey]}" />
                        <c:set var="percent" value="${total > 0 ? (count * 100 / total) : 0}" />
                        <div class="row rating-row">
                            <div class="col-2 text-left small font-weight-bold">${star} sao</div>
                            <div class="col-8">
                                <div class="progress">
                                    <div class="progress-bar bg-warning" role="progressbar" 
                                         style="width: ${percent}%" aria-valuenow="${percent}" 
                                         aria-valuemin="0" aria-valuemax="100"></div>
                                </div>
                            </div>
                            <div class="col-2 text-right small text-muted">${count}</div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</body>
</html>



