/**
 * Created by huangzhangting on 16/11/30.
 */
$(document).ready(function() {

    initLeftMenu();

    var $doc = $(document);
    $doc.on("click",".parentMenu",function(){
        var _obj = $(this);
        iconChange(_obj,function(_obj){
            var subMenu = _obj.next();
            if(subMenu.hasClass('hidden')){
                subMenu.removeClass('hidden');
            }else{
                subMenu.addClass('hidden');
            }
        });
        //_obj.next().slideToggle();

    }).on("click",".administrator",function(){
        var obj = $(this);
        iconChange(obj,function(obj){
            $(".admin-menu-list").slideToggle();
        });
    });
    $('#logoutBtn').click(function(){
        var IDP_URL = $("#IDP_URL").val();

        $.ajax({
            type: 'GET',
            url: IDP_URL + 'logout',
            dataType: 'jsonp',
            success: function (data) {
                window.location.href = '/openidLogout/logout';
            }
        });

    });
});
/*上下箭头切换*/
function iconChange(_obj,callBack){
    var $action = _obj.find(".action");
    if(_obj.hasClass("down")){
        $action.text('收起');
        _obj.find(".fa-angle-down").removeClass("fa-angle-down").addClass("fa-angle-up");
        _obj.removeClass("down").addClass("up");
    }else{
        $action.text('展开');
        _obj.find(".fa-angle-up").removeClass("fa-angle-up").addClass("fa-angle-down");
        _obj.removeClass("up").addClass("down");
    }
    callBack && callBack(_obj);
}
/* 后期如果功能增加了，搞成数据库配置的，也很容易调整 */
function initLeftMenu(obj){
    var selectedLi = $('.mana-nav-left .selected');
    if(selectedLi.length==0){
        console.log('没有选中的菜单');
        return;
    }
    selectedLi.find(".current-icon").removeClass("hidden");
    if(selectedLi.hasClass('home-menu')){
        return;
    }

    var parent = selectedLi.parent();
    parent.removeClass('hidden');
    parent.find(".down-icon").css("transform","rotate(180deg)");
    parent.removeClass("down").addClass("up");


    //首页菜单
    //if(activeLi.hasClass('home-menu')){
    //    activeLi.find('a').append('<span class="selected"></span>');
    //    return;
    //}

    //非首页菜单
    //var parent = activeLi.parent();
    //parent.parent().addClass('active open');
    //var parentPrev = parent.prev();
    //parentPrev.append('<span class="selected"></span>');
    //parentPrev.find('.arrow').addClass('open');

}

/* 特殊菜单操作 */
function specialMenu(url){
    var accessCode = $('#SEND_COUPON_KEY').val();
    if(accessCode!==undefined && accessCode!=''){
        Ajax.get({
            url: '/common/checkSendCouponKey',
            data: {key: accessCode},
            success: function(result){
                if(result.success){
                    location.href = url;
                }else{
                    checkAccessCode(url);
                }
            }
        });

    }else{
        checkAccessCode(url);
    }
}
function checkAccessCode(url){
    LayerUtil.alert({
        content: '<input type="password" style="width:250px;height:28px;padding:2px 5px;border:1px solid #ddd" id="accessCode">',
        title: '请输入访问密码',
        btnText: '确定',
        showIcon: true,
        callBack: function(index){
            var accessCode = MaNa.repSpace($('#accessCode').val());
            //if(accessCode==''){
            //    LayerUtil.msg('请输入访问密码');
            //    return;
            //}

            Ajax.get({
                url: '/common/checkSendCouponKey',
                data: {key: accessCode},
                success: function(result){
                    if(result.success){
                        location.href = url;
                    }else{
                        LayerUtil.msg('访问密码错误');
                    }
                }
            });

            layer.close(index);
        }
    });

    $('#accessCode').val('').focus();
}