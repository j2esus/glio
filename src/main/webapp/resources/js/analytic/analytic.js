var $btnRefresh,
    $dataTableGral,
    $btnRefreshSub;
    

var $divCategory,
    $divSubcategory;
     
var _indexSelectedCategory = -1, _dataCategory = [];

var $dataTableSub;


$(document).ready(function () {
    initComponents();
    initPanels();
    initEvents();
    findDataCategory();
});

function initComponents() {
    $btnRefresh = $('#btnRefresh');
    $btnRefreshSub = $('#btnRefreshSub');
    $dataTableGral = $('#dataTableGral');
    
    $dataTableSub = $('#dataTableSub');
    
    $divCategory = $('#divCategory');
    $divSubcategory = $('#divSubcategory');
}

function initPanels() {
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
}

function onClickBtnRefresh() {
    findDataCategory();
}

function onClickBtnRefreshSub(){
    showSubcategoryData();
}

function findDataCategory() {
    var data = [];
    var labels = [];
    var total = 0;
    
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
            } else {
                _notify.show("La consulta no produjo resultados.", "danger");
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function addRowToTable(item, table, percent) {
    var noFila = parseInt(table.find("tbody").eq(0).find("tr").length);

    var fila = "";
    fila += "<tr data-meta-row='" + noFila + "'>";
    fila += "<td>" + item.name + "</td>";
    fila += "<td  align='right'>" + accounting.formatMoney(item.amount) + "</td>";
    fila += "<td  align='right'>" + percent + " %</td>";
    fila += "</tr>";
    table.append(fila);
}

function buildChartCategory(data, labels) {

    $('#divGraphCategory').html('');
    $('#divGraphCategory').html('<canvas id="canvGraphCategory" style="width: 100%"></canvas>');

    var myChart = document.getElementById("canvGraphCategory");
    var ctxMix = myChart.getContext('2d');
    
    var config = {
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
    var colors = [];
    for(i = 0; i < length;i++){
        var random = '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
        colors.push(random);
    }
    
    return colors; 
};

function showSubcategoryData(){
    var item = _dataCategory[_indexSelectedCategory];
    $('#titleModalNew').html(item.name);
    $divCategory.css("display", "none");
    $divSubcategory.css("display", "block");
    findDataSubcategory(item.id);
}

function findDataSubcategory(idCategory) {
    var data = [];
    var labels = [];
    var total = 0;
    
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
                
            }
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function buildChartSubcategory(data, labels) {

    $('#divGraphSubcategory').html('');
    $('#divGraphSubcategory').html('<canvas id="canvGraphSubcategory" style="width: 100%"></canvas>');

    var myChart = document.getElementById("canvGraphSubcategory");
    var ctxMix = myChart.getContext('2d');
    
    var config = {
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