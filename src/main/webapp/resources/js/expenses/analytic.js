let $btnYearBack, $idCategoryF, $idSubcategoryF, $btnCategoryBack;

let $btnRefresh, $year, $totalMonth, $totalMonthCategory;

let _indexSelectedMonth = -1, _indexSelectedCategoryDetail = -1;

let _dataCategoryDetails = [];

let _months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

$(document).ready(function () {
    initComponents();
    initEvents();
    createGeneralCategoryPieChart();
    createChartsPerYear();
    createLineChartYearComparator();
});

function initComponents() {
    $btnYearBack = $('#btnYearBack');
    $btnCategoryBack = $('#btnCategoryBack');
    $idCategoryF = $('#idCategoryF');
    $idSubcategoryF = $('#idSubcategoryF');

    $btnRefresh = $('#btnRefresh');
    $year = $('#year');
    $totalMonth = $('#totalMonth');
    $totalMonthCategory = $('#totalMonthCategory');

    $btnYearBack.hide();
    $btnCategoryBack.hide();
}

function initEvents() {
    $btnRefresh.click(onClickBtnRefresh);
    $year.change(onChangeYear);
    $btnYearBack.click(onClickBtnYearBack);
    $btnCategoryBack.click(onClickBtnCategoryBack);

    $idCategoryF.change(function(){
        findSubcategories($idCategoryF.val(), $idSubcategoryF, "--Subcategoría");
        createLineChartYearComparator();
    });

    $idSubcategoryF.change(function(){
        createLineChartYearComparator();
    });
}

function onClickBtnRefresh() {
    createGeneralCategoryPieChart();
    createChartsPerYear();
    createLineChartYearComparator();
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

function createGeneralCategoryPieChart() {
    let values = [];
    let labels = [];

    $.ajax({
        type: "POST",
        url: $.PATH + "analytic/findDataCategory",
        beforeSend: function (xhr) {
            _blockUI.block();
        },
        success: function (items) {
            if (items.length > 0) {
                $.each(items, function (i, item) {
                    labels.push(item.name);
                    values.push([item.name, item.amount]);
                });
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
            createGeneralPieChart(values);
        }
    });
}

function createGeneralPieChart(values){
    c3.generate({
        bindto: '#divGeneralCategoryPieChart',
        data: {
            columns: values,
            type: 'pie'
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

function createLineChartYearComparator() {
    let values = [];
    let totalPerYear = [];

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
        },
        success: function (items) {
            $.each(items, function (year, item) {
                let amounts = item.map(function (i) {
                    return i.amount;
                });
                let cadena = year+";"+amounts.join(";");
                values.push(cadena.split(";"));
                
                totalPerYear.push([year, 
                    amounts.reduce(function (a, b) {
                        return a + b;
                    })
                ]);
            });
        }, complete: function () {
            _blockUI.unblock();
            createLineChartComparator(values);
            createBarChartComparator(totalPerYear);
        }
    });
}

function createLineChartComparator(values){
    c3.generate({
        bindto: '#divComparativeYearLineChart',
        data: {
            columns: values,
            type: 'spline'
        },
        axis: {
            y: {
                tick: {
                    format: d3.format("$,")
                }
            },
            x: {
                type: 'category',
                categories: _months
            }
        }
    });
}

function createBarChartComparator(values) {
    c3.generate({
        bindto: '#divComparativeYearBarChart',
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