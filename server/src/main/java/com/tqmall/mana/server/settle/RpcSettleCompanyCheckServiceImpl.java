package com.tqmall.mana.server.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.VO.settle.SettleCompanyBizPagingResult;
import com.tqmall.mana.beans.VO.settle.SettleCompanyCheckDetailVO;
import com.tqmall.mana.beans.param.settle.SettleCompanyCheckDetailBizParam;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleCompanyCheckDetailBiz;
import com.tqmall.mana.client.beans.param.SettleCompanyCheckDetailParam;
import com.tqmall.mana.client.beans.settle.SettleCompanyCheckDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleCompanyCheckPagingResult;
import com.tqmall.mana.client.service.settle.RpcSettleCompanyCheckService;
import com.tqmall.mana.component.util.BdUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zengjinju on 17/1/20.
 */
public class RpcSettleCompanyCheckServiceImpl implements RpcSettleCompanyCheckService {

    @Autowired
    private SettleCompanyCheckDetailBiz settleCompanyCheckDetailBiz;

    @Override
    public Result<SettleCompanyCheckPagingResult> insuranceCompanyCheckForERP(SettleCompanyCheckDetailParam param) {
        SettleCompanyCheckDetailBizParam bizParam = BdUtil.do2bo(param, SettleCompanyCheckDetailBizParam.class);
        SettleCompanyBizPagingResult bizPagingResult = settleCompanyCheckDetailBiz.insuranceCompanyCheckForERP(bizParam);
        PagingResult<SettleCompanyCheckDetailVO> pagingResult = bizPagingResult.getPagingResult();
        List<SettleCompanyCheckDetailDTO> checkDetailDTOList = BdUtil.do2bo4List(pagingResult.getList(), SettleCompanyCheckDetailDTO.class);
        //封装返回对象
        SettleCompanyCheckPagingResult settlePagingResult = new SettleCompanyCheckPagingResult();
        settlePagingResult.setPagingResult(PagingResult.wrapSuccessfulResult(checkDetailDTOList, pagingResult.getTotal()));
        settlePagingResult.setTotalInsuredFee(bizPagingResult.getTotalInsuredFee());
        settlePagingResult.setTotalInsuredRoyaltyFee(bizPagingResult.getTotalInsuredRoyaltyFee());
        return Result.wrapSuccessfulResult(settlePagingResult);
    }

    @Override
    public Result<Boolean> updateConfirmMoneyStatusByIds(List<Integer> idList, String operator) {
        return Result.wrapSuccessfulResult(settleCompanyCheckDetailBiz.updateConfirmMoneyStatusByIds(idList, operator));
    }

    @Override
    public Result<List<SettleCompanyCheckDetailDTO>> exportCompanyCheckDetailList(SettleCompanyCheckDetailParam param) {
        SettleCompanyCheckDetailBizParam bizParam = BdUtil.do2bo(param, SettleCompanyCheckDetailBizParam.class);
        List<SettleCompanyCheckDetailVO> VOList = settleCompanyCheckDetailBiz.exportCompanyCheckDetailList(bizParam);
        List<SettleCompanyCheckDetailDTO> DTOList = BdUtil.do2bo4List(VOList, SettleCompanyCheckDetailDTO.class);
        return Result.wrapSuccessfulResult(DTOList);
    }
}
