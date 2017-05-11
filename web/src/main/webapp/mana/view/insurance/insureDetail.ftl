<#include "/mana/view/common/header.ftl">
<link href="/mana/css/detail.css" rel="stylesheet" type="text/css"/>
<input type="hidden" id="formId" value="${formId}">
<input type="hidden" id="agentId" value="${agentId}">
<div class="insuranceDetail">
    <p class="insuranceDetail-title">
        <span>保单详情</span>
        <#--<span id="back">返回</span>-->
    </p>
    <div class="insuranceInfo">
        <p class="insuranceInfo-title">
            <span>保单信息</span>
        </p>
        <ul>
            <li>
                <div>
                    <span>保单号：</span>
                    <span>${insurance.outerInsuranceFormNo}</span>
                </div>
                <div>
                    <span>缴费时间：</span>
                    <span>${insurance.payTime?string("yyyy-MM-dd HH:mm:ss")}</span>
                </div>
            </li>
            <li>
                <div>
                    <span>保单状态：</span>
                    <span>${insurance.formStatusName}</span>
                </div>
                <div>
                    <span>退保金额：</span>
                    <span></span>
                </div>
            </li>
            <li>
                <div>
                    <span>保费：</span>
                    <span><#if insurance.insuredFee??>￥</#if> ${insurance.insuredFee}</span>
                </div>
            </li>
        </ul>
    </div>
    <div class="carDetailInfo">
        <p class="carDetailInfo-title"><span>车辆详细信息</span></p>
        <ul>
            <li>
                <div>
                    <span>车牌号码：</span>
                    <span>${insurance.basicBO.vehicleSn}</span>
                </div>
                <div>
                    <span>投保地：</span>
                    <span>${insurance.basicBO.insuredProvince}-${insurance.basicBO.insuredCity}</span>
                </div>
            </li>
            <li>
                <div>
                    <span>发动机号：</span>
                    <span>${insurance.basicBO.carEngineSn}</span>
                </div>
                <div>
                    <span>车架号：</span>
                    <span>${insurance.basicBO.carFrameSn}</span>
                </div>
            </li>
            <li>
                <div>
                    <span>品牌型号：</span>
                    <span>${insurance.basicBO.carConfigType}</span>
                </div>
            </li>
        </ul>
    </div>
    <div class="insuranceContent">
        <p class="insuranceContent-title">保障内容</p>
        <table class="mana-table">
            <thead>
            <tr>
                <th>保障项目</th>
                <th>保额</th>
                <th>不计免赔</th>
                <th>保费</th>
            </tr>
            </thead>
            <tbody>
            <#list insurance.itemBOList as item>
            <tr>
                <td>${item.insuranceName}</td>
                <td>${item.insuranceAmount}</td>
                <td>${item.deductiveName}</td>
                <td>${item.insuranceFee}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <div class="insuranceDate">
            <span>${insurance.insuranceTypeDescription}生效时间：${insurance.packageStartTime?string("yyyy-MM-dd HH:mm:ss")}</span>
            <span>${insurance.insuranceTypeDescription}失效时间：${insurance.packageEndTime?string("yyyy-MM-dd HH:mm:ss")}</span>
        </div>
    </div>
    <div class="carOwnerInfo">
        <p class="carOwnerInfo-title"><span>车主信息</span></p>
        <ul>
            <li>
                <div>
                    <span>姓名：</span>
                    <span>${insurance.basicBO.vehicleOwnerName}</span>
                </div>
                <div>
                    <span>手机号码：</span>
                    <span>${insurance.basicBO.vehicleOwnerPhone}</span>
                </div>
            </li>
            <li>
                <div>
                    <span>证件类型：</span>
                    <span>${insurance.basicBO.vehicleOwnerCertTypeName}</span>
                </div>
                <div>
                    <span>证件号码：</span>
                    <span>${insurance.basicBO.vehicleOwnerCertCode}</span>
                </div>
            </li>
        </ul>
    </div>
    <div class="insuredInfo">
        <p class="insuredInfo-title"><span>被保人信息</span></p>
        <ul>
            <li>
                <div>
                    <span>姓名：</span>
                    <span>${insurance.basicBO.insuredName}</span>
                </div>
                <div>
                    <span>手机号码：</span>
                    <span>${insurance.basicBO.insuredPhone}</span>
                </div>
            </li>
            <li>
                <div>
                    <span>证件类型：</span>
                    <span>${insurance.basicBO.insuredCertTypeName}</span>
                </div>
                <div>
                    <span>证件号码：</span>
                    <span>${insurance.basicBO.insuredCertCode}</span>
                </div>
            </li>
        </ul>
    </div>
    <div class="insurerInfo">
        <p class="insurerInfo-title"><span>投保人信息</span></p>
        <ul>
            <li>
                <div>
                    <span>姓名：</span>
                    <span>${insurance.basicBO.applicantName}</span>
                </div>
                <div>
                    <span>手机号码：</span>
                    <span>${insurance.basicBO.vehicleOwnerPhone}</span>
                </div>
            </li>
            <li>
                <div>
                    <span>证件类型：</span>
                    <span>${insurance.basicBO.applicantCertTypeName}</span>
                </div>
                <div>
                    <span>证件号码：</span>
                    <span>${insurance.basicBO.vehicleOwnerCertCode}</span>
                </div>
            </li>
        </ul>
    </div>
</div>
<#include "/mana/view/common/footer.ftl">
<script src="/mana/js/insurance/insureDetail.js"></script>