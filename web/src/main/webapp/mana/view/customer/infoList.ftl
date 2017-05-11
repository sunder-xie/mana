<#include "/mana/view/common/header.ftl">
<link href="/mana/css/customerList.css" rel="stylesheet" type="text/css"/>
<div class="owerBox">
    <div class="topbar"><a href="#">车主管理</a> > 车主列表</div>
    <div class="search-box"><input id="searchTxt" class="js-input" placeholder="输入车主姓名、手机号、车牌号" type="text" /><button id="searchBut">查询</button></div>
    <div class="card-box">
        <table id="filterBox" class="fbiao">
            <tr>
                <td width="120" align="right">车主来源</td>
                <td width="170"><select name="customerSource" class="laiyuan">
                    <option value="">请选择</option>
                    <option value="0">淘汽</option>
                    <option value="1">门店</option>
                </select></td>
                <td width="70">保险公司</td>
                <td width="210">
                    <select name="insureCompanyId" class="baoxian">
                        <option value="">请选择</option>
                        <#list allCompanyList as company>
                        <option value="${company.id}">${company.companyName}</option>
                        </#list>
                    </select>
                </td>
                <td width="70">保险模式</td>
                <td>
                    <select name="cooperationMode" class="baoxian">
                        <option value="">请选择</option>
                        <option value="1">买保险返奖励金</option>
                        <option value="2">买保险送服务</option>
                        <option value="3">买服务送保险</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="120" align="right">车险意向</td>
                <td width="170"><select name="insureIntention" class="laiyuan">
                    <option value="">请选择</option>
                    <option value="1">意向强</option>
                    <option value="2">考虑中</option>
                    <option value="3">无意向</option>
                    <option value="4">已投保</option>
                </select></td>
                <td width="70">车险状态</td>
                <td width="210">
                    <select name="insureStatus" class="baoxian">
                        <option value="">请选择</option>
                        <option value="0">未投保</option>
                        <option value="1">在保</option>
                        <option value="2">脱保</option>
                    </select>
                </td>
                <td width="70">车险到期</td>
                <td>
                    <input name="insureStartDate" id="txtStartDate" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'txtEndDate\',{d:-1})}'})" type="text" class="cxdate" />-<input name="insureEndDate" id="txtEndDate" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'txtStartDate\',{d:1})}'})" type="text" class="cxdate" />
                </td>
            </tr>
            <tr>
                <td width="120" align="right">服务门店名称</td>
                <td colspan="5"><input id="suggetion" name="agentName" placeholder="请填写" type="text" class="mdname" /></td>
                <#--<input id="recommendShopId" value="" name="recommendShopId" type="hidden" />-->
            </tr>
        </table>
        <div class="xiam">
            <button id="okFilter" class="blue">确定筛选</button><button id="resetFilter">重置条件</button>
        </div>
    </div>


    <div class="card-box">
        <h2>
            <span>已筛选业务信息列表</span>
            <div id="fenye" class="fenye"></div>
        </h2>
        <div class="miaos"><button id="tianjia">添加优惠纪录</button></div>
        <div id="wrapper"></div>
        <h2>
            <div id="fenye2" class="fenye"></div>
        </h2>
    </div>
</div>
<div class="mdsug">
</div>
<script type="text/html" id="quantpl">
    <div class="tk-q">
        <select class="quans">
            <option value="">请选择券</option>
            <%for (var i = 0; i < data.length; i++) {%>
            <option value="<%=data[i].id%>"><%=data[i].typeName%></option>
            <%}%>
        </select> X <input placeholder="请输入数量" type="number" class="qnum" /> <span>日期</span>
        <input name="" id="" onfocus="WdatePicker()" type="text" class="cxdate qdate" />
        <button class="faquan">确定发券</button>
    </div>
</script>
<script type="text/html">
    <div class="card-nav">
        <span>共 1765 条记录</span>
        <span>1/231 页</span>
                <span>
                <a href="#">首页</a>
                <a href="#"> < </a>
                <a href="#"> > </a>
                </span>
        <span>尾页</span>
        <span>每页</span>
        <span><input type="number" class="bum" /></span>
        <span>条</span>
        <span>跳转至</span>
        <span><input type="text" class="yeshu" /></span>
        <span>页</span>
    </div>
</script>
<script id="listTpl" type="text/html">
    <table class="clist">
        <tbody class="title">
        <tr>
            <th width="50"><input id="quanbu" type="checkbox" class="xzk" />序号</th>
            <th width="85">车主</th>
            <th width="50">车主来源</th>
            <th width="98">服务门店</th>
            <th width="50">车险意向</th>
            <th width="50">车险状态</th>
            <th width="60">保险公司</th>
            <th width="80">保险模式</th>
            <th width="72">车险到期日</th>
            <th width="100">优惠纪录</th>
            <th width="72">创建时间</th>
            <th width="62">最近登记人</th>
            <th>最近登记日期</th>
        </tr>
        </tbody>
        <tbody class="cont">
        <%for (var i = 0; i < list.length; i++) {%>
        <%var item = list[i];%>
        <tr>
            <td><input value="<%=item.customerVehicleId%>" type="checkbox" class="xzk" /><%=i + 1%></td>
            <td>
                <%if (item.customerName != '') {%>
                <a href="/customer/detailPage?vehicleId=<%=item.customerVehicleId%>" >
                    <%=item.customerName%>
                </a><br>
                <%}%>
                <%if (item.licencePlate != '') {%>
                <%=item.licencePlate%><br>
                <%}%>
                <%=item.customerMobile%></td>
            <td><%=item.customerSourceName%></td>
            <%var obj = item.simpleShopBO%>
            <td>
                <%if (obj) {%>
                    <%=obj.companyName%><br><%=obj.defaultAccountMobile%>
                <%}%>
            </td>
            <td>
                <%if (item.insureIntention == 2) {%>
                    <span class="lv"><%=item.insureIntentionName%></span>
                <%} else {%>
                    <%=item.insureIntentionName%>
                <%}%>
            </td>
            <td><%=item.insureStatusName%></td>
            <td><%=item.insureCompanyName%></td>
            <td><%=item.cooperationModeDescription%><br><%if (item.quitInsureStatus==1) {%><a href="/insurance/insureDetail?formId=<%=item.insuranceFormId%>&agentId=<%=item.agentId%>" target="_blank">保单详情</a><%}%></td>
            <td><%=shortDateHelper(item.insureEndDate)%></td>
            <td>
                <% var vo = item.preferentialLogBOList%>
                <%if (vo) {%>
                <%for (var j = 0; j < vo.length; j++) {%>
                    <p><%=shortDateHelper(vo[j].sendDate) + ' ' + vo[j].preferentialTypeName + 'x' + vo[j].preferentialNum%></p>
                <%}%>
                <%}%>
            </td>
            <td><%=item.gmtCreate && shortDateHelper(item.gmtCreate)%></td>
            <td><%=item.creator%></td>
            <td><%=item.regDate && shortDateHelper(item.regDate)%></td>
        </tr>
        <%}%>
        </tbody>
    </table>
</script>
<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script type="text/javascript" src="/mana/js/customer/infoList.js"></script>
<script>
    Mana.run();
</script>
<#include "/mana/view/common/footer.ftl">