<%-- 
    Document   : index
    Created on : 5/11/2017, 06:00:02 PM
    Author     : j2esus
--%>

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
        <!-- Core plugin JavaScript-->
        <script src="<c:url value="/resources/vendor/jquery-easing/jquery.easing.js"/>"></script>

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
        <!-- end theme -->

        <!-- notify -->
        <script src="<c:url value="/resources/js/util/jquery.blockUI.js"/>"></script>
        <script src="<c:url value="/resources/js/bootstrap/bootstrap-notify.min.js"/>"></script>
        <!-- validator -->
        <script src="<c:url value="/resources/js/util/validator.js"/>"></script>
        <script src="<c:url value="/resources/js/util/util.js"/>"></script>

        <script src="<c:url value="/resources/js/util/login.js"/>"></script>

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
        <div class="text-right" style="color:whitesmoke;padding-right: 5px;font-size: 13px">v.0.20.beta</div>
        <div class="container">
            <div class="card card-login mx-auto mt-5">
                <div class="card-header">
                    <img src="<c:url value="/resources/images/logo.png"/>" style="padding: 30px" class="imgcenter"/>
                </div>
                <div class="card-body">
                    <form method="POST" id="loginForm">
                        <div class="form-group">
                            <label for="exampleInputEmail1">Usuario</label>
                            <input class="form-control" id="txtUser" type="email" aria-describedby="emailHelp" placeholder="Usuario" autocomplete="off" required autofocus/>
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">Password</label>
                            <input class="form-control" id="txtPassword" type="password" placeholder="Password" required/>
                        </div>
                        <!--
                      <div class="form-group">
                        <div class="form-check">
                          <label class="form-check-label">
                            <input class="form-check-input" type="checkbox"> Remember Password</label>
                        </div>
                      </div>
                        -->
                        <button class="btn btn-primary btn-block" style="padding: 15px" type="submit">Login</button>
                    </form>
                    <!--
                  <div class="text-center">
                    <a class="d-block small mt-3" href="register.html">Register an Account</a>
                    <a class="d-block small" href="forgot-password.html">Forgot Password?</a>
                  </div>
                    -->
                </div>
            </div>
        </div>
    </body>
</html>
<script>
    jQuery.PATH = '<c:url value="/"/>';
</script>
