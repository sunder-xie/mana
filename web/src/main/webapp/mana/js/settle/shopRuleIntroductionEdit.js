/**
 * Created by zhouheng on 17/2/8.
 */

$(document).ready(function () {

    //初始化查询规则信息
    initQueryIntroductionInfo();

}).on('click', '.cancel-button', function () {

    window.location.href = "/introduction/shopRuleIntroductionInit"

}).on('click','.sure-button',function () {
    //更新规则说明
    addIntroductionInfo();

});

function initQueryIntroductionInfo() {
    var introductionId = $('#introductionId').val();
    var ruleType = $('#ruleType').val();
    if(introductionId != "" && introductionId != null){
        var action = "/introduction/getRuleIntroductionInfo";
        Ajax.get({
            url: action,
            data: {introductionId:introductionId},
            success: function (result) {
                if(result.success){
                    var ue = UE.getEditor('innerBox');
                    ue.ready(function() {
                        var obj = result.data;
                        ue.setContent(obj['ruleIntroduction']);
                    });
                }
            }
        });
    }else{
        UE.getEditor('innerBox');
    }
}

function addIntroductionInfo() {
    var introductionId = $('#introductionId').val();
    var ruleType = $('#ruleType').val();
    var ue = UE.getEditor('innerBox');
    var html = '';
    ue.ready(function() {
        html = ue.getContent();
    });
    var action = "/introduction/addRuleIntroduction";
    var reqParam = {
        id:introductionId,
        ruleType:ruleType,
        ruleIntroduction:html
    };
    Ajax.post({
        url: action,
        data:reqParam,
        success:function (result) {
            if(result.success){
                layer.msg("更新规则说明成功");
            }else{
                layer.msg(result.message);
            }
        }
    });
}
