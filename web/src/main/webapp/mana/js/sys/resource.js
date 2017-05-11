/**
 * Created by huangzhangting on 16/12/24.
 */

$(document).ready(function(){

    //初始化资源列表
    $('#tg').treegrid({
        title:'资源列表',
        rownumbers: true,
        animate:true,
        fitColumns:true,
        url:'/sys/getResourceBOListTree',
        idField:'id',
        treeField:'resourceName',
        lines:true,
        columns:[[
            {field:'resourceName',title:'资源名称',width:100},
            {field:'resourceUrl',title:'资源路径',width:100},
            {field:'resourcePermission',title:'资源权限',width:50},
            {field:'resourceDescription',title:'资源描述',width:100},
            {field:'typeString',title:'资源类型',width:40},
            {field:'resourceSort',title:'排序',width:40,align:'center'}
        ]],
        toolbar: [{
            text: '添加资源',
            iconCls: 'icon-add',
            handler: function(){
                toAdd();
            }
        },'-',{
            text: '修改资源',
            iconCls: 'icon-edit',
            handler: function(){
                var row = $('#tg').treegrid('getSelected');
                if(row==null){
                    LayerUtil.msg('请先选择一条记录');
                    return;
                }

                toModify(row.id);
            }
        },'-',{
            text: '删除资源',
            iconCls: 'icon-cancel',
            handler: function(){
                var row = $('#tg').treegrid('getSelected');
                if(row==null){
                    LayerUtil.msg('请先选择一条记录');
                    return;
                }

                toDelete(row);
            }
        },'-',{
            text: '回收站',
            iconCls: 'icon-help',
            handler: function(){
                LayerUtil.msg('该功能暂未开放');
            }
        }]
    });


});


function toAdd(){
    var dialogContent = $('#add_template').html();

    $('#add_dialog').dialog({
        title: '添加资源',
        width: 450,
        height: 360,
        closed: false,
        closable: true,
        cache: false,
        content: dialogContent,
        modal: true,
        buttons:[{
            text:'提交',
            handler:function(){

                var formData = getFormData('#add_form');
                if(formData==null){
                    return false;
                }

                Ajax.post({
                    url: '/sys/addResource',
                    data: formData,
                    success: function(result){
                        if(result.success){
                            $('#tg').treegrid('reload');
                            $('#add_dialog').dialog('close');
                            LayerUtil.msg('资源添加成功');
                        }else{
                            LayerUtil.msg(result.message);
                        }
                    }
                });

            }
        },{
            text:'关闭',
            handler:function(){
                $('#add_dialog').dialog('close');
            }
        }],
        onOpen:function(){

            $("#addPid").combotree({
                url: '/sys/getParentResourceTree'
            });

        }
    });

}

function clearPid(idStr){
    var input = $("#"+idStr);
    input.combotree('clear');
    input.combotree('setText', '默认为一级资源');

}

function toModify(id){

    Ajax.get({
        url: '/sys/getResourceById',
        data: {id : id},
        success: function(result){
            if(result.success){

                var dialogContent = template('modify_template', result.data);

                var pid = result.data.parentId;

                $('#add_dialog').dialog({
                    title: '修改资源',
                    width: 450,
                    height: 360,
                    closed: false,
                    closable: true,
                    cache: false,
                    content: dialogContent,
                    modal: true,
                    buttons:[{
                        text:'提交',
                        handler:function(){

                            var formData = getFormData('#modify_form');
                            if(formData==null){
                                return false;
                            }
                            formData.id = id;
                            formData.isDeleted = 'N';

                            Ajax.post({
                                url: '/sys/modifyResource',
                                data: formData,
                                success: function(result){
                                    if(result.success){
                                        $('#tg').treegrid('reload');
                                        $('#add_dialog').dialog('close');
                                        LayerUtil.msg('资源修改成功');
                                    }else{
                                        LayerUtil.msg(result.message);
                                    }
                                }
                            });

                        }
                    },{
                        text:'关闭',
                        handler:function(){
                            $('#add_dialog').dialog('close');
                        }
                    }],
                    onOpen:function(){
                        var input = $('#addPid');
                        input.combotree({
                            url: '/sys/getParentResourceTree?id='+id
                        });

                        if(pid==0){
                            input.combotree('setText', '默认为一级资源');
                        }else{
                            input.combotree('setValue', pid);
                        }
                    }
                });

            }else{

            }
        }
    });

}

function getFormData(form){
    var formData = MaNa.jqSerializeForm(form);
    var name = MaNa.repSpace(formData.resourceName);
    var sort = MaNa.repSpace(formData.resourceSort);
    var checkFlag = MaNa.isPositiveInt(sort);
    if(name==''){
        checkFlag = false;
    }
    if(!checkFlag){
        LayerUtil.msg('输入参数不合法，请再次检查');
        return null;
    }

    formData.resourceName = name;
    formData.resourceSort = sort;
    formData.resourceUrl = MaNa.repSpace(formData.resourceUrl);
    formData.resourcePermission = MaNa.repSpace(formData.resourcePermission);
    formData.resourceDescription = formData.resourceDescription.trim();

    if(!MaNa.isPositiveInt(formData.parentId)){
        formData.parentId = 0;
    }

    return formData;
}

function toDelete(row){
    var msg = '您确定要删除【 '+row.resourceName+' 】吗？';
    $.messager.confirm('确认信息', msg, function(r){
        if(r){

            Ajax.post({
                url: '/sys/deleteResource',
                data: {id: row.id},
                success: function(result){
                    if(result.success){
                        $('#tg').treegrid('reload');
                        LayerUtil.msg('资源删除成功');
                    }else{
                        LayerUtil.msg(result.message);
                    }
                }
            });

        }
    });
}
