/**
 * Created by zhouheng on 17/2/21.
 */

$(document).ready(function () {

    initCheckBox();

    initShopNameEvent();

    initSettleBasicSelect();

}).on('click', '.operate-btn', function (e) {

    initOperationEvent(e);

}).on('click','.cancel-btn',function (e) {

    deleteItem(e);

}).on('click','.sure-button',function () {

    sureBtn();

}).on('click','.cancel-button',function () {

    location.href = "/settle/shopRule/bountyScaleList";

});

function initCheckBox() {
    var area = $('.dispose').find('input[name="area"]');
    var store = $('.dispose').find('input[name="store"]');
    $(area).bind('click', function () {
        $(store).attr("checked", false);
        $('.area-dispose-box').removeClass("display-none");
        $('.store-dispose-box').addClass("display-none");
        $('.area-dispose-item select').bind('change', function () {
            initRegionList();
        })
    });
    $(store).bind('click', function () {
        $(area).attr("checked", false);
        $('.area-dispose-box').addClass("display-none");
        $('.store-dispose-box').removeClass("display-none");
    });

    String.prototype.format = function () {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };
}

function initRegionList() {
    var cooperationMode = 1;//买保险送奖励金
    var insuranceCompanyId = $('.js-insurance-company option:selected').val();
    var regionCode = $('.area-dispose-item select').children('option:selected').val();
    var regionLevel = $('.area-dispose-item select').children('option:selected').data("level");
    Ajax.get({
        url: "/settle/shopRule/getAvailableRegionList",
        data: {
            cooperationMode:cooperationMode,
            insuranceCompanyId:insuranceCompanyId,
            regionParentCode: regionCode,
            regionLevel:regionLevel
        },
        success: function (result) {
            if (result.success) {
                var dataList = result.data;
                $('.js-item').remove();
                $('.area-dispose-box').append(template('areaBoxTemplate', {regionList: dataList}));
                $('#allChecked').bind('click', function () {
                    var domList = $(this).parent().next().find('input');
                    var bool = $('#allChecked').is(":checked");
                    if (bool) {
                        $.each(domList, function (index, dom) {
                            $(dom).prop("checked", true);
                        });
                    } else {
                        $.each(domList, function (index, dom) {
                            $(dom).prop("checked", false);
                        });
                    }
                })
            }
        }
    });
}

function initShopNameEvent() {
    $('#shopName').bind('input propertychange', function () {
        $('.mdsug').empty().hide();
        var shopName = $(this).val();
        if(shopName != null && shopName != ""){
            var $sug = $('.mdsug');
            Ajax.get({
                url: "/ucShop/getSimpleShopList",
                data: {
                    cooperationMode: 1,
                    queryStr: shopName
                },
                success: function (result) {
                    if (result.success) {
                        var _s = '';
                        var list = result.list;
                        for (var i = 0; i < list.length; i++) {
                            _s += '<li data-id="{0}" data-account="{2}">{1}</li>'.format(list[i].id, list[i].companyName,list[i].defaultAccountMobile);
                        }
                        $sug.html('<ul>{0}</ul>'.format(_s)).show();
                        $('.mdsug li').bind('mousedown', function () {
                            var $t = $(this);
                            var $s = $('#shopName');
                            $s.val($t.text());
                            $s.attr("data-id",$t.data("id"));
                            $s.attr("data-account",$t.data("account"));
                            $s.attr("data-text",$t.text());
                            $('.mdsug').empty().hide();
                        })
                    } else {
                        $sug.empty().hide();
                    }
                }
            })
        }
    });
}
//初始化查询下拉
function initSettleBasicSelect() {
    //交强险
    getSettleBasicListAjax('.js-compulsory-calculate', 1);
    //商业险
    getSettleBasicListAjax('.js-commercial-calculate', 2);

    $('.js-compulsory-calculate').bind('change', function () {
        //交强险
        getSettleBasicListAjax('.js-compulsory-calculate', 1);
    });

    $('.js-commercial-calculate').bind('change', function () {
        //商业险
        getSettleBasicListAjax('.js-commercial-calculate', 2);
    });

}

function getSettleBasicListAjax(dom, insuranceType) {
    var cooperationMode = 1;//买保险送奖励金
    var insuranceCompanyId = $('.js-insurance-company option:selected').val();
    var calculateMode = $(dom + ' option:selected').val();
    var reqParam = {
        insuranceCompanyId: insuranceCompanyId,
        cooperationMode: cooperationMode,
        insuranceType: insuranceType,
        calculateMode: calculateMode
    };
    Ajax.get({
        url: "/settle/getBasicBORedisList",
        data: reqParam,
        success: function (result) {
            if (result.success) {
                var dataList = result.data;
                if (insuranceType == 1) {
                    var _h = '';
                    for (var i = 0; i < dataList.length; i++) {
                        _h += '<option value="{0}">{1}</option>'.format(dataList[i].id, dataList[i].groupName);
                    }
                    $('.js-compulsory-basic').html(_h);
                    getItemInfo('.js-add-compulsory-item', dataList[0].id);
                    $('.js-compulsory-basic').bind('change', function (e) {
                        var basicId = $(e.target).parents("div[class='add-item']").find('.js-compulsory-basic option:selected').val();
                        getItemInfo($(e.target).parents("div[class='add-item']").find(".js-add-compulsory-item"), basicId);
                    })
                } else {
                    var _h = '';
                    for (var i = 0; i < dataList.length; i++) {
                        _h += '<option value="{0}">{1}</option>'.format(dataList[i].id, dataList[i].groupName);
                    }
                    $('.js-commercial-basic').html(_h);
                    getItemInfo('.js-add-commercial-item', dataList[0].id);
                    $('.js-commercial-basic').bind('change', function (e) {
                        var basicId = $(e.target).parents("div[class='add-item']").find('.js-commercial-basic option:selected').val();
                        getItemInfo($(e.target).parents("div[class='add-item']").find(".js-add-commercial-item"), basicId);
                    })
                }
            }

        }
    })
}

function getItemInfo(dom, basicId) {
    Ajax.get({
        url: "/settle/getSettleConfigById",
        data: {
            basicId: basicId
        },
        success: function (result) {
            var data = result.data;
            var $dom = $(dom);
            $dom.html(template('addItemTemplate', {basic: data}));
        }
    })
}

function initOperationEvent(e) {
    var $dom = $(e.target);
    var startDate = $dom.parent().find("input[class='cxdate js-start-date']").val();
    var endDate = $dom.parent().find("input[class='cxdate js-end-date']").val();
    var dt = new Date(endDate.replace(/-/g,"/"));
    var nowDate = dt.getTime()+24*60*60*1000;//时间加上一天
    if(endDate == null || endDate == ""){
        layer.tips('终止日期不能为空', $dom.parent().find("input[class='cxdate js-end-date']"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    if(endDate < startDate){
        layer.tips('终止日期必须大于等于开始日期', $dom.parent().find("input[class='cxdate js-end-date']"), {
            tips: [1, '#444'],
            tipsMore: true
        });
        return false;
    }
    $dom.addClass("display-none");
    $dom.next().addClass("display-none");
    $dom.parent().find("input[class='cxdate js-end-date']").unbind("focus");
    var $tmp = $dom.parents("div[class='add-item-box']");
    var first_basic_id = $dom.prev().find("select option:first").val();
    var html = $dom.prev().find("select").html();
    var className = $dom.prev().find("select").attr("class");
    var jsName = $dom.next().next().attr("class");
    $tmp.append(template('addBoxTemplate', {endDate: nowDate, className: className, jsName: jsName}));
    $(e.target).parent().next().find("select").append(html);
    getItemInfo($(e.target).parents("div[class='add-item-box']").find('.' + jsName).last(), first_basic_id);
    $(e.target).parents("div[class='add-item-box']").find('.' + className).last().bind('change', function (e) {
        var basicId = $(e.target).parents("div[class='add-item']").find('.' + className + ' option:selected').val();
        getItemInfo($(e.target).parents("div[class='add-item']").children("div:last-child"), basicId);
    })
}

function deleteItem(e) {
    var $dom = $(e.target);
    $dom.parent().prev().find("input[class='cxdate js-end-date']").removeAttr("readonly");
    $dom.parent().prev().find("input[class='cxdate js-end-date']").val("");
    var size = $dom.parents("div[class='add-item-box']").find("div[class='add-item']").length;
    if(size > 2){
        $dom.parent().prev().find(".operate-btn").removeClass("display-none");
        $dom.parent().prev().find('.cancel-btn').removeClass("display-none");
    }else{
        $dom.parent().prev().find(".operate-btn").removeClass("display-none");
    }
    $dom.parent().remove();
}

function sureBtn() {
    var cooperationMode = 1;
    var insuranceCompanyId = $('.js-insurance-company option:selected').val();
    var insuranceCompanyName = $('.js-insurance-company option:selected').text();
    var settleCondition = $('.js-settle-condition option:selected').val();
    var range = $('.dispose').find("input[class='opa-radio']:checked").val();
    var area = $('.dispose').find('input[name="area"]');
    var store = $('.dispose').find('input[name="store"]');
    if(settleCondition == null || settleCondition == 0){
        layer.tips('请选择结算条件', $('.js-settle-condition'), {
            tips: [1, '#444'],
            tipsMore: true
        });
        layer.msg("请选择结算条件");
        return false;
    }
    var shopConfigVO = {};
    if($(store).prop("checked")){
        var shopId = $('#shopName').data("id");
        var shopName = $('#shopName').data("text");
        var shopAccount = $('#shopName').data("account");
        shopConfigVO = {
            shopId:shopId,
            shopName:shopName,
            shopAccount:shopAccount
        }
    }
    var regionList = [];
    if($(area).prop("checked")){
        var provinceCode = $('.js-region-select option:selected').val();
        var provinceName = $('.js-region-select option:selected').text();
        var domList = $('.area-dispose-box ul').find("input[type='checkbox']:checked");
        if(domList <= 0){
            layer.tips('请勾选地区配置城市', $('.js-region-select'), {
                tips: [1, '#444'],
                tipsMore: true
            });
            layer.msg('请勾选地区配置城市');
            return false;
        }
        $.each(domList,function (index,dom) {
            var cityCode = $(dom).val();
            var cityName = $(dom).next().text();
            var region = {
                provinceCode:provinceCode,
                provinceName:provinceName,
                cityCode:cityCode,
                cityName:cityName
            };
            regionList.push(region);
        })
    }
    var bizRuleItemList = [];
    var bizDom = $('.insurance-box-row').find("input[name='commercial']:checked");
    if(bizDom.length > 0){
        var calculateMode = $(bizDom).parent().find('.js-commercial-calculate option:selected').val();
        var elementList = $(bizDom).parent().find("div[class='add-item']");
        $.each(elementList,function (index,element) {
            var settleConfigBasicId = $(element).find("select option:checked").val();
            var startDate = $(element).find("input[class='cxdate js-start-date']").val();
            var endDate = $(element).find("input[class='cxdate js-end-date']").val();
            var bizRuleItem = {
                calculateMode:calculateMode,
                settleConfigBasicId:settleConfigBasicId,
                startTime:startDate,
                endTime:endDate
            };
            bizRuleItemList.push(bizRuleItem);
        });
    }
    var forceRuleItemList = [];
    var forceDom = $('.insurance-box-outer').find("input[name='compulsory']:checked");
    if(forceDom.length > 0) {
        var calculate = $(forceDom).parents("div[class='insurance-box-outer']").find('.js-compulsory-calculate option:selected').val();
        var forceList = $(forceDom).parent().next().find("div[class='add-item']");
        $.each(forceList, function (index, element) {
            var settleConfigBasicId = $(element).find("select option:checked").val();
            var startDate = $(element).find("input[class='cxdate js-start-date']").val();
            var endDate = $(element).find("input[class='cxdate js-end-date']").val();
            var bizRuleItem = {
                calculateMode: calculate,
                settleConfigBasicId: settleConfigBasicId,
                startTime: startDate,
                endTime: endDate
            };
            forceRuleItemList.push(bizRuleItem);
        });
    }
    var settleShopRule = {
        insuranceCompanyId:insuranceCompanyId,
        insuranceCompanyName:insuranceCompanyName,
        applyRange:range,
        cooperationMode:cooperationMode,
        regionConfigVOList:regionList,
        shopConfigVO:shopConfigVO,
        bizRuleItemList:bizRuleItemList,
        forceRuleItemList:forceRuleItemList,
        settleCondition:settleCondition
    };
    var index = LayerUtil.load();
    Ajax.postJson({
        url:"/settle/shopRule/addShopRuleForReward",
        data:settleShopRule,
        success:function (result) {
            layer.close(index);
            if(result.success){
                layer.msg("新增成功");
                location.href = "/settle/shopRule/bountyScaleList";
            }else{
                layer.msg(result.message)
            }
        }
    })

}




