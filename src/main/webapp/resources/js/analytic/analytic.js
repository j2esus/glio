let $btnRefresh,
        $dataTableGral,
        $btnRefreshSub, $btnRefreshDetails, $btnYearBack;

let $divChart, $btnRefreshMonth, $dataTableMonth, $year, $totalMonth, $tittleCategoryMonth,
        $dataTableCatMonth, $totalMonthCategory;


let $divCategory, $totalCategory,
        $divSubcategory, $totalSubcategory;

let _indexSelectedCategory = -1, _dataCategory = [], _indexSelectedMonth = -1;

let $dataTableSub;

let _months = ["Enero", "Febrero", "Marzon", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];


$(document).ready(function () {
    initComponents();
    initPanels();
    initEvents();
    findDataCategory();
    buildChartsMonths();
});

function initComponents() {
    $btnRefresh = $('#btnRefresh');
    $btnRefreshSub = $('#btnRefreshSub');
    $dataTableGral = $('#dataTableGral');
    $btnRefreshDetails = $('#btnRefreshDetails');
    $btnYearBack = $('#btnYearBack');

    $dataTableSub = $('#dataTableSub');

    $divCategory = $('#divCategory');
    $totalCategory = $('#totalCategory');
    $divSubcategory = $('#divSubcategory');
    $totalSubcategory = $('#totalSubcategory');

    $divChart = $('#divChart');
    $btnRefreshMonth = $('#btnRefreshMonth');
    $dataTableMonth = $('#dataTableMonth');
    $year = $('#year');
    $totalMonth = $('#totalMonth');
    $tittleCategoryMonth = $('#tittleCategoryMonth');
    $dataTableCatMonth = $('#dataTableCatMonth');
    $totalMonthCategory = $('#totalMonthCategory');
    
    $btnYearBack.hide();
}

function initPanels() {
    $divCategory.css("display", "block");
    $divSubcategory.css("display", "none");
}

function returnGeneral(){
    $divCategory.css("display", "block");
    $divSubcategory.css("display", "none");
}

function initEvents() {
    $btnRefresh.click(onClickBtnRefresh);
    $btnRefreshDetails.click(onClickbtnRefreshDetails);

    $dataTableGral.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelectedCategory = $(this).data('meta-row');
    });

    $dataTableGral.on('dblclick', 'tbody tr', function (event) {
        _indexSelectedCategory = $(this).data('meta-row');
        showSubcategoryData();
    });
    
    $dataTableMonth.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
    });
    
    $dataTableMonth.on('dblclick', 'tbody tr', function (event) {
        _indexSelectedMonth = $(this).data('meta-row');
        buildChartMonths_yearMonth();
    });

    $btnRefreshSub.click(onClickBtnRefreshSub);

    $btnRefreshMonth.click(onClickBtnRefreshMonth);

    $year.change(onChangeYear);
    
    $btnYearBack.click(onClickBtnYearBack);
}

function onClickBtnRefresh() {
    findDataCategory();
}

function onClickbtnRefreshDetails(){
    if(_indexSelectedMonth !== -1)
        buildChartMonths_yearMonth();
    else
        buildChartMonths_year();
}

function onClickBtnRefreshSub() {
    showSubcategoryData();
}

function onClickBtnRefreshMonth() {
    buildChartsMonths();
}

function onChangeYear() {
    buildChartsMonths();
}

function onClickBtnYearBack(){
    buildChartMonths_year();
}

function findDataCategory() {
    let data = [];
    let labels = [];
    let total = 0;

    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategory",
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableGral);
            buildChartCategory(data, labels);
            _dataCategory = [];
            _indexSelectedCategory = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labels.push(item.name);
                    total += item.amount;
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / total) * 100);
                    data.push(percent);
                    addRowToTable(item, $dataTableGral, percent);
                    _dataCategory.push(item);
                });

                buildChartCategory(data, labels);
                $totalCategory.html(accounting.formatMoney(total));
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTable(item, table, percent) {
    let noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    let fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td  align='right'>" + accounting.formatMoney(item.amount) + "</td>";
    fila += "<td  align='right'>" + percent + " %</td>";
    fila += "</tr>";
    table.append(fila);
}

function addRowToTableMonth(item, table) {
    let noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    let fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.monthName + "</td>";
    fila += "<td  align='right'>" + accounting.formatMoney(item.amount) + "</td>";
    fila += "</tr>";
    table.append(fila);
}

function buildChartCategory(data, labels) {

    $('#divGraphCategory').html('');
    $('#divGraphCategory').html('<canvas id="canvGraphCategory" style="width: 100%"></canvas>');

    let myChart = document.getElementById("canvGraphCategory");
    let ctxMix = myChart.getContext('2d');

    let config = {
        datasets: [{
                data: data,
                backgroundColor: randomArrayColorGenerator(labels.length)
            }],

        labels: labels
    };

    _chart = new Chart(ctxMix, {
        type: 'pie',
        data: config,
        options: {
            responsive: true
        }
    });
}

function randomArrayColorGenerator(length) {
    let colors = [];
    for (i = 0; i < length; i++) {
        var random = '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
        colors.push(random);
    }

    return colors;
}
;

function showSubcategoryData() {
    let item = _dataCategory[_indexSelectedCategory];
    $('#titleModalNew').html(item.name);
    $divCategory.css("display", "none");
    $divSubcategory.css("display", "block");
    findDataSubcategory(item.id);
}

function findDataSubcategory(idCategory) {
    let data = [];
    let labels = [];
    let total = 0;

    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataSubcategory",
        data: {idCategory: idCategory},
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableSub);
            buildChartSubcategory(data, labels);
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labels.push(item.name);
                    total += item.amount;
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / total) * 100);
                    data.push(percent);
                    addRowToTable(item, $dataTableSub, percent);
                });

                $dataTableSub.tablePagination(_uiUtil.getOptionsPaginator(5));

                buildChartSubcategory(data, labels);
                $totalSubcategory.html(accounting.formatMoney(total));

            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function buildChartSubcategory(data, labels) {

    $('#divGraphSubcategory').html('');
    $('#divGraphSubcategory').html('<canvas id="canvGraphSubcategory" style="width: 100%"></canvas>');

    let myChart = document.getElementById("canvGraphSubcategory");
    let ctxMix = myChart.getContext('2d');

    let config = {
        datasets: [{
                data: data,
                backgroundColor: randomArrayColorGenerator(labels.length)
            }],

        labels: labels
    };

    _chart = new Chart(ctxMix, {
        type: 'pie',
        data: config,
        options: {
            responsive: true
        }
    });
}

function buildChartsMonths() {
    let labelsMonth = [];
    let dataMonth = [];
    
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/getMonthAmounts",
        data: {year: $year.val()},
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableMonth);
            writeGraphMonth(dataMonth, labelsMonth);
            _indexSelectedMonth = -1;
        },
        success: function (items) {
            if (items.length > 0) {
                let totalMonth = 0;
                $.each(items, function (i, item) {
                    labelsMonth.push(item.monthName);
                    dataMonth.push(item.amount);
                    totalMonth += item.amount
                    addRowToTableMonth(item, $dataTableMonth);
                });
                $dataTableMonth.tablePagination(_uiUtil.getOptionsPaginator(6));
                writeGraphMonth(labelsMonth, dataMonth);
                $totalMonth.html(accounting.formatMoney(totalMonth));

            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });


    buildChartMonths_year();
    
}

function buildChartMonths_year(){
    $btnYearBack.hide();
    $tittleCategoryMonth.html($year.val());
    
    let labelsMonthCat = [];
    let dataMonthCat = [];
    let dataPercentMonthCat = [];
    let totalMonthCat = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategoryYear",
        data: {year: $year.val()},
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableCatMonth);
            buildChartMonthCategoryMonth(dataMonthCat, labelsMonthCat);
            $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labelsMonthCat.push(item.name);
                    dataMonthCat.push(item.amount);
                    totalMonthCat += item.amount;
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / totalMonthCat) * 100);
                    dataPercentMonthCat.push(percent);
                    addRowToTable(item, $dataTableCatMonth, percent);
                });
                $dataTableCatMonth.tablePagination(_uiUtil.getOptionsPaginator(5));
                buildChartMonthCategoryMonth(dataPercentMonthCat, labelsMonthCat);
                $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function buildChartMonths_yearMonth(){
    $btnYearBack.show();
    $tittleCategoryMonth.html(_months[_indexSelectedMonth] +" "+$year.val());
    
    let labelsMonthCat = [];
    let dataMonthCat = [];
    let dataPercentMonthCat = [];
    let totalMonthCat = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategoryYearMonth",
        data: {
            year: $year.val(), 
            month: (_indexSelectedMonth +1)
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableCatMonth);
            buildChartMonthCategoryMonth(dataMonthCat, labelsMonthCat);
            $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labelsMonthCat.push(item.name);
                    dataMonthCat.push(item.amount);
                    totalMonthCat += item.amount;
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / totalMonthCat) * 100);
                    dataPercentMonthCat.push(percent);
                    addRowToTable(item, $dataTableCatMonth, percent);
                });
                $dataTableCatMonth.tablePagination(_uiUtil.getOptionsPaginator(5));
                buildChartMonthCategoryMonth(dataPercentMonthCat, labelsMonthCat);
                $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeGraphMonth(months, data) {
    $divChart.html('<canvas id="myBarChart" width="100" height="35"></canvas>');
    var ctxLine = $("#myBarChart");
    new Chart(ctxLine, {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                    label: "Gasto mensual",
                    backgroundColor: "rgba(2,117,216,1)",
                    borderColor: "rgba(2,117,216,1)",
                    data: data
                }]
        },
        options: {
            responsive: true,
            scales: {
                xAxes: [{
                        gridLines: {
                            display: false
                        }
                    }],
                yAxes: [{
                        gridLines: {
                            display: true
                        }
                    }]
            },
            legend: {
                display: false
            }
        }
    });
}

function buildChartMonthCategoryMonth(data, labels) {

    $('#divGraphCategoryMonth').html('');
    $('#divGraphCategoryMonth').html('<canvas id="canvGraphCategoryMonth" style="width: 100%"></canvas>');

    let myChart = document.getElementById("canvGraphCategoryMonth");
    let ctxMix = myChart.getContext('2d');

    let config = {
        datasets: [{
                data: data,
                backgroundColor: randomArrayColorGenerator(labels.length)
            }],

        labels: labels
    };

    _chart = new Chart(ctxMix, {
        type: 'pie',
        data: config,
        options: {
            responsive: true
        }
    });
}