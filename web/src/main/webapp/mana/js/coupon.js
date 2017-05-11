/**
 * Created by huangzhangting on 16/12/6.
 */

/** 模板助手 */
template.helper('typeHelper', function(value){
    if(_typeList===undefined){
        return value;
    }
    var len = _typeList.length;
    for(var i=0; i<len; i++){
        if(value == _typeList[i].id){
            return _typeList[i].typeName;
        }
    }
    return value;
});
template.helper('statusHelper', function(value){
    var msg = value;
    switch (value){
        case 1: msg = '<i style="color: green;font-size: 13px;">发送成功</i>'; break;
        case 2: msg = '<i style="color: orangered;font-size: 13px;">发送失败</i>'; break;
        default: break;
    }
    return msg;
});


var _pageSize = 10;
var _pageIndex = 1;

$(document).ready(function(){

    $('#sendCouponBtn').click(function(){
        sendCoupon();
    });
    $('#returnCouponListBtn').click(function(){
        returnCouponList();
    });
    $('#confirmSendBtn').click(function(){
        confirmSend(this);
    });
    $('#exportExcelBtn').click(function(){
        exportExcel();
    });
    $('#refreshBtn').click(function(){
        getSendLogPage(_pageIndex);
    });

    //初始化短信模板
    initSmsTemplate();

    //初始化券类型
    initType();

    //获取分页数据，必须在初始化券类型之后做
    getSendLogPage(1);
}).on("mouseenter",".template-key",function(){
    var $this = $(this);
    LayerUtil.tips($this.data("title"),$this,4,10000);
}).on("mouseleave",".template-key",function(){
    layer.closeAll();
});

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
            getSendLogPage(p);
        }
    });
};

/* 组装查询参数 */
function getReqParams(page){
    var startDateStr = $('#searchStartDate').val();
    var endDateStr = $('#searchEndDate').val();
    if(startDateStr!='' && endDateStr!=''){
        if(startDateStr >= endDateStr){
            LayerUtil.msg('开始时间必须小于结束时间');
            return null;
        }
    }

    var searchPhone = MaNa.repSpace($('#searchPhone').val());
    if(searchPhone!='' && !MaNa.isMobile(searchPhone)){
        LayerUtil.msg('请输入正确的手机号');
        return null;
    }

    _pageIndex = page;
    var reqParam = {
        pageSize: _pageSize,
        pageIndex: _pageIndex
    };
    if(startDateStr!=''){
        reqParam.startDateStr = startDateStr;
    }
    if(endDateStr!=''){
        reqParam.endDateStr = endDateStr;
    }
    if(searchPhone!=''){
        reqParam.sendMobile = searchPhone;
    }
    var sendStatus = $('#searchStatus').val();
    if(sendStatus > -1){
        reqParam.sendStatus = sendStatus;
    }

    return reqParam;
}

/* 分页查询记录 */
function getSendLogPage(page){
    var reqParam = getReqParams(page);
    if(reqParam==null){
        return;
    }

    var index = LayerUtil.load();

    Ajax.get({
        url: '/coupon/getSendLogPage',
        data: reqParam,
        success: function(result){
            layer.close(index);
            var contentDiv = $('.coupon-list-content');
            if(result.success){
                var html = template('sendLogTableTemplate', result);
                contentDiv.html(html);

                //初始化分页组件
                pageInit(page, result.total);

                //$('.template-key').tooltip();
            }else{
                contentDiv.html('<label>'+result.message+'</label>');
            }
        }
    });
}

/* 初始化券类型下拉框 */
var _typeList;
function initType(){
    Ajax.get({
        url: '/coupon/getAllTypes',
        async: false,
        success: function(result){
            if(result.success){
                _typeList = result.data;
                var html = template('typeTemplate', result);
                $('#typeSelect').html(html);
            }
        }
    });
}

/* 初始化短信模板 */
function initSmsTemplate(){
    Ajax.get({
        url: '/sms/getAllTemplate',
        success: function(result){
            var smsTemplateTable = $('#smsTemplateTable');
            if(result.success){
                var html = template('smsTemplateTableTemplate', result);
                smsTemplateTable.html(html);
            }else{
                smsTemplateTable.html('<tr><td colspan="3" style="color: orangered;">未配置短信模板</td></tr>');
            }
        }
    });
}

/* 发送优惠券 */
function sendCoupon(){
    $('#couponListDiv').addClass('hidden');
    $('#sendCouponDiv').removeClass('hidden');
    resetSendCouponInput();
}

/* 返回列表页面 */
function returnCouponList(){
    $('#couponListDiv').removeClass('hidden');
    $('#sendCouponDiv').addClass('hidden');

}

/* 确认发送 */
function confirmSend(el){
    var checkFlag = true;

    var templateRadio = $(':radio:checked');
    if(templateRadio.length==0){
        LayerUtil.tipsMore('请选择短信模板', $('#smsTemplateTable'));
        checkFlag = false;
    }

    var phoneText = $('#phoneText');
    var phone = MaNa.repSpace(phoneText.val());
    if(''==phone){
        LayerUtil.tipsMore('请输入手机号', phoneText);
        checkFlag = false;
    }

    var sendNum = $('#sendNum');
    var num = MaNa.repSpace(sendNum.val());
    if(!MaNa.isPositiveInt(num)){
        LayerUtil.tipsMore('请输入正确的数量', sendNum);
        checkFlag = false;
    }

    var typeSelect = $('#typeSelect');
    var type = typeSelect.val();
    if(type===null || type===undefined || type<1){
        LayerUtil.tipsMore('请选择券类型', typeSelect);
        checkFlag = false;
    }

    if(!checkFlag){
        return;
    }
    var reqParam = {
        mobiles: phone,
        couponType: type,
        couponNum: num,
        smsTemplateKey: templateRadio.val()
    };

    LayerUtil.confirm('您确认要发送吗？', function(){
        var index = LayerUtil.load(200000);
        Ajax.post({
            url: '/coupon/sendCoupon',
            data: reqParam,
            beforeSend: function(request){
                request.setRequestHeader("SEND_COUPON_KEY", $('#SEND_COUPON_KEY').val());
            },
            success: function(result){
                layer.close(index);

                if(result.success){
                    LayerUtil.msg('发送成功');
                    resetSendCouponInput();
                }else{
                    LayerUtil.msg(result.message);
                }
            }
        });
    });
}
/* 重置发送页面输入信息 */
function resetSendCouponInput(){
    $('#phoneText').val('');
    $('#sendNum').val(1);
}

/* 导出excel */
function exportExcel(){
    LayerUtil.alert({
        content: $('#exportTemplate').html(),
        title: '导出excel',
        btnText: '确定',
        showIcon: false,
        callBack: function(index){
            var startDateInput = $('#startDateStr');
            var startDateStr = startDateInput.val();
            var endDateStr = $('#endDateStr').val();
            if(startDateStr!='' && endDateStr!=''){
                if(startDateStr >= endDateStr){
                    LayerUtil.tips('开始时间必须小于结束时间', startDateInput);
                    return;
                }
            }

            location.href = '/coupon/exportSendLog?startDateStr='+startDateStr+'&endDateStr='+endDateStr;

            LayerUtil.msg('数据处理中，请稍等');
        }
    });
}


