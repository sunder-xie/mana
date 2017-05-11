package com.tqmall.mana.server.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerBizPagingResult;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerCheckerDetailVO;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerBizParam;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerForBrilliantBizParam;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleCarOwnerCheckerBiz;
import com.tqmall.mana.client.beans.param.SettleCarOwnerCheckerForBrilliantParam;
import com.tqmall.mana.client.beans.settle.SettleCarOwnerCheckerDetailDTO;
import com.tqmall.mana.client.beans.param.SettleCarOwnerCheckerParam;
import com.tqmall.mana.client.beans.settle.SettleCarOwnerInsuranceTypeDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleCarOwnerPagingResult;
import com.tqmall.mana.client.service.settle.RpcSettleCarOwnerCheckerService;
import com.tqmall.mana.component.util.BdUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengjinju on 17/1/20.
 */
public class RpcSettleCarOwnerCheckerServiceImpl implements RpcSettleCarOwnerCheckerService {

    @Autowired
    private SettleCarOwnerCheckerBiz settleCarOwnerCheckerBiz;

    @Override
    public Result<SettleCarOwnerPagingResult> carOwnerCheckerForERP(SettleCarOwnerCheckerParam settleCarOwnerCheckerParam) {
        SettleCarOwnerCheckerBizParam bizParam = BdUtil.do2bo(settleCarOwnerCheckerParam, SettleCarOwnerCheckerBizParam.class);
        SettleCarOwnerBizPagingResult bizPagingResult = settleCarOwnerCheckerBiz.carOwnerCheckerForERP(bizParam);

        List<SettleCarOwnerCheckerDetailDTO> carOwnerDTOList = new ArrayList<>();
        for (SettleCarOwnerCheckerDetailVO settleCarOwnerCheckerDetailVO : bizPagingResult.getPagingResult().getList()) {
            SettleCarOwnerCheckerDetailDTO settleCarOwnerCheckerDetailDTO = BdUtil.do2bo(settleCarOwnerCheckerDetailVO, SettleCarOwnerCheckerDetailDTO.class);
            List<SettleCarOwnerInsuranceTypeDetailDTO> insuranceTypeDetailDTOList = BdUtil.do2bo4List(settleCarOwnerCheckerDetailVO.getInsuranceTypeDetailVOList(), SettleCarOwnerInsuranceTypeDetailDTO.class);
            settleCarOwnerCheckerDetailDTO.setInsuranceTypeDetailDTOList(insuranceTypeDetailDTOList);
            carOwnerDTOList.add(settleCarOwnerCheckerDetailDTO);
        }
        //组装返回对象的结果
        SettleCarOwnerPagingResult pagingResult = new SettleCarOwnerPagingResult();
        pagingResult.setPagingResult(PagingResult.wrapSuccessfulResult(carOwnerDTOList, bizPagingResult.getPagingResult().getTotal()));
        pagingResult.setTotalPayAbleInsuredFee(bizPagingResult.getTotalPayAbleInsuredFee());
        pagingResult.setFirstPayTotalAmount(bizPagingResult.getFirstPayTotalAmount());
        pagingResult.setSecondPayTotalAmount(bizPagingResult.getSecondPayTotalAmount());
        pagingResult.setPayableTotalAmount(bizPagingResult.getPayableTotalAmount());
        return Result.wrapSuccessfulResult(pagingResult);
    }

    @Override
    public Result<Boolean> updateConfirmMoneyPaidStatus(List<Integer> idList, String operator) {
        return settleCarOwnerCheckerBiz.updateConfirmMoneyPaidStatus(idList, operator);
    }

    @Override
    public Result<Boolean> reviewPayStatus(Integer id, String operator) {
        return settleCarOwnerCheckerBiz.reviewPayStatus(id, operator);
    }

    @Override
    public Result<List<SettleCarOwnerCheckerDetailDTO>> exportSettleCarOwnerChekerList(SettleCarOwnerCheckerParam param) {
        SettleCarOwnerCheckerBizParam bizParam = BdUtil.do2bo(param, SettleCarOwnerCheckerBizParam.class);
        List<SettleCarOwnerCheckerDetailVO> voList = settleCarOwnerCheckerBiz.exportSettleCarOwnerChekerList(bizParam);
        List<SettleCarOwnerCheckerDetailDTO> DTOList = BdUtil.do2bo4List(voList, SettleCarOwnerCheckerDetailDTO.class);
        return Result.wrapSuccessfulResult(DTOList);
    }

    @Override
    public Result<Boolean> updateByInsuranceOrderSn(SettleCarOwnerCheckerForBrilliantParam param) {
        SettleCarOwnerCheckerForBrilliantBizParam bizParam = BdUtil.do2bo(param, SettleCarOwnerCheckerForBrilliantBizParam.class);
        return Result.wrapSuccessfulResult(settleCarOwnerCheckerBiz.updateByInsuranceOrderSn(bizParam));
    }
}
