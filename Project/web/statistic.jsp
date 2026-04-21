<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thống Kê Doanh Số | V-SNKR Admin</title>
        
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        
        <style>
            /* Đồng bộ biến màu sắc với toàn hệ thống */
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
                padding-top: 40px;
                padding-bottom: 40px;
            }

            /* Main Card Wrapper - Glassmorphism */
            .admin-wrapper {
                background: var(--card-bg);
                backdrop-filter: blur(12px);
                border: 1px solid var(--border);
                border-radius: 20px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.4);
                overflow: hidden;
            }

            /* Header */
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

            /* Nút bấm quay lại */
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

            /* Dashboard Body */
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

            /* Thẻ thống kê (Stat Cards) dạng kính */
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

            .stat-card:hover {
                transform: translateY(-5px);
                background: rgba(0, 0, 0, 0.3);
                border-color: var(--primary);
                box-shadow: 0 10px 25px rgba(0,0,0,0.3);
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

            /* Icon Color Variations cho nền tối */
            .bg-light-primary { background: rgba(234, 88, 12, 0.15); color: #fb923c; }
            .bg-light-success { background: rgba(74, 222, 128, 0.15); color: #4ade80; }
            .bg-light-info { background: rgba(56, 189, 248, 0.15); color: #38bdf8; }
            .bg-light-warning { background: rgba(251, 191, 36, 0.15); color: #fbbf24; }
            
            .text-success-custom { color: #4ade80 !important; }
        </style>
        <script src="js/theme.js"></script>
        <link rel="stylesheet" href="css/theme.css">
    </head>
    <body>

        <div class="container">
            <div class="admin-wrapper mb-4">
                
                <div class="admin-header">
                    <h2><i class="fa-solid fa-chart-pie"></i> Bảng <b>Thống Kê</b></h2>
                    <a href="home" class="btn-custom-secondary">
                        <i class="fa-solid fa-house me-2"></i> Trang Chủ
                    </a>
                </div>

                <div class="dashboard-body">
                    
                    <h3 class="section-title">Kết Quả Kinh Doanh Hôm Nay</h3>
                    <div class="row mb-5">
                        <div class="col-md-6 mb-4 mb-md-0">
                            <div class="stat-card">
                                <div class="stat-icon bg-light-primary">
                                    <i class="fa-solid fa-cart-shopping"></i>
                                </div>
                                <div class="stat-details">
                                    <p class="stat-title">Đơn hàng mới</p>
                                    <h3 class="stat-value">
                                        ${not empty totalOrders ? totalOrders : 0}
                                    </h3>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="stat-card">
                                <div class="stat-icon bg-light-success">
                                    <i class="fa-solid fa-money-bill-wave"></i>
                                </div>
                                <div class="stat-details">
                                    <p class="stat-title">Doanh thu trong ngày</p>
                                    <h3 class="stat-value text-success-custom">
                                        <fmt:formatNumber value="${not empty totalSales ? totalSales : 0}" pattern="#,###"/> đ
                                    </h3>
                                </div>
                            </div>
                        </div>
                    </div>

                    <h3 class="section-title">Tổng Kết Tháng Này</h3>
                    <div class="row">
                        <div class="col-md-6 mb-4 mb-md-0">
                            <div class="stat-card">
                                <div class="stat-icon bg-light-info">
                                    <i class="fa-solid fa-boxes-stacked"></i>
                                </div>
                                <div class="stat-details">
                                    <p class="stat-title">Tổng đơn hàng</p>
                                    <h3 class="stat-value">
                                        ${not empty totalOrdersMonth ? totalOrdersMonth : 0}
                                    </h3>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="stat-card">
                                <div class="stat-icon bg-light-warning">
                                    <i class="fa-solid fa-vault"></i>
                                </div>
                                <div class="stat-details">
                                    <p class="stat-title">Tổng doanh thu</p>
                                    <h3 class="stat-value text-success-custom">
                                        <fmt:formatNumber value="${not empty totalSalesMonth ? totalSalesMonth : 0}" pattern="#,###"/> đ
                                    </h3>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
