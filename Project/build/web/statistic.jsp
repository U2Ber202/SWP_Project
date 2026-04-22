<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Báo cáo doanh thu | V-SNKR Admin</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        
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
                font-family: 'Outfit', sans-serif;
                background-color: var(--bg) !important;
                color: #f1f5f9;
                padding-bottom: 40px;
            }

            .admin-wrapper {
                background: var(--card-bg);
                backdrop-filter: blur(12px);
                border: 1px solid var(--border);
                border-radius: 20px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.4);
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

            .btn-custom-secondary {
                background-color: transparent;
                color: #94a3b8;
                border: 1px solid var(--border);
                border-radius: 12px;
                font-weight: 600;
                padding: 10px 25px;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
            }

            .btn-custom-secondary:hover {
                background-color: var(--glass);
                color: white;
                border-color: var(--border);
            }

            .dashboard-body {
                padding: 40px 30px;
            }

            .section-title {
                font-weight: 700;
                color: #ffffff;
                text-transform: uppercase;
                letter-spacing: 1px;
                margin-bottom: 25px;
                font-size: 1.2rem;
                border-left: 4px solid var(--primary);
                padding-left: 12px;
            }

            .stat-card {
                background: rgba(0, 0, 0, 0.2);
                border-radius: 16px;
                padding: 25px;
                display: flex;
                align-items: center;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                border: 1px solid var(--border);
                transition: all 0.3s ease;
                height: 100%;
            }

            .stat-icon {
                width: 65px;
                height: 65px;
                border-radius: 14px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.8rem;
                margin-right: 20px;
                flex-shrink: 0;
            }

            .stat-details {
                flex-grow: 1;
            }

            .stat-title {
                font-size: 0.85rem;
                font-weight: 600;
                color: #94a3b8;
                margin-bottom: 5px;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .stat-value {
                font-size: 1.8rem;
                font-weight: 800;
                color: #ffffff;
                margin: 0;
            }

            .bg-light-success { background: rgba(74, 222, 128, 0.15); color: #4ade80; }
            .text-success-custom { color: #4ade80 !important; }
            
            .table {
                color: #f1f5f9;
            }
            .table-hover tbody tr:hover {
                background-color: rgba(255, 255, 255, 0.05);
                color: white;
            }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>
        <%@ include file="components/navBarComponent.jsp" %>
        <div class="container" style="margin-top: 100px;">
            <div class="admin-wrapper mb-4">
                
                <div class="admin-header">
                    <h2><i class="fa-solid fa-chart-line"></i> Báo cáo <b>Doanh Thu</b></h2>
                    <a href="home" class="btn-custom-secondary">
                        <i class="fa-solid fa-house me-2"></i> Trang Chủ
                    </a>
                </div>

                <div class="dashboard-body">
                    
                    <!-- Filter Section -->
                    <div class="mb-5">
                        <form action="statistic" method="get" class="row g-3 align-items-end">
                            <div class="col-md-4">
                                <label class="form-label text-muted small text-uppercase fw-bold">Từ ngày</label>
                                <input type="date" name="startDate" class="form-control bg-dark text-white border-secondary" value="${startDate}" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label text-muted small text-uppercase fw-bold">Đến ngày</label>
                                <input type="date" name="endDate" class="form-control bg-dark text-white border-secondary" value="${endDate}" required>
                            </div>
                            <div class="col-md-4">
                                <button type="submit" class="btn btn-primary w-100" style="background: var(--primary); border: none; height: 38px; font-weight: 600;">
                                    <i class="fa-solid fa-filter me-2"></i> Lọc báo cáo
                                </button>
                            </div>
                        </form>
                    </div>

                    <!-- Summary Section -->
                    <div class="row mb-5">
                        <div class="col-12">
                            <div class="stat-card" style="border-left: 5px solid var(--primary);">
                                <div class="stat-icon bg-light-success">
                                    <i class="fa-solid fa-money-bill-trend-up"></i>
                                </div>
                                <div class="stat-details">
                                    <p class="stat-title">Tổng doanh thu trong kỳ</p>
                                    <h3 class="stat-value text-success-custom" style="font-size: 2.5rem;">
                                        <fmt:formatNumber value="${totalRevenue}" pattern="#,###"/> đ
                                    </h3>
                                    <p class="text-muted mb-0">
                                        Thời gian: 
                                        <fmt:formatDate value="${startDate}" pattern="dd/MM/yyyy"/> - 
                                        <fmt:formatDate value="${endDate}" pattern="dd/MM/yyyy"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Detailed Table -->
                    <h3 class="section-title">Chi tiết doanh thu từng ngày</h3>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr style="background: rgba(255,255,255,0.05);">
                                    <th class="py-3 px-4">Ngày</th>
                                    <th class="py-3 px-4">Định dạng</th>
                                    <th class="py-3 px-4 text-end">Doanh thu</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty listRevenue}">
                                        <c:forEach items="${listRevenue}" var="item">
                                            <tr style="border-bottom: 1px solid var(--border);">
                                                <td class="py-3 px-4">
                                                    <fmt:formatDate value="${item.date}" pattern="EEEE"/>
                                                </td>
                                                <td class="py-3 px-4">
                                                    <fmt:formatDate value="${item.date}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td class="py-3 px-4 text-end fw-bold">
                                                    <fmt:formatNumber value="${item.revenue}" pattern="#,###"/> đ
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="3" class="text-center py-5 text-muted">
                                                <i class="fa-solid fa-folder-open fa-3x mb-3 d-block"></i>
                                                Không có dữ liệu doanh thu trong khoảng thời gian này.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                            <tfoot style="background: rgba(255,255,255,0.05); border-top: 2px solid var(--primary);">
                                <tr>
                                    <td colspan="2" class="py-3 px-4 fw-bold">TỔNG CỘNG</td>
                                    <td class="py-3 px-4 text-end fw-bold text-success-custom">
                                        <fmt:formatNumber value="${totalRevenue}" pattern="#,###"/> đ
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                </div>
            </div>
            
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
