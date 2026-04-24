<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Quản lý đánh giá | V-SNKR</title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link
                    href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <script src="js/theme.js"></script>
                <link rel="stylesheet" href="css/theme.css">
                <style>
                    .feedback-card {
                        background: var(--card-bg);
                        border: 1px solid var(--border);
                        border-radius: 20px;
                        padding: 24px;
                        transition: all 0.3s ease;
                        margin-bottom: 20px;
                    }

                    .feedback-card:hover {
                        border-color: var(--primary);
                        transform: translateY(-3px);
                    }

                    .rating-stars {
                        color: #facc15;
                        font-size: 0.9rem;
                    }

                    .user-avatar {
                        width: 50px;
                        height: 50px;
                        background: var(--primary);
                        border-radius: 50%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-weight: 700;
                        font-size: 1.2rem;
                        margin-right: 15px;
                        color: white;
                    }

                    .info-badge {
                        font-size: 0.75rem;
                        padding: 4px 12px;
                        border-radius: 10px;
                        background: var(--glass);
                        border: 1px solid var(--border);
                        color: var(--text-muted);
                        margin-right: 10px;
                    }

                    .btn-delete {
                        color: #f87171;
                        background: rgba(248, 113, 113, 0.1);
                        border: none;
                        border-radius: 10px;
                        padding: 8px 12px;
                    }

                    .btn-delete:hover {
                        background: #f87171;
                        color: white;
                    }

                    .empty-state {
                        text-align: center;
                        padding: 100px 0;
                        color: var(--text-muted);
                    }
                </style>
            </head>

            <body class="bg-theme">
                <%@ include file="components/navBarComponent.jsp" %>
                    <%@ include file="components/toastNotification.jsp" %>
                        <div class="container mt-5">
                            <div class="d-flex justify-content-between align-items-center mb-5">
                                <div>
                                    <h1 class="h2 font-weight-bold">Quản lý đánh giá khách hàng</h1>
                                    <c:choose>
                                        <c:when test="${feedbackScope == 'admin'}">
                                            <p class="text-muted mb-0">Hệ thống đang hiển thị tất cả phản hồi của người dùng.</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted mb-0">Cửa hàng: <span class="text-primary font-weight-bold">${store.name}</span></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="d-flex align-items-center">
                                    <a href="feedbacks?action=statistic" class="btn btn-outline-primary mr-2" style="border-radius: 12px;">
                                        <i class="fas fa-chart-pie mr-2"></i>Thống kê
                                    </a>
                                    <a href="exportFeedback" class="btn btn-success" style="border-radius: 12px;">
                                        <i class="fas fa-file-excel mr-2"></i>Xuất Excel
                                    </a>
                                </div>
                            </div>

                            <div class="mb-4">
                                <div class="btn-group" role="group">
                                    <a href="feedbacks" class="btn ${currentRating == -1 ? 'btn-primary' : 'btn-outline-primary'} btn-sm mr-2" style="border-radius: 10px;">Tất cả</a>
                                    <c:forEach begin="1" end="5" var="i">
                                        <a href="feedbacks?rating=${i}" class="btn ${currentRating == i ? 'btn-primary' : 'btn-outline-primary'} btn-sm mr-2" style="border-radius: 10px;">
                                            ${i} <i class="fas fa-star small"></i>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="row">
                                <c:forEach items="${listFeedbacks}" var="f">
                                    <div class="col-12">
                                        <div class="feedback-card">
                                            <div class="d-flex justify-content-between align-items-start">
                                                <div class="d-flex align-items-center">
                                                    <div class="user-avatar">${f.userName.substring(0,1).toUpperCase()}
                                                    </div>
                                                    <div>
                                                        <div class="d-flex align-items-center mb-1">
                                                            <h5 class="mb-0 font-weight-bold mr-3">${f.userName}</h5>
                                                            <span class="info-badge">
                                                                <i class="fas fa-store mr-1"></i> ${f.storeName}
                                                            </span>
                                                        </div>
                                                        <div class="rating-stars mb-1">
                                                            <c:forEach begin="1" end="${f.rating}">
                                                                <i class="fas fa-star"></i>
                                                            </c:forEach>
                                                            <c:forEach begin="${f.rating + 1}" end="5">
                                                                <i class="far fa-star opacity-50"></i>
                                                            </c:forEach>
                                                            <span class="text-muted ml-2 small">${f.createDate}</span>
                                                        </div>
                                                        <div class="d-flex align-items-center mt-2">
                                                            <a href="detail?productId=${f.productId}"
                                                                class="text-muted small text-decoration-none hover-primary">
                                                                <i class="fas fa-box-open mr-1"></i> Sản phẩm: <span
                                                                    class="text-primary">${f.productName}</span>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${!f.hidden}">
                                                            <a href="feedbacks?action=hide&id=${f.id}&status=true" class="btn btn-sm btn-outline-warning mr-2" style="border-radius: 10px;">
                                                                <i class="fas fa-eye-slash mr-1"></i> Ẩn
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="feedbacks?action=hide&id=${f.id}&status=false" class="btn btn-sm btn-outline-success mr-2" style="border-radius: 10px;">
                                                                <i class="fas fa-eye mr-1"></i> Hiện
                                                            </a>
                                                            <span class="badge badge-danger mr-2">Đã ẩn</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="mt-3" style="line-height: 1.6; color: var(--text-main);">
                                                <i class="fas fa-quote-left mr-2 opacity-20"></i> ${f.content}
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
                                                    href="feedbacks?page=${page - 1}"
                                                    style="background: var(--card-bg); color: var(--text-main); width: 45px; height: 45px;">
                                                    <i class="fas fa-chevron-left"></i>
                                                </a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPage}" var="i">
                                                <li class="page-item ${page == i ? 'active' : ''}">
                                                    <a class="page-link border-0 rounded-circle shadow-sm d-flex align-items-center justify-content-center ${page == i ? '' : 'text-muted'}"
                                                        href="feedbacks?page=${i}"
                                                        style="width: 45px; height: 45px; ${page == i ? 'background: var(--primary); color: white;' : 'background: var(--card-bg); color: var(--text-main);'}">
                                                        ${i}
                                                    </a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                                <a class="page-link border-0 rounded-circle shadow-sm d-flex align-items-center justify-content-center"
                                                    href="feedbacks?page=${page + 1}"
                                                    style="background: var(--card-bg); color: var(--text-main); width: 45px; height: 45px;">
                                                    <i class="fas fa-chevron-right"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>

                            <c:if test="${empty listFeedbacks}">
                                <div class="empty-state">
                                    <i class="far fa-comments fa-4x mb-4 opacity-20"></i>
                                    <h4>Chưa có đánh giá nào được gửi.</h4>
                                    <p>Các phản hồi từ khách hàng sẽ xuất hiện tại đây.</p>
                                </div>
                            </c:if>
                        </div>

                        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            </body>

            </html>


