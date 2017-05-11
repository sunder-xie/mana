<#include "/mana/view/common/header.ftl">

<link href="/mana/css/coupon.css" rel="stylesheet" type="text/css"/>

<#-- 优惠券发送列表 -->
<div id="couponListDiv" class="">
    <div class="note note-success">
        <p>优惠券发送列表</p>
        <p>
            <em style="color: orangered;">
                如果出现发送失败的情况，千万不要通过其它渠道直接把【兑换码】发给车主，因为对系统来说发送失败的【兑换码】是可以再发的。
                <br>
                如果有发送失败的手机号，通过【发送优惠券】再给发一次即可。
            </em>
        </p>
    </div>
    <div class="search-condition">
        发送日期开始时间：<input value="" id="searchStartDate" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
        &nbsp;&nbsp;
        结束时间：<input value="" id="searchEndDate" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
        &nbsp;&nbsp;
        手机号：<input id="searchPhone" maxlength="11" class="search-phone" placeholder="手机号精确匹配">
        &nbsp;&nbsp;
        状态：
        <select id="searchStatus">
            <option value="-1">全部</option>
            <option value="1">发送成功</option>
            <option value="2">发送失败</option>
        </select>
    </div>
    <div class="row btn-div">
        <div class="col-md-6">
            <button id="refreshBtn" type="button" class="btn blue">查询</button>
        </div>
        <div class="col-md-6 text-right">
            <button id="sendCouponBtn" type="button" class="btn green-meadow">发送优惠券</button>
            <button id="exportExcelBtn" type="button" class="btn red">导出excel</button>
        </div>
    </div>

    <div class="coupon-list-content">

    </div>
</div>

<#-- 发送优惠券 -->
<div id="sendCouponDiv" class="hidden">
    <div class="note note-success">
        <p>发送优惠券</p>
    </div>
    <div class="send-coupon-div">
        <table class="send-coupon-table">
            <tbody>
            <tr>
                <th>短信模板</th>
                <td colspan="2">
                    <table class="sms-template-table table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>选择</th>
                            <th>模板内容</th>
                            <th>模板描述</th>
                        </tr>
                        </thead>
                        <tbody id="smsTemplateTable">
                        </tbody>
                    </table>

                </td>
            </tr>
            <tr>
                <th>
                    手机号
                </th>
                <td>
                    <textarea id="phoneText" maxlength="1200" class="phone-textarea" placeholder="请输入手机号"></textarea>
                </td>
                <td class="remark-style">
                    多个手机号，请以逗号隔开，最多支持100个
                    <br>
                    例如：15156781234,18723458778
                </td>
            </tr>
            <tr>
                <th>券类型</th>
                <td>
                    <select id="typeSelect">
                        <option value="-1">请选择</option>
                    </select>
                </td>
                <td></td>
            </tr>
            <tr>
                <th>数量</th>
                <td>
                    <input id="sendNum" maxlength="8" placeholder="请输入发送数量" value="1">
                </td>
                <td class="remark-style">
                    单个手机号发送的数量
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="btn-div confirm-send-div ">
        <button id="confirmSendBtn" type="button" class="btn green-meadow">确认发送</button>
        <button id="returnCouponListBtn" type="button" class="btn yellow">返回优惠券发送列表</button>
    </div>
</div>

<input type="hidden" id="SEND_COUPON_KEY" value="${SEND_COUPON_KEY}">

<#-- 模板 -->
<script type="text/html" id="typeTemplate">
    {{each data as type idx}}
    <option value="{{type.id}}">{{type.typeName}}</option>
    {{/each}}
</script>
<script type="text/html" id="smsTemplateTableTemplate">
    {{each data as temp idx}}
    <tr>
        <td class="text-center template-radio" >
            <input {{if idx==0}}checked{{/if}} type="radio" name="templateRadio" value="{{temp.templateKey}}">
        </td>
        <td class="sms-template-content">
            {{temp.templateContent}}
        </td>
        <td class="sms-template-desc">
            {{temp.templateDesc}}
        </td>
    </tr>
    {{/each}}
</script>
<script type="text/html" id="sendLogTableTemplate">
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th>序号</th>
                <th>发送手机号</th>
                <th>券类型</th>
                <th>数量</th>
                <th>发送内容</th>
                <th>发送日期</th>
                <th>状态</th>
                <th>短信模板</th>
            </tr>
            </thead>
            <tbody>
            {{each list as log idx}}
            <tr>
                <td style="width: 60px;">{{idx+1}}</td>
                <td>{{log.sendMobile}}</td>
                <td>{{log.couponTypeId | typeHelper}}</td>
                <td>{{log.couponNum}}</td>
                <td style="max-width: 200px;">{{log.sendContent}}</td>
                <td>{{log.gmtCreateStr}}</td>
                <td>{{#log.sendStatus | statusHelper}}</td>
                <td class="template-key" data-title="{{log.templateContent}}">{{log.templateKey}}</td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>

    <!--分页组件-->
    <div id="pagination" class="qxy_page"></div>

</script>

<script type="text/html" id="exportTemplate">
    <div style="margin-bottom: 15px;">
        发送日期开始时间：<input value="" id="startDateStr" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
    </div>
    <div>
        发送日期结束时间：<input value="" id="endDateStr" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
    </div>
</script>


<#include "/mana/view/common/footer.ftl">

<script src="/mana/js/coupon.js" type="text/javascript"></script>
