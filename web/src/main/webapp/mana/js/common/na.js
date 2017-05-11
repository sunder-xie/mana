/*
 * xb 05-25 多选框
 * 0923 重写,统一输出,优化结构,支持多个多选框,改变外观 v2.0
 * 0924 减少了循环中的dom操作
 * 配置基本通过class增加,init多个多选框因此需要简单的init参数传入.故把部分参数移到了class上
 * 0928,剩一个css难点没完成
 * 1023修复输出值
 * 1104修复初始化时返回的输出值，统一任何状态下返回data是个list。去除多余选择器和注视。稳定版本
 * 1124 增加模糊搜索功能。取消jquery-ui的引用，使用之前的多选框的结构。
 * 1125 支持freemarker和单独使用，freemarker用法依旧，渲染完毕调用。单独使用传入url，后期可以和省区市联动频繁触发。大改！
 * 1126 有个优化采用闭包，和勇哥讨论下性能差距再改进
 * 1126 单独传入url，JS渲染下次使用
 * 1203 增加单选功能。。。
 * 03XX 单选模仿select
 * 0422 有默认值自动触发匹配时同时更新状态
 * 0429 初始值的保留
 * 0430 区分匹配事件执行不同的效果
 *
 * 0618 模糊搜索支持大小写,正常0,小写1,大写2,娜娜六级加油
 * 0629 增加全选方式优化，遍历option性能优化，多选留到saint时修复
 */
//TODO tile增加 from saint
(function(window, undefined) {
    var Interval,mark;
    $.fn.multiNa = function (extraExport, isReg, oneCheck,refreshMark, headItem, lowerUpperMixed) {

        $(this).addClass(lowerUpperMixed? 'lowerUpperMixed':'').hide();
        //循环读取所有的需要多选
        if (oneCheck) {
            //如果没有额外参数，并且单选，就当为单选，模仿正常的多选框使用，为了兼容，依旧提供val和des
            if(extraExport && extraExport.length==0){
                var isOne=true;
                $(this).addClass('isOne');
            }
            $.each($(this), function (i) {
                var T = $(this),old=false;
                T.data('paramXb',{extraExport:extraExport,isReg:isReg,oneCheck:oneCheck});
                var desList = "";
                var valList = "";
                if(T.next('.multiSelect').length) {
                    old = T.next('.multiSelect');
                }
                var showArea = old ?
                    old : $('<div class="multiSelect form-control" >').attr('id', $(this)[0].id + "Show");
                !old && showArea.append($('<div class="msContent ">'));
                var content = showArea.find('.msContent');
                if(isReg) {
                    if (!old) {
                        showArea.addClass('isReg');
                        var $i = $('<input type="text" class="multiInput xtq_rect_input_text goods_code xtq_border_action" placeholder="请输入">');
                        if(T.hasClass('enterMark')) {
                            $i.addClass('entryList');
                        }
                        if(T.hasClass('enterFirst')) {
                            $i.addClass('focusFirst');
                        }
                        content.append($i.css('width', $(this).parent().width()));
                    } else {
                        var $i = T.next().find('.multiInput');
                    }
                }
                if(T.attr('readonly')){
                    showArea.addClass('disabled');
                    content.find('.multiInput').prop('disabled',true).prop('readonly',true);
                }
                var listArea = $('<div class="simpleList">');
                if(!!headItem) {
                    var $op = $('<option value="">').html(headItem);
                    $(this).prepend($op);
                }
                //循环每个需要单选的内部的option
                $(this).find('option').each(function () {
                    var $p = $('<p class="item">');
                    //获取匹配字段，显示字段和值字段
                    var regText = $.trim($(this).html()), showText = regText.split('%na%')[0], val = $(this).val()||'';
                    if (!isOne) {
                        for (var k = 0; k < extraExport.length; k++) {
                            $p.data(extraExport[k], $(this).data(extraExport[k]));
                        }
                    }
                    $p.data('value', val).data('data', $(this).data('data'));
                    if(refreshMark && ($(this).is('.initSelected') || $(this).prop('selected'))){
                        T.find('option').removeAttr('selected');
                        $(this).prop('selected',true).addClass('initSelected');
                        desList = showText;
                        valList = val;
                        if(!isOne) {
                            for (var k = 0; k < extraExport.length; k++) {
                                T.data(extraExport[k], $(this).data(extraExport[k]));
                            }
                        }
                        if(!$(this).is('.void') && $i && !$i.is('.dynamicNa')) {
                            $i.val(showText);
                        }
                    }else if ($(this).prop('selected')) {
                        $(this).addClass('initSelected');
                        desList = showText;
                        valList = val;
                        if(!isOne) {
                            for (var k = 0; k < extraExport.length; k++) {
                                T.data(extraExport[k], $(this).data(extraExport[k]));
                            }
                        }
                        if(!$(this).is('.void') && $i && !$i.is('.dynamicNa')) {
                            $i.val($.trim(showText));
                        }
                    }
                    var $s = $('<span class="des">');
                    $s.data('reg', (T.hasClass('lowerUpperMixed') || lowerUpperMixed)? regText.toLowerCase() : regText )
                        .html(showText);
                    $p.append($s);
                    if (T.is('.sort')) {
                        listArea.prepend($p);
                    } else {
                        listArea.append($p);
                    }
                });
                $(this).data('args', extraExport).data('desList', desList).data('valList', valList).after(listArea).after(showArea);
                if(isOne){
                    $(this).val(valList);
                }
            });
        } else {
            $.each($(this), function (i) {
                var T = $(this);
                T.data('paramXb',{extraExport:extraExport,isReg:isReg,oneCheck:oneCheck});
                var desList = [];
                var valList = [];
                var showArea = $('<div class="multiSelect form-control" >').attr('id', $(this)[0].id + "Show");
                if (isReg) {
                    showArea.addClass('isReg');
                }
                $(this).val('');
                showArea.append($('<div class="msContent ">'));
                var content = showArea.find('.msContent');
                var listArea = $('<div class="multiDownList">').attr('id', $(this)[0].id + "List");
                var optionNumbers = $(this).find('option').length;
                //循环每个需要单选的内部的option
                $(this).find('option').each(function () {
                    var $p = $('<p class="item">');
                    var $i = $('<input type="checkbox" class="itemI">');
                    if (!$(this).val()) {
                        optionNumbers--;
                        return true;
                    }
                    if (extraExport.length) {
                        for (var k = 0; k < extraExport.length; k++) {
                            $i.data(extraExport[k], $(this).data(extraExport[k]));
                        }
                    }
                    $i.val($(this).val()).data('data', $(this).data('data'));
                    if ($(this).data('n')) {
                        $i.prop('checked', true);
                        desList.push($.trim($(this).html() + "-" + (optionNumbers - $(this).index())));
                        valList.push($(this).val());
                        var $div = $("<span class='selectDes'>");
                        $div.data('index', T.is('.sort') ? $(this).index() : optionNumbers - $(this).index() - 1);
                        if(extraExport.length) {
                            for (var k = 0; k < extraExport.length; k++) {
                                $div.data(extraExport[k], $(this).data(extraExport[k]));
                            }
                        }
                        $div.html($.trim($(this).html()));
                        content.append($div);
                    }
                    var $s = $('<span class="des">');
                    $s.html($.trim($(this).html()).split('%na%')[0]).data('reg', $.trim($(this).html()).split('%na%')[0]);
                    $p.append($i).append($s);
                    if (T.is('.sort')) {
                        listArea.prepend($p);
                    } else {
                        listArea.append($p);
                    }
                });
                listArea.data('all', optionNumbers);
                if (isReg) {
                    var $i = $('<input type="text" class="multiInput xtq_rect_input_text goods_code xtq_border_action" placeholder="多个查询用逗号分割">');
                    content.append($i.css('width', $(this).parent().width()));
                }
                if ($(this).is('.allSelected')) {
                    var $p = $('<p class="item selectAllDiv">');
                    var $i = $('<input type="checkbox" class="selectAll">');
                    $p.append($i).append("<span class='des'>全部(" + optionNumbers + ")</span>");
                    listArea.prepend($p);
                    $(this).attr('all', true);
                }
                $(this).data('args', extraExport).data('desList', desList).data('valList', valList).after(listArea).after(showArea);
                refreshMark && $(this).data('valList','').data('valList','');
            });
        }
        return $(this);
    };
    $.fn.refresh=function(){
        var t=$(this);
        var $input = $(this).next().find('.multiInput');
        var focus =$input.is(':focus');
        $(this).parent().find('.multiSelect .selectDes,.simpleList,.multiDownList').remove();
        $(this).find('.initSelected').removeClass('initSelected');
        $(this).find('option:selected').addClass('initSelected');
        var data=$(this).data('paramXb');
        $(this).multiNa(data.extraExport,data.isReg,data.oneCheck,true);
        if(focus) {
            if($input.is('.dynamicNa')) {
                $input.focus().click();
            }
        }
        return t;
    };
    $.fn.reset = function() {
        var t=$(this);
        var $input = $(this).next().find('.multiInput');
        var focus =$input.is(':focus');
        $(this).parent().find('.multiSelect .selectDes,.simpleList,.multiDownList').remove();
        $(this).find('option:selected').removeAttr('selected');
        var data=$(this).data('paramXb');
        $(this).multiNa(data.extraExport,data.isReg,data.oneCheck,true);
        if(focus) {
            if($input.is('.dynamicNa')) {
                $input.focus().click();
            }
        }
        return t;
    };
    $.fn.clear=function(){
        var t=$(this);
        $(this).parent().find('.multiSelect,.simpleList,.multiDownList').remove();
        var data=$(this).data('paramXb');
        $(this).multiNa(data.extraExport,data.isReg,data.oneCheck,true);
        return t;
    };
    $.fn.rebind=function(value){
        var t=$(this);
        $(this).parent().find('.multiSelect,.simpleList,.multiDownList').remove();
        var data=$(this).data('paramXb');
        $(this).multiNa(data.extraExport,data.isReg,data.oneCheck);
        $(this).parent().find('.multiSelect,.simpleList,.multiDownList').show();
        $(this).parent().find('.multiInput').val(value).focus();
        return t;
    };
    $.fn.disable=function(){
        var t=$(this);
        $(this).next().addClass('disabled').find(".multiInput").prop('readonly',true).prop('disabled',true);
        return t;
    };
    $.fn.enable=function(){
        var t=$(this);
        $(this).next().removeClass('disabled').find(".multiInput").prop('readonly',false).prop('disabled',false);
        return t;
    };
    function _open() {
        var $t = $(this);
        var $list = $(this).next();
        $('.nFocus').removeClass('nFocus');
        $t.addClass('nFocus');
        $('.multiDownList,.simpleList').hide();
        $list.css('width',$t.width()).show(100);
    }
    function _close() {
        $('.nFocus').removeClass('nFocus');
        $('.multiDownList,.simpleList').hide();
        $('.multiDownList>.item,.simpleList>.item').show();
    }
    //绑定事件 if打开单个多选框，else关闭所有单选框
    $(document).on('click', function (e) {
        var t = $(e.target);
        var $f = t.closest('.multiSelect');
        var $input;
        if (t.parent().is('.msContent') || t[0].className && t[0].className.indexOf('msContent') >= 0 || t.is('.multiSelect') || t.parents('.simpleList').length == 1 || t.parents('.multiDownList').length == 1) {
            if (!$(e.target).is('.dynamicNa') && typeof e.originalEvent == "undefined") {
                return;
            }
            if(t.parents('.disabled').length==1){
                return
            }
            //当前打开和点击的不是同一个插件时，关闭打开的。用于插件之间焦点切换
            if(!$f.is($('.nFocus'))) {
                _close($('.nFocus'));
            }
            var $multi = t.parents('.multiSelect');
            if ($multi.length) {
                if ($multi.is('.isReg')) {
                    $input = $multi.find('.multiInput');
                    ////禁用滚动
                    //$('body').css('overflow','hidden');
                    //如果有值，触发匹配
                    $input.val()?$input.trigger('input'):$input.focus();
                    $multi.data('left', $multi[0].scrollLeft);
                    $multi[0].scrollLeft = $multi.find('.multiInput')[0].offsetLeft;
                }
                var list = $multi.next();
                if (list.css('display') == "none") {
                    if(!$f.length && t.is('.dynamicNa')) {
                        $f = t.parents('.multiSelect');
                    }
                    _open.call($f);
                } else {
                    if( !$input || document.activeElement != $input[0]){
                        _close.call($f);
                    }
                }
            }else{
                $('.multiSelect').removeClass('nFocus');
                ////恢复页面滚动
                //$('body').removeAttr('style');
            }
        } else {
            if ($f.length) {
                $f[0].scrollLeft = $f.data('left');
            }
            $('.stopSearch').removeClass('stopSearch');
            _close.call($f);
            if ($('.simpleChecked').length == 1) {
                $('.simpleChecked').trigger('click').removeClass('simpleChecked');
            }
        }
    });
    //多选选择事件
    $(document).on('click', '.multiDownList>p', function (e) {
        var $i = $(this).find('input');
        var $s = $(this).find('span');
        e.target.type == undefined && $i.prop('checked', !$i.prop('checked'));
        var $p = $(this).parents('.multiDownList');
        var sources = $p.prev().prev();
        var showArea = sources.next().find('.msContent');
        var desList = sources.data('desList') || [];
        var valList = sources.data('valList') || [];
        // 选中全选时
        if ($(this).is('.selectAll') || $(this).is('.selectAllDiv')) {
            desList = [];
            valList = [];
            showArea.find('.selectDes').remove();
            if ($i.prop('checked') || $(this).prop('checked')) {
                $.each($(this).parent().find('.itemI'), function () {
                    desList.push($(this).next().html());
                    valList.push($(this).val());
                });
                $(this).parent().find('.itemI').prop('checked',true).end().find('.item').addClass('childChecked');
                showArea.prepend("<span class='selectDes'>全部(" + $p.data('all') + ")</span>");
                $('.nFocus').data('left', 0);
            }
            else {
                $p.find('.selectItemI,.itemI').prop('checked',false).removeClass('childChecked');
            }
            //选中其他选项时
        } else {
            var index = $(this).index('.item:not(".selectAllDiv")');
            if ($i.prop('checked')) {
                var $div = $("<span class='selectDes'>");
                $i.parent().addClass('childChecked');
                $div.data('index', index);
                desList.push($s.html() + "-" + index);
                valList.push($i.val());
                if (sources.data('args')) {
                    for (var i = 0; i < sources.data('args').length; i++) {
                        $div.data(sources.data('args')[i], $(this).find('.itemI').data(sources.data('args')[i]));
                    }
                }
                $div.html($s.html());
                if ($p.data('all') == valList.length) {
                    $p.find('.selectAll').prop('checked', true);
                    showArea.find('.selectDes').remove().end().prepend("<span class='selectDes'>全部(" + $p.data('all') + ")</span>");
                    $('.nFocus').data('left', 0);
                } else {
                    //判断是否有模糊功能
                    if($p.prev().is('.isReg')){
                        var offset;
                        showArea.find('.selectDes:last').length ? showArea.find('.selectDes:last').after($div): showArea.prepend($div);
                        if(showArea.children('.multiInput').val()){
                            //新增或者追加，同时把输入框位置定住
                            offset=showArea.find('.multiInput')[0].offsetLeft;
                            showArea.children(".multiInput").val("");
                        }else {
                            //偏移量右移当前选中的长度 宽度+pa3+ma10+4脑补
                            offset=showArea.find('.multiInput')[0].offsetLeft- $s.width()-17;
                        }
                        showArea.parent()[0].scrollLeft=offset;
                    }else{
                        showArea.find('.selectDes:last').length?showArea.find('.selectDes:last').after($div):showArea.prepend($div);
                    }
                }
            }
            else {
                desList.splice($.inArray($s.html() + "-" + index, desList), 1);
                valList.splice($.inArray($i.val(), valList), 1);
                if ($p.find('.selectAll').prop('checked')) {
                    showArea.find('.selectDes').eq(0).remove();
                    $.each($p.find('.itemI:checked'), function () {
                        var $div = $("<span class='selectDes'>");
                        $div.html($(this).next().html());
                        if(sources.data('args')) {
                            for (var i = 0; i < sources.data('args').length; i++) {
                                $div.data(sources.data('args')[i], $(this).find('.itemI').data(sources.data('args')[i]));
                            }
                        }
                        showArea.find('.selectDes').length ? showArea.find('.selectDes:last').after($div) : showArea.prepend($div);
                    });
                } else {
                    for (var i = 0; i < showArea.find('.selectDes').length; i++) {
                        if (showArea.find('.selectDes').eq(i).data('index') == index) {
                            showArea.find('.selectDes').eq(i).remove();
                        }
                    }
                }
                $p.find('.selectAll').prop('checked', false);
            }
        }
        $(this).parents('.multiDownList').prev().prev().data('desList', desList.length ? desList : []).
        data('valList', valList.length ? valList : []).trigger('change');
    });
    //单选
    $(document).on('click', '.simpleList>p', function (e) {
        var $s = $(this).find('span');
        //e.target.type ==undefined && $i.prop('checked',!$i.prop('checked'));
        var $p = $(this).parents('.simpleList');
        var sources = $p.prev().prev();
        var type = sources.data('type');
        var showArea = sources.next().find('.msContent');
        var desList = sources.data('desList') || "";
        var valList = sources.data('valList') || "";
        if (!$(this).is('.simpleChecked')) {
            desList = $s.html();
            valList = $(this).data('value');
            $p.find('.simpleChecked').removeClass('simpleChecked');
            if (sources.data('args')) {
                for (var i = 0; i < sources.data('args').length; i++) {
                    sources.data(sources.data('args')[i], $(this).data(sources.data('args')[i]));
                }
            }
            sources.next().is('.isReg')? showArea.find('.multiInput').val($.trim($s.html())): showArea.html($.trim($s.html()));
        }else{
            valList=$(this).data('value');
            desList= $.trim($s.html());
            if (sources.data('args')) {
                for (var i = 0; i < sources.data('args').length; i++) {
                    sources.data(sources.data('args')[i], $(this).data(sources.data('args')[i]));
                }
            }
            sources.next().is('.isReg')? showArea.find('.multiInput').val(desList): showArea.html(desList);
        }
        if(sources.is('.isOne')){
            sources.val(valList);
        }
        if(type ==2) {
            sources.addClass('operateChecked');
        }
        sources.data('desList', desList ? desList : "").data('valList', valList ? valList : "")
            .data('data', $(this).data('data')).trigger('change');
        $p.prev().removeClass('nFocus').end().hide();
        //showArea.find('.multiInput').trigger('blur');
        if(!e.originalEvent){
            e.stopPropagation();
        }
    });
    //按住右滑删除事件
    $(document).on('mousedown', '.selectDes', function (e) {
        if (e.buttons == 1) {
            var $div = $('<div class="closeArea">');
            $div.html("X");
            $(this).append($div);
        }
    });
    $(document).on('mouseover', '.closeArea', function () {
        $(this).parents('.multiSelect').next().find('.item:not(.selectAllDiv)').eq($(this).parent().data('index')).click();
    });
    $(document).on('mouseup', '.selectDes', function () {
        $(this).find('.closeArea').remove();
    });
    $(document).on('focus', '.multiInput', function() {
        $(this).trigger('input');
    });
    $(document).on('blur', '.multiInput', function() {
        var $source = $(this).parents('.multiSelect').prev();
        if(event.relatedTarget && !$source.is('.connectedPlug')) {
            $('.simpleChecked').click();
        }
    });
    $(document).on('input', '.multiInput:not(".dynamicNa")', function (e) {
        var otherShow, regMark=false;
        var $source = $(this).parent().parent().prev();
        //trigger触发
        if(!e.originalEvent) {
            otherShow=true;
        }
        var mark=$(this).parents('.isReg');
        if(!mark.is('.nFocus')){
            _open.call(mark);
        }
        $('.simpleChecked').removeClass('simpleChecked');
        var val = $source.is('.lowerUpperMixed') ? $(this).val().toLowerCase() : $(this).val();
        if (!val) {
            $(this).parents('.isReg').next().find('p').show();
            $(this).parent().parent().prev().data("valList","").data("desList","").val("");
        }
        var regList = val.replace(/，/ig, ',').split(',');
        var target = ($('.nFocus').is('.simpleList') ? $(this).parents('.multiSelect').next().find('.des:not(:first-child)') : $(this).parents('.multiSelect').next().find('.des'));
        $.each(target, function () {
            var dataReg = $(this).data('reg');
            for (var i = 0; i < regList.length; i++) {
                if (!regList[i]) {
                    return true;
                }
                var reg = new RegExp(regList[i].replace(/[-\[\]{}()*+?.,\\^$|#\s]/g, "\\$&"));
                if (reg.test(dataReg)) {
                    regMark ? $(this).parent().show() : $(this).parent().addClass('simpleChecked').show();
                    regMark = true;
                } else {
                    !otherShow && $(this).parent().hide();
                }
            }
        });
        !regMark && target.parent(':visible:not(".selectAllDiv,.childChecked")').eq(0).addClass('simpleChecked');
    });
    $(document).on( 'keydown', function(e) {
        var eCode = e.keyCode;
        var $checked = $('.simpleChecked');
        if( eCode == 27 ) {
            return $('body').click();
        }
        if( eCode == 9) {
            var $t = $('.multiInput:not(".dynamicNa"):focus');
            var $source = $t.parents('.multiSelect').prev();
            if (!$t.length ) {
                $(document).trigger('click');
            } else {
                if($source.is('.connectedPlug')) {
                    var $nextPlug = $source.parent().next().find('.connectedPlug');
                    if($nextPlug.length) {
                        if(!$nextPlug.is(".connectedFinish")) {
                            $('.simpleChecked').click();
                            e.preventDefault();
                            return false;
                        }
                    } else {
                        $('.simpleChecked').click();

                    }

                }

            }
        }
        if( $checked.length==1 ) {
            if( eCode == 13) {
                $checked.trigger('click');
            } else if( eCode == 38 || eCode ==40 ) {
                if(!Interval) {
                    Interval = setInterval(function () {
                        upOrDown(eCode ==38? 1:0);
                    }, 200);
                }
            }
        } else {
            var $list = $('.nFocus').next();
            $list.find('.item:not(":hidden")').eq(0).addClass('simpleChecked');
        }
    });
    $(document).on( 'keyup', function(e) {
        var eCode = e.keyCode;
        var $checked = $('.simpleChecked');
        if( $checked.length ) {
            if(eCode == 38 || eCode ==40 ) {
                if(Interval) {
                    clearInterval(Interval);
                    upOrDown(eCode == 38? 1:0);
                }
            }
        }
    });
    function upOrDown(mark) {
        var $t = $('.simpleChecked'), next, $list = $t.parents('.simpleList,.multiDownList'), scrollAll = false;
        var $source = $list.prev().prev(),$nextPlug = $source.parent().next().find('.connectedPlug');
        $nextPlug.removeClass('connectedFinish');
        mark ? next = $t.prevAll('.item:not(":hidden")').eq(0) : next = $t.nextAll('.item:not(":hidden")').eq(0);
        if(!next.length) {
            scrollAll = true;
            mark ? next = $t.parent().find('.item').last() : next = $t.parent().find('.item').first();
        }
        if(next.length) {
            var listTop = $list[0].scrollTop, thisTop = next[0].offsetTop,ev;
            $t.removeClass('simpleChecked');
            next.addClass('simpleChecked');
            if(mark) {
                ev = ">";
            } else {
                ev = "<";
                //自身高度
                thisTop -= $list.height() - 20;
            }
            if(eval("listTop" + ev + "thisTop") || scrollAll ) {
                if(scrollAll) {
                    $list[0].scrollTop = ev == '>' ? $list[0].scrollHeight-220:0;
                } else {
                    $list[0].scrollTop = thisTop;
                }
            }
        }
    }
})(window, undefined);
