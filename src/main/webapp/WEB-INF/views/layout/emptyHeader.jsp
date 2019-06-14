<%-- 
    Document   : emptyHeader
    Created on : 5/11/2017, 11:16:10 PM
    Author     : j2esus
--%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><tiles:insertAttribute name="title" ignore="true"></tiles:insertAttribute></title>
        <tiles:insertAttribute name="resources" />
        <!-- Global site tag (gtag.js) - Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-136826711-3"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());

            gtag('config', 'UA-136826711-3');
        </script>

    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
            <img src="<c:url value="/resources/images/logo_dos.png"/>"/>
        </nav>
        <div class="content" style="padding-top: 120px;">
            <tiles:insertAttribute name="body" />
        </div>
    </body>
</html>
