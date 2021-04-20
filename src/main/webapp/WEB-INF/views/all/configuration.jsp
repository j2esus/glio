<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/user/configuration.js"/>"></script>
<h1>Configuración</h1>
<hr/>
<div class="row">
    <div class="col-12 col-md-9">
        <div id="divDataUser">
            <div class="card">
                <div class="card-header">
                    Datos personales
                </div>
                <div class="card-block">
                    <form id="dataUserForm" method="post" data-toggle="validator">
                        <input type="hidden" id="idUserData" value="${idUser}"/>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-sm-3 col-form-label">Usuario</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="txtUsername" value="${username}" required="required" maxlength="50" pattern="^[_A-z0-9]{1,}$"/>
                                    <span class="input-group-addon" id="basic-addon2">@${company}</span>
                                </div>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-sm-3 col-form-label">Nombre</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" id="txtName" value="${name}" required="required" maxlength="50"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-sm-3 col-form-label">Email</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="email" id="txtEmail" value="${email}" required="required" maxlength="50"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">Guardar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="divChangePassword">
            <div class="card">
                <div class="card-header">
                    Cambiar contraseña
                </div>
                <div class="card-block">
                    <form id="changePasswordForm" method="post" data-toggle="validator">
                        <input type="hidden" id="idUserPassword" value="${idUser}"/>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-sm-3 col-form-label">Anterior:</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="password" id="txtPassword" required="required" data-minlength="6"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-sm-3 col-form-label">Nuevo:</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="password" id="txtNewPassword" required="required" data-minlength="6"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-sm-3 col-form-label">Confirmar:</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="password" id="txtConfirmPassword" required="required" data-minlength="6" data-match="#txtNewPassword"
                                       data-match-error="No corresponde con el nuevo password"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">Guardar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="divTokens">
            <div class="card">
                <div class="card-header">
                    Tokens
                </div>
                <div class="card-block">
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
                </div>
            </div>
        </div>
    </div>
    <div class="col-6 col-md-3">
        <div class="list-group">
            <button type="button" class="list-group-item active">Acciones</button>
            <button type="button" class="list-group-item list-group-item-action" id="btnDataUser">Datos personales</button>
            <button type="button" class="list-group-item list-group-item-action" id="btnChangePassword">Cambiar contraseña</button>
            <button type="button" class="list-group-item list-group-item-action" id="btnToken">Tokens</button>
        </div>    
    </div>
</div>
                        
<!-- modal confirm -->
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
