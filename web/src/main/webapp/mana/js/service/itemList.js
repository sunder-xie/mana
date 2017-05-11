/**
 * Created by zhouheng on 17/2/15.
 */
var _pageNumber = 1;
var _pageSize = 15;
$(document).ready(function () {

    initQuery(1);

}).on('click', '.search-button', function () {

    initQuery(1);

}).on('click','.js-change-status',function (e) {
    
    changeStatus(e);
    
}).on('click','.js-deleted',function (e) {

    deleteItem(e);

}).on('click','.jump-add',function () {
    
    location.href = "/insuranceItem/addItemInit"
    
});



function initQuery(pageNumber) {
    var index = LayerUtil.load();
    var itemName = $('.search-button').prev().val();
    var reqParam = {
        itemName: itemName,
        pageNum: pageNumber,
        pageSize: _pageSize
    };
    Ajax.get({
        url: "/insuranceItem/getServiceItemList",
        data: reqParam,
        success: function (result) {
            layer.close(index);
            if (result.success) {
                $('.fenye').show();
                $('.rebate-list').html("");
                $('.rebate-list').html(template('manaTableTemplate', {dataList: result.list}));
                if (pageNumber == _pageNumber) {
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(result.total / _pageSize)
                    }, function (curr) {
                        //分页查询
                        initQuery(curr);
                    });
                }
            } else {
                layer.msg(result.message);
                $('.rebate-list').html("暂无数据");
                $('.fenye').hide();
            }
        }
    });
}

function changeStatus(e) {
    var id = $(e.target).data("id");
    var status = $(e.target).data("status");
    var reqParam = {
        id:id,
        status:status
    };
    Ajax.get({
        url:"/insuranceItem/updateServiceItemStatus",
        data:reqParam,
        success:function (result) {
            if(result.success){
                if(status == 0){
                    $(e.target).text("上架");
                    $(e.target).parent().prev().text("下架");
                    $(e.target).data("status",1);
                    layer.msg("状态更新成功");
                }else{
                    $(e.target).text("下架");
                    $(e.target).parent().prev().text("上架");
                    $(e.target).data("status",0);
                    layer.msg("状态更新成功");
                }
            }else{
                layer.msg(result.message);
            }
        }
    });
}

function deleteItem(e) {
    var id = $(e.target).data("id");
    // var itemName = $(e.target).parents("tr").find("td:first")[0].innerText;
    LayerUtil.confirm('是否确定删除服务项目配置吗?',function () {
        var index = LayerUtil.load();
        Ajax.get({
            url:"/insuranceItem/deleteServiceItem",
            data:{
                id:id
            },
            success:function (result) {
                layer.close(index);
                if(result.success){
                    layer.msg("删除成功");
                    location.reload();
                }else{
                    LayerUtil.alert({content:result.message,btnText:'我知道了'});
                }
            }
        });
    });
}
