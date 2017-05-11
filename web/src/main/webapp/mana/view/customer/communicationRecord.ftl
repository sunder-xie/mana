<style>
    input,select,textarea{
        padding-left: 6px;
        padding-right: 6px;
        height: 32px;
        width: 150px;
        background: #f4f4f4;
        vertical-align: middle;
        font-size: 14px;
    }
    #communication-record{
        width: 1051px;
        background: #fff;
        display: none;
        border-top: 1px solid #ddd;
        border-bottom: 1px solid #ddd;
        margin-bottom: 15px;
    }
    .communication-record-title{
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        font-weight: bold;
        padding-left: 20px;
        border-bottom: 1px solid #ddd;
        font-family: "Microsoft YaHei";
    }
    .communication-record-detail{
        padding-top: 20px;
    }
    .communication-record-detail > li.recordList, .communication-record-detail > li.mapList{
        margin-bottom: 20px;
        padding-left: 20px;
    }
    .communication-record-detail > li.recordList > div{
        display: inline-block;
    }
    .communication-record-detail > li.recordList span, .communication-record-detail > li.mapList > span{
        display: inline-block;
        width: 75px;
        font-size: 14px;
        vertical-align: middle;
        font-weight: bold;
        font-family: "Microsoft YaHei";
        text-align: right;
    }
    #communicationDate{
        height: 32px;
        background-color: #f4f4f4;
        border: 1px solid #ddd;
        width: 160px;
    }
    .communication-content {
        width: 890px !important;
        height: 180px !important;
        padding-top: 6px;
        padding-bottom: 6px;
    }
    .detailAddress {
        width: 430px !important;
    }
    .recommendShopInfo{
        width: 700px !important;
        height: 40px;
        vertical-align: middle;
        border: 1px solid #ddd;
        font-size: 14px;
    }
    #mapContent{
        display: inline-block;
        width: 905px;
        vertical-align: top;
    }
    #map{
        width: 649px;
        height: 420px;
        display: inline-block;
    }
    #result{
        width: 250px;
        height: 420px;
        display: inline-block;
        border: 1px solid #ddd;
        overflow-y: auto;
    }
    #result > li{
        padding: 10px 13px;
        line-height: 22px;
        cursor: pointer;
        border-bottom: 1px solid #ddd;
    }
    #result > li:hover{
        background: #e3eced;
        border: 1px solid #24696f !important;
    }
    .result-page{
        text-align: center;
        padding: 10px 0;
    }
    .result-page .result-page-list{
        display: inline-block;
        width: 20px;
        height: 20px;
        line-height: 20px;
        border: 1px solid #ddd;
        text-align: center;
        margin: 0 5px;
        color: #00c;
        cursor: pointer;
        -moz-user-select:none;
        -webkit-user-select:none;
        user-select:none;
    }
    .position{
        background: #e3eced;
        border: 1px solid #24696f !important;
    }
    #result .agentName{
        color: #24696f;
        font-size: 14px;
    }
    #mapContent #result div{
        margin: 0;
        font-weight: bold;
        font-family: "Microsoft YaHei";
    }

    .register {
        padding-left: 102px !important;
    }
    #addBtn {
        display: inline-block;
        width: 100px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        background: #287f87;
        color: #fff;
        cursor: pointer;
        border-radius: 3px;
    }
    .register span:nth-of-type(2) {
        width: 100px !important;
    }
    .register span:nth-of-type(3) {
        width: 220px !important;
    }

    .searchShopInfoBtn{
        background: #287f87;
        color: #FFFFFF;
        border: 1px solid #24696f;
        padding: 10px 10px;
        font-size: 15px;
        border-radius: 3px;
        margin-left: 15px;
    }

    .shop-info{

    }
    .shop-info div{
        margin-top: 10px;
    }
    .shopRemarkDiv{
        margin-top: 15px !important;
    }
    #result .shopRemarkDiv{
        margin-top: 6px !important;
    }

    .wrongPosition{
        color: red;
    }
    .hasInsuranceRight{
        color: #fff;
        margin-right: 10px;
        /* border-color: green; */
        background-color: green;
        padding: 3px 4px 3px 4px;
        border-radius: 3px;
        /* font-size: 10px; */
        font-weight: normal;

    }
    .clearRecommendShopBtn{
        color: #fff;
        background-color: #72b8f2;
        padding: 4px;
        border-radius: 3px;
        font-size: 10px;
        font-weight: normal;
        cursor: pointer;
    }

</style>
<div id="communication-record">
    <p class="communication-record-title"><span>沟通记录</span></p>
    <ul class="communication-record-detail">
        <li class="recordList">
            <div>
                <span>沟通渠道</span>
                <select name="communicateChannel" id="communication-way">
                    <option value="0">车主致电</option>
                    <option value="2">拜访</option>
                </select>
            </div>
            <div>
                <span>日期</span>
                <input name="communicateDate" value="${.now?string("yyyy-MM-dd")}" id="communicationDate" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
            </div>
        </li>
        <li class="recordList">
            <span>沟通内容</span>
            <textarea name="communicateContent" maxlength="500" id="communicateContent" class="communication-content"></textarea>
        </li>
        <li class="recordList">
            <span>车主地址</span>
            <select name="customerProvince" id="carOwner-province">
                <option value="">请选择省</option>
            </select>
            <select name="customerCity" id="carOwner-city">
                <option value="">请选择市</option>
            </select>
            <select name="customerDistrict" id="carOwner-area">
                <option value="">请选择区</option>
            </select>
            <input name="customerAddress" id="customerAddress" type="text" class="detailAddress" placeholder="请输入详细地址"/>
        </li>
        <li class="mapList">
            <span>
                推荐门店
                <br>
                <label style="font-size:10px;color:green;">( 目前根据<br>车主地址<br>市、区(县)<br>查询门店 )</label>
                <br><br>
                <span class="clearRecommendShopBtn">暂不推荐</span>
            </span>
            <#--<input name="recommendShopInfo" class="recommendShopInfo" id="recommendShopInfo"/>-->
            <#--<button type="button" class="searchShopInfoBtn">根据车主地址查询门店</button>-->
            <div id="mapContent">
                <ul id="result"></ul>
                <div id="map"></div>
            </div>
        </li>
        <li class="register mapList">
            <span id="addBtn">确定</span>
            &nbsp;&nbsp;
            <span>登记人：${CURRENT_USER_KEY.realName}</span>
            &nbsp;&nbsp;
            <span>登记时间：${.now?string("yyyy-MM-dd HH:mm:ss")}</span>
        </li>
    </ul>
</div>

<#-- 模板 -->
<script type="text/html" id="shopListTemplate">
    <li id="" class="shop-info" data-id="{{id}}" data-msg="">
        <div class="agentName">门店名称：<span>{{companyName}}</span></div>
        <div class="agentAddress">门店地址：<span>{{address}}</span></div>
        <div class="agentMobile">联系电话：<span>{{mobile}}</span></div>
        <div class="shopRemarkDiv">
            {{if hasInsuranceRight==1}}<span class="hasInsuranceRight">已开通保险资格</span>{{/if}}
            {{if wrongPosition==1}}<span class="wrongPosition">门店坐标错误</span>{{/if}}
        </div>
    </li>
</script>
<script type="text/html" id="communicateChannelTemplate">
    <option value="0">车主致电</option>
    <option value="1">电话回访</option>
    <option value="2">拜访</option>
</script>


<script>
    var map = new BMap.Map("map");// 创建Map实例
    map.centerAndZoom("杭州", 12);//创建中心点
    map.setCurrentCity("杭州");
    map.enableScrollWheelZoom(); //启用滚轮放大缩小
    //添加地图类型控件，只要地图、卫星(混合)两种
    map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP,BMAP_HYBRID_MAP]}));
    var navigation = new BMap.NavigationControl({
        anchor: BMAP_ANCHOR_TOP_RIGHT,
        //type: BMAP_NAVIGATION_CONTROL_SMALL,
        offset: new BMap.Size(10, 100)
    });
    map.addControl(navigation);

    // 创建信息窗口对象（宽高自适应内容即可）
    var infoWindow = new BMap.InfoWindow('');

    /*保存经纬度*/
    var arr = [];
    /*保存门店列表*/
    var agentResult = [];
    /*保存标注Id属性*/
    var markerId = '';
    var changePlace = '';

    /* 门店查询参数封装对象 */
    var searchShopParam;


    $(function(){

        initProvinceSelect();
        initCitySelect();

        $('.searchShopInfoBtn').unbind().click(function(){
            searchShpInfoByAddress();
        });

        $('.clearRecommendShopBtn').unbind().click(function(){
            $("#result li").removeClass("position");
            map.closeInfoWindow();
        });
    });


    /* 初始化省份 */
    function initProvinceSelect(){
        Ajax.get({
            url: '/common/regionListByPid',
            success: function(result){
                var html = template('regionTemp', {data: result, msg: '省'});
                $('#insure-province').html(html).change(function(){
                    provinceChange(this.value, '#insure-city');
                });
                $('#carOwner-province').html(html).change(function(){
                    provinceChange(this.value, '#carOwner-city', '#carOwner-area');
                });
            }
        });
    }

    /* 清除下拉框 */
    function clearSelect(el){
        if(el===undefined){
            return;
        }
        var $el = $(el);
        $el.val('');
        $el.get(0).options.length = 1;
    }
    /* 初始化城市 */
    function provinceChange(pid, el, clearEl){
        clearSelect(el);
        clearSelect(clearEl);

        initRegionSelect(pid, el, '市', clearEl);
    }

    /* 初始化地区选择框 */
    function initRegionSelect(pid, el, msg, clearEl, id){
        if(pid==''){
            return;
        }
        Ajax.get({
            url: '/common/regionListByPid',
            data: {pid: pid},
            success: function(result){
                var html = template('regionTemp', {data: result, msg: msg});
                $(el).html(html);

                if(id!==undefined && id>0){
                    $(el).val(id);
                }
            }
        });

    }

    /* 初始化城市选择框 */
    function initCitySelect(){
        $('#carOwner-city').change(function(){
            clearSelect('#carOwner-area');

            initRegionSelect(this.value, '#carOwner-area', '区');
            getShopInfo(this);
        });
    }

    /* 根据区域返回门店 */
    $("#carOwner-area").change(function(){
        getShopInfo(this);
    });

    /* 获取门店信息（初始化地址时用到） */
    var toGetShopInfo = function(reqParam, changePlace){
        /* 清空数据 */
        $("#result").empty();
        map.centerAndZoom(changePlace, 12);//创建中心点

        getAgentList(reqParam);
    };

    /* 获取门店信息 */
    var getShopInfo = function(obj){
        var $obj = $(obj);
        var val = $obj.val();
        if(val == '') return false;
        $("#result").data('first','').data('last','');
        var data = {};
        changePlace = '';
        /*每次获取门店信息都清空之前保存的经纬度*/
        arr = [];
        if($obj.attr("id") == 'carOwner-city'){
            data = {cityId : val};
            changePlace = $("#carOwner-city").find("option:selected").text();
            if(changePlace == '请选择市'){
                return false;
            }
        }else if($obj.attr("id") == 'carOwner-area'){
            data = {districtId : val};
            changePlace = $("#carOwner-area").find("option:selected").text();
            if(changePlace == '请选择区'){
                return false;
            }
        }

        /* 清空数据 */
        $("#result").empty();
        map.centerAndZoom(changePlace, 12);//创建中心点

        getAgentList(data);
    };
    function getAgentList(params){
        removeOverlays();

        var index = LayerUtil.load();
        searchShopParam = $.extend({},{
            pageNumber : 1,
            pageSize : 10
        },params);

        Ajax.get({
            url : '/ucShop/getShopListInfo',
            data : searchShopParam,
            success : function(result){
                if(result.success){
                    mapMarker(result.list);
                    getPages({
                        total : result.total,
                        pageSize : searchShopParam.pageSize,
                        pageNumber : searchShopParam.pageNumber
                    });
                }
                layer.close(index);
            }
        });
    }
    /*地图标注*/
    function mapMarker(shopList){
        agentResult = [];

        var len = shopList.length;
        for(var i=0; i<len; i++){
            var obj = shopList[i];
            var content = template('shopListTemplate', obj);
            /*将门店列表方法数组中*/
            agentResult.push(content);
            if(obj.wrongPosition===1){ //坐标有问题就不描点
                continue;
            }

            var marker = new BMap.Marker(new BMap.Point(obj.longitude, obj.latitude));
            /*添加自定义属性到marker对象上*/
            marker.shopId = obj.id;
            marker.shopInfo = content;

            /*增加点*/
            map.addOverlay(marker);
            /*添加自定义属性到marker对象的V属性上，V属性是标注图标元素*/
            $(marker.V).attr('class','Z-marker').attr('data-id', obj.id);

            marker.addEventListener("click", function(){ //给标注添加点击事件
                //console.log(this);
                infoWindow.setContent(this.shopInfo);
                this.openInfoWindow(infoWindow);
                checkPosition(this.shopId);
            });
        }

        agentList(agentResult);
    }

    /* 产生门店列表 */
    function agentList(agentResult){
        var ul = $("#result").empty();
        for(var i=0;i<agentResult.length;i++){
            ul.append(agentResult[i]);
        }
    }
    /*点击标注选中列表*/
    function checkPosition(markerId){
        $("#result li").removeClass("position").each(function(){
            if($(this).data('id') == markerId){
                $(this).addClass("position");
            }
        });
    }
    /*产生分页列表*/
    function getPages(params){
        var option = $.extend({},{
            firstPage : 1,
            lastPage : 4,
            totals : params.total,
            pageSize : params.pageSize,
            pageNumber : 1
        },params);
        var resultPage = $("#result");
        /*如果不存在上一次保存的起始页，就拿当前的起始页和末尾页 默认显示第一页开始，第四页结束；否则就取保存的起始页和末尾页*/
        if(!resultPage.data('first')){
            var firstPage = option.firstPage;
            var lastPage = option.lastPage;
        }else{
            var firstPage = resultPage.data('first');
            var lastPage = resultPage.data('last');
        }
        /*当前页*/
        var curr = option.pageNumber;
        /*总页数*/
        var pageSize = Math.ceil(option.total/option.pageSize);
        /*如果末尾页大于总页数，就把总页数赋值给末尾页*/
        if(lastPage >= pageSize){
            lastPage = pageSize;
        }else{
            /*否则判断当前也是否小于总页数*/
            if(curr < pageSize){
                /*如果当前也是否大于等于末尾页，起始页和末尾页都＋1*/
                if(curr >= lastPage){
                    firstPage ++;
                    lastPage ++;
                }else if(firstPage > 1 && curr == firstPage){
                    /*如果起始页大于1并且当前也等于起始页，起始页和末尾页都－1*/
                    firstPage --;
                    lastPage --;
                }
            }
        }
        /*把当前的起始页和末尾页都存起来*/
        resultPage.data('first',firstPage).data('last',lastPage);
        var pagesHtml = '<div class="result-page">';
        /*如果当前页大于1，就出现上一页*/
        if(curr > 1) pagesHtml += '<span class="result-page-list result-page-prev" data-pages='+(curr-1)+'><</span>';
        for(var i=firstPage;i<=lastPage;i++){
            pagesHtml += '<span class="result-page-list" data-pages='+i+'>'+i+'</span>';
        }
        /*如果当前页小于总页数，就出现下一页*/
        if(curr < pageSize) pagesHtml += '<span class="result-page-list result-page-next" data-pages='+(curr+1)+'>></span>';
        pagesHtml += '</div>';
        resultPage.append(pagesHtml);
        /*如果页码等于当前页，就被特殊样式处理*/
        $(".result-page-list").each(function(){
            if($(this).data('pages') == curr){
                $(this).css({"border":"none","color":"#333","cursor":"text"});
                /*这个class是用来防止重复点击已选中的页码*/
                $(this).addClass("curSelected");
            }
        });
    }
    function selectShop(el){
        $("#result li").removeClass("position");
        $(el).addClass("position");
        map.closeInfoWindow();
    }

    $(document).on("click","#result li",function(){
        selectShop(this);
        var _this = $(this);
        $(".Z-marker").each(function(){
            if(_this.data('id') == $(this).data('id')){
                $(this).click();
            }
        });
    }).on("click",".result-page-list",function(){
        if(searchShopParam===undefined){
            return;
        }
        var _obj = $(this);
        /*当前页不能重复点击*/
        if(_obj.hasClass("curSelected")) return;
        searchShopParam.pageNumber = parseInt(_obj.data('pages'));
        getAgentList(searchShopParam);
    });

    /* 清除地图上面的元素 */
    function removeOverlays(){
        var overlays = map.getOverlays();
        for(var i=0; i<overlays.length; i++){
            if(overlays[i] == '[object Marker]'){// Marker对象
                map.removeOverlay(overlays[i]);
            }

        }
        map.closeInfoWindow();
    }


    /* 根据车主地址查询门店 */
    function searchShpInfoByAddress(){
        var district = $("#carOwner-area");
        if(district.val() != ''){
            getShopInfo(district);
            return false;
        }

        var city = $("#carOwner-city");
        if(city.val() != ''){
            getShopInfo(city);
            return false;
        }

        LayerUtil.msg('请选择 “市” 或 “区” 查询门店信息');
    }


    /* 设置推荐门店信息 */
    function setRecommendShopInfo(data){
        var recommendShop = $('#mapContent .position');
        if(recommendShop.length==0){
            return;
        }
        recommendShop = recommendShop.eq(0);
        var agentName = recommendShop.find('.agentName').find('span').text();
        var agentAddress = recommendShop.find('.agentAddress').find('span').text();
        var agentMobile = recommendShop.find('.agentMobile').find('span').text();
        var recommendShopInfo = agentName+' '+agentAddress+' '+agentMobile;
        data.recommendShopInfo = recommendShopInfo;
        data.recommendShopId = recommendShop.data('id');
    }

    /* 重置输入内容 */
    function resetCommunicateRecord(){
        $('#communicateContent').val('');

    }

</script>