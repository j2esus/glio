var $btnNewCa,
        $btnConfirmDeleteCa,
        $btnEditCa,
        $btnRefreshCa,
        $dataTableCa,
        $saveModalCategory,
        $dataFormCategory,
        $txtQueryCategory,
        $btnDeleteCategory;


var _indexCategorySelected = -1, _categoryData = [];


var $btnNewSu,
        $btnConfirmDeleteSu,
        $btnEditSu,
        $btnRefreshSu,
        $dataTableSu,
        $saveModalSubcategory,
        $dataFormSubcategory,
        $txtQuerySubcategory,
        $btnDeleteSubcategory;

var _indexSubcategorySelected = -1, _subcategoryData = [];

$(document).ready(function () {
    initComponents();
    initEvents();
    findCategoriesData();
});

function initComponents() {
    //category
    $btnNewCa = $('#btnNewCa');
    $btnConfirmDeleteCa = $('#btnConfirmDeleteCa');
    $btnEditCa = $('#btnEditCa');
    $btnRefreshCa = $('#btnRefreshCa');
    $dataTableCa = $('#dataTableCa');
    $saveModalCategory = $('#saveModalCategory');
    $dataFormCategory = $('#dataFormCategory');
    $txtQueryCategory = $('#txtQueryCategory');
    $btnDeleteCategory = $('#btnDeleteCategory');

    //subcategory
    $btnNewSu = $('#btnNewSu');
    $btnConfirmDeleteSu = $('#btnConfirmDeleteSu');
    $btnEditSu = $('#btnEditSu');
    $btnRefreshSu = $('#btnRefreshSu');
    $dataTableSu = $('#dataTableSu');
    $saveModalSubcategory = $('#saveModalSubcategory');
    $dataFormSubcategory = $('#dataFormSubcategory');
    $txtQuerySubcategory = $('#txtQuerySubcategory');
    $btnDeleteSubcategory = $('#btnDeleteSubcategory');

}

function initEvents() {
    //category
    $btnNewCa.click(btnNewCaOnClick);
    $btnRefreshCa.click(btnRefreshCaOnClick);
    $btnEditCa.click(btnEditCaOnClick);
    $btnConfirmDeleteCa.click(btnConfirmDeleteCaOnClick);
    $btnDeleteCategory.click(btnDeleteCategoryOnClick);
    
    $dataFormCategory.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveCategory();
        }
    });
    
    $dataTableCa.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexCategorySelected = $(this).data('meta-row');
        findSubcategoriesData();
    });
    
    $txtQueryCategory.keypress(function (e) {
        if (e.which == 13) {
            findCategoriesData();
        }
    });
    
    //subcategory
    $btnNewSu.click(btnNewSuOnClick);
    $btnRefreshSu.click(btnRefreshSuOnClick);
    $btnEditSu.click(btnEditSuOnClick);
    $btnConfirmDeleteSu.click(btnConfirmDeleteSuOnClick);
    $btnDeleteSubcategory.click(btnDeleteSubcategoryOnClick);
    
    $dataFormSubcategory.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveSubcategory();
        }
    });
    
    $dataTableSu.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSubcategorySelected = $(this).data('meta-row');
    });
    
    $txtQuerySubcategory.keypress(function (e) {
        if (e.which == 13) {
            findSubcategoriesData();
        }
    });
    
}

function findCategoriesData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "category/findCategories",
        data: {name: $txtQueryCategory.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableCa);
            _indexCategorySelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                _categoryData = [];
                $.each(items, function (i, item) {
                    addRowToTable(item, $dataTableCa);
                    _categoryData.push(item);
                });
                $dataTableCa.tablePagination(_uiUtil.getOptionsPaginator(10));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
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

function btnRefreshCaOnClick() {
    findCategoriesData();
}

function btnNewCaOnClick() {
    _uiUtil.cleanControls($saveModalCategory);
    $('#titleModalNewCategory').html("Agregar categoría");
    $('#idNewCategory').val(0);
    $('#statusCategory').val($("#statusCategory option:first").val());
    $saveModalCategory.modal();
}

function btnEditCaOnClick(){
    if (_indexCategorySelected === -1) {
        _notify.show('Selecciona primero una categoría', 'warning');
        return;
    }

    var item = _categoryData[_indexCategorySelected];
    $('#titleModalNewCategory').html("Editar categoría");
    $('#idNewCategory').val(item.id);
    $('#nameCategory').val(item.name);
    $('#statusCategory').val(item.status);
    $saveModalCategory.modal();
}

function btnConfirmDeleteCaOnClick(){
    if (_indexCategorySelected === -1) {
        _notify.show('Selecciona primero una categoría', 'warning');
        return;
    }

    var item = _categoryData[_indexCategorySelected];
    
    $('#idDeleteCategory').val(item.id);
    $('#deleteLabelCategory').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModalCategory').modal();
}

function saveCategory(){
    var id = $('#idNewCategory').val();
    var name = $('#nameCategory').val();
    var status = $('#statusCategory').val();
    
    $.ajax({
        type: "POST",
        url: $.PATH + "category/saveCategory",
        data: {id: id, name: name, status: status},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Categoría guardada con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $saveModalCategory.modal('hide');
            findCategoriesData();
        }
    });
}

function btnDeleteCategoryOnClick(){
    deleteCategory();
}

function btnDeleteSubcategoryOnClick(){
    deleteSubcategory();
}

function deleteCategory() {
    var id = $('#idDeleteCategory').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "category/deleteCategory",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Categoría eliminada con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalCategory').modal('hide');
            findCategoriesData();
        }
    });
}

//subcategory

function findSubcategoriesData() {
    if (_indexCategorySelected === -1) {
        _notify.show('Selecciona primero una categoría', 'warning');
        return;
    }
    
    var item = _categoryData[_indexCategorySelected];
    $.ajax({
        type: "POST",
        url: $.PATH + "category/findSubcategories",
        data: {idCategory: item.id, name: $txtQuerySubcategory.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableSu);
            _indexSubcategorySelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                _subcategoryData = [];
                $.each(items, function (i, item) {
                    _subcategoryData.push(item);
                    addRowToTableSub(item, $dataTableSu);
                });
                $dataTableSu.tablePagination(_uiUtil.getOptionsPaginator(5));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTableSub(item, table) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function btnNewSuOnClick() {
    if (_indexCategorySelected === -1) {
        _notify.show('Selecciona primero una categoría', 'warning');
        return;
    }
    
    var item = _categoryData[_indexCategorySelected];
    $('#divTitleCategory').html(item.name);
    _uiUtil.cleanControls($saveModalSubcategory);
    $('#titleModalNewSubcategory').html("Agregar subcategoría");
    $('#idNewSubcategory').val(0);
    $('#statusSubcategory').val($("#statusSubcategory option:first").val());
    $saveModalSubcategory.modal();
}

function btnRefreshSuOnClick(){
    findSubcategoriesData();
}

function saveSubcategory(){
    var id = $('#idNewSubcategory').val();
    var name = $('#nameSubcategory').val();
    var status = $('#statusSubcategory').val();
    var item = _categoryData[_indexCategorySelected];
    
    $.ajax({
        type: "POST",
        url: $.PATH + "category/saveSubcategory",
        data: {id: id, name: name, status: status, idCategory: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Subcategoría guardado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $saveModalSubcategory.modal('hide');
            findSubcategoriesData();
        }
    });
}

function btnEditSuOnClick(){
    if (_indexSubcategorySelected === -1) {
        _notify.show('Selecciona primero una subcategoría', 'warning');
        return;
    }

    var item = _subcategoryData[_indexSubcategorySelected];
    $('#titleModalNewSubcategory').html("Editar subcategoría");
    $('#idNewSubcategory').val(item.id);
    $('#nameSubcategory').val(item.name);
    $('#statusSubcategory').val(item.status);
    $saveModalSubcategory.modal();
}

function btnConfirmDeleteSuOnClick(){
    if (_indexSubcategorySelected === -1) {
        _notify.show('Selecciona primero una subcategoría', 'warning');
        return;
    }

    var item = _subcategoryData[_indexSubcategorySelected];
    
    $('#idDeleteSubcategory').val(item.id);
    $('#deleteLabelSubcategory').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModalSubcategory').modal();
}

function btnDeleteSubcategoryOnClick(){
    deleteSubcategory();
}

function deleteSubcategory() {
    var id = $('#idDeleteSubcategory').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "category/deleteSubcategory",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Subcategoría eliminada con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalSubcategory').modal('hide');
            findSubcategoriesData();
        }
    });
}