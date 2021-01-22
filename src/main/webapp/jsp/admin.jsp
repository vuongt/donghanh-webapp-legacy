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
    <link href="../css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="../css/dashboard.css" rel="stylesheet"/>

</head>
<body>

<%@ include file="components/header.jsp" %>

<div class="container-fluid">
    <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar" style="overflow: hidden;">
            <div class="sidebar-sticky">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="#reset">
                            <span data-feather="settings"></span>
                            Khởi động tham số
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#load-data">
                            <span data-feather="file"></span>
                            Tạo bảng dữ liệu
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#parameter">
                            <span data-feather="sliders"></span>
                            Tạo lại bảng dữ liệu của một trường
                        </a>
                    </li>
                </ul>
            </div>
        </nav>


        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
            <c:if test = "${alertMessage != null}">
                <div class="alert ${alertStatus} alert-dismissible fade show" role="alert">
                  ${alertMessage}
                  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
            </c:if>

            <%@ include file="components/back-dashboard.jsp" %>

            <br>
            <div
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h2 class="h2 text-primary">Các bước cần làm đầu mỗi kì </h2>
            </div>

            <div
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h4 class="h4">1. Xoá bảng dữ liệu của kì trước</h4>
            </div>
            <div class="h6">Drop all tables in the test database. Attention: Do not drop the database itself! </div>
            <br>

            <div id="reset"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h4 class="h4">2. Tạo bảng tham số</h4>
            </div>
            <div class="h6">
            Clink vào dưới đây để tạo bảng tham số. Sửa tham số cho phù hợp với thông tin của kì mới,
            quan trọng nhất là sửa CURRENT_SEMESTER ở bước này, các tham số khác có thể chỉnh sau.
            </div>
            <br>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <button class="btn btn-lg btn-block btn-primary"
                            onclick="location.href='admin?action=reset-params'">
                        Tạo bảng tham số
                    </button>
                </div>
                <div class="col-md-4"></div>
            </div>
            <br>

            <div id="activate-${location.key}"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h4 class="h4">3. Upload bảng dữ liệu csv chứa thông tin các sinh viên</h4>
            </div>
            <div>
            <form method="POST" action="admin" enctype="multipart/form-data" >
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <input type="file" name="file" id="file" /> <br/>
                </div>
                <div class="col-md-4"></div>
            </div>

            </br>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <input type="submit" value="Upload" name="upload" id="upload"  class="btn btn-lg btn-block btn-primary"/>
                </div>
                <div class="col-md-4"></div>
            </div>

        </form>
            </div>
            <br>

            <div id="load-data"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h4 class="h4">4. Tạo bảng dữ liệu sinh viên chính</h4>
            </div>
            <div class="h6">
            Clink vào dưới đây để tạo bảng dữ liệu sinh viên chính từ dữ liệu vừa tải lên.
            </div>
            <br>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <button class="btn btn-lg btn-block btn-primary"
                            onclick="location.href='admin?action=load-data'">
                        Tạo bảng dữ liệu
                    </button>
                </div>
                <div class="col-md-4"></div>
            </div>
            <br>

            <div id="activate-${location.key}"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h4 class="h4">5. Kích hoạt dữ liệu từng trường trên trang chủ</h4>
            </div>
            <br>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                <button class="btn btn-lg btn-block btn-primary"
                            onclick="location.href='dashboard'">
                        Quay lại trang chủ
                    </button>
                </div>
                <div class="col-md-4"></div>
            </div>
            <br>
            <br>
            <br>

            <div
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h2 class="h2 text-primary">Chức năng đặc biệt  </h2>
            </div>
            <br>


            <!--            Reset dữ liệu sinh viên theo trường-->
            <div id="activate"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h2 class="h2 text-primary">Reset dữ liệu sinh viên theo trường</h2>
            </div>
            <div id="activate"
                 class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h5 class="h5 text-danger">Are you sure ?
                   </br>
                   By clicking on the university name,
                you will reset all data related to that university in the database, including jury evaluation data.
                Be sure that you know what you are doing.</h5>
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
                                <a class="list-group-item list-group-item-action" href='manage?action=reset&university=${university.code()}'>${university.name()}</a>
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
<script src="../js/feather.min.js"></script>
<script>feather.replace()</script>
<script type="text/javascript" src="../normalize.js" ></script>

</body>
</html>
