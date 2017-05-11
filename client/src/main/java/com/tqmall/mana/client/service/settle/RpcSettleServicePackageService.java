package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.ServicePackageModifyParam;
import com.tqmall.mana.client.beans.settle.SettleServiceCheckDetailDTO;

/**
 * Created by zxg on 17/2/4.
 * 10:11
 * no bug,以后改代码的哥们，祝你好运~！！
 * 服务包service
 */
public interface RpcSettleServicePackageService {

    /**
     * 根据insuranceOrderSn 获得唯一的服务包
     * */
    Result<SettleServiceCheckDetailDTO> getDTOByInsuranceOrderSn(String insuranceOrderSn);


    /**
     * 修改服务包状态
     * @param param
     * @return
     */
    Result modifyServicePackage(ServicePackageModifyParam param);

}
