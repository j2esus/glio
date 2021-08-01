var $btnNew,
        $btnRefresh,
        $btnDelete,
        $btnEdit,
        $btnConfirmDelete,
        $btnSaveOption;

var $saveModal,
        $dataForm;

var $dataFormOption;

var $table;

var _totalOptions = 0;

var _indexSelected = -1,
        _data = [];

$(document).ready(function () {
    initComponents();
    initEvents();
    findData();
    getPublicOptions();
});

function initComponents() {
    $btnNew = $('#btnNew');
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');
    $btnEdit = $('#btnEdit');
    $btnConfirmDelete = $('#btnConfirmDelete');

    $saveModal = $('#saveModal');
    $dataForm = $('#dataForm');
    $btnSaveOption = $('#btnSaveOption');
    $btnSaveOption.hide();

    $dataFormOption = $('#dataFormOption');

    $table = $('#dataTableUserType');
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
            if ($('.checkOption').filter(':checked').length == 0) {
                _notify.show("Debes elegir al menos una funcionalidad", "danger");
                return;
            }
            saveOption();
        }
    });

    $table.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
        loadOptionsByUserType();
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

function onClickDelete() {
    deleteElement();
}

function addRowToTable(item, table) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnEdit() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un tipo de usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];

    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#name').val(item.name);
    $('#status').val(item.status);

    $saveModal.modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/findUserTypes",
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($table);
            uncheckAllOptionsMenu();
            _indexSelected = -1;
            _data = [];
            $("#checkAll").prop("checked", false);
            $btnSaveOption.hide();
            $('#nameUserType').html("");
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

function getOptionsByUserType() {
    var idUserType = $('#idUserType').val();

    $.ajax({
        type: "POST",
        url: $.PATH + "userType/getOptionsByUserType",
        data: {idUserType: idUserType},
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            $("#checkAll").prop("checked", false);
            uncheckAllOptionsMenu();
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    $("#check_"+item.id).prop("checked", true);
                });
                $btnSaveOption.show();
            }
        }, complete: function () {
            _blockUI.unblock();
            checkAllIfAllOptionsAreChecked();
        }
    });
}

function deleteElement() {
    var id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/deleteUserType",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Tipo de usuario eliminado con éxito", 'success');
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
    var status = $('#status').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/saveUserType",
        data: {id: id, name: name, status: status},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Tipo usuario guardado con éxito", 'success');
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

function selectAll() {
    var checkAll = $("#checkAll").is(":checked");
    if (checkAll) {
        checkAllOptionsMenu();
    } else {
        uncheckAllOptionsMenu();
    }
}

function checkAllOptionsMenu(){
    $("input:checkbox[name=checkOption]").each(function () {
        $(this).prop("checked", true);
    });
}

function uncheckAllOptionsMenu() {
    $("input:checkbox[name=checkOption]").each(function () {
        $(this).prop("checked", false);
    });
}

function saveOption() {
    var idUserType = $('#idUserType').val();
    let idOptionsMenu = [];
    $("input:checkbox[name=checkOption]:checked").each(function () {
        let idOption = $(this).prop("id").replace("check_", "");
        idOptionsMenu.push(idOption);
    });
    
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/saveOptions",
        data: {
            idUserType: idUserType, 
            'options[]': idOptionsMenu
        },
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function () {
            _notify.show("Opciones guardadas con éxito", 'success');
        }, complete: function () {
            _blockUI.unblock();
        },
        error: function (xhr, status, error) {
            _notify.show(xhr.responseJSON, 'danger');
        }
    });
}

function onClickBtnConfirmDelete() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un tipo de usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModal').modal();
}

function loadOptionsByUserType() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un tipo de usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];

    $('#idUserType').val(item.id);
    $('#nameUserType').html(item.name);
    getOptionsByUserType();
}

function checkAllIfAllOptionsAreChecked() {    
    let totalChecked = $('.checkOption').filter(':checked').length;
    if (_totalOptions == totalChecked)
        $("#checkAll").prop("checked", true);
    else
        $("#checkAll").prop("checked", false);
}

function getPublicOptions() {
    let table = $('#dataTableOptions');

    $.ajax({
        type: "POST",
        url: $.PATH + "userType/getPublicOptions",
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable(table);
            $("#checkAll").prop("checked", false);
            _totalOptions = 0;
        },
        success: function (items) {
            $.each(items, function (i, item) {
                addRowToTableOptions(item, table);
                _totalOptions++;
            });
            table.tablePagination(_uiUtil.getOptionsPaginator(10));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTableOptions(item, table) {
    var fila = "";
    fila += "<tr>";
    fila += "<td><input type='checkbox' name='checkOption' id='check_" + item.id + "'"+
            " onchange='checkAllIfAllOptionsAreChecked()' class='checkOption'/></td>";
    fila += "<td>" + item.category + "</td>";
    fila += "<td>" + item.name + "</td>";
    fila += "</tr>";
    table.append(fila);
}