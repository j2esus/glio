_uiUtil = (function () {

    function cleanControls(context) {
        $('input[type=text]', context).each(function () {
            $(this).val('');
        });
        $('input[type=checkbox]', context).each(function () {
            $(this).attr('checked', false);
        });
        $('input[type=email]', context).each(function () {
            $(this).val('');
        });
        $('input[type=password]', context).each(function () {
            $(this).val('');
        });
        $('input[type=number]', context).each(function () {
            $(this).val('');
        });
        $('input[type=date]', context).each(function () {
            $(this).val('');
        });
        $('textarea', context).each(function () {
            $(this).val('');
        });
        $('select', context).each(function () {
            $(this).val('');
        });
    }

    function clearDataTable(table) {
        table.find("tbody").eq(0).find("tr").remove();
    }

    function getFormattedDateTime(dateParam) {

        var dateToFormat = new Date();
        dateToFormat.setTime(dateParam);

        var date = dateToFormat.getDate();

        if (date.toString().length == 1)
            date = "0" + date.toString();

        var month = dateToFormat.getMonth();
        month++;

        if (month.toString().length == 1)
            month = "0" + month.toString();

        var year = dateToFormat.getFullYear();

        var hour = dateToFormat.getHours();

        if (hour.toString().length == 1)
            hour = "0" + hour.toString();

        var min = dateToFormat.getMinutes();

        if (min.toString().length == 1)
            min = "0" + min.toString();

        var sec = dateToFormat.getSeconds();

        if (sec.toString().length == 1)
            sec = "0" + sec.toString();

        return date + "/" + month + "/" + year + " " + hour + ":" + min + ":" + sec;
    }

    function getFormattedDate(dateParam) {
        let parts = dateParam.split("-");
        let dateToFormat = new Date(parts[0], parts[1]-1, parts[2]);

        let date = dateToFormat.getDate();

        if (date.toString().length == 1)
            date = "0" + date.toString();

        let month = dateToFormat.getMonth();
        month++;

        if (month.toString().length == 1)
            month = "0" + month.toString();

        let year = dateToFormat.getFullYear();

        return date + "/" + month + "/" + year;
    }

    function getFormattedDateUS(dateParam) {

        let parts = dateParam.split("-");
        let dateToFormat = new Date(parts[0], parts[1]-1, parts[2]);

        let date = dateToFormat.getDate();

        if (date.toString().length == 1)
            date = "0" + date.toString();

        let month = dateToFormat.getMonth();
        month++;

        if (month.toString().length == 1)
            month = "0" + month.toString();

        let year = dateToFormat.getFullYear();

        return year + "-" + month + "-" + date;
    }

    function today() {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();

        if (dd < 10) {
            dd = '0' + dd;
        }

        if (mm < 10) {
            mm = '0' + mm;
        }

        today = yyyy + '-' + mm + '-' + dd;
        return today;
    }

    function describeEntity(entity) {
        var descriptores = [];
        $.ajax({
            method: "POST",
            url: $.PATH + "global/describe?clazz=" + entity,
            async: false,
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                descriptores = response;
            },
            error: function (request, error) {
                $.notify({
                    message: request.statusText
                }, {
                    type: 'danger'
                });
            }
        });
        return descriptores;
    }

    function getOptionsPaginator(noRegistros) {
        var options = {
            currPage: 1,
            optionsForRows: [noRegistros, (noRegistros * 2), (noRegistros * 3)],
            rowsPerPage: noRegistros,
            firstArrow: (new Image()).src = $.IMAGES + "first.gif",
            prevArrow: (new Image()).src = $.IMAGES + "prev.gif",
            lastArrow: (new Image()).src = $.IMAGES + "last.gif",
            nextArrow: (new Image()).src = $.IMAGES + "next.gif",
            topNav: true
        };
        return options;
    }

    function getUsername() {
        return $('#sessionUsername').val();
    }

    function getStringPriority(priority) {
        switch (priority) {
            case 0:
                return 'Alta';
                break;
            case 1:
                return 'Media';
                break;
            default:
                return 'Baja';
                break;
        }
    }

    function writePriorityColorInt(priority) {
        var type = '';
        switch (priority) {
            case 0:
                type = '#f2dede';
                break;
            case 1:
                type = '#fcf8e3';
                break;
            case 2:
                type = '#d9edf7';
                break;
        }
        return type;
    }

    function randomArrayColorGenerator(length) {
        let colors = [];
        do {
            var random = '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
            if (!colors.includes(random)) {
                colors.push(random);
            }
        } while (colors.length < length)
        return colors;
    }

    function getBooleanValueLabel(value){
        if(value)
            return "<span class='badge badge-success'>Si<span>";
        return "<span class='badge badge-warning'>No<span>";
    }

    return {
        cleanControls: cleanControls,
        describeEntity: describeEntity,
        clearDataTable: clearDataTable,
        getFormattedDateTime: getFormattedDateTime,
        getOptionsPaginator: getOptionsPaginator,
        getFormattedDate: getFormattedDate,
        getFormattedDateUS: getFormattedDateUS,
        getUsername: getUsername,
        getStringPriority: getStringPriority,
        writePriorityColorInt: writePriorityColorInt,
        today: today,
        randomArrayColorGenerator: randomArrayColorGenerator,
        getBooleanValueLabel: getBooleanValueLabel
    };
})();

_blockUI = (function () {

    function block() {
        $.blockUI({
            message: "Cargando... espere por favor.",
            overlayCSS: {backgroundColor: '#FFFFFF'},
            baseZ: 2000,
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                color: '#fff'
            }
        });
    }

    function unblock() {
        $.unblockUI();
    }

    return {
        block: block,
        unblock: unblock
    };
})();

_notify = (function () {

    function show(message, type) {
        $.notify({
            message: message
        }, {
            type: type,
            z_index: 2000,
            delay: 3000,
            animate: {
                enter: 'animated fadeInDown',
                exit: 'animated fadeOutUp'
            },
            placement: {
                from: "top",
                align: "center"
            }
        });
    }

    function showDelay(message, type, delay) {
        $.notify({
            message: message
        }, {
            type: type,
            z_index: 2000,
            delay: delay,
            animate: {
                enter: 'animated fadeInDown',
                exit: 'animated fadeOutUp'
            },
            placement: {
                from: "top",
                align: "center"
            }
        });
    }

    return {
        show: show,
        showDelay: showDelay
    };

})();

_jsUtil = (function () {
    function redirect(url) {
        $(location).attr("href", $.PATH + url);
    }

    function compareDate(fecha, fecha2, format) {
        if (format === 'dd-MM-yyyy' || format === 'dd/MM/yyyy') {
            var xDay = fecha.substring(0, 2);
            var xMonth = fecha.substring(3, 5);
            var xYear = fecha.substring(6, 10);

            var yDay = fecha2.substring(0, 2);
            var yMonth = fecha2.substring(3, 5);
            var yYear = fecha2.substring(6, 10);
        } else {
            var xYear = fecha.substring(0, 4);
            var xMonth = fecha.substring(5, 7);
            var xDay = fecha.substring(8, 10);

            var yYear = fecha2.substring(0, 4);
            var yMonth = fecha2.substring(5, 7);
            var yDay = fecha2.substring(8, 10);


        }

        if (xYear > yYear) {
            return (true);
        } else {
            if (xYear == yYear) {
                if (xMonth > yMonth) {
                    return (true);
                } else {
                    if (xMonth == yMonth) {
                        if (xDay > yDay)
                            return (true);
                        else
                            return (false);
                    } else
                        return (false);
                }
            } else
                return (false);
        }
    }

    function round(value) {
        return Math.round(value * 100) / 100;
    }


    return {
        redirect: redirect,
        compareDate: compareDate,
        round: round
    };
})();

(function ($) {
    function jChart(div) {
        let _chart;
        let _canvas;
        
        function init(){
            _canvas = (Math.random().toString(16));
        }

        function buildPie(data, labels) {
            div.html('');
            div.html('<canvas id="'+_canvas+'" style="width: 100%"></canvas>');

            let myChart = document.getElementById(_canvas);
            let ctx = myChart.getContext('2d');

            let config = {
                datasets: [{
                        data: data,
                        backgroundColor: _uiUtil.randomArrayColorGenerator(labels.length)
                    }],
                labels: labels
            };

            _chart = new Chart(ctx, {
                type: 'pie',
                data: config,
                options: {
                    responsive: true
                }
            });
        }

        function clearPie(){
            buildPie([], []);
        }

        $.extend(this, {
            buildPie: buildPie,
            clearPie: clearPie
        });
        
        init();
    }
    $.extend(true, window, {mx: {jeegox: {jChart: jChart}}});
})(jQuery);

(function ($) {

    function Address(stateSelect, townSelect, suburbSelect, zipcode) {
        let label = '--Seleccione';

        function init(){
            stateSelect.change(stateOnChange);
            townSelect.change(townOnChange);
            suburbSelect.change(suburbOnChange);
            zipcode.blur(zipcodeOnBlur);
        }

        function loadStates() {
            $.ajax({
                method: "POST",
                url: $.PATH + "all/findAllStates",
                async: false,
                beforeSend: function () {
                    _blockUI.block();
                    cleanControls();
                },
                success: function (items) {
                    if (items.length > 0) {
                        $.each(items, function (i, item) {
                            stateSelect.append("<option value='" + item.id + "'>" + item.name + "</option>");
0                        });
                    }
                }, complete: function () {
                    _blockUI.unblock();
                }
            });
        }

        function findTowns() {
            $.ajax({
                method: "POST",
                url: $.PATH + "all/findTowns",
                async: false,
                data: {
                    idState: stateSelect.val()
                },
                beforeSend: function () {
                    _blockUI.block();
                    townSelect.empty();
                    suburbSelect.empty();
                    initSelect(townSelect);
                    initSelect(suburbSelect);
                },
                success: function (items) {
                    if (items.length > 0) {
                        $.each(items, function (i, item) {
                            townSelect.append("<option value='" + item.id + "'>" + item.name + "</option>");
                        });
                    }
                }, complete: function () {
                    _blockUI.unblock();
                }
            });
        }

        function findSuburbs() {
            $.ajax({
                method: "POST",
                url: $.PATH + "all/findSuburbs",
                async: false,
                data: {
                    idTown: townSelect.val()
                },
                beforeSend: function () {
                    _blockUI.block();
                    suburbSelect.empty();
                    zipcode.val('');
                    initSelect(suburbSelect);
                },
                success: function (items) {
                    if (items.length > 0) {
                        $.each(items, function (i, item) {
                            suburbSelect.append("<option value='" + item.id + "' cp='" + item.cp + "'>" + item.name + "</option>");
                        });
                    }
                }, complete: function () {
                    _blockUI.unblock();
                }
            });
        }

        function findSuburbsByZipcode() {
            if(zipcode.val() === '')
                return;
            $.ajax({
                method: "POST",
                url: $.PATH + "all/findSuburbsByZipcode",
                async: false,
                data: {
                    cp: zipcode.val()
                },
                beforeSend: function () {
                    _blockUI.block();
                    suburbSelect.empty();
                    townSelect.empty();
                    initSelect(suburbSelect);
                    initSelect(townSelect);
                },
                success: function (items) {
                    if (items.length > 0) {
                        let lastItem = items[0];
                        stateSelect.val(lastItem.father.father.id);
                        findTowns();
                        townSelect.val(lastItem.father.id);
                        $.each(items, function (i, item) {
                            suburbSelect.append("<option value='" + item.id + "' cp='" + item.cp + "'>" + item.name + "</option>");
                        });
                    }else{
                        _notify.show("Codigo postal introducido no produjo resultados.", 'danger');
                    }
                }, complete: function () {
                    _blockUI.unblock();
                }
            });
        }

        function findSuburbsByZipCodeAndName(zc, name) {
            $.ajax({
                method: "POST",
                url: $.PATH + "all/findSuburbsByZipCodeAndName",
                async: false,
                data: {
                    zipcode: zc,
                    name: name
                },
                beforeSend: function () {
                    _blockUI.block();
                    loadStates();
                },
                success: function (response) {
                    stateSelect.val(response.father.father.id);
                    findTowns();
                    townSelect.val(response.father.id);
                    findSuburbs();
                    suburbSelect.val(response.id);
                    zipcode.val(response.cp);
                }, complete: function () {
                    _blockUI.unblock();
                }, error: function(xhr, status, error){
                    _notify.showDelay('Colonia no encontrada: '+name +' con codigo postal: '+zc, 'danger', 20000);
                }
            });
        }

        function cleanControls(){
            stateSelect.empty();
            townSelect.empty();
            suburbSelect.empty();
            zipcode.val('');
            initSelect(stateSelect);
            initSelect(townSelect);
            initSelect(suburbSelect);
        }

        function initSelect(select){
            select.append("<option value=''>" + label + "</option>");
        }

        function stateOnChange(){
            findTowns();
        }

        function townOnChange(){
            findSuburbs();
        }

        function suburbOnChange(){
            zipcode.val($("option:selected", this).attr("cp"));
        }

        function zipcodeOnBlur(){
            findSuburbsByZipcode();
        }

        function changeLabel(newLabel){
            label = newLabel;
        }

        $.extend(this, {
            loadStates: loadStates,
            changeLabel: changeLabel,
            findSuburbsByZipCodeAndName: findSuburbsByZipCodeAndName
        });

        init();
    }
    $.extend(true, window, {mx: {jeegox: {Address: Address}}});
})(jQuery);