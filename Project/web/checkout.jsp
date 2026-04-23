<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Thanh Toán - V-SNKR" />
        <meta name="author" content="V-SNKR" />
        <title>Thanh Toán | V-SNKR</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

        <style>
            /* Đồng bộ biến màu sắc với toàn hệ thống */
            :root {
                --primary: #ea580c;
                --primary-dark: #c2410c;
                --bg: #0f172a;
                --card-bg: #1e293b;
                --glass: #0f172a;
                --border: rgba(255, 255, 255, 0.1);
                --vnpay-color: #38bdf8; /* Xanh dương sáng thân thiện với Dark Mode */
            }

            body {
                font-family: 'Be Vietnam Pro', sans-serif;
                background-color: var(--bg) !important;
                color: #f1f5f9;
            }

            /* Page Header */
            .checkout-header {
                background: linear-gradient(135deg, rgba(0, 0, 0, 0.4) 0%, #0f172a 100%);
                color: white;
                padding: 40px 0;
                margin-bottom: 40px;
                text-align: center;
                border-bottom: 1px solid var(--border);
            }

            .checkout-header h2 {
                font-weight: 800;
                text-transform: uppercase;
                letter-spacing: 2px;
                margin: 0;
            }
            
            .checkout-header h2 i {
                color: var(--primary);
            }

            /* Cards Glassmorphism */
            .checkout-card {
                background: var(--card-bg);
                backdrop-filter: none;
                border-radius: 20px;
                box-shadow: 0 10px 40px rgba(0,0,0,0.3);
                padding: 30px;
                margin-bottom: 30px;
                border: 1px solid var(--border);
            }

            .card-title-custom {
                font-weight: 700;
                text-transform: uppercase;
                border-bottom: 1px solid var(--border);
                padding-bottom: 15px;
                margin-bottom: 25px;
                color: white;
                letter-spacing: 0.5px;
            }
            
            .card-title-custom i {
                color: var(--primary);
            }

            /* Table Styles */
            .table {
                color: #f1f5f9;
            }
            
            .table th {
                border-top: none;
                text-transform: uppercase;
                font-size: 0.85rem;
                letter-spacing: 1px;
                color: #94a3b8;
                border-bottom: 1px solid var(--border);
            }

            .table td {
                vertical-align: middle;
                border-color: var(--border);
            }

            tr.border-bottom td, tr.border-bottom th {
                border-bottom: 1px solid var(--border) !important;
            }

            .product-img {
                width: 60px;
                height: 60px;
                object-fit: cover;
                background: rgba(0,0,0,0.3);
                border: 1px solid var(--border);
                border-radius: 8px;
                padding: 2px;
            }

            /* Form Styles */
            .form-label {
                font-weight: 600;
                color: #94a3b8;
                margin-bottom: 8px;
                font-size: 0.85rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .input-group-text {
                background-color: rgba(0,0,0,0.3);
                border: 1px solid var(--border);
                border-right: none;
                color: #94a3b8;
                border-radius: 8px 0 0 8px;
            }

            .form-control {
                background: rgba(0,0,0,0.2) !important;
                color: white !important;
                border-radius: 0 8px 8px 0;
                border: 1px solid var(--border);
                padding: 10px 15px;
                height: auto;
                transition: all 0.3s ease;
            }
            
            .form-control::placeholder {
                color: #64748b;
            }

            .form-control:focus {
                border-color: var(--primary);
                background: rgba(0,0,0,0.3) !important;
                box-shadow: none;
            }
            
            .input-group:focus-within .input-group-text {
                border-color: var(--primary);
                color: var(--primary);
            }

            textarea.form-control {
                border-radius: 8px; /* Vì textarea không dùng input-group */
            }

            /* Total Box */
            .total-box {
                background: #0f172a;
                border-radius: 12px;
                padding: 20px;
                margin-bottom: 25px;
                border: 1px dashed var(--border);
            }

            /* Buttons */
            .btn-cod {
                background-color: var(--primary);
                color: white;
                border: none;
                border-radius: 12px;
                font-weight: 700;
                text-transform: uppercase;
                padding: 12px;
                transition: all 0.3s ease;
                letter-spacing: 0.5px;
            }

            .btn-cod:hover {
                background-color: var(--primary-dark);
                color: white;
                box-shadow: 0 8px 20px rgba(234, 88, 12, 0.4);
                transform: translateY(-2px);
            }

            .btn-vnpay {
                background-color: transparent;
                color: var(--vnpay-color);
                border: 2px solid var(--vnpay-color);
                border-radius: 12px;
                font-weight: 700;
                text-transform: uppercase;
                padding: 12px;
                transition: all 0.3s ease;
                letter-spacing: 0.5px;
            }

            .btn-vnpay:hover {
                background-color: var(--vnpay-color);
                color: var(--bg);
                box-shadow: 0 8px 20px rgba(56, 189, 248, 0.3);
                transform: translateY(-2px);
            }
            
            .text-danger {
                color: #fbbf24 !important; /* Đổi màu tổng tiền sang vàng để nổi bật trên nền tối */
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <%@include file="components/navBarComponent.jsp" %>

        <header class="checkout-header">
            <div class="container">
                <h2><i class="fa-solid fa-shield-halved"></i> Thanh Toán An Toàn</h2>
            </div>
        </header>

        <section class="mb-5 pb-5">
            <div class="container" style="min-height: 60vh">
                <div class="row">
                    
                    <div class="col-lg-7 mb-4 mb-lg-0">
                        <div class="checkout-card">
                            <h4 class="card-title-custom"><i class="fa-solid fa-box-open mr-2"></i> Đơn Hàng Của Bạn</h4>
                            
                            <div class="table-responsive">
                                <table class="table table-borderless">
                                    <thead>
                                        <tr class="border-bottom">
                                            <th scope="col">SP</th>
                                            <th scope="col">Tên sản phẩm</th>
                                            <th scope="col" class="text-center">Đơn giá</th>
                                            <th scope="col" class="text-center">SL</th>
                                            <th scope="col" class="text-right">Tổng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${carts}" var="C">
                                            <tr class="border-bottom">
                                                <td>
                                                    <img src="${C.value.product.imageUrl}" class="product-img" alt="${C.value.product.name}" onerror="this.src='https://via.placeholder.com/60?text=No+Image'"/>
                                                </td>
                                                <td>
                                                    <p class="font-weight-bold mb-0 text-white">${C.value.product.name}</p>
                                                    <small style="color: #64748b;">Mã: #${C.value.product.id}</small>
<!--                                                    <br>
                                                    <small style="color: #94a3b8;">
                                                        Con ton kho: <span class="font-weight-bold text-warning">${C.value.product.quantity}</span>
                                                    </small>-->
                                                </td>                                      
                                                <td class="text-center align-middle"> <fmt:formatNumber value="${C.value.product.price}" pattern="#,### đ"/></td>
                                                <td class="text-center align-middle font-weight-bold" style="color: #94a3b8;">x${C.value.quantity}</td>
                                                <td class="text-right align-middle font-weight-bold text-danger"><fmt:formatNumber value="${C.value.product.price * C.value.quantity}" pattern="#,### đ"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-5">
                        <div class="checkout-card">
                            <h4 class="card-title-custom"><i class="fa-solid fa-truck-fast mr-2"></i> Thông Tin Giao Hàng</h4>
                            
                            <form action="checkout" method="POST">
                                <c:forEach items="${carts}" var="C">
                                    <input type="hidden" name="productId" value="${C.value.product.id}"/>
                                </c:forEach>

                                <div class="form-group mb-3">
                                    <label for="name" class="form-label">Họ và tên người nhận</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                                        </div>
                                        <input type="text" class="form-control" id="name" name="name" placeholder="Ví dụ: Nguyễn Văn A" value="${name}" required>
                                    </div>
                                </div>
                                
                                <div class="form-group mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                                        </div>
                                        <input type="text" class="form-control" id="phone" name="phone" pattern="^0[0-9]{9}$" title="Vui lòng nhập đúng 10 chữ số bắt đầu bằng số 0" placeholder="09xxxxxxx" value="${phone}" required>
                                    </div>
                                </div>
                                
                                <div class="form-group mb-3">
                                    <label for="address" class="form-label">Địa chỉ nhận hàng</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-location-dot"></i></span>
                                        </div>
                                        <input type="text" class="form-control" id="address" name="address" placeholder="Số nhà, đường, phường/xã, quận/huyện..." value="${address}" required>
                                    </div>
                                </div>
                                
                                <div class="form-group mb-4">
                                    <label for="note" class="form-label">Ghi chú đơn hàng <span style="color: #64748b; font-weight: normal; text-transform: none;">(Tùy chọn)</span></label>
                                    <textarea name="note" id="note" class="form-control" rows="3" placeholder="Ví dụ: Giao hàng giờ hành chính...">${note}</textarea>
                                </div>
                                
                                <div class="form-group mb-4">
                                    <label for="voucher" class="form-label">Chọn mã giảm giá <span style="color: #64748b; font-weight: normal; text-transform: none;">(1 mã cho sản phẩm từng shop)</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text"><i class="fa-solid fa-ticket"></i></span>
                                        </div>
                                        <select class="form-control" id="voucher" name="voucherCode">
                                            <option value="">-- Tự động chọn voucher tốt nhất --</option>
                                            <c:forEach items="${storeVouchers}" var="entry">
                                                <optgroup label="Sản phẩm từ Shop ID: ${entry.key}">
                                                    <c:forEach items="${entry.value}" var="v">
                                                        <option value="${v.code}" ${v.code == voucherCode ? 'selected' : ''}>
                                                            ${v.code} (Giảm ${v.discountPercent}%)
                                                        </option>
                                                    </c:forEach>
                                                </optgroup>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <c:if test="${not empty bestVouchers}">
                                        <small class="text-success mt-2 d-block">
                                            <i class="fas fa-magic mr-1"></i> Hệ thống đã tự động tìm thấy voucher tối ưu cho bạn!
                                        </small>
                                    </c:if>
                                </div>
                                
                                <div class="total-box">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted-custom">Tạm tính:</span>
                                        <span class="text-white"><fmt:formatNumber value="${totalMoney}" pattern="#,### đ"/></span>
                                    </div>
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted-custom">Giảm giá:</span>
                                        <span class="text-success">-<fmt:formatNumber value="${totalDiscount != null ? totalDiscount : 0}" pattern="#,### đ"/></span>
                                    </div>
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted-custom">VAT (10%):</span>
                                        <span class="text-white"><fmt:formatNumber value="${totalVat != null ? totalVat : (totalMoney * 0.10)}" pattern="#,### đ"/></span>
                                    </div>
                                    <hr style="border-color: var(--border);">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <p class="mb-0 text-uppercase font-weight-bold" style="color: #94a3b8; letter-spacing: 1px;">Tổng cộng</p>
                                        <h3 class="font-weight-bold text-danger mb-0">
                                            <fmt:formatNumber value="${finalTotal != null ? finalTotal : (totalMoney * 1.10)}" pattern="#,### đ"/>
                                        </h3>
                                    </div>
                                </div>
                                
                                <div class="d-grid gap-3">
                                    <button type="submit" formaction="checkout" class="btn btn-cod w-100 mb-3">
                                        <i class="fa-solid fa-money-bill-wave mr-2"></i> Thanh toán khi nhận hàng (COD)
                                    </button>
                                    <button type="submit" formaction="vnpay_pay" class="btn btn-vnpay w-100">
                                        <i class="fa-solid fa-qrcode mr-2"></i> Thanh toán qua VNPAY
                                    </button>
                                </div>
                                
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </section>

        <%@include file="components/footerComponent.jsp" %>
        
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>



