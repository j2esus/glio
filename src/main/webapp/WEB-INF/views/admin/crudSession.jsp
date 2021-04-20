<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/session/crudSession.js"/>"></script>

<h1>Sesiones</h1>
<hr/>
<br/>
<div class="text-right">
    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
</div>
<br />
<div class="table-responsive">
    <table class="table table-bordered" id="dataTableSession" width="100%" cellspacing="0">
        <thead>
            <tr>
                <th>Sesión</th>
                <th>Usuario</th>
                <th>Estatus</th>
                <th>Fecha inicio</th>
                <th>Fecha fin</th>
                <th>Finalizar</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<!-- modal close session -->
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
                <button type="button" class="btn btn-danger" id="btnDelete">Aceptar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>