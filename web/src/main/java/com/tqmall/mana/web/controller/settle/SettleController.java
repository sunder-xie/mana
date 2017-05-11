package com.tqmall.mana.web.controller.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.BO.settle.SettleConfigBasicBO;
import com.tqmall.mana.beans.VO.settle.SettleConfigBasicVO;
import com.tqmall.mana.beans.param.settle.SettleConfigBOPageParam;
import com.tqmall.mana.beans.param.settle.SettleConfigVOPageParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleConfigBiz;
import com.tqmall.mana.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhouheng on 17/1/13.
 */
@RequestMapping("settle")
@Controller
@Slf4j
public class SettleController extends BaseController {

    @Autowired
    private SettleConfigBiz settleConfigBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;

    /**
     * 初始化组配置新增页面
     *
     * @return
     */
    @RequestMapping("configBasicAdd")
    public ModelAndView configBasicAdd() {

        ModelAndView modelAndView = new ModelAndView("mana/view/settle/configBasicAdd");
        List<InsuranceCompanyBO> list = insuranceBiz.getAllCompanyList();
        modelAndView.addObject("insuranceCompanyList", list);
        return modelAndView;
    }

    /**
     * 初始化组配置列表
     *
     * @return
     */
    @RequestMapping("configBasicList")
    public ModelAndView configBasicList() {

        ModelAndView modelAndView = new ModelAndView("mana/view/settle/configBasicList");

        return modelAndView;
    }

    /**
     * 初始化组配置编辑页面
     *
     * @return
     */
    @RequestMapping("configBasicEdit")
    public ModelAndView configBasicEdit(Integer basicId) {

        ModelAndView modelAndView = new ModelAndView("mana/view/settle/configBasicEdit");
        List<InsuranceCompanyBO> list = insuranceBiz.getAllCompanyList();
        modelAndView.addObject("insuranceCompanyList", list);
        Result result = settleConfigBiz.getSettleConfig(basicId);
        if (result.isSuccess()) {
            modelAndView.addObject("configBasicBO", result.getData());
        }
        return modelAndView;
    }


    /**
     * 新增组配置信息
     *
     * @param basicVOList
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addSettleConfigList", method = RequestMethod.POST)
    public Result addSettleConfigList(@RequestBody List<SettleConfigBasicVO> basicVOList) {

        return settleConfigBiz.addSettleConfigList(basicVOList);

    }

    /**
     * 通过组配置信息id删除组配置信息
     *
     * @param basicId
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteSettleConfig")
    public Result deleteSettleConfigList(Integer basicId) {

        return settleConfigBiz.deleteSettleConfig(basicId);

    }

    /**
     * 更新组配置信息
     *
     * @param basicVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateSettleConfig", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateSettleConfig(@RequestBody SettleConfigBasicVO basicVO) {

        return settleConfigBiz.updateSettleConfig(basicVO);

    }

    /**
     * 通过保险公司id,险种,保险模式,计费方式查询组配置信息
     *
     * @param voPageParam
     * @return
     */
    @ResponseBody
    @RequestMapping("getBasicBOPagingList")
    public PagingResult<SettleConfigBasicBO> getBasicBOPagingList(SettleConfigVOPageParam voPageParam) {

        return settleConfigBiz.getBasicBOPagingList(voPageParam);

    }

    /**
     * 通过查询条件查询组配置信息列表
     *
     * @param boPageParam
     * @return
     */
    @ResponseBody
    @RequestMapping("getBasicBORedisList")
    public Result<List<SettleConfigBasicBO>> getBasicBORedisList(SettleConfigBOPageParam boPageParam) {

        return Result.wrapSuccessfulResult(settleConfigBiz.getBasicBORedisList(boPageParam));

    }

    /**
     * 通过组配置id获取配置信息
     *
     * @param basicId
     * @return
     */
    @ResponseBody
    @RequestMapping("getSettleConfigById")
    public Result getSettleConfigById(Integer basicId) {

        return settleConfigBiz.getSettleConfig(basicId);

    }

    /**
     * 导出组配置信息列到excel
     *
     * @param
     */
    @RequestMapping("exportSettleConfigList")
    public void exportSettleConfigList() {

        SettleConfigVOPageParam voPageParam = new SettleConfigVOPageParam();

        settleConfigBiz.exportSettleConfigList(response, voPageParam);

    }

}
