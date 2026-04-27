<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Quản Lý Tài Khoản | V-SNKR Admin</title>

            <link
                href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap"
                rel="stylesheet">
            <link rel="stylesheet"
                href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

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

                .create-section {
                    background-color: rgba(0, 0, 0, 0.15);
                    padding: 25px 30px;
                    border-bottom: 1px solid var(--border);
                }

                .input-group-text {
                    background-color: rgba(0, 0, 0, 0.3);
                    border: 1px solid var(--border);
                    border-right: none;
                    color: #94a3b8;
                    border-radius: 8px 0 0 8px;
                }

                .form-control {
                    background: rgba(0, 0, 0, 0.2) !important;
                    border: 1px solid var(--border);
                    color: white !important;
                    border-left: none;
                    border-radius: 0 8px 8px 0;
                    box-shadow: none !important;
                }

                .form-control::placeholder {
                    color: #64748b;
                }

                .btn-custom-primary {
                    background-color: var(--primary);
                    color: white;
                    border: none;
                    border-radius: 8px;
                    font-weight: 600;
                    padding: 10px 20px;
                    width: 100%;
                }

                .btn-custom-secondary {
                    background-color: transparent;
                    color: #94a3b8;
                    border: 1px solid var(--border);
                    border-radius: 12px;
                    font-weight: 600;
                    padding: 10px 25px;
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
                    border-top: 1px solid var(--border);
                }

                .alert {
                    border-radius: 0;
                    margin: 0;
                    border-left: none;
                    border-right: none;
                    border-top: none;
                }

                .alert-success {
                    background: rgba(34, 197, 94, 0.15);
                    color: #4ade80;
                    border-bottom: 1px solid rgba(34, 197, 94, 0.2);
                }

                .alert-danger {
                    background: rgba(239, 68, 68, 0.15);
                    color: #f87171;
                    border-bottom: 1px solid rgba(239, 68, 68, 0.2);
                }

                .search-toolbar {
                    padding: 24px 30px 12px;
                    border-bottom: 1px solid var(--border);
                    background: rgba(255, 255, 255, 0.02);
                }

                .search-toolbar .input-group .form-control {
                    border-left: 1px solid var(--border);
                    border-radius: 8px;
                }

                .status-pill {
                    display: inline-flex;
                    align-items: center;
                    justify-content: center;
                    min-width: 110px;
                    padding: 8px 14px;
                    border-radius: 999px;
                    font-size: 0.85rem;
                    font-weight: 700;
                    text-transform: uppercase;
                }

                .status-pill.active {
                    background: rgba(34, 197, 94, 0.18);
                    color: #4ade80;
                }

                .status-pill.inactive {
                    background: rgba(239, 68, 68, 0.18);
                    color: #f87171;
                }
            </style>

            <script>
                function back() {
                    window.location.href = "home";
                }
            </script>
            <script src="js/theme.js"></script>
            <link rel="stylesheet" href="css/theme.css">
        </head>

        <body class="bg-theme">
            <%@ include file="components/navBarComponent.jsp" %>
                <%@ include file="components/toastNotification.jsp" %>
                    <div class="container">
                        <div class="admin-wrapper mb-4">
                            <div class="admin-header">
                                <h2><i class="fa-solid fa-users-gear"></i> Quản Lý <b>Tài Khoản</b></h2>
                                <button type="button" class="btn btn-outline-light rounded-pill px-4 font-weight-bold"
                                    onclick="back()">
                                    <i class="fa-solid fa-house mr-1"></i> Trang Chủ
                                </button>
                            </div>


                            <div class="create-section">
                                <h5><i class="fa-solid fa-user-plus text-warning mr-2"></i> Thêm tài khoản Chủ sở hữu
                                    (Owner)</h5>
                                <form action="managerAccount" method="post">
                                    <input type="hidden" name="action" value="createOwner">
                                    <div class="row">
                                        <div class="col-md-4 mb-3">
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                                                </div>
                                                <input type="text" class="form-control" name="user" placeholder="Tên đăng nhập" value="${formUser}" required>
                                            </div>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                                </div>
                                                <input type="email" class="form-control" name="email" placeholder="Địa chỉ Email" value="${formEmail}" required>
                                            </div>
                                        </div>
                                        <div class="col-md-4 mb-3">
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                                </div>
                                                <input type="password" class="form-control" name="pass" placeholder="Mật khẩu" required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-5 mb-3">
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i class="fa-solid fa-id-card"></i></span>
                                                </div>
                                                <input type="text" class="form-control" name="fullname" placeholder="Họ và tên" value="${formFullname}" required>
                                            </div>
                                        </div>
                                        <div class="col-md-5 mb-3">
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                                                </div>
                                                <input type="text" class="form-control" name="phone" placeholder="Số điện thoại" value="${formPhone}" required>
                                            </div>
                                        </div>
                                        <div class="col-md-2 mb-3">
                                            <button type="submit" class="btn btn-custom-primary">Tạo Chủ sở hữu</button>
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <div class="search-toolbar">
                                <form action="managerAccount" method="get">
                                    <div class="row align-items-center">
                                        <div class="col-md-9 mb-3 mb-md-0">
                                            <div class="input-group">
                                                <input type="text" class="form-control" name="search" value="${search}"
                                                    placeholder="Tìm kiếm theo ID, tên đăng nhập, vai trò, email, trạng thái">
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <button type="submit" class="btn btn-custom-primary">Tìm kiếm</button>
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <div class="table-responsive">
                                <table class="table custom-table table-hover">
                                    <thead>
                                        <tr>
                                            <th>STT</th>
                                            <th>ID</th>
                                            <th>Tên Đăng Nhập</th>
                                            <th>Vai trò</th>
                                            <th>Email</th>
                                            <th class="text-center">Trạng Thái</th>
                                            <th class="text-center">Thao Tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${accounts}" var="p" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>#${p.uid}</td>
                                                <td>${p.user}</td>
                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${p.role == 'admin'}"><span
                                                                class="badge badge-info px-3 py-2 rounded-pill">Quản trị</span></c:when>
                                                        <c:when test="${p.role == 'owner'}"><span
                                                                class="badge badge-warning px-3 py-2 rounded-pill">Chủ cửa hàng</span></c:when>
                                                        <c:when test="${p.role == 'shipper'}"><span
                                                                class="badge badge-primary px-3 py-2 rounded-pill">Shipper</span>
                                                        </c:when>
                                                        <c:when test="${p.role == 'warehouse_manager'}"><span
                                                                class="badge badge-secondary px-3 py-2 rounded-pill">Quản lý kho</span></c:when>
                                                        <c:otherwise><span
                                                                class="badge badge-light px-3 py-2 rounded-pill">Khách hàng</span></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${p.email}</td>
                                                <td class="text-center">
                                                    <span
                                                        class="status-pill ${p.active ? 'active' : 'inactive'}">${p.active
                                                        ? 'Đang hoạt động' : 'Đã khóa'}</span>
                                                </td>
                                                <td class="text-center">
                                                    <a href="loadAccount?pid=${p.uid}"><i
                                                            class="fa-solid fa-pen-to-square"></i></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty accounts}">
                                            <tr>
                                                <td colspan="7" class="text-center">Không tìm thấy tài khoản phù hợp.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <c:if test="${totalPage >= 1}">
                                <div class="card-footer border-top-0 bg-transparent pb-4">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination justify-content-center mb-0">
                                            <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                                                <a class="page-link"
                                                    href="managerAccount?page=${page - 1}${not empty search ? '&search='.concat(search) : ''}">Trước</a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPage}" var="i">
                                                <li class="page-item ${page == i ? 'active' : ''}">
                                                    <a class="page-link"
                                                        href="managerAccount?page=${i}${not empty search ? '&search='.concat(search) : ''}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                                <a class="page-link"
                                                    href="managerAccount?page=${page + 1}${not empty search ? '&search='.concat(search) : ''}">Sau</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        </body>

        </html>