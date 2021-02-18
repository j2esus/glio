let $filterForm, $table;

$(document).ready(function () {
    initComponents();
    initEvents();
    findData();
});

function initComponents(){
    $filterForm = $('#filterForm');
    $table = $('#table');
}

function initEvents(){
    $filterForm.submit(function (e) {
        e.preventDefault();
        findData();
    });
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "availableStock/findAvailableStockGroupedByArticle",
        data: {
            articleCoincidence: $('#nameF').val(),
            idCategory: $('#categoryF').val()
        },
        beforeSend: function () {
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

function addRowToTable(item, table) {
    let noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    let fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.sku + "</td>";
    fila += "<td>" + item.unity + "</td>";
    fila += "<td>" + item.category + "</td>";
    fila += "<td>" + item.size + "</td>";
    fila += "<td>" + (item.stockTypeIn - item.stockTypeOut) + "</td>";
    fila += "</tr>";
    table.append(fila);
}