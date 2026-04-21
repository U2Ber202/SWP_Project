<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <%@page contentType="text/html" pageEncoding="UTF-8" %>
                <!DOCTYPE html>
                <html lang="vi">

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
                    <title>Quản Lý Kho Giày | V-SNKR Admin</title>

                    <link
                        href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap"
                        rel="stylesheet">
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                    <link rel="stylesheet"
                        href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

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

                        .card {
                            background: var(--card-bg);
                            backdrop-filter: blur(12px);
                            border: 1px solid var(--border) !important;
                            border-radius: 20px;
                            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
                            overflow: hidden;
                        }

                        .card-header {
                            background: rgba(0, 0, 0, 0.2) !important;
                            border-bottom: 1px solid var(--border) !important;
                            font-weight: 600;
                            color: white;
                            padding: 20px 30px;
                            font-size: 1.1rem;
                        }

                        .form-control {
                            background: rgba(0, 0, 0, 0.2) !important;
                            border: 1px solid var(--border);
                            color: white !important;
                            border-radius: 10px;
                            padding: 12px 15px;
                        }

                        .form-control::placeholder {
                            color: #64748b;
                        }

                        select.form-control option {
                            background-color: #1e293b;
                            color: white;
                        }

                        .form-group label {
                            color: #94a3b8 !important;
                            font-weight: 600;
                            text-transform: uppercase;
                            font-size: 0.85rem;
                        }

                        .btn-brand {
                            background-color: var(--primary);
                            color: white;
                            border: none;
                            border-radius: 12px;
                            font-weight: 600;
                            padding: 10px 25px;
                        }

                        .btn-outline-light {
                            border-radius: 30px;
                            border: 1px solid var(--border);
                            color: #e2e8f0;
                            font-weight: 600;
                            padding: 8px 25px;
                        }

                        .table {
                            color: #f1f5f9;
                            margin-bottom: 0;
                        }

                        .table thead th {
                            border-bottom: 1px solid var(--border);
                            border-top: none;
                            color: #94a3b8;
                            font-weight: 600;
                            text-transform: uppercase;
                            font-size: 0.85rem;
                            background-color: rgba(0, 0, 0, 0.2);
                            padding: 15px 20px;
                        }

                        .table tbody td {
                            vertical-align: middle;
                            border-bottom: 1px solid var(--border);
                            border-top: none;
                            padding: 15px 20px;
                        }

                        .shoe-thumbnail {
                            width: 60px;
                            height: 60px;
                            object-fit: cover;
                            border-radius: 12px;
                            border: 1px solid var(--border);
                        }

                        .action-icon {
                            font-size: 1.2rem;
                            margin: 0 8px;
                            display: inline-block;
                        }

                        .action-icon.edit {
                            color: #fbbf24;
                        }

                        .action-icon.delete {
                            color: #f87171;
                        }

                        .badge-success {
                            background-color: rgba(34, 197, 94, 0.15);
                            color: #4ade80;
                            border: 1px solid rgba(34, 197, 94, 0.2);
                            font-weight: 600;
                        }

                        .helper-text {
                            color: #94a3b8;
                            font-size: 0.92rem;
                        }

                        .size-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
                            gap: 12px;
                        }

                        .muted-box {
                            background: rgba(255, 255, 255, 0.04);
                            border: 1px solid var(--border);
                            border-radius: 16px;
                            padding: 16px;
                            color: #cbd5e1;
                        }

                        .text-muted-custom {
                            color: #94a3b8 !important;
                        }

                        .text-info {
                            color: #38bdf8 !important;
                        }

                        .text-danger {
                            color: #f87171 !important;
                        }
                    </style>
                    <script src="js/theme.js"></script>
                    <link rel="stylesheet" href="css/theme.css">
                </head>

                <body class="bg-theme">
                    <%@ include file="components/navBarComponent.jsp" %>
                        <%@ include file="components/toastNotification.jsp" %>
                            <div class="container py-5">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <div>
                                        <h2 class="font-weight-bold mb-0 text-white">
                                            <i class="fa-solid fa-boxes-stacked text-warning mr-2"></i>Quản Lý Kho Giày
                                        </h2>
                                        <c:if test="${not empty managedStore}">
                                            <div class="helper-text mt-2">Kho hiện tại:
                                                <strong>${managedStore.name}</strong> - ID ${managedStore.id}</div>
                                        </c:if>
                                    </div>
                                    <a class="btn btn-outline-light shadow-sm" href="home">
                                        <i class="fa-solid fa-house mr-1"></i> Về trang chủ
                                    </a>
                                </div>


                                <c:if test="${sessionScope.acc.role == 'owner'}">
                                    <div class="card mb-5">
                                        <div class="card-header">
                                            <i class="fa-solid fa-plus text-warning mr-2"></i>Thêm sản phẩm mới
                                        </div>
                                        <div class="card-body p-4">
                                            <form action="add" method="post">
                                                <div class="form-row">
                                                    <div class="form-group col-md-6">
                                                        <label>Tên sản phẩm</label>
                                                        <input class="form-control" name="name" value="${formName}"
                                                            required>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label>Link hình ảnh</label>
                                                        <input class="form-control" name="image" value="${formImage}"
                                                            required>
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-4">
                                                        <label>Giá bán</label>
                                                        <input class="form-control" type="number" min="0" name="price"
                                                            value="${formPrice}" required>
                                                    </div>
                                                    <div class="form-group col-md-4">
                                                        <label>Danh mục</label>
                                                        <select class="form-control" name="category" required>
                                                            <option value="" disabled ${empty formCategory ? 'selected'
                                                                : '' }>-- Chọn danh mục --</option>
                                                            <c:forEach items="${listCategories}" var="cat">
                                                                <option value="${cat.cid}" ${formCategory==cat.cid
                                                                    ? 'selected' : '' }>${cat.cname}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="form-group col-md-3">
                                                        <label>Danh sách size</label>
                                                        <input class="form-control" name="title" value="${formTitle}"
                                                            placeholder="VD: 34,35,36" required>
                                                    </div>
                                                    <div class="form-group col-md-3">
                                                        <label>Xuất xứ / Hãng</label>
                                                        <input name="manufacturer" class="form-control"
                                                            value="${formManufacturer}"
                                                            placeholder="Ví dụ: US-UK, Chinese, Vietnam..." required>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label>Mô tả</label>
                                                    <textarea class="form-control" name="description" rows="3"
                                                        required>${formDescription}</textarea>
                                                </div>
                                                <button class="btn btn-brand" type="submit">Thêm sản phẩm</button>
                                                <div class="helper-text mt-3">Nhập size cách nhau bằng dấu phẩy. Ví dụ:
                                                    34,35,36,37.</div>
                                            </form>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${sessionScope.acc.role == 'owner'}">
                                    <div class="card mb-5">
                                        <div class="card-header">
                                            <i class="fa-solid fa-user-plus text-warning mr-2"></i>Tạo tài khoản quản lý
                                            kho
                                        </div>
                                        <div class="card-body p-4">
                                            <form action="manager" method="post">
                                                <input type="hidden" name="action" value="createWarehouseManager">
                                                <div class="form-row">
                                                    <div class="form-group col-md-3">
                                                        <label>Tên đăng nhập quản lý kho</label>
                                                        <input class="form-control" name="warehouseUser"
                                                            value="${warehouseFormUser}" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label>Email quản lý kho</label>
                                                        <input class="form-control" type="email" name="warehouseEmail"
                                                            value="${warehouseFormEmail}" required>
                                                    </div>
                                                    <div class="form-group col-md-3">
                                                        <label>Họ tên</label>
                                                        <input class="form-control" name="warehouseFullname"
                                                            value="${warehouseFormFullname}" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label>Số điện thoại</label>
                                                        <input class="form-control" name="warehousePhone"
                                                            value="${warehouseFormPhone}" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label>Mật khẩu</label>
                                                        <input class="form-control" type="password" name="warehousePass"
                                                            required>
                                                    </div>
                                                </div>
                                                <button class="btn btn-brand" type="submit">Tạo quản lý kho</button>
                                            </form>
                                        </div>
                                    </div>

                                    <div class="card mb-5">
                                        <div class="card-header">
                                            <i class="fa-solid fa-user-plus text-warning mr-2"></i>Tạo tài khoản shipper
                                            cho cửa hàng
                                        </div>
                                        <div class="card-body p-4">
                                            <form action="manager" method="post">
                                                <input type="hidden" name="action" value="createShipper">
                                                <div class="form-row">
                                                    <div class="form-group col-md-3">
                                                        <label>Tên đăng nhập shipper</label>
                                                        <input class="form-control" name="shipperUser"
                                                            value="${shipperFormUser}" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label>Email shipper</label>
                                                        <input class="form-control" type="email" name="shipperEmail"
                                                            value="${shipperFormEmail}" required>
                                                    </div>
                                                    <div class="form-group col-md-3">
                                                        <label>Họ tên shipper</label>
                                                        <input class="form-control" name="shipperFullname"
                                                            value="${shipperFormFullname}" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label>Số điện thoại shipper</label>
                                                        <input class="form-control" name="shipperPhone"
                                                            value="${shipperFormPhone}" required>
                                                    </div>
                                                    <div class="form-group col-md-2">
                                                        <label>Mật khẩu shipper</label>
                                                        <input class="form-control" type="password" name="shipperPass"
                                                            required>
                                                    </div>
                                                </div>
                                                <button class="btn btn-brand" type="submit">Tạo shipper</button>
                                                <div class="helper-text mt-3">Sau khi tạo shipper, chủ cửa hàng vào
                                                    trang đơn hàng để gán đơn cho shipper.</div>
                                            </form>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${sessionScope.acc.role == 'owner'}">
                                    <div class="card mb-5">
                                        <div class="card-header">
                                            <i class="fa-solid fa-eye text-info mr-2"></i>Phân quyền kho
                                        </div>
                                        <div class="card-body p-4">
                                            <div class="muted-box">
                                                Owner không can thiệp trực tiếp vào kho. Việc nhập kho và cập nhật tồn
                                                theo size được thực hiện bởi role <strong>warehouse_manager</strong>.
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${sessionScope.acc.role == 'warehouse_manager'}">
                                    <div class="card mb-5">
                                        <div class="card-header">
                                            <i class="fa-solid fa-warehouse text-info mr-2"></i>Nhập kho theo size
                                        </div>
                                        <div class="card-body p-4">
                                            <form action="stock-import" method="post" id="stockImportForm">
                                                <input type="hidden" name="storeId" value="${managedStore.id}">
                                                <div class="form-row">
                                                    <div class="form-group col-md-6">
                                                        <label>Sản phẩm cần nhập</label>
                                                        <select class="form-control" name="productId"
                                                            id="stockProductSelect" required>
                                                            <option value="" disabled ${empty stockProductId
                                                                ? 'selected' : '' }>-- Chọn sản phẩm --</option>
                                                            <c:forEach items="${allProducts}" var="item">
                                                                <option value="${item.id}"
                                                                    data-sizes="${fn:escapeXml(item.tiltle)}"
                                                                    ${stockProductId==item.id ? 'selected' : '' }>
                                                                    #${item.id} - ${item.name} (${item.tiltle}) - tồn
                                                                    hiện tại: ${item.quantity} đôi</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label>Ghi chú lô hàng</label>
                                                        <input class="form-control" name="note" value="${stockNote}"
                                                            placeholder="VD: nhập kho đầu cá">
                                                    </div>
                                                </div>
                                                <label class="mb-3">Số lượng theo size</label>
                                                <c:set var="selectedProductSizes" value="" />
                                                <c:forEach items="${allProducts}" var="item">
                                                    <c:if test="${item.id == stockProductId}">
                                                        <c:set var="selectedProductSizes" value="${item.tiltle}" />
                                                    </c:if>
                                                </c:forEach>
                                                <div class="size-grid mb-4" id="sizeGrid">
                                                    <c:forEach items="${fn:split(selectedProductSizes, ',')}"
                                                        var="size">
                                                        <div><input class="form-control" type="number" min="0"
                                                                name="size_${fn:trim(size)}"
                                                                value="${stockSizeValues[fn:trim(size)]}"
                                                                placeholder="Size ${fn:trim(size)}"></div>
                                                    </c:forEach>
                                                </div>
                                                <button class="btn btn-brand" type="submit"><i
                                                        class="fa-solid fa-arrow-up-from-bracket mr-2"></i>Cập nhật nhập
                                                    kho</button>
                                            </form>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="card mb-5">
                                    <div class="card-header border-bottom">
                                        <h5 class="mb-0 font-weight-bold text-white"><i
                                                class="fa-solid fa-list-ul text-info mr-2"></i>Danh sách sản phẩm hiện
                                            có</h5>
                                    </div>
                                    <div class="card-body p-0 table-responsive">
                                        <table class="table table-hover mb-0">
                                            <thead>
                                                <tr>
                                                    <th class="pl-4">ID</th>
                                                    <th>Hình ảnh</th>
                                                    <th>Tên mẫu giày</th>
                                                    <th>Giá bán</th>
                                                    <th>Kho</th>
                                                    <th>Thương hiệu</th>
                                                    <th>Size</th>
                                                    <th class="text-right pr-4">Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${products}" var="p">
                                                    <tr>
                                                        <td class="pl-4 font-weight-bold">#${p.id}</td>
                                                        <td><img src="${p.imageUrl}" alt="${p.name}"
                                                                class="shoe-thumbnail"
                                                                onerror="this.src='https://via.placeholder.com/60?text=No+Image'">
                                                        </td>
                                                        <td class="font-weight-bold">${p.name}</td>
                                                        <td class="font-weight-bold">
                                                            <fmt:formatNumber value="${p.price}" pattern="#,### đ" />
                                                        </td>
                                                        <td>
                                                            <div class="font-weight-bold mb-1"><span
                                                                    class="badge badge-success px-3 py-2 rounded-pill">${p.quantity}
                                                                    đôi</span></div>
                                                            <div class="small text-muted-custom mt-1"
                                                                style="font-size: 0.75rem; max-width: 150px;">
                                                                <c:set var="sqMap" value="${sizeQuantitiesMap[p.id]}" />
                                                                <c:forEach items="${fn:split(p.tiltle, ',')}" var="sz">
                                                                    <c:set var="trimmedSz" value="${fn:trim(sz)}" />
                                                                    <span class="mr-2" style="white-space: nowrap;">
                                                                        <strong>S${trimmedSz}:</strong>
                                                                        <span
                                                                            class="${(sqMap[trimmedSz] != null && sqMap[trimmedSz] > 0) ? 'text-info' : 'text-danger'}">
                                                                            ${sqMap[trimmedSz] != null ?
                                                                            sqMap[trimmedSz] : 0}
                                                                        </span>
                                                                    </span>
                                                                </c:forEach>
                                                            </div>
                                                        </td>
                                                        <td class="small text-warning font-weight-bold">
                                                            ${p.manufacturer}</td>
                                                        <td class="text-muted-custom small">${p.tiltle}</td>
                                                        <td class="text-right pr-4">
                                                            <c:if test="${sessionScope.acc.role == 'owner'}">
                                                                <a class="action-icon edit" href="load?pid=${p.id}"><i
                                                                        class="fa-solid fa-pen-to-square"></i></a>
                                                                <a class="action-icon delete" href="delete?pid=${p.id}"
                                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa mẫu giày này?');"><i
                                                                        class="fa-solid fa-trash-can"></i></a>
                                                            </c:if>
                                                            <c:if
                                                                test="${sessionScope.acc.role == 'warehouse_manager'}">
                                                                <span class="helper-text">Chỉ xem tồn và nhập kho</span>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:if test="${productTotalPage > 1}">
                                        <div class="card-body pt-3">
                                            <ul class="pagination justify-content-center mb-0">
                                                <li class="page-item ${productPage <= 1 ? 'disabled' : ''}"><a
                                                        class="page-link"
                                                        href="manager?productPage=${productPage - 1}&stockImportPage=${stockImportPage}&dailyStockPage=${dailyStockPage}">Trước</a>
                                                </li>
                                                <c:forEach begin="1" end="${productTotalPage}" var="i">
                                                    <li class="page-item ${productPage == i ? 'active' : ''}"><a
                                                            class="page-link"
                                                            href="manager?productPage=${i}&stockImportPage=${stockImportPage}&dailyStockPage=${dailyStockPage}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                                <li
                                                    class="page-item ${productPage >= productTotalPage ? 'disabled' : ''}">
                                                    <a class="page-link"
                                                        href="manager?productPage=${productPage + 1}&stockImportPage=${stockImportPage}&dailyStockPage=${dailyStockPage}">Sau</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </c:if>
                                </div>

                                <div class="row">
                                    <div class="col-lg-7 mb-4">
                                        <div class="card h-100">
                                            <div class="card-header"><i
                                                    class="fa-solid fa-clock-rotate-left text-info mr-2"></i>Lịch sử
                                                nhập kho</div>
                                            <div class="card-body p-0 table-responsive">
                                                <table class="table table-hover mb-0">
                                                    <thead>
                                                        <tr>
                                                            <th>Sản phẩm</th>
                                                            <th>Số lượng</th>
                                                            <th>Chi tiết</th>
                                                            <th>Ngày giờ</th>
                                                            <th>Nhân viên</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${stockImports}" var="item">
                                                            <tr>
                                                                <td>${item.productName}</td>
                                                                <td class="font-weight-bold text-info">
                                                                    ${item.importQuantity} đôi</td>
                                                                <td>${item.note}</td>
                                                                <td class="small">${item.createdAt}</td>
                                                                <td class="font-weight-bold text-warning">
                                                                    ${item.createdByName}</td>
                                                            </tr>
                                                        </c:forEach>
                                                        <c:if test="${empty stockImports}">
                                                            <tr>
                                                                <td colspan="5"
                                                                    class="text-center py-4 text-muted-custom">Chưa có
                                                                    lịch sử nhập kho</td>
                                                            </tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <c:if test="${stockImportTotalPage > 1}">
                                                <div class="card-body pt-3 border-top">
                                                    <ul class="pagination justify-content-center mb-0">
                                                        <li class="page-item ${stockImportPage <= 1 ? 'disabled' : ''}">
                                                            <a class="page-link"
                                                                href="manager?productPage=${productPage}&stockImportPage=${stockImportPage - 1}&dailyStockPage=${dailyStockPage}">Trước</a>
                                                        </li>
                                                        <c:forEach begin="1" end="${stockImportTotalPage}" var="i">
                                                            <li
                                                                class="page-item ${stockImportPage == i ? 'active' : ''}">
                                                                <a class="page-link"
                                                                    href="manager?productPage=${productPage}&stockImportPage=${i}&dailyStockPage=${dailyStockPage}">${i}</a>
                                                            </li>
                                                        </c:forEach>
                                                        <li
                                                            class="page-item ${stockImportPage >= stockImportTotalPage ? 'disabled' : ''}">
                                                            <a class="page-link"
                                                                href="manager?productPage=${productPage}&stockImportPage=${stockImportPage + 1}&dailyStockPage=${dailyStockPage}">Sau</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="col-lg-5 mb-4">
                                        <div class="card h-100">
                                            <div class="card-header"><i
                                                    class="fa-solid fa-calendar-days text-warning mr-2"></i>Tổng nhập
                                                theo ngày</div>
                                            <div class="card-body p-0 table-responsive">
                                                <table class="table table-hover mb-0">
                                                    <thead>
                                                        <tr>
                                                            <th>Ngày</th>
                                                            <th>Tổng nhập</th>
                                                            <th>Cập nhật cuối</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${dailyStockImports}" var="item">
                                                            <tr>
                                                                <td class="font-weight-bold">${item.createdDate}</td>
                                                                <td class="font-weight-bold text-info">
                                                                    ${item.importQuantity} đôi</td>
                                                                <td class="small">${item.createdTime}</td>
                                                            </tr>
                                                        </c:forEach>
                                                        <c:if test="${empty dailyStockImports}">
                                                            <tr>
                                                                <td colspan="3"
                                                                    class="text-center py-4 text-muted-custom">Chưa có
                                                                    dữ liệu thống kê</td>
                                                            </tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <c:if test="${dailyStockTotalPage > 1}">
                                                <div class="card-body pt-3 border-top">
                                                    <ul class="pagination justify-content-center mb-0">
                                                        <li class="page-item ${dailyStockPage <= 1 ? 'disabled' : ''}">
                                                            <a class="page-link"
                                                                href="manager?productPage=${productPage}&stockImportPage=${stockImportPage}&dailyStockPage=${dailyStockPage - 1}">Trước</a>
                                                        </li>
                                                        <c:forEach begin="1" end="${dailyStockTotalPage}" var="i">
                                                            <li
                                                                class="page-item ${dailyStockPage == i ? 'active' : ''}">
                                                                <a class="page-link"
                                                                    href="manager?productPage=${productPage}&stockImportPage=${stockImportPage}&dailyStockPage=${i}">${i}</a>
                                                            </li>
                                                        </c:forEach>
                                                        <li
                                                            class="page-item ${dailyStockPage >= dailyStockTotalPage ? 'disabled' : ''}">
                                                            <a class="page-link"
                                                                href="manager?productPage=${productPage}&stockImportPage=${stockImportPage}&dailyStockPage=${dailyStockPage + 1}">Sau</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
                            <script
                                src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
                            <script>
                                // Chuyen du lieu ton kho size sang JS
                                const sizeStockData = {
                <c:forEach items="${sizeQuantitiesMap}" var="entry" varStatus="status">
                                    "${entry.key}": {
                                    <c:forEach items="${entry.value}" var="szEntry" varStatus="szStatus">
                                        "${szEntry.key}": ${szEntry.value}${not szStatus.last ? ',' : ''}
                                    </c:forEach>
                                }${not status.last ? ',' : '' }
                </c:forEach>
            };

                                (function () {
                                    const productSelect = document.getElementById('stockProductSelect');
                                    const sizeGrid = document.getElementById('sizeGrid');
                                    if (!productSelect || !sizeGrid) {
                                        return;
                                    }

                                    const existingValues = {};
                                    sizeGrid.querySelectorAll('input[name^="size_"]').forEach(function (input) {
                                        existingValues[input.name.replace('size_', '')] = input.value;
                                    });

                                    function renderSizeInputs() {
                                        const selectedOption = productSelect.options[productSelect.selectedIndex];
                                        const pid = productSelect.value;
                                        const sizesAttr = selectedOption ? selectedOption.getAttribute('data-sizes') || '' : '';
                                        const sizes = sizesAttr.split(',').map(function (size) {
                                            return size.trim();
                                        }).filter(function (size) {
                                            return size.length > 0;
                                        });

                                        sizeGrid.innerHTML = '';
                                        const currentStock = sizeStockData[pid] || {};

                                        sizes.forEach(function (size) {
                                            const wrapper = document.createElement('div');
                                            wrapper.className = 'mb-3';

                                            const label = document.createElement('div');
                                            label.className = 'small helper-text mb-1';
                                            const qty = currentStock[size] || 0;
                                            label.innerHTML = 'Size ' + size + ' (Hiện có: <span class="' + (qty > 0 ? 'text-info' : 'text-danger') + '">' + qty + '</span>)';

                                            const input = document.createElement('input');
                                            input.className = 'form-control';
                                            input.type = 'number';
                                            input.min = '0';
                                            input.name = 'size_' + size;
                                            input.placeholder = '+ Nhập thêm số lượng';
                                            input.value = Object.prototype.hasOwnProperty.call(existingValues, size) ? existingValues[size] : '';
                                            input.addEventListener('input', function () {
                                                existingValues[size] = input.value;
                                            });

                                            wrapper.appendChild(label);
                                            wrapper.appendChild(input);
                                            sizeGrid.appendChild(wrapper);
                                        });
                                    }

                                    productSelect.addEventListener('change', renderSizeInputs);
                                    renderSizeInputs();
                                })();
                            </script>
                </body>

                </html>