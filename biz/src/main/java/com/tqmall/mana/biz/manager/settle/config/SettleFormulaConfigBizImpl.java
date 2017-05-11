package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.mana.beans.BO.settle.CalculateTotalRebateResultBO;
import com.tqmall.mana.beans.BO.settle.SettleFeeFormulaConfigBO;
import com.tqmall.mana.beans.entity.settle.SettleFeeFormulaConfigDO;
import com.tqmall.mana.beans.entity.settle.SettleRateConfigDO;
import com.tqmall.mana.beans.param.settle.RateConfigQueryPO;
import com.tqmall.mana.beans.param.settle.SettleFeeCalculatePO;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.enums.FormulaVariableEnum;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.dao.mapper.settle.SettleFeeFormulaConfigDOMapper;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by huangzhangting on 17/3/7.
 */
@Slf4j
@Service
public class SettleFormulaConfigBizImpl implements SettleFormulaConfigBiz {
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private SettleFeeFormulaConfigDOMapper configDOMapper;
    @Autowired
    private SettleRateConfigBiz rateConfigBiz;


    @Override
    public SettleFeeFormulaConfigBO getFormulaConfigById(Integer id) {
        Assert.notNull(id, "id不能为空");
        SettleFeeFormulaConfigDO settleFeeFormulaConfigDO = configDOMapper.selectByPrimaryKey(id);
        if (settleFeeFormulaConfigDO==null){
            return null;
        }

        SettleFeeFormulaConfigBO formulaConfigBO = BdUtil.do2bo(settleFeeFormulaConfigDO, SettleFeeFormulaConfigBO.class);

        return formulaConfigBO;
    }

    @Override
    public SettleFeeFormulaConfigBO getFormulaConfigByKey(String key) {
        Assert.hasLength(key, "公式key不能为空");
        SettleFeeFormulaConfigDO configDO = configDOMapper.selectByKey(key);
        if(configDO==null){
            return null;
        }
        return BdUtil.do2bo(configDO, SettleFeeFormulaConfigBO.class);
    }

    @Override
    public List<SettleFeeFormulaConfigDO> getFormulaConfigDOList() {
        return configDOMapper.selectAll();
    }

    @Override
    public List<SettleFeeFormulaConfigBO> getAllFormulaConfigs() {
        List<SettleFeeFormulaConfigDO> doList = configDOMapper.selectAll();
        if(doList.isEmpty()){
            return new ArrayList<>();
        }

        List<SettleFeeFormulaConfigBO> configBOList = new ArrayList<>();
        for(SettleFeeFormulaConfigDO formulaConfigDO : doList){
            SettleFeeFormulaConfigBO configBO = BdUtil.do2bo(formulaConfigDO, SettleFeeFormulaConfigBO.class);

            configBOList.add(configBO);
        }

        return configBOList;
    }

    @Override
    public boolean addFormulaConfig(SettleFeeFormulaConfigBO configBO) {
        Assert.notNull(configBO, "参数对象不能为空");
        Assert.hasLength(configBO.getFormulaName(), "请填写公式名称");
        Assert.hasLength(configBO.getFormulaExpress(), "请填写计算公式");
        Assert.hasLength(configBO.getFormulaExplain(), "请填写公式说明");

        SettleFeeFormulaConfigDO formulaConfigDO = BdUtil.do2bo(configBO, SettleFeeFormulaConfigDO.class);
        formulaConfigDO.setGmtCreate(new Date());
        formulaConfigDO.setCreator(shiroBiz.getCurrentUserRealName());

        return configDOMapper.insertSelective(formulaConfigDO)>0;
    }

    @Override
    public boolean modifyFormulaConfig(SettleFeeFormulaConfigBO configBO) {
        Assert.notNull(configBO, "参数对象不能为空");
        Integer id = configBO.getId();
        Assert.notNull(id, "数据id不能为空");
        SettleFeeFormulaConfigDO checkDO = configDOMapper.selectByPrimaryKey(id);
        Assert.notNull(checkDO, "未查到数据，id："+id);

        SettleFeeFormulaConfigDO formulaConfigDO = BdUtil.do2bo(configBO, SettleFeeFormulaConfigDO.class);
        formulaConfigDO.setGmtModified(new Date());
        formulaConfigDO.setModifier(shiroBiz.getCurrentUserRealName());

        return configDOMapper.updateByPrimaryKeySelective(formulaConfigDO)>0;
    }

    @Override
    public Set<String> getAllFormulaKeys() {
        Set<String> set = new HashSet<>();
        List<SettleFeeFormulaConfigDO> configDOList = getFormulaConfigDOList();
        for(SettleFeeFormulaConfigDO configDO : configDOList){
            set.add(configDO.getFormulaKey());
        }
        return set;
    }

    @Override
    public CalculateTotalRebateResultBO calculateTotalRebate(SettleFeeCalculatePO calculatePO) {
        log.info("calculateTotalRebate param:{}", JsonUtil.objectToStr(calculatePO));

        Assert.notNull(calculatePO, "参数对象不能为空");
        Assert.notNull(calculatePO.getBizInsuranceFee(), "商业险保费不能为空");
        Assert.notNull(calculatePO.getForceInsuranceFee(), "交强险保费不能为空");
        Assert.notNull(calculatePO.getCouponAmount(), "现金券金额不能为空");
        Assert.notNull(calculatePO.getInsuranceCompanyId(), "保险公司id不能为空");

        String key = calculatePO.getFormulaKey();
        SettleFeeFormulaConfigBO formulaConfigBO = getFormulaConfigByKey(key);
        Assert.notNull(formulaConfigBO, "未查到计算公式，公式key："+key);

        RateConfigQueryPO queryPO = new RateConfigQueryPO();
        queryPO.setInsuranceCompanyId(calculatePO.getInsuranceCompanyId());
        queryPO.setCityCode(calculatePO.getCityCode());
        List<SettleRateConfigDO> rateConfigDOList = rateConfigBiz.getAll(queryPO);

        Evaluator evaluator = new Evaluator();
        evaluator.putVariable(FormulaVariableEnum.FORCE_INSURANCE_FEE.getKey(), calculatePO.getForceInsuranceFee()+"");
        evaluator.putVariable(FormulaVariableEnum.BIZ_INSURANCE_FEE.getKey(), calculatePO.getBizInsuranceFee()+"");
        evaluator.putVariable(FormulaVariableEnum.COUPON_AMOUNT.getKey(), calculatePO.getCouponAmount()+"");

        for(SettleRateConfigDO configDO : rateConfigDOList){
            evaluator.putVariable(configDO.getRateKey(), configDO.getRateValue()+"");
        }

        //公式
        String exp = formulaConfigBO.getFormulaExpress();
        exp = ManaUtil.formatExpForJEval(exp);
        try {
            String fun = evaluator.replaceVariables(exp); //替换变量后的公式
            BigDecimal result = ManaUtil.getDecimalHalfUp(new BigDecimal(evaluator.evaluate(exp)));
            CalculateTotalRebateResultBO resultBO = new CalculateTotalRebateResultBO();
            resultBO.setFormula(fun);
            resultBO.setTotalRebate(result);
            return resultBO;
        } catch (EvaluationException e) {
            log.error("calculateTotalRebate error, formulaKey:"+key+", formula:"+exp, e);
            throw new BusinessException(e.getMessage());
        }
    }
}
