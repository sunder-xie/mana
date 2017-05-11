/**
 * Created by huangzhangting on 16/12/27.
 */
$(document).ready(function(){

    initInsuranceCompany();

    initProvinceSelect();
    initCitySelect();

    $('#addBtn').click(function(){
        toAddCustomer();
    });

});

/* 初始化保险公司 */
function initInsuranceCompany(){
    Ajax.get({
        url: '/common/insuranceCompanyList',
        success: function(result){
            var html = template('insuranceCompanyTemp', {data: result});
            $('#insurance-company').html(html);
        }
    });
}

/* 初始化省份 */
function initProvinceSelect(){
    Ajax.get({
        url: '/common/regionListByPid',
        success: function(result){
            var html = template('regionTemp', {data: result, msg: '省'});
            $('#insure-province').html(html).change(function(){
                provinceChange(this.value, '#insure-city');
            });
            $('#carOwner-province').html(html).change(function(){
                provinceChange(this.value, '#carOwner-city', '#carOwner-area');
            });
        }
    });
}
/* 初始化城市 */
function provinceChange(pid, el, clearEl){
    initRegionSelect(pid, el, '市', clearEl);
}

/* 初始化地区选择框 */
function initRegionSelect(pid, el, msg, clearEl){
    if(pid==''){
        return;
    }
    Ajax.get({
        url: '/common/regionListByPid',
        data: {pid: pid},
        success: function(result){
            var html = template('regionTemp', {data: result, msg: msg});
            $(el).html(html);
        }
    });
    if(clearEl !== undefined){
        $(clearEl).val('');
        $(clearEl).get(0).options.length = 1;
    }
}

/* 初始化城市选择框 */
function initCitySelect(){
    $('#carOwner-city').change(function(){
        initRegionSelect(this.value, '#carOwner-area', '区');
    });
}


/* 添加客户 */
function toAddCustomer(){
    var formData = MaNa.jqSerializeForm('#addCustomerForm');

    try{
        checkAddForm(formData);
    }catch(message){
        LayerUtil.msg(message);
        return false;
    }

    //alert(JSON.stringify(formData));

    var url = '/customer/addCustomer';
    var msg = '新增车主成功';
    if(formData.customerId != ''){
        url = '/customer/addCustomerVehicle';
        msg = '新增车辆成功';
    }

    Ajax.post({
        url: url,
        data: formData,
        success: function(result){
            if(result.success){
                LayerUtil.msgFun(msg, function(){
                    location.reload();
                });
            }else{
                LayerUtil.msg(result.message);
            }

            layer.close(index);
        }
    });

}

function checkInt(i){
    if(i==''){
        return null;
    }
    return i;
}

function checkAddForm(formData){
    if(formData.customerName==''){
        throw '请填写车主姓名';
    }
    if(!MaNa.isMobile(formData.customerMobile)){
        throw '请填写正确的手机号';
    }
    formData.insureStatus = checkInt(formData.insureStatus);
    formData.insureIntention = checkInt(formData.insureIntention);
    formData.insureCompanyId = checkInt(formData.insureCompanyId);

    formData.customerProvince = checkInt(formData.customerProvince);
    formData.customerCity = checkInt(formData.customerCity);
    formData.customerDistrict = checkInt(formData.customerDistrict);

    if(formData.insureProvinceCode != ''){
        formData.insureProvince = $("#insure-province").find("option:selected").text();
    }
    if(formData.insureCityCode != ''){
        formData.insureCity = $("#insure-city").find("option:selected").text();
    }

    var hasLicencePlate = $('input[name="hasLicencePlate"]').get(0);
    if(hasLicencePlate.checked){
        formData.hasLicencePlate = 1;
    }else{
        formData.hasLicencePlate = null;
    }
}

