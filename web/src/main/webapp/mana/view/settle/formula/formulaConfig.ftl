<#include "/mana/view/common/header.ftl">

<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>

<style type="text/css">
    .modify-variable-btn{
        text-decoration: underline !important;
        color: blue;
        cursor: pointer;
        margin-left: 2px;
    }
    .modify-variable-btn:hover{
        color: red;
    }

    .common-table input,.common-table textarea{
        width: 700px;
        padding: 5px;
        font-size: 14px;
    }
    .common-table textarea{
        height: 100px;
    }

    .special-variable-btn{
        margin-left: 10px;
    }

</style>


<div class="search-div">
    <button id="refreshBtn" type="button" class="btn blue">刷新</button>
    <button id="addBtn" type="button" class="btn green">添加公式</button>
    <button id="returnBtn" type="button" class="btn blue hidden">返回列表</button>
    <button id="saveBtn" type="button" class="btn green hidden">保存</button>
</div>

<div class="content-div">

</div>


<#-- 模板 -->
<script type="text/html" id="addTemplate">
    <form id="addForm">
        <table class="common-table">
            <tbody>
            <tr>
                <th>公式key</th>
                <td>
                    <input name="formulaKey">
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>公式名称</th>
                <td>
                    <input name="formulaName">
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>特殊变量</th>
                <td id="specialVariableList">
                </td>
            </tr>
            <tr>
                <th>计算公式</th>
                <td>
                    <input name="formulaExpress">
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>公式说明</th>
                <td>
                    <textarea name="formulaExplain"></textarea>
                    <span class="remark-span">* 必填</span>
                </td>
            </tr>
            <tr>
                <th>是否展示</th>
                <td>
                    <select name="isShow">
                        <option value="0" selected>否</option>
                        <option value="1">是</option>
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</script>


<script type="text/html" id="listTemplate">
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th>序号</th>
                <th>公式key</th>
                <th>公式名称</th>
                <th>计算公式</th>
                <th>公式说明</th>
                <th>是否展示</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            {{each data as obj idx}}
            <tr>
                <td>{{idx+1}}</td>
                <td>{{obj.formulaKey}}</td>
                <td>{{obj.formulaName}}</td>
                <td>{{obj.formulaExpress}}</td>
                <td>{{obj.formulaExplain}}</td>
                <td>{{#obj.isShow | isShowHelper}}</td>
                <td>
                    <button type="button" class="btn btn-sm purple modify-formula-btn" data-id="{{obj.id}}">修改</button>
                    <button type="button" class="btn btn-sm red delete-formula-btn" data-id="{{obj.id}}" >删除</button>
                </td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
</script>

<script type="text/html" id="specialVariableTemplate">
    {{each data as val key}}
    <button type="button" class="btn btn-sm green special-variable-btn" data-value="{{key}}">{{val}}</button>
    {{/each}}
</script>


<#include "/mana/view/common/footer.ftl">


<script src="/mana/js/settle/formula/formulaConfig.js" type="text/javascript"></script>
