<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Lịch sử Nhập kho | V-SNKR</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            :root {
                --primary: #ea580c;
                --bg: #0f172a;
                --card-bg: #1e293b;
                --border: rgba(255, 255, 255, 0.1);
            }
            body { background: var(--bg); color: #f1f5f9; font-family: 'Be Vietnam Pro', sans-serif; padding-top: 50px; }
            .timeline { position: relative; padding: 20px 0; }
            .timeline::before {
                content: '';
                position: absolute;
                left: 31px;
                top: 0;
                bottom: 0;
                width: 2px;
                background: var(--border);
            }
            .timeline-item { position: relative; margin-bottom: 40px; padding-left: 80px; }
            .timeline-dot {
                position: absolute;
                left: 20px;
                top: 0;
                width: 24px;
                height: 24px;
                background: var(--primary);
                border-radius: 50%;
                border: 4px solid var(--bg);
                z-index: 10;
            }
            .timeline-content {
                background: var(--card-bg);
                border: 1px solid var(--border);
                border-radius: 20px;
                padding: 25px;
                transition: all 0.3s ease;
            }
            .timeline-content:hover { transform: translateX(10px); border-color: var(--primary); }
            .time-badge { font-size: 0.8rem; color: var(--primary); font-weight: 700; margin-bottom: 10px; display: block; }
            .qty-badge { background: rgba(234, 88, 12, 0.15); color: #fb923c; padding: 4px 12px; border-radius: 8px; font-weight: 600; }
        </style>
    </head>
    <body>
        <%@ include file="components/navBarComponent.jsp" %>
        
        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center mb-5">
                <div>
                    <h1 class="h2 font-weight-bold">Lịch sử Nhập kho</h1>
                    <p class="text-muted">Cửa hàng: <span class="text-white">${storeName}</span></p>
                </div>
                <a href="manager" class="btn btn-outline-light"><i class="fas fa-arrow-left mr-2"></i>Quay lại Kho</a>
            </div>

            <div class="timeline">
                <c:forEach items="${stockHistory}" var="h">
                    <div class="timeline-item">
                        <div class="timeline-dot"></div>
                        <div class="timeline-content">
                            <span class="time-badge">${h.createdDate} lúc ${h.createdTime}</span>
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h4 class="h5 font-weight-bold mb-1">${h.productName}</h4>
                                    <p class="text-muted small mb-0">Người nhập: ${h.createdByName}</p>
                                </div>
                                <div class="qty-badge">+ ${h.importQuantity} đôi</div>
                            </div>
                            <c:if test="${not empty h.note}">
                                <div class="p-3 rounded bg-dark-50 mt-2 small text-muted border-left" style="border-left: 3px solid var(--primary) !important;">
                                    <i class="fas fa-quote-left mr-2 text-primary opacity-50"></i>${h.note}
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty stockHistory}">
                    <div class="text-center py-5">
                        <i class="fas fa-history fa-4x text-muted opacity-20 mb-4"></i>
                        <h4 class="text-muted">Chưa có lịch sử nhập kho nào.</h4>
                    </div>
                </c:if>
            </div>
        </div>
    </body>
</html>



