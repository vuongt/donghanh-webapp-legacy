<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Tuyet Vuong - Dong Hanh Asso">

    <title>Dashboard</title>
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="../css/dashboard.css" rel="stylesheet"/>

</head>
<body>
<%@ include file="components/header.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%@ include file="dashboard-sidebar.jsp" %>
        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
            <c:if test = "${userProfile == 'ADMIN' || userProfile == 'SUPER_USER'}">
            <!--            Kích hoạt dữ liệu sinh viên theo trường-->
            <div id="activate"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h2 class="h2 text-primary">Kích hoạt dữ liệu sinh viên theo trường</h2>
            </div>

            <c:forEach items="${locationToTitle}" var="location">
                <div id="activate-${location.key}"
                     class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                    <h4 class="h4">${location.value}</h4>
                </div>
                <div class="row">
                    <c:forEach items="${locationToColumns.get(location.key)}" var="column">
                        <div class="col-md-6">
                            <div class="list-group">
                            <c:forEach items="${column}" var="university">
                                <a class="list-group-item list-group-item-action" href='manage?action=activate&university=${university.code()}'>${university.name()}</a>
                            </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <br>
            </c:forEach>
            </c:if>

            <!--            Chấm sinh viên theo trường-->
            <div id="eval"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h2 class="h2 text-primary">Chấm sinh viên theo trường</h2>
            </div>

            <c:forEach items="${locationToTitle}" var="location">
            <div id="eval-${location.key}"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h4 class="h4">${location.value}</h4>
            </div>
            <div class="row">
                <c:forEach items="${locationToColumns.get(location.key)}" var="column">
                    <div class="col-md-6">
                        <div class="list-group">
                        <c:forEach items="${column}" var="university">
                            <a class="list-group-item list-group-item-action" href='evaluate?university=${university.code()}'>${university.name()}</a>
                        </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <br>
            </c:forEach>

            <c:if test = "${userProfile == 'ADMIN' || userProfile == 'SUPER_USER'}">
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <button class="btn btn-lg btn-block btn-success"
                            onclick="location.href='manage?action=form'">
                        Tải phiếu chấm tất cả các trường
                    </button>
                </div>
                <div class="col-md-4"></div>
            </div>

            <!--            Kết quả chính thức-->
            <div id="result"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h2 class="h2 text-primary">Kết quả chính thức</h2>
            </div>

            <c:forEach items="${locationToTitle}" var="location">
                <div id="result-${location.key}"
                     class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                    <h4 class="h4">${location.value}</h4>
                </div>
                <div class="row">
                    <c:forEach items="${locationToColumns.get(location.key)}" var="column">
                        <div class="col-md-6">
                            <div class="list-group">
                            <c:forEach items="${column}" var="university">
                                <a class="list-group-item list-group-item-action" href='bilan?university=${university.code()}'>${university.name()}</a>
                            </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <br>
            </c:forEach>

            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <button class="btn btn-lg btn-block btn-success"
                            onclick="location.href='manage?action=result'">
                        In thông báo kết quả tất cả các trường
                    </button>
                </div>
                <div class="col-md-4"></div>
            </div>
            </br>
            </br>
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
<script>window.jQuery || document.write('<script src="../js/vendor/jquery-slim.min.js"><\/script>')</script>
<script src="../js/vendor/popper.min.js"></script>
<script src="../js/bootstrap.min.js"></script>

<!-- Icons -->
<script src="../js/feather.min.js"></script>
<script>feather.replace()</script>
</body>
</html>
