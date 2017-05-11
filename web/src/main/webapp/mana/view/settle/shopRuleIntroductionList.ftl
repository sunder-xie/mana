<#--基础资料返点管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/shopRuleIntroductionList.css" type="text/css" rel="stylesheet">
<div class="all-box">
    <p class="p-title">规则配置 > 结算规则文案配置</p>
    <div class="search-box">
        <div class="settle-introduction  introduction-ext">
        <img src="/static/img/ruleIntroduction/cash.png" class="cash-img"></img>
            <ul class="introduction-ul">
            <li class="introduction-title">现金结算规则</li>
                <li class="introduction-li">最近更新时间:</li>
                <#if introductionBOList??>
                <li class="introduction-li">
                <#list introductionBOList as introductionBO>
                    <#if introductionBO.ruleType == 1>${introductionBO.gmtModified?string("yyyy-MM-dd HH:mm")}</#if>
                </#list>
                </li>
                </#if>
            </ul>
            <div class="edit-box" data-type="1">编辑</div>
        </div>
        <div class="settle-introduction introduction-ext">
        <img src="/static/img/ruleIntroduction/reward.png" class="cash-img"></img>
            <ul class="introduction-ul">
            <li class="introduction-title">奖励金结算规则</li>
                <li class="introduction-li">最近更新时间:</li>
                <#if introductionBOList??>
                    <li class="introduction-li">
                        <#list introductionBOList as introductionBO>
                            <#if introductionBO.ruleType == 2>${introductionBO.gmtModified?string("yyyy-MM-dd HH:mm")}</#if>
                        </#list>
                    </li>
                </#if>
            </ul>
            <div class="edit-box" data-type="2">编辑</div>
        </div>
        <div class="settle-introduction">
            <img src="/static/img/ruleIntroduction/package.png" class="cash-img"></img>
            <ul class="introduction-ul">
                <li class="introduction-title">服务包结算规则</li>
                <li class="introduction-li">最近更新时间:</li>
                <#if introductionBOList??>
                    <li class="introduction-li">
                        <#list introductionBOList as introductionBO>
                            <#if introductionBO.ruleType == 3>${introductionBO.gmtModified?string("yyyy-MM-dd HH:mm")}</#if>
                        </#list>
                    </li>
                </#if>
            </ul>
            <div class="edit-box" data-type="3">编辑</div>
        </div>
    </div>

</div>

<#include "/mana/view/common/footer.ftl">
<script type="text/javascript">
    $(document).on('click','.edit-box',function(e){
        var ruleType = $(e.target).data("type");
        window.location.href = "/introduction/shopRuleIntroductionEdit?ruleType="+ruleType;
    })
</script>
