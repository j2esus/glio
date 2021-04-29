<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/aim/productivity.js"/>"></script>

<h1>Productividad</h1>
<hr/>
<form id="productivityForm">
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
<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-bar-chart"></i>Datos</div>
    <div class="card-body">
        <div id = "divChart"></div>
    </div>
</div>