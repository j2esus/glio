var $initDate,
    $endDate,
    $divChart,
    $productivityForm,
    $idProject;

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
    var users = [];
    var data = [];
    $.ajax({
        type: "POST",
        url: $.PATH + "productivity/findDataGraphProductivity",
        data: {initDate: $initDate.val(), endDate: $endDate.val(), idProject: $idProject.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            users = [];
            data = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    users.push(item.username);
                    data.push(item.quantity);
                });
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
            writeGraph(users, data);
        }
    });
}

function writeGraph(users, data){
    $divChart.html('<canvas id="myBarChart" width="100" height="50"></canvas>');
    var ctxLine = $("#myBarChart");
    new Chart(ctxLine, {
        type: 'bar',
        data: {
            labels: users,
            datasets: [{
                    label: "Productividad",
                    backgroundColor: "rgba(2,117,216,1)",
                    borderColor: "rgba(2,117,216,1)",
                    data: data
                }]
        },
        options: {
            responsive: true,
            scales: {
                xAxes: [{
                        gridLines: {
                            display: false
                        }
                    }],
                yAxes: [{
                        gridLines: {
                            display: true
                        }
                    }]
            },
            legend: {
                display: false
            }
        }
    });
}

function initData(){
    $initDate.val(_uiUtil.today());
    $endDate.val(_uiUtil.today());
}