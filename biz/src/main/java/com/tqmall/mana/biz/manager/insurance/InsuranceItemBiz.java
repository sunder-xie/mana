package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemBriefBO;
import com.tqmall.mana.beans.param.settle.insurance.CheckServiceItemBOParam;

import java.util.List;

/**
 * Created by zhouheng on 17/2/20.
 */
public interface InsuranceItemBiz {

    /**
     * mana---新建服务项目配置(提交)
     *
     * @param serviceItemBO
     * @return
     */
    Result<Boolean> createServiceItemConfig(InsuranceServiceItemBO serviceItemBO);

    /**
     * mana---更新服务项目配置(保存)
     *
     * @param serviceItemBO
     * @return
     */
    Result<Boolean> updateServiceItemConfig(InsuranceServiceItemBO serviceItemBO);

    /**
     * mana---获取服务项配置列表(支持服务项目名称搜索)
     *
     * @param checkServiceItemBOParam
     * @return
     */
    PagingResult<InsuranceServiceItemBO> getServiceItemList(CheckServiceItemBOParam checkServiceItemBOParam);

    /**
     * mana---根据服务项id获取服务项目配置信息(编辑跳转)
     *
     * @param id
     * @return
     */
    Result<InsuranceServiceItemBO> getServiceItemConfig(Integer id);

    /**
     * mana---根据服务项id对服务项状态修改(下架和上架)
     *
     * @param id
     * @return
     */
    Result<Boolean> updateServiceItemStatus(Integer id,Integer status);

    /**
     * mana---根据服务项id删除服务项(删除)
     *
     * @param id
     * @return
     */
    Result<Boolean> deleteServiceItem(Integer id);

    /**
     * mana --查询服务项目列表
     * @Param itemName
     * @return
     */
    Result<List<InsuranceServiceItemBriefBO>> getServiceItemNameList(String itemName);

}
