<%@page contentType="text/html" pageEncoding="UTF-8"%>
<footer class="footer-custom pt-5">
    <div class="container px-4 px-lg-5">
        <div class="row pb-4">
            <div class="col-lg-4 col-md-12 mb-4 mb-md-0">
                <div class="logo-box mb-4">
                    <h2 class="text-uppercase fw-bold brand-text">
                        <i class="fa-solid fa-fire"></i> V-SNKR
                    </h2>
                </div>
                <p class="text-muted pe-lg-5 footer-text">
                    Chúng tôi cung cấp những mẫu giày sneaker chất lượng nhất, mang đậm phong cách streetwear. Uy tín tạo nên thương hiệu dẫn đầu xu hướng.
                </p>
                <div class="mt-4 social-icons">
                    <a href="#!" class="btn btn-outline-light btn-floating m-1 rounded-circle"><i class="fab fa-facebook-f"></i></a>
                    <a href="#!" class="btn btn-outline-light btn-floating m-1 rounded-circle"><i class="fab fa-instagram"></i></a>
                    <a href="#!" class="btn btn-outline-light btn-floating m-1 rounded-circle"><i class="fab fa-tiktok"></i></a>
                    <a href="#!" class="btn btn-outline-light btn-floating m-1 rounded-circle"><i class="fab fa-youtube"></i></a>
                </div>
            </div>

            <div class="col-lg-2 col-md-4 col-sm-6 mb-4 mb-md-0">
                <h5 class="text-uppercase mb-4 fw-bold footer-heading">Sản Phẩm</h5>
                <ul class="list-unstyled">
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Giày Thể Thao</a></li>
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Sneaker Limited</a></li>
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Giày Chạy Bộ</a></li>
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Phụ Kiện</a></li>
                </ul>
            </div>

            <div class="col-lg-2 col-md-4 col-sm-6 mb-4 mb-md-0">
                <h5 class="text-uppercase mb-4 fw-bold footer-heading">Dịch Vụ</h5>
                <ul class="list-unstyled">
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Chính sách đổi trả</a></li>
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Hướng dẫn chọn size</a></li>
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Giao hàng & phí ship</a></li>
                    <li class="mb-3"><a href="#!" class="text-muted text-decoration-none hover-link">Tra cứu đơn hàng</a></li>
                </ul>
            </div>

            <div class="col-lg-4 col-md-4 col-sm-12 mb-4 mb-md-0">
                <h5 class="text-uppercase mb-4 fw-bold footer-heading">Liên Hệ</h5>
                <ul class="list-unstyled contact-info">
                    <li class="mb-3">
                        <div class="d-flex align-items-start">
                            <i class="fas fa-map-marker-alt contact-icon mt-1 me-3"></i>
                            <span class="text-muted footer-text">123 Phố Sneaker, Quận 1, TP. Hồ Chí Minh, Việt Nam</span>
                        </div>
                    </li>
                    <li class="mb-3">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-phone-alt contact-icon me-3"></i>
                            <span class="text-muted footer-text">+84 987 654 321</span>
                        </div>
                    </li>
                    <li class="mb-3">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-envelope contact-icon me-3"></i>
                            <span class="text-muted footer-text">contact@v-snkr.com</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="footer-bottom text-center py-3 text-muted small">
        <p class="mb-0">&copy; 2026 Design by <strong>Team 1</strong>. All Rights Reserved.</p>
    </div>
</footer>

<style>
    /* Footer Premium Sneaker Aesthetics */
    .footer-custom {
        background-color: var(--dark-bg, #1e272e);
        color: #ffffff;
        font-family: 'Be Vietnam Pro', sans-serif;
        border-top: 4px solid var(--primary-color, #ff5e57);
    }
    
    .brand-text {
        letter-spacing: 2px;
        color: #ffffff;
    }
    
    .brand-text i {
        color: var(--primary-color, #ff5e57);
    }

    .footer-heading {
        position: relative;
        display: inline-block;
        padding-bottom: 10px;
        font-size: 1.1rem;
        letter-spacing: 1px;
    }

    .footer-heading::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 40px;
        height: 3px;
        background: var(--primary-color, #ff5e57);
        border-radius: 2px;
    }

    .footer-text, .text-muted {
        color: #a4b0be !important;
        font-size: 0.95rem;
        line-height: 1.6;
    }

    .hover-link {
        transition: all 0.3s ease;
        display: inline-block;
    }

    .hover-link:hover {
        color: var(--primary-color, #ff5e57) !important;
        transform: translateX(8px);
    }

    .social-icons .btn-floating {
        width: 40px;
        height: 40px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-color: #576574;
        color: #a4b0be;
        transition: all 0.3s ease;
        padding: 0;
    }

    .social-icons .btn-floating:hover {
        background-color: var(--primary-color, #ff5e57);
        border-color: var(--primary-color, #ff5e57);
        color: #ffffff;
        transform: translateY(-5px);
        box-shadow: 0 8px 15px rgba(255, 94, 87, 0.3);
    }

    .contact-icon {
        color: var(--primary-color, #ff5e57);
        font-size: 1.2rem;
    }

    .footer-bottom {
        background-color: #151b20;
        border-top: 1px solid #2f3640;
    }


    html[data-theme="light"] .footer-custom {
        background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%) !important;
        color: #0f172a !important;
        border-top: 4px solid var(--primary, #ea580c);
    }

    html[data-theme="light"] .footer-bottom {
        background-color: #eef2f7 !important;
        border-top: 1px solid #dbe3ee !important;
        color: #64748b !important;
    }

    html[data-theme="light"] .brand-text,
    html[data-theme="light"] .footer-heading,
    html[data-theme="light"] .footer-custom strong {
        color: #0f172a !important;
    }

    html[data-theme="light"] .footer-text,
    html[data-theme="light"] .footer-custom .text-muted,
    html[data-theme="light"] .footer-bottom .text-muted,
    html[data-theme="light"] .contact-info span,
    html[data-theme="light"] .hover-link {
        color: #64748b !important;
    }

    html[data-theme="light"] .hover-link:hover {
        color: var(--primary, #ea580c) !important;
    }

    html[data-theme="light"] .social-icons .btn-floating {
        background: #ffffff !important;
        border-color: #cbd5e1 !important;
        color: #475569 !important;
        box-shadow: 0 8px 18px rgba(15, 23, 42, 0.06);
    }

    html[data-theme="light"] .social-icons .btn-floating:hover {
        background: #fff7ed !important;
        border-color: #fb923c !important;
        color: #c2410c !important;
        box-shadow: 0 10px 20px rgba(251, 146, 60, 0.18) !important;
    }

    html[data-theme="light"] .contact-icon,
    html[data-theme="light"] .brand-text i,
    html[data-theme="light"] .footer-heading::after {
        color: var(--primary, #ea580c) !important;
        background: var(--primary, #ea580c) !important;
    }
</style>

<script src="js/scripts.js"></script>
