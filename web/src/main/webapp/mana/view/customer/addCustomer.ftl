<#include "/mana/view/common/header.ftl">
<#include "/mana/view/customer/carSelect.ftl">
<link href="/mana/css/addCustomer.css" rel="stylesheet" type="text/css"/>
<form id="addCustomerForm">

<#-- 隐藏文本域 -->
    <input id="customerId" type="hidden" name="customerId" value="${customerInfo.id}">

    <div class="addCustomer">
        <p class="addCustomer-title">
        <#if customerInfo??>
            新增车辆
        <#else>
            新增车主
        </#if>
        </p>
        <div class="addCarOwnerInfo">
            <p class="carOwnerInfo-title"><span>车主信息</span></p>
            <ul>
                <li class="infoList">
                    <div>
                        <span><em class="necessary">*</em>车主姓名</span>
                        <input name="customerName" type="text" class="carOwnerName js-complete" data-info="carOwnerName" placeholder="必填" <#if customerInfo??>readonly</#if> value="${customerInfo.customerName}" />
                    </div>
                    <div>
                        <span><em class="necessary">*</em>车主手机</span>
                        <input name="customerMobile" type="text" class="phoneNum js-complete" data-info="phoneNum" placeholder="必填" <#if customerInfo??>readonly</#if> value="${customerInfo.customerMobile}" />
                    </div>
                    <div>
                        <span>车牌号</span>
                        <input name="licencePlate" class="js-complete" data-info="licencePlate" type="text"/>
                    </div>
                    <div>
                        <span>车主来源</span>
                        <select <#if customerInfo??>disabled</#if> name="customerSource" id="carOwner-resource">
                            <option <#if customerInfo.customerSource=0>selected</#if> value="0">淘汽</option>
                            <option <#if customerInfo.customerSource=1>selected</#if> value="1">门店</option>
                        </select>
                    </div>
                </li>
                <li class="infoList">
                    <div>
                        <span>品牌车型</span>
                        <input class="mana-carResult" name="vehicleType" type="text"/>
                        <span class="select-carType">选择车型</span>
                    </div>
                    <div>
                        <span>车险状态</span>
                        <select name="insureStatus" id="insurance-status">
                            <option value="0">未投保</option>
                            <option value="1">在保</option>
                            <option value="2">脱保</option>
                        </select>
                    </div>
                    <div>
                        <span>车险意向</span>
                        <select name="insureIntention" id="intention">
                            <option value="">请选择</option>
                            <option value="1">意向强</option>
                            <option value="2">考虑中</option>
                            <option value="3">无意向</option>
                            <option value="4">已投保</option>
                        </select>
                    </div>
                </li>
                <li class="infoList">
                    <div>
                        <span>车险到期日</span>
                        <input name="insureEndDate" id="insuranceEndDate" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
                    </div>
                </li>
            </ul>
        </div>
        <div class="addInsureInfo">
            <p class="addInsureInfo-title down"><span>投保信息</span><span class="action">展开</span><i class="fa Z-angle fa-angle-down"></i></p>
            <ul class="basicInfo">
                <li class="infoList"><span class="Z-title">基本信息</span></li>
                <li class="infoList">
                    <span>保险公司</span>
                    <select name="insureCompanyId" id="insurance-company">
                        <option value="">请选择</option>
                    </select>
                </li>
                <li class="infoList">
                    <span>投保地区</span>
                    <select name="insureProvinceCode" id="insure-province">
                        <option value="">请选择省</option>
                    </select>
                    <select name="insureCityCode" id="insure-city">
                        <option value="">请选择市</option>
                    </select>
                </li>
                <li class="infoList">
                    <span>车牌号</span>
                    <input name="licencePlate2" class="js-licencePlate" readonly type="text"/>
                    <input name="hasLicencePlate" class="noLicensePlate" type="checkbox"/><span>尚未取得车牌</span>
                </li>
            </ul>
            <ul class="carInfo">
                <li class="infoList"><span class="Z-title">车辆信息</span></li>
                <li class="infoList">
                    <span>品牌型号</span>
                    <input name="insureVehicleType" type="text"/>
                </li>
                <li class="infoList">
                    <span>发动机号</span>
                    <input name="engineNo" type="text"/>
                </li>
                <li class="infoList">
                    <span>车架号</span>
                    <input name="vinNo" type="text"/>
                </li>
                <li class="infoList">
                    <span>车辆登记</span>
                    <input name="vehicleRegDate" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                </li>
            </ul>
            <ul class="carOwnerInfo">
                <li class="infoList"><span class="Z-title">车主信息</span></li>
                <li class="infoList">
                    <span>车主姓名</span>
                    <input name="customerName2" class="js-carOwnerName" type="text" readonly value="${customerInfo.customerName}"/>
                </li>
                <li class="infoList">
                    <span>证件类型</span>
                    <select <#if customerInfo.hasSync=1>disabled</#if> name="certificateType" id="certificate-type">
                        <option value="">请选择</option>
                        <option <#if customerInfo.certificateType='120001'>selected</#if> value="120001">居民身份证</option>
                        <option <#if customerInfo.certificateType='120002'>selected</#if> value="120002">护照</option>
                        <option <#if customerInfo.certificateType='120003'>selected</#if> value="120003">军人证</option>
                    </select>
                </li>
                <li class="infoList">
                    <span>证件号码</span>
                    <input name="certificateNo" type="text" <#if customerInfo.hasSync=1>readonly</#if> value="${customerInfo.certificateNo}"/>
                </li>
                <li class="infoList">
                    <span>车主手机</span>
                    <input name="customerMobile2" class="js-phoneNum" type="text" readonly value="${customerInfo.customerMobile}" />
                </li>
            </ul>
        </div>
        <#include "/mana/view/customer/communicationRecord.ftl">
    </div>
</form>

<#-- 模板 -->
<script type="text/html" id="insuranceCompanyTemp">
    <option value="">请选择</option>
    {{each data as obj idx}}
    <option value="{{obj.id}}">{{obj.companyName}}</option>
    {{/each}}
</script>
<script type="text/html" id="regionTemp">
    <option value="">请选择{{msg}}</option>
    {{each data as obj idx}}
    <option value="{{obj.id}}">{{obj.regionName}}</option>
    {{/each}}
</script>


<#include "/mana/view/common/footer.ftl">
<script src="/mana/js/customer/addCustomer.js"></script>
