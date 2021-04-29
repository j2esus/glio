<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="icon" href="<c:url value="/images/icon.ico"/>" type="image/x-icon"/>
        <!-- Bootstrap core JavaScript-->
        <script src="<c:url value="/resources/vendor/jquery/jquery.js"/>"></script>
        <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.bundle.js"/>"></script>

        <!-- Bootstrap core CSS-->
        <link href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.css"/>" rel="stylesheet">
        <!-- Custom fonts for this template-->
        <link href="<c:url value="/resources/vendor/font-awesome/css/font-awesome.css"/>" rel="stylesheet" type="text/css">
        <!-- Page level plugin CSS-->
        <link href="<c:url value="/resources/vendor/datatables/dataTables.bootstrap4.css"/>" rel="stylesheet">
        <!-- Custom styles for this template-->
        <link href="<c:url value="/resources/styles/sb-admin.css"/>" rel="stylesheet">
        <!-- tema msg -->
        <link rel="stylesheet" href="<c:url value="/resources/styles/jquery.msg.css"/>">
        <!-- animate -->
        <link rel="stylesheet" href="<c:url value="/resources/styles/animate.css"/>">
        <!-- end theme -->

        <!-- validator -->
        <script src="<c:url value="/resources/js/util/validator.js"/>"></script>

        <!-- login css -->
        <link rel="stylesheet" href="<c:url value="/resources/styles/signin.css"/>">

        <!-- Global site tag (gtag.js) - Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-136826711-2"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());

            gtag('config', 'UA-136826711-2');
        </script>
    </head>
    <body class="bg-dark">
        <div class="text-right" style="color:whitesmoke;padding-right: 5px;font-size: 13px">v.0.37</div>
        <div class="container">
            <c:if test="${message != ''}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <div class="row justify-content-center">
                <div class="col-xl-10 col-lg-12 col-md-9">
                    <div class="card o-hidden border-0 shadow-lg my-5">
                        <div class="card">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-lg-6 d-none d-lg-block">
                                        <img src="<c:url value="/resources/images/logo.png"/>" style="padding: 80px" class="imgcenter"/>
                                    </div>
                                    <div class="col-lg-6">
                                        <br/>
                                        <div class="text-center">
                                            <h4 class="text-gray-900 mb-4">Bienvenidos</h4>
                                        </div>
                                        <form method="post" data-toggle="validator" action="<c:url value="login"/>">
                                            <div class="form-group">
                                                <input class="form-control" name="company" type="text" placeholder="Empresa" autocomplete="off" value="${company}" required autofocus/>
                                                <div class="help-block with-errors"></div>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" name="user" type="text" placeholder="Usuario" autocomplete="off" value="${user}" required/>
                                                <div class="help-block with-errors"></div>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" name="password" type="password" placeholder="Contraseña" required/>
                                                <div class="help-block with-errors"></div>
                                            </div>
                                            <button class="btn btn-primary btn-block" style="padding: 15px" type="submit">Iniciar sesión</button>
                                        </form>
                                        <hr/>
                                        <div class="text-center">
                                            <a class="d-block small mt-3" href="<c:url value="/register"/>">Crear una cuenta</a>
                                            <!--a class="d-block small" href="forgot-password.html">Forgot Password?</a-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
<script>
    jQuery.PATH = '<c:url value="/"/>';
</script>
