var $divDataUser,
        $divChangePassword,
        $divTokens;

var $btnDataUser,
        $btnChangePassword,
        $btnToken;

var $txtUsername,
        $txtName,
        $idUserData,
        $dataUserForm,
        $txtEmail;

var $txtPassword,
        $txtNewPassword,
        $txtConfirmPassword,
        $idUserPassword,
        $changePasswordForm;

var $btnRefresh,
        $btnDelete,
        $table;

$(document).ready(function () {
    initComponents();
    initPanels();
    initEvents();
});

function initComponents() {
    //personal data
    $divDataUser = $('#divDataUser');
    $btnDataUser = $('#btnDataUser');
    $txtUsername = $('#txtUsername');
    $txtName = $('#txtName');
    $dataUserForm = $('#dataUserForm');
    $idUserData = $('#idUserData');
    $txtEmail = $('#txtEmail');

    //change password
    $divChangePassword = $('#divChangePassword');
    $btnChangePassword = $('#btnChangePassword');
    $txtPassword = $('#txtPassword');
    $txtNewPassword = $('#txtNewPassword');
    $txtConfirmPassword = $('#txtConfirmPassword');
    $changePasswordForm = $('#changePasswordForm');
    $idUserPassword = $('#idUserPassword');

    //tokens
    $divTokens = $('#divTokens');
    $btnToken = $('#btnToken');
    $btnRefresh = $('#btnRefresh');
    $btnDelete = $('#btnDelete');
    $table = $('#dataTableToken');
}

function initPanels() {
    $divDataUser.css("display", "block");
    $divChangePassword.css("display", "none");
    $divTokens.css("display", "none");
}

function initEvents() {
    $btnDataUser.click(onClickBtnDataUser);
    $btnChangePassword.click(onClickBtnChangePassword);
    $btnToken.click(onClickBtnToken);
    //$dataUserForm.submit(onSubmitDataUserForm);

    $dataUserForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            changeUserData($txtUsername.val(), $txtName.val(), $idUserData.val(), $txtEmail.val());
        }
    });


    $changePasswordForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            changePassword($txtPassword.val(), $txtNewPassword.val(), $txtConfirmPassword.val(), $idUserPassword.val());
        }
    });

    $btnRefresh.click(onClickRefresh);
    $btnDelete.click(onClickDelete);
}

function changeUserData(username, name, idUser, email) {
    $.ajax({
        type: "POST",
        url: $.PATH + "all/changeUserData",
        data: "username=" + username + "&name=" + name + "&idUser=" + idUser + "&email=" + email,
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Datos guardados con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function changePassword(password, newPassword, confirmPassword, idUser) {
    $.ajax({
        type: "POST",
        url: $.PATH + "all/changePassword",
        data: "password=" + password + "&newPassword=" + newPassword + "&confirmPassword=" + confirmPassword + "&idUser=" + idUser,
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Password cambiado con éxito con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function onClickRefresh() {
    findData();
}

function onClickDelete() {
    deleteElement();
}

function deleteElement() {
    var id = $('#idDelete').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "all/deleteToken",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Token eliminado con éxito", 'success');
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

function onClickBtnDataUser() {
    $divDataUser.css("display", "block");
    $divChangePassword.css("display", "none");
    $divTokens.css("display", "none");
}

function onClickBtnChangePassword() {
    $divDataUser.css("display", "none");
    $divChangePassword.css("display", "block");
    $divTokens.css("display", "none");
}

function onClickBtnToken() {
    $divDataUser.css("display", "none");
    $divChangePassword.css("display", "none");
    $divTokens.css("display", "block");
    findData();
}

function addRowToTable(item, table) {
    var noRow = parseInt(table.find("tbody").eq(0).find("tr").length) + 1;


    var fila = "";
    fila += "<tr><input type='hidden' id='id" + noRow + "' value='" + item.id + "'/>";
    fila += "<td><input type='hidden' id='token" + noRow + "' value='" + item.token + "'/>" + item.token + "</td>";
    fila += "<td>" + item.father.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    if (item.status === 'CLOSED')
        fila += "<td align='center'>" + "<a class='btn btn-primary fa fa-times' onclick='return 0;'></a>" + "</td>";
    else
        fila += "<td align='center'>" + "<a class='btn btn-danger fa fa-trash-o' onclick='confirmDialog(" + noRow + ");'></a>" + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function confirmDialog(noRow) {
    var id = $('#id' + noRow).val();
    var label = $('#token' + noRow).val();
    $('#idDelete').val(id);
    $('#deleteLabel').html("¿Está seguro de eliminar <b>" + label + "</b>?");
    $('#confirmModal').modal();
}

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "all/findTokensUser",
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($table);
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTable(item, $table);
                });
                $table.DataTable({
                    paging: false,
                    destroy: true,
                    scrollY: '50vh',
                    scrollCollapse: true
                });
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}