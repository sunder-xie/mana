<#include "/mana/view/common/header.ftl">

<#--<link href="/static/metronic/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>-->
<#--<link href="/static/metronic/components.css" rel="stylesheet" type="text/css"/>-->

<#-- ztree -->
<link href="/static/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css"/>

<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>

<style type="text/css">


</style>


<div class="search-div">
    工号：<input id="searchStaffNo" maxlength="15">
    手机号：<input id="searchMobile" maxlength="20">
    姓名：<input id="searchName" maxlength="10">
    <button id="searchBtn" type="button" class="btn blue">查询</button>
    <button id="resetBtn" type="button" class="btn yellow">重置条件</button>
    <#--<button id="addBtn" type="button" class="btn green">绿色通道</button>-->
</div>

<div class="content-div">

</div>


<#-- 模板 -->
<script type="text/html" id="modifyRoleTemplate">
    <table class="common-table">
        <tbody>
        <tr>
            <th style="width: 100px;">用户</th>
            <td>
                {{user}}
            </td>
        </tr>
        <tr>
            <th>配置角色</th>
            <td>
                <ul id="roleTree" class="ztree" style="max-height:250px;width:100%;overflow:auto;"></ul>
            </td>
        </tr>
        </tbody>
    </table>
</script>


<script type="text/html" id="userListTemplate">
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th>序号</th>
                <th>工号</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            {{each list as user idx}}
            <tr>
                <td>{{idx+1}}</td>
                <td>{{user.userName}}</td>
                <td>{{user.realName}}</td>
                <td>{{user.mobile}}</td>
                <td>
                    <button type="button" class="btn btn-sm purple modify-role-btn"
                            data-user-name="{{user.userName}}" data-real-name="{{user.realName}}">
                        配置角色
                    </button>
                </td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
    <!--分页组件-->
    <div id="pagination" class="qxy_page"></div>
</script>


<#include "/mana/view/common/footer.ftl">

<script src="/static/zTree/js/jquery.ztree.core.min.js" type="text/javascript" ></script>
<script src="/static/zTree/js/jquery.ztree.excheck.min.js" type="text/javascript" ></script>

<script src="/mana/js/sys/user.js" type="text/javascript"></script>
