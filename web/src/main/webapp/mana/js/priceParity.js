/**
 * Created by huangzhangting on 16/11/30.
 */

/** 模板助手 */
template.helper('feeHelper',function(value){
    if(value > 0){
        return '￥'+value;
    }
    return '<i style="color: orangered;font-size: 12px;">暂无数据</i>';
});
template.helper('remarkHelper',function(value){
    if(value==''){
        return '';
    }
    return '<i style="color: green;font-size: 12px;"> ('+value+')</i>';
});

$(document).ready(function() {

    $('#searchBtn').click(function(){
        searchData();
    });

    var input = $('#searchInput');
    input.focus();
    input.bind('keypress', function (event) {
        if (event.keyCode == 13) {
            $('#searchBtn').trigger('click');
        }
    });

});

var _dataList;
/** 查询保单列表 */
function searchData(){
    var input = $('#searchInput');
    var searchVal = MaNa.repSpace(input.val());
    input.val(searchVal);
    if(''==searchVal){
        LayerUtil.tips('请输入车牌号', input, 3);
        return;
    }

    var index = LayerUtil.load();
    Ajax.get({
        url: '/priceParity/getVOList',
        data: {vehicleSn: searchVal},
        success: function(result){
            var content = $('.ino-content');
            if(result.success){
                _dataList = result.data;

                content.html(template('inoTemplate', result));
                var insureList = content.find('.insure-info');
                insureList.click(function(){
                    getInsuranceDetail(this);
                });
                layer.close(index);
                getInsuranceDetail(insureList.get(0));

            }else{
                content.html('<label>暂无数据</label>');
                $('#detailDiv').empty();
                layer.close(index);
            }
        }
    });
}

/** 查询详情 */
function getInsuranceDetail(el){
    var index = LayerUtil.load();

    $('.ino-content .selected').removeClass('selected');
    $(el).addClass('selected');

    var id = $(el).data('id');
    var len = _dataList.length;
    for(var i=0; i<len; i++){
        var data = _dataList[i];
        if(id==data.insuranceBasicId){
            $('#detailDiv').html(template('detailTemplate', data.basicBO));

            Ajax.get({
                url: '/priceParity/getItemList',
                data: {insuranceBasicId: id},
                success: function(result){
                    var content = $('.detail-content');
                    if(result.success){
                        var data = result.data;
                        var vciHtml = template('detailItemTemplate', {
                            title: '商业险',
                            itemSize: data.vciItemSize,
                            itemList: data.vciItemList
                        });

                        var tciHtml = template('detailItemTemplate', {
                            title: '交强险',
                            itemSize: data.tciItemSize,
                            itemList: data.tciItemList
                        });

                        content.html(template('detailContentTemplate', data));

                        var feeAmountTr = $('#feeAmountTr');
                        feeAmountTr.before(vciHtml);
                        feeAmountTr.before(tciHtml);

                    }else{
                        content.html('<label>暂无数据</label>');

                    }

                    layer.close(index);
                }
            });
            break;
        }
    }
}
