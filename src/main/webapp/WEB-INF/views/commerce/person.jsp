<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/commerce/crudPerson.js"/>"></script>
<input type="hidden" id="personType" value="${personType}"/>
<h1>${title}</h1>
<hr/>
<div id="divPerson">
    <form id="filterForm" method="post">
        <div class="card">
            <div class="card-header">
                Filtros
            </div>
            <div class="card-body">
                <div class="form-inline">
                    <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                        <div class="input-group-addon">Nombre</div>
                        <input type="text" class="form-control" id="nameF"/>
                    </div>
                    <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                        <div class="input-group-addon">Email</div>
                        <input type="text" class="form-control" id="emailF"/>
                    </div>
                    <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                        <div class="input-group-addon">Teléfono</div>
                        <input type="text" class="form-control" id="phoneF"/>
                    </div>
                    <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                        <div class="input-group-addon">RFC</div>
                        <input type="text" class="form-control" id="rfcF"/>
                    </div>
                    <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                        <div class="input-group-addon">Estatus</div>
                        <select id="statusF" class="form-control">
                            <option value="">--Todos</option>
                            <c:forEach items="${status}" var="status">
                                <option value="${status}">${status}</option>
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
                <th>Email</th>
                <th>Teléfono</th>
                <th>RFC</th>
                <th>Estatus</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
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
                                Nombre
                            </div>
                            <div class="col-9">
                                <input type="text" id="name" name="name" class="form-control" required="required"
                                       maxlength="100" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Email
                            </div>
                            <div class="col-9">
                                <input type="email" id="email" name="email" class="form-control"
                                       maxlength="100" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Teléfono
                            </div>
                            <div class="col-9">
                                <input type="text" id="phone" name="phone" class="form-control"
                                       maxlength="10" autocomplete="off" pattern="[0-9]{10}"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                RFC
                            </div>
                            <div class="col-9">
                                <input type="text" id="rfc" name="email" class="form-control"
                                       maxlength="30" autocomplete="off"/>
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
<div id="divDetails">
    <div class="card">
        <div class="card-header">
            <a href="#" onclick="showDivPerson()">Regresar</a> / Detalles
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-lg-6" id="divPersonName"></div>
                <div class="col-lg-6" id="divPersonStatus"></div>
            </div>

            <div class="row">
                <div class="col-lg-6" id="divPersonPhone"></div>
                <div class="col-lg-6" id="divPersonRFC"></div>
            </div>

            <div class="row">
                <div class="col-lg-6" id="divPersonEmail"></div>
            </div>

            <hr/>
            <br/>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="address-tab" data-toggle="tab" href="#address" role="tab" aria-controls="address" aria-selected="false">Direcciones</a>
                </li>
            </ul>

            <div class="tab-content" id="myTabContent">
                <br/>
                <div class="tab-pane fade show active" id="address-bar" role="tabpanel" aria-labelledby="address-tab">
                    <div class="text-right">
                        <button type="button" class="btn btn-primary fa fa-star" id="btnConfirmDefaultAd" data-toggle="tooltip"
                                data-placement="top" title="Poner como predeterminada"></button>
                        <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshAd"></button>
                        <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEditAd"></button>
                        <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDeleteAd"></button>
                        <button type="button" class="btn btn-primary fa fa-plus" id="btnNewAd">Nuevo</button>
                    </div>
                </div>
            </div>
            <br/>
            <div class="table-responsive">
                <table class="table table-bordered table-hover" id="addressTable" width="100%" cellspacing="0">
                    <thead>
                    <tr>
                        <th>Dirección</th>
                        <th>Estado</th>
                        <th>Municipio</th>
                        <th>Colonia</th>
                        <th>Código postal</th>
                        <th>Estatus</th>
                        <th>Predeterminada</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- modal delete -->
<div class="modal fade" id="confirmModalAd" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDeleteAd" />
                <label id="deleteLabelAd"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDeleteAd">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="saveModalAd" role="dialog">
    <div class="modal-dialog modal-lg">
        <form id="dataFormAd" method="post" data-toggle="validator">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="titleModalNewAd"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewAd"/>
                        <input type="hidden" id="defaultt"/>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Dirección
                            </div>
                            <div class="col-9">
                                <textarea id="address" name="address" class="form-control"
                                          required="required" maxlength="300" rows="4"></textarea>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Referencias
                            </div>
                            <div class="col-9">
                                <textarea id="reference" name="reference" class="form-control"
                                          required="required" maxlength="300" rows="4"></textarea>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Código postal
                            </div>
                            <div class="col-9">
                                <input type="text" id="zipcode" name="zipcode" class="form-control"
                                       maxlength="5" autocomplete="off" pattern="[0-9]{5}" required="required"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estado
                            </div>
                            <div class="col-9">
                                <select id="state" class="form-control" required="required">
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Municipio
                            </div>
                            <div class="col-9">
                                <select id="town" class="form-control" required="required">
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Colonia
                            </div>
                            <div class="col-9">
                                <select id="suburb" class="form-control" required="required">
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="statusAd" class="form-control" required="required">
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
                    <button type="submit" class="btn btn-primary" id="btnSaveAd">Guardar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="confirmModalSetDefaultAd" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idSetDefaultAd" />
                <label id="setDefaultLabelAd"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="btnSetDefaultAd">Aceptar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>