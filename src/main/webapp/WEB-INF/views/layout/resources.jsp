<%-- 
    Document   : resources
    Created on : 5/11/2017, 11:16:10 PM
    Author     : j2esus
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="icon" href="<c:url value="/images/icon.ico"/>" type="image/x-icon"/>

<!-- theme -->

<!-- Bootstrap core JavaScript-->
<script src="<c:url value="/resources/vendor/jquery/jquery.js"/>"></script>
<script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.bundle.js"/>"></script>
<script src="<c:url value="/resources/vendor/jquery/jquery-ui.js"/>"></script>
<!-- Core plugin JavaScript-->
<script src="<c:url value="/resources/vendor/jquery-easing/jquery.easing.js"/>"></script>
<!-- Page level plugin JavaScript-->
<script src="<c:url value="/resources/vendor/chart.js/Chart.js"/>"></script>
<script src="<c:url value="/resources/vendor/datatables/jquery.dataTables.js"/>"></script>
<script src="<c:url value="/resources/vendor/datatables/dataTables.bootstrap4.js"/>"></script>

<!-- Custom scripts for all pages-->
<script src="<c:url value="/resources/js/sb-admin.js"/>"></script>

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
<!-- animate -->
<link rel="stylesheet" href="<c:url value="/resources/styles/animate.css"/>">


<!-- notify -->
<script src="<c:url value="/resources/js/util/jquery.blockUI.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-notify.min.js"/>"></script> 
<!-- validator -->
<script src="<c:url value="/resources/js/util/validator.js"/>"></script>
<!-- bootstrap -->
<script src="<c:url value="/resources/js/bootstrap/bootstrap.min.js"/>"></script> 

<!-- util -->
<script src="<c:url value="/resources/js/util/util.js"/>"></script>

<script src="<c:url value="/resources/js/util/jquery.tablePagination.0.5.min.js"/>"></script>
<script src="<c:url value="/resources/js/util/accounting.min.js"/>"></script>

<!-- login css -->
<link rel="stylesheet" href="<c:url value="/resources/styles/signin.css"/>">
<!-- select multiple -->
<link rel="stylesheet" href="<c:url value="/resources/vendor/bootstrap/css/bootstrap-select.min.css"/>">
<script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap-select.min.js"/>"></script>
<!-- c3js -->
<link rel="stylesheet" href="<c:url value="/resources/vendor/c3js/c3.css"/>">
<script src="<c:url value="/resources/vendor/c3js/d3.v5.min.js"/>"></script>
<script src="<c:url value="/resources/vendor/c3js/c3.min.js"/>"></script>
<!-- easytimer -->
<script src="<c:url value="/resources/js/util/easytimer.js"/>"></script>

<input type="hidden" value="${userSession.father.username}" id = "sessionUsername"/>

<script>
    jQuery.PATH = '<c:url value="/"/>';
    jQuery.IMAGES = '<c:url value="/resources/images/"/>';
    jQuery.timersPerTasks = new Map();

    window.chartColors = {
        red: 'rgb(255, 99, 132)',
        orange: 'rgb(255, 159, 64)',
        yellow: 'rgb(255, 205, 86)',
        green: 'rgb(75, 192, 192)',
        blue: 'rgb(54, 162, 235)',
        purple: 'rgb(153, 102, 255)',
        grey: 'rgb(201, 203, 207)'
    };
</script>