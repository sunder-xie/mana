package com.tqmall.mana.external.dubbo.insurance;


import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.CheckServiceItemParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.InsuranceServiceItemParam;
import com.tqmall.insurance.domain.result.InsuranceServiceItemDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouheng on 17/2/20.
 */
public interface ExtInsuranceItemService {

    /**
     * mana---新建服务项目配置(提交)
     *
     * @param serviceItemParam
     * @return
     */
    Result<Boolean> createServiceItemConfig(InsuranceServiceItemParam serviceItemParam);

    /**
     * mana---更新服务项目配置(保存)
     *
     * @param serviceItemParam
     * @return
     */
    Result<Boolean> updateServiceItemConfig(InsuranceServiceItemParam serviceItemParam);

    /**
     * mana---获取服务项配置列表(支持服务项目名称搜索)
     *
     * @param checkServiceItemParam
     * @return
     */
    Result<PageEntityDTO<InsuranceServiceItemDTO>> getServiceItemList(CheckServiceItemParam checkServiceItemParam);

    /**
     * mana---根据服务项id获取服务项目配置信息(编辑跳转)
     *
     * @param id
     * @return
     */
    Result<InsuranceServiceItemDTO> getServiceItemConfig(Integer id);

    /**
     * mana---根据服务项id对服务项状态修改(下架和上架)
     *
     * @param id
     * @return
     */
    Result<Boolean> updateServiceItemStatus(Integer id,Integer status);

    /**
     * mana---根据服务项id获取服务包名称(删除)
     * @param id
     * @return
     */
    Result<List<String>> getServicePackageNameByItemId(Integer id);

    /**
     * mana---根据服务项id删除服务项(删除)
     *
     * @param id
     * @return
     */
    Result<Boolean> deleteServiceItem(Integer id);

    /**
     * mana---获取服务项列表
     * @return
     */
    Result<Map<Integer,String>> getServiceItemNameList(String itemName);


}
