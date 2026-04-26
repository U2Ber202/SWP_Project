<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${store.name} | V-SNKR Shop</title>
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
            body { background: var(--bg); color: #f1f5f9; font-family: 'Be Vietnam Pro', sans-serif; }
            .shop-header {
                background: linear-gradient(rgba(15, 23, 42, 0.7), rgba(15, 23, 42, 0.9)), url('https://images.unsplash.com/photo-1552346154-21d32810aba3?q=80&w=2070&auto=format&fit=crop');
                background-size: cover;
                background-position: center;
                padding: 120px 0 60px;
                border-bottom: 1px solid var(--border);
            }
            .shop-logo {
                width: 120px;
                height: 120px;
                background: var(--primary);
                border-radius: 30px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 3rem;
                margin-bottom: 20px;
                box-shadow: 0 20px 40px rgba(234, 88, 12, 0.3);
            }
            .category-pill {
                background: var(--card-bg);
                border: 1px solid var(--border);
                color: #94a3b8;
                padding: 8px 20px;
                border-radius: 30px;
                margin-right: 10px;
                margin-bottom: 10px;
                display: inline-block;
                transition: all 0.3s ease;
            }
            .category-pill:hover { border-color: var(--primary); color: white; text-decoration: none; }
            .product-card {
                background: var(--card-bg);
                border: 1px solid var(--border);
                border-radius: 20px;
                transition: all 0.35s ease;
                overflow: hidden;
            }
            .product-card:hover { transform: translateY(-8px); border-color: var(--primary); }
            .product-card img { height: 200px; object-fit: cover; }
        </style>
    </head>
    <body>
        <%@ include file="components/navBarComponent.jsp" %>
        
        <header class="shop-header">
            <div class="container text-center">
                <div class="d-flex flex-column align-items-center">
                    <div class="shop-logo"><i class="fas fa-store"></i></div>
                    <h1 class="font-weight-bold display-4">${store.name}</h1>
                    <p class="text-muted lead">Chuyên cung cấp các dòng Sneaker cao cấp nhất</p>
                    <div class="mt-4">
                        <c:forEach items="${listCategories}" var="c">
                            <a href="#" class="category-pill">${c.cname}</a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </header>

        <section class="py-5">
            <div class="container">
                <div class="d-flex justify-content-between align-items-center mb-5">
                    <h2 class="font-weight-bold">Tất cả sản phẩm</h2>
                    <div class="text-muted">${listProducts.size()} sản phẩm</div>
                </div>
                
                <div class="row">
                    <c:forEach items="${listProducts}" var="p">
                        <div class="col-md-6 col-lg-3 mb-4">
                            <div class="card product-card">
                                <img src="${p.imageUrl}" class="card-img-top" alt="${p.name}">
                                <div class="card-body">
                                    <h5 class="font-weight-bold text-white mb-2">${p.name}</h5>
                                    <div class="text-primary font-weight-bold mb-3">
                                        <fmt:formatNumber value="${p.price}" pattern="#,### đ"/>
                                    </div>
                                    <a href="detail?productId=${p.id}" class="btn btn-outline-light btn-block btn-sm">Xem chi tiết</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        <c:if test="${not empty listNews}">
            <section class="py-5 border-top" style="border-color: var(--border) !important;">
                <div class="container">
                    <h2 class="font-weight-bold mb-4">Tin tức từ Shop</h2>
                    <div class="row">
                        <c:forEach items="${listNews}" var="n">
                            <div class="col-lg-3 col-md-6 mb-4">
                                <div class="card product-card" style="height: auto;">
                                    <img src="${n.image}" class="card-img-top" alt="${n.title}" style="height: 150px;">
                                    <div class="card-body p-3">
                                        <h6 class="font-weight-bold text-white text-truncate">${n.title}</h6>
                                        <p class="small text-muted text-truncate">${n.content}</p>
                                        <a href="newsDetail?id=${n.id}" class="btn btn-link p-0 text-warning">Xem thêm <i class="fas fa-arrow-right ml-1"></i></a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </section>
        </c:if>
        </section>

        <%@ include file="components/footerComponent.jsp" %>
    </body>
</html>



