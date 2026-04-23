<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Chỉnh Sửa Danh Mục | V-SNKR</title>

        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

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
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: #f1f5f9;
                display: flex;
                align-items: center;
                justify-content: center;
                min-height: 100vh;
                padding: 40px 0;
            }

            .edit-card {
                background: var(--card-bg);
                backdrop-filter: none;
                border: 1px solid var(--border);
                border-radius: 24px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.4);
                overflow: hidden;
                width: 100%;
                max-width: 500px;
                margin: auto;
            }

            .edit-card-header {
                background: #0f172a;
                color: #ffffff;
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
                color: #94a3b8;
                margin-bottom: 8px;
                font-size: 0.85rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .form-control {
                background: rgba(0,0,0,0.2) !important;
                border: 1px solid var(--border);
                color: white !important;
                border-radius: 0 8px 8px 0;
                padding: 10px 15px;
                font-size: 1rem;
                transition: all 0.3s ease;
            }

            .form-control:focus {
                background: rgba(0,0,0,0.3) !important;
                border-color: var(--primary);
                box-shadow: none;
            }

            .form-control[readonly] {
                background-color: rgba(0,0,0,0.4) !important;
                color: #64748b !important;
                cursor: not-allowed;
            }

            .input-group-text {
                background-color: rgba(0,0,0,0.3);
                border: 1px solid var(--border);
                border-right: none;
                color: #94a3b8;
                border-radius: 8px 0 0 8px;
            }

            .btn-custom-primary {
                background-color: var(--primary);
                color: white;
                border: none;
                padding: 12px 25px;
                border-radius: 12px;
                font-weight: 600;
                letter-spacing: 0.5px;
                transition: all 0.3s ease;
                width: 100%;
                text-transform: uppercase;
            }

            .btn-custom-primary:hover {
                background-color: var(--primary-dark);
                transform: scale(1.02);
                color: white;
            }

            .btn-custom-secondary {
                background-color: transparent;
                color: #94a3b8;
                border: 1px solid var(--border);
                padding: 12px 25px;
                border-radius: 12px;
                font-weight: 600;
                transition: all 0.3s ease;
                width: 100%;
                display: block;
                text-align: center;
                text-decoration: none;
                text-transform: uppercase;
            }

            .btn-custom-secondary:hover {
                background-color: var(--glass);
                color: white;
                text-decoration: none;
            }

            .border-top {
                border-top: 1px solid var(--border) !important;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <div class="container">
            <div class="edit-card">
                <form action="EditCategory" method="post">
                    <div class="edit-card-header">
                        <h4><i class="fa-solid fa-layer-group"></i> Chỉnh Sửa Danh Mục</h4>
                        <a href="managerCategory" class="text-white" style="font-size: 1.2rem; opacity: 0.6; transition: opacity 0.3s;" onmouseover="this.style.opacity='1'" onmouseout="this.style.opacity='0.6'" title="Đóng">
                            <i class="fa-solid fa-xmark"></i>
                        </a>
                    </div>

                    <div class="edit-card-body">
                        <div class="form-group mb-4">
                            <label>ID Danh Mục</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa-solid fa-hashtag"></i></span>
                                </div>
                                <input value="${category.cid}" name="id" type="text" class="form-control" readonly required>
                            </div>
                        </div>

                        <div class="form-group mb-4">
                            <label>Tên Danh Mục</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa-solid fa-tags text-warning"></i></span>
                                </div>
                                <input value="${category.cname}" name="name" type="text" class="form-control" placeholder="Nhập tên danh mục" required>
                            </div>
                        </div>

                        <div class="form-group mb-4">
                            <label>Hãng sản xuất / Xuất xứ</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa-solid fa-globe"></i></span>
                                </div>
                                <input value="${category.manufacturer}" name="manufacturer" type="text" class="form-control" placeholder="Ví dụ: US-UK, Chinese, Vietnam..." required>
                            </div>
                        </div>

                        <div class="row mt-5 pt-4 border-top">
                            <div class="col-6">
                                <a class="btn btn-custom-secondary d-flex align-items-center justify-content-center" href="managerCategory">
                                    <i class="fa-solid fa-arrow-left mr-2"></i> Quay lại
                                </a>
                            </div>
                            <div class="col-6">
                                <button type="submit" class="btn btn-custom-primary d-flex align-items-center justify-content-center">
                                    <i class="fa-solid fa-save mr-2"></i> Cập nhật
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>



