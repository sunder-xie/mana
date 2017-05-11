package com.tqmall.mana.biz.manager.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.*;
import com.tqmall.mana.beans.entity.customer.ManaCustomerDO;
import com.tqmall.mana.beans.param.CommonVOPageParam;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/14.
 */
public interface CustomerBiz {

    /**
     * 新增客户
     *
     * @param addCustomerBO
     * @return
     */
    Result addCustomer(AddCustomerBO addCustomerBO);

    /**
     * 编辑客户,地址及车辆信息
     *
     * @param
     * @return
     */
    Result updateCustomerAndVehicle(UpdateCustomerInfoBO updateCustomerInfoBO);

    /**
     * 新增车辆
     *
     * @param addCustomerBO
     * @return
     */
    Result addCustomerVehicle(AddCustomerBO addCustomerBO);

    /**
     * 按照传入的手机号.姓名或车牌号查询客户相关信息
     *
     * @param queryStr
     * @return
     */
    PagingResult<CustomerVehiclePageBO> searchCustomer(String queryStr, Integer pageSize, Integer pageNumber);

    /**
     * 通过页面参数获取客户信息列表
     *
     * @param commonVOPageParam
     * @return
     */
    PagingResult<CustomerVehiclePageBO> searchCustomerByParam(CommonVOPageParam commonVOPageParam, Integer pageSize, Integer pageNumber);

    /**
     * 通过车辆id获取客户详细信息
     *
     * @param vehicleId
     * @return
     */
    Result<SearchCustomerBO> searchCustomerDetailById(Integer vehicleId);

    /**
     * 客户业务信息列表分页
     *
     * @param commonVOPageParam
     * @return
     */
    PagingResult<CustomerPageBO> searchCustomerFormPagingResult(CommonVOPageParam commonVOPageParam);

    /**
     * 通过客户id查询客户地址信息
     *
     * @param customerId
     * @return
     */
    Result<List<SearchCustomerAddressBO>> searchCustomerAddressList(Integer customerId);

    /**
     * 根据主键id查询客户信息
     *
     * @param id
     * @return
     */
    Result<ManaCustomerDO> getCustomerById(Integer id);

    /**
     * 修改客户地址
     *
     * @param addressBO
     * @return
     */
    Result modifyAddress(UpdateCustomerAddressBO addressBO);

    /**
     * 根据主键id查询客户信息，包含地址
     *
     * @param id
     * @return
     */
    Result<CustomerInfoBO> getCustomerInfoById(Integer id);

    /**
     * 查询客户地址
     *
     * @param customerId
     * @return
     */
    Result<CustomerAddressBO> getAddressByCustomerId(Integer customerId);

}
