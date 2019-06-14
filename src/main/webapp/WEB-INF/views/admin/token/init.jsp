<%-- 
    Document   : init
    Created on : 5/11/2017, 05:49:33 PM
    Author     : j2esus
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/token/crudToken.js"/>"></script>

<h1>Tokens</h1>
<hr/>
<br/>
<div class="text-right">
    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
</div>
<br />
<div class="table-responsive">
    <table class="table table-bordered" id="dataTableToken" width="100%" cellspacing="0">
        <thead>
            <tr>
                <th>Token</th>
                <th>Usuario</th>
                <th>Estatus</th>
                <th>Eliminar</th>
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