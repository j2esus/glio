<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/aim/activity.js"/>"></script>
<script src="<c:url value="/resources/js/aim/commonFunctions.js"/>"></script>

<h1>Actividad</h1>
<hr/>
<form id="activityForm">
    <div class="card">
        <div class="card-header">
            Filtros
        </div>
        <div class="card-body">
            <div class="form-inline">
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Fecha inicio</div>
                    <input type="date" class="form-control" id="initDate" required="required"/>
                    <div class="help-block with-errors"></div>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Fecha fin</div>
                    <input type="date" class="form-control" id="endDate" required="required"/>
                    <div class="help-block with-errors"></div>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Proyecto</div>
                    <select class="custom-select" id = "idProject">
                        <option value="0">--Todos</option>
                        <c:forEach items="${projects}" var="project">
                            <option value="${project.id}">${project.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Objetivo</div>
                    <select class="custom-select" id = "idAim">
                        <option value="0">--Todos</option>
                    </select>
                </div>
                <input type="submit" class="btn btn-success" value="Buscar"/>
            </div>
        </div>
    </div>
</form>
<br/>
<div id="divData">
</div>

<a class="scroll-to-top rounded" href="#page-top">
    <i class="fa fa-angle-up"></i>
</a>