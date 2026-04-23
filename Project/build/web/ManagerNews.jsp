<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản Lý Tin Tức | V-SNKR</title>
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
        .admin-wrapper { background: var(--card-bg); backdrop-filter: none; border: 1px solid var(--border); border-radius: 20px; box-shadow: 0 10px 40px #0f172a; overflow: hidden; }
        .admin-header { background: #0f172a; padding: 25px 30px; border-bottom: 1px solid var(--border); display: flex; justify-content: space-between; align-items: center; }
        .create-form { background-color: rgba(0, 0, 0, 0.15); padding: 25px 30px; border-bottom: 1px solid var(--border); }
        .form-control { background: #0f172a !important; border: 1px solid var(--border) !important; color: white !important; border-radius: 8px !important; }
        .custom-table { margin-bottom: 0; color: #f1f5f9; }
        .custom-table thead th { border-bottom: 1px solid var(--border); border-top: none; color: #94a3b8; padding: 15px 20px; background-color: #0f172a; text-transform: uppercase; font-size: 0.85rem; }
        .custom-table tbody td { padding: 15px 20px; vertical-align: middle; border-top: 1px solid var(--border); }
        .news-img { width: 80px; height: 50px; object-fit: cover; border-radius: 6px; }
        .btn-primary-custom { background-color: var(--primary); border: none; font-weight: 600; border-radius: 8px; }
    </style>
    <script src="js/theme.js"></script>
    <link rel="stylesheet" href="css/theme.css">
</head>
<body class="bg-theme">
    <%@ include file="components/navBarComponent.jsp" %>
    <div class="container mt-4">
        <div class="admin-wrapper">
            <div class="admin-header">
                <h2><i class="fa-solid fa-newspaper mr-2 text-warning"></i> Quản Lý <b>Tin Tức</b></h2>
                <c:if test="${sessionScope.acc.role == 'admin'}">
                    <span class="badge badge-info p-2 px-3">Hệ thống</span>
                </c:if>
                <c:if test="${sessionScope.acc.role == 'owner'}">
                    <span class="badge badge-warning p-2 px-3">Cửa hàng</span>
                </c:if>
            </div>

            <div class="create-form">
                <form action="managerNews" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label>Tiêu đề</label>
                            <input type="text" class="form-control" name="title" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label>Ảnh bìa (URL)</label>
                            <input type="text" class="form-control" name="image" placeholder="https://...">
                        </div>
                        <div class="col-md-4 mb-3 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary-custom btn-block py-2">Đăng tin ngay</button>
                        </div>
                        <div class="col-12">
                            <label>Nội dung</label>
                            <textarea class="form-control" name="content" rows="3" required></textarea>
                        </div>
                    </div>
                </form>
            </div>

            <div class="table-responsive">
                <table class="table custom-table">
                    <thead>
                        <tr>
                            <th>Ảnh</th>
                            <th>Tin tức</th>
                            <c:if test="${sessionScope.acc.role == 'admin'}">
                                <th>Nguồn tin</th>
                            </c:if>
                            <th>Ngày đăng</th>
                            <th class="text-center">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${newsList}" var="n">
                            <tr>
                                <td><img src="${n.image}" class="news-img" onerror="this.src='https://via.placeholder.com/80x50?text=No+Image'"></td>
                                <td>
                                    <div class="font-weight-bold">${n.title}</div>
                                    <div class="small text-muted text-truncate" style="max-width: 400px;">${n.content}</div>
                                </td>
                                <c:if test="${sessionScope.acc.role == 'admin'}">
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty n.storeId}">
                                                <span class="badge badge-info">Hệ thống</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-warning">${n.storeName}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>
                                <td><fmt:formatDate value="${n.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-info mr-2" onclick="editNews(${n.id}, '${n.title}', '${n.image}', '${n.content}')">
                                        <i class="fa-solid fa-edit"></i>
                                    </button>
                                    <form action="managerNews" method="post" style="display:inline;" onsubmit="return confirm('Xóa tin này?')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${n.id}">
                                        <button class="btn btn-sm btn-outline-danger"><i class="fa-solid fa-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Edit Modal -->
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg">
            <div class="modal-content bg-dark text-white border-secondary">
                <form action="managerNews" method="post">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" id="edit-id">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title">Sửa tin tức</h5>
                        <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Tiêu đề</label>
                            <input type="text" class="form-control" name="title" id="edit-title" required>
                        </div>
                        <div class="form-group">
                            <label>Ảnh bìa</label>
                            <input type="text" class="form-control" name="image" id="edit-image">
                        </div>
                        <div class="form-group">
                            <label>Nội dung</label>
                            <textarea class="form-control" name="content" id="edit-content" rows="5" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary-custom">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        function editNews(id, title, image, content) {
            $('#edit-id').val(id);
            $('#edit-title').val(title);
            $('#edit-image').val(image);
            $('#edit-content').val(content);
            $('#editModal').modal('show');
        }
    </script>
</body>
</html>



