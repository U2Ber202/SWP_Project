<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.sendRedirect("send-otp");
    return;
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Quên mật khẩu - V-SNKR" />
        <meta name="author" content="V-SNKR" />
        <title>Khôi Phục Mật Khẩu | V-SNKR</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        
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
                overflow: hidden;
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
                text-decoration: none;
            }

            /* Input trong suốt */
            .form-control-custom {
                background: rgba(0,0,0,0.2) !important;
                border: 1px solid var(--border);
                color: white !important;
                border-radius: 12px;
                padding: 12px 15px;
                font-size: 0.95rem;
                transition: all 0.3s ease;
            }

            .form-control-custom::placeholder {
                color: #64748b;
            }

            .form-control-custom:focus {
                border-color: var(--primary);
                background: rgba(0,0,0,0.3) !important;
                box-shadow: none;
            }

            .icon-wrapper {
                width: 45px;
                display: flex;
                align-items: center;
                justify-content: center;
                color: var(--primary);
                font-size: 1.2rem;
            }

            /* Nút bấm đồng bộ */
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
            }

            .btn-custom:hover {
                background-color: var(--primary-dark);
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.4);
                transform: translateY(-2px);
                color: white;
            }

            /* Links */
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

            /* Image Section */
            .img-fluid-custom {
                width: 100%;
                height: 100%;
                object-fit: cover;
                min-height: 400px;
                opacity: 0.8; /* Làm tối ảnh một chút để phù hợp với nền Dark Mode */
                transition: opacity 0.5s ease;
            }
            
            .img-fluid-custom:hover {
                opacity: 1;
            }

            .form-label {
                font-weight: 600;
                color: #94a3b8;
                margin-bottom: 5px;
                font-size: 0.9rem;
            }
            
            /* Cảnh báo */
            .text-danger {
                color: #f87171 !important;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>

    <body>

        <section class="auth-section">
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-lg-10 col-xl-9">
                        <div class="card-custom row g-0 flex-lg-row flex-column-reverse">
                            
                            <div class="col-lg-6 p-4 p-md-5 d-flex flex-column justify-content-center">
                                <jsp:include page="components/toastNotification.jsp" />
                                <div class="text-center mb-4">
                                    <a href="home" class="brand-logo"><i class="fa-solid fa-bolt"></i> V-SNKR</a>
                                    <h3 class="fw-bold text-uppercase mt-2 text-white">Thay Đổi Mật Khẩu</h3>
                                    <p class="small" style="color: #94a3b8;">Nhập thông tin bên dưới để thiết lập lại mật khẩu của bạn.</p>
                                </div>

                                <form action="forgetPassword" method="post">
                                    
                                    <div class="d-flex flex-row align-items-center mb-4">
                                        <div class="icon-wrapper"><i class="fa-solid fa-user"></i></div>
                                        <div class="flex-fill mb-0">
                                            <input type="text" placeholder="Nhập tên tài khoản (Username)" id="formUser" class="form-control form-control-custom" required name="user"/>
                                        </div>
                                    </div>

                                    <div class="d-flex flex-row align-items-center mb-4">
                                        <div class="icon-wrapper"><i class="fa-solid fa-lock"></i></div>
                                        <div class="flex-fill mb-0">
                                            <input type="password" placeholder="Mật khẩu mới" id="formNewPass" class="form-control form-control-custom" required name="newPassword"/>
                                        </div>
                                    </div>

                                    <div class="d-flex flex-row align-items-center mb-4">
                                        <div class="icon-wrapper"><i class="fa-solid fa-key"></i></div>
                                        <div class="flex-fill mb-0">
                                            <input type="password" placeholder="Xác nhận mật khẩu mới" id="formConfirmPass" class="form-control form-control-custom" required name="confirmPassword"/>
                                        </div>
                                    </div>



                                    <div class="text-center mb-4 mt-2">
                                        <button class="btn btn-custom w-100" type="submit">Xác Nhận Đổi Mật Khẩu</button>
                                    </div>

                                    <div class="text-center pt-2">
                                        <span style="color: #94a3b8;">Đã nhớ mật khẩu?</span> <a href="login" class="auth-link">Quay lại Đăng nhập</a>
                                    </div>
                                </form>
                            </div>

                            <div class="col-lg-6 d-none d-lg-block p-0 position-relative">
                                <div class="position-absolute w-100 h-100" style="background: linear-gradient(to right, var(--bg), transparent 20%); z-index: 1;"></div>
                                <img src="https://images.unsplash.com/photo-1514989940723-e8e51635b782?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80"
                                     class="img-fluid-custom" alt="V-SNKR Premium Shoes">
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>



