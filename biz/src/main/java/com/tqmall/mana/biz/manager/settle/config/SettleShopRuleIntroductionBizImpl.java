package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleShopRuleIntroductionBO;
import com.tqmall.mana.beans.entity.settle.SettleShopRuleIntroductionDO;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.settle.SettleShopRuleIntroductionDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 17/2/7.
 */
@Service
public class SettleShopRuleIntroductionBizImpl implements SettleShopRuleIntroductionBiz {

    @Autowired
    private SettleShopRuleIntroductionDOMapper introductionDOMapper;

    @Override
    public Result addRuleIntroduction(SettleShopRuleIntroductionBO introductionBO) {
        SettleShopRuleIntroductionDO introductionDO = BdUtil.do2bo(introductionBO, SettleShopRuleIntroductionDO.class);
        Integer ruleType = introductionDO.getRuleType();
        Date date = new Date();
        SettleShopRuleIntroductionDO introductionDO1 = getRuleIntroductionDOByRuleType(ruleType);
        if (introductionDO1 != null) {//更新
            introductionDO.setId(introductionDO1.getId());
            introductionDO.setCreator("system");
            introductionDO.setModifier("system");
            introductionDO.setGmtCreate(introductionDO1.getGmtCreate());
            introductionDO.setGmtModified(date);
            introductionDO.setIsDeleted("N");
            introductionDOMapper.updateByPrimaryKeySelective(introductionDO);
        } else {//新增
            introductionDO.setCreator("system");
            introductionDO.setModifier("system");
            introductionDO.setIsDeleted("N");
            introductionDO.setGmtCreate(date);
            introductionDO.setGmtModified(date);
            introductionDOMapper.insertSelective(introductionDO);
        }
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public Result deleteIntroduction(Integer introductionId) {
        if (introductionId == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleShopRuleIntroductionDO introductionDO = introductionDOMapper.selectByPrimaryKey(introductionId);
        if (introductionDO != null) {
            introductionDO.setIsDeleted("Y");
            introductionDO.setGmtModified(new Date());
            introductionDO.setModifier("system");
            introductionDOMapper.updateByPrimaryKey(introductionDO);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result updateIntroduction(SettleShopRuleIntroductionBO introductionBO) {
        Integer introductionId = introductionBO.getId();
        if (introductionId == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleShopRuleIntroductionDO ruleIntroductionDO = introductionDOMapper.selectByPrimaryKey(introductionId);
        if (ruleIntroductionDO != null) {
            SettleShopRuleIntroductionDO introductionDO = BdUtil.do2bo(introductionBO, SettleShopRuleIntroductionDO.class);
            introductionDO.setId(ruleIntroductionDO.getId());
            introductionDO.setGmtModified(new Date());
            introductionDO.setModifier("system");
            introductionDO.setCreator(ruleIntroductionDO.getCreator());
            introductionDO.setGmtCreate(ruleIntroductionDO.getGmtCreate());
            introductionDO.setIsDeleted("N");
            introductionDOMapper.updateByPrimaryKeySelective(introductionDO);
        }
        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result<SettleShopRuleIntroductionBO> getRuleIntroductionInfo(Integer introductionId) {
        if (introductionId == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleShopRuleIntroductionDO introductionDO = introductionDOMapper.selectByPrimaryKey(introductionId);
        if (introductionDO != null) {
            SettleShopRuleIntroductionBO introductionBO = BdUtil.do2bo(introductionDO, SettleShopRuleIntroductionBO.class);
            return Result.wrapSuccessfulResult(introductionBO);
        }
        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result<List<SettleShopRuleIntroductionBO>> getRuleIntroductionList() {
        List<SettleShopRuleIntroductionDO> list = introductionDOMapper.selectIntroductionDOList();
        if(!CollectionUtils.isEmpty(list)){
            List<SettleShopRuleIntroductionBO> introductionBOList = BdUtil.do2bo4List(list,SettleShopRuleIntroductionBO.class);
            return Result.wrapSuccessfulResult(introductionBOList);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result<SettleShopRuleIntroductionBO> getRuleIntroductionByRuleType(Integer ruleType) {
        if (ruleType == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleShopRuleIntroductionDO introductionDO = getRuleIntroductionDOByRuleType(ruleType);
        if (introductionDO != null) {
            SettleShopRuleIntroductionBO introductionBO = BdUtil.do2bo(introductionDO, SettleShopRuleIntroductionBO.class);
            return Result.wrapSuccessfulResult(introductionBO);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    private SettleShopRuleIntroductionDO getRuleIntroductionDOByRuleType(Integer ruleType) {
        if (ruleType == null) {
            return null;
        }
        return introductionDOMapper.selectByRuleType(ruleType);
    }

}
