var $btnNew,
        $btnRefresh,
        $btnDelete,
        $btnEdit,
        $btnConfirmDelete;

var $saveModal,
        $dataForm;

var $table;

var _indexSelected = -1,
        _data = [];

$(document).ready(function () {
    initComponents();
    initEvents();
    findData();
});

function initComponents() {
    $btnNew = $('#btnNew');
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');
    $btnEdit = $('#btnEdit');
    $btnConfirmDelete = $('#btnConfirmDelete');

    $saveModal = $('#saveModal');
    $dataForm = $('#dataForm');

    $table = $('#dataTable');
}

function initEvents() {
    $btnNew.click(onClickNew);
    $btnRefresh.click(onClickRefresh);
    $btnDelete.click(onClickDelete);

    $dataForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveElement();
        }
    });

    $table.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
    });
    
    
    $btnConfirmDelete.click(onClickBtnConfirmDelete);
    
    $btnEdit.click(onClickBtnEdit);
    
    $('#categoryArticle').autocomplete({
        source: function (request, response) {
            $.ajax({
                type: "POST",
                url: $.PATH + "article/findByCompany",
                data: {
                    name: request.term
                },
                success: function (data) {
                    response($.map(data, function (categoryArticle) {
                        return {
                            label: categoryArticle.name,
                            value: categoryArticle.name,
                            id: categoryArticle.id
                        };
                    }));
                }
            });
        },
        minLength: 2,
        select: function (event, ui) {
            $('#idCategoryArticle').val(ui.item.id);
        }
    });

    $('#size').autocomplete({
        source: function (request, response) {
            $.ajax({
                type: "POST",
                url: $.PATH + "article/findSizesByCompany",
                data: {
                    name: request.term
                },
                success: function (data) {
                    response($.map(data, function (size) {
                        return {
                            label: size.name,
                            value: size.name,
                            id: size.id
                        };
                    }));
                }
            });
        },
        minLength: 2,
        select: function (event, ui) {
            $('#idSize').val(ui.item.id);
        }
    });
}

function onClickNew() {
    _uiUtil.cleanControls($saveModal);
    $('#titleModalNew').html("Agregar");
    $('#idNew').val(0);
    $saveModal.modal();
}

function onClickRefresh() {
    findData();
}

function onClickDelete() {
    deleteElement();
}

function addRowToTable(item, table) {
    var noRow = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noRow + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.sku + "</td>";
    fila += "<td>" + item.description + "</td>";
    fila += "<td>" + item.categoryArticle.name + "</td>";
    fila += "<td>" + item.size.name + "</td>";
    fila += "<td align='right'>" + accounting.formatMoney(item.cost) + "</td>";
    fila += "<td align='right'>" + accounting.formatMoney(item.price) + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + item.unity + "</td>";
    fila += "<td>" + _uiUtil.getBooleanValueLabel(item.requiredStock) + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "article/findArticles",
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($table);
            _data = [];
            _indexSelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTable(item, $table);
                    _data.push(item);
                });
                $table.tablePagination(_uiUtil.getOptionsPaginator(10));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function deleteElement() {
    var id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "article/deleteArticle",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Artículo eliminado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModal').modal('hide');
            findData();
        }
    });
}

function saveElement() {
    let id = $('#idNew').val();
    let sku = $('#sku').val();
    let name = $('#name').val();
    let description = $('#description').val();
    let cost = $('#cost').val();
    let price = $('#price').val();
    let status = $('#status').val();
    let unity = $('#unity').val();
    let idCategoryArticle = $('#idCategoryArticle').val();
    let idSize = $('#idSize').val();
    let requiredStock = $('#requiredStock').is(':checked');
    $.ajax({
        type: "POST",
        url: $.PATH + "article/saveArticle",
        data: {
            id: id, 
            name: name, 
            sku: sku,
            description: description,
            cost: cost,
            price: price,
            status: status, 
            unity: unity,
            idCategoryArticle: idCategoryArticle,
            idSize: idSize,
            requiredStock: requiredStock
        },
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Artículo guardado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $saveModal.modal('hide');
            findData();
        }
    });
}

function onClickBtnConfirmDelete(){
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un artículo', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModal').modal();
}

function onClickBtnEdit() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un artículo', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    
    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#name').val(item.name);
    $('#sku').val(item.sku);
    $('#description').text(item.description);
    $('#cost').val(item.cost);
    $('#price').val(item.price);
    $('#status').val(item.status);
    $('#unity').val(item.unity);
    $('#idCategoryArticle').val(item.categoryArticle.id);
    $('#categoryArticle').val(item.categoryArticle.name);
    $('#idSize').val(item.size.id);
    $('#size').val(item.size.name);
    $('#requiredStock').removeAttr("checked");
    if(item.requiredStock)
        $('#requiredStock').attr("checked", "checked");
    $saveModal.modal();
}


