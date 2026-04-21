<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Hồ Sơ Cá Nhân - V-SNKR" />
        <title>Hồ Sơ Cá Nhân | V-SNKR</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

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

            .profile-section {
                min-height: 80vh;
                display: flex;
                align-items: center;
                padding: 50px 0;
            }

            /* Card Glassmorphism */
            .profile-card {
                background: var(--card-bg);
                backdrop-filter: blur(15px);
                border: 1px solid var(--border);
                border-top: 5px solid var(--primary);
                border-radius: 20px;
                box-shadow: 0 15px 40px rgba(0,0,0,0.4);
                padding: 40px;
            }

            .profile-header {
                text-align: center;
                margin-bottom: 30px;
            }

            .avatar-placeholder {
                width: 90px;
                height: 90px;
                background: rgba(0, 0, 0, 0.3);
                color: var(--primary);
                border: 1px solid var(--border);
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-size: 2.5rem;
                margin-bottom: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            }

            .profile-header h3 {
                font-weight: 700;
                color: #ffffff;
                text-transform: uppercase;
                letter-spacing: 1px;
                font-size: 1.5rem;
                margin: 0;
            }

            /* Form Styles */
            .form-label {
                font-weight: 600;
                color: #94a3b8;
                margin-bottom: 8px;
                font-size: 0.85rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .input-group-text {
                background-color: rgba(0,0,0,0.3);
                border: 1px solid var(--border);
                border-right: none;
                color: #94a3b8;
                border-radius: 8px 0 0 8px;
                transition: all 0.3s ease;
            }

            .form-control {
                background: rgba(0,0,0,0.2) !important;
                border: 1px solid var(--border);
                border-radius: 0 8px 8px 0;
                padding: 10px 15px;
                height: auto;
                color: white !important;
                transition: all 0.3s ease;
            }
            
            .form-control::placeholder {
                color: #64748b;
            }

            .form-control:focus {
                border-color: var(--primary);
                background-color: rgba(0,0,0,0.3) !important;
                box-shadow: none;
            }

            .input-group:focus-within .input-group-text {
                border-color: var(--primary);
                color: var(--primary);
            }

            /* Ô input không được phép sửa (Readonly) */
            .form-control[readonly] {
                background-color: rgba(0,0,0,0.4) !important;
                color: #64748b !important;
                cursor: not-allowed;
                font-weight: 500;
            }

            textarea.form-control {
                border-radius: 8px; /* Textarea không dùng input-group-prepend */
            }

            /* Buttons */
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
                margin-top: 10px;
            }

            .btn-custom:hover {
                background-color: var(--primary-dark);
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.4);
                transform: translateY(-2px);
                color: white;
            }

            /* Alert Tùy Chỉnh Cho Dark Mode */
            .alert {
                border-radius: 12px;
                font-weight: 500;
                font-size: 0.95rem;
                border: none;
            }
            .alert-success {
                background-color: rgba(34, 197, 94, 0.15);
                color: #4ade80;
                border: 1px solid rgba(34, 197, 94, 0.2);
            }
            
            .alert .close {
                color: currentColor;
                opacity: 0.8;
                text-shadow: none;
            }
            .alert .close:hover {
                opacity: 1;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <%@include file="components/navBarComponent.jsp" %>

        <section class="profile-section">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="profile-card">
                            
                            <div class="profile-header">
                                <div class="avatar-placeholder">
                                    <i class="fa-solid fa-user-astronaut"></i>
                                </div>
                                <h3>Thông Tin Cá Nhân</h3>
                                <p class="small mt-2 mb-0" style="color: #94a3b8;">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
                            </div>


                            <form action="profile" method="POST">
                                
                                <div class="form-group mb-3">
                                    <label class="form-label">Tên đăng nhập</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-user-lock"></i></span>
                                        </div>
                                        <input type="text" class="form-control" name="user" value="${acc.user}" readonly title="Tên đăng nhập không thể thay đổi">
                                    </div>
                                </div>

                                <div class="form-group mb-3">
                                    <label class="form-label">Họ và tên</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-address-card"></i></span>
                                        </div>
                                        <input type="text" class="form-control" name="fullname" value="${not empty formFullname ? formFullname : acc.fullname}" placeholder="Nhập họ và tên của bạn...">
                                    </div>
                                </div>

                                <div class="form-group mb-3">
                                    <label class="form-label">Số điện thoại</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                                        </div>
                                        <input type="text" class="form-control" name="phone" value="${not empty formPhone ? formPhone : acc.phone}" placeholder="Nhập số điện thoại liên lạc...">
                                    </div>
                                </div>

                                <div class="form-group mb-3">
                                    <label class="form-label">Địa chỉ Email</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                        </div>
                                        <input type="email" class="form-control" name="email" value="${not empty formEmail ? formEmail : acc.email}" placeholder="Nhập địa chỉ email...">
                                    </div>
                                </div>

                                <div class="form-group mb-4">
                                    <label class="form-label">Địa chỉ giao hàng mặc định</label>
                                    <textarea class="form-control" name="address" rows="3" placeholder="Nhập địa chỉ chi tiết...">${not empty formAddress ? formAddress : acc.address}</textarea>
                                </div>

                                <button type="submit" class="btn btn-custom w-100">
                                    <i class="fa-solid fa-floppy-disk mr-2"></i> Lưu Cập Nhật
                                </button>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <%@include file="components/footerComponent.jsp" %>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
