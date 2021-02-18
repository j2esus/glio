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

    $table = $('#dataTableUser');
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
    $('#password').prop('disabled', false);
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
    fila += "<td>" + item.username + "</td>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + item.userType.name + "</td>";
    fila += "<td>" + item.email + "</td>";
    fila += "<td>" + _uiUtil.getBooleanValueLabel(item.onlyOneAccess) + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function confirmDialog(noRow) {
    var id = $('#id' + noRow).val();
    var label = $('#name' + noRow).val();
    $('#idDelete').val(id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>" + label + "</b>?");
    $('#confirmModal').modal();
}

function onClickBtnEdit() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];

    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#username').val(item.username.split("@")[0]);
    $('#name').val(item.name);
    $('#password').val(item.password);
    $('#userType').val(item.userType.id);
    $('#status').val(item.status);
    $('#email').val(item.email);
    $('#onlyOneAccess').removeAttr("checked");
    if(item.onlyOneAccess)
        $('#onlyOneAccess').attr("checked", "checked");
    $('#password').prop('disabled', true);
    $saveModal.modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "user/findUsers",
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
        url: $.PATH + "user/deleteUser",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Usuario eliminado con éxito", 'success');
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
    var username = $('#username').val() + $('#company').html();

    var name = $('#name').val();
    var password = $('#password').val();

    var status = $('#status').val();
    var idUserType = $('#userType').val();
    var onlyOneAccess = $('#onlyOneAccess').is(':checked');
    var email = $('#email').val();

    $.ajax({
        type: "POST",
        url: $.PATH + "user/saveUser",
        data: {id: id, username: username, password: password, name: name, status: status,
            idUserType: idUserType, onlyOneAccess: onlyOneAccess, email: email},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Usuario guardado con éxito", 'success');
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
        _notify.show('Debes seleccionar un usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModal').modal();
    
}