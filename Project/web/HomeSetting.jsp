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
            .alert {
                border-radius: 14px;
                border: none;
            }
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
            /* ✅ Fix modal solid - không trong suốt */
            .modal-content {
                background: #1e293b !important;
                border: 1px solid rgba(255, 255, 255, 0.15) !important;
                border-radius: 20px !important;
                box-shadow: 0 25px 60px rgba(0, 0, 0, 0.8) !important;
            }

            .modal-backdrop {
                background-color: rgba(0, 0, 0, 0.75) !important;
            }

            .modal-backdrop.show {
                opacity: 1 !important;
            }

            /* ✅ Fix input trong modal solid */
            .modal .form-control,
            .modal textarea.form-control {
                background: #0f172a !important;
                border: 1px solid rgba(255, 255, 255, 0.15) !important;
                color: #f1f5f9 !important;
                border-radius: 10px !important;
            }

            .modal .form-control:focus,
            .modal textarea.form-control:focus {
                border-color: var(--primary) !important;
                box-shadow: 0 0 0 3px rgba(234, 88, 12, 0.2) !important;
            }

            .modal .form-control::placeholder {
                color: #475569 !important;
            }

            /* ✅ Fix label trong modal */
            .modal label {
                color: #94a3b8 !important;
                font-weight: 600;
                font-size: 0.875rem;
            }

            /* ✅ Fix modal header & footer */
            .modal .modal-header {
                padding: 20px 24px 12px !important;
                border-bottom: 1px solid rgba(255, 255, 255, 0.08) !important;
            }

            .modal .modal-footer {
                padding: 12px 24px 20px !important;
                border-top: 1px solid rgba(255, 255, 255, 0.08) !important;
            }

            .modal .modal-title {
                color: #f1f5f9 !important;
                font-weight: 700 !important;
            }

            .modal .close {
                color: #94a3b8 !important;
                opacity: 1 !important;
                text-shadow: none !important;
            }

            .modal .close:hover {
                color: #f87171 !important;
            }

            /* ✅ Fix custom-select trong modal */
            .modal .custom-select {
                background: #0f172a !important;
                border: 1px solid rgba(255, 255, 255, 0.15) !important;
                color: #f1f5f9 !important;
                border-radius: 10px !important;
            }

            /* ✅ Fix custom switch trong modal */
            .modal .custom-control-label {
                color: #cbd5e1 !important;
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
                        <input type="hidden" name="action" value="updateGeneral">
                        <div class="row">
                            <div class="col-lg-6 mb-4">
                                <!-- ... existing hero content ... -->
                                <div class="setting-box">
                                    <h5 class="mb-4 text-warning"><i class="fas fa-rocket mr-2"></i>Hero Section (Mặc định)</h5>
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
                                <i class="fas fa-save mr-2"></i>Lưu cấu hình chung
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Slider Management Section -->
            <div class="panel">
                <div class="panel-header d-flex justify-content-between align-items-center">
                    <h2 class="panel-title mb-0"><i class="fas fa-images mr-2 text-warning"></i>Quản lý Sliders (Carousel)</h2>
                    <button class="btn btn-primary-custom btn-sm" data-toggle="modal" data-target="#addSliderModal">
                        <i class="fas fa-plus mr-1"></i>Thêm slider mới
                    </button>
                </div>
                <div class="panel-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0 text-white" style="background: transparent;">
                            <thead style="background: rgba(0,0,0,0.1)">
                                <tr>
                                    <th class="border-0 pl-4">Ảnh</th>
                                    <th class="border-0">Tiêu đề & Mô tả</th>
                                    <th class="border-0">Trạng thái</th>
                                    <th class="border-0 text-right pr-4">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${sliders}" var="s">
                                    <tr style="border-bottom: 1px solid var(--border)">
                                        <td class="pl-4 align-middle">
                                            <img src="${s.imageUrl}" alt="Slider" style="width: 120px; height: 60px; object-fit: cover; border-radius: 8px; border: 1px solid var(--border);">
                                        </td>
                                        <td class="align-middle">
                                            <div class="font-weight-bold text-warning">${s.title}</div>
                                            <div class="small text-muted text-truncate" style="max-width: 300px;">${s.description}</div>
                                        </td>
                                        <td class="align-middle">
                                            <c:choose>
                                                <c:when test="${s.status}">
                                                    <span class="badge badge-success px-2 py-1">Đang hiển thị</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-secondary px-2 py-1">Đã ẩn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="pr-4 text-right align-middle">
                                            <button class="btn btn-sm btn-info mr-1" onclick="editSlider(${s.id}, '${s.title}', '${s.imageUrl}', '${s.backLink}', ${s.status}, '${s.description}')">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-sm btn-danger" onclick="confirmDeleteSlider(${s.id})">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty sliders}">
                                    <tr>
                                        <td colspan="4" class="text-center py-5 text-muted">Chưa có slider nào. Hệ thống sẽ hiển thị Hero section mặc định.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Slider Modal -->
        <div class="modal fade" id="addSliderModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-plus-circle text-warning mr-2"></i>Thêm Slider mới
                        </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="homeSetting" method="post">
                        <input type="hidden" name="action" value="addSlider">
                        <div class="modal-body p-4">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Tiêu đề slider</label>
                                        <input type="text" name="title" class="form-control" required placeholder="VD: Bộ sưu tập mùa hè">
                                    </div>
                                    <div class="form-group">
                                        <label>Đường dẫn ảnh (URL)</label>
                                        <input type="text" name="imageUrl" class="form-control" required placeholder="https://...">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Link khi click (Tuỳ chọn)</label>
                                        <input type="text" name="backLink" class="form-control" placeholder="#shop">
                                    </div>
                                    <div class="form-group mt-4 pt-2">
                                        <div class="custom-control custom-switch">
                                            <input type="checkbox" class="custom-control-input" id="newSliderStatus" name="status" checked>
                                            <label class="custom-control-label" for="newSliderStatus">Cho phép hiển thị ngay</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group mb-0">
                                        <label>Mô tả ngắn</label>
                                        <textarea name="description" class="form-control" rows="3" placeholder="Nhập mô tả cho slider..."></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary-custom" data-dismiss="modal">
                                <i class="fas fa-times mr-1"></i>Hủy
                            </button>
                            <button type="submit" class="btn btn-primary-custom">
                                <i class="fas fa-plus mr-1"></i>Xác nhận thêm
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Edit Slider Modal -->
        <div class="modal fade" id="editSliderModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-edit text-warning mr-2"></i>Chỉnh sửa Slider
                        </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="homeSetting" method="post">
                        <input type="hidden" name="action" value="updateSlider">
                        <input type="hidden" name="id" id="edit-id">
                        <div class="modal-body p-4">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Tiêu đề slider</label>
                                        <input type="text" name="title" id="edit-title" class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Đường dẫn ảnh (URL)</label>
                                        <input type="text" name="imageUrl" id="edit-image" class="form-control" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Link khi click</label>
                                        <input type="text" name="backLink" id="edit-link" class="form-control">
                                    </div>
                                    <div class="form-group mt-4 pt-2">
                                        <div class="custom-control custom-switch">
                                            <input type="checkbox" class="custom-control-input" id="edit-status" name="status">
                                            <label class="custom-control-label" for="edit-status">Hiển thị slider</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group mb-0">
                                        <label>Mô tả ngắn</label>
                                        <textarea name="description" id="edit-desc" class="form-control" rows="3"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary-custom" data-dismiss="modal">
                                <i class="fas fa-times mr-1"></i>Hủy
                            </button>
                            <button type="submit" class="btn btn-primary-custom">
                                <i class="fas fa-save mr-1"></i>Cập nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <form id="deleteSliderForm" action="homeSetting" method="post" style="display: none;">
            <input type="hidden" name="action" value="deleteSlider">
            <input type="hidden" name="id" id="delete-id">
        </form>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
                                                function editSlider(id, title, image, link, status, desc) {
                                                    $('#edit-id').val(id);
                                                    $('#edit-title').val(title);
                                                    $('#edit-image').val(image);
                                                    $('#edit-link').val(link);
                                                    $('#edit-status').prop('checked', status);
                                                    $('#edit-desc').val(desc);
                                                    $('#editSliderModal').modal('show');
                                                }

                                                function confirmDeleteSlider(id) {
                                                    if (confirm('Bạn có chắc chắn muốn xóa slider này?')) {
                                                        $('#delete-id').val(id);
                                                        $('#deleteSliderForm').submit();
                                                    }
                                                }
        </script>
    </body>
</html>

