<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/util/register.js"/>"></script>
<script src="https://www.google.com/recaptcha/api.js"></script>
<div class="container">
    <h1>Registro</h1>
    <hr/>
    <div class="row">
        <div class="col-12 col-md-9">
            <div class="card">
                <div class="card-header">
                    Datos
                </div>
                <div class="card-block">
                    <form id="registerForm" method="post" data-toggle="validator">
                        <div class="form-group row">
                            <label for="example-text-input" class="col-2 col-form-label">Empresa</label>
                            <div class="col-10">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="txtCompany"required="required" maxlength="50" data-minlength="4" pattern="^[_A-z0-9]{1,}$" autocomplete="off"/>
                                </div>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-2 col-form-label">Usuario</label>
                            <div class="col-10">
                                <input class="form-control" type="text" id="txtUser" required="required" maxlength="50" data-minlength="4" pattern="^[_A-z0-9]{1,}$" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-2 col-form-label">Email</label>
                            <div class="col-10">
                                <input class="form-control" type="email" id="txtEmail" required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-2 col-form-label">Password</label>
                            <div class="col-10">
                                <input class="form-control" type="password" id="txtPassword" required="required" data-minlength="6" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="example-text-input" class="col-2 col-form-label">Confirmar</label>
                            <div class="col-10">
                                <input class="form-control" type="password" id="txtConfirmPassword" required="required" data-minlength="6" data-match="#txtPassword"
                                       data-match-error="No corresponde el password" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-10">
                                <button type="submit" class="btn btn-primary">Aceptar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <img src="<c:url value="/resources/images/register.png"/>"/>
        </div>
    </div>
</div>

