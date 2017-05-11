<#include "/mana/view/common/header.ftl">

<#--<link href="/static/metronic/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>-->
<#--<link href="/static/metronic/components.css" rel="stylesheet" type="text/css"/>-->

<#-- ztree -->
<link href="/static/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css"/>

<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>

<style type="text/css">

</style>


<div class="search-div">
    <button id="refreshBtn" type="button" class="btn blue">刷新</button>
    <button id="addRoleBtn" type="button" class="btn green">添加角色</button>
</div>

<div class="content-div">

</div>


<#-- 模板 -->
<script type="text/html" id="addRoleTemplate">
    <form id="addRoleForm">
        <table class="common-table">
            <tbody>
            <tr>
                <th>角色名称</th>
                <td>
                    <input name="roleName" maxlength="20" value="{{roleName}}">
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>描述</th>
                <td>
                    <input name="roleDescription" maxlength="50" value="{{roleDescription}}">
                </td>
            </tr>
            <tr>
                <th>排序</th>
                <td>
                    <input name="roleSort" maxlength="5" value="{{roleSort}}">
                    <span class="remark-span">* 请输入正整数</span>
                </td>
            </tr>
            <tr>
                <th>配置资源</th>
                <td>
                    <ul id="resourceTree" class="ztree z-tree-ul"></ul>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</script>


<script type="text/html" id="roleListTemplate">
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th>序号</th>
                <th>角色名称</th>
                <th>描述</th>
                <th>排序</th>
                <th>拥有资源</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            {{each data as role idx}}
            <tr>
                <td>{{idx+1}}</td>
                <td>{{role.roleName}}</td>
                <td>{{role.roleDescription}}</td>
                <td>{{role.roleSort}}</td>
                <td>
                    <button type="button" class="btn btn-sm blue show-resource-btn" data-role-id="{{role.id}}">查看</button>
                </td>
                <td>
                    <button type="button" class="btn btn-sm purple modify-role-btn" data-role-id="{{role.id}}">修改</button>
                    <button type="button" class="btn btn-sm red delete-role-btn" data-role-id="{{role.id}}" data-role-name="{{role.roleName}}">删除</button>
                </td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
</script>

<script type="text/html" id="roleResourceTreeTemplate">
    <ul id="roleResourceTree_{{roleId}}" class="ztree" ></ul>
</script>


<#include "/mana/view/common/footer.ftl">

<script src="/static/zTree/js/jquery.ztree.core.min.js" type="text/javascript" ></script>
<script src="/static/zTree/js/jquery.ztree.excheck.min.js" type="text/javascript" ></script>

<script src="/mana/js/sys/role.js" type="text/javascript"></script>
