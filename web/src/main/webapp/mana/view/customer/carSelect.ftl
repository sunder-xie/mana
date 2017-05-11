<style>
    .current{
        background: #287f87 !important;
        color: #fff !important;
    }
    #carSelect{
        width: 100%;
        background: #fff;
        display: none;
    }
    .carSelect-title{
        height: 60px;
        line-height: 60px;
        padding-left: 20px;
        font-size: 16px;
        color: #666;
        background: #ecf0f5;
        font-family: "Microsoft YaHei";
    }
    .carSelect-body{
        border: 1px solid #ddd;
        padding: 10px 0;
    }
    .carSelect-content{
        width: 822px;
        border: 1px solid #ddd;margin: 0 auto;
    }
    .select-result{
        width: 822px;
        height: 40px;
        line-height: 40px;
        display: -webkit-box;
        display: -moz-box;
        display: -ms-flexbox;
        display: -webkit-flex;
        display: flex;
        margin: 0 auto 10px;
        font-size: 14px;
    }
    .select-result .select-result-list{
        display: inline-block;
        -webkit-box-flex: 3;
        -moz-box-flex: 3;
        -webkit-flex: 3;
        -ms-flex: 3;
        flex: 3;
    }
    .select-result > div:nth-of-type(2){
        display: inline-block;
        -webkit-box-flex: 1;
        -moz-box-flex: 1;
        -webkit-flex: 1;
        -ms-flex: 1;
        flex: 1;
    }
    .select-result > div:nth-of-type(2){
        text-align: right;
    }
    .select-result > div >span{
        border: 1px solid #ddd;
        display: inline-block;
    }
    .select-result > div > span:nth-of-type(n+2){
        margin-left: 10px;
    }
    .select-result > div > span > b{
        display: inline-block;
        padding: 0 5px;
    }
    .select-result > div > span i{
        display: inline-block;
        width: 30px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        border-left: 1px solid #ddd;
        font-size: 18px;
        color: #8d8d8d;
        cursor: pointer;
    }
    #selectBtn{
        width: 54px;
        height: 38px;
        line-height: 38px;
        text-align: center;
        background: #287f87;
        color: #fff;
        cursor: pointer;
    }
    .carSelect-step{
        height: 50px;
        line-height: 50px;
        margin-bottom: 5px;
        display: -webkit-box;
        display: -moz-box;
        display: -ms-flexbox;
        display: -webkit-flex;
        display: flex;
    }
    .carSelect-step > span{
        display: inline-block;
        -webkit-box-flex: 1;
        -moz-box-flex: 1;
        -webkit-flex: 1;
        -ms-flex: 1;
        flex: 1;
        text-align: center;
        font-size: 16px;
        font-family: "Microsoft YaHei";
        color: #8c8c8c;
        border-bottom: 1px solid #ddd;
        background: #ecf0f5;
    }
    .carSelect-step > span:nth-of-type(n+2){
        border-left: 1px solid #ddd;
    }
    .carSelect-step > span > em{
        display: inline-block;
        width: 20px;
        height: 20px;
        background: #8c8c8c;
        line-height: 20px;
        color: #fff;
        border-radius: 3px;
        margin-right: 5px;
    }
    .carSelect-letter, .carBrandList, .carSeriesList, .carModelList{
        width: 750px;
    }
    .carSelect-letter{
        margin: 0 auto;
    }
    .carSelect-letter > span:nth-of-type(1){
        margin-left: 5px !important;
        color: #8c8c8c !important;
        font-family: "Microsoft YaHei";
    }
    .carSelect-letter > span:nth-of-type(n+2):hover{
        background: #287f87 !important;
        color: #fff !important;
    }
    .carSelect-letter .hot {
        display: inline-block;
        width: 40px;
        height: 22px;
        line-height: 22px;
        text-align: center;
        background: #d10000;
        color: #fff;
        cursor: pointer;
    }
    .carSelect-letter > span:nth-of-type(n+3){
        display: inline-block;
        width: 22px;
        height: 22px;
        line-height: 22px;
        text-align: center;
        background: #eee;
        color: #888;
        margin: 5px 0;
        cursor: pointer;
    }
    .carBrandList, .carSeriesList, .carModelList{
        margin: 10px auto;
    }
    .carSelect-content > ul > li:hover{
        border: 1px solid #24696f;
    }
    .carBrandList li, .carSeriesList li, .carModelList li{
        display: inline-block;
        border: 1px solid #ddd;
        padding: 6px 10px 6px 0;
        margin: 5px;
        cursor: pointer;
    }
    .carSeriesList li, .carModelList li{
        padding: 6px !important;
    }
    .carBrandList li img, .carBrandList li span{
        vertical-align: middle;
    }
    .carBrandList li img{
        height: 32px;
        border-right: 1px solid #ddd;
    }
    .carBrandList li span{
        margin-left: 15px;
    }
    .carBrandList li span, .carSeriesList li span, .carModelList li span
    {
        font-size: 14px;
        color: #666;
        font-family: "Microsoft YaHei";
    }
</style>
<div id="carSelect">
    <div class="carSelect-title">请选择适配车型</div>
    <div class="carSelect-body">
        <div class="select-result hidden">
            <div class="select-result-list"></div>
            <div>
                <span id="selectBtn">确定</span>
            </div>
        </div>
        <div class="carSelect-content">
            <div class="carSelect-step">
                <span><em>1</em>选择品牌</span>
                <span><em>2</em>选择车系</span>
                <span><em>3</em>选择车型</span>
            </div>
            <div class="carSelect-letter"></div>
            <ul class="carBrandList data-list"></ul>
            <ul class="carSeriesList data-list hidden"></ul>
            <ul class="carModelList data-list hidden"></ul>
        </div>
    </div>
</div>
<script id="carSelectLetter" type="text/html">
    <div class="carSelect-letter">
        <span>品牌字母选择:</span>
        {{each firstWordList as letter idx}}
        {{if idx == 0}}
        <span keyword="{{letter}}" class="letter current hot">{{letter}}</span>
        {{else}}
        <span keyword="{{letter}}" class="letter">{{letter}}</span>
        {{/if}}
        {{/each}}
    </div>
</script>
<script id="carBrandList" type="text/html">
    {{each carBrand as brand idx}}
    <li data-pid="{{brand.id}}"><img src="{{brand.logo}}"/><span>{{brand.name}}</span></li>
    {{/each}}
</script>
<script id="carSeriesList" type="text/html">
    {{each carSeriesList as series idx}}
    <li data-pid="{{series.id}}"><span>({{series.importInfo}}){{series.name}}</span></li>
    {{/each}}
</script>
<script id="carModelList" type="text/html">
    {{each carModelList as model idx}}
    <li data-pid="{{model.id}}"><span>{{model.name}}</span></li>
    {{/each}}
</script>
<script>
    function carSelectInit(option){
        var args = $.extend({},{
            dom: '',
            print : '',
            callback: null
        },option);
        if(args.print == '') args.print = args.dom;
        var $doc = $(document);
        /*所有车型品牌数据缓存*/
        var allCarBrandCache = null;
        /*车型品牌步骤tab，字母导航，步骤div显示的一些操作*/
        var initSelect = function(){
            var len = $(".select-result-list span").length;
            var step = $(".carSelect-step span");
            if(len > 0) {
                $(".select-result").removeClass("hidden");
                $(".carSelect-letter").addClass("hidden");
            }else{
                $(".select-result").addClass("hidden");
                $(".carSelect-letter").removeClass("hidden");
            }
            if(len >= step.length) return false;
            step.each(function(){
                if($(this).index() == len){
                    step.css({"background":"#ecf0f5","border-bottom":"1px solid #ddd","color":"#8c8c8c"});
                    step.find("em").css({"background":"#8c8c8c"});
                    $(this).css({"background":"#fff","border-bottom":"1px solid #fff","color":"#287f87"});
                    $(this).find("em").css("background","#287f87");
                    $(".carSelect-content ul").addClass("hidden").eq(len).removeClass("hidden");
                }
            });
        };
//        initSelect();

        /*添加品牌，车系，车型的操作*/
        var appendResult = function(obj){
            var step = $(".carSelect-step span");
            var len = $(".select-result-list span").length;
            if(len >= step.length) return false;
            var resultHtml = '';
            var resultContent = $(obj).find("span").text();
            resultHtml += '<span><b class="select-result-name">'+resultContent+'</b><i class="select-result-delete">x</i></span>';
            $(".select-result-list").append(resultHtml);
        };
        /*调用车型品牌接口*/
        var getBrand = function(){
            Ajax.get({
                url: '/common/carBrandSelect',
                async : false,
                success: function(result){
                    var carLetterHtml = template("carSelectLetter",result);
                    $(".carSelect-letter").html(carLetterHtml);
                    allCarBrandCache = result.dataMap;
                }
            });
        };
        /*点击字母选择车型*/
        var letterClick = function(obj){
            $(".letter").removeClass("current");
            $(obj?obj:'.hot').addClass("current");
            var keyword = $(obj?obj:'.current').attr('keyword');
            var carBrandHtml = template("carBrandList",{'carBrand':allCarBrandCache[keyword]});
            $(".carBrandList").html(carBrandHtml);
        };
        /*请求下一级*/
        var getNextLevel = function(obj,pid){
            Ajax.get({
                url: '/common/carListByPid',
                data : {pid : pid},
                success: function(result){
                    if($(obj).parent().hasClass("carBrandList")){
                        var carSeriesHtml = template("carSeriesList",{'carSeriesList':result});
                        $(".carSeriesList").html(carSeriesHtml);
                    }else if($(obj).parent().hasClass("carSeriesList")){
                        var carModelHtml = template("carModelList",{'carModelList':result});
                        $(".carModelList").html(carModelHtml);
                    }else{
                        return false;
                    }
                }
            });
        };
        $doc.on("click",".data-list li",function(){
            appendResult($(this));
            initSelect();
        }).on("click",".select-result-delete",function(){
            $(this).parent().remove();
            initSelect();
        }).on("click","#selectBtn",function(){
            var resultName = '',selectResult=$(".select-result-list span");
            selectResult.each(function(){
                resultName += $(this).find(".select-result-name").text()+" ";
            });
            resultName = MaNa.trimRight(resultName);
            if($(args.print).is("input") || $(args.print).is("textarea")){
                $(args.print).val(resultName);
            }else{
                $(args.print).text(resultName);
            }
            $(".select-result").addClass("hidden");
            selectResult.remove();
            layer.closeAll();
        }).on("click",".letter",function(){
            letterClick($(this));
        }).on("click",".data-list li",function(){
            var pid = $(this).data('pid');
            getNextLevel($(this),pid);
        }).off("click",args.dom).on("click",args.dom,function(){
            LayerUtil.open({
                title : false,
                type : 1,
                btn : false,
                content : $("#carSelect"),
                area : ['auto','350px'],
                success : function(){
                    getBrand();
                    initSelect();
                    letterClick();
                },
                cancel : function(index){
                    $(".select-result").addClass("hidden");
                    $(".select-result-list span").remove();
                    layer.close(index);
                }
            });
        });
    }
</script>