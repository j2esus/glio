var $btnRefresh,
    $btnDelete;

var $table;

$(document).ready(function () {
    initComponents();
    initEvents();
    findData();
});

function initComponents() {
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');

    $table = $('#dataTableToken');
}

function initEvents() {
    $btnRefresh.click(onClickRefresh);
    $btnDelete.click(onClickDelete);
}

function onClickRefresh() {
    findData();
}

function onClickDelete(){
    deleteElement();
}

function addRowToTable(item, table) {
    var noRow = parseInt(table.find("tbody").eq(0).find("tr").length) + 1;
    

    var fila = "";
    fila += "<tr><input type='hidden' id='id" + noRow + "' value='" + item.id + "'/>";
    fila += "<td><input type='hidden' id='token" + noRow + "' value='" + item.token + "'/>" + item.token + "</td>";
    fila += "<td>" + item.father.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    if(item.status === 'CLOSED')
        fila += "<td align='center'>" + "<a class='btn btn-primary fa fa-times' onclick='return 0;'></a>" + "</td>";
    else
        fila += "<td align='center'>" + "<a class='btn btn-danger fa fa-trash-o' onclick='confirmDialog(" + noRow +");'></a>" + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function confirmDialog(noRow){
    var id = $('#id'+noRow).val();
    var label = $('#token'+noRow).val();
    $('#idDelete').val(id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>"+label+"</b>?");
    $('#confirmModal').modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "token/findTokens",
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
        url: $.PATH + "token/deleteToken",
        data: { id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Token eliminado con éxito", 'success');
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


