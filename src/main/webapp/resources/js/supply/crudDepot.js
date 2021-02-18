let $btnNew,
    $btnRefresh,
    $btnDelete,
    $btnEdit,
    $btnConfirmDelete;
    
let $saveModal,
    $dataForm;

let $dataFormOption;

let $table;

let _indexSelected = -1,
    _data = [];

let $txtFilterQuery, $txtFilterStatus;

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
    
    $dataFormOption = $('#dataFormOption');

    $txtFilterQuery = $('#txtFilterQuery');
    $txtFilterStatus = $('#txtFilterStatus');

    $table = $('#table');
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
    
    $dataFormOption.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveOption();
        }
    });
    
    $table.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
    });
    
    $btnConfirmDelete.click(onClickBtnConfirmDelete);
    
    $btnEdit.click(onClickBtnEdit);
}

function onClickNew() {
    _uiUtil.cleanControls($saveModal);
    $('#titleModalNew').html("Agregar");
    $('#idNew').val(0);
    $('#status').val($("#status option:first").val());
    $saveModal.modal();
}

function onClickRefresh() {
    findData();
}

function onClickDelete(){
    deleteElement();
}

function addRowToTable(item, table) {
    let noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    let fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnEdit(){
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un almacen ', 'warning');
        return;
    }
    let item = _data[_indexSelected];
    
    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#name').val(item.name);
    $('#status').val(item.status);
    
    $saveModal.modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "depot/findByNameAndStatus",
        data: {
            name: $txtFilterQuery.val(),
            status: $txtFilterStatus.val()
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($table);
            _indexSelected = -1;
            _data = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTable(item, $table);
                    _data.push(item);
                });
                $table.tablePagination(_uiUtil.getOptionsPaginator(10));
            } else {
                _notify.show("La consulta no produjo resultados.","danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}


function deleteElement(){
    let id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "depot/delete",
        data: { id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Almacen eliminado con éxito", 'success');
            }else{
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModal').modal('hide');
            findData();
        }
    });
}

function saveElement(){
    let id = $('#idNew').val();
    let name = $('#name').val();
    let status = $('#status').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "depot/save",
        data: { id: id, name: name, status: status},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Almacen guardado con éxito", 'success');
            }else{
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
        _notify.show('Debes seleccionar un almacen', 'warning');
        return;
    }
    let item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>"+item.name+"</b>?");
    $('#confirmModal').modal();
}
