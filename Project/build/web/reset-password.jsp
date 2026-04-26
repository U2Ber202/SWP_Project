<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Đặt Lại Mật Khẩu | V-SNKR</title>
        
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
                backdrop-filter: none;
                border: 1px solid var(--border);
                border-radius: 20px;
                box-shadow: 0 15px 40px rgba(0, 0, 0, 0.5);
                padding: 2.5rem;
            }

            .brand-logo {
                font-weight: 800;
                font-size: 2rem;
                letter-spacing: 1px;
                color: #ffffff;
                text-decoration: none;
                display: inline-block;
                margin-bottom: 10px;
                transition: all 0.3s ease;
            }

            .brand-logo i {
                color: var(--primary);
            }

            .brand-logo:hover {
                color: var(--primary);
            }

            .form-label {
                font-weight: 600;
                color: #94a3b8;
                margin-bottom: 8px;
                font-size: 0.85rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            /* Inputs */
            .input-group-text {
                background-color: rgba(0,0,0,0.3);
                border: 1px solid var(--border);
                border-right: none;
                color: #94a3b8;
                border-radius: 8px 0 0 8px;
                transition: all 0.3s ease;
            }

            .form-control-custom {
                background: rgba(0,0,0,0.2) !important;
                border-radius: 0 8px 8px 0;
                padding: 12px 15px;
                border: 1px solid var(--border);
                border-left: none;
                font-size: 0.95rem;
                color: white !important;
                transition: all 0.3s ease;
            }
            
            .form-control-custom::placeholder {
                color: #64748b;
            }

            .form-control-custom:focus {
                border-color: var(--primary);
                background-color: rgba(0,0,0,0.3) !important;
                box-shadow: none;
            }

            .input-group:focus-within .input-group-text,
            .input-group:focus-within .form-control-custom {
                border-color: var(--primary);
            }
            
            .input-group:focus-within .input-group-text {
                color: var(--primary);
            }

            /* Buttons */
            .btn-custom {
                background-color: var(--primary);
                color: white;
                border-radius: 12px;
                font-weight: 600;
                padding: 12px;
                text-transform: uppercase;
                letter-spacing: 1px;
                border: none;
                transition: all 0.3s ease;
                margin-top: 15px;
            }

            .btn-custom:hover {
                background-color: var(--primary-dark);
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.4);
                transform: translateY(-2px);
                color: white;
            }

            /* Alerts */
            .alert {
                border-radius: 12px;
                font-size: 0.9rem;
                font-weight: 500;
                border: none;
            }
            .alert-danger { background: rgba(239, 68, 68, 0.15); color: #f87171; border: 1px solid rgba(239, 68, 68, 0.2); }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        
        <section class="auth-section">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-5">
                        <div class="card card-custom">
                            <jsp:include page="components/toastNotification.jsp" />
                            <div class="text-center mb-4">
                                <a href="home" class="brand-logo"><i class="fa-solid fa-bolt"></i> V-SNKR</a>
                                <h3 class="fw-bold text-uppercase mt-3 mb-2 text-white">Tạo Mật Khẩu Mới</h3>
                                <p class="small px-3" style="color: #94a3b8;">
                                    Vui lòng nhập mật khẩu mới cho tài khoản của bạn. Mật khẩu nên chứa cả chữ và số để tăng tính bảo mật.
                                </p>
                                <p class="small px-3 mb-0" style="color: #94a3b8;">
                                    OTP chỉ có hiệu lực trong 10 phút và phải được xác minh trước khi đặt lại mật khẩu.
                                </p>
                            </div>

                            
                            <form action="reset-password" method="POST">
                                
                                <div class="mb-4">
                                    <label class="form-label">Mật khẩu mới</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                        <input type="password" name="newPassword" class="form-control form-control-custom" required placeholder="Nhập mật khẩu mới...">
                                    </div>
                                </div>
                                
                                <div class="mb-4">
                                    <label class="form-label">Xác nhận mật khẩu</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                                        <input type="password" name="confirmPassword" class="form-control form-control-custom" required placeholder="Nhập lại mật khẩu mới...">
                                    </div>
                                </div>
                                
                                <button type="submit" class="btn btn-custom w-100">
                                    <i class="fa-solid fa-floppy-disk me-2"></i> Lưu mật khẩu mới
                                </button>
                                
                            </form>
                            
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



