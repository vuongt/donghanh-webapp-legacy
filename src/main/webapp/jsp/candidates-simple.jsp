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

    <div class="container-fluid">
        <div class="row">
            <main role="main" class="col-md-12 ml-sm-auto col-lg-12 pt-3 px-4">
            <%@ include file="components/back-dashboard.jsp" %>
            <br>
            <h1 class="h1 p-3" align="center">${title}</h1>
            <table class="table table-bordered table-hover" id='table_${university}_${jury.index}'>
                <thead class="table-primary">
                    <th>Mã số</th>
                    <th>Họ và tên đệm</th>
                    <th>Tên</th>
                </thead>
                <c:forEach items="${candidates}" var="candidate">
                <tr>
                    <td>${candidate.code}</td>
                    <td>${candidate.last_name}</td>
                    <td>${candidate.first_name}</td>
                </tr>
                </c:forEach>
            </table>
            </main>
        </div>
    </div>
    <%@ include file="components/footer.jsp" %>
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
<script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
<script>feather.replace()</script>
</body>
</html>