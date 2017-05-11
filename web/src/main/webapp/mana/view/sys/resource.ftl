<#include "/mana/view/common/header.ftl">

<link href="/static/jquery-easyui-1.4.2/themes/bootstrap/easyui.css" rel="stylesheet" type="text/css" />
<link href="/static/jquery-easyui-1.4.2/themes/icon.css" rel="stylesheet" type="text/css" />

<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>


<style type="text/css">
    td{
        vertical-align: middle;
    }
</style>


<table id="tg" style="height: 100%"></table>

<div id="add_dialog"></div>

<!-- 自定义模板 -->
<script type="text/html" id="add_template">
    <form id="add_form">
        <table class="common-table" >
            <tr>
                <th style="width:90px;">资源名称</th>
                <td>
                    <input name="resourceName" maxlength="20" >
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>资源路径</th>
                <td>
                    <input name="resourceUrl" maxlength="100" >
                </td>
            </tr>
            <tr>
                <th>资源权限</th>
                <td>
                    <input name="resourcePermission" maxlength="50" >
                </td>
            </tr>
            <tr>
                <th>上级资源</th>
                <td>
                    <input id="addPid" name="parentId" value="默认为一级资源">
                    <span onclick="clearPid('addPid')" class="clear-span" >清除</span>
                </td>
            </tr>
            <tr>
                <th>资源类型</th>
                <td>
                    <select name="resourceType" >
                        <option value="0">菜单</option>
                        <option value="1">操作</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>排序</th>
                <td>
                    <input name="resourceSort" maxlength="5" value="1">
                    <span class="remark-span">* 请输入正整数</span>
                </td>
            </tr>
            <tr>
                <th>资源描述</th>
                <td>
                    <input name="resourceDescription" maxlength="30" >
                </td>
            </tr>
        </table>
    </form>
</script>

<script type="text/html" id="modify_template">
    <form id="modify_form">
        <table class="common-table" >
            <tr>
                <th style="width:90px;">资源名称</th>
                <td>
                    <input name="resourceName" maxlength="20" value="{{resourceName}}">
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>资源路径</th>
                <td>
                    <input name="resourceUrl" maxlength="100" value="{{resourceUrl}}">
                </td>
            </tr>
            <tr>
                <th>资源权限</th>
                <td>
                    <input name="resourcePermission" maxlength="50" value="{{resourcePermission}}">
                </td>
            </tr>
            <tr>
                <th>上级资源</th>
                <td>
                    <input id="addPid" name="parentId" value="默认为一级资源">
                    <span onclick="clearPid('addPid')" class="clear-span">清除</span>
                </td>
            </tr>
            <tr>
                <th>资源类型</th>
                <td>
                    <select name="resourceType" >
                        <option {{if resourceType==0}}selected{{/if}} value="0">菜单</option>
                        <option {{if resourceType==1}}selected{{/if}} value="1">操作</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>排序</th>
                <td>
                    <input name="resourceSort" maxlength="5" value="{{resourceSort}}">
                    <span class="remark-span">* 请输入正整数</span>
                </td>
            </tr>
            <tr>
                <th>资源描述</th>
                <td>
                    <input name="resourceDescription" maxlength="30" value="{{resourceDescription}}">
                </td>
            </tr>
        </table>
    </form>
</script>


<#include "/mana/view/common/footer.ftl">

<script src="/static/jquery-easyui-1.4.2/jquery.easyui.min.js" type="text/javascript"></script>
<script src="/mana/js/sys/resource.js" type="text/javascript"></script>

