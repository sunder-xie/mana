/**
 * Created by huangzhangting on 17/3/8.
 */

template.helper('isShowHelper', function(val){
    var msg = '';
    switch (val){
        case 1: msg = '<em style="color:red;font-size:14px;">是</em>'; break;
        case 0: msg = '否'; break;
        default: break;
    }
    return msg;
});

$(document).ready(function(){
    getAllFormulaConfigs();

    $('#refreshBtn').click(function(){
        getAllFormulaConfigs();
    });
    $('#addBtn').click(function(){
        addDataPage();
    });
    $('#returnBtn').click(function(){
        $('#refreshBtn').removeClass('hidden');
        $('#addBtn').removeClass('hidden');
        $('#returnBtn').addClass('hidden');
        $('#saveBtn').addClass('hidden');

        getAllFormulaConfigs();
    });
    $('#saveBtn').click(function(){
        saveData();
    });
});

function getAllFormulaConfigs(){
    var index = LayerUtil.load();
    Ajax.get({
        url: '/settle/formulaConfig/getAllFormulaConfigs',
        success: function(result){
            //console.log(result);
            var div = $('.content-div');
            if(result.success){
                if(result.data.length==0){
                    div.html('<label>暂无数据</label>');
                }else{
                    div.html(template('listTemplate', result));

                    $('.delete-formula-btn').click(function(){
                        deleteFormulaConfig(this);
                    });

                    $('.modify-formula-btn').click(function(){
                        toModifyFormulaConfig(this);
                    });

                    $('.modify-variable-btn').click(function(){
                        toModifyFormulaVariable(this);
                    });
                }
            }else{
                LayerUtil.msg(result.message);
            }

            layer.close(index);
        }
    })
}

function addDataPage(){
    $('#refreshBtn').addClass('hidden');
    $('#addBtn').addClass('hidden');
    $('#returnBtn').removeClass('hidden');
    $('#saveBtn').removeClass('hidden');

    $('.content-div').html(template('addTemplate', {}));

    Ajax.get({
        url: '/settle/formulaConfig/getSpecialVariables',
        success: function(result){
            var td = $('#specialVariableList');
            if(result.success){
                td.html(template('specialVariableTemplate', result));

                $('.special-variable-btn').click(function(){
                    var formulaExpress = $('input[name="formulaExpress"]');
                    formulaExpress.val(formulaExpress.val()+' '+$(this).data('value')+' ');
                    formulaExpress.focus();
                });
            }else{
                td.html(result.message);
            }
        }
    });
}
function saveData(){
    var input = $('input[name="formulaKey"]');
    var formulaKey = input.val().trim();
    if(formulaKey==''){
        LayerUtil.tips('请填写公式key', input);
        return;
    }

    input = $('input[name="formulaName"]');
    var formulaName = input.val().trim();
    if(formulaName==''){
        LayerUtil.tips('请填写公式名称', input);
        return;
    }

    input = $('input[name="formulaExpress"]');
    var formulaExpress = input.val().trim();
    if(formulaExpress==''){
        LayerUtil.tips('请填写计算公式', input);
        return;
    }

    input = $('textarea[name="formulaExplain"]');
    var formulaExplain = input.val().trim();
    if(formulaExplain==''){
        LayerUtil.tips('请填写公式说明', input);
        return;
    }

    var index = LayerUtil.load();

    Ajax.post({
        url: '/settle/formulaConfig/addFormulaConfig',
        data: {
            formulaKey: formulaKey,
            formulaName: formulaName,
            formulaExpress: formulaExpress,
            formulaExplain: formulaExplain,
            isShow: $('select[name="isShow"]').val()
        },
        success: function(result){
            if(result.success){
                LayerUtil.msgFun('保存成功', function(){
                    $('#returnBtn').trigger('click');
                });
            }else{
                LayerUtil.msg(result.message);
            }
            layer.close(index);
        }
    });

}

function deleteFormulaConfig(el){

    LayerUtil.confirm('确认要删除吗？', function(){

        var index = LayerUtil.load();
        var dataId = $(el).data('id');
        Ajax.post({
            url: '/settle/formulaConfig/deleteFormulaConfig',
            data: {id: dataId},
            success: function(result){
                if(result.success){
                    LayerUtil.msgFun('删除成功', function(){
                        getAllFormulaConfigs();
                    });
                }else{
                    LayerUtil.msg(result.message);
                }

                layer.close(index);
            }
        });

    });

}

function toModifyFormulaConfig(el){
    LayerUtil.tips('还在开发中。。。', el);
}

function toModifyFormulaVariable(el){
    LayerUtil.tips('还在开发中。。。', el);
}
