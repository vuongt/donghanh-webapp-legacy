<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div>
    <h1 class="h1 p-3" align="center">Danh sách sinh viên được chấm bởi giám khảo ${jury.index}</h1>
    <form action="evaluate" method="post" id='form_${university}_${jury.index}' accept-charset="utf-8">
    <div class="form-group row justify-content-md-center">
        <label class="col-sm-2 col-form-label">Họ và tên giám khảo:</label>
        <div class="col-sm-6">
          <input size='60' class="form-control" style='text-align : center;' type='text' name='juryName' value='${jury.name}'>
        </div>
    </div>

    <table class="table table-bordered table-hover">
        <thead class="table-primary">
            <th>Mã số</th>
            <th>Họ</th>
            <th>Tên</th>
            <th>Hoàn cảnh</th>
            <th>Chuẩn hóa</th>
            <th>Học tập</th>
            <th>Chuẩn hóa</th>
            <th>Ước mơ</th>
            <th>Chuẩn hóa</th>
            <th>Điểm cộng</th>
            <th>Chuẩn hóa</th>
            <th>Tổng</th>
            <th>Chuẩn hóa</th>
        </thead>
        <c:forEach items="${jury.candidates}" var="candidate">
        <tr>
            <td>${candidate.code}</td>
            <td>${candidate.last_name}</td>
            <td>${candidate.first_name}</td>
            <td><input class="form-control" type='number' step='0.1' name='hoancanh_${candidate.code}' value="${candidate.hoancanh}"></td>
            <td align='right'>${candidate.hoancanhnorm}</td>
            <td><input class="form-control" type='number' step='0.1' name='hoctap_${candidate.code}' value="${candidate.hoctap}"></td>
            <td align='right'>${candidate.hoctapnorm}</td>
            <td><input class="form-control" type='number' step='0.1' name='uocmo_${candidate.code}' value="${candidate.uocmo}"></td>
            <td align='right'>${candidate.uocmonorm}</td>
            <td><input class="form-control" type='number' step='0.1' name='diemcong_${candidate.code}' value="${candidate.diemcong}"></td>
            <td align='right'>${candidate.diemcongnorm}</td>
            <td align='right'><b>${candidate.tongket}</b></td>
            <td align='right'><b>${candidate.tongketnorm}</b></td>
        </tr>
        </c:forEach>
        <tr align='right'>
            <td>Trung bình</td>
            <td></td>
            <td></td>
            <td>${jury.average.hoancanh}</td>
            <td>${jury.average.hoancanhnorm}</td>
            <td>${jury.average.hoctap}</td>
            <td>${jury.average.hoctapnorm}</td>
            <td>${jury.average.uocmo}</td>
            <td>${jury.average.uocmonorm}</td>
            <td>${jury.average.diemcong}</td>
            <td>${jury.average.diemcongnorm}</td>
            <td><b>${jury.average.tongket}</b></td>
            <td><b>${jury.average.tongketnorm}</b></td>
        </tr>
        <tr align='right'>
            <td>Độ lệch chuẩn</td>
            <td></td>
            <td></td>
            <td>${jury.stddev.hoancanh}</td>
            <td>${jury.stddev.hoancanhnorm}</td>
            <td>${jury.stddev.hoctap}</td>
            <td>${jury.stddev.hoctapnorm}</td>
            <td>${jury.stddev.uocmo}</td>
            <td>${jury.stddev.uocmonorm}</td>
            <td>${jury.stddev.diemcong}</td>
            <td>${jury.stddev.diemcongnorm}</td>
            <td><b>${jury.stddev.tongket}</b></td>
            <td><b>${jury.stddev.tongketnorm}</b></td>
        </tr>
    </table>
    <div class="row justify-content-end">
        <div class="col-2">
            <input class="form-control" type="hidden" name='juryIndex' value='${jury.index}'></input>
            <input class="form-control" type="hidden" name='university' value='${university}'></input>
            <input type='submit' class="btn btn-success btn-block" value='Lưu kết quả'></input>
        </div>
    </div>
    </form>
</div>
