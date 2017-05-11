package com.tqmall.mana.biz.manager.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.AddCommunicateRecordBO;
import com.tqmall.mana.beans.BO.customer.SearchCommunicateRecordBO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerAddressDO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerCommunicateRecordDO;
import com.tqmall.mana.beans.param.CommonPageParam;
import com.tqmall.mana.beans.param.CommonVOPageParam;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerAddressDOMapper;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerCommunicateRecordDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 17/1/3.
 */
@Slf4j
@Service
public class CommunicateRecordBizImpl implements CommunicateRecordBiz {
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private ManaCustomerAddressDOMapper addressDOMapper;
    @Autowired
    private ManaCustomerCommunicateRecordDOMapper communicateRecordDOMapper;



    /**
     * ========== 新增沟通记录 ==========
     */
    private String checkAddCommunicateRecord(AddCommunicateRecordBO communicateRecordBO) {
        if (communicateRecordBO == null || communicateRecordBO.getCustomerVehicleId() == null
                || communicateRecordBO.getCustomerId() == null) {

            log.info("checkAddCommunicateRecord failed, param is empty. param:{}",
                    JsonUtil.objectToStr(communicateRecordBO));
            return "参数不能为空";
        }
        if (communicateRecordBO.getCommunicateChannel() == null) {
            return "请选择沟通渠道";
        }
//        if(communicateRecordBO.getCommunicateDate()==null){
//            return "请选择沟通日期";
//        }

        return null;
    }

    @Transactional
    @Override
    public Result addCommunicateRecord(AddCommunicateRecordBO communicateRecordBO) {
        String checkStr = checkAddCommunicateRecord(communicateRecordBO);
        if (checkStr != null) {
            return ResultUtil.errorResult("", checkStr);
        }
        ManaCustomerCommunicateRecordDO recordDO = BdUtil.do2bo(communicateRecordBO, ManaCustomerCommunicateRecordDO.class);
        if (recordDO == null) {
            throw new BusinessException("系统发生内部错误");
        }

        Date now = new Date();
        String operator = shiroBiz.getCurrentUserRealName();
        recordDO.setGmtCreate(now);
        recordDO.setCreator(operator);

        communicateRecordDOMapper.insertSelective(recordDO);

        /** 新增或修改地址信息 */
        ManaCustomerAddressDO addressDO = BdUtil.do2bo(communicateRecordBO, ManaCustomerAddressDO.class);
        if (addressDO != null) {
            if (communicateRecordBO.getAddressId() != null) {
                /** 修改地址 */
                addressDO.setId(communicateRecordBO.getAddressId());
                addressDO.setCustomerId(null); //不操作客户id
                addressDO.setModifier(operator);
                addressDO.setGmtModified(now);
                addressDO.setDefaultProperty(); //允许清空地址
                addressDOMapper.updateByPrimaryKeySelective(addressDO);
            } else {
                /** 新增地址 */
                if(!addressDO.isEmpty()){
                    addressDO.setCreator(operator);
                    addressDO.setGmtCreate(now);
                    addressDOMapper.insertSelective(addressDO);
                }
            }
        }

        return ResultUtil.successResult(1);
    }


    @Override
    public PagingResult<SearchCommunicateRecordBO> searchCommunicationRecordPagingResult(CommonVOPageParam commonVOPageParam) {
        if (commonVOPageParam.getCustomerVehicleId() == null) {
            return PagingResult.wrapErrorResult("", "缺少必要参数");
        }

        CommonPageParam commonPageParam = BdUtil.do2bo(commonVOPageParam, CommonPageParam.class);

        Integer pageIndex = commonPageParam.getPageIndex();
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        Integer pageSize = commonPageParam.getPageSize();
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;  //默认每页5️条数据
            commonPageParam.setPageSize(pageSize);
        }
        Integer offerSet = (pageIndex - 1) * pageSize;
        commonPageParam.setOfferSet(offerSet);

        List<ManaCustomerCommunicateRecordDO> communicateRecordDOList = communicateRecordDOMapper.selectCommunicateRecordDOList(commonPageParam);
        if (!communicateRecordDOList.isEmpty()) {

            List<SearchCommunicateRecordBO> communicateRecordBOList = BdUtil.do2bo4List(communicateRecordDOList, SearchCommunicateRecordBO.class);

            Integer count = communicateRecordDOMapper.selectCommunicateRecordDOCount(commonPageParam);

            return PagingResult.wrapSuccessfulResult(communicateRecordBOList, count);
        }
        return PagingResult.wrapErrorResult("", "无查询结果");
    }

    @Override
    public Result<SearchCommunicateRecordBO> searchLatestCommunicationRecord(Integer customerVehicleId) {
        if (customerVehicleId == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        ManaCustomerCommunicateRecordDO communicateRecordDO = communicateRecordDOMapper.selectLatestCommunicateRecordDOById(customerVehicleId);
        if (communicateRecordDO != null) {
            SearchCommunicateRecordBO communicateRecordBO = BdUtil.do2bo(communicateRecordDO, SearchCommunicateRecordBO.class);
            return Result.wrapSuccessfulResult(communicateRecordBO);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);

    }


}
