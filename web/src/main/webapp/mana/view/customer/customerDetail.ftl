<#include "/mana/view/common/header.ftl">
<#include "/mana/view/customer/carSelect.ftl">
<#include "/mana/view/customer/communicationRecord.ftl">
<link href="/mana/css/customerDetail.css" rel="stylesheet" type="text/css"/>

<#-- 隐藏文本域 -->
<input type="hidden" id="customerId" value="${customer.id}">
<#--<input type="hidden" id="customerMobile" value="${customer.customerMobile}">-->
<input type="hidden" id="vehicleId" value="${vehicleId}">
<input type="hidden" id="addressId" value="${customer.addressBO.id}">

<div class="owerBox">
    <div class="topbar"><a href="#">车主管理</a> > 车主详情</div>
    <div class="dleft">
        <#--一个卡片-->
        <div class="card-box">
            <h2>
                <span>车主信息</span>
                <div class="card-nav">
                    <span class="blue" onclick="location.href='/customer/addPage?customerId=${customer.id}'">新增车辆</span>
                    <#if customer.vehicleBO.hasSync = 1>
                        <span class="hasSync">数据已同步</span>
                    <#else>
                        <span class="noEditor">修改</span>
                    </#if>
                    <span class="editor oknav">确认</span>
                    <span class="editor cancelnav">取消</span>
                </div>
            </h2>
            <div class="tab-cont customerEdit" id="customerBasic">
                <table>
                    <tr>
                        <td><span class="tit"><i style="color:red;font-size:15px;">*</i> 车主姓名</span></td>
                        <td>
                            <span class="cot">
                            <#if customer.hasSync=1>
                                <label>${customer.customerName}</label>
                            <#else>
                                <input name="customerName" type="text" disabled value="${customer.customerName}" />
                            </#if>
                            </span>
                        </td>
                        <td><span class="tit"><i style="color:red;font-size:15px;">*</i> 车主手机</span></td>
                        <td>
                            <span class="cot">
                            <#if customer.hasSync=1>
                                <label>${customer.customerMobile}</label>
                            <#else>
                                <input name="customerMobile" type="text" disabled value="${customer.customerMobile}" />
                            </#if>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td><span class="tit">车牌号</span></td>
                        <td><span class="cot"><input name="licencePlate" type="text" disabled value="${customer.vehicleBO.licencePlate}" /></span></td>
                        <td><span class="tit">车主来源</span></td>
                        <td>
                            <span class="cot">
                                <#if customer.hasSync=1>
                                    <label>${customer.customerSourceName}</label>
                                <#else>
                                    <select name="customerSource" disabled>
                                        <option <#if customer.customerSource=0>selected</#if> value="0">淘汽</option>
                                        <option <#if customer.customerSource=1>selected</#if> value="1">门店</option>
                                    </select>
                                </#if>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td><span class="tit">品牌车型</span></td>
                        <td><span class="cot" title="${customer.vehicleBO.vehicleType}"><input name="vehicleType" class="carSelectResult" type="text" disabled value="${customer.vehicleBO.vehicleType}" /><button class="carSelectBut">车型选择</button></span></td>
                        <td><span class="tit">车险到期</span></td>
                        <td><span class="cot"><input name="insureEndDate" class="Wdate" onfocus="WdatePicker()" type="text" disabled value="${customer.vehicleBO.insureEndDate?string("yyyy-MM-dd")}"></span></td>
                    </tr>
                    <tr>
                        <td><span class="tit">车险状态</span></td>
                        <td><span class="cot"><select name="insureStatus" disabled>
                            <#--<option value=""></option>-->
                            <option <#if customer.vehicleBO.insureStatus == 0>selected</#if>  value="0">未投保</option>
                            <option <#if customer.vehicleBO.insureStatus== 1>selected</#if>  value="1">在保</option>
                            <option <#if customer.vehicleBO.insureStatus== 2>selected</#if>  value="2">脱保</option>
                        </select></span></td>
                        <td><span class="tit">车险意向</span></td>
                        <td>
                            <span class="cot">
                                <label>
                            <#switch customer.vehicleBO.insureIntention>
                                <#case 1>意向强<#break>
                                <#case 2>考虑中<#break>
                                <#case 3>无意向<#break>
                                <#case 4>已投保<#break>
                                <#default>
                            </#switch>
                                </label>
                                <select name="insureIntention" disabled class="hidden">
                                    <option value="0">请选择</option>
                                    <option <#if customer.vehicleBO.insureIntention== 1>selected</#if> value="1">意向强</option>
                                    <option <#if customer.vehicleBO.insureIntention== 2>selected</#if> value="2">考虑中</option>
                                    <option <#if customer.vehicleBO.insureIntention== 3>selected</#if> value="3">无意向</option>
                                    <option <#if customer.vehicleBO.insureIntention== 4>selected</#if> value="4">已投保</option>
                                </select>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td><span class="tit">车主地址</span></td>
                        <td colspan="3">
                            <span class="cotp dztxt">${customer.addressBO.addressStr}</span>
                            <div class="addbox">
                                <select id="sheng" name="customerProvince" class="addsel">
                                <#if customer.addressBO.customerProvinceName??>
                                    <option value="">请选择省</option>
                                    <option selected value="${customer.addressBO.customerProvince}">${customer.addressBO.customerProvinceName}</option>
                                <#else>
                                    <option value="">请选择省</option>
                                </#if>
                                </select>
                                <select id="shi" name="customerCity" class="addsel">
                                <#if customer.addressBO.customerCityName??>
                                    <option value="">请选择市</option>
                                    <option selected value="${customer.addressBO.customerCity}">${customer.addressBO.customerCityName}</option>
                                <#else>
                                    <option value="">请选择市</option>
                                </#if>
                                </select>
                                <select id="xian" name="customerDistrict" class="addsel">
                                <#if customer.addressBO.customerDistrictName??>
                                    <option value="">请选择区</option>
                                    <option selected value="${customer.addressBO.customerDistrict}">${customer.addressBO.customerDistrictName}</option>
                                <#else>
                                    <option value="">请选择区</option>
                                </#if>
                                </select>
                                <input id="dizi" name="customerAddress" type="text" class="addint" value="${customer.addressBO.customerAddress}" />
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <#--一个卡片-->
            <div class="card-box">
                <h2>
                    <span>投保信息</span>
                </h2>
                <div class="tab-cont customerEdit" id="customerInsure">
                    <table>
                        <tr>
                            <td><span class="tit">投保地区</span></td>
                            <td>
                                <span class="cotp dztxt"><#if customer.vehicleBO.insureProvinceCode!="">${customer.vehicleBO.insureProvince}</#if><#if customer.vehicleBO.insureCityCode!="">${customer.vehicleBO.insureCity}</#if></span>
                                <div class="insureAddressBox">
                                    <select id="insureSheng" name="insureProvinceCode" class="addsel">
                                    <#if customer.vehicleBO.insureProvinceCode!=''>
                                        <option value="">请选择省</option>
                                        <option selected value="${customer.vehicleBO.insureProvinceCode}">${customer.vehicleBO.insureProvince}</option>
                                    <#else>
                                        <option value="">请选择省</option>
                                    </#if>
                                    </select>
                                    <select id="insureShi" name="insureCityCode" class="addsel">
                                    <#if customer.vehicleBO.insureCityCode!=''>
                                        <option value="">请选择市</option>
                                        <option selected value="${customer.vehicleBO.insureCityCode}">${customer.vehicleBO.insureCity}</option>
                                    <#else>
                                        <option value="">请选择市</option>
                                    </#if>
                                    </select>
                                </div>
                            </td>
                            <td><span class="tit">保险公司</span></td>
                            <td>
                            <span class="cotp dztxt">${customer.vehicleBO.insureCompanyName}</span>
                            <div class="insureCompanyBox">
                                <select id="insureCompanyId" name="insureCompanyId" class="addsel-ext">
                                <#if customer.vehicleBO.insureCompanyName??>
                                    <option value="0">请选择</option>
                                    <option selected value="${customer.vehicleBO.insureCompanyId}">${customer.vehicleBO.insureCompanyName}</option>
                                <#else>
                                    <option value="0">请选择</option>
                                </#if>
                                </select>
                            </div>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="tit"><em>品牌型号</em></span></td>
                            <td><span class="cot" title="${customer.vehicleBO.insureVehicleType}"><input name="insureVehicleType" disabled value="${customer.vehicleBO.insureVehicleType}" type="text" /></span></td>
                            <td><span class="tit"><em>车主姓名</em></span></td>
                            <td>
                                <span class="cot">
                                    <#if customer.hasSync=1>
                                        <label>${customer.customerName}</label>
                                    <#else>
                                        <input name="customerName" disabled value="${customer.customerName}" type="text" />
                                    </#if>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="tit">发动机号</span></td>
                            <td><span class="cot"><input name="engineNo" disabled value="${customer.vehicleBO.engineNo}" type="text" /></span></td>
                            <td><span class="tit">证件类型</span></td>
                            <td>
                                <span class="cot">
                                    <label>
                                        <#switch customer.certificateType>
                                            <#case '120001'>居民身份证<#break>
                                            <#case '120002'>护照<#break>
                                            <#case '120003'>军人证<#break>
                                            <#default>
                                        </#switch>
                                    </label>
                                    <select name="certificateType" disabled class="hidden">
                                        <option value="">请选择</option>
                                        <option <#if customer.certificateType == '120001'>selected</#if> value="120001">居民身份证</option>
                                        <option <#if customer.certificateType == '120002'>selected</#if> value="120002">护照</option>
                                        <option <#if customer.certificateType == '120003'>selected</#if> value="120003">军人证</option>
                                    </select>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="tit">车架号</span></td>
                            <td><span class="cot"><input name="vinNo" disabled value="${customer.vehicleBO.vinNo}" type="text" /></span></td>
                            <td><span class="tit">证件号码</span></td>
                            <td>
                                <span class="cot">
                                <#if customer.hasSync=1>
                                    <label>${customer.certificateNo}</label>
                                <#else>
                                    <input type="text" disabled value="${customer.certificateNo}" name="certificateNo" />
                                </#if>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="tit">登记日期</span></td>
                            <td><span class="cot"><input disabled class="Wdate" onfocus="WdatePicker()" type="text" value="${customer.vehicleBO.vehicleRegDate?string("yyyy-MM-dd")}" name="vehicleRegDate" /></span></td>
                            <td><span class="tit">车主手机</span></td>
                            <td>
                                <span class="cot">
                                <#if customer.hasSync=1>
                                    <label>${customer.customerMobile}</label>
                                <#else>
                                    <input type="text" disabled value="${customer.customerMobile}" name="customerMobile">
                                </#if>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="tit">投保状态</span></td>
                            <td colspan="3">
                                <span class="cot">
                                    <label>
                                    <#if customer.vehicleBO.quitInsureStatus = 0>未投保</#if>
                                    <#if customer.vehicleBO.quitInsureStatus = 1>已投保</#if>
                                    <#if customer.vehicleBO.quitInsureStatus = 2>已退保</#if>
                                    </label>
                                    <#if customer.vehicleBO.quitInsureStatus = 1>
                                    <a style="margin-left: 20px;" href="/insurance/insureDetail?formId=${customer.vehicleBO.insuranceFormId}&agentId=${customer.vehicleBO.agentId}" target="_blank">保单详情</a>
                                    </#if>
                                </span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

        <#--一个卡片-->
            <div class="card-box">
                <h2>
                    <span>业务信息</span>
                </h2>
                <#-- 权限控制 -->
            <#--<@shiro.hasPermission name="customerDetail:businessInfo">-->
            <#if customer.insuranceInfoVOList??>
                <#list customer.insuranceInfoVOList as insuranceInfo>
                <ul class="tab-list">
                    <li class="gray">
                        <div class="">
                            <span class="bigger">${insuranceInfo.gmtCreate?string("yyyy-MM-dd")}</span>
                            &nbsp;
                            <span>保险模式：</span><span class="bigger">${insuranceInfo.cooperationModeStr}</span>

                        </div>
                    </li>
                    <li>
                        <#-- 虚拟保单 -->
                        <#if insuranceInfo.isVirtual = 1 >
                        <div>
                            <span>服务包名称：</span><span class="bigger">${insuranceInfo.packageName}</span>
                            &nbsp;
                            <span>服务包价格：</span><span class="bigger">${insuranceInfo.marketPrice}</span>
                        </div>
                        </#if>
                        <div>
                            <span>品牌车型：</span><span class="bigger">${insuranceInfo.configType}</span>
                        </div>
                        <div>
                            <span>服务门店：</span><span class="bigger">${insuranceInfo.agentName}</span>
                            &nbsp;&nbsp;
                            <span>支付金额：</span><span class="bigger">${insuranceInfo.payAmount}</span>
                        </div>
                        <div>
                            <#if insuranceInfo.formInfoVOList??>
                                <span>保险费：</span>
                                <span class="bigger">
                                    <#list insuranceInfo.formInfoVOList as form>
                                        <a class="lian" href="/insurance/insureDetail?formId=${form.formId}&agentId=${insuranceInfo.agentId}" target="_blank">
                                        ${form.insureTypeStr} ${form.insuredFee}
                                        </a>
                                        &nbsp;&nbsp;
                                    </#list>
                                </span>
                            </#if>
                        </div>

                    </li>

                </ul>
                </#list>
            </#if>
            <#-- 优惠信息 -->
            <#if customer.preferentialVOList??>
                <#list customer.preferentialVOList as preferential>
                    <ul class="tab-list">
                        <li class="gray">
                            <div class="">
                                <span class="bigger">${preferential.sendDate?string("yyyy-MM-dd")}</span>
                                &nbsp;&nbsp;
                                <span>福利：</span><span class="bigger">${preferential.preferentialContent}</span>
                                &nbsp;&nbsp;
                                <span>登记人：</span><span class="bigger">${preferential.creator}</span>
                            </div>
                        </li>
                    </ul>
                </#list>
            </#if>
            <#--</@shiro.hasPermission>-->
            </div>

    </div>
    <div class="dright">
    <#--一个卡片-->
        <div class="card-box" id="communicateRecord">
            <h2>
                <span>沟通纪录</span>
                <div class="card-nav">
                    <span id="addCommunicationRecord">新增纪录</span>
                </div>
            </h2>
            <ul class="tab-list" id="js-communication-list">
                <#list customer.vehicleBO.recordBOPagingResult.list as record>
                <li class="gray">
                    <div class="left">
                        <span class="bigger">${record.communicateDate?string("yyyy-MM-dd")}</span>
                        <span>沟通渠道：</span><span class="bigger">${record.communicateChannelName}</span>
                    </div>
                    <div class="right"><span>登记人：</span><span class="bigger">${record.creator}</span></div>
                    <div class="clear"></div>
                </li>
                <li>
                    <div class="listw">
                        <div class="left"><span>推荐门店：</span></div>
                        <div class="right"><span class="bigger">${record.recommendShopInfo}</span></div>
                        <div class="clear"></div>
                    </div>
                    <div class="listw">
                        <div class="left"><span>咨询内容：</span></div>
                        <div class="right"><span class="bigger">${record.communicateContent}</span></div>
                        <div class="clear"></div>
                    </div>
                </li>
                </#list>
            </ul>
            <#if customer.vehicleBO.recordBOPagingResult.list?size < customer.vehicleBO.recordBOPagingResult.total>
            <p class="gengd"><a href="javascript:" id="js-more">更多…</a></p>
            </#if>
        </div>
    </div>
    <div class="clear"></div>
</div>
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
<script type="text/html" id="communicateListTemplate">
    {{each dataList as record index}}
    <li class="gray">
        <div class="left">
            <span class="bigger">{{record.communicateDate | shortDateHelper}}</span>
            <span>沟通渠道：</span><span class="bigger">{{record.communicateChannelName}}</span>
        </div>
        <div class="right"><span>登记人：</span><span class="bigger">{{record.creator}}</span></div>
        <div class="clear"></div>
    </li>
    <li>
        <div class="listw">
            <div class="left"><span>推荐门店：</span></div>
            <div class="right"><span class="bigger">{{record.recommendShopInfo}}</span></div>
            <div class="clear"></div>
        </div>
        <div class="listw">
            <div class="left"><span>咨询内容：</span></div>
            <div class="right"><span class="bigger">{{record.communicateContent}}</span></div>
            <div class="clear"></div>
        </div>
    </li>
    {{/each}}
</script>
<#include "/mana/view/common/footer.ftl">
<script src="/mana/js/customer/customerDetail.js"></script>
<script>
    Mana.run();
</script>