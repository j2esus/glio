<%-- 
    Document   : init
    Created on : 5/11/2017, 05:49:33 PM
    Author     : j2esus
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/user/crudUser.js"/>"></script>

<h1>Usuarios</h1>
<hr/>
<br/>
<div class="text-right">
    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
    <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEdit"></button>
    <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDelete"></button>
    <button type="button" class="btn btn-primary fa fa-plus" id="btnNew">Nuevo</button>
</div>
<br />
<div class="table-responsive">
    <table class="table table-bordered table-hover" id="dataTableUser" width="100%" cellspacing="0">
        <thead>
            <tr>
                <th>Nombre usuario</th>
                <th>Nombre</th>
                <th>Estatus</th>
                <th>Tipo usuario</th>
                <th>Email</th>
                <th>Un solo acceso</th>
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
                                Usuario:
                            </div>
                            <div class="col-9">
                                <div class="input-group">
                                    <input type="text" id="username" name="username" class="form-control" required="required" maxlength="50" pattern="^[_A-z0-9]{1,}$" autocomplete="off"/>
                                    <span class="input-group-addon" id="company">@${company}</span>
                                </div>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
                            </div>
                            <div class="col-9">
                                <input type="text" id="name" name="name" class="form-control" required="required" maxlength="80" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Password:
                            </div>
                            <div class="col-9">
                                <input type="password" id="password" name="password" class="form-control" required="required" maxlength="100" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="status" class="form-control" required="required">
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
                                Tipo usuario
                            </div>
                            <div class="col-9">
                                <select id="userType" class="form-control" required="required">
                                    <option value="">--Seleccione</option>
                                    <c:forEach items="${userTypes}" var="userType">
                                        <option value="${userType.id}">${userType.name}</option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                                
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Email
                            </div>
                            <div class="col-9">
                                <input type="email" id="email" name="email" class="form-control" required="required" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Un acceso
                            </div>
                            <div class="col-9">
                                <input type="checkbox" id="onlyOneAccess"/>
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