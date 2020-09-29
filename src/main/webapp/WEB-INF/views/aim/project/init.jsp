<%-- 
    Document   : init
    Created on : 5/11/2017, 05:49:33 PM
    Author     : j2esus
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/project/crudProject.js"/>"></script>

<h1>Proyectos</h1>
<hr/>
<div id="divProjects">
    <br/>
    <form action="javascript:findProjectData()">
        <div class="card">
            <div class="card-header">
                Filtros
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-lg-5">
                        <input type="text" class="form-control" id="txtFilterQuery" placeholder="Buscar..." autocomplete="off">
                    </div>
                    <div class="col-lg-1">
                        <select class="custom-select" id="txtFilterStatus">
                            <option value="">--Todos</option>
                            <c:forEach items="${status}" var="statusItem">
                                <option value="${statusItem}">${statusItem}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <input type="submit" class="btn btn-success" value="Buscar"/>
                </div>
            </div>
        </div>
    </form>
    <br/>
    <div class="text-right">
        <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshProject"></button>
        <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEditProject"></button>
        <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDeleteProject"></button>
        <button type="button" class="btn btn-primary fa fa-plus" id="btnNewProject">Nuevo</button>
    </div>
    <br/>
    <div class="table-responsive">
        <table class="table table-bordered table-hover" id="dataTableProject" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Estatus</th>
                    <th>Fecha inicio</th>
                    <th>Fecha fin</th>
                    <th>Tareas</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
</div>

<div id="divTasks">
    <ol class="breadcrumb">
        <li class="breadcrumb-item">
            <a href="<c:url value="init"/>">Proyectos</a>
        </li>
        <li class="breadcrumb-item active">Tareas</li>
    </ol>
    <h4><span id="divTitleTasks" class="badge badge-success"></span></h4>
    <br/>

    <div class="row">
        <div class="col-lg-6">
            <div class="card">
                <div class="card-header">
                    <i class="fa fa-bar-chart"></i> Objetivos
                    <button type="button" class="btn btn-primary fa fa-plus" id="btnNewAim" style="float: right;"></button>
                    <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDeleteAim" style="float: right;"></button>
                    <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEditAim" style="float: right;"></button>
                    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshAim" style="float: right;"></button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover" id="dataTableAim" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Estatus</th>
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
        <div class="col-lg-6">
            <div class="card">
                <div class="card-header">
                    <i class="fa fa-tasks"></i> Tareas
                    <button type="button" class="btn btn-primary fa fa-plus" id="btnNewTask" style="float: right;"></button>
                    <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDeleteTask" style="float: right;"></button>
                    <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEditTask" style="float: right;"></button>
                    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshTask" style="float: right;"></button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover" id="dataTableTask" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Estatus</th>
                                    <th>Prioridad</th>
                                    <th>Estimado</th>
                                    <th>Usuario</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                    <div class="text-right">
                        <span id="totalTask" class="badge badge-success"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- edit/add element-->
<div class="modal fade" id="saveModalProject" role="dialog">
    <div class="modal-dialog">
        <form id="dataFormProject" method="post" data-toggle="validator">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="titleModalNewProject"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewProject"/>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
                            </div>
                            <div class="col-9">
                                <input type="text" id="nameProject" name="nameProject" class="form-control" required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Descripción
                            </div>
                            <div class="col-9">
                                <textarea id="descriptionProject" name="descriptionProject" class="form-control" required="required" maxlength="100" rows="3"></textarea>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="statusProject" class="form-control" required="required">
                                    <option value="">--Seleccione</option>
                                    <c:forEach items="${status}" var="status">
                                        <option value="${status}">${status}</option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Fecha inicio
                            </div>
                            <div class="col-9">
                                <input type="date" id="initDate" name="initDate" class="form-control" required="required"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Fecha fin
                            </div>
                            <div class="col-9">
                                <input type="date" id="endDate" name="endDate" class="form-control" required="required"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" id="btnSave">Guardar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- modal delete -->
<div class="modal fade" id="confirmModalProject" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDeleteProject" />
                <label id="deleteLabelProject"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDeleteProject">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>

<!-- AIM -->

<!-- edit/add element-->
<div class="modal fade" id="saveModalAim" role="dialog">
    <div class="modal-dialog">
        <form id="dataFormAim" method="post" data-toggle="validator">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="titleModalNewAim"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewAim"/>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
                            </div>
                            <div class="col-9">
                                <input type="text" id="nameAim" name="nameAim" class="form-control" required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Descripción
                            </div>
                            <div class="col-9">
                                <textarea id="descriptionAim" name="descriptionAim" class="form-control" required="required" maxlength="100" rows="3"></textarea>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="statusAim" class="form-control" required="required">
                                    <option value="">--Seleccione</option>
                                    <c:forEach items="${statusAim}" var="status">
                                        <option value="${status}">${status}</option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Fecha inicio
                            </div>
                            <div class="col-9">
                                <input type="date" id="initDateAim" name="initDateAim" class="form-control" required="required"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Fecha fin
                            </div>
                            <div class="col-9">
                                <input type="date" id="endDateAim" name="endDateAim" class="form-control" required="required"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" id="btnSaveAim">Guardar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- modal delete aim -->
<div class="modal fade" id="confirmModalAim" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDeleteAim" />
                <label id="deleteLabelAim"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDeleteAim">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
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
                    <h4 class="modal-title" id="titleModalNewTask"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewTask"/>
                        <div class="form-group row">
                            <div class="col-12 text-right"><span id="divTitleAim" class="badge badge-success"></span></div>
                        </div>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
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
                                <input type="number" id="estimatedTask" name="estimatedTask" class="form-control" required="required" min="1"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Usuario
                            </div>
                            <div class="col-9">
                                <input type="hidden" id="userTask"/>
                                <input type="text" class="form-control" id="userNameAuto" required="required">
                                <div class="help-block with-errors"></div>
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

<div class="modal fade" id="confirmModalTask" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDeleteTask" />
                <label id="deleteLabelTask"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDeleteTask">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>