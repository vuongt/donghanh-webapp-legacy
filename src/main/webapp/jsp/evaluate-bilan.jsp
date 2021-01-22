<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <div>
        <h1 align="center">Bảng tổng kết trường ${university}</h1>
        <h3 align="center">Hệ số giám khảo 0 (VN): ${bilan.vnCoef} - Tổng hệ số các giám khảo ${bilan.evaluatedBy}: ${1 - bilan.vnCoef}</h3>
        <table class="table table-bordered table-hover" id='table_${university}_bilan'>
            <thead class="table-primary">
                <th>Mã số</th>
                <th>Họ</th>
                <th>Tên</th>
                <c:forEach items="${bilan.juryNames}" var="juryName">
                <th>${juryName}</th>
                </c:forEach>
                <th>Điểm tổng kết</th>
                <th>Chọn</th>
            </thead>
            <c:forEach items="${bilan.candidates}" var="candidate">
            <tr>
                <td>${candidate.code}</td>
                <td>${candidate.last_name}</td>
                <td>${candidate.first_name}</td>
                <c:forEach items="${candidate.scores}" var="score">
                <td>${score}</td>
                </c:forEach>
                <td><b>${candidate.finalScore}</b></td>
                <c:if test = "${candidate.selected}">
                <td><input type='checkbox' class='selected' id='selected_${candidate.code}' checked></td>
                </c:if>
                <c:if test = "${!candidate.selected}">
                <td><input type='checkbox' class='selected' id='selected_${candidate.code}'></td>
                </c:if>
            </tr>
            </c:forEach>
        </table>
        <div class="row justify-content-end">
                <div class="col-2">
                    <a id='confirm_link_final' href=''></a>
                </div>
                <div class="col-2">
                    <button class="btn btn-success btn-block" onclick='update_selection_results("${university}")'>Duyệt kết quả</button>
                </div>
        </div>
        <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <button class="btn btn-lg btn-block btn-success"
                            onclick="location.href='manage?action=result&university=${university}'">
                        Tải thông báo kết quả
                    </button>
                </div>
                <div class="col-md-4"></div>
            </div>
    </div>