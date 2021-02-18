let $depots, $articles, $unities, $stockValue;
let __divGraphPercentByCategory, __divGraphUnitiesByCategory, __divGraphValuesByCategory;
let $totalUnitiesByCategory, $totalValueByCategory;
let $btnRefresh;

$(document).ready(function () {
    initComponents();
    initEvents();
    loadData();
});

function initComponents(){
    $depots = $('#depots');
    $articles = $('#articles');
    $unities = $('#unities');
    $stockValue = $('#stockValue');
    __divGraphPercentByCategory = new mx.jeegox.jChart($('#divGraphPercentByCategory'));
    __divGraphUnitiesByCategory = new mx.jeegox.jChart($('#divGraphUnitiesByCategory'));
    __divGraphValuesByCategory = new mx.jeegox.jChart($('#divGraphValuesByCategory'));
    $totalUnitiesByCategory = $('#totalUnitiesByCategory');
    $totalValueByCategory = $('#totalValueByCategory');
    $btnRefresh = $('#btnRefresh');
}

function initEvents(){
    $btnRefresh.click(btnRefreshOnClick);
}

function btnRefreshOnClick(){
    loadData();
}

function loadData(){
    countDepots();
    countArticles();
    countArticleUnities();
    getTotalStockValue();
    getTotalStockGroupedByCategory();
    getTotalValueStockGroupedByCategory();
}

function countDepots() {
    $.ajax({
        type: "POST",
        url: $.PATH + "inventoryAnalysis/countDepots",
        beforeSend: function (xhr) {
            _blockUI.block();
            $depots.html("0");
        },
        success: function (response) {
            $depots.html(_jsUtil.numberWithCommas(response));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function countArticles() {
    $.ajax({
        type: "POST",
        url: $.PATH + "inventoryAnalysis/countArticles",
        beforeSend: function (xhr) {
            _blockUI.block();
            $articles.html("0");
        },
        success: function (response) {
            $articles.html(_jsUtil.numberWithCommas(response));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function countArticleUnities() {
    $.ajax({
        type: "POST",
        url: $.PATH + "inventoryAnalysis/countArticleUnities",
        beforeSend: function (xhr) {
            _blockUI.block();
            $unities.html("0");
        },
        success: function (response) {
            $unities.html(_jsUtil.numberWithCommas(response));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function getTotalStockValue() {
    $.ajax({
        type: "POST",
        url: $.PATH + "inventoryAnalysis/getTotalStockValue",
        beforeSend: function (xhr) {
            _blockUI.block();
            $stockValue.html("0");
        },
        success: function (response) {
            $stockValue.html(accounting.formatMoney(response));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function getTotalStockGroupedByCategory() {
    $.ajax({
        type: "POST",
        url: $.PATH + "inventoryAnalysis/getTotalStockGroupedByCategory",
        beforeSend: function (xhr) {
            _blockUI.block();
            __divGraphPercentByCategory.clearPie();
            __divGraphUnitiesByCategory.clearHorizontal();
        },
        success: function (items) {
            let labels = [];
            let data = [];
            let dataPercent = [];
            let total = 0;
            $.each(items, function (i, item) {
                labels.push(item.name);
                total += (item.stockTypeIn - item.stockTypeOut);
                data.push(item.stockTypeIn - item.stockTypeOut);
            });

            $.each(items, function (i, item) {
                let percent = _jsUtil.round(((item.stockTypeIn - item.stockTypeOut) / total) * 100);
                dataPercent.push(percent);
            });
            __divGraphPercentByCategory.buildPie(dataPercent, labels);
            __divGraphUnitiesByCategory.buildHorizontal(data, labels);
            $totalUnitiesByCategory.html(_jsUtil.numberWithCommas(total));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}

function getTotalValueStockGroupedByCategory() {
    $.ajax({
        type: "POST",
        url: $.PATH + "inventoryAnalysis/getTotalValueStockGroupedByCategory",
        beforeSend: function (xhr) {
            _blockUI.block();
            __divGraphValuesByCategory.clearHorizontal();
        },
        success: function (items) {
            let labels = [];
            let data = [];
            let total = 0;
            $.each(items, function (i, item) {
                labels.push(item.name);
                total += (item.stockTypeIn - item.stockTypeOut);
                data.push(item.stockTypeIn - item.stockTypeOut);
            });
            __divGraphValuesByCategory.buildHorizontal(data, labels);
            $totalValueByCategory.html(accounting.formatMoney(total));
        }, complete: function () {
            _blockUI.unblock();
        }
    });
}