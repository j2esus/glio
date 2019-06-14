var $idProject;

var $idDivActives,
        $txtQueryActives;

var $idDivInProcess,
        $txtQueryInProcess;

var $idDivFinished,
        $txtQueryFinished;

var $btnNew,
        $saveModalTask,
        $idProjectTask,
        $dataFormTask;

$(document).ready(function () {
    initComponents();
    initEvents();
    findTasksPending();
    findTasksInProcess();
    findTasksFinished();
});

function initComponents() {
    $idProject = $('#idProject');

    $idDivActives = $('#idDivActives');
    $txtQueryActives = $('#txtQueryActives');

    $idDivInProcess = $('#idDivInProcess');
    $txtQueryInProcess = $('#txtQueryInProcess');

    $idDivFinished = $('#idDivFinished');
    $txtQueryFinished = $('#txtQueryFinished');

    $btnNew = $('#btnNew');
    $saveModalTask = $('#saveModalTask');
    $idProjectTask = $('#idProjectTask');
    $dataFormTask = $('#dataFormTask');
}

function initEvents() {
    //actives
    $txtQueryActives.keypress(function (e) {
        if (e.which == 13) {
            findTasksPending();
        }
    });

    $(".classActives").change(function () {
        findTasksPending();
    });

    //in process
    $txtQueryInProcess.keypress(function (e) {
        if (e.which == 13) {
            findTasksInProcess();
        }
    });

    $(".classInProcess").change(function () {
        findTasksInProcess();
    });

    //finished
    $txtQueryFinished.keypress(function (e) {
        if (e.which == 13) {
            findTasksFinished();
        }
    });

    $(".classFinished").change(function () {
        findTasksFinished();
    });

    $idProject.change(function () {
        findTasksPending();
        findTasksInProcess();
        findTasksFinished();
    });

    //new
    $btnNew.click(bntNewOnClick);
    $idProjectTask.change(idProjectTaskOnChange);
    $dataFormTask.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveTask();
        }
    });
}

function findTasksPending() {
    var priorities = [];
    $(".classActives:checked").each(function () {
        priorities.push($(this).val());
    });

    $.ajax({
        type: "POST",
        url: $.PATH + "task/findTasksPending",
        data: {query: $txtQueryActives.val(),
            priorities: priorities, idProject: $idProject.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            $idDivActives.html('');
        },
        success: function (response) {
            $('#headActives').html(response.count);
            if (response.actives.length > 0) {
                var html = '';
                $.each(response.actives, function (i, item) {
                    html += writeCodeActives(item);
                });
                $idDivActives.html(html);
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeColor(priority) {
    var type = 'header';
    switch (priority) {
        case 'ALTA':
            type = 'danger';
            break;
        case 'MEDIA':
            type = 'warning';
            break;
        case 'BAJA':
            type = 'info';
            break;
    }
    return type;
}

//actives
function writeCodeActives(item) {
    var type = writeColor(item.priority);

    var code = '<div class="card">' +
            '<div class="card-' + type + '"><button class="btn btn-link" data-toggle="collapse" data-target="#'+item.id+'">+</button>' + item.name + '<span class="badge badge-success" style="float:right;padding:0.75em 0.4em">' + item.userOwner.username + '</span>';

    if (item.userOwner.username === _uiUtil.getUsername()) {
        code += '<button type="button" class="btn btn-info fa fa-play" style="float: right;" onclick="toInProcess(' + item.id + ')"></button>';
    }
    code += '</div><div class="collapse" id="'+item.id+'"><div class="card-body">' + item.description + '</div></div>';
    code += '</div>';
    return code;
}

//in process
function findTasksInProcess() {
    var priorities = [];
    $(".classInProcess:checked").each(function () {
        priorities.push($(this).val());
    });

    $.ajax({
        type: "POST",
        url: $.PATH + "task/findTasksInProcess",
        data: {query: $txtQueryInProcess.val(),
            priorities: priorities, idProject: $idProject.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            $idDivInProcess.html('');
        },
        success: function (response) {
            $('#headInProcess').html(response.count);
            if (response.inProcess.length > 0) {
                var html = '';
                $.each(response.inProcess, function (i, item) {
                    html += writeCodeInProcess(item);
                });
                $idDivInProcess.html(html);
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeCodeInProcess(item) {
    var type = writeColor(item.priority);

    var code = '<div class="card">' +
            '<div class="card-' + type + '"><button class="btn btn-link" data-toggle="collapse" data-target="#'+item.id+'">+</button>' + item.name;

    code += '<span class="badge badge-success" style="float:right;padding:0.75em 0.4em">' + item.userOwner.username + '</span>';

    if (item.status === 'IN_PROCESS' && item.userOwner.username === _uiUtil.getUsername()) {
        code += '<button type="button" class="btn btn-primary fa fa-check" style="float: right;" onclick="toFinished(' + item.id + ')"></button>';
        code += '<button type="button" class="btn btn-info fa fa-pause" style="float: right;" onclick="toPause(' + item.id + ')"></button>';
        code += '<img src="' + $.PATH + 'images/loading.gif" style="float: right;padding-right:3px;" width="33px" height="30px"></img>';

    }

    if (item.status === 'PAUSED' && item.userOwner.username === _uiUtil.getUsername()) {
        code += '<button type="button" class="btn btn-info fa fa-play" style="float: right" onclick="toRestart(' + item.id + ')"></button>';
    }

    code += '</div>' +
            '<div class="collapse" id="'+item.id+'"><div class="card-body">' + item.description + '</div></div>' +
            '</div>';
    return code;
}

//finished
function findTasksFinished() {
    var priorities = [];
    $(".classFinished:checked").each(function () {
        priorities.push($(this).val());
    });

    $.ajax({
        type: "POST",
        url: $.PATH + "task/findTasksFinished",
        data: {query: $txtQueryFinished.val(),
            priorities: priorities, idProject: $idProject.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            $idDivFinished.html('');
        },
        success: function (response) {
            $('#headFinished').html(response.count);
            if (response.finished.length > 0) {
                var html = '';
                $.each(response.finished, function (i, item) {
                    html += writeCodeFinished(item);
                });
                $idDivFinished.html(html);
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeCodeFinished(item) {
    var type = writeColor(item.priority);

    var code = '<div class="card">' +
            '<div class="card-' + type + '"><button class="btn btn-link" data-toggle="collapse" data-target="#'+item.id+'">+</button>' + item.name + '<span class="badge badge-success" style="float:right;padding:0.75em 0.4em">' + item.userOwner.username + '</span>';

    if (item.userOwner.username === _uiUtil.getUsername() || item.userRequester.username === _uiUtil.getUsername()) {
        code += '<button type="button" class="btn btn-danger fa fa-times" style="float: right;" onclick="toCancel(' + item.id + ')"></button>';
    }
    if (item.userRequester.username === _uiUtil.getUsername()) {
        code += '<button type="button" class="btn btn-info fa fa-check" style="float: right;" onclick="toAccepted(' + item.id + ')"></button>';
    }


    code += '</div><div class="collapse" id="'+item.id+'"><div class="card-body">' + item.description + '</div></div>' +
            '</div>';
    return code;
}

//actions

function toInProcess(id) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toStartTask",
        data: {idTask: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                findTasksPending();
                findTasksInProcess();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toPause(id) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toPauseTask",
        data: {idTask: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                findTasksInProcess();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toRestart(id) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toRestarTask",
        data: {idTask: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                findTasksInProcess();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function toFinished(id) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toFinishTask",
        data: {idTask: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                findTasksInProcess();
                findTasksFinished();
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

function toCancel(id) {
    $.ajax({
        type: "POST",
        url: $.PATH + "task/toCancelTask",
        data: {idTask: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                findTasksInProcess();
                findTasksFinished();
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
    var id = $('#idNewTask').val();
    var name = $('#nameTask').val();
    var description = $('#descriptionTask').val();
    var priority = $('#priorityTask').val();
    var estimated = $('#estimatedTask').val();
    var idAim = $('#idAimTask').val();

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
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            _uiUtil.cleanControls($saveModalTask);
            findTasksPending();
        }
    });
}