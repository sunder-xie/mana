<#--现金券比例列表-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleList.css" type="text/css" rel="stylesheet">
<div class="all-box">
    <p class="p-title">代金券 > 规则管理</p>
    <ul class="search-box">
        <li class="row-box">
            <div class="select-condition">
                <#--<select id="province" data-typeName = 'provinceCode'>-->
                    <#--<option value="">请选择省</option>-->
                <#--</select>-->
                <#--<select id="city" data-typeName = 'cityCode'>-->
                    <#--<option value="">请选择市</option>-->
                <#--</select>-->
                <input id="province"  type="text" placeholder="请输入省份" maxlength="15"  class="group-name">
                <input id="city"  type="text" placeholder="请输入城市" maxlength="15"  class="group-name">

            </div>
            <button class="search-button">搜索</button>
            <button class="new-add-button">+新增</button>
        </li>
    </ul>
    <div class="all-list-box">
        <div class="rebate-list">
        <div class="fenye" id="fenye"></div>
        <div class="cashCouponRuleList"></div>
            <script type="text/html" id="cashCouponRuleListTemplate">
                <table class="mana-table ">
                <thead>
                <tr>
                    <td>规则适用地区</td>
                    <td>最近操作时间</td>
                    <td>操作人</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody class="mana-body">
                {{each list as obj index}}

                <tr class="{{index}}">
                    <td class="ver-align">{{obj.provinceName}} {{obj.cityName}}</td>
                    <td class="ver-align">{{obj.gmtModified | longDateHelper }}</td>
                    <td>{{obj.modifier}}</td>
                    <td><a href="/cashCouponConfig/addConfigInit?id={{obj.id}}">编辑</a>
                        <a href="javascript:void 0;"  class="delete" data-id="{{obj.id}}" class="js-deleted">删除</a>
                    </td>
                </tr>

                {{/each}}
                </tbody>
            </table>
            </script>
        </div>
        <div id="fenye2" class="fenye"></div>
    </div>
    </div>
</div>
<script id="listTpl" type="text/html">

</script>
<script type="text/javascript" src="/mana/js/settle/cashCoupon/cashCouponRuleList.js"></script>
<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<#include "/mana/view/common/footer.ftl">

