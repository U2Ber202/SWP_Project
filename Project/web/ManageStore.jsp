<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Quản lý cửa hàng | V-SNKR Admin</title>
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
                background-color: var(--bg);
                color: #f1f5f9;
                padding: 32px 0 48px;
            }

            .panel {
                background: var(--card-bg);
                backdrop-filter: blur(12px);
                border: 1px solid var(--border);
                border-radius: 24px;
                box-shadow: 0 14px 40px rgba(0, 0, 0, 0.3);
                overflow: hidden;
                margin-bottom: 24px;
            }

            .panel-header {
                padding: 24px 28px;
                border-bottom: 1px solid var(--border);
                background: rgba(0, 0, 0, 0.18);
            }

            .panel-title {
                margin: 0;
                font-size: 1.35rem;
                font-weight: 700;
            }

            .panel-body {
                padding: 24px 28px;
            }

            .btn-primary-custom {
                background: var(--primary);
                border: none;
                border-radius: 12px;
                color: white;
                font-weight: 600;
                padding: 10px 18px;
            }

            .btn-secondary-custom {
                background: transparent;
                color: #cbd5e1;
                border: 1px solid var(--border);
                border-radius: 12px;
                font-weight: 600;
                padding: 10px 18px;
            }

            .table {
                color: #f1f5f9;
                margin-bottom: 0;
            }

            .table thead th {
                border-top: none;
                border-bottom: 1px solid var(--border);
                color: #94a3b8;
                text-transform: uppercase;
                font-size: 0.8rem;
            }

            .table td,
            .table th {
                border-color: var(--border);
                vertical-align: middle;
            }

            .form-control,
            .custom-select,
            textarea.form-control {
                background: rgba(0, 0, 0, 0.2);
                border: 1px solid var(--border);
                color: white;
                border-radius: 12px;
            }

            .custom-select option {
                background: #1e293b;
                color: white;
            }

            .modal-content {
                background: #1e293b;
                color: #f1f5f9;
                border: 1px solid var(--border);
                border-radius: 20px;
            }

            .modal-header,
            .modal-footer {
                border-color: var(--border);
            }

            .alert {
                border-radius: 14px;
                border: none;
            }

            .setting-box {
                background: rgba(255, 255, 255, 0.03);
                border: 1px solid var(--border);
                border-radius: 18px;
                padding: 18px;
                height: 100%;
            }

            .custom-control-label {
                color: #e2e8f0;
            }

            label {
                color: #cbd5e1;
                font-weight: 600;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>

    <body class="bg-theme">
        <%@ include file="components/navBarComponent.jsp" %>
        <%@ include file="components/toastNotification.jsp" %>
        <div class="container">
            <div class="d-flex justify-content-between align-items-center flex-wrap mb-4">
                <div>
                    <h1 class="h3 font-weight-bold mb-1">Quản lý cửa hàng và trang home</h1>
                    <div class="text-muted">Admin có thể sửa nội dung hero và gán owner, quản lý kho cho
                        từng store.</div>
                </div>
                <div class="mt-3 mt-md-0">
                    <a href="home" class="btn btn-secondary-custom mr-2">Trang chủ</a>
                    <a href="#addStoreModal" class="btn btn-primary-custom" data-toggle="modal">Thêm cửa
                        hàng</a>
                </div>
            </div>


            <div class="panel">
                <div class="panel-header d-flex justify-content-between align-items-center flex-wrap">
                    <h2 class="panel-title mb-0">Danh sách cửa hàng</h2>
                    <span class="text-muted small mt-2 mt-md-0">${listStores.size()} cửa hàng đang tồn
                        tại</span>
                </div>
                <div class="panel-body p-0">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="pl-4">ID</th>
                                <th>Tên cửa hàng</th>
                                <th>Chủ sở hữu (Owner)</th>
                                <th>Quản lý kho</th>
                                <th class="text-center pr-4">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listStores}" var="s">
                                <tr>
                                    <td class="pl-4">#${s.id}</td>
                                    <td class="font-weight-bold">${s.name}</td>
                                    <td>
                                        <c:forEach items="${listAccounts}" var="acc">
                                            <c:if test="${acc.uid == s.ownerId}">${acc.user}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach items="${warehouseManagersByStore[s.id]}" var="wm">
                                            <c:if test="${wm.uid == s.warehouseManagerId}">${wm.user}</c:if>
                                        </c:forEach>
                                        <c:if test="${s.warehouseManagerId == 0}">Chưa gán</c:if>
                                        </td>
                                        <td class="text-center pr-4">
                                            <div class="btn-group">
                                                <!--                                            <a href="manager?storeId={s.id}" class="btn btn-sm btn-outline-info mr-2" title="Xem Kho/Sản phẩm"><i class="fa-solid fa-boxes-stacked"></i></a>
                                    <a href="stockHistory?storeId={s.id}" class="btn btn-sm btn-outline-primary mr-2" title="Xem Lịch sử kho"><i class="fa-solid fa-clock-rotate-left"></i></a>
                                    <a href="store-front?id=${s.id}" class="btn btn-sm btn-outline-success mr-2" title="Xem Shop công khai"><i class="fa-solid fa-eye"></i></a>-->
                                            <a href="#editStoreModal${s.id}"
                                               class="btn btn-sm btn-outline-warning mr-2"
                                               data-toggle="modal" title="Sửa store"><i
                                                    class="fa-solid fa-pen-to-square"></i></a>
                                            <a href="#deleteStoreModal${s.id}"
                                               class="btn btn-sm btn-outline-danger" data-toggle="modal"
                                               title="Xóa store"><i class="fa-solid fa-trash-can"></i></a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <c:if test="${totalPage >= 1}">
                    <div class="card-footer border-top-0 bg-transparent pb-4">
                        <nav aria-label="Page navigation">
                            <ul class="pagination justify-content-center mb-0">
                                <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="manageStore?page=${page - 1}">Trước</a>
                                </li>
                                <c:forEach begin="1" end="${totalPage}" var="i">
                                    <li class="page-item ${page == i ? 'active' : ''}">
                                        <a class="page-link" href="manageStore?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                    <a class="page-link" href="manageStore?page=${page + 1}">Sau</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </c:if>
            </div>

        </div>

        <c:forEach items="${listStores}" var="s">
            <div id="editStoreModal${s.id}" class="modal fade">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form action="manageStore" method="post">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="storeId" value="${s.id}">
                            <div class="modal-header">
                                <h5 class="modal-title">Cập nhật cửa hàng</h5>
                                <button type="button" class="close text-white"
                                        data-dismiss="modal">&times;</button>
                            </div>
                            <div class="modal-body">
                                <c:if test="${not empty error}">
                                    <div class="mb-3 d-flex align-items-center"
                                         style="background:rgba(239,68,68,0.15); color:#fca5a5; border:1px solid rgba(239,68,68,0.4); padding:12px 16px; border-radius:12px; font-size:0.95rem;">
                                        <i class="fa-solid fa-circle-exclamation mr-2"></i>
                                        <span>${error}</span>
                                    </div>
                                </c:if>
                                <div class="form-group">
                                    <label>Tên cửa hàng</label>
                                    <input type="text" name="storeName" class="form-control"
                                           value="${s.name}" required>
                                </div>
                                <div class="form-group">
                                    <label>Chủ sở hữu (Owner)</label>
                                    <select name="ownerId" class="custom-select" required>
                                        <c:forEach items="${listAccounts}" var="acc">
                                            <option value="${acc.uid}" ${acc.uid==s.ownerId ? 'selected'
                                                             : '' }>${acc.user} (ID: ${acc.uid})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group mb-0">
                                    <label>Quản lý kho</label>
                                    <select name="warehouseManagerId" class="custom-select">
                                        <option value="">Chưa gán quản lý kho</option>
                                        <c:forEach items="${warehouseManagersByStore[s.id]}" var="wm">
                                            <option value="${wm.uid}" ${wm.uid==s.warehouseManagerId
                                                             ? 'selected' : '' }>${wm.user} (ID: ${wm.uid})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary-custom"
                                        data-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-primary-custom">Lưu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div id="deleteStoreModal${s.id}" class="modal fade">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form action="manageStore" method="post">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="storeId" value="${s.id}">
                            <div class="modal-header">
                                <h5 class="modal-title text-danger">Xóa cửa hàng</h5>
                                <button type="button" class="close text-white"
                                        data-dismiss="modal">&times;</button>
                            </div>
                            <div class="modal-body">Bạn chắc chắn muốn xóa <strong>${s.name}</strong>?</div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary-custom"
                                        data-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-danger">Xóa</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>

        <div id="addStoreModal" class="modal fade">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <form action="manageStore" method="post">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Thêm cửa hàng mới</h5>
                            <button type="button" class="close text-white"
                                    data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <%-- Đặt vào đầu modal-body của addStoreModal và editStoreModal --%>
                            <c:if test="${not empty error}">
                                <div class="mb-3 d-flex align-items-center"
                                     style="background:rgba(239,68,68,0.15); color:#fca5a5; border:1px solid rgba(239,68,68,0.4); padding:12px 16px; border-radius:12px; font-size:0.95rem;">
                                    <i class="fa-solid fa-circle-exclamation mr-2"></i>
                                    <span>${error}</span>
                                </div>
                            </c:if>
                            <div class="form-group">
                                <label>Tên cửa hàng</label>
                                <input type="text" name="storeName" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Chủ sở hữu (Owner)</label>
                                <select name="ownerId" class="custom-select" required>
                                    <option value="" disabled selected>Chọn tài khoản owner</option>
                                    <c:forEach items="${listAccounts}" var="acc">
                                        <option value="${acc.uid}">${acc.user} (ID: ${acc.uid})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group mb-0">
                                <label>Quản lý kho</label>
                                <select name="warehouseManagerId" class="custom-select">
                                    <option value="" selected>Chưa gán quản lý kho</option>
                                    <c:forEach items="${listWarehouseManagers}" var="wm">
                                        <option value="${wm.uid}">${wm.user} (ID: ${wm.uid})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary-custom"
                                    data-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary-custom">Thêm</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>

</html>