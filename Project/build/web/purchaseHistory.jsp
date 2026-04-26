<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đơn mua của tôi | V-SNKR</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body { font-family: 'Be Vietnam Pro', sans-serif; background-color: var(--bg) !important; color: var(--text-main); }
        .order-card { background: var(--card-bg); border: 1px solid var(--border); border-radius: 20px; padding: 25px; margin-bottom: 20px; transition: 0.3s; }
        .order-card:hover { border-color: var(--primary); transform: translateY(-5px); }
        .status-badge { padding: 5px 12px; border-radius: 999px; font-size: 0.75rem; font-weight: 700; }
        .status-shipped { background: rgba(34, 197, 94, 0.2); color: #4ade80; }
        .status-other { background: rgba(255, 255, 255, 0.1); color: var(--text-muted); }
        .btn-contact { background: rgba(234, 88, 12, 0.1); border: 1px solid var(--primary); color: var(--primary); border-radius: 10px; font-weight: 600; }
        .btn-contact:hover { background: var(--primary); color: white; }
        
        .modal-header { border-bottom: 1px solid var(--border) !important; }
        .modal-footer { border-top: 1px solid var(--border) !important; }
        .bg-glass { background: var(--bg) !important; border: 1px solid var(--border); }
        .italic { font-style: italic; }
    </style>
    <script src="js/theme.js"></script>
    <link rel="stylesheet" href="css/theme.css">
</head>
<body class="bg-theme">
    <%@ include file="components/navBarComponent.jsp" %>
    <div class="container mt-4 mb-5">
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success border-0 glass-card text-success mb-4" style="background: rgba(34, 197, 94, 0.1);">
                <i class="fas fa-check-circle mr-2"></i> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger border-0 glass-card text-danger mb-4" style="background: rgba(239, 68, 68, 0.1);">
                <i class="fas fa-exclamation-circle mr-2"></i> ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <h2 class="mb-4"><i class="fa-solid fa-bag-shopping text-warning mr-2"></i> Lịch sử <b>Đơn mua</b></h2>
        
        <c:forEach items="${orders}" var="o">
            <c:set var="ship" value="${shippingByOrderId[o.id]}" />
            <div class="order-card">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-muted">Mã đơn hàng: <b>#${o.id}</b> | Ngày đặt: ${o.createdDate}</span>
                    <span class="status-badge ${ship.status == 'Shipped' ? 'status-shipped' : 'status-other'}">
                        ${not empty ship.status ? ship.status : 'Đang xử lý'}
                    </span>
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <h5>Tổng cộng: <span class="text-warning"><fmt:formatNumber value="${o.totalPrice}" pattern="#,### đ"/></span></h5>
                        <p class="small text-muted mb-0">Ghi chú: ${o.note}</p>
                    </div>
                    <div class="col-md-4 text-right">
                        
                        <%-- Chỉ cho phép liên hệ nếu đơn hàng đã giao (Shipped) --%>
                        <%-- Chỉ cho phép liên hệ nếu đơn hàng đã giao (Shipped) và chưa gửi yêu cầu hỗ trợ --%>
                        <c:if test="${ship.status == 'Shipped'}">
                            <c:set var="isSupported" value="${supportedOrderIds.contains(o.id)}" />
                            <button class="btn btn-sm ${isSupported ? 'btn-outline-info' : 'btn-contact'}" onclick="openContactModal(${o.id})">
                                <i class="fa-solid fa-headset mr-1"></i> 
                                ${isSupported ? 'Gửi thêm hỗ trợ' : 'Liên hệ/Hỗ trợ'}
                            </button>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
        
        <c:if test="${empty orders}">
            <div class="text-center py-5">
                <i class="fa-solid fa-box-open fa-4x text-muted mb-3"></i>
                <p>Bạn chưa có đơn hàng nào.</p>
                <a href="home" class="btn btn-warning mt-3">Mua sắm ngay</a>
            </div>
        </c:if>

        <!-- Support History Section -->
        <div class="mt-5">
            <h3 class="mb-4"><i class="fa-solid fa-envelope-open-text text-info mr-2"></i> Lịch sử <b>Hỗ trợ/Khiếu nại</b></h3>
            <div class="table-responsive glass-card p-4" style="background: rgba(255,255,255,0.02); border: 1px solid var(--border); border-radius: 20px;">
                <table class="table text-white">
                    <thead>
                        <tr class="text-muted small uppercase">
                            <th>Đơn hàng</th>
                            <th>Nội dung gửi</th>
                            <th>Ngày gửi</th>
                            <th>Trạng thái</th>
                            <th>Phản hồi Shop</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${contactList}" var="cl">
                            <tr>
                                <td><b>#${cl.orderId}</b></td>
                                <td style="max-width: 250px;">${cl.message}</td>
                                <td class="small"><fmt:formatDate value="${cl.createdAt}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <span class="status-badge ${cl.status == 'Đã phản hồi' ? 'status-shipped' : 'status-other'}">
                                        ${cl.status}
                                    </span>
                                </td>
                                <td>
                                    <c:if test="${empty cl.responseMessage}">
                                        <i class="text-muted small">Đang chờ xử lý...</i>
                                    </c:if>
                                    <c:if test="${not empty cl.responseMessage}">
                                        <button class="btn btn-sm btn-outline-info" onclick="viewOwnerReply('<c:out value='${cl.message}'/>', '<c:out value='${cl.responseMessage}'/>', '<c:out value='${cl.storeName}'/>', ${cl.orderId})">
                                            Xem giải đáp
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty contactList}">
                            <tr><td colspan="5" class="text-center py-3 text-muted">Bạn chưa gửi yêu cầu hỗ trợ nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Contact Modal -->
    <div class="modal fade" id="contactModal" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="addContact" method="post">
                    <input type="hidden" name="orderId" id="contact-order-id">
                    <div class="modal-header">
                        <h5 class="modal-title">Gửi yêu cầu hỗ trợ đơn hàng</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <p class="small text-muted">Vui lòng mô tả vấn đề bạn gặp phải với đơn hàng này (Ví dụ: Sai kích thước, hàng lỗi, hoàn tiền...).</p>
                        <div class="form-group">
                            <label>Nội dung lời nhắn</label>
                            <textarea class="form-control" name="message" rows="5" required placeholder="Nhập nội dung tại đây..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-warning px-4">Gửi yêu cầu</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="ownerReplyModal" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fa-solid fa-comments text-warning mr-2"></i> Giải đáp từ <span id="reply-store-name" class="text-warning font-weight-bold"></span></h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="mb-4">
                        <label class="text-muted small mb-2"><i class="fa-solid fa-quote-left mr-1"></i> Bạn đã hỏi:</label>
                        <div id="reply-customer-text" class="p-3 bg-glass rounded small italic text-info border-left" style="border-left: 3px solid #0ea5e9"></div>
                    </div>
                    <div>
                        <label class="text-success small mb-2 font-weight-bold"><i class="fa-solid fa-reply-all mr-1"></i> Cửa hàng phản hồi:</label>
                        <div id="reply-owner-text" class="p-3 bg-glass rounded border-left border-success" style="border-left-width: 4px !important; background: rgba(34, 197, 94, 0.05) !important;"></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <p class="small text-muted mr-auto">Bạn vẫn cần hỗ trợ?</p>
                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-warning btn-sm" id="btn-respond-back">
                        <i class="fa-solid fa-paper-plane mr-1"></i> Phản hồi lại
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        let currentOrderIdForReply = null;

        function openContactModal(orderId) {
            $('#contact-order-id').val(orderId);
            $('#contactModal').modal('show');
        }
        function viewOwnerReply(custMsg, ownerMsg, storeName, orderId) {
            currentOrderIdForReply = orderId;
            $('#reply-store-name').text(storeName);
            $('#reply-customer-text').text(custMsg);
            $('#reply-owner-text').text(ownerMsg);
            $('#ownerReplyModal').modal('show');
        }

        $(document).ready(function() {
            $('#btn-respond-back').on('click', function() {
                $('#ownerReplyModal').modal('hide');
                setTimeout(function() {
                    openContactModal(currentOrderIdForReply);
                    // Pre-fill with a hint if desired
                    $('#contactModal textarea').val("Phản hồi bổ sung cho shop: ");
                }, 400);
            });
        });
    </script>
</body>
</html>



