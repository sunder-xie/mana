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

$(document).ready(function(){

    getUserPage(1);

    $('#searchBtn').click(function(){
        getUserPage(1);
    });

    var input = $('#searchStaffNo');
    input.focus();
    $('.search-div input').bind('keypress', function (event) {
        if (event.keyCode == 13) {
            $('#searchBtn').trigger('click');
        }
    });

    $('#resetBtn').click(function(){
        $('.search-div input').val('');
        $('#searchStaffNo').focus();
    });

});


var _pageSize = 20;
var _pageIndex = 1;

//分页
var pageInit = function (curr, total) {
    JqPage.init({
        //总记录数
        itemSize: total,
        //每页记录数
        pageSize: _pageSize,
        //当前页
        current: curr || 1,
        //点击分页后的回调
        backFn: function (p) {
            getUserPage(p);
        }
    });
};

function getReqParam(page){
    _pageIndex = page;
    var param = {
        pageIndex: page,
        pageSize: _pageSize
    };
    var searchStaffNo = MaNa.repSpace($('#searchStaffNo').val());
    if(searchStaffNo != ''){
        param.staffNo = searchStaffNo;
    }
    var searchMobile = MaNa.repSpace($('#searchMobile').val());
    if(searchMobile != ''){
        param.mobile = searchMobile;
    }
    var searchName = MaNa.repSpace($('#searchName').val());
    if(searchName != ''){
        param.name = searchName;
    }

    return param;
}

function getUserPage(page){
    var index = LayerUtil.load();

    var param = getReqParam(page);

    Ajax.get({
        url: '/sys/getUserPage',
        data: param,
        success: function(result){
            var contentDiv = $('.content-div');
            if(result.success){
                var html = template('userListTemplate', result);
                contentDiv.html(html);

                pageInit(page, result.total);

                bindTableBtnEvent();
            }else{
                contentDiv.html('<label>'+result.message+'</label>');
            }

            layer.close(index);
        }
    });
}

/* 表格内部按钮绑定事件 */
function bindTableBtnEvent(){
    $('.modify-role-btn').click(function(){
        toModifyRole(this);
    });
}

function toModifyRole(el){
    var userName = $(el).data('user-name');
    var realName = $(el).data('real-name');
    var msg = realName+' ( '+userName+' )';
    var contentHtml = template('modifyRoleTemplate', {user: msg});

    LayerUtil.alertNoIcon({
        content: contentHtml,
        title: '配置角色',
        width: 450,
        callBack: function(index){
            var formData = getFormData(userName);
            if(formData==null){
                alert('角色配置没有改变');
                return false;
            }

            Ajax.post({
                url: '/sys/modifyUserRole',
                data: formData,
                success: function(result){
                    if(result.success){
                        LayerUtil.msg('角色配置成功');
                    }else{
                        alert(result.message);
                    }
                }
            });
        }
    });

    var zTree = $('#roleTree');
    Ajax.get({
        url: '/sys/getUserRoleZTree',
        data: {userName: userName},
        success: function(result){
            $.fn.zTree.init(zTree, zTreeSetting, result);
        }
    });
}

function getFormData(userName){
    var treeObj = $.fn.zTree.getZTreeObj('roleTree');
    if(treeObj == null){
        return null;
    }
    var nodes = treeObj.getChangeCheckedNodes();
    if(nodes.length == 0){ //没有改变角色
        return null;
    }

    var formData = {userName: userName};
    nodes = treeObj.getCheckedNodes(true);
    var len = nodes.length;
    if(len > 0){
        for(var i=0; i<len; i++){
            formData['roleIds['+i+']'] = nodes[i].id;
        }
    }
    return formData;
}

