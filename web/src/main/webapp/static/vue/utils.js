(function ($, window, undefined) {
    function _Dom2MouseInit(option) {
        var op = $.extend({
            bubbles: true,                      //是否冒泡
            cancelable: true,                   //是否可以被取消
            view: document.defaultView,         //事件默认的视图,不能修改
            detail: 0,                          //附加的事件信息，单击次数
            screenX: 0,                         //事件距离屏幕左边的X坐标
            screenY: 0,                         //事件记录屏幕上边的Y坐标
            clientX: 0,                         //事件距离可视区域左边的X坐标
            clientY: 0,                         //事件距离可视区域上边的Y坐标
            ctrlKey: false,                     //ctrl是否被按下
            altKey: false,                      //alt是否被按下
            shiftKey: false,                    //shift是否被按下
            metaKey: false,                     //meta是否被按下, windows 键 windows / ⌘键  mac
            button: 0,                          //鼠标按键 0,1,2 左中右三键
            relatedTarget: null                 //只有在模拟mouseover和mouseout会用到
        }, option);
        for (key in op) {
            eval('var ' + key + '=op[key]');
        }
        var a = document.createEvent('MouseEvents');
        a.initMouseEvent(op.type, bubbles, cancelable, document.defaultView, detail, screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey, button, relatedTarget);
        return a;
    };
    $.fn.mouse = function (option) {
        $(this).each(function () {
            $(this)[0].dispatchEvent(_Dom2MouseInit(option));
        });
        return $(this);
    };
    window.vueUtils = {
        data: {
            OPTION_FIRST: ['请选择省', '请选择市', '请选择区', '请选择街道']
        },
        methods: {
            parseUrl: function () {
                var u = location.href.split('?'), d = {};
                if (u.length == 1) return {};
                u[1].split('&').forEach(function (v) {
                    var vd = v.split('=');
                    d[vd[0]] = vd[1];
                });
                return d;
            },
            //兼容my97data
            saveTime: function () {
                $(event.target).mouse({
                    type: 'input'
                })
            },
            timeReport: function (reg, time, voidmark) {
                var t = time ? new Date(time) : new Date();
                if (!time && voidmark) return '';
                var f = {
                    Y: t.getFullYear(),
                    M: t.getMonth() + 1,
                    D: t.getDate(),
                    h: t.getHours(),
                    m: t.getMinutes(),
                    s: t.getSeconds()
                };
                return reg.replace(/([YMDhms])+/g, function (v, i) {
                    var val = '0' + f[i];
                    if (i) {
                        return val.slice(-v.length);
                    }
                });
            },
            _getAddr: function (parent_id, obj, index, callback) {
                var _this = this;
                $.ajax({
                    type: "get",
                    data: {pid: parent_id},
                    dataType: "json",
                    url: '/common/regionListByPid',
                    success: function (json) {
                        if (json.length) {
                            var option = '<option value="" selected="selected">' + _this.OPTION_FIRST[index] + '</option>';
                            for (var i = 0; i < json.length; i++) {
                                var item = json[i];
                                option += '<option value="' + item.id + '">' + item.regionName + '</option>';
                            }
                        }
                        obj.empty().append(option);
                        console.log(obj);
                        if (!!callback) {
                            callback && callback(parent_id, obj);
                        } else {
                            obj.enable();
                        }
                    }
                });
            },
            getArea: function () {
                var _this = this, arg = arguments, l = arguments.length - 1, request = true;
                if (typeof arg[l] == 'boolean') {
                    arg = Array.prototype.slice.call(arg).slice(0, l);
                    request = arguments[l];
                    l--;
                }
                //区街道数据
                //1105增加未请求完成不能操作功能,稳定版，后续根据需求增加回调。暂时不加
                request && this._getAddr(1, arg[0], 0);
                arg[l - 1].disable();
                for (var i = 0; i < l; i++) {
                    (function (i) {
                        arg[i].disable().on('change', function () {
                            arg[i + 1].disable();
                            if ($(this).val() != '-1') {
                                for (var k = i + 1; k < arg.length; k++) {
                                    arg[k].empty().disable();
                                }
                                _this._getAddr($(this).val(), arg[i + 1], i + 1);
                            }
                        });
                    })(i);
                }
            },
            /**
             * 过滤数据中为空的属性, 在vue的数据对象请求前的过滤
             * @param data
             * @returns data
             */
            filterData: function (data) {
                for (var item in data) {
                    if (!data[item] && data[item] !== 0) {
                        delete data[item];
                    }
                }
                return data;
            },
            /**
             * 输入时判断
             * @param data
             *  @return data
             */
            inputCheck:function (data,reg,tips,eve) {
                var _target = $(eve.target);
                if(!data) {
                    _target.removeClass('add-red-border');
                    return false;
                }
                if(!reg.test(data)){
                    layer.alert('必须输入'+tips, {
                        icon: 7
                    });
                    _target.addClass('add-red-border');
                    return ;
                }
                _target.removeClass('add-red-border');
            },
            /**
             * 输入时判断是否为整数
             * @param data
             *  @return data
             */
            integerCheck:function (data,eve) {
                var reg = /^\d+$/,
                    tips = '整数';
                this.inputCheck(data,reg,tips,eve);
            },
            /**
             * 输入时判断是否为数字
             * @param data
             *  @return data
             */
            NumberCheck:function (data,eve) {
                var reg = /^\d+(\.(\d+)?)?$/,
                    tips = '数字';
                this.inputCheck(data,reg,tips,eve);
            }
            ,
            /**
             * 必须输入两位小数
             * @param data
             *  @return data
             */
            decimalCheck:function (data,eve) {
                var reg = /^\d+(\.\d{1,2})?$/,
                    tips = '两位以内的小数或整数';
                this.inputCheck(data,reg,tips,eve);
            }
        },
        component: {},
        filters: {
            'timeFormat': function (time, reg, mark) {
                if (mark && !time) {
                    return "";
                }
                var t = time ? new Date(time) : new Date();
                var f = {
                    Y: t.getFullYear(),
                    M: t.getMonth() + 1,
                    D: t.getDate(),
                    h: t.getHours(),
                    m: t.getMinutes(),
                    s: t.getSeconds()
                };
                return reg.replace(/([YMDhms])+/g, function (v, i) {
                    var val = '0' + f[i];
                    if (i) {
                        return val.slice(-v.length);
                    }
                })
            },
        }
    }

})($, window, undefined);