<#include "/mana/view/common/header.ftl">

<link href="/mana/css/sysManage.css" rel="stylesheet" type="text/css"/>

<style type="text/css">
    .vehicle-sn{
        height: 28px;
        width: 200px;
        padding: 0 4px;
        font-size: 14px;
    }
    .title-div{
        padding: 10px 0;
        margin-bottom: 10px;
    }
    .has-sync-vehicle-sn{
        padding: 10px 0;
        margin-top: 10px;
    }
</style>

<div style="font-size: 16px">
    <div class="title-div">结算数据同步</div>
    <div>
        请输入车牌号：
        <input id="vehicleSn" maxlength="10" class="vehicle-sn">
        <button id="syncSettleDataBtn" type="button" class="btn blue">开始同步数据</button>
        <div class="has-sync-vehicle-sn">
            已同步车牌号：
        </div>
    </div>
</div>


<#include "/mana/view/common/footer.ftl">

<script type="text/html" id="vehicleSnTemplate">
    <em style="color: orangered;margin-right: 15px;">{{data}}</em>
</script>

<script type="text/javascript">
    $(document).ready(function(){
        $('#syncSettleDataBtn').click(function(){
            var index = LayerUtil.load(120000);
            var vehicleSn = $('#vehicleSn').val();
            Ajax.post({
                url: '/insuranceDataSync/syncSettleDataByVehicleSn',
                data: {vehicleSn: vehicleSn},
                success: function(result){
                    layer.close(index);
                    LayerUtil.msg(result.message);
                    if(result.success){
                        $('#vehicleSn').val('');
                        $('.has-sync-vehicle-sn').append(template('vehicleSnTemplate', {data: vehicleSn}));
                    }
                }
            })
        });
    });
</script>
