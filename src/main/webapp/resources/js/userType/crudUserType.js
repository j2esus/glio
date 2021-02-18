var $btnNew,
    $btnRefresh,
    $btnDelete,
    $btnEdit,
    $btnConfirmDelete,
    $btnAccess;
    
var $saveModal,
    $dataForm;

var $optionsModal,
    $dataFormOption;

var $table;

var _totalOptions = 0;

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
    $btnAccess = $('#btnAccess');
    
    $saveModal = $('#saveModal');
    $dataForm = $('#dataForm');
    
    $optionsModal = $('#optionsModal');
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
            saveOption();
        }
    });
    
    $table.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
    });
    
    $btnConfirmDelete.click(onClickBtnConfirmDelete);
    
    $btnEdit.click(onClickBtnEdit);
    
    $btnAccess.click(onClickBtnAccess);
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
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function addRowToTableOptions(item, table) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr><input type='hidden' id='idOption" + noFila + "' value='" + item.idOptionMenu + "'/>";
    if(item.assigned){
        fila += "<td><input type='checkbox' id='check"+noFila+"' name = 'check"+noFila+"' checked = 'checked'/></td>";
    }else{
        fila += "<td><input type='checkbox' id='check"+noFila+"' name = 'check"+noFila+"'/></td>";
    }
    fila += "<td>" + item.categoryOptionName + "</td>";
    fila += "<td>" + item.optionMenuName + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnEdit(){
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

function onClickBtnAccess(){
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un tipo de usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    
    $('#titleModalOption').html("Agregar opciones menú");
    $('#idUserType').val(item.id);
    $('#nameUserType').val(item.name);
    $("#checkAll").prop("checked", false);
    $optionsModal.modal();
    findOptions();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/findUserTypes",
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

function findOptions() {
    var idUserType = $('#idUserType').val();
    var table = $('#dataTableOptions');
    
    _totalOptions = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/findOptions",
        data: {idUserType: idUserType},
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable(table);
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTableOptions(item, table);
                    _totalOptions++;
                });
                table.tablePagination(_uiUtil.getOptionsPaginator(4));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}



function deleteElement(){
    var id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/deleteUserType",
        data: { id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Tipo de usuario eliminado con éxito", 'success');
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
    var id = $('#idNew').val();
    var name = $('#name').val();
    var status = $('#status').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/saveUserType",
        data: { id: id, name: name, status: status},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Tipo usuario guardado con éxito", 'success');
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

function selectAll(){
    var checkAll = $("#checkAll").is(":checked");
    if (checkAll) {
        for (var i = 0; i <= _totalOptions; i++) {
            $("#check" + i).prop("checked", true);
        }
    } else {
        for (var i = 0; i <= _totalOptions; i++) {
            $("#check" + i).prop("checked", false);
        }
    }
}

function saveOption(){
    var idUserType = $('#idUserType').val();
    var optionsAdd = "";
    var optionsDel = "";
    
    var i = 0;
    while ($("#check" + i).val() != undefined) {
        if ($("#check" + i).is(":checked")) {
            optionsAdd += $('#idOption' + i).val()+",";
        }else{
            optionsDel += $('#idOption' + i).val()+",";
        }
        i++;
    }
    
    if(optionsAdd.length > 0){
        optionsAdd = optionsAdd.substring(0, optionsAdd.length-1);
    }
    
    if(optionsDel.length > 0){
        optionsDel = optionsDel.substring(0, optionsDel.length-1);
    }
    
    $.ajax({
        type: "POST",
        url: $.PATH + "userType/saveOptions",
        data: { idUserType: idUserType, optionsAdd: optionsAdd,optionsDel:optionsDel},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Opciones guardadas con éxito", 'success');
            }else{
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $optionsModal.modal('hide');
            findData();
        }
    });
}

function onClickBtnConfirmDelete(){
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un tipo de usuario', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>"+item.name+"</b>?");
    $('#confirmModal').modal();
    
}
