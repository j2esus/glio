<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/supply/crudDepot.js"/>"></script>

<h1>Almacenes</h1>
<hr/>
<form action="javascript:findData()">
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
    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
    <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEdit"></button>
    <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDelete"></button>
    <button type="button" class="btn btn-primary fa fa-plus" id="btnNew">Nuevo</button>
</div>
<br />
<div class="table-responsive">
    <table class="table table-bordered table-hover" id="table" width="100%" cellspacing="0">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>Estatus</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<!-- modal delete -->
<div class="modal fade" id="confirmModal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDelete" />
                <label id="deleteLabel"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDelete">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>

<!-- edit/add element-->
<div class="modal fade" id="saveModal" role="dialog">
    <div class="modal-dialog">
        <form id="dataForm" method="post" data-toggle="validator">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="titleModalNew"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNew"/>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
                            </div>
                            <div class="col-9">
                                <input type="text" id="name" name="name" class="form-control" required="required" maxlength="50" pattern="^[_A-z0-9 ]{1,}$" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="status" class="form-control" required="required">
                                    <c:forEach items="${status}" var="status">
                                        <option value="${status}">${status}</option>
                                    </c:forEach>
                                </select>
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