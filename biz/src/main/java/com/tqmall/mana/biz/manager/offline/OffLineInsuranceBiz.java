package com.tqmall.mana.biz.manager.offline;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.offline.InsuranceOfflineTempInfoBO;
import com.tqmall.mana.beans.VO.insurance.InsuranceOffLineTempInfoVO;
import com.tqmall.mana.beans.param.insurance.SearchTempInfoParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhouheng on 17/3/24.
 */
public interface OffLineInsuranceBiz {

    /**
     * 线下录单信息查询（支持分页）
     *
     * @param searchTempInfoParam
     * @return
     */
    PagingResult<InsuranceOfflineTempInfoBO> getTempInfoPageList(SearchTempInfoParam searchTempInfoParam);

    /**
     * mana---录入保单进行审核操作
     * @param tempInfoVO
     * @return
     */
    void modifyEnteringFormAuditStatus(InsuranceOffLineTempInfoVO tempInfoVO);

    /**
     * 导出数据到excel
     *
     * @param response
     * @param searchTempInfoParam
     */
    void exportOffLineInsuranceList(HttpServletResponse response, SearchTempInfoParam searchTempInfoParam);

}
