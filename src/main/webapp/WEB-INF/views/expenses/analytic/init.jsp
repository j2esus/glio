<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/analytic/analytic.js"/>"></script>

<h1>An�lisis de gastos</h1>
<hr/>
<ul class="nav nav-tabs" id="myTab" role="tablist">
    <li class="nav-item">
        <a class="nav-link active" id="general-tab" data-toggle="tab" href="#general" role="tab" aria-controls="general" aria-selected="true">General</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="month-tab" data-toggle="tab" href="#month" role="tab" aria-controls="month" aria-selected="false">Mes</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false">Desglosada</a>
    </li>
</ul>
<div class="tab-content" id="myTabContent">
    <br/>
    <div class="tab-pane fade show active" id="general" role="tabpanel" aria-labelledby="general-tab">
        <div class="text-right">
            <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
        </div>
        <div class="row">
            <div class="col-lg-4">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover" id="dataTableGral" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Categor�a</th>
                                    <th>Monto</th>
                                    <th>Porcentaje</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div id="divGraphCategory">
                    <canvas id="canvGraphCategory" style="width: 100%"></canvas>
                </div>
            </div>
        </div>
    </div>
    <div class="tab-pane fade" id="month" role="tabpanel" aria-labelledby="month-tab">

    </div>
    <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">

    </div>
</div>


<div class="modal fade bd-example-modal-lg" id="subcategoryModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4><span id="titleModalNew" class="badge badge-success"></span></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-lg-10">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTableSub" width="100%" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>Subcategor�a</th>
                                        <th>Monto</th>
                                        <th>Porcentaje</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-lg-10">
                        <div id="divGraphSubcategory">
                            <canvas id="canvGraphSubcategory" style="width: 100%"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>