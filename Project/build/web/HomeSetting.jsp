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
            body {
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: var(--text-main);
                padding: 32px 0 48px;
            }
            .panel {
                background: var(--card-bg) !important;
                backdrop-filter: none !important;
                border: 1px solid var(--border);
                border-radius: 24px;
                box-shadow: 0 14px 40px rgba(0,0,0,0.1);
                overflow: hidden;
                margin-bottom: 24px;
            }
            .panel-header {
                padding: 24px 28px;
                border-bottom: 1px solid var(--border);
                background: rgba(0, 0, 0, 0.05) !important;
            }
            .btn-primary-custom {
                background: var(--primary) !important;
                border: none;
                border-radius: 12px;
                color: white !important;
                font-weight: 600;
                padding: 10px 24px;
                transition: all 0.3s ease;
            }
            .form-control, .custom-select {
                background: var(--bg) !important;
                border: 1px solid var(--border) !important;
                color: var(--text-main) !important;
                border-radius: 12px;
                opacity: 1 !important;
                height: 45px !important;
                padding: 8px 15px;
            }

            .custom-select {
                padding: 0 15px !important;
            }

            textarea.form-control {
                background: var(--bg) !important;
                border: 1px solid var(--border) !important;
                color: var(--text-main) !important;
                border-radius: 12px;
                opacity: 1 !important;
                height: auto !important;
                padding: 12px 15px;
            }
            
            .setting-box {
                background: var(--bg) !important;
                border: 1px solid var(--border);
                border-radius: 18px;
                padding: 24px;
                height: 100%;
            }
            .custom-control-label {
                color: var(--text-main);
            }
            label {
                color: var(--text-muted);
                font-weight: 600;
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
                        <table class="table table-hover mb-0">
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
                    <div class="modal-header border-0 pb-0">
                        <h5 class="modal-title text-white font-weight-bold">Thêm Slider mới</h5>
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
                                        <input type="text" name="title" class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Đường dẫn ảnh (URL)</label>
                                        <input type="text" name="imageUrl" class="form-control" required placeholder="https://...">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Link khi click (Optional)</label>
                                        <input type="text" name="backLink" class="form-control" placeholder="#shop">
                                    </div>
                                    <div class="form-group mt-4">
                                        <div class="custom-control custom-switch">
                                            <input type="checkbox" class="custom-control-input" id="newSliderStatus" name="status" checked>
                                            <label class="custom-control-label" for="newSliderStatus">Cho phép hiển thị ngay</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Mô tả ngắn</label>
                                        <textarea name="description" class="form-control" rows="3"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer border-0">
                            <button type="button" class="btn btn-secondary-custom" data-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary-custom">Xác nhận thêm</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Edit Slider Modal -->
        <div class="modal fade" id="editSliderModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header border-0 pb-0">
                        <h5 class="modal-title text-white font-weight-bold">Chỉnh sửa Slider</h5>
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
                                    <div class="form-group mt-4">
                                        <div class="custom-control custom-switch">
                                            <input type="checkbox" class="custom-control-input" id="edit-status" name="status">
                                            <label class="custom-control-label" for="edit-status">Hiển thị slider</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="form-group">
                                        <label>Mô tả ngắn</label>
                                        <textarea name="description" id="edit-desc" class="form-control" rows="3"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer border-0">
                            <button type="button" class="btn btn-secondary-custom" data-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary-custom">Cập nhật</button>
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




