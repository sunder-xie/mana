<#--基础资料新增组界面-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleAdd.css" type="text/css" rel="stylesheet">
<div class="">
    <p class="p-title">基础管理 > 新增返点比例</p>
    <div class="select-condition-box">
        <div class="select-condition-item">
            <span>保险公司</span>
            <select id="insureCompanyId">
            <#list insuranceCompanyList as company>
                <option value="${company.id}">${company.companyName}</option>
            </#list>
            </select>
        </div>
        <div class="select-condition-item">
            <span>保险模式</span>
            <select id="cooperationMode">
                <option value="1">买保险返奖励金</option>
                <option value="2">买保险送服务</option>
                <option value="3">买服务送保险</option>
            </select>
        </div>
    </div>
    <div class="add-box">
        <div class="inner-box clearFix">
            <div class="insurance-box" id="compulsoryInsurance">
                <div class="insurance-title">
                    <input type="checkbox" name="insuranceType" value="1" class="opa-radio">
                    <span>交强险</span>
                </div>
                <#--<p class="insurance-title">交强险</p>-->
                <div class="add-item-box">
                    <div class="add-item">
                        <span>计算方式</span>
                        <input type="checkbox" checked name="count-way" value="1" class="opa-radio">
                        <span>单笔</span>
                        <#--<input type="checkbox" name="count-way" value="2" class="opa-radio">-->
                        <#--<span>月累计</span>-->
                    </div>
                    <div class="add-item">
                        <span>保费区间分组名</span>
                        <input type="text" placeholder="请输入" maxlength="15" class="group-name">
                    </div>
                    <div class="add-item clearFix">
                        <div class="interval-title">保费区间</div>
                        <ul class="interval-box">
                            <li class="interval-item">
                                <span class="interval-div">0.00</span>到
                                <input type="text" class="interval-input">
                                <span>比例</span><input type="text" class="scale-input">
                                <button id="leftAdd" class="add operate-btn">+</button>
                                <#--<button id="leftRemove" class="remove operate-btn">-</button>-->
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="insurance-box" id="commercialInsurance">
                <div class="insurance-title">
                    <input type="checkbox" name="insuranceType" value="2" class="opa-radio">
                    <span>商业险</span>
                </div>
                <div class="add-item-box">
                    <div class="add-item">
                        <span>计算方式</span>
                        <input type="checkbox" checked name="count-way" value="1" class="opa-radio">
                        <span>单笔</span>
                        <#--<input type="checkbox" name="count-way" value="2" class="opa-radio">-->
                        <#--<span>月累计</span>-->
                    </div>
                    <div class="add-item">
                        <span>保费区间分组名</span>
                        <input type="text" placeholder="请输入" maxlength="15" class="group-name">
                    </div>
                    <div class="add-item clearFix">
                        <div class="interval-title">保费区间</div>
                        <ul class="interval-box">
                            <li class="interval-item">
                                <span class="interval-div">0.00</span>到
                                <input type="text" class="interval-input">
                                <span>比例</span><input type="text" class="scale-input">
                                <button class="add operate-btn">+</button>
                                <#--<button class="remove operate-btn">-</button>-->
                            </li>
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
<script type="text/javascript" src="/mana/js/settle/configBasicAdd.js"></script>
<#include "/mana/view/common/footer.ftl">

