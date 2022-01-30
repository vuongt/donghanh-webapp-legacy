<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row">
    <div class="col-md-3">
        <button type="button" class="btn btn-primary" onclick="location.href='dashboard'">Quay lại trang chủ</button>
    </div>
    <div class="col-md-3">
    </div>
    <div class="col-md-3">
    </div>
    <div class="col-md-3">
    <c:if test = "${userProfile == 'ADMIN' || userProfile == 'SUPER_USER'}">
    <div class="float-right">
        <button type="button" class="btn btn-success" onclick="location.href='manage?action=form&university=${university}'">Tải phiếu chấm</button>
    </div>
    </c:if>
    </div>
</div>