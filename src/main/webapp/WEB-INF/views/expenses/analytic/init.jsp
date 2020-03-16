<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/analytic/analytic.js"/>"></script>

<h1>Análisis de gastos</h1>
<hr/>
<ul class="nav nav-tabs" id="myTab" role="tablist">
    <li class="nav-item">
        <a class="nav-link active" id="month-tab" data-toggle="tab" href="#month" role="tab" aria-controls="month" aria-selected="false">Mes</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="general-tab" data-toggle="tab" href="#general" role="tab" aria-controls="general" aria-selected="true">General</a>
    </li>
</ul>
<div class="tab-content" id="myTabContent">
    <br/>
    <div class="tab-pane fade show active" id="month" role="tabpanel" aria-labelledby="month-tab">
        <div class="text-right">
            <select class="custom-select" id = "year">
                <c:forEach items="${dates}" var="date">
                    <option value="${date}">${date}</option>
                </c:forEach>
            </select>
            <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshMonth"></button>
        </div>
        <br/>

        <div class="row">
            <div class="col-lg-9" id = "divChart"></div>
            <div class="col-lg-3">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover" id="dataTableMonth" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Mes</th>
                                <th>Monto</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div class="text-right">
                    <span id="totalMonth" class="badge badge-success"></span>
                </div>
            </div>
        </div>
        <br/>
        <hr/>
        <br/>
        <div class="row">
            <div class="col-lg-9">
                <h2><span id="tittleCategoryMonth" class="badge badge-success"></span></h2>
            </div>
            <div  class="col-lg-3">
                <div class="text-right">
                    <button type="button" class="btn btn-primary fa fa-arrow-left" id="btnYearBack"></button>
                    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshDetails"></button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-4">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover" id="dataTableCatMonth" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Categoría</th>
                                <th>Monto</th>
                                <th>Porcentaje</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
                <div class="text-right">
                    <span id="totalMonthCategory" class="badge badge-success"></span>
                </div>
            </div>
            <div class="col-lg-8">
                <div id="divGraphCategoryMonth">
                    <canvas id="canvGraphCategoryMonth" style="width: 100%"></canvas>
                </div>
            </div>
        </div>
    </div>
    <div class="tab-pane fade" id="general" role="tabpanel" aria-labelledby="general-tab">
        <div id="divCategory">
            <div class="text-right">
                <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
            </div>
            <br/>
            <div class="row">
                <div class="col-lg-4">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover" id="dataTableGral" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Categoría</th>
                                    <th>Monto</th>
                                    <th>Porcentaje</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                    <div class="text-right">
                        <span id="totalCategory" class="badge badge-success"></span>
                    </div>
                </div>
                <div class="col-lg-8">
                    <div id="divGraphCategory">
                        <canvas id="canvGraphCategory" style="width: 100%"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div id="divSubcategory">
            <div class="row">
                <div class="col-lg-4">
                    <a href="javascript:returnGeneral()">Regresar</a></span>
                </div>
                <div class="col-lg-4 text-center">
                    <h4><span id="titleModalNew" class="badge badge-success"></h4>
                </div>
                <div class="col-lg-4 text-right">
                    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshSub"></button>
                </div>
            </div>
            <br/>

            <div class="row">
                <div class="col-lg-4">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTableSub" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Subcategoría</th>
                                    <th>Monto</th>
                                    <th>Porcentaje</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                    <div class="text-right">
                        <span id="totalSubcategory" class="badge badge-success"></span>
                    </div>
                </div>
                <div class="col-lg-8">
                    <div id="divGraphSubcategory">
                        <canvas id="canvGraphSubcategory" style="width: 100%"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>