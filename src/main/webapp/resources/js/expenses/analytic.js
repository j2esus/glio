let $btnRefresh,
        $dataTableGral,
        $btnRefreshSub, $btnYearBack, $btnGralBack, $btnRefreshComparator,
    $idCategoryF, $idSubcategoryF, $btnCategoryBack;

let $btnRefreshMonth, $year, $totalMonth, $totalMonthCategory;

let $divCategory, $totalCategory,
        $divSubcategory, $totalSubcategory;

let _indexSelectedCategory = -1, _dataCategory = [], _indexSelectedMonth = -1, _indexSelectedCategoryDetail = -1;

let $dataTableSub;

let _dataCategoryDetails = [];

let _months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

let __chartCategory, __chartSubcategory;

$(document).ready(function () {
    initComponents();
    initPanels();
    initEvents();
    findDataCategory();
    createChartsPerYear();
    buildChartsComparator();
});

function initComponents() {
    $btnRefresh = $('#btnRefresh');
    $btnRefreshSub = $('#btnRefreshSub');
    $dataTableGral = $('#dataTableGral');
    $btnYearBack = $('#btnYearBack');
    $btnCategoryBack = $('#btnCategoryBack');
    $btnGralBack = $('#btnGralBack');
    $btnRefreshComparator = $('#btnRefreshComparator');
    $idCategoryF = $('#idCategoryF');
    $idSubcategoryF = $('#idSubcategoryF');

    $dataTableSub = $('#dataTableSub');

    $divCategory = $('#divCategory');
    $totalCategory = $('#totalCategory');
    $divSubcategory = $('#divSubcategory');
    $totalSubcategory = $('#totalSubcategory');

    $btnRefreshMonth = $('#btnRefreshMonth');
    $year = $('#year');
    $totalMonth = $('#totalMonth');
    $totalMonthCategory = $('#totalMonthCategory');

    $btnYearBack.hide();
    $btnCategoryBack.hide();
    
    __chartCategory = new mx.jeegox.jChart($('#divGraphCategory'));
    __chartSubcategory = new mx.jeegox.jChart($('#divGraphSubcategory'));
}

function initPanels() {
    $divCategory.css("display", "block");
    $divSubcategory.css("display", "none");
}

function returnGeneral() {
    $divCategory.css("display", "block");
    $divSubcategory.css("display", "none");
}

function initEvents() {
    $btnRefresh.click(onClickBtnRefresh);

    $dataTableGral.on('click', 'tbody tr', function (event) {
        $(this).addClass('row-selected').siblings().removeClass('row-selected');
        _indexSelectedCategory = $(this).data('meta-row');
    });

    $dataTableGral.on('dblclick', 'tbody tr', function (event) {
        _indexSelectedCategory = $(this).data('meta-row');
        showSubcategoryData();
    });

    $btnRefreshSub.click(onClickBtnRefreshSub);
    $btnRefreshMonth.click(onClickBtnRefreshMonth);
    $year.change(onChangeYear);
    $btnYearBack.click(onClickBtnYearBack);
    $btnCategoryBack.click(onClickBtnCategoryBack);
    $btnGralBack.click(onClickBtnGralBack);
    $btnRefreshComparator.click(onClickBtnRefreshComparator);


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

function onClickBtnRefreshSub() {
    showSubcategoryData();
}

function onClickBtnRefreshMonth() {
    createChartsPerYear();
}

function onChangeYear() {
    createChartsPerYear();
}

function onClickBtnYearBack() {
    setYearLevel();
    createCategoryDetailCharts();
}

function onClickBtnCategoryBack(){
    setCategoryMonthLevel();
    createCategoryDetailCharts();
}

function onClickBtnGralBack() {
    returnGeneral();
}

function onClickBtnRefreshComparator(){
    buildChartsComparator();
}

function setSubcategoryLevel() {
    let item = _dataCategoryDetails[_indexSelectedCategoryDetail];

    let title = item.name;
    if (_indexSelectedMonth !== -1)
        title += " " + _months[_indexSelectedMonth];

    title += " " + $year.val();

    $('#labelPieChartDetailPerMonth').html(title);
    $('#labelBarChartDetailPerMonth').html(title);
    $btnCategoryBack.show();
    $btnYearBack.hide();
}

function setYearLevel() {
    $btnYearBack.hide();
    $btnCategoryBack.hide();
    _indexSelectedMonth = -1;
    $('#labelPieChartDetailPerMonth').html($year.val());
    $('#labelBarChartDetailPerMonth').html($year.val());
}

function setCategoryLevel() {
    $btnYearBack.show();
    $btnCategoryBack.hide();
    $('#labelPieChartDetailPerMonth').html(_months[_indexSelectedMonth] + " " + $year.val());
    $('#labelBarChartDetailPerMonth').html(_months[_indexSelectedMonth] + " " + $year.val());
}

function setCategoryMonthLevel(){
    if (_indexSelectedMonth != -1)
        setCategoryLevel();
    else
        setYearLevel();
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

function createChartsPerYear() {
    setYearLevel();
    let values = [];
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/getMonthAmounts",
        data: {
            year: $year.val()
        },
        async: false,
        beforeSend: function (xhr) {
            _blockUI.block();
            createMonthBarChart(values);
        },
        success: function (items) {
            if (items.length > 0) {
                let totalMonth = 0;
                $.each(items, function (i, item) {
                    values.push([item.monthName, item.amount]);
                    totalMonth += _jsUtil.round(item.amount);
                });
                $totalMonth.html(accounting.formatMoney(totalMonth));

            }
        }, complete: function () {
            createMonthBarChart(values);
            createCategoryDetailCharts();
            _blockUI.unblock();
        }
    });
}

function createMonthBarChart(values) {
    c3.generate({
        bindto: '#divMonthBarChart',
        data: {
            columns: values,
            type: 'bar',
            onclick: function (d, i) {
                _indexSelectedMonth = _months.indexOf(d.id);
                setCategoryLevel();
                createCategoryDetailCharts();
            },
            labels: {
                format: function (v, id, i, j) {
                    return accounting.formatMoney(v);
                }
            }
        },
        tooltip: {
            grouped: false
        },
        axis: {
            y: {
                tick: {
                    format: d3.format("$,")
                }
            }
        },
        bar: {
            width: {
                ratio: 1
            }
        }
    });
}

function createCategoryDetailCharts() {
    let values = [];
    let totalMonthCat = 0;
    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findAmountsCategoryBy",
        data: {
            year: $year.val(),
            month: _indexSelectedMonth + 1
        },
        beforeSend: function (xhr) {
            _blockUI.block();
            $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            _dataCategoryDetails = [];
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    totalMonthCat += item.amount;
                    _dataCategoryDetails.push(item);
                    values.push([item.name, item.amount]);
                });
                $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
            createDetailPieChart(values, function (d, i) {
                let category = _dataCategoryDetails.find(function (category, index) {
                    if (category.name == d.id){
                        _indexSelectedCategoryDetail = index;
                        return true;
                    }
                        
                });
                setSubcategoryLevel();
                createSubcategoryDetailCharts(category.id);
            });
            createDetailBarChart(values);
        }
    });
}

function createDetailPieChart(values, func) {
    c3.generate({
        bindto: '#divDetailPieChart',
        data: {
            columns: values,
            type: 'pie',
            onclick: func
        }
    });
}

function createDetailBarChart(values) {
    c3.generate({
        bindto: '#divDetailBarChart',
        data: {
            columns: values,
            type: 'bar',
            labels: {
                format: function (v, id, i, j) {
                    return accounting.formatMoney(v);
                }
            }
        },
        axis: {
            rotated: true,
            y: {
                show: false,
                tick: {
                    format: d3.format("$,")
                }
            },
            x: {
                show: false
            }
        },
        bar: {
            width: {
                ratio: 1
            }
        }
    });
}

function createSubcategoryDetailCharts(idCategory) {
    let values = [];
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
            $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    totalMonthCat += item.amount;
                    values.push([item.name, item.amount]);
                });
                $totalMonthCategory.html(accounting.formatMoney(totalMonthCat));
            }
        }, complete: function () {
            _blockUI.unblock();
            createDetailPieChart(values, function (d, i) {});
            createDetailBarChart(values);
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