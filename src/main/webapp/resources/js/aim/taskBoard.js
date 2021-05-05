let $tasks, $aims, $projects, $finishedTasks;

let $projectFilter, $statusFilter, $nameFilter;
let $dataTable;

let $btnToStart, $btnToPause, $btnToFinish, $btnToCancel;

let $btnNew,
        $saveModalTask,
        $idProjectTask,
        $dataFormTask;
let _tasks = [], _indexSelected = -1, _task = null, _inProcessTasks = new Map();

$(document).ready(function () {
    initComponents();
    initEvents();
    loadSummaryOfTasks();
    findTasks();
});

function initComponents() {
    $tasks = $('#tasks');
    $aims = $('#aims');
    $projects = $('#projects');
    $finishedTasks = $('#finishedTasks');
    
    $projectFilter = $('#projectFilter');
    $statusFilter = $('#statusFilter');
    $aimFilter = $('#aimFilter');
    selectDefaultStatusFilter();
    
    $nameFilter = $('#nameFilter');
    
    $dataTable = $('#dataTable');

    $btnNew = $('#btnNew');
    $saveModalTask = $('#saveModalTask');
    $idProjectTask = $('#idProjectTask');
    $dataFormTask = $('#dataFormTask');
    
    $btnToStart = $('#btnToStart');
    $btnToPause = $('#btnToPause');
    $btnToFinish = $('#btnToFinish');
    $btnToCancel = $('#btnToCancel');
    hideTaskActionButtons();
}

function initEvents() {

    $projectFilter.change(function () {
        findActiveAims($projectFilter.val(), $aimFilter, '--Objetivo', '0');
        findTasks();
    });
    
    $aimFilter.change(function () {
        findTasks();
    });
    
    $statusFilter.change(function () {
        findTasks();
    });
    
    $nameFilter.keypress(function (e) {
        if (e.which == 13) {
            findTasks();
        }
    });
    
    $(".classPriority").change(function () {
        findTasks();
    });

    $btnNew.click(bntNewOnClick);
    $idProjectTask.change(idProjectTaskOnChange);
    $dataFormTask.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveTask();
        }
    });
    
    $dataTable.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelected = $(this).data('meta-row');
        showTaskDetails();
    });
    
    $btnToStart.click(function(){
        toStartTask();
    });
    
    $btnToPause.click(function(){
        toPause();
    });
    
    $btnToFinish.click(function(){
        toFinished();
    });
    
    $btnToCancel.click(function(){
        toCancel();
    });
}

function toStartTask() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toStartTask",
        data: {idTask: _task.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                refreshCurrentTaskData(_task, 'IN_PROCESS');
                _timersPerTasks.start(_task.id);
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toPause() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toPauseTask",
        data: {idTask: _task.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                refreshCurrentTaskData(_task, 'PAUSED');
                _timersPerTasks.pause(_task.id);
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toFinished() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toFinishTask",
        data: {idTask: _task.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                refreshCurrentTaskData(_task, 'FINISHED');
                _timersPerTasks.stop(_task.id);
                countActiveTasksByUserOwner();
                countFinishedByUserOwner();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toCancel() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toCancelTask",
        data: {idTask: _task.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                refreshCurrentTaskData(_task, 'PAUSED');
                countActiveTasksByUserOwner();
                countFinishedByUserOwner();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function bntNewOnClick() {
    $('#idNewTask').val(0);
    findActiveAims($idProjectTask.val(), $('#idAimTask'), '--Seleccione', '');
    $saveModalTask.modal();
}

function idProjectTaskOnChange() {
    findActiveAims($idProjectTask.val(), $('#idAimTask'), '--Seleccione', '');
}

function saveTask() {
    let id = $('#idNewTask').val();
    let name = $('#nameTask').val();
    let description = $('#descriptionTask').val();
    let priority = $('#priorityTask').val();
    let estimated = $('#estimatedTask').val();
    let idAim = $('#idAimTask').val();

    $.ajax({
        type: "POST",
        url: $.PATH + "task/saveTask",
        data: {id: id, name: name, description: description, priority: priority,
            estimated: estimated, username: _uiUtil.getUsername(), idAim: idAim},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (task) {
            _notify.show("Tarea guardado con éxito", 'success');
            $saveModalTask.modal('hide');
            addRowToTable(task, $dataTable);
            countActiveTasksByUserOwner();
            countFinishedByUserOwner();
        }, complete: function () {
            _blockUI.unblock();
            _uiUtil.cleanControls($saveModalTask);
        }
    });
}

function loadSummaryOfTasks(){
    countActiveTasksByUserOwner();
    countActiveAimsByUserOwner();
    countActiveProjectsByUserOwner();
    countFinishedByUserOwner();
}

function countActiveTasksByUserOwner(){
    $.ajax({
        type: "POST",
        url: $.PATH + "task/countActiveTasksByUserOwner",
        beforeSend: function (xhr) {
            _blockUI.block();
            $tasks.html('0');
        },
        success: function (response) {
            $tasks.html(response);
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function countFinishedByUserOwner() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/countFinishedByUserOwner",
        beforeSend: function (xhr) {
            _blockUI.block();
            $finishedTasks.html('0');
        },
        success: function (response) {
            $finishedTasks.html(response);
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function countActiveAimsByUserOwner() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/countActiveAimsByUserOwner",
        beforeSend: function (xhr) {
            _blockUI.block();
            $aims.html('0');
        },
        success: function (response) {
            $aims.html(response);
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function countActiveProjectsByUserOwner() {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/countActiveProjectsByUserOwner",
        beforeSend: function (xhr) {
            _blockUI.block();
            $projects.html('0');
        },
        success: function (response) {
            $projects.html(response);
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function findTasks() {
    var priorities = [];
    $(".classPriority:checked").each(function () {
        priorities.push($(this).val());
    });

    $.ajax({
        type: "POST",
        url: $.PATH + "task/findTasks",
        data: {
            query: $nameFilter.val(),
            priorities: priorities, 
            idProject: $projectFilter.val(),
            idAim: $aimFilter.val(),
            status: $statusFilter.selectpicker('val')
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTable);
            _tasks = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    addRowToTable(item, $dataTable);
                });
                $dataTable.tablePagination(_uiUtil.getOptionsPaginator(10));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTable(item, table) {
    _tasks.push(item);
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);
    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td id='tableStatusIdTask"+item.id+"'><div class='badge badge-success'> " + item.status + "</div></td>";
    fila += "<td>"+_uiUtil.getPriorityClass(item.priority)+"</td>";
    fila += "</tr>";
    table.append(fila);
}

function showTaskDetails() {
    _task = _tasks[_indexSelected];
    $('#divName').html(_task.name);
    $('#divStatus').html('<span class="badge badge-success">'+_task.status+"<span>");
    $('#divDescription').html(_task.description);
    $('#divPriority').html(_uiUtil.getPriorityClass(_task.priority));
    $('#divEstimated').html(_uiUtil.secondsToHHmmss(_task.estimatedTime));
    $('#divCreatedBy').html(_task.userRequester.username);
    $('#divAssignedTo').html(_task.userOwner.username);
    
    configButtonsByStatus(_task.status);
    setSummaryTimePerTask();
}

function configButtonsByStatus(status){
    switch(status){
        case 'PENDING':
        case 'PAUSED':
            $btnToStart.show();
            $btnToPause.hide();
            $btnToFinish.hide();
            $btnToCancel.hide();
        break;
        case 'IN_PROCESS':
                $btnToStart.hide();
                $btnToPause.show();
                $btnToFinish.show();
                $btnToCancel.hide();
        break;
        case 'FINISHED':
            $btnToStart.hide();
            $btnToPause.hide();
            $btnToFinish.hide();
            $btnToCancel.show();
            break;
    default:
        $btnToStart.hide();
        $btnToPause.hide();
        $btnToFinish.hide();
        $btnToCancel.hide();
    }
}

function hideTaskActionButtons(){
    $btnToStart.hide();
    $btnToPause.hide();
    $btnToFinish.hide();
    $btnToCancel.hide();
}

function refreshCurrentTaskData(task, status){
    task.status = status;
    configButtonsByStatus(task.status);
    $('#divStatus').html('<span class="badge badge-success">' + task.status + "<span>");
    $('#tableStatusIdTask'+task.id).html('<span class="badge badge-success">' + task.status + "<span>");
}

function setSummaryTimePerTask() {
    forceToPauseBecauseTaskWithoutTimer();
    $.ajax({
        type: "POST",
        url: $.PATH + "task/findSummaryTime",
        data: {
            idTask: _task.id
        },
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (taskDTO) {
            let currentTimePerTask = _jsUtil.round(taskDTO.estimatedTime - taskDTO.realTime);
            let chart = createOrUpdateChart(calculatePercent(taskDTO.estimatedTime, taskDTO.realTime));
            if(currentTimePerTask > 0)
                createTimerPerTask(taskDTO.idTask, currentTimePerTask, chart, taskDTO.estimatedTime, taskDTO.realTime);
            setTimerLabel(currentTimePerTask <= 0 ? "AGOTADO" : _uiUtil.secondsToHHmmss(currentTimePerTask));
        }, complete: function () {
            _blockUI.unblock();
            forceToStartBecauseTaskWithoutTimer();
        }
    });
}

function forceToPauseBecauseTaskWithoutTimer(){
    let timer = _timersPerTasks.get(_task.id);
    if (timer == undefined && _task.status == 'IN_PROCESS') {
        _inProcessTasks.set(_task.id, _task);
        toPause();
    }
}

function forceToStartBecauseTaskWithoutTimer() {
    let currentTask = _inProcessTasks.get(_task.id);
    if (currentTask != undefined) {
        toStartTask();
        _inProcessTasks.delete(_task.id);
    }
}

function createOrUpdateChart(value){
    return c3.generate({
        bindto: '#chart',
        data: {
            type: 'gauge',
            columns: [
                ['Tiempo consumido', value]
            ]
        },
        color: {
            pattern: ['#60B044', '#F6C600', '#F97600', '#FF0000'],
            threshold: {
                values: [70, 80, 90, 100]
            }
        }
    });
}

function calculatePercent(estimated, real) {
    let percent = _jsUtil.round((real / estimated) * 100);
    return percent;
}

function setTimerLabel(value){
    $('#divCurrentTimePerTask').html(value);
}

function createTimerPerTask(idTask, currentTimePerTask, chart, estimatedTime, realTime){
    _timersPerTasks.removeAllEventListeners();
    let timer = _timersPerTasks.get(idTask);
    if(timer == undefined){
        _timersPerTasks.set(idTask, createNewTimer(currentTimePerTask, chart, estimatedTime, realTime));
    }else{
        if (timer.timer.isRunning()) {
            timer.timer.addEventListener('secondsUpdated', timer.listener);
        } else {
            _timersPerTasks.remove(idTask);
            _timersPerTasks.set(idTask, createNewTimer(currentTimePerTask, chart, estimatedTime, realTime));
        }
    }
}

function createNewTimer(currentTimePerTask, chart, estimatedTime, realTime){
    let easyTimer = new easytimer.Timer({countdown: true, startValues: {seconds: currentTimePerTask}});
    let timer = {
        timer: easyTimer,
        listener: function (e) {
            chart.load({
                columns: [['Tiempo consumido',  calculatePercent(estimatedTime, realTime++)]]
            });
            setTimerLabel(easyTimer.getTimeValues().toString());
        }
    };
    return timer;
}

function selectDefaultStatusFilter(){
    $statusFilter.selectpicker('selectAll');
    let statusValues = $statusFilter.selectpicker('val');
    statusValues.pop();
    $statusFilter.selectpicker('val', statusValues);
}