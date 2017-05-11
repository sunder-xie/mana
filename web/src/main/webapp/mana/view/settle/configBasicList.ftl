<#--基础资料返点管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleList.css" type="text/css" rel="stylesheet">
<div class="all-box">
    <p class="p-title">基础管理 > 返点管理</p>
    <ul class="search-box">
        <li class="row-box">
            <button class="jump-add">新增组</button>
        </li>
        <li class="row-box">
            <div class="select-condition">
                <span>保险公司</span>
                <select id="insureCompanyId"></select>
            </div>
            <div class="select-condition">
                <span>保险模式</span>
                <select id="cooperationMode">
                    <option value="">请选择</option>
                    <option value="1">买保险返奖励金</option>
                    <option value="2">买保险送服务</option>
                    <option value="3">买服务送保险</option>
                </select>
            </div>
            <div class="select-condition">
                <span>险种</span>
                <select id="insuranceType">
                    <option value="">请选择</option>
                    <option value="1">交强险</option>
                    <option value="2">商业险</option>
                </select>
            </div>
            <#--<div class="select-condition">-->
                <#--<span>计算方式</span>-->
                <#--<select id="calculateMode">-->
                    <#--<option value="">请选择</option>-->
                    <#--<option value="1">单笔</option>-->
                    <#--<option value="2">月累计</option>-->
                <#--</select>-->
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
            <table class="mana-table"></table>
        </div>
        <div id="fenye2" class="fenye"></div>
    </div>

</div>

<script type="text/html" id="configBasicListTemplate">
    <thead>
    <tr>
        <th>保险公司</th>
        <th>保险模式</th>
        <th>险种</th>
        <#--<th>计算方式</th>-->
        <th>保费区间分组名</th>
        <th>保费区间</th>
        <th>比例</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    {{each list as basic idx}}
    <tr>
        <td class="ver-align">{{basic.insuranceCompanyName}}</td>
        <td class="ver-align">{{basic.cooperationModeName}}</td>
        <td class="ver-align">{{basic.insuranceTypeName}}</td>
        <#--<td class="ver-align">{{basic.calculateModeName}}</td>-->
        <td class="ver-align">{{basic.groupName}}</td>
        <td class="ver-align">
            <table>
                <tbody>
                {{each basic.itemBOList as item i}}
                <tr>
                    {{if item.itemEndValue != null}}
                    <td>{{item.itemStartValue}} - {{item.itemEndValue}}</td>
                    {{/if}}
                    {{if item.itemEndValue == null}}
                    <td>{{item.itemStartValue}}及以上</td>
                    {{/if}}
                </tr>
                {{/each}}
                </tbody>
            </table>
        </td>
        <td class="ver-align">
            <table>
                <tbody>
                {{each basic.itemBOList as item i}}
                <tr>
                    <td>{{item.itemRate}}</td>
                </tr>
                {{/each}}
                </tbody>
            </table>
        </td>
        <td class="ver-align">
            <table>
                <tbody>
                <tr>
                    <td>
                        <!--todo :取basic中hasUsed判断是否被使用-->
                        {{if basic.hasUsed==0}}
                        <a href="/settle/configBasicEdit?basicId={{basic.id}}">修改</a>
                        <a href="javascript:void 0;" class="js-del" data-id="{{basic.id}}">删除</a>
                        {{else}}
                        <i>使用中</i>
                        {{/if}}
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    {{/each}}
    </tbody>
</script>

<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script type="text/javascript" src="/mana/js/settle/configBasicList.js"></script>
<#include "/mana/view/common/footer.ftl">
