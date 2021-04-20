<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/supply/availableStock.js"/>"></script>
<h1>Existencia</h1>
<hr/>
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
                    <div class="input-group-addon">Categoria</div>
                    <select id="categoryF" class="form-control">
                        <option value="0">--Todos</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <input type="submit" class="btn btn-success" value="Buscar"/>
            </div>
        </div>
    </div>
</form>
<br/>
<div class="table-responsive">
    <table class="table table-bordered table-hover" id="table" width="100%" cellspacing="0">
        <thead>
        <tr>
            <th>Articulo</th>
            <th>Sku</th>
            <th>Unidad</th>
            <th>Categoria</th>
            <th>Tamaño</th>
            <th>Existencia</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

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
