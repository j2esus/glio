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
    fila += "<td>" + item.description + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + item.totalUser + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "company/findCompanies",
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
        url: $.PATH + "company/deleteCompany",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Empresa eliminada con éxito", 'success');
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
    var id = $('#idNew').val();
    var name = $('#name').val();
    var description = $('#description').val();
    var status = $('#status').val();
    var totalUser = $('#totalUser').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "company/saveCompany",
        data: {id: id, name: name, description: description, status: status, totalUser: totalUser},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Empresa guardada con éxito", 'success');
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
        _notify.show('Debes seleccionar una empresa', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModal').modal();
}

function onClickBtnEdit() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar una empresa', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    
    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#name').val(item.name);
    $('#description').text(item.description);
    $('#status').val(item.status);
    $('#totalUser').val(item.totalUser);

    $saveModal.modal();
}