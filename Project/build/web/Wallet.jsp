<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Ví của tôi | V-SNKR</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            :root {
                --primary: #ea580c;
                --bg: #0f172a;
                --card-bg: #1e293b;
                --border: rgba(255, 255, 255, 0.1);
            }
            body { background: var(--bg); color: #f1f5f9; font-family: 'Be Vietnam Pro', sans-serif; }
            .wallet-card {
                background: linear-gradient(135deg, var(--primary), #c2410c);
                border-radius: 30px;
                padding: 40px;
                color: white;
                box-shadow: 0 20px 50px rgba(234, 88, 12, 0.3);
                margin-top: 50px;
                position: relative;
                overflow: hidden;
            }
            .wallet-card::after {
                content: '\f51e';
                font-family: 'Font Awesome 6 Free';
                font-weight: 900;
                position: absolute;
                right: -20px;
                bottom: -20px;
                font-size: 10rem;
                opacity: 0.1;
                transform: rotate(-15deg);
            }
            .transaction-table { background: var(--card-bg); border-radius: 20px; overflow: hidden; border: 1px solid var(--border); }
            .transaction-table th { background: #0f172a; border: none; color: #94a3b8; font-size: 0.8rem; text-transform: uppercase; }
            .transaction-table td { border-top: 1px solid var(--border); vertical-align: middle; }
        </style>
    </head>
    <body>
        <%@ include file="components/navBarComponent.jsp" %>
        
        <div class="container">
            <div class="wallet-card mb-5">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <span class="text-uppercase small font-weight-bold opacity-75">Tổng chi tiêu sắm sửa</span>
                        <h2 class="display-3 font-weight-bold mb-0">
                            <fmt:formatNumber value="${totalSpent}" pattern="#,### đ"/>
                        </h2>
                    </div>
                    <div class="col-md-4 text-md-right mt-3 mt-md-0">
                        <div class="p-3 bg-white text-dark rounded-pill d-inline-block px-4 font-weight-bold">
                            Hạng Thành viên: Bạc
                        </div>
                    </div>
                </div>
            </div>

            <h3 class="font-weight-bold mb-4">Lịch sử giao dịch</h3>
            <div class="transaction-table">
                <table class="table table-dark table-hover mb-0">
                    <thead>
                        <tr>
                            <th class="pl-4">Ngày giao dịch</th>
                            <th>Mã đơn hàng</th>
                            <th>Ghi chú</th>
                            <th class="text-right pr-4">Số tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${orderHistory}" var="o">
                            <tr>
                                <td class="pl-4 text-muted">${o.createdDate}</td>
                                <td class="font-weight-bold">#ORD-${o.id}</td>
                                <td>${o.note}</td>
                                <td class="text-right pr-4 font-weight-bold text-warning">
                                    - <fmt:formatNumber value="${o.totalPrice}" pattern="#,### đ"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty orderHistory}">
                    <div class="p-5 text-center text-muted">Bạn chưa thực hiện giao dịch nào.</div>
                </c:if>
            </div>
        </div>
    </body>
</html>



