var $txtQueryProjects,
        $dataTableProject,
        _indexProjectSelected = -1,
        _projectData = [],
        _aimData = [];

var $divAims;

var $detailsModal;


$(document).ready(function () {
    initComponents();
    initEvents();
    findProjectData();
});

function initComponents() {
    $txtQueryProjects = $('#txtQueryProjects');
    $dataTableProject = $('#dataTableProject');
    $divAims = $('#divAims');
    $detailsModal = $('#detailsModal');
}

function initEvents() {
    $txtQueryProjects.keypress(function (e) {
        if (e.which == 13) {
            findProjectData();
        }
    });

    $dataTableProject.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexProjectSelected = $(this).data('meta-row');
        toBuildGraphProject();
    });
}

function findProjectData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/findProjects",
        data: {query: $txtQueryProjects.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableProject);
            _indexProjectSelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                _projectData = [];
                $.each(items, function (i, item) {
                    addRowToTable(item, $dataTableProject);
                    _projectData.push(item);
                });
                $dataTableProject.tablePagination(_uiUtil.getOptionsPaginator(5));
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
    fila += "<td>" + item.description + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDate(item.initDate) + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDate(item.endDate) + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function toBuildGraphProject() {
    var item = _projectData[_indexProjectSelected];
    let data = [];
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/countTasksGroupedByStatus_project",
        data: {idProject: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
            labels = [];
            dataGraph = [];
        },
        success: function (response) {
            $.each(response, function (key, value) {
                data.push([key, value]);
            });
        }, complete: function () {
            _blockUI.unblock();
            c3.generate({
                bindto: '#divProjectChart',
                data: {
                    columns: data,
                    type: 'pie'
                }
            });
            toBuildDivForAims(item.id);
        }
    });
}

function toBuildDivForAims(idProject) {
    $divAims = $('#divAims');
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/findAims",
        data: {idProject: idProject},
        beforeSend: function (xhr) {
            _blockUI.block();
            $divAims.html('');
            _aimData = [];
        },
        success: function (data) {
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += writeAimGraphHtml(i, data[i]);
                _aimData.push(data[i]);
            }
            $divAims.html(html);
            $.each(_aimData, function (i, item) {
                toBuildAimChart(item.id);
            });
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toBuildAimChart(idAim) {
    let data = [];
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/countTasksGroupedByStatus_aim",
        data: {
            idAim: idAim
        },
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            $.each(response, function (key, value) {
                data.push([key, value]);
            });
            c3.generate({
                bindto: '#aim' + idAim,
                data: {
                    columns: data,
                    type: 'pie'
                }
            });
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeAimGraphHtml(index, item) {
    var html = '';
    if (index % 3 == 0) {
        html += '<div class="row">';
    }

    html += '<div class="col-lg-4">';

    html += '<div class="card">';
    html += '<div class="card-header">';
    html += '<div class="text-left" style="float:left">' + item.name + '</div><div class="text-right"><button type="button" class="btn btn-success fa fa-bars" onclick="showDetails(' + index + ')"></button></div>';
    html += '</div>';
    html += '<div class="card-body">';
    html += '<div id="aim' + item.id + '" width="100%"></div>';
    html += '</div>';
    html += '</div>';


    html += '</div>';

    if (index % 3 == 2) {
        html += '</div>';
        html += '<br/>';
    }

    return html;
}

function showDetails(index) {
    var item = _aimData[index];
    $dataTableTask = $('#dataTableTask');
    $('#titleModalDetail').html(item.name);
    $('#descriptionModal').html(item.description);
    $('#initDateModal').html(_uiUtil.getFormattedDate(item.initDate));
    $('#endDateModal').html(_uiUtil.getFormattedDate(item.endDate));

    $.ajax({
        type: "POST",
        url: $.PATH + "advance/findTasks",
        data: {idAim: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableTask);
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTableTask(item, $dataTableTask);
                });
                $dataTableTask.tablePagination(_uiUtil.getOptionsPaginator(5));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
    $detailsModal.modal();
}


function addRowToTableTask(item, table) {
    var fila = "";
    fila += "<tr>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + _uiUtil.getPriorityClass(item.priority) + "</td>";
    fila += "<td>" + item.userOwner.username + "</td>";
    fila += "</tr>";
    table.append(fila);
}