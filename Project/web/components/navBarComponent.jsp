<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/theme.css">
<script src="js/theme.js"></script>
<style>
    @import url('https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;600;700&display=swap');

    .site-nav {
        background: rgba(15, 23, 42, 0.8) !important;
        backdrop-filter: blur(15px);
        border-bottom: 1px solid rgba(255, 255, 255, 0.08);
        padding: 0.8rem 0;
        font-family: 'Be Vietnam Pro', sans-serif;
    }

    .site-nav .navbar-brand {
        font-size: 1.5rem;
        letter-spacing: -0.5px;
        color: white !important;
    }

    .site-nav .nav-link {
        font-weight: 500;
        color: #94a3b8 !important;
        transition: all 0.3s ease;
        padding: 0.5rem 1rem !important;
    }

    .site-nav .nav-link:hover, .site-nav .nav-link.active {
        color: #f97316 !important;
    }

    .cart-btn {
        position: relative;
        background: rgba(255, 255, 255, 0.05);
        border: 1px solid rgba(255, 255, 255, 0.1);
        color: white !important;
        border-radius: 12px;
        padding: 0.5rem 1rem;
        transition: all 0.3s ease;
    }

    .cart-btn:hover {
        background: rgba(249, 115, 22, 0.1);
        border-color: #f97316;
        color: #f97316 !important;
    }

    .cart-badge {
        position: absolute;
        top: -8px;
        right: -8px;
        background: #ea580c;
        color: white;
        font-size: 0.7rem;
        font-weight: 700;
        padding: 2px 6px;
        border-radius: 8px;
        box-shadow: 0 4px 10px rgba(234, 88, 12, 0.4);
    }

    .nav-search {
        background: rgba(0, 0, 0, 0.2) !important;
        border: 1px solid rgba(255, 255, 255, 0.1) !important;
        color: white !important;
        border-radius: 10px !important;
        padding-left: 1rem !important;
    }

    .site-badge {
        background: rgba(234, 88, 12, 0.1);
        border: 1px solid rgba(234, 88, 12, 0.2);
        color: #fb923c;
        border-radius: 8px;
        padding: 0.2rem 0.6rem;
        font-size: 0.7rem;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }
</style>

<nav class="navbar navbar-expand-lg site-nav sticky-top">
    <div class="container">
        <a class="navbar-brand font-weight-bold" href="home">
            <i class="fas fa-bolt text-warning mr-2"></i>V-SNKR
        </a>
        <button class="navbar-toggler text-white" type="button" data-toggle="collapse" data-target="#mainNav">
            <i class="fas fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav mr-auto">
                <!-- Management Links Grouped Under Dashboard -->
                <c:if test="${sessionScope.acc != null and sessionScope.acc.role != 'customer'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-warning" href="#" id="dashboardDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-gauge-high mr-1"></i> Dashboard
                        </a>
                        <div class="dropdown-menu dropdown-menu-dark bg-dark border-secondary shadow-lg" aria-labelledby="dashboardDropdown">
                            <c:if test="${sessionScope.acc.role == 'owner'}">
                                <a class="dropdown-item text-white" href="store-front?id=${ownerStore.id}"><i class="fas fa-store mr-2"></i>Xem Shop</a>
                                <div class="dropdown-divider border-secondary"></div>
                                <a class="dropdown-item text-white" href="managerCategory"><i class="fas fa-tags mr-2"></i>Danh mục</a>
                                <a class="dropdown-item text-white" href="manager"><i class="fas fa-boxes-stacked mr-2"></i>Kho hàng</a>
                                <a class="dropdown-item text-white" href="vouchers"><i class="fas fa-ticket mr-2"></i>Mã giảm giá</a>
                                <a class="dropdown-item text-white" href="orders"><i class="fas fa-clipboard-list mr-2"></i>Đơn hàng</a>
                                <a class="dropdown-item text-white" href="feedbacks"><i class="fas fa-comments mr-2"></i>Đánh giá</a>
                                <a class="dropdown-item text-white" href="statistic"><i class="fas fa-chart-line mr-2"></i>Thống kê</a>
                            </c:if>
                            <c:if test="${sessionScope.acc.role == 'shipper'}">
                                <a class="dropdown-item text-white" href="orders"><i class="fas fa-truck mr-2"></i>Đơn giao hàng</a>
                            </c:if>
                            <c:if test="${sessionScope.acc.role == 'warehouse_manager'}">
                                <a class="dropdown-item text-white" href="manager"><i class="fas fa-warehouse mr-2"></i>Quản lý kho</a>
                                <a class="dropdown-item text-white" href="stockHistory"><i class="fas fa-history mr-2"></i>Lịch sử kho</a>
                            </c:if>
                            <c:if test="${sessionScope.acc.role == 'admin'}">
                                <a class="dropdown-item text-white" href="managerAccount"><i class="fas fa-users mr-2"></i>Tài khoản</a>
                                <a class="dropdown-item text-white" href="manageStore"><i class="fas fa-store-alt mr-2"></i>Cửa hàng</a>
                                <a class="dropdown-item text-white" href="homeSetting"><i class="fas fa-window-maximize mr-2"></i>Cài đặt Home</a>
                                <a class="dropdown-item text-white" href="vouchers"><i class="fas fa-ticket mr-2"></i>Voucher hệ thống</a>
                                <a class="dropdown-item text-white" href="feedbacks"><i class="fas fa-comments mr-2"></i>Phản hồi hệ thống</a>
                                <a class="dropdown-item text-white" href="statistic"><i class="fas fa-chart-pie mr-2"></i>Tổng quan</a>
                            </c:if>
                        </div>
                    </li>
                </c:if>
                
                <c:if test="${sessionScope.acc == null or sessionScope.acc.role == 'customer'}">
                    <li class="nav-item"><a class="nav-link" href="home">Khám phá</a></li>
                    <li class="nav-item"><a class="nav-link" href="stores">Cửa hàng</a></li>
                </c:if>

                <c:if test="${sessionScope.acc != null}">
                    <li class="nav-item">
                        <a class="nav-link" href="profile" title="Hồ sơ cá nhân">
                            <i class="fas fa-user-circle mr-1"></i> Hồ sơ
                        </a>
                    </li>
                </c:if>
            </ul>

            <form action="home" method="get" class="form-inline mr-lg-4">
                <div class="position-relative">
                    <input class="form-control form-control-sm nav-search" type="search" name="keyword" value="${key}" placeholder="Tìm kiếm giày...">
                    <i class="fas fa-search position-absolute text-muted" style="right: 12px; top: 50%; transform: translateY(-50%); font-size: 0.8rem;"></i>
                </div>
            </form>

            <div class="d-flex align-items-center mt-3 mt-lg-0">
                <button type="button" id="theme-toggle" class="theme-toggle-btn" title="??i giao di?n S?ng/T?i" onclick="toggleTheme()">
                    <i class="fas fa-moon"></i>
                </button>
                <c:if test="${sessionScope.acc.role == 'customer'}">
                    <a href="wallet" class="btn btn-outline-warning mr-2 btn-sm px-3" style="border-radius: 10px;">
                        <i class="fas fa-wallet mr-1"></i> Ví
                    </a>
                    <a href="carts" class="cart-btn mr-3 d-flex align-items-center text-decoration-none">
                        <i class="fas fa-shopping-bag mr-lg-2"></i>
                        <span class="d-none d-lg-inline">Giỏ hàng</span>
                        <c:if test="${not empty sessionScope.carts}">
                            <span class="cart-badge">${sessionScope.carts.size()}</span>
                        </c:if>
                    </a>
                </c:if>

                <c:if test="${sessionScope.acc != null}">
                    <span class="site-badge mr-3">
                        <c:choose>
                            <c:when test="${sessionScope.acc.role == 'admin'}">Quản trị</c:when>
                            <c:when test="${sessionScope.acc.role == 'owner'}">Chủ shop</c:when>
                            <c:when test="${sessionScope.acc.role == 'shipper'}">Shipper</c:when>
                            <c:when test="${sessionScope.acc.role == 'warehouse_manager'}">Kho</c:when>
                            <c:otherwise>Khách</c:otherwise>
                        </c:choose>
                    </span>
                    <a class="text-muted hover-white" href="logout" title="Đăng xuất"><i class="fas fa-sign-out-alt fa-lg"></i></a>
                </c:if>

                <c:if test="${sessionScope.acc == null}">
                    <a class="btn btn-link text-white text-decoration-none mr-3" href="login">Đăng nhập</a>
                    <a class="btn btn-warning btn-sm px-4" style="border-radius: 10px; font-weight: 600;" href="signup">Đăng ký</a>
                </c:if>
            </div>
        </div>
    </div>
</nav>
<jsp:include page="/components/toastNotification.jsp" />

