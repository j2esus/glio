function findAllAims(idProject, select, text, defautlValueEmptyOption) {
    select.empty();
    select.append("<option value='" + defautlValueEmptyOption + "'>" + text + "</option>");

    $.ajax({
        type: "POST",
        url: $.PATH + "all/findAllAims",
        async: false,
        data: {idProject: idProject},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    select.append("<option value='" + item.id + "'>" + item.name + "</option>");
                });
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function findActiveAims(idProject, select, text, defautlValueEmptyOption) {
    select.empty();
    select.append("<option value='"+defautlValueEmptyOption+"'>" + text + "</option>");

    $.ajax({
        type: "POST",
        url: $.PATH + "all/findActiveAims",
        async: false,
        data: {idProject: idProject},
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    select.append("<option value='" + item.id + "'>" + item.name + "</option>");
                });
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

