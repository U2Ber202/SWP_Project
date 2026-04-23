<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Quản lý Voucher | V-SNKR</title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link
                    href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <script src="js/theme.js"></script>
                <link rel="stylesheet" href="css/theme.css">
                <style>
                    .voucher-card {
                        background: var(--card-bg);
                        border: 2px dashed var(--border);
                        border-radius: 20px;
                        padding: 30px;
                        position: relative;
                        transition: all 0.3s ease;
                        height: 100%;
                    }

                    .voucher-card:hover {
                        border-color: var(--primary);
                        transform: translateY(-5px);
                    }

                    .voucher-card::before,
                    .voucher-card::after {
                        content: '';
                        position: absolute;
                        top: 50%;
                        width: 20px;
                        height: 40px;
                        background: var(--bg);
                        transform: translateY(-50%);
                        z-index: 2;
                        transition: background 0.3s ease;
                    }

                    .voucher-card::before {
                        left: -12px;
                        border-radius: 0 20px 20px 0;
                        border: 1px solid var(--border);
                        border-left: none;
                    }

                    .voucher-card::after {
                        right: -12px;
                        border-radius: 20px 0 0 20px;
                        border: 1px solid var(--border);
                        border-right: none;
                    }

                    .discount-val {
                        font-size: 2.2rem;
                        font-weight: 800;
                        color: var(--primary);
                    }

                    .btn-add {
                        background: linear-gradient(135deg, var(--primary), #fb923c);
                        border: none;
                        border-radius: 12px;
                        padding: 12px 30px;
                        font-weight: 700;
                        box-shadow: 0 10px 20px rgba(234, 88, 12, 0.3);
                    }

                    .modal-content.opaque-form {
                        background-color: #1e293b !important; /* Solid dark color */
                        border: 2px solid #ea580c !important; /* Solid border */
                        opacity: 1 !important;
                        backdrop-filter: none !important;
                        -webkit-backdrop-filter: none !important;
                        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5) !important;
                    }

                    .form-control {
                        background: #0f172a !important; /* Solid dark color */
                        border: 1px solid var(--border);
                        color: var(--text-main) !important;
                        border-radius: 10px;
                        opacity: 1 !important;
                    }

                    [data-theme="light"] .form-control {
                        background: #ffffff !important;
                        color: #0f172a !important;
                    }

                    [data-theme="light"] .modal-content {
                        background: #ffffff !important;
                        backdrop-filter: none !important;
                    }

                    .form-control:focus {
                        background: #0f172a !important;
                        border-color: var(--primary);
                        color: var(--text-main) !important;
                    }

                    .action-buttons {
                        gap: 8px;
                    }

                    .btn-action {
                        min-width: 42px;
                    }

                    .store-badge {
                        background: rgba(234, 88, 12, 0.18);
                        color: #fdba74;
                        border: 1px solid rgba(251, 146, 60, 0.3);
                    }
                </style>
            </head>

            <body class="bg-theme">
                <%@ include file="components/navBarComponent.jsp" %>
                    <%@ include file="components/toastNotification.jsp" %>

                        <div class="container mt-5">
                            <div class="d-flex justify-content-between align-items-center mb-5">
                                <div>
                                    <h1 class="h2 font-weight-bold">Quản lý mã giảm giá</h1>
                                    <c:choose>
                                        <c:when test="${voucherScope == 'admin'}">
                                            <p class="text-muted mb-0">Admin có thể quản lý toàn bộ voucher của các cửa
                                                hàng.</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted mb-0">Cửa hàng: <span
                                                    class="text-white">${store.name}</span></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <button class="btn btn-add text-white" data-toggle="modal"
                                    data-target="#addVoucherModal">
                                    <i class="fas fa-plus mr-2"></i>Tạo mã mới
                                </button>
                            </div>

                            <div class="row">
                                <c:forEach items="${listVouchers}" var="v">
                                    <div class="col-md-6 mb-4">
                                        <div class="voucher-card">
                                            <div class="row align-items-center">
                                                <div class="col-4 text-center border-right">
                                                    <div class="discount-val">${v.discountPercent}%</div>
                                                    <div class="small font-weight-bold">GIẢM GIÁ</div>
                                                </div>
                                                <div class="col-8 pl-4">
                                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                                        <h4 class="h5 font-weight-bold mb-0">${v.code}</h4>
                                                        <c:if test="${voucherScope == 'admin'}">
                                                            <span
                                                                class="badge store-badge px-3 py-2">${v.storeName}</span>
                                                        </c:if>
                                                    </div>
                                                    <p class="small text-muted mb-1">Thời gian: ${v.startDate} đến ${v.expiryDate}</p>
                                                    <p class="small text-muted mb-1">Tối thiểu:
                                                        <c:choose>
                                                            <c:when test="${v.minOrderValue != null}">
                                                                <fmt:formatNumber value="${v.minOrderValue}"
                                                                    pattern="#,###" /> đ
                                                            </c:when>
                                                            <c:otherwise>Không giới hạn</c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <p class="small text-muted mb-3">Giảm tối đa:
                                                        <c:choose>
                                                            <c:when test="${v.maxDiscount != null}">
                                                                <fmt:formatNumber value="${v.maxDiscount}"
                                                                    pattern="#,###" /> đ
                                                            </c:when>
                                                            <c:otherwise>Không giới hạn</c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <c:choose>
                                                            <c:when test="${v.expiryDate < today}">
                                                                <span class="badge badge-danger px-3 py-2">Expired</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-success px-3 py-2">Active</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <div class="d-flex action-buttons">
                                                            <button type="button"
                                                                class="btn btn-sm btn-outline-warning btn-action"
                                                                data-toggle="modal" data-target="#editVoucherModal"
                                                                data-id="${v.id}" data-code="${v.code}"
                                                                data-discount="${v.discountPercent}"
                                                                data-min-order="${v.minOrderValue}"
                                                                data-max-discount="${v.maxDiscount}"
                                                                data-expiry="${v.expiryDate}"
                                                                data-start="${v.startDate}"
                                                                data-store-id="${v.storeId}">
                                                                <i class="fas fa-pen"></i>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <c:if test="${totalPage >= 1}">
                                <div class="d-flex justify-content-center mt-5 mb-4">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination pagination-lg mb-0" style="gap: 10px;">
                                            <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                                                <a class="page-link border-0 rounded-circle shadow-sm d-flex align-items-center justify-content-center"
                                                    href="vouchers?page=${page - 1}"
                                                    style="background: var(--card-bg); color: var(--text-main); width: 45px; height: 45px;">
                                                    <i class="fas fa-chevron-left"></i>
                                                </a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPage}" var="i">
                                                <li class="page-item ${page == i ? 'active' : ''}">
                                                    <a class="page-link border-0 rounded-circle shadow-sm d-flex align-items-center justify-content-center ${page == i ? '' : 'text-muted'}"
                                                        href="vouchers?page=${i}"
                                                        style="width: 45px; height: 45px; ${page == i ? 'background: var(--primary); color: white;' : 'background: var(--card-bg); color: var(--text-main);'}">
                                                        ${i}
                                                    </a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                                <a class="page-link border-0 rounded-circle shadow-sm d-flex align-items-center justify-content-center"
                                                    href="vouchers?page=${page + 1}"
                                                    style="background: var(--card-bg); color: var(--text-main); width: 45px; height: 45px;">
                                                    <i class="fas fa-chevron-right"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>

                            <c:if test="${empty listVouchers}">
                                <div class="text-center py-5">
                                    <i class="fas fa-ticket-alt fa-4x text-muted opacity-20 mb-4"></i>
                                    <h4 class="text-muted">Chưa có mã giảm giá nào được tạo.</h4>
                                </div>
                            </c:if>
                        </div>

                        <div class="modal fade" id="addVoucherModal" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content opaque-form">
                                    <form action="vouchers" method="post">
                                        <input type="hidden" name="action" value="add">
                                        <div class="modal-header border-0">
                                            <h5 class="modal-title font-weight-bold">Tạo voucher mới</h5>
                                            <button type="button" class="close text-white"
                                                data-dismiss="modal">&times;</button>
                                        </div>
                                        <div class="modal-body">
                                            <c:if test="${voucherScope == 'admin'}">
                                                <div class="form-group">
                                                    <label>Cửa hàng</label>
                                                    <select name="storeId" class="form-control" required>
                                                        <option value="">-- Chọn cửa hàng --</option>
                                                        <c:forEach items="${allStores}" var="s">
                                                            <option value="${s.id}">${s.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label>Mã code (VD: GIAM20)</label>
                                                <input type="text" name="code" class="form-control" required>
                                            </div>
                                            <div class="form-group">
                                                <label>Phần trăm giảm (%)</label>
                                                <input type="number" name="discountPercent" class="form-control" min="1"
                                                    max="100" required>
                                            </div>
                                            <div class="form-group">
                                                <label>Đơn tối thiểu (đ)</label>
                                                <input type="number" name="minOrderValue" class="form-control" min="1"
                                                    placeholder="Để trống nếu không có">
                                            </div>
                                            <div class="form-group">
                                                <label>Giảm tối đa (đ)</label>
                                                <input type="number" name="maxDiscount" class="form-control" min="1"
                                                    placeholder="Để trống nếu không có">
                                            </div>
                                            <div class="row">
                                                <div class="col-6">
                                                    <div class="form-group">
                                                        <label>Ngày bắt đầu</label>
                                                        <input type="date" name="startDate" class="form-control" required>
                                                    </div>
                                                </div>
                                                <div class="col-6">
                                                    <div class="form-group">
                                                        <label>Ngày hết hạn</label>
                                                        <input type="date" name="expiryDate" class="form-control" required>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer border-0">
                                            <button type="button" class="btn btn-outline-light"
                                                data-dismiss="modal">Hủy</button>
                                            <button type="submit" class="btn btn-add text-white">Xác nhận tạo</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade" id="editVoucherModal" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content opaque-form">
                                    <form action="vouchers" method="post">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="id" id="editVoucherId">
                                        <div class="modal-header border-0">
                                            <h5 class="modal-title font-weight-bold">Cập nhật voucher</h5>
                                            <button type="button" class="close text-white"
                                                data-dismiss="modal">&times;</button>
                                        </div>
                                        <div class="modal-body">
                                            <c:if test="${voucherScope == 'admin'}">
                                                <div class="form-group">
                                                    <label>Cua hang</label>
                                                    <select name="storeId" id="editVoucherStoreId" class="form-control"
                                                        required>
                                                        <option value="">-- Chon cua hang --</option>
                                                        <c:forEach items="${allStores}" var="s">
                                                            <option value="${s.id}">${s.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label>Ma code</label>
                                                <input type="text" name="code" id="editVoucherCode" class="form-control"
                                                    required>
                                            </div>
                                            <div class="form-group">
                                                <label>Phan tram giam (%)</label>
                                                <input type="number" name="discountPercent" id="editVoucherDiscount"
                                                    class="form-control" min="1" max="100" required>
                                            </div>
                                            <div class="form-group">
                                                <label>Don toi thieu (d)</label>
                                                <input type="number" name="minOrderValue" id="editVoucherMinOrder"
                                                    class="form-control" min="1" placeholder="De trong neu khong co">
                                            </div>
                                            <div class="form-group">
                                                <label>Giam toi da (d)</label>
                                                <input type="number" name="maxDiscount" id="editVoucherMaxDiscount"
                                                    class="form-control" min="1" placeholder="De trong neu khong co">
                                            </div>
                                            <div class="row">
                                                <div class="col-6">
                                                    <div class="form-group">
                                                        <label>Ngày bắt đầu</label>
                                                        <input type="date" name="startDate" id="editVoucherStart"
                                                            class="form-control" required>
                                                    </div>
                                                </div>
                                                <div class="col-6">
                                                    <div class="form-group">
                                                        <label>Ngày hết hạn</label>
                                                        <input type="date" name="expiryDate" id="editVoucherExpiry"
                                                            class="form-control" required>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer border-0">
                                            <button type="button" class="btn btn-outline-light"
                                                data-dismiss="modal">Hủy</button>
                                            <button type="submit" class="btn btn-add text-white">Lưu thay đổi</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
                        <script>
                            $('#editVoucherModal').on('show.bs.modal', function (event) {
                                var button = $(event.relatedTarget);
                                $('#editVoucherId').val(button.data('id'));
                                $('#editVoucherCode').val(button.data('code'));
                                $('#editVoucherDiscount').val(button.data('discount'));
                                $('#editVoucherMinOrder').val(button.data('min-order') || '');
                                $('#editVoucherMaxDiscount').val(button.data('max-discount') || '');
                                $('#editVoucherExpiry').val(button.data('expiry'));
                                $('#editVoucherStart').val(button.data('start'));
                                if ($('#editVoucherStoreId').length) {
                                    $('#editVoucherStoreId').val(button.data('store-id'));
                                }
                            });
                        </script>
            </body>

            </html>