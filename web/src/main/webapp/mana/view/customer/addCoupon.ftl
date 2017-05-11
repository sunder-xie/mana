<style>
    input,select{
        background: #f4f4f4;
    }
    #addCoupon{
        width: 850px;
        background: #fff;
        display: none;
    }
    .addCoupon-title{
        height: 60px;
        line-height: 60px;
        padding-left: 20px;
        font-size: 16px;
        color: #666;
        background: #ecf0f5;
        font-family: "Microsoft YaHei";
    }
    .addCoupon-body{
        border: 1px solid #ddd;
        padding: 20px;
        display: -webkit-box;
        display: -moz-box;
        display: -ms-flexbox;
        display: -webkit-flex;
        display: flex;
    }
    .addCoupon-body li:nth-of-type(2){
        -webkit-box-flex: 2;
        -moz-box-flex: 2;
        -webkit-flex: 2;
        -ms-flex: 2;
        flex: 2;
    }
    .select-coupon{
        -webkit-box-flex: 3;
        -moz-box-flex: 3;
        -webkit-flex: 3;
        -ms-flex: 3;
        flex: 3;
    }
    #couponList{
        width: 180px;
        height: 32px;
    }
    .multi{
        margin:0 5px;
    }
    #couponNum{
        width: 97px;
        height: 32px;
        text-align: center;
    }
    #couponSendDate{
        margin-left: 5px;
    }
    .couponSendDate > span{
        font-size: 14px;
        vertical-align: middle;
    }
    #sendCouponBtn{
        display: inline-block;
        width: 100px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        background: #287f87;
        color: #fff;
        cursor: pointer;
        font-family: "Microsoft YaHei";
    }
</style>
<div id="addCoupon">
    <div class="addCoupon-title">添加优惠信息</div>
    <ul class="addCoupon-body">
        <li class="select-coupon">
            <select name="coupon-list" id="couponList">
                <option value="">请选择券</option>
            </select>
            <span class="multi">x</span>
            <input type="text" id="couponNum" placeholder="请输入数量"/>
        </li>
        <li class="couponSendDate">
            <span>日期</span>
            <input value="" id="couponSendDate" class="Wdate date-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
        </li>
        <li><span id="sendCouponBtn">确定发券</span></li>
    </ul>
</div>