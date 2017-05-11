/**
 * 通用分页组件
 * @extends laypage
 * @version 0.0.1
 */


var LayerPage = (function (layui) {
    "use strict";
    var laypage;
    var config = {
        cont: null,
        pages: 0,
        curr: 1,
        groups: 5,
        skin: '#287f87',
        first: '首页',
        last: '尾页',
        prev: '<',
        next: '>',
        skip: true
    };
    var fenyeCallback = null;
    var contArr = [];

    function _jump(obj, first) {
        if (first)
            return;
        $.each(contArr, function () {
//            if (obj.cont == this)
//                return false;
            if (obj.cont != this) {
                laypage($.extend({}, obj, { cont: this }));
            }
        });
        fenyeCallback(obj.curr);
    }

    function _init() {
        config.jump = _jump;
        $.each(contArr, function () {
            laypage($.extend({}, config, { cont: this }));
        });
    }

    return {

        init: function (opt, callback) {
            fenyeCallback = callback;
            if (typeof opt.cont == 'string') {
                contArr = [opt.cont];
            } else {
                contArr = opt.cont;
            }
            delete opt.cont;
            $.extend(config, opt);
            layui.use('laypage', function () {
                laypage = layui.laypage;
                _init();
            });
        },

        reset: function (opt, callback) {
            if (typeof opt == 'function') {
                fenyeCallback = opt;
            } else if (opt != undefined) {
                if (typeof opt.cont == 'string') {
                    contArr = [opt.cont];
                } else if (opt.cont != undefined) {
                    contArr = opt.cont;
                }
                delete opt.cont;
                $.extend(config, opt);
                if (callback)
                    fenyeCallback = callback;
            }
            _init();
        }

    };
})(layui);