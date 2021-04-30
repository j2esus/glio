var $initDate,
    $endDate,
    $divChart,
    $productivityForm,
    $idProject,
    $idAim;

$(document).ready(function () {
    initComponents();
    initEvents();
    initData();
});

function initComponents() {
    $initDate = $('#initDate');
    $endDate = $('#endDate');
    $divChart = $('#divChart');
    $productivityForm = $('#productivityForm');
    $idProject = $('#idProject');
    $idAim = $('#idAim');
}

function initEvents() {
    $initDate.focusout(onFocusOutInit);
    $endDate.focusout(onFocusOutEnd);
    
    $productivityForm.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            buildChart();
        }
    });
    
    $idProject.change(function () {
        findAims($idProject.val(), $idAim, '--Todos');
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

function buildChart() {
    let productivityData = [];
    $.ajax({
        type: "POST",
        url: $.PATH + "productivity/findDataGraphProductivity",
        data: {initDate: $initDate.val(), endDate: $endDate.val(), idProject: $idProject.val(), idAim: $idAim.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    productivityData.push([item.username, item.quantity]);
                });
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            console.log(productivityData);
            _blockUI.unblock();
            createBarChart(productivityData);
        }
    });
}

function createBarChart(productivityData){
    c3.generate({
        bindto: '#divChart',
        data: {
            columns: productivityData,
            type: 'bar'
        },
        bar: {
            ratio: 0.2
        },
        size: {
            height: 400
        }
    });
}

function initData(){
    $initDate.val(_uiUtil.today());
    $endDate.val(_uiUtil.today());
}