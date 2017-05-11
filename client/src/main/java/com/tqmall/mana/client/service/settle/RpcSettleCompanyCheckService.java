package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.SettleCompanyCheckDetailParam;
import com.tqmall.mana.client.beans.settle.SettleCompanyCheckDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleCompanyCheckPagingResult;

import java.util.List;

/**
 * Created by zengjinju on 17/1/20.
 */
public interface RpcSettleCompanyCheckService {

    Result<SettleCompanyCheckPagingResult> insuranceCompanyCheckForERP(SettleCompanyCheckDetailParam param);

    /**
     * 确认收款
     * @param idList 数据id集合
     * @param operator 操作人
     * @return
     */
    Result<Boolean> updateConfirmMoneyStatusByIds(List<Integer> idList, String operator);

    Result<List<SettleCompanyCheckDetailDTO>> exportCompanyCheckDetailList(SettleCompanyCheckDetailParam param);
}
