<#--基础资料新增组界面-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleAdd.css" type="text/css" rel="stylesheet">
<input type="hidden" value="${configBasicBO.id}" id="basicId">
<div class="">
    <p class="p-title">基础管理 > 编辑返点比例</p>
    <div class="select-condition-box">
        <div class="select-condition-item">
            <span>保险公司</span>
            <select id="insureCompanyId">
                <#list insuranceCompanyList as company>
                <option <#if configBasicBO.insuranceCompanyId == company.id>selected </#if>value="${company.id}">${company.companyName}</option>
                </#list>
            </select>
        </div>
        <div class="select-condition-item">
            <span>保险模式</span>
            <select id="cooperationMode">
                <option <#if configBasicBO.cooperationMode==1>selected</#if> value="1">买保险返奖励金</option>
                <option <#if configBasicBO.cooperationMode==2>selected</#if> value="2">买保险送服务</option>
                <option <#if configBasicBO.cooperationMode==3>selected</#if> value="3">买服务送保险</option>
            </select>
        </div>
    </div>
    <div class="add-box">
        <div class="inner-box clearFix">
            <div class="insurance-box" id="insuranceType">
                <div class="insurance-title js-add-item2">
                    <span>保险类型</span>
                    <input type="checkbox" name="insuranceType" <#if configBasicBO.insuranceType==1>checked</#if> value="1" class="opa-radio">
                    <span>交强险</span>
                    <input type="checkbox" name="insuranceType" <#if configBasicBO.insuranceType==2>checked</#if> value="2" class="opa-radio">
                    <span>商业险</span>
                </div>
            <#--<p class="insurance-title">交强险</p>-->
                <div class="add-item-box">
                    <div class="add-item js-add-item1">
                        <span>计算方式</span>
                        <input type="checkbox" name="count-way" <#if configBasicBO.calculateMode==1>checked</#if> value="1" class="opa-radio">
                        <span>单笔</span>
                        <#--<input type="checkbox" name="count-way" <#if configBasicBO.calculateMode==2>checked</#if> value="2" class="opa-radio">-->
                        <#--<span>月累计</span>-->
                    </div>
                    <div class="add-item">
                        <span>保费区间分组名</span>
                        <input type="text" placeholder="请输入" maxlength="15" value="${configBasicBO.groupName}" class="group-name">
                    </div>
                    <div class="add-item clearFix">
                        <div class="interval-title">保费区间</div>
                        <ul class="interval-box">
                            <#list configBasicBO.itemBOList as item>
                            <li class="interval-item">
                                <span class="interval-div">${item.itemStartValue}</span>到
                                <#if item_has_next>
                                <input type="text" class="interval-input" readonly value="${item.itemEndValue}">
                                <#else>
                                    <input type="text" class="interval-input" value="${item.itemEndValue}">
                                </#if>
                                <span>比例</span><input type="text" class="scale-input" value="${item.itemRate}">
                                <#if configBasicBO.itemBOList?size==1>
                                <button id="leftAdd" class="add operate-btn">+</button>
                                <#else>
                                    <#if item_index==0>
                                        <button id="leftAdd" class="add operate-btn display-none">+</button>
                                    <#else>
                                        <#if !item_has_next>
                                            <button id="leftAdd" class="add operate-btn">+</button>
                                            <button class="remove operate-btn">-</button>
                                        <#else>
                                            <button id="leftAdd" class="add operate-btn display-none">+</button>
                                            <button class="remove operate-btn display-none">-</button>
                                        </#if>
                                    </#if>
                                </#if>
                            </li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-div">
            <button class="sure-button">确定</button>
            <button class="cancel-button">取消</button>
        </div>
    </div>
</div>
<script type="text/html" id="itemTemplate">
    <li class="interval-item">
        <span class="interval-div">{{startValue}}</span>到
        <input type="text" class="interval-input">
        <span>比例</span><input type="text" class="scale-input">
        <button class="add operate-btn">+</button>
        <button class="remove operate-btn">-</button>
    </li>
</script>
<script type="text/javascript" src="/mana/js/settle/configBasicEdit.js"></script>
<#include "/mana/view/common/footer.ftl">

