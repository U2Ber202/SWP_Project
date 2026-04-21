<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <script>
                (function () {
                    try {
                        var theme = localStorage.getItem('theme') || 'dark';
                        document.documentElement.setAttribute('data-theme', theme);
                    } catch (e) {
                        document.documentElement.setAttribute('data-theme', 'dark');
                    }
                })();
            </script>
            <title>Cập Nhật Mẫu Giày | V-SNKR</title>

            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap"
                rel="stylesheet">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

            <style>
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

                .card {
                    background: var(--card-bg);
                    backdrop-filter: blur(12px);
                    border: 1px solid var(--border) !important;
                    border-radius: 24px;
                    overflow: hidden;
                }

                .card-header {
                    background: rgba(0, 0, 0, 0.2) !important;
                    border-bottom: 1px solid var(--border);
                    font-weight: 600;
                    letter-spacing: 0.5px;
                    color: white;
                }

                .form-control {
                    background: rgba(0, 0, 0, 0.2) !important;
                    border: 1px solid var(--border);
                    color: white !important;
                    border-radius: 10px;
                    transition: all 0.3s ease;
                }

                .form-control:focus {
                    background: rgba(0, 0, 0, 0.3) !important;
                    border-color: var(--primary);
                    box-shadow: none;
                }

                select.form-control option {
                    background-color: var(--bg);
                    color: white;
                }

                .btn-brand {
                    background-color: var(--primary);
                    border: 0;
                    border-radius: 12px;
                    color: white;
                    font-weight: 600;
                    transition: all 0.3s ease;
                }

                .btn-brand:hover {
                    background-color: var(--primary-dark);
                    color: white;
                    transform: scale(1.02);
                }

                .btn-outline-light {
                    border-radius: 12px;
                    border: 1px solid var(--border);
                    color: #94a3b8;
                    background: transparent;
                    transition: all 0.3s ease;
                }

                .btn-outline-light:hover {
                    background: var(--glass);
                    color: white;
                }

                .img-preview {
                    max-height: 250px;
                    object-fit: cover;
                    border-radius: 16px;
                    border: 1px solid var(--border);
                    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
                }

                .text-muted {
                    color: #94a3b8 !important;
                }

                .border-bottom {
                    border-bottom: 1px solid var(--border) !important;
                }
            </style>
            <script src="js/theme.js"></script>
            <link rel="stylesheet" href="css/theme.css">
        </head>

        <body class="bg-theme">
            <%@ include file="components/navBarComponent.jsp" %>
                <%@ include file="components/toastNotification.jsp" %>
                    <div class="container py-5">
                        <div class="row justify-content-center">
                            <div class="col-lg-10">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h2 class="font-weight-bold mb-0 text-white">
                                        <i class="fas fa-edit text-warning mr-2"></i>Cập nhật mẫu giày
                                    </h2>
                                    <a class="btn btn-outline-light shadow-sm px-3 py-2" href="manager">
                                        <i class="fas fa-arrow-left mr-1"></i> Trở về kho
                                    </a>
                                </div>

                                <div class="card shadow-lg">
                                    <div class="card-header py-3">
                                        <i class="fas fa-info-circle text-warning mr-2"></i>Chỉnh sửa thông tin sản phẩm
                                        #${product.id}
                                    </div>
                                    <div class="card-body p-4">

                                        <div class="text-center mb-4 pb-4 border-bottom">
                                            <img id="shoePreview" src="${product.imageUrl}" alt="Preview"
                                                class="img-preview"
                                                onerror="this.src='https://via.placeholder.com/300x250?text=No+Image'">
                                            <p class="text-muted small mt-3"><i class="fas fa-camera mr-1"></i>Ảnh hiển
                                                thị thực tế</p>
                                        </div>

                                        <form action="edit" method="post">
                                            <input type="hidden" name="id" value="${product.id}">

                                            <div class="form-row">
                                                <div class="form-group col-md-4">
                                                    <label class="small font-weight-bold text-muted text-uppercase">Tên
                                                        mẫu giày</label>
                                                    <input class="form-control" name="name" value="${product.name}"
                                                        required>
                                                </div>
                                                <div class="form-group col-md-2">
                                                    <label class="small font-weight-bold text-muted text-uppercase">Giá
                                                        bán (VNĐ)</label>
                                                    <input class="form-control" name="price" type="number" min="0"
                                                        value="${product.price}" required>
                                                </div>
                                                <div class="form-group col-md-2">
                                                    <label class="small font-weight-bold text-muted text-uppercase">Tồn
                                                        kho</label>
                                                    <input class="form-control" type="text"
                                                        value="${product.quantity} đôi" readonly>
                                                    <small class="text-muted">Dùng form nhập kho riêng để cập nhật
                                                        tồn.</small>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label class="small font-weight-bold text-muted text-uppercase">Link
                                                        hình ảnh</label>
                                                    <input class="form-control" name="image" value="${product.imageUrl}"
                                                        oninput="document.getElementById('shoePreview').src=this.value"
                                                        required>
                                                </div>
                                            </div>

                                            <div class="form-row mt-2">
                                                <div class="form-group col-md-4">
                                                    <label
                                                        class="small font-weight-bold text-muted text-uppercase">Thương
                                                        hiệu / Danh mục</label>
                                                    <select class="form-control" name="category" required>
                                                        <c:forEach items="${listCategories}" var="o">
                                                            <option value="${o.cid}" ${product.categoryId==o.cid
                                                                ? 'selected' : '' }>${o.cname}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label class="small font-weight-bold text-muted text-uppercase">Xuất
                                                        xứ / Hãng</label>
                                                    <input name="manufacturer" class="form-control"
                                                        value="${product.manufacturer}"
                                                        placeholder="Ví dụ: US-UK, Chinese, Vietnam..." required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label class="small font-weight-bold text-muted text-uppercase">Kích
                                                        cỡ (Size)</label>
                                                    <input class="form-control" name="title" value="${product.tiltle}"
                                                        required>
                                                </div>
                                            </div>

                                            <div class="form-group mt-2">
                                                <label class="small font-weight-bold text-muted text-uppercase">Mô tả
                                                    sản phẩm</label>
                                                <textarea class="form-control" name="description" rows="4"
                                                    required>${product.description}</textarea>
                                            </div>

                                            <div class="mt-4 pt-3 border-top">
                                                <button class="btn btn-brand px-4 py-2 mr-2" type="submit">
                                                    <i class="fas fa-save mr-2"></i>Lưu thay đổi
                                                </button>
                                                <a class="btn btn-outline-light px-4 py-2" href="manager">Hủy bỏ</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>