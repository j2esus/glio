var $loginForm,
    $txtUser,
    $txtPassword;

$(document).ready(function () {
    initComponents();
    initEvents();
});

function initComponents(){
    $loginForm = $('#loginForm');
    $txtUser = $('#txtUser');
    $txtPassword = $('#txtPassword');
}

function initEvents(){
    $loginForm.submit(onSubmitLoginForm);
}

function onSubmitLoginForm(e){
    if (!e.isDefaultPrevented()) {
        e.preventDefault();
        login($txtUser.val(), $txtPassword.val());
    }
}

function login(username, password){
    $.ajax({
        type: "POST",
        url: $.PATH+"login",
        async: false,
        data: "username=" + username+"&password="+password,
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success:function(response){
            if(response === "OK"){
                _jsUtil.redirect("all/dash");
            }else{
                _notify.show(response, 'danger');
            }
        },
        complete:function(){
            _blockUI.unblock();
        }
   });
}

function onClickBtnConfiguration(){
    _jsUtil.redirect("all/configuration");
}

