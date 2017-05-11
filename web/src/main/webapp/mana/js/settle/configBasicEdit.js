/**
 * Created by zhouheng on 17/2/6.
 */

template.helper('numberFormatHelper',function(num){
    if (num == null || num == undefined) {
        return '0.00';
    }
    return (Math.round(num * 100) / 100).toFixed(2);
});

$(document).ready(function(){

    initCheckBox();

}).on('click', '.sure-button', function () {

    updateConfigBasic();

}).on('click', '.add', function (e) {

    addItem(e);

}).on('click', '.remove', function (e) {

    removeItem(e);

}).on('click','.cancel-button',function () {
    
    location.href = "/settle/configBasicList";
    
});

function initCheckBox() {
    $('.js-add-item1').find('input[type=checkbox]').bind('click', function () {
        $('.js-add-item1').find('input[type=checkbox]').not(this).attr("checked", false);
    });

    $('.js-add-item2').find('input[type=checkbox]').bind('click', function () {
        $('.js-add-item2').find('input[type=checkbox]').not(this).attr("checked", false);
    });
}

/**
 * 更新组配置
 * @returns {boolean}
 */
function updateConfigBasic() {
    var configName = $('#insuranceType .group-name').val();
    var calculateMode = $('.js-add-item1').find('input[type=checkbox]:checked').val();
    var insuranceType = $('.js-add-item2').find('input[type=checkbox]:checked').val();
    if(insuranceType == null || insuranceType == ""){
        layer.tips('请选择险种', $(".js-add-item2 .opa-radio"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if(calculateMode == null || calculateMode == ""){
        layer.tips('必须选择一种计费方式', $(".js-add-item1 .opa-radio"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if (configName == null || configName == "") {
        layer.tips('请填写保费区间分组名', $("#insuranceType .group-name"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if (configName.length >= 15) {
        layer.tips('返点配置名称长度必须小于15', $("#insuranceType .group-name"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }

    var dom = $('#insuranceType').find('li[class="interval-item"]').last();
    var interval = $(dom).find("input[class='interval-input']").val();
    var scale = $(dom).find("input[class='scale-input']").val();
    if(interval == "" && scale == ""){
        layer.tips('请填写保费区间对应比例', $(dom).find("input[class='scale-input']"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if(interval != ""){
        $(dom).find("input[class='interval-input']").val("");
    }
    var domList = $('#insuranceType').find('li[class="interval-item"]');
    var flag = true;
    $.each(domList,function(index,dom) {
        var boolean = checkItem(dom);
        if (!boolean) {
            flag = false;
        }
    });
    if(!flag){
        return false;
    }
    var itemVOList = [];
    $.each(domList,function(index,dom){
        var interval = $(dom).find("input[class='interval-input']").val();
        var scale = $(dom).find("input[class='scale-input']").val();
        var item = {
            groupName:configName,
            itemEndValue:interval,
            itemRate:scale
        };
        itemVOList.push(item);
    });
    var basic = $('#insureCompanyId option:selected')[0];
    var reqParam = {
        id:$('#basicId').val(),
        groupName:configName,
        insuranceCompanyId:$(basic).val(),
        insuranceCompanyName:basic.innerHTML,
        cooperationMode:$('#cooperationMode option:selected').val(),
        insuranceType:insuranceType,
        calculateMode:calculateMode,
        itemVOList:itemVOList
    };
    var index = LayerUtil.load();
    var action = '/settle/updateSettleConfig';
    Ajax.postJson({
        url: action,
        data: reqParam,
        success: function (result) {
            layer.close(index);
            if(result.success){
                layer.msg("编辑成功");
                location.href = "/settle/configBasicList";
            }else{
                layer.msg(result.message);
            }
            layer.close(index);
        }
    });
}

/**
 * 新增阶梯
 * @param e
 * @returns {boolean}
 */
function addItem(e) {
    var dom = $(e.target).parent();
    var boolean = checkItem(dom);
    if(!boolean){
        return false;
    }
    var startValue = dom.find("span[class='interval-div']")[0].innerText;
    var endValue = dom.find("input[class='interval-input']").val();
    if (endValue == "" || endValue == null) {
        layer.tips('最大返点配置区间后不能新增新的区间', $(dom).find("button[class='add operate-btn']"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }else{
        if (!isNaN(endValue)) {
            if (endValue <= 0) {
                layer.tips('返点配置区间结束值必须大于0', $(dom).find("input[class='interval-input']"), {
                    tips: [1, '#444'],
                    tipsMore: true
                });
                dom.find("input[class='interval-input']").val("");
                return false;
            }
            var dot = endValue.indexOf(".");
            if (dot != -1) {
                var dotCnt = endValue.substring(dot + 1, endValue.length);
                if (dotCnt.length > 2) {
                    layer.tips('返点配置比例值小数位不能超过2位！', $(dom).find("input[class='interval-input']"), {
                        tips: [1, '#444'],
                        tipsMore: true
                    });
                    return false;
                }
            }
        } else {
            layer.tips('返点配置区间结束值必须为数字', $(dom).find("input[class='interval-input']"), {
                tips: [1, '#444'],
                tipsMore: true
            });
            dom.find("input[class='interval-input']").val("");
            return false;
        }
        if (parseFloat(endValue) <= parseFloat(startValue)) {
            layer.tips('返点配置区间结束值必须大于区间开始值', $(dom).find("input[class='interval-input']"), {
                tips: [1, '#444'],
                tipsMore: true
            });
            dom.find("input[class='interval-input']").val("");
            return false;
        }
    }
    var nextStartValue = parseFloat(endValue) + 0.01;
    $(e.target).parent().find("button[class='add operate-btn']").addClass("display-none");
    $(e.target).parent().find("button[class='remove operate-btn']").addClass("display-none");
    $(e.target).parents("ul").append(template('itemTemplate', {startValue: nextStartValue}));
    $(e.target).parent().find("input[class='interval-input']").attr("readonly","true");
}

/**
 * 删除阶梯
 * @param e
 */
function removeItem(e) {
    var dom = $(e.target).parent();
    dom.prev().find("button[class='add operate-btn display-none']").removeClass("display-none");
    dom.prev().find("button[class='remove operate-btn display-none']").removeClass("display-none");
    dom.prev().find("input[class='interval-input']").removeAttr("readonly");
    dom.prev().find("input[class='interval-input']").val("");
    dom.remove();
}

/**
 * 新增阶梯时验证阶梯
 * @param dom
 * @returns {boolean}
 */
function checkItem(dom){
    var $dom = $(dom);
    var itemRate = $dom.find("input[class='scale-input']").val();
    if (!isNaN(itemRate) && itemRate != "" && itemRate != null) {
        if(itemRate < 0 || itemRate>=1){
            layer.tips('返点配置比例值必须大于0并且小于1', $dom.find('.scale-input'), {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.find("input[class='scale-input']").val("");
            return false;
        }
        if (itemRate < 0) {
            layer.tips('返点配置比例值必须大于等于0', $dom.find("input[class='scale-input']"), {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.find("input[class='scale-input']").val("");
            return false;
        }
        var dot = itemRate.indexOf(".");
        if (dot != -1) {
            var dotCnt = itemRate.substring(dot + 1, itemRate.length);
            if (dotCnt.length > 2) {
                layer.tips('返点配置比例值小数位后不能超过2位！', $dom.find("input[class='scale-input']"), {
                    tips: [1, '#444'],
                    tipsMore: true
                });
                return false;
            }
        }
    } else {
        layer.tips('返点配置比例值必须为数字', $dom.find("input[class='scale-input']"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        $dom.find("input[class='scale-input']").val("");
        return false;
    }
    return true;
}

