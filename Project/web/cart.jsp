<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Giỏ Hàng | V-SNKR</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            :root {
                --primary: #ea580c;
                --bg: #0f172a;
                --card-bg: #1e293b;
                --border: rgba(255, 255, 255, 0.1);
            }
            body {
                background: var(--bg);
                color: #f1f5f9;
                font-family: 'Be Vietnam Pro', sans-serif;
            }
            .cart-card {
                background: var(--card-bg);
                backdrop-filter: none;
                border: 1px solid var(--border);
                border-radius: 24px;
            }
            .table {
                color: #f1f5f9;
            }
            .table thead th {
                border-top: 0;
                border-bottom: 1px solid var(--border);
                text-transform: uppercase;
                font-size: 0.75rem;
                letter-spacing: 1px;
                color: #94a3b8;
            }
            .table td {
                border-bottom: 1px solid var(--border);
                vertical-align: middle;
            }
            .thumb {
                width: 80px;
                height: 80px;
                object-fit: cover;
                border-radius: 16px;
                border: 1px solid var(--border);
            }
            .btn-primary {
                background: var(--primary);
                border: 0;
                border-radius: 12px;
                font-weight: 600;
            }
            .btn-outline-light {
                border-radius: 12px;
                border: 1px solid var(--border);
                color: #94a3b8;
            }
            .form-control {
                background: rgba(0,0,0,0.2);
                border: 1px solid var(--border);
                color: white;
                border-radius: 10px;
            }
            .form-control:focus {
                background: rgba(0,0,0,0.3);
                border-color: var(--primary);
                color: white;
                box-shadow: none;
            }

            .product-suggestion-card {
                background: var(--card-bg);
                border: 1px solid var(--border);
                border-radius: 20px;
                transition: all 0.3s ease;
                overflow: hidden;
            }
            .product-suggestion-card:hover {
                transform: translateY(-5px);
                border-color: var(--primary);
            }
            .countdown {
                background: rgba(234, 88, 12, 0.1);
                color: #fb923c;
                padding: 4px 10px;
                border-radius: 8px;
                font-family: monospace;
                font-weight: 600;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <%@include file="components/navBarComponent.jsp" %>
        <div class="container py-5">
            <div class="d-flex align-items-center mb-4">
                <a href="home" class="btn btn-outline-light mr-3"><i class="fas fa-arrow-left"></i></a>
                <h2 class="font-weight-bold mb-0">Giỏ Hàng</h2>
            </div>


            <c:choose>
                <c:when test="${empty carts}">
                    <div class="text-center py-5 cart-card">
                        <i class="fas fa-shopping-basket fa-4x text-muted mb-4"></i>
                        <h4 class="text-muted">Chưa có sản phẩm</h4>
                        <a href="home" class="btn btn-primary mt-3">Bắt đầu mua sắm</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <div class="col-lg-8">
                            <div class="card cart-card mb-4">
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>Sản Phẩm</th>
                                                    <th>Giá</th>
                                                    <th>Hết hạn trong</th>
                                                    <th>Số lượng</th>
                                                    <th>Tổng</th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${carts}" var="item">
                                                    <tr>
                                                        <td>
                                                            <div class="d-flex align-items-center">
                                                                <img class="thumb mr-3" src="${item.value.product.imageUrl}" alt="${item.value.product.name}">
                                                                <div>
                                                                    <div class="font-weight-bold">${item.value.product.name}</div>
                                                                    <div class="small text-muted">${item.value.product.storeName}</div>
                                                                   
                                                                    <small style="color: #94a3b8;">
                                                                        Còn tồn kho: <span class="font-weight-bold text-warning">${item.value.product.quantity}</span>
                                                                    </small>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td class="text-nowrap"><fmt:formatNumber value="${item.value.product.price}" pattern="#,### đ"/></td>
                                                        <td><span class="countdown" data-expiry="${item.value.expiresAt}"></span></td>
                                                        <td style="width: 100px;">
                                                            <form action="update-quantity" method="post">
                                                                <input type="hidden" name="productId" value="${item.key}">
                                                                <input class="form-control form-control-sm" type="number" min="1" name="quantity" value="${item.value.quantity}" onchange="this.form.submit()">
                                                            </form>
                                                        </td>
                                                        <td class="font-weight-bold text-warning text-nowrap">
                                                            <fmt:formatNumber value="${item.value.quantity * item.value.product.price}" pattern="#,### đ"/>
                                                        </td>
                                                        <td><a class="text-danger" href="delete-cart?productId=${item.key}"><i class="fas fa-trash-alt"></i></a></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4">
                            <div class="card cart-card mb-4">
                                <div class="card-body">
                                    <h5 class="font-weight-bold mb-4">Đơn Hàng</h5>
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted">Tổng</span>
                                        <span><fmt:formatNumber value="${totalMoney}" pattern="#,###" />đ</span>
                                    </div>
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted">Shipping</span>
                                        <span class="text-success">FREE</span>
                                    </div>
                                    <hr style="border-color: var(--border);">
                                    <div class="d-flex justify-content-between align-items-center mb-4">
                                        <span class="h5 mb-0">Tổng tiền</span>
                                        <span class="h4 font-weight-bold text-warning mb-0"><fmt:formatNumber value="${totalMoney}" pattern="#,###" />đ</span>
                                    </div>
                                    <a class="btn btn-primary btn-block py-3 mb-3" href="checkout">
                                        Thanh Toán <i class="fas fa-arrow-right ml-2"></i>
                                    </a>

                                    <div class="mt-5">
                                        <h6 class="font-weight-bold mb-3 text-muted text-uppercase small">Gợi ý cho bạn</h6>
                                        <c:forEach items="${featuredProducts}" var="p" end="2">
                                            <div class="d-flex align-items-center mb-3 p-2 product-suggestion-card">
                                                <img src="${p.imageUrl}" style="width: 60px; height: 60px; object-fit: cover; border-radius: 12px;">
                                                <div class="ml-3 overflow-hidden">
                                                    <div class="text-truncate font-weight-bold small">${p.name}</div>
                                                    <div class="text-warning small font-weight-bold"><fmt:formatNumber value="${p.price}" pattern="#,###" />đ</div>
                                                    <a href="detail?productId=${p.id}" class="small text-muted text-decoration-none">Xem chi tiết</a>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <script>
            var expireRequestSent = false;

            function syncExpiredItems() {
                if (expireRequestSent) {
                    return;
                }
                expireRequestSent = true;
                fetch('expire-cart', {
                    method: 'POST',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                }).finally(function () {
                    window.location.reload();
                });
            }

            function renderCountdown() {
                document.querySelectorAll('.countdown').forEach(function (node) {
                    var expiry = Number(node.dataset.expiry || 0);
                    var diff = Math.max(0, expiry - Date.now());
                    var totalSeconds = Math.floor(diff / 1000);
                    var minutes = Math.floor(totalSeconds / 60);
                    var seconds = totalSeconds % 60;
                    node.textContent = minutes + 'm ' + String(seconds).padStart(2, '0') + 's';
                    if (diff === 0) {
                        syncExpiredItems();
                    }
                });
            }
            renderCountdown();
            setInterval(renderCountdown, 1000);
        </script>
    </body>
</html>



