<#--基础资料返点管理-->
<#include "/mana/view/common/header.ftl">
<#include "/mana/view/offline/auditTg.ftl">
<link href="/mana/css/offline.css" type="text/css" rel="stylesheet">
<div id="vue">
    <div class="all-box">
        <p class="p-title">保单管理 > 录入保单审核</p>
        <ul class="search-box">
            <li class="row-box">
                <input type="text" placeholder="门店名称" v-model="searchParam.agentName"/>
                <input type="text" placeholder="门店管理员手机号" v-model="searchParam.agentMobile"/>
                <input type="text" placeholder="车牌号" v-model="searchParam.vehicleSn"/>
                <input name="insureStartDate" id="txtStartDate" v-model="searchParam.gmtCreateStart" @blur="saveTime" @change="saveTime"
                       onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'txtEndDate\',{d:-1})}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" class="cxdate"
                       placeholder="录入时间"/>
                -<input name="insureEndDate" id="txtEndDate" v-model="searchParam.gmtCreateEnd" @blur="saveTime" @change="saveTime"
                        onfocus="WdatePicker({minDate:'#F{$dp.$D(\'txtStartDate\',{d:1})}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" class="cxdate"
                        placeholder="录入时间"/>
                <div class="select-condition">
                    <select v-model="searchParam.auditStatus" id="auditStatus">
                        <option value="-1">审核状态</option>
                        <option value="0">待审核</option>
                        <option value="1">审核通过</option>
                        <option value="2">审核驳回</option>
                    </select>
                </div>
                <button class="search-button" @click="queryList(1)">查询</button>
            </li>
        </ul>
    </div>

    <li class="row-box">
        <button class="export-button" @click="exportData">导出数据</button>
    </li>
    <div class="all-list-box">
        <div class="fenye" id="fenye"></div>
        <div class="rebate-list">
            <table class="mana-table">
                <thead>
                <td>门店信息</td>
                <td>车牌号</td>
                <td>保单号</td>
                <td>保费</td>
                <td>车船税</td>
                <td>录入时间</td>
                <td>审核状态</td>
                <td>操作</td>
                </thead>
                <tbody>
                <tr v-for="(item, index) in content" @click="selectedItem">
                    <td class="text-center">
                        <p>{{item.agentName}}</p>
                        <p>{{item.agentMobile}}</p>
                    </td>
                    <td>{{item.vehicleSn}}</td>
                    <td>
                        <table style="width: 100%;">
                            <tbody>
                            <tr v-if="item.commercialFormNo != null">
                                <td class="color-red">商</td>
                                <td>{{item.commercialFormNo}}</td>
                            </tr>
                            <tr v-if="item.forcibleFormNo != null">
                                <td class="color-red">交</td>
                                <td>{{item.forcibleFormNo}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                    <td>
                        <table style="width: 100%;">
                            <tbody>
                            <tr v-if="item.commercialFee==null">
                                <td class="color-red">商</td>
                                <td>未填</td>
                            </tr>
                            <tr v-if="item.commercialFee>0">
                                <td class="color-red">商</td>
                                <td>￥{{item.commercialFee}}</td>
                            </tr>
                            <tr v-if="item.forcibleFee == null">
                                <td class="color-red">交</td>
                                <td>未填</td>
                            </tr>
                            <tr v-if="item.forcibleFee > 0">
                                <td class="color-red">交</td>
                                <td>￥{{item.forcibleFee}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                    <td v-if="item.vesselTaxFee==null">未填</td>
                    <td v-if="item.vesselTaxFee>0">￥{{item.vesselTaxFee}}</td>
                    <td>{{item.gmtCreate | dateHelper}}</td>
                    <td v-if="item.auditStatus==0">待审核</td>
                    <td v-if="item.auditStatus==1">审核通过</td>
                    <td v-if="item.auditStatus==2">审核驳回</td>
                    <td v-if="item.auditStatus==0" class="text-center">
                        <input type="button" class="audit-status-tg-btn" @click="auditTg(item)" :id="item.id" value="审核通过"/>
                        <input type="button" class="audit-status-bh-btn" @click="auditBh(item)" value="审核驳回"/>
                    </td>
                    <td v-else></td>
                </tr>
                <tr v-show="noData">
                    <td colspan="8">暂无数据</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="fenye2" class="fenye" v-show="noData"></div>
    </div>

</div>

<script type="text/javascript" src="/mana/js/common/layerPage.js"></script>
<script type="text/javascript" src="/mana/js/offline/offLineList.js"></script>
<#include "/mana/view/common/footer.ftl">
