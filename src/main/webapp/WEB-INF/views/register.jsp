<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/util/register.js"/>"></script>
<script src="https://www.google.com/recaptcha/api.js" async defer></script>

<style>
    hr.new3 {
        border: 2px solid #007bff;
    }
</style>

<div class="container">
    <center><h1>Crear una cuenta</h1></center>
    <hr/>
    <br/>
    <div class="row">
        <div class="col-lg-6">
            <h4 >Registrate y obten los siguientes beneficios</h4>
            <br/>
            <div class="resume-content mr-auto" style="padding-top: 3%;padding-left: 5%">
                <ul class="fa-ul mb-0" style="font-size: 1.15rem">
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        3 usuarios completamente gratis.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Planea los proyectos basado en objetivos y tareas.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Asigna las tareas a los miembros del equipo.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Gestiona y lleva el control del tiempo invertido en cada tarea.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Identifica a los miembros más productivos del equipo.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Monitorea en cualquier momento el avance de los proyectos.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Recibe un correo diario de las actividades realizadas en el día y el tiempo invertido en ellas.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Evalua los proyectos respecto al tiempo estimado vs. tiempo real.</li>
                    <li>
                        <i class="fa-li fa fa-check"></i>
                        Adicional, lleva un control de gastos, pantalla de monitoreo y evaluación.</li>
                </ul>
            </div>
        </div>
        <div class="col-lg-6">
            <c:if test="${message != ''}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Error!</strong> ${message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>

            <div class="card border-primary">
                <div class="card-header text-white bg-primary">
                    Datos
                </div>
                <div class="card-block">
                    <form id="registerForm" method="post" data-toggle="validator" action="<c:url value="/register"/>">
                        <div class="form-group row">
                            <div class="col-12">
                                <div class="input-group">
                                    <input type="text" class="form-control" id="companyName" placeholder="Empresa" name="companyName" value="${company}"
                                           required="required" maxlength="50" data-minlength="4" pattern="^[_A-z0-9]{1,50}$" autocomplete="off"/>
                                </div>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-12">
                                <input class="form-control" type="text" id="username" placeholder="Usuario" name="username" value="${username}"
                                       required="required" maxlength="50" data-minlength="4" pattern="^[_A-z0-9]{1,50}$" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-12">
                                <input class="form-control" type="email" id="email" placeholder="email" name="email"  value="${email}"
                                       required="required" maxlength="50" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-12">
                                <input class="form-control" type="password" id="password" placeholder="password" name= "password" value="${password}"
                                       required="required" data-minlength="6" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-12">
                                <input class="form-control" type="password" id="txtConfirmPassword" placeholder="Confirmar password" required="required" data-minlength="6" data-match="#password"
                                       data-match-error="No corresponde el password" autocomplete="off"/>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-12">
                                <div class="g-recaptcha" data-sitekey="6Lc0EjAUAAAAAGuBbK2QpLfyYwqXOy1cdPxgVEbS"></div>
                                <div class="help-block with-errors"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-6">
                                <button type="submit" class="btn btn-primary">Aceptar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

