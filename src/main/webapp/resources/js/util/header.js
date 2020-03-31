var $sideNavToggler,
    $btnConfiguration,
    $btnLogout,
    $btnDash;

$(document).ready(function () {
    initComponentsHeader();
    initEventsHeader();
});

function initComponentsHeader(){
    $sideNavToggler = $('#sidenavToggler');
    $btnConfiguration = $('#btnConfiguration');
    $btnLogout = $('#btnLogout');
    $btnDash = $('#btnDash');
}

function initEventsHeader(){
    $sideNavToggler.click(onClickSideNavToggler);
    $btnConfiguration.click(onClickBtnConfiguration);
    $btnLogout.click(btnLogoutOnClick);
    $btnDash.click(btnDashOnClick);
}

function onClickSideNavToggler(e){
    e.preventDefault();
    $("body").toggleClass("sidenav-toggled");
    $(".navbar-sidenav .nav-link-collapse").addClass("collapsed");
    $(".navbar-sidenav .sidenav-second-level, .navbar-sidenav .sidenav-third-level").removeClass("show");
}

function onClickBtnConfiguration(){
    _jsUtil.redirect("all/configuration");
}

function btnLogoutOnClick(){
    countInProcess();
}

function btnDashOnClick(){
    _jsUtil.redirect("all/dash");
}

function logout(){
    $.ajax({
        type: "POST",
        url: $.PATH+"logout",
        beforeSend: function (xhr) {
            //_blockUI.block();
        },
        success:function(response){
            //_blockUI.unblock();
            if(response === "OK"){
                _jsUtil.redirect("");
            }else{
                _notify.show(response, 'danger');
            }
        }
   });
}

function countInProcess(){
    $.ajax({
        type: "POST",
        url: $.PATH+"all/countInProcess",
        beforeSend: function (xhr) {
            //_blockUI.block();
        },
        success:function(response){
            //_blockUI.unblock();
            if(response === 0){
                logout();
            }else{
                _notify.show('No puedes cerrar sesión, tienes una tarea pendiente por pausar o finalizar.', 'danger');
            }
        }
   });
}

