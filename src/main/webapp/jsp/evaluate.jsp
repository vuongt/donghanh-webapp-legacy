<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${title}</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/dashboard.css" rel="stylesheet">

</head>
<body>
    <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
        <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Dong Hanh Web App</a>
        <input class="form-control form-control-dark w-100" type="text" aria-label="Search">
        <ul class="navbar-nav px-3">
            <li class="nav-item text-nowrap">
                <a class="nav-link" href="Jury?action=LOG_OUT">Sign out</a>
            </li>
        </ul>
    </nav>
    <div class="container-fluid">
        <div class="row">
            <main role="main" class="col-md-12 ml-sm-auto col-lg-12 pt-3 px-4">
            <div class="row">
                <div class="col-md-3">
                    <button type="button" class="btn btn-primary" onclick="location.href='Jury.html'">Quay lại trang chủ</button>
                </div>
            </div>
            <br>
            <div class="alert alert-danger" role="alert">
              Lưu ý: điểm phải là một số nguyên, hoặc số thập phân với dấu CHẤM. Dấu phẩy không được chấp nhận.
            </div>
                <c:if test = "${nbJudges == 0}">
                <h1>Chưa có người chấm<h1>
                </c:if>

                <c:if test = "${nbJudges > 0}">
                <c:forEach items="${juries}" var="jury">
                <h1 class="h1 p-3" align="center">Danh sách sinh viên được chấm bởi giám khảo ${jury.index}</h1>
                <div class="form-group row justify-content-md-center">
                    <label class="col-sm-2 col-form-label">Họ và tên giám khảo:</label>
                    <div class="col-sm-6">
                      <input size='60' class="form-control" style='text-align : center;' type='text' id='jugename${jury.index}' value='${jury.name}'>
                    </div>
                 </div>
                <table class="table table-bordered table-hover" id='table_${university}_${jury.index}'>
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
                        <td><input size='10' style='text-align : right;' class='raw_mark_${jury.index}' type='text' id='hoancanh_${candidate.code}_gk${jury.index}' value="${candidate.hoancanh}"></td>
                        <td align='right'>${candidate.hoancanh}</td>
                        <td><input size='10' style='text-align : right;' class='raw_mark_${jury.index}' type='text' id='hoctap_${candidate.code}_gk${jury.index}' value="${candidate.hoctap}"></td>
                        <td align='right'>${candidate.hoctap}</td>
                        <td><input size='10' style='text-align : right;' class='raw_mark_${jury.index}' type='text' id='uocmo_${candidate.code}_gk${jury.index}' value="${candidate.uocmo}"></td>
                        <td align='right'>${candidate.uocmo}</td>
                        <td><input size='10' style='text-align : right;' class='raw_mark_${jury.index}' type='text' id='diemcong_${candidate.code}_gk${jury.index}' value="${candidate.diemcong}"></td>
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
                    <a id='confirm_link_${jury.index}' href=''></a>
                </div>
                <div class="col-2">
                    <button class="btn btn-success btn-block" onclick='jury_normalizer("${jury.index}", "${university}")'>Lưu để chuẩn hóa</button>
                </div>
              </div>
             <br>
             <br>
        </c:forEach>
    </c:if>

    <footer class="pt-4 my-md-5 pt-md-5 border-top">
        <div class="row">
            <div class="col-12 col-md">
                <h5 class="text-muted"><img class="mb-2" src="images/dh-icon.png" alt="" width="24" height="24">
                Dong Hanh Association</h5>
                <small class="d-block mb-3 text-muted">&copy; 2019-2020</small>
            </div>
        </div>
    </footer>
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
        <script src="js/vendor/popper.min.js"></script>
        <script src="js/bootstrap.min.js"></script>

        <!-- Icons -->
        <script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
        <script>feather.replace()</script>
        <script type="text/javascript" src="normalize.js" ></script>
    </body>
    </html>