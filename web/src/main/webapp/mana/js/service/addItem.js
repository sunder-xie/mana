/**
 * Created by zhouheng on 17/2/15.
 */
$(document).ready(function () {
    //初始化checkbox
    initCheckBox();

    initQueryGoods();

    UE.getEditor('suitableModelDescription');
    UE.getEditor('contentDescription');
    UE.getEditor('workDescription');
    
}).on('click','.js-save-goods',function(){
    //页面保存商品
    saveGoods();

}).on('click','.js-save-video',function(){
    //页面保存视频信息
    saveVideo();

}).on('click','input[name="matchModel"]',function () {
    //匹配车型
    matchModel();

}).on('click','.add-one-button',function () {
    //新增一条
    addOne();

}).on('click','.subtract-one-button',function () {
    //减少一条
    subtractOne();

}).on('click','.sure-button',function () {
    
    sureBtn();
    
}).on('click','input[name="multiplyItem"]',function () {

    var isMultiplyItem = $('input[name="multiplyItem"]').is(":checked");
    if(isMultiplyItem){
        $('.num-input').val(0.5);
    }else{
        $('.num-input').val(1);
    }

}).on('click','.cancel-button',function () {
    
    location.href = "/insuranceItem/itemListInit";
    
});

function checkGoodsSn(back) {
    var goodsSn = $('.js-code-input').val();
    var action = "/goods/getGoodsConvert";
    var ok = false;
    Ajax.get({
        url: action,
        data: {
            q: goodsSn
        },
        success: function (result) {
            if (result.success) {
                var data = result.data;
                if (data.length == 1) {
                    var sn = data[0].goodsSn;
                    if (sn != goodsSn) {
                        layer.msg("请确认商品编码是否正确!");
                    } else {
                        ok = true;
                    }

                } else {
                    layer.msg("请确认商品编码是否正确!");
                }
            } else {
                layer.msg(result.message);
            }
            back(ok);
        }
    })
}

//初始化查询商品编码信息
function initQueryGoods(){
    var $sug = $('.mdsug');
    $('.js-code-input').bind('input propertychange',function () {
        var goodsSn = $('.js-code-input').val();
        if(goodsSn != null && goodsSn !=""){
            var action = "/goods/getGoodsConvert";
            Ajax.get({
                url:action,
                data:{
                    q:goodsSn
                },
                success:function (result) {
                    if(result.success){
                        var data = result.data;
                        var _s = '';
                        for (var i = 0; i < data.length; i++) {
                            _s += '<li data-sn="{1}">{0}</li>'.format(data[i].goodsSn, data[i].goodsSn);
                        }
                        $sug.html('<ul>{0}</ul>'.format(_s)).show();
                        $('.mdsug li').bind('mousedown', function () {
                            var $t = $(this);
                            var $s = $('.js-code-input');
                            $s.val($t.text());
                            $s.attr("data-sn",$t.data($t.text()));
                            $('.mdsug').empty().hide();
                        })
                    }else{
                        $sug.empty().hide();
                    }
                }
            })
        }else{
            $sug.empty().hide();
        }
    });

}

//初始化选择框
function initCheckBox() {
    $('input[name="shelfStatus"]').bind('click', function () {
        $('input[name="shelfStatus"]').not(this).attr("checked", false);
    });

    String.prototype.format = function () {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };
}

function saveGoods() {
    var goodsSn ;
    var materialType;
    var materialTypeName;
    var materialPrice = $('.js-price-input').val();
    var priceFlag = validateInputPrice($('.js-price-input'),materialPrice,'商品单价');
    if(!priceFlag){
        return false;
    }
    var goodsNum = $('.num-input').val();
    var isMultiplyItem = $('input[name="multiplyItem"]').is(":checked");
    if(isMultiplyItem){
        isMultiplyItem = 1;
    }else{
        isMultiplyItem = 0;
    }
    var isMatchModel = $('input[name="matchModel"]').is(":checked");
    if(isMatchModel){
        isMatchModel = 1;
    }else{
        isMatchModel = 0;
    }
    if(isMatchModel == false){
        goodsSn = $('.js-code-input').val();
        if(goodsSn == null || goodsSn == "" || goodsSn == undefined){
            layer.tips('请将信息填写完整后再添加!', '.js-code-input', {
                tips: [1, '#444']
            });
            return false;
        }
        var checked = checkGoodsSn(function (res) {
            if(res){
                var num = goodsNum*2;
                if(!isNaN(num) && (/^\d+$/.test(num))){
                    var length = $('.js-top-ul .mana-table tr').length;
                    if(length == 0){
                        $('.js-top-ul').append(template('goodsTableTemplate'));
                    }
                    var goods = {
                        goodsSn:goodsSn,
                        materialType:materialType,
                        materialPrice:materialPrice,
                        materialTypeName:materialTypeName,
                        goodsNum:goodsNum,
                        isMatchModel:isMatchModel,
                        needMultiplyItemNums:isMultiplyItem
                    };
                    $('.js-top-ul .mana-table').append(template('goodsTableTrTemplate',{goods:goods}));
                    $('.js-top-ul .mana-table').on('click','.js-goods-delete',function(e){
                        var dom = $(e.target).parents("tr");
                        $(dom).remove();
                        if($('.js-top-ul .mana-table tr').length == 0){
                            $('.js-top-ul .item-box').parents("li").remove();
                        }
                    })
                }else{
                    $('.num-input').val(0.5);
                }
            }
        });
    }else{
        materialType = $('.select-width').find("option:selected").val();
        materialTypeName = $('.select-width').find("option:selected")[0].innerText;
        var num = goodsNum*2;
        if(!isNaN(num) && (/^\d+$/.test(num))){
            var length = $('.js-top-ul .mana-table tr').length;
            if(length == 0){
                $('.js-top-ul').append(template('goodsTableTemplate'));
            }
            var goods = {
                goodsSn:goodsSn,
                materialType:materialType,
                materialPrice:materialPrice,
                materialTypeName:materialTypeName,
                goodsNum:goodsNum,
                isMatchModel:isMatchModel,
                needMultiplyItemNums:isMultiplyItem
            };
            $('.js-top-ul .mana-table').append(template('goodsTableTrTemplate',{goods:goods}));
            $('.js-top-ul .mana-table').on('click','.js-goods-delete',function(e){
                var dom = $(e.target).parents("tr");
                $(dom).remove();
                if($('.js-top-ul .mana-table tr').length == 0){
                    $('.js-top-ul .item-box').parents("li").remove();
                }
            })
        }else{
            $('.num-input').val(0.5);
        }
    }


}

function saveVideo() {
    var videoName = $('.js-video-name').val();
    var videoAddress = $('.js-video-address').val();
    if(videoName == "" || videoName == null || videoName == undefined){
        layer.tips('请将信息填写完整后再添加!', '.js-video-name', {
            tips: [1, '#444']
        });
        return false;
    }
    if(videoAddress == "" || videoAddress == null || videoAddress == undefined){
        layer.tips('请将信息填写完整后再添加!', '.js-video-address', {
            tips: [1, '#444']
        });
        return false;
    }
    var length = $('.js-down-ul .mana-table tr').length;
    if(length == 0){
        $('.js-down-ul').append(template('videoTableTemplate'));
    }
    $('.js-down-ul .mana-table').append(template('videoTableTrTemplate',{videoName:videoName,videoAddress:videoAddress}));
    $('.js-down-ul .mana-table').on('click','.js-video-delete',function(e){
        var dom = $(e.target).parents("tr");
        $(dom).remove();
        if($('.js-down-ul .mana-table tr').length == 0){
            $('.js-down-ul .item-box').parents("li").remove();
        }
    })
}

//是否匹配车型
function matchModel() {
    var checked = $('input[name="matchModel"]').is(":checked");
    if(checked == true){
        $('.js-code-input').addClass("display-none");
        $('select[class="select-width display-none"]').removeClass("display-none");
        $('.js-li-title').text("商品类型")
    }else{
        $('.js-code-input').removeClass("display-none");
        $('select[class="select-width"]').addClass("display-none");
        $('.js-li-title').text("商品编码")

    }
}

//加一条商品
function addOne() {
    var isMultiplyItem = $('input[name="multiplyItem"]').is(":checked");
    var num = $('.num-input').val();
    if(isMultiplyItem){
        var current = parseFloat(num) + 0.5;
        if(current <= 0){
            current = 0.5;
        }
        $('.num-input').val(current);
    }else{
        var currentNum  = parseInt(num) + 1;
        if(currentNum <= 0){
            currentNum = 1;
        }
        $('.num-input').val(currentNum);
    }
}
//减一条商品
function subtractOne() {
    var isMultiplyItem = $('input[name="multiplyItem"]').is(":checked");
    var num = $('.num-input').val();
    if(isMultiplyItem){
        var current= parseFloat(num) - 1;
        if(current <= 0){
            current = 0.5;
        }
        $('.num-input').val(current);
    }else{
        var currentNum = parseInt(num) - 1;
        if(currentNum <= 0){
            currentNum = 1;
        }
        $('.num-input').val(currentNum);
    }
}

function sureBtn() {
    var itemName = $('input[name="itemName"]').val();
    if(itemName == "" || itemName == null || itemName == undefined){
        layer.tips('服务项名称不能为空!', $('input[name="itemName"]'), {
            tips: [1, '#444']
        });
        return false;
    }
    var itemPrice = $('input[name="itemPrice"]').val();
    var flag = validateInputPrice($('input[name="itemPrice"]'),itemPrice, '项目市场单价');
    if(!flag){
        return false;
    }
    var itemUnit = $('input[name="itemUnit"]').val();
    if(itemUnit == "" || itemUnit == null || itemUnit == undefined){
        layer.tips('服务单位不能为空!', $('input[name="itemUnit"]'), {
            tips: [1, '#444']
        });
        return false;
    }
    var itemMaterialPrice = $('input[name="itemMaterialPrice"]').val();
    var materialFlag = validateInputPrice($('input[name="itemMaterialPrice"]'),itemMaterialPrice,'项目物料费');
    if(!materialFlag){
        return false;
    }
    var itemWorkFee = $('input[name="itemWorkFee"]').val();
    var itemFeeFlag = validateInputPrice($('input[name="itemWorkFee"]'),itemWorkFee,'项目工时费');
    if(!itemFeeFlag){
        return false;
    }
    var buyMinTime = $('input[name="buyMinTime"]').val();
    var buyMaxTime = $('input[name="buyMaxTime"]').val();
    var buyMinTimeFlag = validateBuyTime($('input[name="buyMinTime"]'),buyMinTime);
    if(!buyMinTimeFlag){
        return false;
    }
    var buyMaxTimeFlag = validateBuyTime($('input[name="buyMaxTime"]'),buyMaxTime);
    if(!buyMaxTimeFlag){
        return false;
    }
    if(parseFloat(buyMaxTime) < parseFloat(buyMinTime)){
        layer.tips('最大购买次数必须大于等于最小购买次数', $('input[name="buyMaxTime"]'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        $('input[name="buyMaxTime"]').val("");
        return false;
    }
    var shelfStatus = $('input[name="shelfStatus"]:checked').val();
    var ue1 = UE.getEditor('suitableModelDescription');
    var ue2 = UE.getEditor('contentDescription');
    var ue3 = UE.getEditor('workDescription');
    var itemCarModel = '';
    var itemContentProcedure = '';
    var itemActionMerit = '';
    ue1.ready(function() {
        itemCarModel = ue1.getContentTxt();
    });
    ue2.ready(function() {
        itemContentProcedure = ue2.getContentTxt();
    });
    ue3.ready(function() {
        itemActionMerit = ue3.getContentTxt();
    });
    var serviceItem = {
        itemName:itemName,
        itemCarModel:itemCarModel,
        itemContentProcedure:itemContentProcedure,
        itemActionMerit:itemActionMerit,
        itemUnit:itemUnit,
        itemPrice:itemPrice,
        itemMaterialPrice:itemMaterialPrice,
        itemWorkPrice:itemWorkFee,
        buyMinNum:buyMinTime,
        buyMaxNum:buyMaxTime,
        itemStatus:shelfStatus,
        materialTemplateParams:null,
        serviceItemVideoParams:null
    };

    var domList = $('.js-top-ul tr');
    var materialTemplateParams = [];
    $.each(domList,function (index,dom) {
        var $dom = $(dom);
        var goodsSn;
        var materialType;
        var isMatchModel = $dom.find('.js-match-model').data("match");
        if(isMatchModel == 1){
            materialType = $dom.find('.js-match-model').data("type");
        }else{
            goodsSn = $dom.find('.js-match-model')[0].innerText;
        }
        var materialPrice = $dom.find('.js-material-price')[0].innerText;
        var needMultiplyItemNums = $dom.find('.js-multiply-item').data("multiply");
        var goodsNum = $dom.find('.js-goods-num')[0].innerText;
        var material = {
            goodsSn:goodsSn,
            materialType:materialType,
            materialPrice:materialPrice,
            needMultiplyItemNums:needMultiplyItemNums,
            goodsNum:goodsNum
        };
        materialTemplateParams.push(material);
    });

    var serviceItemVideoParams = [];
    var videoList = $('.js-down-ul tr');
    $.each(videoList,function (idx,video) {
        var $s = $(video);
        var videoName = $s.find(".js-video-name")[0].innerText;
        var address = $s.find(".js-video-address")[0].innerText;
        var video = {
            videoName:videoName,
            videoTutorial:address
        };
        serviceItemVideoParams.push(video);
    });

    serviceItem['materialTemplateParams'] = materialTemplateParams;
    serviceItem['serviceItemVideoParams'] = serviceItemVideoParams;
    var index = LayerUtil.load();
    Ajax.postJson({
        url:"/insuranceItem/createServiceItemConfig",
        data:serviceItem,
        success:function (result) {
            layer.close(index);
            if(result.success){
                layer.msg("新增成功");
                location.href = "/insuranceItem/itemListInit";
            }else{
                layer.msg(result.message);
            }
        }
    });
}

function validateInputPrice(dom,value,tip) {
    var $dom = $(dom);
    if (!isNaN(value)) {
        if (value > 99999999) {
            layer.tips(tip+'必须小于99999999', $dom, {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.val("");
            return false;
        }
        if (value <= 0) {
            layer.tips(tip+'必须大于0', $dom, {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.val("");
            return false;
        }
        var dot = value.indexOf(".");
        if (dot != -1) {
            var dotCnt = value.substring(dot + 1, value.length);
            if (dotCnt.length > 2) {
                layer.tips(tip+'小数位不能超过2位！', $dom, {
                    tips: [1, '#444'],
                    tipsMore: true
                });
                $dom.val("");
                return false;
            }
        }
    } else {
        layer.tips(tip+'必须为数字', $dom, {
            tips: [1, '#444'],
            tipsMore: true
        });
        $dom.val("");
        return false;
    }
    return true;
}

function validateBuyTime(dom,value) {
    var $dom = $(dom);
    if (!isNaN(value)) {
        if (value < 1) {
            layer.tips('购买次数必须大于等于1', $dom, {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.val("");
            return false;
        }
        var dot = value.indexOf(".");
        if (dot != -1) {
            var dotCnt = value.substring(dot + 1, value.length);
            if (dotCnt.length > 0) {
                layer.tips('购买次数必须为整数！', $dom, {
                    tips: [1, '#444'],
                    tipsMore: true
                });
                $dom.val("");
                return false;
            }
        }
        if (value > 99999999) {
            layer.tips('购买次数必须小于99999999', $dom, {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.val("");
            return false;
        }
    } else {
        layer.tips('购买次数必须为数字', $dom, {
            tips: [1, '#444'],
            tipsMore: true
        });
        $dom.val("");
        return false;
    }
    return true;
}