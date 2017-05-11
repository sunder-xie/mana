<!--奖励金比例管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleAdd.css" type="text/css" rel="stylesheet">
<input type="hidden" value="${bountyScaleId}" id="bountyScaleId">
<div class="">
    <p class="p-title">规则配置 > 奖励金模式 > 编辑</p>
    <div class="select-condition-box">
        <div class="select-condition-item">
            <span>保险公司</span>
            <select class="js-insurance-company" disabled>
            <#if allCompanyList ??>
                <#list allCompanyList as company>
                    <option <#if shopRuleBO.insuranceCompanyId == company.id>selected</#if> value="${company.id}">${company.companyName}</option>
                </#list>
            </#if>
            </select>
        </div>
        <div class="select-condition-item">
            <span>结算条件</span>
            <select class="js-settle-condition">
                <option value="0">请选择</option>
                <option <#if shopRuleBO.settleCondition == 1>selected </#if>value="1">签单日期</option>
                <option <#if shopRuleBO.settleCondition == 2>selected </#if> value="2">起保日期</option>
            </select>
        </div>
        <div class="dispose">
            <input class="opa-radio" <#if shopRuleBO.applyRange == 2>checked</#if> type="checkbox" name="store" value="2">门店配置
            <input class="opa-radio" <#if shopRuleBO.applyRange == 1></#if> type="checkbox" name="area" value="1">地区配置
        </div>
        <div class="store-dispose-box">
            <input type="text" id="shopName" placeholder="请输入门店名称">
            <div class="mdsug"></div>
        </div>
        <div class="area-dispose-box display-none">
            <div class="area-dispose-item">
                <select class="js-region-select">
                    <option value="0">请选择省份</option>
                <#if regionList ??>
                    <#list regionList as region>
                        <option value="${region.regionCode}" data-level="${region.regionLevel}">${region.regionName}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
    </div>
    <div class="add-box">
        <div class="inner-box clearFix">
            <div class="insurance-box-row">
                <input class="opa-radio float-left" name="commercial" type="checkbox">
                <div class="insurance-title">商业险</div>
                <div class="add-item-box">
                    <div class="add-item-title">
                        <div class="select-condition-item display-none">
                            <span>计算方式</span>
                            <select class="js-commercial-calculate">
                                <option value="1">单笔</option>
                                <#--<option value="2">月累计</option>-->
                            </select>
                        </div>
                        <div class="select-condition-item display-none">
                            <span>计算时间</span>
                            <input type="text" class="cxdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   placeholder="请选择日/时/分/秒">
                        </div>
                    </div>
                    <div class="add-item">
                        <div class="dispose-condition-item">
                            <span class="margin-r-10">生效起止时间</span>
                            <input type="text" class="cxdate js-start-date" disabled value="${.now?string("yyyy-MM-dd")}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
                            <span class="margin-r-10">至</span>
                            <input type="text" class="cxdate js-end-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                        </div>
                        <div class="dispose-condition-item">
                            <span>分组名称</span>
                            <select class="js-commercial-basic"></select>
                        </div>
                        <button class="operate-btn">+</button>
                        <button class="cancel-btn display-none">-</button>
                        <div class="js-add-commercial-item"></div>
                    </div>
                </div>
            </div>
            <div class="insurance-box-outer">
                <div class="insurance-box-row">
                    <div class="compulsory-box-title">
                        <input class="opa-radio float-left" name="compulsory" type="checkbox">
                        <div class="insurance-title jq-title">交强险</div>
                    </div>
                    <div class="add-item-box">
                        <div class="add-item-title">
                            <div class="select-condition-item display-none">
                                <span>计算方式</span>
                                <select class="js-compulsory-calculate">
                                    <option value="1">单笔</option>
                                    <#--<option value="2">月累计</option>-->
                                </select>
                            </div>
                            <div class="select-condition-item display-none">
                                <span>计算时间</span>
                                <input type="text" class="cxdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                       placeholder="请选择日/时/分/秒">
                            </div>
                        </div>
                        <div class="add-item">
                            <div class="dispose-condition-item">
                                <span class="margin-r-10">生效起止时间</span>
                                <input type="text" class="cxdate js-start-date" disabled value="${.now?string("yyyy-MM-dd")}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                                <span class="margin-r-10">至</span>
                                <input type="text" class="cxdate js-end-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                            </div>
                            <div class="dispose-condition-item">
                                <span>分组名称</span>
                                <select class="js-compulsory-basic"></select>
                            </div>
                            <button class="operate-btn">+</button>
                            <button class="cancel-btn display-none">-</button>
                            <div class="js-add-compulsory-item"></div>
                        </div>
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
<script type="text/html" id="areaBoxTemplate">
    {{if regionList.length > 0}}
    <div class="area-dispose-item js-item">
        <input id="allChecked" class="all-check opa-radio" type="checkbox"> 全选
    </div>
    <ul class="area-dispose-item js-item">
        {{each regionList as region index}}
        <li>
            <input type="checkbox" {{if region.select == 1}} checked {{/if}} class="opa-radio" value="{{region.regionCode}}">
            <i>{{region.regionName}}</i>
        </li>
        {{/each}}
    </ul>
    {{/if}}
</script>
<script type="text/html" id="addItemTemplate">
    <table class="mana-table add-table-style">
        <thead>
        <tr>
            <td>保费区间分组名</td>
            <td>保险公司</td>
            <td>险种</td>
            <#--<td>计算方式</td>-->
            <td>保费区间</td>
            <td>比例</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="ver-align">{{basic.groupName}}</td>
            <td class="ver-align">{{basic.insuranceCompanyName}}</td>
            <td class="ver-align">{{basic.insuranceTypeName}}</td>
            <#--{{if basic.calculateMode == 1}}-->
            <#--<td class="ver-align">单笔</td>-->
            <#--{{else}}-->
            <#--<td class="ver-align">月累计</td>-->
            <#--{{/if}}-->
            <td class="ver-align">
                <table class="width-100">
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
                <table class="width-100">
                    <tbody>
                    {{each basic.itemBOList as item i}}
                    <tr>
                        <td>{{item.itemRate}}</td>
                    </tr>
                    {{/each}}
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
</script>
<script type="text/html" id="addBoxTemplate">
    <div class="add-item">
        <div class="dispose-condition-item">
            <span class="margin-r-10">生效起止时间</span>
            <input type="text" class="cxdate js-start-date" disabled value="{{endDate  | shortDateHelper}}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="${.now?string('yyyy-MM-dd')}"/>
            <span class="margin-r-10">至</span>
            <input type="text" class="cxdate js-end-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
        </div>
        <div class="dispose-condition-item">
            <span>分组名称</span>
            <select class="{{className}}"></select>
        </div>
        <button class="operate-btn">+</button>
        <button class="cancel-btn">-</button>
        <div class="{{jsName}}"></div>
    </div>
</script>
<script type="text/html" id="editBoxTemplate">
    <div class="add-item">
        <div class="dispose-condition-item">
            <span class="margin-r-10">生效起止时间</span>
            <input type="text" class="cxdate js-start-date" disabled value="{{config.startTime  | shortDateHelper}}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="${.now?string('yyyy-MM-dd')}"/>
            <span class="margin-r-10">至</span>
            <input type="text" class="cxdate js-end-date" value="{{config.endTime  | shortDateHelper}}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
        </div>
        <div class="dispose-condition-item">
            <span>分组名称</span>
            <select class="{{className}}">
                {{each basicList as basic index}}
                <option {{if basic.id==config.settleConfigBasicVO.id}}selected{{/if}} value="{{basic.id}}">{{basic.groupName}}</option>
                {{/each}}
            </select>
        </div>
        {{if itemSize ==1}}
        <button class="operate-btn">+</button>
        <button class="cancel-btn display-none">-</button>
        {{else if itemSize == itemIndex+1}}
        <button class="operate-btn">+</button>
        <button class="cancel-btn">-</button>
        {{else}}
        <button class="operate-btn display-none">+</button>
        <button class="cancel-btn display-none">-</button>
        {{/if}}
        {{if itemSize == itemIndex+1}}

        {{/if}}
        <div class="{{jsName}}">
            <table class="mana-table add-table-style">
                <thead>
                <tr>
                    <td>保费区间分组名</td>
                    <td>保险公司</td>
                    <td>险种</td>
                    <#--<td>计算方式</td>-->
                    <td>保费区间</td>
                    <td>比例</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="ver-align">{{config.settleConfigBasicVO.groupName}}</td>
                    <td class="ver-align">{{config.settleConfigBasicVO.insuranceCompanyName}}</td>
                    <td class="ver-align">{{config.settleConfigBasicVO.insuranceTypeName}}</td>
                    <#--<td class="ver-align">{{config.settleConfigBasicVO.calculateModeName}}</td>-->
                    <td class="ver-align">
                        <table class="width-100">
                            <tbody>
                            {{each config.settleConfigBasicVO.itemVOList as item i}}
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
                        <table class="width-100">
                            <tbody>
                            {{each config.settleConfigBasicVO.itemVOList as item i}}
                            <tr>
                                <td>{{item.itemRate}}</td>
                            </tr>
                            {{/each}}
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</script>
<script type="text/javascript" src="/mana/js/settle/bountyScaleManageEdit.js"></script>
<#include "/mana/view/common/footer.ftl">