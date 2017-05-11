package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleConfigBasicBO;
import com.tqmall.mana.beans.BO.settle.SettleConfigExcelBO;
import com.tqmall.mana.beans.BO.settle.SettleConfigItemBO;
import com.tqmall.mana.beans.VO.settle.SettleConfigBasicVO;
import com.tqmall.mana.beans.entity.settle.SettleConfigBasicDO;
import com.tqmall.mana.beans.entity.settle.SettleConfigItemDO;
import com.tqmall.mana.beans.param.settle.SettleConfigBOPageParam;
import com.tqmall.mana.beans.param.settle.SettleConfigVOPageParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.enums.YesNoEnum;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.component.util.excel.PoiUtil;
import com.tqmall.mana.dao.mapper.settle.SettleConfigBasicDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleConfigItemDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by huangzhangting on 17/1/12.
 */
@Slf4j
@Service
public class SettleConfigBizImpl implements SettleConfigBiz {

    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private SettleShopRuleBiz settleShopRuleBiz;
    @Autowired
    private SettleConfigBasicDOMapper basicDOMapper;
    @Autowired
    private SettleConfigItemDOMapper itemDOMapper;
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;

    @Transactional
    @Override
    public Result addSettleConfigList(List<SettleConfigBasicVO> basicVOList) {
        if (CollectionUtils.isEmpty(basicVOList)) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        //验证组配置名称是否存在,不能存在相同的组配置名称
        checkGroupName(basicVOList);
        //VO转BO
        List<SettleConfigBasicBO> basicBOList = convertSettleVO2BOList(basicVOList);
        //插入
        for (SettleConfigBasicBO basicBO : basicBOList) {
            SettleConfigBasicDO basicDO = BdUtil.do2bo(basicBO, SettleConfigBasicDO.class);
            List<SettleConfigItemDO> itemDOList = BdUtil.do2bo4List(basicBO.getItemBOList(), SettleConfigItemDO.class);
            /**新增组配置基础信息*/
            addBasicDO(basicDO);
            Integer basicId = basicDO.getId();
            if (basicId == null) {
                return ResultUtil.errorResult("", "新增组配置信息失败");
            }
            //组配置详细
            /**批量新增组配置信息*/
            batchAddItemDOList(itemDOList, basicId, ConstantBean.ACTION_ADD);
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 内部方法:组装前端传入参数
     *
     * @param basicVOList
     * @return
     */
    private List<SettleConfigBasicBO> convertSettleVO2BOList(List<SettleConfigBasicVO> basicVOList) {

        List<SettleConfigBasicBO> basicBOList = new ArrayList<>();
        //basicVO转basicBO
        for (SettleConfigBasicVO basic : basicVOList) {
            SettleConfigBasicBO basicBO = BdUtil.do2bo(basic, SettleConfigBasicBO.class);
            List<SettleConfigItemBO> list = BdUtil.do2bo4List(basic.getItemVOList(), SettleConfigItemBO.class);
            basicBO.setItemBOList(list);
            basicBOList.add(basicBO);
        }
        //组配置详细排序,item排序
        for (SettleConfigBasicBO basicBO : basicBOList) {
            List<SettleConfigItemBO> list = basicBO.getItemBOList();
            Collections.sort(list, new Comparator<SettleConfigItemBO>() {
                @Override
                public int compare(SettleConfigItemBO o1, SettleConfigItemBO o2) {
                    if (o1.getItemEndValue() == null) {
                        return 1;
                    }
                    if (o2.getItemEndValue() == null) {
                        return -1;
                    }
                    return o1.getItemEndValue().compareTo(o2.getItemEndValue());
                }
            });
        }
        //item开始值和结束值设置
        for (int i = 0; i < basicBOList.size(); i++) {
            List<SettleConfigItemBO> list = basicBOList.get(i).getItemBOList();
            for (int j = 0; j < list.size(); j++) {
                if (j == 0) {
                    list.get(j).setItemStartValue(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    BigDecimal preItemEndValue = list.get(j - 1).getItemEndValue();
                    if (preItemEndValue != null) {
                        BigDecimal itemStartValue = preItemEndValue.add(new BigDecimal("0.01")).setScale(2, BigDecimal.ROUND_HALF_UP);
                        list.get(j).setItemStartValue(itemStartValue);
                    }
                }
            }
        }
        return basicBOList;
    }

    @Transactional
    @Override
    public Result deleteSettleConfig(Integer basicId) {
        if (basicId == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleConfigBasicDO basicDO = getBasicDOByPrimaryKey(basicId);
        if (basicDO == null) {
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        //查看组配置名称是否在使用中
        Boolean hasUsed = settleShopRuleBiz.querySettleConfigIsUse(basicId);
        if (hasUsed.equals(true)) {
            return ResultUtil.errorResult("", "该组配置信息正在使用中,不能删除");
        }
        /**删除组配置基础信息*/
        basicDO.setIsDeleted(ConstantBean.IS_DELETED_KEY_Y);
        updateBasicDO(basicDO);
        /**批量删除组配置详细信息*/
        batchDeletedItemDOList(basicId);
        return Result.wrapSuccessfulResult(basicId);
    }

    @Override
    public Result getSettleConfig(Integer basicId) {
        if (basicId == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleConfigBasicDO configBasicDO = getBasicDOByPrimaryKey(basicId);
        SettleConfigBasicBO configBasicBO = BdUtil.do2bo(configBasicDO, SettleConfigBasicBO.class);
        //查看组配置名称是否在使用中
        Boolean hasUsed = settleShopRuleBiz.querySettleConfigIsUse(basicId);
        if (hasUsed.equals(true)) {
            configBasicBO.setHasUsed(YesNoEnum.YES.getCode());
        }
        //查询组配置信息列
        List<SettleConfigItemBO> itemBOList = getItemBORedisList(basicId);
        if (!CollectionUtils.isEmpty(itemBOList)) {
            configBasicBO.setItemBOList(itemBOList);
        }

        return Result.wrapSuccessfulResult(configBasicBO);
    }

    @Transactional
    @Override
    public Result updateSettleConfig(SettleConfigBasicVO basicVO) {
        if (basicVO == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        SettleConfigBasicDO basic = getBasicDOByPrimaryKey(basicVO.getId());
        if (basic == null) {
            throw new BusinessCheckException("", "组配置信息不存在");
        }
        if (ConstantBean.IS_DELETED_KEY_Y.equals(basic.getIsDeleted())) {
            throw new BusinessCheckException("", "组配置信息不存在");
        }
        //验证组配置名称是否存在,不能存在相同组配置名称
        List<SettleConfigBasicVO> basicVOList = new ArrayList<>();
        basicVOList.add(basicVO);
        checkGroupName(basicVOList);
        //VO转BO
        List<SettleConfigBasicBO> basicBOList = convertSettleVO2BOList(basicVOList);
        //更新
        for (SettleConfigBasicBO basicBO : basicBOList) {
            SettleConfigBasicDO basicDO = BdUtil.do2bo(basicBO, SettleConfigBasicDO.class);
            Integer basicId = basicDO.getId();
            //查看组配置名称是否在使用中
            Boolean hasUsed = settleShopRuleBiz.querySettleConfigIsUse(basicId);
            if (hasUsed.equals(true)) {
                return ResultUtil.errorResult("", "该组配置信息正在使用中,不能修改");
            }
            /**更新组配置基础信息*/
            updateBasicDO(basicDO);
            /**批量删除组配置详细信息*/
            SettleConfigItemDO itemDO = batchDeletedItemDOList(basicId);
            /**新增(实际是先删除后新增)组配置详细信息:创建人和时间一致*/
            List<SettleConfigItemBO> itemBOList = basicBO.getItemBOList();
            if (!CollectionUtils.isEmpty(itemBOList)) {
                List<SettleConfigItemDO> itemDOList = BdUtil.do2bo4List(basicBO.getItemBOList(), SettleConfigItemDO.class);
                String creator = itemDO.getCreator();
                Date gmtCreate = itemDO.getGmtCreate();
                for (SettleConfigItemDO item : itemDOList) {
                    item.setCreator(creator);
                    item.setGmtCreate(gmtCreate);
                }
                batchAddItemDOList(itemDOList, basicId, ConstantBean.ACTION_UPDATE);
            }

        }

        return Result.wrapSuccessfulResult(true);

    }

    @Override
    public PagingResult<SettleConfigBasicBO> getBasicBOPagingList(SettleConfigVOPageParam voPageParam) {
        //查询分页基础信息
        PagingResult<SettleConfigBasicBO> pagingResult = getBasicBOList(voPageParam);
        //组装详细信息
        if (pagingResult.isSuccess()) {
            List<SettleConfigBasicBO> basicBOList = pagingResult.getList();
            for (SettleConfigBasicBO basicBO : basicBOList) {
                Integer basicId = basicBO.getId();
                //查看组配置名称是否在使用中
                Boolean hasUsed = settleShopRuleBiz.querySettleConfigIsUse(basicId);
                if (hasUsed.equals(true)) {
                    basicBO.setHasUsed(YesNoEnum.YES.getCode());
                }else{
                    basicBO.setHasUsed(YesNoEnum.NO.getCode());
                }
                //查询组配置信息列
                List<SettleConfigItemBO> itemBOList = getItemBORedisList(basicId);
                if (!CollectionUtils.isEmpty(itemBOList)) {
                    basicBO.setItemBOList(itemBOList);
                }
            }
        }

        return pagingResult;
    }

    @Override
    public PagingResult<SettleConfigBasicBO> getBasicBOList(SettleConfigVOPageParam voPageParam) {
        if (voPageParam == null) {
            return PagingResult.wrapErrorResult("", "缺少必要参数");
        }
        SettleConfigBOPageParam boPageParam = BdUtil.do2bo(voPageParam, SettleConfigBOPageParam.class);
        Integer pageSize = boPageParam.getPageSize();
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
            boPageParam.setPageSize(pageSize);
        }
        Integer pageNumber = boPageParam.getPageNumber();
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        Integer offerSet = (pageNumber - 1) * pageSize;
        boPageParam.setOfferSet(offerSet);

        List<SettleConfigBasicBO> basicBOList = getBasicBORedisList(boPageParam);
        if (!CollectionUtils.isEmpty(basicBOList)) {
            //分页总个数
            Integer count = basicDOMapper.selectBasicDOListCountByParam(boPageParam);
            return PagingResult.wrapSuccessfulResult(basicBOList, count);
        }
        return PagingResult.wrapErrorResult("", "无查询数据");
    }

    @Override
    public List<SettleConfigBasicBO> getBasicBORedisList(SettleConfigBOPageParam boPageParam) {
        //分页查询组配置信息
        List<SettleConfigBasicDO> basicDOList = basicDOMapper.selectBasicDOListByParam(boPageParam);
        List<SettleConfigBasicBO> basicBOList = BdUtil.do2bo4List(basicDOList, SettleConfigBasicBO.class);
        if (!CollectionUtils.isEmpty(basicDOList)) {

            for (SettleConfigBasicBO basicBO : basicBOList) {
                Integer cooperationMode = basicBO.getCooperationMode();
                Integer calculateMode = basicBO.getCalculateMode();
                String cooperationModeName = insuranceDicBiz.getCooperationModeNameByDicId(cooperationMode);
                String calculateModeName = insuranceDicBiz.getCalculateModeNameByDicId(calculateMode);
                basicBO.setCooperationModeName(cooperationModeName);
                basicBO.setCalculateModeName(calculateModeName);
            }
        }
        return basicBOList;
    }

    @Override
    public List<SettleConfigItemBO> getItemBORedisList(Integer basicId) {
        List<SettleConfigItemDO> itemDOList = itemDOMapper.selectItemDOListByBasicId(basicId);
        if (!CollectionUtils.isEmpty(itemDOList)) {
            List<SettleConfigItemBO> itemBOList = BdUtil.do2bo4List(itemDOList, SettleConfigItemBO.class);
            //按照组配置详细的开始值由小到大倒序排序
            Collections.sort(itemBOList, new Comparator<SettleConfigItemBO>() {
                @Override
                public int compare(SettleConfigItemBO o1, SettleConfigItemBO o2) {
                    return o1.getItemStartValue().compareTo(o2.getItemStartValue());
                }
            });
            return itemBOList;
        }
        return null;
    }

    @Override
    public void exportSettleConfigList(HttpServletResponse response, SettleConfigVOPageParam voPageParam) {
        if (voPageParam == null) {
            throw new BusinessCheckException("", "导出数据异常");
        }
        PoiUtil poiUtil = new PoiUtil();
        SettleConfigBOPageParam boPageParam = BdUtil.do2bo(voPageParam, SettleConfigBOPageParam.class);
        Integer count = basicDOMapper.selectBasicDOListCountByParam(boPageParam);
        if (count == 0) {
            try {
                poiUtil.noDataExcel(response);
            } catch (Exception e) {
                log.error("export send log excel error", e);
            }
        }
        boPageParam.setOfferSet(0);
        boPageParam.setPageSize(count);
        List<SettleConfigBasicBO> basicBOList = getBasicBORedisList(boPageParam);
        if (!CollectionUtils.isEmpty(basicBOList)) {
            List<SettleConfigExcelBO> excelBOList = BdUtil.do2bo4List(basicBOList, SettleConfigExcelBO.class);
            for (SettleConfigExcelBO excelBO : excelBOList) {
                //设置值
                excelBO.setCooperationModeName(insuranceDicBiz.getCooperationModeNameByDicId(excelBO.getCooperationMode()));
                excelBO.setCalculateModeName(insuranceDicBiz.getCalculateModeNameByDicId(excelBO.getCalculateMode()));
                Integer basicId = excelBO.getId();
                List<SettleConfigItemBO> itemBOList = getItemBORedisList(basicId);
                excelBO.setItemBOList(itemBOList);
            }
            String[] heads = new String[]{"保险公司", "保险模式", "险种", "计费方式", "保费区间分组名", "保费区间", "比例"};
            String[] fields = new String[]{"insuranceCompanyName", "cooperationModeName", "insuranceTypeName", "calculateModeName",
                    "groupName", "itemBOList.itemStartValue.itemEndValue", "itemBOList.itemRate"};
            try {
                poiUtil.exportXlsxToClient(response, "组配置详细列表", heads, fields, excelBOList);
            } catch (Exception e) {
                log.error("export send log excel error", e);
            }
        }

    }

    /**
     * 内部:通过组配置基础id获取组配置基础信息
     *
     * @param basicId
     * @return
     */
    private SettleConfigBasicDO getBasicDOByPrimaryKey(Integer basicId) {
        if (basicId == null) {
            return null;
        }
        return basicDOMapper.selectByPrimaryKey(basicId);
    }

    /**
     * 内部:更新组配置基础信息
     *
     * @param basicDO
     */
    private void updateBasicDO(SettleConfigBasicDO basicDO) {
        Date date = new Date();
        basicDO.setModifier("system");
        basicDO.setGmtModified(date);
        basicDOMapper.updateByPrimaryKeySelective(basicDO);
    }

    /**
     * 内部:新增组配置基础信息
     *
     * @param basicDO
     */
    private void addBasicDO(SettleConfigBasicDO basicDO) {
        basicDO.setGmtCreate(new Date());
        basicDO.setCreator("system");
        basicDO.setModifier("system");
        basicDOMapper.insertSelective(basicDO);
    }

    /**
     * 内部:批量插入组配置详细信息
     *
     * @param itemDOList
     */
    private void batchAddItemDOList(List<SettleConfigItemDO> itemDOList, Integer basicId, String actionFlag) {
        if (CollectionUtils.isEmpty(itemDOList)) {
            throw new BusinessCheckException("", "缺少组配置详细信息");
        }
        Date date = new Date();
        for (SettleConfigItemDO itemDO : itemDOList) {
            itemDO.setBasicId(basicId);
            if (actionFlag.equals(ConstantBean.ACTION_ADD)) {
                itemDO.setCreator("system");
                itemDO.setModifier("system");
                itemDO.setGmtCreate(date);
                itemDO.setGmtModified(date);
            }
            if (actionFlag.equals(ConstantBean.ACTION_UPDATE)) {
                itemDO.setGmtModified(date);
                itemDO.setModifier("system");
            }
            itemDOMapper.insertSelective(itemDO);
        }
    }

    /**
     * 内部:批量删除组配置详细信息
     *
     * @param basicId
     */
    private SettleConfigItemDO batchDeletedItemDOList(Integer basicId) {
        if (basicId == null) {
            throw new BusinessCheckException("", "参数异常");
        }
        List<SettleConfigItemDO> list = itemDOMapper.selectItemDOListByBasicId(basicId);
        if (!CollectionUtils.isEmpty(list)) {
            Date date = new Date();
            for (SettleConfigItemDO itemDO : list) {
                itemDO.setIsDeleted(ConstantBean.IS_DELETED_KEY_Y);
                itemDO.setModifier("system");
                itemDO.setGmtModified(date);
                itemDOMapper.updateByPrimaryKeySelective(itemDO);
            }
            return list.get(0);
        }
        return null;
    }

    /**
     * 内部:验证组配置名称是否存在
     *
     * @param basicVOList
     */
    private void checkGroupName(List<SettleConfigBasicVO> basicVOList) {

        //查看组配置名称是否已存在
        for (int j = 0; j < basicVOList.size(); j++) {
            String groupName = basicVOList.get(j).getGroupName();
            if (StringUtils.isEmpty(groupName)) {
                throw new BusinessCheckException("", "组配置名称不能为空");
            }
            for (int i = j + 1; i < basicVOList.size(); i++) {
                String name = basicVOList.get(i).getGroupName();
                if (groupName.equals(name)) {
                    throw new BusinessCheckException("", "保费区间分组名不允许重复，请重新输入");
                }
            }
            //组配置名称不能同名 //
            List<SettleConfigBasicDO> configBasicDOList = basicDOMapper.selectDOListByGroupName(groupName);
            if (!CollectionUtils.isEmpty(configBasicDOList)) {
                Integer basicId = basicVOList.get(j).getId();
                for (SettleConfigBasicDO configDO : configBasicDOList) {
                    if (!configDO.getId().equals(basicId)) {
                        throw new BusinessCheckException("", "保费区间分组名称["+groupName+"]已存在，请重新输入");
                    }
                }
            }
        }
    }

}
