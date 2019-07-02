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

    $table = $('#dataTableSession');
}

function initEvents() {
    $btnRefresh.click(onClickRefresh);
    $btnDelete.click(onClickDelete);
}

function onClickRefresh() {
    findData();
}

function onClickDelete(){
    finishElement();
}

function addRowToTable(item, table) {
    var noRow = parseInt(table.find("tbody").eq(0).find("tr").length) + 1;
    
    var endDate = item.endDate === null ? '':_uiUtil.getFormattedDateTime(item.endDate);

    var fila = "";
    fila += "<tr><input type='hidden' id='id" + noRow + "' value='" + item.id + "'/>";
    fila += "<td><input type='hidden' id='session" + noRow + "' value='" + item.session + "'/>" + item.session + "</td>";
    fila += "<td>" + item.father.username + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDateTime(item.initDate) + "</td>";
    fila += "<td>" + endDate + "</td>";
    if(item.status === 'CLOSED')
        fila += "<td align='center'>" + "<a class='btn btn-primary fa fa-times' onclick='return 0;'></a>" + "</td>";
    else
        fila += "<td align='center'>" + "<a class='btn btn-danger fa fa-times' onclick='confirmDialog(" + noRow +");'></a>" + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function confirmDialog(noRow){
    var id = $('#id'+noRow).val();
    var label = $('#session'+noRow).val();
    $('#idDelete').val(id);
    $('#deleteLabel').html("¿Está seguro de finalizar <b>"+label+"</b>?");
    $('#confirmModal').modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "session/findSessions",
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

function finishElement(){
    var id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "session/finishSession",
        data: { id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Sesión finalizada con éxito", 'success');
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


