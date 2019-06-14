var $registerForm,
    $txtCompany,
    $txtUser,
    $txtEmail,
    $txtPassword,
    $txtConfirmPassword;
    

$(document).ready(function () {
    initComponents();
    initEvents();
});

function initComponents(){
    $registerForm = $('#registerForm');
    $txtCompany = $('#txtCompany');
    $txtUser = $('#txtUser');
    $txtEmail = $('#txtEmail');
    $txtPassword = $('#txtPassword');
    $txtConfirmPassword = $('#txtConfirmPassword');
}

function initEvents(){
    
    $registerForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            register();
        }
    });
}

function register(){
    $.ajax({
        type: "POST",
        url: $.PATH+"toRegister",
        data: "companyName=" + $txtCompany.val()+"&username="+$txtUser.val()+"&email="+$txtEmail.val()+"&password="+$txtPassword.val(),
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success:function(response){
            if(response === "OK"){
                //_notify.show("Registro realizado con éxito", 'success');
                //_uiUtil.cleanControls($registerForm);
                _jsUtil.redirect("");
            }else{
                _notify.show(response, 'danger');
            }
        },complete:function(){
            _blockUI.unblock();
        }
   });
}


