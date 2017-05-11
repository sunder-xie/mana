package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.CheckServiceItemParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.InsuranceServiceItemParam;
import com.tqmall.insurance.domain.result.InsuranceServiceItemDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.RpcServiceItemService;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouheng on 17/2/20.
 */
@Slf4j
@Service
public class ExtInsuranceItemServiceImpl implements ExtInsuranceItemService {

    @Autowired
    private RpcServiceItemService rpcServiceItemService;

    @Override
    public Result<Boolean> createServiceItemConfig(InsuranceServiceItemParam serviceItemParam) {
        log.info("createServiceItemConfig , param:{}", JsonUtil.objectToStr(serviceItemParam));
        return rpcServiceItemService.createServiceItemConfig(serviceItemParam);
    }

    @Override
    public Result<Boolean> updateServiceItemConfig(InsuranceServiceItemParam serviceItemParam) {
        log.info("updateServiceItemConfig , param:{}", JsonUtil.objectToStr(serviceItemParam));
        return rpcServiceItemService.updateServiceItemConfig(serviceItemParam);
    }

    @Override
    public Result<PageEntityDTO<InsuranceServiceItemDTO>> getServiceItemList(CheckServiceItemParam checkServiceItemParam){
        log.info("getServiceItemList ,param:{}",JsonUtil.objectToStr(checkServiceItemParam));
        return rpcServiceItemService.getServiceItemList(checkServiceItemParam);
    }

    @Override
    public Result<InsuranceServiceItemDTO> getServiceItemConfig(Integer id) {
        log.info("getServiceItemConfig, id:{}",id);
        if(id == null){
            log.info("getServiceItemConfig param error");
            return ResultUtil.errorResult("001","参数异常");
        }
        return rpcServiceItemService.getServiceItemConfig(id);
    }

    @Override
    public Result<Boolean> updateServiceItemStatus(Integer id,Integer status) {
        log.info("updateServiceItemStatus, id:{}",id);
        if(id == null || status == null){
            log.info("updateServiceItemStatus param error");
            return ResultUtil.errorResult("001","参数异常");
        }
        return rpcServiceItemService.updateServiceItemStatus(id,status);
    }

    @Override
    public Result<List<String>> getServicePackageNameByItemId(Integer id) {
        log.info("getServicePackageNameByItemId, id:{}",id);
        if(id == null ){
            log.info("getServicePackageNameByItemId param error");
            return ResultUtil.errorResult("001","参数异常");
        }
        return rpcServiceItemService.getServicePackageNameByItemId(id);
    }

    @Override
    public Result<Boolean> deleteServiceItem(Integer id) {
        log.info("deleteServiceItem, id:{}",id);
        if(id == null){
            log.info("deleteServiceItem param error");
            return ResultUtil.errorResult("001","参数异常");
        }
        return rpcServiceItemService.deleteServiceItem(id);
    }

    @Override
    public Result<Map<Integer,String>> getServiceItemNameList(String itemName) {
        log.info("getServiceItemNameList param:{}",itemName);
        return rpcServiceItemService.getServiceItemNameList(itemName);
    }
}
