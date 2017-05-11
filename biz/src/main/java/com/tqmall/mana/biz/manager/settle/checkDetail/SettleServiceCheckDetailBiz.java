package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.mana.beans.entity.settle.SettleServiceCheckDetailDO;
import com.tqmall.mana.beans.param.settle.ServicePackageModifyPO;

/**
 * Created by zxg on 17/2/4.
 * 10:31
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface SettleServiceCheckDetailBiz {

    SettleServiceCheckDetailDO getByInsuranceOrderSn(String insuranceOrderSn);

    /**
     * 修改服务包状态
     * @param param
     * @return
     */
    boolean modifyServicePackage(ServicePackageModifyPO param);

}
