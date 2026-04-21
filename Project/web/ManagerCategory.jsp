<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Quản Lý Danh Mục | V-SNKR Admin</title>

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
                    color: var(--text-main);
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

                .action-bar {
                    padding: 20px 30px;
                    background-color: rgba(0, 0, 0, 0.15);
                    border-bottom: 1px solid var(--border);
                    display: flex;
                    justify-content: flex-end;
                }

                .btn-custom-primary {
                    background-color: var(--primary);
                    color: white;
                    border: none;
                    border-radius: 12px;
                    font-weight: 600;
                    padding: 10px 20px;
                    transition: all 0.3s ease;
                }

                .btn-custom-primary:hover {
                    background-color: var(--primary-dark);
                    transform: scale(1.02);
                    color: white;
                }

                .btn-custom-secondary {
                    background-color: transparent;
                    color: #94a3b8;
                    border: 1px solid var(--border);
                    border-radius: 12px;
                    font-weight: 600;
                    padding: 10px 25px;
                    transition: all 0.3s ease;
                }

                .btn-custom-secondary:hover {
                    background-color: var(--glass);
                    color: white;
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
                    padding: 15px 30px;
                    background-color: rgba(0, 0, 0, 0.2);
                    text-transform: uppercase;
                    font-size: 0.85rem;
                    letter-spacing: 0.5px;
                }

                .custom-table tbody td {
                    padding: 15px 30px;
                    vertical-align: middle;
                    border-top: 1px solid var(--border);
                    font-weight: 500;
                }

                .custom-table tbody tr:hover {
                    background-color: rgba(255, 255, 255, 0.05);
                }

                .action-icon {
                    font-size: 1.2rem;
                    margin: 0 8px;
                    transition: all 0.2s ease;
                    display: inline-block;
                }

                .action-icon.edit {
                    color: #fbbf24;
                }

                .action-icon.delete {
                    color: #f87171;
                }

                .action-icon:hover {
                    transform: scale(1.15);
                    opacity: 0.8;
                }

                .modal-content {
                    background: #1e293b;
                    border-radius: 20px;
                    border: 1px solid var(--border);
                    box-shadow: 0 15px 50px rgba(0, 0, 0, 0.5);
                    color: #f1f5f9;
                }

                .modal-header {
                    border-bottom: 1px solid var(--border);
                    padding: 20px 30px;
                }

                .modal-body {
                    padding: 30px;
                }

                .modal-footer {
                    border-top: 1px solid var(--border);
                    padding: 20px 30px;
                    background-color: rgba(0, 0, 0, 0.1);
                    border-radius: 0 0 20px 20px;
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
                    border-radius: 0 8px 8px 0;
                }

                .form-control:focus {
                    background: rgba(0, 0, 0, 0.3) !important;
                    border-color: var(--primary);
                    box-shadow: none;
                }

                .btn-light {
                    background-color: transparent;
                    color: #94a3b8;
                    border: 1px solid var(--border) !important;
                }

                .btn-light:hover {
                    background-color: var(--glass);
                    color: white;
                }
            </style>

            <script>
                function back() {
                    window.location.href = "home";
                }
                function doDelete(id) {
                    var c = confirm("Bạn có chắc chắn muốn xóa danh mục này?");
                    if (c) {
                        window.location.href = "delete-category?cid=" + id;
                    }
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
                                <h2><i class="fa-solid fa-layer-group"></i> Quản Lý <b>Danh Mục</b></h2>
                                <button type="button" class="btn btn-outline-light rounded-pill px-4 font-weight-bold"
                                    onclick="back()">
                                    <i class="fa-solid fa-house mr-1"></i> Trang Chủ
                                </button>
                            </div>


                            <div class="action-bar">
                                <a href="#addCategoryModal" class="btn btn-custom-primary" data-toggle="modal">
                                    <i class="fa-solid fa-plus mr-1"></i> Thêm Danh Mục Mới
                                </a>
                            </div>

                            <div class="table-responsive">
                                <table class="table custom-table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã Danh Mục (ID)</th>
                                            <th>Tên Danh Mục</th>
                                            <th>Hãng sản xuất</th>
                                            <th class="text-center">Thao Tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${listCategories}" var="c">
                                            <tr>
                                                <td><span class="badge badge-light border p-2">#${c.cid}</span></td>
                                                <td class="font-weight-bold" style="color: #e2e8f0;">${c.cname}</td>
                                                <td>${empty c.manufacturer ? 'Chưa cập nhật' : c.manufacturer}</td>
                                                <td class="text-center">
                                                    <a href="loadCategory?cid=${c.cid}" class="action-icon edit"
                                                        data-toggle="tooltip" title="Chỉnh sửa">
                                                        <i class="fa-solid fa-pen-to-square"></i>
                                                    </a>
                                                    <a href="javascript:void(0);" onclick="doDelete('${c.cid}')"
                                                        class="action-icon delete" data-toggle="tooltip"
                                                        title="Xóa danh mục">
                                                        <i class="fa-solid fa-trash-can"></i>
                                                    </a>
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
                                                <a class="page-link" href="managerCategory?page=${page - 1}">Trước</a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPage}" var="i">
                                                <li class="page-item ${page == i ? 'active' : ''}">
                                                    <a class="page-link" href="managerCategory?page=${i}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
                                                <a class="page-link" href="managerCategory?page=${page + 1}">Sau</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>
                        </div>

                        <div class="d-flex justify-content-end">
                            <button type="button" class="btn btn-custom-secondary" onclick="back()">
                                <i class="fa-solid fa-arrow-left mr-1"></i> Quay Lại
                            </button>
                        </div>
                    </div>

                    <div id="addCategoryModal" class="modal fade">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <form action="addcategory" method="post">
                                    <div class="modal-header">
                                        <h4 class="modal-title"><i class="fa-solid fa-tags text-warning mr-2"></i> Thêm
                                            Danh Mục Mới</h4>
                                        <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">&times;</button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label class="font-weight-bold">Tên Danh Mục</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i
                                                            class="fa-solid fa-font"></i></span>
                                                </div>
                                                <input name="name" type="text" class="form-control"
                                                    placeholder="Nhập tên danh mục" required>
                                            </div>
                                        </div>
                                        <div class="form-group mb-0">
                                            <label class="font-weight-bold">Hãng sản xuất / Xuất xứ</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text"><i
                                                            class="fa-solid fa-globe"></i></span>
                                                </div>
                                                <input name="manufacturer" type="text" class="form-control"
                                                    placeholder="Ví dụ: US-UK, Chinese, Vietnam..." required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="button" class="btn btn-light rounded-pill px-4"
                                            data-dismiss="modal" value="Hủy">
                                        <button type="submit" class="btn btn-custom-primary rounded-pill px-4"
                                            style="width: auto;">
                                            <i class="fa-solid fa-check mr-1"></i> Thêm Mới
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
                    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
                    <script>
                        $(function () {
                            $('[data-toggle="tooltip"]').tooltip();
                        });
                    </script>
        </body>

        </html>