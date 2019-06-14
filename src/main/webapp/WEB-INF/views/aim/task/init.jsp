<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/task/task.js"/>"></script>

<div class="row">
    <div class="col-8">
        <h1>Tareas</h1>
    </div>
    <div class="col-4 text-right">
        <button type="button" id="btnNew" class="btn btn-primary fa fa-plus"></button>
        <select class="custom-select" id = "idProject">
            <option value="0">--Todos</option>
            <c:forEach items="${projects}" var="project">
                <option value="${project.id}">${project.name}</option>
            </c:forEach>
        </select>
    </div>
</div>

<hr/>
<br/>
<div class="row">
    <div class="col-lg-4">
        <div class="text-left" style="float:left">
            <input type="text" class="form-control" placeholder="Buscar..." id="txtQueryActives"/>
        </div>
        <div class="text-right">
            <div class="btn-group btn-group-sm" data-toggle="buttons">
                <label class="btn btn-danger active">
                    <input type="checkbox" name="chkActives" class="classActives" checked="checked" value="ALTA">Alta
                </label>
                <label class="btn btn-warning active">
                    <input type="checkbox" name="chkActives" class="classActives" checked="checked" value="MEDIA">Media
                </label>
                <label class="btn btn-primary active">
                    <input type="checkbox" name="chkActives" class="classActives" checked="checked" value="BAJA">Baja
                </label>
            </div>

        </div>
        <br/>
        <div class="card">
            <div class="card-header">Pendientes <span id="headActives" class="badge badge-success"></span></div>
            <div class="card-body" id = "idDivActives"></div>
        </div>
    </div>
    <div class="col-lg-4">
        <div class="text-left" style="float:left">
            <input type="text" class="form-control" placeholder="Buscar..." id="txtQueryInProcess" autocomplete="off"/>
        </div>
        <div class="text-right">
            <div class="btn-group btn-group-sm" data-toggle="buttons">
                <label class="btn btn-danger active">
                    <input type="checkbox" name="chkInProcess" class="classInProcess" checked="checked" value="ALTA">Alta
                </label>
                <label class="btn btn-warning active">
                    <input type="checkbox" name="chkInProcess" class="classInProcess" checked="checked" value="MEDIA">Media
                </label>
                <label class="btn btn-primary active">
                    <input type="checkbox" name="chkInProcess" class="classInProcess" checked="checked" value="BAJA">Baja
                </label>
            </div>
        </div>
        <br/>
        <div class="card">
            <div class="card-header">En proceso <span id="headInProcess" class="badge badge-success"></span></div>
            <div class="card-body" id = "idDivInProcess"></div>
        </div>
    </div>
    <div class="col-lg-4">
        <div class="text-left" style="float:left">
            <input type="text" class="form-control" placeholder="Buscar..." id="txtQueryFinished" autocomplete="off"/>
        </div>
        <div class="text-right">
            <div class="btn-group btn-group-sm" data-toggle="buttons">
                <label class="btn btn-danger active">
                    <input type="checkbox" name="chkFinished" class="classFinished" checked="checked" value="ALTA">Alta
                </label>
                <label class="btn btn-warning active">
                    <input type="checkbox" name="chkFinished" class="classFinished" checked="checked" value="MEDIA">Media
                </label>
                <label class="btn btn-primary active">
                    <input type="checkbox" name="chkFinished" class="classFinished" checked="checked" value="BAJA">Baja
                </label>
            </div>
        </div>
        <br/>
        <div class="card">
            <div class="card-header">Finalizadas <span id="headFinished" class="badge badge-success"></span></div>
            <div class="card-body" id = "idDivFinished"></div>
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
                                <input type="number" id="estimatedTask" name="estimatedTask" class="form-control" required="required" min="1"/>
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