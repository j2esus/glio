<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container" style="text-align: center">
    <h1 class="page-header">Error 404</h1>
    <br/>
    <h4>No se puede encontrar la página web (HTTP 404).</h4>
    <br/><br/>
    <center><a class="btn btn-primary" href="<c:url value="/"/>">Regresar</a></center>
</div>