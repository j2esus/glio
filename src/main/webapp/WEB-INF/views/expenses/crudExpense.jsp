<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/expenses/crudExpense.js"/>"></script>

<h1>Gastos</h1>
<hr/>
<form id="expenseForm">
    <div class="card">
        <div class="card-header">
            Filtros
        </div>
        <div class="card-body">
            <div class="form-inline">
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Fecha inicio</div>
                    <input type="date" class="form-control" id="initDate" required="required"/>
                    <div class="help-block with-errors"></div>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Fecha fin</div>
                    <input type="date" class="form-control" id="endDate" required="required"/>
                    <div class="help-block with-errors"></div>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Categoría</div>
                    <select class="custom-select" id = "idCategoryF">
                        <option value="0">--Todos</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Subcategoría</div>
                    <select class="custom-select" id = "idSubcategoryF">
                        <option value="0">--Todos</option>
                    </select>
                </div>
                <div class="input-group mb-2 mr-sm-2 mb-sm-0">
                    <div class="input-group-addon">Descripción</div>
                    <input type="text" class="form-control" id="descriptionF"/>
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
<br/>
<div class="table-responsive">
    <table class="table table-bordered table-hover" id="dataTable" width="100%" cellspacing="0">
        <thead>
            <tr>
                <th>Fecha</th>
                <th>Importe</th>
                <th>Categoría</th>
                <th>Subcategoría</th>
                <th>Anotaciones</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>
<br/>
<div class="text-right">
    <h3><span id="total" class="badge badge-success"></span></h3>
</div>
<br/>
<!-- edit/add element-->
<div class="modal fade" id="saveModal" role="dialog">
    <div class="modal-dialog">
        <form id="dataForm" method="post" data-toggle="validator">
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
                                Importe
                            </div>
                            <div class="col-9">
                                <input type="number" id="amount" class="form-control" min="0" step=".01" required="required" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Anotaciones
                            </div>
                            <div class="col-9">
                                <textarea id="description" name="description" class="form-control" required="required" maxlength="500" rows="3"></textarea>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Categoría
                            </div>
                            <div class="col-9">
                                <select class="form-control" required="required" id = "idCategory">
                                    <option value="0">--Seleccione</option>
                                    <c:forEach items="${categories}" var="category">
                                        <option value="${category.id}">${category.name}</option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Subcategoría
                            </div>
                            <div class="col-9">
                                <select class="form-control" required="required" id = "idSubcategory">
                                    <option value="0">--Seleccione</option>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-3 col-form-label">
                                Subcategoría
                            </div>
                            <div class="col-9">
                                <input type="date" class="form-control" id="dateE" required="required"/>
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
<div class="modal fade" id="confirmModal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Confirmación</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="idDelete"/>
                <label id="deleteLabel"></label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="btnDelete">Eliminar</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>