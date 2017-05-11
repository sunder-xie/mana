<#include "/mana/view/common/header.ftl">

<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>

<style type="text/css">

    .title-div{
        padding: 10px 0;
        margin-bottom: 10px;
    }

    .common-table input{
        width: 300px;
        height: 25px;
        background-color: white;
    }
    .btn-div{
        padding: 20px;
    }
    .btn-div button{
        margin-right: 10px;
    }

</style>

<div style="font-size: 16px">
    <div class="title-div">使用现金券</div>
    <div>
        <form id="form">
            <table class="common-table">
                <tbody>
                <tr>
                    <th>保险公司</th>
                    <td>
                        <select name="insuranceCompanyId">
                            <option value="">请选择</option>
                        <#list insuranceCompanyList as company >
                            <option value="${company.id}">${company.companyName}</option>
                        </#list>
                        </select>
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>投保城市编码</th>
                    <td>
                        <input name="cityCode">
                    </td>
                </tr>
                <tr>
                    <th>淘汽保单号</th>
                    <td>
                        <input name="insuranceOrderSn">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>现金券编号</th>
                    <td>
                        <input name="couponSn">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>现金券金额</th>
                    <td>
                        <input name="couponAmount">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>现金券兑现时间</th>
                    <td>
                        <input name="couponCashTime" class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>商业险返利计算公式key</th>
                    <td>
                        <select name="bizInsuranceFormulaKey">
                            <option value="">请选择</option>
                        <#list formulaKeyList as key >
                            <option value="${key}">${key}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>交强险返利计算公式key</th>
                    <td>
                        <select name="forceInsuranceFormulaKey">
                            <option value="">请选择</option>
                        <#list formulaKeyList as key >
                            <option value="${key}">${key}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="btn-div">
                <button id="submitBtn" type="button" class="btn blue">提交</button>
                <button id="" type="reset" class="btn yellow">重置</button>
            </div>
        </form>
    </div>
</div>


<#include "/mana/view/common/footer.ftl">


<script type="text/javascript">
    $(document).ready(function(){
        $('#submitBtn').click(function(){
            var form = MaNa.jqSerializeForm('form');
            if(form.insuranceOrderSn=='' || form.couponSn==''
                    || form.couponAmount=='' || form.couponCashTime==''
                    || form.insuranceCompanyId==''){
                LayerUtil.msg('请填写完整信息');
                return;
            }

            var index = LayerUtil.load(120000);

            Ajax.post({
                url: '/insuranceDataSync/useCashCoupon',
                data: form,
                success: function(result){
                    layer.close(index);
                    if(result.success){
                        LayerUtil.msg('用券成功');
                    }else{
                        LayerUtil.msg(result.message);
                    }
                }
            })
        });
    });
</script>
