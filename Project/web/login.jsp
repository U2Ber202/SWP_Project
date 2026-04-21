<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Đăng Nhập | V-SNKR</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
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

            /* Căn chỉnh chiều cao toàn màn hình */
            .auth-wrapper {
                min-height: 100vh;
                display: flex;
            }

            /* Cột chứa form */
            .form-column {
                background-color: var(--bg);
                display: flex;
                flex-direction: column;
                justify-content: center;
                padding: 40px;
                position: relative;
                z-index: 2;
            }

            /* Cột chứa ảnh với gradient chuyển êm vào Dark Mode */
            .image-column {
                background: linear-gradient(to right, var(--bg) 0%, rgba(15, 23, 42, 0.4) 50%, rgba(15, 23, 42, 0.7) 100%), 
                            url('https://images.unsplash.com/photo-1549298916-b41d501d3772?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80') center/cover no-repeat;
                position: relative;
            }

            .brand-logo {
                font-weight: 800;
                font-size: 2.2rem;
                letter-spacing: 1px;
                color: #ffffff;
                text-decoration: none;
                margin-bottom: 2rem;
                display: inline-block;
                transition: all 0.3s ease;
            }

            .brand-logo i {
                color: var(--primary);
            }
            
            .brand-logo:hover {
                color: var(--primary);
            }

            .auth-title {
                font-weight: 700;
                letter-spacing: 1px;
                text-transform: uppercase;
                margin-bottom: 1.5rem;
                color: #ffffff;
            }

            /* Tùy chỉnh input */
            .form-label {
                font-weight: 600;
                font-size: 0.95rem;
                margin-bottom: 8px;
                color: #94a3b8;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .form-control-custom {
                background: rgba(0,0,0,0.2) !important;
                border-radius: 12px;
                padding: 12px 15px;
                border: 1px solid var(--border);
                color: white !important;
                transition: all 0.3s ease;
                font-size: 1rem;
            }
            
            .form-control-custom::placeholder {
                color: #64748b;
            }

            .form-control-custom:focus {
                border-color: var(--primary);
                background-color: rgba(0,0,0,0.3) !important;
                box-shadow: none;
            }

            /* Tùy chỉnh Checkbox */
            .form-check-input {
                background-color: rgba(0,0,0,0.2);
                border-color: var(--border);
                cursor: pointer;
            }
            
            .form-check-input:focus {
                box-shadow: none;
                border-color: var(--primary);
            }

            .form-check-input:checked {
                background-color: var(--primary);
                border-color: var(--primary);
            }
            
            .form-check-label {
                cursor: pointer;
                color: #94a3b8;
            }

            /* Nút bấm */
            .btn-custom {
                background-color: var(--primary);
                color: white;
                border: none;
                border-radius: 12px;
                font-weight: 600;
                padding: 12px;
                text-transform: uppercase;
                letter-spacing: 1px;
                transition: all 0.3s ease;
                width: 100%;
                margin-top: 10px;
            }

            .btn-custom:hover {
                background-color: var(--primary-dark);
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.4);
                transform: translateY(-2px);
                color: white;
            }

            /* Link */
            .auth-link {
                color: var(--primary);
                font-weight: 600;
                text-decoration: none;
                transition: color 0.3s ease;
            }

            .auth-link:hover {
                color: var(--primary-dark);
                text-decoration: underline;
            }

            /* Thông báo lỗi */
            .alert-danger {
                background-color: rgba(239, 68, 68, 0.15);
                color: #f87171;
                border: 1px solid rgba(239, 68, 68, 0.2);
                border-radius: 12px;
            }

            .alert-success {
                background-color: rgba(34, 197, 94, 0.15);
                color: #4ade80;
                border: 1px solid rgba(34, 197, 94, 0.2);
                border-radius: 12px;
            }


            /* Responsive container width */
            .form-inner {
                width: 100%;
                max-width: 450px;
                margin: 0 auto;
            }
            
            /* Text Muted */
            .text-muted {
                color: #94a3b8 !important;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>

        <div class="container-fluid p-0">
            <div class="row g-0 auth-wrapper">
                
                <div class="col-lg-6 form-column">
                    <jsp:include page="components/toastNotification.jsp" />
                    <div class="form-inner">
                        
                        <div class="text-center text-lg-start">
                            <a href="home" class="brand-logo"><i class="fa-solid fa-bolt"></i> V-SNKR</a>
                        </div>

                        <form action="login" method="post">
                            <h3 class="auth-title">Đăng Nhập</h3>
                            
                            <div class="mb-4">

                                <label class="form-label" for="usernameInput">Tên người dùng</label>
                                <input type="text" value="${username}" id="usernameInput" class="form-control form-control-custom" placeholder="Nhập tên đăng nhập..." name="Username" required autofocus/>
                            </div>

                            <div class="mb-4">
                                <label class="form-label" for="passwordInput">Mật khẩu</label>
                                <input type="password" id="passwordInput" class="form-control form-control-custom" placeholder="Nhập mật khẩu..." name="Password" required/>
                            </div>

                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" value="1" id="rememberCheck" ${rememberUsername ? 'checked' : ''} name="r"/>
                                    <label class="form-check-label" style="font-weight: 500;" for="rememberCheck">
                                        Nhớ tên đăng nhập
                                    </label>
                                </div>
                                <a href="send-otp" class="auth-link small">Quên mật khẩu?</a>
                            </div>

                            <div class="mb-4">
                                <button class="btn btn-custom" type="submit">
                                    <i class="fa-solid fa-right-to-bracket me-2"></i> Đăng nhập
                                </button>
                            </div>

                            <div class="text-center text-muted mt-4 pt-3 border-top" style="border-color: var(--border) !important;">
                                <p class="mb-2">Bạn chưa có tài khoản? <a href="signup.jsp" class="auth-link">Đăng ký ngay</a></p>
                                <p class="mb-0">
                                    <a href="home" class="text-muted text-decoration-none transition-all hover:text-white" onmouseover="this.style.color='white'" onmouseout="this.style.color='#94a3b8'">
                                        <i class="fa-solid fa-arrow-left me-1"></i> Quay lại trang chủ
                                    </a>
                                </p>
                            </div>

                        </form>
                    </div>
                </div>

                <div class="col-lg-6 d-none d-lg-block image-column"></div>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
