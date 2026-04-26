<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Quản Lý Tin Tức | V-SNKR</title>
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            :root {
                --primary: #ea580c;
                --bg: #0f172a;
                --card-bg: rgba(255, 255, 255, 0.05);
                --border: rgba(255, 255, 255, 0.1);
            }
            body {
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: #f1f5f9;
                padding-bottom: 40px;
            }
            .admin-wrapper {
                background: var(--card-bg);
                backdrop-filter: blur(12px);
                border: 1px solid var(--border);
                border-radius: 20px;
                box-shadow: 0 10px 40px rgba(0,0,0,0.3);
                overflow: hidden;
            }
            .admin-header {
                background: rgba(0,0,0,0.2);
                padding: 25px 30px;
                border-bottom: 1px solid var(--border);
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .create-form {
                background-color: rgba(0,0,0,0.15);
                padding: 25px 30px;
                border-bottom: 1px solid var(--border);
            }
            .form-control {
                background: rgba(0,0,0,0.2) !important;
                border: 1px solid var(--border) !important;
                color: white !important;
                border-radius: 8px !important;
            }
            .form-control:focus {
                border-color: var(--primary) !important;
                box-shadow: 0 0 0 2px rgba(234,88,12,0.25) !important;
            }
            .custom-table {
                margin-bottom: 0;
                color: #f1f5f9;
            }
            .custom-table thead th {
                border-bottom: 1px solid var(--border);
                border-top: none;
                color: #94a3b8;
                padding: 15px 20px;
                background-color: rgba(0,0,0,0.2);
                text-transform: uppercase;
                font-size: 0.85rem;
            }
            .custom-table tbody td {
                padding: 15px 20px;
                vertical-align: middle;
                border-top: 1px solid var(--border);
            }
            .news-img {
                width: 80px;
                height: 50px;
                object-fit: cover;
                border-radius: 6px;
            }
            .btn-primary-custom {
                background-color: var(--primary);
                border: none;
                font-weight: 600;
                border-radius: 8px;
                color: white;
            }
            .btn-primary-custom:hover {
                background-color: #c2410c;
                color: white;
            }

            /* ✅ Modal đặc hoàn toàn - không trong suốt */
            .modal-content {
                background-color: #1e293b !important; /* Đặc, không trong suốt */
                color: #f1f5f9 !important;
                border: 1px solid rgba(255,255,255,0.15) !important;
                border-radius: 16px !important;
                box-shadow: 0 25px 60px rgba(0,0,0,0.8) !important;
            }
            .modal-header {
                background-color: #0f172a !important;
                border-bottom: 1px solid rgba(255,255,255,0.1) !important;
                border-radius: 16px 16px 0 0 !important;
                padding: 20px 24px !important;
            }
            .modal-body {
                background-color: #1e293b !important;
                padding: 24px !important;
            }
            .modal-footer {
                background-color: #0f172a !important;
                border-top: 1px solid rgba(255,255,255,0.1) !important;
                border-radius: 0 0 16px 16px !important;
                padding: 16px 24px !important;
            }
            .modal-title {
                color: #f1f5f9 !important;
                font-weight: 700;
            }
            .modal-body label {
                color: #cbd5e1 !important;
                font-weight: 600;
                margin-bottom: 6px;
            }
            .modal-body .form-control {
                background-color: #0f172a !important;
                border: 1px solid rgba(255,255,255,0.15) !important;
                color: #f1f5f9 !important;
            }
            .modal-body .form-control:focus {
                border-color: var(--primary) !important;
                box-shadow: 0 0 0 2px rgba(234,88,12,0.3) !important;
            }
            .close {
                color: #94a3b8 !important;
                opacity: 1 !important;
            }
            .close:hover {
                color: #f1f5f9 !important;
            }

            /* Badge visible/invisible */
            .badge-visible   {
                background: rgba(34,197,94,0.2);
                color: #4ade80;
                border: 1px solid rgba(34,197,94,0.3);
                padding: 5px 10px;
                border-radius: 999px;
                font-size: 0.8rem;
            }
            .badge-invisible {
                background: rgba(100,116,139,0.2);
                color: #94a3b8;
                border: 1px solid rgba(100,116,139,0.3);
                padding: 5px 10px;
                border-radius: 999px;
                font-size: 0.8rem;
            }

            /* Row mờ khi invisible */
            .row-invisible td {
                opacity: 0.5;
            }

            /* ✅ Điều chỉnh độ rộng cột */
            .custom-table th:nth-child(1), /* Ảnh */
            .custom-table td:nth-child(1) {
                width: 100px;
            }

            .custom-table th:nth-child(2), /* Tin tức */
            .custom-table td:nth-child(2) {
                width: auto;
                max-width: 350px;
            }

            .custom-table th:nth-child(3), /* Nguồn tin (nếu có) */
            .custom-table td:nth-child(3) {
                width: 120px;
            }

            .custom-table th:nth-child(4), /* Trạng thái */
            .custom-table td:nth-child(4) {
                width: 110px;
                white-space: nowrap;
            }

            .custom-table th:nth-child(5), /* Ngày đăng */
            .custom-table td:nth-child(5) {
                width: 150px;
            }

            .custom-table th:nth-child(6), /* Thao tác */
            .custom-table td:nth-child(6) {
                width: 110px;
                text-align: center;
            }

            .badge-visible, .badge-invisible {
                white-space: nowrap; /* ✅ Không xuống dòng */
                display: inline-block;
                min-width: 90px;
                text-align: center;
            </style>
            <script src="js/theme.js"></script>
            <link rel="stylesheet" href="css/theme.css">
        </head>
        <body class="bg-theme">
            <%@ include file="components/navBarComponent.jsp" %>
            <%@ include file="components/toastNotification.jsp" %>

            <div class="container mt-4">
                <div class="admin-wrapper">

                    <!-- Header -->
                    <div class="admin-header">
                        <h2><i class="fa-solid fa-newspaper mr-2 text-warning"></i> Quản Lý <b>Tin Tức</b></h2>
                        <c:choose>
                            <c:when test="${sessionScope.acc.role == 'admin'}">
                                <span class="badge badge-info p-2 px-3">Hệ thống</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-warning p-2 px-3">Cửa hàng</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Thông báo lỗi / thành công -->
                    <c:if test="${not empty sessionScope.error}">
                        <div class="alert alert-danger mx-4 mt-3" style="background:rgba(239,68,68,0.15);
                             color:#f87171;
                             border:1px solid rgba(239,68,68,0.3);
                             border-radius:10px;">
                            <i class="fa-solid fa-circle-exclamation mr-2"></i>${sessionScope.error}
                        </div>
                        <c:remove var="error" scope="session"/>
                    </c:if>

                    <!-- Form thêm tin -->
                    <div class="create-form">
                        <h6 class="mb-3 font-weight-bold text-warning"><i class="fa-solid fa-plus mr-1"></i> Đăng tin mới</h6>
                        <form action="managerNews" method="post">
                            <input type="hidden" name="action" value="add">
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label>Tiêu đề <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="title" required maxlength="200" placeholder="Nhập tiêu đề tin...">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label>Ảnh bìa (URL)</label>
                                    <input type="text" class="form-control" name="image" placeholder="https://...">
                                </div>
                                <div class="col-md-4 mb-3 d-flex align-items-end">
                                    <button type="submit" class="btn btn-primary-custom btn-block py-2">
                                        <i class="fa-solid fa-paper-plane mr-1"></i> Đăng tin ngay
                                    </button>
                                </div>
                                <div class="col-12">
                                    <label>Nội dung <span class="text-danger">*</span></label>
                                    <textarea class="form-control" name="content" rows="3" required placeholder="Nhập nội dung tin tức..."></textarea>
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- Bảng danh sách -->
                    <div class="table-responsive">
                        <table class="table custom-table">
                            <thead>
                                <tr>
                                    <th>Ảnh</th>
                                    <th>Tin tức</th>
                                        <c:if test="${sessionScope.acc.role == 'admin'}">
                                        <th>Nguồn tin</th>
                                        </c:if>
                                    <th>Trạng thái</th>
                                    <th>Ngày đăng</th>
                                    <th class="text-center">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${newsList}" var="n">
                                    <%-- ✅ Row mờ nếu invisible --%>
                                    <tr class="${n.visible ? '' : 'row-invisible'}">
                                        <td>
                                            <img src="${empty n.image ? 'https://via.placeholder.com/80x50?text=No+Image' : n.image}"
                                                 class="news-img"
                                                 onerror="this.src='https://via.placeholder.com/80x50?text=No+Image'">
                                        </td>
                                        <td>
                                            <div class="font-weight-bold">${n.title}</div>
                                            <div class="small text-muted text-truncate" style="max-width:400px;">${n.content}</div>
                                        </td>
                                        <c:if test="${sessionScope.acc.role == 'admin'}">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty n.storeId}">
                                                        <span class="badge badge-info">Hệ thống</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-warning">${n.storeName}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:if>
                                        <td>
                                            <%-- ✅ Hiển thị trạng thái Visible / Invisible --%>
                                            <c:choose>
                                                <c:when test="${n.visible}">
                                                    <span class="badge-visible"><i class="fa-solid fa-eye mr-1"></i>Hiển thị</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge-invisible"><i class="fa-solid fa-eye-slash mr-1"></i>Đã ẩn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${n.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </td>
                                        <td class="text-center">
                                            <%-- Nút Sửa --%>
                                            <%-- Thay thế Nút Sửa cũ bằng cái mới này --%>
                                            <button class="btn btn-sm btn-outline-info mr-2 edit-news-btn"
                                                    data-id="${n.id}"
                                                    data-title="<c:out value='${n.title}'/>"
                                                    data-image="<c:out value='${empty n.image ? "" : n.image}'/>"
                                                    data-content="<c:out value='${n.content}'/>">
                                                <i class="fa-solid fa-edit"></i>
                                            </button>

                                            <%-- ✅ Nút Toggle Visible/Invisible thay vì xóa --%>
                                            <form action="managerNews" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="toggleVisible">
                                                <input type="hidden" name="id" value="${n.id}">
                                                <button type="submit"
                                                        class="btn btn-sm ${n.visible ? 'btn-outline-secondary' : 'btn-outline-success'}"
                                                        title="${n.visible ? 'Ẩn bài viết' : 'Hiện bài viết'}">
                                                    <i class="fa-solid ${n.visible ? 'fa-eye-slash' : 'fa-eye'}"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty newsList}">
                                    <tr>
                                        <td colspan="6" class="text-center py-4 text-muted">Chưa có tin tức nào.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <%-- ✅ Edit Modal - giao diện đặc, không trong suốt --%>
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog">
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content">
                        <form action="managerNews" method="post">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="id" id="edit-id">
                            <div class="modal-header">
                                <h5 class="modal-title"><i class="fa-solid fa-pen-to-square mr-2 text-warning"></i>Sửa tin tức</h5>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Tiêu đề <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="title" id="edit-title" required maxlength="200">
                                </div>
                                <div class="form-group">
                                    <label>Ảnh bìa (URL)</label>
                                    <input type="text" class="form-control" name="image" id="edit-image" placeholder="https://...">
                                </div>
                                <div class="form-group mb-0">
                                    <label>Nội dung <span class="text-danger">*</span></label>
                                    <textarea class="form-control" name="content" id="edit-content" rows="6" required></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-primary-custom">
                                    <i class="fa-solid fa-floppy-disk mr-1"></i> Lưu thay đổi
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            <script>
                $(document).ready(function () {
                    // Dùng event delegation cho các nút được thêm động
                    $(document).on('click', '.edit-news-btn', function () {
                        var id = $(this).data('id');
                        var title = $(this).data('title');
                        var image = $(this).data('image');
                        var content = $(this).data('content');

                        $('#edit-id').val(id);
                        $('#edit-title').val(title);
                        $('#edit-image').val(image);
                        $('#edit-content').val(content);
                        $('#editModal').modal('show');
                    });
                });
            </script>
        </body>
    </html>
