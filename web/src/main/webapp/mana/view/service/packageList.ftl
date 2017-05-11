<#--服务包管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/service/packageList.css" type="text/css" rel="stylesheet">
<div class="all-box">
    <p class="p-title">车险服务包 > 服务包管理</p>
    <ul class="search-box">
        <li class="row-box">
            <button class="jump-add">新建服务包</button>
        </li>
        <li class="row-box">
            <input type="text" placeholder="请输入服务包名称">
            <button class="search-button">搜索</button>
        </li>
    </ul>
    <div class="all-list-box">
        <div class="fenye" id="fenye"></div>
        <div class="rebate-list"></div>
        <div id="fenye2" class="fenye"></div>
    </div>

</div>

<script type="text/html" id="manaTableTemplate">
    <table class="mana-table">
        <thead>
        <tr>
            <th>服务包名称</th>
            <th>服务包市场价</th>
            <th>门店可得返利</th>
            <th>创建时间</th>
            <th>上架状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        {{each dataList as data}}
        <tr>
            <td>{{data.packageName}}</td>
            <td>{{data.marketPrice}}</td>
            <td>{{data.rewardAmount}}</td>
            <td>{{data.gmtCreate | shortDateHelper}}</td>
            {{if data.servicePackageStatus == 0}}
            <td data-id="{{data.servicePackageStatus}}">下架</td>
            {{else}}
            <td data-id="{{data.servicePackageStatus}}">上架</td>
            {{/if}}
            <td>
                {{if data.servicePackageStatus == 0}}
                <a href="javascript:void 0;" data-id="{{data.id}}" data-status="1" class="js-change-status">上架</a>
                {{else}}
                <a href="javascript:void 0;" data-id="{{data.id}}" data-status="0" class="js-change-status">下架</a>
                {{/if}}
                <a href="/insurancePackage/editPackageInit?packageId={{data.id}}">编辑</a>
                <a href="javascript:void 0;" data-id="{{data.id}}" class="js-deleted">删除</a>
            </td>
        </tr>
        {{/each}}
        </tbody>
    </table>
</script>

<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script type="text/javascript" src="/mana/js/service/packageList.js"></script>
<#include "/mana/view/common/footer.ftl">
