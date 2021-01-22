<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Dashboard</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="css/dashboard.css" rel="stylesheet"/>

</head>
<body>
<%@ include file="components/header.jsp" %>

<div class="container-fluid">
    <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar" style="overflow: hidden;">
            <div class="sidebar-sticky">
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
            </div>
        </nav>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
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
                                <a class="list-group-item list-group-item-action" href='visitor?university=${university.code()}'>${university.name()}</a>
                            </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <br>
            </c:forEach>

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
<script src="../feather.min.js"></script>
<script>feather.replace()</script>
<script type="text/javascript" src="../normalize.js" ></script>

</body>
</html>
