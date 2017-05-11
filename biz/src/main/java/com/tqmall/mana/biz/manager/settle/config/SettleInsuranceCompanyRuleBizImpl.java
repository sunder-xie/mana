package com.tqmall.mana.biz.manager.settle.config;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.common.exception.BusinessProcessFailException;
import com.tqmall.insurance.domain.result.InsuranceRegionDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceRegionService;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.BO.settle.SettleCompanyRulePageBO;
import com.tqmall.mana.beans.VO.settle.SettleCompanyRuleSearchVO;
import com.tqmall.mana.beans.VO.settle.SettleInsuranceCompanyRuleItemVO;
import com.tqmall.mana.beans.VO.settle.SettleInsuranceCompanyRuleVO;
import com.tqmall.mana.beans.entity.settle.SettleCompanyCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.SettleInsuranceCompanyRuleDO;
import com.tqmall.mana.beans.entity.settle.SettleInsuranceCompanyRuleItemDO;
import com.tqmall.mana.beans.entity.settle.SettleScenarioInsuranceCategoryRelDO;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.beans.param.settle.SettleShopRuleRegionParam;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleBasicMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleFormMsg;
import com.tqmall.mana.biz.mq.insurance.settle.InsuranceSettleItemMsg;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.ScenarioTypeEnum;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.redis.RedisClientTemplate;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.redis.lock.LockCallBack;
import com.tqmall.mana.component.redis.lock.RedisLockClient;
import com.tqmall.mana.component.util.BeanUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.settle.SettleCompanyCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleInsuranceCompanyRuleDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleInsuranceCompanyRuleItemDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleScenarioInsuranceCategoryRelDOMapper;
import com.tqmall.mana.dao.mapper.settle.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 保险公司结算规则配置业务代码
 * <p>
 * Created by huangzhangting on 17/1/13.
 */
@Slf4j
@Service
public class SettleInsuranceCompanyRuleBizImpl implements SettleInsuranceCompanyRuleBiz {
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private SettleShopRuleBiz settleShopRuleBiz;
    @Autowired
    private RpcInsuranceRegionService rpcInsuranceRegionService;
    @Autowired
    private SettleInsuranceCompanyRuleDOMapper settleInsuranceCompanyRuleDOMapper;
    @Autowired
    private SettleInsuranceCompanyRuleItemDOMapper settleInsuranceCompanyRuleItemDOMapper;
    @Autowired
    private SettleScenarioInsuranceCategoryRelDOMapper settleScenarioInsuranceCategoryRelDOMapper;
    @Autowired
    private RedisClientTemplate redisClientTemplate;
    @Autowired
    private SettleCompanyCheckDetailDOMapper settleCompanyCheckDetailDOMapper;
    @Autowired
    private SettleShopRuleRegionConfigDOMapper settleShopRuleRegionConfigDOMapper;
    @Autowired
    private RedisLockClient redisLockClient;
    @Autowired
    private InsuranceBiz insuranceBiz;

    private final static String SCENARIO_TYPE = RedisKeyBean.SYSTEM_PREFIX + "scenarioTypes_insuranceCompanyId_";


    @Override
    public Result<List<InsuranceRegionDTO>> getInsuredRegionAndIsOpen(String regionParentCode) {
        if (StringUtils.isEmpty(regionParentCode)) {
            log.info("regionParentCode is null");
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        Result<List<InsuranceRegionDTO>> result = rpcInsuranceRegionService.selectRegionByCodeNndIsOpen(regionParentCode);
        if ("000000".equals(regionParentCode)) {
            return result;
        }
        //获取某个省份的未配置过的城市站
        List<InsuranceRegionDTO> resultList = new ArrayList<>();
        List<InsuranceRegionDTO> list = result.getData();
        final Map<String, InsuranceRegionDTO> map = new HashMap<>();
        List<String> regionCodeList = Lists.transform(list, new Function<InsuranceRegionDTO, String>() {
            @Override
            public String apply(InsuranceRegionDTO insuranceRegionDTO) {
                map.put(insuranceRegionDTO.getRegionCode(), insuranceRegionDTO);
                return insuranceRegionDTO.getRegionCode();
            }
        });
        //从settle_insurance_company_rule表中获取当前省份配置过分成比例的城市站
        List<SettleInsuranceCompanyRuleDO> ruleDOList = settleInsuranceCompanyRuleDOMapper.getInsuranceCompanyRuleListByProvinceCode(regionParentCode);
        List<String> configedRegionCode = Lists.transform(ruleDOList, new Function<SettleInsuranceCompanyRuleDO, String>() {
            @Override
            public String apply(SettleInsuranceCompanyRuleDO settleInsuranceCompanyRuleDO) {
                return settleInsuranceCompanyRuleDO.getCityCode();
            }
        });
        //获取未配置过的城市站code
        regionCodeList.removeAll(configedRegionCode);
        for (String regionCode : regionCodeList) {
            if (map.containsKey(regionCode)) {
                resultList.add(map.get(regionCode));
            }
        }
        return Result.wrapSuccessfulResult(resultList);
    }

    @Override
    public List<ScenarioTypeEnum> getScenarioTypes() {
        return Arrays.asList(ScenarioTypeEnum.values());
    }

    @Transactional
    @Override
    public Result<Boolean> saveInsuranceCompanyRule(SettleInsuranceCompanyRuleVO settleInsuranceCompanyRuleVO) {
        Assert.notNull(settleInsuranceCompanyRuleVO);
        String operator = shiroBiz.getCurrentUserRealName();
        List<SettleInsuranceCompanyRuleItemDO> itemList = new ArrayList<>();
        BeanUtil.checkParamisNull(settleInsuranceCompanyRuleVO);
        Integer size = settleInsuranceCompanyRuleVO.getCityCodeList().size();
        //当id不为null时，更新比例
        if (settleInsuranceCompanyRuleVO.getId() != null) {
            for (SettleInsuranceCompanyRuleItemVO itemVO : settleInsuranceCompanyRuleVO.getRuleItemList()) {
                SettleInsuranceCompanyRuleItemDO itemDO = new SettleInsuranceCompanyRuleItemDO();
                BeanUtils.copyProperties(itemVO, itemDO);
                itemDO.getCommissionRate().setScale(2);
                itemDO.setModifier(operator);
                itemDO.setGmtModified(DateTime.now().toDate());
                itemDO.setSettleRuleId(settleInsuranceCompanyRuleVO.getId());
                itemList.add(itemDO);
                settleInsuranceCompanyRuleItemDOMapper.batchUpdate(itemDO);
            }
            //settleInsuranceCompanyRuleItemDOMapper.batchUpdate(itemList);
        } else {
            //判断当前选择的城市站是否已经配置过
            List<SettleInsuranceCompanyRuleDO> list = settleInsuranceCompanyRuleDOMapper.selectByCityCods(settleInsuranceCompanyRuleVO.getCityCodeList());
            if (list.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (SettleInsuranceCompanyRuleDO settleInsuranceCompanyRuleDO : list) {
                    builder.append(settleInsuranceCompanyRuleDO.getCityName()).append(",");
                }
                return Result.wrapErrorResult("", builder.toString() + "城市已经配置过");
            }
            for (int i = 0; i < size; i++) {
                SettleInsuranceCompanyRuleDO settleInsuranceCompanyRuleDO = new SettleInsuranceCompanyRuleDO();
                BeanUtils.copyProperties(settleInsuranceCompanyRuleVO, settleInsuranceCompanyRuleDO);
                settleInsuranceCompanyRuleDO.setCityCode(settleInsuranceCompanyRuleVO.getCityCodeList().get(i));
                settleInsuranceCompanyRuleDO.setCityName(settleInsuranceCompanyRuleVO.getCityNameList().get(i));
                settleInsuranceCompanyRuleDO.setCreator(operator);
                settleInsuranceCompanyRuleDO.setGmtCreate(DateTime.now().toDate());
                settleInsuranceCompanyRuleDOMapper.insertSelective(settleInsuranceCompanyRuleDO);
                itemList.clear();
                for (SettleInsuranceCompanyRuleItemVO companyRuleItemVO : settleInsuranceCompanyRuleVO.getRuleItemList()) {
                    SettleInsuranceCompanyRuleItemDO itemDO = new SettleInsuranceCompanyRuleItemDO();
                    itemDO.setCreator(operator);
                    itemDO.setGmtCreate(DateTime.now().toDate());
                    itemDO.setInsuranceType(companyRuleItemVO.getInsuranceType());
                    itemDO.setCommissionRate(companyRuleItemVO.getCommissionRate().setScale(2));
                    itemDO.setScenarioType(companyRuleItemVO.getScenarioType());
                    //保存settle_insurance_company_rule.id
                    itemDO.setSettleRuleId(settleInsuranceCompanyRuleDO.getId());
                    itemList.add(itemDO);
                }
                settleInsuranceCompanyRuleItemDOMapper.batchInsert(itemList);
            }
        }
        return Result.wrapSuccessfulResult(Boolean.TRUE);
    }

    @Transactional
    @Override
    public Boolean deleteInsuranceCompanyRuleById(Integer id) {
        Assert.notNull(id, "id不能为null");
        int effectiveRow = settleInsuranceCompanyRuleDOMapper.deleteInsuranceCompanyRuleById(id);
        int itemEffectiveRow = settleInsuranceCompanyRuleItemDOMapper.deleteRuleItemBySettleRuleId(id);
        return effectiveRow > 0 && itemEffectiveRow > 0;
    }

    @Override
    public PagingResult<SettleCompanyRulePageBO> searchInsuranceCompanyRuleForPage(SettleCompanyRuleSearchVO searchVO) {
        Map<String, Object> searchParam = new HashMap<>();
        if ("".equals(searchVO.getCityCode())) {
            searchVO.setCityCode(null);
        }
        if ("".equals(searchVO.getProvinceCode())) {
            searchVO.setProvinceCode(null);
        }
        searchParam.put("start", (searchVO.getPageNumber() - 1) * searchVO.getPageSize());
        searchParam.put("limit", searchVO.getPageSize() < 0 ? 1 : searchVO.getPageSize());
        searchParam.put("cityCode", searchVO.getCityCode());
        searchParam.put("provinceCode", searchVO.getProvinceCode());
        Integer totalSize = settleInsuranceCompanyRuleDOMapper.getCountByCondation(searchParam);
        List<SettleInsuranceCompanyRuleDO> ruleDOList = settleInsuranceCompanyRuleDOMapper.selectByCityCodeAndProvinceCode(searchParam);
        final Map<Integer, SettleInsuranceCompanyRuleDO> map = new HashMap<>();
        List<Integer> ruleDOIds = Lists.transform(ruleDOList, new Function<SettleInsuranceCompanyRuleDO, Integer>() {
            @Override
            public Integer apply(SettleInsuranceCompanyRuleDO settleInsuranceCompanyRuleDO) {
                map.put(settleInsuranceCompanyRuleDO.getId(), settleInsuranceCompanyRuleDO);
                return settleInsuranceCompanyRuleDO.getId();
            }
        });
        List<SettleInsuranceCompanyRuleItemDO> itemList = settleInsuranceCompanyRuleItemDOMapper.selectRuleItemBysettleRuleId(ruleDOIds);
        List<SettleCompanyRulePageBO> content = new ArrayList<>();
        Map<Integer, List<SettleInsuranceCompanyRuleItemVO>> ruleItemVOMap = new HashMap<>();
        for (SettleInsuranceCompanyRuleItemDO itemDO : itemList) {
            SettleInsuranceCompanyRuleItemVO ruleItemVO = new SettleInsuranceCompanyRuleItemVO();
            ruleItemVO.setCommissionRate(itemDO.getCommissionRate());
            ruleItemVO.setScenarioType(itemDO.getScenarioType());
            ruleItemVO.setScenarioName(ScenarioTypeEnum.codeDescription(itemDO.getScenarioType()));
            ruleItemVO.setInsuranceType(itemDO.getInsuranceType());
            ruleItemVO.setInsuranceName(InsuranceTypeEnum.codeDescription(itemDO.getInsuranceType()));

            if (ruleItemVOMap.containsKey(itemDO.getSettleRuleId())) {
                ruleItemVOMap.get(itemDO.getSettleRuleId()).add(ruleItemVO);
            } else {
                List<SettleInsuranceCompanyRuleItemVO> list = new ArrayList<>();
                list.add(ruleItemVO);
                ruleItemVOMap.put(itemDO.getSettleRuleId(), list);
            }
        }
        for (Map.Entry<Integer, SettleInsuranceCompanyRuleDO> entry : map.entrySet()) {
            SettleCompanyRulePageBO pageBO = new SettleCompanyRulePageBO();
            BeanUtils.copyProperties(entry.getValue(), pageBO);
            pageBO.setRuleItemList(ruleItemVOMap.get(entry.getKey()));
            content.add(pageBO);
        }
        Collections.sort(content, new Comparator<SettleCompanyRulePageBO>() {
            @Override
            public int compare(SettleCompanyRulePageBO o1, SettleCompanyRulePageBO o2) {
                int result = o1.getGmtCreate().compareTo(o2.getGmtCreate());
                if (result > 0) {
                    result = -1;
                } else if (result < 0) {
                    result = 1;
                }
                return result;
            }
        });
        return PagingResult.wrapSuccessfulResult(content, totalSize);
    }

    @Override
    public Result<List<InsuranceRegionDTO>> getRegionList(String regionParentCode) {
        if (StringUtils.isEmpty(regionParentCode)) {
            regionParentCode = "000000";
        }
        return rpcInsuranceRegionService.selectRegionByCodeNndIsOpen(regionParentCode);
    }

    @Override
    public Result<SettleInsuranceCompanyRuleVO> getInsuranceCompanyRuleById(Integer id) {
        Assert.notNull(id, "id不能为空");
        SettleInsuranceCompanyRuleVO ruleVO = new SettleInsuranceCompanyRuleVO();
        SettleInsuranceCompanyRuleDO settleInsuranceCompanyRuleDO = settleInsuranceCompanyRuleDOMapper.selectByPrimaryKey(id);
        if (settleInsuranceCompanyRuleDO == null) {
            return Result.wrapErrorResult("", "获取的返点配置信息不存在");
        }
        BeanUtils.copyProperties(settleInsuranceCompanyRuleDO, ruleVO);
        List<Integer> list = new ArrayList<>();
        list.add(settleInsuranceCompanyRuleDO.getId());
        List<SettleInsuranceCompanyRuleItemDO> itemList = settleInsuranceCompanyRuleItemDOMapper.selectRuleItemBysettleRuleId(list);
        if (CollectionUtils.isEmpty(itemList)) {
            return Result.wrapErrorResult("", "获取的返点配置信息不存在");
        }
        List<SettleInsuranceCompanyRuleItemVO> itemVOList = new ArrayList<>();
        for (SettleInsuranceCompanyRuleItemDO settleInsuranceCompanyRuleItemDO : itemList) {
            SettleInsuranceCompanyRuleItemVO itemVO = new SettleInsuranceCompanyRuleItemVO();
            itemVO.setCommissionRate(settleInsuranceCompanyRuleItemDO.getCommissionRate());
            itemVO.setInsuranceType(settleInsuranceCompanyRuleItemDO.getInsuranceType());
            itemVO.setScenarioType(settleInsuranceCompanyRuleItemDO.getScenarioType());
            itemVOList.add(itemVO);
        }
        List<String> cityCodeList = new ArrayList<>();
        List<String> cityNameList = new ArrayList<>();
        cityCodeList.add(settleInsuranceCompanyRuleDO.getCityCode());
        cityNameList.add(settleInsuranceCompanyRuleDO.getCityName());
        ruleVO.setCityCodeList(cityCodeList);
        ruleVO.setRuleItemList(itemVOList);
        ruleVO.setCityNameList(cityNameList);
        return Result.wrapSuccessfulResult(ruleVO);
    }

    @Override
    public Integer getScenarioType(InsuranceSettleBasicMsg basicMsg) {
        Assert.notNull(basicMsg, "insuranceSettleBasicMsg不能为空");
        Assert.notNull(basicMsg.getInsuranceCompanyId(), "保险公司id不能为空");
        Assert.hasLength(basicMsg.getInsuredCityCode(), "投保城市不能为空");

        List<InsuranceSettleFormMsg> formMsgList = basicMsg.getFormMsgList();
        Assert.notEmpty(formMsgList, "保单不能为空");
        //险种条目
        List<InsuranceSettleItemMsg> allItemMsgList = new ArrayList<>();
        for (InsuranceSettleFormMsg formMsg : formMsgList) {
            List<InsuranceSettleItemMsg> itemMsgList = formMsg.getItemMsgList();
            Assert.notEmpty(itemMsgList, "险种条目不能为空");
            for (InsuranceSettleItemMsg itemMsg : itemMsgList) {
                Assert.hasLength(itemMsg.getInsuranceCategoryCode(), "险种代码不能为空");
                allItemMsgList.add(itemMsg);
            }
        }

        List<SettleScenarioInsuranceCategoryRelDO> scenarioTypeList = getScenarioTypesByCompany(basicMsg.getInsuranceCompanyId());
        if (CollectionUtils.isEmpty(scenarioTypeList)) {
            log.info("当前不存在任何的投保场景");
            throw new BusinessCheckException("", "not exit any scenario types");
        }
        //记录每个险种所关联的投保场景
        Map<String, List<SettleScenarioInsuranceCategoryRelDO>> scenarioRelMap = Maps.newHashMap();
        //保存投保场景和权重值的对应关系
        Map<Integer, Integer> scenarioWeightMap = Maps.newHashMap();
        for (SettleScenarioInsuranceCategoryRelDO categoryRelDO : scenarioTypeList) {
            if (scenarioRelMap.containsKey(categoryRelDO.getInsuranceCategoryCode())) {
                scenarioRelMap.get(categoryRelDO.getInsuranceCategoryCode()).add(categoryRelDO);
            } else {
                List<SettleScenarioInsuranceCategoryRelDO> list = new ArrayList<>();
                list.add(categoryRelDO);
                scenarioRelMap.put(categoryRelDO.getInsuranceCategoryCode(), list);
            }
            //计算投保场景和权重的对应关系
            if (!scenarioWeightMap.containsKey(categoryRelDO.getScenarioWeight())) {
                scenarioWeightMap.put(Integer.valueOf(categoryRelDO.getScenarioType()), categoryRelDO.getScenarioWeight());
            }
        }
        //记录最终的投保场景结果
        List<Integer> resultScenarioType = new ArrayList<>();
        //获取当前保单对应的具体的投保场景
//        for (InsuranceSettleFormMsg insuranceForm : basicMsg.getFormMsgList()) {
        for (InsuranceSettleItemMsg insuranceItem : allItemMsgList) {
            if (scenarioRelMap.containsKey(insuranceItem.getInsuranceCategoryCode())) {
                List<Integer> typeList = Lists.transform((List<SettleScenarioInsuranceCategoryRelDO>) scenarioRelMap.get(insuranceItem.getInsuranceCategoryCode()), new Function<SettleScenarioInsuranceCategoryRelDO, Integer>() {
                    @Override
                    public Integer apply(SettleScenarioInsuranceCategoryRelDO categoryRelDO) {
                        return Integer.valueOf(categoryRelDO.getScenarioType());
                    }
                });

                //判断是否是第一次进来
                if (CollectionUtils.isEmpty(resultScenarioType)) {
                    //获取当前保单item所对应的投保场景
                    resultScenarioType = typeList;
                } else {
                    //计算投保场景的交集
                    resultScenarioType.retainAll(typeList);
                }
            }
        }
//        }
        Integer scenarioType = 0;
        if (resultScenarioType.size() > 1) {
            Integer max = 0;
            //取出权重最大的投保场景
            for (Integer i : resultScenarioType) {
                Integer weight = scenarioWeightMap.get(i);
                if (weight > max) {
                    max = weight;
                    scenarioType = i;
                }
            }
        } else if (resultScenarioType.size() == 1) {
            scenarioType = resultScenarioType.get(0);
        }
        return scenarioType;
    }

    @Override
    public Boolean calculateInsuredRoyaltyFee(final InsuranceSettleBasicMsg basicMsg) {
        Object result = redisLockClient.lock(RedisKeyBean.LOCK_CALCULATE_INSURANCE_ROYALTY_FEE + basicMsg.getInsuranceOrderSn(), new LockCallBack() {
            @Override
            public Object onTask() {
                return doCalculateInsuredRoyaltyFee(basicMsg);
            }
        });
        if (result == null) {
            throw new BusinessProcessFailException("-0001", "计算安心保费分成的时候出现未知异常");
        }
        return (Boolean) result;
    }

    @Override
    public Result<List<InsuranceRegionDTO>> getAvailableRegionList(SettleShopRuleRegionParam settleShopRuleRegionParam) {
        String regionParentCode = settleShopRuleRegionParam.getRegionParentCode();
        if (StringUtils.isEmpty(regionParentCode)) {
            regionParentCode = "000000";
        }
        Result<List<InsuranceRegionDTO>> result = rpcInsuranceRegionService.selectRegionByCodeNndIsOpen(regionParentCode);
        if(!result.isSuccess()){
            log.info("[dubbo]接口调用失败:原因:{}",result.getMessage());
            throw new BusinessCheckException("接口调用失败:原因:{}",result.getMessage());
        }
        Integer regionLevel = settleShopRuleRegionParam.getRegionLevel();
        if(regionLevel !=null && regionLevel == 0){
            Integer cooperationMode = settleShopRuleRegionParam.getCooperationMode();
            Integer insuranceCompanyId = settleShopRuleRegionParam.getInsuranceCompanyId();
            if(cooperationMode == null || insuranceCompanyId == null){
                throw new BusinessCheckException("001","参数异常");
            }
            Integer settleShopRuleId = settleShopRuleRegionParam.getSettleRuleId();
            List<Integer> listIds = settleShopRuleBiz.getRuleIdListByCooperationMode(cooperationMode,insuranceCompanyId);
            List<Integer> newList = Lists.newArrayList();
            if(settleShopRuleId != null){
                for (Integer id:listIds) {
                    if(settleShopRuleId.equals(id)){
                        continue;
                    }
                    newList.add(id);
                }
            }else{
                newList.addAll(listIds);
            }
            List<InsuranceRegionDTO> regionDTOs = Lists.newArrayList();
            if(!CollectionUtils.isEmpty(newList)){
                List<String> list = settleShopRuleRegionConfigDOMapper.selectAvailableRegionList(regionParentCode,newList);
                for (InsuranceRegionDTO insurance:result.getData()) {
                    boolean findFlag = false;
                    for (String regionCode:list) {
                        if(regionCode.equals(insurance.getRegionCode())){
                            findFlag = true;
                            break;
                        }
                    }
                    if(!findFlag){
                        regionDTOs.add(insurance);
                    }
                }
            }else{
                regionDTOs.addAll(result.getData());
            }

            return Result.wrapSuccessfulResult(regionDTOs);
        }

        return result;
    }

    private Boolean doCalculateInsuredRoyaltyFee(InsuranceSettleBasicMsg basicMsg) {
        List<InsuranceCompanyBO> companyBOList = insuranceBiz.getAllCompanyList();
        Integer insuranceCompanyId = basicMsg.getInsuranceCompanyId();
        String insuranceCompanyName = "";
        for (InsuranceCompanyBO companyBO : companyBOList) {
            if (companyBO.getId().equals(insuranceCompanyId)) {
                insuranceCompanyName = companyBO.getCompanyName();
                break;
            }
        }
        List<String> formSnList = new ArrayList<>();
        for (InsuranceSettleFormMsg formMsg : basicMsg.getFormMsgList()) {
            formSnList.add(formMsg.getInsuredFormNo());
        }
        //去重操作
        List<SettleCompanyCheckDetailDO> list =
                settleCompanyCheckDetailDOMapper.selectBySettleFormSnList(formSnList, insuranceCompanyId);
        if (!CollectionUtils.isEmpty(list)) {
            log.info("根据formSnList{},获取的SettleCompanyCheckDetailDO已存在", JsonUtil.objectToStr(formSnList));
            return Boolean.TRUE;
        }
        Integer scenarioTypeId = getScenarioType(basicMsg);
        if (scenarioTypeId == 0) {
            throw new BusinessCheckException("", "获取投保场景出错");
        }
        String operator = "";//shiroBiz.getCurrentUserRealName();
        List<String> cityCodeList = new ArrayList<>();
        cityCodeList.add(basicMsg.getInsuredCityCode());
        List<SettleInsuranceCompanyRuleDO> companyRuleDOList = settleInsuranceCompanyRuleDOMapper.selectByCityCods(cityCodeList);
        if (companyRuleDOList.size() > 1) {
            throw new BusinessCheckException("", "根据cityCode=" + basicMsg.getInsuredCityCode() + "获取的投保城市不唯一");
        }
        BigDecimal royaltyFee = BigDecimal.ZERO;
        List<SettleCompanyCheckDetailDO> companyCheckDetailDOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(companyRuleDOList)) {
            for (InsuranceSettleFormMsg formMsg : basicMsg.getFormMsgList()) {
                SettleCompanyCheckDetailDO settleCompanyCheckDetailDO = new SettleCompanyCheckDetailDO();
                settleCompanyCheckDetailDO.setInsuranceCompanyId(basicMsg.getInsuranceCompanyId());
                settleCompanyCheckDetailDO.setInsuranceCompanyName(insuranceCompanyName);
                settleCompanyCheckDetailDO.setScenarioTypeId(scenarioTypeId);
                settleCompanyCheckDetailDO.setCreator(operator);
                //计算交强险的保费分成,当投保城市不存在的时候默认的分成比例是0
                royaltyFee = formMsg.getInsuredFee().multiply(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
                settleCompanyCheckDetailDO.setInsuredRoyaltyFee(royaltyFee);
                settleCompanyCheckDetailDO.setInsuredRoyaltyRate(BigDecimal.ZERO.setScale(2));
                settleCompanyCheckDetailDO.setSettleFormSn(formMsg.getInsuredFormNo());
                companyCheckDetailDOList.add(settleCompanyCheckDetailDO);
            }
        } else {
            List<SettleInsuranceCompanyRuleItemDO> ruleItemDOList = settleInsuranceCompanyRuleItemDOMapper.selectRuleItemBySettleRuleIdAndScenarioType(companyRuleDOList.get(0).getId(), scenarioTypeId);
            for (SettleInsuranceCompanyRuleItemDO ruleItemDO : ruleItemDOList) {
                SettleCompanyCheckDetailDO settleCompanyCheckDetailDO = new SettleCompanyCheckDetailDO();
                settleCompanyCheckDetailDO.setInsuranceCompanyId(basicMsg.getInsuranceCompanyId());
                settleCompanyCheckDetailDO.setInsuranceCompanyName(insuranceCompanyName);
                settleCompanyCheckDetailDO.setScenarioTypeId(scenarioTypeId);
                settleCompanyCheckDetailDO.setCreator(operator);
                for (InsuranceSettleFormMsg formMsg : basicMsg.getFormMsgList()) {
                    //计算交强险的保费分成
                    if (InsuranceTypeEnum.FORCE_INSURANCE.getCode().equals(ruleItemDO.getInsuranceType()) && ruleItemDO.getInsuranceType().equals(formMsg.getInsuranceTypeId())) {
                        royaltyFee = formMsg.getInsuredFee().multiply(ruleItemDO.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        settleCompanyCheckDetailDO.setSettleFormSn(formMsg.getInsuredFormNo());
                    } else if (InsuranceTypeEnum.BIZ_INSURANCE.getCode().equals(ruleItemDO.getInsuranceType()) && ruleItemDO.getInsuranceType().equals(formMsg.getInsuranceTypeId())) {
                        royaltyFee = formMsg.getInsuredFee().multiply(ruleItemDO.getCommissionRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        settleCompanyCheckDetailDO.setSettleFormSn(formMsg.getInsuredFormNo());
                    }
                }
                settleCompanyCheckDetailDO.setInsuredRoyaltyFee(royaltyFee);
                settleCompanyCheckDetailDO.setInsuredRoyaltyRate(ruleItemDO.getCommissionRate());
                companyCheckDetailDOList.add(settleCompanyCheckDetailDO);
            }
        }
        return settleCompanyCheckDetailDOMapper.batchInsert(companyCheckDetailDOList) > 0;
    }

    //对投保场景和权重字典表做缓存处理
    private List<SettleScenarioInsuranceCategoryRelDO> getScenarioTypesByCompany(Integer insuranceCompanyId) {
        List<SettleScenarioInsuranceCategoryRelDO> list = redisClientTemplate.lazyGetList(SCENARIO_TYPE + insuranceCompanyId, SettleScenarioInsuranceCategoryRelDO.class);
        if (CollectionUtils.isEmpty(list)) {
            list = settleScenarioInsuranceCategoryRelDOMapper.getAllScenarioTypeByCompany(insuranceCompanyId);
            redisClientTemplate.lazySet(SCENARIO_TYPE + insuranceCompanyId, list, RedisKeyBean.RREDIS_EXP_DAY);
        }
        return list;
    }
}
