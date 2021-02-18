<%-- 
    Document   : init
    Created on : 5/11/2017, 05:49:33 PM
    Author     : j2esus
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/advance/advance.js"/>"></script>

<h1>Avances</h1>
<hr/>


<div class="row">
    <div class="col-lg-7">
        <div class="card">
            <div class="card-header">
                Proyectos
            </div>
            <div class="card-body">
                <div class="text-left" style="float:right">
                    <input type="text" class="form-control" placeholder="Buscar..." id="txtQueryProjects" autocomplete="off"/>
                </div>
                <br/>
                <br/>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover" id="dataTableProject" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Descripción</th>
                                <th>Fecha inicio</th>
                                <th>Fecha fin</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-5">
        <div class="card">
            <div class="card-header">
                Gráfico
            </div>
            <div>
                <div class="text-center" id="divProjectChart">

                </div>
            </div>
        </div>
    </div>
</div>
<br/>
<div class="fa fa-bar-chart">
    Objetivos
</div>
<br/>
<br/>
<div id="divAims">

</div>


<div class="modal fade" id="detailsModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4><span id="titleModalDetail" class="badge badge-success"></span></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <div class="col-3">
                        <b>Fecha inicio</b>
                    </div>
                    <div class="col-9" id = "initDateModal">
                        
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-3">
                        <b>Fecha fin</b>
                    </div>
                    <div class="col-9" id = "endDateModal">
                        
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-3">
                        <b>Descrpción</b>
                    </div>
                    <div class="col-9" id = "descriptionModal">
                        
                    </div>
                </div>
                
                <br/>
                <div class="row">
                    <div class="col-12">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTableTask" width="100%" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>Nombre</th>
                                        <th>Estatus</th>
                                        <th>Prioridad</th>
                                        <th>Usuario</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
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

<a class="scroll-to-top rounded" href="#page-top">
    <i class="fa fa-angle-up"></i>
</a>