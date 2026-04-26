<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Chỉnh Sửa Tài Khoản | V-SNKR</title>

            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap"
                rel="stylesheet">
            <link rel="stylesheet"
                href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
            <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

            <style>
                /* Đồng bộ biến màu sắc với trang chủ */
                body {
                    font-family: 'Be Vietnam Pro', sans-serif;
                    background-color: var(--bg) !important;
                    color: var(--text-main);
                }

                .main-content {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    min-height: calc(100vh - 80px); /* Adjusting for navbar height */
                    padding: 40px 0;
                }

                /* Đồng bộ Card thành Opaque */
                .edit-card {
                    background: var(--card-bg) !important;
                    border: 1px solid var(--border);
                    border-radius: 24px;
                    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
                    overflow: hidden;
                    width: 100%;
                    max-width: 700px;
                    margin: auto;
                    backdrop-filter: none !important;
                }

                .edit-card-header {
                    background: var(--card-bg) !important;
                    color: var(--text-main);
                    padding: 25px 30px;
                    border-bottom: 1px solid var(--border);
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                }

                .edit-card-header h4 {
                    margin: 0;
                    font-weight: 700;
                    font-size: 1.4rem;
                    letter-spacing: 0.5px;
                    text-transform: uppercase;
                }

                .edit-card-header h4 i {
                    color: var(--primary);
                    margin-right: 10px;
                }

                .edit-card-body {
                    padding: 40px 30px;
                }

                .form-group label {
                    font-weight: 600;
                    color: var(--text-muted);
                    margin-bottom: 8px;
                    font-size: 0.85rem;
                    text-transform: uppercase;
                    letter-spacing: 0.5px;
                }

                /* Đồng bộ Input Form */
                .form-control {
                    background: var(--bg) !important;
                    border: 1px solid var(--border);
                    color: var(--text-main) !important;
                    border-radius: 0 8px 8px 0;
                    padding: 10px 15px;
                    font-size: 1rem;
                    transition: all 0.3s ease;
                }

                .form-control:focus {
                    background: var(--bg) !important;
                    border-color: var(--primary);
                    box-shadow: none;
                }

                /* Trường không được sửa (readonly) */
                .form-control[readonly] {
                    background-color: rgba(0, 0, 0, 0.05) !important;
                    color: var(--text-muted) !important;
                    cursor: not-allowed;
                }

                .input-group-text {
                    background-color: var(--bg);
                    border: 1px solid var(--border);
                    border-right: none;
                    color: var(--text-muted);
                    border-radius: 8px 0 0 8px;
                }

                /* Nút bấm đồng bộ */
                .btn-custom-primary {
                    background-color: var(--primary);
                    color: white;
                    border: none;
                    padding: 10px 25px;
                    border-radius: 12px;
                    font-weight: 600;
                    letter-spacing: 0.5px;
                    transition: all 0.3s ease;
                }

                .btn-custom-primary:hover {
                    background-color: var(--primary-dark);
                    transform: scale(1.02);
                    color: white;
                }

                .btn-custom-secondary {
                    background-color: transparent;
                    color: var(--text-muted);
                    border: 1px solid var(--border);
                    padding: 10px 25px;
                    border-radius: 12px;
                    font-weight: 600;
                    transition: all 0.3s ease;
                }

                .btn-custom-secondary:hover {
                    background-color: var(--bg);
                    color: var(--text-main);
                }

                .border-top {
                    border-top: 1px solid var(--border) !important;
                }

                .status-switch {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    gap: 16px;
                    padding: 14px 18px;
                    border: 1px solid var(--border);
                    border-radius: 14px;
                    background: var(--bg);
                }

                .status-switch-label {
                    display: flex;
                    flex-direction: column;
                    gap: 4px;
                }

                .status-switch-label strong {
                    color: var(--text-main);
                    font-size: 1rem;
                }

                .status-switch-label span {
                    color: var(--text-muted);
                    font-size: 0.9rem;
                }

                .switch {
                    position: relative;
                    display: inline-block;
                    width: 64px;
                    height: 34px;
                    margin: 0;
                }

                .switch input {
                    opacity: 0;
                    width: 0;
                    height: 0;
                }

                .slider {
                    position: absolute;
                    cursor: pointer;
                    inset: 0;
                    background-color: rgba(239, 68, 68, 0.45);
                    transition: 0.3s;
                    border-radius: 999px;
                    border: 1px solid rgba(255, 255, 255, 0.15);
                }

                .slider:before {
                    position: absolute;
                    content: "";
                    height: 24px;
                    width: 24px;
                    left: 4px;
                    top: 4px;
                    background-color: white;
                    transition: 0.3s;
                    border-radius: 50%;
                }

                .switch input:checked+.slider {
                    background-color: rgba(34, 197, 94, 0.7);
                }

                .switch input:checked+.slider:before {
                    transform: translateX(30px);
                }
            </style>
            <script src="js/theme.js"></script>
            <link rel="stylesheet" href="css/theme.css">
        </head>

        <body class="bg-theme">
            <%@ include file="components/navBarComponent.jsp" %>
            <div class="main-content">
                <div class="container">
                    <div class="edit-card">
                        <form action="EditAccount" method="post">
                                <div class="edit-card-header">
                                    <h4><i class="fa-solid fa-user-pen"></i> Chỉnh Sửa Tài Khoản</h4>
                                    <a href="managerAccount" class="text-main"
                                        style="font-size: 1.2rem; opacity: 0.6; transition: opacity 0.3s;"
                                        onmouseover="this.style.opacity='1'" onmouseout="this.style.opacity='0.6'"><i
                                            class="fa-solid fa-xmark"></i></a>
                                </div>

                                <div class="edit-card-body">

                                    <div class="row">
                                        <div class="col-md-6 form-group mb-4">
                                            <label>ID Tài Khoản</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i
                                                            class="fa-solid fa-id-card"></i></span>
                                                </div>
                                                <input value="${account.uid}" name="id" type="text" class="form-control"
                                                    readonly required>
                                            </div>
                                        </div>

                                        <div class="col-md-6 form-group mb-4">
                                            <label>Tên Đăng Nhập</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i
                                                            class="fa-solid fa-user"></i></span>
                                                </div>
                                                <input value="${account.user}" name="user" type="text"
                                                    class="form-control" readonly required>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-12 form-group mb-4">
                                            <label>Trạng Thái</label>
                                            <div class="status-switch">
                                                <div class="status-switch-label">
                                                    <strong id="statusText">${account.active ? 'Active' : 'Inactive'}
                                                        Mode</strong>
                                                    <span>Bật hoặc tắt trạng thái hoạt động của tài khoản</span>
                                                </div>
                                                <label class="switch">
                                                    <input id="activeToggle" name="active" type="checkbox"
                                                        value="active" ${account.active ? 'checked' : '' }>
                                                    <span class="slider"></span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group mb-4" id="reasonGroup" style="display: none;">
                                        <label>Lý Do Thay Đổi Trạng Thái</label>
                                        <textarea name="reason" id="reasonInput" class="form-control" rows="3"
                                            placeholder="Nhập lý do thay đổi trạng thái (bắt buộc)..."></textarea>
                                    </div>

                                    <div class="form-group mb-4">
                                        <label>Địa Chỉ Email</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text"><i
                                                        class="fa-solid fa-envelope"></i></span>
                                            </div>
                                            <input value="${account.email}" name="email" type="email"
                                                class="form-control" placeholder="Nhập địa chỉ email..." required>
                                        </div>
                                    </div>


                                    <div class="d-flex justify-content-end mt-5 pt-4 border-top">
                                        <a class="btn btn-custom-secondary mr-3 d-flex align-items-center"
                                            href="managerAccount">
                                            <i class="fa-solid fa-arrow-left mr-2"></i> Quay lại
                                        </a>
                                        <button type="submit" class="btn btn-custom-primary d-flex align-items-center">
                                            <i class="fa-solid fa-save mr-2"></i> Cập nhật
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
                    <script>
                        (function () {
                            var toggle = document.getElementById('activeToggle');
                            var statusText = document.getElementById('statusText');

                            var initialChecked = toggle.checked;
                            var reasonGroup = document.getElementById('reasonGroup');
                            var reasonInput = document.getElementById('reasonInput');

                            function syncStatus() {
                                var isChanged = toggle.checked !== initialChecked;
                                statusText.textContent = toggle.checked ? 'Active Mode' : 'Inactive Mode';

                                if (isChanged) {
                                    reasonGroup.style.display = 'block';
                                    reasonInput.setAttribute('required', 'required');
                                } else {
                                    reasonGroup.style.display = 'none';
                                    reasonInput.removeAttribute('required');
                                }
                            }

                            if (toggle && statusText) {
                                toggle.addEventListener('change', syncStatus);
                                syncStatus();
                            }
                        })();
                    </script>
        </body>

        </html>


