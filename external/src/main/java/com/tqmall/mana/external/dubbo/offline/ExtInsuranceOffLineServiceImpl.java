package com.tqmall.mana.external.dubbo.offline;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.offline.InsuranceOffLineTempInfoParam;
import com.tqmall.insurance.domain.param.insurance.offline.SearchTempInfoListParam;
import com.tqmall.insurance.domain.result.offline.InsuranceOfflineTempInfoDTO;
import com.tqmall.insurance.service.insurance.offline.RpcInsuranceOffLineService;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhouheng on 17/3/24.
 */
@Service
@Slf4j
public class ExtInsuranceOffLineServiceImpl implements ExtInsuranceOffLineService {

    @Autowired
    private RpcInsuranceOffLineService rpcInsuranceOffLineService;


    @Override
    public Map<Integer, String> getAuditStatusMap() {
        log.info("[dubbo-->insurance]获取审核状态字典开始");
        Result<Map<Integer, String>> result = rpcInsuranceOffLineService.getAuditStatusMap();
        if (!result.isSuccess() && result.getData() == null) {
            log.error("[dubbo-->insurance]获取审核状态字典失败", result.getMessage());
            throw new BusinessException(result.getMessage());
        }
        log.info("[dubbo-->insurance]获取审核状态字典成功", JsonUtil.objectToStr(result));
        return result.getData();
    }

    @Override
    public PagingResult<InsuranceOfflineTempInfoDTO> getTempInfoPageList(SearchTempInfoListParam searchTempInfoListParam) {
        log.info("[dubbo-->insurance]获取线下录单信息列表开始参数={}",searchTempInfoListParam);
        PagingResult<InsuranceOfflineTempInfoDTO> pagingResult = rpcInsuranceOffLineService.getTempInfoPageList(searchTempInfoListParam);
        if (!pagingResult.isSuccess()) {
            log.error("[dubbo-->insurance]获取线下录单信息列表失败", pagingResult.getMessage());
            throw new BusinessException(pagingResult.getMessage());
        }
        log.info("[dubbo-->insurance]获取线下录单信息列表成功,结果:{}", JsonUtil.objectToStr(pagingResult));
        return pagingResult;
    }

    @Override
    public String modifyEnteringFormAuditStatus(InsuranceOffLineTempInfoParam tempInfoParam) {
        log.info("[dubbo-->insurance]更新线下录单信息列表开始");
        Result<String> result = rpcInsuranceOffLineService.modifyEnteringFormAuditStatus(tempInfoParam);
        if (!result.isSuccess()) {
            log.error("[dubbo-->insurance]获取线下录单信息列表失败", result.getMessage());
            throw new BusinessException(result.getMessage());
        }
        log.info("[dubbo-->insurance]更新线下录单信息列表成功");
        return result.getData();
    }
}
