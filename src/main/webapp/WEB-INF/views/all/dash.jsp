<%-- 
    Document   : init
    Created on : 5/11/2017, 05:49:33 PM
    Author     : j2esus
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Módulos</h1>
<hr/>
<!-- Icon Cards-->
<c:forEach items="${categoryList}" var="categoryItem">
    <c:if test="${i mod 4 == 0}">
        <div class="row">
            <c:set var="i" value="0"/>
        </c:if>
        <div class="col-xl-3 col-sm-6 mb-3">
            <div class="card text-white ${categoryItem.clazz} o-hidden h-100">
                <div class="card-body">
                    <div class="card-body-icon">
                        <i class="fa fa-fw ${categoryItem.icon}"></i>
                    </div>
                    <div class="mr-5">${categoryItem.name}</div>
                </div>
                <a class="card-footer text-white clearfix small z-1" href="<c:url value="/all/module?id=${categoryItem.id}"/>">
                    <span class="float-left">Mostrar</span>
                    <span class="float-right">
                        <i class="fa fa-angle-right"></i>
                    </span>
                </a>
            </div>
        </div>
        <c:if test="${i mod 4 == 3}">
        </div>
        </c:if>
    <c:set var="i" value="${i+1}"/>
</c:forEach>