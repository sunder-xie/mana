<style>
    div {
        /*width: 100%;*/
        /*height: 100%;*/
        /*text-align: center;*/
    }
    textarea{
        vertical-align:super;
        margin: 15px;
        width: 280px;
        height: 180px;
    }
    .audit-table {
        margin: auto;
    }

    .audit-table input{
        /*padding-left: 6px;*/
        /*padding-right: 6px;*/
        width: 250px;
        /*background: #f4f4f4;*/
        /*vertical-align: middle;*/
        /*font-size: 14px;*/
        /*border-radius: 2px;*/
    }

    .must-mark:before {
        content: "*";
        color: red;
        font-size: 16px;
    }

    .audit-table tbody > tr, .audit-table tbody > tr tbody tr {
        background: white !important;
    }

    .audit-table tbody tr td {
        padding: 3px !important;
    }

    .select-width {
        width: 263px;
    }
    .data-width{
        width: 227px!important;
    }
    .region-width{
        width: 130px;
    }
    #vueAudit{
        text-align: center;
    }
</style>

<script id="auditTg" type="text/html">
    <div id="vueAudit">
        <table class="audit-table">
            <tbody>
            <tr>
                <td class="must-mark">保险公司:</td>
                <td>
                    <select id="insureCompanyId" v-model="item.insuranceCompanyId" class="select-width"></select>
                </td>
            </tr>
            <tr>
                <td class="must-mark">车牌号:</td>
                <td>
                    <input type="text" maxlength="15" v-model="item.vehicleSn"/>
                </td>
            </tr>
            <tr>
                <td class="must-mark">投保地:</td>
                <td>
                    <select name="" class="region-width" v-model:value="item.insuredProvinceCode" id="province"></select>
                    <select name="" class="region-width" v-model:value="item.insuredCityCode" id="city"></select>
                </td>
            </tr>
            <tr>
                <td class="must-mark">签单日期:</td>
                <td>
                    <input name="insureStartDate" id="txtStartDate" v-model="item.gmtPay" @blur="saveTime" @change="saveTime"
                           onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'txtEndDate\',{d:-1})}',dateFmt:'yyyy-MM-dd HH:ss:mm'})" type="text"
                           class="cxdate cxdate-ext data-width"
                           placeholder="请选择时间"/>
                </td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td>商业险保费:</td>
                <td>
                    <input type="text" maxlength="10" v-model="item.commercialFee" placeholder="请输入金额"/>
                </td>
            </tr>
            <tr>
                <td>商业险保单号:</td>
                <td>
                    <input type="text" maxlength="30" v-model="item.commercialFormNo" placeholder="请输入单号"/>
                </td>
            </tr>
            <tr>
                <td>商业险起保日期:</td>
                <td>
                    <input name="insureStartDate" id="txtStartDate" v-model="item.gmtCommercialStart" @blur="saveTime" @change="saveTime"
                           onfocus="WdatePicker({startDate:'%y-%M-%d 00:00:00',maxDate:'#F{$dp.$D(\'txtEndDate\',{d:-1})}',dateFmt:'yyyy-MM-dd HH:ss:mm'})" type="text"
                           class="cxdate cxdate-ext data-width"
                           placeholder="请选择起保时间"/>
                </td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td>交强险保费:</td>
                <td>
                    <input type="text" maxlength="10" v-model="item.forcibleFee" placeholder="请输入金额"/>
                </td>
            </tr>
            <tr>
                <td>车船税:</td>
                <td>
                    <input type="text" maxlength="10" v-model="item.vesselTaxFee" placeholder="请输入金额"/>
                </td>
            </tr>
            <tr>
                <td>交强险保单号:</td>
                <td>
                    <input type="text" maxlength="30" v-model="item.forcibleFormNo" placeholder="请输入单号"/>
                </td>
            </tr>
            <tr>
                <td>交强险起保日期:</td>
                <td>
                    <input name="insureStartDate" id="txtStartDate" v-model="item.gmtForcibleStart" @blur="saveTime" @change="saveTime"
                           onfocus="WdatePicker({startDate:'%y-%M-%d 00:00:00',maxDate:'#F{$dp.$D(\'txtEndDate\',{d:-1})}',dateFmt:'yyyy-MM-dd HH:ss:mm'})" type="text"
                           class="cxdate cxdate-ext data-width"
                           placeholder="请选择起保时间"/>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="btn-foot">
            <input type="button" class="export-button" @click="cancel" value="取消"/>
            <input type="button" class="search-button" @click="submit" value="提交"/>
        </div>
    </div>
</script>
<script id="auditBh" type="text/html">
    <div id="vueAudit">
        <textarea v-model="rejectReason" cols="3" rows="5" maxlength="100"></textarea>
        <div class="js-auditBh">
            <input type="button" class="export-button" @click="cancel2" value="取消"/>
            <input type="button" class="search-button" @click="submit2" value="提交"/>
        </div>
    </div>
</script>