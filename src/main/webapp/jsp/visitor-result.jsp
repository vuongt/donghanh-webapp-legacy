<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Tuyet Vuong - Dong Hanh Asso">

    <title>${title}</title>
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="../css/dashboard.css" rel="stylesheet"/>

</head>
<body>
<%@ include file="components/header.jsp" %>

    <div class="container-fluid">
        <div class="row">
            <main role="main" class="col-md-12 ml-sm-auto col-lg-12 pt-3 px-4">
                    <h1 align="center">Bảng tổng kết</h1>
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

                <c:if test = "${nbJudges == 0}">
                <h1>Chưa có người chấm<h1>
                </c:if>

                <c:if test = "${nbJudges > 0}">
                <c:forEach items="${juries}" var="jury">
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
                        <td align='right'>${candidate.hoancanh}</td>
                        <td><input class="form-control" type='number' step='0.1' name='hoctap_${candidate.code}' value="${candidate.hoctap}"></td>
                        <td align='right'>${candidate.hoctap}</td>
                        <td><input class="form-control" type='number' step='0.1' name='uocmo_${candidate.code}' value="${candidate.uocmo}"></td>
                        <td align='right'>${candidate.uocmo}</td>
                        <td><input class="form-control" type='number' step='0.1' name='diemcong_${candidate.code}' value="${candidate.diemcong}"></td>
                        <td align='right'>${candidate.diemcong}</td>
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
             <br>
             <br>
        </c:forEach>
    </c:if>

    <%@ include file="components/footer.jsp" %>
</main>
</div>
</div>
    <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-slim.min.js"><\/script>')</script>
        <script src="../js/vendor/popper.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>

        <!-- Icons -->
        <script src="../js/feather.min.js"></script>
        <script>feather.replace()</script>
    </body>
    </html>