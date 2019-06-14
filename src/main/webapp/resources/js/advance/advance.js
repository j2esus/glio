var $txtQueryProjects,
    $dataTableProject,
    _indexProjectSelected = -1,
    _projectData = [],
    $divProjectChart,
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
    $divProjectChart = $('#divProjectChart');
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
    $divProjectChart.html('<canvas id="projectsChart" width="100%"></canvas>');

    var ctxPie = $("#projectsChart");
    var labels = [];
    var dataGraph = [];
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/findDataGraphProject",
        data: {idProject: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
            labels = [];
            dataGraph = [];
        },
        success: function (data) {
            var i = 0;
            var total = 0;
            for(i = 0; i<data.length; i++){
                var value = data[i];
                total += value.quantity;
            }
            for(i = 0; i<data.length; i++){
                var value = data[i];
                labels[i] = value.status;
                dataGraph[i] = (value.quantity / total) * 100;
            }
        }, complete: function () {
            _blockUI.unblock();
            new Chart(ctxPie, {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                            data: dataGraph,
                            backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745']
                        }]
                }
            });
            toBuildGraphAim(item.id);
        }
    });
}

function toBuildGraphAim(idProject) {
    $divAims = $('#divAims');
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/findDataGraphAim",
        data: {idProject: idProject},
        beforeSend: function (xhr) {
            _blockUI.block();
            $divAims.html('');
            _aimData = [];
        },
        success: function (data) {
            var i = 0;
            var html = '';
            for(i = 0;i<data.length;i++){
                html += writeAimGraphHtml(i, data[i]);
                _aimData.push(data[i]);
            }
            $divAims.html(html);
            //graficas
            
            for(i = 0;i<data.length;i++){
                var dataGraph = data[i].data;
                var labelsAim = [];
                var dataGraphAim = [];
                var total = 0;
                for(j = 0;j<dataGraph.length;j++){
                    var value = dataGraph[j];
                    total += value.quantity;
                }
                for(j = 0;j<dataGraph.length;j++){
                    var value = dataGraph[j];
                    labelsAim[j] = value.status;
                    dataGraphAim[j] = (value.quantity / total) * 100;
                }
                
                new Chart($('#'+data[i].aim.id), {
                    type: 'pie',
                    data: {
                        labels: labelsAim,
                        datasets: [{
                                data: dataGraphAim,
                                backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745']
                            }]
                    }
                });
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeAimGraphHtml(index, aimData){
    var html = '';
    var item = aimData.aim;
    if(index%3 == 0){
        html += '<div class="row">';
    }
    
    html += '<div class="col-lg-4">';
    
    html += '<div class="card">';
    html += '<div class="card-header">';
    html += '<div class="text-left" style="float:left">'+item.name +'</div><div class="text-right"><button type="button" class="btn btn-success fa fa-bars" onclick="showDetails('+index+')"></button></div>';
    html += '</div>';
    html += '<div class="card-body">';
    html += '<canvas id="'+item.id+'" width="100%"></canvas>'; 
    html += '</div>';
    html += '</div>';

    
    html += '</div>';
    
    if(index%3 == 2){
        html += '</div>';
        html += '<br/>';
    }
    
    return html;
}

function showDetails(index){
    var item = _aimData[index];
    $dataTableTask = $('#dataTableTask');
    $('#titleModalDetail').html(item.aim.name);
    $('#descriptionModal').html(item.aim.description);
    $('#initDateModal').html(_uiUtil.getFormattedDate(item.aim.initDate));
    $('#endDateModal').html(_uiUtil.getFormattedDate(item.aim.endDate));
    
    $.ajax({
        type: "POST",
        url: $.PATH + "advance/findTasks",
        data: {idAim: item.aim.id},
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
    var color = writeColor(item.priority);
    var fila = "";
    fila += "<tr>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td bgcolor='"+color+"'>" + item.priority + "</td>";
    fila += "<td>" + item.userOwner.username + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function writeColor(priority){
    var type = '';
    switch(priority){
        case 'ALTA':
            type = '#f2dede';
            break;
        case 'MEDIA':
            type = '#fcf8e3';
            break;
        case 'BAJA':
            type = '#d9edf7';
            break;
    }
    return type;
}