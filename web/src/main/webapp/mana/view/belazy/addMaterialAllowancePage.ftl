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
    <div class="title-div">添加机滤补贴</div>
    <div>
        <form id="form">
            <table class="common-table">
                <tbody>
                <tr>
                    <th>淘汽保单号</th>
                    <td>
                        <input name="insuranceOrderSn">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>物料订单号</th>
                    <td>
                        <input name="materialOrderSn">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>应补贴金额</th>
                    <td>
                        <input name="payableAmount">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>补贴生效时间</th>
                    <td>
                        <input name="allowanceEffectTime" class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>物料数量</th>
                    <td>
                        <input name="materialNum">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>订单仓库id</th>
                    <td>
                        <input name="whsId">
                        <span class="remark-span">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>订单仓库名称</th>
                    <td>
                        <input name="whsName">
                        <span class="remark-span">* 必填</span>
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
            if(form.insuranceOrderSn==''
                    || form.materialOrderSn=='' || form.payableAmount==''
                    || form.allowanceEffectTime=='' || form.materialNum==''
                    || form.whsId=='' || form.whsName==''){
                LayerUtil.msg('请填写完整信息');
                return;
            }

            var index = LayerUtil.load(120000);

            Ajax.post({
                url: '/insuranceDataSync/addMaterialAllowance',
                data: form,
                success: function(result){
                    layer.close(index);
                    if(result.success){
                        LayerUtil.msg('添加机滤补贴成功');
                    }else{
                        LayerUtil.msg(result.message);
                    }
                }
            })
        });
    });
</script>
