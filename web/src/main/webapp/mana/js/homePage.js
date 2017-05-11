/**
 * Created by zhouheng on 16/12/26.
 */
var _pageSize = 3;
var _pageNumber = 1;

$(document).ready(function () {
    
    //初始化搜索框聚焦
    $('.js-input').focus();
    
    //初始化查询
    initQuery(_pageNumber);

}).on('click','.doSearch',function(){
    
    initQuery(_pageNumber);
    
});

//初始化查询
function initQuery(pageNumber) {
    var index= LayerUtil.load();
    $('.carOwnerInfoList tbody').html("");
    var queryStr = $('.js-input').val();
    var reqParam = {
        queryStr: queryStr,
        pageSize: _pageSize,
        pageNumber:pageNumber
    };
    Ajax.get({
        url:'/customer/searchCustomer',
        data: reqParam,
        success:function(result){
            //模板渲染
            var offerSet = (pageNumber-1)*_pageSize;
            var list = result.list;
            $('.carOwnerInfoList tbody').html(template('carOwnerInfoListTemplate',{list:list,offerSet:offerSet+1}));

            //初始化分页组件
            pageInit(pageNumber, result.total);
            layer.close(index);

            //渲染成功后初始化客户详细查询链接
            $(document).on('click','.js-customer-detail',function(){

                var vehicleId = $(this).data('id');
                window.location.href = '/customer/detailPage?vehicleId='+vehicleId;
            });

        }
    })
}

//分页
var pageInit = function (curr, total) {
    JqPage.init({
        //总记录数
        itemSize: total,
        //每页记录数
        pageSize: _pageSize,
        //当前页
        current: curr || 1,
        //点击分页后的回调
        backFn: function (p) {
            initQuery(p);
        }
    });
};