package com.tqmall.mana.biz.manager.settle.settleCalculate;


import com.tqmall.mana.beans.entity.settle.SettleCarOwnerCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.SettleFormDO;
import com.tqmall.mana.beans.entity.settle.SettleServiceCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailDO;


/**
 *
 * 保险结算数据操作业务
 *
 * Created by huangzhangting on 17/1/22.
 */
public interface InsuranceSettleDataOperateBiz {
    /**
     * 插入服务包数据
     * @param checkDetail
     * @return 参考 SettleDataOperateEnum
     */
    Integer insertOrUpdateServiceCheckDetail(String insuranceOrderSn, SettleServiceCheckDetailDO checkDetail, Integer shopId);

    /**
     * 插入保单数据
     * @param settleForm
     * @return 参考 SettleDataOperateEnum
     */
    Integer insertOrUpdateSettleForm(String insuranceOrderSn, SettleFormDO settleForm);

    /**
     * 插入门店对账数据
     * @param checkDetail
     * @return 参考 SettleDataOperateEnum
     */
    Integer insertOrUpdateShopCheckDetail(String insuranceOrderSn, SettleShopCheckDetailDO checkDetail);

    /**
     * 修改门店对账数据
     * @param checkDetail
     * @return
     */
    Integer updateShopCheckDetail(SettleShopCheckDetailDO checkDetail);

    /**
     * 插入车主对账数据
     * @param checkDetail
     * @return 参考 SettleDataOperateEnum
     */
    Integer insertOrUpdateCarOwnerCheckDetail(String insuranceOrderSn, SettleCarOwnerCheckDetailDO checkDetail);


    /**
     * （因为erp那边商业险+服务包返利需要合并成一条展示）
     * 如果是买服务包送保险模式，并且买了商业险，更新关联的服务包返利记录erp_flag=0，
     * 并且将商业险的操作信息同步，跟服务包返利记录的操作信息一致
     * */
    void updateServicePackageRebate(SettleShopCheckDetailDO shopCheckDetailDO);


    /** 门店结算基础信息表，统计数据 */
//    void settleShopBaseForPackage(SettleShopCheckDetailDO checkDetailDO, Integer packageStatus);
//    void settleShopBaseForForm(List<SettleShopCheckDetailDO> checkDetailDOList);


    /** 获取保险公司名称 */
    String getInsuranceCompanyName(Integer companyId);
    /** 获取保险模式名称 */
    String getCooperationModeName(Integer cooperationModeId);
    /** 获取门店账号 */
    String getAccountMobile(Integer shopId);
    /**
     * 查询门店标签
     * @param shopId 门店id
     * @return 1：云修店 2：档口店
     */
    Integer getAgentTag(Integer shopId);

}
