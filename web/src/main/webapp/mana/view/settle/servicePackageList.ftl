<#--服务包比例列表-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleList.css" type="text/css" rel="stylesheet">
<div class="all-box">
    <p class="p-title">服务包模式 > 服务包模式比例列表</p>
    <ul class="search-box">
        <li class="row-box">
            <button class="jump-add">新建服务包</button>
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
                <span>保险模式</span>
                <select id="mode" data-typeName = 'cooperationModes'>
                    <option value="">请选择</option>
                    <option value="2">买保险送服务包</option>
                    <option value="3">买服务包送保险</option>
                </select>
            </div>
            <div class="select-condition">
                <span>门店账号</span>
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
                    <th>保险公司</th>
                    <th>配置地区</th>
                    <th>门店账号及名称</th>
                    <th>保险模式</th>
                    <th>返点基准</th>
                    <th>计算方式</th>
                    <th>结算条件</th>
                    <th>资金类型</th>
                    <th>返点类型</th>
                    <th>生效起止日期</th>
                    <th>保费区间分组名</th>
                    <th>保费区间</th>
                    <th>比例</th>
                    <th>操作</th>
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
<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script type="text/javascript" src="/mana/js/settle/servicePackageList.js"></script>
<script type="text/javascript" src="/mana/js/settle/common/settleCommon.js"></script>
<#include "/mana/view/common/footer.ftl">