/**
 * Created by zhouheng on 16/12/26.
 */
var _pageSize = 10;
var _pageNumber = 1;


$(document).ready(function () {

    //初始化搜索框聚焦
    $('.js-input').focus();

    //初始化查询
    initQuery(_pageNumber);

    //搜索框回车键
    $('.js-input').bind('keypress', function (event) {
        if (event.keyCode == 13) {
            $('.doSearch').trigger('click');
        }
    });

}).on('click', '.doSearch', function () {

    initQuery(_pageNumber);

});

//初始化查询
function initQuery(pageNumber) {
    var index = LayerUtil.load();
    //$('.carOwnerInfoList tbody').html("");
    var queryStr = $('.js-input').val();
    var reqParam = {
        queryStr: queryStr,
        pageSize: _pageSize,
        pageNumber: pageNumber
    };
    Ajax.get({
            url: '/customer/searchCustomer',
            data: reqParam,
            success: function (result) {
            //模板渲染
            var offerSet = (pageNumber - 1) * _pageSize;
            var list = result.list;
            if(list != null && list.length > 0){
                $('.fenye').show();
                $('.carOwnerInfoList').removeClass("car-owner-Info-set");
                if(pageNumber == _pageNumber){
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(result.total/_pageSize)},function (curr) {
                        //分页查询
                        initQuery(curr);
                    });
                }
                $('.carOwnerInfoList').html(template('carOwnerInfoListTemplate', {
                    list: list,
                    offerSet: offerSet + 1
                }));
            }else{
                $('.carOwnerInfoList').html("暂无数据");
                $('.fenye').hide();
                $('.carOwnerInfoList').addClass("car-owner-Info-set")
            }

            layer.close(index);

            //渲染成功后初始化客户详细查询链接
            $(document).on('click', '.js-customer-detail', function () {

                var vehicleId = $(this).data('id');
                window.location.href = '/customer/detailPage?vehicleId=' + vehicleId;
            });
        }
    })
}