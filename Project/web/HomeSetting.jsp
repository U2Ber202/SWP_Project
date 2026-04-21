<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Cấu hình trang chủ | V-SNKR Admin</title>
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
                background-color: var(--bg);
                color: #f1f5f9;
                padding: 32px 0 48px;
            }
            .panel {
                background: var(--card-bg);
                backdrop-filter: blur(12px);
                border: 1px solid var(--border);
                border-radius: 24px;
                box-shadow: 0 14px 40px rgba(0, 0, 0, 0.3);
                overflow: hidden;
                margin-bottom: 24px;
            }
            .panel-header {
                padding: 24px 28px;
                border-bottom: 1px solid var(--border);
                background: rgba(0, 0, 0, 0.18);
            }
            .panel-title {
                margin: 0;
                font-size: 1.35rem;
                font-weight: 700;
            }
            .panel-body {
                padding: 24px 28px;
            }
            .btn-primary-custom {
                background: var(--primary);
                border: none;
                border-radius: 12px;
                color: white;
                font-weight: 600;
                padding: 10px 24px;
                transition: all 0.3s ease;
            }
            .btn-primary-custom:hover {
                background: var(--primary-dark);
                transform: translateY(-2px);
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.3);
            }
            .btn-secondary-custom {
                background: transparent;
                color: #cbd5e1;
                border: 1px solid var(--border);
                border-radius: 12px;
                font-weight: 600;
                padding: 10px 18px;
            }
            .form-control, .custom-select, textarea.form-control {
                background: rgba(0, 0, 0, 0.2);
                border: 1px solid var(--border);
                color: white;
                border-radius: 12px;
                padding: 12px;
            }
            .form-control:focus, .custom-select:focus {
                background: rgba(0, 0, 0, 0.3);
                border-color: var(--primary);
                color: white;
                box-shadow: none;
            }
            .custom-select option {
                background: #1e293b;
                color: white;
            }
            .alert { border-radius: 14px; border: none; }
            .setting-box {
                background: rgba(255, 255, 255, 0.03);
                border: 1px solid var(--border);
                border-radius: 18px;
                padding: 24px;
                height: 100%;
            }
            .custom-control-label {
                color: #e2e8f0;
                cursor: pointer;
            }
            label {
                color: #cbd5e1;
                font-weight: 600;
                margin-bottom: 8px;
            }
            .preview-card {
                background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
                border-radius: 20px;
                padding: 30px;
                border: 1px solid var(--border);
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <%@ include file="components/navBarComponent.jsp" %>
        
        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center flex-wrap mb-4">
                <div>
                    <h1 class="h3 font-weight-bold mb-1">Cài đặt trang chủ</h1>
                    <div class="text-muted">Quản lý nội dung hiển thị tại trang chủ (Hero section, Featured products).</div>
                </div>
                <div class="mt-3 mt-md-0">
                    <a href="manageStore" class="btn btn-secondary-custom mr-2">Quản lý cửa hàng</a>
                    <a href="home" class="btn btn-secondary-custom" target="_blank">Xem trang chủ <i class="fas fa-external-link-alt ml-1 small"></i></a>
                </div>
            </div>


            <div class="panel mt-4">
                <div class="panel-header">
                    <h2 class="panel-title mb-0"><i class="fas fa-sliders-h mr-2 text-warning"></i>Cấu hình nội dung</h2>
                </div>
                <div class="panel-body">
                    <form action="homeSetting" method="post">
                        <div class="row">
                            <div class="col-lg-6 mb-4">
                                <div class="setting-box">
                                    <h5 class="mb-4 text-warning"><i class="fas fa-rocket mr-2"></i>Hero Section</h5>
                                    <div class="form-group">
                                        <label>Nhãn nổi bật (Badge)</label>
                                        <input type="text" name="heroBadge" class="form-control" value="${homeSetting.heroBadge}" required placeholder="VD: SỰ KIỆN LỚN">
                                    </div>
                                    <div class="form-group">
                                        <label>Tiêu đề chính (Title)</label>
                                        <input type="text" name="heroTitle" class="form-control" value="${homeSetting.heroTitle}" required placeholder="VD: Phong cách của bạn">
                                    </div>
                                    <div class="form-group">
                                        <label>Điểm nhấn (Highlight)</label>
                                        <input type="text" name="heroHighlight" class="form-control" value="${homeSetting.heroHighlight}" placeholder="VD: V-SNKR Official">
                                    </div>
                                    <div class="form-group mb-0">
                                        <label>Mô tả chi tiết</label>
                                        <textarea name="heroDescription" class="form-control" rows="4" required placeholder="Nhập mô tả ngắn gọn về cửa hàng...">${homeSetting.heroDescription}</textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6 mb-4">
                                <div class="setting-box">
                                    <h5 class="mb-4 text-warning"><i class="fas fa-star mr-2"></i>Hành động & Nổi bật</h5>
                                    <div class="row">
                                        <div class="col-md-6 form-group">
                                            <label>Nút chính</label>
                                            <input type="text" name="primaryButtonText" class="form-control" value="${homeSetting.primaryButtonText}" required>
                                        </div>
                                        <div class="col-md-6 form-group">
                                            <label>Nút phụ</label>
                                            <input type="text" name="secondaryButtonText" class="form-control" value="${homeSetting.secondaryButtonText}">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label>Tiêu đề phần nổi bật</label>
                                        <input type="text" name="featuredTitle" class="form-control" value="${homeSetting.featuredTitle}" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Cách hiển thị sản phẩm nổi bật</label>
                                        <select name="featuredMode" class="custom-select">
                                            <option value="newest" ${homeSetting.featuredMode == 'newest' ? 'selected' : ''}>Mới nhất lên đầu</option>
                                            <option value="price_desc" ${homeSetting.featuredMode == 'price_desc' ? 'selected' : ''}>Giá cao nhất lên đầu</option>
                                            <option value="price_asc" ${homeSetting.featuredMode == 'price_asc' ? 'selected' : ''}>Giá thấp nhất lên đầu</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Ghim sản phẩm ưu tiên</label>
                                        <select name="featuredProductId" class="custom-select">
                                            <option value="">-- Không ghim sản phẩm --</option>
                                            <c:forEach items="${allProducts}" var="p">
                                                <option value="${p.id}" ${homeSetting.featuredProductId == p.id ? 'selected' : ''}>[ID: ${p.id}] ${p.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-row mt-4">
                                        <div class="col-4 text-center">
                                            <div class="custom-control custom-switch">
                                                <input type="checkbox" class="custom-control-input" id="showStats" name="showStats" ${homeSetting.showStats ? 'checked' : ''}>
                                                <label class="custom-control-label" for="showStats">Hiện stats</label>
                                            </div>
                                        </div>
                                        <div class="col-4 text-center">
                                            <div class="custom-control custom-switch">
                                                <input type="checkbox" class="custom-control-input" id="showFilterSidebar" name="showFilterSidebar" ${homeSetting.showFilterSidebar ? 'checked' : ''}>
                                                <label class="custom-control-label" for="showFilterSidebar">Hiện bộ lọc</label>
                                            </div>
                                        </div>
                                        <div class="col-4 text-center">
                                            <div class="custom-control custom-switch">
                                                <input type="checkbox" class="custom-control-input" id="showFeaturedSection" name="showFeaturedSection" ${homeSetting.showFeaturedSection ? 'checked' : ''}>
                                                <label class="custom-control-label" for="showFeaturedSection">Hiện nổi bật</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="d-flex justify-content-end mt-2">
                             <button type="submit" class="btn btn-primary-custom px-5">
                                 <i class="fas fa-save mr-2"></i>Lưu tất cả thay đổi
                             </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
