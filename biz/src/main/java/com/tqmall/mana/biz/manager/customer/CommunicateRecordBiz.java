package com.tqmall.mana.biz.manager.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.AddCommunicateRecordBO;
import com.tqmall.mana.beans.BO.customer.SearchCommunicateRecordBO;
import com.tqmall.mana.beans.param.CommonVOPageParam;

/**
 * 沟通记录biz
 * <p>
 * Created by huangzhangting on 17/1/3.
 */
public interface CommunicateRecordBiz {

    /**
     * 新增沟通记录
     *
     * @param communicateRecordBO
     * @return
     */
    Result addCommunicateRecord(AddCommunicateRecordBO communicateRecordBO);

    /**
     * 分页查询沟通记录
     *
     * @param commonVOPageParam
     * @return
     */
    PagingResult<SearchCommunicateRecordBO> searchCommunicationRecordPagingResult(CommonVOPageParam commonVOPageParam);

    /**
     * 通过车辆id获取最新沟通记录
     *
     * @param customerVehicleId
     * @return
     */
    Result<SearchCommunicateRecordBO> searchLatestCommunicationRecord(Integer customerVehicleId);

}
