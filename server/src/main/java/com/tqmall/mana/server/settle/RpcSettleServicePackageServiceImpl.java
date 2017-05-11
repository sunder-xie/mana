package com.tqmall.mana.server.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.entity.settle.SettleServiceCheckDetailDO;
import com.tqmall.mana.beans.param.settle.ServicePackageModifyPO;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleServiceCheckDetailBiz;
import com.tqmall.mana.client.beans.param.ServicePackageModifyParam;
import com.tqmall.mana.client.beans.settle.SettleServiceCheckDetailDTO;
import com.tqmall.mana.client.service.settle.RpcSettleServicePackageService;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zxg on 17/2/4.
 * 10:14
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class RpcSettleServicePackageServiceImpl implements RpcSettleServicePackageService {

    @Autowired
    private SettleServiceCheckDetailBiz settleServiceCheckDetailBiz;


    @Override
    public Result<SettleServiceCheckDetailDTO> getDTOByInsuranceOrderSn(String insuranceOrderSn) {

        SettleServiceCheckDetailDO byInsuranceOrderSn = settleServiceCheckDetailBiz.getByInsuranceOrderSn(insuranceOrderSn);

        return ResultUtil.successResult(byInsuranceOrderSn,SettleServiceCheckDetailDTO.class);
    }

    @Override
    public Result modifyServicePackage(ServicePackageModifyParam param) {
        ServicePackageModifyPO modifyPO = BdUtil.do2bo(param, ServicePackageModifyPO.class);
        boolean flag = settleServiceCheckDetailBiz.modifyServicePackage(modifyPO);
        if(flag){
            return ResultUtil.successResult("", "修改成功");
        }
        return ResultUtil.errorResult("", "修改失败");
    }

}
