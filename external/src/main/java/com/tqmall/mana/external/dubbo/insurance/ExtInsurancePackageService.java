package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.servicepackage.CheckServicePackageParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.ServicePackageParam;
import com.tqmall.insurance.domain.result.InsuranceServicePackageDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;

/**
 * Created by zhouheng on 17/2/24.
 */
public interface ExtInsurancePackageService {

    /**
     * mana---创建服务包配置(提交)
     *
     * @param servicePackageParam
     * @return
     */
    Result<Boolean> createServicePackage(ServicePackageParam servicePackageParam);

    /**
     * mana---更新服务包配置(保存)
     *
     * @param servicePackageParam
     * @return
     */
    Result<Boolean> updateServicePackageConfig(ServicePackageParam servicePackageParam);

    /**
     * mana---获取服务项配置列表(支持服务项目名称搜索)
     *
     * @param checkServicePackageParam
     * @return
     */
    Result<PageEntityDTO<InsuranceServicePackageDTO>> getServicePackageList(CheckServicePackageParam checkServicePackageParam);

    /**
     * mana---根据服务包id获取服务包配置(编辑跳转)
     *
     * @param id
     * @return
     */
    Result<InsuranceServicePackageDTO> getServicePackageConfig(Integer id);

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
