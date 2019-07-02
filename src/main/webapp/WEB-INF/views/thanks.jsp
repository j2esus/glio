<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    hr.new3 {
        border: 2px solid #007bff;
    }
</style>

<div class="container">
    <center><h1>¡Gracias!</h1></center>
    <hr/>
    <br/>
    <div class="row">
        <div class="col-lg-3"></div>
        <div class="col-lg-6">
            <div class="card border-primary">
                <div class="card-header text-white bg-primary">
                    Datos de acceso
                </div>
                <div class="card-block">
                    <p class="card-text">Usuario: <strong>${username}</strong></p>
                    <p class="card-text">Password: <strong>****************</strong></p>
                    <hr/>
                    <div class="text-right">
                        <a href="<c:url value="/"/>" class="btn btn-primary">Ir a Login</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3"></div>
    </div>
</div>

