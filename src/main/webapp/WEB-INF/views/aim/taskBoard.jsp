<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/aim/taskBoard.js"/>"></script>

<div class="row">
    <div class="col-8">
        <h1>Tablero de tareas</h1>
    </div>
    <div class="col-4 text-right">
        <button type="button" id="btnNew" class="btn btn-primary fa fa-plus"></button>
    </div>
</div>
<hr/>
<div class="row">
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border border-primary">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-cogs fa-2x text-primary"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-primary" id="projects">0</div>
                        <div class="small text-muted">Proyectos</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border border-warning">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-list-alt fa-2x text-warning"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-warning" id="aims">0</div>
                        <div class="small text-muted">Objetivos</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border border-success">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-list fa-2x text-success"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-success" id="tasks">0</div>
                        <div class="small text-muted">Tareas</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border-danger">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-list fa-2x text-danger"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-danger" id="todays">0</div>
                        <div class="small text-muted">Hoy</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">
                Listado de tareas
            </div>
            <div class="card-body">
                <div class="form-group mb-2">
                    <input type="text" class="form-control" placeholder="Buscar..." id="nameFilter" autocomplete="off"/>
                </div>
                <form class="form-inline" role="form">
                    <div class="form-group mb-2 mr-sm-2 mb-sm-0">
                        <select class="custom-select" id = "projectFilter">
                            <option value="0">--Proyecto</option>
                            <c:forEach items="${projects}" var="project">
                                <option value="${project.id}">${project.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group mb-2 mr-sm-2 mb-sm-0">
                        <select class="selectpicker" id = "statusFilter" multiple="multiple" data-actions-box="true">
                            <c:forEach items="${status}" var="statusItem">
                                <option value="${statusItem}">${statusItem}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group mb-2 mr-sm-2 mb-sm-0">
                        <div class="btn-group btn-group-sm" data-toggle="buttons">
                            <label class="btn btn-danger active">
                                <input type="checkbox" name="chkPriority" class="classPriority" checked="checked" value="ALTA">Alta
                            </label>
                            <label class="btn btn-warning active">
                                <input type="checkbox" name="chkPriority" class="classPriority" checked="checked" value="MEDIA">Media
                            </label>
                            <label class="btn btn-primary active">
                                <input type="checkbox" name="chkPriority" class="classPriority" checked="checked" value="BAJA">Baja
                            </label>
                        </div>
                    </div>
                </form>
                <hr/>
                <br/>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover" id="dataTable" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Estatus</th>
                                <th>Prioridad</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">
                Detalles tarea
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-lg-2 mb-2"><b>Nombre</b></div>
                    <div class="col-lg-4 mb-2" id="divName"></div>
                    <div class="col-lg-2 mb-2"><b>Estatus</b></div>
                    <div class="col-lg-4 mb-2" id="divStatus"></div>
                </div>
                <div class="row">
                    <div class="col-lg-2 mb-2"><b>Descripción</b></div>
                    <div class="col-lg-10 mb-2" id="divDescription"></div>
                </div>
                <div class="row">
                    <div class="col-lg-2 mb-2"><b>Prioridad</b></div>
                    <div class="col-lg-4 mb-2" id="divPriority"></div>
                    <div class="col-lg-2 mb-2"><b>Estimado</b></div>
                    <div class="col-lg-4 mb-2" id="divEstimated"></div>
                </div>
                <div class="row">
                    <div class="col-lg-2 mb-2"><b>Creado por</b></div>
                    <div class="col-lg-4 mb-2" id="divCreatedBy"></div>
                    <div class="col-lg-2 mb-2"><b>Asignado a</b></div>
                    <div class="col-lg-4 mb-2" id="divAssignedTo"></div>
                </div>
                <hr/>
                <div class="row">
                    <div class="col-lg-6">
                        <div id="chart"></div>
                    </div>
                    <div class="col-lg-6">
                        <div id="divCurrentTimePerTask" class="h1 text-center" style="padding: 5%"></div>
                        <div class="text-center">
                            <button type="button" id="btnToStart" class="btn btn-primary">Empezar</button>
                            <button type="button" id="btnToPause" class="btn btn-primary">Pausar</button>
                            <button type="button" id="btnToFinish" class="btn btn-primary">Terminar</button>
                            <button type="button" id="btnToCancel" class="btn btn-primary">Cancelar</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>        
    </div>
</div>

<!-- TASK -->
<!-- edit/add element-->
<div class="modal fade" id="saveModalTask" role="dialog">
    <div class="modal-dialog">
        <form id="dataFormTask" method="post" data-toggle="validator">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Agregar tarea</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewTask"/>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre
                            </div>
                            <div class="col-9">
                                <input type="text" id="nameTask" name="nameTask" class="form-control" required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Descripción
                            </div>
                            <div class="col-9">
                                <textarea id="descriptionTask" name="descriptionTask" class="form-control" required="required" maxlength="500" rows="3"></textarea>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Prioridad
                            </div>
                            <div class="col-9">
                                <select id="priorityTask" class="form-control" required="required">
                                    <option value="">--Seleccione</option>
                                    <c:forEach items="${priorities}" var="priority">
                                        <option value="${priority}">${priority}</option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estimado
                            </div>
                            <div class="col-9">
                                <select id="estimatedTask" class="form-control" required="required">
                                    <option value="900">00:15</option>
                                    <option value="1800">00:30</option>
                                    <option value="2700">00:45</option>
                                    <option value="3600">01:00</option>
                                    <option value="5400">01:30</option>
                                    <option value="7200">02:00</option>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Proyecto
                            </div>
                            <div class="col-9">
                                <select class="custom-select" id = "idProjectTask">
                                    <c:forEach items="${projects}" var="project">
                                        <option value="${project.id}">${project.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Objetivo
                            </div>
                            <div class="col-9">
                                <select class="custom-select" id = "idAimTask">

                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" id="btnSaveTask">Guardar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </form>
    </div>
</div>

<a class="scroll-to-top rounded" href="#page-top">
    <i class="fa fa-angle-up"></i>
</a>