<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
            <title>Quản Lý Giao Hàng | V-SNKR Admin</title>
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap"
                rel="stylesheet">
            <link rel="stylesheet"
                href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
                    padding-bottom: 40px;
                }

                .admin-wrapper {
                    background: var(--card-bg);
                    backdrop-filter: none;
                    border: 1px solid var(--border);
                    border-radius: 20px;
                    box-shadow: 0 10px 40px #0f172a;
                    overflow: hidden;
                }

                .admin-header {
                    background: #0f172a;
                    color: #fff;
                    padding: 25px 30px;
                    border-bottom: 1px solid var(--border);
                }

                .admin-header h2 {
                    margin: 0;
                    font-size: 1.5rem;
                    font-weight: 700;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                }

                .admin-header h2 i {
                    color: var(--primary);
                    margin-right: 10px;
                }

                .table-container {
                    padding: 30px;
                }

                .custom-table {
                    margin-bottom: 0;
                    color: #f1f5f9;
                }

                .custom-table thead th {
                    border-bottom: 1px solid var(--border);
                    border-top: none;
                    color: #94a3b8;
                    font-weight: 600;
                    padding: 15px 20px;
                    background-color: #0f172a;
                    text-transform: uppercase;
                    font-size: 0.85rem;
                }

                .custom-table tbody td {
                    padding: 20px;
                    vertical-align: middle;
                    border-color: var(--border);
                    border-bottom: 1px solid var(--border);
                    font-weight: 500;
                }

                .btn-custom-primary {
                    background-color: var(--primary);
                    color: white;
                    border: none;
                    border-radius: 8px;
                    font-weight: 600;
                }

                .btn-custom-secondary {
                    background-color: transparent;
                    color: #94a3b8;
                    border: 1px solid var(--border);
                    border-radius: 12px;
                    font-weight: 600;
                    padding: 10px 25px;
                    text-decoration: none;
                }

                .footer-actions {
                    background-color: rgba(0, 0, 0, 0.15);
                    padding: 20px 30px;
                    border-top: 1px solid var(--border);
                }

                .badge-status {
                    font-size: 0.85rem;
                    padding: 8px 15px;
                    border-radius: 30px;
                    font-weight: 600;
                }

                .badge-status.bg-success {
                    background-color: rgba(34, 197, 94, 0.15) !important;
                    color: #4ade80 !important;
                    border: 1px solid rgba(34, 197, 94, 0.2);
                }

                .badge-status.bg-warning {
                    background-color: rgba(251, 191, 36, 0.15) !important;
                    color: #fbbf24 !important;
                    border: 1px solid rgba(251, 191, 36, 0.2);
                }

                .badge-light-custom {
                    background-color: rgba(255, 255, 255, 0.1);
                    color: #e2e8f0;
                    border: 1px solid var(--border) !important;
                }

                .form-select {
                    background-color: #0f172a !important;
                    color: white !important;
                    border: 1px solid var(--border) !important;
                    border-radius: 8px;
                }

                .text-muted-custom {
                    color: #94a3b8 !important;
                }
            </style>
            <script src="js/theme.js"></script>
            <link rel="stylesheet" href="css/theme.css">
        </head>

        <body class="bg-theme">
            <%@ include file="components/navBarComponent.jsp" %>
                <%@ include file="components/toastNotification.jsp" %>
                    <div class="container">
                        <div class="admin-wrapper mb-4">
                            <div class="admin-header">
                                <h2><i class="fa-solid fa-truck-fast"></i> Quản Lý <b>Giao Hàng</b></h2>
                            </div>

                            <div class="table-container table-responsive">
                                <table class="table custom-table align-middle">
                                    <thead>
                                        <tr>
                                            <th>Mã Giao Hàng</th>
                                            <th>Họ Và Tên</th>
                                            <th>Số Điện Thoại</th>
                                            <th>Địa Chỉ</th>
                                            <th>Trạng Thái</th>
                                            <th>Quản lý ship hàng</th>
                                            <th class="text-center">Thao Tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><span class="badge badge-light-custom px-3 py-2">#${shipping.id}</span>
                                            </td>
                                            <td class="fw-bold text-white">${shipping.name}</td>
                                            <td><i
                                                    class="fa-solid fa-phone text-muted-custom me-2"></i>${shipping.phone}
                                            </td>
                                            <td><i
                                                    class="fa-solid fa-location-dot text-muted-custom me-2"></i>${shipping.address}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${shipping.status == 'Shipped'}">
                                                        <span class="badge bg-success badge-status">Đã Giao</span>
                                                    </c:when>
                                                    <c:when test="${shipping.status == 'Shipping'}">
                                                        <span class="badge bg-warning badge-status"
                                                            style="background-color: var(--primary) !important; color: white !important;">Đang
                                                            Giao</span>
                                                    </c:when>
                                                    <c:when test="${shipping.status == 'Cancelled'}">
                                                        <span class="badge bg-danger badge-status">Đã Hủy</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-warning badge-status">Đang Xử Lý</span>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                            <td>${empty shipping.shipperName ? 'Chưa gán' : shipping.shipperName}</td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${shipping.status == 'Shipped'}">
                                                        <span class="text-muted-custom small fw-bold">Đã khóa cập
                                                            nhật</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${sessionScope.acc.role == 'shipper'}">
                                                                <form action="shipping" method="post"
                                                                    class="d-flex justify-content-center align-items-center gap-2 mb-0">
                                                                    <input type="hidden" name="id"
                                                                        value="${shipping.id}">
                                                                    <input type="hidden" name="orderId"
                                                                        value="${orderId}">
                                                                    <select name="status"
                                                                        class="form-select form-select-sm w-auto fw-bold shadow-none text-warning">
                                                                        <option value="Pending"
                                                                            ${shipping.status=='Pending' ? 'selected'
                                                                            : '' }>Chờ xử lý</option>
                                                                        <option value="Shipping"
                                                                            ${shipping.status=='Shipping' ? 'selected'
                                                                            : '' }>Đang giao</option>
                                                                        <option value="Shipped"
                                                                            ${shipping.status=='Shipped' ? 'selected'
                                                                            : '' }>Đã giao</option>
                                                                    </select>

                                                                    <button type="submit"
                                                                        class="btn btn-custom-primary btn-sm px-3 py-1">Cập
                                                                        nhật</button>
                                                                </form>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted-custom small fw-bold">Chỉ
                                                                    Quản lý ship hàng được cập nhật</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div class="footer-actions d-flex justify-content-start">
                                <a href="orders" class="btn btn-custom-secondary">Quay lại đơn hàng</a>
                            </div>
                        </div>
                    </div>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>


