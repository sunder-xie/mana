/**
 * 业务信息列表页面
 * @createDate 2016-12-27
 */

var Mana = {};
Mana.run = (function () {

    "use strict";

    var baseUrl = '/';

    var laypage;

    String.prototype.format = function() {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };

    function getDate(action, params, callback, noLoader) {
        var url = baseUrl + action,
            _params = {
                _t: Math.random()
            };
        if (arguments.length >= 3) {
            $.extend(_params, params);
        } else {
            callback = params;
        }
        if (!noLoader) {
            var loadingId = layer.load(2, { time: 30000 });
        }
        return $.getJSON(url, _params, function (json) {
            noLoader || layer.close(loadingId);
            callback(json);
        });
    }

    function postDate(action, params, callback, noLoader) {
        var url = baseUrl + action,
            _params = {
                _t: Math.random()
            };
        if (arguments.length >= 3) {
            $.extend(_params, params);
        } else {
            callback = params;
        }
        if (!noLoader) {
            var loadingId = layer.load(2, { time: 30000 });
        }
        return $.ajax({
            url: url,
            type: "POST",
            data: _params,
//            contentType: "application/json",
            dataType: "json",
            success: function (json) {
                noLoader || layer.close(loadingId);
                callback(json);
            }
        });
    }

    var Model = {

        getShopListInfo: function (name, callback) {
            var action = 'ucShop/getShopListInfo';
            var params = {
                companyName: name,
                pageSize: 10
            };
            return getDate(action, params, callback, true);
        },

        getCustomerList: function (params, callback) {
            var action = 'customer/getCustomerFormPagingResult';
            return getDate(action, params, callback);
        },

        getAllTypes: function (callback) {
            var action = 'coupon/getAllTypes';
            return getDate(action, callback);
        },

        addPreferentialList: function (params, callback) {
            var action = 'customer/addPreferentialList';
            return postDate(action, params, callback);
        }

    };

    var Server = {

        defaultPageSize: 10,

        pageSize: 0,

        queryStr: '',

        filterOpt: null,

        init: function () {
            Model.getCustomerList({
                pageSize: Server.defaultPageSize,
                pageIndex: 1
            }, function (json) {
                if (json.success) {
                    var $box = $('#wrapper');
                    var html = template('listTpl', json);
                    $box.html(html);
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(json.total / Server.defaultPageSize)
                    }, Server.jumpPage.bind(Server));
                    bindEvent();
                } else {
                    alert(json.message);
                }
            });
        },

        current: null,

        jumpPage: function (curr) {
            this.current = curr;
            var params = $.extend({
                pageSize: this.defaultPageSize,
                pageIndex: curr,
                queryStr: this.queryStr
            }, this.filterOpt);
            Model.getCustomerList(params, function (json) {
                if (json.success) {
                    var $box = $('#wrapper');
                    var html = template('listTpl', json);
                    $box.html(html);
                } else {
                    alert(json.message);
                }
            });
        },

        query: function (v) {
            var params = {
                pageSize: Server.defaultPageSize,
                pageIndex: 1,
                queryStr: v
            };
            Model.getCustomerList(params, function (json) {
                if (json.success) {
                    Server.queryStr = v;
                    LayerPage.reset({
                        pages: Math.ceil(json.total / Server.defaultPageSize)
                    });
                    var $box = $('#wrapper');
                    var html = template('listTpl', json);
                    $box.html(html);
                } else {
                    alert(json.message);
                }
            });
        },

        filter: function (opt) {
            var params = $.extend({
                pageSize: this.defaultPageSize,
                pageIndex: 1,
                queryStr: this.queryStr
            }, opt);
            Model.getCustomerList(params, function (json) {
                if (json.success) {
                    Server.filterOpt = opt;
                    LayerPage.reset({
                        pages: Math.ceil(json.total / Server.defaultPageSize)
                    });
                    var $box = $('#wrapper');
                    var html = template('listTpl', json);
                    $box.html(html);
                } else {
                    //TODO fixme
                    alert(json.message);
                }
            });
        },

        faquan: function (arr, obj) {
            var params = {};
            $.each(arr, function (i, v) {
                params['vehicleIdList[' + i + ']'] = v;
            });
            $.extend(params, obj);
            Model.addPreferentialList(params, function (json) {
                if (json.success) {
                    LayerUtil.msgFun('优惠记录添加成功', function(){
                        Server.jumpPage(Server.current);
                    })
                } else {
                    LayerUtil.msg(json.msg);
                }
            });
        },

        alertId: null,

        quanCallback: null,

        alertQuan: function (callback) {
            var _this = this;
            Model.getAllTypes(function (json) {
                if (json.success) {
                    var str = template('quantpl', json);
                    _this.alertId = layer.open({
                        type: 1,
                        title: ['添加优惠信息', 'background:#ecf0f5'],
                        area: ['auto', 'auto'],
                        content: str,
                        shade: [0.3, '#000'],
                        shift: 2
                        //fix: false
                    });
                } else {
                    layer.msg(json.message);
                }
            });
            this.quanCallback = callback;
        }

    };

    function getFormData(selector) {
        var data = {};
        $(selector + ' select').add(selector + ' input').each(function () {
            var value = $.trim(this.value);
            if(value != null && value != ""){
                data[this.name] = value;
            }
        });
        return data;
    }

    function clearFormDate(selector) {
        $(selector + ' select').add(selector + ' input').each(function () {
            this.value = '';
        });
    }

    function bindEvent() {
        $(document)
            .on('mousedown', '.mdsug li', function () {
                var $t = $(this);
                var $s = $('#suggetion');
//                var $id = $('#recommendShopId');
                $s.val($t.text());
//                $id.val($t.data('id'));
                $('.mdsug').empty().hide();
            })
            .on('input', '#suggetion', function () {
                var v = $.trim(this.value);
                var $sug = $('.mdsug');
                Model.getShopListInfo(v, function (json) {
                    if (json.success) {
                        var _s = '';
                        var list = json.list;
                        for (var i = 0; i < list.length; i++) {
                            _s += '<li data-id="{0}">{1}</li>'.format(list[i].id, list[i].companyName);
                        }
                        $sug.html('<ul>{0}</ul>'.format(_s)).show();
                    } else {
                        $sug.empty().hide();
                    }
                });
            })
            .on('blur', '#suggetion', function () {
                $('.mdsug').empty().hide();
            })
            .on('click', '#tianjia', function () {
                var arr = [];
                $('.cont input[type="checkbox"]:checked').each(function () {
                    arr.push(this.value);
                });
                if (arr.length == 0) {
                    layer.msg('请先选择需要发放优惠券的列表');
                    return;
                }
                Server.alertQuan(function (obj) {
                    Server.faquan(arr, obj);
                });
            })
            .on('click', '.faquan', function () {
                var $qs = $('.quans');
                var $qn = $('.qnum');
                var $qd = $('.qdate');
                var res = {
                    sendDate: $.trim($qd.val()),
                    preferentialType: $qs.val(),
                    preferentialNum: $.trim($qn.val())
                };
                if (res.preferentialType == '') {
                    layer.tips('请选择券', $qs, {
                        tips: [1, '#444']
                    });
                    return;
                }
                if (res.preferentialNum == '') {
                    layer.tips('请输入数量', $qn, {
                        tips: [1, '#444']
                    });
                    return;
                }
                if (res.preferentialNum <= 0) {
                    layer.tips('优惠券数量必须大于0', $qn, {
                        tips: [1, '#444']
                    });
                    return;
                }
                if (res.sendDate == '') {
                    layer.tips('请选择日期', $qd, {
                        tips: [1, '#444']
                    });
                    return;
                }
                layer.close(Server.alertId);
                Server.quanCallback && Server.quanCallback(res);
                Server.alertId = null;
                Server.quanCallback = null;
            })
            .on('change', '#quanbu', function () {
                if (this.checked) {
                    $('.cont input[type="checkbox"]').each(function () {
                        this.checked = true;
                    });
                } else {
                    $('.cont input[type="checkbox"]').each(function () {
                        this.checked = false;
                    });
                }
            })
            .on('click', '.cont > tr', function () {
                var input = $(this).find('input[type="checkbox"]')[0];
                input.checked = !input.checked;
            })
            .on('click', '.cont .xzk', function (e) {
                e.stopPropagation();
            })
            .on('click', '.cont a', function (e) {
                e.stopPropagation();
            })
            .on('click', '#resetFilter', function () {
                clearFormDate('#filterBox');
            })
            .on('click', '#okFilter', function () {
                var params = getFormData('#filterBox');
                Server.filter(params);
            })
            .on('click', '#searchBut', function () {
                var v = $.trim($('#searchTxt').val());
                Server.query(v);
            });
    }

//    return function () {
//        layui.use('laypage', function () {
//            laypage = layui.laypage;
//            Server.init();
//        });
//    }

    return Server.init;

})();

//template.helper('$dateFormat', function (timeStr) {
//    if(timeStr == null || timeStr == undefined){
//        return null;
//    }
//    var d = new Date(timeStr);
//    return d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
//});
