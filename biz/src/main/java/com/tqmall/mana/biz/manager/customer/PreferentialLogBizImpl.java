package com.tqmall.mana.biz.manager.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.AddPreferentialLogBO;
import com.tqmall.mana.beans.BO.customer.AddPreferentialVO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponTypeDO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerPreferentialLogDO;
import com.tqmall.mana.beans.param.PreferentialPageParam;
import com.tqmall.mana.beans.param.PreferentialVOPageParam;
import com.tqmall.mana.biz.manager.coupon.CouponBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerPreferentialLogDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 17/1/3.
 */
@Slf4j
@Service
public class PreferentialLogBizImpl implements PreferentialLogBiz {
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private ManaCustomerPreferentialLogDOMapper preferentialLogDOMapper;
    @Autowired
    private CouponBiz couponBiz;


    @Override
    public PagingResult<AddPreferentialLogBO> getPreferentialBOList(PreferentialVOPageParam preferentialVOPageParam) {
        if (preferentialVOPageParam == null) {
            return PagingResult.wrapErrorResult("", "缺少必要参数");
        }
        PreferentialPageParam preferentialPageParam = BdUtil.do2bo(preferentialVOPageParam, PreferentialPageParam.class);

        Integer pageIndex = preferentialPageParam.getPageIndex();
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        Integer pageSize = preferentialPageParam.getPageSize();
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;  //默认每页5️条数据
            preferentialPageParam.setPageSize(pageSize);
        }
        Integer offerSet = (pageIndex - 1) * pageSize;
        preferentialPageParam.setOfferSet(offerSet);
        List<ManaCustomerPreferentialLogDO> preferentialDOList = preferentialLogDOMapper.getPreferentialPagingList(preferentialPageParam);
        if (!CollectionUtils.isEmpty(preferentialDOList)) {
            List<ManaCouponTypeDO> manaCouponTypeDOList = couponBiz.getAllTypes();
            for (ManaCustomerPreferentialLogDO preferentialLogDO : preferentialDOList) {
                for (ManaCouponTypeDO manaCouponTypeDO : manaCouponTypeDOList) {
                    if (preferentialLogDO.getPreferentialType().equals(manaCouponTypeDO.getId())) {
                        String preferentialTypeName = manaCouponTypeDO.getTypeName();
                        preferentialLogDO.setPreferentialTypeName(preferentialTypeName);
                    }
                }
            }
            List<AddPreferentialLogBO> preferentialLogBOList = BdUtil.do2bo4List(preferentialDOList, AddPreferentialLogBO.class);

            Integer count = preferentialLogDOMapper.getPreferentialPagingCount(preferentialPageParam);

            return PagingResult.wrapSuccessfulResult(preferentialLogBOList, count);
        }

        return PagingResult.wrapErrorResult("", "无查询结果");
    }

    @Override
    public Result addPreferentialLog(AddPreferentialLogBO addPreferentialLogBO) {

        String checkStr = checkAddPreferential(addPreferentialLogBO);
        if (checkStr != null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        ManaCustomerPreferentialLogDO preferentialLogDO = BdUtil.do2bo(addPreferentialLogBO, ManaCustomerPreferentialLogDO.class);
        if (preferentialLogDO == null) {
            throw new BusinessException("内部发生异常");
        }
        preferentialLogDO.setGmtCreate(new Date());
        preferentialLogDO.setCreator(shiroBiz.getCurrentUserRealName());
        preferentialLogDOMapper.insertSelective(preferentialLogDO);

        return Result.wrapSuccessfulResult(1);
    }

    @Override
    public Result addPreferentialLogList(AddPreferentialVO addPreferentialVO) {
        List<Integer> vehicleIdList = addPreferentialVO.getVehicleIdList();
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        ManaCustomerPreferentialLogDO customerPreferentialLogDO = BdUtil.do2bo(addPreferentialVO, ManaCustomerPreferentialLogDO.class);
        if (customerPreferentialLogDO == null) {
            return ResultUtil.errorResult("", "类型转换错误");
        }
        customerPreferentialLogDO.setGmtCreate(new Date());
        customerPreferentialLogDO.setCreator(shiroBiz.getCurrentUserRealName());

        //TODO 改成批量插入 @周恒
        for (Integer vehicleId : vehicleIdList) {

            customerPreferentialLogDO.setCustomerVehicleId(vehicleId);
            preferentialLogDOMapper.insertSelective(customerPreferentialLogDO);
        }

        return Result.wrapSuccessfulResult(1);
    }


    private String checkAddPreferential(AddPreferentialLogBO addPreferentialLogBO) {
        if (addPreferentialLogBO == null || addPreferentialLogBO.getCustomerVehicleId() == null) {
            return "参数不能为空";
        }
        if (addPreferentialLogBO.getPreferentialType() == null) {
            return "请选择优惠券类型";
        }
        if (addPreferentialLogBO.getPreferentialNum() == null) {
            return "请输入优惠券数量";
        }
        if (addPreferentialLogBO.getPreferentialNum() < 1) {
            return "优惠券数量必须大于0";
        }
        if (addPreferentialLogBO.getSendDate() == null) {
            return "请选择优惠券发送日期";
        }
        return null;
    }

}
