/**
 * Created by zhouheng on 17/1/18.
 */
var _pageSize = 10;
var _pageNumber = 1;
$(document).ready(function(){

    //初始化保险公司下拉框
    initInsureCompanySelect();

    queryConfigBasicList(1);
    
}).on('click','.search-button',function(){

    queryConfigBasicList(1);

}).on('click','.js-del',function(e){

    var dom = e.target;
    var id = $(dom).data("id");
    LayerUtil.confirm('是否确定删除返点比例设置吗?',function () {
        var action = '/settle/deleteSettleConfig';
        Ajax.get({
            url: action,
            data: {basicId:id},
            success: function (result) {
                if(result.success){
                    LayerUtil.msgFun('删除成功', function () {
                        location.reload();
                    })
                }else{
                    layer.msg(result.message);
                }
            }
        });
    });
}).on('click','.jump-add',function () {

    location.href = "/settle/configBasicAdd";

});


/**
 * 获取所有保险公司数据并初始化下拉框
 */
function initInsureCompanySelect() {

    var action = '/insurance/getAllCompanyList';
    Ajax.get({
        url: action,
        data: {},
        success: function (result) {
            var companyList = result.data;

            var _h = '<option value="">请选择</option>';
            for (var i = 0; i < companyList.length; i++) {
                _h += '<option{2} value="{0}">{1}</option>'.format(companyList[i].id, companyList[i].companyName, i == companyList[i].id ? ' selected' : '');
            }
            $('#insureCompanyId').html(_h);
        }
    });
    //重写
    String.prototype.format = function () {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };
}

function queryConfigBasicList(pageNumber){
    var action = '/settle/getBasicBOPagingList';
    var reqParam = {
        insureCompanyId:$('#insureCompanyId option:selected').val(),
        cooperationMode:$('#cooperationMode option:selected').val(),
        insuranceType : $('#insuranceType option:selected').val(),
        calculateMode : $('#calculateMode option:selected').val(),
        pageSize:_pageSize,
        pageNumber:pageNumber
    };
    var index = LayerUtil.load();
    Ajax.get({
        url: action,
        data: reqParam,
        success: function (result) {
            layer.close(index);
            var list = result.list;
            if(list != null && list.length > 0){
                $('.fenye').show();
                $('.mana-table').html("");
                $('.rebate-list').removeClass("car-owner-Info-set");
                $('.mana-table').append(template('configBasicListTemplate',{list:result.list}));
                if (pageNumber == _pageNumber) {
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(result.total/_pageSize)},function (curr) {
                        //分页查询
                        queryConfigBasicList(curr);
                    });
                }
                changeBackground();
            }else{
                layer.msg(result.message);
                $('.mana-table').html("暂无数据");
                $('.fenye').hide();
                $('.rebate-list').addClass("car-owner-Info-set");
            }
        }
    });
}

//鼠标移入移出列表背景变色
function changeBackground() {
    $('.mana-table tbody tr').hover(function () {
        $(this).addClass('bColor-green').find('tr').addClass('bColor-green')

    },function()
    {
        $(this).removeClass('bColor-green').find('tr').removeClass('bColor-green')
    })
}
