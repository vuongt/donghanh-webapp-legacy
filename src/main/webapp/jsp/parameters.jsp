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
    <link href="../css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="../css/dashboard.css" rel="stylesheet"/>

</head>
<body>
<%@ include file="components/header.jsp" %>

    <div class="container-fluid px-5">
        <div class="row">
            <main role="main" class="col-md-12 ml-sm-auto col-lg-12 pt-3 px-4">
            <%@ include file="components/back-dashboard.jsp" %>
                <br>
                <h1 class="h1 p-3" align="center">Tham số DH France</h1>
                <form action="parameter" method="post" accept-charset="utf-8">
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Mã trường</th><th>Tên trường</th><th>Tên quỹ học bổng</th><th>Đối tượng</th><th>Nơi chấm</th><th>Logo</th><th>Hệ số VN</th><th>Số gk/hồ sơ</th><th>Tổng số giám khảo</th>
                    </thead>
                    <c:forEach items="${universityParams}" var="uni">
                        <c:if test = "${uni.evaluatedBy() == 'FR'}">
                            <tr>
                                <td>${uni.code()}</td>
                                <td>
                                    <input class="form-control" type="hidden" required name='${uni.code()}' value='submit'></input>
                                    <input class="form-control" type='text' required name='${uni.code()}_name' value='${uni.name()}'></input>
                                </td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_foundation' value='${uni.foundationName()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_studentClass' value='${uni.studentClass()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_evaluatedBy' value='${uni.evaluatedBy()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_logo' value='${uni.logo()}'></input></td>
                                <td><input class="form-control" type='number' step='0.1' required name='${uni.code()}_vnCoef' value='${uni.vnCoef()}'></input></td>
                                <td><input class="form-control" type='number' required name='${uni.code()}_nbJuriesByCopy' value='${uni.nbJuriesByCopy()}'></input></td>
                                <td><input class="form-control" type='number' required name='${uni.code()}_nbJuries' value='${uni.nbJuries()}'></input></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
                <input class="form-control" type="hidden" name='action' value='updateUniversityParams'></input>
                <input class="btn btn-primary" type='submit'></input>
                </form>

                <h1 class="h1 p-3" align="center">Tham số DH Singapore</h1>
                <form action="parameter" method="post" accept-charset="utf-8">
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Mã trường</th><th>Tên trường</th><th>Tên quỹ học bổng</th><th>Đối tượng</th><th>Nơi chấm</th><th>Logo</th><th>Hệ số VN</th><th>Số gk/hồ sơ</th><th>Số nhóm</th>

                    </thead>
                    <c:forEach items="${universityParams}" var="uni">
                        <c:if test = "${uni.evaluatedBy() == 'SG'}">
                            <tr>
                                <td>${uni.code()}</td>
                                <td>
                                    <input class="form-control" type="hidden" required name='${uni.code()}' value='submit'></input>
                                    <input class="form-control" type='text' required name='${uni.code()}_name' value='${uni.name()}'></input>
                                </td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_foundation' value='${uni.foundationName()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_studentClass' value='${uni.studentClass()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_evaluatedBy' value='${uni.evaluatedBy()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_logo' value='${uni.logo()}'></input></td>
                                <td><input class="form-control" type='number' step='0.1' required name='${uni.code()}_vnCoef' value='${uni.vnCoef()}'></input></td>
                                <td><input class="form-control" type='number' required name='${uni.code()}_nbJuriesByCopy' value='${uni.nbJuriesByCopy()}'></input></td>
                                <td><input class="form-control" type='number' required name='${uni.code()}_nbJuries' value='${uni.nbJuries()}'></input></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
                <input class="form-control" type="hidden" name='action' value='updateUniversityParams'></input>
                <input class="btn btn-primary" type='submit'></input>
                </form>

                <h1 class="h1 p-3" align="center">Tham số DH Korea</h1>
                <form action="parameter" method="post" accept-charset="utf-8">
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Mã trường</th><th>Tên trường</th><th>Tên quỹ học bổng</th><th>Đối tượng</th><th>Nơi chấm</th><th>Logo</th><th>Hệ số VN</th><th>Số gk/hồ sơ</th><th>Tổng số giám khảo</th>
                    </thead>
                    <c:forEach items="${universityParams}" var="uni">
                        <c:if test = "${uni.evaluatedBy() == 'KR'}">
                            <tr>
                                <td>${uni.code()}</td>
                                <td>
                                    <input class="form-control" type="hidden" required name='${uni.code()}' value='submit'></input>
                                    <input class="form-control" type='text' required name='${uni.code()}_name' value='${uni.name()}'></input>
                                </td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_foundation' value='${uni.foundationName()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_studentClass' value='${uni.studentClass()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_evaluatedBy' value='${uni.evaluatedBy()}'></input></td>
                                <td><input class="form-control" type='text' required name='${uni.code()}_logo' value='${uni.logo()}'></input></td>
                                <td><input class="form-control" type='number' step='0.1' required name='${uni.code()}_vnCoef' value='${uni.vnCoef()}'></input></td>
                                <td><input class="form-control" type='number' required name='${uni.code()}_nbJuriesByCopy' value='${uni.nbJuriesByCopy()}'></input></td>
                                <td><input class="form-control" type='number' required name='${uni.code()}_nbJuries' value='${uni.nbJuries()}'></input></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
                <input class="form-control" type="hidden" name='action' value='updateUniversityParams'></input>
                <input class="btn btn-primary" type='submit'></input>
                </form>

                <h1 class="h1 p-3" align="center">Cài đặt</h1>
                <form action="parameter" method="post" accept-charset="utf-8">
                <table class="table table-bordered table-hover" border="1" align="center">
                    <thead class="table-primary">
                        <th>Tham số</th><th>Giá trị</th>
                    </thead>
                    <c:forEach items="${parameters}" var="parameter">
                        <tr>
                            <td>${parameter.key}</td>
                            <td>
                            <input class="form-control" type='text' size='80' name='${parameter.key}' value='${parameter.value}'></input>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <input class="form-control" type="hidden" name='action' value='updateCommonParams'></input>
                <input class="btn btn-primary" type='submit'></input>
                </form>

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
            <script>window.jQuery || document.write('<script src="../js/vendor/jquery-slim.min.js"><\/script>')</script>
            <script src="../js/vendor/popper.min.js"></script>
            <script src="../js/bootstrap.min.js"></script>

            <!-- Icons -->
            <script src="../js/feather.min.js"></script>
            <script>feather.replace()</script>
</body>
</html>
