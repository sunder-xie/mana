package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleConfigBasicDO;
import com.tqmall.mana.beans.param.settle.SettleConfigBOPageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SettleConfigBasicDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleConfigBasicDO record);

    int insertSelective(SettleConfigBasicDO record);

    SettleConfigBasicDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleConfigBasicDO record);

    int updateByPrimaryKey(SettleConfigBasicDO record);

    /**
     * 分页查询组配置信息
     *
     * @param boPageParam
     * @return
     */
    List<SettleConfigBasicDO> selectBasicDOListByParam(SettleConfigBOPageParam boPageParam);

    /**
     * 分页查询组配置信息
     *
     * @param boPageParam
     * @return
     */
    Integer selectBasicDOListCountByParam(SettleConfigBOPageParam boPageParam);


    /**
     * 通过保险公司类型和组配置名称查询组配置名称是否已存在
     *
     * @param groupName
     * @return
     */
    List<SettleConfigBasicDO> selectDOListByGroupName(@Param("groupName") String groupName);


    /**
     * 根据id集合查询被删除的组
     *
     * @param idSet
     * @return
     */
    List<String> getDeletedGroupByIds(@Param("idSet") Set<Integer> idSet);


    List<SettleConfigBasicDO> getByIds(@Param("idSet") Set<Integer> idSet);

}