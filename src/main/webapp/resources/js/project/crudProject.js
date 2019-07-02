var $divProjects,
     $divTasks;

var _indexProjectSelected = -1,
    _aimData = [],
    _projectData = [],
    _indexAimSelected = -1,
    _taskData = [],
    _indexTaskSelected = -1;

//projects
var $btnNewProject,
    $btnRefreshProject,
    $btnConfirmDeleteProject,
    $btnDeleteProject,
    $btnEditProject,
    $initDate,
    $endDate;

var $txtFilterName,
    $txtFilterStatus,
    $txtFilterDescription;

var $saveModalProject,
    $dataFormProject;

var $tableProject;

//aim
var $btnRefreshAim,
    $btnNewAim,
    $btnConfirmDeleteAim,
    $btnDeleteAim,
    $btnEditAim,
    $saveModalAim,
    $dataFormAim,
    $tableAim,
    $initDateAim,
    $endDateAim;

//tasks
var $btnRefreshTask,
    $btnNewTask,
    $btnConfirmDeleteTask,
    $btnDeleteTask,
    $btnEditTask,
    $saveModalTask,
    $dataFormTask,
    $tableTask;

$(document).ready(function () {
    initComponents();
    initPanels();
    initEvents();
    findProjectData();
});

function initComponents() {
    $divProjects = $('#divProjects');
    $btnNewProject = $('#btnNewProject');
    $btnRefreshProject = $('#btnRefreshProject');
    $btnConfirmDeleteProject = $('#btnConfirmDeleteProject');
    $btnDeleteProject = $('#btnDeleteProject');
    $btnEditProject = $('#btnEditProject');
    $initDate = $('#initDate');
    $endDate = $('#endDate');

    $tableProject = $('#dataTableProject');

    $txtFilterName = $('#txtFilterName');
    $txtFilterStatus = $('#txtFilterStatus');
    $txtFilterDescription = $('#txtFilterDescription');

    $saveModalProject = $('#saveModalProject');
    $dataFormProject = $('#dataFormProject');

    $divTasks = $('#divTasks');

    //aims
    $btnRefreshAim = $('#btnRefreshAim');
    $btnNewAim = $('#btnNewAim');
    $btnConfirmDeleteAim = $('#btnConfirmDeleteAim');
    $btnDeleteAim = $('#btnDeleteAim');
    $btnEditAim = $('#btnEditAim');
    $saveModalAim = $('#saveModalAim');
    $dataFormAim = $('#dataFormAim');
    $tableAim = $('#dataTableAim');
    $initDateAim = $('#initDateAim');
    $endDateAim = $('#endDateAim');

    //tasks
    $btnRefreshTask = $('#btnRefreshTask');
    $btnNewTask = $('#btnNewTask');
    $btnConfirmDeleteTask = $('#btnConfirmDeleteTask');
    $btnDeleteTask = $('#btnDeleteTask');
    $btnEditTask = $('#btnEditTask');
    $saveModalTask = $('#saveModalTask');
    $dataFormTask = $('#dataFormTask');
    $tableTask = $('#dataTableTask');
}

function initEvents() {
    //projects
    $btnNewProject.click(onClickNewProject);
    $btnRefreshProject.click(onClickRefreshProject);
    $btnConfirmDeleteProject.click(onClickConfirmDeleteProject);
    $btnDeleteProject.click(onClickDeleteProject);
    $btnEditProject.click(onClickEditProject);

    $dataFormProject.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveProject();
        }
    });

    $initDate.focusout(onFocusOutInitProject);
    $endDate.focusout(onFocusOutEndProject);

    $tableProject.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexProjectSelected = $(this).data('meta-row');
    });
    
    $tableProject.on('dblclick', 'tbody tr', function (event) {
        showDivTasks(_indexProjectSelected);
    });

    //aims
    $btnRefreshAim.click(onClickRefreshAim);
    $btnNewAim.click(onClickNewAim);
    $btnConfirmDeleteAim.click(onClickConfirmDeleteAim);
    $btnDeleteAim.click(onClickDeleteAim);
    $btnEditAim.click(onClickEditAim);
    $dataFormAim.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveAim();
        }
    });

    $initDateAim.focusout(onFocusOutInitAim);
    $endDateAim.focusout(onFocusOutEndAim);

    $tableAim.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexAimSelected = $(this).data('meta-row');
        findTaskData();
    });

    //tasks
    $btnNewTask.click(onClickBtnNewTask);
    $btnConfirmDeleteTask.click(onClickBtnCofirmDeleteTask);
    $btnDeleteTask.click(onClickBtnDeleteTask);
    $btnEditTask.click(onClickBtnEditTask);
    $btnRefreshTask.click(onClickBtnRefreshTask);
    $dataFormTask.validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();
            saveTask();
        }
    });
    
    $tableTask.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexTaskSelected = $(this).data('meta-row');
    });
}

function initPanels() {
    $divProjects.css("display", "block");
    $divTasks.css("display", "none");
}

function onClickNewProject() {
    _uiUtil.cleanControls($saveModalProject);
    $('#titleModalNewProject').html("Agregar proyecto");
    $('#idNewProject').val(0);
    $saveModalProject.modal();
}

function onClickRefreshProject() {
    findProjectData();
}

function onClickConfirmDeleteProject() {
    if (_indexProjectSelected === -1) {
        _notify.show('Debes seleccionar un proyecto', 'warning');
        return;
    }
    var item = _projectData[_indexProjectSelected];
    $('#idDeleteProject').val(item.id);
    $('#deleteLabelProject').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModalProject').modal();
}

function onClickDeleteProject() {
    deleteProject();
}

function onClickEditProject() {
    if (_indexProjectSelected === -1) {
        _notify.show('Debes seleccionar un proyecto', 'warning');
        return;
    }

    var item = _projectData[_indexProjectSelected];
    $('#titleModalNewProject').html("Editar proyecto");
    $('#idNewProject').val(item.id);
    $('#nameProject').val(item.name);
    $('#descriptionProject').text(item.description);
    $('#statusProject').val(item.status);
    $('#initDate').val(_uiUtil.getFormattedDateUS(item.initDate));
    $('#endDate').val(_uiUtil.getFormattedDateUS(item.endDate));
    $saveModalProject.modal();
}

function findProjectData() {
    $.ajax({
        type: "POST",
        url: $.PATH + "project/findProjects",
        data: {name: $txtFilterName.val(), status: $txtFilterStatus.val(),
            description: $txtFilterDescription.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($tableProject);
            _indexProjectSelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                _projectData = [];
                $.each(items, function (i, item) {
                    addRowToTable(item, $tableProject);
                    _projectData.push(item);
                });
                $tableProject.tablePagination(_uiUtil.getOptionsPaginator(10));
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
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDate(item.initDate) + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDate(item.endDate) + "</td>";
    fila += "<td align='center'>" + "<a class='btn btn-info fa fa-tasks' onclick='showDivTasks(" + noFila + ");'></a>" + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function saveProject() {
    var id = $('#idNewProject').val();
    var name = $('#nameProject').val();
    var description = $('#descriptionProject').val();
    var status = $('#statusProject').val();

    $.ajax({
        type: "POST",
        url: $.PATH + "project/saveProject",
        data: {id: id, name: name, description: description, status: status, initDate: $initDate.val(),
            endDate: $endDate.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Proyecto guardado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $saveModalProject.modal('hide');
            findProjectData();
        }
    });
}

function deleteProject() {
    var id = $('#idDeleteProject').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "project/deleteProject",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Proyecto eliminado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalProject').modal('hide');
            findProjectData();
        }
    });
}

function showDivTasks(noRow) {
    var item = _projectData[noRow];
    _indexProjectSelected = noRow;
    $divProjects.css("display", "none");
    $divTasks.css("display", "block");
    $('#divTitleTasks').html(item.name);

    $initDateAim.attr("min", _uiUtil.getFormattedDateUS(item.initDate));
    $initDateAim.attr("max", _uiUtil.getFormattedDateUS(item.endDate));

    $endDateAim.attr("min", _uiUtil.getFormattedDateUS(item.initDate));
    $endDateAim.attr("max", _uiUtil.getFormattedDateUS(item.endDate));
    findAimData();
}

function onClickNewAim() {
    _uiUtil.cleanControls($saveModalAim);
    $('#titleModalNewAim').html("Agregar objetivo");
    $('#idNewAim').val(0);
    $saveModalAim.modal();
}

function saveAim() {
    var id = $('#idNewAim').val();
    var name = $('#nameAim').val();
    var description = $('#descriptionAim').val();
    var status = $('#statusAim').val();
    var initDate = $('#initDateAim').val();
    var endDate = $('#endDateAim').val();
    var item = _projectData[_indexProjectSelected];

    $.ajax({
        type: "POST",
        url: $.PATH + "project/saveAim",
        data: {id: id, name: name, description: description, status: status, initDate: initDate,
            endDate: endDate, idProject: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Objetivo guardado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            _uiUtil.cleanControls($saveModalAim);
            $saveModalAim.modal('hide');
            findAimData();
        }
    });
}

function findAimData() {
    var item = _projectData[_indexProjectSelected];
    $.ajax({
        type: "POST",
        url: $.PATH + "project/findAims",
        data: {idProject: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($tableAim);
            _indexAimSelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                _aimData = [];
                $.each(items, function (i, item) {
                    _aimData.push(item);
                    addRowToTableAim(item, $tableAim);
                });
                $tableAim.tablePagination(_uiUtil.getOptionsPaginator(5));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTableAim(item, table) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDate(item.initDate) + "</td>";
    fila += "<td>" + _uiUtil.getFormattedDate(item.endDate) + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickRefreshAim() {
    findAimData();
    _uiUtil.clearDataTable($tableTask);
    _indexTaskSelected = -1;
}

function onClickConfirmDeleteAim() {
    if (_indexAimSelected === -1) {
        _notify.show('Selecciona primero un objetivo', 'warning');
        return;
    }

    var item = _aimData[_indexAimSelected];
    $('#idDeleteAim').val(item.id);
    $('#deleteLabelAim').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModalAim').modal();
}

function onClickEditAim() {
    if (_indexAimSelected === -1) {
        _notify.show('Selecciona primero un objetivo', 'warning');
        return;
    }

    var item = _aimData[_indexAimSelected];
    $('#titleModalNewAim').html("Editar objetivo");

    $('#idNewAim').val(item.id);
    $('#nameAim').val(item.name);
    $('#descriptionAim').val(item.description);
    $('#statusAim').val(item.status);
    $('#initDateAim').val(_uiUtil.getFormattedDateUS(item.initDate));
    $('#endDateAim').val(_uiUtil.getFormattedDateUS(item.endDate));
    $saveModalAim.modal();
}

function onClickDeleteAim() {
    deleteAim();
}

function deleteAim() {
    var id = $('#idDeleteAim').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "project/deleteAim",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Objetivo eliminado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalAim').modal('hide');
            findAimData();
        }
    });
}

function onFocusOutInitAim() {
    if (_jsUtil.compareDate($initDateAim.val(), $endDateAim.val(), 'yyyy-MM-dd')) {
        $endDateAim.val($initDateAim.val());
    }
}

function onFocusOutEndAim() {
    if (_jsUtil.compareDate($initDateAim.val(), $endDateAim.val(), 'yyyy-MM-dd')) {
        $initDateAim.val($endDateAim.val());
    }
}

function onFocusOutInitProject() {
    if (_jsUtil.compareDate($initDate.val(), $endDate.val(), 'yyyy-MM-dd')) {
        $endDate.val($initDate.val());
    }
}

function onFocusOutEndProject() {
    if (_jsUtil.compareDate($initDate.val(), $endDate.val(), 'yyyy-MM-dd')) {
        $initDate.val($endDate.val());
    }
}

function onClickBtnNewTask() {
    if (_indexAimSelected === -1) {
        _notify.show('Selecciona primero un objetivo', 'warning');
        return;
    }
    
    var item = _aimData[_indexAimSelected];
    
    _uiUtil.cleanControls($saveModalTask);
    $('#titleModalNewTask').html("Agregar tarea");
    $('#idNewTask').val(0);
    $('#divTitleAim').html(item.name);
    $saveModalTask.modal();
}

function saveTask() {
    var id = $('#idNewTask').val();
    var name = $('#nameTask').val();
    var description = $('#descriptionTask').val();
    var priority = $('#priorityTask').val();
    var estimated = $('#estimatedTask').val();
    var username = $('#userTask').val();
    var item = _aimData[_indexAimSelected];

    $.ajax({
        type: "POST",
        url: $.PATH + "project/saveTask",
        data: {id: id, name: name, description: description, priority: priority,
            estimated: estimated, username:username, idAim: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Tarea guardado con éxito", 'success');
                if(id != 0){
                    $saveModalTask.modal('hide');
                }
                $('#nameTask').focus();
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            _uiUtil.cleanControls($saveModalTask);
            findTaskData();
        }
    });
}

function findTaskData() {
    var item = _aimData[_indexAimSelected];
    $.ajax({
        type: "POST",
        url: $.PATH + "project/findTasks",
        data: {idAim: item.id},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($tableTask);
            _indexTaskSelected = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                _taskData = [];
                $.each(items, function (i, item) {
                    _taskData.push(item);
                    addRowToTableTask(item, $tableTask);
                });
                $tableTask.tablePagination(_uiUtil.getOptionsPaginator(5));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTableTask(item, table) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td>" + item.status + "</td>";
    fila += "<td>" + item.priority + "</td>";
    fila += "<td>" + item.estimatedTime + "</td>";
    fila += "<td>" + item.userOwner.username + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function onClickBtnRefreshTask(){
    if (_indexAimSelected === -1) {
        _notify.show('Selecciona primero un objetivo', 'warning');
        return;
    }
    
    findTaskData();
}

function onClickBtnCofirmDeleteTask() {
    if (_indexTaskSelected === -1) {
        _notify.show('Selecciona primero una tarea', 'warning');
        return;
    }

    var item = _taskData[_indexTaskSelected];
    $('#idDeleteTask').val(item.id);
    $('#deleteLabelTask').html("¿Está seguro de eliminar <b>" + item.name + "</b>?");
    $('#confirmModalTask').modal();
}

function onClickBtnDeleteTask() {
    deleteTask();
}

function deleteTask() {
    var id = $('#idDeleteTask').val();
    $.ajax({
        type: "POST",
        url: $.PATH + "project/deleteTask",
        data: {id: id},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (response) {
            if (response === "OK") {
                _notify.show("Tarea eliminado con éxito", 'success');
            } else {
                _notify.show(response, 'danger');
            }
        }, complete: function () {
            _blockUI.unblock();
            $('#confirmModalTask').modal('hide');
            findTaskData();
        }
    });
}

function onClickBtnEditTask() {
    if (_indexTaskSelected === -1) {
        _notify.show('Selecciona primero una tarea', 'warning');
        return;
    }

    var item = _taskData[_indexTaskSelected];
    $('#titleModalNewTask').html("Editar tarea");

    $('#idNewTask').val(item.id);
    $('#nameTask').val(item.name);
    $('#descriptionTask').val(item.description);
    $('#priorityTask').val(item.priority);
    $('#estimatedTask').val(item.estimatedTime);
    $('#userTask').val(item.userOwner.username);
    $saveModalTask.modal();
}