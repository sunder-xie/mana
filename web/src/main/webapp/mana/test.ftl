<#include "/mana/view/common/header.ftl">

<#-- ztree -->
<link href="/static/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css"/>


<h2>你愁啥 (#‵′)凸</h2>
<br><br>
<button id="addResource" class="btn blue">添加资源</button>
&nbsp;&nbsp;&nbsp;
<button class="btn blue">添加角色</button>


<ul id="resourceTree" class="ztree" style="height:330px;width:100%;overflow:auto;"></ul>

<#include "/mana/view/common/footer.ftl">

<script src="/static/zTree/js/jquery.ztree.core.min.js" type="text/javascript" ></script>
<script src="/static/zTree/js/jquery.ztree.excheck.min.js" type="text/javascript" ></script>


<script type="text/html" id="addResourceTemplate">

</script>

<script type="text/javascript">
    var zTreeSetting = {
        callback: {
            beforeClick:function(treeId, treeNode) {

            },
            onClick:function(event, treeId, treeNode){

            }
        },
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: { "Y": "ps", "N": "s" }
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pid",
                rootPId: -1
            }
        },
        view: {
            dblClickExpand: true,
            showLine: true,
            selectedMulti: false
        }
    };


    $(document).ready(function(){

        var zTree = $('#resourceTree');

        Ajax.get({
            url: '/sys/getAllResourcesZTree',
            success: function(result){
                console.log(result);
                $.fn.zTree.init(zTree, zTreeSetting, result);
            }
        });


        $('#addResource').click(function(){
            Ajax.get({
                url: '/test/ajaxExceptionTest',
                success: function(result){
                    console.log(result);
                }
            });
        });

    });
</script>