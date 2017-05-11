<#include "/mana/view/common/header.ftl">
<link href="/mana/css/priceParity.css" rel="stylesheet" type="text/css"/>
    <div class="priceParity-content">
        <div>
            <div class="search-div">
                <input id="searchInput" type="text" autocomplete="on" maxlength="20" placeholder="请输入车牌号">
                <button id="searchBtn" type="button" class="btn green-meadow">搜索</button>
            </div>

            <div class="content-title">
                投保单号&nbsp;&nbsp;&nbsp;<em>( 仅展示最新的 3 条数据 )</em>
            </div>
            <div class="ino-content">
                <label>请输入车牌号查询数据</label>
            </div>

            <div id="detailDiv"></div>
        </div>
    </div>
</div>
<#include "/mana/view/common/footer.ftl">

<#-- 模板 -->
<script type="text/html" id="inoTemplate">
    {{each data as no idx}}
    <div class="row insure-info" data-id="{{no.insuranceBasicId}}">
        <div class="col-md-4">
            商业险单号：{{no.vciNo}}
        </div>
        <div class="col-md-4">
            交强险单号：{{no.tciNo}}
        </div>
        <div class="col-md-4">
            投保时间：{{no.gmtCreateStr}}
        </div>
    </div>
    {{/each}}
</script>
<script type="text/html" id="detailTemplate">
    <div class="content-title">车辆信息</div>
    <div class="car-content">
        <div class="row">
            <div class="col-md-6">
                <b>车牌号码：</b>
                {{vehicleSn}}
            </div>
            <div class="col-md-6">
                <b>投保地：</b>
                {{insuredProvince}} - {{insuredCity}}
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <b>发动机号：</b>
                {{carEngineSn}}
            </div>
            <div class="col-md-6">
                <b>车架号：</b>
                {{carFrameSn}}
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <b>品牌型号：</b>
                {{carConfigType}}
            </div>
        </div>
    </div>
    <div class="content-title">保障内容</div>
    <div class="detail-content">
    </div>
</script>
<script type="text/html" id="detailContentTemplate">
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>保险类别</th>
                <th>保险项目</th>
                <th>不计免赔</th>
                <th>保额</th>
                <th>保费（安心）</th>
                <th>保费（人保）</th>
                <th>保费（平安）</th>
            </tr>
            </thead>
            <tbody>

            <tr id="feeAmountTr">
                <td colspan="3" style="text-align: center;">保费合计</td>
                <td></td>
                <td>{{#feeAmountAnXin | feeHelper}}</td>
                <td>{{#feeAmountRenBao | feeHelper}}</td>
                <td>{{#feeAmountPingAn | feeHelper}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</script>
<script type="text/html" id="detailItemTemplate">
    {{if itemSize > 0}}
    {{each itemList as item idx}}
    <tr>
        {{if idx==0}}
        <td rowspan="{{itemSize}}">{{title}}</td>
        {{/if}}
        <td>{{item.insuranceName}}</td>
        <td>
            <input {{if item.isDeductible==0}}checked{{/if}} type="checkbox" onclick="this.checked=!this.checked">
        </td>
        <td>{{#item.insuranceAmount | feeHelper}}</td>
        <td>{{#item.insuranceFee | feeHelper}}</td>
        <td>{{#item.insuranceFeeRenBao | feeHelper}}{{#item.renBaoRemark | remarkHelper}}</td>
        <td>{{#item.insuranceFeePingAn | feeHelper}}{{#item.pingAnRemark | remarkHelper}}</td>
    </tr>
    {{/each}}
    {{/if}}
</script>

<script src="/mana/js/priceParity.js" type="text/javascript"></script>
