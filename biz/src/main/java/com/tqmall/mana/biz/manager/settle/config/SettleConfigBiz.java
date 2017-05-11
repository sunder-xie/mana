package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleConfigBasicBO;
import com.tqmall.mana.beans.BO.settle.SettleConfigItemBO;
import com.tqmall.mana.beans.VO.settle.SettleConfigBasicVO;
import com.tqmall.mana.beans.entity.settle.SettleConfigBasicDO;
import com.tqmall.mana.beans.entity.settle.SettleConfigItemDO;
import com.tqmall.mana.beans.param.settle.SettleConfigBOPageParam;
import com.tqmall.mana.beans.param.settle.SettleConfigVOPageParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by huangzhangting on 17/1/12.
 */
public interface SettleConfigBiz {

    /**
     * 新增组配置信息
     *
     * @param basicVOList
     * @return
     */
    Result addSettleConfigList(List<SettleConfigBasicVO> basicVOList);

    /**
     * 通过组配置id删除配置相关信息
     *
     * @param basicId
     * @return
     */
    Result deleteSettleConfig(Integer basicId);
    /**
     * 通过组配置id查询配置相关信息
     *
     * @param basicId
     * @return
     */
    Result getSettleConfig(Integer basicId);

    /**
     * 更新组配置信息
     *
     * @param basicVO
     * @return
     */
    Result updateSettleConfig(SettleConfigBasicVO basicVO);

    /**
     * 分页查询组配置信息
     *
     * @param voPageParam
     * @return
     */
    PagingResult<SettleConfigBasicBO> getBasicBOPagingList(SettleConfigVOPageParam voPageParam);

    /**
     * 通过查询条件分页查询组配置基础信息
     *
     * @param voPageParam
     * @return
     */
    PagingResult<SettleConfigBasicBO> getBasicBOList(SettleConfigVOPageParam voPageParam);

    /**
     * redis : 通过查询条件查询组配置基础信息
     *
     * @param boPageParam
     * @return
     */
    List<SettleConfigBasicBO> getBasicBORedisList(SettleConfigBOPageParam boPageParam);

    /**
     * redis : 通过组配置id查询组配置详细信息
     *
     * @param basicId
     * @return
     */
    List<SettleConfigItemBO> getItemBORedisList(Integer basicId);

    /**
     * 导出组配置数据到excel
     *
     * @param response
     * @param voPageParam
     */
    void exportSettleConfigList(HttpServletResponse response, SettleConfigVOPageParam voPageParam);

}
