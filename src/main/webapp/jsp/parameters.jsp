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
    <script type="text/javascript" src="normalize.js" ></script>

</head>
<body>
    <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
        <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Dong Hanh Web App</a>
        <input class="form-control form-control-dark w-100" type="text" aria-label="Search">
        <ul class="navbar-nav px-3">
            <li class="nav-item text-nowrap">
                <a class="nav-link" href="Parameters?action=LOG_OUT">Sign out</a>
            </li>
        </ul>
    </nav>
    <div class="container-fluid px-5">
        <div class="row">
            <main role="main" class="col-md-12 ml-sm-auto col-lg-12 pt-3 px-4">
                <div class="row">
                    <div class="col-md-3">
                        <button type="button" class="btn btn-primary" onclick="location.href='Techies.html'">Quay lại trang chủ</button>
                    </div>
                </div>
                <br>
                <!-- TODO: USE A LOOP FOR THIS -->
                <h1 class="h1 p-3" align="center">Tham số DH France</h1>
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Mã trường</th><th>Tên trường</th><th>Tên quỹ học bổng</th><th>Đối tượng</th><th>Nơi chấm</th><th>Logo</th><th>Hệ số VN</th><th>Số gk/hồ sơ</th><th>Số hồ sơ max</th><th></th>
                    </thead>
                    <c:forEach items="${universities}" var="uni">
                        <c:if test = "${uni.evaluatedBy == 'FR'}">
                            <tr>
                                <td>${uni.code}</td>
                                <td><input type='text' size='30' id='UniversityName_${uni.code}' value='${uni.name}'></input></td>
                                <td><input type='text' size='20' id='FoundationName_${uni.code}' value='${uni.code}'></input></td>
                                <td><input type='text' size='10' id='StudentClass_${uni.code}' value='${uni.studentClass}'></input></td>
                                <td><input type='text' size='5' id='EvaluatedBy_${uni.code}' value='${uni.evaluatedBy}'></input></td>
                                <td><input type='text' size='20' id='Logo_${uni.code}' value='${uni.logo}'></input></td>
                                <td><input type='text' size='5' id='VnCoefs_${uni.code}' value='${uni.vnCoefs}'></input></td>
                                <td><input type='text' size='5' id='NbJugesByCopy_${uni.code}' value='${uni.nbJuriesByCopy}'></input></td>
                                <td><input type='text' size='5' id='MaxDocs_${uni.code}' value='${uni.maxDocs}'></input></td>
                                <td><button type='button' size='10' onclick='set_university_params("${uni.code}")'>Cập nhật</button><p><a href='' id='confirm_university_${uni.code}'></a></p></td>
                            </tr>
                        </c:if>

                    </c:forEach>
                </table>
                <h1 class="h1 p-3" align="center">Tham số DH Singapore</h1>
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Mã trường</th><th>Tên trường</th><th>Tên quỹ học bổng</th><th>Đối tượng</th><th>Nơi chấm</th><th>Logo</th><th>Hệ số VN</th><th>Số gk/hồ sơ</th><th>Số nhóm</th><th></th>
                    </thead>
                    <c:forEach items="${universities}" var="uni">
                        <c:if test = "${uni.evaluatedBy == 'SG'}">
                            <tr>
                                <td>${uni.code}</td>
                                <td><input type='text' size='30' id='UniversityName_${uni.code}' value='${uni.name}'></input></td>
                                <td><input type='text' size='20' id='FoundationName_${uni.code}' value='${uni.code}'></input></td>
                                <td><input type='text' size='10' id='StudentClass_${uni.code}' value='${uni.studentClass}'></input></td>
                                <td><input type='text' size='5' id='EvaluatedBy_${uni.code}' value='${uni.evaluatedBy}'></input></td>
                                <td><input type='text' size='20' id='Logo_${uni.code}' value='${uni.logo}'></input></td>
                                <td><input type='text' size='5' id='VnCoefs_${uni.code}' value='${uni.vnCoefs}'></input></td>
                                <td><input type='text' size='5' id='NbJugesByCopy_${uni.code}' value='${uni.nbJuriesByCopy}'></input></td>
                                <td><input type='text' size='5' id='MaxDocs_${uni.code}' value='${uni.maxDocs}'></input></td>
                                <td><button type='button' size='10' onclick='set_university_params("${uni.code}")'>Cập nhật</button><p><a href='' id='confirm_university_${uni.code}'></a></p></td>
                            </tr>
                        </c:if>

                    </c:forEach>
                </table>

                <h1 class="h1 p-3" align="center">Cài đặt</h1>
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Tham số</th><th>Giá trị</th>
                    </thead>
                    <c:forEach items="${parameters}" var="parameter">
                        <tr>
                            <td>${parameter.name}</td>
                            <td>
                                <div class="form-group row justify-content-md-center">
                                    <div class="col-sm-6"><input class="form-control" type='text' size='80' id='input_${parameter.name}' value='${parameter.value}'></input></div>
                                    <div class="col-sm-6"><button class="btn btn-primary" onclick='set_params("${parameter.name}")'>Cập nhật</button>
                                    <p><a href='' id='confirm_${parameter.name}'></a></p></div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

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

</body>
</html>
