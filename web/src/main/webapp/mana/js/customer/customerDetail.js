/**
 * Created by zhouheng on 16/12/27.
 */
//当前点击更多时请求第二页数据
var _pageIndex = 2;
//每页5条数据
var _pageSize = 5;

/*新增沟通记录*/
$(document).on("click", "#addCommunicationRecord", function () {
    LayerUtil.open({
        title: false,
        type: 1,
        btn: false,
        content: $("#communication-record"),
        area: ['auto', '600px']
    });

    //重置输入内容
    resetCommunicateRecord();
    //重置沟通渠道
    $('#communication-way').html($('#communicateChannelTemplate').html());

    Ajax.get({
        url: '/customer/getAddressByCustomerId',
        data: {customerId: $('#customerId').val()},
        success: function (result) {
            //console.log(result);
            var addressId;
            if (result.success) {
                addressId = result.data.id;
                initAddress(result.data);
            }

            $('#addBtn').unbind().click(function () {
                toAddCommunicateRecord(addressId);
            });
        }
    });

});

/** 点击更多:获取更多一页的沟通记录数据*/
$(document).on('click', '#js-more', function () {

    var vehicleId = $('#vehicleId').val();

    var reqParam = {
        pageSize: _pageSize,
        pageIndex: _pageIndex,
        customerVehicleId: vehicleId
    };

    Ajax.get({
        url: '/customer/getCommunicationRecord',
        data: reqParam,
        success: function (result) {

            var total = result.total;
            if (total <= _pageIndex * _pageSize) {
                $('.gengd').hide();
            }else{
                $('.gengd').show();
            }

            $('#js-communication-list').append(template('communicateListTemplate', {dataList: result.list}));

            _pageIndex = _pageIndex + 1;

        }
    });

});

/* 初始化地址信息 */
function initAddress(addressObj) {
    var province = addressObj.customerProvince;
    var city = addressObj.customerCity;
    var district = addressObj.customerDistrict;

    if (province > 0) {
        $('#carOwner-province').val(province);
        initRegionSelect(province, '#carOwner-city', '市', undefined, city);
    }
    if (city > 0) {
        $('#carOwner-city').val(city);
        initRegionSelect(city, '#carOwner-area', '区', undefined, district);
    }
    $('#customerAddress').val(addressObj.customerAddress);

    //加载门店信息
    var districtName = addressObj.districtName;
    if(districtName!==null && districtName!='' && district>0){
        toGetShopInfo({districtId: district}, districtName);
    }else{
        var cityName = addressObj.cityName;
        if(cityName!==null && cityName!='' && city>0){
            toGetShopInfo({cityId: city}, cityName);
        }
    }

}


/* 添加聊天记录 */
function toAddCommunicateRecord(addressId) {
    var index = LayerUtil.load();

    var customerId = $('#customerId').val();
    var customerMobile = $('#customerMobile').val();
    var vehicleId = $('#vehicleId').val();
    var communicateChannel = $('#communication-way').val();
    var communicateDate = $('#communicationDate').val();
    var communicateContent = $('#communicateContent').val().trim();

    var customerProvince = $('#carOwner-province').val();
    var customerCity = $('#carOwner-city').val();
    var customerDistrict = $('#carOwner-area').val();
    var customerAddress = $('#customerAddress').val().trim();

    var formData = {
        customerId: customerId,
        customerMobile: customerMobile,
        customerVehicleId: vehicleId,
        communicateChannel: communicateChannel,
        communicateContent: communicateContent
    };
    if (addressId != undefined) {
        formData.addressId = addressId;
    }

    if (communicateDate != '') {
        formData.communicateDate = communicateDate;
    }

    if (customerProvince != '') {
        formData.customerProvince = customerProvince;
    }
    if (customerCity != '') {
        formData.customerCity = customerCity;
    }
    if (customerDistrict != '') {
        formData.customerDistrict = customerDistrict;
    }
    if (customerAddress != '') {
        formData.customerAddress = customerAddress;
    }

    /* 设置推荐门店信息 */
    setRecommendShopInfo(formData);

    Ajax.post({
        url: '/customer/addCommunicateRecord',
        data: formData,
        success: function (result) {
            layer.close(index);

            if (result.success) {
                LayerUtil.msgFun('添加沟通记录成功', function () {
                    $('#js-communication-list').html("");
                    //关闭弹窗
                    layer.closeAll();
                    //获取沟通记录列表
                    getCommunicateRecordList();
                });
            } else {
                alert(result.message);
            }
        }
    });

}

/**
 * 获取沟通记录列表
 */
function getCommunicateRecordList(){

    var vehicleId = $('#vehicleId').val();

    var reqParam = {
        pageSize: _pageSize,
        pageIndex: 1,
        customerVehicleId: vehicleId
    };

    Ajax.get({
        url: '/customer/getCommunicationRecord',
        data: reqParam,
        success: function (result) {
            _pageIndex = 2;

            var total = result.total;
            if (total <= _pageSize) {
                $('.gengd').hide();
            }else {
                $('.gengd').show();
            }

            $('#js-communication-list').append(template('communicateListTemplate', {dataList: result.list}));
        }
    });
}

/**
 * 获取所有保险公司数据并初始化下拉框
 */
function initInsureCompanySelect(a){

    var action = '/insurance/getAllCompanyList';
    Ajax.get({
        url: action,
        data : {},
        success: function (result) {
            var companyList = result.data;

            var _h = '<option value="0">请选择</option>';
            for (var i = 0; i < companyList.length; i++) {
                _h += '<option{2} value="{0}">{1}</option>'.format(companyList[i].id, companyList[i].companyName, a == companyList[i].id ? ' selected' : '');
            }
            $('#insureCompanyId').html(_h);

        }
    });
}




var Mana = {};
Mana.run = (function () {

    "use strict";

    var baseUrl = '/';

    String.prototype.format = function () {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };

    function getDate(action, params, callback, noLoader) {
        var url = baseUrl + action,
            _params = {
                _t: Math.random()
            };
        if (arguments.length >= 3) {
            $.extend(_params, params);
        } else {
            callback = params;
        }
        if (!noLoader) {
            var loadingId = layer.load(2, {time: 30000});
        }
        return $.getJSON(url, _params, function (json) {
            noLoader || layer.close(loadingId);
            callback(json);
        });
    }

    function postDate(action, params, callback, noLoader) {
        var url = baseUrl + action,
            _params = {
                _t: Math.random()
            };
        if (arguments.length >= 3) {
            $.extend(_params, params);
        } else {
            callback = params;
        }
        if (!noLoader) {
            var loadingId = layer.load(2, {time: 30000});
        }
        return $.ajax({
            url: url,
            type: "POST",
            data: _params,
//            contentType: "application/json",
            dataType: "json",
            success: function (json) {
                noLoader || layer.close(loadingId);
                callback(json);
            }
        });
    }

    var Model = {

        regionListByPid: function (pid, callback, beforeSend) {
            //if($.isFunction(beforeSend)){
            //    beforeSend();
            //}
            if (!Server.isPid(pid)) {
                return false;
            }
            var action = 'common/regionListByPid';
            var params;
            if (callback == undefined) {
                callback = pid;
            } else {
                params = {pid: pid};
            }
            return getDate(action, params, callback);
        },

        updateCustomerAndVehicle: function (params, callback) {
            var action = 'customer/updateCustomerAndVehicle';
            return postDate(action, params, callback);
        }

    };

    var Server = {

        init: function () {
            carSelectInit({
                dom: '.carSelectBut',
                print: '.carSelectResult'
            });
            var _this = this;
            $(document)
                .on('change', 'input[name="customerName"]', function () {
                    var v = $.trim(this.value);
                    if (v == '') {
                        layer.tips('请填写车主姓名', this, {
                            tips: [1, '#444']
                        });
                    }
                    //if (!/^[\u4E00-\u9FA5\uF900-\uFA2D]{2,20}|[ a-zA-Z\.]{2,20}$/.test(v)) {
                    //    layer.tips('姓名格式不合法', this, {
                    //        tips: [1, '#444']
                    //    });
                    //    return;
                    //}
                    $('input[name="customerName"]').val(v);
                })
                .on('change', 'input[name="customerMobile"]', function () {
                    var v = $.trim(this.value);
                    if (!MaNa.isMobile(v)) {
                        layer.tips('请填写正确的手机号', this, {
                            tips: [1, '#444']
                        });
                    }
                    $('input[name="customerMobile"]').val(v);
                })
                //车主地址设置
                .on('change', '#sheng', function () {
                    Server.clearRegionSelect('#shi');
                    Server.clearRegionSelect('#xian');

                    Model.regionListByPid(this.value, function (json) {
                        var _h = '<option value="">请选择市</option>';
                        for (var i = 0; i < json.length; i++) {
                            _h += '<option value="{0}">{1}</option>'.format(json[i].id, json[i].regionName);
                        }
                        $('#shi').html(_h);
                    });

                })
                .on('change', '#shi', function () {
                    Server.clearRegionSelect('#xian');

                    Model.regionListByPid(this.value, function (json) {
                        var _h = '<option value="">请选择区</option>';
                        for (var i = 0; i < json.length; i++) {
                            _h += '<option value="{0}">{1}</option>'.format(json[i].id, json[i].regionName);
                        }
                        $('#xian').html(_h);
                    });
                })
                //投保地址设置
                .on('change', '#insureSheng', function () {
                    Server.clearRegionSelect('#insureShi');

                    Model.regionListByPid(this.value, function (json) {
                        var _h = '<option value="">请选择市</option>';
                        for (var i = 0; i < json.length; i++) {
                            _h += '<option value="{0}">{1}</option>'.format(json[i].id, json[i].regionName);
                        }
                        $('#insureShi').html(_h);
                    });

                })
                .on('click', '.cancelnav', function () {
                    $('.addbox').html(_this.oldAddBox);
                    $('.insureAddressBox').html(_this.insureAddBox);
                    $('.insureCompanyBox').html(_this.insureCompanyBox);
                    _this.offEditor(_this.cdata);
                })
                .on('click', '.oknav', function () {
                    var data = getFormData('.customerEdit');
                    if (!checkFormData(data)) {
                        return false;
                    }

                    Model.updateCustomerAndVehicle($.extend({
                        id: $('#customerId').val(),
                        customerMobile: $('#customerMobile').val(),
                        vehicleId: $('#vehicleId').val(),
                        addressId: $('#addressId').val()
                    }, data), function (json) {
                        if (json.success) {
                            LayerUtil.msgFun('车主信息修改成功', function () {
                                _this.offEditor(data);
                                location.reload();
                            })
                        } else {
                            layer.msg(json.message);
                        }
                    });
                })
                .on('click', '.noEditor', function () {
                    _this.onEditor();
                    _this.cdata = getFormData('.customerEdit');
                    _this.initAddress();
                    initInsureCompanySelect(_this.cdata.insureCompanyId);
                });
        },

        oldAddBox: '',
        clearRegionSelect: function (el) {
            var $el = $(el);
            $el.val('');
            $el.get(0).options.length = 1;
        },
        isPid: function (pid) {
            if (pid === undefined || pid === null || pid == '' || pid < 1) {
                return false;
            }
            return true;
        },
        initAddress: function () {
            var a = this.cdata.customerProvince;
            var b = this.cdata.customerCity;
            var c = this.cdata.customerDistrict;
            var d = this.cdata['insureProvinceCode'];
            var e = this.cdata['insureCityCode'];
            Model.regionListByPid(function (json) {
                var _h = '<option value="">请选择省</option>';
                for (var i = 0; i < json.length; i++) {
                    _h += '<option{2} value="{0}">{1}</option>'.format(json[i].id, json[i].regionName, a == json[i].id ? ' selected' : '');
                }
                $('#sheng').html(_h);

                var _p = '<option value="">请选择省</option>';
                for (var j = 0; j < json.length; j++) {
                    _p += '<option{2} value="{0}">{1}</option>'.format(json[j].id, json[j].regionName, d == json[j].id ? ' selected' : '');
                }
                $('#insureSheng').html(_p);
            });

            if (this.isPid(a)) {
                Model.regionListByPid(a, function (json) {
                    var _h = '<option value="">请选择市</option>';
                    for (var i = 0; i < json.length; i++) {
                        _h += '<option{2} value="{0}">{1}</option>'.format(json[i].id, json[i].regionName, b == json[i].id ? ' selected' : '');
                    }
                    $('#shi').html(_h);

                });
            }

            if (this.isPid(b)) {
                Model.regionListByPid(b, function (json) {
                    var _h = '<option value="">请选择区</option>';
                    for (var i = 0; i < json.length; i++) {
                        _h += '<option{2} value="{0}">{1}</option>'.format(json[i].id, json[i].regionName, c == json[i].id ? ' selected' : '');
                    }
                    $('#xian').html(_h);
                });
            }

            if (this.isPid(d)) {
                Model.regionListByPid(d, function (json) {
                    var _c = '<option value="">请选择市</option>';
                    for (var j = 0; j < json.length; j++) {
                        _c += '<option{2} value="{0}">{1}</option>'.format(json[j].id, json[j].regionName, e == json[j].id ? ' selected' : '');
                    }
                    $('#insureShi').html(_c);
                });
            }

        },
        cdata: null,

        onEditor: function () {
            $('.noEditor').css('display', 'none');
            $('.editor').css('display', 'inline-block');
            $('.carSelectBut').css('display', 'inline-block');
            $('.customerEdit input').add('.customerEdit select').not('select[name="quitInsureStatus"]').removeAttr('disabled');
            $('.dztxt').css('display', 'none');
            this.oldAddBox = $('.addbox').css('display', 'inline-block').html();
            this.insureAddBox = $('.insureAddressBox').css('display', 'inline-block').html();
            this.insureCompanyBox = $('.insureCompanyBox').css('display', 'inline-block').html();

            this.showSelect($('select[name="insureIntention"]'));
            this.showSelect($('select[name="certificateType"]'));
            this.showSelect($('select[name="insureCompanyId"]'));
        },

        offEditor: function (data) {
            $('.noEditor').css('display', 'inline-block');
            $('.editor').css('display', 'none');
            $('.carSelectBut').css('display', 'none');
            $('.customerEdit input').add('.customerEdit select').each(function () {
                this.value = data[this.name];
            }).attr('disabled', true);
            $('.dztxt').css('display', 'inline-block');
            $('.addbox').css('display', 'none');
            $('.insureAddressBox').css('display', 'none');
            $('.insureCompanyBox').css('display', 'none');

            this.hiddenSelect($('select[name="insureIntention"]'));
            this.hiddenSelect($('select[name="certificateType"]'));
            this.hiddenSelect($('select[name="insureCompanyId"]'));
        },

        showSelect: function ($el) {
            $el.prev().addClass('hidden');
            $el.removeClass('hidden');
        },
        hiddenSelect: function ($el) {
            $el.addClass('hidden');
            $el.prev().removeClass('hidden');
        },

        getAddressStr: function () {
            var province = $('#sheng option:selected').text();
            var city = $('#shi option:selected').text();
            var district = $('#xian option:selected').text();

        }

    };

    function checkFormData(formData){
        var checkFlag = true;
        var customerMobile = formData.customerMobile;
        if(customerMobile!==undefined && !MaNa.isMobile(customerMobile)){
            layer.tips('请填写正确的手机号', $('input[name="customerMobile"]'), {
                tips: [1, '#444'],
                tipsMore: true
            });
            checkFlag = false;
        }
        var customerName = formData.customerName;
        if(customerName!==undefined && customerName==''){
            layer.tips('请填写车主姓名', $('input[name="customerName"]'), {
                tips: [1, '#444'],
                tipsMore: true
            });
            checkFlag = false;
        }
        var licencePlate = formData.licencePlate;
        if(licencePlate!='' && !MaNa.isLicencePlate(licencePlate)){
            layer.tips('请填写正确的车牌号', $('input[name="licencePlate"]'), {
                tips: [3, '#444'],
                tipsMore: true
            });
            checkFlag = false;
        }

        return checkFlag;
    }

    function getFormData(selector) {
        var data = {};
        $(selector + ' select').add(selector + ' input').each(function () {
            data[this.name] = $.trim(this.value);
        });

        if(data.insureCityCode != ''){
            data['insureCity'] = $("#insureShi option:selected").text();
        }else{
            data['insureCity'] = '';
        }
        if(data.insureProvinceCode != ''){
            data['insureProvince'] = $("#insureSheng option:selected").text();
        }else{
            data['insureProvince'] = '';
        }

        return data;
    }

    return function () {
        Server.init();
    }

})();

