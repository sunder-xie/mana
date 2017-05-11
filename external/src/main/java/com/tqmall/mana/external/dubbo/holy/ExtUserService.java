package com.tqmall.mana.external.dubbo.holy;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.holy.provider.entity.crm.user.UserInfoDTO;
import com.tqmall.holy.provider.entity.crm.user.UserInfoPageDTO;
import com.tqmall.holy.provider.param.crm.UserInfoParam;
import com.tqmall.holy.provider.service.crm.RpcUserInfoService;
import com.tqmall.mana.beans.BO.sys.SearchUserBO;
import com.tqmall.mana.beans.BO.sys.UserBO;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/26.
 */
@Slf4j
@Service
public class ExtUserService {
    @Autowired
    private RpcUserInfoService rpcUserInfoService;


    private UserInfoParam getReqParam(SearchUserBO searchUserBO){
        UserInfoParam param = new UserInfoParam();
        if(searchUserBO==null){
            return param;
        }
        if(!StringUtils.isEmpty(searchUserBO.getStaffNo())){
            param.setStaffNo(searchUserBO.getStaffNo());
        }
        if(!StringUtils.isEmpty(searchUserBO.getMobile())){
            param.setMobile(searchUserBO.getMobile());
        }
        if(!StringUtils.isEmpty(searchUserBO.getName())){
            param.setUserName(searchUserBO.getName());
        }

        Integer pageIndex = searchUserBO.getPageIndex();
        Integer pageSize = searchUserBO.getPageSize();
        if(pageIndex==null || pageIndex<1){
            pageIndex = 1;
        }
        if(pageSize==null || pageSize<1){
            pageSize = 20;
        }
        param.setPage(pageIndex);
        param.setPageSize(pageSize);

        return param;
    }

    public PagingResult<UserBO> getUserList(SearchUserBO searchUserBO){
        UserInfoParam param = getReqParam(searchUserBO);

        Result<UserInfoPageDTO> result = rpcUserInfoService.getUserInfo(param);
        if(result.isSuccess()){
            UserInfoPageDTO pageDTO = result.getData();
            if(pageDTO != null){
                List<UserInfoDTO> userInfoDTOList = pageDTO.getUserInfoDTOList();
                if(!CollectionUtils.isEmpty(userInfoDTOList)){
                    List<UserBO> userBOList = new ArrayList<>();
                    for(UserInfoDTO userInfoDTO : userInfoDTOList){
                        UserBO userBO = new UserBO();
                        userBO.setUserName(userInfoDTO.getStaffNo());
                        userBO.setRealName(userInfoDTO.getName());
                        userBO.setMobile(userInfoDTO.getMobile());

                        userBOList.add(userBO);
                    }

                    return PagingResult.wrapSuccessfulResult(userBOList, pageDTO.getNum().intValue());
                }
            }
        }

        log.info("getUserInfo from holy failed, param:{}, result:{}",
                JsonUtil.objectToStr(param), JsonUtil.objectToStr(result));

        return ResultUtil.errorPageResult(DataError.NO_DATA_ERROR);
    }
}
