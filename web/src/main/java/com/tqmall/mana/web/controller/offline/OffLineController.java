package com.tqmall.mana.web.controller.offline;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.offline.InsuranceOfflineTempInfoBO;
import com.tqmall.mana.beans.VO.insurance.InsuranceOffLineTempInfoVO;
import com.tqmall.mana.beans.param.insurance.SearchTempInfoParam;
import com.tqmall.mana.biz.manager.offline.OffLineInsuranceBiz;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhouheng on 17/3/22.
 */
@Controller
@RequestMapping("offline")
public class OffLineController extends BaseController {

    @Autowired
    private OffLineInsuranceBiz offLineInsuranceBiz;

    @RequestMapping("offLineList")
    public ModelAndView insuranceList() {

        ModelAndView modelAndView = new ModelAndView("mana/view/offline/offLineList");

        return modelAndView;

    }

    /**
     * 线下录单信息查询（支持分页）
     *
     * @param searchTempInfoParam
     * @return
     */
    @RequestMapping("getTempInfoPageList")
    @ResponseBody
    public PagingResult<InsuranceOfflineTempInfoBO> getTempInfoPageList(SearchTempInfoParam searchTempInfoParam) {

        return offLineInsuranceBiz.getTempInfoPageList(searchTempInfoParam);

    }

    /**
     * mana---录入保单进行审核操作
     *
     * @param tempInfoVO
     * @return
     */
    @RequestMapping(value = "modifyEnteringFormAuditStatus",method = RequestMethod.POST)
    @ResponseBody
    public Result<Void> modifyEnteringFormAuditStatus(InsuranceOffLineTempInfoVO tempInfoVO){

        offLineInsuranceBiz.modifyEnteringFormAuditStatus(tempInfoVO);

        return Result.wrapSuccessfulResult(null);

    }

    @RequestMapping("exportData")
    public void exportData(SearchTempInfoParam searchTempInfoParam){

        offLineInsuranceBiz.exportOffLineInsuranceList(response,searchTempInfoParam);

    }


}
