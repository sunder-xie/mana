package com.tqmall.mana.server.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleShopBO;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailPackageModeBO;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailRewardModeBO;
import com.tqmall.mana.beans.param.settle.BalanceStatusModifyPO;
import com.tqmall.mana.beans.param.settle.ShopSettleDetailQueryPO;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopBaseBiz;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopCheckBiz;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopStatisticsBiz;
import com.tqmall.mana.client.beans.param.BalanceStatusModifyParam;
import com.tqmall.mana.client.beans.param.ShopSettleDetailPackageModeQueryParam;
import com.tqmall.mana.client.beans.param.ShopSettleDetailRewardModeQueryParam;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailPackageModeDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailRewardModeDTO;
import com.tqmall.mana.client.service.settle.RpcSettleShopService;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zxg on 17/1/17.
 * 19:45
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class RpcSettleShopServiceImpl implements RpcSettleShopService {

    @Autowired
    private SettleShopCheckBiz settleShopCheckBiz;
    @Autowired
    private SettleShopBaseBiz settleShopBiz;
    @Autowired
    private SettleShopStatisticsBiz settleShopStatisticsBiz;


    @Override
    public Result<SettleShopDTO> getSettleShopDetailByShopId(Integer shopId) {
//        SettleShopBO settleShopBO = settleShopBiz.getShopByShopId(shopId);
//        if(settleShopBO == null){
//            SettleShopDTO settleShopDTO = new SettleShopDTO();
//            settleShopDTO.setAgentId(shopId);
//            return Result.wrapSuccessfulResult(settleShopDTO);
//        }

        SettleShopBO settleShopBO = settleShopStatisticsBiz.getShopStatisticsData(shopId);
        return ResultUtil.successResult(settleShopBO, SettleShopDTO.class);
    }

    @Override
    public PagingResult<ShopSettleDetailRewardModeDTO> shopSettleDetailRewardModePage(ShopSettleDetailRewardModeQueryParam param){
        ShopSettleDetailQueryPO queryPO = BdUtil.do2bo(param, ShopSettleDetailQueryPO.class);
        PagingResult<ShopSettleDetailRewardModeBO> result = settleShopCheckBiz.shopSettleDetailRewardModePage(queryPO);
        List<ShopSettleDetailRewardModeDTO> dtoList = BdUtil.do2bo4List(result.getList(), ShopSettleDetailRewardModeDTO.class);
        return PagingResult.wrapSuccessfulResult(dtoList, result.getTotal());
    }

    @Override
    public PagingResult<ShopSettleDetailPackageModeDTO> shopSettleDetailPackageModePage(ShopSettleDetailPackageModeQueryParam param){
        ShopSettleDetailQueryPO queryPO = BdUtil.do2bo(param, ShopSettleDetailQueryPO.class);
        PagingResult<ShopSettleDetailPackageModeBO> result = settleShopCheckBiz.shopSettleDetailPackageModePage(queryPO);
        List<ShopSettleDetailPackageModeDTO> dtoList = BdUtil.do2bo4List(result.getList(), ShopSettleDetailPackageModeDTO.class);
        return PagingResult.wrapSuccessfulResult(dtoList, result.getTotal());
    }

    @Override
    public Result modifyBalanceStatus(BalanceStatusModifyParam param) {
        BalanceStatusModifyPO modifyPO = BdUtil.do2bo(param, BalanceStatusModifyPO.class);
        if(settleShopCheckBiz.modifyBalanceStatus(modifyPO)){
            return ResultUtil.successResult("", "对账状态修改成功");
        }
        return ResultUtil.errorResult("", "对账状态修改失败");
    }

}
