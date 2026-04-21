<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Xác Minh OTP | V-SNKR</title>
        
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
                --card-bg: rgba(255, 255, 255, 0.05);
                --glass: rgba(255, 255, 255, 0.03);
                --border: rgba(255, 255, 255, 0.1);
            }

            body {
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: #f1f5f9;
            }

            .auth-section {
                min-height: 100vh;
                display: flex;
                align-items: center;
                padding: 40px 0;
            }

            /* Card Glassmorphism */
            .card-custom {
                background: var(--card-bg);
                backdrop-filter: blur(15px);
                border: 1px solid var(--border);
                border-radius: 20px;
                box-shadow: 0 15px 40px rgba(0, 0, 0, 0.5);
                padding: 3rem 2.5rem;
            }

            .brand-logo {
                font-weight: 800;
                font-size: 2rem;
                letter-spacing: 1px;
                color: #ffffff;
                text-decoration: none;
                display: inline-block;
                margin-bottom: 15px;
                transition: all 0.3s ease;
            }

            .brand-logo i {
                color: var(--primary);
            }

            .brand-logo:hover {
                color: var(--primary);
            }

            .otp-icon {
                font-size: 3.5rem;
                color: var(--primary);
                margin-bottom: 15px;
                filter: drop-shadow(0 0 10px rgba(234, 88, 12, 0.3));
            }

            .form-label {
                font-weight: 600;
                color: #94a3b8;
                margin-bottom: 10px;
                text-transform: uppercase;
                letter-spacing: 1px;
                font-size: 0.85rem;
            }

            /* Customizing the OTP Input */
            .otp-input {
                letter-spacing: 15px;
                font-weight: 700;
                color: #ffffff !important;
                border: 2px solid var(--border);
                border-radius: 12px;
                padding: 15px;
                background-color: rgba(0, 0, 0, 0.2) !important;
                transition: all 0.3s ease;
                font-family: 'Be Vietnam Pro', sans-serif;
            }

            .otp-input:focus {
                border-color: var(--primary);
                box-shadow: none;
                background-color: rgba(0, 0, 0, 0.3) !important;
            }
            
            .otp-input::placeholder {
                color: #334155;
                letter-spacing: 15px;
            }

            .btn-custom {
                background-color: var(--primary);
                color: white;
                border: none;
                border-radius: 12px;
                font-weight: 700;
                padding: 12px;
                text-transform: uppercase;
                letter-spacing: 1px;
                transition: all 0.3s ease;
                margin-top: 15px;
            }

            .btn-custom:hover {
                background-color: var(--primary-dark);
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.4);
                transform: translateY(-2px);
                color: white;
            }

            .auth-link {
                color: var(--primary);
                font-weight: 600;
                text-decoration: none;
                transition: all 0.3s ease;
                font-size: 0.95rem;
            }

            .auth-link:hover {
                color: var(--primary-dark);
                text-decoration: underline;
            }

            /* Alerts */
            .alert {
                border-radius: 12px;
                font-size: 0.9rem;
                font-weight: 500;
                border: none;
                text-align: left;
            }
            .alert-danger { background: rgba(239, 68, 68, 0.15); color: #f87171; }
            .alert-success { background: rgba(34, 197, 94, 0.15); color: #4ade80; }
            
            .border-top {
                border-top: 1px solid var(--border) !important;
            }
            
            .text-muted-custom {
                color: #94a3b8 !important;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        
        <section class="auth-section">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-5">
                        <div class="card card-custom text-center">
                            <jsp:include page="components/toastNotification.jsp" />
                            <a href="home" class="brand-logo"><i class="fa-solid fa-bolt"></i> V-SNKR</a>
                            
                            <div class="mt-2 mb-4">
                                <i class="fa-solid fa-shield-halved otp-icon"></i>
                                <h3 class="fw-bold text-uppercase mt-2 text-white">Xác Minh Mã OTP</h3>
                                <p class="text-muted-custom small px-3 mt-2">
                                    Vui lòng nhập mã bảo mật gồm 6 chữ số vừa được gửi đến email:<br>
                                    <strong class="text-white">${sessionScope.resetEmail}</strong>
                                </p>
                            </div>


                            
                            <form action="verify-otp" method="POST" class="text-start mt-3">
                                <div class="mb-4">
                                    <label class="form-label d-block text-center">Nhập mã OTP</label>
                                    <input type="text" name="otp" class="form-control text-center fs-2 otp-input mx-auto" 
                                           required maxlength="6" pattern="\d{6}" autocomplete="one-time-code" 
                                           placeholder="------" style="max-width: 280px;">
                                </div>
                                
                                <button type="submit" class="btn btn-custom w-100">
                                    <i class="fa-solid fa-lock-open me-2"></i> Xác Nhận OTP
                                </button>
                            </form>
                            
                            <div class="mt-4 pt-4 border-top">
                                <p class="text-muted-custom small mb-2">Chưa nhận được mã?</p>
                                <div class="d-flex justify-content-center align-items-center">
                                    <form action="send-otp" method="post" class="d-inline">
                                        <input type="hidden" name="email" value="${sessionScope.resetEmail}">
                                        <button type="submit" class="auth-link border-0 bg-transparent p-0">
                                            <i class="fa-solid fa-rotate-right me-1"></i> Gửi lại mã
                                        </button>
                                    </form>
                                    <span class="mx-3" style="color: var(--border);">|</span>
                                    <a href="login" class="text-muted-custom text-decoration-none small hover-white" onmouseover="this.style.color='white'" onmouseout="this.style.color='#94a3b8'">
                                        Quay lại Đăng nhập
                                    </a>
                                </div>
                            </div>
                            
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
