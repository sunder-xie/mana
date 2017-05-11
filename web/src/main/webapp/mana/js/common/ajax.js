/**
 * Created by huangzhangting on 16/6/28.
 */

//jquery ajax请求
var JqAjax = {
    sendAjaxRequest: function(type, url, successFun, data, errorCallback, contentType, async){
        var param = {
            url: url,
            type: type,
            success: successFun,
            errorCallback: errorCallback
        };
        if(data!==void 0){
            param['data'] = data;
        }
        if(contentType!==void 0){
            param['contentType'] = contentType;
        }
        if(async===false){
            param['async'] = false;
        }

        var reqParam = AjaxParamProcessor.process(param);

        $.ajax(reqParam);
    },
    get: function(url, successFun, data, errorCallback, async){
        this.sendAjaxRequest('GET', url, successFun, data, errorCallback, undefined, async);
    },
    post: function(url, successFun, data, errorCallback, contentType){
        this.sendAjaxRequest('POST', url, successFun, data, errorCallback, contentType);
    },
    postJson: function(url, successFun, data, errorCallback){
        this.post(url, successFun, JSON.stringify(data), errorCallback, 'application/json');
    }
};

//使用对象传参（推荐使用）
var Ajax = function(){
    var sendAjaxRequest = function(param){
        return $.ajax(AjaxParamProcessor.process(param));
    };
    return {
        get: function(param){
            return sendAjaxRequest(param);
        },
        post: function(param){
            param.type = 'POST';
            return this.get(param);
        },
        postJson: function(param){
            param.contentType = 'application/json';
            param.data = JSON.stringify(param.data);
            return this.post(param);
        }
    };
}();


/** 请求参数处理器 */
var AjaxParamProcessor = {
    process: function(param){
        var reqParam = $.extend({
            url: '',
            type: 'GET',
            dataType: 'json',
            data: null,
            async: true,
            cache: true,
            contentType: 'application/x-www-form-urlencoded',
            timeout: 60000,
            beforeSend: undefined,
            success: function(){}
        }, param);

        var defaultTimeOutFun = function(){
            var isLoginPage = $('#isLoginPage').val(); //判断当前页面是否登录页
            if(isLoginPage === undefined){
                LayerUtil.confirm('非常抱歉，您登录超时了，请重新登录', function () {
                    location.href = '/';
                });
            }
        };

        var successFun = reqParam.success;

        reqParam.success = function(result){
            if(!result.success){
                if(result.code=='99999999'){ //登录超时
                    defaultTimeOutFun();
                    LayerUtil.closeAllLoad();
                    return false;
                }
                if(result.code=='99999995'){ //未认证通过
                    LayerUtil.msg(result.message);
                    LayerUtil.closeAllLoad();
                    return false;
                }
            }
            successFun(result);
        };

        /** 错误回调方法，封装对象 */
        var errorCallback = param.errorCallback;

        reqParam.error = function(XMLHttpRequest, textStatus, errorThrown){
            if(XMLHttpRequest.status==200 && 'parsererror'==textStatus){
                var responseText = XMLHttpRequest.responseText;
                if(responseText.indexOf('isLoginPage') > -1){ //返回登录页面
                    var hasTimeoutFun = false;
                    if(errorCallback!==undefined && errorCallback!==null){
                        if($.isFunction(errorCallback.timeout)){
                            /** 登录超时，回调方法 */
                            errorCallback.timeout();
                            hasTimeoutFun = true;
                        }
                    }
                    if(!hasTimeoutFun){
                        defaultTimeOutFun();
                    }

                }else if(responseText.indexOf('isErrorPage') > -1){ //返回系统报错页面
                    LayerUtil.msg('非常抱歉，系统发生内部错误');
                }

            }else if(XMLHttpRequest.readyState==0 && XMLHttpRequest.statusText=='error'){

            }else{// if(XMLHttpRequest.readyState==4)
                LayerUtil.msg('非常抱歉，系统发生内部错误');
            }

            if(errorCallback!==undefined && errorCallback!==null){
                if($.isFunction(errorCallback.unblockUI)){
                    /** 解除页面加载，回调方法 */
                    errorCallback.unblockUI();
                }
            }

            LayerUtil.closeAllLoad();
        };

        return reqParam;
    }
};
