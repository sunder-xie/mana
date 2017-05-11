package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.CheckServiceItemParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.InsuranceMaterialTemplateParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.InsuranceServiceItemParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.InsuranceServiceItemVideoParam;
import com.tqmall.insurance.domain.result.InsuranceMaterialTemplateDTO;
import com.tqmall.insurance.domain.result.InsuranceServiceItemDTO;
import com.tqmall.insurance.domain.result.InsuranceServiceItemVideoDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.mana.beans.BO.insurance.InsuranceMaterialTemplateBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemBriefBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemVideoBO;
import com.tqmall.mana.beans.param.settle.insurance.CheckServiceItemBOParam;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouheng on 17/2/20.
 */
@Slf4j
@Service
public class InsuranceItemBizImpl implements InsuranceItemBiz {

    @Autowired
    private ExtInsuranceItemService extInsuranceItemService;

    @Override
    public Result<Boolean> createServiceItemConfig(InsuranceServiceItemBO serviceItemBO) {

        InsuranceServiceItemParam insuranceServiceItemParam = convertBOtoParam(serviceItemBO);

        Result<Boolean> result = extInsuranceItemService.createServiceItemConfig(insuranceServiceItemParam);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;

    }

    @Override
    public Result<Boolean> updateServiceItemConfig(InsuranceServiceItemBO serviceItemBO) {

        InsuranceServiceItemParam insuranceServiceItemParam = convertBOtoParam(serviceItemBO);
        Result<Boolean> result = extInsuranceItemService.updateServiceItemConfig(insuranceServiceItemParam);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;

    }

    @Override
    public PagingResult<InsuranceServiceItemBO> getServiceItemList(CheckServiceItemBOParam checkServiceItemBOParam) {
        String itemName = checkServiceItemBOParam.getItemName();
        if("".equals(itemName)){
            checkServiceItemBOParam.setItemName(null);
        }
        CheckServiceItemParam checkServiceItemParam = BdUtil.do2bo(checkServiceItemBOParam,CheckServiceItemParam.class);

        Result<PageEntityDTO<InsuranceServiceItemDTO>> result = extInsuranceItemService.getServiceItemList(checkServiceItemParam);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        PageEntityDTO<InsuranceServiceItemDTO> pageEntityDTO = result.getData();
        List<InsuranceServiceItemDTO> serviceItemDTOList = pageEntityDTO.getRecordList();
        if(CollectionUtils.isEmpty(serviceItemDTOList)){
            throw new BusinessCheckException("001","暂无数据");
        }
        List<InsuranceServiceItemBO> serviceItemBOList = convertDTOtoBOList(serviceItemDTOList);
        int total = pageEntityDTO.getTotalNum();
        return PagingResult.wrapSuccessfulResult(serviceItemBOList, total);
    }

    @Override
    public Result<InsuranceServiceItemBO> getServiceItemConfig(Integer id) {
        if(id == null){
            throw new BusinessCheckException("000","参数异常");
        }
        Result<InsuranceServiceItemDTO> result = extInsuranceItemService.getServiceItemConfig(id);
        if(!result.isSuccess()){
            return ResultUtil.errorResult("001",result.getMessage());
        }
        InsuranceServiceItemDTO insuranceServiceItemDTO = result.getData();
        InsuranceServiceItemBO serviceItemBO = convertDTOtoBO(insuranceServiceItemDTO);
        return Result.wrapSuccessfulResult(serviceItemBO);
    }

    @Override
    public Result<Boolean> updateServiceItemStatus(Integer id,Integer status) {
        if(id == null){
            throw new BusinessCheckException("000"," 参数异常");
        }
        Result<Boolean> result = extInsuranceItemService.updateServiceItemStatus(id,status);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;
    }

    @Override
    public Result<Boolean> deleteServiceItem(Integer id) {
        if(id == null){
            throw new BusinessCheckException("000","参数异常");
        }

        Result<List<String>> resultString = extInsuranceItemService.getServicePackageNameByItemId(id);
        if(!resultString.isSuccess()){
            throw new BusinessException(resultString.getMessage());
        }
        List<String> list = resultString.getData();
        if (!CollectionUtils.isEmpty(list)){
            StringBuilder stringBuilder= new StringBuilder();
            for (String str:list) {
                stringBuilder.append(" "+str+" ");
            }
            throw new BusinessException("抱歉,该项目与服务包关联,无法删除,关联的服务包为: ["+stringBuilder+"]");
        }
        Result<Boolean> result = extInsuranceItemService.deleteServiceItem(id);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<InsuranceServiceItemBriefBO>> getServiceItemNameList(String itemName) {

        Result<Map<Integer,String>> result = extInsuranceItemService.getServiceItemNameList(itemName);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        if(result.getData()==null){
           throw new BusinessException("暂无查询数据");
        }
        Map<Integer,String> map = result.getData();
        List<InsuranceServiceItemBriefBO> list = new ArrayList<>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            InsuranceServiceItemBriefBO briefBO = new InsuranceServiceItemBriefBO();
            Integer key = (Integer) it.next();
            briefBO.setId(key);
            briefBO.setItemName(map.get(key));
            list.add(briefBO);
        }
        return Result.wrapSuccessfulResult(list);
    }

    /**
     * 第一级
     * 内部DTO转BOList
     *
     * @param serviceItemDTOList
     * @return
     */
    private List<InsuranceServiceItemBO> convertDTOtoBOList(List<InsuranceServiceItemDTO> serviceItemDTOList) {
        if(CollectionUtils.isEmpty(serviceItemDTOList)){
            throw new BusinessCheckException("002","数据异常");
        }
        List<InsuranceServiceItemBO> serviceItemBOList = new ArrayList<>();
        for (InsuranceServiceItemDTO serviceItemDTO : serviceItemDTOList) {
            InsuranceServiceItemBO serviceItemBO = convertDTOtoBO(serviceItemDTO);
            serviceItemBOList.add(serviceItemBO);
        }
        return serviceItemBOList;
    }

    /**
     * 第二级
     * 内部DTO转BO
     *
     * @param serviceItemDTO
     * @return
     */
    private InsuranceServiceItemBO convertDTOtoBO(InsuranceServiceItemDTO serviceItemDTO) {

        InsuranceServiceItemBO serviceItemBO = BdUtil.do2bo(serviceItemDTO, InsuranceServiceItemBO.class);

        List<InsuranceMaterialTemplateDTO> list = serviceItemDTO.getMaterialTemplateList();
        List<InsuranceMaterialTemplateBO> materialTemplateBOList = BdUtil.do2bo4List(list, InsuranceMaterialTemplateBO.class);

        List<InsuranceServiceItemVideoDTO> itemVideoDTOList = serviceItemDTO.getServiceItemVideoList();
        List<InsuranceServiceItemVideoBO> itemVideoBOList = BdUtil.do2bo4List(itemVideoDTOList, InsuranceServiceItemVideoBO.class);

        serviceItemBO.setMaterialTemplateParams(materialTemplateBOList);
        serviceItemBO.setServiceItemVideoParams(itemVideoBOList);

        return serviceItemBO;
    }

    /**
     * 内部BO转param
     *
     * @param serviceItemBO
     * @return
     */
    private InsuranceServiceItemParam convertBOtoParam(InsuranceServiceItemBO serviceItemBO) {

        InsuranceServiceItemParam serviceItemParam = BdUtil.do2bo(serviceItemBO, InsuranceServiceItemParam.class);

        List<InsuranceMaterialTemplateBO> list = serviceItemBO.getMaterialTemplateParams();
        List<InsuranceMaterialTemplateParam> materialTemplateParamList = BdUtil.do2bo4List(list, InsuranceMaterialTemplateParam.class);

        List<InsuranceServiceItemVideoBO> itemVideoBOList = serviceItemBO.getServiceItemVideoParams();
        List<InsuranceServiceItemVideoParam> itemVideoParams = BdUtil.do2bo4List(itemVideoBOList, InsuranceServiceItemVideoParam.class);

        serviceItemParam.setMaterialTemplateParams(materialTemplateParamList);
        serviceItemParam.setServiceItemVideoParams(itemVideoParams);

        return serviceItemParam;
    }

}
