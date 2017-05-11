package com.tqmall.mana.external.dubbo.uc;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.common.errorcode.support.SourceKey;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.annotation.cache.ManaCacheKey;
import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.ucenter.object.result.account.AccountDTO;
import com.tqmall.ucenter.object.result.shop.ShopDTO;
import com.tqmall.ucenter.object.result.shop.ShopInfoDTO;
import com.tqmall.ucenter.object.result.shoptag.ShopTagDTO;
import com.tqmall.ucenter.service.account.RpcAccountService;
import com.tqmall.ucenter.service.shop.RpcShopInfoService;
import com.tqmall.ucenter.service.shoptag.RpcShopTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/23.
 */
@Slf4j
@Service
public class ExtShopInfoService {
    @Autowired
    private RpcShopInfoService rpcShopInfoService;
    @Autowired
    private RpcAccountService rpcAccountService;
    @Autowired
    private RpcShopTagService rpcShopTagService;


    private void setDefaultAccountInfo(SimpleShopBO simpleShopBO, AccountDTO accountDTO) {
        simpleShopBO.setDefaultAccountId(accountDTO.getId());
        simpleShopBO.setDefaultAccountMobile(accountDTO.getMobile());
        simpleShopBO.setDefaultAccountRealName(accountDTO.getRealName());
    }

    public SimpleShopBO getSimpleShopInfo(Integer shopId) {
        log.info("get shop info params, appName:{}, shopId:{}", ConstantBean.APPLICATION_NAME, shopId);
        if (shopId == null || shopId < 1) {
            return null;
        }

        Result<ShopInfoDTO> result = rpcShopInfoService.getShopInfoByShopId(ConstantBean.APPLICATION_NAME, shopId);
        if (result.isSuccess()) {
            ShopInfoDTO shopInfoDTO = result.getData();
            ShopDTO shopDTO = shopInfoDTO.getShopDTO();
            if (shopDTO != null) {
                SimpleShopBO simpleShopBO = new SimpleShopBO();
                simpleShopBO.setId(shopDTO.getId());
                simpleShopBO.setCompanyName(shopDTO.getCompanyName());

                List<AccountDTO> accountDTOList = shopInfoDTO.getAccountDTOs();
                if (!CollectionUtils.isEmpty(accountDTOList)) {
                    for (AccountDTO accountDTO : accountDTOList) {
                        if (accountDTO.getAccountStatus() == 0) {
                            setDefaultAccountInfo(simpleShopBO, accountDTO);
                            break;
                        }
                    }
                    if (simpleShopBO.getDefaultAccountId() == null) {
                        setDefaultAccountInfo(simpleShopBO, accountDTOList.get(0));
                    }
                }

                return simpleShopBO;
            }
        }

        log.info("get shop info, result:{}", JsonUtil.objectToStr(result));
        return null;
    }


    public List<AccountDTO> getAccountListByShopId(Integer shopId) {
        log.info("get account list params, appName:{}, shopId:{}", ConstantBean.APPLICATION_NAME, shopId);
        if (shopId == null || shopId < 1) {
            return new ArrayList<>();
        }
        Result<List<AccountDTO>> result = rpcAccountService.getAccountListByShopId(ConstantBean.APPLICATION_NAME, shopId);
        if (result.isSuccess() && !CollectionUtils.isEmpty(result.getData())) {
            return result.getData();
        }
        log.info("get account list, result:{}", JsonUtil.objectToStr(result));
        return new ArrayList<>();
    }

    @ManaCache(cacheName = RedisKeyBean.FOREVER_ACCOUNT_INFO_KEY)
    public AccountDTO getAccountByShopId(@ManaCacheKey Integer shopId) {
        List<AccountDTO> list = getAccountListByShopId(shopId);
        if (list.isEmpty()) {
            return null;
        }

        for (AccountDTO accountDTO : list) {
            Integer accountStatus = accountDTO.getAccountStatus();
            if (accountStatus != null && accountStatus == 0) {
                return accountDTO;
            }
        }
        return null;
    }

    public List<ShopTagDTO> getTagsByShopId(Integer shopId) {
        log.info("getTagsByShopId, appName:{}, shopId:{}", ConstantBean.APPLICATION_NAME, shopId);

        Result<List<ShopTagDTO>> result = rpcShopTagService.getTagsByShopId(ConstantBean.APPLICATION_NAME, shopId);
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
            return result.getData();
        }

        log.info("getTagsByShopId failed, result:{}", JsonUtil.objectToStr(result));
        return new ArrayList<>();
    }

    public AccountDTO getAccountByMobile(String agentMobile) {
        if (StringUtils.isEmpty(agentMobile)) {
            log.info("根据手机号调用uc接口获取门店信息参数错误");
            throw new BusinessCheckException("000", "参数异常");
        }

        //根据手机号查询agentId
        log.info("根据手机号调用uc接口获取门店id,mobile={}", agentMobile);
        Result<AccountDTO> accountDTOResult = rpcAccountService.getAccountByMobile(SourceKey.CRM.getKey(), agentMobile);
        if (!accountDTOResult.isSuccess() && accountDTOResult.getData() == null) {
            log.error("调用uc获取门店id失败,mobile={}", agentMobile);
            throw new BusinessCheckException("", "内部数据查询异常");
        }
        log.info("根据手机号调用uc接口获取门店信息,account", accountDTOResult.getData());
        return accountDTOResult.getData();
    }


}
