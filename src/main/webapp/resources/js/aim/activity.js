var $initDate,
    $endDate,
    $activityForm,
    $divData,
    $idProject;

$(document).ready(function () {
    initComponents();
    initEvents();
    initData();
});

function initComponents() {
    $initDate = $('#initDate');
    $endDate = $('#endDate');
    $activityForm = $('#activityForm');
    $divData = $('#divData');
    $idProject = $('#idProject');
}

function initEvents() {
    $initDate.focusout(onFocusOutInit);
    $endDate.focusout(onFocusOutEnd);
    
    $activityForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            findData();
        }
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

function findData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "activity/findActivityData",
        data: {initDate: $initDate.val(), endDate: $endDate.val(), idProject: $idProject.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (items) {
            if (items.length > 0) {
                var html = '';
                $.each(items, function (i, item) {
                    console.log(item);
                    html += writeUserData(item);
                });
                $divData.html(html);
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeUserData(item){
    var user = item.user;
    var tasks = item.tasks;
    var node = '<div class="card">';
    node += '<div class="card-header">';
    node += '<i class="fa fa-user-circle"></i>  <span class="badge badge-primary">'+user.username+'</span></div>';
    node += '<div class="card-body">';
    node += '<div class="row">';
    
    //user data
    node += '<div class="col-3">';
    node += '<p>Nombre: <b>'+user.name+'</b></p>';
    node += '<p>Email: <b>'+user.email+'</b></p>';
    node += '<p>Tipo usuario: <b>'+user.userType.name+'</b></p>';
    node += '</div>';
    
    //tasks data
    node += '<div class="col-9">';
    node += '<div class="table-responsive"><table class="table table-bordered" width="100%" cellspacing="0">';
    
    node += '<thead><tr>';
    node += '<th>Tarea</th>';
    node += '<th>Prioridad</th>';
    node += '<th>Estimado</th>';
    node += '<th>Consumido</th>';
    node += '</tr></thead>';
    
    node += '<tbody>';
    var estimated = 0;
    var real = 0;
    if(tasks.length <= 0){
        node += '<tr>';
        node += '<td colspan = "4"><span class="badge badge-danger">Sin actividad </span></td>';
        node += '<tr>';
    }else{
        for(var i = 0; i< tasks.length; i++){
            var item = tasks[i];
            var color = _uiUtil.writePriorityColorInt(item.priority);
            node += '<tr>';
            node += '<td>'+item.name+'</td>';
            node += '<td bgcolor="'+color+'">'+_uiUtil.getStringPriority(item.priority)+'</td>';
            node += '<td>'+item.estimatedTime+'</td>';
            node += '<td>'+item.realTime+'</td>';
            node += '<tr>';
            estimated+=item.estimatedTime;
            real += item.realTime;
        }
    }
    node += '</tbody>';
            
    node += '</table></div>';
    //resumen
    node += '<p><span class="badge badge-success">Estimado:</span> <b>'+estimated +'</b> <span class="badge badge-success">Real:</span> <b>'+Math.round(real * 100) / 100+ '</b> </p>';
    node += '</div>';
    
    node += '</div>';
    node += '</div></div><br/>';
    return node;
}

function initData(){
    $initDate.val(_uiUtil.today());
    $endDate.val(_uiUtil.today());
}