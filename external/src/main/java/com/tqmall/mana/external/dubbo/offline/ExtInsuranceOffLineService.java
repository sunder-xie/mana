package com.tqmall.mana.external.dubbo.offline;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.insurance.domain.param.insurance.offline.InsuranceOffLineTempInfoParam;
import com.tqmall.insurance.domain.param.insurance.offline.SearchTempInfoListParam;
import com.tqmall.insurance.domain.result.offline.InsuranceOfflineTempInfoDTO;

import java.util.Map;

/**
 * Created by zhouheng on 17/3/24.
 */
public interface ExtInsuranceOffLineService {

    /**
     * 保单审核状态, 0:未审核 1:审核通过 2:审核驳回
     *
     * @return
     */
    Map<Integer,String> getAuditStatusMap();

    /**
     * 线下录单信息查询（支持分页）
     *
     * @param searchTempInfoListParam
     * @return
     */
    PagingResult<InsuranceOfflineTempInfoDTO> getTempInfoPageList(SearchTempInfoListParam searchTempInfoListParam);

    /**
     * mana---录入保单进行审核操作
     * @param tempInfoParam
     * @return
     */
    String modifyEnteringFormAuditStatus(InsuranceOffLineTempInfoParam tempInfoParam);

}
