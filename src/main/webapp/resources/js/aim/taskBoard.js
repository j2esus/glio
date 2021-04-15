let $tasks, $aims, $projects;

let $projectFilter, $statusFilter, $nameFilter;
let $dataTable;

let $btnToStart, $btnToPause, $btnToFinish, $btnToCancel;

let $btnNew,
        $saveModalTask,
        $idProjectTask,
        $dataFormTask;
let _tasks = [], _indexSelected = -1, _task = null;

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
    
    $projectFilter = $('#projectFilter');
    $statusFilter = $('#statusFilter');
    $statusFilter.selectpicker('selectAll');
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
                let timerData = _timersPerTasks.get(_task.id);
                timerData.timer.start();
                timerData.timer.addEventListener('secondsUpdated', timerData.listener);
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
                let timerData = _timersPerTasks.get(_task.id);
                timerData.timer.pause();
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
                let timerData = _timersPerTasks.get(_task.id);
                timerData.timer.stop();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toAccepted(id) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toAcceptedTask",
        data: {idTask: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                findTasksFinished();
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
    findAims($idProjectTask.val());
    $saveModalTask.modal();
}

function findAims(idProject) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/findAim",
        data: {idProject: idProject},
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            $('#idAimTask').empty();
        },
        success: function (response) {
            if (response.length > 0) {
                $.each(response, function (i, item) {
                    $('#idAimTask').append("<option value=" + item.id + ">" + item.name + "</option>");
                });
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function idProjectTaskOnChange() {
    findAims($idProjectTask.val());
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
        success: function (response) {
            if (response === "OK") {
                _notify.show("Tarea guardado con éxito", 'success');
                $saveModalTask.modal('hide');
                findTasks();
            } else {
                _notify.show(response, 'danger');
            }
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
                    _tasks.push(item);
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
    setSummaryTimePerTask(_task.id);
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

function setSummaryTimePerTask(idTask) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/findSummaryTime",
        data: {
            idTask: idTask
        },
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (taskDTO) {
            let currentTimePerTask = _jsUtil.round(taskDTO.estimatedTime - taskDTO.realTime);
            checkIfTimerExists(taskDTO.idTask, currentTimePerTask);
            setTimerLabel(_uiUtil.secondsToHHmmss(currentTimePerTask));
            createOrUpdateChart(calculatePercent(taskDTO.estimatedTime, taskDTO.realTime));
            
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function createOrUpdateChart(value){
    var chart = c3.generate({
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

function checkIfTimerExists(idTask, currentTimePerTask){
    removeAllEventListeners();
    let timer = _timersPerTasks.get(idTask);
    if(timer == undefined){
        _timersPerTasks.set(idTask, createNewTimer(currentTimePerTask));
    }else{
        if (timer.timer.isRunning()) {
            timer.timer.addEventListener('secondsUpdated', timer.listener);
        } else {
            _timersPerTasks.remove(idTask);
            _timersPerTasks.set(idTask, createNewTimer(currentTimePerTask));
        }
    }
}

function createNewTimer(currentTimePerTask){
    let easyTimer = new easytimer.Timer({countdown: true, startValues: {seconds: currentTimePerTask}});
    let timer = {
        timer: easyTimer,
        listener: function (e) {
            setTimerLabel(easyTimer.getTimeValues().toString());
        }
    };
    return timer;
}

function removeAllEventListeners(){
    for (let timerItem of _timersPerTasks.values()) {
        timerItem.timer.removeEventListener('secondsUpdated', timerItem.listener);
    }
}
