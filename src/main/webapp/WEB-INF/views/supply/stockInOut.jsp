<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/supply/stockInOut.js"/>"></script>
<input type="hidden" id="stockType" value="${stockType}"/>
<h1>${title}</h1>
<hr/>
<br/>
<div class="card">
    <div class="card-header">Datos</div>
    <div class="card-body">
        <div class="form-horizontal">
            <div class="form-group row">
                <div class="col-3 col-form-label">
                    SKU
                </div>
                <div class="col-7">
                    <input type="text" id="skuFilter" name="skuFilter" class="form-control" required="required" maxlength="50" autocomplete="off"/>
                    <div class="help-block with-errors"></div>
                </div>
                <div class="col-2">
                    <button type="button" class="btn btn-success" id="btnSearchArticle">Buscar</button>
                </div>
            </div>
        </div>
        <form id="dataFormStock" method="post" data-toggle="validator">
            <div class="form-horizontal">
                <div class="form-group row">
                    <div class="col-3 col-form-label">
                        Nombre artículo
                    </div>
                    <div class="col-7">
                        <input type="text" id="name" name="name" class="form-control" required="required" maxlength="100" readonly="readonly"/>
                        <div class="help-block with-errors"></div>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-3 col-form-label">
                        Descripción artículo
                    </div>
                    <div class="col-7">
                        <textarea maxlength="150" id="descriptionArticle" readonly="readonly" class="form-control"></textarea>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-3 col-form-label">
                        Unidad artículo
                    </div>
                    <div class="col-7">
                        <input type="text" id="unity" name="unity" class="form-control" required="required" maxlength="30" readonly="readonly"/>
                        <div class="help-block with-errors"></div>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-3 col-form-label">
                        Almacén
                    </div>
                    <div class="col-7">
                        <select class="form-control" id="depot" required="required">
                            <option value="">--Seleccione</option>
                            <c:forEach items="${depots}" var="depot">
                                <option value="${depot.id}">${depot.name}</option>
                            </c:forEach>
                        </select>
                        <div class="help-block with-errors"></div>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-3 col-form-label">
                        Cantidad
                    </div>
                    <div class="col-7">
                        <input type="number" min="1" pattern="^[0-9]+" id="quantity" name="quantity" class="form-control" required="required" maxlength="30">
                        <div class="help-block with-errors"></div>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-3 col-form-label">
                        Descripción
                    </div>
                    <div class="col-7">
                        <textarea id="description" name="description" maxlength="300" class="form-control"></textarea>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-3 col-form-label">
                    </div>
                    <div class="col-7">
                        <button type="submit" class="btn btn-primary" id="btnSave">Guardar</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
