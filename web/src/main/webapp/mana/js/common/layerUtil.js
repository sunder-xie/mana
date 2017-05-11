/**
 * Created by huangzhangting on 16/11/2.
 */

var LayerUtil = function(){
    var backgroundColor = '#393D49';
    var shadeOpacity = 0.2;

    return{
        /** 页面中间冒泡 */
        msg: function(msg, time, callBack){
            if(time===undefined || time===null){
                time = 2000;
            }
            layer.msg(msg, {shade: [shadeOpacity, backgroundColor], time: time}, function(){
                if($.isFunction(callBack)){
                    callBack();
                }
            });
        },
        msgFun: function(msg, callBack){
            LayerUtil.msg(msg, undefined, callBack);
        },
        /** 在指定元素附近冒泡
         *  obj：指定元素，可以是js对象、jquery对象、jquery支持的选择器
         *  position：1-上边 2-右边 3-下边 4-左边
         *  tipsMore：true-不关闭之前的tips，默认关闭
         * */
        tips: function(msg, obj, position,showTime, tipsMore){
            if(position===undefined || position===null){
                position = 1;
            }
            var options = {tips: [position, backgroundColor],time:showTime};
            if(tipsMore===true){
                options.tipsMore = true;
            }
            layer.tips(msg, obj, options);
        },
        tipsMore: function(msg, obj, position){
            LayerUtil.tips(msg, obj, position, true);
        },
        /** 页面加载层，最长等待time毫秒，默认等待20秒
         * 返回值用于关闭加载：layer.close(index);
         * */
        load: function(time){
            if(time===undefined || time===null){
                time = 20000;
            }
            return layer.load(2, {shade: [shadeOpacity, backgroundColor], time: time});
        },
        /** 粗暴的关闭所有加载层 */
        closeAllLoad: function(){
            layer.closeAll('loading');
        },
        /** 信息确认 */
        confirm: function(msg, callBack){
            layer.confirm(msg, {icon: 3, title:'确认框'}, function(index){
                if($.isFunction(callBack)){
                    callBack();
                }
                layer.close(index);
            });
        },
        /** 信息框
         * 基于layer.alert简单的实现，基本满足需求了，
         * 如果不够用，可以使用layer.open，再封装一个方法，做的更复杂些
         * */
        alert: function(param){
            var p = $.extend({
                content: '',  //内容可以是html，弹窗的宽度、高度会自适应内容
                title: '提示',
                btnText: '确定',
                showIcon: true,
                width: undefined, //有时候自适应效果不佳，可以使用这个参数，一般情况不需要用到
                callBack: undefined
            }, param);

            var options = {title: p.title};
            if(p.btnText!==undefined){
                options.btn = [p.btnText];
            }
            if(p.showIcon===true){
                options.icon = 0;
            }

            layer.alert(p.content, options, function(index){
                if($.isFunction(p.callBack)){
                    p.callBack(index);
                }else{
                    layer.close(index);
                }
            });
            if(p.width!==undefined){
                $('.layui-layer-dialog').css('width', p.width);
            }
        },
        /** 没有图标 */
        alertNoIcon: function(param){
            param.showIcon = false;
            LayerUtil.alert(param);
        },
        open: function(param){
            return layer.open($.extend({
                content: '',
                title: '信息',
                btn: '确认',
                area : ['auto','auto'],
                callBack: undefined
            },param));
        }
    };
}();
