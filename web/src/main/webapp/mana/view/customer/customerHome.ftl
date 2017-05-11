<#include "/mana/view/common/header.ftl">
<link href="/mana/css/customerHome.css" rel="stylesheet" type="text/css"/>

<div class="carOwnerInfo">
    <div class="carOwnerInfo-search">
        <input type="text" class="js-input" placeholder="请输入车主姓名、手机号、车牌号"/><span class="doSearch">查询</span>
    </div>
    <div id="fenye" class="fenye"></div>
    <div class="carOwnerInfoList"></div>
    <div id="fenye2" class="fenye"></div>
</div>

<script type="text/html" id="carOwnerInfoListTemplate">
    <table class="mana-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>车主姓名</th>
            <th>手机号码</th>
            <th>车牌号</th>
            <th>车主来源</th>
            <th>保险状态</th>
            <th>车险到期日</th>
            <th>车险意向</th>
        </tr>
        </thead>
        <tbody>
        {{each list as obj index}}
        <tr>
            <td>{{offerSet+index}}</td>
            <td class="customer-detail-color js-customer-detail" data-id="{{obj.vehicleId}}">{{obj.customerName}}</td>
            <td>{{obj.customerMobile}}</td>
            <td>{{obj.licencePlate}}</td>
            <td>{{obj.customerSourceName}}</td>
            <td>{{obj.insureStatusName}}</td>
            <td>{{obj.insureEndDate | shortDateHelper}}</td>
            <td>{{obj.insureIntentionName}}</td>
        </tr>
        {{/each}}
        </tbody>
    </table>
    <!--分页组件-->
    <#--<div id="pagination" class="qxy_page"></div>-->
</script>

<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script src="/mana/js/customer/customerHome.js"></script>

<#include "/mana/view/common/footer.ftl">
