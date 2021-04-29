<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/expenses/crudCategory.js"/>"></script>

<h1>Categorías</h1>
<hr/>
<br/>
<div class="row">
    <div class="col-lg-6">
        <div class="text-left">
            <input type="text" class="form-control" placeholder="Buscar..." id="txtQueryCategory"/>
        </div>
        <br/>
        <div class="card">
            <div class="card-header">
                Categoría
                <button type="button" class="btn btn-primary fa fa-plus" id="btnNewCa" style="float: right;"></button>
                <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDeleteCa" style="float: right;"></button>
                <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEditCa" style="float: right;"></button>
                <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshCa" style="float: right;"></button>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover" id="dataTableCa" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Estatus</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="text-left">
            <input type="text" class="form-control" placeholder="Buscar..." id="txtQuerySubcategory"/>
        </div>
        <br/>
        <div class="card">
            <div class="card-header">
                Subcategoría
                <button type="button" class="btn btn-primary fa fa-plus" id="btnNewSu" style="float: right;"></button>
                <button type="button" class="btn btn-danger fa fa-trash-o" id="btnConfirmDeleteSu" style="float: right;"></button>
                <button type="button" class="btn btn-primary fa fa-pencil-square-o" id="btnEditSu" style="float: right;"></button>
                <button type="button" class="btn btn-success fa fa-refresh" id="btnRefreshSu" style="float: right;"></button>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover" id="dataTableSu" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Estatus</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- edit/add element-->
<div class="modal fade" id="saveModalCategory" role="dialog">
    <div class="modal-dialog">
        <form id="dataFormCategory" method="post" data-toggle="validator">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="titleModalNewCategory"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewCategory"/>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
                            </div>
                            <div class="col-9">
                                <input type="text" id="nameCategory" class="form-control" required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="statusCategory" class="form-control" required="required">
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

<!-- modal delete -->
<div class="modal fade" id="confirmModalCategory" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDeleteCategory"/>
                <label id="deleteLabelCategory"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDeleteCategory">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="saveModalSubcategory" role="dialog">
    <div class="modal-dialog">
        <form id="dataFormSubcategory" method="post" data-toggle="validator">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="titleModalNewSubcategory"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <input type="hidden" id="idNewSubcategory"/>
                        <div class="form-group row">
                            <div class="col-12 text-right"><span id="divTitleCategory" class="badge badge-success"></span></div>
                        </div>
                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Nombre:
                            </div>
                            <div class="col-9">
                                <input type="text" id="nameSubcategory" class="form-control" required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Estatus
                            </div>
                            <div class="col-9">
                                <select id="statusSubcategory" class="form-control" required="required">
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
                    <button type="submit" class="btn btn-primary" id="btnSaveSub">Guardar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="confirmModalSubcategory" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDeleteSubcategory"/>
                <label id="deleteLabelSubcategory"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDeleteSubcategory">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>