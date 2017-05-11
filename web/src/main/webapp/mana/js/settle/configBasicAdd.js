/**
 * Created by zhouheng on 17/1/18.
 */

$(document).ready(function () {

    //初始化checkBox
    initCheckBox();

}).on('click', '.sure-button', function () {

    addConfigBasicList();

}).on('click', '.add', function (e) {

    addItem(e);

}).on('click', '.remove', function (e) {

    removeItem(e);

}).on('click','.cancel-button',function () {

    location.href = "/settle/configBasicList";

});
/**
 * 新增返点配置
 * @param e
 */
function addConfigBasicList() {
    var domList = $('input[name="insuranceType"]:checked');
    var checkedLength = domList.length;
    if(checkedLength <= 0){
        layer.tips('请选择险种', $("#compulsoryInsurance .opa-radio"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    var list = [];
    var flag = true;
    $.each(domList,function(index,dom){
        var $dom = $(dom);
        var insuranceType = $dom.val();
        if(insuranceType == 1){
            var id = "compulsoryInsurance";
            var calculateMode = $('#'+id+' .add-item').find('input[type=checkbox]:checked').val();
            var configName = $('#'+id+' .group-name').val();
            var  check = checkBasic(id);
            if(!check){
                flag = false;
                return false;
            }
            var itemVOList = [];
            var domList = $('#'+id).find('li[class="interval-item"]');
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
                groupName:configName,
                insuranceCompanyId:$(basic).val(),
                insuranceCompanyName:basic.innerHTML,
                cooperationMode:$('#cooperationMode option:selected').val(),
                insuranceType:insuranceType,
                calculateMode:calculateMode,
                itemVOList:itemVOList
            };
            list.push(reqParam);
        }else{
            var id = "commercialInsurance";
            var calculateMode = $('#'+id+' .add-item').find('input[type=checkbox]:checked').val();
            var configName = $('#'+id+' .group-name').val();
            var  check = checkBasic(id);
            if(!check){
                flag = false;
                return false;
            }
            var itemVOList = [];
            var domList = $('#'+id).find('li[class="interval-item"]');
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
                groupName:configName,
                insuranceCompanyId:$(basic).val(),
                insuranceCompanyName:basic.innerHTML,
                cooperationMode:$('#cooperationMode option:selected').val(),
                insuranceType:insuranceType,
                calculateMode:calculateMode,
                itemVOList:itemVOList
            };
            list.push(reqParam);
        }
    });
    if(!flag){
        return false;
    }
    var index = LayerUtil.load();
    var action = '/settle/addSettleConfigList';
    Ajax.postJson({
        url: action,
        data: list,
        success: function (result) {
            layer.close(index);
            if(result.success){
                layer.msg("新增成功");
                location.href = "/settle/configBasicList";
            }else{
                layer.msg(result.message);
            }
        }
    });
}

function initCheckBox() {
    $('#compulsoryInsurance .add-item').find('input[type=checkbox]').bind('click', function () {
        $('#compulsoryInsurance .add-item').find('input[type=checkbox]').not(this).attr("checked", false);
    });

    $('#commercialInsurance .add-item').find('input[type=checkbox]').bind('click', function () {
        $('#commercialInsurance .add-item').find('input[type=checkbox]').not(this).attr("checked", false);
    });
}

/**
 * 新增返点配置比例区间
 * @param e
 * @returns {boolean}
 */
function addItem(e) {
    var dom = $(e.target).parent();
    var id = $(e.target).parents(".insurance-box").attr("id");
    var boolean = checkItem(id,dom);
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
    } else {
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
                    layer.tips('返点配置比例值小数位不能超过2位！', $('#'+id+' .interval-input'), {
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

function removeItem(e) {
    var dom = $(e.target).parent();
    dom.prev().find("button[class='add operate-btn display-none']").removeClass("display-none");
    dom.prev().find("button[class='remove operate-btn display-none']").removeClass("display-none");
    dom.prev().find("input[class='interval-input']").removeAttr("readonly");
    dom.prev().find("input[class='interval-input']").val("");
    dom.remove();
}

function checkBasic(id) {
    var calculateMode = $('#'+id+' .add-item').find('input[type=checkbox]:checked').val();
    var configName = $('#'+id+' .group-name').val();
    if (configName == null || configName == "") {
        layer.tips('请填写保费区间分组名', $('#'+id+' .group-name'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if (configName.length >= 15) {
        layer.tips('返点配置名称长度必须小于15', $('#'+id+' .group-name'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if(calculateMode == null || calculateMode == ""){
        layer.tips('必须选择一种计费方式', $('#'+id+' .add-item .opa-radio'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    var dom = $('#'+id).find('li[class="interval-item"]').last();
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
    var domList = $('#'+id).find('li[class="interval-item"]');
    var flag = true;
    $.each(domList,function(index,dom) {
        var boolean = checkItem(id,dom);
        if (!boolean) {
            flag = false;
        }
    });
    if(!flag){
        return false;
    }
    return true;
}

/**
 * 新增阶梯时验证阶梯
 * @param dom
 * @returns {boolean}
 */
function checkItem(id,dom){
    var $dom = $(dom);
    var interval = $dom.find("input[class='interval-input']").val();
    var scale = $dom.find("input[class='scale-input']").val();
    if(interval == "" && scale == ""){
        layer.tips('返点配置比例值不能为空', $(dom).find("input[class='scale-input']"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        $dom.find("input[class='scale-input']").val("");
        return false;
    }
    if (!isNaN(scale) && scale != "" && scale != null) {
        if(scale < 0 || scale>=1){
            layer.tips('返点配置比例值必须大于0并且小于1', $('#'+id+' .scale-input'), {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.find("input[class='scale-input']").val("");
            return false;
        }
        if (scale < 0) {
            layer.tips('返点配置比例值必须大于等于0', $dom.find('input[class="scale-input"]'), {
                tips: [1, '#444'],
                tipsMore: true
            });
            $dom.find("input[class='scale-input']").val("");
            return false;
        }
        var dot = scale.indexOf(".");
        if (dot != -1) {
            var dotCnt = scale.substring(dot + 1, scale.length);
            if (dotCnt.length > 2) {
                layer.tips('返点配置比例值小数位后不能超过2位！', $('#'+id+' .scale-input'), {
                    tips: [1, '#444'],
                    tipsMore: true
                });
                $dom.find("input[class='scale-input']").val("");
                return false;
            }
        }
    } else {
        layer.tips('返点配置比例值必须为数字', $('#'+id+' .scale-input'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        $dom.find("input[class='scale-input']").val("");
        return false;
    }
    return true;
}
