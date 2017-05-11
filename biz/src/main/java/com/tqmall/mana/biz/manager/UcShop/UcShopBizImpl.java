package com.tqmall.mana.biz.manager.UcShop;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.UcShopBO;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.param.ShopQueryPO;
import com.tqmall.mana.beans.param.UcShopRequestParam;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceShopConfigService;
import com.tqmall.mana.external.dubbo.search.ExtUcShopService;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.ucenter.param.InsuranceShopParam;
import com.tqmall.search.dubbo.client.ucenter.param.UcShopParam;
import com.tqmall.search.dubbo.client.ucenter.result.InsuranceShopResult;
import com.tqmall.search.dubbo.client.ucenter.result.UcShopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhouheng on 16/12/22.
 */
@Service
public class UcShopBizImpl implements UcShopBiz {

    @Autowired
    private ExtUcShopService extUcShopService;
    @Autowired
    private ExtInsuranceShopConfigService extInsuranceShopConfigService;


    //中国地区经纬度大体范围 73°E~135°E ， 4°N~53°N
    private final BigDecimal minChinaLongitude = new BigDecimal(73);
    private final BigDecimal maxChinaLongitude = new BigDecimal(135);

    private final BigDecimal minChinaLatitude = new BigDecimal(4);
    private final BigDecimal maxChinaLatitude = new BigDecimal(53);

    private boolean inChinaLongitude(BigDecimal longitude) {
        if (longitude == null) {
            return false;
        }
        if (longitude.compareTo(minChinaLongitude) < 0) {
            return false;
        }
        return longitude.compareTo(maxChinaLongitude) < 0;
    }

    private boolean inChinaLatitude(BigDecimal latitude) {
        if (latitude == null) {
            return false;
        }
        if (latitude.compareTo(minChinaLatitude) < 0) {
            return false;
        }
        return latitude.compareTo(maxChinaLatitude) < 0;
    }

    /* 检查经纬度，用于前端提示 */
    private void checkLongitudeAndLatitude(UcShopBO shopBO) {
        if (shopBO == null) {
            return;
        }
        //经度
        if (!inChinaLongitude(shopBO.getLongitude())) {
            shopBO.setWrongPosition(1);
            return;
        }
        //纬度
        if (!inChinaLatitude(shopBO.getLatitude())) {
            shopBO.setWrongPosition(1);
        }
    }

    @Override
    public PagingResult<UcShopBO> getShopListInfo(UcShopRequestParam requestParam) {
        if (requestParam == null) {
            return PagingResult.wrapErrorResult("", "缺少必要参数");
        }
        Integer pageNumber = requestParam.getPageNumber();
        Integer pageSize = requestParam.getPageSize();
        //搜索那边是从0开始，我们这边统一从1开始
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 0;
        }else{
            pageNumber = pageNumber - 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }

        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);
        UcShopParam ucShopParam = BdUtil.do2bo(requestParam, UcShopParam.class);
        ucShopParam.setShopStatus(0);//表示正常门店
        //门店信息分页
        Page<UcShopDTO> ucShopDTOPage = extUcShopService.getUcShops(ucShopParam, pageableRequest);
        if (ucShopDTOPage != null) {
            List<UcShopDTO> ucShopDTOList = ucShopDTOPage.getContent();
            if (!CollectionUtils.isEmpty(ucShopDTOList)) {
                Set<Integer> insuranceShopIds = extInsuranceShopConfigService.getAllShopIds();
                List<UcShopBO> ucShopBOList = new ArrayList<>();
                if (insuranceShopIds.isEmpty()) {
                    for (UcShopDTO shopDTO : ucShopDTOList) {
                        UcShopBO ucShopBO = convertShop(shopDTO);
                        checkLongitudeAndLatitude(ucShopBO);
                        ucShopBOList.add(ucShopBO);
                    }
                } else {
                    for (UcShopDTO shopDTO : ucShopDTOList) {
                        UcShopBO ucShopBO = convertShop(shopDTO);
                        checkLongitudeAndLatitude(ucShopBO);
                        if (insuranceShopIds.contains(ucShopBO.getId())) {
                            ucShopBO.setHasInsuranceRight(1);
                        }
                        ucShopBOList.add(ucShopBO);
                    }
                    //排序
                    Collections.sort(ucShopBOList, new Comparator<UcShopBO>() {
                        @Override
                        public int compare(UcShopBO o1, UcShopBO o2) {
                            int flag = o2.getHasInsuranceRight().compareTo(o1.getHasInsuranceRight());
                            if (flag == 0) {
                                return o1.getCompanyName().compareTo(o2.getCompanyName());
                            }
                            return flag;
                        }
                    });
                }

                return PagingResult.wrapSuccessfulResult(ucShopBOList, (int) ucShopDTOPage.getTotalElements());
            }
        }

        return PagingResult.wrapErrorResult("", "未查到数据");
    }

    /* 转换门店数据 */
    private UcShopBO convertShop(UcShopDTO shopDTO){
        UcShopBO ucShopBO = setShopAddressInfo(shopDTO.getUcAddressDTOList());
        if(ucShopBO==null){
            ucShopBO = new UcShopBO();
        }
        ucShopBO.setId(shopDTO.getId());
        ucShopBO.setCompanyName(shopDTO.getCompanyName());
        return ucShopBO;
    }
    /* 设置门店地址 */
    private UcShopBO setShopAddressInfo(List<UcShopDTO.UcAddressDTO> addressDTOList){
        if(CollectionUtils.isEmpty(addressDTOList)){
            return null;
        }
        if(addressDTOList.size() > 1){
            Collections.sort(addressDTOList, new Comparator<UcShopDTO.UcAddressDTO>() {
                @Override
                public int compare(UcShopDTO.UcAddressDTO o1, UcShopDTO.UcAddressDTO o2) {
                    if(o2.getIsDefault()==null){
                        o2.setIsDefault(0);
                    }
                    if(o1.getIsDefault()==null){
                        o1.setIsDefault(0);
                    }
                    if(o2.getAddrType()==null){
                        o2.setAddrType(2);
                    }
                    if(o1.getAddrType()==null){
                        o1.setAddrType(2);
                    }
                    int flag = o2.getIsDefault().compareTo(o1.getIsDefault());
                    if(flag==0){
                        return o1.getAddrType().compareTo(o2.getAddrType());
                    }
                    return flag;
                }
            });
        }
        return BdUtil.do2bo(addressDTOList.get(0), UcShopBO.class);
    }

    @Override
    public PagingResult<SimpleShopBO> getSimpleShopList(ShopQueryPO shopQueryPO) {
        InsuranceShopParam shopParam = new InsuranceShopParam();
        if(shopQueryPO!=null){
            String queryStr = shopQueryPO.getQueryStr();
            if(queryStr!=null){
                queryStr = queryStr.trim();
                if(ManaUtil.isMobile(queryStr)){
                    shopParam.setMobile(queryStr);
                }else{
                    shopParam.setCompanyName(queryStr);
                }
            }
            shopParam.setCooperationMode(shopQueryPO.getCooperationMode());
        }

        PageableRequest pageableRequest = new PageableRequest(0, 20);
        Page<InsuranceShopResult> shopResultPage = extUcShopService.getInsuranceShops(shopParam, pageableRequest);
        if(shopResultPage!=null){
            List<InsuranceShopResult> shopList = shopResultPage.getContent();
            if(!CollectionUtils.isEmpty(shopList)){
                List<SimpleShopBO> simpleShopBOList = new ArrayList<>();
                for(InsuranceShopResult shop : shopList){
                    SimpleShopBO simpleShopBO = new SimpleShopBO();
                    simpleShopBO.setId(shop.getId());
                    simpleShopBO.setCompanyName(shop.getCompanyName());

                    setDefaultAccountInfo(simpleShopBO, shop);

                    simpleShopBOList.add(simpleShopBO);
                }
                return PagingResult.wrapSuccessfulResult(simpleShopBOList, (int)shopResultPage.getTotalElements());
            }
        }

        return PagingResult.wrapErrorResult("", "未查到数据");
    }
    private void setDefaultAccountInfo(SimpleShopBO simpleShopBO, InsuranceShopResult shop){
        List<InsuranceShopResult.UcAccountResult> ucAccountResults = shop.getUcAccountResults();
        if(CollectionUtils.isEmpty(ucAccountResults)){
            return;
        }
        InsuranceShopResult.UcAccountResult accountResult = ucAccountResults.get(0);
        simpleShopBO.setDefaultAccountId(accountResult.getId());
        simpleShopBO.setDefaultAccountMobile(accountResult.getMobile());
        simpleShopBO.setDefaultAccountRealName(accountResult.getRealName());
    }


}
