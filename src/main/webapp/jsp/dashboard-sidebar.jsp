<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="col-md-2 d-none d-md-block bg-light sidebar" style="overflow: hidden;">
    <div class="sidebar-sticky">
        <c:if test = "${userProfile == 'ADMIN' || userProfile == 'SUPER_USER'}">

        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link" href="candidate">
                    <span data-feather="database"></span>
                    Toàn bộ dữ liệu sinh viên
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="parameter">
                    <span data-feather="sliders"></span>
                    Chỉnh sửa tham số
                </a>
            </li>
            <c:if test = "${userProfile == 'SUPER_USER'}">
            <li class="nav-item">
                <a class="nav-link" href="admin">
                    <span data-feather="settings"></span>
                    Admin
                </a>
            </li>
            </c:if>
        </ul>
        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Kích hoạt dữ liệu sinh viên theo trường</span>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="#activate-FR">
                    <span data-feather="users"></span>
                    Đồng Hành France
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#activate-SG">
                    <span data-feather="users"></span>
                    Đồng Hành Singapore
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#activate-kr">
                    <span data-feather="users"></span>
                    Đồng Hành Korea
                </a>
            </li>
        </ul>
        </c:if>
        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Chấm sinh viên theo trường</span>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="#eval-FR">
                    <span data-feather="pen-tool"></span>
                    Đồng Hành France
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#eval-SG">
                    <span data-feather="pen-tool"></span>
                    Đồng Hành Singapore
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#eval-KR">
                    <span data-feather="pen-tool"></span>
                    Đồng Hành Korea
                </a>
            </li>
        </ul>
        <c:if test = "${userProfile == 'ADMIN' || userProfile == 'SUPER_USER'}">
        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Kết quả chính thức</span>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="#result-FR">
                    <span data-feather="file-text"></span>
                    Đồng Hành France
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#result-SG">
                    <span data-feather="file-text"></span>
                    Đồng Hành Singapore
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#result-KR">
                    <span data-feather="file-text"></span>
                    Đồng Hành Korea
                </a>
            </li>
        </ul>
        </c:if>
    </div>
</nav>