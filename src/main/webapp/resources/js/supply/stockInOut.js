let $stockType;
let _article = null;

let $skuFilter, $btnSearchArticle;
let $name, $description, $unity, $descriptionArticle, $dataFormStock;

$(document).ready(function () {
    initComponents();
    initEvents();
});

function initComponents(){
    $skuFilter = $('#skuFilter');
    $btnSearchArticle = $('#btnSearchArticle');
    $stockType = $('#stockType');
    $name = $('#name');
    $description = $('#description');
    $unity = $('#unity');
    $descriptionArticle = $('#descriptionArticle');
    $dataFormStock = $('#dataFormStock');
}

function initEvents(){
    $skuFilter.keypress(function(e) {
        if(e.which == 13) {
            findBySkuAndStockRequired();
        }
    });

    $btnSearchArticle.click(btnSearchArticleOnClick);

    $dataFormStock.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            executeOperation();
        }
    });
}

function btnSearchArticleOnClick(){
    findBySkuAndStockRequired();
}

function findBySkuAndStockRequired() {
    $.ajax({
        method: "POST",
        url: $.PATH + $stockType.val() + "/findBySkuAndStockRequired",
        async: false,
        data: {
            sku: $skuFilter.val()
        },
        beforeSend: function () {
            _blockUI.block();
        },
        success: function (response) {
            _article = response;
            setArticleValues(_article);
        }, complete: function () {
            _blockUI.unblock();
        }, error: function(xhr, status, error){
            _notify.showDelay('Articulo no encontrado.', 'danger');
            clearControlsArticle();
        }
    });
}

function setArticleValues(article){
    $name.val(article.name);
    $descriptionArticle.val(article.description);
    $unity.val(article.unity);
}

function clearControlsArticle(){
    $name.val('');
    $descriptionArticle.val('');
    $unity.val('');
    _article = null;
}

function executeOperation(){
    let idDepot = $('#depot').val();
    let quantity = $('#quantity').val();
    let description = $('#description').val();

    $.ajax({
        type: "POST",
        url: $.PATH + $stockType.val() + "/executeMovement",
        data: {
            idArticle: _article.id,
            idDepot: idDepot,
            quantity: quantity,
            description: description
        },
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            _notify.show("Movimiento agregado con exito.", 'success');
        }, complete: function () {
            clearControlsArticle();
            clearControlsDepot();
            clearControlsStock();
            _blockUI.unblock();
        }, error: function(xhr, status, error){
            _notify.showDelay(xhr.responseText, 'danger');
        }
    });
}

function clearControlsDepot(){
    $('#depot').val('');
}

function clearControlsStock(){
    $('#quantity').val('');
    $('#description').val('');
}