package com.tqmall.mana.biz.manager.coupon;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.service.insurance.RpcInsuranceRegionService;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponGoodsConfigBO;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRegionConfigBO;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRuleConfigBO;
import com.tqmall.mana.beans.entity.cashcoupon.CashCouponGoodsConfigDO;
import com.tqmall.mana.beans.entity.cashcoupon.CashCouponRegionConfigDO;
import com.tqmall.mana.beans.entity.cashcoupon.CashCouponRuleConfigDO;
import com.tqmall.mana.beans.param.settle.CashCouponConfigSearchParam;
import com.tqmall.mana.beans.param.settle.CashCouponGoodsConfigPO;
import com.tqmall.mana.beans.param.settle.CashCouponRegionConfigPO;
import com.tqmall.mana.beans.param.settle.CashCouponRuleConfigPO;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.dao.mapper.cashcoupon.CashCouponGoodsConfigDOMapper;
import com.tqmall.mana.dao.mapper.cashcoupon.CashCouponRegionConfigDOMapper;
import com.tqmall.mana.dao.mapper.cashcoupon.CashCouponRuleConfigDOMapper;
import com.tqmall.mana.external.dubbo.stall.ExtRegionService;
import com.tqmall.tqmallstall.domain.result.RegionDTO;
import com.tqmall.tqmallstall.domain.result.RegionListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhanghong on 17/4/10.
 */
@Service
@Slf4j
public class CashCouponRuleConfigBizImpl implements CashCouponRuleConfigBiz{

    @Autowired
    private CashCouponRuleConfigDOMapper ruleConfigDOMapper;
    @Autowired
    private CashCouponRegionConfigDOMapper regionConfigDOMapper;
    @Autowired
    private CashCouponGoodsConfigDOMapper goodsConfigDOMapper;
    @Autowired
    private ExtRegionService extRegionService;
    @Autowired
    private RpcInsuranceRegionService insuranceRegionService;
    @Autowired
    private ShiroBiz shiroBiz;

    @Override
    @Transactional
    public boolean createCashCouponRuleConfig(CashCouponRuleConfigPO ruleConfigBizParam) {
        checkParam(ruleConfigBizParam);
        String operator = shiroBiz.getCurrentUserRealName();
        CashCouponRuleConfigDO ruleConfigDO= BdUtil.do2bo(ruleConfigBizParam,CashCouponRuleConfigDO.class);
        ruleConfigDO.setGmtCreate(new Date());
        ruleConfigDO.setIsDeleted("N");
        ruleConfigDO.setGmtModified(new Date());
        ruleConfigDO.setCreator(operator);
        ruleConfigDO.setModifier(operator);
        ruleConfigDOMapper.insertSelective(ruleConfigDO);
        Integer ruleConfigId=ruleConfigDO.getId();
        List<CashCouponRegionConfigDO> regionConfigDOs=BdUtil.do2bo4List(ruleConfigBizParam.getRegionConfigBizParams(), CashCouponRegionConfigDO.class);
        List<String> hasConfigRegion= Lists.newArrayList();
        for(CashCouponRegionConfigDO regionConfigDO:regionConfigDOs){
            //对城市站做幂等
            CashCouponRegionConfigDO couponRegionConfigDO=regionConfigDOMapper.selectByCityCode(regionConfigDO.getCityCode());
            if(couponRegionConfigDO!=null){
                hasConfigRegion.add(couponRegionConfigDO.getCityName());
            }
            regionConfigDO.setGmtCreate(new Date());
            regionConfigDO.setRuleConfigId(ruleConfigId);
            regionConfigDO.setIsDeleted("N");
            regionConfigDO.setGmtModified(new Date());
            regionConfigDO.setCreator(operator);
            regionConfigDO.setModifier(operator);
            regionConfigDOMapper.insertSelective(regionConfigDO);
        }
        if(!CollectionUtils.isEmpty(hasConfigRegion)){
            log.error("该城市站的现金券配置规则已经配置,城市站cityName={}",hasConfigRegion);
            throw new BusinessException("该城市站的现金券配置规则已经配置,请联系相应技术人员");
        }
        List<CashCouponGoodsConfigDO> goodsConfigDOs=BdUtil.do2bo4List(ruleConfigBizParam.getGoodsConfigBizParams(),CashCouponGoodsConfigDO.class);
        for(CashCouponGoodsConfigDO goodsConfigDO:goodsConfigDOs){
            goodsConfigDO.setGmtCreate(new Date());
            goodsConfigDO.setRuleConfigId(ruleConfigId);
            goodsConfigDO.setIsDeleted("N");
            goodsConfigDO.setGmtModified(new Date());
            goodsConfigDO.setCreator(operator);
            goodsConfigDO.setModifier(operator);
            goodsConfigDOMapper.insertSelective(goodsConfigDO);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateCashCouponRuleConfig(CashCouponRuleConfigPO ruleConfigBizParam) {
        CashCouponRuleConfigDO ruleConfigDO= BdUtil.do2bo(ruleConfigBizParam,CashCouponRuleConfigDO.class);
        String operator = shiroBiz.getCurrentUserRealName();
        ruleConfigDO.setGmtModified(new Date());
        ruleConfigDO.setModifier(operator);
        ruleConfigDO.setIsDeleted("N");
        ruleConfigDOMapper.updateByPrimaryKeySelective(ruleConfigDO);
        Integer ruleConfigId=ruleConfigDO.getId();

        goodsConfigDOMapper.deleteByRuleId(ruleConfigId);
        List<CashCouponGoodsConfigDO> goodsConfigDOs=BdUtil.do2bo4List(ruleConfigBizParam.getGoodsConfigBizParams(),CashCouponGoodsConfigDO.class);
        for(CashCouponGoodsConfigDO goodsConfigDO:goodsConfigDOs){
            goodsConfigDO.setGmtCreate(new Date());
            goodsConfigDO.setRuleConfigId(ruleConfigId);
            ruleConfigDO.setModifier(operator);
            ruleConfigDO.setIsDeleted("N");
            goodsConfigDOMapper.insertSelective(goodsConfigDO);
        }
        return true;
    }

    @Override
    public Result<List<RegionListDTO>> getAllOpenedRegionList() {
        log.info("调用stall接口开始,获取区域列表");
        List<RegionListDTO> regionListResult = extRegionService.getAllOpenedRegionList();
        log.info("调用stall接口结束,获取区域列表");
        List<String> cityCodes=regionConfigDOMapper.selectCityCode();
        List<RegionListDTO> oldRegionResult=regionListResult;
        List<RegionListDTO> newRegionResult= Lists.newArrayList();
        for(RegionListDTO regionListDTO:oldRegionResult){
            log.info("调用insurance接口开始,该区域是否开通保险业务(主要对省),id={}",regionListDTO.getParentRegion().getId());
            Result<Boolean> result=insuranceRegionService.isInsuranceAvaiableInRegion(regionListDTO.getParentRegion().getId());
            log.info("调用insurance接口结束,result={}", result.getData());
            if(result.getData().compareTo(Boolean.FALSE)==0){
                continue;
            }
            List<RegionDTO> oldSubRegionList=regionListDTO.getSubRegionList();
            List<RegionDTO> newSubRegionList=Lists.newArrayList();
            for(RegionDTO regionDTO:oldSubRegionList){
                if(!cityCodes.contains(regionDTO.getId().toString())){
                    newSubRegionList.add(regionDTO);
                }
            }
            RegionListDTO newRegionDTO=new RegionListDTO();
            RegionDTO parentRegion=BdUtil.do2bo(regionListDTO.getParentRegion(), RegionDTO.class);
            if(CollectionUtils.isEmpty(newSubRegionList)){
                continue;
            }
            newRegionDTO.setParentRegion(parentRegion);
            newRegionDTO.setSubRegionList(newSubRegionList);
            newRegionResult.add(newRegionDTO);
        }
        return Result.wrapSuccessfulResult(newRegionResult);
    }


    @Override
    public PagingResult<CashCouponRegionConfigBO> getRegionConfigPageList(CashCouponConfigSearchParam searchParam) {
        if(searchParam == null ){
            throw new BusinessCheckException("001","参数异常");
        }
        Map<String,Object> map = obj2Map(searchParam);

        Integer count = regionConfigDOMapper.selectRegionConfigCount(map);
        if(count <= 0){
            List<CashCouponRegionConfigBO> list = Lists.newArrayList();
            return PagingResult.wrapSuccessfulResult(list,0);
        }
        List<CashCouponRegionConfigDO> configDOs = regionConfigDOMapper.selectRegionConfigPageList(map);
        if(CollectionUtils.isEmpty(configDOs)){
            throw new BusinessException("暂无查询数据");
        }

        List<CashCouponRuleConfigDO> ruleConfigDOs = batchQueryConfigDOList(configDOs);
        for (CashCouponRegionConfigDO configDO:configDOs) {
            for (CashCouponRuleConfigDO ruleConfigDO:ruleConfigDOs) {
                if(configDO.getRuleConfigId().equals(ruleConfigDO.getId())){
                    configDO.setGmtModified(ruleConfigDO.getGmtModified());
                    configDO.setModifier(ruleConfigDO.getModifier());
                    break;
                }
            }
        }

        List<CashCouponRegionConfigBO> list = BdUtil.do2bo4List(configDOs,CashCouponRegionConfigBO.class);
        return PagingResult.wrapSuccessfulResult(list,count);
    }

    private List<CashCouponRuleConfigDO> batchQueryConfigDOList(List<CashCouponRegionConfigDO> configDOs){
        if(CollectionUtils.isEmpty(configDOs)){
            throw new BusinessCheckException("001","参数异常");
        }
        Set<Integer> ruleConfigIds = Sets.newHashSet();
        for (CashCouponRegionConfigDO regionConfigDO:configDOs) {
            ruleConfigIds.add(regionConfigDO.getRuleConfigId());
        }
        List<CashCouponRuleConfigDO> ruleConfigDOs = ruleConfigDOMapper.batchSelectConfigList(ruleConfigIds);
        if(CollectionUtils.isEmpty(configDOs)){
            throw new BusinessException("内部数据异常");
        }

        return ruleConfigDOs;
    }

    @Override
    public CashCouponRuleConfigBO getConfigInfoByCityCode(String cityCode) {
        if(StringUtils.isEmpty(cityCode)){
            throw null;
        }
        CashCouponRegionConfigDO couponRegionConfigDO = regionConfigDOMapper.selectByCityCode(cityCode);
        if(couponRegionConfigDO== null){
            return null;
        }
        Integer ruleConfigId = couponRegionConfigDO.getRuleConfigId();
        if(ruleConfigId == null){
            return null;
        }
        CashCouponRuleConfigDO configDO = ruleConfigDOMapper.selectByPrimaryKey(ruleConfigId);
        if(configDO == null){
            return null;
        }

        return BdUtil.do2bo(configDO, CashCouponRuleConfigBO.class);
    }


    @Override
    public CashCouponRuleConfigBO getCreateRuleConfigInfo(String cityCode) {
        if(StringUtils.isEmpty(cityCode)){
            throw null;
        }
        CashCouponRegionConfigDO couponRegionConfigDO = regionConfigDOMapper.selectByCityCode(cityCode);
        if(couponRegionConfigDO== null){
            return null;
        }
        Integer ruleConfigId=couponRegionConfigDO.getRuleConfigId();
        CashCouponRuleConfigDO cashCouponRuleConfigDO=ruleConfigDOMapper.selectByPrimaryKey(ruleConfigId);
        if(cashCouponRuleConfigDO==null){
            return null;
        }
        CashCouponRuleConfigBO ruleConfigBO=BdUtil.do2bo(cashCouponRuleConfigDO,CashCouponRuleConfigBO.class);
        List<CashCouponGoodsConfigDO> goodsConfigDOs=goodsConfigDOMapper.selectByRuleId(ruleConfigId);
        if(CollectionUtils.isEmpty(goodsConfigDOs)){
            return null;
        }
        List<CashCouponGoodsConfigBO> goodsConfigBOs=BdUtil.do2bo4List(goodsConfigDOs,CashCouponGoodsConfigBO.class);
        ruleConfigBO.setGoodsConfigBOList(goodsConfigBOs);
        return ruleConfigBO;
    }

    @Override
    public CashCouponRuleConfigPO getConfigInfoById(Integer id) {
        if(id == null){
            throw new BusinessCheckException("001","参数异常");
        }
        CashCouponRegionConfigDO regionConfigDO = regionConfigDOMapper.selectByPrimaryKey(id);
        if(regionConfigDO == null){
            throw new BusinessException("暂无查询数据");
        }
        Integer ruleConfigId = regionConfigDO.getRuleConfigId();
        CashCouponRuleConfigDO ruleConfigDO = ruleConfigDOMapper.selectByPrimaryKey(ruleConfigId);
        if(ruleConfigDO == null){
            throw new BusinessException("内部数据异常");
        }
        List<CashCouponGoodsConfigDO> goodsConfigDOs=goodsConfigDOMapper.selectByRuleId(ruleConfigId);
        if(CollectionUtils.isEmpty(goodsConfigDOs)){
            throw new BusinessException("内部数据异常");
        }
        List<CashCouponRegionConfigDO> regionConfigDOs = regionConfigDOMapper.selectRegionConfigByRuleConfigId(ruleConfigId);
        if(CollectionUtils.isEmpty(regionConfigDOs)){
            throw new BusinessException("暂无查询数据");
        }
        CashCouponRuleConfigPO configPO = BdUtil.do2bo(ruleConfigDO,CashCouponRuleConfigPO.class);
        List<CashCouponRegionConfigPO> regionConfigBOs = BdUtil.do2bo4List(regionConfigDOs,CashCouponRegionConfigPO.class);
        List<CashCouponGoodsConfigPO> goodsConfigBOs = BdUtil.do2bo4List(goodsConfigDOs,CashCouponGoodsConfigPO.class);
        configPO.setRegionConfigBizParams(regionConfigBOs);
        configPO.setGoodsConfigBizParams(goodsConfigBOs);
        return configPO;
    }

    @Override
    @Transactional
    public void deleteConfigInfo(Integer regionConfigId) {
        if(regionConfigId == null){
            throw new BusinessCheckException("001","参数异常");
        }
        CashCouponRegionConfigDO regionConfigDO = regionConfigDOMapper.selectByPrimaryKey(regionConfigId);
        if(regionConfigDO == null){
            throw new BusinessException("待删除数据不存在");
        }
        Integer ruleConfigId = regionConfigDO.getRuleConfigId();
        if(ruleConfigId == null){
            throw new BusinessException("内部数据异常");
        }
        List<CashCouponRegionConfigDO> regionConfigDOs = regionConfigDOMapper.selectRegionConfigByRuleConfigId(ruleConfigId);
        if(regionConfigDOs.size() == 1){
            /**删除配置商品信息*/
            deletedGoodsInfoById(ruleConfigId);
            /**删除配置信息*/
            deletedRuleConfigInfo(ruleConfigId);
        }
        /**删除配置区域信息*/
        deletedRegionInfoById(regionConfigId);

    }

    private void deletedRuleConfigInfo(Integer ruleConfigId){
        CashCouponRuleConfigDO configDO = new CashCouponRuleConfigDO();
        configDO.setId(ruleConfigId);
        configDO.setGmtModified(new Date());
        configDO.setModifier(shiroBiz.getCurrentUserRealName());
        configDO.setIsDeleted("Y");
        ruleConfigDOMapper.updateByPrimaryKeySelective(configDO);
    }

    private void deletedGoodsInfoById(Integer ruleConfigId){
        CashCouponGoodsConfigDO goodsConfigDO = new CashCouponGoodsConfigDO();
        goodsConfigDO.setRuleConfigId(ruleConfigId);
        goodsConfigDO.setGmtModified(new Date());
        goodsConfigDO.setModifier(shiroBiz.getCurrentUserRealName());
        goodsConfigDO.setIsDeleted("Y");
        goodsConfigDOMapper.updateByRuleConfigId(goodsConfigDO);
    }

    private void deletedRegionInfoById(Integer regionConfigId){
        CashCouponRegionConfigDO regionConfigDO = new CashCouponRegionConfigDO();
        regionConfigDO.setId(regionConfigId);
        regionConfigDO.setGmtModified(new Date());
        regionConfigDO.setModifier(shiroBiz.getCurrentUserRealName());
        regionConfigDO.setIsDeleted("Y");
        regionConfigDOMapper.updateByPrimaryKeySelective(regionConfigDO);
    }

    private Map<String,Object> obj2Map(CashCouponConfigSearchParam searchParam){
        Map<String,Object> map = Maps.newHashMap();
        map.put("provinceName",searchParam.getProvinceName());
        map.put("cityName",searchParam.getCityName());
        Integer pageSize = searchParam.getPageSize();
        if(pageSize == null ||pageSize < 1){
            pageSize = 10;
        }
        Integer pageNumber = searchParam.getPageNumber();
        if(pageNumber == null || pageNumber < 1){
            pageNumber = 1;
        }
        Integer offSet = (pageNumber - 1)*pageSize;
        map.put("offSet",offSet);
        map.put("pageSize",pageSize);
        return map;
    }


    private void checkParam(CashCouponRuleConfigPO ruleConfigBizParam) {
        if(ruleConfigBizParam.getOnlineCommercialMinfee()==null
                || ruleConfigBizParam.getOnlineCooperationMode()==null
                || ruleConfigBizParam.getOnlineForcibleMinfee()==null
                || ruleConfigBizParam.getOnlineFormValidateDays()==null){
            throw new BusinessException("系统出单的保单入参为null");
        }
        if(ruleConfigBizParam.getOfflineCooperationMode()==null
                ||ruleConfigBizParam.getOfflineCommercialMinfee()==null
                ||ruleConfigBizParam.getOfflineForcibleMinfee()==null
                ||ruleConfigBizParam.getOfflineFormValidateDays()==null){
            throw new BusinessException("门店补录的保单入参为null");
        }
        if(ruleConfigBizParam.getCommercialRebateRatio()==null
                ||ruleConfigBizParam.getForcibleRebateRatio()==null){
            throw new BusinessException("用券后返点比例入参为null");
        }
        if(ruleConfigBizParam.getFaceAmount()==null
                ||ruleConfigBizParam.getCashCouponValidateDays()==null
                ||ruleConfigBizParam.getRuleStatus()==null){
            throw new BusinessException("现金券生成入参为null");
        }
        if(CollectionUtils.isEmpty(ruleConfigBizParam.getGoodsConfigBizParams())){
            throw new BusinessException("商品配置入参为null");
        }
        if(CollectionUtils.isEmpty(ruleConfigBizParam.getRegionConfigBizParams())){
            throw new BusinessException("区域配置入参为null");
        }
    }
}
