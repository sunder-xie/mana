/**
 * Created by zhouheng on 16/12/27.
 */

$(document).ready(function(){

    //保单详情初始化
    initInsuranceInfo();

});

//初始化保单信息
function initInsuranceInfo() {

    var index= LayerUtil.load();
    //保单id
    var formId = $('#formId').val();
    //门店id
    var agentId = $('#agentId').val();

    Ajax.get({
        url:'/insurance/getInsuranceFormInfo',
        data:{
            formId:formId,
            agentId:agentId
        },
        success:function(result){

            var data = result.data;

            layer.close(index);
        }
    });
}
$(document).on("click","#back",function(){
    window.history.go(-1);
})