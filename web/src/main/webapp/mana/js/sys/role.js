/**
 * Created by huangzhangting on 16/12/25.
 */

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

var simpleZTreeSetting = {
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

    getAllRoles();

    $('#addRoleBtn').click(function(){
        toAddRole();
    });

    $('#refreshBtn').click(function(){
        getAllRoles();
    });

});

function getAllRoles(){
    var index = LayerUtil.load();

    Ajax.get({
        url: '/sys/getAllRoles',
        success: function(result){
            if(result.success){
                var html = template('roleListTemplate', result);
                $('.content-div').html(html);

                bindTableBtnEvent();
            }else{
                $('.content-div').html('<label>'+result.message+'</label>');
            }

            layer.close(index);
        }
    });
}
/* 表格内部按钮绑定事件 */
function bindTableBtnEvent(){
    $('.show-resource-btn').click(function(){
        showResource(this);
    });
    $('.modify-role-btn').click(function(){
        var roleId = $(this).data('role-id');
        toModifyRole(roleId);
    });
    $('.delete-role-btn').click(function(){
        toDeleteRole(this);
    });
}

function toAddRole(){
    var contentHtml = template('addRoleTemplate', {roleSort: 1});
    LayerUtil.alertNoIcon({
        content: contentHtml,
        title: '添加角色',
        width: 450,
        callBack: function(index){
            var formData = getFormData('#addRoleForm');
            if(formData==null){
                return false;
            }
            formData.roleDescription = formData.roleDescription.trim();
            packageResourceIds("resourceTree", formData);

            //console.log(formData);

            Ajax.post({
                url: '/sys/addRole',
                data: formData,
                success: function(result){
                    if(result.success){
                        LayerUtil.msg('角色添加成功');
                        getAllRoles();
                    }else{
                        //TODO 因为 layer.msg 会关闭 layer.alert 暂时没时间解决
                        alert(result.message);
                    }
                }
            });

        }
    });

    var zTree = $('#resourceTree');
    Ajax.get({
        url: '/sys/getAllResourcesZTree',
        success: function(result){
            $.fn.zTree.init(zTree, zTreeSetting, result);
        }
    });
}

function getFormData(form){
    var formData = MaNa.jqSerializeForm(form);
    var roleName = formData.roleName.trim();
    var roleSort = MaNa.repSpace(formData.roleSort);
    var checkFlag = MaNa.isPositiveInt(roleSort);
    if(roleName==''){
        checkFlag = false;
    }
    if(!checkFlag){
        alert('输入参数不合法，请再次检查');
        return null;
    }
    formData.roleName = roleName;
    formData.roleSort = parseInt(roleSort);
    return formData;
}
/* 组装资源id （spring mvc 接受复杂对象参数，比较麻烦~~~）*/
function packageResourceIds(tree, formData){
    var treeObj = $.fn.zTree.getZTreeObj(tree);
    if(treeObj != null){
        var nodes = treeObj.getCheckedNodes(true);
        var len = nodes.length;
        if(len > 0){
            for(var i=0; i<len; i++){
                formData['resourceIds['+i+']'] = nodes[i].id;
            }
        }
    }
}

/* 修改角色 */
function toModifyRole(roleId){
    var idx = LayerUtil.load();

    Ajax.post({
        url: '/sys/getRoleForModify',
        data: {id: roleId},
        success: function(result){
            layer.close(idx);

            if(result.success){

                var contentHtml = template('addRoleTemplate', result.data);
                LayerUtil.alertNoIcon({
                    content: contentHtml,
                    title: '修改角色',
                    width: 450,
                    callBack: function(index){
                        var formData = getFormData('#addRoleForm');
                        if(formData==null){
                            return false;
                        }
                        formData.id = roleId;
                        formData.roleDescription = formData.roleDescription.trim();
                        packageResourceIdsForModify("resourceTree", formData);

                        //console.log(formData);

                        Ajax.post({
                            url: '/sys/modifyRole',
                            data: formData,
                            success: function(result){
                                if(result.success){
                                    LayerUtil.msg('角色修改成功');
                                    getAllRoles();
                                }else{
                                    //TODO 因为 layer.msg 会关闭 layer.alert 暂时没时间解决
                                    alert(result.message);
                                }
                            }
                        });

                    }
                });

                $.fn.zTree.init($('#resourceTree'), zTreeSetting, result.data.resourceList);

            }else{
                LayerUtil.msg(result.message);
            }
        }
    });
}

function packageResourceIdsForModify(tree, formData){
    var treeObj = $.fn.zTree.getZTreeObj(tree);
    if(treeObj == null){
        return;
    }
    var nodes = treeObj.getChangeCheckedNodes();
    if(nodes.length == 0){ //没有改变资源
        return;
    }

    formData.simpleModify = false;

    nodes = treeObj.getCheckedNodes(true);
    var len = nodes.length;
    if(len > 0){
        for(var i=0; i<len; i++){
            formData['resourceIds['+i+']'] = nodes[i].id;
        }
    }
}

/* 删除角色 */
function toDeleteRole(el){
    var roleId = $(el).data('role-id');
    var roleName = $(el).data('role-name');
    var msg = '您确认要删除角色【 '+roleName+' 】吗？';
    LayerUtil.confirm(msg, function(){
        Ajax.post({
            url: '/sys/deleteRole',
            data: {id: roleId},
            success: function(result){
                if(result.success){
                    LayerUtil.msg('角色删除成功');
                    getAllRoles();
                }else{
                    LayerUtil.msg(result.message);
                }
            }
        });
    });
}

/* 查看资源 */
function showResource(el){
    var roleId = $(el).data('role-id');
    Ajax.get({
        url: '/sys/getRoleResourceZTree',
        data: {roleId: roleId},
        success: function(result){
            var td = $(el).parent();
            if(result.success){
                var html = template('roleResourceTreeTemplate', {roleId: roleId});
                td.html(html);
                $.fn.zTree.init($('#roleResourceTree_'+roleId), simpleZTreeSetting, result.data);
            }else{
                td.html(result.message);
            }
        }
    });
}
