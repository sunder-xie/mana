/**
 * Created by zhouheng on 17/2/15.
 */
var _itemList = [];
$(document).ready(function () {

    initCheckBox();

    initSelectOption();

    UE.getEditor('packageDescription');
    UE.getEditor('marketTip');

}).on('click','.save-button',function(){

    saveItem();

}).on('click','.add-one-button',function () {

    addOne();

}).on('click','.subtract-one-button',function () {

    subtractOne();

}).on('click','.sure-button',function () {

    sureBtn()

}).on('click','.cancel-button',function () {

    location.href = "/insurancePackage/packageListInit";

});

//初始化选择框
function initCheckBox() {
    $('.div-left-checkbox').bind('click', function () {
        $('.div-left-checkbox').not(this).attr("checked", false);
    });
}

function initSelectOption() {
    String.prototype.format = function () {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };
    var action = '/insuranceItem/getServiceItemNameList';
    Ajax.get({
        url: action,
        data : {},
        success: function (result) {
            if(result.success){
                var data =result.data;
                // _itemList = result.data;
                // initOption();
                $('.item-select').html("");
                var _h = '';
                for (var i = 0; i <data.length; i++) {
                    _h += '<option value="{0}">{1}</option>'.format(data[i].id, data[i].itemName);
                }
                $('.item-select').html(_h).multiNa([], true, true);
            }else{
                layer.msg(result.message);
            }
        }
    });
}

//新增一条项目条目
function saveItem() {
    var itemName = $('.item-select :selected')[0].innerText;
    var itemId = $('.item-select :selected').val();
    var itemNum = $('.num-input').val();
    $('.item-select :selected').remove();
    $('.item-select').refresh();
    if(itemName == null || itemName == "" || itemName == undefined){
        layer.tips('请将信息填写完整后再添加!', '.item-select', {
            tips: [1, '#444']
        });
        return false;
    }
    if(!isNaN(itemNum) && (/^\d+$/.test(itemNum))){
        var length = $('.mana-table tr').length;
        if(length == 0){
            $('.div-left-ul').append(template('manaTableParentTemplate'));
        }
        $('.mana-table').append(template('manaTableTemplate',{itemId:itemId,itemName:itemName,itemNum:itemNum}));
        $('.mana-table').off('click.aa').on('click.aa','.js-delete',function(e){
            var dom = $(e.target).parents("tr");
            var id = $(dom).find(".js-item-name").data("id");
            var itemName = $(dom).find(".js-item-name")[0].innerText;
            var str = '<option value="'+id+'" class="initSelected">'+itemName+'</option>';
            $('.item-select').prepend(str);
            $('.item-select').refresh();
            $(dom).remove();
            if($('.mana-table tr').length == 0){
                $('.item-box').parents("li").remove();
            }
        })
    }else{
        $('.num-input').val(1);
    }
}


function initOption() {

    // $('.item-select').chosen('destroy').chosen('init');
    // $(".chzn-search input").css("height","250px");//设置下拉出来的搜索框的高度，一般不用设置
    // // $(".item-select").trigger("liszt:updated");
}

// function reduceOption(item) {
//     var itemList = [];
//     for(var i=0;i<_itemList.length;i++){
//         if(_itemList[i].id == item.id){
//             continue;
//         }
//         itemList.push(_itemList[i]);
//     }
//     _itemList = itemList;
//     initOption();
// }

function addOption(e) {
    var dom = $(e.target).parents("tr");
    var id = $(dom).find(".js-item-name").data("id");
    var itemName = $(dom).find(".js-item-name")[0].innerText;
    var item = {
        id:id,
        itemName:itemName
    };
    _itemList.push(item);
    initOption();
}

//加一条商品
function addOne() {
    var num = $('.num-input').val();
    var currentNum = parseInt(num) + 1;
    if(currentNum <= 0){
        currentNum = 1;
    }
    $('.num-input').val(currentNum);
}
//减一条商品
function subtractOne() {
    var num = $('.num-input').val();
    var currentNum = parseInt(num) - 1;
    if(currentNum <= 0){
        currentNum = 1;
    }
    $('.num-input').val(currentNum);
}

function sureBtn() {
    var packageName = $('input[name="packageName"]').val();
    if(packageName == null || packageName == "" || packageName == undefined){
        layer.tips('服务包名称不能为空!', $('input[name="packageName"]'), {
            tips: [1, '#444']
        });
        return false;
    }
    var packagePrice = $('input[name="packagePrice"]').val();
    var flag = validateInputPrice($('input[name="packagePrice"]'),packagePrice, '服务包市场价');
    if(!flag){
        return false;
    }
    var minFee = $('input[name="minFee"]').val();
    var maxFee = $('input[name="maxFee"]').val();
    var minFeeFlag = validateInsureFee($('input[name="minFee"]'),minFee);
    if(!minFeeFlag){
        return false;
    }

    var maxFeeFlag = validateInsureFee($('input[name="maxFee"]'),maxFee);
    if(!maxFeeFlag){
        return false;
    }
    if(parseFloat(maxFee) <= parseFloat(minFee)){
        layer.tips('最大保费区间必须大于最小保费区间', $('input[name="maxFee"]'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        $('input[name="minFee"]').val("");
        return false;
    }
    var oilAmount = $('input[name="oilAmount"]').val();
    var flag = validateInputPrice($('input[name="oilAmount"]'),oilAmount, '机油用量');
    if(!flag){
        return false;
    }
    var oilGoodsSn = $('input[name="oilGoodsSn"]').val();
    // var rewardAmount = $('input[name="rewardAmount"]').val();
    // var flag = validateInputPrice($('input[name="rewardAmount"]'),rewardAmount, '门店可得返利');
    // if(!flag){
    //     return false;
    // }
    var shelfStatus = $('input[name="shelfStatus"]:checked').val();
    var ue1 = UE.getEditor('packageDescription');
    var ue2 = UE.getEditor('marketTip');
    var packageDescription = '';
    var marketTip = '';
    ue1.ready(function() {
        packageDescription = ue1.getContentTxt();
    });
    if(packageDescription == ''){
        layer.msg("服务包描述不能为空");
        return false;
    }
    ue2.ready(function() {
        marketTip = ue2.getContentTxt();
    });
    
    var domList = $('.div-left-ul tr');
    if(domList.length <= 0){
        layer.msg("必须添加服务项目");
        return false;
    }
    var serviceItemParams = [];
    $.each(domList,function (index,dom) {
        var $dom = $(dom);
        var itemId = $dom.find('.js-item-name').data("id");
        var itemName = $dom.find('.js-item-name')[0].innerText;
        var itemNum = $dom.find('.js-item-num')[0].innerText;
        var item = {
            id:itemId,
            itemName:itemName,
            itemTimes:itemNum
        };
        serviceItemParams.push(item);
    });

    var servicePackage = {
        packageName:packageName,
        marketPrice:packagePrice,
        suitableStartPrice:minFee,
        suitableEndPrice:maxFee,
        // rewardAmount:rewardAmount,
        engineOilSn:oilGoodsSn,
        description:packageDescription,
        engineOilCapacity:oilAmount,
        servicePackageStatus:shelfStatus,
        promtNote:marketTip,
        serviceItemParams:serviceItemParams
    };
    var index = LayerUtil.load();
    Ajax.postJson({
        url:'/insurancePackage/createServicePackage',
        data:servicePackage,
        success:function (result) {
            layer.close(index);
            if(result.success){
                layer.msg("新增成功");
                location.href = "/insurancePackage/packageListInit";
            }else{
                layer.msg(result.message);
            }
        }
    })
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

function validateInsureFee(dom,value) {
    var $dom = $(dom);
    if (!isNaN(value)) {
        if (value < 1) {
            layer.tips('保费必须大于等于1', $dom, {
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
                layer.tips('保费必须为整数！', $dom, {
                    tips: [1, '#444'],
                    tipsMore: true
                });
                $dom.val("");
                return false;
            }
        }
        if (value > 99999999) {
            layer.tips('保费必须小于99999999', $dom, {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.val("");
            return false;
        }
    } else {
        layer.tips('保费必须为数字', $dom, {
            tips: [1, '#444'],
            tipsMore: true
        });
        $dom.val("");
        return false;
    }
    return true;
}

