var $btnNew,
        $btnRefresh,
        $btnDelete,
        $btnEdit,
        $btnConfirmDelete;

var $saveModal,
        $dataForm;
//filters
var $initDate,
        $endDate,
        $idCategoryF,
        $idSubcategoryF,
        $expenseForm;

var $amount,
    $description,
    $idCategory,
    $idSubcategory,
    $dateE;

var $optionsModal,
        $dataFormOption;

var $table;

var _indexSelected = -1,
        _data = [];

$(document).ready(function () {
    initComponents();
    initEvents();
    initData();
    findData();
});

function initComponents() {
    $btnNew = $('#btnNew');
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');
    $btnEdit = $('#btnEdit');
    $btnConfirmDelete = $('#btnConfirmDelete');

    $initDate = $('#initDate');
    $endDate = $('#endDate');
    $idCategoryF = $('#idCategoryF');
    $idSubcategoryF = $('#idSubcategoryF');
    $expenseForm = $('#expenseForm');

    //form
    $amount = $('#amount');
    $description = $('#description');
    $idCategory = $('#idCategory');
    $idSubcategory = $('#idSubcategory');
    $dateE = $('#dateE');

    $saveModal = $('#saveModal');
    $dataForm = $('#dataForm');

    $optionsModal = $('#optionsModal');
    $dataFormOption = $('#dataFormOption');

    $table = $('#dataTable');
}

function initEvents() {
    $btnNew.click(onClickNew);
    $btnRefresh.click(onClickRefresh);
    $btnDelete.click(onClickDelete);
    $initDate.focusout(onFocusOutInit);
    $endDate.focusout(onFocusOutEnd);

    $dataForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveElement();
        }
    });

    $dataFormOption.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
        }
    });
    
    $expenseForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            findData();
        }
    });

    $table.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
    });

    $btnConfirmDelete.click(onClickBtnConfirmDelete);

    $btnEdit.click(onClickBtnEdit);

    $idCategoryF.change(function () {
        findSubcategories($idCategoryF.val(), $idSubcategoryF, '--Todos');
    });

    $idCategory.change(function () {
        findSubcategories($idCategory.val(), $idSubcategory, '--Seleccione');
    });

}

function onFocusOutInit() {
    if (_jsUtil.compareDate($initDate.val(), $endDate.val(), 'yyyy-MM-dd')) {
        $endDate.val($initDate.val());
    }
}

function onFocusOutEnd() {
    if (_jsUtil.compareDate($initDate.val(), $endDate.val(), 'yyyy-MM-dd')) {
        $initDate.val($endDate.val());
    }
}

function onClickNew() {
    _uiUtil.cleanControls($saveModal);
    $('#titleModalNew').html("Agregar");
    $('#idNew').val(0);
    $dateE.val(_uiUtil.today());
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
    fila += "<td>" + _uiUtil.getFormattedDate(item.date); + "</td>";
    fila += "<td align='right'>" + accounting.formatMoney(item.amount) + "</td>";
    fila += "<td>" + item.subcategory.father.name+"</td>";
    fila += "<td>" + item.subcategory.name + "</td>";
    fila += "<td>" + item.description + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnEdit() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un gasto', 'warning');
        return;
    }
    var item = _data[_indexSelected];

    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#amount').val(item.amount);
    $('#description').val(item.description);
    $('#idCategory').val(item.subcategory.father.id);
    $('#dateE').val(_uiUtil.getFormattedDateUS(item.date));
    
    findSubcategories($idCategory.val(), $idSubcategory, '--Seleccione');
    $('#idSubcategory').val(item.subcategory.id);
    $saveModal.modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "expense/findExpenses",
        data: {initDate: $initDate.val(), endDate: $endDate.val(),idCategory: $idCategoryF.val(), 
            idSubcategory: $idSubcategoryF.val()},
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
        url: $.PATH + "expense/deleteExpense",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Gasto eliminado con éxito", 'success');
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
    
    $.ajax({
        type: "POST",
        url: $.PATH + "expense/saveExpense",
        data: {id: id, amount: $amount.val(), description: $description.val(),
            idSubcategory:$idSubcategory.val(),dateE: $dateE.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Gasto guardado con éxito", 'success');
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

function onClickBtnConfirmDelete() {
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un gasto', 'warning');
        return;
    }
    var item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar el gasto con importe <b>" + item.amount + "</b>?");
    $('#confirmModal').modal();

}

function initData() {
    $initDate.val(_uiUtil.today());
    $endDate.val(_uiUtil.today());
}

function findSubcategories(idCategory, select, text) {
    select.empty();
    select.append("<option value='0'>" + text + "</option>");

    $.ajax({
        type: "POST",
        url: $.PATH + "expense/findSubcategories",
        async: false,
        data: {idCategory: idCategory},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    select.append("<option value='" + item.id + "'>" + item.name + "</option>");
                });
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}
