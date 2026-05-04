<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <fmt:setLocale value="vi_VN" />
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <script>
                    (function () {
                        try {
                            var theme = localStorage.getItem('theme') || 'dark';
                            document.documentElement.setAttribute('data-theme', theme);
                        } catch (e) {
                            document.documentElement.setAttribute('data-theme', 'dark');
                        }
                    })();
                </script>
                <title>Trang chủ | V-SNKR</title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link
                    href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <style>
                    :root {
                        --primary: #ea580c;
                        --primary-dark: #c2410c;
                        --bg: #0f172a;
                        --card-bg: #1e293b;
                        --glass: #0f172a;
                        --border: rgba(255, 255, 255, 0.1);
                    }

                    body {
                        background: var(--bg);
                        color: var(--text-main);
                        font-family: 'Be Vietnam Pro', sans-serif;
                    }

                    .hero {
                        background: radial-gradient(circle at top right, #334155, #0f172a);
                        color: white;
                        padding: 6rem 0;
                        position: relative;
                        overflow: hidden;
                    }

                    .hero::after {
                        content: '';
                        position: absolute;
                        top: -10%;
                        right: -10%;
                        width: 40%;
                        height: 60%;
                        background: var(--primary);
                        filter: blur(150px);
                        opacity: 0.15;
                        z-index: 0;
                    }

                    .hero-card,
                    .filter-card,
                    .product-card,
                    .admin-setting-card {
                        background: var(--card-bg);
                        backdrop-filter: none;
                        border: 1px solid var(--border);
                        border-radius: 20px;
                        color: var(--text-main);
                    }

                    .product-card {
                        transition: all 0.35s ease;
                        overflow: hidden;
                        height: 100%;
                    }

                    .product-card:hover {
                        transform: translateY(-8px);
                        border-color: var(--primary);
                        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.35);
                    }

                    .product-card img {
                        height: 240px;
                        object-fit: cover;
                    }

                    .stock-badge {
                        font-weight: 600;
                        font-size: 0.75rem;
                        border-radius: 8px;
                        padding: 0.4rem 0.8rem;
                        text-transform: uppercase;
                        letter-spacing: 0.5px;
                    }

                    .stock-good {
                        background: rgba(34, 197, 94, 0.15);
                        color: #4ade80;
                        border: 1px solid rgba(34, 197, 94, 0.2);
                    }

                    .stock-low {
                        background: rgba(234, 179, 8, 0.15);
                        color: #facc15;
                        border: 1px solid rgba(234, 179, 8, 0.2);
                    }

                    .stock-out {
                        background: rgba(239, 68, 68, 0.15);
                        color: #f87171;
                        border: 1px solid rgba(239, 68, 68, 0.2);
                    }

                    .btn-primary {
                        background: var(--primary);
                        border: 0;
                        border-radius: 12px;
                        padding: 0.65rem 1.5rem;
                        font-weight: 600;
                    }

                    .btn-primary:hover {
                        background: var(--primary-dark);
                    }

                    .btn-outline-light {
                        border-radius: 12px;
                        border: 1px solid var(--border);
                        color: #cbd5e1;
                    }

                    .btn-outline-light:hover {
                        background: var(--glass);
                        color: white;
                    }

                    .form-control {
                        background: #0f172a;
                        border: 1px solid var(--border);
                        color: white;
                        border-radius: 10px;
                    }

                    .form-control:focus {
                        background: #0f172a;
                        border-color: var(--primary);
                        color: white;
                        box-shadow: none;
                    }

                    select.form-control option {
                        background: #1e293b;
                        color: white;
                    }

                    .price-tag {
                        font-size: 1.2rem;
                        font-weight: 700;
                        color: var(--primary);
                    }

                    .section-title {
                        position: relative;
                        display: inline-block;
                        margin-bottom: 2rem;
                    }

                    .section-title::after {
                        content: '';
                        position: absolute;
                        bottom: -8px;
                        left: 0;
                        width: 40px;
                        height: 3px;
                        background: var(--primary);
                        border-radius: 3px;
                    }

                    .sticky-filter {
                        top: 20px;
                    }
                </style>
                <script src="js/theme.js"></script>
                <link rel="stylesheet" href="css/theme.css">
            </head>

            <body>
                <%@ include file="components/navBarComponent.jsp" %>

                    <c:set var="setting" value="${homeSetting}" />
                    <c:set var="productColumnClass" value="col-12" />
                    <c:if test="${not empty setting and setting.showFilterSidebar}">
                        <c:set var="productColumnClass" value="col-lg-9" />
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty activeSliders}">
                            <div id="homeCarousel" class="carousel slide" data-ride="carousel" data-interval="5000">
                                <ol class="carousel-indicators">
                                    <c:forEach items="${activeSliders}" var="s" varStatus="status">
                                        <li data-target="#homeCarousel" data-slide-to="${status.index}"
                                            class="${status.first ? 'active' : ''}"></li>
                                    </c:forEach>
                                </ol>
                                <div class="carousel-inner">
                                    <c:forEach items="${activeSliders}" var="s" varStatus="status">
                                        <div class="carousel-item ${status.first ? 'active' : ''}"
                                            style="height: 500px; background: url('${s.imageUrl}') center/cover no-repeat;">
                                            <div class="carousel-caption d-none d-md-block text-left"
                                                style="bottom: 20%; left: 10%; right: auto; background: rgba(0,0,0,0.5); padding: 30px; border-radius: 20px; backdrop-filter: none !important; border: 1px solid rgba(255,255,255,0.1); max-width: 600px;">
                                                <h1 class="display-4 font-weight-bold mb-3">${s.title}</h1>
                                                <p class="lead mb-4">${s.description}</p>
                                                <c:if test="${not empty s.productId and s.productId > 0}">
                                                    <a href="detail?productId=${s.productId}" class="btn btn-primary px-4 py-2">Khám phá
                                                        ngay</a>
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <a class="carousel-control-prev" href="#homeCarousel" role="button" data-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="carousel-control-next" href="#homeCarousel" role="button" data-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <section class="hero">
                                <div class="container position-relative" style="z-index: 1;">
                                    <div class="row align-items-center">
                                        <div class="col-lg-7">
                                            <span class="badge badge-pill badge-warning mb-3 px-3 py-2"
                                                style="background: rgba(234, 88, 12, 0.2); color: #fb923c; border: 1px solid rgba(234, 88, 12, 0.3);">
                                                ${not empty setting.heroBadge ? setting.heroBadge : 'BST Nổi bật'}
                                            </span>
                                            <h1 class="display-4 font-weight-bold mb-4" style="line-height: 1.1;">
                                                ${not empty setting.heroTitle ? setting.heroTitle : 'Nâng cấp phong
                                                cách'}
                                                <span class="text-warning">${not empty setting.heroHighlight ?
                                                    setting.heroHighlight : 'Sneaker'}</span>
                                            </h1>
                                            <p class="lead mb-5 text-muted" style="font-size: 1.1rem;">${not empty
                                                setting.heroDescription ? setting.heroDescription : 'Khám phá những mẫu
                                                giày mới nhất.'}</p>

                                            <div class="d-flex flex-wrap">
                                                <a href="#shop"
                                                    class="btn btn-primary mr-3 mb-2">${setting.primaryButtonText}</a>
                                                <a href="#featured-products"
                                                    class="btn btn-outline-light mb-2">${setting.secondaryButtonText}</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${setting.showStats}">
                        <div class="container mt-n5 position-relative" style="z-index: 10;">
                            <div class="hero-card shadow-lg p-4" style="background: rgba(30, 41, 59, 0.8); border: 1px solid rgba(255,255,255,0.1); backdrop-filter: none !important; border-radius: 20px;">
                                <div class="row text-center">
                                    <div class="col-4">
                                        <div class="h2 mb-0 font-weight-bold text-warning">${productCount}</div>
                                        <small class="text-white-50 text-uppercase letter-spacing-1">Sản phẩm</small>
                                    </div>
                                    <div class="col-4 border-left border-right border-white-10">
                                        <div class="h2 mb-0 font-weight-bold text-warning">${listCategories.size()}</div>
                                        <small class="text-white-50 text-uppercase letter-spacing-1">Danh mục</small>
                                    </div>
                                    <div class="col-4">
                                        <div class="h2 mb-0 font-weight-bold text-warning">${listStores.size()}</div>
                                        <small class="text-white-50 text-uppercase letter-spacing-1">Cửa hàng</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty listNews}">
                        <section class="py-4">
                            <div class="container">
                                <h3 class="section-title font-weight-bold">Tin tức mới nhất</h3>
                                <div class="row">
                                    <c:forEach items="${listNews}" var="n">
                                        <div class="col-lg-3 col-md-6 mb-4">
                                            <div class="card product-card" style="height: auto; border-radius: 15px;">
                                                <div class="position-relative">
                                                    <img src="${n.image}" class="card-img-top" alt="${n.title}" style="height: 160px; border-radius: 15px 15px 0 0;">
                                                    <c:if test="${not empty n.storeId}">
                                                        <span class="badge badge-warning position-absolute p-1 px-2" style="top: 10px; right: 10px; font-size: 0.7rem;">Shop News</span>
                                                    </c:if>
                                                </div>
                                                <div class="card-body p-3">
                                                    <h6 class="font-weight-bold text-white text-truncate mb-1 font-size-sm">${n.title}</h6>
                                                    <p class="small text-muted text-truncate mb-2">${n.content}</p>
                                                    <a href="newsDetail?id=${n.id}" class="btn btn-link p-0 text-warning silver small">Đọc tiếp <i class="fas fa-arrow-right ml-1"></i></a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </section>
                    </c:if>

                    <section class="py-5" id="shop">
                        <div class="container">

                            <div class="row">
                                <c:if test="${setting.showFilterSidebar}">
                                    <div class="col-lg-3 mb-4">
                                        <div class="card filter-card">
                                            <div class="card-body">
                                                <h5 class="font-weight-bold mb-4"><i
                                                        class="fas fa-filter mr-2 text-warning"></i>Bộ lọc tìm kiếm</h5>
                                                <form action="home" method="get">
                                                    <div class="form-group">
                                                        <label
                                                            class="small text-muted text-uppercase font-weight-bold">Cửa
                                                            hàng</label>
                                                        <select class="form-control" name="storeId"
                                                            ${sessionScope.acc.role=='owner' ? 'disabled' : '' }>
                                                            <option value="">Tất cả cửa hàng</option>
                                                            <c:forEach items="${listStores}" var="store">
                                                                <option value="${store.id}" ${selectedStoreId==store.id
                                                                    ? 'selected' : '' }>${store.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label
                                                            class="small text-muted text-uppercase font-weight-bold">Danh
                                                            mục</label>
                                                        <select class="form-control" name="categoryId">
                                                            <option value="">Tất cả danh mục</option>
                                                            <c:forEach items="${listCategories}" var="category">
                                                                <option value="${category.cid}"
                                                                    ${selectedCategoryId==category.cid ? 'selected' : ''
                                                                    }>${category.cname}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label
                                                            class="small text-muted text-uppercase font-weight-bold">Từ
                                                            khóa</label>
                                                        <input class="form-control" name="keyword" value="${key}"
                                                            placeholder="Tìm kiếm sản phẩm...">
                                                    </div>
                                                    <button class="btn btn-primary btn-block mt-4" type="submit">Áp dụng
                                                        lọc</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="${productColumnClass}">
                                    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap">
                                        <div class="mb-2">
                                            <h3 class="font-weight-bold mb-0">Danh sách sản phẩm</h3>
                                            <p class="text-muted small mb-0">Hiển thị ${listProducts.size()} /
                                                ${productCount} sản phẩm (Trang ${page}/${totalPage})</p>
                                        </div>
                                        <div class="text-muted small mb-2">
                                            <c:choose>
                                                <c:when test="${homeSetting.featuredProductId != null}">Admin đang ghim
                                                    một sản phẩm cụ thể lên đầu</c:when>
                                                <c:when test="${homeSetting.featuredMode == 'price_desc'}">Top đang được
                                                    set theo giá cao nhất</c:when>
                                                <c:when test="${homeSetting.featuredMode == 'price_asc'}">Top đang được
                                                    set theo giá thấp nhất</c:when>
                                                <c:otherwise>Top đang được set theo mới nhất</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <c:forEach items="${listProducts}" var="p">
                                            <div class="col-md-6 col-xl-4 mb-4">
                                                <div class="card product-card">
                                                    <div class="position-relative">
                                                        <img src="${p.imageUrl}" class="card-img-top" alt="${p.name}">
                                                        <div class="position-absolute" style="top: 15px; right: 15px;">
                                                            <c:choose>
                                                                <c:when test="${p.quantity == 0}">
                                                                    <span class="stock-badge stock-out">Hết hàng</span>
                                                                </c:when>
                                                                <c:when test="${p.quantity <= 5}">
                                                                    <span class="stock-badge stock-low">Còn
                                                                        ${p.quantity} đôi</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="stock-badge stock-good">Còn
                                                                        ${p.quantity} đôi</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>
                                                    <div class="card-body d-flex flex-column">
                                                        <div class="mb-2">
                                                            <span class="text-muted small">${p.storeName}</span>
                                                            <h5 class="font-weight-bold mb-1 mt-1 text-white">${p.name}
                                                            </h5>
                                                        </div>
                                                        <p class="small text-muted flex-grow-1">${p.tiltle}</p>
                                                        <div
                                                            class="d-flex justify-content-between align-items-center mt-3">
                                                            <div class="price-tag">
                                                                <fmt:formatNumber value="${p.price}"
                                                                    pattern="#,### đ" />
                                                            </div>
                                                            <div class="d-flex">
                                                                <a class="btn btn-outline-light btn-sm mr-2"
                                                                    href="detail?productId=${p.id}"><i
                                                                        class="fas fa-eye"></i></a>
                                                                <a class="btn btn-primary btn-sm ${p.quantity == 0 ? 'disabled' : ''}"
                                                                    href="add-to-cart?productId=${p.id}">
                                                                    <i class="fas fa-plus mr-1"></i>Mua
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>

                                        <c:if test="${empty listProducts}">
                                            <div class="col-12 text-center py-5">
                                                <img src="https://cdni.iconscout.com/illustration/premium/thumb/no-product-found-8260029-6617195.png"
                                                    alt="Không có kết quả" style="max-width: 200px; opacity: 0.5;">
                                                <h5 class="mt-4 text-muted">Chưa tìm thấy sản phẩm phù hợp</h5>
                                                <p class="text-muted small">Thử đổi từ khóa hoặc bộ lọc để xem thêm kết
                                                    quả.</p>
                                            </div>
                                        </c:if>
                                    </div>

                                    <c:if test="${totalPage >= 1}">
                                        <div class="d-flex justify-content-center mt-5 mb-4">
                                            <nav aria-label="Phân trang sản phẩm">
                                                <ul class="pagination pagination-md mb-0">
                                                    <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                                                        <a class="page-link border-0 shadow-sm"
                                                            href="home?page=${page - 1}&keyword=${key}&categoryId=${selectedCategoryId}&storeId=${selectedStoreId}"
                                                            style="background: var(--card-bg); color: var(--text-main); border-radius: 10px 0 0 10px;">
                                                            <i class="fas fa-chevron-left"></i>
                                                        </a>
                                                    </li>
                                                    <c:forEach begin="1" end="${totalPage}" var="i">
                                                        <li class="page-item ${page == i ? 'active' : ''}">
                                                            <a class="page-link border-0 shadow-sm ${page == i ? '' : 'text-muted'}"
                                                                href="home?page=${i}&keyword=${key}&categoryId=${selectedCategoryId}&storeId=${selectedStoreId}"
                                                                style="${page == i ? 'background: var(--primary); color: white;' : 'background: var(--card-bg); color: var(--text-main);'}">
                                                                ${i}
                                                            </a>
                                                        </li>
                                                    </c:forEach>
                                                    <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                                        <a class="page-link border-0 shadow-sm"
                                                            href="home?page=${page + 1}&keyword=${key}&categoryId=${selectedCategoryId}&storeId=${selectedStoreId}"
                                                            style="background: var(--card-bg); color: var(--text-main); border-radius: 0 10px 10px 0;">
                                                            <i class="fas fa-chevron-right"></i>
                                                        </a>
                                                    </li>
                                                </ul>
                                            </nav>
                                        </div>
                                    </c:if>
                                </div>
                            </div>

                            <c:if test="${homeSetting.showFeaturedSection}">
                                <div class="mt-5 pt-5 border-top" id="featured-products"
                                    style="border-color: var(--border) !important;">
                                    <h3 class="section-title font-weight-bold">${homeSetting.featuredTitle}</h3>
                                    <div class="row">
                                        <c:forEach items="${featuredProducts}" var="p">
                                            <div class="col-md-6 col-lg-3 mb-4">
                                                <div class="card product-card p-2">
                                                    <img src="${p.imageUrl}" class="rounded shadow-sm"
                                                        style="height: 180px;" alt="${p.name}">
                                                    <div class="card-body px-1 py-3">
                                                        <div class="font-weight-bold text-white text-truncate">${p.name}
                                                        </div>
                                                        <div class="small text-muted mb-2">${p.storeName}</div>
                                                        <div
                                                            class="d-flex justify-content-between align-items-center mt-2">
                                                            <div class="text-warning font-weight-bold">
                                                                <fmt:formatNumber value="${p.price}"
                                                                    pattern="#,### đ" />
                                                            </div>
                                                            <a href="detail?productId=${p.id}"
                                                                class="btn btn-link text-muted p-0"><i
                                                                    class="fas fa-arrow-right"></i></a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        <c:if test="${empty featuredProducts}">
                                            <div class="col-12 text-muted">Chưa có sản phẩm để hiển thị trong khung nổi
                                                bật.</div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                    </div>
                </section>

                    <%@ include file="components/footerComponent.jsp" %>

                        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
                        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
                        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
                        <script>
                            $(function () {
                                $('.product-card').each(function (index) {
                                    $(this).css({
                                        opacity: '0',
                                        transform: 'translateY(20px)',
                                        transition: 'all 0.45s ease-out ' + (index * 0.05) + 's'
                                    });
                                });

                                setTimeout(function () {
                                    $('.product-card').css({
                                        opacity: '1',
                                        transform: 'translateY(0)'
                                    });
                                }, 120);

                                // Force carousel to start playing
                                $('.carousel').carousel({
                                    interval: 5000,
                                    pause: "hover"
                                });
                            });
                        </script>
            </body>

            </html>


