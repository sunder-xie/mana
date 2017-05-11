/**
 * Created by ZhuangQianQian on 16/12/26.
 */
$(document).ready(function(){

    initInsuranceCompany();

    carSelectInit({
        dom : '.select-carType',
        print : '.mana-carResult'
    });

    /* 获取客户地址 */
    getCustomerAddress();

});
/*车主信息和投保车主信息联动*/
var autoComplete = function(obj){
    var data = $(obj).data('info');
    $(".js-"+data).val($(obj).val());
};

$(document).on('click','#addBtn',function(){
    toAddCustomer();
}).on("click",".addInsureInfo-title",function(){
    iconChange($(this),function(_obj){
        var next = $(_obj).nextAll();
        next.each(function(){
            $(this).slideToggle();
        })
    });
}).on("input propertychange",".js-complete",function(){
    autoComplete($(this));
});
/* ========== hzt ========== */

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

/* 添加客户 */
function toAddCustomer(){
    var index = LayerUtil.load();

    var formData = MaNa.jqSerializeForm('#addCustomerForm');

    try{
        checkAddForm(formData);
    }catch(message){
        layer.close(index);
        LayerUtil.msg(message);
        return false;
    }

    /* 设置推荐门店信息 */
    setRecommendShopInfo(formData);

    //alert(JSON.stringify(formData));

    var url = '/customer/addCustomer';
    var msg = '新增车主成功';
    if(formData.customerId != ''){
        url = '/customer/addCustomerVehicle';
        msg = '新增车辆成功';
        if(_addressId !== undefined){
            formData.addressId = _addressId;
        }
    }

    Ajax.post({
        url: url,
        data: formData,
        success: function(result){
            layer.close(index);

            if(result.success){
                LayerUtil.msgFun(msg, function(){
                    location.reload();
                });
            }else{
                LayerUtil.msg(result.message);
            }

        }
    });

}

function checkInt(i){
    if(i==''){
        return null;
    }
    return i;
}
/* 检查地址信息 */
function checkAddress(formData){
    var district = formData.customerDistrict;
    var city = formData.customerCity;
    var province =  formData.customerProvince;
    if(district != null){
        if(province==null){
            throw '请选择省';
        }
        if(city==null){
            throw '请选择市';
        }
    }
    if(city != null){
        if(province==null){
            throw '请选择省';
        }
    }
}

function checkAddForm(formData){
    if(formData.customerName==''){
        throw '请填写车主姓名';
    }
    if(!MaNa.isMobile(formData.customerMobile)){
        throw '请填写正确的手机号';
    }
    var licencePlate = formData.licencePlate;
    if(licencePlate!='' && !MaNa.isLicencePlate(licencePlate)){
        throw '请填写正确的车牌号';
    }
    formData.customerProvince = checkInt(formData.customerProvince);
    formData.customerCity = checkInt(formData.customerCity);
    formData.customerDistrict = checkInt(formData.customerDistrict);

    checkAddress(formData);

    formData.insureStatus = checkInt(formData.insureStatus);
    formData.insureIntention = checkInt(formData.insureIntention);
    formData.insureCompanyId = checkInt(formData.insureCompanyId);

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

/* 初始化客户地址 */
var _addressId;
function getCustomerAddress(){
    var customerId = $('#customerId').val();
    if(customerId==''){
        return;
    }

    Ajax.get({
        url: '/customer/getAddressByCustomerId',
        data: {customerId: customerId},
        success: function (result) {
            //console.log(result);
            if (result.success) {
                _addressId = result.data.id;
                initAddress(result.data);
            }
        }
    });
}
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

