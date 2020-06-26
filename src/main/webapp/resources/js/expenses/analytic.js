let $btnRefresh,
        $dataTableGral,
        $btnRefreshSub, $btnRefreshDetails, $btnYearBack, $btnGralBack, $btnRefreshComparator,
    $idCategoryF, $idSubcategoryF;

let $divChart, $btnRefreshMonth, $dataTableMonth, $year, $totalMonth, $tittleCategoryMonth,
        $dataTableCatMonth, $totalMonthCategory;

let $divCategory, $totalCategory,
        $divSubcategory, $totalSubcategory;

let _indexSelectedCategory = -1, _dataCategory = [], _indexSelectedMonth = -1, _indexSelectedCategoryDetail = -1;

let $dataTableSub, $dataTableSubMonth, $totalMonthSubcategory;

let $divCategoryDetail, $divSubcategoryDetail, $tittleSubcategoryMonth, _dataCategoryDetails = [], $btnRefreshDetailsSub, $btnYearBackSub;

let _months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

let __chartCategory, __chartSubcategory, __chartCategoryYear, __chartSubcategoryYear;

$(document).ready(function () {
    initComponents();
    initPanels();
    initEvents();
    findDataCategory();
    buildChartsMonths();
    buildChartsComparator();
});

function initComponents() {
    $btnRefresh = $('#btnRefresh');
    $btnRefreshSub = $('#btnRefreshSub');
    $dataTableGral = $('#dataTableGral');
    $btnRefreshDetails = $('#btnRefreshDetails');
    $btnYearBack = $('#btnYearBack');
    $btnGralBack = $('#btnGralBack');
    $btnRefreshComparator = $('#btnRefreshComparator');
    $idCategoryF = $('#idCategoryF');
    $idSubcategoryF = $('#idSubcategoryF');

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

    $divCategoryDetail = $('#divCategoryDetail');
    $divSubcategoryDetail = $('#divSubcategoryDetail');
    $tittleSubcategoryMonth = $('#tittleSubcategoryMonth');
    $btnRefreshDetailsSub = $('#btnRefreshDetailsSub');
    $btnYearBackSub = $('#btnYearBackSub');
    $dataTableSubMonth = $('#dataTableSubMonth');
    $totalMonthSubcategory = $('#totalMonthSubcategory');

    $btnYearBack.hide();
    
    __chartCategory = new mx.jeegox.jChart($('#divGraphCategory'));
    __chartSubcategory = new mx.jeegox.jChart($('#divGraphSubcategory'));
    __chartCategoryYear = new mx.jeegox.jChart($('#divGraphCategoryMonth'));
    __chartSubcategoryYear = new mx.jeegox.jChart($('#divGraphSubcategoryMonth'));
}

function initPanels() {
    $divCategory.css("display", "block");
    $divSubcategory.css("display", "none");

    $divCategoryDetail.css("display", "block");
    $divSubcategoryDetail.css("display", "none");
}

function returnGeneral() {
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

    $dataTableCatMonth.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
    });

    $dataTableCatMonth.on('dblclick', 'tbody tr', function (event) {
        _indexSelectedCategoryDetail = $(this).data('meta-row');
        showSubcategoryDetails();
    });

    $btnRefreshSub.click(onClickBtnRefreshSub);
    $btnRefreshMonth.click(onClickBtnRefreshMonth);
    $year.change(onChangeYear);
    $btnYearBack.click(onClickBtnYearBack);
    $btnGralBack.click(onClickBtnGralBack);
    $btnRefreshComparator.click(onClickBtnRefreshComparator);

    $btnYearBackSub.click(onClickBtnYearBackSub);
    $btnRefreshDetailsSub.click(onClickBtnRefreshDetailsSub);


    $idCategoryF.change(function(){
        findSubcategories($idCategoryF.val(), $idSubcategoryF, "--Todos");
        buildChartsComparator();
    });

    $idSubcategoryF.change(function(){
        buildChartsComparator();
    });
}

function onClickBtnRefresh() {
    findDataCategory();
}

function onClickbtnRefreshDetails() {
    if (_indexSelectedMonth !== -1)
        buildChartMonths_yearMonth();
    else
        buildChartMonths_year();
}

function onClickBtnYearBackSub() {
    onClickbtnRefreshDetails();
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

function onClickBtnYearBack() {
    buildChartMonths_year();
}

function onClickBtnGralBack() {
    returnGeneral();
}

function onClickBtnRefreshComparator(){
    buildChartsComparator();
}

function showSubcategoryDetails() {
    $divCategoryDetail.css("display", "none");
    $divSubcategoryDetail.css("display", "block");

    let item = _dataCategoryDetails[_indexSelectedCategoryDetail];

    let title = item.name;
    if (_indexSelectedMonth !== -1)
        title += " " + _months[_indexSelectedMonth];

    title += " " + $year.val();

    $tittleSubcategoryMonth.html(title);
    
    buildCharSubcategory_yearMonth(item.id);
}

function onClickBtnRefreshDetailsSub(){
    showSubcategoryDetails();
}

function showCategoryDetails_year() {
    $divCategoryDetail.css("display", "block");
    $divSubcategoryDetail.css("display", "none");

    $btnYearBack.hide();
    $tittleCategoryMonth.html($year.val());
    _indexSelectedMonth = -1;
}

function showCategoryDetails_month() {
    $divCategoryDetail.css("display", "block");
    $divSubcategoryDetail.css("display", "none");

    $btnYearBack.show();
    $tittleCategoryMonth.html(_months[_indexSelectedMonth] + " " + $year.val());
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
            __chartCategory.clearPie();
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

                $dataTableGral.tablePagination(_uiUtil.getOptionsPaginator(5));
                __chartCategory.buildPie(data, labels);
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
            __chartSubcategory.clearPie();
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

                __chartSubcategory.buildPie(data, labels);
                $totalSubcategory.html(accounting.formatMoney(total));

            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}
function buildChartsMonths() {
    let labelsMonth = [];
    let dataMonth = [];

    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/getMonthAmounts",
        data: {
            year: $year.val()
        },
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
                    dataMonth.push(_jsUtil.round(item.amount));
                    totalMonth += _jsUtil.round(item.amount);
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

function buildChartMonths_year() {
    showCategoryDetails_year();

    let labels = [];
    let data = [];
    let totalMonthCat = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategoryYear",
        data: {
            year: $year.val()
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableCatMonth);
            __chartCategoryYear.clearPie();
            $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            _dataCategoryDetails = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labels.push(item.name);
                    totalMonthCat += item.amount;
                    _dataCategoryDetails.push(item);
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / totalMonthCat) * 100);
                    data.push(percent);
                    addRowToTable(item, $dataTableCatMonth, percent);
                });
                $dataTableCatMonth.tablePagination(_uiUtil.getOptionsPaginator(5));
                __chartCategoryYear.buildPie(data, labels);
                $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function buildChartMonths_yearMonth() {
    showCategoryDetails_month();

    let labels = [];
    let data = [];
    let totalMonthCat = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategoryYearMonth",
        data: {
            year: $year.val(),
            month: (_indexSelectedMonth + 1)
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableCatMonth);
            __chartCategoryYear.clearPie();
            $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            _dataCategoryDetails = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labels.push(item.name);
                    totalMonthCat += item.amount;
                    _dataCategoryDetails.push(item);
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / totalMonthCat) * 100);
                    data.push(percent);
                    addRowToTable(item, $dataTableCatMonth, percent);
                });
                $dataTableCatMonth.tablePagination(_uiUtil.getOptionsPaginator(5));
                __chartCategoryYear.buildPie(data, labels);
                $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function buildCharSubcategory_yearMonth(idCategory) {

    let labels = [];
    let data = [];
    let totalMonthCat = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataSubcategoryYearMonth",
        data: {
            idCategory: idCategory, 
            year: $year.val(),
            month: _indexSelectedMonth === -1 ? _indexSelectedMonth : (_indexSelectedMonth + 1)
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            _uiUtil.clearDataTable($dataTableSubMonth);
            __chartSubcategoryYear.clearPie();
            $totalMonthSubcategory.html(accounting.formatMoney(totalMonthCat));
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labels.push(item.name);
                    totalMonthCat += item.amount;
                });

                $.each(items, function (i, item) {
                    var percent = _jsUtil.round((item.amount / totalMonthCat) * 100);
                    data.push(percent);
                    addRowToTable(item, $dataTableSubMonth, percent);
                });
                $dataTableSubMonth.tablePagination(_uiUtil.getOptionsPaginator(5));
                __chartSubcategoryYear.buildPie(data, labels);
                $totalMonthSubcategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeGraphMonth(months, data) {
    $divChart.html('<canvas id="myBarChart" width="100" height="35"></canvas>');
    const ctxLine = $("#myBarChart");
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

function buildChartsComparator() {
    let datasets= [];

    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategoryAllYears",
        data: {
            idCategory: $idCategoryF.val(),
            idSubcategory: $idSubcategoryF.val()
        },
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            //writeGraphComparator(datasets);
        },
        success: function (items) {
            let colors = _uiUtil.randomArrayColorGenerator($year.find("option").length);
            let i = 0;
            $.each(items, function (year, item) {
                datasets.push({
                    label: year,
                    backgroundColor: colors[i],
                    borderColor: colors[i],
                    data: item.map(function(i){
                        return i.amount;
                    }),
                    fill: false
                });
                i++;
            });
            writeGraphComparator(datasets);
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function writeGraphComparator(datasets) {
    $('#divCharComparator').html('<canvas id="myComparatorChart" width="100" height="40"></canvas>');
    const ctxLine = $("#myComparatorChart");
    new Chart(ctxLine, {
        type: 'line',
        data: {
            labels: _months,
            datasets: datasets
        },
        options: {
            responsive: true,
            tooltips: {
                mode: 'index',
                intersect: false,
            },
            hover: {
                mode: 'nearest',
                intersect: true
            }
        }
    });
}

function findSubcategories(idCategory, select, text) {
    select.empty();
    select.append("<option value='-1'>" + text + "</option>");

    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findSubcategories",
        async: false,
        data: {
            idCategory: idCategory
        },
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