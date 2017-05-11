package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.mana.beans.BO.insurance.ServicePackageBO;
import com.tqmall.mana.beans.param.settle.insurance.CheckServicePackageBOParam;

/**
 * Created by zhouheng on 17/2/24.
 */
public interface InsurancePackageBiz {

    /**
     * mana---创建服务包配置(提交)
     *
     * @param servicePackageBO
     * @return
     */
    Result<Boolean> createServicePackage(ServicePackageBO servicePackageBO);

    /**
     * mana---更新服务包配置(保存)
     *
     * @param servicePackageBO
     * @return
     */
    Result<Boolean> updateServicePackageConfig(ServicePackageBO servicePackageBO);

    /**
     * mana---获取服务项配置列表(支持服务项目名称搜索)
     *
     * @param checkServicePackageBOParam
     * @return
     */
    PagingResult<ServicePackageBO> getServicePackageList(CheckServicePackageBOParam checkServicePackageBOParam);

    /**
     * mana---根据服务包id获取服务包配置(编辑跳转)
     *
     * @param id
     * @return
     */
    Result<ServicePackageBO> getServicePackageConfig(Integer id);

    /**
     * mana---根据服务包id对服务包状态修改(下架和上架)
     *
     * @param id
     * @param packageStatus
     * @return
     */
    Result<Boolean> updateServicePackageStatus(Integer id, Integer packageStatus);

    /**
     * mana---根据服务包id删除服务包(删除)
     *
     * @param id
     * @return
     */
    Result<Boolean> deleteServicePackage(Integer id);


}
