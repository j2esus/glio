<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/supply/inventoryAnalysis.js"/>"></script>
<h1>Analisis</h1>
<hr/>
<div class="text-right">
    <button type="button" class="btn btn-success fa fa-refresh" id="btnRefresh"></button>
</div>
<br />
<div class="row">
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border border-primary">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-cubes fa-2x text-primary"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-primary" id="depots">0</div>
                        <div class="small text-muted">Almacenes</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border border-warning">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-list-alt fa-2x text-warning"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-warning" id="articles">0</div>
                        <div class="small text-muted">Articulos</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border border-success">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-cube fa-2x text-success"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-success" id="unities">0</div>
                        <div class="small text-muted">Unidades</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card border-danger">
            <div class="card-body text-center">
                <div class="row">
                    <div class="col-lg-4">
                        <h3><i class="fa fa-money fa-2x text-danger"></i></h3>
                    </div>
                    <div class="col-lg-8">
                        <div class="h4 mb-0 text-danger" id="stockValue">0</div>
                        <div class="small text-muted">Valor</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-4">
        <div class="card">
            <div class="card-header">Porcentaje por categoria</div>
            <div class="card-body">
                <div id="divGraphPercentByCategory"></div>
            </div>
        </div>
    </div>

    <div class="col-lg-4">
        <div class="card">
            <div class="card-header">Unidades por categoria</div>
            <div class="card-body">
                <div id="divGraphUnitiesByCategory"></div>
            </div>
            <div class="card-footer">
                <div class="text-right"><span id="totalUnitiesByCategory" class="badge badge-success"></span></div>
            </div>
        </div>
    </div>

    <div class="col-lg-4">
        <div class="card">
            <div class="card-header">Valor por categoria</div>
            <div class="card-body">
                <div id="divGraphValuesByCategory"></div>
            </div>
            <div class="card-footer">
                <div class="text-right"><span id="totalValueByCategory" class="badge badge-danger"></span></div>
            </div>
        </div>
    </div>
</div>