_messageUtil = (function () {

    function findMessage(code) {
        var message = null;
        message = localStorage.getItem(code);
        if (message != null)
            return message;
        $.ajax({
            method: "POST",
            url: $.PATH + "i18n/findMessage",
            data: "code=" + code,
            async: false,
            success: function (response) {
                message = response;
            }
        }).done(function () {
            localStorage.setItem(code, message);
            return message;
        });
    }

    function loadMessages(codes) {
        $.ajax({
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: $.PATH + 'i18n/loadMessages',
            data: JSON.stringify(codes),
            async: false,
            success: function (response) {
                $.each(response, function (k, v) {
                    if (localStorage.getItem(k) === null)
                        localStorage.setItem(k, v);
                });
            }
        });
    }

    return {
        findMessage: findMessage,
        loadMessages: loadMessages
    };
})();

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

        return date + "/" + month + "/" + year;
    }

    function getFormattedDateUS(dateParam) {

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
        randomArrayColorGenerator: randomArrayColorGenerator
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

    return {
        show: show
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

        function builtPie(data, labels) {
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

        $.extend(this, {
            builtPie: builtPie
        });
        
        init();
    }
    $.extend(true, window, {mx: {jeegox: {jChart: jChart}}});
})(jQuery);