<#--返点配置列表管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleList.css" type="text/css" rel="stylesheet">
<link href="/mana/css/customerHome.css" rel="stylesheet" type="text/css"/>
<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>

<div class="all-box">
    <p class="p-title">基础管理 > 返点管理</p>
    <ul class="search-box">
        <li class="row-box">
            <div class="select-condition">
                <span>省份</span>
                <select id="province">
                    <option value="">请选择</option>
                </select>
            </div>
            <div class="select-condition">
                <span>地区</span>
                <select id="city">
                    <option value="">请选择</option>
                </select>
            </div>
        </li>
        <li class="row-box">
            <button class="search-button btn">查询</button>
            <button id="add" class="jump-add">新增</button>
        </li>
    </ul>
    <div class="all-list-box">
    <#--<div class="fenye" id="fenye"></div>-->
        <div class="rebate-list">
            <div id="fenye" class="fenye">abc</div>
            <div class="companyRuleList"></div>
            <div id="fenye2" class="fenye"></div>
        </div>
    </div>
</div>
<script type="text/html" id="companyRuleListTemplate">
    <table class="mana-table">
        <thead>
        <tr>
            <th>保险公司</th>
            <th>省</th>
            <th>市</th>
            <th>投保场景</th>
            <th>险种</th>
            <th>返点比例</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        {{each list as obj index}}
            {{each obj.ruleItemList as item i}}
                {{if i==0 }}
                    <tr class="{{index}}">
                        <td rowspan="{{obj.ruleItemList.length}}" class="ver-align">{{obj.insuranceCompanyName}}</td>
                        <td rowspan="{{obj.ruleItemList.length}}" class="ver-align">{{obj.provinceName}}</td>
                        <td rowspan="{{obj.ruleItemList.length}}" class="ver-align">{{obj.cityName}}</td>
                        <td class="ver-align">{{item.scenarioName}}</td>
                        <td class="ver-align">{{item.insuranceName}}</td>
                        <td>{{item.commissionRate}}</td>
                        <td rowspan="{{obj.ruleItemList.length}}" class="ver-align">
                            <a href="/settle/insuranceCompanyRule/rulePage?id={{obj.id}}" class="btn btn-sm purple modify-role-btn">修改</a>
                            <button class="btn btn-sm red delete-role-btn delete" data-value="{{obj.id}}" >删除</button>
                        </td>
                    </tr>
                {{else}}
                    <tr class="{{index}}">
                        <td class="ver-align">{{item.scenarioName}}</td>
                        <td class="ver-align">{{item.insuranceName}}</td>
                        <td>{{item.commissionRate}}</td>
                    </tr>
                 {{/if}}
            {{/each}}
        {{/each}}
        </tbody>
    </table>
</script>

<script type="text/javascript" src="/mana/js/settle/insuranceCompanyRuleList.js"></script>
<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<#include "/mana/view/common/footer.ftl">
