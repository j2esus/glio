let $personType;

let $btnNew,
    $btnRefresh,
    $btnDelete,
    $btnEdit,
    $btnConfirmDelete;
    
let $saveModal,
    $dataForm;

let $filterForm;

let $table;

let _indexSelected = -1,
    _data = [];

let $divDetails, $divPerson;

let _titles = {
    'client': 'Cliente',
    'provider': 'Proveedor'
}

let $btnRefreshAd, $btnEditAd, $btnConfirmDeleteAd, $btnNewAd, $addressTable, $btnDeleteAd, $saveModalAd, $dataFormAd,
    $btnConfirmDefaultAd, $btnSetDefaultAd;
let _indexSelectedAd = -1, _dataAd;
let __addressUtil;

$(document).ready(function () {
    initComponents();
    initEvents();
    initPanels();
    findData();
});

function initPanels(){
    $divPerson.css("display", "block");
    $divDetails.css("display", "none");
}

function initComponents() {
    $personType = $('#personType');

    $btnNew = $('#btnNew');
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');
    $btnEdit = $('#btnEdit');
    $btnConfirmDelete = $('#btnConfirmDelete');
    
    $saveModal = $('#saveModal');
    $dataForm = $('#dataForm');
    $filterForm = $('#filterForm');

    $table = $('#table');

    $divDetails = $('#divDetails');
    $divPerson = $('#divPerson');

    $btnRefreshAd = $('#btnRefreshAd');
    $btnEditAd = $('#btnEditAd');
    $btnConfirmDeleteAd = $('#btnConfirmDeleteAd');
    $btnNewAd = $('#btnNewAd');
    $addressTable = $('#addressTable');
    $btnDeleteAd = $('#btnDeleteAd');
    $saveModalAd = $('#saveModalAd');
    $dataFormAd = $('#dataFormAd');
    $btnConfirmDefaultAd = $('#btnConfirmDefaultAd');
    $btnSetDefaultAd = $('#btnSetDefaultAd');

    __addressUtil = new mx.jeegox.Address($('#state'),$('#town'), $('#suburb'), $('#zipcode') );
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

    $filterForm.submit(function (e) {
        e.preventDefault();
        findData();
    });
    
    $table.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
    });

    $table.on('dblclick', 'tbody tr', function (event) {
        _indexSelected = $(this).data('meta-row');
        showDivDetails();
    });
    
    $btnConfirmDelete.click(onClickBtnConfirmDelete);
    $btnEdit.click(onClickBtnEdit);

    $addressTable.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelectedAd = $(this).data('meta-row');
    });

    $btnRefreshAd.click(onClickBtnRefreshAd);
    $btnConfirmDeleteAd.click(onClickBtnConfirmDeleteAd);
    $btnDeleteAd.click(onClickDeleteAd);
    $btnNewAd.click(onClickBtnNewAd);

    $dataFormAd.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveAddress();
        }
    });

    $btnEditAd.click(onClickBtnEditAd);
    $btnConfirmDefaultAd.click(onClickBtnConfirmDefaultAd);
    $btnSetDefaultAd.click(onClickBtnSetDefaultAd);
}

function onClickNew() {
    _uiUtil.cleanControls($saveModal);
    $('#titleModalNew').html("Agregar");
    $('#idNew').val(0);
    $('#status').val($("#status option:first").val());
    $saveModal.modal();
}

function showDivDetails(){
    let person = _data[_indexSelected];
    $divPerson.css('display', 'none');
    $divDetails.css('display', 'block');

    $('#divPersonName').html('<h5>' + person.name.toUpperCase() + '</h5>');
    $('#divPersonStatus').html(person.status == 'ACTIVE' ? '<h5><span class="badge badge-success">'+person.status+'</span></h5>' :
        '<h5><span class="badge badge-danger">'+person.status+'</span></h5>');
    $('#divPersonPhone').html(person.phone);
    $('#divPersonRFC').html(person.rfc);
    $('#divPersonEmail').html(person.email);

    findDataAd();
}

function showDivPerson(){
    $divPerson.css('display', 'block');
    $divDetails.css('display', 'none');
}

function onClickRefresh() {
    findData();
}

function onClickDelete(){
    deleteElement();
}

function addRowToTable(item, table) {
    let noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    let fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.email + "</td>";
    fila += "<td>" + item.phone + "</td>";
    fila += "<td>" + item.rfc + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnEdit(){
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un '+ _titles[$personType.val()], 'warning');
        return;
    }
    let item = _data[_indexSelected];
    
    $('#titleModalNew').html("Editar");
    $('#idNew').val(item.id);
    $('#name').val(item.name);
    $('#email').val(item.email);
    $('#phone').val(item.phone);
    $('#rfc').val(item.rfc);
    $('#status').val(item.status);
    
    $saveModal.modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/find",
        data: {
            name: $('#nameF').val(),
            email: $('#emailF').val(),
            phone: $('#phoneF').val(),
            rfc: $('#rfcF').val(),
            status: $('#statusF').val()
        },
        beforeSend: function () {
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

function deleteElement(){
    let id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/delete",
        data: { id: id},
        beforeSend: function () {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show(_titles[$personType.val()] + " eliminado con éxito", 'success');
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
    let id = $('#idNew').val();
    let name = $('#name').val();
    let email = $('#email').val();
    let phone = $('#phone').val();
    let rfc = $('#rfc').val();
    let status = $('#status').val();
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/save",
        data: {
            id: id,
            name: name,
            email: email,
            phone: phone,
            rfc: rfc,
            status: status
        },
        beforeSend: function () {
            _blockUI.block();
        },
        success: function (response) {
            _notify.show(_titles[$personType.val()] + " guardado con éxito", 'success');
        }, complete: function () {
            _blockUI.unblock();
            $saveModal.modal('hide');
            findData();
        },
        error: function (xhr, status, error) {
            _notify.show(xhr.responseJSON, 'danger');
        }
    });
}

function onClickBtnConfirmDelete(){
    if (_indexSelected === -1) {
        _notify.show('Debes seleccionar un '+ _titles[$personType.val()], 'warning');
        return;
    }
    let item = _data[_indexSelected];
    $('#idDelete').val(item.id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>"+item.name+"</b>?");
    $('#confirmModal').modal();
}

function findDataAd() {
    let person = _data[_indexSelected];
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/findAddress",
        data: {
            idPerson: person.id,
        },
        beforeSend: function () {
            _blockUI.block();
            _uiUtil.clearDataTable($addressTable);
            _indexSelectedAd = -1;
            _dataAd = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTableAd(item, $addressTable);
                    _dataAd.push(item);
                });
                $addressTable.tablePagination(_uiUtil.getOptionsPaginator(5));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTableAd(item, table) {
    let noFila = parseInt(table.find("tbody").eq(0).find("tr").length);
    let defaultAction = '';
    if(item.defaultt)
        defaultAction = '<span class="badge badge-success">Predeterminada</span>';
    let fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.address + "</td>";
    fila += "<td>" + item.state + "</td>";
    fila += "<td>" + item.town + "</td>";
    fila += "<td>" + item.suburb + "</td>";
    fila += "<td>" + item.zipcode + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + defaultAction + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnRefreshAd(){
    findDataAd();
}

function onClickBtnConfirmDeleteAd(){
    if (_indexSelectedAd === -1) {
        _notify.show('Debes seleccionar una dirección', 'warning');
        return;
    }
    let item = _dataAd[_indexSelectedAd];
    $('#idDeleteAd').val(item.id);
    $('#deleteLabelAd').html("¿Está seguro de eliminar <b>"+item.address+"</b>?");
    $('#confirmModalAd').modal();
}

function onClickDeleteAd(){
    deleteElementAddress();
}

function deleteElementAddress(){
    let id = $('#idDeleteAd').val();
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/deleteAddress",
        data: { id: id},
        beforeSend: function () {
            _blockUI.block();
        },
        success: function (response) {
            if(response === "OK"){
                _notify.show("Dirección eliminada con éxito", 'success');
            }else{
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalAd').modal('hide');
            findDataAd();
        }
    });
}

function onClickBtnNewAd() {
    _uiUtil.cleanControls($saveModalAd);
    $('#titleModalNewAd').html("Agregar");
    $('#idNewAd').val(0);
    $('#statusAd').val($("#status option:first").val());
    $('#defaultt').val('false');
    __addressUtil.loadStates();
    $saveModalAd.modal();
}

function saveAddress(){
    let person = _data[_indexSelected];
    let id = $('#idNewAd').val();
    let address = $('#address').val();
    let reference = $('#reference').val();
    let zipcode = $('#zipcode').val();
    let state = $('#state option:selected').text();
    let town = $('#town option:selected').text();
    let suburb = $('#suburb option:selected').text();
    let status = $('#statusAd').val();
    let defaultt = $('#defaultt').val() === 'false'? false: true;
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/saveAddress",
        data: {
            id: id,
            address: address,
            reference: reference,
            zipcode: zipcode,
            state: state,
            town: town,
            suburb: suburb,
            status: status,
            idPerson: person.id,
            defaultt: defaultt
        },
        beforeSend: function () {
            _blockUI.block();
        },
        success: function () {
            _notify.show("Dirección guardada con éxito", 'success');
        }, complete: function () {
            _blockUI.unblock();
            $saveModalAd.modal('hide');
            findDataAd();
        },
        error: function (xhr, status, error) {
            _notify.show(xhr.responseJSON, 'danger');
        }
    });
}

function onClickBtnEditAd(){
    if (_indexSelectedAd === -1) {
        _notify.show('Debes seleccionar una dirección ', 'warning');
        return;
    }
    let item = _dataAd[_indexSelectedAd];

    $('#titleModalNewAd').html("Editar");
    $('#idNewAd').val(item.id);
    $('#address').val(item.address);
    $('#reference').val(item.reference);
    $('#defaultt').val(item.defaultt);
    $('#status').val(item.status);

    __addressUtil.findSuburbsByZipCodeAndName(item.zipcode, item.suburb);
    $saveModalAd.modal();
}
function onClickBtnConfirmDefaultAd(){
    if (_indexSelectedAd === -1) {
        _notify.show('Debes seleccionar una dirección', 'warning');
        return;
    }
    let item = _dataAd[_indexSelectedAd];
    $('#idSetDefaultAd').val(item.id);
    $('#setDefaultLabelAd').html("¿Está seguro de poner  <b>"+item.address+"</b> como predeterminada?");
    $('#confirmModalSetDefaultAd').modal();
}

function onClickBtnSetDefaultAd(){
    setAddressDefault();
}

function setAddressDefault(){
    let id = $('#idSetDefaultAd').val();
    $.ajax({
        type: "POST",
        url: $.PATH + $personType.val() + "/setDefaultAddress",
        data: { id: id},
        beforeSend: function () {
            _blockUI.block();
        },
        success: function () {
            _notify.show("La dirección se puso como predeterminada con éxito", 'success');
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalSetDefaultAd').modal('hide');
            findDataAd();
        }
    });
}