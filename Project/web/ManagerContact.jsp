<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Quản Lý Liên Hệ | V-SNKR</title>
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
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
                overflow: hidden;
            }
            .admin-header {
                background: rgba(0, 0, 0, 0.2);
                padding: 25px 30px;
                border-bottom: 1px solid var(--border);
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .custom-table {
                margin-bottom: 0;
                color: #f1f5f9;
            }
            .custom-table thead th {
                border-bottom: 1px solid var(--border);
                color: #94a3b8;
                padding: 15px 20px;
                background-color: rgba(0, 0, 0, 0.2);
                text-transform: uppercase;
                font-size: 0.85rem;
            }
            .custom-table tbody td {
                padding: 15px 20px;
                vertical-align: middle;
                border-top: 1px solid var(--border);
            }
            .status-badge {
                padding: 5px 12px;
                border-radius: 999px;
                font-size: 0.75rem;
                font-weight: 700;
            }
            .status-pending {
                background: rgba(234, 88, 12, 0.2);
                color: #fb923c;
            }
            .status-replied {
                background: rgba(34, 197, 94, 0.2);
                color: #4ade80;
            }
            .bg-orange {
                background-color: var(--primary) !important;
            }
            .modal-content {
                background: #1e293b !important;
                border: 1px solid rgba(255,255,255,0.1) !important;
                border-radius: 20px !important;
            }

            .modal-header {
                background: #0f172a !important;
                border-bottom: 1px solid rgba(255,255,255,0.1) !important;
                border-radius: 20px 20px 0 0 !important;
            }

            .modal-body {
                background: #1e293b !important;
            }

            .modal-footer {
                background: #0f172a !important;
                border-top: 1px solid rgba(255,255,255,0.1) !important;
                border-radius: 0 0 20px 20px !important;
            }

            /* ✅ Input trong modal - nền đặc */
            .modal-body .form-control {
                background: #0f172a !important;
                color: #f1f5f9 !important;
                border: 1px solid rgba(255,255,255,0.15) !important;
                border-radius: 12px !important;
            }

            .modal-body .form-control:focus {
                border-color: var(--primary) !important;
                box-shadow: 0 0 0 2px rgba(234,88,12,0.3) !important;
            }

            .modal-body .form-control::placeholder {
                color: #64748b !important;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body class="bg-theme">
        <%@ include file="components/navBarComponent.jsp" %>
        <div class="container mt-4">
            <div class="admin-wrapper">
                <div class="admin-header">
                    <h2><i class="fa-solid fa-headset mr-2 text-warning"></i> Hỗ trợ <b>Đơn hàng</b></h2>
                    <c:if test="${sessionScope.acc.role == 'admin'}">
                        <span class="badge badge-info p-2 px-3">Tất cả cửa hàng</span>
                    </c:if>
                    <c:if test="${sessionScope.acc.role == 'owner'}">
                        <span class="badge badge-warning p-2 px-3">${not empty storeName ? storeName : 'Cửa hàng của tôi'}</span>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger ml-3 mb-0 py-1" style="font-size: 0.9rem;">${error}</div>
                    </c:if>
                </div>

                <div class="table-responsive">
                    <table class="table custom-table">
                        <thead>
                            <tr>
                                <th>Đơn hàng</th>
                                <th>Khách hàng</th>
                                    <c:if test="${sessionScope.acc.role == 'admin'}">
                                    <th>Cửa hàng</th>
                                    </c:if>
                                <th>Nội dung liên hệ</th>
                                <th>Trạng thái</th>
                                <th class="text-center">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${contactList}" var="c">
                                <tr>
                                    <td><b>#${c.orderId}</b></td>
                                    <td>${c.accountName}</td>
                                    <c:if test="${sessionScope.acc.role == 'admin'}">
                                        <td>${c.storeName}</td>
                                    </c:if>
                                    <td>
                                        <div class="small text-muted"><fmt:formatDate value="${c.createdAt}" pattern="dd/MM/yyyy HH:mm"/></div>
                                        <div style="max-width: 300px;">${c.message}</div>
                                    </td>
                                    <td>
                                        <span class="status-badge ${c.status == 'Đã phản hồi' ? 'status-replied' : 'status-pending'}">
                                            ${c.status}
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <c:if test="${c.status == 'Chờ xử lý'}">
                                            <button class="btn btn-sm btn-primary mr-1 btn-reply" 
                                                    data-id="${c.id}" 
                                                    data-message="<c:out value='${c.message}'/>" 
                                                    title="Phản hồi khách">
                                                <i class="fa-solid fa-reply"></i>
                                            </button>
                                        </c:if>
                                        <c:if test="${not empty c.responseMessage}">
                                            <button class="btn btn-sm btn-outline-info mr-1 btn-view-reply" 
                                                    data-message="<c:out value='${c.message}'/>" 
                                                    data-response="<c:out value='${c.responseMessage}'/>" 
                                                    title="Xem phản hồi">
                                                <i class="fa-solid fa-eye"></i>
                                            </button>
                                        </c:if>
                                        <!--                                    <form action="managerContact" method="post" style="display:inline;" onsubmit="return confirm('Xóa yêu cầu hỗ trợ này?')">
                                                                                <input type="hidden" name="action" value="delete">
                                                                                <input type="hidden" name="id" value="${c.id}">
                                                                                <button class="btn btn-sm btn-outline-danger"><i class="fa-solid fa-trash"></i></button>
                                                                            </form>-->
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty contactList}">
                                <tr>
                                    <td colspan="6" class="text-center py-5 text-muted">Chưa có liên hệ hỗ trợ nào.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Reply Modal -->
        <div class="modal fade" id="replyModal" tabindex="-1" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content bg-dark text-white border-secondary">
                    <form action="managerContact" method="post">
                        <input type="hidden" name="action" value="respond">
                        <input type="hidden" name="id" id="reply-id">
                        <div class="modal-header border-secondary">
                            <h5 class="modal-title"><i class="fa-solid fa-reply-all mr-2 text-warning"></i>Phản hồi khách hàng</h5>
                            <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-4">
                                <label class="text-muted small mb-2"><i class="fa-solid fa-quote-left mr-1"></i> Nội dung khách gửi:</label>
                                <div id="reply-customer-msg" class="p-3 bg-glass rounded small text-info" style="background: rgba(255,255,255,0.05) !important; border: 1px solid var(--border);"></div>
                            </div>
                            <div class="form-group mb-0">
                                <label class="small text-muted mb-2"><i class="fa-solid fa-pen mr-1"></i> Tin nhắn phản hồi của bạn</label>
                                <textarea class="form-control" name="responseMessage" rows="5" required 
                                          style="background: #0f172a !important; color: #f1f5f9 !important; border: 1px solid var(--border) !important; border-radius: 12px !important; padding: 12px !important;"
                                          placeholder="Nhập lời giải đáp cho khách hàng..."></textarea>
                            </div>
                        </div>
                        <div class="modal-footer border-secondary">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background: #334155; border: none;">Hủy</button>
                            <button type="submit" class="btn btn-primary" style="background: var(--primary); border: none; padding: 8px 20px;">
                                <i class="fa-solid fa-paper-plane mr-1"></i> Gửi phản hồi
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- View Reply Modal -->
        <div class="modal fade" id="viewModal" tabindex="-1" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content bg-dark text-white border-secondary">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title"><i class="fa-solid fa-message mr-2 text-info"></i>Chi tiết trao đổi</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-4">
                            <label class="text-warning small mb-2"><i class="fa-solid fa-circle-question mr-1"></i> Khách gửi:</label>
                            <div id="view-customer-msg" class="p-3 rounded small" style="background: rgba(255,255,255,0.05); border: 1px solid var(--border);"></div>
                        </div>
                        <div>
                            <label class="text-success small mb-2"><i class="fa-solid fa-circle-check mr-1"></i> Shop phản hồi:</label>
                            <div id="view-owner-msg" class="p-3 rounded border-left" style="background: rgba(255,255,255,0.05); border-left: 3px solid #4ade80 !important; border: 1px solid var(--border);"></div>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background: #334155; border: none;">Đóng</button>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function () {
                $('.btn-reply').on('click', function () {
                    const id = $(this).data('id');
                    const msg = $(this).data('message');
                    $('#reply-id').val(id);
                    $('#reply-customer-msg').text(msg);
                    $('#replyModal').modal('show');
                });

                $('.btn-view-reply').on('click', function () {
                    const custMsg = $(this).data('message');
                    const ownerMsg = $(this).data('response');
                    $('#view-customer-msg').text(custMsg);
                    $('#view-owner-msg').text(ownerMsg);
                    $('#viewModal').modal('show');
                });
            });
        </script>
    </body>
</html>
