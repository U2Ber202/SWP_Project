<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${news.title} | V-SNKR</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        :root { --primary: #ea580c; --bg: #0f172a; --card-bg: rgba(255, 255, 255, 0.05); --border: rgba(255, 255, 255, 0.1); }
        body { font-family: 'Be Vietnam Pro', sans-serif; background-color: var(--bg) !important; color: #f1f5f9; }
        .news-header { position: relative; height: 400px; border-radius: 30px; overflow: hidden; margin-bottom: 40px; box-shadow: 0 20px 50px rgba(0,0,0,0.5); }
        .news-header img { width: 100%; height: 100%; object-fit: cover; }
        .news-overlay { position: absolute; bottom: 0; left: 0; right: 0; background: linear-gradient(transparent, rgba(15, 23, 42, 0.95)); padding: 60px 40px 40px; }
        .news-content { line-height: 1.8; font-size: 1.1rem; color: #cbd5e1; white-space: pre-line; }
        .back-btn { background: rgba(255,255,255,0.05); border: 1px solid var(--border); color: white; border-radius: 12px; padding: 10px 20px; transition: all 0.3s; }
        .back-btn:hover { background: var(--primary); color: white; border-color: var(--primary); text-decoration: none; }
    </style>
    <script src="js/theme.js"></script>
    <link rel="stylesheet" href="css/theme.css">
</head>
<body class="bg-theme">
    <%@ include file="components/navBarComponent.jsp" %>
    
    <div class="container mb-5">
        <a href="home" class="back-btn d-inline-block mb-4"><i class="fas fa-arrow-left mr-2"></i>Quay lại trang chủ</a>
        
        <div class="news-header">
            <img src="${news.image}" alt="${news.title}" onerror="this.src='https://images.unsplash.com/photo-1552346154-21d32810aba3?q=80&w=2070'">
            <div class="news-overlay">
                <div class="mb-2">
                    <span class="badge badge-warning py-2 px-3 rounded-pill uppercase font-weight-bold">
                        <c:choose>
                            <c:when test="${empty news.storeId}">Hệ thống</c:when>
                            <c:otherwise>Cửa hàng</c:otherwise>
                        </c:choose>
                    </span>
                    <span class="ml-3 text-muted silver"><i class="far fa-calendar-alt mr-1"></i> <fmt:formatDate value="${news.createdAt}" pattern="dd MMMM, yyyy"/></span>
                </div>
                <h1 class="display-4 font-weight-bold">${news.title}</h1>
            </div>
        </div>
        
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="news-content article">
                    ${news.content}
                </div>
            </div>
        </div>
    </div>

    <%@ include file="components/footerComponent.jsp" %>
</body>
</html>
