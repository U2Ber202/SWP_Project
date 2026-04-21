<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Duy trì thông báo Toast -->
<div id="toast-container" style="position: fixed; top: 100px; right: 20px; z-index: 9999; pointer-events: none;">
    <%-- Xử lý thông báo Lỗi (Red) --%>
    <c:set var="errorMsg" value="${not empty mess ? mess : (not empty error ? error : (not empty errorStore ? errorStore : (not empty productError ? productError : (not empty warehouseError ? warehouseError : (not empty shipperError ? shipperError : (not empty stockError ? stockError : ''))))))}" />
    <c:if test="${not empty errorMsg}">
        <div class="toast-item alert alert-danger shadow-lg animate__animated animate__fadeInRight" 
             style="min-width: 320px; border-radius: 12px; pointer-events: auto; display: flex; align-items: center; border-left: 5px solid #ef4444; background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); color: white; margin-bottom: 10px; padding: 15px 20px;">
            <i class="fas fa-circle-exclamation mr-3" style="font-size: 1.3rem; color: #ef4444;"></i>
            <div style="flex-grow: 1; font-weight: 500;">${errorMsg}</div>
            <button type="button" class="close ml-3 text-white" onclick="this.parentElement.remove()" style="opacity: 0.5;">&times;</button>
        </div>
        <c:remove var="error" scope="session" />
        <c:remove var="mess" scope="session" />
        <c:remove var="errorStore" scope="session" />
        <c:remove var="productError" scope="session" />
        <c:remove var="warehouseError" scope="session" />
        <c:remove var="shipperError" scope="session" />
        <c:remove var="stockError" scope="session" />
    </c:if>

    <%-- Xử lý thông báo Thành công (Green) --%>
    <c:set var="successMsg" value="${not empty successMess ? successMess : (not empty success ? success : (not empty message ? message : (not empty warehouseMessage ? warehouseMessage : (not empty shipperMessage ? shipperMessage : (not empty stockSuccess ? stockSuccess : (not empty cartMessage ? cartMessage : ''))))))}" />
    <c:if test="${not empty successMsg}">
        <div class="toast-item alert alert-success shadow-lg animate__animated animate__fadeInRight" 
             style="min-width: 320px; border-radius: 12px; pointer-events: auto; display: flex; align-items: center; border-left: 5px solid #22c55e; background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); color: white; margin-bottom: 10px; padding: 15px 20px;">
            <i class="fas fa-circle-check mr-3" style="font-size: 1.3rem; color: #22c55e;"></i>
            <div style="flex-grow: 1; font-weight: 500;">${successMsg}</div>
            <button type="button" class="close ml-3 text-white" onclick="this.parentElement.remove()" style="opacity: 0.5;">&times;</button>
        </div>
        <c:remove var="success" scope="session" />
        <c:remove var="successMess" scope="session" />
        <c:remove var="message" scope="session" />
        <c:remove var="warehouseMessage" scope="session" />
        <c:remove var="shipperMessage" scope="session" />
        <c:remove var="stockSuccess" scope="session" />
        <c:remove var="cartMessage" scope="session" />
    </c:if>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const toasts = document.querySelectorAll('.toast-item');
        toasts.forEach(toast => {
            // Tự động biến mất sau 5 giây
            setTimeout(() => {
                if (toast.parentElement) {
                    toast.classList.replace('animate__fadeInRight', 'animate__fadeOutRight');
                    setTimeout(() => {
                        toast.remove();
                    }, 800);
                }
            }, 5000);
        });
    });
</script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
