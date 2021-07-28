<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/expenses/analytic.js"/>"></script>

<div class="row">
    <div class="col-8">
        <h1>Análisis de gastos</h1>
    </div>
    <div class="col-4 text-right">
        <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
    </div>
</div>
<hr/>
<div class="row">
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">
                Gastos por mes
                <div class="text-right" style="float: right;">
                    <select class="custom-select" id = "year">
                        <c:forEach items="${dates}" var="date">
                            <option value="${date}">${date}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="card-body">
                <div id="divMonthBarChart"></div>
            </div>
            <div class="card-footer">
                <div class="text-right" style="float: right;">
                    <span id="totalMonth" class="badge badge-success"></span>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-3">
        <div class="card">
            <div class="card-header">
                <button type="button" class="btn btn-outline-primary btn-sm" id="btnYearBack"><</button>
                <button type="button" class="btn btn-outline-primary btn-sm" id="btnCategoryBack"><</button>
                Detalles
                <div class="text-right" style="float: right;">
                    <h4><span id="labelPieChartDetailPerMonth" class="badge badge-success"></span><h4>
                </div>
            </div>
            <div class="card-body">
                <div id="divDetailPieChart"></div>
            </div>
            <div class="card-footer">
                <br/>
            </div>
        </div>
    </div>
    <div class="col-lg-3">
        <div class="card">
            <div class="card-header">
                Montos
                <div class="text-right" style="float: right;">
                    <h4><span id="labelBarChartDetailPerMonth" class="badge badge-success"></span></h4>
                </div>
            </div>
            <div class="card-body">
                <div id="divDetailBarChart"></div>
            </div>
            <div class="card-footer">
                <div class="text-right" style="float: right;">
                    <span id="totalMonthCategory" class="badge badge-success"></span>
                </div>
            </div>
        </div>
    </div>
</div>
<br/>
<div class="row">
    <div class="col-lg-3">
        <div class="card">
            <div class="card-header">General por categoría</div>
            <div class="card-body">
                <div id="divGeneralCategoryPieChart"></div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">Comparativo de años por mes
                <div class="text-right" style="float: right;">
                    <select class="custom-select" id = "idCategoryF">
                        <option value="-1">--Categoría</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                    <select class="custom-select" id = "idSubcategoryF">
                        <option value="0">--Subcategoría</option>
                    </select>
                </div>
            </div>
            <div class="card-body">
                <div id="divComparativeYearLineChart">
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-3">
        <div class="card">
            <div class="card-header">Comparativo por año</div>
            <div class="card-body">
                <div id="divComparativeYearBarChart"></div>
            </div>
        </div>
    </div>
</div>
<br/>
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fa fa-angle-up"></i>
</a>