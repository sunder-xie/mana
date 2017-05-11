package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.mana.beans.BO.settle.CalculateTotalRebateResultBO;
import com.tqmall.mana.beans.BO.settle.SettleFeeFormulaConfigBO;
import com.tqmall.mana.beans.entity.settle.SettleFeeFormulaConfigDO;
import com.tqmall.mana.beans.param.settle.SettleFeeCalculatePO;

import java.util.List;
import java.util.Set;


/**
 * Created by huangzhangting on 17/3/7.
 */
public interface SettleFormulaConfigBiz {
    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    SettleFeeFormulaConfigBO getFormulaConfigById(Integer id);

    /**
     *
     * @param key
     * @return
     */
    SettleFeeFormulaConfigBO getFormulaConfigByKey(String key);

    /**
     * 查询公式列表
     * @return
     */
    List<SettleFeeFormulaConfigDO> getFormulaConfigDOList();


    List<SettleFeeFormulaConfigBO> getAllFormulaConfigs();


    boolean addFormulaConfig(SettleFeeFormulaConfigBO configBO);


    boolean modifyFormulaConfig(SettleFeeFormulaConfigBO configBO);

    Set<String> getAllFormulaKeys();


    CalculateTotalRebateResultBO calculateTotalRebate(SettleFeeCalculatePO calculatePO);

}
