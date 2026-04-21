<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <title>Đăng Ký Tài Khoản | V-SNKR</title>
    
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        :root {
            --primary: #ff6b2b; /* Cam rực rỡ theo ảnh */
            --primary-glow: rgba(255, 107, 43, 0.4);
            --bg: #0f172a;
            --glass-bg: rgba(255, 255, 255, 0.07);
            --glass-border: rgba(255, 255, 255, 0.1);
            --input-bg: rgba(0, 0, 0, 0.3);
        }

        body {
            font-family: 'Be Vietnam Pro', sans-serif;
            background-color: var(--bg) !important;
            color: #f1f5f9;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            overflow-x: hidden;
        }

        /* Hiệu ứng tia sáng nền */
        body::before {
            content: "";
            position: absolute;
            width: 300px;
            height: 300px;
            background: var(--primary);
            filter: blur(150px);
            top: 10%;
            left: 10%;
            z-index: -1;
            opacity: 0.2;
        }

        .auth-container {
            width: 100%;
            max-width: 1100px;
            padding: 20px;
            z-index: 1;
        }

        .main-card {
            background: transparent;
            border: none;
            display: flex;
            flex-direction: row;
            align-items: center;
            gap: 50px;
        }

        /* Phần hình ảnh giày bên trái */
        .auth-image-side {
            flex: 1;
            display: none; /* Ẩn trên mobile */
            position: relative;
        }

        @media (min-width: 992px) {
            .auth-image-side { display: block; }
        }

        .sneaker-img {
            width: 100%;
            border-radius: 30px;
            box-shadow: 0 20px 50px rgba(0,0,0,0.5);
            /* Nếu bạn có ảnh thật, hãy thay URL vào đây */
            background: url('https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=1000') center/cover;
            height: 600px;
            position: relative;
            overflow: hidden;
        }

        .sneaker-img::after {
            content: "";
            position: absolute;
            inset: 0;
            background: linear-gradient(45deg, rgba(255, 107, 43, 0.2), transparent);
        }

        /* Card Form bên phải */
        .auth-form-side {
            flex: 1;
            max-width: 480px;
        }

        .card-custom {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            border: 1px solid var(--glass-border);
            border-radius: 24px;
            padding: 2.5rem;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
        }

        .brand-logo {
            font-weight: 800;
            font-size: 2.2rem;
            color: #ffffff;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            margin-bottom: 10px;
        }

        .brand-logo i { color: var(--primary); }

        .auth-title {
            font-weight: 700;
            font-size: 1.5rem;
            margin-bottom: 5px;
        }

        /* Inputs Style */
        .form-label {
            color: #94a3b8;
            font-size: 0.8rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 8px;
        }

        .input-group {
            background: var(--input-bg);
            border: 1px solid var(--glass-border);
            border-radius: 12px;
            transition: all 0.3s ease;
            margin-bottom: 20px;
        }

        .input-group:focus-within {
            border-color: var(--primary);
            box-shadow: 0 0 0 2px var(--primary-glow);
        }

        .input-group-text {
            background: transparent;
            border: none;
            color: #64748b;
            padding-left: 15px;
        }

        .form-control-custom {
            background: transparent !important;
            border: none;
            color: white !important;
            padding: 12px 15px;
            font-size: 0.95rem;
        }

        .form-control-custom::placeholder { color: #475569; }
        .form-control-custom:focus { box-shadow: none; }

        /* Button */
        .btn-custom {
            background: var(--primary);
            color: white;
            border: none;
            border-radius: 12px;
            padding: 14px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: all 0.3s ease;
            width: 100%;
        }

        .btn-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px var(--primary-glow);
            filter: brightness(1.1);
            color: white;
        }

        /* Alert */
        .alert-custom {
            background: rgba(239, 68, 68, 0.1);
            border: 1px solid rgba(239, 68, 68, 0.2);
            color: #f87171;
            border-radius: 10px;
            font-size: 0.85rem;
            padding: 10px;
        }

        .auth-link {
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
        }

        .auth-link:hover { text-decoration: underline; }
    </style>
    <script src="js/theme.js"></script>
    <link rel="stylesheet" href="css/theme.css">
</head>
<body>

    <div class="auth-container">
        <div class="main-card">
            
            <div class="auth-image-side">
                <div class="sneaker-img">
                    </div>
            </div>

            <div class="auth-form-side">
                <jsp:include page="components/toastNotification.jsp" />
                <div class="card card-custom">
                    <div class="text-center mb-4">
                        <a href="home" class="brand-logo">
                            <i class="fa-solid fa-bolt"></i> V-SNKR
                        </a>
                        <h3 class="auth-title">Đăng Ký Tài Khoản</h3>
                        <p class="text-muted small">Tạo tài khoản để trải nghiệm mua sắm...</p>
                    </div>

                    <form action="signup" method="post">

                        <div class="form-group">
                            <label class="form-label">Tên người dùng</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                                <input type="text" class="form-control form-control-custom" placeholder="Nhập tên đăng nhập..." required name="user" value="${formUser}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Địa chỉ Email</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                <input type="email" class="form-control form-control-custom" placeholder="Nhập địa chỉ email..." required name="email" value="${formEmail}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Mật khẩu</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                <input type="password" class="form-control form-control-custom" placeholder="Nhập mật khẩu..." required name="pass"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Nhập lại mật khẩu</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                                <input type="password" class="form-control form-control-custom" placeholder="Nhập lại mật khẩu..." required name="repass"/>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-custom mt-2">
                            Đăng Ký Ngay
                        </button>

                        <div class="text-center mt-4 pt-3" style="border-top: 1px solid var(--glass-border)">
                            <span class="text-muted small">Bạn đã có tài khoản?</span> 
                            <a href="login" class="auth-link small ms-1">Đăng nhập tại đây</a>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
