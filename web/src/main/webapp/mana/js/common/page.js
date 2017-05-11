/**
 * Created by huangzhangting on 16/7/28.
 */

var JqPage = {
    init: function(options){
        var args = $.extend({
            dom : $(".qxy_page"),
            itemSize : 0,
            pageSize : 10,
            current : 0,
            backFn : function(){}
        },options);
        args.pageCount = (typeof args.itemSize == "number" && typeof args.pageSize == "number") ? Math.ceil(args.itemSize/args.pageSize) : 0;
        args.width = args.dom.width();
        JqPage.toInit(args);
    },
    toInit:function(args){
        args.dom.addClass('qxy_page');
        $(".qxy_page_inner:eq(0)",args.dom).length || args.dom.append('<div class="qxy_page_inner">');
        JqPage.fillHtml(args);
        args.dom.append('</div>');
        JqPage.bindEvent(args);
    },
    //填充html
    fillHtml:function(args){
        var obj = $(".qxy_page_inner:eq(0)",args.dom);
        obj.empty();

        //上一页之前
        obj.append('<span class="disabled qxy_page_count">共'+args.itemSize+'条记录</span>');
        if(args.current < 10000){
            if(args.current > 1 && args.current <= args.pageCount){
                if(args.width > 900){
                    obj.append('<a class="qxy_page_first" href="javascript:;">首页</a>');
                }
                obj.append('<a href="javascript:;" class="qxy_page_prev">上一页</a>');

            }else{
                obj.remove('.prevPage');
                if(args.width > 900){
                    obj.append('<span class="disabled">首页</span>');
                }
                obj.append('<span class="disabled">上一页</span>');
            }
        }
        //中间页码
        if(args.current == 1){
            obj.append('<span class="yqx-pg-current">1</span>');
        }else{
            obj.append('<a href="javascript:;" class="qxy_page_num">1</a>');
        }
        if(args.current >= 4 && args.pageCount > 5){
            obj.append('<span>...</span>');
        }
        var start = args.current -1,end = args.current+1;

        if(args.current > args.pageCount - 3){
            start = args.pageCount - 3;
        }
        if(start < 1){
            start = 1;
        }
        if(args.current < 4 && args.pageCount >= 4){
            if(args.pageCount > 4){
                end = 4;
            }else if(args.pageCount == 4){
                end = 3;
            }else{
                end = args.pageCount - 1;
            }
        }
        if(args.pageCount == 1){
            end = 1;
        }

        for (;start <= end; start++) {
            // console.log(start +","+ end);
            if(start < args.pageCount && start > 1){
                if(start != args.current){
                    obj.append('<a href="javascript:;" class="qxy_page_num">'+ start +'</a>');
                }else{
                    obj.append('<span class="yqx-pg-current">'+ start +'</span>');
                }
            }
        }
        if(args.current + 1 < args.pageCount-1 && args.pageCount > 5){
            obj.append('<span>...</span>');
        }

        if(args.pageCount > 1){
            if(args.current == args.pageCount){
                obj.append('<span class="yqx-pg-current">'+args.pageCount+'</span>');
            }else{
                obj.append('<a href="javascript:;" class="qxy_page_num">'+args.pageCount+'</a>');
            }
        }

        //下一页之后
        if(args.current<10000){
            if(args.current < args.pageCount){
                obj.append('<a href="javascript:;" class="qxy_page_next">下一页</a>');
                if(args.width > 900){
                    obj.append('<a class="qxy_page_last" href="javascript:;">末页</a>');
                }
            }else{
                obj.remove('.nextPage');
                obj.append('<span class="disabled">下一页</span>');
                if(args.width > 900){
                    obj.append('<span class="disabled qxy_page_last">末页</span>');
                }
            }
        }
        if(args.pageCount <=1 ){
            obj.append('<span class="qxy_go_num disabled">共'+args.pageCount+'页,去第 <input type="text" disabled="disabled" value=""> 页</span>');
            obj.append('<span class="qxy_go_btn disabled" href="javascript:;">跳转</span>');
        }else{
            obj.append('<span class="qxy_go_num">共'+args.pageCount+'页,去第 <input type="text" value=""> 页</span>');
            obj.append('<a class="qxy_go_btn" href="javascript:;">跳转</a>');
        }
    },
    //绑定事件
    bindEvent:function(args){
        //页码事件绑定
        args.dom.off("click","a.qxy_page_num").on("click","a.qxy_page_num",function(){
            var current = parseInt($(this).text());
            JqPage.fillHtml({"current":current,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
            if(typeof(args.backFn)== "function"){
                args.backFn(current);
            }
        });
        //上一页
        args.dom.off("click","a.qxy_page_prev").on("click","a.qxy_page_prev",function(){
            var current = parseInt($("span.yqx-pg-current",args.dom).text());
            if(current == 1) return;
            JqPage.fillHtml({"current":current-1,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
            if(typeof(args.backFn)=="function"){
                args.backFn(current-1);
            }
        });
        //下一页
        args.dom.off("click","a.qxy_page_next").on("click","a.qxy_page_next",function(){
            var current = parseInt($("span.yqx-pg-current",args.dom).text());
            if(current == args.pageCount) return;
            JqPage.fillHtml({"current":current+1,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
            if(typeof(args.backFn)=="function"){
                args.backFn(current+1);
            }
        });
        //首页事件
        args.dom.off("click","a.qxy_page_first").on("click","a.qxy_page_first",function(){
            JqPage.fillHtml({"current":1,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
            if(typeof(args.backFn)=="function"){
                args.backFn(1);
            }
        });
        //末页事件
        args.dom.off("click","a.qxy_page_last").on("click","a.qxy_page_last",function(){
            JqPage.fillHtml({"current":args.pageCount,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
            if(typeof(args.backFn)=="function"){
                args.backFn(args.pageCount);
            }
        });
        //跳转事件
        args.dom.off("click","a.qxy_go_btn").on("click","a.qxy_go_btn",function(){
            var current = parseInt(args.dom.find("span.yqx-pg-current").text());
            var goNumInput = $(this).prev().find("input");
            var goNum = parseInt(goNumInput.val());
            if(goNum > args.pageCount || !$.isNumeric(goNum) || goNum == current){
                goNumInput.val("").focus();
                return;
            }
            JqPage.fillHtml({"current":args.pageCount,"pageCount":args.pageCount,dom:args.dom,itemSize:args.itemSize});
            if(typeof(args.backFn)=="function"){
                args.backFn(goNum || current);
            }
        });
    }
};
