<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <fmt:setLocale value="vi_VN" />
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

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
                <title>${product.name} - Premium Shoe Store</title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link
                    href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <style>
                    :root {
                        --primary: #ea580c;
                        --bg: #0f172a;
                        --card-bg: rgba(255, 255, 255, 0.05);
                        --border: rgba(255, 255, 255, 0.1);
                    }

                    body {
                        background: var(--bg);
                        color: var(--text-main);
                        font-family: 'Be Vietnam Pro', sans-serif;
                    }

                    .detail-card {
                        background: var(--card-bg);
                        backdrop-filter: blur(12px);
                        border: 1px solid var(--border);
                        border-radius: 24px;
                        padding: 2.5rem;
                    }

                    .product-img {
                        border-radius: 20px;
                        border: 1px solid var(--border);
                        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
                    }

                    .price-large {
                        font-size: 2.5rem;
                        font-weight: 700;
                        color: var(--primary);
                    }

                    .btn-primary {
                        background: var(--primary);
                        border: 0;
                        border-radius: 12px;
                        padding: 1rem 2rem;
                        font-weight: 600;
                        font-size: 1.1rem;
                    }

                    .stock-info {
                        display: inline-block;
                        padding: 0.5rem 1rem;
                        background: rgba(255, 255, 255, 0.05);
                        border-radius: 10px;
                        font-size: 0.9rem;
                    }

                    .related-card {
                        background: var(--card-bg);
                        border: 1px solid var(--border);
                        border-radius: 18px;
                        transition: all 0.3s ease;
                    }

                    .related-card:hover {
                        transform: translateY(-5px);
                        border-color: var(--primary);
                    }
                </style>
                <script src="js/theme.js"></script>
                <link rel="stylesheet" href="css/theme.css">
            </head>

            <body class="bg-theme" style="background: var(--bg) !important;">
                <%@include file="components/navBarComponent.jsp" %>
                    <%@include file="components/toastNotification.jsp" %>
                        <div class="container py-5">
                            <div class="detail-card">
                                <div class="row">
                                    <div class="col-lg-6 mb-4 mb-lg-0">
                                        <img class="img-fluid product-img" src="${product.imageUrl}"
                                            alt="${product.name}">
                                    </div>
                                    <div class="col-lg-6 px-lg-5">
                                        <nav aria-label="breadcrumb">
                                            <ol class="breadcrumb bg-transparent p-0">
                                                <li class="breadcrumb-item"><a href="home"
                                                        class="text-muted small">Trang chủ</a></li>
                                                <li class="breadcrumb-item active text-white small" aria-current="page">
                                                    ${product.name}</li>
                                            </ol>
                                        </nav>
                                        <div class="text-warning text-uppercase small font-weight-bold mb-2">
                                            ${product.storeName}</div>
                                        <h1 class="display-4 font-weight-bold mb-3">${product.name}</h1>
                                        <div class="price-large mb-4">
                                            <fmt:formatNumber value="${product.price}" pattern="#,### đ" />
                                        </div>
                                        <p class="text-muted mb-4" style="font-size: 1.1rem; line-height: 1.7;">
                                            ${product.description}</p>

                                        <div class="mb-4">
                                            <div class="stock-info mr-3">
                                                <span class="text-muted mr-2">Kích Thước:</span> ${product.tiltle}
                                            </div>
                                            <div class="stock-info">
                                                <span class="text-muted mr-2">Trạng Thái</span>
                                                <span
                                                    class="${product.quantity > 0 ? 'text-success' : 'text-danger'} font-weight-bold">
                                                    ${product.quantity > 0 ? 'Có sẵn (' += product.quantity += ' đôi)' :
                                                    'Hết hàng'}
                                                </span>
                                            </div>
                                        </div>

                                        <a class="btn btn-primary btn-block shadow-lg ${product.quantity == 0 ? 'disabled' : ''}"
                                            href="add-to-cart?productId=${product.id}">
                                            <i class="fas fa-shopping-bag mr-2"></i> Thêm vào giỏ hàng
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <div class="mt-5">
                                <div class="row">
                                    <div class="col-lg-8">
                                        <h4 class="font-weight-bold mb-4"><i
                                                class="fas fa-comments text-warning mr-2"></i>Đánh giá từ khách hàng
                                        </h4>
                                        <c:forEach items="${listFeedbacks}" var="f">
                                            <div class="mb-4 p-4"
                                                style="background: rgba(255, 255, 255, 0.03); border: 1px solid var(--border); border-radius: 18px;">
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <div class="d-flex align-items-center">
                                                        <h6 class="font-weight-bold mb-0 mr-2">${f.userName}</h6>
                                                        <c:if test="${sessionScope.acc != null && f.accountId == sessionScope.acc.uid}">
                                                            <span class="badge badge-primary small">Của bạn</span>
                                                        </c:if>
                                                    </div>
                                                    <div class="text-warning small">
                                                        <c:forEach begin="1" end="${f.rating}"><i
                                                                class="fas fa-star"></i></c:forEach>
                                                        <c:forEach begin="${f.rating + 1}" end="5"><i
                                                                class="far fa-star"></i></c:forEach>
                                                    </div>
                                                </div>
                                                <p class="text-white small mb-2">${f.content}</p>
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <span class="text-muted" style="font-size: 0.7rem;">
                                                        <fmt:formatDate value="${f.createDate}"
                                                            pattern="dd/MM/yyyy HH:mm" />
                                                    </span>
                                                    <c:if test="${sessionScope.acc != null && f.accountId == sessionScope.acc.uid}">
                                                        <div class="small">
                                                            <button class="btn btn-link btn-sm text-info p-0 mr-2" 
                                                                    onclick="openEditFeedbackModal('${f.id}', '${f.rating}', '${f.content}')">
                                                                <i class="fas fa-edit mr-1"></i>Sửa
                                                            </button>
                                                            <%-- Customer cannot delete comment as per requirement --%>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        <c:if test="${empty listFeedbacks}">
                                            <div class="text-center py-4 text-muted">
                                                <i class="fas fa-comment-slash fa-2x mb-3"></i>
                                                <p>Chưa có đánh giá nào cho sản phẩm này.</p>
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="col-lg-4">
                                        <c:if test="${sessionScope.acc != null && sessionScope.acc.role == 'customer'}">
                                            <div class="p-4"
                                                style="background: rgba(234, 88, 12, 0.05); border: 1px dashed var(--primary); border-radius: 18px;">
                                                <h5 class="font-weight-bold mb-3">Viết đánh giá</h5>
                                                <form action="addFeedback" method="post" onsubmit="return validateFeedback()">
                                                    <input type="hidden" name="productId" value="${product.id}">
                                                    <input type="hidden" name="storeId" value="${product.storeId}">
                                                    <div class="form-group">
                                                        <label class="small text-muted">Số sao</label>
                                                        <select name="rating" class="form-control"
                                                            style="background: var(--bg); border: 1px solid var(--border); color: white;">
                                                            <option value="5">5 Sao (Tuyệt vời)</option>
                                                            <option value="4">4 Sao (Hài lòng)</option>
                                                            <option value="3">3 Sao (Bình thường)</option>
                                                            <option value="2">2 Sao (Kém)</option>
                                                            <option value="1">1 Sao (Rất tệ)</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <div class="d-flex justify-content-between">
                                                            <label class="small text-muted">Nội dung</label>
                                                            <span id="charCount" class="small text-muted">0/50</span>
                                                        </div>
                                                        <textarea name="content" id="feedbackContent" class="form-control" rows="3"
                                                            placeholder="Chia sẻ cảm nhận của bạn về sản phẩm..." maxlength="50"
                                                            oninput="updateCharCount()"
                                                            style="background: var(--bg); border: 1px solid var(--border); color: white;"></textarea>
                                                    </div>
                                                    <button type="submit" class="btn btn-primary btn-block btn-sm">Gửi
                                                        đánh giá</button>
                                                </form>
                                            </div>
                                        </c:if>
                                        <c:choose>
                                            <c:when test="${sessionScope.acc == null}">
                                                <div class="p-4 text-center"
                                                    style="background: rgba(255, 255, 255, 0.03); border: 1px solid var(--border); border-radius: 18px;">
                                                    <p class="small text-muted">Vui lòng đăng nhập để viết đánh giá.</p>
                                                    <a href="login" class="btn btn-outline-warning btn-sm">Đăng nhập ngay</a>
                                                </div>
                                            </c:when>
                                            <c:when test="${sessionScope.acc.role != 'customer'}">
                                                <div class="p-4 text-center"
                                                    style="background: rgba(255, 255, 255, 0.03); border: 1px solid var(--border); border-radius: 18px;">
                                                    <p class="small text-muted">Tài khoản nhân viên/quản trị không thể gửi đánh giá.</p>
                                                    <i class="fas fa-user-shield fa-2x text-warning opacity-50 mt-2"></i>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <div class="mt-5 pt-5">
                                <h3 class="font-weight-bold mb-4">Có thể bạn thích</h3>
                                <div class="row">
                                    <c:forEach items="${listLast}" var="L">
                                        <div class="col-md-6 col-lg-3 mb-4">
                                            <div class="card related-card h-100 p-2">
                                                <img class="card-img-top rounded" src="${L.imageUrl}" alt="${L.name}"
                                                    style="height: 180px; object-fit: cover;">
                                                <div class="card-body px-1">
                                                    <div class="font-weight-bold text-main text-truncate">${L.name}
                                                    </div>
                                                    <div class="text-warning mt-1">
                                                        <fmt:formatNumber value="${L.price}" pattern="#,### đ" />
                                                    </div>
                                                    <a class="btn btn-sm btn-outline-light btn-block mt-3"
                                                        href="detail?productId=${L.id}">Xem chi tiết</a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                        <div class="modal fade" id="editFeedbackModal" tabindex="-1" role="dialog" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content text-white" style="background: #1e293b; border-radius: 20px; border: 1px solid var(--border);">
                                    <div class="modal-header border-0">
                                        <h5 class="modal-title font-weight-bold">Chỉnh sửa đánh giá</h5>
                                        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <form action="editFeedback" method="post">
                                        <input type="hidden" name="feedbackId" id="editFeedbackId">
                                        <input type="hidden" name="productId" value="${product.id}">
                                        <div class="modal-body">
                                            <div class="form-group">
                                                <label class="small text-muted">Số sao</label>
                                                <select name="rating" id="editFeedbackRating" class="form-control"
                                                    style="background: var(--bg); border: 1px solid var(--border); color: white;">
                                                    <option value="5">5 Sao (Tuyệt vời)</option>
                                                    <option value="4">4 Sao (Hài lòng)</option>
                                                    <option value="3">3 Sao (Bình thường)</option>
                                                    <option value="2">2 Sao (Kém)</option>
                                                    <option value="1">1 Sao (Rất tệ)</option>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label class="small text-muted">Nội dung</label>
                                                <textarea name="content" id="editFeedbackContent" class="form-control" rows="4"
                                                    maxlength="50"
                                                    style="background: var(--bg); border: 1px solid var(--border); color: white;"></textarea>
                                            </div>
                                        </div>
                                        <div class="modal-footer border-0">
                                            <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal" style="border-radius: 10px;">Hủy</button>
                                            <button type="submit" class="btn btn-primary btn-sm px-4">Lưu thay đổi</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
                        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
                        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
                        <script>
                            function openEditFeedbackModal(id, rating, content) {
                                $('#editFeedbackId').val(id);
                                $('#editFeedbackRating').val(rating);
                                $('#editFeedbackContent').val(content);
                                $('#editFeedbackModal').modal('show');
                            }

                            function updateCharCount() {
                                const content = document.getElementById('feedbackContent').value;
                                document.getElementById('charCount').innerText = content.length + '/50';
                            }

                            function validateFeedback() {
                                const content = document.getElementById('feedbackContent').value;
                                if (content.length > 50) {
                                    alert('Nội dung đánh giá không được vượt quá 50 ký tự.');
                                    return false;
                                }
                                if (content.trim() === '') {
                                    alert('Vui lòng nhập nội dung đánh giá.');
                                    return false;
                                }
                                return true;
                            }
                        </script>
            </body>

            </html>