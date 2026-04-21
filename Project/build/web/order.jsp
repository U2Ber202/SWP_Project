<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <title>Quản Lý Đơn Hàng | V-SNKR Admin</title>
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
                        --card-bg: rgba(255, 255, 255, 0.05);
                        --glass: rgba(255, 255, 255, 0.03);
                        --border: rgba(255, 255, 255, 0.1);
                    }

                    body {
                        font-family: 'Be Vietnam Pro', sans-serif;
                        background-color: var(--bg) !important;
                        color: #f1f5f9;
                        padding-top: 40px;
                        padding-bottom: 40px;
                    }

                    .admin-wrapper {
                        background: var(--card-bg);
                        backdrop-filter: blur(12px);
                        border: 1px solid var(--border);
                        border-radius: 20px;
                        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
                        overflow: hidden;
                    }

                    .admin-header {
                        background: rgba(0, 0, 0, 0.2);
                        color: #fff;
                        padding: 25px 30px;
                        border-bottom: 1px solid var(--border);
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
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

                    .btn-custom-secondary {
                        background-color: transparent;
                        color: #94a3b8;
                        border: 1px solid var(--border);
                        border-radius: 12px;
                        font-weight: 600;
                        padding: 10px 25px;
                        transition: all 0.3s ease;
                        text-decoration: none;
                        display: inline-flex;
                        align-items: center;
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
                        background-color: rgba(0, 0, 0, 0.2);
                        text-transform: uppercase;
                        font-size: 0.85rem;
                    }

                    .custom-table tbody td {
                        padding: 15px 20px;
                        vertical-align: middle;
                        border-color: var(--border);
                        border-bottom: 1px solid var(--border);
                        font-weight: 500;
                    }

                    .btn-action {
                        border-radius: 10px;
                        padding: 8px 15px;
                        font-size: 0.85rem;
                        font-weight: 600;
                        transition: all 0.3s ease;
                        border: 1px solid transparent;
                        display: inline-flex;
                        align-items: center;
                        text-decoration: none;
                    }

                    .btn-detail {
                        color: #38bdf8;
                        background-color: rgba(56, 189, 248, 0.1);
                        border-color: rgba(56, 189, 248, 0.2);
                    }

                    .btn-ship {
                        color: #4ade80;
                        background-color: rgba(74, 222, 128, 0.1);
                        border-color: rgba(74, 222, 128, 0.2);
                    }

                    .badge-light-custom {
                        background-color: rgba(255, 255, 255, 0.1);
                        color: #e2e8f0;
                        border: 1px solid var(--border) !important;
                    }

                    .text-danger-custom {
                        color: #fbbf24 !important;
                    }

                    .text-muted-custom {
                        color: #64748b !important;
                    }

                    .form-select,
                    .form-control {
                        background: rgba(0, 0, 0, 0.2) !important;
                        border: 1px solid var(--border) !important;
                        color: white !important;
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
                                    <h2><i class="fa-solid fa-boxes-packing"></i> Quản Lý <b>Đơn Hàng</b></h2>
                                    <a href="home" class="btn btn-custom-secondary">
                                        <i class="fa-solid fa-house me-2"></i> Trang Chủ
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table custom-table table-hover align-middle">
                                        <thead>
                                            <tr>
                                                <th>Mã Đơn</th>
                                                <th>Mã KH</th>
                                                <th>Tổng Tiền</th>
                                                <th>Ghi Chú</th>
                                                <th>Ngày Đặt</th>
                                                <th>Trạng Thái Vận Chuyển</th>
                                                <th>Shipper</th>
                                                <th class="text-center">Thao Tác</th>

                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${orders}" var="order">
                                                <c:set var="shipping" value="${shippingByOrderId[order.id]}" />
                                                <tr>
                                                    <td><span
                                                            class="badge badge-light-custom px-3 py-2 rounded-pill">#${order.id}</span>
                                                    </td>
                                                    <td><i
                                                            class="fa-solid fa-user text-muted-custom me-2"></i>${order.accountId}
                                                    </td>
                                                    <td class="text-danger-custom fw-bold">
                                                        <fmt:formatNumber value="${order.totalPrice}"
                                                            pattern="#,### đ" />
                                                    </td>
                                                    <td>${empty order.note ? 'Không có ghi chú' : order.note}</td>
                                                    <td><i
                                                            class="fa-regular fa-calendar text-muted-custom me-2"></i>${order.createdDate}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when
                                                                test="${shipping == null || shipping.status == 'Pending'}">
                                                                <span class="badge bg-secondary">Chờ xử lý</span>
                                                            </c:when>
                                                            <c:when test="${shipping.status == 'Shipped'}">
                                                                <span class="badge bg-success">Đã giao</span>
                                                            </c:when>
                                                            <c:when test="${shipping.status == 'Shipping'}">
                                                                <span class="badge bg-warning text-dark">Đang
                                                                    giao</span>
                                                            </c:when>
                                                            <c:when test="${shipping.status == 'Cancelled'}">
                                                                <span class="badge bg-danger">Đã hủy</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-info">${shipping.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </td>
                                                    <td>
                                                        <c:if test="${sessionScope.acc.role == 'owner'}">
                                                            <c:choose>
                                                                <c:when
                                                                    test="${shipping != null and shipping.status == 'Shipped'}">
                                                                    <span class="text-muted-custom">Đã giao, khoá gán
                                                                        shipper</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form action="orders" method="post"
                                                                        class="d-flex gap-2 align-items-center mb-0">
                                                                        <input type="hidden" name="action"
                                                                            value="assignShipper">
                                                                        <input type="hidden" name="orderId"
                                                                            value="${order.id}">
                                                                        <select name="shipperId"
                                                                            class="form-select form-select-sm">
                                                                            <c:forEach items="${storeShippers}"
                                                                                var="sp">
                                                                                <option value="${sp.uid}" ${shipping
                                                                                    !=null && shipping.shipperId==sp.uid
                                                                                    ? 'selected' : '' }>${sp.user}
                                                                                </option>
                                                                            </c:forEach>
                                                                        </select>
                                                                        <button class="btn btn-sm btn-ship"
                                                                            type="submit">Gán</button>
                                                                    </form>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${sessionScope.acc.role == 'shipper'}">
                                                            <span class="text-muted-custom">Đơn đã được gán cho
                                                                bạn</span>
                                                        </c:if>
                                                    </td>
                                                    <td class="text-center">
                                                        <div class="d-flex justify-content-center gap-2">
                                                            <a class="btn btn-action btn-detail"
                                                                href="orderdetail?orderId=${order.id}">Chi tiết</a>
                                                            <a class="btn btn-action btn-ship"
                                                                href="shipping?orderId=${order.id}">Giao hàng</a>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <c:if test="${empty orders}">
                                        <div class="text-center py-5">
                                            <i class="fa-solid fa-box-open mb-3"
                                                style="font-size: 3rem; color: #475569;"></i>
                                            <h5 style="color: #94a3b8;">Hiện tại chưa có đơn hàng nào.</h5>
                                        </div>
                                    </c:if>
                                </div>
                                <c:if test="${totalPage >= 1}">
                                    <div class="card-footer border-top-0 bg-transparent pb-4 mt-3">
                                        <nav aria-label="Page navigation">
                                            <ul class="pagination justify-content-center mb-0">
                                                <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                                                    <a class="page-link" href="orders?page=${page - 1}">Trước</a>
                                                </li>
                                                <c:forEach begin="1" end="${totalPage}" var="i">
                                                    <li class="page-item ${page == i ? 'active' : ''}">
                                                        <a class="page-link" href="orders?page=${i}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                                <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                                    <a class="page-link" href="orders?page=${page + 1}">Sau</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>