function findAims(idProject, select, text) {
    select.empty();
    select.append("<option value='0'>" + text + "</option>");

    $.ajax({
        type: "POST",
        url: $.PATH + "all/findAims",
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

