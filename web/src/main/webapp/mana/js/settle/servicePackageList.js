/**
 * Created by huage on 2017/2/22.
 */
var _pageSize = 10;
var _pageNumber = 1;
var flag_two = true;
$(document).ready(function () {
    initLoad();
    clickEvent();
    inputEvent();
}).on('click','.jump-add',function () {

    location.href = "/settle/shopRule/servicePackageManage";

});

// 进页面加载
function initLoad() {
    var url = '/insurance/getAllCompanyList',
        tpl = 'insuranceCompanyTemp',
        id = '#insureCompanyId';
    settleCommon.insureCompany(url,tpl,id);
    //省份
    var data =  {regionParentCode:'000000'},
        urlT = '/insurance/getRegionList',
        IdName = '#province',
        msg = '省',
        tplT = 'insuranceCityTemp';
    settleCommon.getCityData(urlT,data,IdName,msg,tplT);
    //进页面列表展示列表
    queryBountyScaleList(1);
}


//页面点击事件
function clickEvent() {
    // 点击省份掉市的接口
    $(document).on('change','#province',function () {
        var $this = $(this),
            val = $this.val();
        if(!val){
            $('#city').html('<option value="">请选择市</option>');
            return false;
        }else{
            var data =  {regionParentCode:val},
                url = '/insurance/getRegionList',
                IdName = '#city',
                msg = '市',
                tpl = 'insuranceCityTemp';
            settleCommon.getCityData(url,data,IdName,msg,tpl)
        }
    });

    //点击门店名称模糊匹配的其中一条
    $(document).on('click','.storeResult li',function () {
        var $this = $(this);
        $this.parent('ul').siblings('input').val($this.text()).data('id',$this.data('id'));
        $this.parent('ul').hide();
    });

    //点击查询
    $(document).on('click','.search-button',function () {
        var modeV = $('#mode option:selected').val();
        if(!modeV){
            flag_two = true;
            queryBountyScaleList(1);
        }else{
            queryBountyScaleList(1);
        }
    });

    //点击删除
    $(document).on('click','.remove-tr',function () {
        var thisId = $(this).parents('tr').data('id');
        LayerUtil.confirm("确定要删除服务包模式设置吗?",function () {
            Ajax.get({
                url:'/settle/shopRule/deleteShopRule',
                data:{
                    id:thisId
                },
                success:function (result) {
                    if(result.success){
                        LayerUtil.msgFun('删除成功', function () {
                            location.reload();
                        })
                    }else{
                        layer.msg(result.message);
                    }
                }
            })
        });
    })
}


function inputEvent() {
    $('#storeName').bind('input propertychange',function () {
        var modeVal = $('#mode option:selected').val();
        var val = $.trim($(this).val());
        if (val){
            Ajax.get({
                url:'/ucShop/getSimpleShopList',
                data:{
                    cooperationMode:modeVal,
                    queryStr:val
                },
                success:function (result) {
                    if(result.success){
                        var data = result.list,
                            string = '';
                        for (var i = 0;i<data.length;i++){
                            string += '<li data-id='+ data[i].id + '>'+data[i].companyName + ' ' + data[i].defaultAccountMobile + '</li>'
                        }
                        $('.storeResult').html(string).show();
                    }else{
                        $('.storeResult').html(result.message).show();
                    }
                }
            })
        }else{
            $('.storeResult').html('').hide();
            $('#storeName').data('id','');
        }
    })
}

function queryBountyScaleList(pageNumber) {
    var index = LayerUtil.load();
    var data  = settleCommon.getData();
    var modeVal = $('#mode option:selected').val();
    if (!modeVal){
        data['cooperationModes[0]'] = 2;
        data['cooperationModes[1]'] = 3;
    }
    data['pageSize'] = _pageSize;
    data['pageIndex'] = pageNumber;
    Ajax.get({
        url:'/settle/shopRule/queryShopRulePage',
        data:data,
        success:function (result) {
            layer.close(index);
            if (result.success){
                $('.fenye').show();
                var list = result.list;
                var html = tableList(list);
                $('.mana-body').html(html);
                if(pageNumber == _pageNumber){
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(result.total/_pageSize)},function (curr) {
                        //分页查询
                        queryBountyScaleList(curr);
                    });
                }
                initListBackground();
            }else{
                var body_html_none = '<tr><td class="ver-align" colspan="14">暂无数据</td></tr>';
                $('.mana-body').html(body_html_none);
            }
        }
    })
}


function tableList(list) {
    // 这个表格就是这么恶心，别嫌弃
    var body_html = '';
    for (var i = 0; i<list.length;i++){
        var flag = true;
        body_html += '<tr data-id="'+list[i].id+'" class="'+i+'">';
        var applyRangeHtml = '';
        if (list[i].applyRange == 1){
            applyRangeHtml += '<td class="ver-align" rowspan="'+list[i].rowSpan+'">'+list[i].applyRangeString+'</td>'+'<td class="ver-align" rowspan="'+list[i].rowSpan+'"></td>'
        }else if(list[i].applyRange == 2){
            applyRangeHtml += '<td class="ver-align" rowspan="'+list[i].rowSpan+'"></td>'+'<td class="ver-align" rowspan="'+list[i].rowSpan+'">'+list[i].applyRangeString+'</td>'
        }
        body_html += '<td class="ver-align" rowspan="'+list[i].rowSpan+'">'+list[i].insuranceCompanyName+'</td>'+applyRangeHtml+'<td class="ver-align" rowspan="'+list[i].rowSpan+'">'+list[i].cooperationModeName+'</td>';
        for(var j=0;j<list[i].rebateStandardVOs.length;j++){
            var StandardVOs = list[i].rebateStandardVOs[j];
            if(j>0){
                body_html += '<tr class="'+i+'">'
            }
            body_html += '<td class="ver-align" rowspan="'+StandardVOs.rowSpan+'">'+StandardVOs.rebateStandardName+'</td>'+
                '<td class="ver-align" rowspan="'+StandardVOs.rowSpan+'">'+StandardVOs.calculateModeName+'</td>'+
                '<td class="ver-align" rowspan="'+StandardVOs.rowSpan+'">'+StandardVOs.settleConditionName+'</td>'+
                '<td class="ver-align" rowspan="'+StandardVOs.rowSpan+'">'+StandardVOs.fundTypeName+'</td>'+
                '<td class="ver-align" rowspan="'+StandardVOs.rowSpan+'">'+StandardVOs.rebateTypeName+'</td>';
            for (var k = 0;k<StandardVOs.configBasicVOList.length;k++){
                var BasicVOList = StandardVOs.configBasicVOList[k];
                if(k>0){
                    body_html += '<tr class="'+i+'">'
                }
                body_html += '<td class="ver-align" rowspan="'+BasicVOList.rowSpan+'">'+settleCommon.changeDate(BasicVOList.startTime)+'至'+settleCommon.changeDate(BasicVOList.endTime,'以后')+'</td>';
                var operate = '<td class="ver-align" rowspan="'+list[i].rowSpan+'">' +
                    '<a class="modify" href="/settle/shopRule/servicePackageManage?id='+list[i].id+'">修改</a>' +
                    '<a href="javascript:void 0;" class="remove-tr">删除</a>' +
                    '</td>';
                if(BasicVOList.settleConfigBasicVO){
                    body_html +='<td class="ver-align" rowspan="'+BasicVOList.rowSpan+'">'+ BasicVOList.settleConfigBasicVO.groupName+'</td>';
                    var itemVOList = BasicVOList.settleConfigBasicVO.itemVOList;
                    for(var n = 0; n<itemVOList.length;n++){
                        if(n>0){
                            body_html += '<tr class="'+i+'">';
                        }
                        var itemVOListH = '';
                        if (itemVOList[n].itemEndValue){
                            itemVOListH += '<td class="ver-align">' + itemVOList[n].itemStartValue + '~' + itemVOList[n].itemEndValue + '</td>'
                        }else{
                            itemVOListH += '<td class="ver-align">'+itemVOList[n].itemStartValue+'及以上</td>'
                        }
                        body_html += itemVOListH+'<td class="ver-align">'+itemVOList[n].itemRate+'</td>';
                        if(n>0){
                            body_html += '</tr>';
                        } else{
                            if (flag){
                                body_html += operate;
                                flag = false;
                            }
                        }
                    }
                }else{
                    if (flag){
                        body_html+='<td></td><td></td><td></td>'+operate;
                        flag = false;
                    }else{
                        body_html+='<td></td><td></td><td></td>'
                    }
                }
                if(k>0){
                    body_html += '</tr>'
                }
            }
            if(j>0){
                body_html += '</tr>'
            }
        }
        body_html += '</tr>';
    }
    return body_html;
}

//改变列表的背景色
function initListBackground() {
    var table_tr = $('.mana-table tbody tr');
    table_tr.each(function () {
        var module = parseInt($(this).prop('class'),10)%2;
        if(module == 0){
            $(this).css('background','#f9f9f9');
        }else{
            $(this).css('background','#fff');
        }
    })
}