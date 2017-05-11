<#assign shiro=JspTaglibs["/WEB-INF/taglib/shiro.tld"] />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>车险管理系统</title>
    <#--<meta http-equiv="X-UA-Compatible" content="IE=edge">-->
    <#--<meta content="width=device-width, initial-scale=1" name="viewport"/>-->
    <#--<meta content="" name="description"/>-->
    <#--<meta content="" name="author"/>-->

    <link rel="shortcut icon" href="/static/img/whale.png"/>
    <link href="/static/metronic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="/static/metronic/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/mana/css/common/base.min.css"/>
    <link href="/static/layer/skin/layer.css" rel="stylesheet" type="text/css"/>
    <link href="/mana/css/common/na.css" rel="stylesheet" type="text/css">
    <link href="/mana/css/mana.css" rel="stylesheet" type="text/css"/>
    <link href="/static/layui/css/layui.css" rel="stylesheet" type="text/css"/>
    <script src="/static/ueditor/third-party/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="/static/jquery-dateFormat/jquery-dateFormat.min.js" type="text/javascript"></script>
    <#-- vue环境-->
    <#if environment == 'dev'>
        <script src="/static/vue/vue.js" ></script>
    <#else>
        <script src="/static/vue/vue.min.js"></script>
    </#if>
    <script src="/static/vue/utils.js"></script>
<#if originalArtTemplate??>
    <#-- 原生的模板语法，更加灵活，但是跟精简的不兼容 -->
        <script src="/static/template.js" type="text/javascript"></script>
    <#else>
        <script src="/static/other/template.js"></script>
    </#if>

    <script src="/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="/static/layer/layer.js" type="text/javascript"></script>
    <script src="/mana/js/common/layerUtil.js" type="text/javascript"></script>
    <script src="/mana/js/common/ajax.js" type="text/javascript"></script>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=FDHTnWaOO6Xznzrp9WcdOvIwOFDq75Q5"></script>
    <script src="/mana/js/common/mana.js" type="text/javascript"></script>
    <script src="/mana/js/common/main.js" type="text/javascript"></script>
    <script src="/static/layui/layui.js" type="text/javascript"></script>
    <script src="/mana/js/common/na.js" type="text/javascript"></script>
    <#-- 分页js -->
    <script src="/mana/js/common/page.js" type="text/javascript"></script>

    <#-- 通用的模板助手 -->
    <script src="/mana/js/common/templateHelper.js" type="text/javascript"></script>
    <#--vue公共方法-->
    <script src="/mana/js/common/vueHelper.js" type="text/javascript"></script>

</head>
<body>
    <input type="hidden" id="IDP_URL" value="${IDP_URL}">
    <div class="top-menu">
        <#--<span>淘汽车险<em>CRM</em></span>-->
        <span>车险管理系统</span>
        <#--<span class="administrator down"><i class="admin-icon"></i>管理员<i class="down-icon"></i><i class="fa icon-off"></i></span>-->
        <span class="administrator down"><i class="admin-icon"></i><em>${CURRENT_USER_KEY.realName}</em><i class="fa icon-off"></i><em id="logoutBtn">退出</em></span>
        <#--<ul class="admin-menu-list">-->
            <#--<li id="logoutBtn">登出</li>-->
        <#--</ul>-->
    </div>
    <div class="mana-body-content">
        <#include "/mana/view/common/leftMenu.ftl">
        <div class="mana-content-right">
