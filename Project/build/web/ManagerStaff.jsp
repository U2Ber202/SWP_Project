<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản Lý Nhân Viên | V-SNKR</title>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        :root {
            --primary: #ea580c;
            --bg: #0f172a;
            --card-bg: #1e293b;
            --border: rgba(255, 255, 255, 0.1);
        }
        body { font-family: 'Be Vietnam Pro', sans-serif; background-color: var(--bg) !important; color: #f1f5f9; padding-bottom: 40px; }
        .glass-card { background: var(--card-bg); backdrop-filter: none; border: 1px solid var(--border); border-radius: 20px; box-shadow: 0 10px 40px #0f172a; overflow: hidden; margin-bottom: 30px; }
        .card-header-custom { background: #0f172a; padding: 20px 30px; border-bottom: 1px solid var(--border); }
        .form-control { background: #0f172a !important; border: 1px solid var(--border) !important; color: white !important; }
        .form-control:focus { background: #0f172a !important; border-color: var(--primary) !important; color: white !important; box-shadow: none; }
        .btn-primary-custom { background-color: var(--primary); border: none; font-weight: 600; border-radius: 8px; }
        .table { color: #f1f5f9; }
        .table thead th { border-top: none; border-bottom: 1px solid var(--border); background: rgba(0,0,0,0.1); color: #94a3b8; font-size: 0.8rem; text-transform: uppercase; }
        .table td { border-top: 1px solid var(--border); vertical-align: middle; }
        .badge-role { border-radius: 30px; padding: 5px 12px; font-size: 0.75rem; }
        .history-item { border-left: 2px solid var(--primary); padding-left: 15px; margin-bottom: 15px; }
    </style>
</head>
<body class="bg-theme">
    <%@ include file="components/navBarComponent.jsp" %>
    
    <div class="container mt-4">
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show glass-card border-danger" role="alert">
                <i class="fas fa-exclamation-circle mr-2"></i> ${sessionScope.error}
                <button type="button" class="close text-white" data-dismiss="modal" onclick="${sessionScope.remove('error')}">&times;</button>
            </div>
            <% session.removeAttribute("error"); %>
        </c:if>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show glass-card border-success" role="alert">
                <i class="fas fa-check-circle mr-2"></i> ${sessionScope.success}
                <button type="button" class="close text-white" data-dismiss="modal" onclick="${sessionScope.remove('success')}">&times;</button>
            </div>
            <% session.removeAttribute("success"); %>
        </c:if>

        <h2 class="mb-4 font-weight-bold"><i class="fas fa-users-cog mr-2 text-warning"></i> Quản Lý Nhân Sự</h2>
        
        <div class="row">
            <!-- Staff List & Create -->
            <div class="col-lg-8">
                <div class="glass-card">
                    <div class="card-header-custom d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">Nhân viên hiện tại</h5>
                        <button class="btn btn-primary-custom btn-sm" data-toggle="modal" data-target="#addModal">
                            <i class="fas fa-user-plus mr-1"></i> Thêm nhân viên
                        </button>
                    </div>
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead>
                                <tr>
                                    <th>Họ tên</th>
                                    <th>Vai trò</th>
                                    <th>Liên hệ</th>
                                    <th>Trạng thái</th>
                                    <th>Xử lý</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${staffList}" var="s">
                                    <tr>
                                        <td>
                                            <div class="font-weight-bold">${s.fullname}</div>
                                            <div class="small text-muted">@${s.user}</div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${s.role == 'warehouse_manager'}">
                                                    <span class="badge badge-info badge-role">Quản lý kho</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-success badge-role">Shipper</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="small"><i class="fas fa-envelope mr-1 text-muted"></i> ${s.email}</div>
                                            <div class="small"><i class="fas fa-phone mr-1 text-muted"></i> ${s.phone}</div>
                                        </td>
                                        <td>
                                            <c:if test="${s.active}">
                                                <span class="text-success"><i class="fas fa-check-circle mr-1"></i>Hoạt động</span>
                                            </c:if>
                                            <c:if test="${!s.active}">
                                                <span class="text-danger"><i class="fas fa-times-circle mr-1"></i>Khóa</span>
                                            </c:if>
                                        </td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-light" onclick='editStaff(${s.uid}, "${s.fullname}", "${s.phone}", "${s.email}", ${s.active})'>
                                                <i class="fas fa-edit"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty staffList}">
                                    <tr><td colspan="5" class="text-center py-4 text-muted small">Chưa có nhân viên nào được gán</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- Action History -->
            <div class="col-lg-4">
                <div class="glass-card">
                    <div class="card-header-custom">
                        <h5 class="mb-0"><i class="fas fa-history mr-1"></i> Lịch sử tác động</h5>
                    </div>
                    <div class="p-4" style="max-height: 500px; overflow-y: auto;">
                        <c:forEach items="${historyList}" var="h">
                            <div class="history-item">
                                <div class="small font-weight-bold text-warning">
                                    ${h.actionType == 'ADD' ? 'THÊM MỚI' : 'CẬP NHẬT'}
                                </div>
                                <div class="small text-white mb-1">${h.details}</div>
                                <div class="text-muted" style="font-size: 0.7rem;">
                                    <i class="fas fa-clock mr-1"></i><fmt:formatDate value="${h.actionAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty historyList}">
                            <div class="text-center text-muted small py-3">Chưa có lịch sử hoạt động</div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Modal -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-white border-secondary shadow-lg">
                <form action="managerStaff" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title font-weight-bold">Tạo tài khoản nhân viên</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label class="small">Tên đăng nhập</label>
                                <input type="text" class="form-control" name="user" required>
                            </div>
                            <div class="col-md-6 form-group">
                                <label class="small">Mật khẩu</label>
                                <input type="password" class="form-control" name="pass" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="small">Họ và tên</label>
                            <input type="text" class="form-control" name="fullname" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label class="small">Số điện thoại</label>
                                <input type="text" class="form-control" name="phone">
                            </div>
                            <div class="col-md-6 form-group">
                                <label class="small">Email</label>
                                <input type="email" class="form-control" name="email">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="small">Vai trò nhân viên</label>
                            <select class="form-control" name="role" required>
                                <option value="warehouse_manager">Quản lý kho</option>
                                <option value="shipper">Shipper (Quản lý giao hàng)</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary-custom px-4">Tạo tài khoản</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Modal -->
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-white border-secondary shadow-lg">
                <form action="managerStaff" method="post">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" id="edit-id">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title font-weight-bold">Cập nhật nhân viên</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="small">Họ và tên</label>
                            <input type="text" class="form-control" name="fullname" id="edit-fullname" required>
                        </div>
                        <div class="form-group">
                            <label class="small">Email</label>
                            <input type="email" class="form-control" name="email" id="edit-email" required>
                        </div>
                        <div class="form-group">
                            <label class="small">Số điện thoại</label>
                            <input type="text" class="form-control" name="phone" id="edit-phone">
                        </div>
                        <div class="custom-control custom-switch mt-3">
                            <input type="checkbox" class="custom-control-input" name="active" id="edit-active">
                            <label class="custom-control-label" for="edit-active">Cho phép hoạt động</label>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary-custom px-4">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        function editStaff(id, fullname, phone, email, active) {
            $('#edit-id').val(id);
            $('#edit-fullname').val(fullname);
            $('#edit-phone').val(phone);
            $('#edit-email').val(email);
            $('#edit-active').prop('checked', active);
            $('#editModal').modal('show');
        }
    </script>
</body>
</html>



