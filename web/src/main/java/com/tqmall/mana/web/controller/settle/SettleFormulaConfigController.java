package com.tqmall.mana.web.controller.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleFeeFormulaConfigBO;
import com.tqmall.mana.beans.entity.settle.SettleRateConfigDO;
import com.tqmall.mana.beans.param.settle.RateConfigQueryPO;
import com.tqmall.mana.biz.manager.settle.config.SettleFormulaConfigBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleRateConfigBiz;
import com.tqmall.mana.component.enums.FormulaVariableEnum;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by huangzhangting on 17/3/8.
 */
@Controller
@RequestMapping("settle/formulaConfig")
public class SettleFormulaConfigController extends BaseController{
    @Autowired
    private SettleFormulaConfigBiz formulaConfigBiz;
    @Autowired
    private SettleRateConfigBiz rateConfigBiz;


    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("mana/view/settle/formula/formulaConfig");
        return view;
    }

    /**
     * 查询所有公式
     * @return
     */
    @RequestMapping("getAllFormulaConfigs")
    @ResponseBody
    public Result<List<SettleFeeFormulaConfigBO>> getAllFormulaConfigs(){
        return ResultUtil.successResult(formulaConfigBiz.getAllFormulaConfigs());
    }

    /**
     * 添加公式配置
     * @param configBO
     * @return
     */
    @RequestMapping("addFormulaConfig")
    @ResponseBody
    public Result addFormulaConfig(SettleFeeFormulaConfigBO configBO){
        return ResultUtil.successResult(formulaConfigBiz.addFormulaConfig(configBO));
    }

    /**
     * 删除公式配置
     * @param id
     * @return
     */
    @RequestMapping("deleteFormulaConfig")
    @ResponseBody
    public Result deleteFormulaConfig(Integer id){
        Assert.notNull(id, "id不能为空");
        SettleFeeFormulaConfigBO configBO = new SettleFeeFormulaConfigBO();
        configBO.setId(id);
        configBO.setIsDeleted("Y");
        return ResultUtil.successResult(formulaConfigBiz.modifyFormulaConfig(configBO));
    }

    /**
     * 获取特殊变量列表
     */
    @RequestMapping("getSpecialVariables")
    @ResponseBody
    public Result<Map<String, String>> getSpecialVariables(){
        Map<String, String> variableMap = new TreeMap<>();
        for(FormulaVariableEnum variableEnum : FormulaVariableEnum.values()){
            variableMap.put(variableEnum.getKey(), variableEnum.getDesc());
        }
        List<SettleRateConfigDO> rateConfigDOs = rateConfigBiz.getAll(new RateConfigQueryPO());
        for(SettleRateConfigDO configDO : rateConfigDOs){
            variableMap.put(configDO.getRateKey(), configDO.getRateExplain());
        }

        return ResultUtil.successResult(variableMap);
    }

}
