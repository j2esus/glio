var $btnNew,
    $btnRefresh,
    $btnDelete;
    
var $saveModal,
    $dataForm;

var $table;

$(document).ready(function () {
    initComponents();
    initEvents();
    findData();
});

function initComponents() {
    $btnNew = $('#btnNew');
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');
    
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

function onClickDelete(){
    deleteElement();
}

function addRowToTable(item, table) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length) + 1;

    var fila = "";
    fila += "<tr><input type='hidden' id='id" + noFila + "' value='" + item.id + "'/>";
    fila += "<td><input type='hidden' id='name" + noFila + "' value='" + item.name + "'/>" + item.name + "</td>";
    fila += "<td><input type='hidden' id='description" + noFila + "' value='" + item.description + "'/>" + item.description + "</td>";
    fila += "<td><input type='hidden' id='status" + noFila + "' value='" + item.status + "'/>" + item.status + "</td>";
    fila += "<td><input type='hidden' id='totalUser" + noFila + "' value='" + item.totalUser + "'/>" + item.totalUser + "</td>";
    fila += "<td align='center'>" + "<a class='btn btn-primary fa fa-pencil-square-o' onclick='editData(" + noFila + ");'></a>" + "</td>";
    fila += "<td align='center'>" + "<a class='btn btn-danger fa fa-trash-o' onclick='confirmDialog(" + noFila +");'></a>" + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function confirmDialog(noRow){
    var id = $('#id'+noRow).val();
    var label = $('#name'+noRow).val();
    $('#idDelete').val(id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>"+label+"</b>?");
    $('#confirmModal').modal();
}

function editData(noRow){
    $('#titleModalNew').html("Editar");
    $('#idNew').val($('#id'+noRow).val());
    $('#name').val($('#name'+noRow).val());
    $('#description').text($('#description'+noRow).val());
    $('#status').val($('#status'+noRow).val());
    $('#totalUser').val($('#totalUser'+noRow).val());
    
    $saveModal.modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "company/findCompanies",
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($table);
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTable(item, $table);
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
    var id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "company/deleteCompany",
        data: { id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Empresa eliminada con éxito", 'success');
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
    var description = $('#description').val();
    var status = $('#status').val();
    var totalUser = $('#totalUser').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "company/saveCompany",
        data: { id: id, name: name,description: description, status: status, totalUser:totalUser},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Empresa guardada con éxito", 'success');
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


