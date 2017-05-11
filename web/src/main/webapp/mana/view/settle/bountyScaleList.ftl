<#--奖励金比例列表-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleList.css" type="text/css" rel="stylesheet">
<div class="all-box">
    <p class="p-title">奖励金模式 > 奖励金模式比例列表</p>
    <ul class="search-box">
        <li class="row-box">
            <button class="jump-add">新建奖励金</button>
        </li>
        <li class="row-box">
            <div class="select-condition">
                <span>保险公司</span>
                <select id="insureCompanyId" data-typeName = 'insuranceCompanyId'></select>
            </div>
            <div class="select-condition">
                <span>配置地区</span>
                <select id="province" data-typeName = 'provinceCode'>
                    <option value="">请选择省</option>
                </select>
                <select id="city" data-typeName = 'cityCode'>
                    <option value="">请选择市</option>
                </select>
            </div>
            <div class="select-condition">
                <span>门店名称</span>
                <input type="text" placeholder="请输入" id="storeName" data-typeName="shopId">
                <ul class="storeResult"></ul>
            </div>
        </li>
        <li class="row-box">
            <button class="search-button">查询</button>
            <button class="export-button">导出</button>
        </li>
    </ul>
    <div class="all-list-box">
        <div class="fenye" id="fenye"></div>
        <div class="rebate-list">
            <table class="mana-table bounty-table">
                <thead>
                <tr>
                    <td>保险公司</td>
                    <td>结算条件</td>
                    <td>配置地区</td>
                    <td>门店账号及名称</td>
                    <td>险种</td>
                    <td>计算方式</td>
                    <td>生效起止日期</td>
                    <td>保费区间分组名</td>
                    <td>保费区间</td>
                    <td>比例</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody class="mana-body">

                </tbody>
            </table>
        </div>
        <div id="fenye2" class="fenye"></div>
    </div>
</div>

<script id="insuranceCompanyTemp" type="text/html">
    <option value="">请选择</option>
    {{each data as obj i}}
    <option value="{{obj.id}}">{{obj.companyName}}</option>
    {{/each}}
</script>
<script id="insuranceCityTemp" type="text/html">
    <option value="">请选择{{msg}}</option>
    {{each data as obj i}}
    <option value="{{obj.regionCode}}">{{obj.regionName}}</option>
    {{/each}}
</script>

<script id="listTpl" type="text/html">

</script>
<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script type="text/javascript" src="/mana/js/settle/bountyScaleList.js"></script>
<script type="text/javascript" src="/mana/js/settle/common/settleCommon.js"></script>
<#include "/mana/view/common/footer.ftl">