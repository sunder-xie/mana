<#--基础资料返点管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/shopRuleIntroductionEdit.css" type="text/css" rel="stylesheet">
<input type="hidden" value="${introductionId}" id="introductionId">
<input type="hidden" value="${ruleType}" id="ruleType">
<div class="all-box">
    <p class="p-title">规则配置 > 结算规则文案配置 > 编辑结算规则</p>
    <div class="add-box">
        <div class="inner-box">
            <#--页面编辑器-->
            <script id="innerBox" class="ueditor-outer"></script>
        </div>
        <div class="btn-div">
            <button class="sure-button">保存</button>
            <button class="cancel-button">返回</button>
        </div>
    </div>
</div>
<!-- 配置文件 -->
<script type="text/javascript" src="/static/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/static/ueditor/ueditor.all.js"></script>

<script src="/mana/js/settle/shopRuleIntroductionEdit.js" type="text/javascript"></script>
<#include "/mana/view/common/footer.ftl">
