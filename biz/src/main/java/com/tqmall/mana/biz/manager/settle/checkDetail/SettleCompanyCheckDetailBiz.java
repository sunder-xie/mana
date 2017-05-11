package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.mana.beans.VO.settle.SettleCompanyBizPagingResult;
import com.tqmall.mana.beans.VO.settle.SettleCompanyCheckDetailVO;
import com.tqmall.mana.beans.param.settle.SettleCompanyCheckDetailBizParam;

import java.util.List;

/**
 * Created by zengjinju on 17/1/21.
 */
public interface SettleCompanyCheckDetailBiz {
    SettleCompanyBizPagingResult insuranceCompanyCheckForERP(SettleCompanyCheckDetailBizParam param);

    /**
     * 根据id更新收款状态
     * @param idList
     * @return
     */
    Boolean updateConfirmMoneyStatusByIds(List<Integer> idList, String operator);

    /**
     * 导出安心返利对账业务表
     * @param param
     * @return
     */
    List<SettleCompanyCheckDetailVO> exportCompanyCheckDetailList(SettleCompanyCheckDetailBizParam param);
}
